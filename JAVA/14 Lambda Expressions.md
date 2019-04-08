# 14. 람다식(Lambda Expressions)

## 람다식이란?

- 람다식은 수학자 알론조 처치(Alonzo Church)가 발표한 람다 계산법에서 사용된 식으로 , 이를 제자인 존 매카시(John McCarthy)가 프로그래밍 언어에 도입했다.

- 람다식은 **익명 함수(anonymous function)를 생성하기 위한 식**으로 객체 지향 언어보다는 **함수 지향 언어**에 가깝다.

- 객체 지향 프로그래밍에 익숙한 개발자들에겐 혼동을 줄 수 있지만, 자바에서 람다식을 수용한 이유는 자바 **코드가 매우 간결**해지고, **컬렉션의 요소를 필터링하거나 매핑해서 원하는 결과를 쉽게 짐작**할 수 있다.

- 람다식의 형태는 매개 변수를 가진 코드 블록이지만, 런타임 시에는 익명 구현 객체를 생성한다.

```
람다식 → 매개 변수를 가진 코드 블록 → 익명 구현 객체
```

> Runnable 인터페이스의 익명 구현 객체를 생성하는 전형적인 코드

```java
Runnable runnable = new Runnable() {
    public void run() { ... }
}
```

> 익명 구현 객체를 람다식으로 표현

```java
Runnable runnable = () -> { ... };
```

- 람다식은 `(매개변수)->{실행코드}` 형태로 작성되는데, 마치 함수의 형태를 띠고 있지만 런타임 시에 인터페이스의 익명 구현 객체로 생성된다.
  
- 어떤 인터페이스를 구현할 것인가는 대입되는 인터페이스가 무엇이냐에 달려있다.

    - 위 코드는 Runnable 변수에 대입되므로 람다식은 Runnable의 익명 구현 객체를 생성하게 된다.

<br>

## 람다식 기본 문법

함수적 스타일의 람다식을 작성하는 방법은 다음과 같다.

```java
(타입 매개변수, ...) -> { 실행문; ... }
```

- `(타입 매개변수, ...)` 는 오른쪽 중괄호 `{}` 블록을 실행하기 위해 필요한 값을 제공하는 역할을 한다.

    - 매개 변수의 이름은 개발자가 자유롭게 줄 수 있다.

    - `->` 기호는 매개 변수를 이용해서 중괄호 `{}`를 실행한다는 뜻으로 해석
  
<br>

> int 매개 변수 a의 값을 콘솔에 출력하는 람다식

```java
(int a) -> { System.out.println(a); }
```

- **매개 변수 타입**은 런타임 시에 대입되는 값에 따라 **자동으로 인식**될 수 있기 때문에 **람다식에서는 매개 변수의 타입을 일반적으로 언급하지 않는다.**

    - 따라서 위의 코드는 아래와 같이 표현할 수 있다.

```java
a -> { System.out.println(a); }
```

- 하나의 매개 변수만 있다면 괄호 `( )`를 생략할 수 있고, 하나의 실행문만 있다면 중괄호 `{}`도 생략 할 수 있다.

```java
a -> System.out.println(a)
```

- 만약 매개 변수가 없다면 람다식에서 매개 변수 자리가 없어지므로 다음과 같이 빈 괄호`()`를 반드시 사용해야 한다.

```java
( ) -> { 실행문; ... }
```

- 중괄호 `{}`를 실행하고 결과값을 리턴해야 한다면 다음과 같이 return문으로 결과값을 지정할 수 있다.

```java
(x, y) -> { return x + y; }
```

- 중괄호 `{}`애 **return문 만 있을 경우**, 람다식에서는 **return문을 사용하지 않고 다음과 같이 작성하는 것이 정석**이다.

```java
(x, y) -> x + y
```

<br>

## 타겟 타입과 함수적 인터페이스

```java
인터페이스 변수 = 람다식;
```

- 람다식은 인터페이스 변수에 대입된다.

    - 이 말은 람다식은 인터페이스의 익명 구현 객체를 생성한다는 뜻이 된다.

- 인터페이스는 직접 객체화할 수 없기 때문에 구현 클래스가 필요한데, 람다식은 익명 구현 클래스를 생성하고 객체화한다.

- 람다식은 대입될 인터페이스의 종류에 따라 작성 방법이 달라지기 때문에 **람다식이 대입될 인터페이스를 `람다식의 타켓 타입(target type)`** 이라고 한다.

<br>

### 함수적 인터페이스 `@FunctionalInterface`

