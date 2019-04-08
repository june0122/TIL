# 챕터 13. 제네릭(Generic)

## 제네릭을 사용하는 이유

- Java 5부터 제네릭 타입이 새로 추가됨.

- 제네릭 타입을 이용함으로써 잘못된 타입이 사용될 수 있는 문제를 컴파일 과정에서 제거할 수 있다.

<br>

### 컴파일 시 강한 타입 체크를 할 수 있다.

- 자바 컴파일러는 코드에서 잘못 사용된 타입 때문에 발생하는 문제점을 제거하기 위해 제네릭 코드에 대해 강한 타입 체크를 한다.

- 실행 시 타입 에러가 나는 것보다는 **컴파일 시에 미리 타입을 강하게 체크**해서 **에러를 사전에 방지**하는 것이 좋다.

<br>

### 타입 변환(casting)을 제거한다.

- `비제네릭 코드`는 **불필요한 타입 변환**을 하기 때문에 **프로그램 성능에 악영향**을 미친다.

> `비제네릭 코드` : List에 문자열 요소를 저장했지만, 요소를 찾아올 때는 반드시 String으로 타입 변환

```java
List list = new ArrayList();
list.add("hello");
String str = (String) list.get(0);  // 타입 변환을 해야 한다.
```

> `제네릭 코드` : List에 저장되는 요소를 String 타입으로 국한하기 때문에 요소를 찾아올 때 타입 변환을 할 필요가 없어 프로그램 성능이 향상된다.

```java
List<String> list = new ArrayList<String>();
list.add("hello");
String str = list.get(0);  // 타입 변환을 하지 않는다.
```

<br>

## 제네릭 타입 `class<T>` , `interface<T>`

- 제네릭 타입은 타입을 파라미터로 가지는 클래스와 인터페이스를 말한다.

- 제네릭 타입은 클래스 또는 인터페이스 이름 뒤에 `< >` 부호가 붙고, 사이에 타입 파라미터가 위치한다.

```
public class 클래스명<T> { ... }
public interface 인터페이스명<T> { ... }
```

- 타입 파라미터는 변수명과 동일한 규칙에 따라 작성할 수 있지만, 일반적으로 대문자 알파벳 한 글자로 표현한다.

- 제네릭 타입을 실제 코드에서 사용하려면 타입 파라미터에 구체적인 타입을 지정해야 한다.

```java
public class Box {
    private Object object;
    public void set(Object object) { this.object = onject; }
    public Object get() { return objectl }
}
```

- Box 클래스의 필드 타입이 Object인데, `Object` 타입으로 선언한 이유는 **필드에 모든 종류의 객체를 저장하고 싶어서**이다.

- Object 클래스는 모든 자바 클래스의 최상위 조상(부모) 클래스이다. 따라서 자식 객체는 부모 타입에 대입할 수 있다는 성질 때문에 모든 자바 객체는 Object 타입으로 자동 타입 변환되어 저장된다.

```java
Object object = 자바의 모든 객체;
```

- `set()` 메소드 : 매개 변수 타입으로 Object를 사용함으로써 매개값으로 자바의 모든 객체를 받을 수 있게 했고, 받은 매개값을 Object 필드에 저장시킨다.

- `get()` 메소드 : Object 필드에 저장된 객체를 Object 타입으로 리턴한다.

<br>

> 필드에 저장된 원래 타입의 객체를 얻을 경우, 강제 타입 변환을 해야 함

```java
Box box = new Box();
box.set("hello");  // String 타입을 Object 타입으로 자동 타입 변환해서 저장
String str = (String) box.get();  // Object 타입을 String 타입으로 강제 타입 변환해서 얻음
```

<br>

#### 비제네릭 타입 예시

> Box 클래스

```java
public class Box {
    private Object object;
    public void set(Object object) { this.object = object; }
    public Object get() { return object; }
}
```

> Apple 클래스

```java
public class Apple {
}
```

> BoxExample, 비제네릭 타입 이용

```java
public class BoxExample {
    public static void main(String[] args) {
        Box box = new Box();
        box.set("홍길동");                  // String -> Object (자동 타입 변환)
        String name = (String )box.get();   // Object -> String (강제 타입 변환)

        box.set(new Apple());               // Apple -> Object (자동 타입 변환)
        Apple apple = (Apple) box.get();    // Object -> Apple (강제 타입 변환)
    }
}
```

<br>

#### 제네릭 타입 예시

> Box, 제네릭 타입

