# Compose의 부수 효과<small>(Side-effect)</small>

컴포저블은 부수 효과가 없어야 한다. 하지만 앱의 상태를 변경하는 데 필요한 경우, 컴포저블의 수명 주기를 인식하는 관리된 환경에서 부수 효과를 호출해야 한다. 본문에서는 Jetpack Compose가 제공하는 다양한 부수 효과 API에 관해 알아본다.

> 핵심 용어 : 부수 효과는 컴포저블 함수의 범위 밖에서 발생하는 앱 상태에 관한 변경사항이다.

## 상태 및 효과 사용 사례

Compose 이해 문서에 설명된 대로 컴포저블에는 부수 효과가 없어야 합니다. 상태 관리 문서에 설명된 대로 앱 상태를 변경해야 하는 경우 이러한 부수 효과가 예측 가능한 방식으로 실행되도록 Effect API를 사용해야 합니다.

핵심 용어: 효과는 UI를 내보내지 않으며 컴포지션이 완료될 때 부수 효과를 실행하는 구성 가능한 함수입니다.
Compose에서 다양한 가능성 효과를 이용할 수 있기 때문에 과다하게 사용될 수 있습니다. 상태 관리 문서에 설명된 대로 효과에서 실행하는 작업이 UI와 관련되고 단방향 데이터 흐름을 중단하지 않아야 합니다.

참고: 반응형 UI는 본질적으로 비동기이며 Jetpack Compose는 콜백을 사용하는 대신 API 수준에서 코루틴을 이용하여 이를 해결합니다. 코루틴에 관한 자세한 내용은 Android의 Kotlin 코루틴 가이드를 참고하세요.
LaunchedEffect: 컴포저블의 범위에서 정지 함수 실행
컴포저블 내에서 안전하게 정지 함수를 호출하려면 LaunchedEffect 컴포저블을 사용하세요. LaunchedEffect가 컴포지션을 시작하면 매개변수로 전달된 코드 블록으로 코루틴이 실행됩니다. LaunchedEffect가 컴포지션을 종료하면 코루틴이 취소됩니다. LaunchedEffect가 다른 키로 재구성되면(아래 효과 다시 시작 섹션 참고) 기존 코루틴이 취소되고 새 코루틴에서 새 정지 함수가 실행됩니다.

예를 들어 Scaffold의 Snackbar는 정지 함수인 SnackbarHostState.showSnackbar 함수를 사용하여 표시됩니다.

```kotlin
@Composable
fun MyScreen(
    state: UiState<List<Movie>>,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    // If the UI state contains an error, show snackbar
    if (state.hasError) {

        // `LaunchedEffect` will cancel and re-launch if
        // `scaffoldState.snackbarHostState` changes
        LaunchedEffect(scaffoldState.snackbarHostState) {
            // Show snackbar using a coroutine, when the coroutine is cancelled the
            // snackbar will automatically dismiss. This coroutine will cancel whenever
            // `state.hasError` is false, and only start when `state.hasError` is true
            // (due to the above if-check), or if `scaffoldState.snackbarHostState` changes.
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Error message",
                actionLabel = "Retry message"
            )
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        /* ... */
    }
}
```

위 코드에서는 상태에 오류가 포함되어 있으면 코루틴이 트리거되고 오류가 포함되어 있지 않으면 취소됩니다. LaunchedEffect 호출 사이트가 if 문 내에 있으므로 문장이 거짓일 때 LaunchedEffect가 컴포지션에 있으면 삭제되고 따라서 코루틴이 취소됩니다.

rememberCoroutineScope: 컴포지션 인식 범위를 확보하여 컴포저블 외부에서 코루틴 실행
LaunchedEffect는 구성 가능한 함수이므로 구성 가능한 다른 함수 내에서만 사용할 수 있습니다. 컴포저블 외부에 있지만 컴포지션을 종료한 후 자동으로 취소되도록 범위가 지정된 코루틴을 실행하려면 rememberCoroutineScope를 사용하세요. 또한 코루틴 하나 이상의 수명 주기를 수동으로 관리해야 할 때마다, 예를 들어 사용자 이벤트가 발생할 때 애니메이션을 취소해야 하는 경우 rememberCoroutineScope를 사용하세요.

rememberCoroutineScope는 호출되는 컴포지션의 지점에 바인딩된 CoroutineScope를 반환하는 구성 가능한 함수입니다. 호출이 컴포지션을 종료하면 범위가 취소됩니다.

이전 예에 따라 사용자가 Button을 탭할 때 이 코드를 사용하여 Snackbar를 표시할 수 있습니다.

```kotlin
@Composable
fun MoviesScreen(scaffoldState: ScaffoldState = rememberScaffoldState()) {

    // Creates a CoroutineScope bound to the MoviesScreen's lifecycle
    val scope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        Column {
            /* ... */
            Button(
                onClick = {
                    // Create a new coroutine in the event handler
                    // to show a snackbar
                    scope.launch {
                        scaffoldState.snackbarHostState
                            .showSnackbar("Something happened!")
                    }
                }
            ) {
                Text("Press me")
            }
        }
    }
}
```

