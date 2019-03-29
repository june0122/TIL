# 챕터 07. 상속(Inheritance)

## 상속 개념

> 부모 클래스 (상위 클래스)

```java
public class A {
    int field1;
    void method1() { ... }
}
```

> 자식 클래스 (하위 클래스, 파생 클래스)

```java
public class B extends A {
    int field2;
    void method2() { ... }
}
```

```java
B b = new B();

// A로부터 물려받은 필드와 메소드
b.field1 = 10;
b.method1();

// B가 추가한 필드와 메소드
b.field2 = "홍길동"
method2();
```

- 상속을 해도 부모 클래스의 모든 필드와 메소드들을 물려받는 것은 아니다.

    - 부모 클래스에서 **private** 접근 제한을 갖는 필드와 메소드는 상속 대상에서 제외된다.

    - 그리고 부모 클래스와 자식 클래스가 **다른 패키지**에 존재한다면 **default** 접근 제한을 갖는 필드와 메소드도 상속 대상에서 제외된다.

    - 그 이외의 경우는 모두 상속의 대상이 된다.

- 상속을 이용하면 클래스의 수정을 최소화시킴으로써 유지 보수 시간을 최소화시켜준다.

<br>

## 클래스 상속

- 프로그램에서느 *자식이 부모를 선택* 한다.

- 자식 클래스를 선언할 때 어떤 부모 클래스를 상속받을 것인지를 결정하고 부모 클래스는 다음과 같이 **extends** 뒤에 기술한다.

```java
class 자식클래스 extends 부모클래스 {
    // 필드
    // 생성자
    // 메소드
}
```

- 다른 언어와 달리 **자바는 다중 상속을 허용하지 않는다.**

    - extends 뒤에는 단 하나의 부모 클래스만 와야 한다.

<br>

## 부모 생성자 호출

- 현실에서 부모 없는 자식이 있을 수 없듯이 자바에서도 **자식 객체를 생성하면, 부모 객체가 먼저 생성되고 자식 객체가 그 다음에 생성**된다.

```java
DmbCellPhone dmbCellPhone = new DmbCellPhone();
```

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55210123-541f8780-5229-11e9-9197-cd161398cd8c.png'>
</p>
<br>

- 모든 객체는 클래스의 생성자를 호출해야만 생성된다. 부모 객체도 예외는 아니다. 부모 생성자는 자식 생성자의 맨 첫 줄에서 호출된다.

- 예로 DmbCellPhone(자식) 클래스의 생성자가 명시적으로 선언되지 않았다면 컴파일러는 다음과 같은 기본 생성자를 생성해 낸다..

```java
public DmbCellPhone() {
    super();
}
```

- 첫 줄에 **super();**가 추가된 것을 볼 수 있다.

- super() 는 **부모의 기본 생성자를 호출**한다. 즉 Cellphone(부모) 클래스의 다음 생성자를 호출한다.

```java
public CellPhone() {

}
```

- 만약 직접 자식 생성자를 선언하고 명시적으로 부모 생성자를 호출하고 싶다면 다음과 같이 작성하면 된다.

```java
자식클래스( 매개변수선언, ... ) {
    super( 매개값, ... );
    ...
}
```

- `super( 매개값, ... )` 는 **매개값의 타입과 일치하는 부모 생성자를 호출**한다.

- *부모 클래스에 기본 생성자가 없고 매개 변수가 있는 생성자만 있다면* 자식 생성자에서 반드시 부모 생성자 호출을 위해 `super( 매개값, ... )` 을 **명시적으로 호출**해야 한다.

- `super( 매개값, ... )` 는 반드시 자식 생성자 첫 줄에 위치해야 한다. 그렇지 않으면 컴파일 에러가 난다.

> People 부모 클래스

```java
public class People {
    public String name;
    public String ssn;

    public People(String name, String ssn) {
        this.name = name;
        this.ssn = ssn;
    }
}
```

- people 클래스는 기본 생성자가 없고 name과 ssn을 매개값으로 받아 객체를 생성시키는 생성자만 있다.

    - 그렇기 때문에 People을 상속하는 Student 클래스는 생성자에서 super(name, ssn)으로 Person 클래스의 생성자를 호출해야 한다.

