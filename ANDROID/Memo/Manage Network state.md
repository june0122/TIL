# 네트워크 연결 상태 관리

<p align = 'center'>
<img width = '650' src = 'https://user-images.githubusercontent.com/39554623/180914798-f4a86d71-f24a-4801-ac15-d75404fd9df8.jpeg'>
</p>

우리가 사용하는 많은 앱들이 REST API를 통해 서버로부터 데이터를 받아오는 등의 네트워크 작업을 하기 때문에 사용자의 앱이 네트워크에 연결되어 있는가 확인하는 작업은 매우 중요합니다. 현재 네트워크 연결 상태를 실시간으로 사용자에게 UI를 통해 알려주지 않으면 사용자가 네트워크가 안정적이지 않은 환경이거나 실수로 네트워크를 꺼버린 상태인데도 서버와의 통신이 필요한 작업을 계속 시도하면서 *"왜 안돼는거야!"* 라며 답답함을 느낄 수 있기 때문이죠.

이번 글에서는 사용자가 네트워크에 연결되어 있지 않은 상태에서 계속해서 네트워크 작업을 요청하는 불상사가 발생하지 않도록 **실시간으로 네트워크 연결 상태를 UI에 표시하는 방법**을 정리하고자 합니다.

이 글에 정리한 코드를 통해 구현한 결과물은 아래와 같습니다.

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/180229151-04467454-32f3-44b7-a169-ce2912c8a4be.gif'>
</p>

## 프로젝트 구조

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/180915606-356ff985-1a43-4fe7-9d61-a76bb4a04b1c.png'>
</p>

네트워크 연결 상태를 확인하고 UI로 보여주기 위해선 보라색으로 선택된 6개의 클래스가 필요하며, 아래의 기술들에 대한 이해가 필요합니다.

- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- ViewModel
- LiveData
- Coroutines
- Flow
- DataBinding

## Dependency Injection

네트워크 연결 관련 핵심 로직들은 Dependency Injection 라이브러리인 Hilt를 통해서 제공됩니다. *NetworkChecker*는 앱이 실행되는 동안 계속 네트워크 연결을 감지하기 위해 사용되므로 `@InstallIn` 어노테이션을 통해 `SingletonComponent`에 *NetworkChecker*를 설치합니다. 이를 통해 *NetworkChecker*는 단일 인스턴스로 생성되고 앱 전역에서 사용될 수 있습니다.

`@InstallIn` 어노테이션은 **Hilt가 컴포넌트를 생성할 때 해당 어노테이션이 달린 클래스가 포함되어야 하는 컴포넌트를 선언**하는 역할을 하며, `@Module` 또는 `@EntryPoint` 어노테이션이 달린 클래스에만 사용할 수 있습니다.

