# Kotlin Flows in practice

> Flow: 데이터의 스트림을 모델링하는 코틀린 타입(Kotlin type for modeling streams of data)

> Flow: 코루틴에서 단일 값만 반환하는 suspend 함수와 달리, 여러 값을 순차적으로 전송할 수 있는 타입(In coroutines, a flow is a type that can emit multiple values sequentially, as opposed to suspend functions that return only a single value.)

- Reactive programming<small>(반응형 프로그래밍)</small>
- Creating, transforming, and observing flows
- Flows in the Android UI
- Testing flows

#### Reactive programming<small>(반응형 프로그래밍)</small>

관찰하는 쪽이 관찰 대상의 변화에 자동으로 반응하는 패턴을 사용하는 시스템

#### Flow의 용어

> Kotlin `Flow<T>`: Flow는 `T`와 같이 어떤 유형이든 될 수 있다. (예로 유저 데이터나 UI 상태)

- Producer<small>(생산자)</small>: flow에 데이터를 입력<small>(emit)</small>
  - Data source 또는 Repository
- Consumer<small>(소비자)</small>: flow로부터 데이터를 수집<small>(collect)</small>
  - UI 레이어

## Creating Flow

<p align = 'center'>
<img width = '800' src = 'https://user-images.githubusercontent.com/39554623/160439863-a078358a-56a2-413c-a970-57c8b8990a04.png'>
</p>

대부분의 경우 Flow를 직접 만들 필요는 없다. 데이터 소스에서 사용하는 라이브러리는 코루틴과 Flows에 이미 통합되어 있기 때문이다.

안드로이드 개발에서 자주 사용하는 DataSource, Retrofit, Room, WorkManager 등은 데이터가 담긴 댐 역할을 한다. 라이브러리가 Flows를 사용하여 데이터를 제공하고 개발자는 데이터 생성 방법을 몰라도 파이프에 연결하기만 하면 된다.

### Room을 통한 예시

#### Room DAO

```kotlin
@Dao
interface CodelabsDao {

    @Query("SELECT * FROM codelabs")
    fun getAllCodelabs(): Flow<List<Codelab>>
}
```

`X` 타입의 Flow를 노출하여 데이터베이스 변경 사항을 알릴 수 있다. Room 라이브러리는 생산자 역할을 맡아서 업데이트가 있을 때마다 쿼리의 내용을 전송<small>(emit)</small>한다.

### 직접 Flow을 생성하기

#### Flow builder

UserMessagesDataSource에서 수시로 앱에서 온 메시지를 확인하려고 하는 상황을 예시로:

```kotlin
class UserMessagesDataSource(
    private val messagesApi: MessagesApi,
    private val refreshIntervalMs: Long = 5000
) {
    // Message List 타입의 flow로 사용자 메시지를 노출할 수 있다.
    val latestMessages: Flow<List<Message>> = flow { // Flow 빌더를 통해 flow 생성
        // Flow 빌더는 suspend 블록을 매개변수로 받기에, suspend 함수를 호출할 수 있다.
        // flow가 코루틴의 context에서 실행되기 때문이다.
        while(true) {
            val userMessages = messagesApi.fetchLatestMessages() // API로부터 메시지를 가져오기
            emit(userMessages) // emit suspend 함수를 통해 flow에 결과(메시지)를 emit한다. 그러면 collector가 item을 받을 때까지 코루틴을 중단한다.
            delay(refreshIntervalMs) // 일정 시간동안 코루틴을 중단한다.
        }
    }
}
```

위 예제의 flow에서는 연산이 동일한 코루틴 내에서 순차적으로 실행된다. `while(true)` 루프로 인해 관찰자가 떠나고 item의 수집을 멈출 때까지 latestMessages를 계속 가져온다. 그리고 Flow 빌더에 전달된 suspend 블록은 *생산자 블록*이라고도 한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/160449016-15c53094-97a5-4ba6-9042-d497a2462814.png '>
</p>

## Collecting Flows