```java
public class Box<T> {
    private T t;
    public T get() { return t; }
    public void set(T t) { this.t = t; }
}
```

> BoxExample, 제네릭 타입 이용

```java
public class BoxExample {
    public static void main(String[] args) {
        Box<String> box1 = new Box<String>();
        box1.set("hello");
        String str = box1.get();
        
        Box<Integer> box2 = new Box<Integer>();
        box2.set(6);
        int value = box2.get();
    }
}
```

<br>

## 멀티 타입 파라미터 `class<K, V, ...>` , `interface<K, V, ...>`

- 제네릭 타입은 두 개 이상의 멀티 타입 파라미터를 사용할 수 있는데, 이 경우 각 타입 파라미터를 콤마로 구분한다.

> Product, 제네릭 클래스

- `Product<T, M>` 제네릭 타입 정의

```java
public class Product<T, M> {
    private T kind;
    private M model;
    
    public T getKind() { return this.kind; }
    public M getModel() { return this.model; }
    
    public void setKind(T kind) { this.kind = kind; }
    public void setModel(M model) { this.model = model; }
}
```

> ProductExample, 제네릭 객체 생성

- `Product<Tv, String>` 객체와 `Product<Car, String>` 객체를 생성

- Getter와 Setter 호출 방법

```java
public class ProductExample {
    public static void main(String[] args) {
        Product<Tv, String> product1 = new Product<Tv, String>();
        product1.setKind(new Tv());
        product1.setModel("스마트Tv");
        Tv tv = product1.getKind();
        String tvModel = product1.getModel();
        
        Product<Car, String> product2 = new Product<Car, String>();
        product2.setKind(new Car());
        product2.setModel("디젤");
        Car car = product2.getKind();
        String carModel = product2.getModel();
    }
}
```

<br>

- 제네릭 타입 변수 선언과 객체 생성을 동시에 할 때 타입 파라미터 자리에 구체적인 타입을 지정하는 코드가 중복해서 나와 다소 복잡해질 수 있다.

- Java 7부터는 제네릭 타입 파라미터의 중복 기술을 줄이기 위해 **다이아몬드 연산자 `<>`** 를 제공한다.

> Java 6 이전 버전에서 사용한 제네릭 타입 변수 선언과 객체 생성 코드

```java
Product<Tv, String> product = new Product<Tv, String>();
```

> Java 7부터 다이아몬드 연산자를 사용해서 간단히 표현할 수 있다.

```java
Product<Tv, String> product = new Product<>();
```

<br>

## 제네릭 메소드 `<T, R> R method(T t)`

- 제네릭 메소드는 매개 타입과 리턴 타입으로 타입 파라미터를 같는 메소드를 말한다. 제네릭 메소드를 선언하는 방법은 리턴 타잎 앞에 `<>` 기호를 추가하고 타입 파라미터를 기술한 다음, 리턴 타입과 매개 타입으로 타입 파라미터를 사용하면 된다.

```java
public <타입파라미터, ...> 리턴타입 메소드명(매개변수, ...) { ... }
```

<br>

> boxing() 제네릭 메소드, `<>` 기호 안에 타입 파라미터 T를 기술한 뒤, 매개 변수 타입으로 T를 사용했고,리턴 타입으로 제네릭 타입 Box<T>를 사용했다.

```java
public <T> Box<T> boxing(T t) { ... }
```

- 제네릭 메소드는 두 가지 방식으로 호출 가능

- 코드에서 타입 파라미터의 구체적인 타입을 명시적으로 지정해도 되고, 컴파일러가 매개값의 타입을 보고 구체적인 타입을 추정하도록 할 수 있다.

```java
리턴타입 변수 = <구체적타입> 메소드명(매개값);  // 명시적으로 구체적 타입을 지정
리턴타입 변수 = 메소드명(매개값);  // 매개값을 보고 구체적 타입을 추정
```

- 다음 코드는 boxing() 메소드를 호출하는 코드이다.

```java
Box<Integer> box = <Integer>boxing(100);  // 타입 파라미터를 명시적으로 Integer로 지정
Box<Integer> box = boxing(100);  // 타입 파라미터를 Integer로 추정
```

<br>

> Util, 제네릭 메소드

- Util 클래스에 정적 제네릭 메소드로 boxing()을 정의하고 BoxingMethodExample 클래스에서 호출

```java
public class Util {
    public static <T> Box<T> boxing(T t) {
        Box<T> box = new Box<T>();
        box.set(t);
        return box;
    }
}
```

> BoxingMethodExample, 제네릭 메소드 호출

