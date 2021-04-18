# Fragment LayoutId constructor

- 기존에는 Fragment에 대해서 `onCreateView()`에서 레이아웃 인플레이팅 관련 코드를 직접 작성했었지만, `androidx.fragment version 1.1.0`에서 생성자를 통해서 해당 기능을 편하게 사용할 수 있는 기능이 추가되었다.
- `onCreateView()`의 보일러 플레이트를 삭제하는 기능이 추가되었으므로 프로그래머는 `onViewCreated()`에서 bind에 대한 부분만 간편하게 작성하면 된다.

```kotlin
// just inflates a layout and returns it. No need to overriding onCreateView()
class MainFragment2 : Fragment(R.layout.main_fragment) {
    private val binding: MainFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameTextView.text = "Hello2"
        binding.okButton.setOnClickListener {
            Toast.makeText(view.context, "Hello!!!!", Toast.LENGTH_SHORT).show()
        }
    }
}
```
### [androidx.fragment version 1.1.0](https://developer.android.com/jetpack/androidx/releases/fragment?hl=en#1.1.0) 에서의 설명

> **Fragment LayoutId constructor**

Subclasses of Fragment can now optionally call into a constructor on Fragment that takes an `R.layout` ID, indicating the layout that should be used for this fragment as an alternative to overriding `onCreateView()`. The inflated layout can be configured in `onViewCreated()`.


### Airbnb의 lottie-android에서의 활용 예시

> https://github.com/airbnb/lottie-android/blob/master/sample/src/main/kotlin/com/airbnb/lottie/samples/LottiefilesFragment.kt

```kotlin
class LottiefilesFragment : BaseMvRxFragment(R.layout.lottiefiles_fragment) {
    private val binding: LottiefilesFragmentBinding by viewBinding()
    private val viewModel: LottiefilesViewModel by fragmentViewModel()

    private object AnimationItemDataDiffCallback : DiffUtil.ItemCallback<AnimationData>() {
        override fun areItemsTheSame(oldItem: AnimationData, newItem: AnimationData) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AnimationData, newItem: AnimationData) = oldItem == newItem
    }

    private class AnimationItemViewHolder(context: Context) : RecyclerView.ViewHolder(AnimationItemView(context)) {
        fun bind(data: AnimationData?) {
            val view = itemView as AnimationItemView
            view.setTitle(data?.title)
            view.setPreviewUrl(data?.preview)
            view.setPreviewBackgroundColor(data?.bgColorInt)
            view.setOnClickListener {
                val intent = PlayerActivity.intent(view.context, CompositionArgs(animationData = data))
                view.context.startActivity(intent)
            }
        }
    }


    private val adapter = object : PagingDataAdapter<AnimationData, AnimationItemViewHolder>(AnimationItemDataDiffCallback) {
        override fun onBindViewHolder(holder: AnimationItemViewHolder, position: Int) = holder.bind(getItem(position))

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AnimationItemViewHolder(parent.context)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pager.collectLatest(adapter::submitData)
        }
        binding.tabBar.setRecentClickListener {
            viewModel.setMode(LottiefilesMode.Recent)
            requireContext().hideKeyboard()
        }
        binding.tabBar.setPopularClickListener {
            viewModel.setMode(LottiefilesMode.Popular)
            requireContext().hideKeyboard()
        }
        binding.tabBar.setSearchClickListener {
            viewModel.setMode(LottiefilesMode.Search)
            requireContext().hideKeyboard()
        }
        binding.searchView.query.onEach { query ->
            viewModel.setQuery(query)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun invalidate(): Unit = withState(viewModel) { state ->
        binding.searchView.isVisible = state.mode == LottiefilesMode.Search
        binding.tabBar.setMode(state.mode)
    }
}
```

### StackOverFlow의 관련 질문

- https://stackoverflow.com/questions/59900059/fragments-without-oncreateview