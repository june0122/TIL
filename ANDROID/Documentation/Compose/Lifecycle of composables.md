# 컴포저블 수명 주기

## 수명 주기 개요

#### 컴포지션<small>(composition)</small>

- **UI를 기술하는 컴포저블의 트리 구조**
  - <small>원문에서 describe를 사용하는데 이는 설명보다는 기술(記述), 즉 '어떤 것을 기록한다'는 의미로 해석하는 것이 좋을 것 같다.</small>
- 컴포저블을 실행하여 생성되며 앱의 UI를 기술한다.
- Jetpack Compose가 컴포저블을 실행할 때 빌드한 UI에 대한 설명

Jetpack Compose는 처음으로 컴포저블을 실행할 때, 즉 초기 컴포지션<small>(initial composition)</small> 때, 컴포지션에서 UI를 기술하기 위해 호출하는 **컴포저블을 추적**한다. 앱의 상태가 변경되면 Jetpack Compose는 **리컴포지션**을 예약한다. 리컴포지션은 Jetpack Compose가 상태 변경에 따라 변경될 수 있는 컴포저블을 재실행한 다음, 변경사항을 반영하도록 컴포지션을 업데이트하는 것이다.

컴포지션은 초기 컴포지션을 통해서만 생성되고 리컴포지션을 통해서만 업데이트될 수 있다. 컴포지션을 수정하는 유일한 방법은 리컴포지션을 통하는 것이다.

> 요점 : 컴포저블의 수명 주기는 ① 컴포지션 시작, ② 0회 이상 리컴포지션, ③ 컴포지션 종료 이벤트로 정의된다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/130401469-5b2bcac3-4a39-4e43-a16a-005786817d37.png'>
</p>

리컴포지션은 일반적으로 `State<T>` 객체가 변경되면 트리거된다. Compose는 이러한 객체를 추적하고 컴포지션에서 특정 `State<T>`를 읽는 모든 컴포저블 및 호출하는 컴포저블 중 *건너뛸 수 없는* 모든 컴포저블을 실행한다.

