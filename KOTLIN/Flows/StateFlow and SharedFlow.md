# StateFlow and SharedFlow

`StateFlow`와 `SharedFlow`는 Flow에서 최적으로 상태 업데이트를 내보내고 여러 소비자에게 값을 내보낼 수 있게 해주는 Flow API이다.

## StateFlow

`StateFlow`는 현재 상태와 새로운 상태의 업데이트를 수집기<small>(collector)</small>에 내보내는 관찰 가능한 상태 홀더 Flow<small>(state-holder observable flow)</small>이다. `value` 프로퍼티를 통해서도 현재 상태 값을 읽을 수 있다. 상태를 업데이트하고 Flow에 전송하려면 `MutableStateFlow` 클래스의 `value` 프로퍼티에 새 값을 할당해야 한다.

Android에서 `StateFlow`는 관찰 가능한 변경 가능 상태<small>(observable mutable state)</small>를 유지해야 하는 클래스에 아주 적합하다.

`View`가 UI 상태 업데이트를 수신 대기하고 구성 변경에도 기본적으로 화면 상태가 지속되도록 `LatestNewsViewModel`에서 `StateFlow`를 노출할 수 있다.

```kotlin
class LatestNewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    // Backing property to avoid state updates from other classes (다른 클래스에서 상태를 업데이트하는 것을 막기 위한 Backing property)
    private val _uiState = MutableStateFlow(LatestNewsUiState.Success(emptyList()))
    // The UI collects from this StateFlow to get its state updates (UI는 상태 업데이트를 얻기 위해 이 StateFlow에서 수집한다.)
    val uiState: StateFlow<LatestNewsUiState> = _uiState

    init {
        viewModelScope.launch {
            newsRepository.favoriteLatestNews
                // Update View with the latest favorite news
                // Writes to the value property of MutableStateFlow,
                // adding a new element to the flow and updating all
                // of its collectors
                .collect { favoriteNews ->
                    _uiState.value = LatestNewsUiState.Success(favoriteNews)
                }
        }
    }
}

// Represents different states for the LatestNews screen
sealed class LatestNewsUiState {
    data class Success(news: List<ArticleHeadline>): LatestNewsUiState()
    data class Error(exception: Throwable): LatestNewsUiState()
}
```

`MutableStateFlow` 업데이트를 담당하는 클래스가 생산자이고, `StateFlow`에서 수집되는 모든 클래스가 소비자다. Flow 빌더를 사용하여 빌드된 **Cold Flow**과 달리 StateFlow는 **Hot Flow**이다. 수집될 때마다 생산자 블록을 새로 고침하는 Cold Flow와 달리, Hot Flow는 Flow에서 수집해도 생산자 코드가 트리거되지 않는다. `StateFlow`는 항상 활성 상태이고 메모리 내에 있으며 가비지 컬렉션 루트에서 달리 참조가 없는 경우에만 가비지 컬렉션에 사용할 수 있다.

새로운 소비자가 Flow에서 수집을 시작하면 스트림의 마지막 상태와 후속 상태가 수신된다. `LiveData` 같이 관찰 가능한 다른 클래스에서 이 동작을 찾을 수 있다.

`View`는 다른 Flow와 마찬가지로 `StateFlow`를 수신 대기<small>(listen)</small>한다.

```kotlin
class LatestNewsActivity : AppCompatActivity() {
    private val latestNewsViewModel = // getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        // Start a coroutine in the lifecycle scope (라이프사이클 스코프에서 코루틴을 시작)
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED
            // (repeatOnLifecycle은 수명 주기가 STARTED (또는 그 이상)에 있을 때마다 새 코루틴에서 블록을 launch하고 STOPPED일 때 취소한다.)
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values. (Flow를 트리거하고 값을 수신 대기한다.)
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED (이것은 수명 주기가 STARTED일 때 발생하고, STOPPED일 때 수집을 중단한다.)
                latestNewsViewModel.uiState.collect { uiState ->
                    // New value received
                    when (uiState) {
                        is LatestNewsUiState.Success -> showFavoriteNews(uiState.news)
                        is LatestNewsUiState.Error -> showError(uiState.exception)
                    }
                }
            }
        }
    }
}
```

> **⚠︎ 경고**: UI를 업데이트해야 하는 경우 `launch` 또는 `launchIn` 확장 함수로 **UI에서 직접 Flow를 수집하면 안 된다.** 이 함수들은 뷰가 표시되지 않는 경우에도 이벤트를 처리한다. 이 동작으로 인해 앱이 다운될 수도 있다. 이를 방지하려면 위와 같이 `repeatOnLifecycle` API를 사용해야 한다.

> **참고**: `repeatOnLifecycle` API는 *androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01* 라이브러리 이상 버전에서만 사용할 수 있다.

Flow를 `StateFlow`로 변환하려면 `stateIn` 중간<small>(intermediate)</small> 연산자를 사용해야 한다.

### StateFlow, Flow, LiveData

`StateFlow`와 `LiveData`는 비슷한 점이 있다. 둘 다 관찰 가능한 데이터 홀더 클래스<small>(observable data holder class)</small>이며, 앱 아키텍처에 사용할 때 비슷한 패턴을 따른다.

그러나 `StateFlow`와 `LiveData`는 다음과 같이 다르게 작동한다.

- `StateFlow`의 경우 초기 상태를 생성자에 전달해야 하지만, `LiveData`의 경우는 전달하지 않는다.
- 뷰가 STOPPED 상태가 되면 `LiveData.observe()`는 소비자를 자동으로 등록 취소<small>(unregister)</small>하는 반면, `StateFlow` 또는 다른 Flow에서 수집하는 경우 자동으로 수집을 중지하지 않는다. 동일한 동작을 실행하려면 `Lifecycle.repeatOnLifecycle` 블록에서 Flow를 수집해야 한다.

