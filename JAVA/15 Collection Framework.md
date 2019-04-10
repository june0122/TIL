# 컬렉션 프레임워크(Collection Framework)

## 컬렉션 프레임워크 소개

- 애플리케이션을 개발하다보면 다수의 객체를 저장해두고 필요할 때마다 꺼내서 사용하는 경우가 많다. 여러 개의 객체를 저장해두고, 필요할 때마다 하나씩 꺼내서 이용한다고 가정해보자.

    - 가장 간단한 방법은 `배열`을 이용하는 것이다.

      - 배열은 쉽게 생성하고 사용할 수 있지만, 저장할 수 있는 객체 수가 배열을 생성할 때 결정되기 때문에 불특정 다수의 객체를 저장하기엔 무리가 있다.

      - 그리고 객체를 삭제했을 때 해당 인덱스가 비게 되어 낱알 빠진 옥수수 같은 형태가 되는데, 새로운 객체 저장시에 어디가 비어있는지 일일이 확인하는 코드도 필요해진다.

- 이러한 문제점을 해결하기 위해, 널리 알려져 있는 `자료구조(Data Structure)`를 바탕으로 객체들을 효율적으로 추가, 삭제, 검색할 수 있도록 `java.util 패키지`에 `컬렉션과 관련된 인터페이스와 클래스들을 포함`시켜 놓았다.

    - `컬렉션(Collection)`이란 사전적 의미로 요소를 수집해서 저장하는 것을 말하는데, 자바 컬렉션은 **객체를 수집해서 저장하는 역할**을 한다.

    - `프레임워크`란 **사용 방법을 미리 정해 놓은 라이브러리**를 말한다.

- 컬렉션 프레임워크의 주요 인터페이스로는 **List, Set, Map** 이 있다.

    - 이 인터페이스들은 컬렉션을 사용하는 방법을 정의한 것인데, 다음은 이 인터페이스로 사용 가능한 컬렉션 클래스를 보여준다.

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55860507-5b428000-5baf-11e9-9db5-a1da58c818f6.png'>
</p>

<br>

- `ArrayList, Vector, LinkedList`는 **List 인터페이스를 구현한 클래스**로, List 인터페이스로 사용가능한 컬렉션이다.

- `HashSet, TreeSet`은 **Set 인터페이스를 구현한 클래스**로, Set 인터페이스로 사용가능한 컬렉션이다.

- `HashMap, Hashtable, TreeMap, Properties`는 **Map 인터페이스를 구현한 클래스**로, Map 인터페이스로 사용가능한 컬렉션이다.

<br>

- `List와 Set`은 **객체를 추가, 삭제, 검색하는 방법에 많은 공통점**이 있기 때문에 **이 인터페이스들의 공통된 메소드들만 모아 Collection 인터페이스로 정의**해두고 있다.

- `Map`은 키와 값(Key, Value)을 하나의 쌍으로 묶어서 관리하는 구조로 되어 있어, **List 및 Set과는 사용 방법이 완전히 다르다.**

<br>

## List 컬렉션

- 객체를 일렬로 늘어놓은 구조

- 객체를 인덱스로 관리하기 때문에 객체를 저장하면 자동 인덱스가 부여되고 인덱스로 객체를 검색, 삭제할 수 있는 기능 제공

- List 컬렉션은 **객체 자체를 저장하는 것이 아니라 객체의 번지를 참조**한다.

- 동일한 객체 중복 저장 가능

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55868721-01968180-5bc0-11e9-9ff1-0b12a66974e3.png'>
</p>

<br>

> List 인터페이스의 메소드들

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55868722-01968180-5bc0-11e9-9f17-216cda4d51ba.png'>
</p>
<br>

### ArrayList

> ArrayListExample, String 객체를 저장하는 ArrayList

