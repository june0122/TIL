# 챕터 06. 클래스

## 객체 지향 프로그래밍(Object Oriented Programming)

### 객체란?

- 속성 : 필드(field)

- 동작 : 메소드(method)

```
리턴값 = 객체.메소드(매개값1, 매개값2, ...);
```

<br>

### 객체 간의 관계

- 객체는 개별적으로 사용될 수 있지만, 대부분 다른 객체와 관계를 맺고 있다.

    - 집합 관계 : 하나는 부품, 하나는 완성품

    - 사용 관계 : 객체 간의 상호작용
    
    - 상속 관계 : 상위(부모) 객체를 기반으로 하위(자식) 객체를 생성하는 관계

<br>

### 객체 지향 프로그래밍의 특징

> 캡슐화(Encapsulation)

- 객체의 필드, 메소드를 하나로 묶고, 실제 구현 내용을 감추는 것.

- 외부 객체는 객체 내부의 구조를 알지 못하며 객체가 노출해서 제공하는 필드와 메소드만 이용할 수 있다.

- 필드와 메소드를 캡슐화하여 보호하는 이유는 외부의 잘못된 사용으로 인해 **객체가 손상되지 않도록** 하는데 있다.

    - 자바 언어는 캡슐화된 멤버를 노출시킬 것인지, 숨길 것인지를 결정하기 위해 **접근 제한자(Access Modifier)**를 사용한다.

<br>

> 상속(Inheritance)

- 상위 객체는 자기가 가지고 있는 필드와 메소드를 하위 객체에게 물려주어 하위 객체가 사용할 수 있도록 해준다.

- 상속은 상위 객체를 재사용해서 하위 객체를 쉽고 빨리 설계할 수 있도록 도와주고, 이미 잘 개발된 객체를 재사용해서 새로운 객체를 만들기 때문에 반복된 코드의 중복을 줄여준다.

<br>

> 다형성(Polymorphism) : `-morphism : ((연결형)) 「특정 형태를 가진 상태」의 뜻`

- 같은 타입이지만 실행 결과가 다양한 객체를 이용할 수 있는 성질을 말한다.

    - 하나의 타입에 여러 객체를 대입함으로써 다양한 기능을 이용할 수 있도록 해준다.

- 자바는 다형성을 위해 부모 클래스 또는 인터페이스 타입 변환을 허용한다.

    - 부모 타입에는 모든 자식 객체가 대입될 수 있고, 인터페이스 타입에는 모든 구현 객체가 대입될 수 있다.

- 다형성의 효과로 **객체의 부품화** 가 가능하다.

    - 자동차를 설계할 때 타이어 인터페이스 타입을 적용했다면 이 인터페이스를 구현한 실제 타이어들은 어떤 것이든 상관없이 장착(대입)이 가능하다.

<br>

## 객체와 클래스

- 메모리에서 사용하고 싶은 객체가 있다면 우선 설계도로 해당 객체를 만드는 작업이 필요하다. 자동차를 이용하기 위해서는 우선 공장에서 설계도를 보고 자동차를 만들듯이 말이다.
    
    - 자바에서는 **설계도**가 바로 **클래스(class)** 이다.

- `클래스로부터 만들어진 객체`를 **해당 클래스의 인스턴스(instance)**라고 한다.

    - 자동차 객체는 자동차 클래스(설계도)의 인스턴스인 셈이다.

- 클래스로부터 객체를 만드는 과정을 **인스턴스화** 라고 한다.

<br>

> 객체 지향 프로그래밍 개발의 세 가지 단계

```
클래스 설계 - 설계된 클래스를 가지고 사용할 객체를 생성 - 생성된 객체 이용
```

<br>

## 클래스 선언

|번호|작성 규칙|예|
|---|---|---|
|1|하나 이상의 문자로 이루어져야 한다.|Car, SportsCar|
|2|첫 번째 글자는 숫자가 올 수 없다.|Car, 3Car(x)|
|3|'$', '_' 외의 특수 문자 사용 불가|$Car, _Car, @Car(x), #Car(x)|
|4|자바 키워드는 사용 불가|int(x), for(x)|

<br>

```java
public class Car {

}

class Tire {

}
```

- 두 개 이상의 클래스가 선언된 소스 파일을 컴파일하면 `바이트 코드 파일 (.class)`은 클래스를 선언한 개수만큼 생긴다.

    - 결국 소스 파일은 클래스 선언을 담고 있는 저장 단위일 뿐, 클래스 자체가 아니다.

    - 상기 코드를 컴파일하면 Car.class와 Tire.class가 각각 생성된다.

- **파일 이름과 동일한 이름의 클래스 선언에만 public 접근 제한자를 붙일 수 있다.**

    - 가급적이면 파일 하나당 동일한 이름의 클래스 하나를 선언하는 것이 좋다.

<br>

## 객체 생성과 클래스 변수

- 클래스로부터 객체를 생성하는 방법은 다음과 같이 **new 연산자** 를 사용한다.

```java
new 클래스();
```

- `new`는 클래스로부터 객체를 생성시키는 연산자이다. `new` 연산자 뒤에는 **생성자** 가 오는데, `생성자`는 `클래스()` 형태를 가지고 있다.

  - new 연산자로 생성된 객체는 메모리 힙(heap) 영역에 생성된다.

- 객체 지향 프로그램에서 메모리 내에서 생성된 객체의 위치를 모르면 갹체를 사용할 수 없다.

  - new 연산자는 힙 영역에 객체를 생성시킨 후, 객체의 주소를 리턴하도록 되어 있다.

> 클래스 타입으로 선언된 변수에 new 연산자가 리턴한 객체의 주소를 저장하는 코드

```java
클래스 변수;
변수 = new 클래스();
```

```
클래스 변수 = new 클래스();
```

<br>

> 클래스의 두 가지 용도

- 라이브러리(API: Application Program Interface)용 클래스
  
  - 다른 클래스에서 이용할 목적으로 설계된다. 

- 실행용 클래스

  - 프로그램의 실행 진입점인 main() 메소드를 제공하는 역할

<br>

## 클래스의 구성 멤버

> 필드

- 객체의 고유 데이터, 부품 객체, 상태 정보를 저장하는 곳

- 선언 형태는 변수(variable)와 비슷하지만, 필드를 변수라 부르지 않는다.

- 변수는 생성자와 메소드 전체에서 사용되며 객체가 소멸되지 않는 한 객체와 함께 존재한다.

> 생성자

- new 연산자로 호출되는 특별한 중괄호 { } 블록
  
- **객체 생성 시 초기화**를 담당

- 필드를 초기화하거나, 메소드를 호출해서 객체를 사용할 준비를 한다.

- 생성자는 메소드와 비슷하게 생겼지만, **클래스 이름으로 되어 있고 리턴 타입이 없다.**

> 메소드

- 객체의 동작에 해당하는 중괄호 { } 블록

- 메소드는 필드를 읽고 수정하는 역할도 하지만, 다른 객체를 생성해서 다양한 기능을 수행하기도 한다.

- 메소드는 객체 간의 데이터 전달의 수단으로 사용

- 외부로부터 매개값을 받을 수도 있고, 실행 후 어떤 값을 리턴할 수도 있다.

<br>

## 필드

### 필드 선언

- 필드 선언은 클래스 중괄호 { } 블록 어디서든 존재할 수 있다.
  
  - 하지만 생성자와 메소드 중괄호 블록 내부에는 선언될 수 없다.

  - 생성자와 메소드 중괄호 블록 내부에 선언된 것은 모두 `로컬 변수`가 된다.

- 필드 선언은 변수의 선언 형태와 비슷하다.

    - 그래서 `클래스 멤버 변수`라고 부르기도 하는데, *되도록 필드라는 용어를 그대로 사용* 하자.

- 초기값이 지정되지 않은 필드들은 객체 생성 시 자동으로 기본 초기값으로 설정된다.

    - 정수 타입 필드는 0, 실수 타입 필드는 0.0, boolean 타입은 초기값이 `false`이다.

    - 참조 타입은 객체를 참조하고 있지 않은 상태인 `null`로 초기화된다.

<br>

