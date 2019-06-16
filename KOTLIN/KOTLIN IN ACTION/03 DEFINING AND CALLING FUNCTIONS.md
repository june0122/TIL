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

