# 19 데이터 바인딩과 MVVM

앱에 포함된 여러 음원을 사용자가 조회하고 들을 수 있는 BeatBox라는 새로운 프로젝트를 통해 **데이터 바인딩 <sup>data binding</sup>** 이라는 Jetpack 아키텍처 컴포넌트 라이브러리를 사용하는 방법을 배운다. 그리고 데이터 바인딩을 사용해서 **MVVM<small>(Model-View-View Model)</small>** 아키텍처를 구현하고 애셋 <sup>asset</sup> 시스템을 사용해서 음원 파일을 저장하는 방법도 알아본다.

## 다른 아키텍처가 왜 필요할까?

지금까지 작성했던 모든 앱은 간단한 형태의 MVC 아키텍쳐를 사용해 잘 작동하였다. 그런데 무슨 문제가 있길래 아키텍처를 변경해야 할까?

#### 장점 
  
MVC 아키텍처는 규모가 작고 간단한 앱에는 좋다. 새로운 기능을 추가하기 쉽고 앱의 동적인 부분을 쉽게 알 수 있을 뿐만 아니라 프로젝트의 초기 단계에 확고한 개발 기간을 만들어줘서 앱을 빨리 개발할 수 있다.

#### 단점

그런데 프로젝트가 커지면 문제가 발생한다. MVC의 컨트롤러 역할인 액티비티나 프래그먼트의 규모가 커지면서 작성과 이해가 어려워져서 새로운 기능을 추가하거나 결함을 해결하는 데 시간이 오래 걸린다. 따라서 언젠가는 그런 컨트롤러들을 더 작은 부분으로 분할해야 한다.

#### 해결책

그렇다면 어떻게 해야 할까? 점점 비대해지는 컨트롤러 클래스들이 하는 작업을 파악하여 하나의 거대한 클래스 대신 여러 클래스가 작업을 분담해 협업하게 하면 된다.

그런데 서로 다른 작업을 어떻게 분할해야 할까? 이에 대한 답은 MVVM과 같은 아키텍처를 사용하는 것이다. 단, 작업을 분할하는 것은 전적으로 프로그래머의 일이다.

MVVM에서는 뷰와 밀접한 콘트롤러 코드를 레이아웃 파일로 옮길 수 있다. 게다가 동적인<small>(변하는 데이터를 처리하는)</small> 컨트롤러 코드의 일부를 **뷰 모델 클래스**에 넣어서 앱의 테스트와 검증도 쉽게 할 수 있다. 단, 뷰모델 클래스를 어떤 규모로 할 것인지는 각자의 판단에 달렸다. 뷰모델 클래스가 커지면 작게 분할하면 된다.

## MVVM 뷰모델 vs Jetpack ViewModel

MVVM의 일부인 뷰모델은 [여기](https://june0122.github.io/2021/05/13/android-bnr-04/)에서 다뤘던 **Jetpack ViewModel<small>(AAC ViewModel)</small>** 클래스와 같은 것이 아니다. 따라서 혼동되지 않도록 Jetpack 클래스의 이름을 항상 **ViewModel**로 나타내고 MVVM 개념 관련해서는 '뷰모델'이라고 한다.

다시 정리하자면, Jetpack ViewModel은 액티비티나 프래그먼트의 생명주기에 걸쳐 데이터를 유지하고 관리하는 클래스다. 반면에 MVVM의 뷰모델은 개념적인 아키텍처의 일부분을 말한다. 뷰모델은 Jetpack ViewModel 클래스를 사용해서 구현할 수 있다. 그런데 곧 본문의 내용을 통해 알 수 있지만, ViewModel 클래스를 사용하지 않고도 구현할 수 있다.

## BeatBox 프로젝트 준비 작업

BeatBox 앱의 액티비티에서는 RecyclerView에 격자 <sup>grid</sup> 형태의 버튼을 보여줄 것이다. RecyclerView의 의존성을 build.gradle 파일에 추가하고 res/layout/activity_main.xml의 모든 XML을 삭제하고 아래와 같이 RecyclerView로 교체한다.

> MainActivity의 레이아웃 파일 변경하기 (res/layout/activity_main.xml)

```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/recycler_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

이 상태에서 앱을 실행하면 빈화면만 나오는데, 이제부터 데이터 바인딩<small>(data binding)</small>을 구현해보자.

## 단순 데이터 바인딩 구현하기

RecyclerView를 코드와 연결해야 한다. 일반적으로 많이 하는 작업이지만, 이번에는 데이터 바인딩을 사용한다.

데이터 바인딩은 레이아웃을 사용할 때 몇 가지 장점을 제공한다. 곧 보겠지만 간단한 예로, `findViewById(...)` 를 호출하지 않고 뷰를 사용할 수 있게 해준다<small>(자동으로 뷰에 데이터를 넘겨줌)</small>. 진보된 데이터 바인딩의 사용법은 추후에 알아본다.

우선 데이터 바인딩을 활성화 하고 kotlin-kapt 플러그인을 적용하도록 build.gradle 파일에 추가한다.

> 데이터 바인딩 활성화하기 (build.gradle)

```gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
}