### 필드 사용

```java
public class CarExample {
    public static void main(String[] args) {
        // 객체 생성
        Car myCar = new Car();

        // 필드값 읽기
        System.out.println("제작회사: " + myCar.company);
        System.out.println("모델명: " + myCar.mode1);
        System.out.println("색상: " + myCar.color);
        System.out.println("최고속도: " + myCar.maxSpeed);
        System.out.println("현재속도: " + myCar.speed);

        // 필드값 변경
        myCar.speed = 60;
        System.out.println("수정된 속도: " + myCar.speed);
    }
}

```

```
제작회사: 현대자동차
모델명: 그랜져
색상: 검정
최고속도: 350
현재속도: 0
수정된 속도: 60
```

<br>

## 생성자(Constructor)

생성자는 new 연산자와 같이 사용되어 클래스로부터 객체를 생성할 때 호출되어 객체의 초기화를 담당한다. **객체 초기화**란 `필드를 초기화`하거나, `메소드를 호출`해서 `객체를 사용할 준비`를 하는 것을 말한다.

### 기본 생성자

- 모든 클래스는 생성자가 반드시 존재하며, 하나 이상을 가질 수 있다.

- 만약 클래스 내부에 생성자 선언을 생략했다면 컴파일러는 다음과 같이 중괄호 { } 블록 내용이 비어있는 **기본 생성자** (Default Constructor)를 바이트 코드에 자동 추가시킨다.

```
[public] 클래스() { }
```

- 클래스가 public class로 선언되면 생성자에도 public이 붙지만, 클래스가 public 없이 선언되면 기본 생성자에도 public이 붙지 않는다.

<br>

---

#### Car 클래스를 설계할 때 생성자를 생략하면 기본 생성자가 다음과 같이 생성된다.

<br>

> 소스 파일(Car.java)

```java
public class Car() {

}
```

> 바이트 코드 파일(Car.class)

```java
public class Car() {
    public Car() { }  // 기본 생성자가 자동으로 추가된다.
}
```

- 그렇기 때문에 클래스에 생성자를 선언하지 않아도 다음과 같이 new 연산자 뒤에 기본 생성자를 호출해서 객체를 생성할 수 있다.

```java
Car myCar = new Car();
                ─┬────
              기본 생성자
```

- 그러나 클래스에 명시적으로 선언한 생성자가 한 개라도 있으면, 컴파일러는 기본 생성자를 추가하지 않는다.

    - 명시적으로 생성자를 선언하는 이유는 객체를 다양하게 초기화하기 위해서이다.

---

<br>

### 생성자 선언

- 생성자는 메소드와 비슷한 모양을 갖고 있으나, 리턴 타입이 없고 클래스 이름과 동일하다.

```java
Car myCar = new Car("그랜저", "검정", 300);
```

```java
public class Car {
    // 생성자
    Car(String model, String color, int maxSpeed) { . . . }
}
```

- ***클래스에 생성자가 명시적으로 선언되어 있을 경우에는 반드시 선언된 생성자를 호출해서 객체를 생성해야만 한다.***

<br>

---

> 생성자 선언

```java
public class Car {
    // 생성자
    Car(String color, int cc) {
    }
}
```

> 생성자를 호출해서 객체 생성

```java
public class CarExample {
    public static void main(String[] args) {
        Car myCar = new Car("검정", 3000);
        // Car myCar = new Car();   (x) 기본 생성자 호출 불가!
    }
}
```

---

<br>

### 필드 초기화

- 클래스로부터 객체가 생성될 때 필드는 기본 초기값으로 자동 설정된다.

- 만약 다른 값으로 초기화하고 싶다면 두 가지 방법이 있다.

    1. 필드를 선언할 때 초기값을 주는 방법

        - 필드 선언 시 초기값을 주면 동일한 클래스로부터 생성되는 객체들은 모두 같은 데이터를 갖게 된다.

        - 물론 객체 생성 후 변경할 수 있지만, 객체 생성 시점에는 필드의 값이 모두 같다.

    2. 생성자에서 초기값을 주는 방법

        - 예시로 이름이나 주민번호가 필드값이라면 클래스를 작성할 때 초기값을 줄 수 없고 객체 생성 시점에 다양한 값을 가져야 한다.

        - 따라서 생성자의 매개값으로 이 값들을 받아 초기화하는 것이 맞다.

    ```java
    public class Korean {
        String nation = "대한민국";
        String name;
        String ssn;

        // 생성자
        public Korean(String n, String s) {
            name = n;
            ssn = s;
        }
    }
    ```

<br>

> 생성자에서 필드 초기화

```java
public class Korean {
    // 필드
    String nation = "대한민국";
    String name;
    String ssn;

    // 생성자
    public Korean(String n, String s) {
        name = n;
        ssn = s;
    }
}
```

> 객체 생성 후 필드값 출력

```java
public class KoreanExample {
    public static void main(String[] args) {
        Korean k1 = new Korean("박자바", "910222-1111111");
        System.out.println("k1.name : " + k1.name);
        System.out.println("k1.ssn : " + k1.ssn);

        Korean k2 = new Korean("김시플", "820612-1234111");
        System.out.println("k2.name : " + k2.name);
        System.out.println("k2.ssn : " + k2.ssn);
    }
}
```

```
k1.name : 박자바
k1.ssn : 910222-1111111
k2.name : 김시플
k2.ssn : 820612-1234111
```

- 위 `Korean 생성자`의 매개 변수 이름은 각각 `n`과 `s`를 사용했다.

- 매개 변수의 이름이 너무 짧으면 코드의 가독성이 좋지 않기 때문에 가능하면 초기화시킬 필드 이름과 비슷하거나 동일한 이름을 사용할 것을 권한다.

- ***관례적으로 필드와 동일한 이름을 갖는 매개 변수를 사용한다.***

    - 이 경우 필드와 매개 변수 이름이 동일하기 때문에 **생성자 내부에서 해당 필드에 `접근할 수 없다`.**

    - 동일한 이름의 매개 변수가 사용 우선순위가 높기 때문이다.

### ★ 해결 방법은 `this.` 를 붙이면 된다!

- `this`는 객체 자신의 참조인데, 우리가 우리 자신을 '나'라고 하듯, 객체가 객체 자신을 'this'라고 한다.

- `this.필드` 는 this라는 참조 변수로 필드를 사용하는 것과 동일하다.

```java
public Korean(String name, String ssn) {
    this.name = name;
    this.ssn = ssn;
}
```

- 생성자의 매개 변수는 중요한 몇 개 필드만 매개 변수를 통해 초기화되고, 나머지 필드들은 필드 선언 시에 초기화하거나 생성자 내부에서 임의의 값 또는 계산된 값으로 초기화한다. 아니면 객체 생성 후에 필드값을 별도로 저장하기도 한다.

<br>

### 생성자 오버로딩(Overloading)

> 생성자 오버로딩이란 **매개 변수를 달리하는 생성자를 여러 개 선언하는 것**을 말한다.

- 외부에서 제공되는 다양한 데이터들을 이용해서 객체를 초기화하려면 생성자도 다양화될 필요가 있다.

- 자바는 다양한 방법으로 객체를 생성할 수 있도록 생성자 오버로딩을 제공한다.

<br>

---

> 오버로딩의 예시

```java
public class Car {
    Car() { ... }
    Car(String model) { ... }
    Car(String model, String color) { ... }
    Car(String model, String color, int maxSpeed) { ... }
}
```

> `오버로딩 시 주의점` : 매개 변수의 타입과 개수 그리고 선언된 순서가 똑같을 경우, **매개 변수 이름만 바꾸는 것은 생성자 오버로딩이라 볼 수 없다.**

```java
Car(String model, String color) { ... }
Car(String color, String model) { ... }  // 오버로딩이 아니다
```

- 생성자가 오버로딩돠어 있을 경우, new 연산자로 생성자를 호출할 때 제공되는 매개값의 타입과 수에 의해 호출될 생성자가 결정된다.

> 다양한 방법으로 Car 객체 생성

```java
Car car1 = new Car();
Car car2 = new Car("그랜저");
Car car3 = new Car("그랜저", "흰색");
Car car4 = new Car("그랜저", "흰색", 300);
```

