# 챕터 03. 연산자

## 단항 연산자

### 부호 연산자(+, -)

- **`부호 연산자 사용 시 주의점`** : 부호 연산자의 **산출 타입은 int 타입**이 된다.

```java
short s = 100;
short result = -s;  // 컴파일 에러

int result2 = -s;  // 부호 연산자의 산출 타입은 int!
```

<br>

### 비트 반전 연산자(~)

- **`비트 반전 연산자 사용 시 주의점`** : 비트 반전 연산자의 **산출 타입은 int 타입**이 된다.

```
byte v1 = 10;
byte v2 = ~v1;  // 컴파일 에러

int v3 = ~v1;  // 비트 반전 연산자의 산출 타입도 int!
```

- **`비트 반전 연산자를 이용하여 부호가 반대인 정수 구하기`** : 비트 반전 연산자의 산출값에 1을 더하면 된다.

```java
byte v1 = 10;
int v2 = ~v1 + 1;  // -10이 v2에 저장
```

- **`Integer.toBinaryString`** : 정수값을 총 32비트의 이진 문자열로 리턴하는 메소드

    - `Integer.toBinaryString()` 메소드는 앞의 비트가 모두 0이면 0은 생략되고 나머지 문자열만 리턴하기 때문에 총 32개의 문자열을 모두 얻기 위해서는 다음과 같은 메소드가 필요하다.

    - 리턴하는 str의 문자 수를 조사해서 32보다 작으면 앞에 0을 붙이도록 한 것이다.

    ```java
    public static String toBinaryString(int value) {
        String str = Integer.toBinaryString(value);
        while (str.length() < 32) {  // 문자열의 길이가 32가 될 때까지
            str = '0' + str;  // 앞에 0을 붙인다.
        }
        return str;
    }
    ```

    <br>

```java
import static java.lang.Integer.toBinaryString;

public class BitReverseOperatorExample {
    public static void main(String[] args) {
        int v1 = 10;
        int v2 = ~v1;
        int v3 = v2 + 1;
        System.out.println(toBinaryString(v1) + " (십진수: " + v1 + ")");
        System.out.println(toBinaryString(v2) + " (십진수: " + v2 + ")");
        System.out.println(toBinaryString(v3) + " (십진수: " + v3 + ")");
        System.out.println();

        int v4 = -10;
        int v5 = ~v4;
        int v6 = ~v4 + 1;
        System.out.println(toBinaryString(v4) + " (십진수: " + v4 + ")");
        System.out.println(toBinaryString(v5) + " (십진수: " + v5 + ")");
        System.out.println(toBinaryString(v6) + " (십진수: " + v6 + ")");
    }

    public static String toBinaryString(int value) {
        String str = Integer.toBinaryString(value);
        while (str.length() < 32) {
            str = '0' + str;
        }
        return str;
    }
}
```

<br>

## 이항 연산자

### 산술 연산자 (+, -, *, /, %)

> 피연산자들의 타입이 동일하지 않을 때

- long을 제외한 정수 타입 연산은 int 타입으로 산출되고, 피연산자 중 하나라도 실수 타입이면 실수 타입으로 산출된다.

  - byte + byte -> int + int = int

  - int + long -> long + long = long

  - int + double -> double + double = double

<br>

> **오버플로우 탐지**

- 산출 타입으로 표현할 수 없는 값이 산출되었을 경우, 오버플로우가 발생하고 쓰레기값(엉뚱한 값)을 얻을 수 있다.

- 코드에서 피연산자의 값을 직접 리터럴로 주는 경우는 드물다.
  
    - 대부분은 사용자로부터 입력받거나 프로그램 실행 도중에 생성되는 데이터로 산술 연산이 수행된다.
    
    - 이런 경우 바로 **산술 연산자를 사용하지 말고 메소드를 이용하는 것이 좋다.** 메소드는 산술 연산을 하기 전에 피연산자들의 값을 조사해서 오버플로우를 탐지할 수 있기 때문이다.

```java
public class CheckOverflowExample {
    public static void main(String[] args) {
        try {
            int result = safeAdd(2000000000, 2000000000);
            System.out.println(result);
        } catch(ArithmeticException e) {
            System.out.println("오버플로우가 발생하여 정확하게 계산 불가");
        }
    }

    public static int safeAdd(int left, int right) {
        if((right > 0)) {
            if(left > (Integer.MAX_VALUE - right)) {
                throw new ArithmeticException("오버플로우 발생");
            }
        } else {  // 'right <= 0' 인 경우
            if(left < (Integer.MIN_VALUE - right)) {
                throw new ArithmeticException("오버플로우 발생");
            }
        }
        return left + right;
    }
}
```

```
오버플로우가 발생하여 정확하게 계산 불가
```

<br>

> **NaN과 Infinity 연산**

- `/` 또는 `%` 연산자를 사용할 때, 좌측 피연산자가 정수 타입인 경우 나누는 수인 우측 피연산자는 0을 사용할 수 없다. 만일 0으로 나누면 컴파일은 정상적으로 되지만, 실행 시 ArithmeticException(예외)이 발생한다.

```
5 / 0 -> ArithmeticException 예외 발생
5 % 0 -> ArithmeticException 예외 발생
```

- ArithmeticException이 발생했을 경우 프로그램이 종료되지 않게 하려면 예외 처리를 해야한다.

```java
try {
    int z = x % y;
    System.out.println("z: " + z);
} catch(ArithmeticException e) {
    System.out.println("0으로 나누면 안됨");
}
```

- 하지만 실수 타입인 0.0 또는 0.0f로 나누면 ArithmeticException이 발생하지 않고 `/` 연산의 결과는 `Infinity` 값을 가지며, % 연산의 결과는 `NaN(Not a Number)` 을 가진다. 

