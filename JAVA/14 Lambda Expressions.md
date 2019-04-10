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

- 자바에서 제공되는 표준 API에서 한 개의 추상 메소드를 가지는 인터페이스들은 모두 람다식을 이용해서 익명 구현 객체로 표현이 가능하다.

- 예를 들어 스레드의 작업을 정의하는 Runnable 인터페이스는 매개 변수와 리턴값이 없는 run() 메소드만 존재하기 때문에 다음과 같이 람다식을 이용해서 Runnable 인스턴스를 생성시킬 수 있다.

> RunnableExample, 함수적 인터페이스와 람다식

```java
public class RunnableExample {
    public static void main(String[] args) {
        Runnable runnable = () -> {       // 람다식 (스레드가 실행하는 코드)
            for(int i = 0; i < 10; i++) {
                System.out.println(i);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
```

- Thread 생성자를 호출할 때 다음과 같이 람다식을 매개값으로 대입해도 된다.

```java
Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
            }
        });
```

- Java 8부터는 빈번하게 사용되는 함수적 인터페이스(functional interface)는 `java.util.function` 표준 API 패키지로 제공한다.

- 이 패키지에서 제공하는 함수적 인터페이스의 목적은 메소드 또는 생성자의 매개 타입으로 사용되어 람다식을 대입할 수 있도록 하기 위해서이다.

- `java.util.function` 패키지의 함수적 인터페이스는 크게 `Consumer, Supplier, Function, Operator, Predicate`로 구분된다.

  - 구분 기준은 **인터페이스에 선언된 추상 메소드의 매개값과 리턴값의 유무**이다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55770122-2e627000-5abe-11e9-8eb9-02900d8947cc.png'>
</p>
<br>

### Consumer 함수적 인터페이스

- Consumer 함수적 인터페이스의 특징은 리턴값이 없는 `accept()` 메소드를 가지고 있다. `accept()` 메소드는 **단지 매개값을 소비하는 역할만 한다.**

    - 소비한다는 말은 사용만 할 뿐 리턴값이 없다는 뜻이다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55770336-e5f78200-5abe-11e9-9b8c-38b7ee39cb1c.png'>
</p>
<br>


> `Consumer<T>` 인터페이스를 타겟 타입으로 하는 람다식

- accept() 메소드는 매개값으로 double 하나를 가지므로 람다식도 한 개의 매개 변수를 사용한다.

```java
Consumer<String> consumer = t -> { t를 소비하는 실행문; };
```

<br>

> BiConsumer<T, U> 인터페이스를 타겟 타입으로 하는 람다식

- `accept()` 메소드는 매개값으로 T와 U 두 개의 객체를 가지므로 람다식도 두 개의 매개 변수를 사용한다.
 
  - 타입 파라미터 T와 U에 String이 대입되었으므로 람다식의 t와 u 매개 변수 타입은 각각 String이 된다.

```java
BiConsumer<String, String> consumer = (t, u) -> { t와 u를 소비하는 실행문; }
```

<br>

> DoubleConsumer 인터페이스를 타겟 타입으로 하는 람다식

```java
DoubleConsumer consumer = d -> { d를 소비하는 실행문; }
```

<br>

> ObjIntConsumer<T> 인터페이스를 타겟 타입으로 하는 람다식

- `accept()` 메소드는 매개값으로 T 객체와 int 값 두 개를 가지므로 람다식도 두 개의 매개 변수를 사용한다.

    - T가 String 타입이므로 람다식의 t 매개 변수 타입은 String이 되고, i는 고정적으로 int 타입이 된다.

```java
ObjIntConsumer<String> consumer = (t, i) -> { t와 i를 소비하는 실행문; }
```

<br>

> ConsumerExample, Consumer 함수적 인터페이스

