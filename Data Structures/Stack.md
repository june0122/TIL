# Stack

## 스택의 연산들

스택에는 오직 두 개의 필수적인 연산들이 존재한다.

- `push` : 스택 맨 위에 요소를 추가
- `pop` : 스택 맨 위의 요소를 제거

즉, 스택은 한 쪽에서만 요소의 추가나 제거가 가능한 자료구조이다. Computer science에선 스택은 LIFO<small>(last-in first-out)</small> 자료구조라고도 한다. 마지막에 들어간<small>(push)</small> 요소들은 가장 먼저 나온다<small>(pop)</small>.

> stack 패키지 내부에 Stack 인터페이스 선언 (stack/Stack.kt)

```kotlin
interface Stack<T : Any> {
    fun push(element: T)
    fun pop(): T?
}
```
```
※ 위의 Stack 인터페이스는 Vector 클래스를 상속받고 본문에서 필요로 하지 않는 메서드들을 제공하는 Kotlin과 Java의 Stack 클래스와 다르다.  
```

스택은 다음과 같은 프로그래밍 분야에서 눈에 띄게 사용된다.

- 안드로이드는 **fragment stack**을 사용하여 Activity의 안팎으로 fragment들을 push 및 pop을 한다.
- 메모리 할당은 아키텍처 수준에서 스택을 사용한다. **지역 변수의 메모리**도 스택을 사용하여 관리된다.
- 미로에서 길을 찾는 것과 같은 **Search and conquer 알고리즘**은 스택을 사용하여 백트래킹을 용이하게 한다.

## 구현

다양한 방식으로 Stack 인터페이스를 구현할 수 있는데 올바른 storage type을 선택하는 것이 중요하다. **ArrayList**는 마지막 인덱스를 매개변수로 사용하여 `add` 및 `removeAt`을 통해 한쪽 끝에서 O(1)<small>(상수 시간)</small> 삽입 및 삭제를 제공하므로 확실한 선택이다. 이 두 연산을 사용하면 스택의 LIFO 특성이 쉽게 구현된다.

> StackImpl 클래스에 `toString` 재정의 (StackImpl.kt)

```kotlin
class StackImpl<T : Any> : Stack<T> {
    private val storage = arrayListOf<T>()  
    override fun toString() = buildString {
      appendLine("----top----")
      storage.asReversed().forEach {
        appendLine("$it")
      }
      appendLine("-----------")
    }
}
```

데이터에 대해 ArrayList 유형의 private property를 정의하고 디버그 목적으로 해당 내용을 표시하기 위해 `toString` 메서드를 재정의한다. <small>(이 코드를 사용하면 아직 push 및 pop 연산을 구현하지 않았기에 오류가 발생한다.)</small>

### push 및 pop 연산

> `push` 및 `pop` 추가 (StackImpl.kt)

```kotlin
override fun push(element: T) {
    storage.add(element)
}

override fun pop(): T? {
    if (storage.size == 0) {
      return null
    }
    return storage.removeAt(storage.size - 1)
}
```

`push` 메서드에서 ArrayList의 `add` 메서드를 이용해서 매개변수로 전달된 값을 ArrayList의 끝에 추가한다<small>(append)</small>. `pop` 메서드는 ArrayList가 비어 있으면 null을 반환하고 그렇지 않다면 마지막에 삽입한 요소를 제거하고 반환한다.

아래의 코드를 통해 직접 구현한 스택이 올바르게 작동하는지 확인한다.

> 테스트용 코드 작성 (Main.kt)

```kotlin
fun main() {
    val stack = StackImpl<Int>().apply {
        push(1)
        push(2)
        push(3)
        push(4)
    }
    print(stack)

    val poppedElement = stack.pop()
    if (poppedElement != null) {
        println("Popped: $poppedElement")
    }
    print(stack)
}
```

```
----top----
4
3
2
1
-----------
Popped: 4
----top----
3
2
1
-----------
```

`push` 및 `pop`은 둘 다 O(1) 시간 복잡도를 가진다.

### 유용한 추가 연산들

스택을 더 쉽게 사용할 수 있는 몇 가지 유용한 연산들을 추가한다.

> Stack 인터페이스에 `peek` 추가 (stack/Stack.kt)

```kotlin
interface Stack<T : Any> {
    ...

    fun peek(): T?

    val count: Int
        get

    val isEmpty: Boolean
        get() = count == 0
}
```

`peek`의 개념은 내용을 변경하지 않고 스택의 맨 위 요소를 보는 것이다. *count* 속성은 스택의 요소 수를 반환하며 *isEmpty* 속성을 구현하는데 사용된다.

> `peek` 구현하기 (StackImpl.kt)