android {
    ...
    buildTypes {
        ...
    }
    dataBinding {
        enabled = true
    }
    ...
}
```

kotlin-kapt 플러그인을 적용하면 데이터 바인딩에서 코틀린의 애노테이션을 처리할 수 있다. 이것이 중요한 이유는 본문의 뒷부분에서 알 수 있다.

레이아웃 파일에서 데이터 바인딩을 사용하려면 &lt;layout&gt; 태그로 레이아웃 XML 전체를 둘러싸서 데이터 바인딩용 레이아웃 파일로 변경하면 된다. *activity_main.xml*의 파일 이름을 *main_activity.xml*로 변경하고 코드를 아래와 같이 수정한다<small>(자동으로 생성되는 바인딩 클래스의 이름의 가독성을 올리기 위해 이름 변경)</small>.

> 데이터 바인딩용 레이아웃 파일로 변경하기 (res/layout/main_activity.xml)

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</layout>
```

&lt;layout&gt; 태그는 이 레이아웃에 데이터 바인딩을 한다는 것을 나타낸다. 레이아웃에 이 태그가 있으면 데이터 바인딩 라이브러리가 **바인딩 클래스<small>(binding class)</small>** 를 자동으로 생성한다. 기본적으로 이 클래스 이름은 레이아웃 파일 이름 끝에 **Binding**이 붙은 채로 지정된다<small>(복합 단어이면 각 단어의 첫 자를 대문자로 사용하는 카멜 명명법이 사용됨)</small>.

따라서 여기서는 **MainActivityBinding**이라는 *main_activity.xml*의 바인딩 클래스가 자동으로 생성된다. 그리고 이 클래스가 데이터 바인딩에 사용되므로 `setContentView(Int)`를 사용해서 뷰를 인플레이트하는 대신에 **MainActivityBinding**의 인스턴스를 인플레이트한다.

**MainActivityBinding**은 root 속성에 뷰 계층<small>(레이아웃 전체)</small>의 참조뿐 아니라 레이아웃 파일에 android:id가 지정된 각 자식 뷰의 참조도 갖는다. 따라서 여기서는 **MainActivityBinding** 클래스가 두 개의 참조,  ① 레이아웃 전체의 참조와 ② RecyclerView를 참조하는 recyclerView<small>(자동 생성됨)</small>을 가진다.

> 바인딩 클래스

<p align = 'center'>
<img width = '250' src = 'https://user-images.githubusercontent.com/39554623/120102817-cd0f2680-c187-11eb-89f3-bcda5c8fabef.jpeg'>
</p>

그런데 이 레이아웃은 하나의 뷰만 갖고 있어서 두 개의 참조 모두 RecyclerView를 가리킨다.

이제는 바인딩 클래스를 사용할 수 있다. 우선 DataBindingUtil을 사용해서 MainActivityBinding 인스턴스를 인플레이트하도록 MainActivity의 `onCreate(...)`을 변경한다. 이때 다른 클래스처럼 MainActivityBinding도 import해야 한다.

> 바인딩 클래스 인플레이트하기 (MainActivity.kt)

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        // 바인딩 클래스 인플레이트
        val binding: MainActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.main_activity)
    }
}
```

바인딩 클래스가 생성되었다. 이제 RecyclerView를 구성한다.

> RecyclerView 구성하기 (MainActivity.kt)

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: MainActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.main_activity)

        // RecyclerView 구성
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
        }
    }
}
```

**RecyclerView**가 한 행에 세 개의 격자를 가지며, 각 격자에는 아래의 버튼 레이아웃이 포함된다.

> 음원 버튼 레이아웃 생성하기 (res/layout/list_item_sound.xml)

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <Button
        android:layout_width="match_parent"
        android:layout_height="120dp"
        android:layout_marginStart="5dp"
        android:layout_marginEnd="5dp"
        tools:text="Sound name" />
</layout>
```

이렇게 하면 **ListItemSoundBinding** 클래스가 자동 생성된다. 그다음으로 이 클래스 인스턴스를 통해서 list_item_sound.xml과 연결되는 **SoundHolder**를 생성한다.

> SoundHolder 생성하기 (MainActivity.kt)

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
    }

    private inner class SoundHolder(private val binding: ListItemSoundBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}
```

이 **SoundHolder**에서는 자동 생성된 바인딩 클래스인 ListItemSoundBinding을 사용한다. 그다음으로 이 **SoundHolder**와 연결되는 어댑터를 생성한다.

> SoundAdapter 생성하기 (MainActivity.kt)

```kotlin
class MainActivity : AppCompatActivity() {
    ...

    private inner class SoundHolder(private val binding: ListItemSoundBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    private inner class SoundAdapter() : RecyclerView.Adapter<SoundHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,
                R.layout.list_item_sound,
                parent,
                false
            )
            return SoundHolder(binding) 
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
        }

        override fun getItemCount(): Int = 0
    }
}
```

이제는 `onCreate(...)`에서 **SoundAdapter**르 연결하면 된다.

