# Android UI - ConstraintLayout

## Android Jetpack

> Jetpack : 개발자가 고품질 앱을 손쉽게 개발할 수 있게 돕는 라이브러리, 도구, 가이드 모음

- Android Jetpack 구성요소

  - 기초
  - 아키텍처
  - 동작
  - UI

### AndroidX

> AndroidX는 Android 팀이 Jetpack 내에서 라이브러리를 개발, 테스트, 패키징, 버전 관리, 출시하는 데 사용하는 오픈소스 프로젝트이다.

- 과거의 안드로이드는 각종 라이브러리, 도구, 가이드들을 `android.support.*`와 `android.arch.*`와 같은 패키지 명을 통해 제공하였었다.

- 기존 support, architecture 라이브러리 등을 [Android Jetpack](https://developer.android.com/jetpack?authuser=19&hl=ko)으로 통합하면서 `androidx.*` 패키지 명으로 교체되었다.



## `ConstraintLayout` 이란?

- 복잡한 레이아웃을 단순한 계층구조를 이용하여 표현할 수 있는 ViewGroup<sup> android.view.ViewGroup</sup>이다.

  - API level 9<sup> Gingerbread</sup>에서부터 시작하는 안드로이드 시스템에서 사용할 수 있는 [지원 라이브러리](https://developer.android.com/topic/libraries/support-library?hl=ko)이다.

- 레이아웃에 배치되는 뷰들에 여러 **제약**을 적용하여 각 뷰의 위치와 크기를 결정한다.

  - 제약<sup> Constraint</sup>이란, 각 요소들의 최종 위치와 크기를 결정하게 될 조건들이다.
  
    - 예를 들어, 특정 뷰 왼쪽 사이드를 지정된 뷰의 오른쪽 사이드에 맞추거나, 뷰의 왼쪽,오른쪽 사이드를 각각 부모 레이아웃의 왼쪽,오른쪽 사이드에 맞추는 것 등을 말한다.

  - 이러한 각각의 제약은 ConstraintLayout이 가지는 하나의 레이아웃 속성으로 매핑이 된다.

<br>

## `ConstraintLayout`의 사용법

### 기본 형태

> ### app:layout_constraint[View1's direction]()_to[Direction]()Of = "[View2]()"

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/62030288-c4fa9e80-b21f-11e9-9904-795bda435c28.png'>
</p>
<br>

- 기본적으로 뷰의 위치를 **다른 뷰에 상대적으로 지정**한다.

- `View1's direction` 을 `View2` 의 `Direction` 에 위치시킨다.
 
  - `Direction`은 Top, Bottom, Left, Right<sup> 상하좌우</sup>와 같은 상대적인 방향을 뜻한다.
  
<br>

### 1. Relative Positioning

<br>
<p align = 'center'>
<img width = 600, src = 'https://user-images.githubusercontent.com/39554623/62031123-941b6900-b221-11e9-8aa1-cdbc82ed1141.png'>
</p>
<br>

```xml
    <TextView
            android:layout_width="80dp"
            android:layout_height="50dp"
            android:id="@+id/text1"
            android:text="@string/text1"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"/>
    <TextView
            android:layout_width="80dp"
            android:layout_height="50dp"
            android:id="@+id/text2"
            android:text="@string/text2"
            app:layout_constraintLeft_toRightOf="@id/text1"/>
```

- `app:layout_constraintLeft_toRightOf = "@id/text1"`

  - 왼쪽 사이드를 대상 뷰의 오른쪽 사이드에 배치하는 속성

  - `해당 View의 Left`를 `text1`의 `Right`에 배치시키는 제약이다.

<br>

### 2. Centering & Bias

<br>
<p align = 'center'>
<img width = 600, src = 'https://user-images.githubusercontent.com/39554623/62033422-de531900-b226-11e9-9d7f-627cc4104f4f.png'>
</p>
<br>

```xml
    <TextView
        android:id="@+id/OBJECT"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="36dp"
        android:gravity="center"
        android:text="@string/object"
        android:textSize="50sp"
        app:autoSizeTextType="uniform"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```

- 좌우, 혹은 상하로 Constraint를 정의하면 가운데로 정렬된다.

  ```xml
  app:layout_constraintLeft_toLeftOf="parent"
  app:layout_constraintRight_toRightOf="parent"
  ```
<br>

<br>
<p align = 'center'>
<img width = 600, src = 'https://user-images.githubusercontent.com/39554623/62033760-a698a100-b227-11e9-8c60-13589ddf83df.png'>
</p>
<br>

- 정렬되는 위치를 **`bias`** 를 통해서 결정할 수 있다.<sup> 0 ~ 1.0</sup> 

    ```xml
    app:layout_constraintHorizontal_bias="0.2"
    ```

<br>

### 3. Circular Constraints

<br>
<p align = 'center'>
<img width = 600, src = 'https://user-images.githubusercontent.com/39554623/62108160-55021c00-b2e4-11e9-8cd7-f05a09acda62.png'>
</p>
<br>

```xml
app:layout_constraintCircle="@id/point"
app:layout_constraintCircleRadius="48dp"
app:layout_constraintCircleAngle="45"
```

- 특정 뷰<sup> POINT</sup>를 중심으로 원형으로 배치하려면 원형으로 배치할 뷰<sup> OBJECT</sup>에 Constraint를 정의한다.

<br>

### 4. Adjust the view size

> **Attributes 윈도우의 컨트롤들**

<br>
<p align = 'center'>
<img width = 350 src = 'https://user-images.githubusercontent.com/39554623/62110624-ca242000-b2e9-11e9-8696-9dc446ee5c49.png'>
</p>

<p align = 'center'>
<strong>
① size ratio, ② constraint 삭제, ③ height/width mode, ④ margins, ⑤ constraint bias
</strong>
</p>


- <img align = "absmiddle" height = 20 src = 'https://user-images.githubusercontent.com/39554623/62110195-dbb8f800-b2e8-11e9-87cf-60b6b59978fc.png' > **Fixed** : 특정한 크기를 지정할 수 있다.

- <img align = "absmiddle" height = 20 src = 'https://user-images.githubusercontent.com/39554623/62110197-dbb8f800-b2e8-11e9-9eb0-e4d4f359ab29.png' > **Wrap Content** : view의 contents에 알맞게 확장된다.

- <img align = "absmiddle" height = 20 src = 'https://user-images.githubusercontent.com/39554623/62110198-dbb8f800-b2e8-11e9-9d1a-82adfaeb79ca.png' > **Match Constraints** : 각면의 제약 조건을 충족시킬 수 있도록, 가능한만큼 view가 확장된다. 그리고 아래의 속성들과 값들을 통해서 이러한 동작을 수정할 수 있다.

  - **`layout_constraintWidth_default`**

     - `spread` : 기본값으로, 각면의 제약 조건을 충족시킬 수 있도록, 가능한만큼 view가 확장된다.
     - `wrap` : contents에 크기를 맞추기 위해 필요한만큼만 view를 확장하지만, 제약 조건이 더 작은 view를 요구하는 경우 그것을 허용한다. `Wrap Content`와의 차이점을 눈여겨봐야하는데, `Wrap Content`로 설정할 경우, 너비가 항상 content의 너비와 일치해야 하지만, `Match Constraints`의 `layout_constraintWidth_default` 속성에서 `wrap`으로 값을 설정하면 view가 content의 너비보다 작아질 수 있다. 

  - **`layout_constraintWidth_min`** : 뷰의 최소 너비에 대한 dp 수치를 취한다.

  - **`layout_constraintWidth_max`** : 뷰의 최대 너비에 대한 dp 수치를 취한다.


<br>

### 5. Set size as a ratio

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/62115016-b204ce80-b2f2-11e9-9908-01bce80c77f0.png'>
<img src = 'https://user-images.githubusercontent.com/39554623/62115019-b204ce80-b2f2-11e9-90c4-8900a122ebb9.png'>
<img src = 'https://user-images.githubusercontent.com/39554623/62115021-b204ce80-b2f2-11e9-9e41-99d29b598c27.png'>
</p>

- view 크기 중 하나 이상이 `match constraints`<sup> 0dp</sup>로 설정된 경우, view의 크기를 원하는 비율로 설정할 수 있다.

  - 비율을 설정하려면 패널 정사각형의 좌측 상단의 삼각형<sup> Attributes 윈도우 컨트롤 ①번</sup>을 클릭한 다음 비율을 입력한다.

<br>

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/62115024-b29d6500-b2f2-11e9-83bb-4c8796eeb985.png'>
<img src = 'https://user-images.githubusercontent.com/39554623/62115025-b29d6500-b2f2-11e9-8172-9712f48e8047.png'>
<img src = 'https://user-images.githubusercontent.com/39554623/62115026-b335fb80-b2f2-11e9-904d-74cea76e893f.png'>
</p>

<p align = 'center'>
▲ 좌측부터 차례로 1:1, 2:1, 16:10 비율을 적용
</p>

<br>

> xml 코드에서의 비율 설정

- 가로 세로의 비율을 지정하는 속성으로, 가로 세로 순서를 명시적으로 선언할 수도 있다.

```xml
app:layout_constraintDimensionRatio="(W|H,)[width]:[height]"

ex)
app:layout_constraintDimensionRatio="2:1"
app:layout_constraintDimensionRatio="H,16:9" <!-- height:width -->
```

<br>

### 6. Chains

<br>
<p align = 'center'>
<img width = 600 src = 'https://user-images.githubusercontent.com/39554623/62119927-93ef9c00-b2fb-11e9-84d5-1d81cf3a6f3c.png'>
</p>
<br>

- 두 뷰가 서로 연결되어 있을 경우 Chain이 생성되며, 여러 뷰끼리 묶어서 처리할 때 유용하다.

    ```xml
    <Button
    android:id="@+id/buttonA"
    app:layout_constraintRight_toLeftOf="@+id/buttonB"
    app:layout_constraintLeft_toLeftOf="parent"/>

    <Button
    android:id="@+id/buttonB"
    app:layout_constraintLeft_toRightOf="@+id/buttonA"
    app:layout_constraintRight_toRightOf="parent"/>
    ```

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/62120526-b7671680-b2fc-11e9-9ab0-ebd361e2280a.png'>
</p>
<br>


<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/62125097-0d40bc00-b307-11e9-8df5-2213e4bb721c.png'>
</p>
<br>

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/62126155-0ff0e080-b30a-11e9-8da6-50102f0ea3fe.png'>
</p>
<br>


<img src = 'https://user-images.githubusercontent.com/39554623/62126158-10897700-b30a-11e9-93ee-9805874f58e0.png'>
<img src = 'https://user-images.githubusercontent.com/39554623/62126159-10897700-b30a-11e9-99e7-72d2171d44cb.png'>
<img src = 'https://user-images.githubusercontent.com/39554623/62126156-0ff0e080-b30a-11e9-8dc1-5365aa4db4fa.png'>
<img src = 'https://user-images.githubusercontent.com/39554623/62126160-10897700-b30a-11e9-95fc-160fd1d3cec4.png'>
<img src = 'https://user-images.githubusercontent.com/39554623/62126161-11220d80-b30a-11e9-9c8b-0c5aaa2d0d86.png'>

<br>

## 참고

- https://developer.android.com/training/constraint-layout

- https://velog.io/@tura/android-jetpack-constraint-layout

- https://developer.android.com/jetpack?authuser=19&hl=ko