### `아이템 17`

# 변경 가능성을 최소화하라 (불변 객체)

> **불변 클래스(immutable class)** 란 간단히 말해 그 인스턴스의 내부 값을 수정할 수 없는 클래스다.

- 불변 인스턴스에 간직된 정보는 고정되어 객체가 파괴되는 순간까지 **절대 달라지지 않는다.**

- 자바 플랫폼 라이브러리에도 다양한 불변 클래스가 있다.

    - `String, 기본 타입의 박싱된 클래스들, BigInteger, BigDecimal`이 여기 속한다.
    
- 불변 클래스는 **가변 클래스(mutable class)보다 설계하고 구현하고 사용하기 쉬우며, 오류가 생길 여지도 적고 훨씬 안전**하다.

<br>

## 클래스를 불변으로 만드는 다섯 가지 규칙

### 1. 객체의 상태를 변경하는 메서드(변경자)를 제공하지 않는다.

<br>

### 2. 클래스를 확장할 수 없도록 한다.

> 클래스가 불변임을 보장하려면 "자신을 상속하지 못하게" 해야 한다.

- 하위 클래스에서 부주의하게 혹은 나쁜 의도로 객체의 상태를 변하게 만드는 사태를 막아준다.

- 상속을 막는 대표적인 방법은 클래스를 final로 선언하는 것이지만, **더 유연한 방법**<sup id="a1">[1](#f1)</sup>도 있다.

<br>

### 3. 모든 필드를 `final`로 선언한다.

- 시스템이 강제하는 수단을 이용해 설계자의 의도를 명확히 드러내는 방법

- 새로 생성된 인스턴스를 동기화 없이 다른 스레드로 건네도 문제없이 동작하게끔 보장하는데도 필요.

<br>

### 4. 모든 필드를 `private`으로 선언한다.

- 필드가 참조하는 가변 객체를 클라이언트에서 직접 접근해 수정하는 일을 막아준다.

- 기술적으론 기본 타입 필드나 불변 객체를 참조하는 필드를 `public final`로만 선언해도 불변 객체가 되지만, 이렇게 하면 다음 릴리스에서 내부 표현을 바꾸지 못하므로 권하지 않는다. [`아이템15`](), [`아이템16`]()

<br>

### 자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다.

- 클래스에 가변 객체를 참조하는 필드가 하나라도 있다면 클라이언트에서 그 객체의 참조를 얻을 수 없도록 해야 한다.

  - 이런 필드는 절대 클라이언트가 제공한 객체 참조를 가리키게 해서는 안 되며, 접근자 메서드가 그 필드를 그대로 반환해서도 안 된다.

  - **생성자, 접근자,** [**readObject**]() **메서드** `item 88` 모두에서 방어적 복사를 수행하라.

<br>

> 불변 복소수 클래스<sup id="a2">[ ](#f2)</sup>

```java
public final class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart()        { return re; }
    public double imaginaryPart()   { return im; }

    public Complex plus(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    public Complex minus(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    public Complex times(Complex c) {
        return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
    }

    public Complex dividedBy(Complex c) {
        double tmp = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / tmp, (im * c.re - re * c.im) / tmp);
    }

    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Complex))
            return false;
        Complex c = (Complex) o;

        // == 대신 compare를 사용하는 이유는 63쪽을 확인(item 10)
        return Double.compare(c.re, re) == 0
            && Double.compare(c.im, im) == 0;
    }

    @Override public int hashCode() {
        return 31 * Double.hashCode(re) + Double.hashCode(im);
    }

    @Override public String toString() {
        return "(" + re + " + " + im + "i)";
    }
}
```

<br>

- 위 클래스는 복소수를 표현한다.

    - Object의 메서드 몇 개를 재정의했고, 실수부와 허수부 값을 반환하는 접근자 메서드(realPart와 imaginaryPart)와 사칙연산 메서드(plus, minus, times, divideBy)를 정의

- **사칙연산 메서드들이 인스턴스 자신은 수정하지 않고 `새로운 Complex 인스턴스를 만들어 반환`하는 모습에 주목.**

    - 이처럼 *`피연산자에 함수를 적용해 그 결과를 반환하지만, 피연산자 자체는 그대로인 프로그래밍 패턴`* 을 **함수형 프로그래밍**이라 한다.

    - 이와 달리, **절차적 혹은 명령형 프로그래밍**에서는 메서드에서 피연산자인 자신을 수정해 *`자신의 상태가 변하게 된다.`*

- `메서드 이름`으로 **동사 대신 전치사를 사용**한 점도 주목.

    - `add` : 더하다, 보태다, 추가하다 / `plus` : ~을 더한, 더하여
    
    - 이는 해당 메서드가 **객체의 값을 변경하지 않는다는 사실을 강조하려는 의도**

    - ※ `BigInteger`와 `BigDecimal` 클래스가 이 명명 규칙을 따르지 않아 잘못 사용하는 경우가 자주 있으므로 주의!

<br>

## 불변 객체의 장점

### 1. 단순하다.

- 생성된 시점의 상태를 파괴될 때까지 그대로 간직한다.

    - 모든 생성자가 클래스 불변식(class invariant)을 보장한다면 그 클래스를 사용하는 프로그래머가 다른 노력을 들이지 않고도 영원히 불변으로 남음.

- 반면 `가변 객체`는 복잡한 상태에 놓일 수 있다.

    - 변경자 메서드가 일으키는 상태 전이를 문서로 정밀하게 남겨 놓지 않으면 믿고 사용하기 어려울 수 있다.

<br>

### 2. "스레드 안전"하여 따로 동기화할 필요 없다.

- 여러 스레드가 동시에 사용해도 절대 훼손되지 않는다.

- 클래스를 스레드 안전하게 만드는 가장 쉬운 방법이기도 함.

  #### 불변 객체는 안심하고 "공유"할 수 있다.

    - 따라서 불변 클래스라면 **한 번 만든 인스턴스를 최대한 "재활용"** 하길 권한다.

      - 가장 쉬운 재활용 방법 : 자주 쓰이는 값들을 상수`(public static final)`로 제공

        ```java
        public static final Complex ZERO = new Complex(0, 0);
        public static final Complex ONE  = new Complex(1, 0);
        public static final Complex I    = new Complex(0, 1);
        ```

    - 불변 클래스는 **자주 사용되는 인스턴스를 '캐싱'하여 같은 인스턴스를 중복 생성하지 않게 해주는** [**정적 팩터리**](https://github.com/june0122/TIL/blob/master/JAVA/EffectiveJava/item%2001.md)`item 1`**를 제공**할 수 있다.

      - 정적 팩터리를 사용하면 여러 클라이언트가 인스턴스를 공유하여 메모리 사용량과 가비지 컬렉션 비용이 줄어든다. 

      - 새로운 클래스 설계 시, public 생성자 대신 정적 팩터리를 만들어두면 클라이언트를 수정하지 않고도 필요에 따라 캐시 기능을 나중에 덧붙일 수 있다.
    
    - 불변 객체를 자유롭게 공유할 수 있다는 점은 [**방어적 복사**]()`item 50`**도 필요 없다**는 결론으로 이어진다.

      - 아무리 복사해봐야 원본과 같으니 복사 자체가 의미가 없다. (악의적인 의도로 값의 변경을 위해 객체를 복사하더라도 불변 객체이므로 소용이 없다.)

      - 그러니 불변 클래스는 `clone 메서드`나 `복사 생성자`를 제공하지 않는 게 좋다.

        - `String 클래스의 복사 생성자`는 이 사실을 잘 이해하지 못한 자바 초창기 때 만들어진 것으로, 되도록 사용하지 말아야 함.

<br>

### 3. 불변 객체는 자유롭게 공유할 수 있음은 물론, 불변 객체끼리는 내부 데이터를 공유할 수 있다.


<br>

### 4. 객체를 만들 때 다른 불변 객체들을 구성요소로 사용하면 이점이 많다.

- 값이 바뀌지 않는 구성요소들로 이뤄진 객체라면 그 구조가 아무리 복잡하더라도 불변식을 유지하기 훨씬 수월하다.

  - 예로, 불변 객체는 `Map`의 key와 `Set(집합)`의 원소로 쓰기에 안성맞춤이다.

<br>

### 5. 불변 객체는 그 자체로 "실패 원자성(failure atomicity)"을 제공한다.

- 상태가 절대 변하지 않으니 잠깐이라도 불일치 상태에 빠질 가능성이 없다.

<br>

## 불변 클래스의 단점

### 1. 값이 다르면 반드시 독립된 객체로 만들어야 한다.

> 값의 가짓수가 많다면 이들을 모두 만드는 데 큰 비용을 치러야 한다.

<br>

### 2. 원하는 객체를 완성하기까지의 단계가 많고, 그 중간 단계에서 만들어진 객체들이 모두 버려진다면 성능 문제가 더 불거진다.

> 이 문제에 대처하는 방법은 두 가지이다.

- 첫 번째. **다단계 연산(multistep operation)** 들을 예측하여 기본 기능으로 제공하는 방법

    - 이러한 다단계 연산을 기본으로 제공한다면 더 이상 각 단계마다 객체를 생성하지 않아도 된다. 불변 객체는 내부적으로 아주 영리한 방식으로 구현할 수 있기 때문이다.

        - 예컨대, BigInteger는 모듈러 지수 같은 다단계 연산 속도를 높여주는 가변 `동반 클래스(companion class)`를 사용하기란 BigInteger를 쓰는 것보다 훨씬 어렵다.

        - 그 어려운 부분을 모두 BigInteger가 대신 처리해준다.

- 두 번째. 클라이언트들이 원하는 **복잡한 연산들을 정확히 예측**할 수 있다면  **package-private의 가변 동반 클래스**만으로 충분하다. 그렇지 않다면 이 클래스를 **public으로 제공**하는 게 최선이다.

    - 자바 플랫폼 라이브러리에서 이에 해당하는 **대표적 예시가 `String 클래스`** 이다.

    - **String의 가변 동반 클래스는 바로 `StringBuilder`**(와 구닥다리 전임자 `StringBuffer`)다.

<br>

## 클래스가 불변임을 보장하려면 "자신을 상속하지 못하게" 해야 한다.

### 자신을 상속하지 못하게 하는 가장 쉬운 방법은 `final 클래스로 선언`이지만, `더 유연한 방법`이 있다.

- #### <b id="f1"><sup>1</sup></b> 모든 생성자를 `private` 혹은 `package-private`으로 만들고 [public 정적 팩터리](https://github.com/june0122/TIL/blob/master/JAVA/EffectiveJava/item%2001.md)를 제공하는 방법이다. [ ↩](#a1)<br>

<br>

> 다음은 위의 코드의 [**Complex 클래스**](#a2) 를 생성자 대신 정적 팩터리를 사용한 불변 클래스로 구현한 코드이다.

```java
public class Complex {
    private final double re;
    private final double im;

    private Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public static Complex valueOf(double re, double im) {
        return new Complex(re, im);
    }

    ...
}
```