> SoundAdapter 연결하기 (MainActivity.kt)

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding: MainActivityBinding =
        DataBindingUtil.setContentView(this, R.layout.main_activity)
    binding.recyclerView.apply {
        layoutManager = GridLayoutManager(context, 3)
        adapter = SoundAdapter() // SoundAdapter 연결하기
    }
}
```

드디어 데이터 바인딩을 사용해서 RecyclerView를 설정하였다. 아직은 앱을 실행해도 화면에 아무것도 나오지 않는다.

## 에셋 가져오기

다음으로 음원 파일들을 프로젝트에 추가해 런타임 시에 사용할 수 있게 한다. 이 작업은 리소스 시스템을 사용하는 대신 **애셋 <sup>asset</sup>** 을 사용하는데, 애셋은 리소스 자체라고 생각하면 된다. 즉, 리소스처럼 APK에 포함되지만 시스템에서 특별한 구성<small>(디렉터리 구조화와 참조 생성 등)</small>을 하지 않은 리소스다.

애셋은 구성하는 시스템이 없으므로 원하는 대로 애셋의 이름을 지정하거나 폴더 구조로 구성할 수 있다. 그러나 단점도 있다. 참조를 하기 위한 구성이나 관리하는 시스템이 없으므로 장치의 화면 해상도, 언어, 방향 등이 달라지면 자동으로 대응할 수 없으며, 레이아웃 파일이나 다른 리소스에서 자동으로 사용할 수도 없다.

일반적으로는 리소스를 사용하는 것이 좋다. 그러나 본문의 BeatBox 앱처럼 코드에서 음원 파일들만 사용할 때는 애셋이 유리하다. 대부분의 게임 앱에서는 그래픽과 음원을 애셋으로 사용한다.

음원 애셋을 추가하기 위해 프로젝트에 아래 이미지와 같이 프로젝트 도구 창의 **app**에서 오른쪽 마우스 버튼을 클릭한 후 `New -> Folder -> Assets Folder`를 선택한다. 그리고 대화상자에서 'Change Folder Location'을 체크되지 않은 상태로 두고 'Target Source Set'을 **main**으로 선택한다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/120105687-fedaba00-c194-11eb-8580-0aadf366d4de.png'>
</p>

**Finish** 버튼을 클릭하면 **app** 아래에 **assets** 폴더가 생성된다.

그다음에 app/assets 폴더에 서브 디렉터리로 'sample_sounds'를 생성한다. 이렇게 하면 **assets** 폴더에 있는 모든 파일이 이 앱과 함께 배포된다. 여기서는 편리하게 사용하려고 서브 폴더를 생성했지만, 리소스와는 달리 서브 폴더가 없어도 된다.

에셋 폴더의 음원 파일들은 [이곳](https://github.com/Jpub/AndroidBNR4/tree/main/Ch19/BeatBox/app/src/main/assets/sample_sounds)에서 다운로드한다.

## 에셋 사용하기

BeatBox에서는 애셋 관리와 연관된 많은 일을 하게 된다. 즉, 애셋을 찾아 유지하고 관리하며 음원으로 재생하는 일이다. 이런 일을 하는 새로운 클래스인 **BeatBox**를 생성하고, 두 개의 상수와 로그 메시지에 사용할 태그 값과 애셋이 저장된 폴더 이름을 추가한다.

> 새로운 BeatBox 클래스 (BeatBox.kt)

```kotlin
private const val TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"

class BeatBox {
    
}
```

애셋은 **AssetManager** 클래스로 사용하며, **AssetManager** 인스턴스는 어떤 **Context**에서도 생성할 수 있다. BeatBox 생성자는 **AssetManager** 인스턴스 참조를 인자로 받는다.

> AssetManager 인스턴스 참조 받기 (BeatBox.kt)

```kotlin
private const val TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"

class BeatBox(private val assets: AssetManager) {

}
```

애셋을 사용할 때 어떤 **Context**를 사용할 것인지 고민할 필요는 없다. 어떤 상황이든 모든 Context의 AssetManager가 에셋과 연결될 수 있기 때문이다.

에셋에 있는 파일들의 내역을 얻을 때는 `list(String)` 함수를 사용한다. 그리고 이 함수를 사용해서 에셋의 파일 내역을 찾는 `loadSounds()` 함수를 추가한다.

> 에셋 찾기 (BeatBox.kt)

```kotlin
class BeatBox(private val assets: AssetManager) {

    fun loadSounds() : List<String> {
        try {
            val soundNames = assets.list(SOUNDS_FOLDER)!!
            Log.d(TAG, "Found ${soundNames.size} sounds")
            return soundNames.asList()
        } catch (e: Exception) {
            Log.e(TAG, "Could not list assets", e)
            return emptyList()
        }
    }
}
```

`AssetManager.list(String)`에서는 인자로 전달된 폴더 경로에 포함된 파일들의 이름을 반환한다. 따라서 여기서는 sample_sounds 폴더 이름을 전달해 이 폴더에 넣었던 모든 .wav 파일의 이름을 알 수 있다.

지금까지 추가한 코드가 잘 작동하는지 BeatBox의 인스턴스를 생성하고 `loadSounds()` 함수를 호출하는 코드를 MainActivity에 추가해 확인한다.

> BeatBox 인스턴스 생성하기 (MainActivity.kt)

```kotlin
class MainActivity : AppCompatActivity() {
    
