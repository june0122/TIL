# [Coroutines basics](https://developer.android.com/kotlin/coroutines?hl=ko)

## 코루틴 개요

- 코루틴은 **루틴**의 일종
- 협동 루틴이라 할 수 있으며, Coroutine의 `Co`는 *with* 또는 *together*를 뜻한다.
 
> 루틴과 코루틴의 차이

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/120277451-08783500-c2ef-11eb-96e7-403bf6e5d5bd.png'>
</p>

코루틴은 이전에 자신의 실행이 마지막으로 **중단**되었던 지점 다음의 장소에서 실행을 **재개**한다. <small>(함수의 진입점과 출구점이 여러 개 존재한다)</small> 

### 코루틴의 역사

코루틴은 멜빈 콘웨이에 의해 1958년 처음 만들어진 용어이며, 이를 어셈블리 프로그램에 적용했다. 코루틴은 협력 작업 <sup>cooperative tasks</sup>, 예외 <sup>exceptions</sup>, 이벤트 루프 <sup>event loops</sup>, 반복자 <sup> iterators</sup>, 무한 리스트 <sup>infinite lists</sup> 및 파이프 <sup>pipes</sup>와 같은 친숙한 프로그램 구성 요소를 구현하는 데 적합하다고 소개되어있는데 <sup id = "a1">[1](#f1)</sup> 구글 문서에서는 코루틴의 사용 범위를 좀 더 명확하게 제시하고 있다.

### Android Developers 코틀린 가이드에서의 코루틴 정의 <sup id = "a1">[2](#f1)</sup>

- 코루틴은 **비동기적으로 실행되는 코드를 간소화하기 위해** Android에서 사용할 수 있는 동시 실행 설계 **패턴**<small>(concurrency design pattern)</small>이다.
  - 비동기 처리는 callback, cancel, 리소스 관리 등을 해줘야하므로 어려움이 있는데 코루틴을 이용하면 쉽게 처리할 수 있다.
- Android에서 코루틴은 **메인 스레드를 blocking하여 앱이 응답하지 않게 만들 수도 있는 장기 실행 작업을 관리하는 데 도움**이 됩니다.

### Google Codelabs의 코루틴 정의 <sup id = "a1">[3](#f1)</sup>

- 코루틴은 비동기 콜백 처리를 순차적인 코드로 작성할 수 있게 해주는 코틀린 기능이다. <small>(**Coroutines** are a Kotlin feature that **converts async callbacks for long-running tasks**, such as database or network access, into **sequential code**.)</small>
- `suspend` 함수를 사용하여 비동기 코드를 순차적으로 만들어준다. <small>(Use suspend functions to **make async code sequential**)</small>

## 코루틴 기초

먼저 코루틴을 사용하기 위해 `kotlinx.coroutines.*`를 import해야하므로 [공식 프로젝트 README](https://github.com/Kotlin/kotlinx.coroutines/blob/master/README.md#using-in-your-projects)를 참고하여 최신 버전을 의존성에 추가한다.

> ### ① Thread

```kotlin
fun main() {
    thread {
        Thread.sleep(1000L)
        println("World!")
    }

    println("Hello, ")
    Thread.sleep(2000L) // // block main thread for 2 seconds to keep JVM alive
}
```

`main()`이 `println("World!")`가 실행되기도 전에 종료되는 것을 막기 위해 맨 밑줄에 `Thread.sleep(2000L)`이 추가되어 있다.

> ### ② Your first Coroutine

```kotlin
fun main() {
    GlobalScope.launch { // launch a new coroutine in background and continue
        delay(1000L) // non-blocking delay for 1 second
        println("World!") // print after delay
    }
    
    println("Hello, ") // main thread continues while coroutine is delayed
    Thread.sleep(2000L) // block main thread for 2 seconds to keep JVM alive
}
```

**launch**는 독립적으로 실행이 불가능하며 **CoroutineScope** 내에서만 선언된다.

> ### ③ Bridging blocking and non-blocking worlds

`Thread.sleep()`과 `delay()`가 혼용되는 것을 막기 위해 `delay()`로 통일해보자. 하지만 CoroutineScope 외부에 `delay()`를 추가하려는데 에러가 발생한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/120302445-decc0780-c308-11eb-97ae-ede496319166.png'>
</p>

`Thread.sleep(2000L)`은 메인 스레드를 blocking하는 형태였으므로, `delay()`로 변경하기 위해서는 명시적으로 Blocking하는 코루틴을 만들어줘야 한다. 그 역할을 하는 코루틴 빌더가 **runBlocking**이다.

```kotlin
fun main() {
    GlobalScope.launch { // launch a new coroutine in background and continue
        delay(1000L)
        println("World!")
    }

    println("Hello, ") // main thread continues here immediately
    runBlocking { // but this expression blocks the main thread
        delay(2000L) // ... while we delay for 2 seconds to keep JVM alive
    }
}
```

**lauch**는 자신을 호출하는 스레드를 **Blocking 하지 않는다.**

**runBlocking**은 자신을 호출하는 스레드를 **Blocking 한다.**

> ### ④ rewritten in a more idiomatic way<small>(관용적인 형태로 만들기)</small>

이때까지 작성한 코드들은 main 함수 안의 내용이 완료되기 전까지는 return되지 않기를 원하는 것이기 때문에 **runBlocking**을 마지막에만 사용하는 것이 아니라 코드 전체를 **runBlocking**으로 감싸는 형태로 작성한다.

```kotlin
fun main() {
    runBlocking {
        GlobalScope.launch {
            delay(1000L)
            println("World!")
        }
        
        println("Hello, ")
        delay(2000L)
    }
}
```

메인 함수를 **runBlocking**으로 감쌌기 때문에 전체 코드가 실행되기 전까지는 메인 스레드가 **runBlocking** 때문에 return이 되지 않는다. 그리고 위의 코드는 아래와 같이 한 번 더 변경될 수 있다.

```kotlin
fun main() = runBlocking { // // start main coroutine
    GlobalScope.launch { // launch a new coroutine in background and continue
        delay(1000L)
        println("World!")
    }

    println("Hello, ") // main coroutine continues here immediately
    delay(2000L) // delaying for 2 seconds to keep JVM alive
}
```

> ### ⑤ Waiting for a job

프로그램이 종료되는 것을 막기 위해 delay를 사용하는 것은 좋은 접근법이 아니다. 명시적으로 `job`을 생성하여 기다리게 해보자.

lauch를 하게 되면 Job이 반환되는데, Job 객체에다가 `join()`을 하게 되면 해당 Job이 완료될 때까지 기다리게 된다.

```kotlin
fun main() = runBlocking {
    val job = GlobalScope.launch {
        delay(1000L)
        println("World!")
    }

    println("Hello, ")
    job.join()
}
```

> ### ⑥ Structured concurrency

이전 예제들에선 코루틴이 완료되는 것을 기다리기 위해 2초동안 `sleep()`을 실행하거나 Job 객체를 만들어서 `join()`을 사용하였다. 하지만 앞으로 여러 개의 코루틴을 실행하게 될텐데 이런 형태라면 아래와 같이 Job을 따로 관리해주는 등, 유지 보수하기 어려운 코드가 된다.

```kotlin
fun main() = runBlocking {
    val job1 = GlobalScope.launch {
        delay(1000L)
        println("World!")
    }

    val job2 = GlobalScope.launch {
        delay(2000L)
        println("Let's Go!")
    }

    println("Hello, ")
    
    job1.join()
    job2.join()
}
```

코틀린에서는 이를 방지하기 위해서 **Structured concurrency**를 사용하라고 권장한다. runBlocking과 launch에서 실행된 코루틴이 구조적인 관계를 만든다면 서로 기다려주는 일을 할 수 있기 때문이다.

`GlobalScope.launch`를 사용하게 되면, **top-level 코루틴을 생성**하게 된다. 이렇게 되면 **많은 메모리를 소모**하게 된다. 그러므로 GlobalScope에서 launch를 하는 것이 아닌 runBlocking을 통해 들어온 CoroutineScope에서 lauch를 한다.

```kotlin
fun main() = runBlocking { // this: CoroutineScope
    launch { // // launch a new coroutine in the scope of runBlocking (this. 생략 가능)
        delay(1000L)
        println("World!")
    }
    println("Hello, ")
}
``` 

즉, **Structured concurrency**는 top-level 코루틴을 만들지 말고 `runBlocking`의 자식으로 코루틴을 만들면 부모 코루틴이 자식 코루틴이 완료되는 것까지 다 기다려주기 때문에 구조적인 형태를 이용해서 코루틴을 관리하라는 개념이다.

참고로 GlobalScope는 잘못 사용하기 쉬운 advanced API이므로 [Kotlin Coroutines 1.5.0](https://blog.jetbrains.com/kotlin/2021/05/kotlin-coroutines-1-5-0-released/#globalscope)부터 **delicate API**로 표시되어 컴파일러가 경고를 보낸다.

> ### ⑦ Extract function refactoring

lauch 내부의 코드를 하나의 함수로 추출해보자. 그러면 아래와 같은 오류가 발생한다.

```kotlin
fun main() = runBlocking {
    launch { 
        doWorld() 
    }
    println("Hello")
}

fun doWorld() {
    delay(1000L)
    println("World!")
}
```

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/120296667-5139e900-c303-11eb-8cac-7727a7e86643.png'>
</p>

*Suspend function 'delay' should be called only from a coroutine or another suspend function*. 즉, 일시 중단되는 함수인 `delay()`는 오직 코루틴이나 다른 suspend function으로부터만 호출될 수 있다는 뜻이다.

이를 해결하기 위해선 `suspend` 키워드를 함수 앞에 붙여주면 된다.

```kotlin
fun main() = runBlocking {
    launch { 
        doWorld()
    }
    println("Hello")
}

suspend fun doWorld() {
    delay(1000L)
    println("World!")
}
```

> ### ⑧ Coroutines ARE light-weight

10만 개의 코루틴을 한 번에 생성하여 1초 뒤에 한 번의 점을 찍게 하는 코드를 실행해본다. 직접 실행해보면 아무런 오류나 성능적 이슈없이 점들이 출력된다.

```kotlin
fun main() = runBlocking {
    repeat(100_000) { // launch a lot of coroutines
        launch {
            delay(5000L)
            print(".")
        }
    }
}
```

<p align = 'center'>
<img width = '800' src = 'https://user-images.githubusercontent.com/39554623/120319461-ffea2380-c31b-11eb-9d19-0c86475f0b5b.png'>
</p>


그렇다면 이번에 코루틴이 아닌 스레드로 바꿔서 똑같은 작업을 해보자. OutOfMemoryError가 발생하는 것을 확인할 수 있다.

```kotlin
fun main() = runBlocking {
    repeat(100_000) { // launch a lot of coroutines
        thread {
            Thread.sleep(5000L)
            print(".")
        }
    }
}
```

<p align = 'center'>
<img width = '800' src = 'https://user-images.githubusercontent.com/39554623/120319256-c9140d80-c31b-11eb-9f38-b9e891fd4dcf.png'>
</p>

이 두 코드를 통해 **코루틴이 스레드보다 구조적으로 상당히 가볍다**는 것을 확인할 수 있다. 많은 스레드를 만들면 부하가 일어날 수 있지만, 코루틴은 많은 양을 생성하여도 크게 부하가 가지 않는 것이다.

> ### ⑨ Global coroutines are like daemon threads

```kotlin
fun main() = runBlocking {
    GlobalScope.launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }

    delay(1300L)
}
```

```
I'm sleeping 0 ...
I'm sleeping 1 ...
I'm sleeping 2 ...

Process finished with exit code 0
```

**코루틴이 계속 실행되고 있다고해서 프로세스가 유지되지는 않는다**라는 것을 보여주는 코드이다. 코루틴은 **데몬 스레드처럼** 프로세스가 살아있을 동안에만 동작할 수 있다라는 것이다.

즉, **프로세스가 종료되면 코루틴도 끝난다**라는 것이다.

> ### ⑩ suspend <-> resume

```kotlin
fun main() = runBlocking {
    launch {
        repeat(5) { i ->
            println("Coroutine A, $i")
        }
    }

    launch {
        repeat(5) { i ->
            println("Coroutine B, $i")
        }
    }

    println("Coroutine Outer")
}
```

```
Coroutine Outer
Coroutine A, 0
Coroutine A, 1
Coroutine A, 2
Coroutine A, 3
Coroutine A, 4
Coroutine B, 0
Coroutine B, 1
Coroutine B, 2
Coroutine B, 3
Coroutine B, 4
```

println을 override 하여 어떤 스레드에서 실행되었는지 확인한다.

```kotlin
fun main() = runBlocking {
    ...
}

fun <T> println(msg: T) {
    kotlin.io.println("$msg [${Thread.currentThread().name}]")
}
```

```
Coroutine Outer [main]
Coroutine A, 0 [main]
Coroutine A, 1 [main]
Coroutine A, 2 [main]
Coroutine A, 3 [main]
Coroutine A, 4 [main]
Coroutine B, 0 [main]
Coroutine B, 1 [main]
Coroutine B, 2 [main]
Coroutine B, 3 [main]
Coroutine B, 4 [main]
```

`Help - Edit Custom VM Options...`에서 **-Dkotlinx.coroutines.debug**를 복사한 뒤, 실행 버튼 왼쪽의 탭을 클릭하여 `Edit Configurations...` 옵션에 들어가 VM options 칸에 복사한 **-Dkotlinx.coroutines.debug**를 붙여 넣어주면 어느 코루틴에서 실행되었는지까지 확인할 수 있다.

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/120322472-6886cf80-c31f-11eb-861b-dab8a29df423.png'>
</p>

<p align = 'center'>
<img width = '800' src = 'https://user-images.githubusercontent.com/39554623/120322546-7b010900-c31f-11eb-92e6-54504ccd150f.png'>
</p>

```
Coroutine Outer [main @coroutine#1]
Coroutine A, 0 [main @coroutine#2]
Coroutine A, 1 [main @coroutine#2]
Coroutine A, 2 [main @coroutine#2]
Coroutine A, 3 [main @coroutine#2]
Coroutine A, 4 [main @coroutine#2]
Coroutine B, 0 [main @coroutine#3]
Coroutine B, 1 [main @coroutine#3]
Coroutine B, 2 [main @coroutine#3]
Coroutine B, 3 [main @coroutine#3]
Coroutine B, 4 [main @coroutine#3]
```

첫 번째 코루틴<small>(coroutine#1)</small>은 runBlocking에 의해서 만들어졌고, 나머지 두 번째와 세 번째 코루틴은 launch에 의해 순서대로 만들어졌다.

이제 Coroutine A에 10ms의 delay를 줘보자.

```kotlin
fun main() = runBlocking {
    launch {
        repeat(5) { i ->
            println("Coroutine A, $i")
            delay(10L) // 10ms의 delay
        }
    }

    launch {
        repeat(5) { i ->
            println("Coroutine B, $i")
        }
    }

    println("Coroutine Outer")
}
...
```

```
Coroutine Outer [main @coroutine#1]
Coroutine A, 0 [main @coroutine#2]
Coroutine B, 0 [main @coroutine#3]
Coroutine B, 1 [main @coroutine#3]
Coroutine B, 2 [main @coroutine#3]
Coroutine B, 3 [main @coroutine#3]
Coroutine B, 4 [main @coroutine#3]
Coroutine A, 1 [main @coroutine#2]
Coroutine A, 2 [main @coroutine#2]
Coroutine A, 3 [main @coroutine#2]
Coroutine A, 4 [main @coroutine#2]
```

A가 모두 출력되고 B가 출력되었던 이전의 코드와 달리 A가 한 번만 출력되고 B가 모두 출력된 다음에야 나머지 A가 출력된다. A가 한 번 출력되고 `delay()`가 실행되었을 때, A는 중단되고 B로 실행이 넘어간 것이다. B 내부에는 중단점이 없기 때문에 모두 다 호출이 되고나서야 A가 다시 실행된다.

그렇다면 A와 B가 사이좋게 오고가며 실행될 수 있도록 하려면 어떻게 해야할까? B에도 중단점을 추가, 즉 delay를 추가해주면 된다.

```kotlin
fun main() = runBlocking {
    launch {
        repeat(5) { i ->
            println("Coroutine A, $i")
            delay(10L)
        }
    }

    launch {
        repeat(5) { i ->
            println("Coroutine B, $i")
            delay(10L)
        }
    }

    println("Coroutine Outer")
}
...
```

```
Coroutine Outer [main @coroutine#1]
Coroutine A, 0 [main @coroutine#2]
Coroutine B, 0 [main @coroutine#3]
Coroutine B, 1 [main @coroutine#3]
Coroutine B, 2 [main @coroutine#3]
Coroutine B, 3 [main @coroutine#3]
Coroutine B, 4 [main @coroutine#3]
Coroutine A, 1 [main @coroutine#2]
Coroutine A, 2 [main @coroutine#2]
Coroutine A, 3 [main @coroutine#2]
Coroutine A, 4 [main @coroutine#2]
```

*Coroutine A*가 먼저 출력될 것 같은데 *Coroutine Outer*가 먼저 출력되는 것을 통해 `launch { }`가 코루틴이 실행되기 위한 스케줄링을 해놓는다는 것을 추측할 수 있다.

## 정리

코루틴은 **일시 중단이 가능한 계산의 인스턴스**다<small>(an instance of suspendable computation)</small>. 다른 코드와 동시에 작동하는 코드 블록을 실행해야 한다는 점에서 스레드와 개념적으로 유사하다. 하지만 코루틴은 특정 스레드에 바인딩되지 않는다. 한 스레드에서 실행을 일시 중지하고 다른 스레드에서 다시 시작할 수 있다.

코루틴은 경량 스레드라고 생각되어질 수 있지만, 실제 사용하는데 있어 스레드와 매우 다르게 만드는 몇 가지 중요한 차이점이 있다.

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking { // this: CoroutineScope
    launch { // launch a new coroutine and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World!") // print after delay
    }
    println("Hello") // main coroutine continues while a previous one is delayed
}
```

```
Hello
World!
```

#### `launch {}` : Main Thread 를 Unblocking 한 채 `{}` 구문 내 작업을 수행

**launch**는 **코루틴 빌더 <sup>coroutine builder</sup>** 이다. 코루틴 빌더는 새로운 코루틴을 독립적으로 작동하는 나머지 코드들과 **동시에** 실행시킨다. 이것이 *Hello*가 가장 먼저 출력되는 이유이다.

#### `delay()`

**delay**는 특별한 suspending function으로, 코루틴을 특정 시간동안 일시 중단시킨다. 코루틴을 일시 중단시켜도 메인 스레드가 Blocking 되지는 않지만, 다른 코루틴이 코드에 메인 스레드를 실행하고 사용할 수 있습니다.

#### `runBlocking {}` : Main Thread 를 Blocking 한 채 `{}` 구문 내 작업을 새 Thread 에 할당하여 수행

**runBlocking** 또한 **코루틴 빌더**로써, 코루틴이 아닌 일반적인 `fun main()` 영역과 `runBlocking {}` 괄호 안의 코루틴이 있는 코드들을 연결한다.

**launch**는 독립적으로 실행이 불가능하며 **CoroutineScope** 내에서만 선언되기 때문에, 만약 코드에서 **runBlocking**을 제거하거나 잊어버린다면 **launch**를 호출할 때 에러가 발생한다.

**runBlocking**의 이름은 `runBlocking {}` 내부의 모든 코루틴이 실행을 완료할 때까지 이를 실행하는 스레드<small>(여기선 메인 스레드)</small>가 호출되는 동안 Blocking 된다는 것을 의미한다. 스레드는 값 비싼 리소스이고 스레드를 Blocking하는 것은 비효율적이면서 프로그래머가 종종 원하지 않기 때문에 애플리케이션의 최상위 수준에서 실제 코드 내에서는 거의 사용되지 않는 **runBlocking**을 자주 볼 수 있을 것이다.


> 구조화된 동시성 <sup>Structured concurrency﻿</sup>

코루틴은 구조화된 동시성의 원칙을 따른다. 즉 새로운 코루틴은 코루틴의 수명을 제한하는 특정 CoroutineScope에서만 실행될 수 있다. 위의 예제 코드는 runBlocking이 해당 범위를 설정하며 이것이 *World!*가 1초 지연된 다음 출력되고 종료되는 이유이다.

실제 어플리케이션에서는 많은 코루틴을 실행하게 될 것이다. 구조화된 동시성은 손실이나 누수가 없는 것을 보장한다. 외부 범위는 모든 하위 코루틴이 완료될 때까지는 완료할 수 없다. 구조화된 동시성은 또한 코드 내부의 오류가 제대로 보고되고 손실되지 않는 것을 보장해준다.


### 함수를 추출하여 리팩토링하기

`launch {...}` 내부의 코드 블록을 별도의 함수로 추출해보자. 이 코드에 IDE의 *Extract Function* 기능을 수행하면 아래와 같이 `suspend` 변경자 <sup>modifier</sup>가 있는 새 함수가 생성이 된다. 일시 중단 함수 <sup>suspending function</sup>는 일반 함수와 마찬가지로 코루틴 내부에서 사용할 수 있지만 추가 기능은 다른 일시 중단 함수<small>(`delay()`와 같은)</small>를 사용하여 코루틴의 실행을 중단시킬 수 있다는 것이다.

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/120298097-ba6e2c00-c304-11eb-9271-b3e1f9efe246.png'>
</p>

```kotlin
fun main() = runBlocking { // this: CoroutineScope
    launch { doWorld() }
    println("Hello")
}

// this is your first suspending function
suspend fun doWorld() {
    delay(1000L)
    println("World!")
}
```

> 추출한 함수의 `suspend` 변경자를 제거하면?

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/120296667-5139e900-c303-11eb-8cac-7727a7e86643.png'>
</p>

추출한 함수의 `suspend` 변경자를 제거하면 *Suspend function 'delay' should be called only from a coroutine or another suspend function*라는 경고문이 뜬다. 즉, 일시 중단되는 함수인 `delay()`는 오직 코루틴이나 다른 일시 중단 함수로부터만 호출될 수 있다는 뜻이다.

## References

- Kotlin Docs, Coroutines basics : https://kotlinlang.org/docs/coroutines-basics.html
- 새차원, 코틀린 코루틴 : https://youtu.be/14AGUuh8Bp8

----

<b id = "f1"><sup> 1 </sup></b> https://en.wikipedia.org/wiki/Coroutine [ ↩](#a1)

<b id = "f1"><sup> 2 </sup></b> https://developer.android.com/kotlin/coroutines?hl=ko [ ↩](#a1)

<b id = "f1"><sup> 3 </sup></b> https://developer.android.com/codelabs/kotlin-coroutines#0 [ ↩](#a1)