```java
public class BoxingMethodExample {
    public static void main(String[] args) {
        Box<Integer> box1 = Util.<Integer>boxing(100);
        int intValue = box1.get();

        Box<String> box2 = Util.boxing("홍길동");
        String strValue = box2.get();
    }
}
```

<br>

- 다음 예제는 Util 클래스에 정적 제네릭 메소드로 compare()를 정의하고 CompareMethodExample 클래스에서 호출했다. 타입 파라미터는 K와 V로 선언되었는데, 제네릭 타입 Pair가 K와 V를 가지고 있기 때문이다. compare() 메소드는 두 개의 Pair 매개값으로 받아 K와 V 값이 동일한지 검사하고 boolean 값을 리턴한다.

> Util, 제네릭 메소드

```java
public class Util {
    public static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {
       boolean keyCompare = p1.getKey().equals(p2.getKey());
       boolean valueCompare = p1.getValue().equals(p2.getValue());
       return keyCompare && valueCompare;
    }
}
```

> Pair, 제네릭 타입

```java
public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) { this.key = key; }
    public void setValue(V value) { this.value = value; }
    public K getKey() { return key; }
    public V getValue() { return value; }
}

```

> CompareMethodExample, 제네릭 메소드 호출

```java
public class CompareMethodExample {
    public static void main(String[] args) {
        Pair<Integer, String> p1 = new Pair<Integer, String>(1, "사과");
        Pair<Integer, String> p2 = new Pair<Integer, String>(1, "사과");
        boolean result1 = Util.<Integer, String>compare(p1, p2);
        if (result1) {
            System.out.println("논리적으로 동등한 객체입니다.");
        } else {
            System.out.println("논리적으로 동등하지 않은 객체입니다.");
        }

        Pair<String, String> p3 = new Pair<String, String>("user1", "홍길동");
        Pair<String, String> p4 = new Pair<String, String>("user2", "홍길동");
        boolean result2 = Util.compare(p3, p4);
        if (result2) {
            System.out.println("논리적으로 동등한 객체입니다.");
        } else {
            System.out.println("논리적으로 동등하지 않은 객체입니다.");
        }
    }
}
```

```
논리적으로 동등한 객체입니다.
논리적으로 동등하지 않은 객체입니다.
```

<br>

## 제한된 타입 파라미터 `T extends 최상위타입`

- 타입 파라미터에 지정되는 구체적인 타입을 제한할 필요가 종종있다.

    - 예를 들어 숫자를 연산하는 제네릭 메소드는 메개값으로 Number 타입 또는 하위 클래스 타입 `Byte, Short, Integer, Long, Double` 의 인스턴스만 가져야 한다.

    - 이것이 제한된 타입 파라미터(bounded type parameter)가 필요한 이유다.

- 제한된 타입 파라미터를 선언하려면 타입 파라미터 뒤에 extends 키워드를 붙이고 상위 타입을 명시하면 된다.

    - 상위 타입은 클래스뿐만 아니라 인터페이스도 가능

    - 인터페이스라고 해서 `implements`를 사용하지 않음

```java
public <T extends 상위타입> 리턴타입 메소드(매개변수, ...) { ... }
```

- 타입 파라미터에 지정되는 구체적인 타입은 상위 타입이거나 상위 타입의 하위 또는 구현 클래스만 가능하다.

- `주의할 점`은 **메소드의 중괄호 `{}` 안에서 타입 파라미터 변수로 사용 가능한 것은 상위 타입의 멤버(필드, 메소드)로 제한**된다. **하위 타입에만 있는 필드와 메소드는 사용할 수 없다.**

```java
public <T extends Number> int compare(T t1, T t2) {
    double v1 = t1.doubleValue();  // Number의 doubleValue() 메소드 사용
    double v2 = t2.doubleValue();  // Number의 doubleValue() 메소드 사용
    return Double.compare(v1, v2);
}
```

- doubleValue() 메소드 : Number 클래스에 정의된 메소드로 숫자를 double 타입으로 변환

- Double.compare() 메소드 : 첫 번째 매개값이 작으면 -1, 같으면 0, 크면 1을 리턴

> Util, 제네릭 메소드

```java
public class Util {
    public static <T extends Number> int compare(T t1, T t2) {
        double v1 = t1.doubleValue();
        double v2 = t2.doubleValue();
        return Double.compare(v1, v2);
    }
}
```

> BoundedTypeParameterExample, 제네릭 메소드 호출