    private lateinit var beatBox: BeatBox
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beatBox = BeatBox(assets)
        beatBox.loadSounds()
        
        val binding: MainActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.main_activity)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = SoundAdapter()
        }
    }
    ...
}
```

BeatBox 앱을 실행하고 LogCat을 확인해보면, 현재 애셋 폴더에 22개의 .wav 파일이 있으므로 아래와 같은 메시지가 보인다.

```
2021-05-31 01:29:02.299 26730-26730/com.june0122.beatbox D/BeatBox: Found 22 sounds
```

## 애셋 사용 코드 추가하기

애셋 파일들의 이름을 갖게 되었으니, 이 이름들을 각 버튼에 설정해 사용자에게 보여줄 수 있다. 궁극적으로는 음원 파일을 재생하므로, 파일 이름과 사용자가 볼 수 있는 이름 및 해당 음원 관련 정보를 유지하고 관리하는 객체가 필요하다.

이 모든 것을 갖는 **Sound** 클래스를 아래와 같이 생성한다.

> Sound 클래스 생성하기 (Sound.kt)

```kotlin
private const val WAV = ".wav"

class Sound(val assetPath: String) {

    val name = assetPath.split("/").last().removeSuffix(WAV)
}
```

Sound 클래스의 생성자에서는 화면에 보여줄 음원 파일의 이름을 만드는 일을 한다. 즉, `String.split(String).last()`를 사용해서 경로 문자열 맨 끝에 있는 파일 이름을 얻고, `String.removeSuffix(String)`을 사용해서 확장자인 .wav를 제거한다.

그다음으로 `BeatBox.loadSounds()` 함수에서 Sound 인스턴스들의 List를 생성한다.

> Sound 객체를 저장하는 List 생성하기 (BeatBox.kt)

```kotlin
class BeatBox(private val assets: AssetManager) {

    val sounds: List<Sound>

    init {
        sounds = loadSounds()
    }

//  fun loadSounds() : List<String> {
    fun loadSounds(): List<Sound> {
        val soundNames: Array<String>

        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!
//            val soundNames = assets.list(SOUNDS_FOLDER)!!
//            Log.d(TAG, "Found ${soundNames.size} sounds")
//            return soundNames.asList()
        } catch (e: Exception) {
            Log.e(TAG, "Could not list assets", e)
            return emptyList()
        }
        val sounds = mutableListOf<Sound>()
        soundNames.forEach { fileName ->
            val assetPath = "$SOUNDS_FOLDER/$fileName"
            val sound = Sound(assetPath)
            sounds.add(sound)
        }
        return sounds
    }
}
```

다음으로 SoundAdapter를 Sound 인스턴스가 저장된 List에 연결한다. 우선 `getItemCount()` 함수에서 sounds.size를 반환하게 변경한다<small>(sounds.size는 sounds List에 저장된 음원 파일의 개수를 나타냄)</small>.

> 음원 파일의 개수 알아내기 (MainActivity.kt)

```kotlin
private inner class SoundAdapter(private val sounds: List<Sound>) : RecyclerView.Adapter<SoundHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
        ...
    }

    override fun onBindViewHolder(holder: SoundHolder, position: Int) {
    }

    override fun getItemCount(): Int = sounds.size
}
```

그다음에 `onCreate(...)`에서 BeatBox의 Sound 인스턴스 List를 어댑터 인자로 전달한다.

> Sound 인스턴스를 저장한 List를 어댑터에 전달하기 (MainActivity.kt)

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    ...
    binding.recyclerView.apply {
        layoutManager = GridLayoutManager(context, 3)
        adapter = SoundAdapter(beatBox.sounds)
    }
}
```

마지막으로 `onCreate(...)`에서 `loadSounds()` 함수 호출 코드를 삭제한다.

> `loadSounds()` 함수 호출 코드 삭제하기 (MainActivity.kt)

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    ...
    beatBox = BeatBox(assets)
