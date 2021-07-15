# Queue

우리는 모두 줄을 서서 기다리는 것에 익숙하다. 좋아하는 영화의 티켓을 구매하기 위해 줄을 서는 것과 같은 실생활의 예시들은 <b>큐<small>(Queue)</small></b> 자료구조를 모방한다.

큐는 <b>FIFO<small>(first in, first out)</small></b>의 형태로, 처음 추가된 요소가 제일 먼저 제거되는 요소가 된다. 큐는 나중에 처리할 요소의 순서를 유지해야 할 때 편리하다.

## 큐의 일반적인 연산들

먼저 큐에 대한 인터페이스를 설정한다. base 패키지 내부에 Queue.kt라는 파일을 만들고 Queue 인터페이스를 정의하는 다음 코드를 추가한다.

> 큐 인터페이스 정의 (base/Queue.kt)

```kotlin
interface Queue<T : Any> {

    fun enqueue(element: T): Boolean

    fun dequeue(): T?

    val count: Int
        get

    val isEmpty: Boolean
        get() = count == 0

    fun peek(): T?
}
```

이제부터 구현하는 모든 것은 큐의 핵심 작업을 설명하는 위의 인터페이스의 규약을 따른다.

큐의 핵심 연산들은 다음과 같다.

- `enqueue` : <b>대기열<small>(queue)</small></b>의 뒤에 요소를 삽입하고 연산이 성공하면 true를 반환한다.
- `dequeue` : 대기열의 맨 앞 요소를 제거하고 반환한다.
- `isEmpty` : *count* 속성을 사용하여 대기열이 비어있는지 확인한다.
- `peek` : 대기열의 맨 앞에 있는 요소를 제거하지 않고 값만 반환한다.

큐는 앞쪽에서 제거하고 뒤쪽에서 삽입하는 것에만 관심이 있다. 그 사이에 내용이 무엇인지 알 필요가 없다.

## 큐의 이해

큐의 작동 방식을 이해하는 가장 쉬운 방법은 작동 예제를 보는 것이다. 영화 티켓을 위해 줄을 서서 기다리는 사람들을 상상해보자.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/124502496-9370b180-ddfe-11eb-8e2d-4841fcb3d2ef.png'>
</p>

이 대기열에는 현재 Ray, Brian, Sam 및 Mic이 있다. Ray는 티켓을 받으면 줄에서 나간다. `dequeue()`를 호출하면 Ray가 대기열의 앞쪽에서 제거된다.

`peek()`을 호출하면 Brian이 현재 맨앞에 있기 때문에 Brian이 반환된다.

이제 막 표를 사기 위해 비키가 줄에 합류한다. `enqueue("Vicki")`를 호출하면 Vicki가 대기열 뒤에 추가된다.

## 큐의 구현 방법들

본문에선 네 가지 방법의 큐 구현 방법들을 알아본다.

1. 배열 기반 리스트<small>(array based list)</small> 사용
2. 이중 연결 리스트<small>(doubly linked list)</small> 사용
3. ring buffer 사용
4. 두 개의 스택 사용

### 1. 리스트 기반 구현

코틀린 표준 라이브러리에는 더 높은 수준의 추상화를 구축하는데 사용할 수 있는 고도로 최적화된 자료구조의 핵심 세트가 함께 제공된다. 이들 중 하나가 연속적이고<small>(contiguous)</small> 정렬된 요소들의 리스트를 저장하는 자료구조인 **ArrayList**이다. ArrayList를 이용해서 큐를 구현해보자.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124503376-67563000-de00-11eb-9be3-0bb2fcff0246.png'>
</p>

list 패키지 내부에 ArrayListQueue.kt 파일을 생성하고 아래의 코드를 추가한다.

```kotlin
class ArrayListQueue<T : Any> : Queue<T> {
    private val list = arrayListOf<T>()
}
```

Queue 인터페이스를 구현하는 제네릭 ArrayListQueue 클래스를 정의했다. 인터페이스 구현은 저장하는 요소에 대해 동일한 제네릭 타입인 `T`를 사용한다.