---

<br>

> 생성자의 오버로딩

```java
public class Car {
    // 필드
    String company = "현대자동차";
    String model;
    String color;
    int maxSpeed;

    // 생성자 1
    Car() {

    }

    // 생성자 2
    Car(String model) {
        this.model = model;
    }

    // 생성자 3
    Car(String model, String color) {
        this.model = model;
        this.color = color;
    }

    // 생성자 4
    Car(String model, String color, int maxSpeed) {
        this.model = model;
        this.color = color;
        this.maxSpeed = maxSpeed;
    }
}
```

> 객체 생성 시 생성자 선택

```java
public class CarExample {
    public static void main(String[] args) {
        Car car1 = new Car();  // 생성자 1 선택
        System.out.println("car1.company : " + car1.company);
        System.out.println();

        Car car2 = new Car("자가용");  // 생성자 2 선택
        System.out.println("car2.company : " + car2.company);
        System.out.println("car2.model : " + car2.model);
        System.out.println();

        Car car3 = new Car("자가용", "뻘강");  // 생성자 3 선택
        System.out.println("car3.company : " + car3.company);
        System.out.println("car3.model : " + car3.model);
        System.out.println("car3.color : " + car3.color);
        System.out.println();

        Car car4 = new Car("택시", "검정", 200);  // 생성자 4 선택
        System.out.println("car4.company : " + car4.company);
        System.out.println("car4.model : " + car4.model);
        System.out.println("car4.color : " + car4.color);
        System.out.println("car4.maxSpeed : " + car4.maxSpeed);
    }
}
```

```
car1.company : 현대자동차

car2.company : 현대자동차
car2.model : 자가용

car3.company : 현대자동차
car3.model : 자가용
car3.color : 뻘강

car4.company : 현대자동차
car4.model : 택시
car4.color : 검정
car4.maxSpeed : 200
```

---

<br>

### 다른 생성자 호출 `this()`

- 생성자 오버로딩이 많아질 경우 **생성자 간의 중복된 코드가 발생**할 수 있다.

- 이 경우, 필드 초기화 내용은 한 생성자에만 집중적으로 작성하고 나머지 생성자는 초기화 내용을 가지고 있는 생성자를 호출하는 방법으로 개선할 수 있다.

> 생성자에서 다른 생성자를 호출할 때는 다음과 같이 `this()` 코드를 사용한다.

```
클래스([매개 변수 선언, ... ]) {
    this( 매개변수, ... , 값, ... );  // 클래스의 다른 생성자 호출
    실행문;
}
```

- `this()`는 자신의 다른 생성자를 호출하는 코드로 **반드시 생성자의 첫줄에서만 허용**된다.

<br>

> 아래 코드의 중복 코드를 없애보자

  ```java
    Car(String model) {
        this.model = model;
        this.color = "은색";
        this.maxSpeed = 250;
    }

    Car(String model, String color) {
        this.model = model;
        this.color = color;
        this.maxSpeed = 250;
    }

    Car(String model, String color, int maxSpeed) {
        this.model = model;
        this.color = color;
        this.maxSpeed = maxSpeed;
    }
  ```

> 다른 생성자를 호출해서 중복 코드 줄이기

```java
public class Car {
    // 필드
    String company = "현대자동차";
    String model;
    String color;
    int maxSpeed;

    Car() {

    }

    Car(String model) {
        this(model, "은색", 250);
    }

    Car(String model, String color) {
        this(model, color, 250)
    }

    Car(String model, String color, int maxSpeed) {
        this.model = model;
        this.color = color;
        this.maxSpeed = maxSpeed;
    }
}
```

> 객체 성성 시 생성자 선택

```java
public class CarExample {
    public static void main(String[] args) {
        Car car1 = new Car();  // 생성자 1 선택
        System.out.println("car1.company : " + car1.company);
        System.out.println();

        Car car2 = new Car("자가용");  // 생성자 2 선택
        System.out.println("car2.company : " + car2.company);
        System.out.println("car2.model : " + car2.model);
        System.out.println();

        Car car3 = new Car("자가용", "뻘강");  // 생성자 3 선택
        System.out.println("car3.company : " + car3.company);
        System.out.println("car3.model : " + car3.model);
        System.out.println("car3.color : " + car3.color);
        System.out.println();

        Car car4 = new Car("택시", "검정", 200);  // 생성자 4 선택
        System.out.println("car4.company : " + car4.company);
        System.out.println("car4.model : " + car4.model);
        System.out.println("car4.color : " + car4.color);
        System.out.println("car4.maxSpeed : " + car4.maxSpeed);
    }
}
```

---

<br>

## 메소드

- 메소드는 필드를 읽고 수정하는 역할도 하지만, 다른 객체를 생성해서 다양한 기능을 수행하기도 한다.

- 메소드는 객체 간의 데이터 전달의 수단으로 사용된다. 외부로부터 매개값을 받을 수도 있고, 실행 후 어떤 값을 리턴할 수도 있다.

### 메소드 선언

- 메소드 선언은 선언부(`리턴타입`, `메소드이름`, `매개변수선언`)와 실행 블록으로 구성된다. 메소드 선언부를 **메소드 시그너쳐(signature)**라고도 한다.

<br>

> 리턴 타입

- 리턴값이 있느냐 없느냐에 따라 메소드를 호출하는 방법이 조금 다르다.

```java
// 메소드 선언

void powerOn() { ... }
double divide(int x, int y) { ... }
```

```java
// 메소드 호출

powerOn();
double result = divide( 10, 20 );
```

- 리턴 타입이 있다고해서 반드시 리턴값을 변수에 저장할 필요는 없다. 리턴값이 중요하지 않고 메소드 실행이 중요한 경우에는 다음과 같이 변수 선언 없이 메소드를 호출할 수도 있다.

<br>

> 메소드 이름

- 숫자로 시작하면 안되고, `$` 와 `_` 를 제외한 특수 문자를 사용하지 말아야 한다.

- 관례적으로 메소드명은 소문자로 작성한다.
  
- 서로 다른 단어가 혼합된 이름이라면 뒤이어 오는 단어의 첫머리 글자는 대문자로 작성한다.

```java
void run() { ... }
void startEngine() { ... }
String getName() { ... }
int[] getScores() { ... }
```

<br>

> 매개 변수 선언

```java
// 메소드 선언

public class Calculator {

    // 메소드
    void powerOn() {
        System.out.println("전원을 켭니다.");
    }

    int plus(int x, int y) {
        int result = x + y;
        return result;
    }

    double divide(int x, int y) {
        double result = (double) x / (double) y;
        return result;
    }

    void powerOff() {
        System.out.println("전원을 끕니다.");
    }
}
```

```java
// 메소드 호출

public class CalculatorExample {
    public static void main(String[] args) {
        Calculator myCalc = new Calculator();
        myCalc.powerOn();

        int result1 = myCalc.plus(5, 6);
        System.out.println("result1: " + result1);

        byte x = 10;
        byte y = 4;
        double result2 = myCalc.divide(x, y);
        System.out.println("result2: " + result2);

        myCalc.powerOff();
    }
}
```

<br>

> 매개 변수의 수를 모를 경우

- 메소드의 매개 변수는 개수가 이미 정해져 있는 것이 일반적이지만, 경우에 따라서는 메소드를 선언할 때 매개 변수의 개수를 알 수 없는 경우가 있다.

- 해결책은 다음과 같이 **매개 변수를 배열 타입으로 선언**하는 것이다.

```java
int sum1(int[] values) {  }
```

```java
int values = { 1, 2, 3 };
int result = sum1(values);
int result = sum1(new int[] { 1, 2, 3, 4, 5 })
```

- 매개 변수를 배열 타입으로 선언하면, 메소드를 호출하기 전에 배열을 생성해야 하는 불편한 점이 있다.

- 그래서 **배열을 생성하지 않고 값의 리스트만 넘겨주는 방법**도 있다.

- **메소드의 매개 변수를 `...` 를 사용해서 선언**하게 되면, 메소드 호출 시 넘겨준 값의 수에 따라 **자동으로 배열이 생성**되고 매개값으로 사용된다.

