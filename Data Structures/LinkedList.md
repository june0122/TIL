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

> Main.kt

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

> LinkedList.kt

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

> Main.kt

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

> LinkedList.kt

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

> Main.kt

```kotlin
fun main() {
    // fluent interface push 예시
    val list = LinkedList<Int>()
    list.push(3).push(2).push(1)
    println(list)
}
```

Fluent interface 패턴을 통해 복수의 요소들을 리스트의 시작 부분에 쉽게 추가할 수 있게 되었다.

## append 연산