안드로이드에서 생산자와 소비자 계층은 그 이후의 계층 요구 사항에 맞게 데이터 스트림을 수정할 수 있다. Flow를 변환하려면 <b>중간 연산자<small>(intermediate operator)</small></b>를 사용한다.

#### Original flow

```kotlin
val userMessages: Flow<MessagesUiModel> =
    userMessagesDataSource.latestMessages
```

#### Flow.map

latestMessages 스트림이 flow의 시작점이라고 하면, `map` 연산자를 사용해서 데이터를 다른 타입으로 변환할 수 있다.

```kotlin
val userMessages: Flow<MessagesUiModel> =
    userMessagesDataSource.latestMessages
        .map { userMessages ->
            userMessages.toUiModel()
        }
```

map 람다 내부에서 데이터 소스에서 받은 원본 메시지를 MessagesUiModel로 변환한다. 이는 앱의 이 계층에 대해 더욱 우수한 추상화를 제공한다.

#### Flow.filter

각 연산자는 기능에 따라 데이터를 전송<small>(emit)</small>하는 새로운 flow를 생성한다.

스트림을 필터링하여 중요한 알림이 포함된 해당 메시지의 flow를 가져올 수 있다.

```kotlin
val userMessages: Flow<MessagesUiModel> =
    userMessagesDataSource.latestMessages
        .map { userMessages ->
            userMessages.toUiModel()
        }
        .filter { messagesUiModel ->
            messagesUiModel.containsImportantNotifications()
        }
```

#### Flow.catch

그러면 스트림에서 발생하는 오류는 어떻게 처리할까?

`catch` 연산자는 업스트림 Flow에서 item을 처리하는 동안 발생할 만한 예외를 찾아낸다.

```kotlin
val userMessages: Flow<MessagesUiModel> =
    userMessagesDataSource.latestMessages
        .map { userMessages ->
            userMessages.toUiModel()
        }
        .filter { messagesUiModel ->
            messagesUiModel.containsImportantNotifications()
        }
        .catch { e ->
            analytics.log("Error loading reserved event")
        }
```

<p align = 'center'>
<img height = '220' src = 'https://user-images.githubusercontent.com/39554623/160448923-6905f6ce-e797-4c39-88b2-7cae2c1e3910.png'>
<img height = '220' src = 'https://user-images.githubusercontent.com/39554623/160448950-fa62de8d-23a5-4ed2-9563-083840b6f165.png'>
</p>

- 업스트림 Flow는 생산자 블록에서 생성한 flow이고 현재 연산자 전에 이들을 호출한다.
- 다운스트림 Flow는 현재 연산자 이후에 발생하는 모든 것을 지칭한다.

```kotlin
val userMessages: Flow<MessagesUiModel> =
    userMessagesDataSource.latestMessages
        ...
        .catch { e ->
            analytics.log("Error loading reserved event")
            if (e is IllegalArgumentException) throw e
            else emit(emptyList())
        }
```

`catch`는 필요하거나 새 값을 전송할 때 예외를 다시 발생시킬 수 있다. 예를 들어 위의 코드는 IllegalArgumentException을 다시 발생시키지만, 다른 예외가 발생하면 emptyList를 전달한다.

## Observing Flows

일반적으로 flow 수집은 UI 계층에서 일어난다. 화면에 데이터를 표시할 위치이기 때문이다. 우리의 예시에서는 리스트에 최신 메시지를 표시할 것이다. 

#### Flow.collect

값을 수신하기 시작하기 위해서는 **terminal 연산자**를 사용해야 한다. 스트림의 모든 값을 전송<small>(emit)</small> 즉시 가져오려면 `collect`를 사용해야 한다.

```kotlin
userMessage.collet { messages ->
    listAdapter.submitList(messages)
}
```

`collect`는 새로운 값이 생길 때마다 호출되는 함수를 매개변수로 받는다. 그리고 이건 suspend 함수이므로 코루틴 내에서 실행해야 한다.

terminal 연산자를 flow에 적용하면, 필요에 따라 flow가 생성되고 값을 전송하기 시작한다. 반면, intermediate 연산자는 일련의 연산자<small>(chain of operations)</small>만 설정하며 item을 flow로 전송했을 때 lazy하게 연산자를 실행한다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/160455407-ca7ce39c-b505-445e-9e43-b347a25d66ac.png'>
</p>

