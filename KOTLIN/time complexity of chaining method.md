# 메서드 체이닝 패턴이 적용된 코틀린 코드의 시간 복잡도 구하기

> 다음 코드의 시간 복잡도는 어떻게 될까?

```kotlin
arr.filter { it > 0 }.reduce { acc, i -> acc + i }
```

일반적인 for-loop문이 들어간 명령형 코드에서 시간 복잡도를 계산하는건 직관적이고 익숙하지만, 함수형 코드의 시간 복잡도를 계산하는건 다소 낯선 일이다.

더욱이 위의 코드와 같이 [메서드 체이닝<small>(method chaining)</small>](https://en.wikipedia.org/wiki/Method_chaining) 패턴으로 작성된 코드의 시간 복잡도를 구하라는 이야기를 처음 들으면 당황스러울 수 있다<small>(면접에서 내가 그랬다...)</small>. 다음에는 당황하는 일이 없도록 잘 정리해보자.

아래 예제의 확장 함수 `IntArray.sumPositive()`는 IntArray의 요소 중 양수만 걸러내고<small>(filter)</small> 그 결과에 대해 초기값 없이 첫번째 요소부터 컬렉션 내의 데이터를 모두 모으는<small>(reduce)</small> 함수다.

```kotlin
fun IntArray.sumPositive() = this.filter { it > 0 }.reduce { acc, i -> acc + i }

println(intArrayOf(1, 2, 3, 4).sumPositive()) // 10
println(intArrayOf(1, 2, 3, -4).sumPositive()) // 6
```

메서드 체이닝은 복잡하게 생각할 것 없이 편의성을 위한 것이다. 컬렉션 함수형 API의 필수적인 함수 `filter`와 `map`은 모두 List를 반환하기 때문에 `val result = arr.map { ... }`처럼 변수명을 선언하여 그 `result` list에 대해 다른 작업을 할 수도 있고, **또는** `arr.map { ... }.filter { ... }.groupBy { ... }`처럼 반환된 list에 대해 직접 작업을 수행할 수도 있다.

**즉, 메서드 체이닝은 순차 실행과 동일하다.** 다음의 예시를 보자.

### 예제 코드 1

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

보다시피 `filter()`가 <i>O(n)</i>, `reduce()`도 <i>O(n)</i>의 시간 복잡도를 가지므로 <i>O(n) + O(n) => O(n)</i>이라는 시간복잡도를 얻을 수 있다.

연습을 위해 메서드 체이닝을 사용하는 다른 코드도 살펴보자. 람다 내부에 메서드 체이닝이 중첩된 경우에는 어떻게 시간 복잡도를 분석해야 할까?

### 예제 코드 2

```kotlin
// 메서드 체이닝
fun groupAnagrams(strs: Array<String>): List<List<String>>
    = strs.groupBy { it.toCharArray().sorted().toString() }.map { it.value }

// 순차 실행
fun groupAnagramsByProcedure(strs: Array<String>): List<List<String>> {
    // n = length of strs
    // k = maximum length of a string in strs
    val sortedGroup = strs.groupBy { s -> // 기본적으로 O(n)
        val charArr = s.toCharArray() // 최악의 경우 O(k), System.arraycopy()가 네이티브 메서드라 정확히 예측 불가
        val sortedArr = charArr.sorted() // O(klogk), TimSort(Insertion Sort + Merge Sort)
        sortedArr.toString() // O(k)
    }
    return sortedGroup.map { it.value } // O(n)
}

// O(n) * ( O(k) + O(klogk) + O(k) ) + O(n) => O(nklogk)
```

람다 내부에 메서드 체이닝이 중첩되어 있어도 똑같이 체이닝된 메서드들은 순차 실행처럼 처리를 한 뒤, 이를 둘러싸고 있는 `groupBy` 함수의 시간 복잡도를 곱해주는 형태로 계산할 수 있다. 중간에 정렬이 사용되는 부분이 있는데 혼동될 수 있는 부분이 있는 것 같아 추가적으로 정리하고자 한다.

#### 코틀린의 정렬은 어떻게 구현되어 있을까?

- `Arrays.sort`(primitive type) => Dual-Pivot QuickSort <small>(Insertion Sort + Quick Sort)</small>
  - 시간 복잡도, 최선: <i>O(nlogn)</i>, 평균: <i>O(nlogn)</i>, 최악: <i>O(N<sup>2</sup>)</i>
- `Collections.sort`(reference type) => TimSort <small>(Insertion Sort + Merge Sort)</small>
  - 시간 복잡도, 최선: <i>O(n)</i>, 평균: <i>O(nlogn)</i>, 최악: <i>O(nlogn)</i>

####  `charArr.sorted()`는 CharArray를 정렬한 것이라 원시 타입 배열일 때 사용되는 정렬인 Dual-Pivot QuickSort를 사용해야 되는가 아닌가?

결론부터 말하자면 `sort()`가 아닌 `sorted()`는 내부적으로 `toTypedArray()`로 인해 박싱된 Char 배열인 **Array&lt;Char&gt;** 타입을 반환한다. <b>Array&lt;Char&gt;</b>은 박싱된 Char의 배열<small>(자바 타입은 java.lang.Character[])</small>이다. 박싱되지 않은 원시 타입의 배열이 필요하다면 **CharArray**를 사용해야 한다.

코틀린은 원시 타입의 배열을 표현하는 별도 클래스를 각 원시 타입마다 하나씩 제공한다<small>(IntArray, ByteArray, CharArray…)</small>. 이 모든 타입은 자바 원시 타입 배열인 int[], byte[], char[] 등으로 컴파일된다. 따라서 그런 배열의 값은 박싱하지 않고 가장 효율적인 방식으로 저장된다.

## References

- stackoverflow: [Complexity of chaining methods in javascript](https://stackoverflow.com/questions/59800533/)
- kotlin in action. 6.3.5 객체의 배열과 원시 타입의 배열. 298p