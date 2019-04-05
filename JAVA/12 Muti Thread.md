# 챕터 12. 멀티 스레드 (Multi Tread)

## 멀티 스레드 개념

### 프로세스와 스레드

- 프로세스(process) : 실행 중인 하나의 애플리케이션
    
    - 하나의 애플리케이션은 다중 프로세스를 만들기도 한다. `크롬 브라우저를 두 개 실행하면 두 개의 크롬 프로세스가 생성`

- 멀티 태스킹(multi tasking) : 두 가지 이상의 작업을 동시에 처리하는 것, 운영체제는 멀티 태스킹을 할 수 있도록 CPU 및 메모리 자원을 프로세스마다 적절히 할당해주고, 병렬로 실행시킨다. 

   - 멀티 프로세스 : 애플리케이션 단위의 멀티 태스킹
     
     - 운영체제에서 할당받은 자신의 메모리를 가지고 실행하기 때문에 서로 독립적. 따라서 하나의 프로세스에서 오류가 발생해도 다른 프로세스에게 영향을 미치지 않음. 
   
   - 멀티 스레드 : 애플리케이션 내부에서의 멀티 태스킹

     - 하나의 프로세스 내부에서 생성되기 때문에 하나의 스레드가 예외를 발생시키면 프로세스 자체가 종료될 수 있어 다른 스레드에게 영향을 미친다. → 그렇기 때문에 멀티 스레드에서는 **예외 처리에 만전**을 기해야 한다.

<br>

### 메인 스레드

- 모든 자바 애플리케이션은 메인 스레드가 main() 메소드를 실행하면서 시작되고, main() 메소드의 마지막 코드를 실행하거나 return문을 만나면 실행이 종료된다.

- 메인 스레드는 필요에 따라 작업 스레드들을 만들어서 병렬로 코드를 실행할 수 있다. 즉 메인스레드는 멀티 스레드를 생성해서 멀티 태스킹을 수행한다.

    - 싱글 스레드 애플리케이션에서는 메인 스레드가 종료하면 프로세스도 종료된다.

    - 하지만 멀티 스레드 애플리케이션에서는 실행 중인 스레드가 하나라도 있디먄, 프로세스는 종료되지 않는다.

<br>

## 작업 스레드 생성과 실행

- `애플리케이션의 구조를 생각하자!` : 멀티 스레드로 실행하는 애플리케이션을 개발하려면 먼저 몇 개의 작업을 병렬로 실행할지 결정하고, 각 작업별로 스레드를 생성해야 한다.

- 어떤 자바 애플리케이션이건 메인 스레드는 반드시 존재하므로 메인 작업 이외에 추가적인 병렬 작업의 수만큼 스레드를 생성하면 된다.

    - 자바에서는 작업 스레드도 객체로 생성되기 때문에 클래스가 필요

    - `java.lang.Tread` 클래스를 **직접 객체화**해서 생성해도 되지만, **Thread를 상속**해서 하위 클래스를 만들어 생성할 수도 있다.

<br>

### Thread 클래스로부터 "직접" 생성

- `java.lang.Tread` 클래스로부터 작업 스레드 객체를 직접 생성하려면 다음과 같이 **`Runnable`을 매개값으로 갖는 생성자를 호출**해야 한다.

```java
Thread thread = new Thread(Runnable target);
```

- `Runnable`은 작업 스레드가 실행할 수 있는 코드를 가지고 있는 객체라고 해서 붙여진 이름이다. Runnable은 인터페이스 타입이기 때문에 구현 객체를 만들어 대입해야 한다.

- **Runnable에는 run() 메소드 하나가 정의되어 있는데, 구현 클래스는 run()을 재정의해서 작업 스레드가 실행할 코드를 작성해야 한다.**

> Runnable 구현 클래스

```java
class task implements Runnable {
    public void run() {
        스레드가 실행할 코드;
    }
}
```

- Runnable은 작업 내용을 가지고 있는 객체일 뿐, 실제 스레드는 아니다.

- Runnable 구현 객체를 생성한 후, 이것을 매개값으로 해서 Thread 생성자를 호출하면 비로소 작업 스레드가 생성된다. 

```java
Runnable task = new Task();
           │
           └────────────────┐
                            ↓
Thread thread = new Thread(task);
```

- 코드를 좀 더 절약하기 위해 Thread 생성자를 호출할 때 Runnable 익명 객체를 매개값으로 사용할 수 있다. (이 방법이 더 많이 사용된다.)

- ***익명 객체*** 는 단독으로 생성이 불가능하고, 클래스를 상속하거나 인터페이스를 구현해야만 생성이 가능하다.

    - ① `필드의 초기값`, ② `로컬 변수의 초기값`, ③ `매개 변수의 매개값`으로 주로 대입한다.

    - UI 이벤트 처리, 스레드 객체를 간편하게 생성할 목적으로 사용

```java
Thread thread = new Thread(new Runnable() {
    public void run() {
        스레드가 실행할 코드;
    }
});
```
 
- Runnable 인터페이스는 run() 메소드 하나만 정의되어 있기 때문에 함수적 인터페이스이다.

- 따라서 다음과 같이 람다식을 매개값으로 사용할 수도 있다. (자바 8부터 지원)

```java
Thread thread = new Thread(() -> {
    스레드가 실행할 코드;
});
```

- 작업 스레드는 생성되는 즉시 실행되지 않고, `start()` 메소드를 다음과 같이 호출해야만 비로소 실행된다.

```java
thread.start();
```

- start() 메소드가 호출되면, 작업 스레드는 매개값으로 받은 Runnable의 run() 메소드를 실행하면서 자신의 작업을 처리한다.

<br>

> 메인 스레드만 이용한 경우

- 비프음이 5번 발생 뒤, 프린팅이 5회 발생한다.

- 비프음 발생과 프린팅은 서로 다른 작업이므로 메인 스레드가 동시에 두 가지 작업을 처리할 수 없다. 

```java
import java.awt.*;

public class BeepPrintExample {
    public static void main(String[] args) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();  // Toolkit 객체 얻기
        for (int i = 0; i < 5; i++) {
            toolkit.beep();  // 비프음 발생
            try {
                Thread.sleep(500);
            } catch (Exception e) {}
        }

        for (int i = 0; i < 5; i++) {
            System.out.println("띵");
            try {Thread.sleep(500);} catch (Exception e) {}
        }
    }
}
```

- 비프음을 발생시키면서 동시에 프린팅을 하려면 두 작업 중 하나를 메인 스레드가 아닌 다른 스레드에서 실행시켜야 한다.

- 프린팅은 메인 스레드가 담당하고 비프음은 작업 스레드가 담당하도록 수정한다.

- 작업을 정의하는 Runnable 구현 클래스를 다음과 같이 작성한다.

> 비프음을 들려주는 작업 정의

```java
import java.awt.*;

public class BeepTask implements Runnable {
    public void run() {
        // 스레드 실행 내용
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        for (int i = 0; i < 5; i++) {
            toolkit.beep();  // 비프음 발생
            try { Thread.sleep(500); } catch (Exception e) {}
        }
    }
}
```

> 메인 스레드와 작업 스레드가 동시에 실행

```java
import java.awt.*;

public class BeepPrintExample2 {
    public static void main(String[] args) {  //  main 스레드
        // 방법 ① : BeepTask 객체 생성 후, 이것을 매개값으로 작업 스레드를 생성
        Runnable beepTask = new BeepTask();
        Thread thread = new Thread(beepTask);
        thread.start();  // 작업 스레드의 start

        for (int i = 0; i < 5; i++) {
            System.out.println("띵");
            try {Thread.sleep(500);} catch (Exception e) {}
        }
    }
}
```

- BeepTask 객체 생성 후, 이것을 매개값으로 작업 스레드를 생성하는 2라인을 대체하여 작업 스레드를 만들 수 있는 `또다른 두 가지 방법`을 살펴보자.

> Runnable 익명 객체 이용

```java
import java.awt.*;

public class BeepPrintExample2 {
    public static void main(String[] args) {  //  main 스레드

        // 방법 ② : Runnable 익명 객체 이용
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                for (int i = 0; i < 5; i++) {
                    toolkit.beep();
                    try { Thread.sleep(500); } catch (Exception e) {}  // .sleep() 의 InterruptedException 예외 처리
                }
            }
        });

        thread.start();

        for (int i = 0; i < 5; i++) {
            System.out.println("띵");
            try {Thread.sleep(500);} catch (Exception e) {}
        }
    }
}
```

> 람다식 이용

```java
import java.awt.*;

public class BeepPrintExample2 {
    public static void main(String[] args) {  //  main 스레드
        // 방법 ③ : 람다식(Lambda) 이용
        Thread thread = new Thread(() -> {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            for (int i = 0; i < 5; i++) {
                toolkit.beep();
                try { Thread.sleep(500); } catch (Exception e) { }
            }
        });

        thread.start();

        for (int i = 0; i < 5; i++) {
            System.out.println("띵");
            try {Thread.sleep(500);} catch (Exception e) {}
        }
    }
}
```

<br>

### Thread "하위 클래스로부터" 생성

- 작업 스레드가 실행할 작업을 Runnable로 만들지 않고, Thread의 하위 클래스로 작업 스레드를 정의하면서 작업 내용을 포함시킬 수도 있다.

> 비프음을 들려주는 스레드

```java
import java.awt.*;

public class BeepThread extends Thread {
    @Override
    public void run() {
        // 스레드 실행 내용
        // super.run();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        for (int i = 0; i < 5; i++) {
            toolkit.beep();
            try { Thread.sleep(500); } catch (Exception e) {}
        }
    }
}
```

> 메인 스레드와 작업 스레드가 동시에 실행