```java
int sum2(int ... values) {  }
```

- `...` 로 선언된 매개 변수의 값은 다음과 같이 메소드 호출 시 리스트로 나열해주면 된다.

```java
int result = sum2(1, 2, 3);
int result = sum2(1, 2, 3, 4, 5);
```

- `...` 로 선언된 매개 변수는 배열 타입이므로 다음과 같이 배열을 직접 매개값으로 사용해도 좋다.

```java
int[] values = { 1, 2, 3 };
int result = sum2(values);
int result = sum2(new int[] { 1, 2, 3, 4, 5 });  // 배열도 객체이므로
```

<br>

> Computer.java

```java
public class Computer {
    int sum1(int[] values) {
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }

    int sum2(int... values) {
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }
}
```

> ComputerExample.java

```java
public class ComputerExample {
    public static void main(String[] args) {
        Computer myCom = new Computer();

        int[] values1 = {1, 2, 3};

        int result1 = myCom.sum1(values1);
        System.out.println("result1: " + result1);

        int result2 = myCom.sum2(new int[]{1, 2, 3, 4, 5});
        System.out.println("result2: " + result2);

        int result3 = myCom.sum2(1, 2, 3);
        System.out.println("result3: " + result3);

        int result4 = myCom.sum2(1, 2, 3, 4, 5);
        System.out.println("result4: " + result4);
    }
}
```

<br>

### 리턴(return)문

> 리턴값이 있는 메소드

- 메소드 선언에 리턴 타입이 있는 메소드는 밥드시 리턴(return)문을 사용해서 리턴값을 지정해야 한다. 

- 만약 return문이 없다면 컴파일 오류가 발생한다. return문이 실행되면 메소드는 즉시 종료된다.

```java
return 리턴값;
```

- return문의 리턴값은 리턴 타입이거나 리턴 타입으로 변환될 수 있어야 한다.

    - 예를 들어 리턴 타입이 int인 plus() 메소드에서 byte, short, int 타입의 값이 리턴되어도 상관없다.

    - byte와 short은 int로 자동 타입 변환되어 리턴되기 때문이다.

```java
int plus(int x, int y) {
    int result = x + y;
    return result;
}
```

```java
int plus(int x, int y) {
    byte result = (byte) (x + y);
    return result;
}
```

- `return문을 사용할 때 주의할 점` : return문 이후에 실행문이 오면 `Unreachable code` 라는 컴파일 오류가 발생한다.

- 하지만 다음과 같은 경우, 컴파일 에러가 발생하지 않는다.

```java
boolean isLeftGas() {
    if (gas == 0) {
        System.out.println("gas가 없습니다.");
        return false;
    }
    System.out.println("gas가 있습니다.");
    return true;
}
```

<br>

> 리턴값이 없는 메소드 (void)

- void로 선언된 리턴값이 없는 메소드에서도 return문을 사용할 수 있다. 다음과 같이 return문을 사용하면 **메소드 실행을 강제 종료시킨다.**

```java
return;
```

<br>

> 리턴값 없는 메소드 예제

```java
// Gas.java

public class Gas {
    int gas;

    void setGas(int gas) {
        this.gas = gas;
    }

    boolean isLeftGas() {
        if (gas == 0) {
            System.out.println("gas가 없습니다.");
            return false;
        } else {
            System.out.println("gas가 있습니다.");
            return true;
        }
    }

    void run() {
        while (true) {
            if (gas > 0) {
                System.out.println("달립니다. (gas 잔량 : " + gas + ")");
                gas -= 1;
            } else {
                System.out.println("멈춥니다. (gas 잔량 : " + gas + ")");
                return;
            }
        }
    }

}
```

```java
// GasExample.java

public class GasExample {
    public static void main(String[] args) {
        Gas myGas = new Gas();

        myGas.setGas(4);

        boolean gasState = myGas.isLeftGas();
        if (gasState) {
            System.out.println("출발합니다.");
            myGas.run();
        }

        if (myGas.isLeftGas()) {  // 조건을 'gasState'로 변경 가능
            System.out.println("gas를 주입할 필요가 없습니다.");
        } else {
            System.out.println("gas를 주입하세요.");
        }
    }
}

```

```
gas가 있습니다.
출발합니다.
달립니다. (gas 잔량 : 4)
달립니다. (gas 잔량 : 3)
달립니다. (gas 잔량 : 2)
달립니다. (gas 잔량 : 1)
멈춥니다. (gas 잔량 : 0)
gas가 없습니다.
gas를 주입하세요.
```

<br>

### 메소드 호출

- `클래스의 내부`의 다른 메소드에서 호출할 경우에는 단순한 메소드 이름으로 호출하면 된다.

- 하지만 `클래스 외부`에서 호출할 경우에는 우선 클래스로부터 객체를 생성한 뒤, 참조 변수를 이용해서 메소드를 호출해야 한다.

<br>

### 메소드 오버로딩

- 클래스 내에 같은 이름의 메소드를 여러 개 선언하는 것을 **메소드 오버로딩** 이라고 한다.

- **메소드 오버로딩의 조건** : 매개 변수의 `타입`, `개수`, `순서` 중 하나가 달라야 한다.

- 메소드 오버로딩이 필요한 이유 : 매개값을 다양하게 받아 처리할 수 있도록 하기 위해서

```
class 클래스 {
    리턴 타입  메소드이름  ( 타입 변수, ... ) { ... }
        ↑         ↑            ↑

       무관      동일      매개 변수의 타입, 개수, 순서가 달라야 함

        ↓         ↓            ↓
    리턴 타입  메소드이름  ( 타입 변수, ... ) { ... }
}
```

<br>

> 다음 코드들은 어떻게 될까? 컴파일 오류가 날까?

```java
// plus(int x, int y) 메소드
// plus(double x, double y) 메소드 가 있을 때

int x = 10;
double y = 20.3;
plus(x, y);
```

- 첫 번째 매개 변수가 int 타입이고, 두 번째 매개 변수가 double 타입인 plus() 메소드가 없기 때문에 컴파일 오류가 날 것 같지만, `plus(double x, double y) 메소드`가 실행된다.

- 자바 가상 기계는 일차적으로 매개 변수 타입을 보지만, 매개 변수의 타입이 일치하지 않을 경우, **자동 타입 변환이 가능한지를 검사**한다.

    - 첫 번째 매개 변수인 int 타입은 double 타입으로 변환이 가능하므로 plus(double x, double y) 메소드가 선택된다.

<br>

- **메소드 오버로딩할 때 주의할 점** : 매개 변수의 타입과 개수, 순서가 똑같을 경우 `매개 변수의 이름만 바꾸는 것` 은 메소드 오버로딩이라고 볼 수 없다. 또한 `리턴 타입만 다르고 매개 변수가 동일`하다면 이것은 **오버로딩이 아니다.**

    - 리턴 타입은 JVM이 메소드를 선택할 때 아무런 도움을 주지 못하기 때문이다. 만약 아래와 같이 선언했다면 오버로딩이 아니기 때문에 컴파일 오류가 발생한다.

```java
int divide(int x, int y) { ... }
double divide(int boonja, int boonmo) { ... }
```

<br>

> 메소드 오버로딩의 대표적 예 : `System.out.println() 메소드`

- println() 메소드는 호출할 때 주어진 매개값의 타입에 따라서 오버로딩된 println() 메소드를 호출한다.

```java
void println() { .. }
void println(boolean x) { .. }
void println(char x) { .. }
void println(char[] x) { .. }
void println(double x) { .. }
void println(float x) { .. }
void println(int x) { .. }
void println(long x) { .. }
void println(Object x) { .. }
void println(String x) { .. }
```

<br>

> Calculator 클래스에 areaRectangle() 메소드를 오버로딩해서 매개값이 한 개면 정사각형의 넓이를, 두 개이면 직사각형의 넓이를 계산하여 리턴

```java
public class RectCalc {
    // 정사각형의 넓이
    double areaRectangle(double width) {
        return width * width;
    }

    // 직사각형의 넓이
    double areaRectangle(double width, double height) {
        return width * height;
    }
}
```

