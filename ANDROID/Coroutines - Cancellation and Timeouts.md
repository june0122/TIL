# [Coroutines - Cancellation and Timeouts](https://kotlinlang.org/docs/cancellation-and-timeouts.html#making-computation-code-cancellable)

> ### ① <b id = "f1">Cancelling coroutine execution</b>  [ ↩](#a1)

```kotlin
fun main() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancel() // cancels the job 
    job.join() // waits for job's completion
    println("main: Now I can quit.")
}
```

```
job: I'm sleeping 0 ... [main]
job: I'm sleeping 1 ... [main]
job: I'm sleeping 2 ... [main]
main: I'm tired of waiting! [main]
main: Now I can quit. [main]
```

코루틴을 실행할 떄 launch를 해서 반환된 Job 객체에서 `cancel()`을 호출할 수 있고, 이를 통해 코루틴 실행을 취소시킬 수 있다.

> ### ② Cancellation is cooperative

```kotlin
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // computation loop, just wastes CPU
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")    
}
```

```
job: I'm sleeping 0 ... [DefaultDispatcher-worker-1]
job: I'm sleeping 1 ... [DefaultDispatcher-worker-1]
job: I'm sleeping 2 ... [DefaultDispatcher-worker-1]
main: I'm tired of waiting! [main]
job: I'm sleeping 3 ... [DefaultDispatcher-worker-1]
job: I'm sleeping 4 ... [DefaultDispatcher-worker-1]
main: Now I can quit. [main]
```

코드의 의도는 코루틴이 3번정도 실행되고 취소되는 것을 기대했을 것이다. 하지만 `main: I'm tired of waiting!`이 호출된 뒤 코루틴의 실행은 취소되지 않고 5번의 횟수를 채운 뒤 종료된다.