- 모든 인터페이스를 람다식의 타겟 타입으로 사용할 수는 없다.

  - **람다식이 하나의 메소드를 정의하기 때문에** "두 개 이상의 추상 메소드가 선언된 인터페이스"에는 람다식을 이용해서 구현 객체를 생성할 수 없다.

- **하나의 추상 메소드가 선언된 인터페이스**만이 람다식의 타켓 타입이 될 수 있는데, 이러한 인터페이스를 **함수적 인터페이스(functional interface)** 라고 한다.

- 인터페이스 선언 시 **`@FunctionalInterface`** 어노테이션을 붙이면, **함수적 인터페이스를 작성할 때 두 개 이상의 추상 메소드가 선언되지 않도록 컴파일러가 체킹**해주는 기능이 있다.

    - 두 개 이상의 추상 메소드가 선언되면 컴파일 오류 발생

```java
@FunctionalInterface
public interface MyFunctionalInterface {
    public void method();
    public void otherMethod();  // 컴파일 오류
}
```

- `@FunctionalInterface` 어노테이션은 선택 사항이다.

    - 해당 어노테이션이 없더라도 **하나의 추상 메소드만 있다면 모두 함수적 인터페이스이다.**

    - 그러나 실수로 두 개 이상의 추상 메소드를 선언하는 것을 방지하고 싶다면 붙여주도록 하자.

<br>

### 매개 변수와 리턴값이 없는 람다식

> MyFunctionalInterface, 함수적 인터페이스

```java
@FunctionalInterface
public interface MyFunctionalInterface {
    public void method();
}
```

- 이 인터페이스를 타겟 타입으로 갖는 람다식은 다음과 같은 형태로 작성한다.

    - 람다식에서 매개 변수가 없는 이유는 method()가 매개 변수를 가지지 않기 때문이다.

```java
MyFunctionalInterface fi = () -> { ... }
```

- 람다식이 대입된 인터페이스의 참조 변수는 다음과 같이 method()를 호출할 수 있다.

    - method() 호출은 람다식의 중괄호 `{}`를 실행시킨다.

```java
fi.method();
```

<br>

> MyFunctionalInterfaceExample, 람다식

```java
public class MyFunctionalInterfaceExample {
    public static void main(String[] args) {
        MyFunctionalInterface fi;

        fi = () -> {
            String str = "method call1";
            System.out.println(str);
        };
        fi.method();

        fi = () -> { System.out.println("method call2"); };
        fi.method();

        fi = () -> System.out.println("method call3");
        fi.method();
    }
}
```

```
method call1
method call2
method call3
```

<br>

### 매개 변수가 있는 람다식

- 다음과 같이 매개 변수가 있고 리턴값이 없는 추상 메소드를 가진 함수적 인터페이스가 있다고 보자.

> MyFunctionalInterface, 함수적 인터페이스

```java
@FunctionalInterface
public interface MyFunctionalInterface {
    public void method(int x);
}
```

- 이 인터페이스를 타겟 타입을 갖는 람다식은 다음과 같은 형태이다.

    - 람다식에서 매개 변수가 한 개인 이유는 method()가 매개 변수를 하나만 가지기 때문이다.

```java
MyFunctionalInterface fi = (x) -> { ... }

또는

MyFunctionalInterface fi = x -> { ... }
```

- 람다식이 대입된 인터페이스 참조 변수는 다음과 같이 method()를 호출할 수 있다.

```java
fi.method(5);
```

> MyFunctionalInterfaceExample, 람다식

```java
public class MyFunctionalInterfaceExample {
    public static void main(String[] args) {
        MyFunctionalInterface fi;
        
        fi = (x) -> {
            int result = x * 5;
            System.out.println(result);
        };
        fi.method(2);
        
        fi = (x) -> { System.out.println(x * 5); };
        fi.method(2);
        
        fi = x -> System.out.println(x * 5);
        fi.method(2);
    }
}
```

<br>

### 리턴값이 있는 람다식

- 다음과 같이 매개 변수가 있고 리턴값이 있는 추상 메소드를 가진 함수적 인터페이스가 있다고 보자.

> MyFunctionalInterface, 함수적 인터페이스

```java
@FunctionalInterface
public interface MyFunctionalInterface {
    public int method(int x, int y);
}
```

- 이 인터페이스를 타겟 타입으로 갖는 람다식은 다음과 같은 형태로 작성해야 한다.

```java
MyFunctionalInterface fi = (x, y) -> { ...; return 값; }
```

- 만약 중괄호 `{}`에 return문만 있고, return문 뒤에 연산식이나 메소드 호출이 오는 경우라면 다음과 같이 작성할 수 있다.

