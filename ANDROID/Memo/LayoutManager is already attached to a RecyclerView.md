# LayoutManager is already attached to a RecyclerView 에러

## 상황

- 하나의 액티비티에 2개의 프래그먼트가 하단 탭에 할당된 상태 <small>(HomeFragment와 SettingsFragment)</small>
- *HomeFragment → SettingFragment → HomeFragment*, 즉 홈 화면에서 설정 화면으로 갔다가 다시 홈 화면으로 돌아올 때 홈 화면이 표시되는

```kotlin
class HomeFragment : Fragment() {
    ...
    override fun onDestroyView() {
        super.onDestroyView()
        streamerRecyclerView.layoutManager = null
        snsRecyclerView.layoutManager = null
        contentRecyclerView.layoutManager = null
    }
    ...
}
```

- 하나의 액티비티에 2개의 프래그먼트가 하단 탭에 할당된 상태 <small>(HomeFragment와 SettingsFragment)</small>
- 설정 프래그먼트로 이동했다가 다시 홈 프래그먼트로 돌아왔을때 *LayoutManager is already attached to a RecyclerView* 에러가 발생
  - 홈 프래그먼트의 `onDestroyView()` 메서드에서 리사이클러뷰의 LayoutManager를 null로 처리하지 않았을 경우

## 원인

```kotlin
class HomeFragment : Fragment() {
    private val horizontalLayoutManager =
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    ...
}
```

- `horizontalLayoutManager`를 클래스의 필드에 인스턴스 변수로 정의해놓았기 때문에 클래스의 인스턴스가 생성될 때 `LinearLayoutManager` 객체가 생성
  - Strong Reference<small>(강한 참조)</small>이므로 GC에 의해 수거되지 않음
  - 직접 리사이클러뷰의 LayoutManager에 null을 할당해주는 것으로 해결 가능

#### 기존 코드

```kotlin
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val horizontalLayoutManager = 
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    private val streamerListAdapter = StreamerListAdapter { position ->
        configureSmoothScroller(position)
        homeViewModel.onStreamerClick(position)
    }
    ...

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerViews()
        homeViewModel.streamerListAdapter = streamerListAdapter
        ...
    }

    override fun onDestroyView() {
        super.onDestroyView()
        streamerRecyclerView.layoutManager = null
    }

    private fun configureRecyclerViews() {
        streamerRecyclerView = binding.rvStreamer.apply {
            this.layoutManager = horizontalLayoutManager
            adapter = streamerListAdapter
            addItemDecoration(StreamerItemDecoration(streamerItemPx))
        }
    }    

    private fun configureSmoothScroller(position: Int) {
        val smoothScroller = CenterSmoothScroller(requireContext())
        smoothScroller.targetPosition = position
        horizontalLayoutManager.startSmoothScroll(smoothScroller)
    }
}
```

#### 수정 코드

```kotlin
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val streamerListAdapter = StreamerListAdapter { position ->
        configureSmoothScroller(position)
        homeViewModel.onStreamerClick(position)
    }
    ...

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerViews()
        homeViewModel.streamerListAdapter = streamerListAdapter
        ...
    }

    private fun configureRecyclerViews() {
        streamerRecyclerView = binding.rvStreamer.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = streamerListAdapter
            addItemDecoration(StreamerItemDecoration(streamerItemPx))
        }
    }    

    private fun configureSmoothScroller(position: Int) {
        val smoothScroller = CenterSmoothScroller(requireContext())
        smoothScroller.targetPosition = position
        streamerRecyclerView.layoutManager?.startSmoothScroll(smoothScroller)
    }
}
```
