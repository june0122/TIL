# Linked List

**Linked list**는 선형, 단방향 시퀀스로 배열된 값의 모음이다. linked list는 *Kotlin Array, ArrayList*와 같은 연속적인 저장소 옵션들<small>(contiguous storage options)</small>에 비해 몇 가지 이론적인 장점이 있다.

1. 리스트의 앞부분에서 **상수 시간** 삽입 및 제거 수행
2. 안정적인 성능

<p align = 'center'>
<img width = '250' src = 'https://user-images.githubusercontent.com/39554623/122393576-f87b7900-cfaf-11eb-9b60-ac29477387db.png'>
</p>

다이어그램이 보여주듯, linked list는 노드들의 체인이다. 노드는 두 가지의 책임을 가지고 있다.

1. 값을 가지고 있어야한다.
2. 다음 노드에 대한 참조를 가지고 있어야 한다. 다음 노드에 대한 참조가 없다면 *null*을 통해 리스트의 끝을 나타낸다.

<p align = 'center'>
<img width = '250' src = 'https://user-images.githubusercontent.com/39554623/122393617-06c99500-cfb0-11eb-959a-18b6ea1d2fc8.png'>
</p>

## Node

> Node.kt

```kotlin
data class Node<T>(var value: T, var next: Node<T>? = null) {
    override fun toString(): String {
        return if (next != null) {
            "$value -> ${next.toString()}"
        } else {
            "$value"
        }
    }
}
```

> 테스트 (*Main.kt*)

```kotlin
fun main() {
    // 노드 생성 및 연결하기
    val node1 = Node(value = 1)
    val node2 = Node(value = 2)
    val node3 = Node(value = 3)
    
    node1.next = node2
    node2.next = node3
    
    println(node1)
}
```

```
1 -> 2 -> 3
```

이런 방법으로 리스트를 작성하는 것은 실용적이지 못하다. 이러한 문제를 해결시켜주는 일반적인 방법은 Node 객체들을 관리하는 **LinkedList**를 사용하는 것이다.

## LinkedList

> LinkedList.kt

```kotlin
class LinkedList<T> {
    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    private var size = 0
    
    fun isEmpty(): Boolean {
        return size == 0
    }

    override fun toString(): String {
        return if (isEmpty()) {
            "Empty list"
        } else {
            head.toString()
        }
    }
}
```

Linked list에는 첫 번째와 마지막 노드를 각각 참조하는 **head**와 **tail**의 개념이 있다. 

<p align = 'center'>
<img width = '250' src = 'https://user-images.githubusercontent.com/39554623/122397634-1c40be00-cfb4-11eb-8ed1-2db54feb2e5f.png'>
</p>

또한 *size* 속성<small>(property)</small>에서 linked list의 크기를 추적할 수 있다.

## 리스트에 값들을 추가하기

다음으로, Node 객체를 관리하기 위한 인터페이스를 작성한다. 먼저 값 추가를 처리한다. Linked list에 값을 추가하는 방법에는 세 가지가 있으며, 각각 고유한 성능 특징을 지니고 있다.

1. **push** : 리스트의 맨 앞에 값을 추가
2. **append** : 리스트의 끝에 값을 추가
3. **insert** : 리스트의 특정 노드 뒤에 값을 추가

이들 각각을 차례로 구현하고 성능 특징을 분석해본다.

### push 연산

리스트의 맨 앞에 값을 추가하는 것은 **push** 연산으로 알려져 있다. 또한 **head-first insertion**이라고도 한다. push 연산의 코드는 매우 간단하다.

> `push(…)` (*LinkedList.kt*)

```kotlin
fun push(value: T) {
    head = Node(value = value, next = head)
    if (tail == null) {
        tail = head
    }
    size += 1
}
```

빈 리스트에 값을 push할 경우, 새로운 노드는 리스트의 head와 tail이 된다. 리스트에 새로운 노드가 추가되었기 때문에 *size*의 값도 증가시켜준다.