```java
public class BeepPrintExample3 {
    public static void main(String[] args) {
        Thread thread = new BeepThread();
        thread.start();  // BeepTread의 run() 메소드 호출
        
        for (int i = 0; i < 5; i++) {
            System.out.println("띵");
            try { Thread.sleep(500); } catch (Exception e) {}
        }
    }
}
```

> 'BeepThread 객체 생성'을 대체하여 작업 스레드를 만드는 또 다른 방법

```java
import java.awt.*;

public class BeepPrintExample3 {
    public static void main(String[] args) {
        // Thread thread = new BeepThread(); 을 대체하는 또 다른 방법

        Thread thread = new Thread() {
            @Override
            public void run() {
                // super.run();
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                for (int i = 0; i < 5; i++) {
                    toolkit.beep();
                    try { Thread.sleep(500); } catch (Exception e) {}
                }
            }
        };
        thread.start();

        for (int i = 0; i < 5; i++) {
            System.out.println("띵");
            try { Thread.sleep(500); } catch (Exception e) {}
        }
    }
}
```

<br>

### 스레드의 이름

- 스레드는 자신의 이름을 가지고 있다.

- 스레드의 이름이 큰 역할을 하는 것은 아니지만, 디버깅할 때 어떤 스레드가 어떤 작업을 하는지 조사할 목적으로 가끔 사용된다.

  - 메인 스레드는 `main`이라는 이름을 가지고 있고, 우리가 직접 생성한 스레드는 자동적으로 `Thread-n`이라는 이름으로 설정된다. (`n`은 스레드의 번호)

- Thread-n 대신 **다른 이름으로 설정하고 싶다면** Thread 클래스의 `setName()` 메소드로 변경하면 된다.

```java
thread.setName("스레드 이름");
```

- 반대로 스레드 **이름을 알고 싶을 경우**에는 `getName()` 메소드를 호출하면 된다.

```java
thread.getName();
```

- `setName()`과 `getName()` 은 Thread의 인스턴스 메소드이므로 스레드 객체의 참조가 필요하다. 만약 스레드 객체의 참조를 가지고 있지 않다면, **Thread의 정적 메소드인 `currentThread()`** 로 코드를 실행하는 현재 스레드의 참조를 얻을 수 있다.

```java
Thread thread = Thread.currentThread();
```

<br>

> 메인 스레드 이름 출력 및 UserThread 생성 및 시작

```java
public class ThreadNameExample {
    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();  // 이 코드를 실행하는 스레드 객체 얻기
        System.out.println("프로그램 시작 스레드 이름: " + mainThread.getName());

        Thread threadA = new ThreadA();  // ThreadA 생성
        System.out.println("작업 스레드 이름: " + threadA.getName());
        threadA.start();

        Thread threadB = new ThreadB();  // ThreadB 생성
        System.out.println("작업 스레드 이름: " + threadB.getName());
        threadB.start();
    }
}
```

> ThreadA 클래스

```java
public class ThreadA extends Thread {
    public ThreadA() {
        setName("ThreadA");  // 스레드 이름 설정
    }

    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            System.out.println(getName() + "가 출력한 내용");  // ThreadA 실행 내용
        }
    }
}
```

> ThreadB 클래스

```java
public class ThreadB extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            System.out.println(getName() + "가 출력한 내용");  // ThreadA 실행 내용
        }
    }
}
```

```
프로그램 시작 스레드 이름: main
작업 스레드 이름: ThreadA
작업 스레드 이름: Thread-1
ThreadA가 출력한 내용
ThreadA가 출력한 내용
Thread-1가 출력한 내용
Thread-1가 출력한 내용
```

```
프로그램 시작 스레드 이름: main
작업 스레드 이름: ThreadA
작업 스레드 이름: Thread-1
ThreadA가 출력한 내용
Thread-1가 출력한 내용
ThreadA가 출력한 내용
Thread-1가 출력한 내용
```

```
프로그램 시작 스레드 이름: main
작업 스레드 이름: ThreadA
작업 스레드 이름: Thread-1
Thread-1가 출력한 내용
Thread-1가 출력한 내용
ThreadA가 출력한 내용
ThreadA가 출력한 내용
```

```
// ThreadA와 ThreadB 생성 사이에 try { Thread.sleep(500); } catch (Exception e) { } 추가
// 책에서의 예제가 위의 추가 코드 없이 이러한 결과가 나옴.

프로그램 시작 스레드 이름: main
작업 스레드 이름: ThreadA
ThreadA가 출력한 내용
ThreadA가 출력한 내용
작업 스레드 이름: Thread-1
Thread-1가 출력한 내용
Thread-1가 출력한 내용
```

<br>

## 스레드 우선 순위

- 멀티 스레드는 동시성(Concurrency) 또는 병렬성(Parallelism)으로 실행되기 때문에 이 용어들에 대해 정확히 이해하는 것이 좋다.

- `동시성` : 멀티 작업을 위해 하나의 코어에서 멀티 스레드가 번갈아가며 실행하는 성질

- `병렬성` : 멀티 작업을 위해 멀티 코어에서 개별 스레드를 동시에 실행하는 성질

    - 싱글 코어 CPU를 이용한 멀티 스레드 작업은 병렬적으로 실행되는 것처럼 보이지만, 사실은 번갈아가며 실행하는 동시성 작업이다.

    - 번갈아 실행하는 것이 워낙 빠르다보니 병렬성으로 보일 뿐이다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55465735-b963e600-5638-11e9-8e0e-ae06bc5c7547.png'>
</p>
<br>

- 스레드의 개수가 코어의 수보다 많을 경우, 스레드를 어떤 순서에 의해 동시성으로 진행할 것인가를 결정해야 하는데, 이것을 **스레드 스케줄링**이라고 한다.

- 스레드 스케줄링에 의해 스레드들은 아주 짧은 시간에 번갈아가면서 그들의 `run()` 메소드를 조금씩 실행한다.

<br>

> 자바의 스레드 스케줄링의 두 가지 방식

1. 우선순위(Priority) 방식

    - 우선순위가 높은 스레드가 실행 상태를 더 많이 가지도록 스케줄링하는 것

    - 스레드 객체에 우선순위를 부여하므로 개발자가 코드로 제어 가능

      - 1 ~ 10까지의 우선 순위가 부여. 1이 가장 우선순위가 낮고, 10이 가장 높다. 우선순위를 부여하지 않으면 모든 스레드들은 기본적으로 5의 우선순위를 할당 받는다.

    - 우선 순위를 변경하고 싶다면 Thread 클래스가 제공하는 `setPriority()` 메소드를 이용한다.

    ```java
    thread.setPriority(우선순위);
    ```

    - 우선순위의 매개값으로 1~10까지의 값을 직접 주어도 되지만, 코드의 가독성을 위해 Thread 클래스의 상수를 사용할 수 있다.

    ```java
    thread.setPriority(Thread.MAX_PRIORITY);    // 10
    thread.setPriority(Thread.NORM_PRIORITY);   // 5
    thread.setPriority(Thread.MIN_PRIORITY);    // 1
    ```

2. 순환 할당(Round-Robin) 방식

    - 시간 할당량(Time Slice)을 정해서 하나의 스레드를 정해진 시간만큼 실행하고 다시 다른 스레드를 실행하는 방식

    - JVM에 의해 정해지므로 코드로 제어가 불가능

<br>

## 동기화 메소드와 동기화 블록

### 공유 객체를 사용할 때의 주의할 점

- 싱글 스레드 프로그램에서는 한 개의 스레드가 객체를 독차지해서 사용하면 되지만, 멀티 스레드 프로그램에서는 **객체를 공유**해서 작업해야 하는 경우가 있다.

- 이 경우, 스레드 A를 사용하던 객체가 스레드 B에 의해 상태가 변경될 수 있기 때문에 스레드 A가 **의도했던 것과는 다른 결과를 산출**할 수도 있다.

> MainThreadExample, 메인 스레드가 실행하는 코드

```java
public class MainThreadExample {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        User1 user1 = new User1();        // User1 스레드 설정
        user1.setCalculator(calculator);  // 공유 객체 설정
        user1.start();

        User2 user2 = new User2();        // User2 스레드 설정
        user2.setCalculator(calculator);  // 공유 객체 설정
        user2.start();
    }
}
```

> Calculator, 공유 객체

```java
public class Calculator {
    private int memory;

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        System.out.println(Thread.currentThread().getName() + ": " + this.memory);
    }
}
```

> User1, User1 스레드

```java
public class User1 extends Thread {
    private  Calculator calculator;

    public void setCalculator(Calculator calculator) {
        this.setName("User1");  // 스레드 이름을 User1로 설정
        this.calculator = calculator;  // 공유 갹체인 Calculator를 필드에 저장
    }

    @Override
    public void run() {
        calculator.setMemory(100);  // 공유 객체인 Calculator의 메모리에 100 저장
    }
}
```

> User2, User2 스레드

```java
public class User2 extends Thread {
    private  Calculator calculator;

    public void setCalculator(Calculator calculator) {
        this.setName("User2");  // 스레드 이름을 User2로 설정
        this.calculator = calculator;  // 공유 갹체인 Calculator를 필드에 저장
    }

    @Override
    public void run() {
        calculator.setMemory(50);  // 공유 객체인 Calculator의 메모리에 100 저장
    }
}
```

> User1은 필드에 100을 저장했지만, User2 스레드가 변경한 memory 필드값 50이 나온다.

```
User1: 50
User2: 50
```

<br>

### 동기화 메소드 및 동기화 블록

- 스레드가 사용 중인 객체를 다른 스레드가 변경할 수 없도록 하려면 스레드 작업이 끝날 때까지 객체에 잠금을 걸어서 다른 스레드가 사용할 수 없도록 해야 한다.