> 참고 : 컴포저블의 수명 주기는 뷰, 활동 및 프래그먼트의 수명 주기보다 간단하다. 컴포저블이 수명 주기가 더 복잡한 외부 리소스를 관리하거나 이와 상호작용해야 하는 경우 [Effect API](https://developer.android.com/jetpack/compose/side-effects#state-effect-use-cases)를 사용해야 한다.

컴포저블이 여러 번 호출되면 컴포지션에 여러 인스턴스가 배치된다. 컴포지션의 각 호출에는 자체 수명 주기가 있다.

```kotlin
@Composable
fun MyComposable() {
    Column {
        Text("Hello")
        Text("World")
    }
}
```

> 위 코드의 컴포지션 내 `MyComposable`의 표현

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/130402716-3f66abc7-9c33-418e-848c-158f7e9d6c71.png'>
</p>

컴포저블이 여러 번 호출되면 컴포지션에 여러 인스턴스가 배치된다. 색상이 다른 요소는 해당 요소가 별도의 인스턴스임을 나타낸다.

## 컴포지션 내 컴포저블의 분석

컴포지션 내 컴포저블의 인스턴스는 <b>호출 사이트<small>(call site)</small></b>로 식별된다. Compose 컴파일러는 각 호출 사이트를 고유한 것으로 간주한다. 여러 호출 사이트에서 컴포저블을 호출하면 컴포지션에 컴포저블의 여러 인스턴스가 생성된다.

> 핵심 용어: 호출 사이트는 컴포저블이 호출되는 *소스 코드 위치*다. 호출 사이트는 컴포지션 내부에서의 위치에 영향을 미치므로 UI 트리에도 영향을 미친다.

리컴포지션 시 컴포저블이 이전 컴포지션 시 호출한 것과 다른 컴포저블을 호출하는 경우, Compose는 **호출되거나 호출되지 않은 컴포저블을 식별하며** 두 컴포지션 모두에서 호출된 컴포저블의 경우 **입력이 변경되지 않은 경우 재구성하지 않는다.**

부수 효과를 컴포저블과 연결하기 위해서는 리컴포지션마다 다시 시작하는 대신 완료할 수 있도록 ID를 유지하는 것이 중요하다.

다음 예를 참고하자.

```kotlin
@Composable
fun LoginScreen(showError: Boolean) {
    if (showError) {
        LoginError()
    }
    LoginInput() // 호출 사이트는 LoginInput이 컴포지션 내부에서 배치되는 위치에 영향을 준다
}

@Composable
fun LoginInput() { /* ... */ }
```

위의 코드에서 `LoginScreen`은 `LoginError` 컴포저블을 조건부로 호출하며 항상 `LoginInput` 컴포저블을 호출한다. 각 호출에는 고유한 호출 사이트 및 컴파일러가 호출을 고유하게 식별하는 데 사용하는 소스 위치가 있다.

> `showError` 플래그가 `true`로 변경된 경우 앞의 코드가 재구성되는 방식을 보여주는 다이어그램. `LoginError` 컴포저블이 추가되지만 다른 컴포저블은 재구성되지 않는다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/130406707-f05431af-f4f6-4b0c-aa9e-a62da32daf94.png'>
</p>

위 그림은 상태가 변경되고 리컴포지션이 발생할 때 컴포지션 내 LoginScreen의 표현을 담고 있으며, 색상이 동일하면 재구성되지 않았음을 의미한다.

`LoginInput`이 첫 번째로 호출되었다가 두 번째로 호출되었지만 `LoginInput` 인스턴스는 여러 리컴포지션에 걸쳐 유지된다. 또한 `LoginInput`에는 리컴포지션 간에 변경된 매개변수가 없으므로 Compose가 `LoginInput` 호출을 건너뛴다.

### 스마트 리컴포지션에 도움이 되는 정보 추가

컴포저블을 여러 번 호출하면 컴포저블이 컴포지션에도 여러 번 추가된다. 동일한 호출 사이트에서 컴포저블을 여러 번 호출하는 경우, Compose가 각 컴포저블 호출을 고유하게 식별할 수 있는 정보가 없으므로 인스턴스를 구분하기 위해 호출 사이트 외에 실행 순서가 사용된다. 이 동작만 필요한 경우도 있지만, 경우에 따라 원치 않는 동작이 발생할 수도 있다.

```kotlin
@Composable
fun MoviesScreen(movies: List<Movie>) {
    Column {
        for (movie in movies) {
            // MovieOverview 컴포저블은 for loop의 인덱스 위치가 지정된 컴포지션에 배치된다
            MovieOverview(movie)
        }
    }
}
```

위의 예에서 Compose는 호출 사이트 외에 실행 순서를 사용하여 컴포지션에서 인스턴스를 구분한다. 새 `movie`가 리스트의 하단에 추가된 경우 Compose는 인스턴스의 리스트 내 위치가 변경되지 않았고 따라서 인스턴스의 `movie` 입력이 동일하므로 컴포지션에 이미 있는 인스턴스를 재사용할 수 있다.

> 리스트의 하단에 새 요소가 추가된 경우의 컴포지션 내 `MoviesScreen`의 표현. 이때 컴포지션의 `MovieOverview` 컴포저블은 재사용할 수 있다. `MovieOverview`의 색상이 동일하면 컴포저블이 재구성되지 않았음을 의미한다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/130410236-3eb502b3-0b48-4145-9330-c0d62d3a5c0d.png'>
</p>

하지만 리스트의 상단 또는 가운데에 항목을 추가하거나, 항목을 삭제하거나 재정렬하여 `movies` 리스트가 변경되면 리스트에서 입력 매개변수의 위치가 변경된 모든 `MovieOverview` 호출에서 리컴포지션이 발생한다. 이는 예를 들어 `MovieOverview`가 부수 효과를 사용하여 영화 이미지를 가져오는 경우 매우 중요하다. 효과가 적용되는 동안 리컴포지션이 발생하면 효과가 취소되고 다시 시작된다.

```kotlin
@Composable
fun MovieOverview(movie: Movie) {
    Column {
        // 부수 작용은 본문의 뒷부분에서 설명한다.
        // MovieOverview가 재구성하면
        // 이미지 가져오기가 진행되는 동안 취소되고 다시 시작된다.
        val image = loadNetworkImage(movie.url)
        MovieHeader(image)

        /* ... */
    }
}
```

> 리스트 상단에 새 요소가 추가될 때의 컴포지션 내 `MoviesScreen`의 표현. `MovieOverview` 컴포저블은 재사용할 수 없으며 모든 부수 효과가 다시 시작된다. `MovieOverview`의 색상이 다르면 컴포저블이 재구성되었음을 의미한다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/130410373-76719b6f-5f3b-4487-bb86-f4c5408c6fda.png'>
</p>

이상적으로 `MovieOverview` 인스턴스의 ID는 인스턴스에 전달된 `movie`의 ID에 연결된 것으로 간주된다. 영화 리스트를 재정렬하는 경우 다른 영화 인스턴스로 각 `MovieOverview` 컴포저블을 재구성하는 대신 컴포지션 트리 내 인스턴스를 재정렬하는 것이 이상적이다. Compose는 트리의 특정 부분을 식별하는데 사용할 값을 런타임에 알릴 수 있는 방법인 [`key`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary#key(kotlin.Array,kotlin.Function0)) 컴포저블을 제공한다.

`key` 컴포저블 호출로 코드 블록을 래핑하고 하나 이상의 값을 전달하면, 이러한 값이 컴포지션에서 인스턴스를 식별하는 데 함께 사용된다. `key` 값은 <i>전역적으로<small>(globally)</small></i> 고유하지 않아도 되며, 호출 사이트에서의 컴포저블 호출 간에만 고유하면 된다. 따라서 이 예에서 각 `movie`에는 `movies` 사이에 고유한 `key`가 있어야 한다. 앱의 다른 위치에 있는 다른 컴포저블과 이 `key`를 공유하는 것은 괜찮다.

```kotlin
@Composable
fun MoviesScreen(movies: List<Movie>) {
    Column {
        for (movie in movies) {
            key(movie.id) { // 이 movie의 고유한 ID
                MovieOverview(movie)
            }
        }
    }
}
```

위에서 리스트의 요소가 변경되더라도, Compose는 개별 `MovieOverview` 호출을 인식하고 재사용할 수 있다.

> 리스트에 새 요소가 추가될 때 컴포지션 내 `MoviesScreen`의 표현. `MovieOverview` 컴포저블에는 고유 키가 있으므로 Compose가 변경되지 않은 `MovieOverview` 인스턴스를 인식하고 재사용할 수 있다. 인스턴스의 부수 효과는 계속 실행된다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/130410492-72e043e3-5136-404b-814c-17b7222a88d8.png'>
</p>

> 요점 : `key` 컴포저블을 사용하면 Compose가 컴포지션에서 컴포저블 인스턴스를 식별할 수 있다. 이 기능은 여러 컴포저블이 동일한 호출 사이트에서 호출되고 부수 효과 또는 내부 상태가 포함되어 있을 때 중요하다.

일부 컴포저블에는 `key` 컴포저블 지원 기능이 내장되어 있다. 예를 들어 `LazyColumn`의 경우 `items` DSL에 맞춤 `key`를 지정할 수 있다.

```kotlin
@Composable
fun MoviesScreen(movies: List<Movie>) {
    LazyColumn {
        items(movies, key = { movie -> movie.id }) { movie ->
            MovieOverview(movie)
        }
    }
}
```

### 입력이 변경되지 않은 경우 건너뛰기

컴포지션에 이미 컴포저블이 있는 경우, 모든 입력이 안정적이고 변경되지 않았으면 리컴포지션을 건너뛸 수 있다.

안정적인 타입<small>(stable type)</small>은 다음 계약을 준수해야 한다.

- `equals` 결과가 동일한 두 인스턴스의 경우 항상 동일하다.
- 타입의 공개 속성이 변경되면 컴포지션에 알림이 전송된다.
- 모든 공개 속성 타입도 안정적이다.

`@Stable` 주석을 사용하여 안정적이라고 명시되지 않더라도 Compose 컴파일러가 안정적인 것으로 간주하며 이 계약에 포함되는 중요한 일반 타입이 있다.

- 모든 원시 값 유형: `Boolean`, `Int`, `Long`, `Float`, `Char` 등
- 문자열
- 모든 함수 유형(람다)

이 타입들은 모두 변경할 수 없으므로 stable 계약을 준수할 수 있다. 변경할 수 없는 타입은 절대 변경되지 않으므로 Composition에 변경사항을 알리지 않아도 되며 따라서 이 
계약을 훨씬 더 쉽게 준수할 수 있다.

> 참고 : 완전히 변경 불가능한 모든 타입은 안전하게 안정적인 타입으로 간주할 수 있습니다.

안정적이지만 변경할 수 있는 한 가지 중요한 타입은 Compose의 `MutableState` 타입이다. 값이 `MutableState`로 유지되는 경우, `State`의 `.value` 속성이 변경되면 Compose에 알림이 전송되므로 상태 객체는 전체적으로 안정적인 것으로 간주된다.

컴포저블에 매개변수로 전달된 모든 타입이 안정적인 경우, UI 트리 내 컴포저블 위치를 기반으로 매개변수 값이 동일한지 비교한다. 이전 호출 이후 모든 값이 변경되지 않은 경우 리컴포지션을 건너뛴다.

> 요점 : 모든 입력이 안정적이고 변경되지 않은 경우 Compose는 컴포저블의 리컴포지션을 건너뛴다. 비교에서는 `equals` 메서드를 사용한다.

Compose는 증명할 수 있는 경우에만 타입을 안정적인 것으로 간주한다. 예를 들어 인터페이스는 일반적으로 안정적이지 않은 것으로 간주되며, 구현이 변경 불가능한 변경할 수 있는 공개 속성이 있는 타입도 안정적이지 않다<small>(types with mutable public properties whose implementation could be immutable are not stable either.)</small>.

Compose가 타입이 안정적이라고 추론할 수 없지만 안정적인 것으로 간주하도록 하려면 [`@Stable`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/Stable) 어노테이션으로 표시한다.

```kotlin
// 타입을 stable로 표시하여 건너뛰기 및 스마트 리컴포지션을 선호하게 한다.
@Stable
interface UiState<T : Result<T>> {
    val value: T?
    val exception: Throwable?

    val hasError: Boolean
        get() = exception != null
}
```

위의 코드에서는 `UiState`가 인터페이스이므로 Compose가 일반적으로 이 타입을 안정적이지 않은 것으로 간주할 수 있다. `@Stable` 어노테이션을 추가하면 Compose가 이 타입이 안정적임을 알게 되고 스마트 리컴포지션을 선호하게 된다. 즉, 인터페이스가 매개변수 유형으로 사용되는 경우 Compose가 모든 구현을 안정적인 것으로 간주함을 뜻한다.

> 요점 : Compose가 타입의 안정성을 추론할 수 없다면 Compose가 스마트 재구성을 선호하도록 타입에 `@Stable` 어노테이션을 단다.

## References

- Android developers : [Lifecycle of composables](https://developer.android.com/jetpack/compose/lifecycle#skipping)