# In-Depth Guide to Coroutine Cancellation & Exception Handling

코루틴을 막 학습한 사람에게 코루틴은 매우 간단하고 자바스크립트의 async, await와 비슷하게 보이기도 해서 비동기 프로그래밍을 위한 아주 쉽고 훌륭한 도구로 보일 수 있습니다. 실제로 쉽고 훌륭한 도구이긴 하지만요.

하지만 코루틴을 더 깊게 살펴보면 실제로 걸리기 쉬운 함정들이 많이 존재합니다. 예외 처리나 취소를 `try-catch` 블록을 통해 간단히 할 수 있으리라 생각하지만 실제로는 복잡한 매커니즘으로 동작하고 있기에 많은 것들이 잘못될 수도 있습니다.

본문에서는 유튜브의 [In-Depth Guide to Coroutine Cancellation & Exception Handling](https://youtu.be/VWlwkqmTLHc) 영상을 바탕으로 다음의 내용을 다룹니다.
- 코루틴에서 어떻게 예외를 잡고 처리해야 하는지
- 코루틴에서 예외 처리가 일반적으로 어떻게 작동하는지
- 코루틴이 취소되거나 코루틴을 취소할 때 무엇을 고려해야 하는지

#### dependencies

```console
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1'
implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.5.0'
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0'
```

## 많은 사람들이 오해하는 코루틴의 예외 처리

### `launch`에서의 예외 처리

먼저 CoroutineScope인 `lifecycleScope`를 통해 코루틴 빌더인 `launch`를 수행하는 MainActivity 코드를 작성한 뒤, 내부에 예외를 던지는 자식 코루틴을 생성해봅시다.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { // 부모 코루틴
            launch { // 자식 코루틴
                throw Exception()
            }
        }
    }
}
```

여기서 많은 사람들은 `try-catch` 블록을 통해 간단히 해당 예외를 처리할 수 있으리라 생각합니다. 실제로 아래의 코드를 실행하면 어떻게 될까요?

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            try {
                launch {
                    throw Exception()
                }
            } catch (e: Exception) {
                println("Caught Exception: $e")
            }
        }
    }
}
```

코드를 실행하면 위의 예외가 `try-catch` 블록에서 잡히지 않아 앱에서 크래시가 발생한 것을 확인할 수 있습니다.

<p align = 'center'>
<img width = '1000' src = 'https://user-images.githubusercontent.com/39554623/178762710-9b87aded-5ced-43a0-8b31-711a62468138.png'>
</p>

`try-catch` 블록을 분명히 사용했는데도 크래시가 발생했네요. 이것이 바로 코루틴에서 `try-catch` 블록이 제대로 동작하지 않는 경우입니다. 왜 이렇게 동작하는지 이해하기 위해서는 먼저 CoroutineScope와 코루틴이 동작하는 방식을 이해해야 합니다.

우리는 일반적으로 외부에 `lifecycleScope`, `viewModelScope` 또는 직접 생성한 Custom Scope와 같은 CoroutineScope를 가지고 이 Scope 내부에서 코루틴을 실행합니다.

```kotlin
lifecycleScope.launch {
    try {
        launch {
            throw Exception()
        }
    } catch (e: Exception) {
        println("Caught Exception: $e")
    }
}
```

그리고 위의 코드처럼 해당 코루틴 내부에 자식 코루틴을 만들 수 있는데, 자식 코루틴 내부에서 예외를 던지면 어떤 일이 발생할까요?

참고로 해당 예외에 대한 가장 일반적인 예시는 Retrofit을 사용한 API 호출에서 *HttpException*이 발생하여 서버가 404 Not Found를 응답하는 경우 예외를 던지는 상황입니다.