> Student 자식 클래스

```java
public class Student extends People {
    public int studentNo;

    public Student(String name, String ssn, int studentNo) {
        super(name, ssn);       // 생성자의 첫 줄에 부모 생성자 호출
        this.studentNo = studentNo;
    }
}

```

- 5번째 라인의 `super(name, ssn)`은 People 생성자인 `People(String name, String ssn)`을 호출한다.

> StudentExample 자식 객체 이용

```java
public class StudentExample {
    public static void main(String[] args) {
        Student student = new Student("홍길동", "123456-1234567", 1);
        // 부모에게서 물려받은 필드 출력
        System.out.println("name : " + student.name);
        System.out.println("ssn : " + student.ssn);

        // 자식에 있는 필드
        System.out.println("studentNo : " + student.studentNo);
    }
}

```

<br>

## 메소드 재정의

- 부모 클래스의 메소드는 자식 클래스가 사용하기에 적합하지 않은 경우가 있을 수 있다.

- 이 경우 일부 메소드는 자식 클래스에서 다시 수정해서 사용해야 한다.

- 자바는 이런 경우를 위해 **메소드 오버라이딩(Overriding)** 기능을 제공한다.

### 메소드 재정의(@Override)

- 메소드 오버라이딩은 상속된 메소드의 내용이 자식 클래스에 맞지 않을 경우, **자식 클래스에서 동일한 메소드를 재정의하는 것**을 말한다.

- 메소드가 오버라이딩되었다면 부모 객체의 메소드는 숨겨지기 때문에, 자식 객체에서 메소드를 호출하면 오버라이딩된 자식 메소드가 호출된다.

<br>

- 메소드를 오버라이딩할 때는 다음과 같은 규칙에 주의해서 작성한다

    - 부모의 메소드와 동일한 시그너처(리턴 타입, 메소드 이름, 매개 변수 리스트)를 가져야 한다.

    - 접근 제한을 더 강하게 오버라이딩할 수 없다.

        - 부모 메소드가 public 접근 제한을 가지고 있을 경우 오버라이딩하는 자식 메소드는 default나 private 접근 제한으로 수정할 수 없다는 뜻이다.

        - 반대는 가능하다.

    - 새로운 예외(Exception)를 throws할 수 없다.

> Calculator, 부모 클래스

```java
public class Calculator {
    double areaCircle(double r) {
        System.out.println("Calculator 객체의 areaCircle() 실행");
        return 3.14159 * r * r;
    }
}
```

> Computer, 자식 클래스

```java
public class Computer extends Calculator {
    @Override
    double areaCircle(double r) {
        System.out.println("Computer 객체의 areaCircle() 실행");
        return Math.PI * r * r;
    }
}
```

- 2번째 라인의 `@Override` 어노테이션은 생략해도 좋으나, 이것을 붙여주게 되면 areaCirlce() 메소드가 정확히 오버라이딩된 것인지 컴파일러가 체크하기 때문에 *개발자의 실수를 줄여준다.*

    - 예를 들어 개발자가 `e` 를 빼먹어 `areaCircl()` 과 같이 작성하면 컴파일 에러가 난다.

> ComputerExample, 메소드 오버라이딩 테스트

```java
public class ComputerExample {
    public static void main(String[] args) {
        int r = 10;

        Calculator calculator = new Calculator();
        System.out.println("원면적 : " + calculator.areaCircle(r));
        System.out.println();

        Computer computer = new Computer();
        System.out.println("원면적 : " + computer.areaCircle(r));  // 재정의된 메소드 호출
    }
}
```

<br>

### 부모 메소드 호출(super)

- 자식 클래스에서 부모 클래스의 메소드를 오버라이딩하게 되면, *부모 클래스의 메소드는 숨겨지고* 오버라이딩된 자식 메소드만 사용된다.

- 그러나 자식 클래스 내부에서 오버라이딩된 부모 클래스의 메소드를 호출해야 하는 상황이 발생한다면 명시적으로 super 키워드를 붙여서 부모 메소드를 호출할 수 있다.

    - super는 부모 개체를 참조하고 있기 때문에 부모 메소드에 직접 접근할 수 있다.

```java
super.부모메소드();
```

> Airplane

