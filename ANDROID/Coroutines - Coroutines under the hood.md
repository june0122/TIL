# Coroutines - Coroutines under the hood


## 코루틴은 JVM에서 내부적으로 어떻게 동작하는 것일까?

아래의 내용은 [KotlinConf 2017 - Deep Dive into Coroutines on JVM by Roman Elizarov](https://www.youtube.com/watch?v=YrrUCSi72E8&t=110s) 영상의 내용들이다.

#### There is no magic

코루틴은 디컴파일되면 일반 코드일 뿐이다. **Continuation Passing Style<small>(CPS, 연속 전달 방식)</small>** 이라는 형태의 코드로 전환한다.

> ### A toy problem

```kotlin
fun postItem(item: Item) {
    val token = requestToken()
    val post = createPost(token, item)
    processPost(post)
}
```
서버에서 토큰을 가져와서 게시물을 포스트한 다음, 포스트 완료처리를 하는 세 가지 연산을 코루틴으로 만들면 JVM 혹은 바이트코드에서 내부적으로 어떤 형태로 동작하는 것일까?

이것이 Continuation Passing Style로 내부적으로 컴파일할 때 아래와 같이 바뀐다.

> ### Continuation Passing Style
 
```kotlin
fun postItem(item: Item) {
    requestToken { token ->
        val post = createPost(token, item)
        processPost(post)
    }
}
```

Continuation Passing Style은 결과를 호출자에게 직접 반환하는 대신 **Callback**같은 것 <sup>continuation</sup>으로 결과를 전달하는 것을 의미한다.

> ### Kotlin suspending functions

```kotlin
suspend fun createPost(token: Token, item: Item): Post { … }
```

`createPost(…)`라는 suspend 함수를 하나를 만들었을 때, 코루틴에서는 일시 중단이 되었다가 재개가 되는데 어떻게 이것이 가능한 것인지 알아보자.

> ### Kotlin suspending functions

```java
// suspend fun createPost(token: Token, item: Item): Post { … }
     ↓
// Java/JVM 
Object createPost(Token token, Item item, Continuation<Post> cont) { … }
```

내부적으로는 JVM에 들어갈 때 바이트코드로 컴파일되면서 같은 `createPost(…)`인데 **Continuation**이 생성되어 Continuation Passing Style로 변환된다.

호출했던 함수의 끝에 매개변수가 하나 추가되서 **Continuation**이라는 객체를 넘겨주는 것으로 변환되는 것이다.

> ### Labels

```kotlin
    suspen fun postItem(item: Item) {
    // LABEL 0
↛       val token = requestToken()
    // LABEL 1
↛       val post = createPost(token, item)
    // LABEL 2
        processPost(post)
    }
```

먼저 Labael이라는 작업을 하게 되는데 코루틴에서 순차적으로 작성했던 코드들이 `suspend` 함수가 되면 컴파일할 때 Label이 찍히게 된다. 

이 함수가 재개되어야 하는데, 재개될 때 필요한 **Suspention Point**<small>(중단 지점과 재개 지점)</small>가 요구된다. 그래서 이 지점들을 Label로 찍어놓는 것이다. 이런 작업을 코틀린 컴파일러가 내부적으로 하게 된다.

대략적으로 아래와 같은 형태가 되는데, 작성했던 함수가 내부적으론 `switch-case`문처럼 바뀌어 case문이 3개가 생성되고 세 번을 실행하는 것을 알 수 있다. 함수를 실행할 때 0번이든, 1번이든, 2번이든 함수를 재개할 수 있는 지점이 생긴 것이다. 그리고 이 함수를 호출한 지점은 중단점이 될 수도 있는 것이다.

```kotlin
suspend fun postItem(item: Item) {
    switch (label) {
        case 0:
            val token = requestToken()
        case 1:
            val post = createPost(token, item)
        case 2:
            processPost(post)
    }
}
```

Label들이 다 완성되고 나면 Continuation Passing Style로 변환을 하게 된다.

```kotlin
fun postItem(item: Item, cont: Continuation) {
    val sm = object : CoroutineImpl { … }
    switch (sm.label) {
        case 0:
            val token = requestToken(sm)
        case 1:
            val post = createPost(token, item, sm)
        case 2:
            processPost(post)
    }
}
```

**Continuation**이라는 객체가 있고, 매 번 함수를 호출할 때마다 **continuation**을 넘겨준다. **continuation**은 Callback 인터페이스 같은 것으로, 재개를 해주는 인터페이스를 가진 객체인 것이다.

위의 코드에서 `sm`이라고 하는 것은 **state machine**을 의미하는데, 각각의 함수가 호출될 때 상태<small>(지금까지 했던 연산의 결과)</small>를 같이 넘겨줘야 한다. 이 **state machine**의 정체는 결국 **Continuation**이고, **Continuation**이 어떠한 정보값을 가진 형태로 Passing이 되면서 코루틴이 내부적으로 동작하게 되는 것이다.

```kotlin
fun postItem(item: Item, cont: Continuation) {
    
    val sm = cont as? ThisSM ?: object : ThisSM {
        fun resume(…) {
            postItem(null, this)
        }
    }

    switch (sm.label) {
        case 0:
            sm.item = item
            sm.label = 1
            requestToken(sm)
        case 1:
            createPost(token, item, sm)
        …
    }
}
```

각각의 suspend function이 Continuation<small>(위 코드에선 `sm`)</small>을 마지막 매개변수로 가져가게 된다.

- 만약 `requestToken(sm)`이 완료되었다면 `sm`<small>(continuation)</small>에다가 `resume()`을 호출하게 된다.
- 다시 `createPost(token, item, sm)`가 호출되고 이것이 완료되었을 때도 `sm`<small>(continuation)</small>에다가 `resume()`을 호출하는 형태가 반복되는 것이다.

그렇다면 `resume()`은 정체가 무엇일까? 위의 코드에서 `resume()`은 결국 자기 자신을 불러주는 것이다. <small>(`postItem(…)` 내부에서 `postItem(…)`을 다시 호출하고 있음)</small>

- 예시로, suspend function인 `requestToken(sm)`의 연산이 끝났을 때 `resume()`을 통해 다시 `postItem(…)`이 호출되는데, 그때 Label 값을 하나 올려서 다른 케이스가 호출되도록 하는 것이다. 이렇게 되면 내부적으로는 마치 suspend function이 호출되고 다음 번 케이스, 그리고 또다시 다음 번 케이스로 넘어가는 형태가 되는 것이다.

## Decomplie된 코드 살펴보기

```kotlin
fun main(): Unit {
    GlobalScope.launch {
        val userData = fetchUserData()
        val userCache = cacheUserData(userData)
        updateTextView(userCache)
    }
}

suspend fun fetchUserData() = "user_name"

suspend fun cacheUserData(user: String) = user

fun updateTextView(user: String) = user
``` 

위의 코드를 코틀린의 바이트코드로 만든 다음, Decompile하여 Java 코드로 만들어보자.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/120593066-e3fc9400-c479-11eb-9f3d-3ad7738018d3.png'>
</p>

> Decomplie된 코드

```kotlin
public final class Example_nomagic_01Kt {
   public static final void main() {
      BuildersKt.launch$default((CoroutineScope)GlobalScope.INSTANCE, (CoroutineContext)null, (CoroutineStart)null, (Function2)(new Function2((Continuation)null) {
         int label;

         @Nullable
         public final Object invokeSuspend(@NotNull Object $result) {
            Object var10000;
            label17: {
               Object var4 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               switch(this.label) {
               case 0:
                  ResultKt.throwOnFailure($result);
                  this.label = 1;
                  var10000 = Example_nomagic_01Kt.fetchUserData(this);
                  if (var10000 == var4) {
                     return var4;
                  }
                  break;
               case 1:
                  ResultKt.throwOnFailure($result);
                  var10000 = $result;
                  break;
               case 2:
                  ResultKt.throwOnFailure($result);
                  var10000 = $result;
                  break label17;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               String userData = (String)var10000;
               this.label = 2;
               var10000 = Example_nomagic_01Kt.cacheUserData(userData, this);
               if (var10000 == var4) {
                  return var4;
               }
            }

            String userCache = (String)var10000;
            Example_nomagic_01Kt.updateTextView(userCache);
            return Unit.INSTANCE;
         }

         @NotNull
         public final Continuation create(@Nullable Object value, @NotNull Continuation completion) {
            Intrinsics.checkNotNullParameter(completion, "completion");
            Function2 var3 = new <anonymous constructor>(completion);
            return var3;
         }

         public final Object invoke(Object var1, Object var2) {
            return ((<undefinedtype>)this.create(var1, (Continuation)var2)).invokeSuspend(Unit.INSTANCE);
         }
      }), 3, (Object)null);
   }

   // $FF: synthetic method
   public static void main(String[] var0) {
      main();
   }

   @Nullable
   public static final Object fetchUserData(@NotNull Continuation $completion) {
      return "user_name";
   }

   @Nullable
   public static final Object cacheUserData(@NotNull String user, @NotNull Continuation $completion) {
      return user;
   }

   @NotNull
   public static final String updateTextView(@NotNull String user) {
      Intrinsics.checkNotNullParameter(user, "user");
      return user;
   }
}
```

1. 코드의 *64번째 라인*을 보면 `fetchUserData(…)`와 `cacheUserData(…)`와 같이 suspend function이었던 함수들이 일반 함수로 변경되고 마지막 매개변수로 Continuation이 들어간 것을 확인할 수 있다. 

2. 일시 중단과 재개를 위해서 suspention point를 label로 표시해두는 **Labeling** 작업이 Decompile되어 *11번째 라인*과 같이 `switch-case`문이 생성된 것을 확인할 수 있다.
   - 케이스가 3개 생성되었는데 첫 번째 케이스에서 `fetchUserData(…)`를 호출하면서 **Continuation** 객체가 넘어가는 것을 확인할 수 있다.
   - 위의 자료에서 설명했던 부분과는 다른 부분이 존재하지만, 요지는 `switch-case` 형태로 Decompile되면서 다시 재개될 수 있는 형태로 만들어지고 **Continuation** 객체가 전달되고 있는 것을 확인할 수 있다는 것이다.
   - 아래 이미지처럼 *15번째 라인*과 *34번째 라인*에서 함수의 마지막 매개변수로 Continuation 객체가 `this`로 전달되고 있다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/120595224-004e0000-c47d-11eb-8e2c-3f965f4c09ed.png'>
</p>

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/120595278-10fe7600-c47d-11eb-8ffc-cbcb1437b475.png'>
</p>

## CPS simulation 해보기

```kotlin
    GlobalScope.launch {
↛       val userData = fetchUserData()
↛       val userCache = cacheUserData(userData)
        updateTextView(userCache)
    }
```

```kotlin
fun main() {
    println("[in] main")
    myCoroutine(MyContinuation())
    println("\n[out] main")
}

fun myCoroutine(cont: MyContinuation) {
    when(cont.label) {
        0 -> {
            println("\nmyCoroutine(), label: ${cont.label}")
            cont.label = 1
            fetchUserData(cont)
        }
        1 -> {
            println("\nmyCoroutine(), label: ${cont.label}")
            val userData = cont.result
            cont.label = 2
            cacheUserData(userData, cont)
        }
        2 -> {
            println("\nmyCoroutine(), label: ${cont.label}")
            val userCache = cont.result
            updateTextView(userCache)
        }
    }
}

fun fetchUserData(cont: MyContinuation) {
    println("fetchUserData(), called")
    val result = "[서버에서 받은 사용자 정보]"
    println("fetchUserData(), 작업완료: $result")
    cont.resumeWith(Result.success(result))
}

fun cacheUserData(user: String, cont: MyContinuation) {
    println("cacheUserData(), called")
    val result = "[캐쉬함 $user]"
    println("cacheUserData(), 작업완료: $result")
    cont.resumeWith(Result.success(result))
}

fun updateTextView(user: String) {
    println("updateTextView(), called")
    println("updateTextView(), 작업완료: [텍스트 뷰에 출력 $user]")
}

class MyContinuation(override val context: CoroutineContext = EmptyCoroutineContext)
    : Continuation<String> {

    var label = 0
    var result = ""

    override fun resumeWith(result: Result<String>) {
        this.result = result.getOrThrow()
        println("Continuation.resumeWith()")
        myCoroutine(this)
    }
}
```
```
[in] main

myCoroutine(), label: 0
fetchUserData(), called
fetchUserData(), 작업완료: [서버에서 받은 사용자 정보]
Continuation.resumeWith()

myCoroutine(), label: 1
cacheUserData(), called
cacheUserData(), 작업완료: [캐쉬함 [서버에서 받은 사용자 정보]]
Continuation.resumeWith()

myCoroutine(), label: 2
updateTextView(), called
updateTextView(), 작업완료: [텍스트 뷰에 출력 [캐쉬함 [서버에서 받은 사용자 정보]]]

[out] main
```

## 정리

- There is no magic
  - CPS<small>Continuation Passing Style</small> == Callbacks
  - CPS Transformation
- Decompile
  - Labels
  - Callback
- CPS simulation
  - debugging

## References

- [새차원, 코틀틴 코루틴](https://youtu.be/DOXyH1RtMC0)
- [KotlinConf 2017 - Deep Dive into Coroutines on JVM by Roman Elizarov](https://www.youtube.com/watch?v=YrrUCSi72E8&t=110s)