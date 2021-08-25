# Jetpack Compose 아키텍처 레이어링

이 페이지에서는 Jetpack Compose를 구성하는 아키텍처 레이어와 관련 디자인에 큰 영향을 주는 핵심 원칙을 간략하게 설명합니다.

Jetpack Compose는 단일 모놀리식 프로젝트<small>(single monolithic project)</small>가 아닙니다. 완전한 스택을 만들기 위해 함께 조합된 다수의 모듈로 만들어졌습니다. Jetpack Compose를 구성하는 여러 모듈을 이해하면 다음이 가능합니다.

- 적절한 수준의 추상화를 사용하여 앱 또는 라이브러리 빌드
- 보다 세부적인 제어나 커스터마이즈를 위해 낮은 수준으로 '드롭 다운'할 수 있는 경우 파악
- 의존성 최소화

## 레이어

Jetpack Compose의 기본 레이어는 다음과 같습니다.

<figure>
    <img width = '250' src = 'https://user-images.githubusercontent.com/39554623/130799388-967c3f74-8e3a-4ea7-9a74-44aecf7506a8.png '>
    <figcaption>▲ Jetpack Compose의 기본 레이어</figcaption>
</figure>

각 레이어는 하위 수준에 기반하고, 상위 수준의 구성요소를 만들기 위해 기능을 결합합니다. 각 레이어는 하위 레이어의 공개 API를 기반으로 하여 모듈 경계를 확인하고 필요한 경우 레이어를 대체할 수 있게 해줍니다. 이러한 레이어를 아래부터 살펴보겠습니다.

#### [Runtime](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary)

- 이 모듈은 `remember`, `mutableStateOf`, `@Composable` 어노테이션, `SideEffect` 같은 Compose 런타임의 기초를 제공합니다. UI가 아닌 Compose의 트리 관리 기능만 필요한 경우 이 레이어에 바로 빌드하는 것이 좋습니다.

#### [UI](https://developer.android.com/reference/kotlin/androidx/compose/ui/package-summary)

- UI 레이어는 여러 개의 모듈(`ui-text`, `ui-graphics`, `ui-tooling` 등)로 구성됩니다. 이러한 모듈은 `LayoutNode`, `Modifier`, input handlers, custom layouts, 그리기 같은 UI 툴킷의 기본 사항을 구현합니다. UI 툴킷의 기본 개념만 필요한 경우 이 레이어를 기반으로 빌드하는 것이 좋습니다.

#### [Foundation](https://developer.android.com/reference/kotlin/androidx/compose/foundation/package-summary)

- 이 모듈은 Compose UI에 `Row`, `Column`, `LazyColumn`, 특정 동작 인식 등과 같이 디자인 시스템에 구속되지 않는 구성요소를 제공합니다. 자체 디자인 시스템을 만들 때는 Foundation<small>(기초)</small> 레이어를 기반으로 빌드하는 것이 좋습니다.

#### [Material](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary)

- 이 모듈은 Compose UI에 머티리얼 디자인 시스템의 구현을 제공하고 테마 설정 시스템, 스타일 적용된 구성요소, 물결 표시, 아이콘도 제공합니다. 앱에 머티리얼 디자인을 사용할 때는 이 레이어를 기반으로 빌드합니다.

## 디자인 원칙

Jetpack Compose의 기본 원칙은 몇 가지 모놀리식 구성요소를 제공하는 것보다 함께 조립(또는 구성)할 수 있는 작고 집중된 기능을 제공하는 것입니다. 이 접근방식에는 여러 가지 장점이 있습니다.

### 컨트롤

상위 수준의 구성요소는 자동으로 더 많은 작업을 실행하지만, 개발자가 직접 제어할 수 있는 정도를 제한합니다. 더 많은 제어가 필요하면 하위 수준의 구성요소를 사용하도록 '드롭 다운'하면 됩니다.