//  beatBox.loadSounds()
    ...
}
```

이제는 BeatBox의 init 블록 외부에서 `BeatBox.loadSounds()` 함수가 더 이상 호출되지 않는다. 따라서 이 함수의 가시성 <sup>visibility</sup>을 public으로 지정할 필요가 없으므로 private로 변경한다.

> `BeatBox.loadSounds()`의 가시성을 private로 변경 (BeatBox.kt)

```kotlin
class BeatBox(private val assets: AssetManager) {
    ...
    private fun loadSounds(): List<Sound> {
        ...
    }
}
```

앱을 실행해보면 격자 형태로 나타난 버튼들을 볼 수 있다.

> 비어 있는 버튼들

<p align = 'center'>
<img width = '200' src = 'https://user-images.githubusercontent.com/39554623/120112870-b8945380-c1b2-11eb-989d-21247299a776.png'>
</p>

이제 각 버튼에 음원 이름을 보여주기 위해 데이터 바인딩을 사용해보자.

## 데이터 바인딩하기

데이터 바인딩을 사용할 때는 레이아웃 파일에 데이터를 가진 객체를 선언할 수 있다. 앞의 [다른 프로젝트](https://github.com/june0122/criminal-intent-android/blob/master/app/src/main/java/com/june0122/criminalintent/Crime.kt)에서 알아보았던 범죄 객체 <sup>Crime</sup>를 예로 들면 다음과 같다.

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="crime"
            type="com.june0122.criminalintent.Crime"/>
    </data>
    ...
</layout>
```

이렇게 하면 다음과 같이 레이아웃 파일에서 해당 데이터 객체의 값을 **바인딩 연산자 <sup>binding mustache</sup>** 인 `@{}`를 사용해서 바로 참조할 수 있다.

```xml
<CheckBox
    android:id="@+id/list_item_crime_solved_check_box"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_alignParentRight="true"
    android:checked="@{crime.isSolved()}"
    android:padding="4dp" />
```

데이터 바인딩을 객체 다이어그램으로 나타내면 다음과 같다.

> 레이아웃과 코틀린 객체 간의 데이터 바인딩

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/120102816-cd0f2680-c187-11eb-8972-05d2bc44b5a4.jpeg'>
</p>

여기서는 음원 이름을 각 버튼에 넣는다. 이때 데이터 바인딩을 사용해서 list_item_sound.xml 레이아웃 파일에 **Sound** 객체를 직접 바인딩한다.

> list_item_sound.xml 레이아웃과 Sound 객체의 바인딩

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/120102815-cc769000-c187-11eb-92d7-f95c06a2a3a1.jpeg'>
</p>

그런데 이렇게 하면 아키텍처 관점에서 문제가 생긴다. 아래의 MVC 모델을 보자.

> 문제가 있는 MVC 아키텍처

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/120102813-cb456300-c187-11eb-9202-5ffebb94e732.jpeg'>
</p>

대부분의 아키텍처에서 하나의 클래스는 **한 가지 책임 <sup>SRP, single responsibility principle</sup>** 만을 가지게 하는 것이 기본 원리다. MVC도 그렇다. 즉, 모델은 앱이 작동하는 방법을 나타내며, 컨트롤러는 모델과 뷰를 중재하면서 앱의 데이터를 보여주는 방법을 결정하고, 뷰는 화면에 데이터를 보여준다.

위와 같이 데이터 바인딩을 사용하면 각 아키턱처 요소의 역할 분담이 분명하게 이루어지지 않는다. 보여줄 뷰의 데이터를 준비하는 코드를 **Sound** 모델 객체가 갖게 되어 컨트롤러 역할을 하기 때문이다. 따라서 Sound.kt에는 앱이 작동하는 방법을 나타내는 코드와 보여줄 뷰의 데이터를 준비하는 코드가 뒤섞이게 된다.

그러므로 데이터 바인딩을 제대로 사용하려면 뷰모델이라는 새로운 객체가 필요하다. 그리고 이 객체는 보여줄 뷰의 데이터를 준비하는 방법을 결정하는 책임을 갖는다.

> 모델-뷰-뷰모델

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/120102810-c97b9f80-c187-11eb-82dd-896e37523c78.jpeg'>
</p>

이런 아키텍처를 **MVVM**이라고 하며, 보여줄 데이터를 형식화하기 위해 MVC의 컨트롤러 클래스가 런타임 시에 했던 대부분의 일을 뷰모델이 담당한다. 즉, 레이아웃에서 위젯들을 데이터와 바인딩하던 일을 뷰모델이 하게 된다. 그리고 컨트롤러<small>(액티비티나 프래그먼트)</small>는 데이터 바인딩과 뷰모델을 초기화하고 연결하는 일을 맞게 된다.

### 뷰모델 생성하기

뷰모델인 **SoundViewModel** 클래스를 생성하고, 사용할 Sound 객체 참조를 갖는 sound 속성을 추가한다.

> SoundViewModel 생성하기 (SoundViewModel.kt)

```kotlin
class SoundViewModel {
    
    var sound: Sound? = null
        set(sound) {
            field = sound
        }
}
```

그리고 각 버튼에 보여줄 제목을 갖는 title 속성도 추가한다.

> 버튼 제목을 갖는 속성 추가하기 (SoundViewModel.kt)

```kotlin
class SoundViewModel {

    var sound: Sound? = null
        set(sound) {
            field = sound
        }
    
    val title: String?
        get() = sound?.name
}
```

### 뷰모델에 바인딩하기

다음으로 뷰모델을 레이아웃 파일과 연결한다. 우선 레이아웃 파일에 속성을 선언한다.

> 뷰모델을 바인딩하는 속성 선언하기 (res/layout/list_item_sound.xml)