```java
public class BoundedTypeParameterExample {
    public static void main(String[] args) {
        // String str = Util.compare("a", "b");  // String은 Number 타입이 아님

        int result1 = Util.compare(10, 20);
        System.out.println(result1);

        int result2 = Util.compare(4.5, 3);
        System.out.println(result2);
    }
}
```

```
-1
1
```

<br>

## 와일드카드 타입 `<?>, <? extends ...>, <? super ...>`

- 코드에서 `?`를 일반적으로 와일드카드(wildcard)라고 부른다.

- 제네릭 타입을 매개값이나 리턴 타입으로 사용할 때 구체적인 타입 대신에 와일드카드를 다음과 같이 세 가지 형태로 사용할 수 있다.

    1. `제네릭타입<?>` : Unbounded Wildcards(제한 없음)

        - 타입 파라미터를 대치하는 구체적인 타입으로 모든 클래스나 인터페이스 타입이 올 수 있다.

    2. `제네릭타입<? extends 상위타입>` : Upper Bounded Wildcards(상위 클래스 제한)

        - 타입 파라미터를 대치하는 구체적인 타입으로 상위 타입이나 하위 타입만 올 수 있다.

    3. `제네릭타입<? super 하위타입>` Lower Bounded Wildcards(하위 클래스 제한)

        - 타입 파라미터를 대치하는 구체적인 타입으로 하위 타입이나 상위 타입이 올 수 있다.

<br>

- 설명만으론 이해가 어려우니 예제를 통해 숙지하도록 하자.

- 제네릭 타입 Course는 과정 클래스로 과정 이름과 수강생을 저장할 수 있는 배열을 가지고 있다. 타입 파라미터 T가 적용된 곳은 수강생 타입 부분이다.

> Course, 제네릭 타입

```java
public class Course<T> {
    private String name;
    private T[] students;

    public Course(String name, int capacity) {
        this.name = name;
        students = (T[]) (new Object[capacity]);
        // 타입 파라미터로 배열을 생성하려면 new T[n] 형태로 생성할 수 없다.
        // (T[]) (new Object[n]) 으로 생성해야 한다.
    }

    public String getName() { return name; }
    public T[] getStudents() { return students; }

    // 배열에 비어있는 부분을 찾아서 수강생을 추가하는 메소드
    public void add(T t) {
        for (int i = 0; i < students.length; i++) {
            if (students[i] == null) {
                students[i] = t;
                break;
            }
        }
    }
}
```

- 수강생이 될 수 있는 타입은 다음 4가지 클래스라고 가정하자.

- Person의 하위 클래스로 Worker와 Student가 있고, Student의 하위 클래스로 HighStudent가 있다.

    1. `Course<?>`

        - 수강생은 모든 타입(Person, Walker, Student, HighStudent)이 될 수 있다.

    2. `Course<? extends Student>`

        - 수강생은 Student와 HighStudent만 될 수 있다.

    3. `Course<? super Worker>`

        - 수강생은 Worker와 Person만 될 수 있다.

- 다음 예제는 registerCourseXXX () 메소드의 매개값으로 와일드카드 타입을 사용하였다.

- registerCourse() 는 모든 수강생이 들을 수 있는 과정을 등록하고, registerCourseStudent() 는 학생만, registerCourseWorker는 직장인만 들을 수 있는 과정을 등록한다.

<br>

> Person

```java
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() { return name; }


    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return name;
    }
}
```

> Student

```java
public class Student extends Person {
    public Student(String name) {
        super(name);
    }
}
```

> Worker

```java
public class Worker extends Person {
    public Worker(String name) {
        super(name);
    }
}
```

> HighStudent

```java
public class HighStudent extends Student {
    public HighStudent(String name) {
        super(name);
    }
}
```


> WildCardExample, 와일드카드 타입 매개 변수