```java
public class Airplane {
    public void land() {
        System.out.println("착륙합니다.");
    }

    public void fly() {
        System.out.println("일반비행합니다.");
    }

    public void takeOff() {
        System.out.println("이륙합니다.");
    }
}
```

> SupersonicAirplane

```java
public class SupersonicAirplane extends Airplane {
    public static final int NORMAL = 1;
    public static final int SUPERSONIC = 2;

    public int flyMode = NORMAL;

    @Override
    public void fly() {
        if (flyMode == SUPERSONIC) {
            System.out.println("초음속비행입니다.");
        } else {
            // Airplane 객체의 fly() 메소드 호출
            super.fly();
        }
    }
}
```

> SupersonicAirplaneExample

```java
public class SupersonicAirplaneExample {
    public static void main(String[] args) {
        SupersonicAirplane sa = new SupersonicAirplane();
        sa.takeOff();
        sa.fly();
        sa.flyMode = SupersonicAirplane.SUPERSONIC;
        sa.fly();
        sa.flyMode = SupersonicAirplane.NORMAL;
        sa.fly();
        sa.land();
    }
}
```

<br>

## final 클래스와 final 메소드

- `final` 키워드는 클래스, 필드, 메소드 선언 시에 사용할 수 있다.

- final 키워드는 해당 선언이 최종 상태이고, **결코 수정될 수 없음**을 뜻한다.

- `클래스와 메소드 선언 시에 final 키워드가 지정`되면 **상속과 관련**이 있다.

<br>

### 상속할 수 없는 final 클래스

- **클래스**를 선언할 때 `final` 키워드를 class 앞에 붙이게 되면 이 클래스는 최종적인 클래스이므로 **상속할 수 없는 클래스**가 된다.

    - 즉 final 클래스는 부모 클래스가 될 수 없어 자식 클래스를 만들 수 없다.

- final 클래스의 대표적인 예는 `자바 표준 API에서 제공하는 String 클래스`이다.

<br>

### 오버라이딩할 수 없는 final 메소드

- **메소드**를 선언할 때 `final` 키워드를 붙이게 되면 이 메소드는 최종적인 메소드이므로 **오버라이딩(Overriding)할 수 없는 메소드**가 된다.
  
    - 즉 부모 클래스를 상속해서 자식 클래스를 선언할 때 부모 클래스에 선언된 final 메소드는 자식 클래스에서 재정의할 수 없다는 것이다.

<br>

## protected 접근 제한자

> `접근 허용도` : public > protected > default > private

|지시자|클래스 내부|동일 패키지|상속받은 클래스|이외의 영역(남남)|
|------|:------:|:------:|:------:|:------:|
|private|●|×|×|×|
|default|●|●|×|×|
|protected|●|●|●|×|
|public|●|●|●|●|

- **protected**는 `public`과 `default` 접근 제한의 중간쯤에 해당한다.

- 같은 패키지에서는 default와 같이 접근 제한이 없지만, **다른 패키지에서는 자식 클래스만 접근을 허용한다.**

- `protected`는 필드와 생성자, 메소드 선언에 사용될 수 있다.

> A , 부모 클래스

```java
package package1;

public class A {
    protected String field;

    protected A() {
    }

    protected void method() {
    }
}
```

> B , 동일 패키지 클래스

```java
package package1;

public class B {
    public void method() {
        A a = new A();
        a.field = "value";
        a.method();
    }
}
```

> D , 다른 패키지의 자식 클래스

```java
package package2;
import package1.A;

public class D extends A {
    public D() {
        super();    // new 연산자로 생성자 직접 호출 불가, super() 사용해야함
        this.field = "value";
        this.method();
    }
}
```

<br>

## 타입 변환과 다형성

- ***다형성*** : 같은 타입이지만 실행 결과가 다양한 객체를 이용할 수 있는 성질

- 코드 측면에서 보면 다형성은 하나의 타입에 여러 객체를 대입함으로써 다양한 기능을 이용할 수 있도록 해준다.

- 다형성을 위해 자바는 부모 클래스로 타입 변환을 허용한다.

    - 즉 **부모 타입에 모든 자식 객체가 대입될 수 있다.**