- 멀티 스레드 프로그램에서 단 하나의 스레드만 실행할 수 있는 코드 영역을 **임계 영역(critical section)**이라고 한다.

    - 자바는 임계 영역을 지정하기 위해 **동기화(syncronized) 메소드와 동기화 블록**을 제공한다.

    - 스레드가 객체 내부의 동기화 메소드 또는 블록에 들어가면 즉시 객체에 잠금을 걸어 다른 스레드가 임계 영역 코드를 실행하지 못하도록 한다.

    - `동기화 메소드를 만드는 방법` : 매소드 선언에 **`synchronized`** 키워드를 붙인다. 인스턴스와 정적 메소드 어디든 사용 가능하다.

```java
public synchronized void method() {
    임계 영역;  // 단 하나의 스레드만 실행
}
```

- **`동기화 메소드`** 는 **메소드 전체 내용이 임계 영역**이므로 스레드가 동기화 메소드를 실행하는 즉시 객체에 잠금이 일어나고, 스레드가 동기화 메소드를 실행 종료하면 잠금이 풀린다.

- 메소드 전체 내용이 아니라, **일부 내용만 임계 영역으로** 만들고 싶다면 다음과 같이 **`동기화 블록`** 을 만들면 된다.

```java
public void method() {
    // 여러 스레드가 실행 가능 영역
    ...
    synchronized(공유객체) {
        임계 영역  // 단 하나의 스레드만 실행
    }
    // 여러 스레드가 실행 가능 영역
    ...
}
```

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55475348-aa882e00-564e-11e9-947b-7d4135ea91d5.png'>
</p>
<br>

- 동기화 개념을 통해 이전 예제에서 문제가 된 공유 객체인 Calculator를 수정한다.

> Calculator, 동기화 메소드로 수정된 공유 객체

```java
public class Calculator {
    private int memory;

    public int getMemory() {
        return memory;
    }

    public synchronized void setMemory(int memory) {
        this.memory = memory;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        System.out.println(Thread.currentThread().getName() + ": " + this.memory);
    }
}
```

```
// 제대로 결과값이 나온다.

User1: 100
User2: 50
```

<br>

> 위 예제에서는 `setMemory()` 메소드를 동기화 메소드로 만들었는데, 다음과 같이 동기화 블록으로도 수정할 수 있다.

```java
public class Calculator {
    private int memory;

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        synchronized (this) {  // 공유 객체인 Calculator의 참조(잠금 대상)
            this.memory = memory;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread().getName() + ": " + this.memory);
        }
    }
}
```

<br>

## 스레드 상태

- 스레드 객체를 생성하고, `start()` 메소드를 호출하면 곧바로 스레드가 실행되는 것처럼 보이지만 사실은 **실행 대기 상태(RUNNABLE)** 가 된다.

    - 실행 대기 상태에 있는 스레드 중에서 스레드 스케줄링으로 선택된 스레드가 비로서 CPU를 점유하고 run() 메소드를 실행한다. → **이때를 실행(RUNNING) 상태라고 한다.**

    - 스레드는 `실행 대기 상태`와 `실행 상태`를 번갈아가면서 자신의 run() 메소드를 조금씩 실행한다.

    - 실행 상태에서 run() 메소드가 종료되면, 더 이상 실행할 코드가 없기 때문에 스레드의 실행은 멈추게 된다. → **이때를 종료 상태(TERMINATED)라고 한다.**

- 경우에 따라서 스레드는 실행 상태에서 실행 대기 상태로 가지 않을 수도 있다.

    - 실행 상태에서 **일시 정지 상태**로 가기도 하는데, 일시 정지 상태는 스레드가 실행할 수 없는 상태이다.

    - 일시 정지 상태에는 `WAITING, TIMED_WAITING, BLOCKED`가 있다.

<br>

