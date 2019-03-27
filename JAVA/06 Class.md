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

        if (myGas.isLeftGas()) {
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