- 이것을 이용하면 객체는 **부품화**가 가능하다.

    - 예로 자동차를 설계할 때 타이어 클래스 타입을 적용했다면 이 클래스를 상속한 실제 타이어들은 어떤 것이든 상관없이 장착(대입)이 가능하다.

> 자식 타입 객체 대입

```java
public class Car {
    Tire t1 = new HankookTire();
    Tire t2 = new KumhooTire();
}
```

- 클래스 타입도 마찬가지로 타입 변환이 있는데, **클래스 타입의 변환은 상속 관계애 있는 클래스 사이에서 발생**한다.

    - 자식 타입은 부모 타입으로 자동 타입 변환이 가능하다.

<br>

### 자동 타입 변환(Promotion)

- 부모 타입으로 자동 변환된 이후에는 **부모 클래스에 선언된 필드와 메소드만 접근이 가능하다.**
  
- 그러나 예외가 있는데, 메소드가 자식 클래스에서 오버라이딩되었다면 자식 클래스의 메소드가 대신 호출된다.

<br>

### 필드의 다형성

> Tire, 타이어 클래스

```java
public class Tire {
    // 필드
    public int maxRotation;  // 최대 회전수(타이어 수명)
    public int accumulatedRotation;  // 누적 회전수
    public String location;  // 타이어의 위치

    // 생성자
    public Tire(String location, int maxRotation) {
        this.location = location;
        this.maxRotation = maxRotation;
    }

    // 메소드
    public boolean roll() {
        ++accumulatedRotation;
        if (accumulatedRotation < maxRotation) {
            System.out.println(location + " Tire 수명: " + (maxRotation - accumulatedRotation) + "회");
            return true;
        } else {
            System.out.println("*** " + location + " Tire 펑크 ***");
            return false;
        }
    }
}
```

> Car, Tire를 부품으로 가지는 클래스

```java
public class Car {
    // 필드
    Tire frontLeftTire = new Tire("앞왼쪽", 6);
    Tire frontRightTire = new Tire("앞오른쪽", 2);
    Tire backLeftTire = new Tire("뒤왼쪽", 3);
    Tire backRightTire = new Tire("뒤오른쪽", 4);
    
    // 생성자
    // 메소드
    int run() {
        System.out.println("[자동차가 달립니다.]");
        if (frontLeftTire.roll() == false) { stop(); return 1; }
        if (frontRightTire.roll() == false) { stop(); return 2; }
        if (backLeftTire.roll() == false) { stop(); return 3; }
        if (backRightTire.roll() == false) { stop(); return 4; }
        return 0;
    }
    
    void stop() {
        System.out.println("[자동차가 멈춥니다.]");
    }
}
```

> HankookTire, Tire의 자식 클래스

```java
public class HankookTire extends Tire {
    // 필드
    // 생성자
    public HankookTire(String location, int maxRotation) {
        super(location, maxRotation);
    }

    // 메소드
    @Override
    public boolean roll() {
        ++accumulatedRotation;
        if (accumulatedRotation < maxRotation) {
            System.out.println(location + "HankookTire 수명: " + (maxRotation - accumulatedRotation) + "회");
            return true;
        } else {
            System.out.println("*** " + location + " HankookTire 펑크 ***");
            return false;
        }
    }
}
```

> KumhoTire, Tire의 자식 클래스

```java
public class KumhoTire extends Tire {
    // 필드
    // 생성자
    public KumhoTire(String location, int maxRotation) {
        super(location, maxRotation);
    }

    // 메소드
    @Override
    public boolean roll() {
        ++accumulatedRotation;
        if (accumulatedRotation < maxRotation) {
            System.out.println(location + "KumhoTire 수명: " + (maxRotation - accumulatedRotation) + "회");
            return true;
        } else {
            System.out.println("*** " + location + " KumhoTire 펑크 ***");
            return false;
        }
    }
}
```

> CarExample, 실행 클래스