```kotlin
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="viewModel"
            type="com.june0122.beatbox.SoundViewModel" />
    </data>

    <Button
        android:layout_width="match_parent"
        android:layout_height="120dp"
        android:layout_marginStart="5dp"
        android:layout_marginEnd="5dp"
        android:text="@{viewModel.title}"
        tools:text="Sound name" />
</layout>
```

바인딩 연산자인 `@{}` 내부에서는 간단한 코틀린 표현식을 사용할 수 있다. 예를 들어, 함수 연쇄 호출이나 수식 등이다.

RecyclerView의 각 항목<small>(여기서는 버튼)</small> 데이터를 갖는 SoundHolder에 다음 코드를 추가한다. 우선 SoundViewModel 인스턴스를 생성하고 이것의 참조를 바인딩 클래스인 ListItemSoundBinding의 viewModel 속성에 설정한다. 그리고 바인딩 함수인 `bind(...)`를 추가한다.

> 뷰모델과 바인딩하기 (MainActivity.kt)

```kotlin
private inner class SoundHolder(private val binding: ListItemSoundBinding) : RecyclerView.ViewHolder(binding.root) {
    
    init {
        binding.viewModel = SoundViewModel()
    }
    
    fun bind(sound: Sound) {
        binding.apply { 
            viewModel?.sound = sound
            executePendingBindings()
        }
    }
}
```

여기서는 init 초기화 블록에서 뷰모델 인스턴스를 생성하고 바인딩 클래스의 ViewModel 속성을 초기화한다. 그리고 바인딩 함수인 `bind(Sound)`에서는 viewModel 속성을 변경한다.

보통은 `executePendingBindings()`를 호출할 필요 없다. 그러나 이 앱에서는 RecyclerView에 포함된 바인딩 데이터를 변경해야 하며, RecyclerView는 빠른 속도로 뷰를 변경해야 한다. 따라서 RecyclerView에 포함된 레이아웃을 즉각 변경하도록 `executePendingBindings()`를 호출한 것이다. 이렇게 함으로써 RecyclerView와 RecyclerView.Adapter가 즉시 동기화되어 화면에서 RecyclerView를 스크롤할 때 훨씬 매끄럽게 보인다.

마지막으로 `onBindViewHolder(...)`에서 `bind(Sound)` 함수를 호출하여 뷰모델의 각 Sound 인스턴스를 SoundHolder 인스턴스와 연결한다.

> `bind(Sound)` 함수 호출하기 (MainActivity.kt)

```kotlin
private inner class SoundAdapter(private val sounds: List<Sound>) : RecyclerView.Adapter<SoundHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
        ...
    }

    override fun onBindViewHolder(holder: SoundHolder, position: Int) {
        val sound = sounds[position]
        holder.bind(sound)
    }

    override fun getItemCount(): Int = sounds.size
}
```

앱을 다시 실행하면 이제는 모든 버튼의 제목이 나타난다.

> 제목이 있는 버튼들

<p align = 'center'>
<img width = '200' src = 'https://user-images.githubusercontent.com/39554623/120133878-e3f55d80-c207-11eb-9b69-9984ee03110e.png'>
</p>

### 관찰 가능한 데이터

이제는 모든 게 잘되는 것처럼 보인다. 그러나 이 코드에는 문제가 있다. 앱을 실행하여 가로 방향으로 회전해서 스크롤해보자.

> 이전에 본 데이터가 또다시 나타난다

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/120134432-09369b80-c209-11eb-8d8d-0f440061238e.gif'>
</p>

위에서 '65_CJIPIE'가 보였었는데 아래로 스크롤하니 또다시 '65_CJIPIE'가 보인다. 위아래로 스크롤을 해보면 계속해서 다른 음원 파일의 제목이 보일 것이다.

왜 이런 것일까? 이는 레이아웃이 `SoundHolder.bind(sound)` 함수 내부에서 SoundViewModel의 Sound 객체를 변경했음을 알 수 있는 방법이 없기 때문이다. MVVM 아키텍처에서는 바로 이 방법을 찾는 것이 중요하다.

따라서 바인딩 데이터가 변경되면 뷰모델이 레이아웃 파일과 소통하게 만드는 것이 다음으로 할 일이다. 이렇게 하려면 뷰모델에서 데이터 바인딩의 **Observable** 인터페이스를 구현해야 한다. 이 인터페이스를 사용하면 바인딩 클래스가 뷰모델에 리스너를 설정할 수 있다. 따라서 바인딩 데이터가 변경되면 자동으로 콜백 호출을 받을 수 있다.

그런데 **Observable** 인터페이스의 모든 함수를 구현할 필요는 없으므로 여기서는 데이터 바인딩의 **BaseObservable** 클래스를 사용하여 다음과 같이 구현한다<small>(이 클래스는 기본적으로 Observable 클래스를 구현하고 있다.)</small>.

1. 뷰모델인 **SoundViewModel**을 **BaseObservable**의 서브 클래스로 선언한다.
2. **SoundViewModel**의 바인딩되는 속성에 `@Bindable` 애노테이션을 지정한다.
3. 바인딩되는 속성의 값이 변경될 때마다 `notifyChange()` 또는 `notifyPropertyChanged(Int)`를 호출한다.

