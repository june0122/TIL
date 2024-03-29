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

이 경우 컴포저블의 너비는 측정된 컴포저블의 `width`가 되고, 높이는 프로그래머가 원하는 `baselineToTop` 높이에서 첫 번째 기준선을 뺀 컴포저블의 `height`입니다.<small>(번역이 매끄럽지 않은데 값들을 로깅하여 확인해보면 쉽게 이해할 수 있습니다. 로깅 결과는 아래에 첨부.)</small>

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

이 경우 텍스트의 `y` 위치는 맨 위 패딩에서 첫 번째 기준선의 위치를 뺀 값에 해당합니다.

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

<figure>
    <img width = '300' src = 'https://user-images.githubusercontent.com/39554623/131245367-0321fb51-b281-42bf-9112-5b13279ff765.png '>
    <figcaption>▲ Modifier.firstBaselineToTop()에 32.dp를 인수로 전달했을 때의 로깅 결과</figcaption>
</figure>


이 수정자가 예상대로 작동하는지 확인하려면 위의 그림에서 본 것처럼 `Text`에서 이 수정자를 사용하면 됩니다.

```kotlin
@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
  LayoutsCodelabTheme {
    Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
  }
}

@Preview
@Composable
fun TextWithNormalPaddingPreview() {
  LayoutsCodelabTheme {
    Text("Hi there!", Modifier.padding(top = 32.dp))
  }
}
```

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/131218840-682d5960-3c14-40bb-bd29-7db4a0aac13a.png'>
</p>

## `Layout` 컴포저블 사용

단일 컴포저블이 측정되고 화면에 배치되는 방식을 제어하는 대신, 컴포저블 그룹에 대해서 동일하게 제어할 필요성이 있을 수 있습니다. 이를 위해 [Layout](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main-release:compose/ui/ui/src/commonMain/kotlin/androidx/compose/ui/layout/Layout.kt) 컴포저블을 사용하여 레이아웃의 자식을 측정하고 배치하는 방법을 수동으로 제어할 수 있습니다. 일반적으로 `Layout`을 사용하는 컴포저블의 일반적인 구조는 다음과 같습니다.

```kotlin
@Composable
fun CustomLayout(
    modifier: Modifier = Modifier,
    // custom layout attributes 
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // measure and position children given constraints logic here
    }
}
```

`CustomLayout`에 필요한 최소 매개변수는 `modifier`와 `content`입니다. 그런 다음 이러한 매개변수가 `Layout`으로 전달됩니다. <small>(`MeasurePolicy` 유형)</small>`Layout`의 trailing 람다에서 `layout` 수정자를 사용해서 얻은 것과 동일한 람다 매개변수를 얻습니다.

`Layout`이 작동하는 모습을 보여주기 위해, `Layout`을 사용하여 API를 이해하는 매우 기본적인 `Column`을 구현해 보겠습니다. 나중에 `Layout` 컴포저블의 유연성을 보여주기 위해 좀 더 복잡한 것을 만들 것입니다.

## 기본적인 Column 구현

여기서 `Column`의 커스텀 구현은 **항목들을 수직으로 배치**합니다. 또한 단순함을 위해 레이아웃은 **부모에서 가능한 한 많은 공간을 차지합니다.**

`MyOwnColumn`이라는 새 컴포저블을 만들고 `Layout` 컴포저블의 공통 구조를 추가합니다.

```kotlin
@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // measure and position children given constraints logic here
    }
}
```

이전과 마찬가지로 가장 먼저 해야 할 일은 한 번 밖에 측정할 수 없는 자식들을 측정하는 것입니다. 레이아웃 수정자가 작동하는 방식과 유사하게 `measurables` 람다 매개변수에서 `measurable.measure(constraints)`을 호출하여 측정할 수 있는 모든 `content`를 얻습니다.

이 사용 사례에서는 자식 뷰들을 더 이상 제한하지 않습니다. 자식을 측정할 때, 나중에 화면에 자식들을 올바르게 배치할 수 있도록 각 행의 `width`와 최대 `height`도 추적해야 합니다.