다음으로 ArrayListQueue 구현을 완료하여 Queue 인터페이스의 규약을 만족시키자.

#### ArrayList 활용

ArrayListQueue 클래스에 다음 코드를 추가한다.

```kotlin
class ArrayListQueue<T : Any> : Queue<T> {
    ...

    override val count: Int
        get() = list.size

    override fun peek(): T? = list.getOrNull(0)
}
```

ArrayList의 기능들을 사용하면 다음을 간단히 구현할 수 있다.

1. 리스트의 동일한 속성을 사용하여 큐의 크기를 가져온다.
2. 큐의 맨 앞에 요소가 존재한다면 값을 반환한다.

이 연산들은 모두 *O(1)*이다.

#### Enqueue

큐의 뒤에 요소를 추가하는 것은 간단하다. ArrayList의 끝에 요소를 추가하기만 하면 된다.

```kotlin
override fun enqueue(element: T): Boolean {
    list.add(element)
    return true
}
```

리스트의 크기에 관계없이 요소를 큐에 추가하는 것은 *O(1)* 연산이다. 리스트 뒤에 빈 공간이 있기 때문이다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124504495-9077c000-de02-11eb-8719-55521aae5db3.png'>
</p>

위의 예시에서 Mic를 추가하면 리스트에 두 개의 빈 공간이 있다.

여러 요소를 추가한 후에는 ArrayList 내부 배열이 결국 가득 차게 된다. 할당된 공간보다 더 많이 사용하려면 추가 공간을 만들기 위해 배열의 크기를 조정해야만 한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124504497-92418380-de02-11eb-8ad1-392ae7528963.png'>
</p>

크기 조정<small>(resizing)</small>은 *O(n)* 연산이다. 크기를 조정하려면 리스트에서 새 메모리를 할당하고 기존의 모든 데이터를 새 리스트에 복사해야 한다. 매번 크기를 두 배로 늘리는 덕분에 자주는 발생하지 않으며, 시간 복잡도는 여전히 <a href = "https://stackoverflow.com/a/249695"><i>Amortized O(1)</i></a>으로 동작한다.

#### Dequeue

전면에서 항목<small>(item)</small>을 제거하려면 약간 더 많은 작업이 필요하다.

```kotlin
override fun dequeue(): T? = 
    if (isEmpty) null else list.removeAt(0)
```

큐가 비어 있으면 `dequeue()`는 단순히 null을 반환한다. 그렇지 않다면 리스트의 맨 앞에서 요소를 제거하고 반환한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124504501-94a3dd80-de02-11eb-9b82-9818df31bf7d.png'>
</p>

큐의 전면에서 요소를 제거하는 연산은 *O(n)* 시간 복잡도를 가진다. 대기열에서 요소를 제거하려면 리스트 시작 부분에서 요소를 제거해야 한다. 리스트의 나머지 모든 요소를 메모리에서 이동시켜야하므로 항상 선형 시간<small>(Linear time, O(n))</small>의 연산이다.

#### 테스트

디버깅을 위해 구현한 큐가 `toString()`을 재정의하도록 한다.

```kotlin
override fun toString(): String = list.toString()
```

이제 구현한 큐를 직접 사용해보도록 하자.

```kotlin
fun main() {
    val queue = ArrayListQueue<String>().apply {
        enqueue("Ray")
        enqueue("Brian")
        enqueue("Eric")
    }
    println(queue)
    queue.dequeue()
    println(queue)
    println("Next up: ${queue.peek()}")
}
```

```
[Ray, Brian, Eric]
[Brian, Eric]
Next up: Brian
```

이 코드는 Ray, Brian 및 Eric을 큐에 넣는다<small>(`enqueue`)</small>. 그런 다음 Ray를 제거하고<small>(`dequeue()`)</small> Brian을 들여다 보지만 제거하지는 않는다<small>(`peek()`)</small>. 

#### 장점과 단점

