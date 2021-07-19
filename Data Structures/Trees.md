# Trees

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125863597-4066759b-d019-421a-bcd4-f0383febe0d0.png'>
</p>

<b>트리<small>(tree)</small></b>는 매우 중요한 자료구조로 다음과 같이 소프트웨어 개발에서 반복되는 많은 문제를 해결하는 데 사용한다.

- 계층적 관계를 표현
- 정렬된 데이터 관리
- 빠른 조회 작업을 용이하게 함

## 트리와 관련된 용어들<small>(Terminology)</small>

### 노드<small>(Node)</small>

연결 리스트와 마찬가지로 트리도 노드로 구성된다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125863852-f54f02f7-560f-4037-8e11-db6f9ba137b7.png'>
</p>

각 노드는 일부 데이터를 캡슐화하고 자식을 추적한다.

### 부모와 자식<small>(Parent and child)</small>

트리는 거꾸로 뒤집힌 실제 나무처럼 위에서 시작하여 아래쪽으로 가지를 뻗어 나간다.

첫 번째 노드를 제외한 모든 노드는 **부모** 노드라고 하는 위의 단일 노드에 연결된다. 바로 아래에 있고 부모 노드에 연결된 노드를 **자식** 노드라 한다. 트리에서 모든 자식은 정확히 한 명의 부모를 가진다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125863863-bf9c778e-92b9-4b93-b44a-edf19b0a64af.png'>
</p>

### 뿌리<small>(Root)</small>

트리의 최상위 노드를 트리의 뿌리<small>(루트, root)</small>라고 한다. 부모가 없는 유일한 노드이기도 하다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125863874-acebca4c-f298-4ab9-b24e-10376320c968.png'>
</p>

### 잎<small>(Leaf)</small>

자식이 없는 노드를 리프라고 한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125863886-cfebea5d-2c3d-497c-9848-a1c709baa36c.png'>
</p>

## 구현

트리는 노드로 구성되어 있으므로 제일 처음해야 할 작업은 TreeNode 클래스를 생성하는 것이다.

> TreeNode.kt

```kotlin
class TreeNode<T>(val value: T) {
    private val children: MutableList<TreeNode<T>> = mutableListOf()
}
```

각 노드는 값을 담당하고 MutableList를 사용하여 모든 자식에 대한 참조를 저장한다.

다음으로 TreeNode 내부에 아래의 메서드를 추가하자

```kotlin
fun add(child: TreeNode<T>) = children.add(child)
```

이 메서드는 노드에 자식 노드를 추가한다.

```kotlin
fun main() {
    val hot = TreeNode("Hot")
    val cold = TreeNode("Cold")
    val beverages = TreeNode("Beverages").run {
        add(hot)
        add(cold)
    }
}
```

계층 구조는 트리 구조의 자연스러운 형태 중 하나이다. 위의 코드는 아래와 같은 구조를 가지고 있다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125865331-9c19532f-d417-4f64-88f0-91cd4314520f.png'>
</p>

## Traversal algorithms

배열이나 리스트와 같은 **선형 컬렉션**을 반복<small>(iterating)</small>하는 것은 간단하다. 선형 컬렉션에는 명확한 시작과 끝이 있다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125865349-410b60ee-2e18-41ea-b07c-05d8f3939925.png'>
</p>

트리를 반복하는 것은 조금 더 복잡하다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125865353-3a4a0fec-6f60-42d7-8165-cdee3b7d6ddb.png'>
</p>

왼쪽에 있는 노드가 우선 순위를 가져야 할까? 노드의 깊이는 우선 순위와 어떤 관련이 있을까? 순회 전략은 해결하려는 문제에 따라 다르게 가져가야 한다.

서로 다른 트리와 문제에 대해 여러 전략이 존재한다. 이 모든 방법은 노드를 **방문**하고 노드 내부의 정보를 사용할 수 있게 해준다.

> TreeNode 클래스의 외부에 정의 추가

```kotlin
typealias Visitor<T> = (TreeNode<T>) -> Unit

class TreeNode<T>(val value: T) {
    ...
}
```

### Depth-first traversal<small>(Depth-first search, DFS, 깊이 우선 탐색)</small>

깊이 우선 탐색은 루트 노드에서 시작하여 리프에 도달한 다음 백트래킹하기 전에 각 분기를 따라 가능한 멀리 트리를 탐색한다.

TreeNode 내부에 다음을 추가한다.

```kotlin
fun forEachDepthFirst(visit: Visitor<T>) {
    visit(this)
    children.forEach { 
        it.forEachDepthFirst(visit)
    }
}
```

