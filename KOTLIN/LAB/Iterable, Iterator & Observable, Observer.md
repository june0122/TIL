# Iterator

- 자바에서는 데이터의 연속적인 자료구조를 표현할 때 List<sup> Collection</sup>을 사용한다.
  
  - 그리고 List 내부의 연속적인 데이터를 꺼내오기 위해 주로 for-each문을 사용한다.

  - 이것이 가능한 것은 **List가 `Iterable` 인터페이스를 구현하고 있기 때문**이다.

<br>

```
Implementing this interface allows an object to be the target of the "for-each loop" statement.
```

- IntelliJ에서 `Iterable` 문서를 보면 Iterable 인터페이스를 구현할 경우 객체가 for-each 문을 사용할 수 있게 허용해준다는 설명이 있다.

```java
public interface Iterable<T> {
    Iterator<T> iterator();
    ...
}
```

- 이는 `Iterable` 인터페이스 내부에 `Iterator()` 메소드가 정의되어 있기 때문이다.

  - List, Queue, Set → Collection → Iterable 순서로 implements 하는데, 마찬가지로 Collection, List, Set 에서도 **`Iterator()` 메소드가 정의만 되어있다.**

  - 즉, **실제 iterator() 메소드의 구현은 하위 클래스**인 ArrayList, LinkedList 등에서 이루어진다.
 
<br>
<p align = 'center'>
<img width = 600, src = 'https://user-images.githubusercontent.com/39554623/64793037-572eea00-d5b5-11e9-9a17-7b822363cd08.png'>
</p>
<br>

> ArrayList

- 아래 코드를 보면 ArrayList에서 `Iterator()` 메소드를 구현하고 있다.

  - 내부에서는 `private class Itr`로 Iterator 인터페이스를 구현하고 있다.<sup> hasNext(), next(), 기타 필요한 메소드 구현하여 return</sup>

```java
public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * <p>The returned iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * An optimized version of AbstractList.Itr
     */
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        Itr() {}

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int size = ArrayList.this.size;
            int i = cursor;
            if (i >= size) {
                return;
            }
            final Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            while (i != size && modCount == expectedModCount) {
                consumer.accept((E) elementData[i++]);
            }
            // update once at end of iteration to reduce heap write traffic
            cursor = i;
            lastRet = i - 1;
            checkForComodification();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
```
<br>

> Kotlin ListIterator 예제

```kotlin
class SListIterator(var current: SList.Node?): Iterator<Int?> {
    override fun hasNext(): Boolean {
        return current != null
    }

    override fun next(): Int? {
        val n = current?.n
        current = current?.next
        return n
    }
}

class SList: Iterable<Int?> {
    override fun iterator(): Iterator<Int?> {
        return SListIterator(head)
    }

    data class Node(val n: Int, val next: Node?)

    var head: Node? = null

    fun addFront(n: Int) {
        head = Node(n, head)
    }
}

fun main() {
    val list = SList()

    list.addFront(10)
    list.addFront(20)
    list.addFront(30)

    // iterator 사용
    val iter = list.iterator()
    while (iter.hasNext()) {
        println(iter.next())
    }

    // for-each문 사용
    for (e in list)
        println(e)
}
```

<br>

## Iterable, Iterator / Observable, Observer

기존 자바에선 **Iterable, Iterator**를 이용해 mapping을 하는 구조<sup> `pull`</sup>였다면 RxJava에선 **Observable, Observer**를 이용하여 mapping을 한다.<sup> `push`</sup>


-  설명 추가