다음은 ArrayList 기반 큐 구현의 알고리즘 및 복잡도에 대해 요약하고 있다. 대부분의 작업은 선형 시간이 걸리는 `dequeue()`를 제외하고는 상수 시간을 가진다. 공간 복잡도는 *O(n)* 이다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124506381-7344f080-de06-11eb-963b-0c16e0146195.png'>
</p>

Kotlin ArrayList를 활용하여 리스트 기반 큐를 간단하게 구현하였다. *O(1)* 추가 연산 덕분에 큐에 넣는 것이 매우 빠르다.

하지만 이 구현에는 몇 가지 단점들이 있다. 항목을 제거하면 모든 요소가 하나씩 이동하므로 큐에서 항목을 제거하는 것은 비효율적일 수 있다. 이것은 매우 큰 큐에서 차이를 만든다. 리스트가 가득 차면 크기를 조정해야 하며 사용하지 않는 공간이 있을 수 있다. 이것은 시간이 지남에 따라 메모리 사용량을 증가시킬 수 있다. 이러한 단점을 어떻게 해결할 수 있을까? 연결 리스트 기반 구현을 살펴보고 ArrayListQueue와 비교해보도록 하자.

### 2. 이중 연결 리스트<small>(doubly linked list)</small> 기반 구현

linkedlist 패키지 내부에 LinkedListQueue.kt 파일을 생성한다.

```kotlin
class LinkedListQueue<T : Any> : Queue<T> {
    private val list = DoublyLinkedList<T>()

    private var size = 0

    override val count: Int
        get() = size
}
```

위 구현은 **ArrayListQueue**와 유사하지만 **ArrayList** 대신 **DoublyLinkedList**를 생성한다.

DoublyLinkedList가 제공하지 않는 *count* 속성 아래로 Queue 인터페이스의 구현을 시작한다.

#### Enqueue

큐의 뒤에 요소를 추가하기 위해 아래의 코드를 작성한다.

```kotlin
override fun enqueue(element: T): Boolean {
    list.append(element)
    size += 1
    return true
}
```

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124506865-80161400-de07-11eb-841b-a5acb773c7a8.png'>
</p>

이중 연결 리스트는 내부에서 새 노드에 대한 꼬리 노드의 이전<small>(prev)</small> 및 다음<small>(next)</small> 참조를 업데이트하고 크기를 늘린다. 이 과정은 *O(1)* 연산이다.

#### Dequeue

큐에서 요소를 제거하기 위해서 아래의 코드를 추가한다.

> 간단한 구현

```kotlin
override fun dequeue(): T? = list.pop()
```

원서는 DoublyLinkedList에 대한 코드가 제공되지 않은 상태에서 위의 코드를 예시로 들어놓았는데, 직접 구현한 LinkedList를 기반으로 Queue를 구현한다면 `dequeue()`를 리스트에서 첫 번째 노드를 제거하는 메서드를 호출하는 것으로 간단히 구현할 수 있다.

> 원서의 코드

```kotlin
override fun dequeue(): T? {
  val firstNode = list.first ?: return null
  size--
  return list.remove(firstNode)
}
```

원서의 코드는 큐의 첫 번째 요소가 존재하는지 확인하고 존재하지 않는다면 null을 반환한다. 큐에 첫 번째 요소가 존재한다면 맨 앞에 있는 요소를 제거하고 이를 반환한다. 크기도 감소한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124506868-81dfd780-de07-11eb-875b-2bb97909b55f.png'>
</p>

리스트 맨 앞을 제거하는 것 또한 *O(1)* 연산이다. ArrayList 구현과 비교할 때 요소를 하나씩 이동할 필요가 없는 대신, 위의 이미지처럼 연결 리스트의 처음 두 노드 사이의 전<small>(prev)</small> 및 다음<small>(next)</small> 포인터를 업데이트하기만 하면 된다.

#### Peek

ArrayList 기반 구현과 유사하게, DoublyLinkedList의 속성을 이용하여 `peek()`을 간단히 구현할 수 있다.

```kotlin
override fun peek(): T? = list.first?.value
```

#### 테스트

디버깅을 위해 아래의 코드를 클래스에 추가하고 테스트 해보자.