이 간단한 코드는 재귀를 사용하여 다음 노드를 처리한다.

재귀적인 구현을 사용하지 않으려면 스택을 사용할 수 있으나 재귀를 이용한 방법이 더 간단하다.

> 재귀 깊이 우선 탐색 테스트

```kotlin
fun makeBeverageTree(): TreeNode<String> {
    val tree = TreeNode("Beverages")
    val hot = TreeNode("hot")
    val cold = TreeNode("cold")
    val tea = TreeNode("tea")
    val coffee = TreeNode("coffee")
    val chocolate = TreeNode("cocoa")
    val blackTea = TreeNode("black")
    val greenTea = TreeNode("green")
    val chaiTea = TreeNode("chai")
    val soda = TreeNode("soda")
    val milk = TreeNode("milk")
    val gingerAle = TreeNode("ginger ale")
    val bitterLemon = TreeNode("bitter lemon")
    tree.add(hot)
    tree.add(cold)
    hot.add(tea)
    hot.add(coffee)
    hot.add(chocolate)
    cold.add(soda)
    cold.add(milk)
    tea.add(blackTea)
    tea.add(greenTea)
    tea.add(chaiTea)
    soda.add(gingerAle)
    soda.add(bitterLemon)
    return tree
}
```
`makeBeverageTree()`는 아래와 같은 트리를 생성한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125865366-20b030f5-3cee-472f-b095-2d50ba7e7151.png'>
</p>

트리를 생성한 다음 `main()`에서 다음과 같은 코드를 실행한다.

```kotlin
fun main() {
    val tree = makeBeverageTree()
    tree.forEachDepthFirst { println(it.value) }
}
```

이 코드의 출력 결과는 깊이 우선 탐색이 각 노드를 방문하는 순서를 보여준다.

```
Beverages
hot
tea
black
green
chai
coffee
cocoa
cold
soda
ginger ale
bitter lemon
milk
```

### Level-order traversal

Level-order traversal은 노드의 깊이를 기반으로 트리의 각 노드를 방문하는 방법이다. 루트에서 시작하여 하위 레벨로 이동하기 전에 같은 레벨의 모든 노드를 방문한다.

TreeNode 내부에 다음을 추가한다.