![Life Cycle of Thread](https://img1.daumcdn.net/thumb/R720x0.q80/?scode=mtistory&fname=http%3A%2F%2Fcfile23.uf.tistory.com%2Fimage%2F123BBA494F699E82126112)

<br>

> `getState()` 메소드

- 이러한 스레드 상태를 확인할 수 있도록 하기 위해 자바 5부터 Thread 클래스에 `getState()` 메소드가 추가되었다.

- `getState()` 메소드는 다음 표처럼 스레드 상태에 따라서 `Thread.State 열거 상수`를 리턴한다.

| 상태               | 설명                                                                                              |
| ---------------- | ----------------------------------------------------------------------------------------------- |
| NEW              | - 객체 생성<br>- 스레드가 만들어진 상태<br>- 아직 start() 메소드가 호출되지 않은 상태                                       |
| RUNNABLE         | - 실행 대기<br>- 실행 상태로 언제든지 갈 수 있는 상태<br> - 스레드 객체가 생선된 후에 start() 메서드를 호출하면 Runnable 상태로 이동       |
| RUNNING          | - 실행 상태<br>- Runnable 상태에서 스레드 스케줄러에 의해 Running 상태로 이동<br>- 스케줄러는 Running 상태의 스레드 중 하나를 선택해서 실행 |
| BLOCKED          | - 일시 정지<br>- 사용하고자 하는 객체의 lock이 풀릴 때까지 기다리는 상태<br>- 스레드가 다른 특정한 이유로 Running 상태에서 Blocked 상태로 이동 |
| WAITING          | - 일시 정지<br>- 다른 스레드가 통지할 때까지 기다리는 상태                                                            |
| TIMED_WAITING    | - 일시 정지<br>- 주어진 시간 동안 기다리는 상태                                                                  |
| TERMINATED(DEAD) | - 실행을 마친 상태(종료)<br>- run() 메소드 완료시 스레드가 종료되면 그 스레드는 다시 시작할 수 없게 된다.                             |

<br>

> StatePrintThread, 타겟 스레드의 상태를 출력하는 스레드

```java
public class StatePrintThread extends Thread {
    private Thread targetThread;

    public StatePrintThread(Thread targetThread) {  // targetThread : 상태를 조사할 스레드
        this.targetThread = targetThread;
    }

    @Override
    public void run() {
        while (true) {
            Thread.State state = targetThread.getState();  // 스레드 상태 얻기
            System.out.println("타겟 스레드 상태: " + state);

            if (state == Thread.State.NEW) {  // 객체 생성 상태일 경우 실행 대기 상태로 만듬
                targetThread.start();
            }

            if (state == Thread.State.TERMINATED) {  // 종료 상태일 경우 while문을 종료
                break;
            }
            try {
                // 0.5 초간 일시 정지
                Thread.sleep(500);
            } catch (Exception e) {}
        }
    }
}
```

> TargetThread, 타켓 스레드

```java
public class TargetThread extends Thread {
    @Override
    public void run() {
        for (long i = 0; i < 1000000000; i++) {  // RUNNABLE 상태 유지
        }

        try {
            // 1.5초간 일시 정지
            Thread.sleep(1500);  // sleep 메소드 호출해서 1.5초간 TIMED_WAITING 상태 유지
        } catch (Exception e) {}

        for (long i = 0; i < 1000000000; i++) {}  // RUNNABLE 상태 유지
    }
}
```

> TargetStateExample, 실행 클래스

- `StatePrintThread`를 생성해서 매개값으로 전달받은 `TargetThread`의 상태를 출력하도록 작성된 실행 클래스

```java
public class TargetThreadExample {
    public static void main(String[] args) {
        StatePrintThread statePrintThread =
                new StatePrintThread(new TargetThread());
        statePrintThread.start();
    }
}
```

- `TargetTread`가 객체로 생성되면 NEW 상태를 가지고, run() 메소드가 종료되면 TERMINATED 상태가 되므로 결국 다음과 같은 상태로 변한다.

> NEW → RUNNABLE → TIMED_WAITING → RUNNABLE → TERMINATED

```
타겟 스레드 상태: NEW
타겟 스레드 상태: RUNNABLE 
타겟 스레드 상태: TIMED_WAITING
타겟 스레드 상태: TIMED_WAITING
타겟 스레드 상태: TIMED_WAITING
타겟 스레드 상태: RUNNABLE
타겟 스레드 상태: TERMINATED
```

<br>

## 스레드 상태 제어

- Deprecated 메소드들은 스레드의 안전성을 헤친다고 하여 더 이상 사용하지 않도록 권장된 메소드들이다.

    - `Deprecated` : 중요도가 떨어져 더 이상 사용되지 않고 앞으로는 사라지게 될 (컴퓨터 시스템 기능 등)

<br>

| 메소드                                                         | 설명                                                                                                                                                      |
| ----------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| interrupt()                                                 | 일시 정지 상태의 스레드에서 InterruptedException 예외를 발생시켜, 예외 처리 코드(catch)<br>에서 실행 대기 상태로 가거나 종료 상태로 갈 수 있도록한다.                                                    |
| notify()<br>notifyAll()                                     | 동기화 블록 내에서 wait() 메소드에 의해 일시 정지 상태에 있는 스레드를 실행 대기 상태로 만든다.                                                                                              |
| resume()                                                    | suspend() 메소드에 의해 일시 정지 상태에 있는 스레드를 실행 대기 상태로 만든다.<br>- Deprecated (대신 notify(), notifyAll() 사용)                                                        |
| sleep(long millis)<br>sleep(long millis, int nanos)         | 주어진 시간 동안 스레드를 일시 정지 상태로 만든다. 주어진 시간이 지나면 자동적으로<br>실행 대기 상태가 된다.                                                                                        |
| join()<br>join(long millis)<br>join(long millis, int nanos) | join() 메소드를 호출한 스레드는 일시 정지 상태가 된다. 실행 대기 상태로 가려면,<br> join() 메소드를 멤버로 가지는 스레드가 종료되거나, 매개값으로 주어진 시간이 지나야 한다.                                             |
| wait()<br>wait(long millis)<br>wait(long millis, int nanos) | 동기화(synchornized) 블록 내에서 스레드를 일시 정지 상태로 만든다. 매개값으로 주어진 <br>시간이 지나면 자동적으로 실행 대기 상태가 된다. 시간이 주어지지 않으면<br> notify(), notifyAll() 메소드에 의해 실행 대기 상태로 갈 수 있다. |
| suspend()                                                   | 스레드를 일시 정지 상태로 만든다. resume() 메소드를 호출하면 다시 실행 대기 상태가 된다.<br>- Deprecated (대신 wait() 사용)                                                                  |
| yield()                                                     | 실행 중에 우선 순위가 동일한 다른 스레드에게 실행을 양보하고 실행 대기 상태가 된다.                                                                                                        |
| stop()                                                      | 스레드를 즉시 종료시킨다.<br>- Deprecated                                                                                                                          |

- wait(), notify(), notifyAll()은 `Object 클래스`의 메소드이고, 그 이외의 메소드들은 모두 `Thread 클래스`의 메소드들이다.


<br>

### 주어진 시간동안 일시 정지 `sleep()`

```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // interrupt() 메소드가 호출되면 실행
}
```

> SleepExample, 3초 주기로 10번 비프음 발생

```java
import java.awt.*;

public class SleepExample {
    public static void main(String[] args) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        for (int i = 0; i < 10; i++) {
            toolkit.beep();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
        }
    }
}
```

<br>

### 다른 스레드에게 실행 양보 `yield()`

- 스레드가 처리하는 작업은 반복적인 실행을 위해 for문이나 while문을 포함하는 경우가 많다. 가끔은 이 반복문들이 무의미한 반복을 하는 경우가 있다.

    - 이것보다는 다른 스레드에게 실행을 양보하고 자신은 실행 대기 상태로 가는 것이 전체 프로그램 성능에 도움이 된다.

```java
public void run() {
    while (true) {
        if (work) {
            System.out.println("ThreadA 작업 내용");
        } else {
            Thread.yield();  // yield()가 없으면 무의미한 반복을 할 분이다.
        }
    }
}
```

<br>

> YieldExample, 스레드 실행 양보 예제

```java
public class YieldExample {
    public static void main(String[] args) {
        ThreadA threadA = new ThreadA();
        ThreadB threadB = new ThreadB();

        threadA.start();
        threadB.start();

        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        threadA.work = false;

        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        threadA.work = true;

        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        threadA.stop = true;
        threadB.stop = true;
    }
}
```

> ThreadA (ThreadB도 동일)

```java
public class ThreadA extends Thread {
    public boolean stop = false;  // 종료 플래그
    public boolean work = true;  // 작업 진행 여부 플래그

    public void run() {
        while (!stop) {
            if (work) {
                System.out.println("ThreadA 작업 내용");
            } else {
                Thread.yield();
            }
        }
        System.out.println("ThreadA 종료");
    }
}
```

<br>

### 다른 스레드의 종료를 기다림 `join()`

- 스레드는 다른 스레드와 독립적으로 실행하는 것이 기본이지만 다른 스레드가 종료될 때까지 기다렸다가 실행해야 하는 경우가 발생할 수도 있다.

> SumThread, 1부터 100까지의 합을 계산하는 스레드

```java
public class SumThread extends Thread {
    private long sum;

    public long getSum() {
        return sum;
    }

    public void setSun() {
        this.sum = sum;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
    }
}

```

> JoinExample, 다른 스레드가 종료될 때까지 일시 정지 상태 유지

```java
public class JoinExample {
    public static void main(String[] args) {
        SumThread sumThread = new SumThread();
        sumThread.start();

        try {
            sumThread.join();  // sumThread가 종료할 때까지 메인 스레드를 일시 정지시킴
        } catch (InterruptedException e) {}
        
        System.out.println("1~100 합: " + sumThread.getSum());
    }
}
```

- JoinExample 클래스의 try~catch 부분을 주석 처리하면 1~100까지의 합은 0이 나오게 된다. (컴퓨터의 성능에 따라 다를 수 있다.) sumThread가 계산 작업을 완료하지 않은 상태에서 합을 먼저 출력하기 때문이다.

<br>

### 스레드간 협업 `wait(), notify(), notifyAll()`

- 경우에 따라서는 두 개의 스레드를 교대로 번갈아가며 실행해야 할 경우가 있다. 

- 자신의 작업이 끝나면 상대방 스레드를 일시 정지 상태에서 풀어주고, 자신은 일시 정지 상태로 만드는 것이다.

- 핵심은 공유 객체에 있다.

- **공유 객체는 두 스레드가 "작업할 내용"을 각각 동기화 메소드로 구분해 놓는다.** 한 스레드가 작업을 완료하면 `notify()` 메소드를 호출해서 일시 정지 상태에 있는 다른 스레드를 실행 대기 상태로 만들고, 자신은 두 번 작업을 하지 않도록 `wait()` 메소드를 호출하여 일시 정지 상태로 만든다.

<br>

- `wait()` 대신 `wait(long timeout)` 이나, `wait(long timeout, int nanos)`를 사용하면 `notify()`를 호출하지 않아도 지정된 시간이 지나면 스레드가 자동적으로 실행 대기 상태가 된다.

- `notify()`는 `wait()`에 의해 일시 정지된 스레드 중 한 개를 실행 대기 상태로 만들고, `notifyAll()` 메소드는 `wait()`에 의해 일시 정지된 모든 스레드를을 실행 대기 상태로 만든다.

- `wait(), notify(), notifyAll()` 메소드들은 Thread 클래스가 아닌 Object 클래스에 선언된 메소드이므로 모든 공유 객체에서 호출이 가능하다.

- 주의할 점은 이 메소드들은 동기화 메소드 또는 동기화 블록 내에서만 사용할 수 있다.

> WorkObject, 두 스레드의 작업 내용을 동기화 메소드로 작성한 공유 객체

```java
public class WorkObject {
    public synchronized void methodA() {
        System.out.println("ThreadA의 methodA() 작업 실행");
        notify();  // 일시정지 상태에 있는 ThreadB를 실행 대기 상태로 만듬
        try {
            wait();  // ThreadA를 일시 정지 상태로 만듬
        } catch (InterruptedException e) {}
    }

    public synchronized void methodB() {
        System.out.println("ThreadB의 methodB() 작업 실행");
        notify();  // 일시정지 상태에 있는 ThreadA를 실행 대기 상태로 만듬
        try {
            wait();  // ThreadB를 일시 정지 상태로 만듬
        } catch (InterruptedException e) {}
    }
}
```

> ThreadA, WorkObject의 methodA()를 실행하는 스레드

```java
public class ThreadA extends Thread {
    private WorkObject workObject;

    public ThreadA(WorkObject workObject) {
        this.workObject = workObject;  // 공유 객체를 매개값으로 받아들여 필드에 저장
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            workObject.methodA();  // 공유 객체의 methodA()를 10번 반복 호출
        }
    }
}
```

> ThreadB, WorkObject의 methodA()를 실행하는 스레드

```java
public class ThreadB extends Thread {
    private WorkObject workObject;

    public ThreadB(WorkObject workObject) {
        this.workObject = workObject;  // 공유 객체를 매개값으로 받아들여 필드에 저장
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            workObject.methodB();  // 공유 객체의 methodB()를 10번 반복 호출
        }
    }
}
```

> WaitNotifyExample, 두 스레드를 생성하고 실행하는 메인 스레드

```java
public class WaitNotifyExample {
    public static void main(String[] args) {
        WorkObject sharedobject = new WorkObject();  // 공유 객체 생성

        ThreadA threadA = new ThreadA(sharedobject);  // ThreadA 생성
        ThreadB threadB = new ThreadB(sharedobject);  // ThreadB 생성

        threadA.start();  // ThreadA 실행
        threadB.start();  // ThreadB 실행
    }
}
```


<br>

### ★ 생산자 소비자 패턴 (Pub-Sub Pattern)

- 데이터를 저장하는 스레드(생산자 스레드)가 데이터를 저장하면, 데이터를 소비하는 스레드(소비자 스레드)가 데이터를 읽고 처리하는 교대 작업을 구현한 것이다.

<br>
<p aling = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55540656-605e8580-56fe-11e9-8c19-fd12dac898bd.png'>
</p>
<br>


> DataBox, 두 스레드의 작업 내용을 동기화 메소드로 작성한 공유 객체

```java
public class DataBox {
    private String data;

    public synchronized String getData() {
        if (this.data == null) {  // data 필드가 null이면 소비자 스레드를 일시 정지 상태로 만듬
            try { wait(); } catch (InterruptedException e) {}
        }
        String returnValue = data;
        System.out.println("ConsumerThread가 읽은 데이터: " + returnValue);
        data = null;  // data 필드를 null로 만들고 생산자 스레드를 실행 대기 상태로 만듬
        notify();
        return returnValue;
    }

    public synchronized void setData(String data) {
        if (this.data != null) {  // data 필드가 null이 아니면 생산자 스레드를 일시 정지 상태로 만듬
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        this.data = data;  // data 필드에 값을 저장하고 소비자 스레드를 실행 대기 상태로 만듬
        System.out.println("ProducerThread가 생성한 데이터: " + data);
        notify();
    }
}
```

> ProducerThread, 데이터를 생산(저장)하는 스레드

```java
public class ProducerThread extends Thread {
    private DataBox dataBox;

    public ProducerThread(DataBox dataBox) {
        this.dataBox = dataBox;  // 공유 객체를 필드에 저장
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            String data = "Data-" +i;
            dataBox.setData(data);  // 새로운 데이터를 저장
        }
    }
}
```

> ConsumerThread, 데이터를 소비하는(읽는) 스레드

```java
public class ConsumerThread extends Thread {
    private DataBox dataBox;

    public ConsumerThread(DataBox dataBox) {
        this.dataBox = dataBox;  // 공유 객체를 필드에 저장
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            String data = dataBox.getData();  // 새로운 데이터를 읽음
        }
    }
}
```

> WaitNotifyExample, 두 스레드를 생성하고 실행하는 메인 스레드

```java
public class WaitNotifyExample {
    public static void main(String[] args) {
        DataBox dataBox = new DataBox();

        ProducerThread producerThread = new ProducerThread(dataBox);
        ConsumerThread consumerThread = new ConsumerThread(dataBox);

        producerThread.start();
        consumerThread.start();
    }
}
```

```
ProducerThread가 생성한 데이터: Data-1
ConsumerThread가 읽은 데이터: Data-1
ProducerThread가 생성한 데이터: Data-2
ConsumerThread가 읽은 데이터: Data-2
ProducerThread가 생성한 데이터: Data-3
ConsumerThread가 읽은 데이터: Data-3
```

<br>

### 스레드의 안전한 종료 `stop 플래그, interrupt`

- 스레드는 자신의 run() 매소드가 모두 실행되면 자동적으로 종료된다. 경우에 따라서는 스레드를 즉시 종료할 필요가 있다.

- Thread는 스레드를 즉시 종료시키기 위해서 `stop()` 메소드를 제공하는데, 이 메소드는 deprecated 되었다.

    - `stop()` 메소드로 스레드를 갑자기 종료하게 되면 스레드가 사용 중이던 자원들이 불안전한 상태로 남겨지기 때문이다.

    - 여기서 `자원`은 파일, 네트워크 연결 등을 말한다.

<br>

### stop 플래그를 이용하는 방법

- 스레드는 run() 메소드가 끝나면 자동적으로 종료되므로, run() 메소드가 정상적으로 종료되도록 유도하는 것이 최선의 방법이다.

```java
public class XXXThread extends Thread {
    private boolean stop;  // stop 플래그 필드

    public void run() {
        while (!stop) {  // stop이 true가 되면 run()이 종료된다.
        스레드가 반복 실행하는 코드;
        }
        // 스레드가 사용한 자원 관리
    }
}
```

> StopFlagExample, 1초 후 출력 스레드를 중지시킴

```java
public class StopFlagExample {
    public static void main(String[] args) {
        PrintThread1 printThread1 = new PrintThread1();
        printThread1.start();

        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        printThread1.setStop(true);
    }
}
```

> PrintThread1, 무한 반복해서 출력하는 스레드

```java
public class PrintThread1 extends Thread {
    private  boolean stop;

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void run() {
        while(!stop) {
            System.out.println("실행 중");
        }
        System.out.println("자원 정리");
        System.out.println("실행 종료");
    }

}
```

<br>

### `interrupt()` 메소드를 이용하는 방법

- `interrupt()` 메소드는 스레드가 일시 정지 상태에 있을 때 InterruptException 예외를 발생시키는 역할을 한다.

- 이것을 이용하여 run() 메소드를 정상 종료시킬 수 있다.

> InterruptExample, 1초 후 출력 스레드를 중지시킴

```java
public class InterruptExample {
    public static void main(String[] args) {
        Thread thread = new PrintThread2();
        thread.start();

        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        thread.interrupt();  // 스레드를 종료시키기 위해 InterruptedException을 발생시킴
    }
}
```

> PrintThread2, 무한 반복해서 출력하는 스레드

```java
public class PrintThread2 extends Thread {
    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("실행 중");
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
        }
        System.out.println("자원 정리");
        System.out.println("실행 종료");
    }
}
```

- `Thread.sleep(1)` 을 주목하자.

    - 스레드가 실행 대기 또는 실행 상태에 있을 때 `interrupt()` 메소드가 실행되면 즉시 InterruptedException 예외가 발생하지 않고, 스레드가 **미래에 일시 정지 상태가 되면 InterruptedException 예외가 발생** 한다.

    - 따라서 스레드가 일시 정지 상태가 되지 않으면 `interrupt()` 메소드 호출은 아무런 의미가 없다. 그래서 짧은 시간이나마 일시 정지시키기 위해 `Thread.sleep(1)` 을 사용한 것이다.

- 일시 정지를 만들지 않고도 `interrupt()` 호출 여부를 알 수 있다.

    - `interrupt()` 메소드가 호출되었다면 스레드의 `interrupted()`와 `isInterrupted()` 메소드는 true를 리턴한다.

      - `interrupted()`는 정적 메소드로 현재 스레드가 interrupted 되었는지 확인

      - `isInterrupted()`는 인스턴스 메소드로 현재 스레드가 interrupted 되었는지 확인할 때 사용한다.

    - 둘 중 어느 것을 사용해도 OK.

```java
boolean status = Thread.interrupted();
boolean status = objThread.isInterrupted();
```

- 다음은 PrintThread2를 수정한 것으로, 일시 정지 코드인 `Thread.sleep(1)`을 사용하지 않고, `Thread.interrupted()`를 사용해서 PrintThread2의 interrupt()가 호출되었는지 확인한 다음 while문을 빠져나간다.

> PrintThread2, 수정본

```java
public class PrintThread2 extends Thread {
    @Override
    public void run() {
        while (true) {
            System.out.println("실행 중");
            if (Thread.interrupted()) {
                break;
            }
        }

        System.out.println("자원 정리");
        System.out.println("실행 종료");
    }
}
```

<br>

## 데몬 스레드

- 데몬(daemon) 스레드는 주 스레드의 작업을 돕는 **보조적인 역할**을 수행하는 스레드이다. 

    - 주 스레드가 종료되면 데몬 스레드는 강제적으로 자동 종료되는데, 그 이유는 주 스레드의 보조 역할을 수행하므로 주 스레드가 종료되면 데몬 스레드의 존재 의미가 없어지기 때문이다.

  - 워드프로세서의 자동 저장, 미디어 플레이어의 동영상 및 음악 재생, 가비지 컬렉터 등에 적용되고, 이 기능들은 주 스레드(워드프로세서, 미디어 플레이어, JVM)가 종료되면 같이 종료된다.

- 스레드를 데몬으로 만들기 위해서는 주 스레드가 데몬이 될 스레드의 `setDaemon(true)`를 호출해주면 된다.

> 메인 스레드가 주 스레드로, AutoSaveThread가 데몬 스레드로

```java
public static void main(String[] args) {
    AutoSaveThread thread = new AutoSaveThread();
    thread.setDaemon(true);
    thread.start();
    ...
}
```

- 주의할 점 : `start()` 메소드가 호출되고 나서 setDaemon(true)`를 호출하면 `IllegalThreadStateException`이 발생하기 때문에 `start()` 메소드 호출 전에 `setDaemon(true)`를 호출해야 한다.

- 현재 실행 중인 스레드가 데몬 스레드인지 아닌지 검사 : `isDaemon()` 메소드의 **리턴값**을 조사하면 된다.

> AutoSaveThread, 1초 주기로 save() 메소드를 호출하는 데몬 스레드

```java
public class AutoSaveThread extends Thread {
    public void save() {
        System.out.println("작업 내용을 저장함.");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            save();
        }
    }
}
```

> DaemonExample, 메인 스레드가 실행하는 코드

```java
public class DaemonExample {
    public static void main(String[] args) {
        AutoSaveThread autoSaveThread = new AutoSaveThread();
        autoSaveThread.setDaemon(true);
        autoSaveThread.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}
        