```kotlin
override fun toString(): String = list.toString()
```

```kotlin
fun main() {
    val queue = LinkedListQueue<String>().apply {
    enqueue("Ray")
    enqueue("Brian")
    enqueue("Eric")
    }
    println(queue)
    queue.dequeue()
    println(queue)
    println("Next up: ${queue.peek()}")
}
```

이 테스트 코드는 ArrayListQueue 구현과 동일한 결과를 생성한다.

#### 장점과 단점

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124506871-84423180-de07-11eb-8b5b-ae09b51587b4.png'>
</p>

ArrayListQueue의 주요 문제점 중 하나는 항목을 대기열에서 빼는데 <i>O(n)</i>이 걸리는 것이다. 연결 리스트 구현을 통해 노드의 이전 및 다음 포인터를 업데이트하는 것만으로 시간 복잡도를 <i>O(1)</i>으로 축소시켰다.

LinkedListQueue의 주요 단점은 위의 표에서 분명하게 나타나지 않는다. <i>O(1)</i> 성능에도 불구하고 높은 오버헤드가 존재하는데, 각 요소는 이전과 다음의 참조를 위한 추가 공간이 있어야 한다<small>(공간 복잡도 증가)</small>. 또한 새 요소를 만들 때마다 상대적으로 비용이 많이 드는 동적 할당이 필요하다. 이에 비해, ArrayListQueue는 더 빠른 대량 할당을 수행한다.

할당에 대한 오버헤드를 제거하고 <i>O(1)</i>의 dequeue를 유지할 수 있을까? 큐가 고정된 크기 이상으로 커지는 것에 대해 걱정할 필요가 없는 경우 **링 버퍼**와 같은 다른 접근 방식을 사용할 수 있다. 예를 들어, 5명의 플레이어가 참여하는 모노폴리 게임에 링 버퍼를 기반으로 한 큐를 사용하여 다음에 올 차례를 추적할 수 있다. 다음으로 링 버퍼 구현을 살펴보자.

### 3. 링 버퍼<small>(Ring Buffer)</small> 기반 구현

원형 버퍼<small>(circular buffer)</small>라고도 불리는 링 버퍼<small>(ring buffer)</small>는 고정 크기 배열이다. 이 자료구조는 마지막에 제거할 항목이 없을 때 시작 부분으로 래핑된다.

링 버퍼를 사용하여 큐를 구현하는 방법에 대한 간단한 예를 아래의 이미지들로 살펴보자.

> 링 버퍼 생성

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/124657417-72cb5900-dedd-11eb-83e7-d1347a5734db.png'>
</p>

먼저 고정 크기가 4인 링 버퍼를 만든다. 링 버퍼에는 두 개의 포인터가 존재한다.

1. 읽기<small>(read)</small> 포인터 : 큐의 앞쪽을 추적
2. 쓰기<small>(write)</small> 포인터 : 사용 가능한 다음 칸을 추적하여 이미 읽어 들인 기존 요소를 재정의할 수 있다.

> 대기열에 항목 추가

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/124657442-7828a380-dedd-11eb-9b9e-81cb4ceefc91.png'>
</p>

대기열에 항목을 추가할 때마다 쓰기 포인터가 1씩 증가한다.

> 항목 2개를 더 추가

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/124657453-7bbc2a80-dedd-11eb-9434-92854fd7844a.png'>
</p>

쓰기 포인터가 두 자리 더 이동하여 읽기 포인터보다 3칸 앞서 있는 것을 확인 할 수 있다. 이는 대기열이 비어 있지 않다는 것을 의미한다.

> 대기열에서 두 개의 항목을 빼기

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/124657465-7fe84800-dedd-11eb-80da-d4634f1a86d8.png'>
</p>

대기열에서 항목을 빼는 것을 링 버퍼를 읽는 것과 동일하다. 읽기 포인터가 어떻게 두 번 이동했는지 주목하자.

> 대기열을 채우기 위해 항목을 하나 더 추가

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/124657480-824aa200-dedd-11eb-9928-35956b11d604.png'>
</p>