userMessage에서 collect를 호출할 때마다 새로운 flow<small>(pipe)</small>가 생성된다. 생산자 블록은 정해진 간격에 따라 API에서 메시지를 새로 고침하기 시작한다. 코루틴 용어에서 이런 flow를 **Cold flow**라고 한다. 필요에 따라 생성되고 관찰되는 중에만 데이터를 전송하기 때문이다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/160456189-8f3d7a87-3ee8-492f-bfb9-1536a5b01555.png'>
</p>

## Flows in Android UI

Android UI에서 flow를 최적으로 수집하는 방법을 알아본다. 고려해야 할 점은 크게 두 가지가 있다.

1. 앱이 백그라운드에 있을 때 리소스를 낭비하지 않아야 한다.
2. 구성 변경<small>(configuration change)</small>와 관련이 있다.

MessagesActivity에서 화면에 메시지 리스트를 표시하려고 한다고 생각해보자. 얼마나 오랫동안 flow에서 수집해야 할까?

```kotlin
class MessagesActivity : AppCompatActivity() {
    val viewModel: MessagesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
    }
}
```

화면에 정보를 표시하지 않을 때는 flow에서 수집해서는 안 된다. 여기에는 여러 옵션이 있는데, 모든 옵션이 UI 수명 주기를 인식한다.

옵션으로는 LiveData 또는 lifecycle coroutine-specific API를 사용할 수 있다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/160457295-a9960359-c9c3-4420-8dbe-620ad4cc2994.png'>
</p>

#### Flow.asLiveData

```kotlin
// import androidx.lifecycle.asLiveData

class MessagesViewModel(repository: MessagesRepository) : ViewModel() {
    val userMessages = repository.userMessages.asLiveData()
    ...
}
``` 

`asLiveData` flow 연산자는 flow를 LiveData로 변환해서 UI가 화면에 표시되는 동안에만 item을 관찰한다. 이러한 변환은 ViewModel 클래스에서도 가능하다. UI에서는 평소처럼 LiveData를 소비한다.

```kotlin
class MessagesActivity : AppCompatActivity() {
    ...
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        viewModel.userMessages.observe(this) { messages ->
            listAdapter.submitList(messages)
        }
    }
}
```

이건 다른 기술을 추가하는 거라서 약간의 편법이기에 꼭 필요한 건 아니다.

#### Lifecycle.repeatOnLifecycle

UI 계층에서 flow를 수집할 때 `repeatOnLifecycle`을 사용하는 게 좋다. 이는 Lifecycle.State를 매개변수로 받는 suspend 함수이다. 이 API는 수명 주기를 인식하며, 수명 주기가 해당 상태에 도달하면 블록을 전달할 새 코루틴이 자동으로 시작된다. 그러면 수명 주기가 그 상태 아래로 떨어지면 진행 중인 코루틴이 취소된다.

블록 내부는 코루틴의 컨텍스트이므로 `collect`를 호출할 수 있다. `repeatOnLifecycle`은 suspend 함수이므로 코루틴에서 호출해야 한다. 액티비티 안에 있기 때문에 `lifecycleScope`를 사용하여 시작할 수 있다.

```kotlin
 // import androidx.lifecycle.repeatOnLifecycle

 class MessagesActivity : AppCompatActivity() {
     ...
     override fun onCreate(savedInstanceState: Bundle?) {
         ...
         lifecycleScope.launch {
↛            repeatOnLifecycle(Lifecycle.State.STARTED)
↛            viewModel.userMessages.collect { messages ->
                  listAdapter.submitList(messages)
             }
         }
     }
 }
```

위의 코드에서 액티비티의 `onCreate`에서 `repeatOnLifecycle` 함수를 호출하듯이, 수명 주기가 초기화되면 `repeatOnLifecycle` 함수를 호출하는 것이 좋다. `repeatOnLifecycle`의 재시작 가능 동작<small>(restartable behavior)</small>은 UI 수명 주기를 자동으로 고려한다.

