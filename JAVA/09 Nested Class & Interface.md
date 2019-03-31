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

<br>

### 멤버 클래스에서 사용 제한