```kotlin
@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each child
            measurable.measure(constraints)
        }
    }
}
```

이제 로직에 측정된 자식 리스트가 있으므로 화면에 배치하기 전에 커스텀한 `Column`의 크기를 계산해야 합니다. 크기를 부모만큼 크게 만들되 부모가 전달한 제약 조건만큼이어야 합니다. `layout(width, height)` 메서드를 호출하여 우리의 고유한 `Column`의 크기를 지정합니다. 이 메서드는 자식을 배치하는데 사용되는 람다도 제공합니다.

```kotlin
@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Measure children - code in the previous code snippet
        ...

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children
        }
    }
}
```

마지막으로 `placeable.placeRelative(x, y)`를 호출하여 화면에 자식들을 배치합니다. 자식들을 수직으로 배치하기 위해, 자식들을 배치한 `y` 좌표를 추적합니다. `MyOwnColumn`의 최종 코드는 다음과 같습니다.

```kotlin
@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each child
            measurable.measure(constraints)
        }

        // Track the y co-ord we have placed children up to
        var yPosition = 0

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}
```

## `MyOwnColumn` in action

`BodyContent` 컴포저블에서 `MyOwnColumn`을 사용하여 화면에서 확인해보자. `BodyContent` 내부의 콘텐츠를 다음으로 바꿉니다.

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/131247235-405ecbb1-a5f1-465a-bc3e-44952b2fe037.png'>
</p>

# 복잡한 커스텀 레이아웃