중요하게 주목해야 할 부분은 `repeatOnLifecycle`를 호출하는 코루틴은 수명 주기가 파괴될 때까지 실행을 다시 시작하지 않는다는 것이다.

만약 여러 flow로부터 수집해야 할 경우, `repeatOnLifecycle` 블록에서 `launch`를 사용해 여러 코루틴을 생성해야 한다.

```kotlin
 lifecycleScope.launch {
     repeatOnLifecycle(Lifecycle.State.STARTED) {
         launch {
↛             viewModel.userMessages.collect { ... }
         }
         launch {
↛             otherFlow.collect { ... }
         }
     }
 }
```

#### Lifecycle.flowWithLifecycle

`flowWithLifecycle`는 수집할 flow가 하나뿐일 때 `repeatOnLifecycle` 대신 사용할 수 있다.

```kotlin
 // import androidx.lifecycle.flowWithLifecycle
 
 class MessagesActivity : AppCompatActivity() {
     ...
     override fun onCreate(savedInstanceState: Bundle?) {
         ...
         lifecycleScope.launch {
↛            viewModel.userMessages
↛                .flowWithLifecycle(lifecycle, State.STARTED)
                 .collect { messages ->
                     listAdapter.submitList(messages)
                 }
         }
     }
 }
```

이 API는 수명 주기가 대상 상태에서 들어가고 나갈 때 item을 전송하고 기본 생산자<small>(underlying producer)</small>를 취소한다.

이 동작을 시각적으로 확인해보기 위해 액티비티 수명 주기를 생성 시점부터 살펴보도록 하자.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/160469752-f84fe97a-e83e-4f08-965c-c077f46ccb8c.png'>
</p>

사용자가 홈 버튼을 누르면 백그라운드로 전송되고, 액티비티에서 onStop 신호를 받은 다음 onStart가 호출되었을 때 다시 앱을 연다.

STARTED 상태로 `repeatOnLifecycle`을 호출하면 UI가 화면에 표시되는 동안 flow 전송<small>(emissions)</small>을 처리하고 앱이 백그라운드로 이동하면 수집이 취소된다.

#### Unsafe collection from the Activity

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/160467949-1d2e3ad7-6f81-4bbe-a974-6331bb924227.png'>
</p>

`repeatOnLifecycle`과 `flowWithLifecycle`은 lifecycle-runtime-ktx 라이브러리의 안정화 2.4 버전에 새로 추가된 API이다. 새로운 API이므로 Android UI에서 다른 방식으로 flow를 수집할 수도 있다.

예를 들어, lifecycleScope에서 시작한 코루틴에서 바로 수집할 수도 있지만, 아래와 같은 방식의 flow 수집은 위험할 수 있다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/160468325-c5f6405f-39a9-4af0-a02f-0c3260efede5.png'>
</p>

위 코드는 flow에서 수집하고 UI 요소를 업데이트하는데, 앱이 백그라운드에 있어도 마찬가지라는 것이 문제점이다. 사실 위와 같은 경우뿐만 아니라 `LifecycleCoroutineScope.launchWhenX` API 제품군도 비슷한 문제가 있다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/160468785-6f9287d9-e4ca-430e-bee7-a25a50cb8db2.png'>
</p>

리소스를 낭비하지 않기 위해선 item이 화면에 표시되지 않을 때는 flow에서 수집해서는 안된다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/160469611-6d7dfd2e-1791-46b7-bbaa-e0e4c08c6d85.png'>
</p>

`lifecycleScope.launch`에서 직접 수집하는 경우 액티비티가 백그라운드에 있을 때에도 계속 flow 업데이트를 받는다. 이는 리소스의 낭비일 뿐만 아니라 위험하기도 하다. 예를 들어, 앱이 백그라운드일 때 대화 상자를 표시하면 충돌을 일으킨다. 이 문제를 해결하려면 `onStart`에서 수동으로 수집을 시작하고 `onStop`에서 수집을 중단해야 한다. 이 방법도 문제는 없지만, `repeatOnLifecycle`을 사용하면 모든 보일러 플레이트가 제거된다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/160470324-2a96ca53-0f19-41dd-8fa4-6a6e053025b9.png'>
</p>

