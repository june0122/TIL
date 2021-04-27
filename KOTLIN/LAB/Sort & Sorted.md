# `Sort` & `Sorted`

### 정의

> `<동사>()` 메서드는 객체를 수정하는 반면, `<동사>ed()` 메서드는 새로운 객체를 리턴한다.

- `sort`는 원본 리스트에 적용되며 어떠한 것도 리턴하지 않는다.
- `sorted`는 원본 리스트를 변경하지 않지만 변경이 적용된 새로운 리스트를 리턴한다.

### MutableList와 List의 `sort`

- `sortBy`, `sortWith` 혹은 `sort` <sup>sorted가 아닌</sup>로 시작하는 메서드가 필요하다면 원본 리스트의 변경이 가능한 MutableList 타입을 사용해야한다.

<br>
<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/116203722-f4349b80-a776-11eb-831d-446f1e0c8ad4.png'>
</p>

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/116203791-08789880-a777-11eb-8ea2-37e896ce855d.png'>
</p>
<br>

### Kotlin docs

> `sortBy`

```kotlin
/**
 * Sorts elements in the list in-place according to natural sort order of the value returned by specified [selector] function.
 * 
 * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
 */
public inline fun <T, R : Comparable<R>> MutableList<T>.sortBy(crossinline selector: (T) -> R?):Unit {
    if (size > 1) sortWith(compareBy(selector))
}
```


> `sortedBy`

```kotlin
/**
 * Returns a list of all elements sorted according to natural sort order of the value returned by specified [selector] function.
 * 
 * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
 */
public inline fun <T, R : Comparable<R>> Iterable<T>.sortedBy(crossinline selector: (T) -> R?): List<T> {
    return sortedWith(compareBy(selector))
}
```


## References

[What is the difference between sortBy, sortedBy in Kotlin](https://stackoverflow.com/questions/61208049/what-is-the-difference-between-sortby-sortedby-and-sortwith-sortedwith-in-ko)