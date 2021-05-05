# Android UI & Layout

- 안드로이드 지원 라이브러리에서 제트팩으로
  - 기존의 안드로이드 지원(support) 라이브러리들은 그 수가 많고 독립적인 개발 및 버전 관리 때문에 어려움이 많았다.
  - 따라서 안드로이드 10부터는 모든 지원 라이브러리를 androidx라는 **네임스페이스 <sup>namespace</sup>** 를 갖는, 몇 개의 더 큰 라이브러리로 통합하게 되었다.
  - 그렇고 이렇게 통합된 라이브러리를 **Jetpack**이라고 한다.

## 뷰 계층 구조(view hierarchy)

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/117080997-e9cb6080-ad79-11eb-96d7-af95c94939ab.png'>
</p>

- 뷰(View)
  - 뷰는 UI를 만드는데 사용되는 구성 요소
  - 장치 화면에 보이는 모든 것이 뷰
  - 사용자가 화면을 보며 상호 작용하는 뷰 -> 위젯 <sup>widget</sup>

- 위젯(Widget)
  - 안드로이드 SDK에는 많은 위젯이 포함되어 원하는 UI(화면에 보이는 모습과 앱과의 상호작용)을 구성 가능
  - 모든 위젯은 View 클래스의 인스턴스이거나 View의 서브 클래스 <sup>TextView나 Button</sup> 중 하나의 인스턴스

- 뷰그룹(ViewGrop)
  - View의 일종
  - 다른 뷰를 포함하고 배치해 화면에 보여주지만 그 자신은 화면에 나타나지 않음
  - 레이아웃도 뷰그룹, 뷰그룹의 서브 클래스에는 ConstraintLayout이나 FrameLayout 등이 있음

> ConstraintLayout이 뷰그룹이며, 유일한 자식이 TextView 위젯인 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## 레이아웃 XML에서 뷰 객체로

xml 파일 내에 정의된 XML 요소들이 어떻게 View 객체가 될까?

GeoQuiz 프로젝트를 생성하면 MainActivity라는 이름의 Activity 서브 클래스가 자동으로 생성된다. MainActivity 클래스 파일은 app/java 디렉터리의 com.june0122.geoquiz 패키지 아래에 있다.

안드로이드는 원래 자바 코드만 지원했기 때문에 현재 소스 코드 파일이 있는 디렉터리의 이름은 java다. 코틀린 소스 파일도 java 디렉터리에 저장된다. 물론 koltin이라는 이름의 새 디렉터리를 생성해 이 디렉터리에 코틀린 소스 파일들을 따로 저장할 수 있지만 해당 소스 파일들이 프로젝트에 포함되도록 kotlin 디렉터리에 있다는 것을 안드로이드 스튜디오에 알려줘야 한다. 하지만 사용 언어마다 소스 파일을 별개의 디렉터리에 두는 것은 그리 유용하지 않으므로 코틀린 소스 파일도 java 디렉터리에 두는 것이 좋다. (괜히 상호 호환성이 큰 장점이 아니다!)

