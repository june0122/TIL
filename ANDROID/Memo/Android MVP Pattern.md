# 안드로이드와 모델-뷰-컨트롤러

## 데이터 클래스(data class)

```kotlin
data class Question(@StringRes val texResId: Int, val answer: Boolean)
```

`@StringRes` 애노테이션은 없어도 되지만, 다음 두 가지 이유로 지정하는 것이 좋다.

1. 생성자에서 유효한 문자열 리소스 ID를 제공하는지를 컴파일 시점에서 Lint(안드로이드 스튜디오에 내장된 코드 검사기)가 검사한다. 따라서 유효하지 않은 리소스 ID가 생성자에 사용되어 런타임 시에 앱이 중단되는 것을 방지해준다.
2. 애노테이션을 지정함으로써 다른 개발자가 쉽게 코드를 알 수 있다.

texResId의 타입이 String이 아니고 Int인 이유는 texResId 변수는 질문 문자열 리소스의 리소스 ID(항상 Int 타입)를 갖기 때문이다.

Question과 같은 클래스처럼 주로 데이터를 갖는 클래스가 많이 있으며, 그중에는 업무에 관련된 것도 있고 프로그램에서 필요해서 생성한 것도 있다. 이런 클래스들은 비즈니스 로직을 처리하는 함수보다는 주로 데이터를 저장하는 속성을 갖는다. 

1. 따라서 클래스 인스턴스끼리 각 속성의 값을 비교하거나(`equals()` 함수)
2. 인스턴스를 컬렉션(HashMap 등)에 저장할 때 사용할 키 값(해시 코드)을 생성하는(`hashCode()` 함수) 기능이나
3. 속성값을 문자열로 쉽게 출력하는(`toString` 함수) 기능이 공통으로 필요하다.

이런 이유로 코틀린에서는 [**데이터 클래스(data class)**](https://github.com/june0122/TIL/blob/master/KOTLIN/LAB/Data%20Classes.md)라는 개념을 추가하였다. 즉, 클래스를 정의할 때 data 키워드를 지정하면 이 클래스를 데이터 클래스로 간주하며, 방금 설명했던 기능들을 처리해주는 함수들을 해당 클레스에 맞게 코틀린 컴파일러가 자동으로 생성해준다.

> GeoQuiz의 객체 다이어그램

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/117186172-8989fc80-ae15-11eb-860b-fcb4c8d573e2.jpeg'>
</p>

## 모델-뷰-컨트롤러와 안드로이드

위 그림의 객체들은 세 부분, 즉 모델(Model), 컨트롤러(Controller), 뷰(View)로 분리됨에 주목하자. 안드로이드 API는 **모델-뷰-컨트롤러**(MVC)라는 아키텍처에 맞추어 설계되었다. 애플리케이션의 어떤 객체든 **모델 객체** 또는 **뷰 객체** 또는 **컨트롤러 객체**가 되어야 한다는 것이 MVC의 주요 관점이다.

### 모델 객체 (Model Object)

- 모델 객체는 애플리케이션의 '데이터'와 '비즈니스 로직'을 갖는다.
- 모델 클래스는 앱과 관계가 있는 것들을 **모델링**한다.
  - ex) 사용자, 상품, 서버에 저장된 사진 등
- 모델 객체는 UI를 모른다. 데이터를 보존하고 관리하는 것이 유일한 목적이다.
- 애플리케이션의 모든 모델 객체들은 **모델 계층(model layer)** 을 구성한다.
  - GeoQuiz의 모델 계층은 Question 클래스로 구성된다.

### 뷰 객체 (View Object)

- 뷰 객체는 자신을 화면에 그리는 방법과 터치와 같은 사용자의 입력에 응답하는 방법이다.
  - 쉽게 말해, 화면에서 볼 수 있는 것이라면 그것은 뷰 객체다.
- 안드로이드는 구성 가능한 뷰 클래스를 풍부하게 제공하지만, 직접 커스텀 클래스를 생성할 수도 있다.
- 애플리케이션의 뷰 객체들은 **뷰 계층(view layer)** 을 구성한다.
  - GeoQuiz의 뷰 계층은 res/layout/activity_main.xml 요소들로부터 인플레이트되는 위젯들로 구성된다.

### 컨트롤러 객체 (Controller Object)

- 컨트롤러 객체는 뷰와 모델 객체를 결속하여 '애플리케이션 로직'을 포함한다.
- 컨트롤러 객체는 뷰 객체에 의해 촉발되는 다양한 이벤트에 응답하고 모델 객체 및 뷰 계층과 주고받는 데이터의 흐름을 관리한다.
- 안드로이드에서 컨트롤러는 일반적으로 **Activity**나 **Fragment**의 서브 클래스이다.
  - GeoQuiz의 컨트롤러 계층은 MainActivity만으로 구성되어 있다.