## `shareIn`을 사용하여 Cold Flow를 Hot Flow로 만들기

`StateFlow`는 Hot Flow로, Flow가 수집되는 동안 또는 가비지 컬렉션 루트에서 다른 참조가 있는 경우 메모리에 남아 있다. `shareIn` 연산자를 사용하여 Cold Flow를 Hot Flow로 전환할 수 있다.

각 수집기<small>(collector)</small>에서 새 Flow를 만들 필요 없이 [Kotlin Flow](https://developer.android.com/kotlin/flow)에서 예로 생성한 `callbackFlow`를 사용하면 Firestore에서 가져온 데이터를 `shareIn`을 통해 수집기끼리 공유할 수 있다. 다음을 전달해야 한다:

- Flow를 공유하는 데 사용되는 `CoroutineScope`. Shared Flow를 필요한 만큼 유지하기 위해 이 Scope는 소비자보다 오래 지속되어야 한다.
- 각 새 수집기로 재생<small>(replay)</small>할 item의 수
- 시작 동작 정책<small>(start behavior policy)</small>

```kotlin
class NewsRemoteDataSource(...,
    private val externalScope: CoroutineScope,
) {
    val latestNews: Flow<List<ArticleHeadline>> = flow {
        ...
    }.shareIn(
        externalScope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )
}
```

위 예시에서 `latestNews` Flow는 마지막으로 내보낸 item을 새 수집기로 재생<small>(replay)</small>하고 `externalScope`가 활성 상태<small>(alive)</small>이고 활성 수집기<small>(active collector)</small>가 있는 한 활성 상태로 유지된다. `SharingStarted.WhileSubscribed()` 시작 정책<small>(start policy)</small>은 활성 구독자가 있는 동안 업스트림 생산자를 활성 상태로 유지한다. 다른 시작 정책도 사용할 수 있다. 예를 들면, `SharingStarted.Eagerly`를 사용하여 생산자를 즉시 시작하거나, `SharingStarted.Lazily`를 사용하여 첫 번째 구독자가 표시된 후에 공유를 시작하고 Flow를 영구적으로 활성 상태로 유지할 수 있다.

> 참고: `externalScope`의 패턴을 자세히 알아보려면 [이 글](https://medium.com/androiddevelopers/coroutines-patterns-for-work-that-shouldnt-be-cancelled-e26c40f142ad)을 확인하자.

## SharedFlow

`shareIn` 함수는 수집하는 모든 소비자에게 값을 내보내는 Hot Flow인 `SharedFlow`를 반환한다. `SharedFlow`는 `StateFlow`의 유연한 구성 일반화<small>(highly-configurable generalization)</small>이다.

`shareIn`을 사용하지 않고 `SharedFlow`를 만들 수 있다. 예를 들어 `SharedFlow`를 사용하면 모든 콘텐츠가 주기적으로 동시에 새로고침되도록 앱의 나머지 부분에 틱을 전송할 수 있다. 최신 뉴스를 가져오는 것 외에도, 좋아하는 주제 컬렉션으로 사용자 정보 섹션을 새로고침할 수도 있다. 다음 코드에서 `TickHandler`는 다른 클래스가 콘텐츠를 새로고침할 시기를 알 수 있도록 `SharedFlow`를 노출합니다. `StateFlow`의 경우처럼 클래스에서 `MutableSharedFlow` 타입의 backing property를 사용하여 item을 Flow로 보낸다.

```kotlin
// Class that centralizes when the content of the app needs to be refreshed (앱 콘텐츠를 새로 고쳐야 할 때 중앙 집중화하는 클래스)
class TickHandler(
    private val externalScope: CoroutineScope,
    private val tickIntervalMs: Long = 5000
) {
    // Backing property to avoid flow emissions from other classes (다른 클래스에서의 Flow 전송을 막기 위한 Backing property)
    private val _tickFlow = MutableSharedFlow<Unit>(replay = 0)
    val tickFlow: SharedFlow<Event<String>> = _tickFlow

    init {
        externalScope.launch {
            while(true) {
                _tickFlow.emit(Unit)
                delay(tickIntervalMs)
            }
        }
    }
}

class NewsRepository(
    ...,
    private val tickHandler: TickHandler,
    private val externalScope: CoroutineScope
) {
    init {
        externalScope.launch {
            // Listen for tick updates
            tickHandler.tickFlow.collect {
                refreshLatestNews()
            }
        }
    }

    suspend fun refreshLatestNews() { ... }
    ...
}
```

다음과 같은 방법으로 `SharedFlow` 동작을 커스텀할 수 있다.

- `replay`를 사용하면 이전에 내보낸 여러 값을 새 구독자를 위해 다시 보낼 수 있다.
- `onBufferOverflow`를 사용하면 버퍼가 전송할 item으로 가득 찬 경우에 적용할 정책을 지정할 수 있다. 기본값은 호출자를 정지<small>(suspend)</small>시키는 `BufferOverflow.SUSPEND`다. 다른 옵션은 `DROP_LATEST` 또는 `DROP_OLDEST`이다.

또한 `MutableSharedFlow`에는 활성 수집기<small>(active collector)</small>의 수가 포함된 `subscriptionCount` 프로퍼티가 있어서 비즈니스 로직을 적절하게 최적화할 수 있다. `MutableSharedFlow`에는 Flow에 전송된 최신 정보를 재생하지 않으려는 경우를 위한 `resetReplayCache` 함수도 있다.

## References

- https://developer.android.com/kotlin/flow/stateflow-and-sharedflow