```kotlin
override fun peek(): T? {
    return storage.lastOrNull()
}
override val count: Int
    get() = storage.size
```

`peek`을 구현함으로써 `pop`의 구현을 더 깔끔한 코드로 변경할 수 있다.

> `pop` 코드를 더 깔끔하게 수정

```kotlin
override fun pop(): T? {
    if (isEmpty) {
        return null
    }
    return storage.removeAt(storage.size - 1)
}
```

### 스택과 Kotlin Collection Interfaces

스택에 Kotlin Collection 인터페이스를 채택할 수 있을지 궁금할 수 있다. 스택의 목적은 데이터에 액세스하는 방법의 수를 제한하는 것인데, **Iterable**과 같은 인터페이스를 채택하면 iterator를 통해 모든 요소들을 노출함으로써 당초의 목표와 어긋나게 된다.

액세스 순서가 보장되도록 기존의 List를 가져와 스택으로 변환하는 것을 원할 수 있다. 물론 배열의 요소들을 순회하고 각 요소들을 `push` 할 수 있다. 하지만 이러한 요소를 Stack 구현에 직접적으로 추가하는 <a href="https://github.com/june0122/Effective-Java/blob/b24a45bbf5e320971bf2dfafe5bcbd38feeb9039/item%2001.md"><b>정적 팩토리 메서드<small>(static factory method)</small></b></a>를 작성할 수 있다.

> 정적 팩토리 메서드 사용하기 (StackImpl.kt)

```kotlin
companion object {
    fun <T : Any> create(items: Iterable<T>): Stack<T> {
      val stack = StackImpl<T>()
      for (item in items) {
        stack.push(item)
      }
      return stack
    }
  }
```

> 테스트용 코드 (Main.kt)

```kotlin
fun main() {
    val list = listOf("A", "B", "C", "D")
    val stack = StackImpl.create(list)
    print(stack)
    println("Popped: ${stack.pop()}")
}
```

```
----top----
D
C
B
A
-----------
Popped: D
```

이 코드는 문자열 스택을 생성하고 최상위 요소인 "D"를 pop 한다.

한 단계 더 나아가 `listOf()` 및 기타 표준 라이브러리 collection factory 함수와 유사한 요소를 나열하여 스택을 초기화할 수 있다. 이를 Stack.kt의 Stack을 구현한 클래스 외부에 추가한다.

> 스택 초기화 함수 추가 (Stack.kt)

```kotlin
fun <T : Any> stackOf(vararg elements: T): Stack<T> {
    return StackImpl.create(elements.asList())
}
```

> 테스트용 코드 (Main.kt)

```kotlin
fun main() {
    val stack = stackOf(1.0, 2.0, 3.0, 4.0)
    print(stack)
    println("Popped: ${stack.pop()}")
}
```

```
----top----
4.0
3.0
2.0
1.0
-----------
Popped: 4.0
```

이렇게 하면 Double의 스택이 생성되고 최상위 값인 4.0이 표시된다. 코틀린 컴파일러의 타입 추론 기능 덕분에 `stackOf` 함수 호출의 제네릭 타입 인자를 지정하지 않아도 된다.

스택은 트리와 그래프를 검색하는 문제에 매우 중요하다. 미로를 통해 길을 찾는다고 상상할 때, 왼쪽, 오른쪽 또는 직진을 결정해야 하는 지점에 올 때마다 가능한 모든 결정들을 스택에 넣을 수 있다. <small>(When you hit a dead end, backtrack by popping from the stack and continuing until you escape or hit another dead end.)</small>

## 스택의 활용

### LinkedList 뒤집기

```kotlin
fun <T : Any> LinkedList<T>.printInReverse() {
    val stack = StackImpl<T>()

    for (node in this) {
        stack.push(node)
    }

    var node = stack.pop()
    while (node != null) {
        println(node)
        node = stack.pop()
    }
}
```

### 괄호 확인

```
// 1
h((e))llo(world)() // balanced parentheses

// 2
(hello world // unbalanced parentheses
```

```kotlin
fun String.checkParentheses(): Boolean {
    val stack = StackImpl<Char>()

    for (char in this) {
        when (char) {
            '(' -> stack.push(char)
            ')' -> {
                if (stack.isEmpty) return false
                else stack.pop()
            }
        }
    }
    return stack.isEmpty
}
```

스택은 괄호 관련 코딩 테스트 문제에 자주 등장하므로 사용 방법을 숙지할 필요가 있다.

- [프로그래머스 레벨 2 - 괄호 변환](https://programmers.co.kr/learn/courses/30/lessons/60058)
- [프로그래머스 레벨 2 - 괄호 회전하기](https://programmers.co.kr/learn/courses/30/lessons/76502)