# 3장 함수 정의와 호출

## 3.1 코틀린에서 컬렉션 만들기

> 컬렉션을 만드는 방법

```kotlin
val set = hashSetOf(1, 7 ,53)
val list = arrayListOf(1, 7, 53)
val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
```

## 3.2 함수를 호출하기 쉽게 만들기

- 자바 컬렉션에는 디폴트 toString 구현이 들어있다.
- 하지만 그 디폴트 toString의 출력 형식은 고정돼 있고 우리에게 필요한 형식이 아닐 수도 있다.

```kotlin
>>> val list = listOf(1, 2, 3)
>>> println(list)

[1, 2, 3]    
```

- 디폴트 구현과 달리 `(1; 2; 3)`처럼 **다른 방식으로 구현하려면?**

  - 자바 프로젝트에 구아바<sup> Guava</sup>나 아파치 커먼즈<sup> Apache Commons</sup> 같은 서드파티 프로젝트를 추가하거나 직접 관련 로직을 구현해야 한다.

  - 코틀린에는 이런 요구 사항을 처리할 수 있는 함수가 표준 라이브러리에 이미 들어있다.

> joinToString() 함수의 초기 구현<sup id = "a1">[ StringBuilder 사용 이유?](#f1)</sup>

```kotlin
fun <T> joinToString(
    collection: Collection<T>,
    separator: String,
    prefix: String,
    postfix: String
): String {

    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}

fun main() {
    val list = listOf(1, 2, 3)
    println(joinToString(list,"; ", "(", ")"))
}
```

### 3.2.1 이름 붙인 인자

- 위의 구현에서 해결하고픈 첫 번째 문제는 **함수 호출 부분의 가독성**이다.

```kotlin
joinToString(list,"; ", "(", ")")
```

- 함수의 시그니처를 살펴보지 않고는 인자로 전달한 각 문자열이 어떤 역할을 하는지 구분하기가 쉽지 않다.

- 특히 불리언 플래그<sup> flag</sup> 값을 전달해야 하는 경우 흔히 발생한다.

  - 이를 해결하기 위해 일부 자바 코딩 스타일에서는 불리언 대신 enum 타입을 사용하라고 권장한다.
  
  - 일부 코딩 스타일에서는 다음과 같이 파라미터 이름을 주석에 넣으라고 요구하기도 한다.

```java
joinToString(list, /*separator*/"; ", /*prefix*/"(", /*postfix*/")")
```

- 코틀린에서는 다음과 같이 이해하기 쉽게 표현할 수 있다.

```kotlin
joinToString(list, separator = "; ", prefix = "(", postfix = ")")
```

- 코틀린으로 작성한 함수를 호출할 때는 함수에 전달하는 인자 중 일부(또는 전부)의 이름을 명시할 수 있다.

  - 호출 시 인자 중 어느 하나라도 이름을 명시하고 나면 혼동을 막기 위해 **그 뒤에 오는 모든 인자는 이름을 꼭 명시**해야 한다.

- **이름 붙인 인자**는 특히 다음 절의 **디폴트 파라미터 값**과 함께 사용할 때 쓸모가 많다.

### 3.2.2 디폴트 파라미터 값

- 자바에서는 일부 클래스에서 오버로딩<sup> overloading</sup>한 메소드가 너무 많아진다는 문제가 있다.
  
  - 인자 중 일부가 생략된 오버로드 함수를 호출할 때 어떤 함수가 불릴지 모호한 경우가 생긴다.

- 코틀린에서는 **함수 선언에서 파라미터의 디폴트 값을 지정할 수 있으므로 이런 오버로드 중 상당수를 피할 수 있다.**
  
```kotlin
fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "(",
    postfix: String = ")"
): String {

    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
```
- 이제 함수를 호출할 때 모든 인자를 쓸 수도 있고, 일부를 생략할 수도 있다.

```kotlin
>>> println(joinToString(list,", ", "(", ")"))
(1, 2, 3)

>>> println(joinToString(list))
(1, 2, 3)

>>> println(joinToString(list, "& "))
(1& 2& 3)
```

- 이름 붙은 인자를 사용하는 경우에는 지정하고 싶은 인자만 이름을 붙여서 순서와 관계 없이 지정할 수 있다.

```kotlin
>>> println(joinToString(list, postfix = ";", prefix = "$ "))
$ 1, 2, 3;
```

### 3.2.3 정적인 유틸리티 클래스 없애기: 최상위 함수와 프로퍼티

#### 최상위 함수, 최상위 프로퍼티

- 코틀린은 자바와 달리 최상위 수준에 함수나 프로퍼티를 정의할 수 있다.

- **JVM은 클래스 안에 들어있는 코드만 실행할 수 있기 때문에 최상위 수준 함수는 자동으로 파일 명을 클래스 명으로하는 클래스에 들어가게 된다.**

- 코틀린은 `static`을 지원하지 않기 때문에 이를 최상위 함수로 대체해서 사용한다.
   
- 최상위는 클래스 외부를 뜻한다.

> 코틀린 코드

```kotlin
const val UNIX_LINE_SEPARATOR = "\n"

fun joinToString(...): { ... }

```

> 위와 동등한 바이트 코드를 만들어내는 자바 코드

```java
public static final String UNIX_LINE_SEPARATOR = "\n";

public static String joinToString(...) { ... }
```

- `const` 변경자를 추가하면 프로퍼티를 `public static final` 필드로 컴파일하게 만들 수 있다.
  
  - 단 원시 타입과 String 타입의 프로퍼티만 `const`로 지정할 수 있다. 

<br>

#### 파일에 대응하는 클래스의 이름 변경하기

- 코틀린 최상위 함수가 포함되는 클래스의 이름을 바꾸고 싶다면 파일에 `@JvmName` 어노테이션을 추가한다.

  - `@JvmName` 어노테이션은 파일의 맨 앞, 패키지 이름 선언 이전에 위치해야 한다.

> 코틀린 코드

```kotlin
@file:JvmName("StringFunctions")

package strings

const val BAR = "bar"

fun baz() {
    println("kotlin")
}
```

> 자바에서 호출

```java
import strings.StringFunctions;

...

        System.out.println(StringFunctions.BAR);
        StringFunctions.baz();

```

<br>

## 3.3 메소드를 다른 클래스에 추가: 확장 함수와 확장 프로퍼티

> ### 확장 함수<sup> extension function</sup>
 
- **기존 자바 API를 재작성하지 않고도** 코틀린이 제공하는 여러 편리한 기능을 사용 할 수 있도록 해준다.

  - 어떤 클래스의 멤버 메소드'인 것처럼' 호출할 수 있지만, 사실은 **그 클래스의 밖에 선언된 함수**다.

> 문자열의 마지막 문자를 돌려주는 메소드

```kotlin
package strings

fun String.lastChar(): Char = this.get(this.length - 1)

// String 이 수신 객체 타입이고, 아래 코드의 "kotlin"이 수신 객체이다
```

```kotlin
>>> println("Kotlin".lastChar())

n
```

- 확장 함수를 만들려면 추가하려는 함수 이름 앞에 그 함수가 확장할 클래스의 이름을 덧붙이기만 하면 된다.

  - 클래스 이름 : **수신 객체 타입<sup> receiver type</sup>**

  - 확장 함수가 호출되는 대상이 되는 값(객체) : **수신 객체<sup> receiver object</sup>**

- 일반 메소드의 본문에서 `this`를 사용할 때와 마찬가지로 확장 함수 본문에서도 `this`를 쓸 수 있다.

  - 그리고 일반 메소드와 마찬가지로 확장 함수 본문에서도 `this`를 생략할 수 있다.

```kotlin
package strings

fun String.lastChar(): Char = get(length - 1)

// 수신 객체 멤버에 this 없이 접근할 수 있다.
```

- **확장 함수가 캡슐화를 깨지는 않는다.**

  - 클래스 안에서 정의한 메소드와 달리 확장 함수 안에서는 클래스 내부에서만 사용할 수 있는 비공개<sup> private</sup> 멤버나 보호된<sup> protected</sup> 멤버를 사용할 수 없다.


### 3.3.1 임포트와 확장 함수

- 확장 함수를 정의했다고 해도 자동으로 프로젝트 안의 모든 소스코드에서 그 함수를 사용할 수 있지는 않다.

- 확장 함수를 사용하기 위해서는 그 함수를 다른 클래스나 함수와 마찬가지로 **임포트해야 한다.**

> 개별 함수 임포트

```kotlin
import strings.lastChar

val c = "Kotlin".lastChar()
```

> `*`를 사용한 임포트

```kotlin
import strings.*

val c = "Kotlin".lastChar()
```

> `as` 키워드를 사용하여 임포트한 클래스나 함수를 다른 이름으로 부르기
 
```kotlin
import strings.lastChar as last

val c = "Kotlin".last()
```

- 한 파일 안에서 다른 여러 패키지에 속해있는 이름이 같은 함수를 가져와 사용해야 하는 경우 이름을 바꿔서 임포트하면 이름 충돌을 막을 수 있다.

- 물론 일반적인 클래스나 함수라면 그 전체 이름<sup> FQN, Fully Qualified Name</sup>을 써도 된다.
 
  - 하지만 **코틀린 문법상 확장 함수는 반드시 짧은 이름을 써야 한다.**

  - 따라서 **임포트할 때 이름을 바꾸는 것**이 확장 함수 이름 충돌을 해결할 수 있는 유일한 방법이다.

<br>

### 3.3.2 자바에서 확장 함수 호출

- 내부적으로 확장 함수는 **수신 객체를 첫 번째 인자로 받는 정적 메소드**이다.

  - 그래서 확장 함수를 호출해도 다른 어댑터<sup> adapter</sup> 객체나 실행 시점 부가 비용이 늘지 않는다.

- 이런 설계로 인해 **자바에서 확장 함수를 사용하기도 편하다.**

  - 단지 정적 메소드를 호출하면서 첫 번째 인자로 수신 객체를 넘기기만 하면 된다.

- 다른 최상위 함수와 마찬가지로 확장 함수가 들어있는 자바 클래스 이름도 확장 함수가 들어있는 파일 이름에 따라 결정된다.

  - 따라서 확장 함수를 `StringUtil.kt` 파일에 정의했다면 다음과 같이 호출할 수 있다.

```java
char c = StringUtilKt.lastChar("Java");
```

### 3.3.3 확장 함수로 유틸리티 함수 정의

> #### 프로그래밍 언어 용어에서 `정적` 이라는 말은 **컴파일 시점** 을 의미하고, `동적` 이라는 말은 **실행 시점** 을 의미한다. 

- **실행 시점**에 객체 타입에 따라 동적으로 호출될 대상 메소드를 결정하는 방식을 **동적 디스패치(dynamic dispatch)** 라고 한다.
  
- **컴파일 시점**에 알려진 변수 타입에 따라 정해진 메소드를 호출하는 방식은 **정적 디스패치(static dispatch)** 라고 한다. 

### 3.3.4 확장 함수는 오버라이드 할 수 없다

***코틀린은 호출될 확장 함수를 '정적'으로 결정한다.***

> 멤버 함수 오버라이드하기

```kotlin
open class View {
    open fun click() = println("View clicked")
}

class Button: View() {
    override fun click() = println("Button clicked")
}

fun main(args: Array<String>) {
    val view: View = Button()
    view.click()
}
```

```kotlin
>>> val view: View = Button()
>>> view.click()

Button clicked
```

- 확장 함수는 클래스의 밖에 선언되므로 **클래스의 일부가 아니다.**

- 이름과 파라미터가 완전히 같은 확장 함수를 기반 클래스와 하위 클래스에 대해 정의해도 실제로는 확장 함수를 호출할 때 **수신 객체로 지정한 변수의 정적 타입에 의해 확장 함수가 결정**된다.

  - 그 변수에 저장된 객체의 동적인 타입에 의해 확장 함수가 결정되지 않는다.

<br>

> 확장 함수는 오버라이드할 수 없다.

```kotlin
fun View.showOff() = println("I'm a view!")
fun Button.showOff() = println("I'm a button!")

>>> val view: View = Button()
>>> view.showOff()  // 확장 함수는 정적으로 결정된다.
I'm a view!
```

- view가 가리키는 객체의 실체 타입이 Button이지만, 이 경우 view의 타입이 View이기 때문에 무조건 View의 확장 함수가 호출된다.

> 자바 코드

```
>>> View view = new Button();
>>> ExtensionsKt.showOff(view);

I'm a view!
```

- 어떤 클래스를 확장한 함수와 그 클래스의 멤버 함수의 이름과 시그니처가 같다면 확장 함수가 아니라 멤버 함수가 호출된다.

  - 멤버 함수의 우선순위가 더 높다.

  - 클래스의 API를 변경할 경우 항상 이를 염두해둬야 한다.

<br>

### 3.3.5 확장 프로퍼티

```kotlin
val String.lastChar: Char
    get() = get(length - 1)
var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length - 1, value)
    }
```

```
>>> println("Kotlin".lastChar)
n

>>> val sb = StringBuilder("Kotlin?")
>>> sb.lastChar = '!'
>>> println(sb)
Kotlin!
```

- 자바에서 확장 프로퍼티를 사용하고 싶다면 항상 `StringUtilKt.getLastChar("Java")` 처럼 게터나 세터를 명시적으로 호출해야 한다.

<br>

## 컬렉션 처리: 가변 길이 인자, 중위 함수 호출, 라이브러리 지원

- `vararg` 키워드를 사용하면 호출 시 인자 개수가 달라질 수 있는 함수를 정의할 수 있다.

- 중위<sup> infix</sup> 함수 호출 구문을 사용하면 인자가 하나뿐인 메소드를 간편하게 호할 수 있다.

- 구조 분해 선언<sup> destructuring declaration</sup>을 사용하면 복합적인 값을 분해해서 여러 변수에 나눠 담을 수 있다.

<br>

### 3.4.1 자바 컬렉션 API 확장

> **가변 길이 인자** : 메소드를 호출할 때 원하는 개수만큼 값을 인자로 넘기면 자바 컴파일러가 배열에 그 값들을 넣어주는 기능

- 자바에서는 타입 뒤에 `...` 를 붙였지만, 코틀린에서는 파라미터 앞에 `vararg` 변경자를 붙인다.

```kotlin
fun listOf<T>(vararg values: T): List<T> { ... }
```

- 이미 배열에 들어있는 원소를 가변 길이 인자로 넘길 때도 코틀린과 자바 구문이 다르다.

  - 자바에서는 배열을 그냥 넘기면 되지만 코틀린에서는 배열을 명시적으로 풀어서 배열의 각 원소가 인자로 전달되게 해야 한다.

  - 기술적으로는 **스프레드<sup> spread</sup> 연산자** 가 그런 작업을 해준다.

    - 실제로 전달하려는 배열 앞에 `*`를 붙이기만 하면 된다.

<br>

### 3.4.2 가변 인자 함수: 인자의 개수가 달라질 수 있는 함수 정의



<br>

### 3.4.3 값의 쌍 다루기: 중위 호출과 구조 분해 선언

> 중위 호출 <sup> infix call</sup> 방식으로 `to` 라는 일반 메소드를 호출

```kotlin
val map = mapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
```

- 중위 호출 시에는 수신 객체와 유일한 메소드 인자 사이에 메소드 이름을 넣는다.

  - 이때 객체, 메소드 이름, 유일한 인자 사이에는 공백이 들어가야 한다.

```kotlin
// 다음 두 호출은 동일하다.

1.to("one")  // 일반적인 방식의 to 메소드 호출
1 to "one"   // 중위 호출 방식으로 to 메소드 호출
```

- **인자가 하나뿐인** 일반 메소드나 인자가 하나뿐인 확장 함수에 중위 호출을 사용할 수 있다.

- 함수(메소드)를 중위 호출에 사용하게 허용하고 싶다면 **`infix` 변경자를 함수 선언 앞에 추가**해야 한다.

> `to` 함수의 정의 간략 ver.

```kotlin
infix fun Any.to(other: Any) = Pair(this, other)
```

- 이 `to` 함수는 `Pair의` 인스턴스를 반환한다.

  - `Pair`는 **코틀린 표준 라이브러리 클래스**로, 그 이름대로 두 원소로 이뤄진 순서쌍을 표현한다.
  
  - 실제로 `to`는 제네릭 함수이지만 여기서는 설명을 위해 세부 사항을 생략했다.
 
- **`to` 함수는 확장 함수다.**

  - `to`를 사용하면 타입과 관계없이 임의의 순서쌍을 만들 수 있다.
  
  - 이는 `to`의 수신 객체가 **'제네릭'** 하다는 뜻이다.


- `Pair`의 내용으로 **두 변수를 즉시 초기화**할 수 있다.

```kotlin
val (number, name) = 1 to "one"
```

> 이러한 기능을 **구조 분해 선언<sup> destructuring declaration</sup>** 이라 부른다.

<br>

- `Pair` 인스턴스 외 다른 객체에도 구조 분해를 적용할 수 있다.

  - key와 value라는 두 변수를 맵의 원소를 사용해 초기화할 수 있다.

  - 루프에서도 구조 분해 선언을 활용할 수 있다.

    ```kotlin
    for ((index, element) in collection.withIndex()) {
        println("$index: $element")
    }
    ```


>  mapOf 함수의 선언

```kotlin
fun <K, V> mapOf(vararg values: Pair<K, V>): Map<K, V>
```
- 코틀린을 잘 모른다면, 코틀린이 맵에 대해 제공하는 특별한 문법인 것처럼 느껴질 수 있다.

  - 하지만 실제로는 **일반적인 함수를 더 간결한 구문으로 호출하는 것** 뿐이다.

<br>

## 3.5 문자열과 정규식 다루기

<br>



## 3.6 코드 다듬기: 로컬 함수와 확장

> 반복하지 말라 (DRY, Don't Repeat Yourself)

- 자바 코드를 작성할 때는 DRY 원칙을 피하기는 쉽지 않다.

- 코틀린에서는 함수에서 추출한 함수를 원 함수 내부에 중첩시킬 수 있다.

  - 문법적인 부가 비용을 들이지 않고도 깔끔하게 코드를 조작할 수 있다.

<br>

-------------------------------------------------------

## <b id = "f1"><sup> 1 </sup></b> StringBuilder 사용 의도 [ ↩](#a1)

> Q. 해당 예제에서 문자열을 연결할 때, 간단하게 `+` 연산자를 사용하지 않고 StringBuilder를 사용한 이유는 무엇일까?

```kotlin
fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "(",
    postfix: String = ")"
): String {

    var result = prefix

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result += separator
        result += element
    }

    result += postfix
    return result
}
```  

- 문자열 클래스(String)에 대해서 `+` 연산을 통해 문자열을 조작하면, String 객체는 불변 객체이므로 내부적으로 StringBuilder 타입의 새로운 객체 타입을 생성해서 연산을 처리하므로 위의 코드는 루프의 반복 횟수만큼 새로운 객체가 생성될 것이고, 이러한 부분이 계속 반복된다면 이후에 GC를 통해 수거하는 비용이 늘어날 수 있다.

- 여러 회 반복 되는 반복문 내에서 + 연산자를 사용하여 문자열을 합치는 것은 99% 비효율적이다. 이런 경우에는 StringBuilder를 사용하는 것이 더 효율적이다.