순서는 다음과 같습니다.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {  // 4. 예외가 처리되지 않고 root 코루틴까지 예외가 전파됨(이 시점에 앱 크래시 발생)
            try {
                launch {  // 3. 여전히 예외가 처리되지 않았으므로 이 코루틴으로도 예외가 전파됨
                    launch {  // 2. 현재 코루틴으로 예외가 전파됨(propagation)
                        throw Exception()  // 1. 예외 발생
                    }
                }
            } catch (e: Exception) {
                println("Caught Exception: $e")
            }
        }
    }
}
```

코루틴에서 예외가 전파되는 것처럼 코루틴의 취소<small>(cancellation)</small>에서도 같은 일이 발생합니다.

코루틴이 취소될 때 *CancellationException*을 던지는데 이 예외는 항상 코루틴에서 처리되거나 잡히기 때문에 무언가 잘못되거나 나쁜 것이 아닙니다. 하지만 취소는 여전히 **코루틴 트리**로 전파되므로 부모 코루틴을 포함해 모든 자식 코루틴들이 특정 코루틴이 취소된 것을 감지합니다.

코루틴 트리는 structured concurrency의 동작 방식을 통해 코루틴이 내부적으로 트리 구조<small>(부모-자식)</small>의 형태로 관리가 되고 있음을 추측할 수 있는데 더 자세한 내용을 보고 싶으시다면 [이 글](https://suhwan.dev/2022/01/21/Kotlin-coroutine-structured-concurrency/)을 참고하시면 좋을 것 같습니다.

### `async`에서의 예외 처리

`lauch`와 비교해서 `asyc`에서 예외 처리가 동작하는 방식의 차이점은 `async`는 `await`를 호출할 때 **누적된 예외를 던지는 것**입니다.

아래의 코드에서 `await()`는 root 코루틴인 launch 블록을 async 블록이 실행되고 0.5초 뒤에 `Result` 값을 사용가능 할 때까지 suspend됩니다.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val string = async {
                delay(500L)
                "Result"
            }
            println(string.await())
        }
    }
}
```

위에서 `async`는 `await`를 호출할 때 누적된 예외를 던진다고 했습니다. 그렇다면 아래의 코드는 어느 시점에 크래시를 발생시킬까요?

```kotlin
lifecycleScope.launch {  // 2. 예외가 부모 코루틴으로 전파되어 앱에 크래시가 발생 
    val string = async {
        delay(500L)
        throw Exception("error")  // 1. 예외를 던지자마자
        "Result"
    }
    println(string.await())
}
```

이 코드에선 `await`가 호출되는 시점에 예외를 던지지 않습니다. `launch`를 사용하고 있기 때문에 `async` 블록 내에서 예외를 던지자마자 앱에 크래시가 발생합니다.

그러면 개념을 다른 예시로 이해해보기 위해 이번엔 `await()` 라인을 삭제해보겠습니다.

```kotlin
lifecycleScope.launch {
    val string = async {
        delay(500L)
        throw Exception("error") 
        "Result"
    }
}
```

이 코드도 여전히 크래시가 발생합니다. 위에서 `async`는 `await`를 호출할 때 누적된 예외를 던진거나 전파한다고 했음에도 불구하고 왜 그러는걸까요? 

코루틴에서 예외가 전파되는 매커니즘을 생각해봅시다. 위 코드에서 async 블록은 자식 코루틴이기 때문에 해당 블록 내에서 예외를 던지면 부모 코루틴인 launch 블록으로 예외를 전파시킵니다. 예외가 처리되지 않았다면 이전 launch 블록에서 예외를 던지는 코드들처럼 즉시 프로그램에 크래시를 발생시킵니다.

하지만 launch를 async로 대체하고 앱을 재실행해보면 크래시가 발생하지 않습니다. 자식 코루틴에서 발생한 예외가 부모 코루틴으로 전파되더라도 둘 다 async 블록이기 때문에 즉시 앱에 크래시를 발생시키지 않습니다.

```kotlin
lifecycleScope.async {
    val string = async {
        delay(500L)
        throw Exception("error") 
        "Result"
    }
}
```

외부 async 블록의 리턴값을 `deferred`에 담고 `deferred`를 다른 스코프 내부에서 `deferred.await()`를 통해 소비하도록 아래와 같이 코드를 작성하면 앱에 크래시가 발생합니다.

```kotlin
val deferred = lifecycleScope.async {
    val string = async {
        delay(500L)
        "Result" 
    }
}
lifecycleScope.launch {  // 2. launch 블록은 앱에 크래시를 발생시킨다. 
    deferred.await()     // 1. 예외 처리를 별도로 하지 않았으므로 await()가 던진 예외가 위로 전파되고(raise) 
}                     
```

위와 같이 코드에 `deferred`가 있다면 아래의 코드처럼 `await()`를 호출하는 라인을 try-catch 블록으로 감싸주는 것으로 예외를 처리할 순 있습니다. 앱을 실행해도 크래시도 발생하지 않고요.

