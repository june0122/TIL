# 최상위 함수와 프로퍼티

## 최상위 함수

객체 지향 언어인 자바에서는 모든 코드를 클래스의 메소드로 작성해야 한다. 하지만 실제 코드를 작성하면 어느 한 클래스에 포함시키기 어려운 코드가 많이 생긴다. 중요한 객체는 하나뿐이지만 그 연산을 객체의 인스턴스 API에 추가해서 API를 너무 크게 만들고 싶지 않은 경우가 있다.

그 결과 다양한 정적 메서드를 모아두는 역할만 담당하며, 특별한 상태나 인스턴스 메서드는 없는 클래스가 생겨난다. JDK의 Collectios 클래스가 전형적인 예시이며, 프로그래머가 작성한 코드에서 Util이 이름에 들어있는 클래스가 비슷한 예이다.

코틀린에서는 이런 무의미한 클래스가 필요없다. 대신 함수를 직접 소스 파일의 최상위 수준, 모든 다른 클래스의 밖에 위치시키면 된다. 이러한 함수를 최상위 함수<small>(Top-Level Function)</small>이라 한다. 최상위 함수는 여전히 파일의 맨 앞에 정의된 패키지의 멤버 함수이므로 다른 패키지에서 그 함수를 사용하려면 그 함수가 정의된 패키지를 임포트해야 한다. 하지만 임포트 시 유틸리티 클래스 이름이 추가로 들어갈 필요는 없다.

#### join.kt 파일 내부에 `joinToString()` 함수를 최상위 함수로 선언

```kotlin
package strings

fun <T> joinToString(collection: Collection<T>, separator: String, prefix: String, postfix: String): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0 ) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
```

이 함수가 실행될 수 있는 이유는 JVM이 클래스 안에 들어있는 코드만을 실행할 수 있기 때문에 컴파일러는 이 파일을 컴파일할 때 새로운 클래스를 정의해준다. join.kt를 IDE에서 제공하는 바이트코드 디컴파일<small>(Tools - Kotlin - Show Kotlin Bytecode - Decomplie)</small>을 통해 자바로 표현되는 코드를 확인해보면 다음과 같은 구조를 보여준다.

```java
package strings;

public final class JoinKt {
    @NotNull
    public static final String joinToString(...) {
        ...
    }
}
```

코틀린 컴파일러가 생성하는 클래스의 이름은 최상위 함수가 들어있던 코틀린 소스 파일의 이름과 대응한다. 코틀린 파일의 모든 최상위 함수는 이 클래스의 정적인 메서드가 된다. 자바에서 코틀린의 최상위 함수를 호출하는 방법은 다음과 같다.

```java
import strings.JoinKt;

public class CallTopLevelFunctionInJava {
    public static void main(String[] args) {
        ...

        JoinKt.joinToString(list, ",", ",", "");
    }
}
```

### 파일에 대응하는 클래스의 이름 변경하기

코틀린 최상위 함수가 포함되는 클래스의 이름을 바꾸고 싶다면 파일에 `@JvmName` 어노테이션을 추가하면 된다. @JvmName` 어노테이션은 파일의 맨 앞, 패키지 이름 선언 이전에 위치해야 한다.

```kotlin
@file:JvmName("StringFunctions") // 클래스 이름을 지정하는 어노테이션

package strings // @file:JvmName 어노테이션 뒤에 패키지 문이 와야 한다.

fun joinToString(...): String { ... }
```

이제 다음과 같이 자바에서 joinToString 함수를 호출할 수 있다.

```java
import functions.StringFunctions;
    ...
        StringFunctions.joinToString(list, ", ", "", "");
```

## 최상위 프로퍼티

함수와 마찬가지로 프로퍼티도 파일의 최상위 수준에 놓을 수 있다. 최상위 프로퍼티<small>(Top-Level Property)</small>의 값은 정적 필드에 저장되는데, 이를 활용해 코드에 상수를 추가할 수 있다.

```kotlin
val UNIX_LINE_SEPERATOR = "\n"
```

기본적으로 최상위 프로퍼티도 다른 모든 프로퍼티처럼 접근자 메소드를 통해 자바 코드에 노출된다<small>(val의 경우 게터, var의 경우 게터와 세터가 생긴다)</small>. 겉으론 상수처럼 보이는데, 실제로는 게터를 사용해야 한다면 자연스럽지 못하다. 더 자연스럽게 사용하려면 이 상수를 `public static final` 필드로 컴파일해야 한다. `const` 변경자를 추가하면 프로퍼티를 `public static final` 필드로 컴파일하게 만들 수 있다<small>(단, 원시 타입과 String 타입의 프로퍼티만 `const`로 지정할 수 있다)</small>.

```kotlin
const val UNIX_LINE_SEPERATOR = "\n"
```

앞의 코드는 다음 자바 코드와 동등한 바이트코드를 만들어낸다.

```java
public static final String UNIX_LINE_SEPERATOR = "\n";
```