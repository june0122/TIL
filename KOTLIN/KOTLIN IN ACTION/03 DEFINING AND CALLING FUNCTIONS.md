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

> joinToString() 함수의 초기 구현

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