```java
public class RectCalcExample {
    public static void main(String[] args) {
        RectCalc myRectCalc = new RectCalc();

        // 정사각형 넓이 구하기
        double result1 = myRectCalc.areaRectangle(10);

        // 직사각형 넓이 구하기
        double result2 = myRectCalc.areaRectangle(10, 20);

        // 결과 출력
        System.out.println("정사각형 넓이=" + result1);
        System.out.println("직사각형 넓이=" + result2);
    }
}
```

<br>

## 인스턴스 멤버와 this

- `인스턴스 멤버` : 객체(인스턴스)를 생성한 후 사용할 수 있는 필드와 메소드

    - 이들을 각각 `인스턴스 필드`, `인스턴스 메소드` 라고 부른다.

- this는 주로 생성자와 메소드의 매개 변수 이름이 필드와 동일한 경우, **인스턴스 멤버인 필드임을 "명시"하고자 할 때 사용**된다.

> InstanceCar.java

```java
public class InstanceCar {
    // 필드
    String model;
    int speed;

    // 생성자
    InstanceCar(String model) {
        this.model = model;
    }

    void setSpeed(int speed) {
        this.speed = speed;
    }

    void run() {
        for (int i = 10; i <= 50; i += 10) {
            this.setSpeed(i);  // this 를 붙이지 않아도 동작한다. (명시용)
            System.out.println(this.model + "가 달립니다. (시속:" + this.speed + "km/h)");  // this 를 붙이지 않아도 동작한다. (명시용)
        }
    }
}
```

> InstanceCarExample.java

```java
public class InstanceCarExample {
    public static void main(String[] args) {
        InstanceCar myCar = new InstanceCar("포르쉐");
        InstanceCar yourCar = new InstanceCar("벤츠");

        myCar.run();
        yourCar.run();
    }
}
```

```
포르쉐가 달립니다. (시속:10km/h)
포르쉐가 달립니다. (시속:20km/h)
포르쉐가 달립니다. (시속:30km/h)
포르쉐가 달립니다. (시속:40km/h)
포르쉐가 달립니다. (시속:50km/h)
벤츠가 달립니다. (시속:10km/h)
벤츠가 달립니다. (시속:20km/h)
벤츠가 달립니다. (시속:30km/h)
벤츠가 달립니다. (시속:40km/h)
벤츠가 달립니다. (시속:50km/h)
```

<br>

## 정적 멤버와 static

- 정적(static)은 `고정된`이란 의미를 가지고 있다.

- **정적 멤버**는 클래스에 고정된 멤버로서 객체를 생성하지 않고 사용할 수 있는 필드와 메소드를 말한다.

    - 이들을 각각 `정적 필드` , `정적 메소드` 라고 한다.

- 정적 멤버는 객체(인스턴스)에 소속된 멤버가 아니라 클래스에 소속된 멤버이기 때문에 `클래스 멤버` 라고도 한다.

<br>

### 정적 멤버 선언

- 필드와 메소드 선언 시 `static` 키워드를 추가적으로 붙이면 된다.

```java
public class 클래스 {
    // 정적 필드
    static 타입 필드 [= 초기값];

    // 정적 메소드
    static 리턴 타입 메소드( 매개변수선언, ... ) { ... }
}
```

- 정적 필드와 정적 메소드는 클래스에 고정된 멤버이므로 클래스 로더가 클래스(바이트 코드)를 로딩해서 메소드 메모리 영역에 적재할 때 클래스별로 관리된다.

  - 따라서 클래스 로딩이 끝나면 바로 사용할 수 있다.

#### 필드 선언 시 static, instance 판단 기준

- **필드 선언 시** `인스턴스 필드`로 선언할 것인가, 아니면 `정적 필드`로 선언할 것인가의 **판단 기준**은 `객체마다 가지고 있어야 할 데이터`라면 `인스턴스 필드`로 선언하고, `객체마다 가지고 있을 필요성이 없는 공용적인 데이터`라면 `정적 필드`로 선언하는 것이 좋다.
  
  - 예로, Calculator 클래스에서 원의 넓이나 둘레를 구할 때 필요한 파이는 Calculator 객체마다 가지고 있을 필요가 없는 변하지 않는 공용적인 데이터이므로 정적 필드로 선언하는 것이 좋다.

  - 그러나 객체별로 색깔이 다르다면 색깔은 인스턴스 필드로 선언해야 한다.

```java
public class Calculator {
    String color;                   // 계산기별로 색이 다를 수 있다.
    static double pi = 3.14159;     // 계산기에서 사용되는 파이 값은 동일하다.
}
```

<br>

#### 메소드 선언 시 static, instance 판단 기준

- 인스턴스 필드를 이용해서 실행해야 한다면 인스턴스 메소드

- 인스턴스 필드를 이용하지 않는다면 정적 메소드로 선언한다.

    - 예를 들어 Calculator 클래스의 덧셈, 뺄셈 기능은 인스턴스 필드를 이용하기보다는 외부에서 주어진 매개값들을 가지고 수행하므로 정적 메소드로 선언하는 것이 좋다.

    - 그러나 인스턴스 필드인 색깔을 변경하는 메소드는 인스턴스 메소드로 선언해야 한다.

```java
public Calculator {
    String color;                                        // 인스턴스 필드
    void setColor(String color) { this.color = color; }  // 인스턴스 메소드
    static int plus(int x, int y) { return x + y; }      // 정적 메소드
    static int minus(int x, int y) { return x - y; }     // 정적 메소드
}
```

<br>

### 정적 멤버 사용

- 클래스가 메모리로 로딩되면 정적 멤버를 바로 사용할 수 있는데, 클래스 이름과 함께 도트 `.` 연산자로 접근한다.

```java
클래스.필드;
클래스.메소드( 매개값, ... );
```

- 예로 Calculator 클래스가 다음과 같이 작성되었다면,

```java
public class Calculator {
    static double pi = 3.14159;
    static int plus(int x, int y) { ... }
    static int minus(int x, int y) { ... }
}
```

- 정적 필드 pi와 정적 메소드 plus(), minus() 는 다음과 같이 사용할 수 있다.

```java
double result1 = 10 * 10 * Calculator.pi;
int result2 = Calculator.plus(10, 5);
int result3 = Calculator.minus(10, 5);
```

- 정적 필드와 정적 메소드는 원칙적으로는 클래스 이름으로 접근해야 하지만 다음과 같이 객체 참조 변수로도 접근이 가능하다.

```java
Calculator myCalc = new Calculator();
double result1 = 10 * 10 * myCalcu.pi;
int result2 = myCalcu.plus(10, 5);
int result2 = myCalcu.minus(10, 5);
```

- 하지만 정적 요소는 클래스 이름으로 접근하는 것이 좋다. 이클립스에서는 정적 멤버를 클래스 이름으로 접근하지 않고 객체 참조 변수로 접근했을 경우, 경고 표시( )가 나타난다.

<br>

> 정적 멤버 사용 예제

```java
public class StaticCalculator {
    static double pi = 3.14159;

    static int plus(int x, int y) {
        return x + y;
    }

    static int minus(int x, int y) {
        return x - y;
    }
}
```

```java
// 객체가 생성되지 않는다.

public class StaticCalculatorExample {
    public static void main(String[] args) {
        double result1 = 10 * 10 * StaticCalculator.pi;
        int result2 = StaticCalculator.plus(10, 5);
        int result3 = StaticCalculator.minus(10, 5);

        System.out.println("result1 : " + result1);
        System.out.println("result2 : " + result2);
        System.out.println("result3 : " + result3);
    }
}
```

<br>

### 정적 초기화 블록

- 정적 필드는 다음과 같이 필드 선언과 동시에 초기값을 주는 것이 보통이다.

```java
static double pi = 3.14159;
```

- 그러나 계산이 필요한 초기화 작업이 있을 수 있다.

- 인스턴스 필드는 생성자에서 초기화하지만, 정적 필드는 객체 생성 없이도 사용해야 하므로 생성자에게 초기화 작업을 할 수 없다.

    - 생성자는 객체 생성 시에만 실행되기 때문이다.