```java
import java.util.Arrays;

public class WildCardExample {
    public static void registerCourse(Course<?> course) {
        System.out.println(course.getName() + " 수강생: " + Arrays.toString(course.getStudents()));
    }

    public static void registerCourseStudent(Course<? extends Student> course) {
        System.out.println(course.getName() + " 수강생: " + Arrays.toString(course.getStudents()));
    }

    public static void registerCourseWorker(Course<? super Worker> course) {
        System.out.println(course.getName() + " 수강생: " + Arrays.toString(course.getStudents()));
    }

    public static void main(String[] args) {
        Course<Person> personCourse = new Course<Person>("일반인과정", 5);
        personCourse.add(new Person("일반인"));
        personCourse.add(new Worker("직장인"));
        personCourse.add(new Student("학생"));
        personCourse.add(new HighStudent("고등학생"));

        Course<Worker> workerCourse = new Course<Worker>("직장인과정", 5);
        workerCourse.add(new Worker("직장인"));

        Course<Student> studentCourse = new Course<Student>("학생과정", 5);
        studentCourse.add(new Student("학생"));
        studentCourse.add(new HighStudent("고등학생"));

        Course<HighStudent> highStudentCourse = new Course<HighStudent>("고등학생과정", 5);
        highStudentCourse.add(new HighStudent("고등학생"));

        // 모든 과정 등록 가능
        registerCourse(personCourse);
        registerCourse(workerCourse);
        registerCourse(studentCourse);
        registerCourse(highStudentCourse);
        System.out.println();

        // 학생 과정만 등록 가능
        // registerCourseStudent(personCourse);  (x)
        // registerCourseStudent(workerCourse);  (x)
        registerCourseStudent(studentCourse);
        registerCourseStudent(highStudentCourse);
        System.out.println();

        // 직장인과 일반인 과정만 등록 가능
        registerCourseWorker(personCourse);
        registerCourseWorker(workerCourse);
        // registerCourseWorker(studentCourse);  (x)
        // registerCourseWorker(highStudentCourse);  (x)
    }
}
```

```
일반인과정 수강생: [일반인, 직장인, 학생, 고등학생, null]
직장인과정 수강생: [직장인, null, null, null, null]
학생과정 수강생: [학생, 고등학생, null, null, null]
고등학생과정 수강생: [고등학생, null, null, null, null]

학생과정 수강생: [학생, 고등학생, null, null, null]
고등학생과정 수강생: [고등학생, null, null, null, null]

일반인과정 수강생: [일반인, 직장인, 학생, 고등학생, null]
직장인과정 수강생: [직장인, null, null, null, null]
```

<br>

## 제네릭 타입의 상속과 구현

- 제네릭 타입도 다른 타입과 마찬가지로 부모 클래스가 될 수 있다.

> Product<T, M> 제네릭 타입을 상속해서 ChildProduct<T, M> 타입 정의

```java
public class ChildProduct<T, M> extends Product<T, M> { ... }
```

<br>

- 자식 제네릭 타입은 추가적으로 타입 파라미터를 가질 수 있다.

> 세 가지 타입 파라미터를 가진 자식 제네릭 타입 선언

```java
public class ChildProduct<T, M, C> extends Product<T, M> { ... }
```

<br>

> Product, 부모 제네릭 클래스

```java
public class Product<T, M> {
    private T kind;
    private M model;

    public T getKind() { return this.kind; }
    public M getModel() { return this.model; }

    public void setKind(T kind) { this.kind = kind; }

    public void setModel(M model) { this.model = model; }
}

class Tv {}
```

> ChildProduct, 자식 제네릭 클래스

```java
public class ChildProduct<T, M, C> extends Product<T, M> {
    private C company;

    public C getCompany() { return this.company; }
    public void setCompany(C company) { this.company = company; }
}
```

<br>

- 제네릭 인터페이스를 구현한 클래스도 제네릭 타입이 되는데, 다음과 같이 제네릭 인터페이스가 있다고 가정해보자.

> Storage, 제네릭 인터페이스

```java
public interface Storage<T> {
    public void add(T item, int index);
    public T get(int index);
}

```

- 제네릭 인터페이스인 Storage<T> 타입을 구현한 StorageImpl 클래스도 제네릭 타입이어야 한다.

> StorageImpl, 제네릭 구현 클래스

```java
public class StorageImpl<T> implements Storage<T> {
    private T[] array;
    
    public StorageImpl(int capacity) {
        this.array = (T[])(new Object[capacity]);
    }
    
    @Override
    public void add(T item, int index) {
        array[index] = item;
    }
    
    @Override
    public T get(int index) {
        return array[index];
    }
}
```

- 다음 ChildProductAndStorageExample은 ChildProduct<T, M, C>와 StorageImpl<T> 클래스의 사용 방법을 보여준다.

> ChildProductAndStorageExample

```java
public class ChildProductAndStorageExample {
    public static void main(String[] args) {
        ChildProduct<Tv, String, String> product = new ChildProduct<>();
        product.setKind(new Tv());
        product.setModel("SmartTV");
        product.setCompany("Samsung");

        Storage<Tv> storage = new StorageImpl<Tv>(100);
        storage.add(new Tv(), 0);
        Tv tv = storage.get(0);
    }
}
```