```kotlin
val deferred = lifecycleScope.async {
    val string = async {
        delay(500L)
        "Result"
    }
}
lifecycleScope.launch {
    try {
        deferred.await()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
```

하지만 이렇게 예외를 처리하면 잘못된 상황이 쉬우며 사람들이 많이 실수하는 코드이기도 합니다.

코루틴이 어떻게 동작하는지 더 알아본 뒤, 본문의 끝부분에서 위의 코드에서 발생한 실수에 대해 이야기하겠습니다.

#### CoroutineExceptionHandler

그전에 try-catch 블록 이외에 예외를 처리하는 방법인 CoroutineExceptionHandler에 대해 알아보고자 합니다.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val handler = CoroutineExceptionHandler { _, throwable ->
            println("Caught exception: $throwable")
        }

        lifecycleScope.launch(handler) {  // root 코루틴에 handler를 전달
            throw Exception("Error")
        }
    }
}
```

CoroutineExceptionHandler를 생성한 뒤, 이 handler를 우리가 실행할 코루틴에 적용<small>(install)</small>시킬 수 있는데 **반드시 root 코루틴에게 적용**시켜야 합니다.

<p align = 'center'>
<img width = '1000' src = 'https://user-images.githubusercontent.com/39554623/178797327-75474dd8-f702-4974-b742-e1754318010c.png'>
</p>

코드를 실행해보면 크래시도 발생하지 않고 Logcat에 예외가 잡힌 것을 확인할 수 있습니다. 

CoroutineExceptionHandler는 root 코루틴의 모든 타입의 자식 코루틴에서 잡히지 않은 예외들을 처리할 수 있는 방법입니다.

주의해야할 점은 CoroutineExceptionHandler는 *CancellationException*을 잡지 않는다는 것입니다. 그렇기 때문에 코루틴 하나가 취소되더라도 CoroutineExceptionHandler의 블록은 실행되지 않습니다.

*CancellationException*과 코루틴은 코루틴이 취소되었다고 앱에서 크래시가 발생하는 것을 원하지 않을 것이기 때문에 기본적으로 처리가 되고, 앞에서 언급했듯 잡히지 않은 예외들만을 처리합니다.

## 코루틴의 Scope

이제 특정 코루틴과 자식 코루틴을 어떻게 취소할지를 결정하는 2가지 Scope를 알아보려 합니다.

1. CoroutineScope
2. SupervisorScope

### CoroutineScope

아래에 일반적인 CoroutineScope 내부에 2개의 자식 코루틴을 실행하는 코드가 있습니다. 앱을 실행해볼까요?

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                delay(300L)
                throw Exception("Coroutine 1 failed")
            }
            launch {
                delay(400L)
                println("Coroutine 2 finished")
            }
        }
    }
}
```

<p align = 'center'>
<img width = '1000' src = 'https://user-images.githubusercontent.com/39554623/178922005-8e511058-2f99-45fc-b3b5-b198ac6b53fa.png'>
</p>

당연하게도 Coroutine 2가 완료되기도 전에 Coroutine 1에서 예외를 던지므로 앱에서 크래시가 발생하고 "Coroutine 1 failed"라는 에러를 확인할 수 있습니다.

그렇다면 CoroutineExceptionHandler를 사용하면 어떻게 될까요?

```kotlin
val handler = CoroutineExceptionHandler { _, throwable ->
    println("Caught exception: $throwable")
}

// + 연산자를 통해 2개의 CoroutineContext를 합쳐서 CoroutineScope에 적용
CoroutineScope(Dispatchers.Main + handler).launch {
    launch {
        delay(300L)
        throw Exception("Coroutine 1 failed")
    }
    launch {
        delay(400L)
        println("Coroutine 2 finished")
    }
}
```

<p align = 'center'>
<img width = '1000' src = 'https://user-images.githubusercontent.com/39554623/178924588-1bdee821-e827-49b5-a708-3564aca2b06e.png'>
</p>

"Caught exception: java.lang.Exception: Coroutine 1 failed"에서 예외를 잡아 앱에 크래시가 발생하지 않았지만 Coroutine 2가 완료된 것을 확인할 수 없습니다.

위에서 2개의 자식 코루틴은 개별적으로 실행되는 것으로 보이는데 왜 Coroutine 1이 Coroutine 2에 영향을 끼친 것처럼 보일까요?