```
5 / 0.0 -> Infinity
5 % 0.0 -> NaN
```

- 프로그램 코드에서 Infinity 혹은 NaN 값을 확인하려면 `Double.isInfinite()` 와 `Double.isNaN()` 메소드를 이용하면 된다.

    - 이 메소드들은 double 타입의 값을 매개값으로 받아서 이 값이 Infinity 혹은 NaN이라면 true를 리턴하고, 아니라면 false를 리턴한다.

<br>

> **입력값의 NaN 검사**

- 부동소수점(실수)을 입력받을 때는 반드시 NaN 검사를 해야 한다.

```java
public class InputDataCheckNaN {
    public static void main(String[] args) {
        String userInput = "NaN";
        double val = Double.valueOf(userInput);

        double currentBalance = 10000.0;

        if (Double.isNaN(val)) {
            System.out.println("NaN이 입력되어 처리할 수 없음");
            val = 0.0;
        }

        currentBalance += val;
        System.out.println(currentBalance);
    }
}
```

```
NaN이 입력되어 처리할 수 없음
10000.0
```

<br>

### 문자열 연결 연산자(+)

- 문자열 연결 연산자인 `+` 는 문자열을 서로 결합하는 연산자이다.

```java
String str1 = "JDK" + 6.0;  // JDK6.0
String str2 = str1 + " 특징";  // JDK6.0 특징
```

```java
3 + 3.0 + "JDK";  // 6.0JDK
```

<br>

### 비교 연산자(<, <=, >, >=, ==, !=)

- 비교 연산자는 대소 또는 동등을 비교해서 boolean 타입인 true나 false를 산출한다.

  - 비교 연산자는 흐름 제어문인 조건문(if), 반복문(for, while)에서 주로 이용되어 실행 흐름을 제어할 때 사용된다.

```
('A' < 'B') -> (65 < 66)  피연산자가 char 타입이면 유니코드 값으로 비교

'A' == 65 -> true
3 == 3.0 -> true
```

<br>

> 비교 연산자의 예외

```
0.1 == 0.1f -> false
```

- 이진 포맷의 가수를 사용하는 모든 부동소수점 타입은 0.1을 정확히 표현할 수가 없어서 0.1f는 0.1의 근사값으로 표현되어 0.10000000149011612와 같은 값이 되기 때문에 0.1보다 큰 값이 되어버린다.

    - 피연산자를 모두 float 타입으로 강제 타입 변환하던가 정수로 변환해서 비교 연산을 하면 해결할 수 있다.

<br>

> String 타입의 문자열 비교

```java
String strVar1 = "임준섭";
String strVar2 = "임준섭";
String strVar3 = new String("임준섭");
```

- 자바는 문자열 리터럴이 동일하다면 동일한 String 객체를 참조하도록 되어 있다. 그래서 strVar1과 strVar2는 동일한 String 객체의 번지값을 가지고 있다.

- 그러나 strVar3은 객체 생성 연산자인 new로 생성한 새로운 String 객체의 번지값을 가지고 있다.
  
```
strVar1 == strVar2   ->    true
strVar2 == strVar3   ->    false
```

- 동일한 String 객체이건 다른 String 객체이건 상관없이 String 객체의 문자열만을 비교하고 싶다면 `==` 연산자 대신에 `equals()` 메소드를 사용해야 한다.

- `equals()` 메소드는 원본 문자열과 매개값으로 주어진 비교 문자열이 동일한지 비교한 후 true 또는 false를 리턴한다.

```java
boolean result = str1.equals(str2);  // str1 : 원본 문자열, str2 : 비교 문자열
```

```
strVar1.equals(strVar2)    ->    true
strVar2.equals(strVar3)    ->    true
```

<br>

### 논리 연산자

- AND(논리곱) : `&&` , `&`

- OR(논리합) : `||` , `|`

- XOR(배타적 논리합) : `^`

- NOT(논리부정) : `!`

<br>

> `&&` 과 `&` ( `||` 과 `|`) 의 차이

- 둘의 산출 결과는 같지만 연산 과정이 조금 다르다.

- `&&`는 앞의 피연산자가 false라면 뒤의 피연산자를 평가하지 않고 바로 false라는 산출 결과를 낸다. 그러나 `&`는 두 피연산자 모두를 평가해서 산출 결과를 낸다.

    - 따라서 `&` 보다는 `&&`가 더 효율적으로 동작한다.

- `||` , `|` 도 마찬가지이다.

    - `||` 는 앞의 피연산자가 true라면 뒤의 피연산자를 평가하지 않고 바로 true라는 산출 결과를 낸다.

<br>

### 비트 연산자

<br>

### 삼항 연산자 : `?:` (Ternary-operator 세 개 한 벌 - 연산자)

- 삼항 연산자는 `?` 앞의 조건식에 따라 콜론 `:` 앞뒤의 피연산자가 선택된다고 해서 조건 연산식이라고 부르기도 한다.

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/54904863-f508fc80-4f22-11e9-87a1-6eb5c43fa239.jpg'>
</p>

<br>

> 삼항 연산자를 이용했을 때

```java
int scor = '95';
char grade = (score > 90) ? 'A' : 'B'
```

> if문을 이용했을 때

```java
int score = 95;
char grage;
if(score > 90) {
    grage = 'A';
} else {
    grade = 'B';
}
```

<br>

> 삼항 연산자 예시

- 삼항 연산자를 중첩해서 사용할 수 있다.

```java
public class ConditionalOperationExample {
    public static void main(String[] args) {
        int score = 85;
        char grade = (score > 90) ? 'A' : ((score >80) ? 'B' : 'C');
        System.out.println(score + "점은 " + grade + "등급입니다.");
    }
}
```
