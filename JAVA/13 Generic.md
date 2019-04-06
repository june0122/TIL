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

