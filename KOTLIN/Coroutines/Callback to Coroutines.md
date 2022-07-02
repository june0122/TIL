# 콜백 스타일의 코드를 순차적 프로그래밍으로 변경해주는 코루틴

> ### Coroutines: Replace callbacks & Main safety

코루틴은 콜백 스타일의 코드를 순차적 프로그래밍으로 변경해주고 안드로이드에서 우리의 코드를 *main safe*하게 만들어준다.

*main safe*하다는 것은 예시로 네트워크 요청을 만드는 코루틴 기반의 함수를 작성할 수 있게 해주고, 그냥 메인 스레드에서 호출해도 아무런 문제가 없다는 것을 뜻한다. 어떻게 코루틴이 이것을 가능하게 해주는지 네트워크 요청을 생성하기 위한 예제 코드를 통해 알아보자.

### Blocking 스타일 코드

메인 스레드에서 `result`를 `networkRequest()`부터 직접 리턴받는 블로킹<small>(blocking)</small> 스타일로 작성한 코드를 보자.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/176938752-a6b472f5-e38d-4db9-acf3-8e91f150def9.png'>
</p>

위의 코드에서 `networkRequest()`는 메인 스레드를 블로킹할 것이다. 이것은 `networkRequest()`를 완료하는데 몇 초 또는 몇 분이 걸릴 수 있고, `networkRequest()`가 실행되는 시간동안 메인 스레드가 업데이트되지 않고 터치에 대해 반응하지 않아 사용자에게는 앱이 멈춰있는 것처럼 보일 것이다. 이것은 사용자에게 매우 좋지 않은 경험이기에 개발자는 안드로이드에서 이러한 상황이 발생하기를 원치 않을 것이다.

### Callback 스타일 코드

같은 함수를 콜백을 통해 작성하면 메인 스레드를 블로킹하지 않을 것이다. 다른 스레드에서 `networkRequest()` 실행을 처리하고 데이터가 준비되면 데이터를 전달하거나 메인 스레드로 다시 호출<small>(callback to the main thread)</small>한다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/176938847-f4502a5e-c857-488e-a4e4-aeadb8a3cac5.png'>
</p>

콜백은 비동기 프로그래밍을 위한 훌륭한 방법이지만 몇가지 문제점이 있다.

- 추가 작업 없이는 에러를 처리하지 못한다.
- 동일한 함수 내에서 너무 많은 콜백이 사용되면 가독성이 나쁘다.

이러한 문제점들을 해결하기 위해 코루틴을 사용할 수 있다.

### Coroutines 코드

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/176938931-7e6bfed3-dfa4-4637-86c8-31790d29e534.png'>
</p>

Blocking 스타일의 코드를 작성했을 때와 똑같아 보이지만 코루틴은 ***suspend*** 함수를 사용하기 때문에 `networkRequest()`가 호출되었을 때 메인 스레드를 블로킹하는 대신 코루틴을 일시 중단<small>(suspend)</small>한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176946793-c55ca522-cb90-4110-adb3-8f80b695edd1.png'>
</p>

이것은 메인 스레드가 UI의 `onDraw()` 메서드와 사용자 터치를 처리할 수 있도록 해준다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176946846-3dd8b2da-e14b-41ad-9947-e235cf3b64ce.png'>
</p>

`result`가 준비되면 다시 전달된다. 콜백을 사용하는 대신 코루틴은 resume될 것이다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176946884-ccb5510e-ea73-4ec5-ba57-25d3bba53d43.png'>
</p>

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176947875-cb452655-e21a-445c-84b6-79549fd3d662.png'>
</p>

코루틴을 사용하면 네트워킹 라이브러리가 네트워크 요청을 실행하기 위해 여전히 다른 스레드를 사용한다는 것에 주목해야 한다. 주요한 차이점은 콜백 버전보다 코드가 간단해지는 경행이 있는 것이다.

suspend 및 resume을 콜백의 대체로 생각할 수 있는데, `networkRequest()`를 호출할 때 ***suspend***는 함수의 나머지 부분들로부터 콜백을 만든다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/176947397-d70c99b1-27a7-4a62-967d-d582777c7475.gif'>
</p>

## `suspend` 함수는 어떻게 실행될까?

```kotlin
suspend fun onDataNeeded() {
    val result = networkRequest()
    show(result)
}

suspend fun networkRequest() {
    ...
}
```

코틀린이 `onDataNeeded()`를 실행하면 일반 함수에서의 [호출 스택<small>(call stack)</small>](https://en.wikipedia.org/wiki/Call_stack)과 약간 다르게 실행된다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176984533-cb41d91c-5cca-4490-bb56-c760c3f96895.png'>
</p>

코루틴을 시작하는 위치를 추적하여 코루틴이 suspend 및 resume을 구현할 수 있도록 해주고, 이를 통해 일반 함수처럼 호출할 수 있게 된다. ***suspend*** 함수는 일반 함수처럼 `onDataNeeded()`를 호출하고, 또다른 ***suspend*** 함수를 만나기 전까지 각 라인의 코드를 실행한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176984823-97db340a-2d90-494b-8fc9-26afe70309f8.gif'>
</p>

다른 ***suspend*** 함수를 호출하기 전, 나중에 다시 실행<small>(resume)</small>하기 위해 현재 상태를 복사한다. 이것이 코틀린이 ***suspend***를 구현하는 방법이다.

이러한 상태를 현재 함수를 어떻게 다시 실행할 지 아는 콜백으로 생각할 수 있다<small>(You can think of this state as a callback that knows how to resume the current function)</small>.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176984943-98273291-f264-43e2-8148-a99037754c66.gif'>
</p>

이때 코틀린은 network call을 생성하기 위해 `networkRequest()` 스스로가 suspend할 때까지만 `networkRequest()`를 실행한다. (Kotlin will run `networkRequest()` until it suspends itself to make the network call.)

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176985250-2a3a4671-8447-406d-a1ac-20cebde6f9a3.gif'>
</p>

이 시점에 모든 메인 스레드의 코루틴들은 suspend 되고, 메인 스레드는 UI 이벤트를 처리할 수 있게 된다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176985179-b0b48f8a-6dad-4e5e-88d4-77cc68583ab8.gif'>
</p>

`networkRequest()`가 돌아오면<small>(comes back)</small> 코틀린은 이 코루틴을 resume한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176985346-81e27209-8819-4038-8360-ee3fbe638316.gif'>
</p>

먼저 앞서 저장했었던 상태를 복구<small>(restoring)</small>해서 `networkRequest()`를 resume하고, `networkRequest()`가 요청 처리를 완료하면 `onDataNeeded()`로 돌아가고<small>(return)</small> `result`를 즉시 사용할 수 있다. 만약 `networkRequest()`가 실패한다면 대신 예외를 던진다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176985562-3a758815-4daf-4b98-9e61-259414ff849b.gif'>
</p>

`networkRequest()`가 요청 처리가 완료되어 `result`를 사용할 수 있게 되면 코루틴은 일반 함수처럼 실행을 이어나간다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/176985558-1c201c3e-8c61-47a4-9055-61acf3328fee.gif'>
</p>

코루틴은 장기 실행 작업<small>(long running task)</small>을 콜백을 사용하지 않고 처리할 수 있도록 해줌으로써 더 간단하고 읽기 쉬운 코드를 만들어준다.

## References

- Kotlin Coroutines codelab: https://youtu.be/ne6CD1ZhAI0