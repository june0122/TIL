# 챕터 02. 변수와 타입

## 변수
 
`리터럴(literal)` : 소스 코드 내에서 직접 입력된 값

## 데이터 타입

### 정수 타입



### 실수 타입

- 자바의 실수 리터럴의 기본 타입은 double
    
    - 실수 리터럴을 float 타입 변수에 그냥 저장할 수 없다.

    - 실수 리터럴을 float에 저장하려면 리터럴 뒤에 소문자 'f'나 대문자 'F'를 붙여야 한다.

<br>

## 타입 변환

### 자동 타입 변환(Promotion)

- 프로그램 실행 도중에 자동적으로 타입 변환이 일어나는 것.
  
- 작은 크기를 가지는 타입이 큰 크기를 가지는 타입에 저장될 때 발생한다.

```java
int intValue = 200;
double doubleValue = intValue;  // 200.0
```

- char 타입의 경우 int 타입으로 자동 변환되면 유니코드 값이 int 타입에 저장.
  
```java
char charValue = 'A';
int intValue = charValue;  // 65가 저장
```

> 자동 타입 변환의 예외 : **byte 타입을 char 타입으로 자동 변환 X**

- char은 2byte의 크기를 가지지만, char의 범위는 0~65535 이므로 음수 저장 불가
  
    - char 타입을 short에 저장하는 것도 불가능하다. (short 범위 : -32,768~32,767) 

```java
byte byteValue = 65;
char charValue = byteValue;  // 컴파일 에러
char charValue = (char) byteValue;  // '강제' 타입 변환
```

<br>

### 강제 타입 변환(Casting)

- 큰 크기의 타입은 작은 크기의 타입으로 자동 타입 변환 불가
  
- 강제적으로 큰 데이터 타입을 작은 데이터 타입으로 쪼개어서 저장하는 것을 캐스팅이라 한다.

- 큰 데이터 타입을 작은 데이터 타입의 크기로 쪼개어 **한 조각만 저장**한다.

> int -> byte casting

```java
int intValue = 103029770;
byte byteValue = (byte) intValue;  // 강제 타입 변환(캐스팅)
```

<br>

> float, double -> int casting

- 소수점 이하 부분은 버려지고, 정수 부분만 저장된다.

```java
double doubleValue = 3.14;
int intValue = (int) doubleValue;  // intValue는 정수 부분인 3만 저장된다.
```

<br>

***`강제 타입 변환의 유의점 1`*** : 값을 변환할 때 값의 손실이 발생하면 안된다.

- 캐스팅 전, 값이 안전하게 보존될 수 있는지 검사를 한다.

```java
public class CheckValueBeforeCasting {
    public static void main(String[] args) {
        int i = 128;

        if ((i < Byte.MIN_VALUE) || (i > Byte.MAX_VALUE)) {
            System.out.println("byte 타입으로 변환할 수 없습니다.");
            System.out.println("값을 다시 확인해주세요.");
        } else {
            byte b = (byte) i;  // 캐스팅을 해주지 않으면 오류 발생
            System.out.println(b);
        }
    }
}
```

<br>

> 자바의 기본 타입 최대값과 최솟값의 상수

|기본 타입|최대값 상수||
|---|---|---|
|byte|Byte.MAX_VALUE|Byte.MIN_VALUE|
|short|Short.MAX_VALUE|Short.MIN_VALUE|
|int|Integer.MAX_VALUE|Integer.MIN_VALUE|
|long|Long.MAX_VALUE|Long.MIN_VALUE|
|float|Float.MAX_VALUE|Float.MIN_VALUE|
|double|Double.MAX_VALUE|Double.MIN_VALUE|

<br>

***`강제 타입 변환의 유의점 2`*** : 정수 타입을 실수 타입으로 변환 시, **정밀도 손실**을 피해야 한다.

```
float : 부호(1비트) + 지수(8비트) + 가수(23비트)

double : 부호(1비트) + 지수(11비트) + 가수(52비트)
```

<br>

### '연산식'에서의 자동 타입 변환

- 연산은 기본적으로 같은 타입의 피연산자 간에만 수행되기 때문에 서로 다른 타입의 피연산자가 있을 경우 두 연산자 중 크기가 큰 타입으로 자동 변환된 후 연산을 수행한다.

- 자바는 정수 연산일 경우 **int 타입을 기본**으로 한다.

    - 피연산자를 4byte 단위로 저장하기 때문에 크기가 4byte보다 작은 타입(byte, char, short)은 4byte인 int 타입으로 변환된 후 연산이 된다. 따라서 연산의 결과도 int 타입이 된다.

```java
byte byteValue = 10;
short shortValue = 15;

byte result = byteValue + byteValue;    // 컴파일 에러
short result2 = shortValue + shortValue;   // 컴파일 에러
```
