# View Binding

- <b>뷰 바인딩<small>(View Binding)</small></b> 기능은 뷰와 상호작용하는 코드를 쉽게 작성할 수 있게 해준다.
- 모듈에서 사용 설정<small>(enable)</small>된 뷰 바인딩은 각 XML 레이아웃 파일의 <b>바인딩 클래스<small>(binding class)</small></b>를 생성한다.
- 바인딩 클래스의 인스턴스는 상응하는 레이아웃에 ID가 있는 모든 뷰의 직접 참조가 포함된다.
- 대부분의 경우, 뷰 바인딩이 `findViewById`를 대체한다.

## 설정 방법

뷰 바인딩은 모듈 별로 사용 설정이 된다<small>(enabled on a module by module)</small>.

모듈에서 뷰 바인딩을 사용 설정<small>(enable)</small> 하려면, module 레벨의 `build.gradle` 파일에 `viewBinding` 빌드 옵션을 아래의 예시와 같이 `true`로 변경한다.

```gradle
android {
        ...
        viewBinding {
            enabled = true
        }
    }
```

바인딩 클래스를 생성하는 동안 레이아웃 파일을 무시하려면 `tools:viewBindingIgnore="true"` 속성을 레이아웃 파일의 루트 뷰에 추가해야 한다.

## 사용법

모듈에 뷰 바인딩이 사용 설정되면, 모듈에 포함된 각 XML 레이아웃 파일의 바인딩 클래스가 생성된다.

각 바인딩 클래스에는 루트 뷰와 ID가 있는 모든 뷰에 대한 참조를 포함한다.

생성된 바인딩 클래스의 이름은 XML 파일의 이름을 카멜 표기법으로 변환하고 끝에 'Binding'이 추가된다.

> `result_profile.xml` 이름을 가진 레이아웃 파일의 예시

```xml
<LinearLayout ... >
        <TextView android:id="@+id/name" />
        <ImageView android:cropToPadding="true" />
        <Button android:id="@+id/button"
            android:background="@drawable/rounded_button" />
    </LinearLayout>
```

생성된 바인딩 클래스의 이름은 `ResultProfileBinding`이 된다. 이 클래스에는 `name`이라는 `TextView`와 `button`이라는 `Button` 등 두 개의 필드가 있다. 레이아웃의 `ImageView`에는 ID가 없으므로 바인딩 클래스에 참조가 없다.

모든 바인딩 클래스는 `getRoot()` 메서드를 포함하고 있는데, **상응하는 레이아웃 파일의 루트 뷰에 대한 직접 참조를 제공**한다.

위의 예시 코드에서는 `ResultProfileBinding` 클래스의 `getRoot()` 메서드가 `LinearLayout` 루트 뷰를 반환한다.

### 액티비티에서의 뷰 바인딩 사용법

액티비티에 사용할 바인딩 클래스 인스턴스를 설정하려면, 액티비티의 `onCreate()` 메서드에서 다음 두 단계를 따라야 한다.

