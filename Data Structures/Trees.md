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

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125865349-410b60ee-2e18-41ea-b07c-05d8f3939925.png'>
</p>

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125865353-3a4a0fec-6f60-42d7-8165-cdee3b7d6ddb.png'>
</p>

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125865366-20b030f5-3cee-472f-b095-2d50ba7e7151.png'>
</p>

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/125865372-a74a4353-1568-422f-ac6c-9abf96f4d858.png'>
</p>