rememberUpdatedState: 값이 변경되는 경우 다시 시작되지 않아야 하는 효과에서 값 참조
주요 매개변수 중 하나가 변경되면 LaunchedEffect가 다시 시작됩니다. 하지만 경우에 따라 효과에서 값이 변경되면 효과를 다시 시작하지 않을 값을 캡처할 수 있습니다. 이렇게 하려면 rememberUpdatedState를 사용하여 캡처하고 업데이트할 수 있는 이 값의 참조를 만들어야 합니다. 이 접근 방식은 비용이 많이 들거나 다시 만들고 다시 시작할 수 없도록 금지된 오래 지속되는 작업이 포함된 효과에 유용합니다.

예를 들어 앱에 시간이 지나면 사라지는 LandingScreen이 있다고 가정해 보겠습니다. LandingScreen이 재구성되는 경우에도 일정 시간 동안 대기하고 시간이 경과되었음을 알리는 효과는 다시 시작해서는 안 됩니다.

```kotlin
@Composable
fun LandingScreen(onTimeout: () -> Unit) {

    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    // Create an effect that matches the lifecycle of LandingScreen.
    // If LandingScreen recomposes, the delay shouldn't start again.
    LaunchedEffect(true) {
        delay(SplashWaitTimeMillis)
        currentOnTimeout()
    }

    /* Landing screen content */
}
```

호출 사이트의 수명 주기와 일치하는 효과를 만들기 위해 Unit 또는 true와 같이 변경되지 않는 상수가 매개변수로 전달됩니다. 위 코드에서는 LaunchedEffect(true)가 사용됩니다. onTimeout 람다에 LandingScreen이 재구성된 최신 값이 항상 포함되도록 하려면 rememberUpdatedState 함수로 onTimeout을 래핑해야 합니다. 코드에서 반환된 State, currentOnTimeout은 효과에서 사용해야 합니다.

> 경고: LaunchedEffect(true)는 while(true)만큼 의심스럽습니다. 유효한 사용 사례가 있더라도 항상 일시중지하고 필요한 항목인지 확인하세요.

DisposableEffect: 정리가 필요한 효과

키가 변경되거나 컴포저블이 컴포지션을 종료한 후 정리해야 하는 부수 효과의 경우 DisposableEffect를 사용하세요. DisposableEffect 키가 변경되면 컴포저블이 현재 효과를 삭제(정리)하고 효과를 다시 호출하여 재설정해야 합니다.

예를 들어 OnBackPressedDispatcher에서 뒤로 버튼을 누르는 동작을 수신 대기하려면 OnBackPressedCallback을 등록해야 합니다. Compose에서 이 이벤트를 수신 대기하려면 DisposableEffect를 사용하여 필요에 따라 콜백을 등록하고 등록 취소하세요.

```kotlin
@Composable
fun BackHandler(backDispatcher: OnBackPressedDispatcher, onBack: () -> Unit) {

    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBack by rememberUpdatedState(onBack)

    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        // Always intercept back events. See the SideEffect for
        // a more complete version
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }

    // If `backDispatcher` changes, dispose and reset the effect
    DisposableEffect(backDispatcher) {
        // Add callback to the backDispatcher
        backDispatcher.addCallback(backCallback)

        // When the effect leaves the Composition, remove the callback
        onDispose {
            backCallback.remove()
        }
    }
}
```

위의 코드에서는 효과가 저장된 backCallback을 backDispatcher에 추가합니다. backDispatcher가 변경되면 효과가 삭제되고 다시 시작됩니다.

DisposableEffect는 onDispose 절을 코드 블록의 최종 문으로 포함해야 합니다. 그렇지 않으면 IDE에 빌드 시간 오류가 표시됩니다.

> 참고: onDispose에 빈 블록을 포함하는 것은 좋은 방법이 아닙니다. 항상 사용 사례에 더 적합한 효과가 있는지 다시 확인하세요.

부수 효과: Compose 상태를 비 Compose 코드에 게시
Compose 상태를 Compose에서 관리하지 않는 객체와 공유하려면 리컴포지션 성공 시마다 호출되는 SideEffect 컴포저블을 사용하세요.

콜백을 사용 설정해야 하는지 전달하려면 위의 BackHandler 코드를 참고한 후 SideEffect를 사용하여 값을 업데이트합니다.

```kotlin
@Composable
fun BackHandler(
    backDispatcher: OnBackPressedDispatcher,
    enabled: Boolean = true, // Whether back events should be intercepted or not
    onBack: () -> Unit
) {
    /* ... */
    val backCallback = remember { /* ... */ }

    // On every successful composition, update the callback with the `enabled` value
    // to tell `backCallback` whether back events should be intercepted or not
    SideEffect {
        backCallback.isEnabled = enabled
    }

    /* Rest of the code */
}
```