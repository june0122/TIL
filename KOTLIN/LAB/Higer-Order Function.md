# 고차 함수: 파라미터와 반환 값으로 람다 사용

> ### 함수에서 정책을 분리하고 싶다면, 인자로 함수를 받으면 된다.

- `+` 연산 뿐만 아니라 `*`, `-` 등 다양한 연산을 사용하고 싶을 때.

- 초기값에 대한 부분도 변경하고 싶을 때.

<br>

> ① 정책을 분리하기 전

```kotlin
fun fold(list: List<Int>): Int {
    var sum = 0
    for (e in list)
        sum += e  // 분리할 부분
    return sum
}
```

<br>

> ② 정책 분리 후

- 초기값을 설정하고, 인자로 함수를 받게 하였다.

```kotlin
fun reduce(list: List<Int>, initialValue: Int, combine: (Int, Int) -> Int): Int {
    var sum = initialValue
    for (e in list)
        sum = combine(sum, e)

    return sum
}
```

```kotlin
fun main() {
    val items = listOf(1, 2, 3, 4)

//    val add = reduce(items, 0, { r, e ->
//        r + e })

// 함수 호출 시 맨 마지막 인자가 람다식이면 아래와 같이 이를 괄호 밖으로 빼낼 수 있다.

    val add = reduce(items, 0) { r, e ->
        r + e }

    println(add)

}
```

<br>

> ③ 타입에 상관없이 사용 : **제네릭**


```kotlin
fun <T, R> reduce(list: List<T>, initialVaule: R, combine: (R, T) -> R): R {
    var accumulator = initialVaule
    for (e in list)
        accumulator = combine(accumulator, e)
    return accumulator
}
```

<br>

> ④ 확장 함수 형태로 변경

```kotlin
fun <T, R> List<T>.reduce(initialVaule: R, combine: (R, T) -> R): R {
    var accumulator = initialVaule
    for (e in this)
        accumulator = combine(accumulator, e)
    return accumulator
}
```

<br>

> ⑤ List 뿐만이 아닌 모든 콜렉션 타입 대입 가능하게 만들기

```kotlin
fun <T, R> Collection<T>.reduce(initialVaule: R, combine: (R, T) -> R): R {
    var accumulator = initialVaule
    for (e in this)
        accumulator = combine(accumulator, e)
    return accumulator
}
```

```kotlin
fun main() {
    val items = listOf(1, 2, 3, 4)
    val add = items.reduce(0) { r, e ->
        r + e
    }
    println(add)
}
```

### 함수에서 정책을 분리하는 2가지 방법

> 변하지 않는 전체 알고리즘에서 변해야 하는 정책은 분리되어야 한다 → 공통성과 가변성의 분리
  
1) Java : 동작 파라미터화 설계

- 정책을 인터페이스 기반 클래스를 통해 분리한다.

```kotlin
interface Predicate<E> {
    fun test(e: E): Boolean
}

fun filter(data: List<Int>, predicate: Predicate<Int>): List<Int> {
    val result = mutableListOf<Int>()
    for (e in data) {
        if (predicate.test(e))
            result.add(e)
    }
    return result
}


fun main() {
    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val result1 = filter(list, object : Predicate<Int> {
        override fun test(e: Int): Boolean = e % 2 == 1
    })
    println(result1)

    val result2 = filter(list, object : Predicate<Int> {
        override fun test(e: Int): Boolean = e % 2 == 0
    })
    println(result2)
}
```
    
2) Kotlin : 정책 함수를 인자로 전달

```kotlin
// 정책 함수를 인자로 전달하면 됩니다.
fun isOdd(e: Int) = e % 2 == 1
fun isEven(e: Int) = e % 2 == 0

// (Int) -> Boolean

fun filter(data: List<Int>, predicate: (Int) -> Boolean): List<Int> {
    val result = mutableListOf<Int>()
    for (e in data) {
        if (predicate(e))
            result.add(e)
    }
    return result
}


fun main() {
    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val result1 = filter(list, ::isOdd)
    println(result1)

    val result2 = filter(list, ::isEven)
    println(result2)
}
```