        System.out.println("메인 스레드 종료");
    }
}
```

<br>

## 스레드 그룹 (ThreadGroup)

- 스레드 그룹은 관련된 스레드를 묶어서 관리할 목적으로 이용된다.

- JVM이 실행되면 `system 스레드 그룹`을 만들고, JVM 운영에 필요한 스레드들을 생성해서 system 스레드 그룹에 포함시킨다. 그리고 system의 하위 스레드 그룹으로 main을 만들고 메인 스레드를 main 스레드 그룹에 포함시킨다.

    - 스레드는 반드시 하나의 스레드 그룹에 포함된다.

    - 명시적으로 스레드 그룹에 포함시키지 않으면 기본적으로 자신을 생성한 스레드와 같은 스레드 그룹에 속한다.

    - 우리가 생성하는 작업 스레드는 대부분 main 스레드가 생성하므로 기본적으로 main 스레드 그룹에 속하게 된다.

<br>

### 스레드 그룹 이름 얻기

- 현재 스레드가 속한 스레드 그룹의 이름을 얻고 싶다면 다음과 같은 코드를 사용할 수 있다.
  
```java
ThreadGroup group = Thread.currentThread().getThreadGroup();
String groupName = group.getName();
```

- Thread의 정적 메소드인 `getAllStackTraces()`를 이용하면 프로세스 내에서 실행하는 모든 스레드에 대한 정보를 얻을 수 있다.

```java
Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
```

`getAllStackTraces()` 메소드는 Map 타입의 객체를 리턴하는데, 키는 스레드 객체이고 값은 스레드의 상태 기록들을 갖고 있는 StackTraceElement[] 배열이다.

> 현재 실행 중인 스레드 정보

```java
import java.util.Map;
import java.util.Set;

public class ThreadInfoExample {
    public static void main(String[] args) {
        AutoSaveThread autoSaveThread = new AutoSaveThread();
        autoSaveThread.setName("AutoSaveTread");
        autoSaveThread.setDaemon(true);
        autoSaveThread.start();

        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();  // 프로세스에서 실행하는 모든 Thread를 가져오는 코드
        Set<Thread> threads = map.keySet();
        for (Thread thread : threads) {
            System.out.println("Name: " + thread.getName() + ((thread.isDaemon())?"(데몬)": "(주)"));
            System.out.println("\t" + "소속그룹: " + thread.getThreadGroup().getName());
            System.out.println();
        }
    }
}
```

```
Name: Monitor Ctrl-Break(데몬)
	소속그룹: main