```java
public class CarExample {
    public static void main(String[] args) {
        Car car = new Car();

        for (int i = 1; i <= 5; i++) {
            int problemLocation = car.run();

            switch (problemLocation) {
                case 1:
                    System.out.println("앞왼쪽 HankookTire로 교채");
                    car.frontLeftTire = new HankookTire("앞왼쪽", 15);
                    break;
                case 2:
                    System.out.println("앞오른쪽 KumhoTire로 교채");
                    car.frontRightTire = new KumhoTire("앞오른쪽", 13);
                    break;
                case 3:
                    System.out.println("뒤왼쪽 HankookTire로 교채");
                    car.backLeftTire = new HankookTire("뒤왼쪽", 14);
                    break;
                case 4:
                    System.out.println("뒤오른쪽 KumhoTire로 교채");
                    car.backRightTire = new KumhoTire("뒤오른쪽", 17);
                    break;
            }
            System.out.println("---------------------------------------------");
        }
    }
}
```

```
[자동차가 달립니다.]
앞왼쪽 Tire 수명: 5회
앞오른쪽 Tire 수명: 1회
뒤왼쪽 Tire 수명: 2회
뒤오른쪽 Tire 수명: 3회
---------------------------------------------
[자동차가 달립니다.]
앞왼쪽 Tire 수명: 4회
*** 앞오른쪽 Tire 펑크 ***
[자동차가 멈춥니다.]
앞오른쪽 KumhoTire로 교채
---------------------------------------------
[자동차가 달립니다.]
앞왼쪽 Tire 수명: 3회
앞오른쪽 KumhoTire 수명: 12회
뒤왼쪽 Tire 수명: 1회
뒤오른쪽 Tire 수명: 2회
---------------------------------------------
[자동차가 달립니다.]
앞왼쪽 Tire 수명: 2회
앞오른쪽 KumhoTire 수명: 11회
*** 뒤왼쪽 Tire 펑크 ***
[자동차가 멈춥니다.]
뒤왼쪽 HankookTire로 교채
---------------------------------------------
[자동차가 달립니다.]
앞왼쪽 Tire 수명: 1회
앞오른쪽 KumhoTire 수명: 10회
뒤왼쪽 HankookTire 수명: 13회
뒤오른쪽 Tire 수명: 1회
---------------------------------------------
```

<br>

### 하나의 배열로 객체 관리

- 이전 예제에서 Car 클래스에 4개의 타이어 객체를 4개의 필드로 각각 저장했다.

    - 우리는 동일한 타입의 값들은 배열로 관리하는 것이 유리하다는 것을 알고 있다. 타이어 객체들을 타이어 배열로 관리해보자.

> 타이어 객체들을 4개의 필드로 저장

```java
public class Car {
    Tire frontLeftTire = new Tire("앞왼쪽", 6);
    Tire frontRightTire = new Tire("앞오른쪽", 2);
    Tire backLeftTire = new Tire("뒤왼쪽", 3);
    Tire backRightTire = new Tire("뒤오른쪽", 4);
}
```

> 타이어 객체들을 배열로 저장

```java
public class Car {
    Tire[] tires = {
        new Tire("앞왼쪽", 6),
        new Tire("앞오른쪽", 2),
        new Tire("뒤왼쪽", 3),
        new Tire("뒤오른쪽", 4)
    };
}
```

- `frontLeftTire`는 tires[0], `frontRightTire`는 tires[1] 과 같이 인덱스로 표현되므로 대입이나 제어문에서 활용하기 쉬워진다.

```java
tires[1] = new KumhoTire("앞오른쪽", 13);
```

- tires 배열의 각 항목은 Tire 타입이므로 자식 객체인 KumhoTire를 대입하면 **자동 타입 변환**이 발생하기 때문에 아무런 문제가 없다.

- 배열의 타입은 Tire이지만 실제 저장 항목이 Tire의 자식 객체라면 모두 가능하다.

- 상속 관계에 있는 객체들을 배열로 관리하면 제어문에서 가장 많이 혜택을 본다.

```java
int run() {
        System.out.println("[자동차가 달립니다.]");
        for (int i = 0; i < tires.length; i++) {
            if (tires[i].roll() == false) {
                stop();
                return (i + 1);
            }
        }
        return 0;
}
```

<br>

> Car, Tire를 부품으로 가지는 클래스 (필드를 배열로 수정)

