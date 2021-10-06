# 코틀린에서 배열의 원소들을 출력하는 방법

## 1. for loop

```kotlin
fun main() {
    val array = intArrayOf(1, 2, 3, 4, 5)

    for (element in array) {
        println(element)
    }
}
```

```
1
2
3
4
5
```

## 2. `contentToString()` : 일차원 배열 출력

```kotlin
fun main() {
    val array = intArrayOf(1, 2, 3, 4, 5)

    println(array.contentToString()) // java : Arrays.toString()
}
```

```
[1, 2, 3, 4, 5]
```

`println(array)`를 사용할 경우 `[I@3f0ee7cb`와 같은 이상한 출력값이 나옵니다. `array.toString()`과 같이 배열의 `toString()` 메서드는 내용이 아닌 배열의 ID를 설명하는 String을 반환합니다. 왜냐하면 배열은 `Object.toString()`을 재정의하지 않기 때문입니다.

배열 내용에 대한 문자열 표현을 얻으려면 java에서 `Arrays.toString(Object[])`, kotlin에선 `contentToString()`을 사용할 수 있습니다.

## 3. `contentDeepToString()` : 다차원 배열 출력

```kotlin
fun main() {
    val array = arrayOf(
        intArrayOf(1, 2),
        intArrayOf(3, 4),
        intArrayOf(5, 6, 7)
    )

    println(array.contentDeepToString()) // java : Arrays.deepToString()
}
```

```
[[1, 2], [3, 4], [5, 6, 7]]
```

다차원 배열의 경우 `contentToString()`을 사용하면 `[[I@3f0ee7cb, [I@75bd9247, [I@7d417077]`와 같은 결과가 나옵니다. 그러므로 다차원 배열의 원소를 출력하고 싶다면 `contentDeepToString()`을 사용해야 합니다.

## References

- stackoverflow : [Why does println(array) have strange output?](https://stackoverflow.com/questions/8410294/why-does-printlnarray-have-strange-output-ljava-lang-string3e25a5)
- [Kotlin Program to Print an Array](https://www.programiz.com/kotlin-programming/examples/print-array)