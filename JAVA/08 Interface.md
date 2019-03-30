# 챕터 08. 인터페이스(Interface)

## 인터페이스의 역할

- 자바의 인터페이스 : 객체의 사용 방법을 정의한 타입

    - 객체의 교환성을 높여주기 때문에 **다형성**을 구현하는 매우 중요한 역할을 한다.
  
    - 특히 자바8의 람다식이 함수적 인터페이스의 구현 객체를 생성하기 때문에 중요성이 커졌다.

- 인터페이스는 개발 코드와 객체가 서로 통신하는 접점 역할을 한다.

    - 개발 코드가 인터페이스의 메소드를 호출하면 인터페이스는 객체의 메소드를 호출시킨다.
  
    - 그렇기 때문에 개발 코드는 객체의 내부 구조를 알 필요가 없고 *인터페이스의 메소드만 알고 있으면 된다.*

- 개발 코드가 직접 객체의 메소드를 호출하지 않고, 중간에 인터페이스를 두는 이유는?

    - 개발 코드를 수정하지 않고, 사용하는 객체를 변경할 수 있도록 하기 위해서이다.

    - 인터페이스는 하나의 객체가 아니라 여러 객체들과 사용이 가능, 어떤 객체를 사용하느냐에 따라 실행 내용과 리턴값이 달라진다.


<br>

## 인터페이스 선언

- 인터페이스는 `~.java` 형태의 소스 파일로 작성되고 컴파일러 `javac.exe` 를 통해 `~.class` 형태로 컴파일 되기 때문에 **물리적 형태는 클래스와 동일**하다.

- 차이점은 소스를 작성할 때 **선언하는 방법이 다르다.**

<br>

### 인터페이스 선언

- 인터페이스 선언은 class 대신에 `interface 키워드`를 사용한다.

```java
[ public ] interface 인터페이스명 { ... }
```

- 클래스 이름을 작성하는 방법과 동일

    - 영어 대소문자를 사용하며, 첫 문자를 대문자로 하고 나머지는 소문자를 사용하는 것이 관례.

    - `public` 접근 제한은 *다른 패키지에서도 인터페이스를 사용할 수 있도록* 해준다.

```java
public interface RemoteControl {}
```

- 클래스는 `필드, 생성자, 메소드`를 구성 멤버로 가지는데 비해, 인터페이스는 **상수와 메소드** 만을 구성 멤버로 가진다.

- 인터페이스는 **객체를 생성할 수 없기 때문에 생성자를 가질 수 없다.**

- 자바 8부터 디폴트 메소드와 정적 메소드도 선언이 가능하다.

```java
interface 인터페이스명 {
    // 상수
    타입 상수명 = 값;
    // 추상 메소드
    타입 메소드명(매개변수, ...);
    // 디폴트 메소드
    default 타입 메소드명(매개변수, ...) { ... }
    // 정적 메소드
    static 타입 메소드명(매개변수) { ... }
}
```

#### 상수 필드(Constant Field)

- 인터페이스는 **`객체 사용 설명서`** 이므로 런타임 시 데이터를 저장할 수 있는 필드를 선언할 수 없다.

- 그러나 **상수 필드는 선언이 가능**하다.

- 상수는 인터페이스에 고정된 값으로 런타임 시에 데이터를 바꿀 수 없다.

- 상수 선언 시에는 반드시 `초기값`을 대입해야 한다.

#### 추상 메소드(Abstract Method)

- 객체가 가지고 있는 메소드를 설명한 것으로 호출할 때 어떤 매개값이 필요하고, 리턴 타입이 무엇인지 알려준다.

- 실제 실행부는 객체(구현 객체)가 가지고 있다.

#### 디폴트 메소드(Default Method)

- 인터페이스에 선언되지만 사실은 `객체(구현 객체)가 가지고 있는 인스턴스 메소드`라고 생각해야 한다.

- 자바 8에서 디폴트 메소드를 허용한 이유는 기존 인터페이스를 확장해서 새로운 기능을 추가하기 위해서이다.

#### 정적 메소드(Static Method)

- 디폴트 메소드와는 달리 **객체가 없어도 인터페이스만으로 호출이 가능**하다.

<br>

### 상수 필드 선언

- 인터페이스는 데이터를 저장할 수 없기 때문에 데이터를 저장할 인스턴스 또는 정적 필드를 선언할 수 없다. 대신 상수 필드만 선언할 수 있다.

  - 상수는 `public static final` 로 선언한다.