### AppCompatActivity 란?

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```
- AppCompatActivity는 안드로이드 Activity 클래스의 서브 클래스
- 과거 안드로이드 버전과의 호환성을 지원하기 위해 제공
- 'AppCompat'은 'application compatibility'의 단축어
- Jetpack의 AppCompat 라이브러리는 안드로이드 버전이 달라도 일관된 UI를 유지하는 데 핵심이 되는 클래스와 리소스들을 포함한다.

> AppCompat의 각 하위 패키지들

```
androidx.appcompat.app
androidx.appcompat.content.res
androidx.appcompat.graphics.drawable
androidx.appcompat.view
androidx.appcompat.widget
```

> app/build.gradle 파일에 추가되어있는 AppCompat 라이브러리 의존성

```gradle
dependencies {
    ...
    implementation 'androidx.appcompat:appcompat:1.2.0'
    ...
}
```

### Activity 함수 `onCreate(Bundle?)`

- 액티비티 서브 클래스의 인스턴스가 생성될 때 자동으로 호출됨
- 이때 이 함수에서는 액티비티가 UI를 화면에 보여주고 처리할 수 있도록 다음 함수를 호출함

### `Activity.setContentView(layoutResID: Int)`

- 이 함수는 레이아웃을 **인플레이트 <sup>inflate</sup>** 해 화면에 나타낸다.
  - 인플레이트 : 뷰 계층 구조를 따라 객체로 생성하는 것
- 레이아웃이 인플레이트되면 레이아웃 파일에 있는 각 위젯이 자신의 속성에 정의된 대로 인스턴스로 생성됨
- 이 함수를 호출할 때는 인플레이트될 레이아웃의 리소스 ID(resource ID)를 인자로 전달

## 리소스와 리소스 ID

레이아웃은 **리소스(resource)** 이다. 리소스는 애플리케이션의 일부이며, 코드가 아닌 이미지 파일이나 오디오 파일 및 XML 파일 같은 것들이다. 프로젝트의 리소스들은 app/res 디렉터리 아래의 서브 디렉터리에 존재한다.

코드에서는 리소스의 리소스 ID를 지정해야 사용 가능하며 모든 리소스 ID는 앱을 빌드할 때마다 안드로이드 빌드 도구가 **R.class**에 자동으로 생성한다(안드로이드 스튜디오 3.6 이전 버전에서는 소스 코드 파일인 R.java를 임시로 생성했지만, 3.7 이상 버전에서는 이 파일을 생성하지 않고 R.class만 생성).

레이아웃은 하나의 리소스 ID가 생성되고, 문자열은 각각에 대해 하나의 리소스 ID가 생성된다.하지만 레이아웃에 포함된 각 위젯에서는 코드에서 참조해 사용할 필요가 있는 것에만 리소스 ID가 필요하므로 직접 지정한 것만 생성된다. (GeoQuiz 레이아웃의 리소스 ID인 R.layout.activity_main에서 activity_main은 R 클래스의 내부 클래스인 layout 안에 정수형 상수로 정의되어 있다. 문자열도 리소스 ID를 가지며, R 클래스의 내부 클래스인 string 안에 정수형 상수로 정의되어 있다. 따라서 strings.xml 파일에 기본으로 정의도니 앱 이름의 문자열은 R.string.app_name으로 참조할 수 있다.)

```xml
<Button
    android:id="@+id/true_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginRight="10dp"
    android:text="@string/true_button" />
```

- XML 파일에서 버튼의 android:id 속성값에는 `+`가 있지만, android:text 속성값에는 없다는 것에 주목
  - android:id 속성은 ID를 **생성하고**, android:text 속성은 문자열을 **참조만 하기** 때문이다.

## 위젯을 코드와 연결하기

- 코트 연결의 두 단계
  - 인플레이트된 View 객체들의 참조를 얻는다.
  - 이 객체들에 리스너를 설정해 사용자 액션에 응답한다.

### 위젯의 참조 얻기

버튼들이 리소스 ID를 가지면 MainActivity에서 사용할 수 있다.

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
    }
}
```

위의 코드에 있듯이, 액티비티에서는 `Activity.findViewById(Int)`를 호출해 View 객체로 인플레이트된 위젯의 참조를 얻는다. 이 함수는 위젯의 리소스 ID를 인자로 받아서 해당 위젯(여기서는 Button)의 객체를 반환한다(이때 View 타입을 Button 타입으로 반환해준다).

### 리스너 설정하기

- 안드로이드 애플리케이션은 **이벤트 기반(event-driven)** 으로 구동된다.
- 이벤트에 응답하기 위해 생성하는 객체를 **리스너 <sup>listener</sup>** 라고 하며, 리스너는 해당 이벤트의 **리스너 인터페이스 <sup>listener interface</sup>** 를 구현한다.
- 안드로이드 SDK에는 다양한 이벤트의 리스너 인터페이스가 존재하여 따로 만들 필요가 없다.

버튼이 눌러졌는지에 대한 이벤트를 리스닝하기 위해서, 아래 예제 코드의 리스너는 **View.OnClickListener** 인터페이스를 구현한다.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    trueButton = findViewById(R.id.true_button)
    falseButton = findViewById(R.id.false_button)
    
    trueButton.setOnClickListener { view: View ->
        // 버튼 클릭의 응답을 여기서 처리
    }
