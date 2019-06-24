# 4장 클래스, 객체, 인터페이스

## 4.1 클래스 

### 4.1.1 코틀린 인터페이스

- 코틀린 인터페이스는 자바 8의 인터페이스와 비슷함.

  - 추상 메소드 뿐만 아니라 **구현이 있는 메소드 정의가 가능**하다.<sup> 이는 자바 8의 디폴트 메소드와 비슷하다.</sup>

  - 다만 인터페이스에는 **아무런 상태<sup> 필드</sup>도 들어갈 수 없다.**

> 코틀린에서 클래스는 `class`로 정의하지만 인터페이스는 `interface`로 정의

```kotlin
interface Clickable {
    fun click()
}
```

> 단순한 인터페이스의 구현

```kotlin
class Button : Clickable {
    override fun click() = println("I was clicked")
}

>>> Button().click()
I was clicked
```

- 자바에서는 `extends`와 `implements` 키워드를 사용하지만, 코틀린에서는 클래스 이름 뒤에 콜론 `:` 을 붙이고 인터페이스와 클래스 이름을 적는 것으로 클래스 확장과 인터페이스 구현을 모두 처리한다.

  - 자바와 마찬가지로 클래스는 오직 하나만 확장 가능하고, 인터페이스는 제한 없이 구현할 수 있다.

- `override` 변경자

  - 자바의 `@Override` 애노테이션과 비슷하지만, **코틀린에서는 `override` 변경자를 꼭 사용해야 한다.**

  - 상위 클래스나 상위 인터페이스에 있는 프로퍼티나 메소드를 오버라이드한다는 표시이다.

<br>

### 4.1.2 `open, final, abstract` 변경자: 기본적으로 final

> #### 취약한 기반 클래스<sup> fragile base class</sup> 문제

- 하위 클래스가 기반 클래스에 대해 가졌던 가정이 **기반 클래스를 변경함으로써** 깨져버린 경우에 생긴다.

- 어떤 클래스가 자신을 상속하는 방법에 대해 정확한 규칙<sup> 어떤 메소드를 어떻게 오버라이드해야 하는지 등</sup>을 제공하지 않는다면 그 클래스의 클라이언트는 기반 클래스를 **작성한 사람의 의도와 다른 방식으로 메소드를 오버라이드할 위험**이 있다.

  - 모든 하위 클래스의 분석은 불가능하고, 기반 클래스를 변경하는 경우 하위 클래스의 동작이 예기치 않게 바뀔 수 있다는 면에서 **기반 클래스는 '취약'하다.**

> #### `open` 과 `final`

- 『Effective Java』 - "상속을 위한 설계와 문서를 갖추거나, 그럴 수 없다면 상속을 금지하라"

  - 하위 클래스에서 오버라이드하게 의도된 클래스와 메소드가 아니라면 모두 `final`로 만들라는 뜻이다.

- 자바는 기본적으로 상속에 대해 열려있지만, **코틀린의 클래스와 메소드는 기본적으로 `final`이다.**

   - 어떤 클래스의 상속을 허용하려면 클래스 앞에 `open` 변경자를 붙여야 한다.
   
   - 오버라이드를 허용하고 싶은 메소드나 프로퍼티의 앞에도 `open` 변경자를 붙여야 한다.

- 기반 클래스나 인터페이스의 멤버를 **오버라이드하는 경우 그 메소드는 기본적으로 열려 있다.**

  - 하위 클래스에서의 **오버라이드를 금지하려면 오버라이드하는 메소드 앞에 `final`을 명시**해야 한다.

```kotlin
open class RichButton : Clickalbe {
    final override fun click() {}
    
    // 여기 있는 final은 쓸데 없는 중복이 아니다.
    // final이 없는 override 메소드나 프로퍼티는 기본적으로 열려있다.
}
```

> #### `abstract`