- 따라서 인터페이스에 선언된 필드는 모두 `public static fianl`의 특성을 갖는다.

```java
[ public static final ] 타입 상수명 = 값;
```

- 상수명은 대문자로 작성하되, 서로 다른 단어로 구성되어 있을 경우에는 언더바(_)로 연결하는 것이 관례이다.

```java
public interface RemoteControl {
    public int MAX_VOLUME = 10;
    public int MIN_VOLUME = 0;
}
```

<br>

### 추상 메소드 선언

- 인터페이스를 통해 호출된 메소드는 최종적으로 객체에서 실행된다.

- 그렇기 때문에 인터페이스의 메소드는 실행 블록이 필요 없는 추상 메소드로 선언한다.

- 추상 메소드는 `리턴 타입, 메소드명, 매개 변수`만 기술되고 중괄호 `{ }`를 붙이지 않는 메소드를 말한다.

```java
[ public abstract ] 리턴타입 메소드명(매개변수, ...);
```

- 인터페이스에 선언된 추상 메소드는 모두 `public abstract` 의 특성을 갖기 때문에 `public abstract`를 생략하더라도 자동적으로 컴파일 과정에서 붙게 된다.

```java
public interface RemoteControl {
    // 상수 (public, static, final 생략하더라도 자동으로 컴파일 과정에서 붙음)
    public static final int MAX_VOLUME = 10;
    public int MIN_VOLUME = 0;

    // 추상 메소드 (public abstract 생략하더라도 자동으로 컴파일 과정에서 붙음)
    public void turnOn();
    public void turnOff();
    public void setVolume(int volume);
}
```

<br>

### 디폴트 메소드 선언

- 자바 8에서 추가된 인터페이스의 새로운 멤버

- 형태는 클래스의 인스턴스 메소드와 동일한데, `default` 키워드가 리턴 타입 앞에 붙는다.

- 디폴트 메소드는 public 특성을 갖기 때문에 `public`을 생략하더라도 자동적으로 컴파일 과정에서 붙게 된다.

```java
[public] default 리턴타입 메소드명(매개변수, ...) { ... }
```

```java
public interface RemoteControl {
    // 상수 (public, static, final 생략하더라도 자동으로 컴파일 과정에서 붙음)
    public static final int MAX_VOLUME = 10;
    public int MIN_VOLUME = 0;

    // 추상 메소드 (public abstract 생략하더라도 자동으로 컴파일 과정에서 붙음)
    public void turnOn();

    public void turnOff();

    public void setVolume(int volume);

    // 디폴트 메소드 (public 생략하더라도 자동으로 컴파일 과정에서 붙음)
    // : 실행 내용까지 작성한다.
    default void setMute(boolean mute) {
        if (mute) {
            System.out.println("무음 처리합니다.");
        } else {
            System.out.println("무음 해제합니다.");
        }
    }
}
```

<br>

### 정적 메소드 선언

- 정적 메소드는 디폴트 메소드와 마찬가지로 자바 8에서 추가된 인터페이스의 새로운 멤버이다.

- 형태는 클래스의 정적 메소드와 완전 동일

- 정적 메소드는 `public` 특성을 갖기 때문에 `public` 을 생략하더라도 자동적으로 컴파일 과정에서 붙게 된다.

```java
[public] static 리턴타입 메소드명(매개변수, ...) { ... }
```

```java
public interface RemoteControl {
    // 상수 (public, static, final 생략하더라도 자동으로 컴파일 과정에서 붙음)
    public static final int MAX_VOLUME = 10;
    public int MIN_VOLUME = 0;

    // 추상 메소드 (public abstract 생략하더라도 자동으로 컴파일 과정에서 붙음)
    public void turnOn();

    public void turnOff();

    public void setVolume(int volume);

    // 디폴트 메소드 (public 생략하더라도 자동으로 컴파일 과정에서 붙음)
    // : 실행 내용까지 작성한다.
    default void setMute(boolean mute) {
        if (mute) {
            System.out.println("무음 처리합니다.");
        } else {
            System.out.println("무음 해제합니다.");
        }
    }

    // 정적 메소드 (public 생략하더라도 자동으로 컴파일 과정애서 붙음)
    static void changeBattery() {
        System.out.println("건전지를 교환합니다.");
    }
}
```

<br>

