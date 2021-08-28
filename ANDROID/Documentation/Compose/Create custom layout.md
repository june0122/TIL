# Compose에서 커스텀 레이아웃 생성하기

Compose는 `Column`, `Row`, `Box`와 같은 기본 제공 컴포저블<small>(built-in composable)</small>을 결합하여 일부 커스텀 레이아웃에 충분할 수 있는 작은 청크로 컴포저블의 재사용성을 촉진합니다.

하지만 직접 자식들을 측정하고 배치해야 하는 앱에 고유한 요소을 빌드해야 할 수도 있습니다. 이를 위해 `Layout` 컴포저블을 사용할 수 있습니다. 실제로 `Column`과 `Row`와 같은 모든 상위 레벨 레이아웃은 `Layout` 컴포저블로 빌드됩니다.

> 참고 : View 시스템에서 커스텀 레이아웃을 생성하려면 `ViewGroup`을 확장하고 measure 및 layout 함수를 구현해야 합니다. 그에 반해 Compose에서는 `Layout` 컴포저블을 사용하여 함수를 작성하기만 하면 된다.

커스텀 레이아웃을 만드는 방법을 알아보기 전에 Compose의 레이아웃 원칙에 대해 더 많이 알아야 합니다.

## Compose의 레이아웃 원칙

일부 컴포저블 함수는 호출될 때 화면에 렌더링될 UI 트리에 추가되는 UI 조각들을 내보냅니다. 각 방출(또는 요소)에는 하나의 부모와 잠재적으로 많은 자식들이 있습니다. 또한 부모 내 위치<small>(`(x, y)` 위치)</small>와 크기<small>(`width`와 `height`)</small>가 있습니다.

요소는 충족되어야 하는 제약 조건들<small>(constraints)</small>로 스스로를 측정해야 합니다. 제약 조건은 요소의 최소 및 최대 `width`와 `height`를 제한합니다. 요소에 자식 요소가 있는 경우 자체 크기를 결정하는 데 도움이 되도록 각 요소를 측정할 수 있습니다. 요소가 자신의 크기를 보고하면 자식 요소를 자신과 관련하여 배치할 수 있습니다. 이것은 커스텀 레이아웃을 만들 때 추가로 설명됩니다.

<b>Compose UI는 멀티 패스 측정<small>(multi-pass measurement)</small>을 허용하지 않습니다.</b> 이는 레이아웃 요소가 다른 측정 구성을 시도하기 위해 자식을 두 번 이상 측정하지 않을 수 있음을 의미합니다. 단일 패스 측정은 성능에 좋으며 Compose가 깊은 UI 트리를 효율적으로 처리할 수 있습니다. 레이아웃 요소가 자식을 두 번 측정하고 해당 자식이 자식 중 하나를 두 번 측정하는 식이라면, 전체 UI를 배치하려는 한 번의 시도는 많은 작업을 수행해야 하므로 앱 성능을 계속 유지하기 어렵습니다. 그러나 단일 자식 층정이 알려 주는 것 외에 추가 정보가 정말로 필요한 경우가 있습니다. 이러한 경우에는 이를 수행하는 방법이 있으므로 나중에 이에 대해 이야기하겠습니다.

## 레이아웃 수정자<small>(layout modifier)</small> 사용

레이아웃 수정자를 사용하여 요소를 측정하고 배치하는 방법을 수동으로 제어합니다. 일반적으로 커스텀 레이아웃 수정자의 일반적인 구조는 다음과 같습니다.

```kotlin
fun Modifier.customLayoutModifier(...) = Modifier.layout { measurable, constraints ->
  ...
})
```

레이아웃 수정자를 사용할 때 두 개의 람다 매개변수를 얻습니다.
- `measurable` : 측정 및 배치할 자식
- `constraints` : 자식의 너비와 높이에 대한 최소 및 최대

화면에 `Text`를 표시하고 텍스트 맨 위에서 첫 번째 줄의 기준선<small>(baseline)</small>까지의 거리를 제어하려고 한다고 가정해 보겠습니다. 그렇게 하려면 레이아웃 수정자를 사용하여 화면에 컴포저블을 수동으로 배치해야 합니다. 상단에서 첫 번째 기준선까지의 거리가 `24.dp`인 다음 그림에서 원하는 동작을 참조하세요. 

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/131217910-7e4b0987-4a23-4a34-a961-5136ba43ed77.png'>
</p>

먼저 `firstBaselineToTop` 수정자를 생성해 보겠습니다.

```kotlin
fun Modifier.firstBaselineToTop(
  firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        ...
    }
)
```

가장 먼저 할 일은 컴포저블을 측정하는 것입니다. Compose의 레이아웃 원칙 섹션에서 언급했듯이 자식은 한 번만 측정할 수 있습니다.

`measurable.measure(constraints)`를 호출하여 컴포저블을 측정합니다. `measure(constraints)`를 호출할 때 `constraints` 람다 매개변수에서 사용 가능한 컴포저블의 주어진 제약 조건을 전달하거나 직접 생성할 수 있습니다. `Measurable`에 대한 `measure()` 호출의 결과는 나중에 할 것처럼 `placeRelative(x, y)`를 호출하여 위치를 지정할 수 있는 `Placeable`입니다.

이 사용 사례의 경우 측정을 더 이상 제약하지 말고 주어진 제약 조건을 사용하세요. 

```kotlin
fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        ...
    }
)
```

이제 컴포저블이 측정되었으므로 콘텐츠를 배치하는 데 사용되는 람다도 허용하는 `layout(width, height)` 메서드를 호출하여 크기를 계산하고 지정해야 합니다.

이 경우 컴포저블의 너비는 측정된 컴포저블의 `width`가 되고, 높이는 top-to-baseline 높이에서 첫 번째 기준선을 뺀 컴포저블의 `height`입니다.

```kotlin
fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        // Check the composable has a first baseline
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        // Height of the composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            ...
        }
    }
)
```

이제 `placeable.placeRelative(x, y)`를 호출하여 컴포저블을 화면에 배치할 수 있습니다. `placeRelative`를 호출하지 않으면 컴포저블이 표시되지 않습니다. `placeRelative`는 현재 `layoutDirection`을 기반으로 배치 가능한 위치를 자동으로 조정합니다.

> 경고 : 커스텀 `Layout` 또는 `LayoutModifier`를 만들 때, Android Studio는 `layout` 함수가 호출될 때까지 경고를 표시합니다.

이 경우 텍스트의 `y` 위치는 상단 패딩에서 첫 번째 기준선의 위치를 뺀 값에 해당합니다.

```kotlin
fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        ...

        // Height of the composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            // Where the composable gets placed
            placeable.placeRelative(0, placeableY)
        }
    }
)
```

이 수정자가 예상대로 작동하는지 확인하려면 위의 그림에서 본 것처럼 `Text`에서 이 수정자를 사용하면 됩니다.

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/131218840-682d5960-3c14-40bb-bd29-7db4a0aac13a.png'>
</p>
