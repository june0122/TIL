# Data Classes

자바 플랫폼에서는 클래스가 `equals`, `hashCode`, `toString` 등의 메소드를 구현해야 한다. 다행히 자바 IDE들이 이런 메소드를 자동으로 생성해주긴 하지만 코드베이스가 번잡해지는건 변함이 없다.

코틀린 컴파일러는 한걸음 더 나가서 이런 메소드를 기계적으로 생성하는 작업을 보이지 않는 곳에서 해주기 때문에 필수 메소드로 인한 번잡함 없이 소스코드를 깔끔하게 유지 가능하다.

그런 코틀린의 원칙이 잘 드러나는 사례가 클래스 생성자나 프로퍼티 접근자를 컴파일러가 자동으로 만들어주는 것이다. 코틀린 컴파일러는 데이터 클래스에 유용한 메소드를 자동으로 만들어준다.

## 1. 모든 클래스가 정의해야 하는 메소드

자바와 마찬가지로 코틀린 클래스도 `toString`, `equals`, `hashCode` 등을 오버라이드할 수 있다. 각각이 어떤 메소드이고 어떻게 그런 메소드를 정의해야 하는지 살펴보자.

```kotlin
class Client (val name: String, val postalCode: Int)
```

이제 이 **클래스의 인스턴스를 어떻게 문자열로 표현**할지 생각해보자.

### 1-1. 문자열 표현 : `toString()`

자바처럼 코틀린의 모든 클래스도 인스턴스의 문자열 표현을 얻을 방법을 제공한다. 주로 디버깅과 로깅 시 이 메소드를 사용한다.

> 기본으로 제공되는 객체의 문자열 표현

```kotlin
class Client (val name: String, val postalCode: Int)
```
```kotlin
>>> val client1 = Client("KAMIYU", 123456)
>>> println(client1)

Client@61bbe9ba
```

기본으로 제공되는 객체의 문자열 표현은 위와 같이 `Client@61bbe9ba` 같은 방식인데, 이 기본 구현을 바꾸려면 `toString` 메소드를 오버라이드해야 한다.

> `Client`에 `toString()` 구현

```kotlin
class Client (val name: String, val postalCode: Int) {
    override fun toString(): String = "Client(name=$name, postalCode=$postalCode)"
}
```
```kotlin
>>> val client1 = Client("KAMIYU", 123456)
>>> println(client1)

Client(name=KAMIYU, postalCode=123456)
```

이런 문자열 표현으로부터 기본 문자열 표현보다 더 많은 정보를 얻을 수 있다.

### 1-2. 객체의 동등성 : `equals()`

Client 클래스를 사용하는 모든 계산은 클래스 밖에서 이뤄진다. Client는 단지 데이터를 저장할 뿐이며, 그에 따라 구조도 단순하고 내부 정보를 외부에 투명하게 노출하게 설계됐다. 그렇지만 클래스는 단순할지라도 동작에 대한 몇 가지 요구 사항이 있을 수 있다. 예를 들어 서로 다른 두 객체가 내부에 동일한 데이터를 포함하는 경우 그 둘을 동등한 객체로 간주해야 할 수도 있다.

```kotlin
>>> val client1 = Client("KAMIYU", 123456)
>>> val client2 = Client("KAMIYU", 123456)
>>> println(client1 == client2)

false
```

위 예제에서는 두 객체가 동일하지 않다. 이는 Client 클래스의 요구 사항을 만족시키고 싶다면 `equals`를 오버라이드 할 필요가 있다는 뜻이다.