- `abstract`로 선언한 추상 클래스는 인스턴스화가 불가능하다.

  - 추상 클래스에는 구현이 없는 추상 멤버가 있기 때문에 하위 클래스에서 그 추상 멤버를 오버라이드해야만 하는 게 보통이다.

  - 추상 멤버는 항상 열려 있기때문에 추상 멤버 앞에 `open` 변경자를 명시할 필요가 없다.

### 4.1.3 가시성 변경자: 기본적으로 공개

> #### 가시성 변경자<sup> visibility modifier</sup>

- 자바의 기본 가시성인 패키지 전용<sup> package private</sup>은 코틀린에 없다.

  - 코틀린은 패키지를 네임스페이스를 관리하기 위한 용도로만 사용하기 때문에 패키지를 가시성 제어에 사용하지 않는다.

- 패키지 전용 가시성에 대한 대안으로 코틀린은 `internal`이라는 새로운 가시성 변경자를 도입<sup> 우리말로는 모듈 내부라 번역함</sup>

  - `internal`은 "모듈 내부에서만 볼 수 있음"이라는 뜻이다.

  - 자바에서는 프로젝트의 외부에 있는 코드라도 패키지 내부에 있는 패키지 전용 선언에 쉽게 접근할 수 있다. 그래서 모듈의 캡슐화가 쉽게 깨진다.

- 코틀린에서는 최상위 선언에 대해 private 가시성을 허용한다.

  - 비공개 가시성인 최상위 선언은 그 **선언이 들어있는 파일 내부에서만** 사용할 수 있다.

<br>

### 4.1.4 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스

- 자바와 달리, 코틀린의 **중첩 클래스<sup> nested class</sup>** 는 명시적으로 요청하지 않는 한 바깥족 클래스 인스턴스에 대한 접근 권한이 없다.

- 코틀린 **중첩 클래스**에 아무런 변경자가 붙지 않으면 **자바 `static` 중첩 클래스와 같다.**

  - 이를 **내부 클래스**로 변경해서 바깥쪽 클래스에 대한 참조를 포함하게 만들고 싶다면 **`inner` 변경자**를 붙여야 한다.

<br>
<p align = 'center'>
<img width = 320, src = 'https://user-images.githubusercontent.com/39554623/59997239-83625b00-9697-11e9-8306-bbb1d57901e0.png'>
</p>
<br>

- 코틀린에서 바깥쪽 클래스의 인스턴스를 가리키는 참조를 표기하는 방법도 자바와 다르다.

  - 내부 클래스 Inner 안에서 바깥쪽 클래스 Outer의 참조에 접근하려면 `this@Outer`라고 써야 한다.

```kotlin
class Outer {
    inner class Inner {
        fun getOuterReference(): Outer = this@Outer
    }
}
```

<br>

### 4.1.5 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한

> ### 라이브러리 설계자 이외에 상속을 사용할 수 없도록 하고 싶다. 

> 인터페이스 구현을 통해 식 표현

```kotlin
import java.lang.IllegalArgumentException

// Composite Pattern

interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

fun eval(e: Expr): Int {
    when(e) {
        is Num -> e.value
        is Sum -> eval(e.left) + eval(e.right)
        else -> throw  IllegalArgumentException("Unkown type")
    }
}
```

> sealed 클래스로 식 표현

```kotlin
sealed class Expr {  // 기반 클래스를 sealed로 봉인
    class Num(val value: Int) : Expr()  // 기반 클래스의 모든 하위 클래스를 중첩 클래스로 나열
    class Sum(val left: Expr, val right: Expr) : Expr()
}

fun eval(e: Expr): Int =
    when(e) {  // when 식이 모든 하위 클래스를 검사하므로 별도의 else 분기 불필요
        is Expr.Num -> e.value
        is Expr.Sum -> eval(e.left) + eval(e.right)
    }
```

- `sealed`로 표시된 클래스는 자동으로 `open`이다.
