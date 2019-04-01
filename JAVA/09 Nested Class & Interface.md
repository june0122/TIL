# 챕터 09. 중첩 클래스와 중첩 인터페이스

## 중첩 클래스와 중첩 인터페이스란?

- 중첩 클래스(Nested Class)란 클래스 내부에 선언한 클래스를 말한다.

    - 중첩 클래스를 사용하면 두 클래스의 멤버들을 서로 쉽게 접근할 수 있다는 장점과 외부에는 불필요한 관계 클래스를 감춤으로써 코드의 복잡성을 줄일 수 있다.

> 중첩 클래스 코드

```java
class ClassName {
    class NestedClassName {  // 중첩 클래스

    }
}
```

- 인터페이스도 중첩 인터페이스가 존재하며, 클래스 내부에 선언할 수 있다.

    - 해당 클래스와 긴밀한 관계를 맺는 구현 클래스를 만들기 위해서 중첩 인터페이스를 사용한다.

> 중첩 인터페이스

```java
class ClassName {
    interface NestedInterfaceName {  // 중첩 인터페이스

    }
}
```

- 중첩 인터페이스는 주로 UI 프로그래밍에서 이벤트를 처리할 목적으로 많이 활용된다.

> 안드로이드 View 클래스의 클릭 이벤트를 처리

```java
public class view {
    public interface OnClickListener {  // 중첩 인터페이스
        public void onClick(View v);
    }
}
```

<br>

## 중첩 클래스

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55287567-9a214a80-53e5-11e9-967f-4d237c99d7a9.png'>
</p>
<br>

- 멤버 클래스도 하나의 클래스이기 때문에 컴파일하면 바이트 코드 파일(.class)이 별도로 생성된다.

```java  
           A  $  B .class
          ───   ───
   바깥 클래스   멤버 클래스
```

```java  
           A  $  B .class
          ───   ───
   바깥 클래스   로컬 클래스
```

<br>

### 인스턴스 멤버 클래스

- `static` 키워드 없이 선언된 클래스

- 인스턴스 필드 · 메소드만 선언이 가능하고 정적 필드 · 메소드는 선언할 수 없다.

```java
class A {
    /** 인스턴스 멤버 클래스 **/
    class B {
        B() {}                       // 생성자
        int field1;                  // 인스턴스 필드
        // static int field2;        // 정적 필드 (X)
        void method1() {}            // 인스턴스 메소드
        // static void method2 {}    // 정적 메소드 (X)
    }
}
```

- A 클래스 외부에서 인스턴스 멤버 클래스 B의 객체를 생성하려면 먼저 A 객체를 생성하고 B 객체를 생성해야 한다.

```java
A a = new A();
A.B b = a.new B();
b.field1 = 3;
b.method1();
```

<br>

### 정적 멤버 클래스

- `static` 키워드로 선언된 클래스

- 정적 멤버 클래스는 모든 종류의 필드와 메소드를 선언할 수 있다.

```java
class A {
    /** 정적 멤버 클래스 **/
    static class C {
        C() {}                       // 생성자
        int field1;                  // 인스턴스 필드
        static int field2;           // 정적 필드
        void method1() {}            // 인스턴스 메소드
        static void method2 {}       // 정적 메소드
    }
}
```

- A 클래스 외부에서 정적 멤버 클래스 C의 객체를 생성하기 위해서는 A 객체를 생성할 필요가 없고 다음과 같이 C 객체를 생성하면 된다.

```java
A.C c = new A.C();
c.field1 = 3;
c.method1();
A.C.field2 = 3;
A.C.method2();
```

<br>

### 로컬 클래스

- 매소드 내에 선언된 클래스를 `로컬 클래스`라고 한다.

- 로컬 클래스는 `접근 제한자` 및 `static`을 붙일 수 없다.

  - 로컬 클래스는 **메소드 내부에서만 사용되므로 접근을 제한할 필요가 없기 때문**이다! 

- 로컬 클래스 내부에는 인스턴스 필드와 메소드만 선언이 가능하고 정적 필드와 메소드는 선언할 수 없다.

```java
void method() {
    /** 로컬 클래스 **/
    class D {
        D() {}                       // 생성자
        int field1;                  // 인스턴스 필드
        // static int field;2        // 정적 필드 (X)
        void method1();              // 인스턴스 메소드
        // static void method2() {}  // 정적 메소드 (X)
    }
}
```