`launchWhenStarted`를 대안으로 고려할 경우, `lifecycleScope.launch`보다는 나은 편이다. 왜냐하면 앱이 백그라운드에 있을 때 flow 수집을 중단하기 때문이다. 하지만 이 방법은 flow 생산자를 계속 활성화시켜서 화면에 표시되지 않을 item으로 메모리를 채울 수 있는 백그라운드에서 item을 전달할 수도 있다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/160470914-0bf50b5f-c232-45eb-a8a0-81d1e8ece20b.png'>
</p>

UI에서 flow 생산자의 구현 방법을 알 수 없으므로 항상 `repeatOnLifecycle`이나 `flowWithLifecycle`을 안전하게 사용하는 것이 좋다. UI가 백그라운드에 있을 때 item을 수집하지 않고 flow 생산자를 살려두기(keep active) 위해서이다.

앱이 백그라운드로 갔을 때 flow 수집을 이렇게 최적화할 경우 구성 변경<small>(Configuration changes)</small> 시의 몇 가지 요령을 알아보자.

### StateFlow

Flow를 뷰에 노출하면 수명 주기가 서로 다른 두 요소 사이에 데이터를 전달해야 한다는 걸 고려해야 한다. 모든 수명 주기가 해당되는 건 아니지만 액티비티와 프래그먼트의 수명 주기는 까다로울 수 있다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/160472604-b3fb2221-e046-4cac-9ef8-de0f291b28c1.png'>
</p>

중요한 예를 들어서 설명하자면 기기가 회전되었거나 구성 변경을 수신하면 모든 액티비티는 다시 시작하지만 ViewModel은 그렇지 않고 계속 남아 있다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/160473198-768e9717-5ba8-4add-b433-214e3232da48.png'>
</p>

ViewModel에서 모든 flow를 노출하는 것은 아니다. 예를 들어 위와 같은 Cold flow는 처음으로 수집될 때마다 다시 시작하기 때문에 Repository는 한 번 회전 후 다시 호출된다.

우리에겐 재생성의 횟수와는 관계없이 데이터를 보관하고 있다가 여러 컬렉터 사이에 공유할 **일종의 버퍼**가 필요하다. **StateFlow**가 바로 이런 목적으로 만들어졌다.

<p align = 'center'>
<img height = '270' src = 'https://user-images.githubusercontent.com/39554623/160473590-b30d38e2-6485-4a63-9e13-f6de446d0977.png'>
<img height = '270' src = 'https://user-images.githubusercontent.com/39554623/160474809-e2fb7805-5934-4397-a63a-1703bba04aea.png'>
</p>

StateFlow는 비유하자면 물탱크와 같다. 컬렉터가 없더라도 데이터를 보관한다. 일회성 수집이 아닐 수 있으므로 액티비티나 프래그먼트와 함께 사용하는 게 안전하다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/160473649-659739ff-c6d4-451d-b7d0-960be6f6cdbd.png'>
</p>

StatFlow의 여러 버전을 사용하고 필요할 때마다 값을 업데이트할 수 있다. 예를 들어 위와 같은 코루틴에서 말이다. 하지만 반응형이라고 하기는 어려운 코드이기에 개선이 필요하다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/160473683-f9ea3505-73e1-4d55-8c9a-d19a13a6ed5a.png'>
</p>

Flow를 StateFlow로 변환하면 StateFlow가 업스트림 Flow에서 모든 업데이트를 받아 최신 값을 저장한다. 컬렉터가 없거나 많을 수 있으므로 ViewModel에서 사용하기에 좋다. 여러 종류의 Flow가 있지만 StateFlow를 매우 정확하게 최적화할 수 있으므로 이걸 권장한다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/160473726-f7866ef9-8478-4750-8598-1ad4df4d524d.png'>
</p>