- 이런 정적 필드의 복잡한 초기화 작업을 위해 **정적 블록(static block)**을 제공한다.

```java
static {
    ...
}
```

- 정적 블록은 클래스가 메모리로 로딩될 때 자동 실행되며, 클래스 내부에 여러 개가 선언되어도 상관없다. 클래스가 메모리로 로딩될 때 선언된 순서대로 실행된다.

```java
public class Television {
    static String company = "Samsung";
    static String model = "LCD";
    static String info;

    static {
        info = company + "-" + model;
    }
}
```

```java
public class TelevisonExample {
    public static void main(String[] args) {
        System.out.println(Television.info);
    }
}
```

```
Samsung-LCD
```

<br>

### 정적 메소드와 블록 선언 시 주의할 점

- 정적 메소드와 정적 블록을 선언할 때 주의할 점은 객체가 없어도 실행된다는 특징때문에, 이들 내부에 인스턴스 필드나 인스턴스 메소드를 사용할 수 없다.

    - 또한 객체 자신의 참조인 this 키워드도 사용이 불가능하다.

- main 메소드도 동일한 규칙이 적용된다.

    - main() 메소드도 정적(static) 메소드이므로 객체 생성 없이 신스턴스 필드와 인스턴스 메소드를 main() 메소드에서 바로 사용할 수 없다.

<br>

### 싱글톤(Singleton)

- 전체 프로그램에서 **단 하나의 객체만 만들도록 보장**해야 하는 경우가 있다. 단 하나만 생성된다고 해서 이 객체를 싱글톤(Singleton)이라 한다.

- 싱글톤을 만들려면 **클래스 외부에서** new 연산자로 생성자를 호출할 수 없도록 막아야 한다. 생성자를 호출한만큼 객체가 생성되기 때문이다.

- `생성자를 외부에서 호출할 수 없도록` 하려면 **생성자 앞에 private 접근 제한자를 붙여주면 된다.**

- 참고로 클래스 내부에서는 new 연산자로 생성자 호출이 가능하다.

- 정적 필드도 private 접근 제한자를 붙여 외부에서 필드값을 변경하지 못하도록 막는다.
  
  - 대신 외부에서 호출할 수 있는 정적 메소드인 getInstance()를 선언하고 정적 필드에서 참조하고 있는 자신의 객체를 리턴해준다.


```java
public class 클래스 {
    // 정적 필드
    private static 클래스 singleton = new 클래스();

    // 생성자
    private 클래스() {}

    // 정적 메소드
    static 클래스 getInstance() {
        return singleton;
    }
}
```

- 외부에서 객체를 얻는 유일한 방법은 getInstance() 메소드를 호출하는 방법이다.
  
  - getInstance() 메소드는 단 하나의 객체만 리턴하기에 아래 코드에서 변수1과 변수2는 동일한 객체를 참조한다.


```java
클래스 변수1 = 클래스.getInstance();
클래스 변수2 = 클래스.getInstance();
```

<br>

> ### `EFFECTIVE JAVA, 아이템 3` : private 생성자나 열거 타입으로 '싱글턴'임을 보증하라


 



---

<br>

> 싱글톤 패턴의 문제점

- 싱글톤 인스턴스가 너무 많은 일을 하거나 많은 데이터를 공유시킬 경우, 다른 클래스의 인스턴스들 간에 결합도가 높아져 "개방-폐쇄 원칙(OCP)" 을 위배하게 된다. ( = 객체 지향 설계 원칙에 어긋난다.)

- 멀티쓰레드 환경에서 동기화 처리를 안하면 두 개가 생성되는 경우가 발생할 수 있다.

<br>

> 싱글톤 예제

```java
public class Singleton {
    private static Singleton singleton = new Singleton();

    private Singleton() {}

    static Singleton getInstance() {
        return singleton;
    }
}
```

```java
public class SingletonExample {
    public static void main(String[] args) {
        /*
        Singleton obj1 = new Sigleton();  // 컴파일 에러
        Singleton obj2 = new Sigleton();  // 컴파일 에러
         */

        Singleton obj1 = Singleton.getInstance();
        Singleton obj2 = Singleton.getInstance();

        if (obj1 == obj2) {
            System.out.println("같은 Singleton 객체 입니다.");
        } else {
            System.out.println("다른 Singleton 객체 입니다.");
        }
    }
}
```

```
같은 Singleton 객체 입니다.
```

<br>

## final 필드와 상수

### final 필드

- `final 필드`는 초기값이 저장되면 이것이 최종적인 값이 되어서 프로그램 실행 도중에 수정할 수 없다는 것이다.

```java
final 타입 필드 [= 초기값];
```

- **final 필드의 초기값을 줄 수 있는 방법**은 **딱 두 가지** 밖에 없다.

    1. 필드 선언 시에 주는 방법
    
    2. 생성자에서 주는 방법

        - 객체 생성 시에 외부 데이터로 초기화해야 한다면 생성자에서 초기값을 지정해야 한다.
        
        - 생성자는 final 필드의 최종 초기화를 마쳐야 하는데, 초기화하지 않은 채로 final 필드를 남겨두면 컴파일 에러가 난다.

<br>

```java
public class Person {
    final String nation = "Korea";
    final String ssn;
    String name;

    public Person(String ssn, String name) {
        this.ssn = ssn;
        this.name = name;
    }
}
```

```java
public class PersonExample {
    public static void main(String[] args) {
        Person p1 = new Person("900111-1111111", "계백");

        System.out.println(p1.nation);
        System.out.println(p1.ssn);
        System.out.println(p1.name);

        // p1.naton = "usa";  // final 필드는 값 수정 불가
        // p1.ssn = "801211-1211111"; // final 필드는 값 수정 불가
        p1.name = "을지문덕";
        System.out.println(p1.name);
    }
}
```

```
Korea
900111-1111111
계백
을지문덕
```

<br>

### 상수(static final)

- 불변의 값을 저장하는 필드 : 상수(constant)

- **final 필드도 한 번 초기화되면 수정할 수 없는 필드이지만, 상수라고 부르진 않는다!**

    - 왜냐하면 **불변의 값**은 **객체마다 저장할 필요가 없는 공용성**을 띄고 있으며, 여러 가지 값으로 초기화될 수 없기 때문이다.

    - final 필드는 객체마다 저장되고, 생성자의 매개값을 통해서 여러 가지 값을 가질 수 있기 때문에 상수가 될 수 없다.

- static final 필드는 객체마다 저장되지 않고, 클래스에만 포함된다. 그리고 한 번 초기값이 저장되면 변경할 수 없다.

```java
static final 타입 상수 [= 초기값];
```

- 초기값이 단순 값이라면 선언 시에 주는 것이 일반적이지만, 복잡한 초기화일 경우 **정적 블록에서도 할 수 있다.**

```java
static final 타입 상수;
static {
    상수 = 초기값;
}
```

- 상수 이름은 모두 대문자로 작성하는 것이 관례

- 만약 서로 다른 단어가 혼합된 이름이라면 언더바 `_` 로 단어를 연결

```java
static final double PI - 3.14159;
static final double EARTH_SURFACE_AREA;
```

<br>

## 패키지

- 패키지는 클래스를 유일하게 만들어주는 식별자 역할을 한다.

- 클래스 이름이 동일하더라도 패키지가 다르면 다른 클래스로 인식한다.

```java
상위패키지.하위패키지.클래스
```

- `패키지가 중요한 이유`는 **클래스만 따로 복사해서 다른 곳으로 이동하면 클래스는 사용할 수 없기 때문**이다.

### 패키지 선언

- 패키지는 클래스를 컴파일하는 과정에서 자동적으로 생성되는 폴더이다.

- 컴파일러는 클래스에 포함되어 있는 패키지 선언을 보고, 파일 시스템의 폴더도 자동 생성시킨다.

> 패키지 선언 방법

```java
package 상위패키지.하위패키지;
public class ClassName { ... }
```

