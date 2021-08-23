# Jetpack Compose 기초 - Compose 이해

Jetpack Compose
- Android를 위한 현대적인 선언형 UI 도구 키트
- Compose는 프론트엔드 뷰를 명령형으로 변형하지 않고도 앱 UI를 렌더링할 수 있게 하는 <b>선언형 API<small>(declarative API)</small></b>를 제공 -> 앱 UI를 더 쉽게 작성하고 유지관리할 수 있도록 지원

## 선언형 프로그래밍 패러다임

지금까지 Android 뷰 계층 구조는 UI 위젯의 트리로 표시해옴. 사용자 상호작용 등의 이유로 인해 앱의 상태가 변경되면, 현재 데이터를 표시하기 위해 UI 계층 구조를 업데이트해야 함
- UI를 업데이트하는 가장 일반적인 방법 : `findViewById()`와 같은 함수를 사용하여 트리를 탐색하고 `button.setText(String)`, `container.addChild(View)` 또는 `img.setImageBitmap(Bitmap)`과 같은 메서드를 호출하여 노드를 변경. 이러한 메서드는 위젯의 내부 상태를 변경한다.

뷰를 수동으로 조작하면 오류 발생 가능성이 커진다. 일반적으로 업데이트에 필요한 뷰의 수가 많을수록 유지보수 복잡성이 증가한다.
- 데이터를 여러 위치에서 렌더링하면 데이터를 표시하는 뷰 중 하나의 업데이트를 누락할 수도 있음
- 두 업데이트가 충돌하여 잘못된 상태를 야기할 수도 있음

Compose는 **선언형 UI 프레임워크**로, 지난 몇 년 간 업계 전반으로 선언형 UI 모델로 전환하기 시작 -> 사용자 인터페이스 빌드 및 업데이트와 관련된 엔지니어링이 크게 간소화됨.
- 이 기법은 처음부터 **화면 전체를 개념적으로 재생성**한 후 **필요한 변경사항만 적용**하는 방식으로 작동
- 이러한 접근 방식은 상태를 가진<small>(Stateful)</small> 뷰 계층 구조를 수동으로 업데이트할 때의 복잡성을 방지할 수 있음

선언형 UI 모델에서 **화면 전체를 재생성**하는 데 있어 한 가지 문제는 시간, 컴퓨팅 성능 및 배터리 사용량 측면에서 잠재적으로 **비용이 많이 든다**는 것
- 이 비용을 줄이기 위해, Compose는 특정 시점에 UI의 어떤 부분을 다시 그려야 하는지를 지능적으로 선택함
- 이는 재구성<small>(Recomposition)</small>에 설명된 대로 UI 구성요소를 디자인하는 방식에 몇 가지 영향을 미친다.

## 간단한 컴포저블 함수

Compose를 사용하면, 데이터를 받아서 UI 요소를 내보내는 컴포저블 함수들을 정의해서 사용자 인터페이스를 빌드할 수 있다.

> `String`을 받아서 인사말 메시지를 표시하는 `Text` 위젯을 내보내는 `Greeting` 컴포저블 함수

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128242024-9ca855c3-f6f3-420b-9493-c65908c15522.png'>
</p>

`Greeting` 함수에 관해 주목할 만한 참고 사항들