```java
public class Car {
    // 필드
    Tire[] tires = {
            new Tire("앞왼쪽", 6),
            new Tire("앞오른쪽", 2),
            new Tire("뒤왼쪽", 3),
            new Tire("뒤오른쪽", 4)
    };

    // 생성자
    // 메소드
    int run() {
        System.out.println("[자동차가 달립니다.]");
        for (int i = 0; i < tires.length; i++) {
            if (tires[i].roll() == false) {
                stop();
                return (i + 1);
            }
        }
        return 0;
    }

    void stop() {
        System.out.println("[자동차가 멈춥니다.]");
    }
}
```

> CarExample, 실행 클래스 (필드 배열 수정 버전)

```java
public class CarExample {
    public static void main(String[] args) {
        Car car = new Car();

        for (int i = 1; i <= 5; i++) {
            int problemLocation = car.run();
            if (problemLocation != 0) {
                System.out.println(car.tires[problemLocation - 1].location + " HankookTire로 교체");
                car.tires[problemLocation - 1] = new HankookTire(car.tires[problemLocation - 1].location, 15);
            }
            System.out.println("---------------------------------------------");
        }
    }
}
```

<br>

### 매개 변수의 다형성

- **자동 타입 변환**은 필드의 값을 대입할 때에도 많이 발생하지만, 주로 **메소드를 호출할 때 많이 발생**한다.

- 메소드를 호출할 때에는 매개 변수의 타입과 동일한 매개값을 지정하는 것이 정석이지만, **매개값을 다양화하기 위해** 매개 변수에 **자식 타입 객체를 지정**할 수도 있다.

> Driver 클래스에 정의된 drive() 메소드, Vehicle 타입의 매개 변수가 선언

```java
class Driver {
    void drive(Vehicle vehicle) {
        vehicle.run();
    }
}
```

> drive 메소드 호출

```java
Driver driver = new Driver();
Vehicle vehicle = new vehicle();
driver.drive(vehicle);
```

- ***매개 변수의 타입이 클래스일 경우, 해당 클래스의 객체뿐만 아니라 자식 객체까지도 매개값으로 사용할 수 있다.***

    - 매개값으로 어떤 자식 객체가 제공되느냐에 따라 메소드의 실행 결과는 다양해질 수 있다. (매개 변수의 다형성)

> Vehicle, 부모 클래스

```java
package com.vehicle;

public class Vehicle {
    public void run() {
        System.out.println("차량이 달립니다.");
    }
}
```

> Driver, Vehicle을 이용하는 클래스

```java
package com.vehicle;

public class Driver {
    public void drive(Vehicle vehicle) {
        vehicle.run();
    }
}
```

> Bus, 자식 클래스

```java
package com.vehicle;

public class Bus extends Vehicle {
    @Override
    public void run() {
        System.out.println("버스가 달립니다.");
    }
}
```

> Taxi, 자식 클래스

```java
package com.vehicle;

public class Taxi extends Vehicle {
    public void run() {
        System.out.println("택시가 달립니다.");
    }
}
```

> DriverExample, 실행 클래스

```java
package com.vehicle;

public class DriverExample {
    public static void main(String[] args) {
        Driver driver = new Driver();

        Bus bus = new Bus();
        Taxi taxi = new Taxi();

        driver.drive(bus);   // 자동 타입 변환 : Vehicle vehicle = bus;
        driver.drive(taxi);  // 자동 타입 변환 : Vehicle vehicle = taxi;
    }
}
```
- 이와 같이 매개값의 자동 타입 변환과 메소드 오버라이딩을 이용해서 매개 변수의 다형성을 구현할 수 있다.

<br>

### 강제 타입 변환(Casting)

- 강제 타입 변환 : 부모 타입을 자식 타입으로 변환하는 것

- 자식 타입이 부모 타입으로 자동 변환한 후, 다시 자식 타입으로 변환할 때 강제 타입 변환을 사용할 수 있다.

- 자식 타입이 부모 타입으로 자동 변환하면, 부모 타입에 선언된 필드와 메소드만 사용 가능하다는 제약이 따른다.

- 만약 자식 타입에 선언된 필드와 메소드를 꼭 사용해야 한다면, 강제 타입 변환을 해서 다시 자식 타입으로 변환한 다음 자식 타입의 필드와 메소드를 사용하면 된다.

<br>

### 객체 타입 확인(instanceof)