## 인터페이스 구현(Implement)

- 개발 코드가 인터페이스 메소드를 호출하면 인터페이스는 객체의 메소드를 호출한다.

- **객체는** `인터페이스에서 정의된 추상 메소드와 동일한 메소드 이름, 매개 타입, 리턴 타입을 가진` **실체 메소드를 가지고 있어야 한다.**

    - 이러한 객체를 **인터페이스의 구현 객체**라고 한다.

    - 구현 객체를 생성하는 클래스를 **구현 클래스**라고 한다.

<br>

### 구현 클래스

- 구현 클래스는 보통의 클래스와 동일한데, 인터페이스 타입으로 사용할 수 있음을 알려주기 위해 클래스 선언부에 `implements` 키워드를 추가하고 인터페이스명을 명시해야 한다.

```java
public class 구현클래스명 implements 인터페이스명 {
    // 인터페이스에 선언된 추상 메소드의 실체 메소드 선언
}
```

- 인터페이스에 선언된 추상 메소드의 실체 메소드를 선언해야 한다.

<br>

- 다음은 Television과 Audio라는 이름을 가지고 있는 RemoteControl 구현 클래스를 작성하는 방법을 보여준다.

> Televison, 구현 클래스

```java
public class Television implements RemoteControl {
    // 필드
    private int volume;

    // turnOn() 추상 메소드의 실체 메소드
    public void turnOn() {
        System.out.println("TV를 켭니다.");
    }

    // turnOff() 추상 메소드의 실체 메소드
    public void turnOff() {
        System.out.println("TV를 끕니다.");
    }

    // setVolume() 추상 메소드의 실체 메소드
    // 인터페이스 상수를 이용해서 volume 필드의 값을 제한
    public void setVolume(int volume) {
        if (volume > RemoteControl.MAX_VOLUME) {
            this.volume = RemoteControl.MAX_VOLUME;
        } else if (volume < RemoteControl.MIN_VOLUME) {
            this.volume = RemoteControl.MIN_VOLUME;
        } else {
            this.volume = volume;
        }
        System.out.println("현재 TV 볼륨: " + volume);
    }
}
```

> Audio, 구현 클래스

```java
public class Audio implements RemoteControl {
    // 필드
    private int volume;

    public void turnOn() {
        System.out.println("Audio를 켭니다.");
    }

    public  void turnOff() {
        System.out.println("Audio를 끝니다.");
    }

    public void setVolume(int volume) {
        if (volume > RemoteControl.MAX_VOLUME) {
            this.volume = RemoteControl.MAX_VOLUME;
        } else if (volume < RemoteControl.MIN_VOLUME) {
            this.volume = RemoteControl.MIN_VOLUME;
        } else {
            this.volume = volume;
        }
        System.out.println("현재 Audio 볼륨: " + volume);
    }
}
```

- 구현 클래스에서 추상 메소드들에 대한 실체 메소드를 작성할 때 주의할 점은 인터페이스의 모든 메소드는 기본적으로 public 접근 제한을 가지므로 public보다 더 낮은 접근 제한으로 작성할 수 없다.

    - public을 생략하면 "Cannot reduce the visibility of the inherited method"라는 컴파일 에러가 난다.

- 인터페이스에 선언된 추상 메소드에 대응하는 실체 메소드를 구현 클래스가 작성하지 않으면 구현 클래스는 자동적으로 추상 클래스가 된다.

    - 그렇기 때문에 클래스 선언부에 `abstract` 키워드를 추가해야 한다.

```java
public abstract class Television implements RemoteControl {
    public void turnOn() { ... }
    public void turnOff() { ... }
    
    // setVolume() 실체 메소드가 없다.
    // 일부만 구현했으므로 추상 클래스가 된다. 
}
```

- 구현 클래스가 작성되면 **new 연산자로 객체를 생성할 수 있다!**

    - **문제는** `어떤 타입의 변수에 대입하느냐`이다.

<br>

> 다음과 같이 Television 객체를 생성하고 Television 변수에 대입한다고 인터페이스를 사용하는 것이 아니다.

```java
Television tv = new Television();
```

> 인터페이스로 구현 객체를 사용하려면 다음과 같이 인터페이스 변수를 선언하고 구현 객체를 대입해야 한다. 인터페이스 변수는 참조 타입이기 때문에 구현 객체가 대입될 경우 구현 객체의 번지를 저장한다.

