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