```java
Parent parent = new Parent();
Child child = (Child) Parent;   // 강제 타입 변환 불가능!
```

- 부모 변수가 참조하는 객체가 부모 객체인지 자식 객체인지 확인하는 방법

- 어떤 객체가 어떤 클래스의 인스턴스인지 확인하려면 **instanceof** 연산자를 사용할 수 있다.

```java
boolean result = 좌항(객체) instanceof 우항(타입)
```

- 좌항의 객체가 우항의 인스턴스이면, 즉 **우항의 타입으로 객체가 생성되었다면 true를 산출**하고 그렇지 않으면 false를 산출한다.

- instanceof 연산자는 매개값의 타입을 조사할 때 주로 사용된다.

    - 메소드 내에서 강제 타입 변환이 필요할 경우 반드시 매개값이 어떤 객체인지 확인하고 안전하게 강제 타입 변환을 해야 한다.

    - 타입 확인을 하지 않고 강제 타입 변환을 시도한다면 **ClassCastException 예외**가 발생할 수 있다.

```java
public class Parent {
}
```

```java
public class Child extends Parent {
}
```

```java
public class InstanceofExample {
    public static void method1(Parent parent) {
        if (parent instanceof Child) {
            Child child = (Child) parent;
            System.out.println("method1 - Child로 변환 성공");
        } else {
            System.out.println("method1 - Child로 변환되지 않음");
        }
    }

    public static void method2(Parent parent) {
        Child child = (Child) parent;
        System.out.println("method2 - Child로 변환 성공");
    }

    public static void main(String[] args) {
        Parent parentA = new Child();
        method1(parentA);
        method2(parentA);

        Parent parentB = new Parent();
        method1(parentB);
        method2(parentB);  // ClassCastException 예외발생
    }
}
```

<br>

## 추상 클래스

### 추상 클래스의 개념

- 사전적 의미로 추상(abstract)은 실체 간에 **공통되는 특성을 추출한 것**을 말한다.

- 추상 클래스는 실체 클래스의 공통되는 필드와 메소드를 추출해서 만들었기 때문에 **객체를 직접 생성해서 사용할 수 없다.**

    - 다시 말해, 추상 클래스는 new 연산자를 사용해서 인스턴스를 생성시키지 못한다.

```java
Animal animal = new Animal();  // ( X )
```

- 추상 클래스는 새로운 실체 클래스를 만들기 위해 부모 클래스로만 사용된다.

    - 코드로 설명하면 추상 클래스는 extends 뒤에만 올 수 있는 클래스이다.

```java
class Ant extends Animal { ... }  // ( O )
```

<br>

### 추상 클래스의 용도

#### 첫 번재, 실체 클래스들의 공통된 필드와 메소드의 이름을 통일할 목적

- 실체 클래스를 설계하는 사람이 여러 사람인 경우, 실체 클래스마다 필드와 메소드가 제각기 다른 이름을 가질 수 있다.

- 추상 클래스에 필드와 메소드를 선언하고, 추상 클래스를 상속함으로써 실체 클래스들의 필드와 메소드 이름을 통일시킬 수 있다.

<br>

#### 두 번째, 실체 클래스를 작성할 때 시간을 절약

- 공통적인 필드와 메소드는 추상 클래스인 Phone에 모두 선언해 두고, 실체 클래스마다 다른 점만 실체 클래스에 선언하게 되면 실체 클래스를 작성하는데 시간을 절약할 수 있다.

<br>

### 추상 클래스 선언

- 추상 클래스의 선언 : 클래스 선언에 **abstract** 키워드를 붙인다.

- `abstract`를 붙이게 되면 new 연산자를 이용해서 객체를 만들지 못하고 상속을 통해 자식 클래스만 만들 수 있다.

```java
public abstract class 클래스 {ㅇ}
    // 필드
    // 생성자
    // 메소드
}
```

- 추상 클래스도 일반 클래스와 마찬가지로 `필드, 생성자, 메소드` 선언을 할 수 있다.

- new 연산자로 직접 생성자를 호출할 수는 없지만 자식 객체가 생성될 때 `super(...)` 를 호출해서 추상 클래스 객체를 생성하므로 **추상 클래스도 생성자가 반드시 있어야 한다.**