- 패키지 이름은 개발자가 임의대로 지어주면 되지만, 여기에도 지켜야 할 몇 가지 규칙이 있다.

    - 숫자로 시작해서는 안되고 `_` , `$` 를 제외한 특수 문자를 사용해서는 안된다.

    - java로 시작하는 패키지는 자반 표준 API에서만 사용하므로 사용해서는 안 된다.

    - **모두 소문자**로 작성하는 것이 관례이다.

    - 여러 개발 회사가 함께 참여하는 대규모 프로젝트나, 다른 회사의 패키지를 이용해서 개발할 경우, 회사들 간에 패키지가 중복되지 않도록 흔히 회사의 도메인 이름을 역순으로 패키지 이름을 지어준다.

      - 포괄적인 이름이 상위 패키지가 되도록 하기 위해서이다.

```java
com.samsung.projectname
com.hyundai.projectname
com.nexon.projectname
org.apache.projectname
```

<br>

### import문

- 같은 패키지에 속하는 클래스들은 아무런 조건 없이 다른 클래스를 사용할 수 있지만, **다른 패키지에 속하는 클래스를 사용하려면 '두 가지 방법' 중 하나를 선택**해야 한다.

> 패키지와 클래스 모두 기술

```java
package com.mycompany;

public class Car {
    com.hankook.Tire tire = new com.hankook.Tire();
}
```

> import 문

```java
package com.mycompany;

import com.hankook.Tire;
// 또는 import.com.hankook.*;

public class Car {
    Tire tire = new Tire();
}

```

<br>

## 접근 제한자(Access Modifier)

> `접근 허용도` : public > protected > default > private

|지시자|클래스 내부|동일 패키지|상속받은 클래스|이외의 영역(남남)|
|------|:------:|:------:|:------:|:------:|
|private|●|×|×|×|
|default|●|●|×|×|
|protected|●|●|●|×|
|public|●|●|●|●|

<br>

### 클래스의 접근 제한 : `public`, `default` 

- 클래스를 선언할 때 고려해야 할 사항
  
  - 같은 패키지 내에서만 사용할 것인가.

  - 다른 패키지에서도 사용할 수 있도록 할 것인가.

- 클래스에 적용할 수 있는 접근 제한은 `public` 과 `defalut` 단 두 가지.

```
// default 접근 제한 : 다른 패키지 사용 불가
class 클래스 { ... }

// public 접근 제한 : 다른 패키지 사용 가능
public 클래스 { ... }
```

#### public 접근 제한

- 클래스를 다른 개발자가 사용할 수 있도록 라이브러리 클래스로 개발되어야 한다면, 반드시 public 접근 제한을 같도록 해야 한다.

- 인터넷으로 배포되는 라이브러리 클래스들도 모두 public 접근 제한을 가지고 있다.


<br>

### 생성자의 접근 제한 : `public`, `protected`, `defalut`, `private`

- 생성자를 어디에서나 호출할 수 있는 건 아니다.

- 생성자가 어떤 접근 제한을 갖는냐에 따라 호출 가능 여부가 결정된다.

```java
public class ClassName {
    public ClassName(...) { ... }

    protected ClassName(...) { ... }

    ClassName(...) { ... }  // 다른 패키지에서는 생성자 호출 불가

    private ClassName(...) { ... }  // 오직 클래스 내부에서만 생성자 호출, 객체 생성
}
```

- 클래스에 생성자를 선언하지 않으면 컴파일러에 의해 자동적으로 기본 생성자가 추가된다.

    - 자동으로 생성되는 기본 생성자의 접근 제한은 클래스의 접근 제한과 동일하다.

    - 클래스가 default 접근 제한을 가지면 생성자도 default, 클래스가 public 접근 제한을 가지면 기본 생성자도 public 접근 제한을 가진다.

- **싱글톤 패턴** : 생성자를 `private` 접근 제한으로 선언하고, 자신의 유일한 객체를 리턴하는 `getInstance() 정적 메소드`를 선언한다.

<br>

### 필드와 메소드의 접근 제한 : `public`, `protected`, `defalut`, `private`

- 필드와 메소드를 선언할 때 고려해야 할 사항

    - 클래스 내부에서만 사용할 것인지.

    - 패키지 내에서만 사용할 것인지.

    - 다른 패키지에서도 사용할 수 있돌고 할 것인지.



<br>

## Getter와 Setter 메소드

- 일반적으로 객체 지향 프로그래밍에서 객체의 데이터는 **객체 외부에서 직접적으로 접근하는 것을 막는다.**

    - 객체의 데이터를 외부에서 마음대로 읽고 변경할 경우 **객체의 무결성(결점이 없는 성질)이 깨어질 수 있기 때문**이다.

- 이러한 문제점을 해결하기 위해 객체 지향 프로그래밍에서는 메소드를 통해서 데이터를 변경하는 방법을 선호한다.

    - **데이터는 외부에서 접근할 수 없도록 막고 메소드는 공개해서 외부에서 메소드를 통해 데이터에 접근하도록 유도**한다. 메소드는 매개값을 검증해서 유효한 값만 데이터로 저장할 수 있기 때문이다. 이러한 역할을 하는 메소드가 `Setter`이다.

> Setter 메소드

```java
void setSpeed(double speed) {
    if (speed < 0) {
        this.speed = 0;
        return;     // void 타입 메소드 return; 시, 프로그램 종료
    } else {
        this.speed = speed;
    }
}
```

- 외부에서 객체의 데이터를 읽을 때도 메소드를 사용하는 것이 좋다.

- 객체 외부에서 개겣의 필드값을 사용하기에 부적절한 경우도 있다. 이런 경우에는 **메소드로 필드값을 가공한 후 외부로 전달**하면 된다. 이런 메소드가 바로 `Getter`이다.


> Getter 메소드

```java
// 필드값인 마일을 km 단위로 환산 후 외부로 리턴
double getSpeed() {
    double km = speed * 1.6;
    return km; 
}
```

<br>

- ***클래스를 선언할 때*** 가능하다면 `필드를 private로 선언`해서 **외부로부터 보호**하고, `필드에 대한 Setter와 Getter 메소드를 작성`해서 **필드값을 안전하게 변경/사용**하는 것이 좋다.

> Setter와 Getter 메소드를 선언하는 방법

```java
private 타입 fieldName;  // 필드 접근 제한자 : private

// Getter 값 얻어오기
public 리턴 타입 getFieldName() {
    return fieldName;
}

// Setter 값 설정하기
public void setFieldName(타입 fieldName) {
    this.fieldName = fieldName;
}
```

- 필드 타입이 **boolean** 일 경우에는 Getter는 `get`으로 시작하지 않고 `is`로 시작하는 것이 관례이다.

<br>

> Getter, Setter 메소드 선언

```java
public class Car {
    // 필드
    private int speed;
    private boolean stop;

    // 생성자

    // 메소드
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        if (speed < 0) {
            this.speed = 0;
            return;
        } else {
            this.speed = speed;
        }
    }


    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
        this.speed = 0;
    }
}
```

> Gette, Setter 메소드 사용

```java
public class CarExample {
    public static void main(String[] args) {
        Car myCar = new Car();

        // 잘못된 속도 변경(비정상적인 속도값 변경) → Setter(setSpeed()) 에서 매개값 검사 후 0으로 변경
        myCar.setSpeed(-50);

        System.out.println("현재 속도: " + myCar.getSpeed());

        // 올바른 속도 변경
        myCar.setSpeed(60);

        // 멈춤
        if (!myCar.isStop()) {
            myCar.setStop(true);
        }

        System.out.println("현재 속도: " + myCar.getSpeed());
    }
}
```

<br>

## 어노테이션(Annotation)

- 사전적 의미론 `주석`

- 어노테이션(Annotation)은 메타데이터(metadata)라고 볼 수 있다.

    - `메타데이터` : 애플리케이션이 처리해야 할 데이터가 아니라, 컴파일 과정과 실행 과정에서 코드를 어떻게 컴파일하고 처리할 것인지를 알려주는 정보

```java
@AnnotationName
```

- 어노테이션의 세 가지 사용 용도

    1. 컴파일러에게 코드 문법 에러를 체크하도록 정보를 제공
    
    2. 소프트웨어 개발 툴이 빌드나 배치 시 코드를 자동으로 생성할 수 있도록 정보를 제공
    
    3. 실행 시 (런타임 시) 특정 기능을 실행하도록 정보를 제공