Name: Signal Dispatcher(데몬)
	소속그룹: system

Name: main(주)
	소속그룹: main

Name: AutoSaveTread(데몬)
	소속그룹: main

Name: Reference Handler(데몬)
	소속그룹: system

Name: Attach Listener(데몬)
	소속그룹: system

Name: Finalizer(데몬)
	소속그룹: system

```

- 실행 결과를 보면 가비지 컬렉션을 담당하는 Finalizer 스레드를 비롯한 일부 스레드들이 system 그룹에 속하고, main() 메소드를 실행하는 main 스레드는 system 그룹의 하위 그룹인 main에 속하는 것을 볼 수 있다.

- 그리고 main 스레드가 실행시킨 AutoSaveThread는 main 스레드가 소속된 main 그룹에 포함되어 있는 것을 볼 수 있다.

<br>

### 스레드 그룹 생성

- 명시적으로 스레드 그룹을 만들고 싶다면 다음 생성자 중 하나를 이용해서 ThreadGroup 객체를 만들면 된다. ThreadGroup 이름만 주거나, 부모 ThreadGroup과 이름을 매개값으로 줄 수 있다.

```java
ThreadGroup tg = new ThreadGroup(String name);
ThreadGroup tg = new ThreadGroup(ThreadGroup parent, String name);
```

- 스레드 그룹 생성 시 부모(parent) 스레드 그룹을 지정하지 않으면 현재 스레드가 속한 그룹의 하위 그룹으로 생성된다.
  
- 새로운 스레드 그룹을 생성한 후, 이 그룹에 스레드를 포함시키려면 Thread 객체를 생성할 때 생성자 매개값으로 스레드 그룹을 지정하면 된다. 스레드 그룹을 매개값으로 갖는 Thread 생성자는 다음 네 가지가 있다.

```java
Thread t = new Thread(ThreadGroup group, Runnable target);
Thread t = new Thread(ThreadGroup group, Runnable target, String name);
Thread t = new Thread(ThreadGroup group, Runnable target, String name, long stackSize);
Thread t = new Thread(ThreadGroup group, String name);
```

- Runnable 타입의 target은 Runnable 구현 객체를 말하며, String 타입의 name은 스레드의 이름이다. long 타입의 stackSize는 JVM이 이 스레드에 할당할 stack 크기이다.

<br>

### 스레드 그룹의 일괄 `interrupt()`

- `스레드를 스레드 그룹에 포함시켰을 때의 이점` : 스레드 그룹에서 제공하는 interrupt() 메소드를 이용하면 **그룹 내에 포함된 모든 스레드들을 일괄 interrupt할 수 있다.**

    - 스레드 그룹의 `interrupt()` 메소드는 포함된 모든 스레드의 interrupt() 메소드를 내부적으로 호출해주기 때문이다.

- 스레드 그룹의 `interrupt()` 메소드는 소속된 스레드의 `interrupt()` 메소드를 호출만 할 뿐, 개별 스레드에서 발생하는 InterruptedException에 대한 예외 처리를 하지 않는다. 따라서 안전한 종료를 위해서는 개별 스레드가 예외 처리를 해야 한다.

<br>
<p aling = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55605531-f2729680-57af-11e9-88bf-b60a6f228e11.jpg'>
</p>
<br>

> WorkThread, InterruptedException이 발생할 때 스레드가 종료되도록 함

```java
public class WorkThread extends Thread {
    public WorkThread(ThreadGroup threadGroup, String threadName) {
        super(threadGroup, threadName);  // 스레드 그룹과 스레드 이름 설정
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(getName() + "interrupted");  // InterruotedException이 발생될 때, while문을 빠져나와 스레드를 종료시킴
                break;
            }
        }
        System.out.println(getName() + " 종료됨");
    }
}
```

> ThreadGroupExample, 스레드 그룹을 이용한 일괄 종료 예제

```java
public class ThreadGroupExample {
    public static void main(String[] args) {
        ThreadGroup myGroup = new ThreadGroup("myGroup");
        WorkThread workThreadA = new WorkThread(myGroup, "workThreadA");
        WorkThread workThreadB = new WorkThread(myGroup, "workThreadB");

        workThreadA.start();
        workThreadB.start();

        System.out.println("[ main 스레드 그룹의 list() 메소드 출력 내용 ]");
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
        mainGroup.list();
        System.out.println();

        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        System.out.println("");
        System.out.println(" [ myGroup 스레드 그룹의 interrupt() 메소드 호출 ] ");
        myGroup.interrupt();
    }
}
```

```
[ main 스레드 그룹의 list() 메소드 출력 내용 ]
java.lang.ThreadGroup[name=main,maxpri=10]
    Thread[main,5,main]
    Thread[Monitor Ctrl-Break,5,main]
    java.lang.ThreadGroup[name=myGroup,maxpri=10]
        Thread[workThreadA,5,myGroup]
        Thread[workThreadB,5,myGroup]


 [ myGroup 스레드 그룹의 interrupt() 메소드 호출 ] 