1. 생성된 바인딩 클래스에 포함된 static `inflate()` 메서드를 호출한다. 이를 통해 액티비티에서 사용할 바인딩 클래스의 인스턴스를 생성한다.
2. `getRoot()`메서드를 호출하거나 [Kotlin property syntax](https://kotlinlang.org/docs/properties.html#declaring-properties)를 사용하여 루트 뷰의 참조를 가져온다.
3. 루트 뷰를 `setContentView()`에 전달<small>(pass)</small>하여 화면 상의 활성 뷰로 만든다.

```kotlin
private lateinit var binding: ResultProfileBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ResultProfileBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)
}
```

이제 바인딩 클래스의 인스턴스를 사용하여 뷰를 참조할 수 있다.

```kotlin
binding.name.text = viewModel.name
binding.button.setOnClickListener { viewModel.userClicked() }
```

### 프래그먼트에서의 뷰 바인딩 사용법

프래그먼트에서 사용할 바인딩 클래스의 인스턴스를 설정하려면, 프래그먼트의 `onCreateView()` 메서드에서 다음 단계를 따라야 한다.

1. 생성된 바인딩 클래스에 포함된 static `inflate()` 메서드를 호출한다. 그러면 프래그먼트에서 사용할 바인딩 클래스의 인스턴스가 생성된다.
2. `getRoot()`메서드를 호출하거나 [Kotlin property syntax](https://kotlinlang.org/docs/properties.html#declaring-properties)를 사용하여 루트 뷰의 참조를 가져온다.
3. `onCreateView()` 메서드에서 루트 뷰를 반환하여 화면 상의 활성 뷰를 만든다.

> ★ 참고 : inflate() 메서드를 사용하려면 layout inflator를 전달해야 한다. 레이아웃이 이미 inflate 되었다면, 바인딩 클래스의 static bind() 메서드를 호출하면 된다. 자세한 내용은 <a id = "a1">[본문의 하단](#f1)</a>이나 [뷰 바인딩 깃허브 샘플의 예시](https://github.com/android/architecture-components-samples/blob/master/ViewBindingSample/app/src/main/java/com/android/example/viewbindingsample/BindFragment.kt#L36-L41)에서 볼 수 있다.

```kotlin
private var _binding: ResultProfileBinding? = null
// This property is only valid between onCreateView and
// onDestroyView.
private val binding get() = _binding!!

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    _binding = ResultProfileBinding.inflate(inflater, container, false)
    val view = binding.root
    return view
}

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
```

이제 바인딩 클래스의 인스턴스를 사용하여 뷰를 참조할 수 있다.

```kotlin
binding.name.text = viewModel.name
binding.button.setOnClickListener { viewModel.userClicked() }
```

> ★ 참고 : 프래그먼트는 뷰보다 오래 지속된다<small>(Fragments outlive their views)</small>. 프래그먼트의 onDestroyView() 메서드에서 바인딩 클래스 인스턴스에 대한 참조를 정리해야 한다.

### 다른 구성<small>(configuration)</small>에 대한 힌트

여러 구성<small>(configuration)</small>에서 뷰를 선언할 때, 특정 레이아웃에 따라 다른 뷰 타입을 사용하는 것이 더 합리적이다.

```xml
# in res/layout/example.xml
<TextView android:id="@+id/user_bio" />

# in res/layout-land/example.xml
<EditText android:id="@+id/user_bio" />
```

이러한 경우, `TextView`가 공통된 기본 클래스<small>(common base class)</small>이기 때문에 생성된 클래스가 `TextView` 타입의 `userBio` 필드를 노출할 것으로 예상할 것이다. 하지만 기술적인 한계로 인해 뷰 바인딩 코드 생성기는 이러한 결정을 내릴 수 없으며, 대신에 단순히 `View` 필드를 생성한다. 이를 위해서는 나중에 `binding.userBio as TextView`를 사용하여 필드를 캐스팅해야 한다.

이 제한 사항을 해결하기 위해, 뷰 바인딩은 `tools:viewBindingType` 속성을 지원하여 생성된 코드에서 어떤 타입을 사용할 것인지 컴파일러에게 알릴 수 있다.

> `tools:viewBindingType` 속성을 사용하여 컴파일러가 필드를 `TextView`로 생성하게 하기

```xml
# in res/layout/example.xml (unchanged)
<TextView android:id="@+id/user_bio" />

# in res/layout-land/example.xml
<EditText android:id="@+id/user_bio" tools:viewBindingType="TextView" />
```

다른 예시로, 하나는 `BottomNavigationView`를 포함하고 다른 하나는 `NavigationRailView`를 포함하는 두 개의 레이아웃이 있다고 가정해보자. 두 클래스 모두 구현의 세부 정보가 포함된 `NavigationBarView`를 확장한다. 코드가 현재 레이아웃에 어떤 하위 클래스가 있는지 정확히 알 필요가 없는 경우, `tools:viewBindingType`를 사용하여 생성된 타입을 두 레이아웃 모두에서 `NavigationBarView`로 설정할 수 있다.

```xml
# in res/layout/navigation_example.xml
<BottomNavigationView android:id="@+id/navigation" tools:viewBindingType="NavigationBarView" />

# in res/layout-w720/navigation_example.xml
<NavigationRailView android:id="@+id/navigation" tools:viewBindingType="NavigationBarView" />
```

참고로 뷰 바인딩은 코드를 생성할 때 속성 값의 유효성을 검사할 수 없다. 컴파일 타임과 런타임 오류를 방지하려면 값이 다음 조건들을 충족해야 한다.

- 값은 `android.view.View`에서 상속되는 클래스여야 한다.
- 값은 해당 값이 배치된 태그의 슈퍼 클래스여야 한다. 예를 들어 다음 값들은 작동하지 않는다.
  - `<TextView tools:viewBindingType="ImageView" />` : ImageView는 TextView와 관련이 없다.
  - `<TextView tools:viewBindingType="Button" />` : Button은 TextView의 슈퍼 클래스가 아니다.
- 최종 타입은 모든 구성에서 일관되게 해결되어야 한다.

## findViewById와의 차이점

뷰 바인딩은 `findViewById`를 사용하는 것에 비해 중요한 장점이 있다.

- **널 안정성**
  - 뷰 바인딩운 뷰에 대한 직접 참조를 생성하므로, 유효하지 않은 view ID로 인해 null pointer exception이 발생할 위험이 없다. 또한 레이아웃의 일부 구성에서만 뷰가 있는 경우, 바인딩 클래스에서 참조를 포함하는 필드가 `@Nullable`로 표시된다.
- **타입 안정성**
  - 각 바인딩 클래스에 있는 필드는 XML 파일에서 참조하는 뷰와 일치하는 타입을 가진다. 즉, 클래스 변환 예외<small>(class cast exception)</small>이 발생할 위험이 없다.

이러한 차이점은 레이아웃과 코드 사이의 비호환성으로 인해 `findViewById`가 런타임에 오류가 발생하는 반면, 뷰 바인딩은 런타임이 아닌 컴파일 타임에 빌드가 실패하게 된다는 것을 의미한다.

연산 속도 면에서도 `findViewById`는 레이아웃 태그를 순회하여 일치하는 뷰를 찾아가기 때문에 연산 속도에 영향을 미치고, 단순 바인딩 코드가 길어진다.

## 데이터 바인딩과의 비교

뷰 바인딩과 데이터 바인딩은 모두 뷰를 직접 참조하는 데 사용할 수 있는 바인딩 클래스를 생성한다. 하지만 뷰 바인딩은 보다 단순한 사용 사례를 처리하기 위한 것이며 데이터 결합에 비해 다음과 같은 이점을 제공한다.

- 더 빠른 컴파일
  - 뷰 바인딩은 주석 처리<small>(annotation processing)</small>이 필요하지 않으므로 컴파일 시간이 더 짧다.
- 사용 편의성
  - 뷰 바인딩은 특별히 태그된 XML 레이아웃 파일이 필요하지 않으므로 앱에서 더 신속하게 채택될 수 있다. 모듈에서 뷰 바인딩을 사용 설정하면 모듈의 모든 레이아웃에 뷰 바인딩이 자동으로 적용된다.

반대로 뷰 바인딩에는 데이터 바인딩과 비교해서 다음과 같은 제한 사항들이 있다.

- 뷰 바인딩은 [레이아웃 변수 또는 레이아웃 표현식](https://developer.android.com/topic/libraries/data-binding/expressions)을 지원하지 않으므로, XML 레이아웃 파일에서 직접 동적 UI 콘텐츠를 선언하는 데 사용할 수 없다.
- 뷰 바인딩은 [양방향 데이터 바인딩](https://developer.android.com/topic/libraries/data-binding/two-way)을 지원하지 않는다.

위 사항을 고려할 때, 일부 사례에서는 뷰 바인딩과 데이터 바인딩을 모두 사용하는 것이 가장 좋다. 고급 기능이 필요한 레이아웃에는 데이터 바인딩을, 고급 기능이 필요 없는 레이아웃에는 뷰 바인딩을 사용할 수 있다.

### <b id = "f1">레이아웃이 이미 인플레이트된 상황에서의 뷰 바인딩</b>  [ ↩](#a1) 

```kotlin
/**
 * View Binding example with a fragment that uses the alternate constructor for inflation and
 * [onViewCreated] for binding.
 */
class BindFragment : Fragment(R.layout.fragment_blank) {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var fragmentBlankBinding: FragmentBlankBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBlankBinding.bind(view)
        fragmentBlankBinding = binding
        binding.textViewFragment.text = getString(string.hello_from_vb_bindfragment)
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        fragmentBlankBinding = null
        super.onDestroyView()
    }
}
```

- 위 코드는 인플레이션을 위해 [Alternate constructor](https://developer.android.com/reference/androidx/fragment/app/Fragment#Fragment(int))를 사용하고 `onViewCreated`를 바인딩에 사용하는 프래그먼트의 뷰 바인딩 예시이다. 레이아웃이 이미 인플레이트 되었기에 바로 바인딩 클래스의 static `bind()` 메서드를 호출하면 된다.

## Additional resources

### Samples

- [View binding sample](https://github.com/android/architecture-components-samples/tree/main/ViewBindingSample)

### Blogs

- [Use view binding to replace findViewById](https://medium.com/androiddevelopers/use-view-binding-to-replace-findviewbyid-c83942471fc)
- [Why Are Kotlin Synthetics Deprecated and What Are the Alternatives?](https://betterprogramming.pub/why-are-kotlin-synthetics-deprecated-and-what-are-the-alternatives-5c2b087dda1c)

### Videos

- [Android Jetpack: Replace findViewById with view binding](https://www.youtube.com/watch?v=W7uujFrljW0)

## References

- Android Docs - View Binding : https://developer.android.com/topic/libraries/view-binding