그 이유는 바로 CoroutineScope를 사용했기 때문입니다.

CoroutineScope는 **예외를 처리했든 안했든** 코루틴이 실패하자마자 모든 자식 코루틴과 전체 Scope를 취소합니다. 다시 정리하자면 CoroutineScope는 단 하나의 코루틴이 실패하더라도 스코프 전체가 취소됩니다. 여기서 실패<small>(fail)</small>는 예외를 던지는 것을 의미합니다.

여기서 다른 버전의 CoroutineScope인 SupervisorScope 개념이 등장합니다.

### SupervisorScope

그러면 위의 코드에서 2개의 launch 블록들을 supervisorScope 내부에 넣고 재실행 해봅시다.

```kotlin
val handler = CoroutineExceptionHandler { _, throwable ->
    println("Caught exception: $throwable")
}

CoroutineScope(Dispatchers.Main + handler).launch {
    supervisorScope {  // 자식 코루틴들을 supervisorScope 내부에 넣는다.
        launch {
            delay(300L)
            throw Exception("Coroutine 1 failed")
        }
        launch {
            delay(400L)
            println("Coroutine 2 finished")
        }
    }
}
```

<p align = 'center'>
<img width = '1000' src = 'https://user-images.githubusercontent.com/39554623/178926662-0f2d3f26-2f13-4823-ab90-12bcb95a2e56.png'>
</p>

위의 Logcat을 보면 예외도 잡혔고 Coroutine 2도 완료된 것을 확인할 수 있습니다.

SupervisorScope는 내부의 코루틴 하나가 실패하거나 예외를 던지더라도 해당 Scope 내부의 다른 코루틴에게 영향을 주지 않습니다.

즉, 여러 개의 코루틴들을 묶어서 하나가 실패하면 모두 실패할지 아닐지에 대한 동작을 CoroutineScope 또는 SupervisorScope를 통해 정의할 수 있는 것입니다.

이 개념이 중요한 이유는 앱에서 커스텀한 CoroutineScope가 필요해지는 경우가 있기 때문입니다. 컴포넌트의 수명 주기를 관리하기 위해 고유한 CoroutineScope를 작성하여 해당 컴포넌트가 적절히 취소되어 더 이상 사용되지 않도록 위해서 말이죠.

`viewModelScope`가 그러한 scope의 예시입니다. ViewModel이 clear되면 해당 ViewModel 내에서 실행되는 모든 코루틴들 또한 clear됩니다.

이러한 동작을 하는 custom scope를 생성하기 위해서 많은 사람들이 `CoroutineScope(Dispatchers.Main + handler)`와 같은 형태의 코드를 사용합니다. 하지만 여기서 가장 큰 실수와 문제점은 자신의 custom scope에 대해 이러한 작업을 수행할 경우, 전체 컴포넌트에서 하나의 코루틴이 실패하면 다른 모든 것들 또한 실패하고 CoroutineScope가 취소된다는 것입니다. Scope가 한 번 취소되면 새로운 코루틴을 다시 실행할 수 없습니다.

이러한 상황이 viewModelScope에서 발생한다고 가정해봅시다. 예시로 하나의 네트워크 호출이 viewModelScope에서 실패하여 예외를 던진다면 내부의 다른 코루틴들도 모두 취소될 것이고, viewModelScope 전체도 취소되어 ViewModel 전체를 다시 생성하지 않는 한 새로운 코루틴을 시작할 수 없습니다. 이것은 우리가 ViewModel에서 원하는 동작이 아닐겁니다.

<p align = 'center'>
<img width = '800' src = 'https://user-images.githubusercontent.com/39554623/178932727-6fa36f71-9486-4eb7-8381-28bf7a99f434.png'>
</p>

실제로 ViewModel의 `viewModelScope`를 확인해보면 내부적으로 `SupervisorJob()`과 `Dispatchers.Main.immediate`를 합친 CoroutineContext를 CoroutineScope에 넘겨주고 있는 것을 확인할 수 있습니다. 이건 아주 중요한 부분인데 `viewModelScope`가 supervisorScope라는 것입니다. 왜냐하면 ViewModel에서 하나의 코루틴이 실패하면 다른 코루틴들도 실패하고 취소되는 동작을 원치 않기 때문이고 이는 `lifcycleScope`의 내부 구현에서도 동일합니다.