- 로컬 클래스는 메소드가 실행될 때 메소드 내에서 객체를 생성하고 사용해야 한다.

- 주로 다음과 같이 비동기 처리를 위해 스레드 객체를 만들 때 사용한다.

> 비동기 처리를 위한 스레드 객체 생성

```java
void method() {
    class DownloadThread extends Thread { ... }
    DownloadThread thread = new DownloadThread();
    thread.start();
}
```

<br>

> A, 중첩 클래스

```java
/** 바깥 클래스 **/
public class A {
    A() {
        System.out.println("A 객체가 생성됨");
    }

    /** 인스턴스 멤버 클래스 **/
    class B {
        B() {
            System.out.println("B 객체가 생성됨");
        }
        int field1;
        // static int field2;
        void method1() { }
        // static void method2() {}
    }

    /** 정적 멤버 클래스 **/
    static class C {
        C() {
            System.out.println("C 객체가 생성됨");
        }
        int field1;
        static int field2;
        void method1() {}
        static void method2() {}
    }

    void method() {
        /** 로컬 클래스 **/
        class D {
            D() {
                System.out.println("D 객체가 생성됨");
            }
            int field1;
            // static int field2;
            void method1() { }
            // static void method2() {}
        }
        D d = new D();
        d.field1 = 3;
        d.method1();
    }
}
```

> Main, 중첩 클래스 객체 생성

```java
public class Main {
    public static void main(String[] args) {
        A a = new A();

        // 인스턴스 멤버 클래스 객체 생성
        A.B b = a.new B();
        b.field1 = 3;
        b.method1();

        // 정적 멤버 클래스 객체 생성
        A.C c = new A.C();
        c.field1 = 3;
        c.method1();
        A.C.field2 = 3;
        A.C.method2();

        // 로컬 클래스 객체 생성을 위한 메소드 호출
        a.method();
    }
}
```

<br>

## 중첩 클래스의 접근 제한

### 바깥 필드와 메소드에서 사용 제한

- 멤버 클래스가 인스턴스 또는 정적으로 선언됨에 따라 **바깥 클래스의 필드와 메소드에** 사용 제한이 생긴다.

    - 인스턴스 멤버 클래스 `B`는 바깥 클래스의 인스턴스 필드(field1)의 초기값이나 인스턴스 메소드 `method1`에서 객체를 생성할 수 있으나, 정적 필드 `field3`의 초기값이나 정적 메소드 `method2()`에서는 객체를 생성할 수 없다.

    - 반면 정적 멤버 클래스 `C`는 모든 필드의 초기값이나 모든 메소드에서 객체를 생성할 수 있다. 

```java
public class A {
    // 인스턴스 필드
    B field1 = new B();
    C field2 = new C();

    // 인스턴스 메소드
    void method1() {
        B var1 = new B();
        C var2 = new C();
    }

    // 정적 필드 초기화
    // statoc B field3 = new B();  // ( X )
    static C field4 = new C();

    // 정적 메소드
    static void method2() {
        // B var1 = new B();  // ( X )
        C var2 = new C();
    }

    // 인스턴스 멤버 클래스
    class B {}

    // 정적 멤버 클래스
    static class C {}
}
```

<br>

### 멤버 클래스에서 사용 제한

- 멤버 클래스가 인스턴스 또는 정적으로 선언됨에 따라 **멤버 클래스 내부에서 바깥 클래스**의 필드와 메소드를 접근할 때에도 제한이 따른다.

    - 인스턴스 멤버 클래스 `B` 안에서는 바깥 클래스의 모든 필드와 모든 메소드에 접근할 수 있지만,

    - 정적 멤버 클래스 `C` 안에서는 바깥 클래스의 정적 필드 `field2`와 메소드 `method2()`에만 접근할 수 있고 인스턴스 필드 `field1`와 메소드 `method1()`는 접근할 수 없다.

<br>

### <b id="f1"><sup>1</sup></b> 로컬 클래스에서 사용 제한 [↩](#a1)<br>

- 로컬 클래스 내부에서는 바깥 클래스의 필드나 메소드를 제한 없이 사용할 수 있다.