> [직접 구현한 Queue](https://github.com/june0122/DataStructureKotlin/tree/master/src/queue)를 사용할 시

```kotlin
fun forEachLevelOrder(visit: Visitor<T>) {
    visit(this)
    val queue = StackQueue<TreeNode<T>>()
    children.forEach { queue.enqueue(it) }
    
    var node = queue.dequeue()
    while (node != null) {
        visit(node)
        node.children.forEach { queue.enqueue(it) }
        node = queue.dequeue()
    }
}
```

> java.util 패키지 내부의 Queue 사용

```kotlin
fun forEachLevelOrder(visit: Visitor<T>) {
    visit(this)
    val queue: Queue<TreeNode<T>> = LinkedList()
    children.forEach { queue.offer(it) }
    var node = queue.poll()
    while (node != null) {
        visit(node)
        node.children.forEach { queue.offer(it) }
        node = queue.poll()
    }
}
```

`forEachLevelOrder()`는 각 노드를 레벨 순으로 방문한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125865372-a74a4353-1568-422f-ac6c-9abf96f4d858.png'>
</p>

노드가 올바른 레벨 순서로 방문되도록 큐를 사용하는 방법에 유의한다. 현재 노드를 방문하여 모든 자식을 큐에 넣는다. 그런 다음 비어 있을 때까지 큐를 사용하기 시작한다. 노드를 방문할 때마다 노드의 모든 자식들도 큐에 넣는다. 이렇게 하면 같은 레벨의 모든 노드를 차례로 방문할 수 있다. 

```kotlin
fun main() {
  val tree = makeBeverageTree()
  tree.forEachLevelOrder { println(it.value) }
}
```

```
beverages
hot
cold
tea
coffee
cocoa
soda
milk
black
green
chai
ginger ale
bitter lemon
```

### 검색

노드를 반복하는 메서드를 이미 구현했으므로 검색 알고리즘을 구현하는데 오래 걸리지 않는다.

TreeNode 내부에 아래의 코드를 추가한다.

```kotlin
fun search(value: T): TreeNode<T>? {
    var result: TreeNode<T>? = null
    
    forEachLevelOrder { 
        if (it.value == value) {
            result = it
        }
    }
    
    return result
}
```

코드를 테스트하기 위해 `main()`에 아래의 코드를 추가한다.

```kotlin
fun main() {
    val tree = makeBeverageTree()
    tree.search("ginger ale")?.let {
        println("Found node: ${it.value}")
    }
    
    tree.search("WKD Blue")?.let {
        println(it.value)
    } ?: println("Couldn't find WKD Blue")
}
```

`main()`을 실행하면 다음과 같은 출력 결과를 볼 수 있다.

```
Found node: ginger ale
Couldn't find WKD Blue
```

위에서 level-order traversal 알고리즘을 사용했는데, 모든 노드를 방문하기 때문에 일치하는 항목이 여러 개일 경우 마지막으로 일치하는 항목이 채택된다. 이것은 사용하는 순회 방법에 따라 다른 객체를 얻을 수 있음을 의미한다.

## 챌린지

아래 트리의 값을 레벨에 따라 순서대로 출력하시오. 같은 레벨에 속하는 노드는 같은 줄에 출력해야 합니다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/126172924-394314ff-9136-4639-bd29-65e1615d6edb.png'>
</p>

```kotlin
15
1 17 20
1 5 0 2 5 7
```

> 예제 트리 생성 코드

```kotlin
fun makeSampleTree(): TreeNode<Int> {
    val tree = TreeNode(15)
    val one = TreeNode(1)
    val seventeen = TreeNode(17)
    val twenty = TreeNode(20)
    tree.add(one)
    tree.add(seventeen)
    tree.add(twenty)
    one.add(TreeNode(1))
    one.add(TreeNode(5))
    one.add(TreeNode(0))
    seventeen.add(TreeNode(2))
    twenty.add(TreeNode(5))
    twenty.add(TreeNode(7))

    return tree
}
```

> 레벨 별 노드 출력 코드 (java.util 패키지 내부의 Queue 사용)

```kotlin
fun printEachLevel() {
    // 1
    val queue: Queue<TreeNode<T>> = LinkedList()
    var nodesLeftInCurrentLevel = 0
    queue.offer(this)
    // 2
    while (queue.isNotEmpty()) {
        // 3
        nodesLeftInCurrentLevel = queue.size
        // 4
        while (nodesLeftInCurrentLevel > 0) {
            val node = queue.poll()
            node?.let {
                print("${node.value} ")
                node.children.forEach { queue.offer(it) }
                nodesLeftInCurrentLevel--
            } ?: break
        }
        // 5
        println()
    }
}
```

> 레벨 별 노드 출력 코드 ([직접 구현한 Queue](https://github.com/june0122/DataStructureKotlin/tree/master/src/queue)를 사용)

```kotlin
fun printEachLevel() {
    // 1
    val queue = StackQueue<TreeNode<T>>()
    var nodesLeftInCurrentLevel = 0
    queue.enqueue(this)
    // 2
    while (queue.isEmpty.not()) {
        // 3
        nodesLeftInCurrentLevel = queue.count
        // 4
        while (nodesLeftInCurrentLevel > 0) {
            val node = queue.dequeue()
            node?.let {
                print("${node.value} ")
                node.children.forEach { queue.enqueue(it) }
                nodesLeftInCurrentLevel--
            } ?: break
        }
        // 5
        println()
    }
}
```

1. 레벨 순서 순회를 용이하게 하기 위해 큐를 초기화하는 것으로 시작한다. 또한 새 줄을 출력하기 전에 작업해야하는 노드의 수를 추적하기 위해 *nodesLeftInCurrentLevel*을 만든다.
2. 레벨 순서 순회는 큐가 빌 때까지 계속된다.
3. 첫 번째 while 루프 내에서 *nodesLeftInCurrentLevel*을 큐의 현재 요소로 설정하여 시작한다.
4. 다른 while 루프를 사용하여 *nodesLeftInCurrentLevel*의 수만큼 큐에서 요소를 빼낸다. 큐에서 빼는 모든 요소는 다음 줄로 넘어가지 않고 출력되며, 노드의 모든 자식을 큐에 넣는다. 
5. 이 시점에서 `println()`을 사용하여 새 줄을 생성한다. 다음 반복에서 *nodesLeftInCurrentLevel*은 이전 반복의 자식 수를 나타내는 *queue.count*로 업데이트된다.

## 요약

- 트리는 연결 리스트와 몇 가지 유사점을 공유한다. 그러나 트리의 노드는 무한히 많은 노드에 연결할 수 있는 반면 연결 리스트의 노드는 하나의 다른 노드에만 연결할 수 있다.
- 깊이 우선 및 레벨 순서 순회는 일반적인 유형의 트리에만 국한되지 않는다. 트리 구조에 따라 구현이 약간 다르지만 다른 유형의 트리에서도 작동할 수 있다.