```java
인터페이스 변수;
변수 = 구현객체;

// or

인터페이스 변수 = 구현객체;
```

- RemoteControl 인터페이스로 구현 객체인 Television과 Audio를 사용하려면 다음과 같이 RemoteControl 타입 변수인 rc를 선언하고 구현 객체를 대입해야 한다.

> 인터페이스 변수에 구현 객체 대입

```java
public class RemoteControlExample {
    public static void main(String[] args) {
        RemoteControl rc;
        rc = new Television();
        re = new Audio();
    }
}
```

<br>

### 익명 구현 객체

- 구현 클래스를 만들어 사용하는 것이 일반적이고, 클래스를 재사용할 수 있어 편리하지만, **일회성의 구현 객체**를 만들기 위해 소스 파일을 만들고 클래스를 선언하는 것은 **비효율적**이다.

- 소스 파일을 만들지 않고도 구현 객체를 만들 수 있는 방법이 바로, **익명 구현 객체** 이다.

  - 자바는 UI 프로그래밍에서의 `이벤트 처리`와 `임시 작업 스레드를 만들기` 위해 익명 구현 객체를 많이 활용.

  - 자바 8에서 지원하는 **람다식**이 인터페이스의 익명 구현 객체를 만들기 때문에 코드 패턴을 잘 익혀둘 필요가 있다. 

<br>

> 익명 구현 객체 생성 후 인터페이스 변수에 대입하는 코드

- 하나의 실행문이므로 끝에는 반드시 세미콜론 `;` 을 붙여야 한다.

```java
인터페이스 변수 = new 인터페이스() {
    // 인터페이스에 선언된 추상 메소드의 실체 메소드 선언
};
```

<br>

> RemoteControl의 익명 구현 객체 생성, 익명 구현 클래스

```java
public class RemoteControlExample {
    public static void main(String[] args) {
        RemoteControl rc = new RemoteControl() {
            public void turnOn() { }
            public void turnOff() { }
            public void setVolume(int volume) { }
        };
    }
}
```

- 모든 객체는 클래스로부터 생성되는데, 익명 구현 객체도 예외는 아니다.

    - RemoteControllerExample.java를 컴파일하면 자바 컴파일러에 의해 자동으로 다음과 같은 클래스 파일이 만들어진다.

```
RemoteControlExample$1.class

// 만약 두 번째 익명 구현 객체를 만들었다면
// 클래스 파일명은 RemoteControlExample$2.class
```

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55274295-341bc100-531a-11e9-8170-b8e8f0c8b009.PNG'>
</p>

<br>

### 다중 인터페이스 구현 클래스

- 객체는 다수의 인터페이스 타입으로 사용할 수 있다.

> 인터페이스 A와 인터페이스 B가 객체의 메소드를 호출할 수 있으려면 객체는 이 두 인터페이스를 모두 구현해야 한다.

```java
public class 구현클래스명 implements 인터페이스A, 인터페이스B {
    // 인터페이스 A에 선언된 추상 메소드의 실체 메소드 선언
    // 인터페이스 B에 선언된 추상 메소드의 실체 메소드 선언
}
```

<br>

## 인터페이스 사용

- 예로 RemoteControl 인터페이스로 구현 객체인 Television과 Audio를 사용하려면 다음과 같이 ① `RemoteControl 타입 변수 rc를 선언`하고 ② `구현 객체를 대입`해야 한다.

```java
RemoteControl rc;
rc = new Television();
rc = new Audio();
```

- 개발 코드에서 인터페이스는 클래스의 필드, 생성자 또는 메소드의 매개 변수, 생성자 또는 메소드의 로컬 변수로 선언될 수 있다.

```java
public class MyClsas {
    // 필드
    RemoteControl rc = new Television();

    // 생성자
    MyClass(RemoteControl rc) {
        this.rc = rc;
    }

    // 메소드
    void methodA() {
        // 로컬변수
        RemoteControl rc = new Audio();
    }
}
```

<br>

### 추상 메소드 사용

- 개발 코드에서 RemoteControl의 변수 rc로 turnOn() 또는 turnOff() 메소드를 호출하면 구현 객체의 turnOn()과 turnOff() 메소드가 자동 실행된다.

```java
RemoteControl rc = new Television();
rc.turnOn();   // Television의 turnOn() 실행
rc.turnOff();   // Television의 turnOff() 실행
```

