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

### 리스트 기반 구현

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

#### 디버그 및 테스트

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

### 이중 연결 리스트<small>(doubly linked list)</small> 구현

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124506865-80161400-de07-11eb-841b-a5acb773c7a8.png'>
</p>

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124506868-81dfd780-de07-11eb-875b-2bb97909b55f.png'>
</p>

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/124506871-84423180-de07-11eb-8b5b-ae09b51587b4.png'>
</p>