```

trueButton의 리스너는 **OnClickListener** 인터페이스를 구현하는 익명 클래스의 인스턴스이며 중괄호 `{}` 안에 정의한다. 그리고 **setOnClickListener** 함수로 등록한다.

안드로이드 프레임워크에는 **onClick(View)** 메서드만 갖는 인터페이스인 **View.OnClickListener**가 정의되어 있다. 이처럼 **단일 추상 메서드 <sup>Single Abstract Method, SAM</sup>** 를 갖는 자바 인터페이스를 **SAM**이라고 하며, 주로 익명의 내부 클래스를 사용해서 구현한다.

코틀린에서는 자바와 호환성을 유지하기 위해 특별한 형태로 SAM을 지원한다. 즉, 함수 리터럴 또는 람다식으로 SAM을 작성하면 이것을 해당 인터페이스의 구현 객체로 변환한다(코틀린에선 함수 리터럴이나 람다식으로 익명 함수 정의 가능). 이와 같은 내부 처리를 **SAM 변환 <sup>SAM conversion</sup>** 이라고 한다. 위의 예제는 람다식을 이용하여 **OnClickListener** 인터페이스를 구현한 것이다.

> View.OnClickListener 인터페이스 문서

```kotlin
package android.view;

/**
 * Interface definition for a callback to be invoked when a view is clicked.
 */
public interface OnClickListener {
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    void onClick(View v);
}
```

```kotlin
trueButton.setOnClickListener(object : View.OnClickListener {
    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
})
```

- trueButton이라는 뷰 객체에 setOnClickListener라는 메소드가 있는데, 이 메소드는 View.OnClickListener 인터페이스 객체를 받는 메소드이다.
- 인터페이스를 객체로 넘길 때, 구현체 부분이 있어야 하므로 익명 객체의 구현체를 만들어서 같이 넘기게 된다.

## 안드로이드 앱 빌드 절차

빌드를 하는 동안 안드로이드 도구가 리소스와 코드 그리고 AndroidManifest.xml 파일(애플리케이션에 관한 메타데이터를 포함)을 가지고 하나의 .apk 파일로 만든다. 그리고 이 파일은 실제 장치나 에뮬레이터에서 실행될 수 있게 디버그 키가 부여된다(.apk를 구글 플레이 스토어에서 배포하려면 구글에서 릴리즈 키를 받아 앱에 포함시켜야 한다).

안드로이드 스튜디오에서는 프로젝트의 빌드와 관리에 필요한 모든 것을 그래들(Gradle) 자동화 빌드 도구를 사용해 처리하므로 신경 쓰지 않아도 된다.

그런데 레이아웃 파일인 activity_main의 내용은 어떻게 애플리케이션의 View 객체로 변환될까? 빌드 절차의 일부로 aapt2 <sup>Android Asset Packaging Tool 2</sup>가 레이아웃 파일의 리소스들을 좀 더 압축된 형태로 컴파일한다. 그리고 이렇게 컴파일된 리소스들이 .apk 파일로 통합된다. 그 다음에 MainActivity의 onCreate(Bundle?) 메서드에서 setContentView(...) 메서드가 호출되면, MainActivity는 LayoutInflater 클래스를 사용해서 레이아웃 파일에 정의된 각 View의 인스턴스를 생성한다.

> activity_main.xml을 인플레이트(XML 요소를 뷰 객체로 생성)하기

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/117084396-1e431a80-ad82-11eb-8b8d-37ed77f36866.jpeg'>
</p>
<br>

뷰 클래스를 XML로 정의하는 대신에 액티비티에서 코틀린이나 자바 코드로 생성할 수도 있다. 하지만 이것은 그리 좋은 방법이 아니다. **뷰 클래스를 XML로 정의하면 프레젠테이션(사용자 인터페이스) 계층을 애플리케이션 로직과 분리할 수 있기 때문이다. <sup>MVC 패턴</sup>**