workThreadBinterrupted
workThreadB 종료됨
workThreadAinterrupted
workThreadA 종료됨
```

<br>

## 스레드풀 (ThreadPool)

- 병렬 작업 처리가 많아지면 스레드 개수가 증가되고 그에 따른 스레드 생성과 스케줄링으로 인해 CPU가 바빠져 메모리 사용량이 늘어나고, 최종적으론 애플리케이션의 성능이 저하된다.

- 갑작스러운 병렬 작업의 폭증으로 인한 **스레드의 폭증을 막으려면 스레드풀을 사용**해야 한다.

    - 스레드풀은 **작업 처리에 사용되는 스레드를 제한된 개수만큼 정해 놓고 작업 큐(Queue)에 들어오는 작업들을 하나씩 스레드가 맡아 처리**한다. 작업 처리가 끝난 스레드는 다시 작업 큐에서 새로운 작업을 가져와 처리한다.

    - 그렇기 때문에 작업 처리 요청이 폭증되어도 스레드의 전체 개수가 늘어나지 않아 애플리케이션의 급격한 성능 저하가 없어진다.

- 자바는 스레드풀을 생성하고 사용할 수 있도록 `java.util.concurrent` 패키지에서 ExecutorService 인터페이스와 Executors 클래스를 제공하고 있다.
 
  - Excutors의 다양한 정적 메소드를 이용해서 **ExecutorService 구현 객체**를 만들수 있는데, **이것이 바로 스레드풀**이다.

<br>

> 스레드풀의 동작 방식

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55606954-c0643300-57b5-11e9-94c6-77aa5cac251b.jpg'>
</p>
<br>

### 스레드풀 생성 및 종료

#### 스레드풀 생성

- ExecutorService 구현 객체는 Executors 클래스의 다음 두 가지 메소드 중 하나를 이용해서 간편하게 생성할 수 있다.

| 메소드명(매개 변수)                      | 초기 스레드 수 | 코어 스레드 수 | 최대 스레드 수          |
| -------------------------------- | -------- | -------- | ----------------- |
| newCachedThreadPool()            | 0        | 0        | Integer.MAX_VALUE |
| newFixedThreadPool(int nThreads) | 0        | nThreads | nThreads          |

- 매개 변수 설명

  - 초기 스레드 수 : 객체가 생성될 때 기본적으로 생성되는 스레드 수

  - 코어 스레드 수 : 스레드 수가 증가된 후 사용 안하는 스레드를 스레드풀에서 제거하여도 최소한으로 유지할 스레드 수

  - 최대 스레드 수 : 스레드풀에서 관리하는 최대 스레드 수

<br>

> `newCachedThreadPool()`을 호출해서 `ExecutorService` 구현 객체를 얻는 코드

```java
ExecutorService executorService = Executors.newCachedThreadPool();
```

> CPU 코어의 수만큼 최대 스레드를 사용하는 스레드풀 생성

```java
ExecutorService executorService = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors()
);
```

- `newCachedThreadPool()` 과 `newFixedThreadPool()` 메소드를 사용하지 않고 코어 스레드 개수와 최대 스레드 개수를 설정하고 싶다면 직접 `ThreadPoolExecutor` 객체를 생성한다.

    - 사실 위 두 메소드도 내부적으로 `ThreadPoolExecutor` 객체를 생성해서 리턴한다.

<br>

> 초기 스레드 개수 0개, 코어 스레드 개수 3개, 최대 스레드 개수 100개인 스레드풀을 생성. 코어 스레드 3개를 제외한 나머지 추가된 스레드가 120초 동안 놀고 있을 경우 해당 스레드를 제거해서 스레드 수 관리

```java
ExecutorService threadPool = new ThreadPoolExecutor(
    3,      // 코어 스레드 개수
    100,    // 최대 스레드 개수
    120L,   // 놀고 있는 시간
    TimeUnit.SECONDS,  // 놀고 있는 시간 단위
    new SynchronousQueue<Runnable>()  // 작업 큐
);
```

<br>

#### 스레드풀 종료

- 스레드풀의 스레드는 기본적으로 데몬 스레드가 아니기 때문에 **main 스레드가 종료되더라도 작업을 처리하기 위해 계속 실행 상태로 남아있다.**

- 그래서 main 메소드가 실행이 끝나도 애플리케이션 프로세스는 종료되지 않기 때문에 스레드풀을 종료시켜 스레드들이 종료 상태가 되도록 처리해줘야 한다.

> ExecutorService의 종료 관련 메소드

| 리턴 타입          | 메소드명(매개변수)                                    | 설명                                                                                                      |
| -------------- | --------------------------------------------- | ------------------------------------------------------------------------------------------------------- |
| void           | shutdown()                                    | 현재 처리 중인 작업뿐만 아니라 작업 큐에 대기하고 있는 모든 작업을 처리한 뒤에 스레드풀을 종료시킨다.                                              |
| List<Runnable> | shutdownNow()                                 | 현재 작업 처리 중인 스레드를 interrupt해서 작업 중지를 시도하고 스레드풀을 종료시킨다. 리턴값은 작업 큐에 있는 미처리된 작업(Runnable)의 목록이다.            |
| boolean        | awaitTermination(long timeout, TimeUnit unit) | shutdown() 메소드 호출 이후, 모든 작업 처리를 timeout 시간 내에 완료하면 true를 리턴,완료하지 못하면 작업 처리 중인 스레드를 interrupt하고 false 리턴 |

<br>

```java
executorService.shutdown();  // 남아있는 작업 마무리 후 스레드풀 종료
또는
executorService.shutdownNow();  // 남아있는 작업과 상관없이 강제로 스레드풀 종료
```

<br>

### 작업 생성과 처리 요청

#### 작업 생성

- 하나의 작업은 Runnable 또는 Callable 구현 클래스로 표현한다.

- 둘의 차이점은 작업 처리 완료 후 리턴값의 유무이다.

<br>

> Runnable 구현 클래스

- Runnable의 run() 메소드는 리턴값이 없다.

```java
Runnable task = new Runnable() {
    @Override
    public void run() {
        // 스레드가 처리할 작업 내용
    }
}
```

<br>

> Callable 구현 클래스

- Callable의 call() 메소드는 리턴값이 있다.

- call()의 리턴 타입은 implements Callable<T>에서 지정한 T 타입이다.

```java
Callable<T> task = new Callable<T>() {
    @Override
    public T call() throws Exception {
        // 스레드가 처리할 작업 내용
        return T;
    }
}
```

<br>

- 스레드풀의 스레드는 작업 큐에서 Runnable 또는 Callable 객체를 가져와 run()과 call() 메소드를 실행한다.

<br>

#### 작업 처리 요청

- 작업 처리 요청이란 ExecutorService의 작업 큐에 Runnable 또는 Callable 객체를 넣는 행위를 말한다.

<br>

> ExecutorService의의 작업 처리 요청을 위한 두 종류의 메소드

|리턴 타입|메소드명(매개변수)|설명|
|--------|-------------|---|
|void|execute(Runnable command)| - Runnable을 작업 큐에 저장 <br> - 작업 처리 결과를 받지 못함|
|Future<?><br>Future<V><br>Future<V>|submit(Runnable task)<br>submit(Runnable task, V result)<br>submit(Callable<V> task)| - Runnable 또는 Callable을 작업 큐에 저장<br> - 리턴된 Future를 통해 작업 처리 결과를 얻을 수 있음|

<br>

> `execute()`와 `submit()`의 두 가지 차이점

1. 리턴값

   `execute()`는 작업 처리 결과를 받지 못하고 `submit()`은 작업 처리 결과를 받을 수 있도록 Future를 리턴한다.

2. 예외 처리
   
   `execute()`는 작업 처리 도중 예외가 발생하면 **스레드가 종료되고 해당 스레드는 스레드풀에서 제거**된다. 따라서 스레드풀을 다른 작업 처리를 위해 **새로운 스레드를 생성**한다.

   `submit()`은 작업 처리 도중에 예외가 발생하더라도 **스레드는 종료되지 않고 다음 작업을 위해 재사용**된다. 그래서 **가급적이면 스레드의 생성 오버헤더를 줄이기 위해서 `submit()`을 사용하는 것이 좋다.**

<br>

> ExecuteExample, execute() 메소드로 작업 처리 요청한 경우

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ExecuteExample {
    public static void main(String[] args) throws Exception {  // JVM에서 예외 처리
        ExecutorService executorService = Executors.newFixedThreadPool(2);  // 최대 스레드 개수가 2인 스레드풀 생성

        for (int i = 0; i < 10; i++) {
            // 작업 정의
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    // 스레드 총 개수 및 작업 스레드 이름 출력
                    ThreadPoolExecutor threadPoolExecutor =
                            (ThreadPoolExecutor) executorService;
                    int poolSize = threadPoolExecutor.getPoolSize();
                    String threadName = Thread.currentThread().getName();
                    System.out.println("[총 스레드 개수: " + poolSize + "] 작업 스레드 이름: " + threadName);

                    // 예외 발생 시킴
                    int value = Integer.parseInt("삼");
                }
            };


            // 작업 처리 요청
            executorService.execute(runnable);
            // executorService.submit(runnable);

            Thread.sleep(10);  // 콘솔에 출력 시간을 주기 위해 0.01초 일시 정지시킴
        }

        executorService.shutdown();  // 스레드풀 종료
    }
}
```

> `execute()` 메소드로 작업했을 경우, 스레드풀의 스레드 최대 개수 2는 변함이 없지만, 실행 스레드의 이름을 보면 **모두 다른 스레드가 작업을 처리하고 있다.** 작업 처리 도중 예외가 발생했기 때문에 해당 스레드는 제거되고 **새 스레드가 계속 생성되기 때문**이다.

```

[총 스레드 개수: 1] 작업 스레드 이름: pool-1-thread-1
Exception in thread "pool-1-thread-1" java.lang.NumberFormatException: For input string: "삼"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at ExecuteExample$1.run(ExecuteExample.java:21)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-3
Exception in thread "pool-1-thread-3" java.lang.NumberFormatException: For input string: "삼"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at ExecuteExample$1.run(ExecuteExample.java:21)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-2
Exception in thread "pool-1-thread-2" java.lang.NumberFormatException: For input string: "삼"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at ExecuteExample$1.run(ExecuteExample.java:21)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-4
Exception in thread "pool-1-thread-4" java.lang.NumberFormatException: For input string: "삼"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)

...

```

> 이번에는 `submit()` 메소드로 작업 처리를 요청한 경우를 살펴보자. 실행 결과를 보면 **예외가 발생하더라도 스레드가 종료되지 않고 계속 재사용되어 다른 작업을 처리**하고 있는 것을 볼 수 있다.

```
[총 스레드 개수: 1] 작업 스레드 이름: pool-1-thread-1
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-2
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-1
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-2
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-1
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-2
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-1
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-2
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-1
[총 스레드 개수: 2] 작업 스레드 이름: pool-1-thread-2
```

<br>

### 블로킹 방식의 작업 완료 통보

> Future 패턴에 대한 설명

퓨처 패턴은 미래에 완료될 작업을 등록하고, 처리 결과를 확인하는 객체를 통해서 작업의 완료를 확인하는 패턴 이다. 정의가 조금 모호하니까 간단한 예를 살펴보자. 애인의 생일에 애인에게 특별한 케이크를 선물하려고 빵집으로 간다. 빵집 주인에게 하트가 99개 그려진 특별한 케이크를 만들어달라고 부탁한 뒤 케이크의 값을 치른다. 빵집 주인이 내일 오후에 케이크를 찾으러 오라면서 케이크 주문서 를 주었다. 여러분은 케이크를 빨리 받고 싶은 마음에 케이크 주문서를 가지고 오후 1시에 빵집을 방문했으나 아직 메이크가 완성되지 않았고 1시간 뒤에 완성될 것 같다고 한다. 1시간 뒤에 여러분은 빵집에서 메이크 주문서로 케이크를 교환했다.

퓨처 패턴은 메서드를 호출하는 즉시 퓨처 객체 를 돌려준다. 위의 예에서는 케이크 주문서에 해당한다. 메서드의 처리 결과는 나중에 Future 객체를 통해서 확인한다. 위의 예에서는 케이크 주문서가 이에 해당한다.

#### 리턴값이 없는 작업 완료 통보

> NoResultExample 리턴값이 없는 작업 완료 통보

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NoResultExample {
    public static void main(String[] args) {
        // CPU의 코어 수만큼 최대 스레드를 사용하는 스레드풀 생성
        ExecutorService executorService =Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        System.out.println("[작업 처리 요청]");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int sum = 0;
                for (int i = 1; i <= 10; i++) { sum += i;}
                System.out.println("[처리 결과] " + sum);
            }
        };
        Future future = executorService.submit(runnable);

        try {
            future.get();
            System.out.println("[작업 처리 완료]");
        } catch (Exception e) {
            System.out.println("[실행 예외 발생함] " + e.getMessage());
        }
        executorService.shutdown();
    }
}
```

```
[작업 처리 요청]
[처리 결과] 55
[작업 처리 완료]
```


#### 리턴값이 있는 작업 완료 통보

> ResultByCallableExample, 리턴값이 있는 작업 완료 통보

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ResultByCallableExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        System.out.println("[작업 처리 요청]");
        Callable<Integer> task = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int sum = 0;
                for (int i = 1; i <= 10; i++) {
                    sum += i;
                }
                return  sum;
            }
        };
        Future<Integer> future = executorService.submit(task);

        try {
            int sum = future.get();
            System.out.println("[처리 결과] " + sum);
            System.out.println("[작업 처리 완료]");
        } catch(Exception e) {
            System.out.println("[실행 예외 발생함]" + e.getMessage());
        }
        executorService.shutdown();
    }
}
```

```
[작업 처리 요청]
[처리 결과] 55
[작업 처리 완료]
```

<br>

#### 작업 처리 결과를 객체 외부에 저장

- 상황에 따라서 스레드가 작업한 결과를 외부 객체에 저장해야 할 경우도 있다.

    - 예를 들어 스레드가 작업 처리를 완료하고 외부 Result 객체에 작업 결과를 저장하면, 애플리케이션이 Result 객체를 사용해서 어떤 작업을 진행할 수 있을 것이다.

    - 대개 **Result 객체는 공유 객체**가 되어, **두 개 이상의 스레드 작업을 취합할 목적으로 이용**된다.