여기서는 SoundViewModel에 약간의 코드만 추가하면 된다. SoundViewModel이 관찰 가능하게<small>(observable)</small> 변경한다.

> 뷰모델이 관찰 가능하게 만들기 (SoundViewModel.kt)

```kotlin
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class SoundViewModel : BaseObservable() {

    var sound: Sound? = null
        set(sound) {
            field = sound
            notifyChange()
        }

    @get:Bindable
    val title: String?
        get() = sound?.name
}
```

`notifyChange()`를 호출하면 데이터 객체<small>(여기서는 Sound)</small>의 모든 바인딩 속성값이 변경되었음을 바인딩 클래스<small>(여기서는 ListItemSoundBinding)</small>에 알린다. 따라서 sound 속성의 값<small>(Sound 인스턴스 참조)</small>이 설정되면 list_item_sound.xml의 바인딩 클래스인 ListItemSoundBinding이 알림을 받게 되어 list_item_sound.xml의 버튼 제목이 변경된다<small>(`Button.setText(String)`이 호출됨)</small>.

앞에서 얘기했던 또 다른 함수인 `notifyPropertyChanged(Int)`도 `notifyChange()`와 같은 기능을 수행한다. 그러나 모든 바인딩 속성이 아닌 특정 바인딩 속성의 값이 변경되었음을 알려준다는 점이 다르다. 예를 들어, `notifyPropertyChanged(BR.title)`의 경우는 `title 속성값만 변경되었음`을 나타낸다.

여기서 **BR.title**은 데이터 바인딩 라이브러리가 생성한 상수다. 클래스 이름인 **BR**은 'binding resource'의 단축어다. **BR** 상수는 `@Bindable` 애노테이션이 지정된 각 속성에 대해 해당 속성과 같은 이름으로 생성된다.

> BR 상수 예시

```kotlin
@get:Bindable val title: String    // BR.title 상수가 생성됨
@get:Bindable val volumn: Int      // BR.volumn 상수가 생성됨
@get:Bindable val etcetera: String // BR.etcetera 상수가 생성됨
```