```java
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.ObjIntConsumer;

public class ConsumerExample {
    public static void main(String[] args) {
        Consumer<String> consumer = t -> System.out.println(t + "8");
        consumer.accept("java");

        BiConsumer<String, String> biConsumer = (t, u) -> { System.out.println(t + u); };
        biConsumer.accept("Java", "8");

        DoubleConsumer doubleConsumer = d -> System.out.println("Java" + d);
        doubleConsumer.accept(8.0);

        ObjIntConsumer<String> objIntConsumer = (t, i) -> System.out.println(t + i);
        objIntConsumer.accept("Java", 8);
    }
}
```

```
java8
Java8
Java8.0
Java8
```

<br>

### Supplier 함수적 인터페이스

- Supplier 함수적 인터페이스의 특징은 매개 변수가 없고 리턴값이 있는 getXXX() 메소드를 가지고 있다. 이 메소드들은 실행 후 호출한 곳으로 데이터를 리턴(공급)하는 역할을 한다.

<br>

> 리턴 타입에 따른 Supplier 함수적 인터페이스

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55771835-8ef4ab80-5ac4-11e9-901f-35681c294826.png'>
</p>
<br>

> Supplier<T> 인터페이스를 타겟 타입으로 하는 람다식

- `get()` 메소드가 매개값을 가지지 않으므로 람다식도 `()`를 사용

- 람다식의 중괄호 `{}` 는 반드시 한 개의 T 객체를 리턴하도록 해야 한다.

- T가 String 타입이므로 람다식의 중괄호 `{}` 는 문자열을 리턴하도록 해야 한다.

```java
Supplier<String> supplier = () -> { ...; return "문자열"; };
```

<br>

> IntSupplier 인터페이스 타겟 타입으로 하는 람다식

- getAsInt() 메소드가 매개값을 가지지 않으므로 람다식도 `()` 사용

- 람다식의 중괄호 `{}` 는 반드시 int 값을 리턴

```
IntSupplier supplier = () -> { ...; return int값; }
```

<br>

> Supplier 함수적 인터페이스, 주사위의 숫자를 랜덤하게 공급하는 IntSupplier 인터페이스를 타겟 타입으로 하는 람다식

```java
import java.util.function.IntSupplier;

public class SupplierExample {
    public static void main(String[] args) {
        IntSupplier intSupplier = () -> {
            int num = (int) (Math.random() * 6) + 1;
            return num;
        };

        int num = intSupplier.getAsInt();
        System.out.println("눈의 수: " + num);
    }
}
```

```
눈의 수: 1
```

<br>

### Function 함수적 인터페이스

- Function 함수적 인터페이스의 특징은 매개값과 리턴값이 있는 applyXXX() 메소드를 가지고 있다.

    - 이 메소드들은 **매개값을 리턴값으로 매핑(타입 변환)하는 역할**을 한다.

<br>

> 매개 변수 타입과 리턴 타입에 따른 Function 함수적 인터페이스

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55771836-8f8d4200-5ac4-11e9-988d-3d23eaccd482.png'>
</p>
<br>

- `apply()` 메소드는 매개값으로 T 객체 하나를 가지므로 람다식도 한 개의 매개 변수를 사용한다. 그리고 `apply()` 메소드의 리턴 타입이 R이므로 람다식 중괄호 `{}`의 리턴값은 R 객체가 된다.

- T가 Student 타입이고 R이 String 타입이므로 t 매개 변수 타입은 Student가 되고, 람다식의 중괄호 `{}` 는 String을 리턴해야 한다.

- `t.getName()`은 Student 객체의 `getName()` 메소드를 호출해서 학생 이름(String)을 얻는다.

- return문만 있을 경우 중괄호 `{}`와 return문은 생략할 수 있다는 것을 이미 배웠다. 다음 코드는 Student 객체를 학생 이름(String)으로 매핑하는 것이다.

```
Function<Student, String> function = t -> { return t.getName(); }
또는
Function<Student, String> function = t -> t.getName();
```

<br>

> ToIntFunction<T> 인터페이스를 타겟 타입으로 하는 람다식