```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListExample {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();

        list.add("java");  // String 객체를 저장
        list.add("JDBC");
        list.add("Servlet/JSP");
        list.add(2, "Database");
        list.add("iBATIS");

        int size = list.size();
        System.out.println("총 객체수: " + size);
        System.out.println();

        String skill = list.get(2);
        System.out.println("2: " + skill);
        System.out.println();

        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            System.out.println(i + ":" + str);
        }
        System.out.println();

        list.remove(2);  // Database 삭제
        list.remove(2);  // Servlet/JSP 삭제
        list.remove("iBATIS");

        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            System.out.println(i + ":" + str);
        }
        System.out.println();

    }
}
```

```
총 객체수: 5

2: Database

0:java
1:JDBC
2:Database
3:Servlet/JSP
4:iBATIS

0:java
1:JDBC
```


<br>

- ArrayList를 생성하고 런타임 시 필요한 객체들을 추가하느 것이 일반적이지만, **고정된 객체들로 구성된 List를 생성**하고 싶을 때 `Arrays.asList(T...a)` 매소드를 사용하는 것이 간편하다.

```java
List<T> list = Arrays.asList(T...a);
```

<br>

> ArrayAsListExample, `Arrays.asList()` 메소드

```java
import java.util.Arrays;
import java.util.List;

public class ArrayAsListExample {
    public static void main(String[] args) {
        List<String> list1 = Arrays.asList("홍길동", "신용권", "김자바");
        for (String name : list1) {
            System.out.println(name);
        }

        List<Integer> list2 = Arrays.asList(1, 2, 3);
        for (int value : list2) {
            System.out.println(value);
        }
    }
}
```

<br>

## Vector

- ArrayList와 동일한 내부 구조

- Vector 생성을 위해서는 저장할 객체 타입을 타입 파라미터로 표기하고 기본 생성자를 호출

```java
List<E> list = new Vector<E>();
```

- ArrayList와 다른 점은 Vector는 **동기화된(sychronized) 메소드로 구성**되어 있기 때문에 멀티 스레드가 동시에 이 메소드들을 실행할 수 없고, 하나의 스레드가 실행을 완료해야만 다른 스레드를 실행할 수 있다.

    - 그래서 멀티 스레드 환경에서 안전하게 객체를 추가, 삭제할 수 있다.

    - 이것을 스레드가 안전(Thread Safe)하다라고 말한다.

<br>

> VectorExample, Board 객체를 저장하는 Vector

```java
import java.util.*;

public class VectorExample {
    public static void main(String[] args) {
        List<Board> list = new Vector<Board>();

        list.add(new Board("제목1", "내용1", "글쓴이1"));
        list.add(new Board("제목2", "내용2", "글쓴이2"));
        list.add(new Board("제목3", "내용3", "글쓴이3"));
        list.add(new Board("제목4", "내용4", "글쓴이4"));
        list.add(new Board("제목5", "내용5", "글쓴이5"));

        list.remove(2);
        list.remove(3);

        for (int i = 0; i < list.size(); i++) {
            Board board = list.get(i);
            System.out.println(board.subject + "\t" + board.content + "\t" + board.writer);
        }
    }
}

```

> Board, 게시물 정보 객체

```java
public class Board {
    String subject;
    String content;
    String writer;

    public Board(String subject, String content, String writer) {
        this.subject = subject;
        this.content = content;
        this.writer = writer;
    }
}
```

```
제목1	내용1	글쓴이1
제목2	내용2	글쓴이2
제목4	내용4	글쓴이4
```

<br>


### LinkedList

- LinkedList는 List 구현 클래스이므로 ArrayList와 사용 방법은 똑같지만 **내부 구조는 완전 다르다.**

- ArrayList는 내부 배열에 객체를 저장해서 인덱스로 관리하지만, LinkedList는 **인접 참조를 링크**해서 체인처럼 관리한다.

    - 특정 인덱스의 객체를 `제거`하면 앞뒤의 링크만 변경되고 나머지 링크는 변경 되지 않는다. 특정 인덱스에 객체를 `삽입`할 때에도 마찬가지.

    - **빈번한 객체 삭제와 삽입이 일어나는 곳에서는 ArrayList보다 LinkedList가 좋은 성능을 발휘**한다.

