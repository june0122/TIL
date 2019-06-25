# 5장 람다로 프로그래밍

## 5.1 람다 식과 멤버 참조

### 5.1.1 람다 소개: 코드 블록을 함수 인자로 넘기기

- 일련의 동작을 변수에 저장하거나 다른 함수에 넘겨야 하는 경우, **과거의 자바는 무명 내부 클래스를 이용**했지만 상당히 번거롭다.

  - 이와 달리 함수형 프로그래밍에서는 **함수를 값처럼 다루는 접근 방법**을 택함으로써 이 문제를 해결한다.

- 람다식을 사용하면 함수를 선언할 필요가 없고 코드 블록을 직접 함수의 인자로 전달할 수 있다.

> 무명 내부 클래스로 리스너 구현

```java
/* 자바 */
button.setOnClickListener(new onClickListener() {
    @Override
    public void onClick(View view) {
        /* 클릭 시 수행할 동작 */
    }
}); 
```

> 람다로 리스너 구현하기

```kotlin
button.setOnClickListener { /* 클릭 시 수행할 동작 */ }
```

<br>

### 5.1.2 람다와 컬렉션

> 컬렉션을 직접 검색

```kotlin
data class Person(val name: String, val age: Int)

fun findTheOldest(people: List<Person>) {
    var maxAge = 0
    var theOldest: Person? = null
    for (person in people) {
        if (person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    println(theOldest)
}

fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    findTheOldest(people)
}
```

> 람다를 사용해 컬렉션 검색

```kotlin
data class Person(val name: String, val age: Int)

fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    println(people.maxBy { it.age })
}
```

> 멤버 참조를 사용해 컬렉션 검색

```kotlin
// 위의 코드와 같은 일을 한다.
println(people.maxBy(Person::age))
```

<br>

### 5.1.3 람다 식의 문법

```kotlin
{ x: Int, y: Int -> x + y }
  --------------    ------
     파라미터         본문
```

- 코틀린 람다 식은 항상 중괄호로 둘러싸여 있다. 인자 목록 주변에 괄호가 없다는 사실을 꼭 기억하라.

- 코드의 일부분으로 블록으로 둘러싸 실행할 필요가 있다면 `run`을 사용한다. `run`은 인자로 받은 람다를 실행해주는 라이브러리 함수다.

- 실행 시점에 코틀린 람다 호출에는 아무 부가 비용이 들지 않으며, 프로그램의 기본 구성 요소와 비슷한 성능을 낸다.

```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31))
println(people.maxBy { it.age })
``` 

- 이 예제에서 코틀린이 코드를 줄여 쓸 수 있게 제공했던 기능을 제공하고 정식으로 람다를 작성하면 아래와 같다.

<br>

> 람다 ①

```kotlin
people.maxBy({ p: Person -> p.age })
```

- 중괄호 안에 있는 코드는 람다 식이고 그 람다식을 `maxBy` 함수에 넘긴다.

  - 람다 식은 `Person` 타입의 값을 인자로 받아서 인자의 `age`를 반환한다.

- 하지만 이 코드는 구분자가 많이 쓰여서 가독성이 떨어진다.

  - 컴파일러가 문맥으로부터 유추할 수 있는 인자 타입을 굳이 적을 필요는 없다.
  
  - 인자가 단 하나뿐인 경우 굳이 인자에 이름을 붙이지 않아도 된다.

- 코틀린에는 함수 호출 시 맨 뒤에 있는 인자가 람다 식이면 그 람다를 괄호 밖으로 빼낼 수 있는 문법 관습이 있다.

  - 이 예제는 람다가 유일한 인자이므로 마지막 인자이기도 하다. 따라서 괄호 뒤에 람다를 둘 수 있다.

<br>

> 람다 ② : 람다 식을 괄호 밖으로 이동

```kotlin
people.maxBy() { p: Person -> p.age }
```

<br>

> 람다 ③ : 람다가 어떤 함수의 유일한 인자이고 괄호 뒤에 람다를 썼다면 호출 시 빈 괄호를 없애도 된다.

```kotlin
people.maxBy { p: Person -> p.age }
```

<br>

> 람다 ④ : 람다 파라미터 타입 제거

- 컴파일러는 프로그래머가 `Person` 타입의 객체가 들어있는 컬렉션에 대해 `maxBy`를 호출한다는 사실을 알고 있으므로 람다의 파라미터도 `Person`이라는 사실을 이해할 수 있다.

```kotlin
people.maxBy { p: Person -> p.age }  // 파라미터 타입 명시
people.maxBy { p -> p.age }          // 파라미터 타입을 생략(컴파일러가 추론)
```

<br>

> 람다 ⑤ : 디폴트 파라미터 이름 `it` 사용

- 람다의 파라미터 이름을 디폴트 이름인 `it`으로 바꾸면 람다 식을 더 간단하게 만들 수 있다.

  - 람다의 파라미터가 하나뿐이고 그 타입을 컴파일러가 추론할 수 있는 경우 `it`을 바로 쓸 수 있다.
  
  - 람다 파라미터 이름을 따로 지정하지 않은 경우에만 `it`이라는 이름이 자동으로 만들어진다.

```kotlin
val names = people.maxBy { it.name }
```

- `it`을 사용하는 관습은 코드를 아주 간단하게 만들어준다. 하지만 이를 남용하면 안된다.

  - 특히 람다 안에 람다가 중첩되는 경우엔 각 람다의 파라미터를 명시하는 편이 낫다

  - 파라미터를 명시하지 않으면 각각의 `it`이 어떤 람다에 속했는지 파악하기 어려울 수 있다.



<br>

> joinToString 예제에 람다를 사용

```kotlin
>>> val people = listOf(Person("Alice", 29), Person("Bob", 31))
>>> val names = people.joinToString(separator = ", ", transform = { p -> p.name })

>>> println(names)

Alice, Bob
```

<br>

> joinToString, 람다를 괄호 밖에 전달

```kotlin
val names = people.joinToString(", ") { p -> p.name }
```

<br>

### 5.1.4 현쟈 영역에 있는 변수에 접근




---


- 클로저 : 외부에 있는 객체의 참조를 암묵적으로 얻어내는 것

  - 익명 개체도, 람다도 클로저를 지원한다.

- 람다 : 익명의 실행 가능한 코드 블럭을 참조하는 것

  - '코틀린에서' 익명 함수와 람다는 다른 개념으로 명확하게 분리되어 있다.

  - 자바 등 다른 언어에서는 같은 개념으로 보기도 하다.