레이아웃의 기본 사항을 다루었으므로 API의 유연성을 보여주기 위해 더 복잡한 예제를 만들어 보겠습니다. 다음 그림 중간에서 볼 수 있는 커스텀 [Material Study Owl](https://material.io/design/material-studies/owl.htm)의 staggered grid를 만들 것입니다.

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/131250819-59c68993-2540-414f-b385-5e2a30255f1c.png'>
</p>

Owl의 staggered grid는 항목을 세로로 배치하고, `n`개의 행이 주어지면 한 번에 열을 채웁니다. `Columns`에 `Row`로는 레이아웃의 비틀림을 얻지 못하므로 사용이 불가능합니다. 데이터가 세로로 표시되도록 준비하면 `Row`의 `Column`을 가질 수 있습니다.

하지만 커스텀 레이아웃을 사용하면 staggered grid에 있는 모든 항목의 높이를 제한할 수도 있습니다. 따라서 레이아웃을 더 잘 제어하고 커스텀 레이아웃을 만드는 방법을 배우기 위해, 우리는 스스로 자식들을 측정하고 배치할 것입니다.

그리드를 다른 방향<small>(orientation)</small>에서 재사용할 수 있도록 하려면 화면에 표시하려는 행의 수를 매개변수로 사용할 수 있습니다. 해당 정보는 레이아웃이 호출될 때 와야 하므로 매개변수로 전달합니다.

```kotlin
@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // measure and position children given constraints logic here
    }
}
```

이전과 마찬가지로 가장 먼저 할 일은 자식들을 측정하는 것입니다. 자식은 한 번만 측정할 수 있음을 기억하십시오.

우리의 사용 사례에서는 자식 뷰를 더 이상 제한하지 않습니다. 자식을 측정할 때 각 행의 너비와 최대 높이도 추적해야 합니다.

```kotlin
Layout(
    modifier = modifier,
    content = content
) { measurables, constraints ->

    // Keep track of the width of each row
    val rowWidths = IntArray(rows) { 0 }

    // Keep track of the max height of each row
    val rowHeights = IntArray(rows) { 0 }

    // Don't constrain child views further, measure them with given constraints
    // List of measured children
    val placeables = measurables.mapIndexed { index, measurable ->

        // Measure each child
        val placeable = measurable.measure(constraints)

        // Track the width and max height of each row
        val row = index % rows
        rowWidths[row] += placeable.width
        rowHeights[row] = max(rowHeights[row], placeable.height)

        placeable
    }
    ...
}
```

이제 로직에서 측정된 자식들의 리스트가 나왔으니, 자식들을 배치하기 전에 그리드의 크기<small>(full `width` & `height`)</small>를 계산해야 합니다. 또한 각 행의 최대 높이를 알고 있기 때문에 Y 위치에서 각 행의 요소를 어디에 배치할 것인지 계산할 수 있습니다. `rowY` 변수에 Y의 위치를 저장합니다.

```kotlin
Layout(
    content = content,
    modifier = modifier
) { measurables, constraints ->
    ... 

    // Grid's width is the widest row
    val width = rowWidths.maxOrNull()
        ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

    // Grid's height is the sum of the tallest element of each row
    // coerced to the height constraints 
    val height = rowHeights.sumOf { it }
        .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

    // Y of each row, based on the height accumulation of previous rows
    val rowY = IntArray(rows) { 0 }
    for (i in 1 until rows) {
        rowY[i] = rowY[i-1] + rowHeights[i-1]
    }

    ...
}
```

마지막으로, `placeable.placeRelative(x, y)`를 호출하여 자식들을 화면 위에 배치합니다. 이 사용 사례에서는 `rowX` 변수의 각 행에 대한 X 좌표도 추적합니다.

```kotlin
Layout(
    content = content,
    modifier = modifier
) { measurables, constraints ->
    ... 

    // Set the size of the parent layout
    layout(width, height) {
        // x cord we have placed up to, per row
        val rowX = IntArray(rows) { 0 }

        placeables.forEachIndexed { index, placeable ->
            val row = index % rows
            placeable.placeRelative(
                x = rowX[row],
                y = rowY[row]
            )
            rowX[row] += placeable.width
        }
    }
```

### 커스텀 StaggeredGrid 사용 예제

이제 자식을 측정하고 배치하는 방법을 알고 있는 커스텀 그리드 레이아웃이 있으므로 앱에서 사용하겠습니다. 그리드에서 Owl 앱에 구현된 chip을 테스트하기 위해 위해 유사한 작업을 수행하는 컴포저블을 쉽게 만들 수 있습니다.

```kotlin
@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp), 
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun ChipPreview() {
    LayoutsCodelabTheme {
        Chip(text = "Hi there")
    }
}
```

<p align = 'center'>
<img width = '200' src = 'https://user-images.githubusercontent.com/39554623/131455078-6daa92cf-34c7-4924-b1e6-0775967e16f7.png'>
</p>

이제 `BodyContent`에 표시할 수 있는 topic 리스트를 만들고 `StaggeredGrid`에 표시해 보겠습니다.

```kotlin
val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)


@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    StaggeredGrid(modifier = modifier) {
        for (topic in topics) {
            Chip(modifier = Modifier.padding(8.dp), text = topic)
        }
    }
}

@Preview
@Composable
fun LayoutsCodelabPreview() {
    LayoutsCodelabTheme {
        BodyContent()
    }
}
```

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/131475141-473070b5-9806-44b4-be02-fa667bba0c07.png'>
</p>

그리드 행의 수를 변경할 수 있는데다 예상대로 작동합니다.

```kotlin
@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    StaggeredGrid(modifier = modifier, rows = 5) {
        for (topic in topics) {
            Chip(modifier = Modifier.padding(8.dp), text = topic)
        }
    }
}
```

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/131480847-4569ba8c-fd4b-44a7-b0f5-6cdaec1d6bdd.png'>
</p>

행 수에 따라 topic이 화면을 벗어날 수 있듯이, scrollable `Row`로 `StaggeredGrid`를 감싸고 수정자를 `StaggeredGrid` 대신 이 `Row`에 전달하면 `BodyContent`를 스크롤할 수 있습니다.

```kotlin
@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        StaggeredGrid {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}
```

[Interactive Preview button](https://developer.android.com/jetpack/compose/tooling?authuser=1#enable-experimental-features)을 사용하거나 Android Studio 실행 버튼을 눌러 장치에서 앱을 실행하면 콘텐츠를 가로로 스크롤할 수 있습니다.