<br>

### 어노테이션 타입 정의와 적용

> 어노테이션의 정의

```java
public @interface AnnotationName {
}
```

> 어노테이션의 사용

```java
@AnnotationName
```

> 어노테이션의 엘리먼트(element) 멤버

- 어노테이션은 엘리먼트를 멤버로 가질 수 있다.

- 각 엘리먼트는 타입과 이름으로 구성되며, 디폴트 값을 가질 수 있다.

```java
public @interface AnnotationName {
    타입 elementName() [default 값];  // 엘리먼트 선언
    ...
}
```

- 엘리먼트의 타입으로는 int나 double과 같은 기본 데이터 타입이나 String, 열거 타입, Class 타입, 그리고 이들의 배열 타입을 사용할 수 있다.

- 엘리먼트의 이름 뒤에는 메소드를 작성하는 것처럼 `( )`를 붙여야 한다.

<br>

> String 타입의 엘리먼트와 int 타입의 엘리먼트 선언 예시

```java
public @interface AnnotationName {
    String elementName1();
    int elementName2() default 5;
}
```

> 위에서 정의한 어노테이션을 코드에 적용할 때

```java
@AnnotationName(elementName1 = "값", elementName2 = 3);

또는

@AnnotationName(elementName1 = "값");
```

- elementName1은 디폴트 값이 없기 때문에 반드시 값을 기술해야 하고, elementName2는 **디폴트 값이 있기 때문에 생략이 가능**하다.

> 어노테이션은 기본 엘리먼트인 value를 가질 수 있다.

```java
public @interface AnnotationName {
    String value();  // 기본 엘리먼트 선언
    int elementName() default 5;
}
```

- value 엘리먼트를 가진 어노테이션을 코드에서 적용할 때에는 다음과 같이 값만 기술할 수 있다.

```java
@AnnotationName("값"):
```

- 만약 value 엘리먼트와 다른 엘리먼트의 값을 동시에 주고 싶다면 다음과 같이 정상적인 방법으로 지정하면 된다.

```java
@AnnotationName(value = "값", elementName = 3);
```

<br>

### 어노테이션 적용 대상

- 어노테이션을 적용할 수 있는 대상은 `java.lang.annotation.ElementType 열거 상수`로 다음과 같이 정의되어 있다.

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55142141-43b2d280-517f-11e9-91f7-2b13090e2942.png'>
</p>

<br>

- 어노테이션이 적용될 대상을 지정할 때에는 **@Target 어노테이션**을 사용한다.
  
- @Target의 기본 엘리먼트인 value는 ElementType 배열을 값으로 가진다.

    - 이것은 어노테이션이 적용될 대상을 여러 개로 지정하기 위해서이다.

> 다음과 같이 어노테이션을 정의할 경우

```java
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface AnnotationName {
}
```

> 다음과 같이 클래스, 필드, 메소드만 어노테이션을 적용할 수 있고 생성자는 적용할 수 없다.

```java
@AnnotationName
public class ClassName {
    @AnnotationName
    private String fieldName;

    // @AnnotationName  (x) → @Target에 CONSTRUCT가 없어 생성자는 적용 못함
    public ClassName() {}

    @AnnotationName
    public void methodName() {}
}
```

<br>

### 어노테이션 유지 정책

- 어노테이션 정의 시 한 가지 더 추가해야 할 내용은 사용 용도에 따라 @AnnotationName을 **어느 범위까지 유지할 것인지 지정**해야 한다.

    - 쉽게 말해, 소스상에서만 유지할 것인지, 컴파일된 클래스까지 유지할 건지, 런타임 시에도 유지할 건지를 지정해야 한다.

- 어노테이션 유지 정책은 `java.lang.annotation.RetentionPolicy 열거 상수`로 다음과 같이 정의되어 있다.  (`Retention : 보유`)

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55142139-43b2d280-517f-11e9-94c9-4f7ba769d418.png'>
</p>

<br>

- **리플렉션(Reflection)** : 런타임 시에 클래스의 메타 정보를 얻는 기능

    - 예를 들어 클래스가 어떤 필드, 생성자, 메소드, 어노테이션을 가지고 있거나 적용이 되었는지 알아내는 것이 리플렉션이다.

    - 리플렉션을 이용해서 런타임 시에 어노테이션 정보를 얻으려면 어노테이션 유지 정책을 `RUNTIME`으로 설정해야 한다.

- 어노테이션 유지 정책을 지정할 때는 **@Retention** 어노테이션을 사용한다.

- @Retention의 기본 엘리먼트인 value는 RetentionPolicy 타입이므로 위 세 가지 상수 중 하나를 지정하면 된다.

```java
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationName {
}
```

<br>

### 런타임 시 어노테이션 정보 사용하기

- 런타임 시에 어노테이션이 적용되었는지 확인하고 엘리먼트 값을 이용해서 특정 작업을 수행하는 방법에 대해 알아보자.

- **어노테이션 자체는 아무런 동작을 가지지 않는 단지 표식일 뿐**이지만, **리플렉션을 이용**해서 어노테이션의 적용 여부와 엘리먼트 값을 읽고 적절히 처리할 수 있다.

    - 클래스에 적용된 어노테이션 정보를 얻으려면 java.lang.Class를 이용하면 되지만 `필드, 생성자, 메소드` 에 적용된 어노테이션 정보를 얻으려면 Class의 다음 메소드를 통해서 java.lang.reflect 패키지의 **Field, Constructor, Method 타입의 배열**을 얻어야 한다.

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55142137-431a3c00-517f-11e9-8f19-40a92c9c1468.png'>
</p>

<br>

- 그런 다음 Class, Field, Constructor, Method가 가지고 있는 다음 메소드를 호출해서 적용된 어노테이션 정보를 얻을 수 있다.


<br>

#### 어노테이션과 리플렉션을 이용한 예제

> 각 메소드의 실행 내용을 구분선으로 분리하여 콘솔에 출력하는 PrintAnnotation

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrintAnnotation {
    String value() default "-";

    int number() default 15;
}
```

> printAnnotation을 적용한 Service 클래스

```java
public class Service {
    @PrintAnnotation
    public void method1() {
        System.out.println("실행 내용1");
    }

    @PrintAnnotation("*")
    public void method2() {
        System.out.println("실행 내용2");
    }

    @PrintAnnotation(value = "#", number = 20)
    public void method3() {
        System.out.println("실행 내용3");
    }
}
```

> 리플렉션을 이용해서 Service 클래스에 적용된 어노테이션 정보를 읽고 엘리먼트 값에 따라 출력할 문자와 출력 횟수를 콘솔에 출력한 후, 해당 메소드를 호출

- method.invoke(new Service()) 는 Service 객체를 생성하고 생성된 Service 객체의 메소드를 호출하는 코드이다.

```java
import java.lang.reflect.Method;

public class PrintAnnotationExample {
    public static void main(String[] args) {
        // Service 클래스로부터 메소드 정보를 얻음
        Method[] declaredMethods = Service.class.getDeclaredMethods();  // Service 클래스에 선언된 메소드 얻기(리플렉션)

        // Method 객체를 하나씩 처리
        for (Method method : declaredMethods) {
            // PrintAnnotation이 적용되었는지 확인
            if (method.isAnnotationPresent(PrintAnnotation.class)) {
                //printAnnotation 객체 얻기
                PrintAnnotation printAnnotation = method.getAnnotation(PrintAnnotation.class);

                // 메소드 이름 출력
                System.out.println("[" + method.getName() + "] ");
                // 구분선 출력
                for (int i = 0; i < printAnnotation.number(); i++) {
                    System.out.print(printAnnotation.value());
                }
                System.out.println();

                try {
                    // 메소드 호출
                    method.invoke(new Service());
                } catch (Exception e) { }
                System.out.println();
            }
        }
    }
}
```

```
[method2] 
***************
실행 내용2

[method1] 
---------------
실행 내용1

[method3] 
####################
실행 내용3
```