쓰기 포인터가 끝에 도달했으므로 시작 인덱스로 다시 랩핑된다.

> 마지막으로 남은 두 개의 항목을 대기열에서 빼기

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/124657496-8676bf80-dedd-11eb-8b69-b5961c7d5dc8.png'>
</p>

남은 두 항목도 대기열에서 빼면서 읽기 포인터도 시작 부분으로 돌아온다.

위 이미지를 통해 읽기와 쓰기 포인터가 동일한 인덱스에 있을 경우 대기열, 즉 큐가 비어 있다는 것을 알 수 있다.

링 버퍼를 통한 큐의 구현은 개념만 확인하고 구현은 생략하였다.

#### 장점과 단점

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124657508-8aa2dd00-dedd-11eb-86f7-208f48e10b6b.png'>
</p>

링 버퍼 기반 큐는 연결 리스트 기반 구현과 `enqueue`와 `dequeue`의 시간 복잡도가 동일하다. 유일한 차이점은 공간 복잡도인데, 링 버퍼의 크기는 고정되어 있으므로 큐에 넣는 것 자체가 실패할 수 있는 단점이 존재한다.

지금까지 배열, 연결 리스트, 링 버퍼 기반까지 총 세 가지의 구현 방법을 보았는데 마지막으로 두 개의 스택을 사용하여 구현된 큐를 알아 볼 것이다.

이중 스택 기반 구현 큐는 메모리 상의 공간적 지역성이 연결 리스트보다 훨씬 우수하고, 링 버퍼와 같이 고정된 크기가 필요하지 않다는 장점이 있다.

### 4. 이중 스택<small>(Double-Stack)</small> 기반 구현

doublestack 패키지 내부에 StackQueue.kt를 추가한다.

```kotlin
class StackQueue<T : Any> : Queue<T> {
    private val leftStack = StackImpl<T>()
    private val rightStack = StackImpl<T>()
}
```

두 개의 스택을 사용하는 아이디어는 요소를 큐에 넣을 때마다 **오른쪽** 스택으로 이동하고, 요소를 큐에서 뺄 때는 FIFO 순서를 사용하여 요소를 검색할 수 있도록 오른쪽 스택을 반대로 뒤집어서 **왼쪽** 스택에 넣는다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/125627123-d8f7ca3e-d598-4fda-b7e8-bf87d948cef6.png'>
</p>

#### 스택 활용하기

그러면 아래의 코드를 추가하여 큐의 공통 기능들을 구현해본다.

```kotlin
override val count: Int
    get() = leftStack.count + rightStack.count 

override val isEmpty: Boolean
    get() = leftStack.isEmpty && rightStack.isEmpty
```

큐가 비어 있는지 확인하려면 왼쪽과 오른쪽의 스택이 모두 비어 있는지 확인하면 된다. 큐에 있는 요소의 개수는 두 스택에 있는 요소 개수의 합이다.

이중 스택으로 구현한 큐는 위에서 설명했듯이 오른쪽 스택에서 왼쪽 스택으로 요소를 전달해야 할 때가 있다. 이는 왼쪽 스택이 비어 있을 때마다 발생한다. 

다음의 헬퍼 메서드를 추가하자.

> 오른쪽 스택에서 왼쪽 스택으로 요소 이동

```kotlin
private fun transferElements() {
    var nextElement = rightStack.pop()
    while (nextElement != null) {
        leftStack.push(nextElement)
        nextElement = rightStack.pop()
    }
}
```

위 코드를 통해 오른쪽 스택으로부터 요소를 꺼내어 왼쪽 스택에 넣을 수 있다. 스택은 LIFO 방식으로 작동하기 때문에 추가적인 작업 없이 역순으로 요소들을 가져올 수 있다.

> `peek()`

```kotlin
override fun peek(): T? {
    if (leftStack.isEmpty) {
        transferElements()
    }
    return leftStack.peek()
}
```

`peek()`은 최상위 요소를 보는 메서드이다. 만약 왼쪽 스택이 비어 있지 않다면 이 스택의 맨 위에 있는 요소가 큐의 맨 앞에 있다.