- 문제는 메소드의 매개 변수나 로컬 변수를 로컬 클래스에서 사용할 때이다.

    - `로컬 클래스의 객체`는 메소드 실행이 끝나도 힙 메모리에 존재해서 계속 사용될 수 있다.

    - **`매개 변수나 로컬 변수`는 메소드 실행이 끝나면 스택 메모리에서 사라지기 때문에 로컬 객체에서 사용할 경우 문제가 발생**한다.

- 자바는 이 문제를 해결하기 위해 컴파일 시 로컬 클래스에서 사용하는 매개 변수나 로컬 변수의 값을 **로컬 클래스 내부에 복사해두고 사용**한다.

    - 매개 변수나 로컬 변수가 수정되어 값이 변경되면 로컬 클래스에 복사해 둔 값과 달라지는 문제를 해결하기 위해 **매개 변수나 로컬 변수를 final로 선언해서 수정을 막는다.**

<br>

> `final` 키워드 존재 여부의 차이점 : '로컬 클래스의 복사 위치'

- 로컬 클래스의 내부 복사 위치에 신경 쓸 필요 없이 ***로컬 클래스에서 사용된 매개 변수와 로컬 변수는 모두 final 특성을 갖고 있다.*** 는 것만 알면 된다.

  - final 키워드가 있다면 : 로컬 클래스의 메소드 내부에 지역 변수로 복사

  - final 키워드가 없다면 : 로컬 클래스의 필드로 복사

- 자바 7까지는 반드시 final 키워드를 붙여야 하지만, 자바 8부터는 붙이진 않아도 final 특징을 가지고 있음을 주목해야 한다.

```java
public class Outter {
    // 자바 7 이전
    public void method1(final int arg) {
        final int localVariable = 1;
        // arg = 100; (X)
        // localVariable = 100; (X)

        class Inner {
            public void method() {
                int result = arg + localVariable;
            }
        }
    }

    // 자바 8이후
    public void method2(int arg) {
        int localVariable = 1;
        // arg = 100; (X)
        // localVariable = 100; (X)

        class Inner {
            public void method() {
                int result = arg + localVariable;
            }
        }
    }
}
```

<br>

### 중첩 클래스에서 바깥 클래스 참조 얻기

- 중첩 클래스 내부에서 `this.필드`, `this.메소드`를 호출하면 중첩 클래스의 필드와 메소드가 사용된다.

- 중첩 클래스 내부에서 바깥 클래스의 객체 참조를 얻으려면 **바깥 클래스의 이름을 this 앞에 붙인다.**

```
바깥클래스.this.필드
바깥클래스.this.메소드();
```

<br>

> Outter2, 중첩 클래스에서 바깥 클래스 참조 얻기

```java
public class Outter2 {
    String field = "Outter-field";

    void method() {
        System.out.println("Outter-method");
    }

    class Nested {
        String field = "Nested-field";

        void method() {
            System.out.println("Nested-method");
        }

        void print() {
            // 중첩 객체 참조
            System.out.println(this.field);
            this.method();
            // 바깥 객체 참조
            System.out.println(Outter2.this.field);
            Outter2.this.method();
        }
    }
}
```

```java
public class Outter2Example {
    public static void main(String[] args) {
        Outter2 outter2 = new Outter2();
        Outter2.Nested nested = outter2.new Nested();
        nested.print();
    }
}
```

```
Nested-field
Nested-method
Outter-field
Outter-method
```

<br>

## 중첩 인터페이스

- 중첩 인터페이스 : 클래스의 멤버로 선언된 인터페이스

    - 인터페이스를 클래스 내부에 선언함으로써 **해당 클래스와 긴밀한 관계를 맺는 구현 클래스를 생성**한다.

    - 특히 UI 프로그래밍에서 이벤트 처리 목적으로 많이 활용

```java
class A {
    // 중첩 인터페이스
    interface I {
        void method();
    }
}
```

<br>

> Button, 중첩 인터페이스

```java
public class Button {
    OnClickListener listener;  // 인터페이스 타입 필드

    void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    void touch() {
        listener.onClick();
    }

    interface OnClickListener {
        void onClick();
    }
}
```

> CallListener, 구현 클래스

```java
public class CallListener implements Button.OnClickListener {
    @Override
    public void onClick() {
        System.out.println("전화를 겁니다.");
    }
}
```