직접 CoroutineScope를 구현하는 경우 이것을 이해하는 것이 매우 중요합니다.

## 사람들이 자주하는 실수

본문의 위에서 언급한 사람들이 코루틴에서 예외를 처리할 때 자주하는 실수에 대해 다뤄보고자 합니다.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val job = launch {
                try {
                    delay(500L)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                println("Coroutine 1 finished")
            }
            delay(300L)
            job.cancel()
        }
    }
}
```

<p align = 'center'>
<img width = '1000' src = 'https://user-images.githubusercontent.com/39554623/178935656-92cbfb75-ddbb-41f4-9fda-26b668c66ccd.png'>
</p>

위의 코드는 0.5초 뒤에 작업이 완료되는 job을 앱이 실행되고 0.3초 뒤에 취소하는 코드입니다. 일반적인 예상대로라면 job이 완료가 되기도 전에 취소했으므로 Coroutine 1은 완료되지 않아야하는데 Logcat에 *CancellationException*이 발생한 이후 "Coroutine 1 finished"가 출력된 것을 확인할 수 있습니다. 이러한 예상치 못한 동작이 발생한 이유를 알아봅시다.

위의 코드에서 job에 할당한 코루틴이 취소되면 어떤 일이 발생할까요?

여기서 suspend 함수인 `delay()`는 코루틴이 취소될 경우 *CancellationException*을 던집니다. 하지만 `delay()`는 일반적인 Exception을 처리하는 try-catch 블록 내에서 실행되고 있기에 *CancellationException*이 try-catch 블록에 의해 처리되어 버립니다. 해당 예외가 이미 처리되어 버렸기 때문에 제대로 전파되지 않으므로 외부의 CoroutineScope는 자식 Coroutine이 취소된 것을 알지 못하는 것이지요. 이것이 여전히 "Coroutine 1 finished"를 출력하는 이유입니다.

`delay()`, `yield()` 등과 같은 cancellable suspending function은 코루틴이 취소될 때 *CancellationException*을 던지는데, 제가 정리한 [Coroutine Cancellation and Exception Handling](https://github.com/june0122/TIL/blob/master/KOTLIN/Coroutines/3.%20Coroutine%20Cancellation.md) 글에서 자세한 내용을 확인할 수 있으니 참고바랍니다.

그러면 이 문제를 어떻게 해결할 수 있을지 고민해봅시다.

#### 방법 1

특정 예외를 정확히 catch하는 것으로 먼저 위의 문제를 해결할 수 있습니다.

이 코드를 실행하면 Logcat에 "Coroutine 1 finished"가 출력되지 않음을 확인할 수 있습니다.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch { // 2. CancellationException이 부모 Scope까지 제대로 전파되어
            val job = launch {
                try {
                    delay(500L)
                } catch (e: HttpRetryException) {  // 1. CancellationException이 잡히지 않으므로
                    e.printStackTrace()
                }
                println("Coroutine 1 finished")  // 3. 이 라인의 작업을 실행하지 않는다.
            }
            delay(300L)
            job.cancel()
        }
    }
}
```

#### 방법 2

General Exception을 catch하고 싶을 경우, 해당 예외가 CancellationException일 경우 다시 예외를 던지는 코드를 작성하는 방법으로 해결할 수 있습니다.

마찬가지로 이 코드를 실행하면 Logcat에 "Coroutine 1 finished"가 출력되지 않음을 확인할 수 있습니다.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {  // 2. CancellationException이 부모 Scope까지 제대로 전파되도록 한다.
            val job = launch {
                try {
                    delay(500L)
                } catch (e: Exception) {
                    if (e is CancellationException) {
                        throw e  // 1. CancellationException일 경우 예외를 다시 던져
                    }
                    e.printStackTrace()
                }
                println("Coroutine 1 finished")
            }
            delay(300L)
            job.cancel()
        }
    }
}
```

코루틴의 취소를 제대로 전파하지 않는 실수는 여러 사람의 코드에서 꽤나 발견되는 실수입니다. 하지만 이러한 실수로 인해 작성한 코루틴이 엉망이 될 수 있고, 이는 코루틴을 취소했는데도 여전히 작업을 수행하기 때문에 리소스를 낭비하는 결과를 초래합니다.