<br>

> 익명 구현 클래스

```java
public class RemoteControlExample {
    public static void main(String[] args) {
        RemoteControl rc = new RemoteControl() {
            public void turnOn() { }
            public void turnOff() { }
            public void setVolume(int volume) { }
        };
    }
}
```

- 익명 구현 클래스와 인터페이스를 사용할 때의 코드의 차이점을 한 번 확인해보자.

> 인터페이스 사용

```java
public class RemoteControlExample {
    public static void main(String[] args) {
        RemoteControl rc = null;  // 인터페이스 변수 선언

        rc = new Television();  // Television 객체를 인터페이스에 대입
        rc.turnOn();            // 인터페이스의 turnOn(), turnOff() 호출
        rc.turnOff();           // 인터페이스의 turnOn(), turnOff() 호출

        rc = new Audio();       // Audio 객체를 인터페이스에 대입
        rc.turnOn();            // 인터페이스의 turnOn(), turnOff() 호출
        rc.turnOff();           // 인터페이스의 turnOn(), turnOff() 호출
    }
}
```

<br>

### 디폴트 메소드 사용

- 디폴트 메소드는 인터페이스에 선언되지만, 인터페이스에서 바로 사용할 수 없다. 디폴트 메소드는 추상 메소드가 아닌 인스턴스 메소드이므로 구현 객체가 있어야 사용할 수 있다.

- 디폴트 메소드는 인터페이스의 모든 구현 객체가 가지고 있는 기본 메소드라고 생각하면 된다.
  
    - 그러나 어떤 구현 객체는 디폴트 메소드의 내용이 맞지 않아 수정이 필요할 수도 있다.
    
    - 구현 클래스를 작성할 때 디폴트 메소드를 재정의(오버라이딩)해서 자신에게 맞게 수정하면 디폴트 메소드가 호출될 때 자신을 재정의한 메소드가 호출된다.

> Audio, 구현 클래스

```java
public class Audio implements RemoteControl {
    // 필드
    private int volume;
    private boolean mute;

    // turnOn() 추상 메소드의 실체 메소드
    public void turnOn() {
        System.out.println("Audio를 켭니다.");
    }


    // turnOff() 추상 메소드의 실체 메소드
    public  void turnOff() {
        System.out.println("Audio를 끝니다.");
    }

    // setVolume() 추상 메소드의 실체 메소드
    public void setVolume(int volume) {
        if (volume > RemoteControl.MAX_VOLUME) {
            this.volume = RemoteControl.MAX_VOLUME;
        } else if (volume < RemoteControl.MIN_VOLUME) {
            this.volume = RemoteControl.MIN_VOLUME;
        } else {
            this.volume = volume;
        }
        System.out.println("현재 Audio 볼륨: " + volume);
    }


    // 디폴트 메소드 재정의
    @Override
    public void setMute(boolean mute) {
        this.mute = mute;
        if (mute) {
            System.out.println("Audio 무음 처리합니다.");
        } else {
            System.out.println("Audio 무음 해제합니다.");
        }
    }
}
```

> RemoteControlExample, 디폴트 메소드 사용

```java
public class RemoteControlExample {
    public static void main(String[] args) {
        RemoteControl rc = null;  // 인터페이스 변수 선언

        rc = new Television();  // Television 객체를 인터페이스에 대입
        rc.turnOn();            // 인터페이스의 turnOn(), turnOff() 호출
        rc.turnOff();           // 인터페이스의 turnOn(), turnOff() 호출
        rc.setMute(true);

        rc = new Audio();       // Audio 객체를 인터페이스에 대입
        rc.turnOn();            // 인터페이스의 turnOn(), turnOff() 호출
        rc.turnOff();           // 인터페이스의 turnOn(), turnOff() 호출
        rc.setMute(true);
    }
}
```

```
TV를 켭니다.
TV를 끕니다.
무음 처리합니다.
Audio를 켭니다.
Audio를 끝니다.
Audio 무음 처리합니다.
```

- setMute() 메소드를 호출하려면 RemoteControl의 구현 객체가 필요한데, Television 객체를 인터페이스 변수에 대입하고 나서 setMute()를 호출할 수 있다.

- **비록 setMute()가 Television에 선언되지는 않았지만** Television 객체가 있으므로 setMute()가 호출 되었다.


<br>

### 정적 메소드 사용