- applyAsInt() 메소드는 매개값으로 T 객체 하나를 가지므로 람다식도 한 개의 매개 변수를 사용한다. 그리고 applyASInt의 리턴 타입이 int이므로 람다식 중괄호 `{}`의 리턴값은 int가 된다.

- T가 Student 타입으므로 t 매개 변수 탕비은 Student가 된다.
  
- t.getScore()는 Student 객체의 getScore() 메소드를 호출해서 학생 점수(int)를 얻는다.

<br>

> Student 객체를 학상 점수로 매핑

```java
ToIntFunction<Student> function = t -> { return t.getScore(); }
또는
ToIntFunction<Student> function = t -> t.getScore();
```

<br>

> FunctionExample1, Function 함수적 인터페이스

- List에 저장된 학생 객체를 하나씩 꺼내서 이름과 점수를 출력

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class FunctionExample1 {
    private static List<Student> list = Arrays.asList(
            new Student("홍길동", 90, 96),
            new Student("신용권", 95, 93)
    );

    public static void printString(Function<Student, String> function) {
        for (Student student : list) {  // list에 저장된 항목 수만큼 루핑
            System.out.println(function.apply(student) + " ");   // 람다식 실행
        }
        System.out.println();
    }

    public static void printInt(ToIntFunction<Student> function) {
        for (Student student : list) {  // list에 저장된 항목 수만큼 루핑
            System.out.println(function.applyAsInt(student) + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("[학생 이름]");
        printString(t -> t.getName());

        System.out.println("[영어 점수]");
        printInt(t -> t.getEnglishScore());

        System.out.println("[수학 점수]");
        printInt(t -> t.getMathScore());
    }
}
```

> Student 클래스

```java
public class Student {
    private String name;
    private int englishScore;
    private int mathScore;

    public Student(String name, int englishScore, int mathScore) {
        this.name = name;
        this.englishScore = englishScore;
        this.mathScore = mathScore;
    }

    public String getName() { return name; }

    public int getEnglishScore() { return englishScore; }

    public int getMathScore() {
        return mathScore;
    }
}
```

<br>

- 다음 예제는 List에 저장된 학생 객체를 하나씩 꺼내서 영어 점수와 수학 점수의 평균값을 산출한다.

> FunctionExample2, Function 함수적 인터페이스

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;

public class FunctionExample2 {
    private static List<Student> list = Arrays.asList(
            new Student("홍길동", 90, 96),
            new Student("신용권", 95, 93)
    );

    public static double avg(ToIntFunction<Student> function) {
        int sum = 0;
        for (Student student : list) {
            sum += function.applyAsInt(student);
        }
        double avg = (double) sum / list.size();
        return avg;
    }

    public static void main(String[] args) {
        double englishAvg = avg(s -> s.getEnglishScore());
        System.out.println("영어 평균 점수: " + englishAvg);

        double mathAvg = avg(s -> s.getMathScore());
        System.out.println("수학 평균 점수: " + mathAvg);
    }
}
```

```
영어 평균 점수: 92.5
수학 평균 점수: 94.5
```

<br>

### Operator 함수적 인터페이스

- Operator 함수적 인터페이스는 Function과 동일하게 매개 변수와 리턴값이 있는 applyXXX() 메소드를 가지고 있다. 하지만 이 메소드들은 매개값을 리턴값으로 매핑(타입 변환)하는 역할보다는 매개값을 이용해서 연산을 수행한 후 동일한 타입으로 리턴값을 재공하는 것

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55771837-8f8d4200-5ac4-11e9-817a-b757141a78c8.png'>
</p>
<br>

> IntBinaryOperator 인터페이스를 타켓 타입으로 하는 람다식

- `applyAsInt()` 메소드는 매개값으로 두 개의 int르 가지므로 람다식도 두 개의 int 매개 변수 a와 b를 사용한다. 그리고 `applyAsInt()` 메소드의 리턴 타입이 int이므로 람다식의 중괄호 `{}`의 리턴값은 int가 된다.

- 두개의 int를 연산해서 결과값으로 int를 리턴

```java
InBinaryOperator operator = (a, b) -> { ...; }
```

<br>

> OperatorExample, Operator 함수적 인터페이스

- 다음 예제는 int[] 배열에서 최대값과 최소값을 얻는다.

```java
import java.util.function.IntBinaryOperator;

public class OperatorExample {
    private static int[] scores =  { 92, 95, 87};

    public static int maxOrMin(IntBinaryOperator operator) {
        int result = scores[0];
        for (int score : scores) {
            result = operator.applyAsInt(result, score);
        }
        return result;
    }

    public static void main(String[] args) {
        // 최대값 얻기
        int max = maxOrMin(
                (a, b) -> {
                    if (a >= b) return a;
                    else  return b;
                }
        );
        System.out.println("최대값: " + max);

        // 최소값 얻기
        int min = maxOrMin(
                (a, b) -> {
                    if (a <= b) return a;
                    else return b;
                }
        );
        System.out.println("최소값: " + min);
    }
}
```

```
최대값: 95
최소값: 87
```

<br>

### Predicate 함수적 인터페이스

- Predicate 함수적 인터페이스는 매개 변수와 boolean 리턴값이 있는 testXXX() 메소드를 가지고 있다. 이 메소드들은 매개값을 조사해서 true 또는 false를 리턴하는 역할을 한다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55771839-8f8d4200-5ac4-11e9-9168-e48fab087f99.png'>
</p>
<br>

```java
Predicate<Student> predicate = t -> { return t.getSex().equals("남자"); }
또는
Predicate<Student> predicate = t -> t.getSex().equals("남자");
```

<br>

- List에 저장된 남자 또는 여자 학생들의 평균 점수를 출력

- avg() 메소드는 Predicate<Student> 매개 변수를 가지고 있다. 따라서 avg() 메소드를 호출할 때 매개값으로 람다식을 사용할 수 있다.

> PredicateExample, Predicate 함수적 인터페이스

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicateExample {
    private static List<Student> list = Arrays.asList(
            new Student("홍길동", "남자", 90),
            new Student("김순희", "여자", 90),
            new Student("감자바", "남자", 95),
            new Student("박한나", "여자", 92)
    );
    
    public static double avg(Predicate<Student> predicate) {
        int count = 0, sum = 0;
        for(Student student : list) {
            if (predicate.test(student)) {
                count++;
                sum += student.getScore();
            }
        }
        return (double) sum / count;
    }
    
    public static void main(String[] args) {
        double maleAvg = avg(t -> t.getSex().equals("남자"));
        System.out.println("남자 평균 점수: " + maleAvg);
        
        double femaleAvg = avg(t -> t.getSex().equals("여자"));
        System.out.println("여자 평균 점수: " + femaleAvg);
    }
}
```

> Student 클래스

```java
public class Student {
    private String name;
    private String sex;
    private int score;

    public Student(String name, String sex, int score) {
        this.name = name;
        this.sex = sex;
        this.score = score;
    }

    public String getSex() { return sex; }
    public int getScore() { return score; }
}
```

```
남자 평균 점수: 92.5
여자 평균 점수: 91.0
```

<br>

### andThen()과 compose() 디폴트 메소드

> `andThen()`

```java
인터페이스AB = 인터페이스A.andThen(인터페이스B);
최종결과 = 인터페이스AB.method();
```

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55783956-82834980-5aea-11e9-9afd-8107f4b95102.png'>
</p>
<br>

- 인터페이스AB의 method를 호출하면 우선 인터페이스A부터 처리하고 결과를 인터페이스B의 매개값으로 제공한다. 인터페이스B는 제공받은 매개값을 가지고 처리한 후 최종 결과를 리턴한다. 

<br>

> `compose()`

```java
인터페이스AB = 인터페이스A.compose(인터페이스B);
최종결과 = 인터페이스AB.method();
```

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55783954-81eab300-5aea-11e9-82ae-21dc8fa630b0.png'>
</p>
<br>

- 인터페이스AB의 method()를 호출하면 우선 인터페이스B부터 처리하고 결과를 인터페이스A의 매개값으로 제공한다. 인터페이스A는 제공받은 매개값을 가지고 처리한 후 최종 결과를 리턴한다.

<br>

> `andThen()`과 `compose()` 디폴트 메소드를 제공하는 `java.util.function` 패키지의 함수적 인터페이스들

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55783953-81eab300-5aea-11e9-95cf-d89aad5c6511.png
'>
</p>
<br>

#### Consumer의 순차적 연결

> ConsumerAndThenExample, Consumer의 순차적 연결

```java
import javax.swing.*;
import java.util.function.Consumer;

public class ConsumerAndThenExample {
    public static void main(String[] args) {
        Consumer<Member> consumerA = (m) -> {
            System.out.println("consumerA: " + m.getName());
        };

        Consumer<Member> consumerB = (m) -> {
            System.out.println("consumerB: " + m.getId());
        };

        Consumer<Member> consumerAB = consumerA.andThen(consumerB);
        consumerAB.accept(new Member("홍길동", "hong", null));
    }
}
```

> Member, 회원 클래스

```java
public class Member {
    private String name;
    private String id;
    private Address address;

    public Member(String name, String id, Address address) {
        this.name = name;
        this.id = id;
        this.address = address;
    }

    public String getName() { return  name; }
    public String getId() { return id; }
    public Address getAddress() { return address; }
}
```

> Address, 주소 클래스

```java
public class Address {
    private String country;
    private String city;

    public Address(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public String getCountry() { return country; }
    public String getCity() { return city; }
}
```

```
consumerA: 홍길동
consumerB: hong
```

<br>

#### function의 순차적 연결

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55786661-a72df000-5aef-11e9-967e-c29dcd558f63.png'>
</p>
<br>

- Address는 두 함수적 인터페이스 간의 전달 데이터이다. Address는 내부적으로 전달되기 때문에 최종 함수적 인터페이스의 형태는 입력 데이터가 Member, 출력 데이터가 String이 되는 Function<Member, String>이 된다.

> FunctionAndThenComposeExample, Function의 순차적 연결

```java
import java.util.function.Function;

public class FunctionAndThenComposeExample {
    public static void main(String[] args) {
        Function<Member, Address> functionA;
        Function<Address, String> functionB;
        Function<Member, String> functionAB;
        String city;

        functionA = (m) -> m.getAddress();
        functionB = (a) -> a.getCity();

        functionAB = functionA.andThen(functionB);
        city = functionAB.apply(
                new Member("홍길동", "hong", new Address("한국", "서울"))
        );
        System.out.println("거주 도시: " + city);

        functionAB = functionB.compose(functionA);
        city = functionAB.apply(
                new Member("홍길동", "hong", new Address("한국", "서울"))
        );
        System.out.println("거주 도시: " + city);
    }
}
```

```
거주 도시: 서울
거주 도시: 서울
```

<br>

### `and(), or(), negate()` 디폴트 메소드와 `isEqual()` 정적 메소드

- Predicate 종류의 함수적 인터페이스는 and(), or(), negate() 디폴트 메소드를 가지고 있다.

- 이 메소드들은 각각 논리 연산자인 `&&, ||, !` 과 대응된다고 볼 수 있다.

<br>

> PredicateAndOrNegateExample, Predicate 간의 논리 연산

```java
import java.util.function.IntPredicate;

public class PredicateAndOrNegateExample {
    public static void main(String[] args) {
        // 2의 배수 검사
        IntPredicate predicateA = a -> a % 2 == 0;

        // 3의 배수 검사
        IntPredicate predicateB = b -> b % 3 == 0;

        IntPredicate predicateAB;
        boolean result;

        // and()
        predicateAB = predicateA.and(predicateB);
        result = predicateAB.test(9);
        System.out.println("9는 2와 3의 배수입니까? " + result);

        // or()
        predicateAB = predicateA.or(predicateB);
        result = predicateAB.test(9);
        System.out.println("9는 2또는 3의 배수입니까? " + result);

        // negate()
        predicateAB = predicateA.negate();
        System.out.println("9는 홀수입니까? " + result);
    }
}
```

```
9는 2와 3의 배수입니까? false
9는 2또는 3의 배수입니까? true
9는 홀수입니까? true
```

<br>

- Predicate<T> 함수적 인터페이스는 `and(), or(), negate()` 디폴트 메소드 이외에 `isEqual()` 정적 메소드를 추가로 제공하낟.

- **`isEqual()`** 메소드는 `test()` 매개값인 `sourceObject`와 `isEqual()`의 매개값인 `targetObject`를 `java.util.Objects` 클래스의 `equals()`의 매개값으로 제공하고, `Objects.equals(sourceObject, targetObject)`의 리턴값을 얻어 **새로운 `Predicate<T>`를 생성한다.**

```java
Predicate<Object> predicate = Predicate.isEqual(targetObject);
boolean result = predicate.test(sourceObject);
```

- `Objects.equals(sourceObject, targetObject)` 는 다음과 같은 리턴값을 제공한다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55788672-b2831a80-5af3-11e9-9735-5c2dc54ed5bb.png'>
</p>
<br>

> Predicate의 `isEqual()` 정적 메소드를 사용한 두 문자열 비교

```java
import java.util.function.Predicate;

public class PredicateIsEqualExample {
    public static void main(String[] args) {
        Predicate<String> predicate;

        predicate = Predicate.isEqual(null);
        System.out.println("null, null: " + predicate.test(null));

        predicate = Predicate.isEqual("Java8");
        System.out.println("null, Java8: " + predicate.test(null));

        predicate = Predicate.isEqual(null);
        System.out.println("Java8, null: " + predicate.test("Java8"));

        predicate = Predicate.isEqual("Java8");
        System.out.println("Java8, Java8: " + predicate.test("Java8"));

        predicate = Predicate.isEqual("Java8");
        System.out.println("Java7, Java8: " + predicate.test("Java7"));
    }
}
```

```
null, null: true
null, Java8: false
Java8, null: false
Java8, Java8: true
Java7, Java8: false
```


<br>

### `minBy(), maxBy()` 정적 메소드

- BinaryOperator<T> 함수적 인터페이스는 minBy()와 maxBy() 정적 메소드를 제공한다. 이 두 메소드는 매개값으로 제공되는 Comparator를 이용해서 최대 T와 최소 T를 얻는 `BinaryOperator<T>`를 리턴한다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55788670-b2831a80-5af3-11e9-97d8-24114586ca45.png'>
</p>
<br>

- `Comparator<T>` 는 다음과 같이 선언된 함수적 인터페이스이다.

    - o1과 o2를 비교해서 o1이 작으면 음수, o1과 o2가 동일하면 0을, o1이 크면 양수를 리턴하는 compare() 메소드가 선언되어 있다.

```java
@FunctionalInterface
public interface Comparator<T> {
    public int compare(T o1, T o2);
}
```

- `Comparator<T>`를 타겟 타입으로 하는 람다식은 다음과 같이 작성할 수 있다.

```java
(o1, o2) -> { ...; return int값; }
```

- 만약 o1과 o2가 int 타입이라면 다음과 같이 `Integer.compare(int, int)` 메소드를 이용할 수 있다.

```java
(o1, o2) -> Integer.compare(o1, o2);
```

<br>

> OperatorMinByMaxByExample, `minBy, maxBy()` 정적 메소드

- 두 과일의 값을 비교햐서 값이 낮거나 높은 과일을 얻어낸다.

```java
import java.util.function.BinaryOperator;

public class OperatorMinByMaxByExample {
    public static void main(String[] args) {
        BinaryOperator<Fruit> binaryOperator;
        Fruit fruit;

        binaryOperator = BinaryOperator.minBy( (f1, f2) -> Integer.compare(f1.price, f2.price));
        fruit = binaryOperator.apply(new Fruit("딸기", 6000), new Fruit("수박", 10000));
        System.out.println(fruit.name);

        binaryOperator = BinaryOperator.maxBy( (f1, f2) -> Integer.compare(f1.price, f2.price));
        fruit = binaryOperator.apply(new Fruit("딸기", 6000), new Fruit("수박", 10000));
        System.out.println(fruit.name);
    }
}
```

> Fruit 클래스

```java
public class Fruit {
    String name;
    int price;

    public Fruit(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return this.name; }
    public int getPrice() { return this.price; }
}
```

```
딸기
수박
```

<br>

## 메소드 참조

- `메소드 참조(Method References)`는 말 그대로 메소드를 참조해서 매개 변수의 정보 및 리턴 타입을 알아내어, **람다식에서 불필요한 매개 변수를 제거하는 것이 목적**이다.

  - 람다식은 종종 기존 메소드를 단순히 호출만 하는 경우가 많다.

<br>

> 예를 들어 두 개의 값을 받아 큰 수를 리턴하는 Math 클래스의 max() 정적 메소드를 호출하는 람다식은 다음과 같다.

```java
(left, right) -> Math.max(left, right);
```

- 람다식은 단순히 두 개의 값을 Math.max() 메소드의 매개값으로 전달하는 역할만 하기 때문에 다소 불편해 보인다. 이 경우에는 다음과 같이 메소드 참조를 이용하면 매우 깔끔하게 처리할 수 있다.

```java
Math :: max;
```

- 메소드 참조도 람다식과 마찬가지로 **인터페이스의 익명 구현 개체로 생성**되므로 **타겟 타입인 인터페이스의 추상 메소드가 어떤 매개 변수를 가지고, 리턴 타입이 무엇인가에 따라 달라진다.**

- 메소드 참조는 정적 또는 인스턴스 메소드를 참조할 수 있고, 생성자 참조도 가능하다.

<br>

### 정적 메소드와 인스턴스 메소드 참조

- `정적(static) 메소드`를 참조할 경우에는 클래스 이름 뒤에 `::` 기호를 붙이고 정적 메소드 이름을 기술한다.

```java
클래스 :: 메소드
```

- `인스턴스 메소드`일 경우에는 먼저 객체를 생성한 다음 참조 변수 뒤에 `::` 기호를 붙이고 인스턴스 메소드 이름을 기술하면 된다.

```
참조변수 :: 메소드
```

<br>

> Calculator, 정적 및 인스턴스 메소드

```java
public class Calculator {
    // 정적 메소드
    public static int staticMethod(int x, int y) {
        return x + y;
    }

    // 인스턴스 메소드
    public int instanceMethod(int x, int y) {
        return x + y;
    }
}
```

> MethodReferenceExample, 정적 및 인스턴스 메소드 참조

```java
import java.util.function.IntBinaryOperator;

public class MethodReferencesExample {
    public static void main(String[] args) {
        IntBinaryOperator operator;

        // 정적 메소드 참조
        operator = (x, y) -> Calculator.staticMethod(x, y);
        System.out.println("결과1: " + operator.applyAsInt(1, 2));

        operator = Calculator :: staticMethod;
        System.out.println("결과2: " + operator.applyAsInt(3, 4));

        // 인스턴스 메소드 참조
        Calculator obj = new Calculator();
        operator = (x, y) -> obj.instanceMethod(x, y);
        System.out.println("결과3: " + operator.applyAsInt(5, 6));

        operator = obj :: instanceMethod;
        System.out.println("결과4: " + operator.applyAsInt(7, 8));
    }
}

```

<br>

### 매개 변수의 메소드 참조

- 메소드는 람다식 외부의 클래스 멤버일 수도 있고, 람다식에서 제공되는 매개 변수의 멤버일 수도 있다.

    - 이전 예제는 람다식 외부의 클래스 멤버인 메소드를 호출하였다. 그러나 다음과 같이 람다식에서 제공하는 a 매개 변수의 메소드를 호출해서 b 매개 변수를 매개값으로 사용하는 경우도 있다.

```java
(a, b) -> { a.instanceMethod(b); }
```

- 이것을 메소드 참조로 표현하면 다음과 같다.

    - a 클래스 이름 뒤에 `::` 기호를 붙이고 메소드 이름을 기술하면 된다.

    - 작성 방법은 정적 메소드 참조와 동일하지만, a의 인스턴스 메소드가 참조되므로 전혀 다른 코드가 실행된다.

```java
클래스 :: instanceMethod
```

<br>

> ArgumentMethodReferenceExample, 매개 변수의 메소드 참조

```java
import java.util.function.ToIntBiFunction;

public class ArgumentMethodReferencesExample {
    public static void main(String[] args) {
        ToIntBiFunction<String, String> function;

        function = (a, b) -> a.compareToIgnoreCase(b);
        print(function.applyAsInt("Java8", "JAVA8"));

        function = String::compareToIgnoreCase;
        print(function.applyAsInt("Java8", "JAVA8"));
    }

    public static void print(int order) {
        if (order < 0) {
            System.out.println("사전순으로 먼저 옵니다.");
        } else if (order == 0) {
            System.out.println("동일한 문자열입니다.");
        } else {
            System.out.println("사전순으로 나중에 옵니다.");
        }
    }
}
```

```
동일한 문자열입니다.
동일한 문자열입니다.
```

<br>

### 생성자 참조

- **메소드 참조(method references)는 생성자 참조도 포함**된다. **생성자를 참조한다는 것은 `객체 생성`을 의미**한다.

- 단순히 메소드 호출로 구성된 람다식을 메소드 참조로 대치할 수 있듯, 단순히 객체를 생성하고 리턴하도록 구성된 람다식은 생성자 참조로 대치할 수 있다.

> 단순히 객체 생성 후 리턴하는 람다식

```java
(a, b) -> { return new 클래스(a, b); }
```

- 이 경우, 생성자 참조로 표현하면 다음과 같다.

    - 클래스 이름 뒤에 `::` 기호를 붙이고 new 연산자를 기술하면 된다.

    - ★ **`생성자가 오버로딩되어 여러 개가 있을 경우`, 컴파일러는 함수적 인터페이스의 추상 메소드와 동일한 매개 변수 타입과 개수를 가지고 있는 생성자를 찾아 실행한다. 만약 해당 생성자가 존재하지 않으면 컴파일 오류가 발생한다.**

```java
클래스 :: new
```

<br>

> ConstructorReferencesExample, 생성자 참조

```java
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConstructorReferencesExample {
    public static void main(String[] args) {
        Function<String, Member> function1 = Member :: new;
        Member member1 = function1.apply("angel");

        BiFunction<String, String, Member> function2 = Member :: new;
        Member member2 = function2.apply("김천사", "angel");
    }
}
```

> Member, 생성자 오버로딩

```java
public class Member {
    private String name;
    private String id;

    public Member() {
        System.out.println("Member() 실행");
    }
    public Member(String id) {
        System.out.println("Member(String id) 실행");
        this.id = id;
    }
    public Member(String name, String id) {
        System.out.println("Member(String name, String id) 실행");
        this.name = name;
        this.id = id;
    }

    public String getId() { return id; }
}
```

```
Member(String id) 실행
Member(String name, String id) 실행
```
