# Data Binding Library

<b>데이터 바인딩 라이브러리<small>(Data Binding Library)</small></b>는 프로그램적인 방식이 아닌 <b>선언적 형식<small>(declarative format)</small></b>으로 레이아웃의 UI 구성요소를 앱의 데이터 소스와 결합<small>(bind)</small>할 수 있는 지원 라이브러리다.

레이아웃은 흔히 UI 프레임워크 메서드를 호출하는 코드가 포함된 액티비티에 정의된다.

> `findViewById()`를 호출하여 `TextView` 위젯을 찾아 `viewModel` 변수의 `userName` 프로퍼티에 결합하는 코드

```kotlin
findViewById<TextView>(R.id.sample_text).apply {
    text = viewModel.userName
}
```

다음 예는 데이터 바인딩 라이브러리를 사용하여 레이아웃 파일에서 직접 위젯에 텍스트를 할당하는 방법을 보여준다. 이 방법을 사용하면 위와 같이 코틀린/자바 코드를 호출할 필요가 없어진다. 

> 할당 표현식에 사용되는 `@{}` 구문

```xml
<TextView
    android:text="@{viewmodel.userName}" />
```

레이아웃 파일에서 구성요소<small>(components)</small>를 결합하면 액티비티에서 많은 UI 프레임워크를 삭제할 수 있어 파일이 더욱 단순화되고 유지관리 또한 쉬워진다. 또한 앱 성능이 향상되며 메모리 누수 및 null 포인터 예외를 방지할 수 있다.

> ★ 참고 : 대부분의 경우, 뷰 바인딩이 더 간단하고 더 좋은 성능으로 데이터 바인딩과 동일한 이점을 제공한다. `findViewById()` 호출을 대체하기 위해 데이터 바인딩을 사용하는 경우 뷰 바인딩을 대신 사용하는 것을 고려해본다.

## 데이터 바인딩 라이브러리 사용하기

### 1. 시작하기

#### 빌드 환경

데이터 바인딩을 사용하도록 앱을 구성하려면 아래 예시처럼 앱 모듈에서 `dataBinding` 빌드 옵션을 `build.gradle` 파일에 추가한다.

```gradle
android {
    ...
    buildFeatures {
        dataBinding true
    }
}
```

> ★ 참고 : 앱 모듈이 데이터 바인딩을 직접 사용하지 않더라도 데이터 바인딩을 사용하는 라이브러리에 종속되는 앱 모듈에서는 데이터 바인딩을 구성해야 한다.

#### 안드로이드 스튜디오의 데이터 바인딩 지원

안드로이드 스튜디오는 다수의 데이터 바인딩 코드 편집 기능을 지원한다. 예를 들어 데이터 바인딩 표현식과 관련하여 다음 기능들을 지원한다.

- 구문 강조표시<small>(Syntax highlighting)</small>
- 표현식 언어 구문 오류 플래그 지정
- XML 코드 완성
- [탐색](https://www.jetbrains.com/help/idea/2017.1/navigation-in-source-code.html) 및 [빠른 문서](https://www.jetbrains.com/help/idea/2017.1/viewing-inline-documentation.html)를 포함하는 참조 

> ★ 참고 : 배열과 [Observable](https://developer.android.com/reference/android/databinding/Observable?hl=ko) 클래스와 같은 제네릭 타입은 오류를 잘못 표시할 수도 있다. 

**Layout Editor**의 **Preview** 창에는 데이터 바인딩 표현식의 기본값<small>(default)</small>이 제공되었을 경우에 표시된다. 예를 들어 **Preview** 창에는 다음 예에서 선언된 `TextView` 위젯의 `my_default` 값이 표시된다.

```xml
<TextView android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@{user.firstName, default=my_default}"/>
```

프로젝트의 설계 단계에서만 기본값을 표시해야 하는 경우 [Tools Attributes Reference](https://developer.android.com/studio/write/tool-attributes)에 설명된 것처럼 기본 표현식 값 대신 `tools` 속성을 사용하면 된다.

### 2. 레이아웃 및 결합 표현식

표현식 언어를 사용하면 뷰에 의해 전달된<small>(dispatched)</small> 이벤트를 처리하는 표현식을 작성할 수 있다. 데이터 바인딩 라이브러리는 레이아웃의 뷰를 데이터 객체와 결합하는데 필요한 클래스를 자동으로 생성한다.

데이터 바인딩 레이아웃 파일은 약간 차이가 있으며 `layout`의 루트 태그로 시작하고 `data` 요소 및 `view` 루트 요소가 뒤따른다. 이 view 요소는 non-binding 레이아웃 파일에 루트가 있는 요소이다. 다음 코드는 샘플 레이아웃 파일을 보여준다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"/>
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.lastName}"/>
   </LinearLayout>
</layout>
```

`data` 내의 `user` 변수는 이 레이아웃에서 사용할 수 있는 프로퍼티를 설명한다.

```xml
<variable name="user" type="com.example.User" />
```

레이아웃 내의 표현식은 `@{}` 구문을 사용하여 attribute 프로퍼티에 작성된다. 여기서 `TextView` 텍스트는 `user` 변수의 `firstName` 프로퍼티로 설정된다.

```kotlin
<TextView android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:text="@{user.firstName}" />
    
```

> ★ 참고 : 레이아웃 표현식은 단위 테스트가 불가능하고 IDE 지원이 제한적이므로 작고 단순하게 유지해야 한다. custom [binding adapter](https://developer.android.com/topic/libraries/data-binding/binding-adapters)를 사용하면 레이아웃 표현식을 단순화할 수 있다.


#### 데이터 객체<small>(Data object)</small>

`User` 항목을 설명하기 위해 plain-old object가 있다고 가정해보자.

```kotlin
data class User(val firstName: String, val lastName: String)
```

이 타입의 객체는 절대로 변경되지 않는 데이터를 가지고 있다. 애플리케이션에는 한 번 읽은 이후에 변경되지 않는 데이터가 있는 게 일반적이다.

#### 데이터를 바인딩하기

각 레이아웃 파일의 바인딩 클래스가 생성된다. 기본적으로 클래스 이름은 레이아웃 파일 이름을 기반으로하여 파스칼 표기법으로 변환하고 *Binding* 접미사<small>(suffix)</small>를 추가한다. 레이아웃 파일의 이름이 `activity_main.xml`일 경우, 생성되는 클래스는 `ActivityMainBinding`이다. 이 클래스는 레이아웃 속성<small>(예: `user` 변수)</small>에서 레이아웃 뷰까지 모든 바인딩을 보유하며 바인딩 표현식의 값을 할당하는 방법을 알고 있다. 권장되는 바인딩 생성 방법은 다음 예에서와 같이 레이아웃을 인플레이팅하는 동안 바인딩을 생성하는 것이다.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

    binding.user = User("Test", "User")
}
```

런타임에 앱의 UI에는 **Test** user가 표시된다. 또는 다음 예에서와 같이 `LayoutInflator`를 사용하여 뷰를 가져올 수 있다.

```kotlin
val binding: ActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater())    
```

`Fragment`, `ListView` 또는 `RecyclerView` 어댑터 내에서 데이터 바인딩 항목을 사용하고 있다면 다음 코드와 같이 바인딩 클래스 또는 `DataBindingUtil` 클래스의 `inflate()` 메서드를 사용할 수도 있다.

```kotlin
val listItemBinding = ListItemBinding.inflate(layoutInflater, viewGroup, false)
// or
val listItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false)
```

###










#### Observable 데이터 객체 작업