<br>

```java
List<E> list = new LinkedList<E>();
```

<br>

> LinkedListExample, ArrayList와 LinkedList의 실행 성능 비교

```java
import java.util.*;

public class LinkedListExample {
    public static void main(String[] args) {
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new LinkedList<String>();

        long startTime;
        long endTime;

        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            list1.add(0, String.valueOf(i));
        }
        endTime = System.nanoTime();
        System.out.println("ArrayList 걸린시간: " + (endTime - startTime) + " ns");

        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            list2.add(0, String.valueOf(i));
        }
        endTime = System.nanoTime();
        System.out.println("LinkedList 걸린시간: " + (endTime - startTime) + " ns");
    }
}
```

```
ArrayList 걸린시간: 17629425 ns
LinkedList 걸린시간: 3744424 ns
```

- 끝에서부터(순차적으로) 추가/삭제하는 경우는 ArrayList가 빠르지만, 중간에 추가 또는 삭제할 경우는 LinkedList가 더 빠르다.

    - ArrayList는 뒤쪽 인덱스들을 모두 1싹 중가 또는 감소시키는 시간이 필요하므로 처리 속도가 느리다.

<br>

|구분|순차적으로 추가/삭제|중간에 추가/삭제|검색|
|---|---|---|---|
|ArrayList|빠르다|느리다|빠르다|
|LinkedList|느리다|빠르다|느리다|

<br>

## Set 컬렉션

- List 컬렉션은 저장 순서를 유지하지만, `Set 컬렉션`은 **저장 순서가 유지되지 않는다.**

- 객체를 중복해서 저장할 수 없고, 하나의 null만 저장할 수 있다.

    - 수학의 집합과 구슬 주머니에 비유되기도 한다.

- Set 컬렉션에는 `HashSet, LinkedHashSet, TreeSet` 등이 있는데, Set 인터페이스들의 메소드들은 **인덱스로 관리하지 않기 때문에 인덱스를 매개값으로 갖는 메소드가 없다.**

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55868724-022f1800-5bc0-11e9-8933-6dd0cd44c516.png'>
</p>
<br>

- Set 컬렉션은 인덱스로 객체를 검색이 불가능하기 때문에, 전체 객체를 대상으로 한 번씩 반복해서 가져오는 **반복자(Iterator)**를 제공한다.

    - `반복자`는 **Iterator 인터페이스를 구현한 객체**를 말하는데, iterator() 메소드를 호출하면 얻을 수 있다.

```java
Set<String> set = ...;
Iterator<String> iterator = set.iterator();
```

<br>

> Iterator 인터페이스에 선언된 메소드들

|리터 타입|메소드명|설명|
|---|---|---|
|boolean|hasNext()|가져올 객체가 있으면 true를 리턴하고 없으면 false를 리턴한다.|
|E|next()|컬렉션에서 하나의 객체를 가져온다.|
|void|remove()|Set 컬렉션에서 객체를 제거한다.|

<br>

- iterator에서 하나의 객체를 가져올 때는 `next()` 메소드를 사용한다.

    - `next()` 메소드를 사용하기 전에 먼저 가져올 객체가 있는지 확인하는 것이 좋다. `hasNext()` 메소드는 가져올 객체가 있으면 true를 리턴하고 더 이상 가져올 객체가 없으면 false를 리턴한다.

<br>

> Set 컬렉션에서 String 객체들을 반복해서 하나씩 가져오는 코드

```java
Set<String> set = ...;
Iterator<String> iterator = set.iterator();
// 저장된 객체 수만큼 루핑한다.
while (iterator.hasNext()) {
    // String 객체를 하나 가져옴
    String str = iterator.next();
}
```

<br>