예를 들어 구성요소 색상을 애니메이션으로 표시하려면 [`animateColorAsState`](https://developer.android.com/reference/kotlin/androidx/compose/animation/package-summary#animateColorAsState(androidx.compose.ui.graphics.Color,androidx.compose.animation.core.AnimationSpec,kotlin.Function1)) API를 사용하면 됩니다.

```kotlin
val color = animateColorAsState(if (condition) Color.Green else Color.Red)
```

그러나 구성요소를 항상 회색으로 시작해야 하는 경우에는 이 API에서 가능하지 않습니다. 대신 하위 수준의 [`Animatable`](https://developer.android.com/reference/kotlin/androidx/compose/animation/core/package-summary?hl=HU#Animatable(kotlin.Float,kotlin.Float)) API를 사용하도록 드롭 다운하면 됩니다.

```kotlin
val color = remember { Animatable(Color.Gray) }
LaunchedEffect(condition) {
    color.animateTo(if (condition) Color.Green else Color.Red)
}
```

상위 수준의 `animateColorAsState` API 자체는 하위 수준의 `Animatable` API를 기반으로 빌드됩니다. 하위 수준의 API 사용이 좀 더 복잡하지만 보다 세부적인 제어가 가능합니다. 필요에 가장 적합한 추상화 수준을 선택하세요.

### 커스터마이즈

작은 구성요소에서 상위 수준의 구성요소를 조합하면 필요한 경우 훨씬 더 쉽게 구성요소를 커스터마이즈할 수 있습니다. 예를 들어 머티리얼 레이어에서 제공하는 `Button`의 [구현](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/src/commonMain/kotlin/androidx/compose/material/Button.kt)을 살펴보겠습니다.

```kotlin
@Composable
fun Button(
    // …
    content: @Composable RowScope.() -> Unit
) {
    Surface(/* … */) {
        CompositionLocalProvider(/* … */) { // set LocalContentAlpha
            ProvideTextStyle(MaterialTheme.typography.button) {
                Row(
                    // …
                    content = content
                )
            }
        }
    }
}
```

`Button`은 4가지 구성요소로 조합되었습니다.

1. 배경, 도형, 클릭 처리 등을 제공하는 머티리얼 [`Surface`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary?hl=HU#Surface(kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.foundation.BorderStroke,androidx.compose.ui.unit.Dp,androidx.compose.foundation.interaction.MutableInteractionSource,androidx.compose.foundation.Indication,kotlin.Boolean,kotlin.String,androidx.compose.ui.semantics.Role,kotlin.Function0))
2. 버튼이 사용되거나 중지될 때 콘텐츠의 알파를 변경하는 [`CompositionLocalProvider`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary#CompositionLocalProvider(kotlin.Array,kotlin.Function0))
3. 사용할 기본 텍스트 스타일을 설정하는 [`ProvideTextStyle`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary?hl=HU#ProvideTextStyle(androidx.compose.ui.text.TextStyle,kotlin.Function0))
4. 버튼 콘텐츠의 기본 레이아웃 정책을 제공하는 `Row`

구조를 좀 더 명확히 하기 위해 일부 매개변수와 주석을 생략했습니다. 하지만 전체 구성요소는 버튼 구현을 위해 이러한 4개 구성요소를 조합하는 역할만 하기 때문에 약 40개의 코드 행에 불과합니다. `Button`과 같은 구성요소는 노출되는 매개변수에 관해 편향적이고, 구성요소의 사용을 더 어렵게 만들 수 있는 매개변수의 증가와 관련해 사용 설정된 일반 커스터마이즈의 균형을 잡습니다. 예를 들어 머티리얼 구성요소는 머티리얼 디자인 시스템에 지정된 커스터마이즈을 제공하기 때문에 머티리얼 디자인 원칙을 쉽게 따를 수 있습니다.

하지만 구성요소의 매개변수 이외의 요소를 맞춤설정하려면 수준을 '드롭 다운'하고 구성요소를 포크<small>(fork)</small>하면 됩니다. 예를 들어, 머티리얼 디자인에는 버튼 배경이 단색이어야 한다고 지정되어 있습니다. 그라데이션 배경이 필요한 경우 이는 `Button` 매개변수에서 지원되지 않습니다. 이 경우 머티리얼 `Button` 구현을 참조로 사용하고 자체 구성요소를 빌드할 수 있습니다.

```kotlin
@Composable
fun GradientButton(
    // …
    background: List<Color>,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        // …
        modifier = modifier
            .clickable(/* … */)
            .background(
                Brush.horizontalGradient(background)
            )
    ) {
        CompositionLocalProvider(/* … */) { // set material LocalContentAlpha
            ProvideTextStyle(MaterialTheme.typography.button) {
                content()
            }
        }
    }
}
```

위 구현에서는 머티리얼의 [현재 콘텐츠 알파](https://developer.android.com/jetpack/compose/themes#emphasis) 개념과 현재 텍스트 스타일 개념 같은 머티리얼 레이어의 구성요소를 계속 사용하고 있습니다. 하지만 머티리얼 `Surface`를 `Row`로 대체하고 스타일을 지정하여 원하는 모양을 만듭니다.

> 주의 : 구성요소를 커스터마이즈하기 위해 하위 레이어로 드롭 다운할 때, 접근성 지원을 무시하는 등 기능을 저하시키지 않도록 합니다. 포크하는 구성요소를 가이드로 삼으세요.

맞춤 디자인 시스템<small>(bespoke design system)</small>을 빌드하는 등 머티리얼 개념을 전혀 사용하지 않으려는 경우 foundation 레이어 구성요소만 순수하게 사용하도록 드롭 다운할 수 있습니다.

```kotlin
@Composable
fun BespokeButton(
    // …
    content: @Composable RowScope.() -> Unit
) {
    Row(
        // …
        modifier = modifier
            .clickable(/* … */)
            .background(/* … */)
    ) {
        // No Material components used
        content()
    }
}
```

Jetpack Compose는 최상위 수준의 구성요소에는 가장 단순한 이름을 예약합니다. 예를 들어 [`androidx.compose.material.Text`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary?hl=HU#Text(kotlin.String,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.TextUnit,androidx.compose.ui.text.font.FontStyle,androidx.compose.ui.text.font.FontWeight,androidx.compose.ui.text.font.FontFamily,androidx.compose.ui.unit.TextUnit,androidx.compose.ui.text.style.TextDecoration,androidx.compose.ui.text.style.TextAlign,androidx.compose.ui.unit.TextUnit,androidx.compose.ui.text.style.TextOverflow,kotlin.Boolean,kotlin.Int,kotlin.Function1,androidx.compose.ui.text.TextStyle))는 [`androidx.compose.foundation.text.BasicText`](https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/package-summary#BasicText(kotlin.String,androidx.compose.ui.Modifier,androidx.compose.ui.text.TextStyle,kotlin.Function1,androidx.compose.ui.text.style.TextOverflow,kotlin.Boolean,kotlin.Int))를 기반으로 합니다. 이렇게 하면 상위 수준을 대체하려고 할 때 자체 구현에 가장 쉽게 찾을 수 있는 이름을 제공할 수 있습니다.

> 주의: 구성요소를 포크하면 업스트림 구성요소의 향후 추가사항이나 버그 수정을 활용하지 못하게 됩니다.

### 정확한 추상화 선택

재사용 가능한 계층화된 구성요소<small>(layered, reusable components)</small>를 빌드한다는 Compose 철학의 의미는 항상 하위 수준의 구성요소만 추구해서는 안 된다는 뜻을 담고 있습니다. 대다수 상위 수준 구성요소가 더 많은 기능을 제공할 뿐만 아니라 접근성 지원과 같은 권장사항을 구현하는 경우도 많습니다.

예를 들어 커스텀 구성요소에 동작<small>(gesture)</small> 지원을 추가하려면 `Modifier.pointerInput`을 사용하여 처음부터 이를 빌드해도 됩니다. 하지만 이 구성요소를 기반으로 빌드된 다른 상위 수준 구성요소가 있고 이러한 구성요소가 좀 더 나은 시작점을 제공할 수 있습니다. `Modifier.draggable`, `Modifier.scrollable`, `Modifier.swipeable` 등이 그 예입니다.

일반적으로 *최상위 수준* 구성요소를 기반으로 하는 것이 좋습니다. 이러한 구성요소가 권장사항의 이점을 누리는 데 필요한 기능을 제공합니다.

### 자세히 알아보기

Custom design system을 빌드하는 예는 [Jetsnack](https://github.com/android/compose-samples/tree/main/Jetsnack) 샘플을 참고하세요.

## References

- Android developers : [Jetpack Compose architectural layering](https://developer.android.com/jetpack/compose/layering)