- 이 함수는 `@Composable` 애노테이션으로 애노테이션이 지정된다. 모든 컴포저블 함수에는 이 애노테이션이 있어야 한다. 이 애노테이션은 이 함수가 **데이터를 UI로 변환하기 위한 함수라는 것을 Compose 컴파일러에 알린다.**
- 이 함수는 데이터를 받는다. 컴포저블 함수는 매개변수를 받을 수 있으며 이 매개변수를 통해 앱 로직이 UI를 형성<small>(describe)</small>할 수 있다.
- 이 함수는 UI에 텍스트를 표시한다. 이를 위해 실제로 텍스트 UI 요소를 생성하는 `Text()` 컴포저블 함수를 호출한다. 컴포저블 함수는 다른 컴포저블 함수를 호출하여 UI 계층 구조를 내보낸다<small>(emit)</small>.
- 이 함수는 아무것도 반환하지 않는다. UI를 내보내는 Compose 함수는 UI 위젯을 구성하는 대신 원하는 화면 상태를 설명하므로 아무것도 반환할 필요가 없다.
- 이 함수는 빠르고, [멱등성<small>(idempotent)</small>](https://june0122.github.io/2021/08/05/term-idempotent/)이며 부수효과<small>(side-effect)</small>가 없다.
  - 이 함수는 동일한 인수로 여러 번 호출될 때 동일한 방식으로 작동하며, 전역 변수 또는 `random()` 호출과 같은 다른 값을 사용하지 않는다.
  - 이 함수는 속성 또는 전역 변수 수정과 같은 부수효과 없이 UI를 형성한다.

일반적으로 모든 컴포저블 함수는 재구성에서 설명한 이유로 인해 이러한 속성을 사용하여 작성해야 한다.

## 선언형 패러다임 전환

많은 명령형 객체 지향<small>(imperative object-oriented)</small> UI 툴킷을 사용하여 위젯의 트리를 인스턴스화함으로써 UI를 초기화 한다.
- 흔히 XML 레이아웃 파일을 인플레이팅해서 이 작업을 한다.
- 각 위젯은 자체의 내부 상태를 유지하고 앱 로직이 위젯과 상호작용할 수 있도록 하는 getter와 setter 메서드를 노출한다.

Compose의 선언형 접근 방식에서 위젯은 비교적 상태를 가지고 있지 않으며<small>(stateless)</small> setter 또는 getter 함수를 노출하지 않는다. 사실상 **위젯은 객체로 노출되지 않는다. 동일한 컴포저블 함수를 다른 인수로 호출하여 UI를 업데이트한다.** 이렇게 하면 [앱 아키텍처 가이드](https://developer.android.com/jetpack/guide)에 설명된 대로 `ViewModel`과 같은 아키텍처 패턴에 상태를 쉽게 제공할 수 있다. 그런 다음 컴포저블은 observable data가 업데이트될 때마다 현재 애플리케이션 상태를 UI로 변환한다.

> 앱 로직은 최상위의 컴포저블 함수에 데이터를 제공한다. 그러면 함수는 데이터를 사용하여 다른 컴포저블을 호출함으로써 UI를 형성하고 적절한 데이터를 해당 컴포저블 및 계층 구조 아래로 전달한다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128247989-65872489-411e-4834-be98-4c56bcaf9f2d.png'>
</p>

사용자가 UI와 상호작용할 때, UI는 `onClick`과 같은 이벤트를 발생시킨다. 이러한 이벤트를 앱 로직에 알려서 앱의 상태를 변경해야 한다. 상태가 변경되면 컴포저블 함수는 새 데이터와 함께 다시 호출된다. 이렇게 하면 UI 요소가 다시 그려진다. 이 프로세스를 <b>재구성<small>(recomposition)</small></b>이라 한다.

> 사용자가 UI 요소와 상호작용하며 이에 따라 이벤트가 트리거된다. 앱 로직이 이벤트에 응답하면, 컴포저블 함수가 필요한 경우 새 매개변수를 사용하여 자동으로 다시 호출된다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128248004-686b8128-c05d-4ad7-aad3-16d71068a137.png'>
</p>

## 동적 콘텐츠

composable 함수는 XML이 아닌 Kotlin으로 작성되기 때문에, 다른 Kotlin 코드와 마찬가지로 동적일 수 있다.

예를 들어 사용자 목록으로 환영하는 UI를 빌드한다고 가정해보자.

```kotlin
@Composable
fun Greeting(names: List<String>) {
    for (name in names) {
        Text("Hello $name")
    }
}
```

이 함수는 이름 목록을 받아서 각 사용자에 대한 인사말을 생성한다. 컴포저블 함수는 상당히 정교할 수 있다<small>(sophisticated)</small>. `if` 문을 사용하여 특정 UI 요소를 표시할지 여부를 결정할 수 있고, 루프를 사용할 수도, 헬퍼 함수를 호출할 수도, 기본 언어의 유연성을 완전히 활용할 수도 있다. **이러한 성능과 유연성은 Jetpack Compose의 주요 이점 중 하나이다.**

## 재구성<small>(Recomposition)</small>

명령형 UI 모델에서 위젯을 변경하려면 위젯에서 setter를 호출하여 내부의 상태를 변경한다. Compose에서는 새 데이터를 사용하여 컴포저블 함수를 다시 호출한다. 이렇게 하면 함수가 *재구성*되며, 필요한 경우 함수에서 내보낸 위젯이 새 데이터로 다시 그려진다. Compose 프레임워크는 변경된 구성요소만 지능적으로 재구성할 수 있다.

예를 들어 다음과 같이 버튼을 표시하는 컴포저블 함수를 고려해보자.

```kotlin
@Composable
fun ClickCounter(clicks: Int, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("I've been clicked $clicks times")
    }
}
```

버튼이 클릭될 때마다 호출자는 `clicks` 값을 업데이트한다. Compose는 `Text` 함수를 사용해 람다를 다시 호출하여 새 값을 표시한다. 이 프로세스를 *재구성*이라 한다. **값에 종속되지 않은 다른 함수들은 재구성되지 않는다.**

앞서 논의했듯이 전체 UI 트리를 재구성하는 하는 작업은 컴퓨팅 성능 및 배터리 수명을 사용한다는 측면에서 컴퓨팅 비용이 많이 들 수 있다. Compose는 *지능적 재구성*을 통해 이 문제를 해결한다.

재구성은 입력이 변경될 때 컴포저블 함수를 다시 호출하는 프로세스다. 이는 함수의 입력이 변경될 때 발생한다. Compose는 새 입력을 기반으로 재구성할 때, 변경되었을 수 있는 함수 또는 람다만 호출하고 나머지는 건너뛴다. 매개변수가 변경되지 않는 함수 또는 람다를 모두 건너뜀으로써 Compose는 재구성을 효율적으로할 수 있다.

함수의 재구성을 건너뛸 수 있으므로 컴포저블 함수 실행의 부수효과에 의존해서는 안된다. 그렇게 하면 사용자가 앱에서 이상하고 예측 불가능한 동작을 경험할 수 있다. 부수효과는 앱의 나머지 부분에 표시되는 변경사항이다. 예를 들어 다음 작업은 모두 위험한 부수효과이다.

- 공유 객체의 속성에 쓰기
- `ViewModel`에서 observable을 업데이트
- Shared preferences 업데이트

컴포저블 함수는 애니메이션이 렌더링될 때와 같이 모든 프레임에서 같은 빈도로 재실행될 수 있다. 애니메이션 도중 버벅거림을 방지하려면 컴포저블 함수가 빨라야 한다. Shared preferences에서 읽기와 같이 비용이 많이 드는 작업을 실행해야 하는 경우 백그라운드 코루틴에서 작업을 실행하고 값 결과를 컴포저블 함수의 매개변수로 전달해야한다.

예를 들어 다음 코드는 컴포저블을 생성하여 `SharedPreferences`의 값을 업데이트한다. 대신 이 코드는 백그라운드 코루틴의 `ViewModel`로 읽기 및 쓰기를 이동한다. 앱 로직은 콜백과 함께 현재 값을 전달하여 업데이트를 트리거한다.

```kotlin
@Composable
fun SharedPrefsToggle(
    text: String,
    value: Boolean,
    onValueChanged: (Boolean) -> Unit
) {
    Row {
        Text(text)
        Checkbox(checked = value, onCheckedChange = onValueChanged)
    }
}
```

이 문서에서는 Compose에서 프로그래밍할 때 알아야 할 여러 가지 사항을 설명한다.

- 컴포저블 함수는 순서와 관계없이 실행할 수 있다.
- 컴포저블 함수는 동시에<small>(parallel)</small> 실행할 수 있다.
- 재구성은 최대한 많은 수의 컴포저블 함수 및 람다를 건너뛴다.
- 재구성은 낙관적이며 취소될 수 있다.
- 컴포저블 함수는 애니메이션의 모든 프레임에서와 같은 빈도로 매우 자주 실행될 수 있다.

다음 섹션에서는 컴포저블 함수를 빌드하여 재구성을 지원하는 방법을 설명한다. 어떤 경우든 컴포저블 함수를 빠르고, 멱등성이며, 부수효과가 없는 상태로 유지하는 것이 좋다.

### 컴포저블 함수는 순서와 관계없이 실행할 수 있음

컴포저블 함수의 코드를 살펴보면 코드가 표시된 순서대로 실행된다고 가정할 수도 있다. 하지만 **코드가 반드시 표시된 순서대로 실행되는 것은 아니다.** 컴포저블 함수에 다른 컴포저블 함수 호출이 포함되어 있다면 그 함수는 순서와 관계없이 실행될 수 있다. Compose에는 일부 UI 요소가 다른 UI 요소보다 우선순위가 높다는 것을 인식하고 그 요소를 먼저 그리는 옵션이 있다.

예를 들어 탭 레이아웃에 세 개의 화면을 그리는 다음과 같은 코드가 있다고 가정해 보자.

```kotlin
@Composable
fun ButtonRow() {
    MyFancyNavigation {
        StartScreen()
        MiddleScreen()
        EndScreen()
    }
}
```

`StartScreen`, `MiddleScreen` 및 `EndScreen` 호출은 순서와 관계없이 발생할 수 있다. 즉, 예를 들어 `StartScreen()`이 일부 전역 변수(부수효과)를 설정하고 `MiddleScreen()`이 이러한 변경사항을 활용하도록 할 수 없음을 의미한다. 대신 이러한 각 함수는 독립적이어야 한다.

### 컴포저블 함수는 동시에<small>(parallel)</small> 실행할 수 있음

Compose는 컴포저블 함수들을 동시에 실행하여 **재구성을 최적화**할 수 있다. 이를 통해 Compose가 다중 코어를 활용하고, 화면에 없는 컴포저블 함수를 낮은 우선순위로 실행할 수 있다.

이 최적화는 컴포저블 함수가 백그라운드 스레드 풀 내에서 실행될 수 있음을 의미한다. 컴포저블 함수가 `ViewModel`에서 함수를 호출하면, Compose는 동시에 여러 스레드에서 이 함수를 호출할 수 있다.

애플리케이션이 올바르게 작동하도록 하려면 모든 컴포저블 함수에 부수효과가 없어야 한다. 대신 UI 스레드에서 항상 실행되는 `onClick`과 같은 콜백에서 부수효과를 트리거한다.

컴포저블 함수가 호출될 때, 호출자<small>(caller)</small>와 다른 스레드에서 호출<small>(invocation)</small>이 발생할 수 있다. 즉, 컴포저블 람다의 변수를 수정하는 코드는 피해야 한다. 이러한 코드는 thread-safe하지 않을 뿐만 아니라 컴포저블 람다의 허용되지 않는 부수효과이기 때문이다.

다음은 목록과 개수를 표시하는 컴포저블을 보여주는 예시다.

```kotlin
@Composable
fun ListComposable(myList: List<String>) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            for (item in myList) {
                Text("Item: $item")
            }
        }
        Text("Count: ${myList.size}")
    }
}
```

이 코드는 부수효과가 없으며, 입력 목록을 UI로 변환한다. 이 코드는 작은 목록을 표시할 때 유용한 코드다. 그러나 함수가 로컬 변수에 쓰는 경우, 이 코드는 thread-safe 하지 않거나 적절하지 않다.

```kotlin
@Composable
@Deprecated("Example with bug")
fun ListWithBug(myList: List<String>) {
    var items = 0

    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            for (item in myList) {
                Text("Item: $item")
                items++ // column의 재구성으로 인한 부수효과 때문에 이러한 코드는 피해야 한다.
            }
        }
        Text("Count: $items")
    }
}
```

이 예에서 items는 모든 재구성을 통해 수정된다. 수정은 애니메이션의 모든 프레임에서 또는 목록이 업데이트될 때 실행될 수 있다. 어느 쪽이든 UI에 잘못된 개수가 표시된다. 이 때문에 이와 같은 쓰기는 Compose에서 지원되지 않는다. 이러한 쓰기를 금지함으로써 프레임워크가 컴포저블 람다를 실행하도록 스레드를 변경할 수 있다.

### 재구성은 가능한 많이 건너뜀

UI의 일부가 잘못된 경우, Compose는 업데이트해야 하는 부분만 재구성하기 위해 최선을 다한다. 즉, UI 트리에서 위 또는 아래에 있는 컴포저블을 실행하지 않고 단일 버튼의 컴포저블을 다시 실행하도록 건너뛸 수 있다.

모든 컴포저블 함수 및 람다는 자체적으로 재구성할 수 있다.

다음은 목록을 렌더링할 때 재구성이 일부 요소를 건너뛸 수 있는 방법을 보여주는 예이다.

```kotlin
/**
 * 헤더와 함께 사용자가 클릭할 수 있는 이름 목록 표시 
 */
@Composable
fun NamePicker(
    header: String,
    names: List<String>,
    onNameClicked: (String) -> Unit
) {
    Column {
        // 이 코드는 [header]가 변경될 때 재구성될 것이다. 하지만 [names]가 변경될 땐 재구성되지 않는다.
        Text(header, style = MaterialTheme.typography.h5)
        Divider()

        // LazyColumn은 RecyclerView의 Compose 버전이다.
        // items()에 전달된 람다는 RecyclerView.ViewHolder와 유사하다.
        LazyColumn {
            items(names) { name ->
                // item의 [name]이 업데이트될 때, 해당 item의 adapter가 재구성된다.
                // adapter는 [header]가 변경된다고 재구성되지 않는다.
                NamePickerItem(name, onNameClicked)
            }
        }
    }
}

/**
 * Display a single name the user can click.
 */
@Composable
private fun NamePickerItem(name: String, onClicked: (String) -> Unit) {
    Text(name, Modifier.clickable(onClick = { onClicked(name) }))
}
```

이러한 각 범위는 재구성 도중에 실행할 유일한 사항일 수 있다. Compose는 `header`가 변경될 때 상위 요소 중 어느 것도 실행하지 않고 `Column` 람다로 건너뛸 수 있다. 그리고 `Column`을 실행할 때 Compose는 `names`가 변경되지 않았다면 `LazyColumnItems`를 건너뛰도록 선택할 수 있다.

다시 말하지만, 모든 컴포저블 함수 또는 람다를 실행하는 작업에는 부수효과가 없어야 한다. 부수효과를 실행해야 할 때는 콜백에서 부수효과를 트리거해야 한다.

### 재구성은 낙관적임

재구성은 Compose가 컴포저블의 매개변수가 변경되었을 수 있다고 생각할 때마다 시작된다. 재구성은 *낙관적*이다. 즉, Compose는 매개변수가 다시 변경되기 전에 재구성을 완료할 것으로 예상한다. 재구성이 완료되기 전에 매개변수가 변경되면 Compose는 재구성을 취소하고 새 매개변수를 사용하여 재구성을 다시 시작할 수 있다.

재구성이 취소되면 Compose는 재구성에서 UI 트리를 삭제한다. 표시되는 UI에 종속되는 부수효과가 있다면 구성이 취소된 경우에도 부수효과가 적용된다. 이로 인해 일관되지 않은 앱 상태가 발생할 수 있다.

낙관적 재구성을 처리할 수 있도록 모든 컴포저블 함수 및 람다가 멱등성이고 부수효과가 없는지 확인해야 한다.

### 컴포저블 함수는 매우 자주 실행될 수 있음

경우에 따라, 컴포저블 함수는 UI 애니메이션의 모든 프레임에서 실행될 수 있다. 함수가 기기 저장소에서 읽기와 같이 비용이 많이 드는 작업을 실행하면 이 함수로 인해 UI 버벅임<small>(jank)</small>이 발생할 수 있다.

예를 들어 위젯이 기기 설정을 읽으려고 하면 잠재적으로 이 설정을 초당 수백 번 읽을 수 있으며 이는 앱 성능에 치명적인 영향을 줄 수 있다.

컴포저블 함수에 데이터가 필요하다면 데이터의 매개변수를 정의해야 한다. 그런 다음, 비용이 많이 드는 작업을 구성<small>(composition)</small> 외부의 다른 스레드로 이동하고, `mutableStateOf` 또는 `LiveData`를 사용하여 Compose에 직접 데이터를 전달할 수 있다.

## References

- Android Developers : [Jetpack Compose Foundation - Thinking in Compose](https://developer.android.com/jetpack/compose/mental-model)