> 테스트 (*Main.kt*)

```kotlin
fun main() {
    // push 예시
    val list = LinkedList<Int>()
    list.push(3)
    list.push(2)
    list.push(1)

    println(list)
}
```

```
1 -> 2 -> 3
```

이대로도 괜찮지만 더욱 멋지게 개선할 수 있다. <b>[Fluent interface](https://ko.wikipedia.org/wiki/%ED%94%8C%EB%A3%A8%EC%96%B8%ED%8A%B8_%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4)</b> 패턴을 사용하여 여러 push 호출을 연결할 수 있다. `push()`로 돌아가서 `LinkedList<T>`를 반환 타입으로 추가한다. 그런 다음 마지막 줄에 `return this`를 추가하여 방금 요소를 push한 목록을 반환한다.

> Fluent interface pattern `push(…)` (*LinkedList.kt*)

```kotlin
fun push(value: T): LinkedList<T> {
    head = Node(value = value, next = head)
    if (tail == null) {
        tail = head
    }
    size += 1
    return this
}
```

> 테스트 (*Main.kt*)

```kotlin
fun main() {
    // fluent interface push 예시
    val list = LinkedList<Int>()
    list.push(3).push(2).push(1)
    println(list)
}
```

Fluent interface 패턴을 통해 복수의 요소들을 리스트의 시작 부분에 쉽게 추가할 수 있게 되었다.

### append 연산

append 연산은 리스트의 끝에 값을 추가하며, **tail-end insertion**이라고도 한다.

> `append(…)` (*LinkedList.kt*)

```kotlin
fun append(value: T) {
    // ➀
    if (isEmpty()) {
        push(value)
        return
    }
    // ➁
    tail?.next = Node(value = value)
    // ➂
    tail = tail?.next
    size += 1
}
```

1. 이전과 마찬가지로 리스트가 비어있으면 head와 tail을 모두 새 노드로 업데이트해야한다. 빈 리스트에 추가하는 것은 기능적으로 *push*와 동일하므로 *push*를 호출하여 작업을 수행한다.
2. 다른 모든 경우에는 현재 tail 노드 뒤에 새 노드를 만든다. if 문에서 리스트가 비어있는 경우<small>(`isEmpty()`)</small>를 이미 처리 했으므로 tail은 여기서 null이 되지 않는다.
3. **tail-end insertion**이므로 새 노드도 리스트의 tail이 된다. 

> 테스트 (*Main.kt*)

```kotlin
fun main() {
    val list = LinkedList<Int>()
    list.append(1)
    list.append(2)
    list.append(3)
    println(list)
}
```

append도 물론 Fluent interface 패턴을 적용시킬 수 있다!

> > Fluent interface pattern `append(…)` (*LinkedList.kt*)

```kotlin
fun append(value: T): LinkedList<T> {
    if (isEmpty()) {
        push(value)
        return this
    }
    tail?.next = Node(value = value)
    tail = tail?.next
    size += 1
    return this
}
```

### insert 연산

**insert** 연산은 리스트의 지정된 위치에 값을 삽입하며 두 단계가 필요하다.

1. 리스트에서 지정된 노드를 찾는다.
2. 지정된 노드의 뒤에 새로운 노드를 삽입한다.

먼저 값을 삽일할 노드를 찾는 코드를 구현하자.

> `nodeAt(…)` (*LinkedList.kt*)

```kotlin
fun nodeAt(index: Int): Node<T>? {
    // ➀
    var currentNode = head
    var currentIndex = 0
    // ➁
    while (currentNode != null && currentIndex < index) {
        currentNode = currentNode.next
        currentIndex += 1
    }
    return currentNode
}
```

`nodeAt()`은 주어진 인덱스를 기반으로 리스트에서 노드 검색을 시도한다. head 노드에서만 리스트의 노드에 접근할 수 있으므로 반복 순회<small>(iterative traversals)</small>를 수행해야 한다.

1. head에 대한 새 참조를 만들고 현재 순회 수를 추적한다.
2. while 루프를 사용하여 원하는 인덱스에 도달할 때까지 리스트 참조를 다음으로 이동시킨다. 빈 리스트 또는 범위를 벗어난 인덱스는 null을 반환한다.

이제 새로운 노드를 삽입해보자.

> `insert(…)` (*LinkedList.kt*)

```kotlin
fun insert(value: T, afterNode: Node<T>): Node<T> {
    // ①
    if (tail == afterNode) {
        append(value)
        return tail!!
    }
    // ②
    val newNode = Node(value = value, next = afterNode.next)
    // ③
    afterNode.next = newNode
    size += 1
    return newNode
}
```
수행한 작업은 다음과 같다.

1. 이 메서드가 tail 노드와 함께 호출되는 경우, 기능적으로 동일한 *append* 메서드를 호출할 수 있다. 이것은 tail의 업데이트를 처리한다.
2. 그렇지 않으면, 새 노드를 만들고 *next* 속성을 리스트의 다음 노드에 연결한다.
3. 지정된 노드의 *next* 값을 다시 할당하여 방금 만든 새 노드에 연결한다.

> 테스트 (*Main.kt*)

```kotlin
fun main() {
    val list = LinkedList<Int>()
    list.push(3)
    list.push(2)
    list.push(1)
    println("Before inserting: $list")
    var middleNode = list.nodeAt(1)!!
    for (i in 1..3) {
        middleNode = list.insert(-1 * i, middleNode)
    }
    println("After inserting: $list")
}
```

```
Before inserting: 1 -> 2 -> 3
After inserting: 1 -> 2 -> -1 -> -2 -> -3 -> 3
```

### 성능 분석

||push|append|insert|nodeAt|
|:--:|:--:|:--:|:--:|:--:|
|행동|head에 삽입|tail에 삽입|노드 뒤에 삽입|주어진 인덱스의 노드를 반환|
|시간 복잡도|O(1)|O(1)|O(1)|O(i), i는 주어진 인덱스|

## 리스트에서 값들을 제거하기

노드의 제거에는 3가지 대표적인 연산들이 있다.

1. **pop** : 리스트 앞부분의 값을 제거
2. **removeLast** : 리스트 끝에 있는 값을 제거
3. **removeAfter** : 리스트의 어느 곳의 값이든 제거

### pop 연산

> `pop()` (*LinkedList.kt*)

```kotlin
fun pop(): T? {
    if (!isEmpty()) size -= 1
    val result = head?.value
    head = head?.next
    
    if (isEmpty()) tail = null
    
    return result
}
```

`pop()`은 리스트에서 제거된 값을 반환한다. 리스트가 비어있을 수 있으므로 이 값은

head를 다음 노드로 이동시켜 리스트의 첫 번째 노드를 효과적으로 제거할 수 있다. 더 이상 연결된 참조가 없기 때문에 가비지 컬렉터는 메서드가 완료되면 메모리에서 이전 노드를 제거한다. 리스트가 비어 있으면 tail도 null로 설정한다.

> 테스트 (*Main.kt*)

```kotlin
fun main() {
    val list = LinkedList<Int>()
    list.push(3)
    list.push(2)
    list.push(1)
    println("Before popping list: $list")
    val poppedValue = list.pop()
    println("After popping list: $list")
    println("Popped value: $poppedValue")
}
```

```
Before popping list: 1 -> 2 -> 3
After popping list: 2 -> 3
Popped value: 1
```

### removeLast 연산

> `removeLast()` (*LinkedList.kt*)

```kotlin

```

> 테스트 (*Main.kt*)

```kotlin

```

### remove 연산

<p align = 'center'>
<img width = '250' src = 'https://user-images.githubusercontent.com/39554623/122572442-b40fdc00-d088-11eb-9132-9ac1d7a295f0.png'>
</p>


> `removeAfter()` (*LinkedList.kt*)

```kotlin

```

> 테스트 (*Main.kt*)

```kotlin

```