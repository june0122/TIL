# 코루틴과 스레드의 비교

코루틴은 장기간 실행되는 작업을 위한 비동기 콜백을 순차적인 코드<small>(sequential code)</small>로 변환해주는 코틀린의 언어적 기능이다.

### 1. 한 스레드에서 다른 스레드로 데이터를 전달하는 것이 번거롭다.

대부분의 경우 콜백이나 알림 매커니즘을 사용하게 되고, 이는 코드의 가독성도 좋지 않다.

#### 콜백 기반의 코드

콜백을 사용하면 한 함수를 다른 함수에 파라미터로 전달하고, 프로세스가 완료되면 이 함수를 호출하는 것이다.

```kotlin
fun postItem(item: Item) {
    preparePostAsync { token ->
        submitPostAsync(token, item) { post ->
            processPost(post)
        }
    }
}

fun preparePostAsync(callback: (Token) -> Unit) {
    // make request and return immediately
    // arrange callback to be invoked later
}
```

#### 코루틴을 이용한 순차적인 코드

```kotlin
fun postItem(item: Item) {
    launch {
        val token = preparePost()
        val post = submitPost(token, item)
        processPost(post)
    }
}

suspend fun preparePost(): Token {
    // makes a request and suspends the coroutine
    return suspendCoroutine { /* ... */ }
}
```

[Codelabs](https://developer.android.com/codelabs/kotlin-coroutines#0)와 [Kotlin Docs](https://kotlinlang.org/docs/async-programming.html#coroutines)에서 콜백을 이용한 코드와 코루틴을 이용한 코드의 비교가 잘 정리되어 있으니 참고하자.

## References

- [Kotlin Coroutines - What, Why & How?](https://medium.com/microsoft-mobile-engineering/kotlin-coroutines-1c8e009cb711)
- [Use Kotlin coroutines with lifecycle-aware components](https://developer.android.com/topic/libraries/architecture/coroutines)
- https://kotlinlang.org/docs/async-programming.html#threading