```java
MyFunctionalInterface fi = (x, y) -> {
    return x + y;
}

↓

MyFunctionalInterface fi = (x, y) -> x + y;
```

```java
MyFunctionalInterface fi = (x, y) {
    return sum(x, y);
}

↓

MyFunctionalInterface fi = (x, y) -> sum(x, y);
```

<br>

> MyFunctionalInterfaceExample, 람다식

```java
public class MyFunctionalInterfaceExample {
    public static void main(String[] args) {
        MyFunctionalInterface fi;

        fi = (x, y) -> {
            int result = x + y;
            return result;
        };
        System.out.println(fi.method(2, 5));

        fi = (x, y) -> {
            return x + y;
        };
        System.out.println(fi.method(2, 5));

        fi = (x, y) -> x + y;
        System.out.println(fi.method(2, 5));
    }

    public static int sum(int x, int y) {
        return (x + y);
    }
}
```

```
7
7
7
```

<br>

## 클레스 멤버와 로컬 변수 사용

- 람다식의 실행 블록에는 클래스의 멤버(필드와 메소드) 및 로컬 변수를 사용할 수 있다.
 
- `클래스의 멤버`는 **제약 사항 없이 사용 가능**하지만, `로컬 변수`는 **제약 사항이 따른다.**

<br>

### 클래스의 멤버 사용

- 람다식 실행 블록에는 클래스의 멤버인 필드와 메소드를 제약 사항 없이 사용할 수 있다.

- 하지만 **this 키워드를 사용할 때에는 주의**가 필요하다.

    - 일반적으로 익명 객체 내부에서 this는 익명 객체의 참조이지만, **람다식에서 this**는 내부적으로 생성되는 익명 객체의 참조가 아니라 **람다식을 실행한 객체의 참조**이다.

> MyFunctionalInterface, 함수적 인터페이스

```java
public interface MyFunctionalInterface {
    public void method();
}
```

> UsingThis, this 사용

```java
public class UsingThis {
    public int outterfield = 10;

    class Inner {
        int innerfield = 20;

        void method() {
            // 람다식
            MyFunctionalInterface fi = () -> {
                System.out.println("outterfield: " + outterfield);
                System.out.println("outterfield: " + UsingThis.this.outterfield + "\n");  // 바깥 객체의 참조를 얻기 위해서는 클래스명 this를 사용

                System.out.println("innerfield: " + innerfield);
                System.out.println("innerfield: " + this.innerfield + "\n");  // 람다식 내부에서 this는 inner 객체를 참조
            };
            fi.method();
        }
    }
}
```

> UsingThisExample, 실행 클래스

```java
public class UsingThisExample {
    public static void main(String... args) {
        UsingThis usingThis = new UsingThis();
        UsingThis.Inner inner = usingThis.new Inner();
        inner.method();
    }
}
```

<br>


### 로컬 변수 사용

- **람다식은 메소드 내부에서 주로 작성되기 때문에 로컬 익명 구현 객체를 생성시킨다고 봐야 한다.**

- 람다식에서 바깥 클래스의 필드나 메소드는 제한 없이 사용할 수 있으나, **메소드의 매개 변수 또는 로컬 변수를 사용하면 이 두 변수는 final 특성을 가져야 한다.** → 1권 414p, `9.5.3 익명 객체의 로컬 변수 사용` 참고

    - 따라서 매개 변수 또는 로컬 변수를 람다식에서 읽는 것은 허용되지만, 람다식 내부 또는 외부에서 변경할 수 없다.

<br>

> MyFunctionalInterface, 함수적 인터페이스

```java
public interface MyFunctionalInterface {
    public void method();
}
```

> UsingLocalVariable, final 특성을 가지는 로컬 변수

```java
public class UsingLocalVariable {
    void method(int arg) {  // arg는 final 특성을 가짐
        int localVar = 40;  // localVar는 final 특성을 가짐

        // arg = 31;           // final 특성 때문에 수정 불가
        // localVar = 41;      // final 특성 때문에 수정 불가

        // 람다식
        MyFunctionalInterface fi = () -> {
            // 로컬 변수 읽기
            System.out.println("arg: " + arg);
            System.out.println("localVar: " + localVar + "\n");
        };
        fi.method();
    }
}
```

> UsingLocalVariableExample, 실행 클래스

```java
public class UsingLocalVariableExample {
    public static void main(String[] args) {
        UsingLocalVariable ulv = new UsingLocalVariable();
        ulv.method(20);
    }
}
```

```
arg: 20
localVar: 40
```

<br>

## 표준 API의 함수적 인터페이스

- 

> RunnableExample, 함수적 인터페이스와 람다식

```java

```