Flow를 StateFlow로 변환할 때 `stateIn` 연산자를 사용할 수 있다. 3개의 매개변수를 받는데, StateFlow에 항상 값이 있어야하기 때문에 *initialValue*가 있어야 하고, *coroutine scope*는 공유가 시작되는 시점을 제어하는데 여기에 ViewModel의 범위를 사용할 수 있다. 그리고 *started*가 흥미로운 요소인데 `WhileSubscribed(5000)`이 무슨 의미인지 살펴보자.

먼저 두 가지 시나리오를 살펴보자.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/160473773-2d7ebfb0-8d1b-4794-9c21-8f37ac96b04f.png'>
</p>

1. Flow의 컬렉터인 액티비티가 짧은 시간동안 파괴되었다가 재생성되는 회전 시나리오
2. 홈으로 이동해서 앱을 백그라운드에 넣는 시나리오

회전 시나리오에서는 최대한 빠르게 전환하려면 Flow를 다시 시작해서는 안 된다. 그러나 홈으로 이동하는 경우 배터리와 다른 리소스를 아끼기 위해 모든 Flow를 중단해야 한다.

그러면 어떤 시나리오인지는 어떻게 탐지할 수 있을까? 바로 <b>timeout<small>(시간 초과)</small></b>을 사용하면 된다.

StateFlow의 수집이 중단되었을 때 모든 업스트림 Flow를 즉시 중단하는 건 아니다. 예를 들자면 오히려 약 5초 정도 잠시 기다린다. timeout 전에 Flow를 수집하면 업스트림 Flow가 취소되지 않는다. `WhileSubscribed(5000)`이 바로 그런 일을 한다.

#### 앱이 백그라운드에 갔을 때의 반응

<p align = 'center'>
<img width = '900' src = 'https://user-images.githubusercontent.com/39554623/160477411-6dc5d03e-b1bf-41eb-8cfd-b7dfdcc7fe0a.png'>
</p>

- 홈 버튼을 누르기 전에 뷰가 업데이트를 수신하고 StateFlow는 정상적으로 업스트림 Flow를 생성한다.

#### 뷰가 중단되었을 때

<p align = 'center'>
<img width = '900' src = 'https://user-images.githubusercontent.com/39554623/160477444-e1ff7024-d2ec-4ded-90b2-95f779ce259c.png'>
</p>

- 뷰가 중단되면 수집이 즉시 종료된다. 그러나 StateFlow는 위에서 구성한 방법으로 인해<small>(`WhileSubscribed(5000)`)</small> 업스트림 Flow를 중단하는데 5초가 걸린다.

#### timeout이 지났을 때

<p align = 'center'>
<img width = '900' src = 'https://user-images.githubusercontent.com/39554623/160477457-0ebdf502-29e3-4edc-bd7c-b13531201831.png'>
</p>

- 제한시간이 지나면 업스트림 Flow가 취소된다.

#### 사용자가 다시 앱을 열 때

<p align = 'center'>
<img width = '900' src = 'https://user-images.githubusercontent.com/39554623/160477473-e97ebeec-f029-4f27-b192-64c93fb78295.png'>
</p>

- 사용자가 다시 앱을 열 경우, 업스트림 Flow가 자동으로 다시 시작된다.

#### 회전 시나리오의 경우

<p align = 'center'>
<img width = '900' src = 'https://user-images.githubusercontent.com/39554623/160477492-2db97a26-681b-456a-b0d9-89000445af9f.png'>
</p>

그러나 회전 시나리오에서 뷰는 잠시만 중단된다. 어쨌든 5초 이내이다. 따라서 StateFlow는 절대 복원되지 않고 모든 업스트림 Flow를 활성 상태로 유지하며 아무 일도 없었던 것처럼 사용자에게 회전 인스턴트를 보낸다.

#### 정리

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/160479789-61671e57-9784-4886-acb0-4d4cc6e3392e.png'>
</p>

StateFlow를 사용하여 ViewModel에서 Flow를 노출하거나 `asLiveData`를 사용해서 이와 동일한 작업을 실행하는 게 좋다.

## References

- Kotlin Flows in practice: https://youtu.be/fSB6_KE95bU
- Kotlin flows on Android: https://developer.android.com/kotlin/flow