그런데 `@InstallIn` 어노테이션을 통해 *NetworkModule* 클래스가 `SingletonComponent`에 포함되어야 한다고 선언을 했는데 `@Provides` 함수에 `@Singleton` 어노테이션을 왜 또 다시 붙여야 하는가 궁금할 수 있습니다. 이 궁금증은 스택오버플로우의 [이 글](https://stackoverflow.com/q/72160844/12364882)에 잘 정리가 되어있으니 참고하면 좋을 것 같습니다.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    ...

    @Singleton
    @Provides
    fun provideNetworkChecker(
        @ApplicationContext context: Context
    ): NetworkChecker = NetworkChecker(context)
}
```

## 네트워크 연결 관련 핵심 로직

### NetworkState

네트워크의 상태를 추적하기 위해 sealed class를 이용하여 `None`, `Connected`, `Reconnected`, `NotConnected` 4가지의 *NetworkState*를 정의합니다. `Reconnected` 상태가 있는 이유는 아래에서 서술하겠습니다.

```kotlin
sealed class NetworkState {
    object None : NetworkState()
    object Connected : NetworkState()
    object Reconnected : NetworkState()
    object NotConnected : NetworkState()
}
```

### NetworkChecker

그리고 네트워크 연결 관련 핵심 로직을 담은 클래스 *NetworkChecker*를 정의합니다.

#### 인터넷 연결 확인

먼저 Hilt에서 사전 정의된 한정자<small>(Predefined qualifier)</small>인 `@ApplicationContext`를 통해 `Context`를 *NetworkChecker*에 제공합니다.

이렇게 제공받은 `Context`에서 `getSystemService()`를 통해 시스템 서비스 객체를 얻을 수 있는데, 이를 통해 `ConnectivityManager` 인스턴스를 생성합니다. 이 인스턴스를 통해 **앱의 현재 기본 네트워크에 관한 참조**를 가져올 수 있습니다.

그리고 `ConnectivityManager`로부터 `getNetworkCapabilities()`를 통해 `NetworkCapabilities` 객체를 얻을 수 있습니다. 이 객체에는 객체에는 전송(Wi-Fi, 셀룰러, 블루투스) 및 네트워크에서 사용할 수 있는 기능과 같은 네트워크 속성 정보가 포함됩니다.

`NetworkCapabilities.hasTransport(int)`를 쿼리하여 기본 네트워크가 사용 중인 전송 방법을 알아볼 수 있는데 WIFI와 CELLULAR(모바일 데이터) transportType들을 담은 리스트 중에서 기본 네트워크가 하나라도 사용하고 있다면 *true*를 반환하는 `isNetworkAvailable()` 메서드를 정의합니다.

```kotlin
class NetworkChecker @Inject constructor(
    applicationContext: Context
) {
    private val connectivityManager: ConnectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val validTransportTypes = listOf(
        NetworkCapabilities.TRANSPORT_WIFI,
        NetworkCapabilities.TRANSPORT_CELLULAR
    )

    private fun isNetworkAvailable(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            return validTransportTypes.any { capabilities.hasTransport(it) }
        }

        return false
    }

    ...
}
```

여기까지 작성한 코드만으로는 네트워크의 순간적인 상태만을 쿼리하기 때문에 디버깅 이외에는 유용하지 않습니다. 새로운 네트워크에 연결이 되거나 네트워크 연결이 끊길 때 알림을 받기 위해서는 [NetworkCallback](https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback)을 등록해야 합니다. 

#### 네트워크 이벤트 수신 대기

네트워크 이벤트를 알아보려면 네트워크 변경에 대한 알림에 사용되는 `NetworkCallback` 클래스를 `ConnectivityManager.registerDefaultNetworkCallback(NetworkCallback)` 및 `ConnectivityManager.registerNetworkCallback(NetworkCallback)`과 함께 사용합니다. 이 두 가지 메서드의 차이점은 스택오버플로우의 [이 글](https://stackoverflow.com/q/53863034/12364882)을 참고하시면 좋을 것 같습니다.

먼저 `NetworkCallback` 클래스의 콜백 메서드 2가지를 살펴보겠습니다.
- [**onAvailable**](https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onAvailable(android.net.Network)) 콜백 메서드는 프레임워크가 연결되고 사용할 준비가 된 새로운 네트워크를 선언하면 호출됩니다.
- [**onLost**](https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onLost(android.net.Network)) 콜백 메서드는 네트워크 연결이 끊기거나 더 이상 이 요청 또는 콜백을 충족하지 않을 때 호출됩니다.

우선 네트워크 변경 콜백을 바인딩하고 네트워크 상태를 저장하는 코드를 살펴봅시다.

```kotlin
class NetworkChecker @Inject constructor(
    applicationContext: Context
) {

    ...

    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.None)
    val networkState: StateFlow<NetworkState> = _networkState

    private var previousNetworkState: NetworkState = NetworkState.None

    init {
        initiateNetworkState()
        registerNetworkCallback(connectivityManager)
    }

    private fun initiateNetworkState() {
        _networkState.value = if (isNetworkAvailable()) {
            NetworkState.Connected.also { previousNetworkState = it }
        } else {
            NetworkState.NotConnected.also { previousNetworkState = it }
        }
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if (previousNetworkState == NetworkState.NotConnected) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(RECONNECTION_UI_VISIBLE_TIME) // 3000L
                    _networkState.value = NetworkState.Connected
                }
                _networkState.value = NetworkState.Reconnected.also {
                    previousNetworkState = it
                }
            } else {
                _networkState.value = NetworkState.Connected.also {
                    previousNetworkState = it
                }
            }
        }
        override fun onLost(network: Network) {
            super.onLost(network)
            CoroutineScope(Dispatchers.Main).launch {
                delay(RECONNECTION_DELAY) // 1000L
                if (isNetworkAvailable().not()) {
                    _networkState.value = NetworkState.NotConnected
                    previousNetworkState = NetworkState.NotConnected
                }
            }
        }
    }

    private fun registerNetworkCallback(manager: ConnectivityManager) {
        val networkRequestBuilder = NetworkRequest.Builder().apply {
            validTransportTypes.forEach { addTransportType(it) }
        }
        manager.registerNetworkCallback(networkRequestBuilder.build(), networkCallback)
    }
}
```

*init 블럭* 에서 초기화하는 동안 현재 네트워크의 상태가 계산되고 네트워크 변경 콜백이 바인딩되므로 `networkState`는 항상 Hilt에서 제공하는 단일 인스턴스에서 가장 최근의 네트워크 상태를 유지합니다.

`networkState`는


```kotlin
override fun onLost(network: Network) {
    super.onLost(network)
    Log.e("TEST", "isNetworkAvailable: ${isNetworkAvailable()}")
    CoroutineScope(Dispatchers.Main).launch {
        delay(RECONNECTION_DELAY) // 1000L
        Log.e("TEST", "isNetworkAvailable: ${isNetworkAvailable()}")
        if (isNetworkAvailable().not()) {
            _networkState.value = NetworkState.NotConnected
            previousNetworkState = NetworkState.NotConnected
        }
    }
}
```

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/180996778-50e9e579-a638-4551-bbbd-80f476379ebd.png'>
</p>


```console
Wifi, 모바일 데이터가 둘 다 켜진 상태에선 Wifi가 우선적으로 잡힘, Wifi를 끄면 모바일 데이터로 연결