왜 이런 것일까? 이는 코루틴 자체가 취소되는데 협조적 <sup>cooperative</sup>이지 않았기 때문이다. 코루틴 내부에 suspend function이 존재하지 않는 것과 달리, <a id = "a1">[첫 번째 예제](#f1)</a>에서는 코루틴 내부에 `delay()`라는 suspend function이 존재하므로 취소가 가능했다.

다시 말해, 첫 번째 예제에서는 suspend function이 있었는데 두 번째 예제에서는 단순 연산만 있고 suspend function의 호출이 코루틴 내부에 없기 때문에 취소가 불가능하다는 것이다. 그럼 첫 번째 예제와 같이 두 번째 예제에도 suspend function을 호출하여 취소에 협조적인 코루틴을 만들어보자. 과연 원하는 실행 결과가 나올까?

```kotlin
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                delay(1L) // suspend function을 추가
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}
```

```
job: I'm sleeping 0 ... [DefaultDispatcher-worker-1]
job: I'm sleeping 1 ... [DefaultDispatcher-worker-1]
job: I'm sleeping 2 ... [DefaultDispatcher-worker-1]
main: I'm tired of waiting! [main]
main: Now I can quit. [main]
```

실행을 해보면 의도했던대로 0, 1, 2 까지만 출력이되고 종료가 된 것을 확인할 수 있다.

suspend function으로써 이런 상황에 `delay()`가 아닌 더 적합한 함수가 있다. 바로 `yield()` 이다. `yield()`를 이용하면 `delay(1L)`을 이용하지 않고도 취소를 확인할 수 있다. 결과는 위와 동일하다.

```kotlin
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                yield() // delay 대신 yield 사용
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}
```

어떻게 해서 cancle이 일어나게 되었을까?

코루틴 가이드에 따르면 `Job.cancel`을 하게 되면 코루틴 내부에서 suspend가 되었다가 다시 재개<small>(resume)</small>되는 시점에 suspend function<small>(여기서는 `yield()`)</small>이 CancellationException을 던진다고 설명되어 있다. Exception을 체크하기 위해 코루틴 내부에서 `try-catch` 문을 사용해보자.

```kotlin
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        try {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) { // computation loop, just wastes CPU
                // print a message twice a second
                if (System.currentTimeMillis() >= nextPrintTime) {
                    yield()
                    kotlin.io.println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        } catch (e: Exception) {
            kotlin.io.println("Exception [$e]")
        }
    }
    delay(1300L) // delay a bit
    kotlin.io.println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    kotlin.io.println("main: Now I can quit.")
}
```

```text
job: I'm sleeping 0 ...
job: I'm sleeping 1 ...
job: I'm sleeping 2 ...
main: I'm tired of waiting!
Exception [kotlinx.coroutines.JobCancellationException: StandaloneCoroutine was cancelled; job=StandaloneCoroutine{Cancelling}@54c334f5]
main: Now I can quit.
```

결과를 확인해보면 3번 출력한 뒤 Exception이 출력되었고 그 내용이 *kotlinx.coroutines.JobCancellationException*이라는 것을 확인할 수 있다. 즉 `Job.cancel`이 동작하는 방법은 코루틴 내부에서 강제로 Exception을 발생시켜 코루틴이 종료되게 하는 것이다. 

**Coroutine cancellation is cooperative. A coroutine code has to cooperate to be cancellable.** 코루틴 문서의 협조적이어야 한다는 말은 코루틴 스스로가 cancel을 체크<small>(CancellationException을 체크)</small>해야한다는 뜻이며 suspend function을 하나라도 실행하지 않으면 코루틴은 종료되지 않는다는 것이다<small>(자신의 로직이 완료되어 리턴이 되지 않는 한에서)</small>.

> ### ③ Making computation code cancellable

코루틴이 취소되기 위해서 협조적인 방식을 취하는데 크게 2가지 방법이 있다.

1. **주기적으로 suspend function을 호출하기** <small>(suspend 되었다가 다시 재개될 때 cancel 되었는지를 확인해서 Exception을 던져주는 방식)</small>
2. **명시적으로 취소 상태를 확인하기** <small>(`isActive`라는 상태를 확인하여 false일 때 해당 코루틴을 종료시키는 방식)</small>

2번 예제에서 첫 번째 방법을 알아보았고, 이번 3번 예제에서 두 번째 방법을 알아본다.

```kotlin
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) { // cancellable computation loop
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}
```

```
job: I'm sleeping 0 ... [DefaultDispatcher-worker-1]
job: I'm sleeping 1 ... [DefaultDispatcher-worker-1]
job: I'm sleeping 2 ... [DefaultDispatcher-worker-1]
main: I'm tired of waiting! [main]
main: Now I can quit. [main]
```

while문을 통해 isActive의 상태를 확인하는 것으로 코루틴이 취소된 것을 확인할 수 있다. `isActive`가 실제로 잘 동작하였는지 로그를 통해 확인해보자.

```kotlin
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        kotlin.io.println("isActive $isActive ...") // while문에 들어가기 전의 isActive 확인
        while (isActive) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
        kotlin.io.println("isActive $isActive ...") // while문에서 나온 뒤의 isActive 확인
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}
```

```
isActive true ...
job: I'm sleeping 0 ... [DefaultDispatcher-worker-1]
job: I'm sleeping 1 ... [DefaultDispatcher-worker-1]
job: I'm sleeping 2 ... [DefaultDispatcher-worker-1]
main: I'm tired of waiting! [main]
isActive false ...
main: Now I can quit. [main]
```

출력 결과에서 while문에 들어가기 전과 후에 `isActive`의 상태값이 변경되는 것을 확인할 수 있다. 이러한 상태값을 통해 코루틴 스스로가 종료할 수 있다.

상태값을 통해 코루틴을 종료시키는 방식은 Exception이 발생하지 않으므로 코루틴 내부를 2번 예제와 같이 코루틴 내부를 `try-catch`문으로 감싸 예외를 확인해보면 Exception이 발생하지 않는 것을 확인할 수 있다.

`isActive`는 **확장 프로퍼티**로 내부 구현은 코루틴의 Job이 실제로 종료되었는지를 체크하는 것이다.

```kotlin
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public val CoroutineScope.isActive: Boolean
    get() = coroutineContext[Job]?.isActive ?: true
```

> ### ④ Closing resources with finally <small>(코루틴을 종료할 때 리소스를 해제하는 방법)</small>

코루틴에서 네트워크나 DB 등을 사용하다가 갑자기 코루틴이 cancel 되었을 때 코드 상에서 리소스를 해제해줘야 하는 위치를 알아보자.

아래의 코루틴은 `delay()`를 포함하여 취소하기에 협조적인 형태로 구현되어 있기에 `job.cancel`을 하면 취소가 될 것이다. 일시 중단이 되었다가 재개되면서 Exception을 던지면 `finally` 블록에서 리소스를 해제하면 된다.

**즉, suspend function으로 취소를 체크할 때 리소스 해제 지역은 `finally` 블록이다.**

`try {...} finally {...}`

```kotlin
fun main() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            // 리소스 해제 위치
            println("job: I'm running finally")
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")    
}
``` 

```
job: I'm sleeping 0 ... [main]
job: I'm sleeping 1 ... [main]
job: I'm sleeping 2 ... [main]
main: I'm tired of waiting! [main]
job: I'm running finally [main]
main: Now I can quit. [main]
```

> ### ⑤ Run non-cancellable block

5번 예제는 **rare한 케이스**이다.

이미 cancel된 코루틴 내부에서 suspend function을 호출해야하는 경우, 즉 `finally` 블록 안에서 다시 코루틴을 실행하려면 어떻게 해야할까?

이런 경우에는 `withContext` 함수에 `NonCancellable`이라는 CoroutineContext를 넘겨서 `withContext(NonCancellable) { ... }`에서 해당 코드를 래핑할 수 있다.

```kotlin
fun main() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            withContext(NonCancellable) {
                println("job: I'm running finally")
                delay(1000L)
                println("job: And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}
```

```
job: I'm sleeping 0 ... [main]
job: I'm sleeping 1 ... [main]
job: I'm sleeping 2 ... [main]
main: I'm tired of waiting! [main]
job: I'm running finally [main]
job: And I've just delayed for 1 sec because I'm non-cancellable [main]
main: Now I can quit. [main]
```

> ### ⑥ Timeout

이전 예제들에서는 코루틴 스스로가 내부에서 cancel을 체크하는 2가지 방법을 알아보았다. 그런데 그외에도 다른 방법이 있는데 바로 **Timeout**이다.

**Timeout**은 launch된 코루틴의 Job을 가지고 cancel하는 것이 아니라, 코루틴을 실행할 때 일정 시간이 지나면 취소되도록 미리 Timeout을 지정하는 방식이다.

```kotlin
fun main() = runBlocking {
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}
```

```
I'm sleeping 0 ... [main]
I'm sleeping 1 ... [main]
I'm sleeping 2 ... [main]
Exception in thread "main" kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1300 ms
	at kotlinx.coroutines.TimeoutKt.TimeoutCancellationException(Timeout.kt:186)
	at kotlinx.coroutines.TimeoutCoroutine.run(Timeout.kt:156)
	at kotlinx.coroutines.EventLoopImplBase$DelayedRunnableTask.run(EventLoop.common.kt:497)
	at kotlinx.coroutines.EventLoopImplBase.processNextEvent(EventLoop.common.kt:274)
	at kotlinx.coroutines.DefaultExecutor.run(DefaultExecutor.kt:69)
	at java.base/java.lang.Thread.run(Thread.java:834)
```

lauch를 통해 실행시킨 것이 아닌, runBlocking 내부 main에서 실행했기 때문에 CancellationException 발생하여 종료되는 것이다. 이런 경우를 해결하기 위해 `withTimeoutOrNull(Long)`을 사용한다.

`withTimeout(Long)`이 아닌 `withTimeoutOrNull(Long)`을 사용하면 Exception이 발생했을 때 결과값이 `null`이 된다.

```kotlin
fun main() = runBlocking {
    val result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
        "Done" // will get cancelled before it produces this result
    }
    println("Result is $result")
}
```

```
I'm sleeping 0 ... [main]
I'm sleeping 1 ... [main]
I'm sleeping 2 ... [main]
Result is null [main]

```