- 인터페이스의 정적 메소드는 **인터페이스로 바로 호출이 가능**하다.

```java
public class RemoteControlExample {
    public static void main(String[] args) {
        RemoteControl.changeBattery();  // 인터페이스로 바로 호출
    }
}
```

<br>

## 타입 변환과 다형성

- 인터페이스도 다형성을 구현하는 기술이 사용되는데, 요즘은 상속보다는 인터페이스를 통해서 다형성을 구현하는 경우가 더 많다.

- **다형성** : 하나의 타입에 대입되는 객체에 따라서 실행 결과가 다양한 형태로 나오는 성질

    - 부모 타입에 어떤 자식 객체를 대입하느냐에 따라 실행 결과가 달라지듯이, 인터페이스 타입에 어떤 구현 객체를 대입하느냐에 따라 실행 결과가 달라진다.

    - **상속**은 `같은 종류의 하위 클래스를 만드는 기술`, **인터페이스**는 `사용 방법이 동일한 클래스를 만드는 기술`

<br>

> 인터페이스의 다형성이란?

- 프로그램을 개발할 때 인터페이스를 사용해서 메소드를 호출하도록 코딩을 했다면, 구현 객체를 교체하는 것은 매우 손쉽고 빠르게 할 수 있다. 프로그램 소스 코드에는 변함이 없는데, 구현 객체를 교체함으로써 프로그램의 실행 결과가 다양해진다.

- '이것이 자바다' p363, 364 정독

<br>

### 자동 타입 변환(Promotion)

- 구현 객체가 인터페이스 타입으로 변환되는 것은 자동 타입 변환에 해당된다.

```
인터페이스 변수 = 구현객체;
            ↑    ───┬───
            └───────┘   
          자동 타입 변환
```

- 인터페이스 구현 클래스를 상속해서 자식 클래스를 만들었다면 자식 객체 역시 인터페이스 타입으로 자동 타입 변환시킬 수 있다.

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/55275470-e9a24080-5329-11e9-8e04-ee4cb464143e.jpg'>
</p>

> [이미지 출처 : 키위남의 블로그](https://blog.naver.com/mals93/220716656982)

<br>

### 필드의 다형성

> Tire, 인터페이스

```java
public interface Tire {
    public void roll();
}
```

> HankookTire, 구현 클래스

```java
public class HankookTire implements Tire {
    @Override
    public void roll() {
        System.out.println("한국 타이어가 굴러갑니다.");
    }
}
```

> KumhoTire, 구현 클래스

```java
public class KumhoTire implements Tire {
    @Override
    public void roll() {
        System.out.println("금호 타이어가 굴러갑니다.");
    }
}
```

> Car, 필드 다형성

```java
public class Car {
    Tire frontLeftTire = new HankookTire();
    Tire frontRightTire = new HankookTire();
    Tire backLeftTire = new HankookTire();
    Tire backRightTire = new HankookTire();

    void run() {
        frontLeftTire.roll();
        frontRightTire.roll();
        backLeftTire.roll();
        backRightTire.roll();
    }
}
```

> CarExample, 필드 다형성 테스트

```java
public class CarExample {
    public static void main(String[] args) {
        Car myCar = new Car();

        myCar.run();
        System.out.println();

        myCar.frontRightTire = new KumhoTire();
        myCar.frontLeftTire = new KumhoTire();

        myCar.run();
    }
}
```

<br>

### 인터페이스 배열로 구현 객체 관리

```java
public class Car {
    Tire[] tires = {
            new HankookTire(),
            new HankookTire(),
            new HankookTire(),
            new HankookTire()
    };

    void run() {
        for(Tire tire : tires) {
            tire.roll();
        }
    }

// 배열로 관리하기 이전 코드

//    Tire frontLeftTire = new HankookTire();
//    Tire frontRightTire = new HankookTire();
//    Tire backLeftTire = new HankookTire();
//    Tire backRightTire = new HankookTire();
//
//    void run() {
//        frontLeftTire.roll();
//        frontRightTire.roll();
//        backLeftTire.roll();
//        backRightTire.roll();
//    }
}
```

```java
public class CarExample {
    public static void main(String[] args) {
        Car myCar = new Car();

        myCar.run();
        System.out.println();

        myCar.tires[0] = new KumhoTire();
        myCar.tires[1] = new KumhoTire();

        myCar.run();
    }
}
```

<br>

### 매개 변수의 다형성



<br>