<br>

- 이런 작업을 하기 위해서 ExecutorService의 submit(Runnable task, V result) 메소드를 사용할 수 있는데, V가 바로 Result 타입이 된다.

- 메소드를 호출하면 즉시 Future<V> 가 리턴되는데, Future의 get() 메소드를 호출하면 스레드가 작업을 완료할 때까지 블로킹되었다가 작업을 완료하면 V 타입 객체를 리턴한다.

- 리턴된 객체는 submit()의 두 번째 매개값으로 준 객체와 동일한데, 차이점은 스레드 처리 결과가 내부에 저장되어 있다는 것이다.

```java
Result result = ...;
Runnable task = new Task(result);
Future<Result> future = executorService.submit(task, result);
result = future.get();
```
  
- 작업 객체는 Runnable 구현 클래스로 생성하는데, 주의할 점은 스레드에서 결과를 저장하기 위해 **외부 Result 객체를 사용해야 하므로 생성자를 통해 Result 객체를 주입받도록** 해야 한다.

```java
class Task implements Runnable {
    Result result;
    Task(Result result) { this.result = result; }  // 생성자를 통해 Result 객체를 주입!
    @Override
    public void run() {
        // 작업 코드
        // 처리 결과를 result 저장
    }
}
```

<br>

> 1부터 10까지의 합을 계산하는 두 개의 작업을 스레드풀에 처리 요청하고, 각각의 스레드가 작업 처리를 완료한 후 산출된 값을 외부 Result 객체에 누적하도록 함.

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ResultByRunnableExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        System.out.println("[작업 처리 요청]");
        // 작업 정의
        class Task implements Runnable {
            // 외부 Result 객체를 필드에 저장
            Result result;
            Task(Result result) {
                this.result = result;
            }
            @Override
            public void run() {
             int sum = 0;
             for (int i = 0; i <= 10; i++) {
                 sum += i;
             }
             result.addValue(sum);  // Result 객체에 작업 결과 저장
            }
        }

        // 두 가지 작업 처리를 요청
        Result result = new Result();
        Runnable task1 = new Task(result);
        Runnable task2 = new Task(result);
        Future<Result> future1 = executorService.submit(task1, result);
        Future<Result> future2 = executorService.submit(task2, result);

        try {
            // 두 가지 작업 결과를 취합
            result = future1.get();
            result = future2.get();
            System.out.println("[처리 결과] " + result.accumValue);
            System.out.println("[작업 처리 완료]");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[실행 예외 발생함] " + e.getMessage());
        }
        executorService.shutdown();
    }
}

class Result {  // 처리 결과를 저장하는 Result 클래스
    int accumValue;
    synchronized void addValue(int value) {
        accumValue += value;
    }
}
```

```
[작업 처리 요청]
[처리 결과] 110
[작업 처리 완료]
```

<br>

#### 작업 완료 순으로 통보

- 작업 요청 순서대로 작업 처리가 완료되는 것은 아니다. 작업의 양과 스레드 스케줄링에 따라서 먼저 요청한 작업이 나중에 완료되는 경우도 발생한다.

- 여러 개의 작업들이 순차적으로 처리될 필요도 없고, 처리 결과도 순차적으로 이용할 필요가 없다면 → 작업 처리가 완료된 것부터 결과를 얻어 이용

  - **`CompletionService`** 를 이용하여 작업 처리가 완료된 것만 통보를 받을 수 있다.

 - `CompletionService`는 처리 완료된 작업을 가져오는 `poll()`과 `take()` 메소드를 제공한다.

<br>

|리턴 타입|메소드명(매개 변수)|설명|
|---|---|---|
|Future<V>|poll()|완료된 작업의 Future를 가져옴.<br>완료된 작업이 없다면 즉시 null을 리턴함|
|Future<V>|poll(long timeout, TimeUnit unit)|완료된 작업의 Future를 가져옴.<br>완료된 작업이 없다면 timeout까지 블로킹됨.|
|Future<V>|take()|완료된 작업의 Future를 가져옴.<br>완료된 작업이 없다면 있을 때까지 블로킹됨.|
|Future<V>|submit(Callable<V> task)|스레드풀에 Callable 작업 처리 요청|
|Future<V>|submit(Runnable task, V result)|스레드풀에 Runnable 작업 처리 요청|

<br>

- CompletionService 구현 클래스는 ExecutorCompletionService<v>이다. 객체를 생성할 때 **생성자 매개값으로 ExecutorService를 제공**하면 된다.

```java
ExecutorService executorService = new Executors.newFixedThreadPool(
    Runtime.getRuntime.availableProcessors()
);

CompletionService<V> completionService = new ExecutorCompletionService<V>(
    executorService
    );
```

- `poll()`과 `take()` 메소드를 이용해서 처리 완료된 작업의 Future를 얻으려면 **CompletionService의 submit() 메소드로 작업 처리 요청**을 해야 한다.

```
completionService.submit(Callable<V> task);
completionService.submit(Runnable task, V result);
```

- 다음은 `take()` 메소드를 호출하여 완료된 Callable 작업이 있을 때까지 블로킹되었다가 완료된 작업의 Future를 얻고, `get()` 메소드로 결과값을 얻어내는 코드이다.

    - while문은 애플리케이션이 종료될 때까지 반복 실행헤야 하므로 스레드풀의 스레드에서 실행하는 것이 좋다.

```java
executorService.submit(new Runnable() {  // 스레드풀의 스레드에서 실행되도록 한다
    @Override
    public void run() {
        while(true) {
            try {
                Future<Integer> future = completionService.take();  // 완료된 작업이 있을 때까지 블로킹 / 완료된 작업이 있으면 Future를 리턴
                int value = future.get();  // get()은 블로킹되지 않고 바로 작업 결과를 리턴 (원래는 기다리지만 take() 메소드로 인해 바로 결과 리턴 (? - 확인 필요) )
            } catch (Exception e) {
                 break;
            }
        }
    }
});
```

<br>

> 3개의 Callable 작업을 처리 요청하고 처리가 완료되는 순으로 작업의 결과값을 콘솔에 출력

```java
import java.util.concurrent.*;

public class CompletionServiceExample extends Thread {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletionService<Integer> completionService =
                new ExecutorCompletionService<>(executorService);  // CompletionService 생성

        System.out.println("[작업 처리 요청]");
        for (int i = 0; i < 3; i++) {
            completionService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int sum = 0;
                    for (int i = 0; i <= 10; i++) {
                        sum += i;
                    }
                    return sum;
                }
            });
        }

        System.out.println("[처리 완료된 작업 확인]");
        executorService.submit(new Runnable() {  // 스레드풀의 스레드에서 실행하도록 함
            @Override
            public void run() {
                while (true) {
                    try {
                        Future<Integer> future = completionService.take();  // 완료된 작업 가져오기
                        int value = future.get();
                        System.out.println("[처리 결과] " + value);
                    } catch (Exception e) {
                        break;
                    }
                }
            }
        });

        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        executorService.shutdown();
    }
}
```

```
[작업 처리 요청]
[처리 완료된 작업 확인]
[처리 결과] 55
[처리 결과] 55
[처리 결과] 55
```

<br>

### 콜백 방식의 작업 완료 통보

- `콜백(callback)`이란 애플리케이션이 스레드에게 작업 처리를 요청한 후, 스레드가 작업을 완료하면 특정 메소드를 자동 실행하는 기법을 말한다.

    - 이때 자동 실행되는 메소드를 콜백 메소드라고 한다.

<br>

> 블로킹 방식과 콜백 방식의 비교

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55627411-18685d00-57e9-11e9-93d3-ccc97a4cc2fe.png'>
</p>

  - [이미지 출처 : 키위남의 블로그](http://blog.naver.com/PostView.nhn?blogId=mals93&logNo=220743747346&parentCategoryNo=&categoryNo=24&viewDate=&isShowPopularPosts=true&from=search)

<br>

> CallBackExample, 콜백 방식의 작업 완료 통보 받기

- 두 개의 문자열을 정수화해서 더하는 작업을 처리하고 결과를 콜백 방식으로 통보하는 예제

    - 쳣 번째 작업은 "3", "3"을 주고, 두 번째 작업은 "3", "삼"을 준다.

    - 첫 번째 작업은 정상 처리되어 `completed()`가 자동 호출, 두 번째 작업은 NumberFormatException이 발생되어 `failed()` 메소드 호출

```java
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallbackExample {
    private ExecutorService executorService;

    public CallbackExample() {
        executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
    }

    // callback 메소드를 가진 CompletionHandler 객체 생성
    private CompletionHandler<Integer, Void> callback =
            new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer result, Void attachment) {
                    System.out.println("completed() 실행: " + result);
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    System.out.println("failed() 실행: " + exc.toString());
                }
            };

    public void doWork(final String x, final String y) {
        Runnable  task = new Runnable() {
            @Override
            public void run() {
                try {
                    int intX = Integer.parseInt(x);
                    int intY = Integer.parseInt(y);
                    int result = intX + intY;
                    callback.completed(result, null);  // 정상 처리했을 경우 호출
                } catch (NumberFormatException e) {
                    callback.failed(e, null);
                }
            }
        };
        executorService.submit(task);  // 스레드풀에게 작업 처리 요청
    }

    public void finish() {
        executorService.shutdown();  // 스레드풀 종료
    }

    public static void main(String[] args) {
        CallbackExample example = new CallbackExample();
        example.doWork("3", "3");
        example.doWork("3", "삼");
        example.finish();
    }
}
```

```
completed() 실행: 6
failed() 실행: java.lang.NumberFormatException: For input string: "삼"
```