- 참고로 코틀린에서 `==` 연산자는 **참조 동일성을 검사하지 않고 객체의 동등성을 검사**한다. 따라서 `==` 연산은 `equals`를 호출하는 식으로 컴파일된다.<sup id = "a1">[1](#f1)</sup>

> `Client`에 `equals()` 구현하기

```kotlin
class Client(val name: String, val postalCode: Int) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Client) // other가 Client인지 검사
            return false
        return name == other.name && // 두 객체의 프로퍼티 값 비교, 코틀린의 is 검사는 자바의 instanceof와 같다.
                postalCode == other.postalCode
    }

    override fun toString(): String = "Client(name=$name, postalCode=$postalCode)"
}
```
  
`equals`를 오버라이드하고 나면 프로퍼티의 값이 모두 같은 두 Client 객체는 **동등**하리라 예상할 수 있다. 실제로 `client1 == client2`는 이제 true를 반환한다.
  - 하지만 Client 클래스로 더 복잡한 작업을 수행해보면 제대로 작동하지 않는 경우가 있다. 이와 관련해서 흔히 면접에서 질문하는 내용이 **"Client가 제대로 작동하지 않는 경우를 말하고 문제가 무엇인지 설명하시오"** 다. `hashCode` 정의를 빠뜨려서 그렇다고 답하는 개발자가 많을 것이다. 이 경우에는 실제 **`hashCode`가 없다는 점이 원인**이다. 이제 왜 hashCode가 중요한지 알아보자.

### 1-3. 해시 컨테이너: `hashCode()`

자바에서는 `equals`를 오버라이드할 때 반드시 `hashCode`도 함께 오버라이드해야 한다. 이유는 다음과 같다.

원소가 'KAMIYU'라는 고객 하나뿐인 집합을 만들자. 그 후 새로 원래의 'KAMIYU'와 똑같은 프로퍼티를 포함하는 새로운 Client 인스턴스를 만들어서 그 인스턴스가 집합 안에 들어있는지 검사해보자. 프로퍼티가 모두 일치하므로 새 인스턴스와 집합에 있는 기존 인스턴스는 동등하다. 따라서 새 인스턴스가 집합에 속했는지 여부를 검사하면 true가 반환되리라 예상할 수 있다. **하지만 실제로는 false가 나온다.**

```kotlin
>>> val processed = hashSetOf(Client("KAMIYU", 123456))
>>> println(processed.contains(Client("KAMIYU", 123456)))

false
```

이는 Client 클래스가 `hashCode` 메소드를 정의하지 않았기 때문이다. JVM 언어에서는 `hashCode`가 지켜야 하는 **"`equals()`가 true를 반환하는 두 객체는 반드시 같은 `hashCode()`를 반환해야 한다"** 라는 제약이 있는데 이를 Client는 어기고 있기 때문이다.

processed 집합은 HashSet이다. HashSet은 원소를 비교할 때 비용을 줄이기 위해 먼저 객체의 해시 코드를 비교하고 해시 코드가 같은 경우에만 실제 값을 비교한다. 방금 본 예제의 두 Client 인스턴스는 해시 코드가 다르기 때문에 두 번째 인스턴스가 집합 안에 들어있지 않다고 판단한다. 해시 코드가 다를 때 `equals`가 반환하는 값은 판단 결과에 영향을 끼치지 못한다. 즉, 원소 객체들이 해시 코드에 대한 규칙을 지키지 않는 경우 HashSet은 제대로 작동할 수 없다. 이 문제를 고치려면 Client가 `hashCode`를 구현해야 한다.

> Client에 `hashCode()` 구현하기 

```kotlin
class Client(val name: String, val postalCode: Int) {
    ...
    override fun hashCode(): Int = name.hashCode() * 31 + postalCode
}
```

이제 이 클래스는 예상대로 작동한다. 하지만 지금까지 얼마나 많은 코드를 작성해야 했는지 생각해보라. 다행히 코틀린 컴파일러는 이 모든 메소드를 자동으로 생성해줄 수 있다. 어떻게 하면 코틀린이 이런 메소드를 생성하게 만들 수 있는지 살펴보자.

## 2. 데이터 클래스: 모든 클래스가 정의해야 하는 메소드 자동 생성

어떤 클래스가 데이터를 저장하는 역할만을 수행한다면 `toString`, `equals`, `hashCode`를 반드시 오버라이드해야 한다. IntelliJ 같은 
IDE는 이러한 메소드들을 자동으로 정의해주고, 작성된 메소드의 정확성과 일관성을 검사해준다.

하지만 코틀린은 더 편리하다! 이제는 이런 메소드를 IDE를 통해 생성할 필요도 없이 **`data`라는 변경자를 클래스 앞에 붙이면 필요한 메소드를 컴파일러가 자동으로 만들어준다.** `data` 변경자가 붙은 클래스를 **데이터 클래스**라고 부른다.

> Client를 데이터 클래스로 선언하기

```kotlin
data class Client(val name: String, val postalCode: Int)
```

이제 Client 클래스는 자바에서 요구하는 모든 메소드를 포함한다.

- 인스턴스 간 비교를 위한 `equals`
- HashMap과 같은 해시 기반 컨테이너에서 키로 사용할 수 있는 `hashCode`
- 클래스의 각 필드를 선언 순서대로 표시하는 문자열 표현을 만들어주는 `toString`

`equals`와 `hashCode`는 주 생성자에 나열된 모든 프로퍼티를 고려해 만들어진다. 생성된 `equals` 메소드는 모든 프로퍼티 값의 동등성을 확인한다. `hashCode` 메소드는 모든 프로퍼티의 해시 값을 바탕으로 계산한 해시 값을 반환한다. 이때 주 생성자 밖에 정의된 프로퍼티는 `equals`나 `hashCode`를 계산할 때 고려의 대상이 아니라는 사실에 유의하라.

코틀린 컴파일러는 data 클래스에게 방금 말한 세 메소드뿐 아니라 몇 가지 유용한 메소드를 더 생성해준다. 

### 2-1. 데이터 클래스와 불변성: `copy()` 메소드

데이터 클래스의 프로퍼티가 꼭 `val` 일 필요는 없다. 원한다면 `var` 프로퍼티를 써도 되지만 모든 프로퍼티를 읽기 전용으로 만들어서 데이터 클래스를 불변 <sup>immutable</sup> 클래스로 만들라고 권장한다. HashMap 등의 컨테이너에 데이터 클래스 객체를 담는 경우엔 불변성이 필수적이다. 데이터 클래스 객체를 키로 하는 값을 컨테이너애 담은 다음에 키로 쓰인 데이터 객체의 프로퍼티를 변경하면 컨테이너 상태가 잘못될 수 있다. 게다가 불변 객체를 사용하면 프로그램에 대해 훨씬 쉽게 추론할 수 있다. 특히 다중 스레드 프로그램의 경우 이런 성질은 더 중요하다. 불변 객체를 주로 사용하는 프로그램에서는 스레드가 사용 중인 데이터를 다른 스레드가 변경할 수 없으므로 스레드를 동기화해야 할 필요가 줄어든다.

데이터 클래스 인스턴스를 불변 객체로 더 쉽게 활용할 수 있게 코틀린 컴파일러는 **객체를 복사하면서 일부 프로퍼티를 바꿀 수 있게 해주는 `copy` 메소드를 제공한다.** 객체를 메모리상에서 직접 바꾸는 대신 복사본을 만드는 편이 더 낫다. 복사본은 원본과 다른 생명주기를 가지며, 복사를 하면서 일부 프로퍼티 값을 바꾸거나 복사본을 제거해도 프로그램에서 원본을 참조하는 다른 부분에 전혀 영향을 끼치지 않는다. Client의 `copy`를 직접 구현한다면 다음과 같을 것이다.

```kotlin
class Client(val name: String, val postalCode: Int) {
    ...
    fun copy(name: String = this.name, postalCode: Int = this.postalCode) = Client(name, postalCode)
}
```

다음은 `copy` 메소드를 사용하는 방법이다.

```kotlin
>>> val kamiyu = Client("KAMIYU", 123456)
>>> println(kamiyu.copy(postalCode = 111000))

Client(name=KAMIYU, postalCode=111000)
```

### 2-2. 구조 분해 선언과 component 함수

구조 분해 선언 <sup>destructuring declaration</sup>이라는 개념에서 **구조 분해를 사용하면 복합적인 값을 분해해서 여러 다른 변수를 한꺼번에 초기화 할 수 있다.**

> 구조 분해 사용 방법

```kotlin
data class Point(val x: Int, val y: Int)
...

>>> val p = Point(10, 20)
>>> val (x, y) = p
>>> println(x)
10
>>> println(y)
20
```

**구조 분해 선언**은 일반 변수 선언과 비슷해 보인다. 다만 **`=`의 좌변에 여러 변수를 괄호로 묶었다**는 점이 다르다.

내부에서 구조 분해 선언은 다시 관례를 사용한다. 구조 분해 선언의 각 변수를 초기화하기 위해 `componentN`이라는 함수를 호출한다. 여기서 `N`은 구조 분해 선언에 있는 변수 위치에 따라 붙는 번호다. 앞에서 살펴본 `val (x, y) = p`는 아래와 같이 컴파일된다.

```kotlin
    val (a,b) = p
          ↓
    val a = p.component1()
    val b = p.component2()     
```

data 클래스의 주 생성자에 들어있는 프로퍼티에 대해서는 컴파일러가 자동으로 `componentN` 함수를 만들어준다. 다음 예제는 데이터 타입이 아닌 클래스에서 이런 함수를 어떻게 구현하는지 보여준다.

```kotlin
class Point(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y
}

// componentN 함수가 구현되어 있지 않다면 아래와 같은 오류를 보여준다.
// Destructuring declaration initializer of type Point must have a 'component1()' function
```

구조 분해 선언은 함수에서 여러 값을 반환할 때 유용하다. 여러 값을 한꺼번에 반환해야 하는 함수가 있다면 반환해야 하는 모든 값이 들어갈 데이터 클래스를 정의하고 함수의 반환 타입을 그 데이터 클래스로 바꾼다. 구조 분해 선언 구문을 사용하면 이런 함수가 반환하는 값을 쉽게 풀어서 여러 변수에 넣을 수 있다.

이런 동작을 보여주기 위해 파일 이름을 이름과 확장자로 나누는 함수를 작성해보자.

> 구조 분해 선언을 사용해 여러 값 반환하기

```kotlin
data class NameComponents(val name: String, val extension: String) // 값을 저장하기 위한 데이터 클래스를 선언

fun splitFileName(fullName: String) : NameComponents {
    val result = fullName.split(".", limit = 2)
    return NameComponents(result[0], result[1]) // 함수에서 데이터 클래스의 인스턴스를 반환
}

>>> val (name, ext) = splitFileName("example.kt") // 구조 분해 선언 구문을 사용해 데이터 클래스를 푼다
>>> println(name)
example
>>> println(ext)
kt
```

배열이나 컬렉션에도 `componentN` 함수가 있음을 안다면 위 예제를 더 개선할 수 있다. 크기가 정해진 컬렉션을 다루는 경우 구조 분해가 특히 더 유용하다. 예를 들어 여기서 split은 2개의 원소로 이뤄진 리스트를 반환한다.

> 컬렉션에 대해 구조 분해 선언 사용하기

```kotlin
data class NameComponents(val name: String, val extension: String)

fun splitFileName(fullName: String) : NameComponents {
    val (name, ext) = fullName.split(".", limit = 2)
    return NameComponents(name, ext)
}
```

물론 무한히 `componentN`을 선언할 수 없으므로 이런 구문을 무한정 사용할 수는 없다. 그럼에도 불구하고 여전히 컬렉션에 대한 구조 분해는 유용하다. 코틀린 표준 라이브러리에서는 맨 앞의 다섯 원소에 대한 `componentN`을 제공한다.

#### 구조 분해 선언과 루프

함수 본문 내의 선언문뿐만 아니라 변수 선언이 들어갈 수 있는 장소라면 어디든 구조 분해 선언을 할 수 있다. 예를 들어 루프 안에서도 구조 분해 선언을 사용할 수 있다. 특히 Map의 원소에 대해 이터레이션할 때 구조 분해 선언이 유용하다.

> 구조 분해 선언을 사용해 맵 이터레이션 하기

```kotlin
fun printEntries(map: Map<String, String>) {
    for ((key, value) in map) { // 루프 변수에 구조 분해 선언 사용
        println("$key -> $value")
    }
}

>>> val map = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")
>>> printEntries(map)
Oracle -> Java
JetBrains -> Kotlin
```

이 간단한 예제는 두 가지 코틀린 관례를 활용한다. 하나는 객체를 iteration하는 관례고, 다른 하나는 구조 분해 선언이다. 코틀린 표준 라이브러리에는 맵에 대한 확장 함수로 iterator가 들어있다. 그 iterator는 맵 원소에 대한 이터레이터를 반환한다. 따라서 자바와 달리 코틀린에서는 맵을 직접 이터레이션할 수 있다. 또한 코틀린 라이브러리는 Map.Entry에 대한 확장 함수로 `component1`과 `component2`를 제공한다. 위의 루프는 이런 확장 함수를 사용하는 아래의 코드와 같다.

```kotlin
for (entry in map.entries) {
    val key = entry.component1()
    val value = entry.component2()
    println("$key -> $value")
}
```

이 예제는 코틀린 관례를 적용할 때 확장 함수가 얼마나 중요한 역할을 하는지 잘 보여준다.

## References

- [Kotlin in Action](http://www.acornpub.co.kr/book/kotlin-in-action#toc) - 4.3절 <sup>171p</sup>, 7.4절 <sup>326p</sup>
- https://kotlinlang.org/docs/data-classes.html#data-classes-and-destructuring-declarations
- https://kotlinlang.org/docs/destructuring-declarations.html#example-returning-two-values-from-a-function

------

<b id = "f1"><sup> 1 </sup></b>  [ 동등성 연산에 `==` 사용 ↩](#a1)

> **동일하다** : 두 객체가 완전히 같을 경우 / **동등하다** : 두 객체가 같은 정보를 같고 있을 경우

자바에서는 `==`를 원시 타입과 참조 타입을 비교할 때 사용한다. 원시 타입의 경우 `==`는 두 피연사자의 값이 같으지 비교한다 <sup>동등성(equality)</sup>. 반면 참조 타입의 경우 `==`는 두 피연산자의 주소가 같은지를 비교한다 <sup>참조 비교(reference comparision)</sup>. 따라서 자바에서는 두 객체의 동등성을 알려면 `equals`를 호출해야 한다. 자바에서는 `equals` 대신 `==`를 호출하면 문제가 될 수 있다는 사실도 아주 잘 알려져 있다.

코틀린에서는 `==` 연산자가 두 객체를 비교하는 기본적인 방법이다. `==`는 내부적으로 `equals`를 호출해서 객체를 비교한다. 따라서 클래스가 `equals`를 오버라이드하면 `==`를 통해 안전하게 그 클래스의 인스턴스를 비교할 수 있다. 참조 비교를 위해서는 `===` 연산자를 사용할 수 있다. `===` 연산자는 자바에서 객체의 참조를 비교할 때 사용하는 `==` 연산자와 같다.

- `==` 연산자는 자바와 코틀린에서 원시 타입의 비교에서는 동일하게 동작하지만, **참조 타입의 비교에서는 다르게 동작한다.**
  - 자바의 참조 타입 비교 `==` : **주소**가 같은지를 비교
  - 코틀린의 참조 타입 비교 `==` : 내부적으로 `equals`를 호출해서 주소 비교가 아닌 **동등성** 비교를 한다.