왼쪽 스택이 비어 있으면 `transferElements()`를 사용한다. 그렇게 하면 `leftStack.peek()`은 항상 올바른 요소 또는 *null*을 반환한다. `isEmpty()`는 여전히 <i>O(1)</i> 작업인 반면 `peek()`은 <i>O(n)</i>이다.

이러한 `peek()`의 구현이 비싼 비용을 요구하는 것처럼 보이지만, 큐의 각 요소는 오른쪽 스택에서 왼쪽 스택으로 한 번만 이동하면 되기 때문에 amortized <i>O(1)</i>이다. 왼쪽 스택이 비어 있을 때 `peek()` 호출은 오른쪽 요소들을 모두 왼쪽 스택으로 이동시키므로 <i>O(n)</i>이지만, 그 외 추가적인 호출에 대해선 <i>O(1)</i>이 된다.

#### Enqueue

```kotlin
override fun enqueue(element: T): Boolean {
    rightStack.push(element)
    return true
}
```

요소를 큐에 추가할 때는 오른쪽 스택이 사용된다. 스택에 요소를 넣는 `push()`는 <i>O(1)</i>이다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125709812-534b1733-5132-4770-9b10-23b3b17392b7.png'>
</p>

#### Dequeue

```kotlin
override fun dequeue(): T? {
    if (leftStack.isEmpty) {
        transferElements()
    }
    return leftStack.pop()
}
```
동작 원리는 다음과 같다.

1. 왼쪽 스택이 비어 있는지 확인한다.
2. 왼쪽 스택이 비어 있으면 오른쪽 스택의 요소를 역순으로 이동시킨다.
3. 왼쪽 스택에서 맨 위의 요소를 제거한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125709818-4e175602-0dbc-45ef-b299-999bc03ec2d2.png'>
</p>

왼쪽 스택이 비어 있을 때에만 오른쪽 스택의 요소를 이동시키므로 `dequeue()`는 `peek()`처럼 amortized <i>O(1)</i> 연산이다.

#### 테스트

```kotlin
override fun toString(): String {
  return "Left stack: \n$leftStack \nRight stack: \n$rightStack"
}
```

```kotlin
fun main() {
    val queue = StackQueue<String>().apply {
        enqueue("Ray")
        enqueue("Brian")
        enqueue("Eric")
    }
    println(queue)
    queue.dequeue()
    println(queue)
    println("Next up: ${queue.peek()}")
}
```

```
Left stack: 
----top----
-----------
 
Right stack: 
----top----
Eric
Brian
Ray
-----------

Left stack: 
----top----
Brian
Eric
-----------
 
Right stack: 
----top----
-----------

Next up: Brian
```

#### 장점과 단점

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/125711056-d0be53af-a4b3-4029-bacb-3a914e9d8115.png'>
</p>

리스트 기반 구현과 비교했을 때, 두 개의 스택을 활용하면 `dequeue()`의 구현을 amortized <i>O(1)</i> 연산으로 변환할 수 있다. 또한 이중 스택 기반 구현은 완전히 동적이고 링 버퍼 기반 구현처럼 고정된 크기로 제한되지도 않는다. 마지막으로 공간적 지역성<small>(spatial locality, 메모리 상 인접 데이터의 재이용률이 높음)</small> 측면에서 연결 리스트 기반 구현을 능가하는데, 이는 리스트이 요소가 메모리 블록에서 서로 옆에 있기 때문이다. 따라서 많은 수의 요소가 한 번의 접근<small>(access)</small>으로 캐시에 로드된다.

> 연속된 배열에 있는 요소들

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125709832-3a3d4f43-4b43-4c06-8319-85f9d593084e.png'>
</p>

> 메모리 전체에 흩어져 있는 연결 리스트의 요소들

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125709837-9a7d64d5-f7cf-45ef-825e-33b7f79b5e18.png'>
</p>

연결 리스트에서 요소는 메모리 블록에 연속적으로 존재하지 않는다. 이로 인해 더 많은 캐시 미스가 발생하여 접근 시간이 늘어난다.