Wifi -> 모바일 데이터 변경 시 onLost 호출 O
모바일 데이터 -> Wifi 변경 시 onLost 호출 X

Wifi -> LTE 변경 시 onLost가 호출되어 networkStatus를 바로 NotConnected로 변경하면
LTE에 연결되어 있는데도 NetworkState가 NotConnected가 되어버린다.

로그를 확인해보면 onLost 콜백 메서드가 호출되자마자 호출된 isNetworkAvailable()의 값은 false인 반면
1초쯤 뒤에 다시 isNetworkAvailable()의 값을 확인해보면 true로 변경되어 있다.
```



## ViewModel

```kotlin
@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkChecker: NetworkChecker
) : ViewModel() {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState

    init {
        viewModelScope.launch {
            networkChecker.networkState.collectLatest {
                _networkState.value = it
            }
        }
    }
}
```

## MainActivity

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DataLoadListener {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binding.lifecycleOwner = this@MainActivity
        binding.viewModel = mainViewModel

        val view = binding.root
        setContentView(view)

        ...
    }

    ...
}
```

## BindingAdapters

```kotlin
object BindingAdapters {

    @JvmStatic
    @BindingAdapter("networkStatusBarBackground")
    fun setNetworkStatusBarBackground(view: ConstraintLayout, status: NetworkState?) {
        val primaryColorTypedValue = TypedValue()
        view.context.theme.resolveAttribute(R.attr.colorPrimary, primaryColorTypedValue, true)

        val backgroundColorTypedValue = TypedValue()
        view.context.theme.resolveAttribute(R.attr.colorBackground, backgroundColorTypedValue, true)

        if (status == NetworkState.Reconnected) view.setBackgroundResource(primaryColorTypedValue.resourceId)
        else view.setBackgroundResource(backgroundColorTypedValue.resourceId)
    }

    @JvmStatic
    @BindingAdapter("networkStatusBarVisibility")
    fun setNetworkStatusBarVisibility(view: ConstraintLayout, status: NetworkState?) {
        when (status) {
            NetworkState.Reconnected -> {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(RECONNECTION_UI_VISIBLE_TIME) // 3000L
                    view.visibility = View.GONE
                }
                view.visibility = View.VISIBLE
            }
            NetworkState.NotConnected -> {
                view.visibility = View.VISIBLE
            }
            else -> {
                view.visibility = View.GONE
            }
        }
    }

    @JvmStatic
    @BindingAdapter("networkStatusText")
    fun setNetworkStatusText(view: TextView, status: NetworkState?) {
        when (status) {
            NetworkState.Reconnected -> {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.Text))
                view.setText(R.string.network_reconneted)
            }
            NetworkState.NotConnected -> {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.TextLight))
                view.setText(R.string.network_not_connected)
            }
            else -> {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.Text))
                view.setText(R.string.network_not_connected)
            }
        }
    }
}
```

## activity_main.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="viewModel"
            type="com.june0122.wakplus.ui.main.MainViewModel" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="?attr/colorBackground"
        tools:context=".ui.main.MainActivity">

        ...

        <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/layout_network_status"
            networkStatusBarBackground="@{viewModel.networkState}"
            networkStatusBarVisibility="@{viewModel.networkState}"
            android:layout_width="0dp"
            android:layout_height="@dimen/network_status_bar_height"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent">

            <TextView
                android:id="@+id/tv_network_status"
                networkStatusText="@{viewModel.networkState}"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="12sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                tools:text="@string/network_not_connected" />
        </androidx.constraintlayout.widget.ConstraintLayout>

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/180229114-c78c9fa5-85ac-40d0-a0b3-4de9e2d83fad.gif'>
</p>

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/180229156-f22b2d47-50e1-4e18-8179-d14d080994dd.gif'>
</p>

## References

- Android developers: [네트워크 상태 읽기](https://developer.android.com/training/basics/network-ops/reading-network-state)
- [Network Connectivity on Modern Android Development with Jetpack Compose](https://attilaakinci.medium.com/network-connectivity-on-compose-a35f6efa1a5c)