> Iterator 사용하지 않고 `향상된 for문을 이용`하여 전체 객체 대상으로 반복

```java
Set<String> set = ...;
// 저장된 객체 수만큼 루핑
for (String str : set) {

}
```

<br>

> remove() 메소드 호출 : Iterator의 next() 메소드로 가져온 객체의 제거

```java
while(iterator.hasNext()) {
    String str = iterator.next();
    if (str.equals("홍길동")) {
        iterator.remove();
    }
}
```

<br>

### HashSet

- HashSet은 Set 인터페이스의 구현 클래스이다.

```java
Set<E> set = new HashSet<E>();
```

- HashSet은 객체들을 순서 없이 저장하고 동일한 객체는 중복 저장하지 않는다.

    - HashSet이 판단하는 동일 객체란 꼭 같은 인스턴스를 뜻하지는 않는다.

- HashSet은 객체를 저장하기 전에 먼저 객체의 `hashCode()` 메소드를 호출해서 해시코드를 얻어낸다. 그리고 이미 저장되어 있는 객체들의 해시코드와 비교한다.

    - 만약 동일한 해시코드가 있다면 다시 `equals()` 메소드로 두 객체를 비교해서 true가 나오면 동일한 객체로 판단하고 중복 저장을 하지 않는다.

<br>

> HashSetExample1, String 객체를 중복 없이 저장하는 HashSet

```java
import java.util.*;

public class HashSetExample1 {
    public static void main(String[] args) {
        Set<String> set = new HashSet<String>();

        set.add("Java");
        set.add("JDBC");
        set.add("Servlet/JSP");
        set.add("Java");
        set.add("iBATIS");

        int size = set.size();
        System.out.println("총 객체수: " + size);

        Iterator<String> iterator = set.iterator();  // 반복자 얻기
        while (iterator.hasNext()) {
            String element = iterator.next();
            System.out.println("\t" + element);
        }

        set.remove("JDBC");
        set.remove("iBATIS");

        System.out.println("총 객체수: " + set.size());

        iterator = set.iterator(); // 반복자 얻기
        while (iterator.hasNext()) {
            String element = iterator.next();
            System.out.println("\t" + element);
        }

        set.clear();  // 모든 객체를 제거하고 비움
        if(set.isEmpty()) {
            System.out.println("비어 있음");
        }
    }
}
```

```
총 객체수: 4
	Java
	JDBC
	Servlet/JSP
	iBATIS
총 객체수: 2
	Java
	Servlet/JSP
비어 있음
```

<br>

- 다음은 사용자 정의 클래스인 Member를 만들고 hashCode()와 equals() 메소드를 오버라이딩한 코드이다.

    - 인스턴스가 달라도 이름과 나이가 동일하다면 동일한 객체로 간주하여 중복 저장되지 않도록 하기 위해서이다.

<br>

> Member, hashCode()와 equals() 메소드 재정의

```java
public class Member {
    public String name;
    public int age;

    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Member) {
            Member member = (Member) obj;
            return member.name.equals(name) && (member.age == age);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode() + age;
    }
}

```

> HashSetExample2, Member 객체를 중복없이 저장하는 HashSet

```java
import java.util.*;

public class HashSetExample2 {
    public static void main(String[] args) {
        Set<Member> set = new HashSet<Member>();

        set.add(new Member("홍길동", 30));
        set.add(new Member("홍길동", 30));

        System.out.println("총 객체수 : " + set.size());  // 저장된 객체 수 얻기
    }
}
```

```
총 객체수 : 1
```

<br>

## Map 컬렉션



<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55868719-01968180-5bc0-11e9-966d-1629564a82d3.png'>
</p>
<br>



<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55868723-022f1800-5bc0-11e9-9a7b-ef8f7611be90.png'>
</p>
<br>


<br>
<p align = 'center'>
<img src = ''>
</p>
<br>


<br>
<p align = 'center'>
<img src = ''>
</p>
<br>