> Phone, 추상 클래스

```java
public abstract class Phone {
    // 필드
    public String owner;

    // 생성자
    public Phone(String owner) {
        this.owner = owner;
    }

    // 메소드
    public void turnOn() {
        System.out.println("폰 전원을 켭니다.");
    }

    public void turnOff() {
        System.out.println("폰 전원을 끕니다.");
    }
}
```

> SmartPhone, 실체 클래스

```java
public class SmartPhone extends Phone {
    // 생성자
    public SmartPhone(String owner) {
        super(owner);  // Phone 생성자 호출
    }

    // 메소드
    public void internetSearch() {
        System.out.println("인터넷 검색을 합니다.");
    }
}
```

> PhoneExample, 추상 클래스 메소드 사용

```java
public class PhoneExample {
    public static void main(String[] args) {
        // Phone phone = new Phone();  // 추상클래스 Phone 생성자 호출 불가
        SmartPhone smartPhone = new SmartPhone("홍길동");

        smartPhone.turnOn();    // Phone의 메소드
        smartPhone.internetSearch();
        smartPhone.turnOff();   // Phone의 메소드
    }
}
```


<br>

### 추상 메소드와 오버라이딩

- **메소드의 선언만 통일**화하고, 실행 **내용은 실체 클래스마다 달라야 하는 경우**가 있다.

- 이런 경우를 위해 추상 클래스는 **추상 메소드**를 선언할 수 있다.

- 추상 클래스를 설계할 때, *하위 클래스가 반드시 실행 내용을 채우도록 강요하고 싶은 메소드가 있을 경우*, 해당 메소드를 **추상 메소드로 선언**하면 된다.

```java
[public | protected] abstract 리턴타입 메소드명(매개변수, ...);
```

- 일반 메소드 선언과의 차이점은 `abstract` 키워드가 붙어 있고 메소드 중괄호 `{ }`가 없다.

<br>

> Animal, 추상 메소드 선언

```java
public abstract class Animal {
    public String kind;

    public void breathe() {
        System.out.println("숨을 쉽니다.");
    }

    public abstract void sound();  // 추상 메소드
}
```

- 다음 Dog 클래스는 추상 클래스인 Animal을 상속하고, 추상 메소드인 sound()를 재정의 했다.

> Dog, 추상 메소드 오버라이딩

```java
public class Dog extends Animal {
    public Dog() {
        this.kind = "포유류";
    }

    @Override       // 추상 메소드 재정의
    public void sound() {
        System.out.println("멍멍");
    }
}
```

- 다음의 Cat 클래스도 추상 클래스인 Animal을 상속하고, 추상 메소드인 sound()를 재정의 했다.

> Cat, 추상 메소드 오버라이딩

```java
public class Cat extends Animal {
    public Cat() {
        this.kind = "포유류";
    }

    @Override
    public void sound() {
        System.out.println("야옹");
    }
}
```

- 다음 AnimalExample 클래스는 Dog와 Cat 객체를 생성해서 sound() 메소드를 호출했다.

- sound() 메소드를 호출하는 방법을 세 가지 방식으로 표현했다.

    1. 가장 일반적인 방식으로 Dog와 Cat 변수로 호출.
   
    2. Animal 변수로 타입 변환해서 sound() 메소드를 호출.
   
    3. 부모 타입의 매개 변수에 자식 객체를 대입해서 메소드의 다형성을 적용.

> AnimalExample, 실행 클래스

```java
public class AnimalExample {
    public static void main(String[] args) {
        Dog dog = new Dog();
        Cat cat = new Cat();

        // 1. 일반적인 방식의 Dog, Cat 변수 호출
        dog.sound();
        cat.sound();
        System.out.println("------");

        // 2. 변수의 자동 타입 변화
        Animal animal = null;
        animal = new Dog();
        animal.sound();

        animal = new Cat();
        animal.sound();
        System.out.println("------");

        // 3. 메소드의 다형성
        animalSound(new Dog());
        animalSound(new Cat());
    }

    public static void animalSound(Animal animal) {
        animal.sound();  // 재정의된 메소드 호출
    }
}
```

```
멍멍
야옹
------
멍멍
야옹
------
멍멍
야옹
```