# 체이닝 메서드 패턴이 적용된 코틀린 코드의 시간복잡도 구하기

> 다음 코드의 시간복잡도는 어떻게 될까요?

```kotlin
arr.filter { it > 0 }.reduce { acc, i -> acc + i }
```

일반적인 for-loop문이 들어간 명령형 코드에서 시간복잡도를 계산하는건 직관적이고 익숙하지만, 함수형 코드의 시간복잡도를 계산하는건 다소 낯선 일이다.

더욱이 위의 코드와 같이 [메서드 체이닝<small>(method chaining)</small>](https://en.wikipedia.org/wiki/Method_chaining) 패턴으로 작성된 코드의 시간복잡도를 구하라는 이야기를 처음 들으면 당황스러울 수 있다<small>(면접에서 내가 그랬다...)</small>. 다음에는 당황하는 일이 없도록 잘 정리해보자.

아래 예제의 확장 함수 `IntArray.sumPositive()`는 IntArray의 요소 중 양수만 걸러내고<small>(filter)</small> 그 결과에 대해 초기값 없이 첫번째 요소부터 컬렉션 내의 데이터를 모두 모으는<small>(reduce)</small> 함수다.

```kotlin
fun IntArray.sumPositive() = this.filter { it > 0 }.reduce { acc, i -> acc + i }

println(intArrayOf(1, 2, 3, 4).sumPositive()) // 10
println(intArrayOf(1, 2, 3, -4).sumPositive()) // 6
```

체이닝 메서드는 복잡하게 생각할 것 없이 편의성을 위한 것이다. 컬렉션 함수형 API의 필수적인 함수 `filter`와 `map`은 모두 List를 반환하기 때문에 `val result = arr.map { ... }`처럼 변수명을 선언하여 그 `result` list에 대해 다른 작업을 할 수도 있고, **또는** `arr.map { ... }.filter { ... }.groupBy { ... }`처럼 반환된 list에 대해 직접 작업을 수행할 수도 있다.

**즉,체이닝 메서드는 순차 실행과 동일하다.** 다음의 예시를 보자.

```kotlin
// 메서드 체이닝
fun IntArray.sumPositive() = this.filter { it > 0 }.reduce { acc, i -> acc + i }

// 순차 실행
val arr = intArrayOf(1, 2, 3, -4)
val filteredArr = arr.filter { it > 0 } // O(n)
val reducedResult = filteredArr.reduce { acc, i -> acc + i } // O(n)

println(arr.sumPositive()) // 6
println(reducedResult) // 6
```

보다시피 `filter()`가 <i>O(n)</i>, `reduce()`도 <i>O(n)</i>의 시간복잡도를 가지므로 <i>O(n) + O(n) => O(n)</i>이라는 시간복잡도를 얻을 수 있다.



```kotlin
fun groupAnagramsByProcedure(strs: Array<String>): List<List<String>> {
    // n = length of strs
    // k = maximum length of a string in strs
    val sortedGroup = strs.groupBy { s -> // 기본적으로 O(n)
        val charArr = s.toCharArray() // 최악의 경우 O(k), System.arraycopy()가 네이티브 메서드라 정확히 예측 불가
        val sortedArr = charArr.sorted() // O(klogk), TimSort(Insertion + Merge)
        sortedArr.toString() // O(k)
    }
    return sortedGroup.map { it.value } // O(n)
}

// O(n) * ( O(k) + O(klogk) + O(k) ) + O(n)

/**
 * charArr.sorted()는 원시타입 배열에 대한 정렬이라 TimSort 아닌가?
 * 원래 원시형 타입은 dual-pivot quicksort 사용하는게 맞지만
 * sorted()는 toTypeArray()를 사용하여 CharArray가 아닌 Array<Char>에 대해 sort() 하므로 
 * 참조형에 대한 정렬!!!
 */
```

## References

- stackoverflow: [Complexity of chaining methods in javascript](https://stackoverflow.com/questions/59800533/)