> 사용자 입력의 MVC 처리 흐름

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/117189778-88f36500-ae19-11eb-9851-e7b834e0d185.png'>
</p>

## MVC 사용하기

애플리케이션 기능이 많아지면 너무 복잡해져 이해하기 어려울 수 있다. 따라서 코드를 클래스로 분리하면 설계에 도움이 되고 전체를 이해하기도 쉬워진다. 개별적인 변수와 함수 대신 클래스 관점으로 생각할 수 있기 때문이다.

이와 유사하게 클래스들을 모델과 뷰 그리고 컨트롤러 계층으로 분리하면 애플리케이션을 설계하고 이해하는 데 도움이 된다. 개별적인 클래스 대신 계층의 관점으로 생각할 수 있기 때문이다.

MVC는 클래스를 재사용하기 쉽도록 해준다. 여러 일을 혼자서 처리하는 클래스보다는 제한된 책임을 갖는 클래스를 재사용하는 것이 더 쉽기 때문이다.

MVC는 큰 앱은 물론 작고 같단한 앱에도 잘 적용되지만, 더 크고 복잡한 앱에서는 컨트롤러 계층이 훨씬 커지거나 복잡해질 수 있다. 대게는 액티비티나 다른 컨트롤러들을 가볍게(thin) 유지하려고 한다. 가벼운 액티비티는 가능한 한 비즈니스 로직을 적게 포함하기 때문이다. 그리고 앱의 컨트롤러를 가볍게 만드는 데 MVC가 더 이상 적합하지 않을 때는 **MVVM**(모델-뷰-뷰모델) 아키텍처가 그 대안이 될 수 있다.

## tools 네임스페이스

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical">  // 레이아웃의 루트 태그에 tools 네임스페이스 추가
    <TextView
        android:id="@+id/question_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:padding="24dp"
        tools:text="@string/question_australia" />  // tools 네임스페이스로 지정
    ...
```

- 이 네임스페이스를 사용하면 TextView 위젯의 속성을 오버라이드해 레이아웃 디자인에 해당 문자열 리소스를 보여줄 수 있다.
  - TextView에 `tools:text` 속성을 추가하면, 안드로이드 스튜디오가 `tools:text` 속성을 알 수 있게 레이아웃의 루트 태그에 tools 네임스페이스를 추가해야 한다.
- tools 네임스페이스 속성들은 앱이 장치에서 실행되어 위젯들이 화면에 나타날 때는 무시된다.
- `android:text`로 지정된 값은 런타임 시에 나타나고, `tools:text`로 지정된 값은 디자인 시에 보기 위해 사용된다.

## 화면 픽셀 밀도

안드로이드는 밀도에 독립적인 크기 단위를 제공한다. 따라서 서로 다른 화면 밀도에서 일정한 크기를 갖도록 그 단위를 사용하면 된다. 그리고 안드로이드가 그 단위를 런타임 시에 픽셀로 변환하므로 신경 쓸 필요도 없다.

<br>
<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/117203388-e8597100-ae29-11eb-8899-4e9280695556.jpeg'>
</p>
<br>

- px: **pixel(픽셀)** 의 줄임말이다. 화면 밀도와는 무관하게 1픽셀은 화면의 1픽셀과 일치한다. 픽셀은 장치의 화면 밀도에 적합하게 조정되지 않으므로 사용을 권장하지 않는다,
- dp: **density-independent pixel(밀도 독립적 필셀)** 의 줄임말이다. 마진과 패딩 등의 크기를 픽셀 값으로 지정하지 않을 때 사용한다. **1dp**는 항상 장치 화면의 1/160인치이며, 화면 밀도와 무관하게 일정한 크기를 갖는다. 따라서 장치의 화면이 고밀도일 때는 더 많은 수의 화면 픽셀을 채우기 위해 dp를 사용한다.
- sp: **scale-independent pixel(크기 독립적 픽셀)** 의 줄임말이다. sp는 사용자의 폰트 크기 선택도 고려한 dp다. 주로 화면에 나타나는 텍스트의 크기를 설정하기 위해 사용한다.
- pt, mm, in: 포인트(1/72인치), 밀리미터, 인치로 크기를 지정할 수 있는 크기 단위이다. 그런데 모든 장치에 잘 맞도록 구성되지 않아 사용을 권장하지 않는다.

실무에서는 dp와 sp를 사용하며, 안드로이드는 이 값들을 런타임 시에 픽셀로 변환한다.