**Observable** 인터페이스를 사용하는 것이 [데이터베이스와 Room 라이브러리](https://june0122.github.io/2021/05/24/android-bnr-11/)에서 기술한 **LiveData**를 사용하는 것과 유사하다고 생각할 수 있다. 실제로 **Observable** 인터페이스 대신 **LiveData**를 데이터 바인딩에 사용할 수 있다. 이 내용은 <a id = "a1">[궁금증 해소 : LiveData와 데이터 바인딩](#f1)</a>에서 설명한다.

앱을 다시 실행해보면 이번에는 가로나 세로, 어느 방향에서 스크롤해도 정상적으로 작동한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/120136860-12763700-c20e-11eb-84b8-1b49d5e5b015.gif'>
</p>

## 궁금증 해소 💁🏻‍♂️ : 데이터 바인딩 추가로 알아보기

### 람다식

레이아웃 파일에 지정한 바인딩 연산자인 `@{}` 내부에는 간단한 코틀린 표현식은 물론이고 **람다식 <sup>lambda expression</sup>**도 사용할 수 있다. 예를 들면 다음과 같다.

```xml
<Button
    android:layout_width="match_parent"
    android:layout_height="120dp"
    android:text="@{viewModel.title}"
    android:onClick="@{(view) -> viewModel.onButtonClick()}"
    tools:text="Sound name" />
```

자바 8의 람다식처럼 이것은 리스너 인터페이스의 구현 코드로 변환한다. 단, 여기처럼 정확한 문법을 사용해야 한다. 즉, 매개변수는 반드시 괄호로 둘러싸야 하고 `->` 오른쪽에는 하나의 표현식만 포함할 수 있다.

또한, 자바의 람다식과는 다르게 람다식 매개변수를 생략할 수도 있다. 따라서 다음과 같이 해도 된다.

```xml
android:onClick="@{() -> viewModel.onButtonClick()}"
```

### 더 편리한 문법

데이터 바인딩에는 큰따옴표 안에 백틱 `` ` `` 기호도 사용할 수 있다.

```xml
android:text="@{`File name: ` + viewModel.title}"
```

여기서 `` `File name` ``은 **"File name"**과 같은 의미다. 또한, 데이터 바인딩 표현식에는 null 처리 연산자를 넣을 수 있다.

```xml
android:text="@{`File name: ` + viewModel.title ?? `No file`}"
```

여기서 title 값이 null이면 `??` 연산자가 null 대신 'No file'을 결괏값으로 산출한다.

또한, 데이터 바인딩 표현식에는 null 값을 자동으로 처리한다. 심지어는 앞의 코드에서 viewModel이 null일지라도 앱이 중단되지 않게 데이터 바인딩에서 null 값 여부를 검사하고 조치한다. 따라서 viewModel이 null일 때는 viewModel.title의 결과를 "null"로 반환한다.

### BindingAdapter

기본적으로 데이터 바인딩에서는 바인딩 표현식을 레이아웃 속성의 게터/세터 호출로 변환한다. 예를 들어, 다음 표현식은 text 속성의 세터인 `setText(String)` 함수 호출로 변환되어 처리된다.

```xml
android:text="@{`File name: ` + viewModel.title ?? `No file`}"
```

그러나 이 정도로는 충분치 않고 특정 속성에 나름의 추가 처리가 필요할 때가 있다. 이때는 다음과 같이 **BindingAdapter**를 사용한다.

```kotlin
@BindingAdapter("app:soundName")
fun bindAssetSound(button: Button, assetFileName: String) {
    ...
}
```

즉, 프로젝트의 어디서든 파일 수준 함수를 생성하고 `@BindingAdapter` 애노테이션을 지정하면 된다. 그리고 바인딩할 속성 이름을 `@BindingAdapter` 애노테이션의 인자로 전달한다<small>(여기서는 app:soundName)</small>. 그다음에 `@BindingAdapter` 애노테이션이 적용되는 **View**를 해당 함수의 첫 번째 인자로 전달한다.

앞의 예에서는 app:soundName 속성을 갖는 **Button**을 데이터 바인딩이 접할 때마다 `bindAssetSound(...)` 함수를 호출한다. 이때 해당 **Button**과 **바인딩 표현식 <sup>binding expression</sup>** 의 결과가 인자로 전달된다<small>(여기서는 나타나지 않았지만, 바인딩 표현식은 app:soundName 속성에 지정되어 있다.)</small>.

**View**나 **ViewGroup** 같은 더 일반화된 뷰의 **BindingAdapter**도 생성할 수 있다. 이때 **BindingAdapter**가 해당 **View**와 이것의 모든 서브 클래스에 적용된다.

예를 들어, Boolean 값을 기준으로 View<small>(와 이것의 모든 서브 클래스 뷰)</small>의 가시성을 설정하는 app:isGone 속성을 정의할 때는 다음과 같이 한다.

```kotlin
@BindingAdapter("app:isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) View.GONE else View.VISIBLE
}
```

여기서는 View가 `bindIsGone(...)`의 첫 번째 인자이므로 isGone 속성은 app 모듈의 View와 이것의 모든 서브 클래스 View에 대해 사용할 수 있다. 예를 들면 Button, TextView, LinearLayout 등에서 사용 가능하다.

안드로이드 표준 라이브러리의 위젯에는 이미 바인딩 어댑터가 정의되어 있다. 예를 들어, TextView에는 TextViewBindingAdapter가 정의되어 있어서 TextView의 속성들에 데이터 바인딩을 할 수 있다<small>(TextViewBindingAdapter는 androidx.databinding.adapters 패키지에 있다)</small>.

## <b id = "f1">궁금증 해소 💁🏻‍♂️ : LiveData와 데이터 바인딩</b>  [ ↩](#a1)

**LiveData**와 데이터 바인딩은 데이터가 변경되는지 관찰하면서 변경될 때 반응하는 방법을 제공한다는 면에서 서로 유사하다. 실제로 **LiveData**와 데이터 바인딩을 같이 사용할 수 있다. 다음 코드에서는 **Observable** 대신 **LiveData**를 사용해서 title 속성을 SoundViewModel에 바인딩했다.

```kotlin
//class SoundViewModel : BaseObservable() {
class SoundViewModel {

    val title: MutableLiveData<String?> = MutableLiveData()
    
    var sound: Sound? = null
        set(sound) {
            field = sound
//            notifyChange()
            title.postValue(sound?.name)
        }

//    @get:Bindable
//    val title: String?
//        get() = sound?.name
}
```

이때는 SoundViewModel이 BaseObservable의 서브 클래스가 되지 않아도 되며, `@Bindable` 애노테이션도 지정하지 않아도 된다. LiveData는 자신의 알림 매커니즘을 갖고 있기 때문이다. 그러나 [데이터베이스와 Room 라이브러리](https://june0122.github.io/2021/05/24/android-bnr-11/)에서 설명한듯이 LiveData는 LifeCycleOwner가 필요하므로 여기서는 title 속성을 관찰할 때 사용할 LifeCycleOwner를 데이터 바인딩 프레임워크에 알려주어야 한다. 따라서 바인딩 객체가 생성된 후 lifeCycleOwner 속성을 설정하기 위해 SoundAdapter를 변경해야 한다.

```kotlin
private inner class SoundAdapter(private val sounds: List<Sound>) : RecyclerView.Adapter<SoundHolder>() {
    ...

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
        val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
            layoutInflater,
            R.layout.list_item_sound,
            parent,
            false
        )
        
        binding.lifecycleOwner = this@MainActivity // lifeCycleOwner 속성 설정
        
        return SoundHolder(binding)
    }
}
```

여기서는 MainActivity를 LifeCycleOwner로 설정한다. 따라서 속성 이름인 title만 바뀌지 않는다면 뷰를 변경할 필요가 없다.