> MessageListener, 구현 클래스

```java
public class MessageListener implements Button.OnClickListener {
    @Override
    public void onClick() {
        System.out.println("메세지를 보냅니다.");
    }
}
```

> ButtonExample, 버튼 이벤트 처리

```java
public class ButtonExample {
    public static void main(String[] args) {
        Button btn = new Button();

        btn.setOnClickListener(new CallListener());
        btn.touch();

        btn.setOnClickListener(new MessageListener());
        btn.touch();
    }
}
```

```
전화를 겁니다.
메세지를 보냅니다.
```

<br>

## 익명 객체

- 익명(anonymous) 객체는 이름이 없는 객체를 말한다.

- 익명 객체는 단독으로 생성할 수 없고 클래스를 상속하거나 인터페이스를 구현해야만 생성할 수 있다.

- 익명 객체는 필드의 초기값이나 로컬 변수의 초기값, 매개 변수의 매개값으로 주로 대입된다.

<br>

### 익명 자식 객체 생성

```java
class Child extends Parent { }  // 자식 클래스 선언

class A {
    Parent field = new Child();  // 필드에 자식 객체를 대입
    void method() {
        Parent localVar = new Child();  // 로컬 변수에 자식 객체를 대입
    }
}
```

- 자식 클래스가 재사용되지 않고, 오로지 해당 필드와 변수의 초기값으로만 사용할 경우라면 익명 자식 객체를 생성해서 초기값으로 대입하는 것이 좋은 방법이다.

- 하나의 실행문이므로 **끝에 세미콜론 `;`** 을 반드시 붙인다.

```java
부모클래스 [ 필드 | 변수 ] = new 부모클래스(매개값, ...) {
    // 필드
    // 메소드
} ;
```

<br>

> Person, 부모 클래스

```java
public class Person {
    void wake() {
        System.out.println("7시에 일어납니다.");
    }
}
```
> Anonymous, 익명 자식 객체 생성

```java
public class Anonymous {
    // 필드 초기값으로 대입
    Person field = new Person() {
        void work() {
            System.out.println("출근합니다.");
        }

        @Override
        void wake() {
            System.out.println("6시에 일어납니다.");
            work();
        }
    };

    void method1() {
        // 로컬 변수값으로 대입
        Person localVar = new Person() {
            void walk() {
                System.out.println("산책합니다.");
            }

            @Override
            void wake() {
                System.out.println("7시에 일어납니다.");
                walk();
            }
        };

        // 로컬 변수 사용
        localVar.wake();
    }

    void method2(Person person) {
        person.wake();
    }
}
```

> AnonymousExample, 익명 자식 객체 생성

```java
public class AnonymousExample {
    public static void main(String[] args) {
        Anonymous anony = new Anonymous();

        // 익명 객체 필드 사용
        anony.field.wake();

        // 익명 객체 로컬 변수 사용
        anony.method1();

        // 익명 객체 매개값 사용
        anony.method2(
                new Person() {
                    void study() {
                        System.out.println("공부합니다.");
                    }

                    @Override
                    void wake() {
                        System.out.println("8시에 일어납니다.");
                        study();
                    }
                }
        );
    }
}
```

<br>

### 익명 구현 객체 생성

- 인터페이스 타입으로 필드나 변수를 선언하고, 구현 객체를 초기값으로 대입하는 경우를 생각해보자

```java
class TV implements RemoteControl { }

class A {
    RemoteControl field = new TV();  // 필드에 구현 객체 대입
    void method() {
        RemoteControl localVar = new TV();  // 로컬 변수에 구현 객체를 대입
    }
}
```

- 그러나 구현 클래스가 재사용되지 않고, 오로지 해당 필드와 변수의 초기값으로만 사용하는 경우라면 익명 구현 객체를 초기값으로 대입하는 것이 좋다.

```java
인터페이스 [ 필드 | 변수 ] = new 인터페이스() {
    // 인터페이스에 선언된 추상 메소드의 실체 메소드 선언
    // 필드
    // 메소드
}
```

<br>

### 익명 객체의 로컬 변수 사용

`로컬 클래스에서 사용 제한` <sup id="a1">[1](#f1)</sup> 의 내용과 유사
