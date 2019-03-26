# 챕터 05. 참조 타입

## 데이터 타입 분류

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/54969375-29cb9100-4fc1-11e9-97e0-8352be94ec35.png'>
</p>

<br>

- 기본 타입으로 선언된 변수와 참조 타입으로 선언된 변수의 차이점은 저장되는 값이 무엇이냐이다.
  
    - 기본 타입의 변수들은 실제 값을 변수 안에 저장한다.

    - 참조 타입의 변수들은 메모리의 번지를 값으로 갖는다. (번지를 통해 객체 참조)

- 변수는 스택 영역에 생성되고, 객체는 힙 영역에서 생성된다.

<br>

## 메모리 사용 영역

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/54969701-9d21d280-4fc2-11e9-9f5f-ea8bf1e44077.png'>
</p>

<br>

### 메소드(Method) 영역

- 코드에서 사용되는 **클래스들**을 클래스 로더로 읽어 클래스별로 `런타임 상수풀` , `필드 데이터` , `메소드 데이터` , `메소드 코드` , `생성자 코드` 등을 분류해서 저장한다.

- 메소드 영역은 JVM이 시작할 때 생성되고 모든 스레드가 공유하는 영역이다.

<br>

### 힙(heap) 영역

- **객체**와 **배열**이 생성되는 영역

- 힙 영역에서 생성돤 객체와 배열은 JVM 스택 영역의 변수나 다른 객체의 필드에서 참조한다.

    - 참조하는 변수나 필드가 없다면 의미 없는 객체가 되어버리기 때문에 이것을 쓰레기 취급하고 JVM은 **쓰레기 수집기(Garbage Collector)** 를 실행시켜 쓰레기 객체를 힙 영역에서 자동으로 제거한다.

        - 그렇기 때문에 개발자는 객체를 제거하기 위해 별도의 코드를 작성할 필요가 없다.

<br>

### JVM 스택(Stack) 영역

- JVM 스택 영역은 각 스레드마다 하나씩 존재하면 스레드가 시작될 때 할당된다.

- 메소드를 호출할 때마다 프레임(Frame)을 추가(push)하고, 메소드가 종료되면 해당 프레임을 제거(pop)하는 동작을 수행한다.

    - 예외 발생 시 printStackTrace() 메소드로 보여주는 Stack Trace의 각 라인은 하나의 프레임을 표현한다.

- 프레임 내부에는 로컬 변수 스택이 있는데, 기본 타입 변수와 참조 타입 변수가 추가(push)되거나 제거(pop)된다.

    - 변수가 이 영역에 생성되는 시점은 초기화가 될 때, 즉 최초로 변수에 값이 저장될 때이다.

    - 변수는 선언된 블록 안에서만 스택에 존재하고 블록을 벗어나면 스택에서 제거된다.

- 기본 타입 변수는 스택 영역에 직접 값을 가지고 있지만, `참조 타입 변수`는 값이 아니라 **힙 영역이나 메소드 영역의 객체 주소** 를 가진다.

<br>

## 참조 변수의 `==`, `!=` 연산

- 참조 타입 변수들 간의 `==`, `!=` 연산은 동일한 객체를 참조하는지, 다른 객체를 참조하는지 알아볼 때 사용된다.

  - 참조 타입 변수의 값은 힙 영역의 객체 주소이므로 결국 주소 값을 비교하는 것이 된다.

  - 동일한 주소 값을 갖고 있다는 것은 동일한 객체를 참조한다는 의미이다.

  - `==`, `!=` 연산자로 객체를 비교하는 코드는 일반적으로 **if문** 에서 많이 사용한다.

```
동일한 객체 참조 시, `==` 연산의 결과는 true, `!=` 연산의 결과는 false
```

<br>

## null과 NullPointerException

- 참조 타입 변수는 힙 영역의 객체를 참조하지 않는다는 뜻으로 null 값을 가질 수 있다.

- null값도 초기값으로 사용할 수 있기 때문에 null로 초기화된 참조 변수는 스택 영역에 생성된다.

> 참조 타입 변수가 null 값을 가지는지 확인하려면 다음과 같이 `==`, `!=` 연산을 수행하면 된다.

```java
refVar1 == null;
refVar2 != null;
```

- 참조 변수를 사용하면서 가장 많이 발생하는 예외 중 하나는 **NullPointException**

    - 참조 타입 변수가 null을 가지고 있을 경우, 참조 타입 변수는 사용할 수 없다.

    - 참조 타입 변수를 사용하는 것은 곧 객체를 사용하는 것을 의미 → 챰조할 객체가 없으므로 사용할 수가 없는 것이다.

<br>

> **NullPointException** 의 예시

```java
int[] intArray = null;
intArray[0] = 10;   // NullPointException
```

```java
String str = null;
System.out.println("총 문자수: " + str.length());  // NullPointException
```

<br>

## String 타입

```java
String name1 = "신민철";
String name2 = "신민철";
String name3 = new String("신민철");

// (name1 == name2) → true
// (name1 == name3) → false
```

- 동일한 String 객체건, 다른 String 객체이건 상관없이 문자열만을 비교할 때에는 String 객체의 `equals() 메소드`를 사용한다.

```java
boolean result = str1.equals(str2);  // str1 : 원본 문자열, str2 : 비교 문자열
```

- String 변수는 참조 타입이므로 초기값으로 null을 대입할 수 있다.

- 다음 코드처럼 hobby 변수가 String 객체를 참조하였으나, null을 대입함으로써 더 이상 String 객체를 참조하지 않도록 할 수 있다.

```java
String hobby = "여행";
hobby = null;
```

- 그렇다면 참조를 잃은 String 객체는 어떻게 될까?

    - JVM은 참조되지 않은 객체를 쓰레기 객체로 취급하고 쓰레기 수집기(Garbage Collector)를 구동시켜 메모리에서 자동 제거한다.

<br>

## 배열 타입

### 배열 선언

> 배열 변수 선언의 두 가지 방법

```java
타입[] 변수;  // 이 방법을 사용하자!

타입 변수[];
```

<br>

### 값 목록으로 배열 생성

- **값의 목록으로 배열 객체를 생성할 때 주의할 점** : 배열 변수를 이미 선언한 후에 다른 실행문에서 중괄호를 사용한 배열 생성은 허용되지 않음.

```java
타입[] 변수;
변수 = { 값0, 값1, 값2, 값3, ... };  // 컴파일 에러
```

- 배열 변수를 미리 선언한 후, 값 목록들이 나중에 결정되는 상황이라면 다음과 같이 new 연산자를 사용해서 값 목록을 지정해주면 된다.

```java
변수 = new 타입[] { 값0, 값1, 값2, 값3, ... };
```

- 예를 들어 배열 names를 다음과 같이 생성할 수 있다.

```java
String names = null;
names = new String[] { "임준섭", "김도형", "함형규" }
```

```java
public class ArrayCreateByValueList {
    public static void main(String[] args) {
        int[] scores;
        scores = new int[] { 83, 90, 87 };
        int sum1 = 0;
        for(int i = 0; i < 3; i++) {
            sum1 += scores[i];
        }
        System.out.println("총합 : " + sum1);

        int sum2 = add( new int[] { 83, 90, 87 });
        System.out.println("총합 : " + sum2);
        System.out.println();
    }

    public static int add(int[] scores) {
        int sum = 0;
        for(int i = 0; i < 3; i++) {
            sum += scores[i];
        }
        return sum;
    }
}
```

<br>

### new 연산자로 배열 생성

- 값의 목록을 가지고 있지 않지만, 향후 값들을 저장할 배열을 미리 만들고 싶다면 `new 연산자`로 다음과 같이 배열 객체를 생성시킬 수 있다.

```java
타입[] 변수 = new 타입[길이];

// int[] intArray = new int[5];
// 길이가 5인 int[] 배열
```

```java
타입[] 변수 = null;
변수 = new 타입[길이];
```

<br>

### 배열 길이

- 베열 객체의 length 필드를 읽으면 코드에서 배열의 길이를 얻을 수 있다.

```
배열변수.length;
```

- 다음은 배열 intArray의 길이를 알아보는 코드이다. num에는 3이 저장된다.

```java
int[] intArray = { 10, 20, 30 };
int num = intArray.lenth;
```

- length 필드는 읽기 전용 필드이기 때문에 값을 바꿀 수가 없다.

```java
intArray.length = 10;  // 잘못된 코드 
```

- for문 등에서 배열 인덱스를 초과해서 사용하면 `ArrayIndexOutOfBoundsException`이 발생한다.

<br>

### 커맨드 라인 입력

> String[] args가 필요한 이유

```java
public static void main(String[] args) { . . . }
```

- 'java 클래스'로 프로그램을 실행하면 JVM은 **길이가 0인 String 배열을 먼저 생성**하고 main() 메소드를 호출할 때 *매개값으로 전달* 한다.

<br>

### 다차원 배열

```java
int[][] scores = new int[2][3];
```

- 위의 코드는 메모리에 세 개의 배열 객체를 생성한다.

  - 배열 변수인 scores는 길이 2인 베열 A를 참조한다.

    - 배열 A의 `scores[0]`은 다시 길이 3인 배열 B를 참조한다.
  
    - `scores[1]` 역시 길이 3인 배열 C를 참조한다.

  - `scores[0]`과 `scores[1]`은 모두 ***★ 배열을 참조하는 변수 역할*** 을 한다.

```java
scores.length;      // 2 (배열 A의 길이)
scores[0].length;   // 3 (배열 B의 길이)
scores[1].length;   // 3 (배열 C의 길이)
```

<br>

- 자바는 **일차원 배열이 서로 연결된 구조로 다차원 배열을 구현**한다.

    - 때문에 수학 행렬 구조가 아닌 **계단식 구조** 를 가질 수 있다.

```java
int[][] scores = new int[2][];
scores[0] = new int[2];  // | 0 | 1 | 
scores[1] = new int[3];  // | 0 | 1 | 2 |
```

- 위의 경우 배열 항목의 수를 조사해보면 다음과 같다.

```java
scores.length;      // 2 (배열 A의 길이)
scores[0].length;   // 2 (배열 B의 길이)
scores[1].length;   // 3 (배열 C의 길이)
```

<br>

> 다차원 배열 개념 종합 예제

```java
public class ArrayInArrayExample {
    public static void main(String[] args) {

        int[][] mathScores = new int[2][3];   // 일반적인 다차원 배열
        for (int i = 0; i < mathScores.length; i++) {
            for (int k = 0; k < mathScores[i].length; k++) {
                System.out.println("mathScores[" + i + "][" + k + "] = " + mathScores[i][k]);
            }
        }
        System.out.println();

        int[][] englishScores = new int[2][];  // 계단식 배열
        englishScores[0] = new int[2];
        englishScores[1] = new int[3];
        for (int i = 0; i < englishScores.length; i++) {
            for (int k = 0; k < englishScores[i].length; k++) {
                System.out.println("englishScores[" + i + "][" + k + "] = " + englishScores[i][k]);
            }
        }
        System.out.println();

        int[][] javaScores = {{95, 80}, {92, 96, 80}};  // 그룹화된 값 목록
        for (int i = 0; i < javaScores.length; i++) {
            for (int k = 0; k < javaScores[i].length; k++) {
                System.out.println("javaScores[" + i + "][" + k + "] = " + javaScores[i][k]);
            }
        }

    }
}
```

<br>

### 객체를 참조하는 배열

- 기본 타입 배열은 각 항목에 직접 값을 갖고 있다.
  
- 하지만 참조 타입(클래스, 인터페이스) 배열은 각 항목에 객체의 번지를 가지고 있다.

    - 예로, String은 클래스 타입이므로 String[] 배열은 각 항목에 문자열이 아니라, String 객체의 주소를 가지고 있다.
    
    - 즉 String 객체를 참조하게 된다.

<br>

### 배열 복사

- **배열은 한 번 생성하면 크기를 변경할 수 없기 때문에** 더 많은 저장 공간이 필요하다면 보다 큰 배열을 만들고 이전 배열로부터 항목 값들을 복사해야 한다.

  - for문을 사용하거나
  
  - `System.arraycopy()` 메소드를 사용한다.

<br>

> for문을 이용한 배열 복사

```java
public class ArrayCopyByFor {
    public static void main(String[] args) {
        int[] oldIntArray = {1, 2, 3, 4, 5};
        int[] newIntArray = new int[oldIntArray.length + 2];

        for (int i = 0; i < oldIntArray.length; i++) {
            newIntArray[i] = oldIntArray[i];
        }

        System.out.print("int[] newIntArray = { ");
        for (int i = 0; i < newIntArray.length; i++) {
            if (i == newIntArray.length - 1) {
                System.out.print(newIntArray[i]);
                break;
            }
            System.out.print(newIntArray[i] + ", ");
        }
        System.out.println(" }");
    }
}
```

```
int[] newIntArray = { 1, 2, 3, 4, 5, 0, 0 };
```

<br>

> System.arraycopy() 메소드를 이용한 배열 복사

```java
System.arrraycopy(Object src, int srcPos, Object dest, int desPos, int length);

// 원본 배열 : arr1, 새 배열 : arr2
// System.arraycopy(arr1, 0, arr2, 0, arr1.length);
```

- 참조 타입 배열의 경우, 배열 복사가 되면 복사가 되는 값이 객체의 번지이므로 **새 배열의 항목은 이전 배열의 항목이 참조하는 객체와 동일** 하다.

    - 이것을 `얕은 복사(shallow copy)`라 한다.

    - 참조하는 객체도 별도로 생성하는 것을 `깊은 복사(deep copy)`라 한다.

<br>

### ☆ 향상된 for문

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/54979265-14b52900-4fe6-11e9-9f26-3c1c59f3cb3d.png'>
</p>

<br>

> 향상된 for문 예제

```java
public class AdvancedForExample {
    public static void main(String[] args) {
        int[] scores = {95, 71, 84, 93, 87};

        int sum = 0;

        for (int score : scores) {
            sum += score;
        }
        System.out.println("점수 총합 = " + sum);

        double avg = (double) sum / scores.length;
        System.out.println("점수 평균 = " + avg);
    }
}
```

<br>

## 열거 타입(enumeration type)

데이터 중에는 **몇 가지로 한정된 값만을 갖는 경우** 가 흔히 있다. 예를 들어 요일에 대한 데이터는 `월, 화, 수, 목, 금, 토, 일` 이라는 7개의 값만을 갖고, 계절에 대한 데이터는 `봄, 여름, 가을, 겨울` 이라는 4개의 값만을 갖는다.

이와 같이 한정된 값만을 갖는 데이터 타입이 열거 타입이다. **`열거 타입`** 은 **몇 개의 열거 상수(enumeration constant) 중에서 하나의 상수를 저장하는 데이터 타입**이다.

<br>

> enum의 기본적인 장점

- 문자열과 비교해, **IDE의 적극적인 지원**을 받을 수 있다.
  
    - 자동완성, 오타검증, 텍스트 리팩토링 등

- 허용 가능한 값들을 제한할 수 있다.

- **리팩토링시 변경 범위가 최소화** 된다.

    - 내용의 추가가 필요하더라도, Enum 코드외에 수정할 필요가 없다.

<br>

> Java Enum의 장점

- C/C++ 의 경우 Enum이 결국 int 값이지만, Java Enum은 완전한 기능을 갖춘 **클래스** 이다.

<br>

### 열거 타입 선언

- 열거 타입을 선언하기 위해서는 열거 타입의 이름을 정하고 열거 타입 이름으로 소스 파일 `.java` 을 생성해야 한다.

    - 열거 타입 이름은 관례적으로 첫 문자를 대문자로 하고 나머지는 소문자로 구성한다.

    - 여러 단어로 구성된 이름이라면 단어 첫 문자는 대문자로 하는 것이 관례이다.

- 열거 상수는 모두 대문자로 작성한다.

    - 만약 열거 상수가 여러 단어로 구성되었다면 단어 사이를 밑줄 `_` 로 연결하는 것이 관례이다.

<br>

> 열거 타입 소스 파일들의 이름 예시

```java
Week.java
MemberGrade.java
ProductKind.java
```

```java
---------------------------------
 Week.java
---------------------------------

public enum Week {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}
```

<br>

- Intellij 에선 `File - New - Java Class` 경로로 들어가 `Kind` 항목에서 `Enum`을 선택해주면 된다.

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/54981274-3f55b080-4feb-11e9-815f-2e6102279e1a.PNG'>
</p>

<br>

### 열거 타입 변수

- 열거 타입도 하나의 데이터 타입이므로 변수를 선언하고 사용해야 한다.

```
열거타입 변수;
```

- 예를 들어 열거 타입 Week로 변수를 선언하면 다음과 같다.

```java
Week today;
Week reservationDay;
```

- 열거 타입 변수를 선언했다면 다음과 같이 열거 상수를 저장할 수 있다.

    - 열거 상수는 단독으로 사용할 수 없고 반드시 `열거타입.열거상수`로 사용된다.

```java
열거타입 변수 = 열거타입.열거상수;
```

- 예를 들어 today 열거 변수에 열거 상수인 SUNDAY를 저장하면 다음과 같다.

```java
Week today = Week.SUNDAY,
```

- 열거 타입 변수는 null 값을 저장할 수 있는데 열거 타입도 참조 타입이기 때문이다.

```java
Week Birthday = null;
```

<br>

> 열거 상수는 열거 객체로 생성된다.

- 열거 타입 Week의 경우 MONDAY부터 SUNDAY까지의 열거 상수는 다음과 같이 총 7개의 Week 객체로 생성된다. 그리고 메소드 영역에 생성된 열거 상수가 해당 Week 객체를 각각 참조하게 된다.

> 그러므로 `today 변수` 와 `Week.SUNDAY` 상수의 `==` 연산 결과는 true 가 된다.

```java
today == Week.SUNDAY;    // true
```

- 그렇다면 다음의 코드에서 week1과 week2 의 `==` 연산의 결과가 왜 true가 나오게 되는지 이해할 수 있을 것이다.

- week1과 week2는 모두 Week.SATURDAY 상수와 같이 동일한 Week 객체를 참조하기 때문이다.

```java
Week week1 = Week.SATURDAY;
Week week2 = Week.SATURDAY;
System.out.println( week1 == week2 );  // true
```

<br>

## ★★ 메모리 구조를 떠올리며 enum class의 생성 흐름을 생각해보자

- ★ Java Enum은 완전한 기능을 갖춘 **클래스** 이다.

    - 메모리의 `메소드 영역`에 enum class가 생성

    - enum class 내부의 열거 상수가 `힙`에 생성된 enum class 객체를 각각 참조한다.

        - 객체를 `힙` 영역에 생성했는데 참조하는 **변수** 나 필드가 없다면 JVM은 **가비지 컬렉터** 를 실행시켜 쓰레기 객체를 `힙` 영역에서 자동적으로 제거한다.

    - 그러므로 `힙` 영역에 생성된 객체를 참조할 변수를 **`스택` 영역에 초기화**, 즉 생성해야 한다. (변수가 스택에 생성되는 시점은 초기화 될 때, 즉 최초로 변수에 값이 저장될 때이다.)

<br>

## 같이 읽어보면 좋은 자료

- ### [자바 메모리 구조](https://goddaehee.tistory.com/149)

- ### [Java Enum 활용기 by 우아한 형제들](http://woowabros.github.io/tools/2017/07/10/java-enum-uses.html)

<br>

### 열거 타입과 열거 상수 예제 (Calendar의 이용)

> Calendar 변수 선언 후, Calendar.getInstance() 메소드가 리턴하는 Calendar 객체를 얻기

```java
Calendar now = Calendar.getInstance();
```

> Calendar 객체를 얻은 후, get() 메소드를 이용해서 년, 월, 일, 요일, 시간, 분, 초를 다음과 같이 얻기

```java
int year = now.get(Calendar.YEAR);          // 년
int month = now.get(Calendar.MONTH);        // 월(1~12)
int day = now.get(Calendar.DAY_OF_MONTH);   // 일
int week = now.get(Calendar.DAY_OF_WEEK);   // 요일(1~7)
int hour = now.get(Calendar.HOUR);          // 시간
int minute = now.get(Calendar.MINUTE);      // 분
int second = now.get(Calendar.SECOND);      // 초
```

> Calendar 예제

```java
import java.util.Calendar;

public class EnumWeekExample {
    public static void main(String[] args) {
        Week today = null;  // 열거 타입 변수 선언

        Calendar cal = Calendar.getInstance();
        // Calendar 변수 선언 후 Calendar.getInstance() 메소드가 리턴하는 Calendar 객체를 얻는다.
        int week = cal.get(Calendar.DAY_OF_WEEK);  // 일(1) ~ 토(7)까지의 숫자를 리턴

        switch (week) {
            case 1:
                today = Week.SUNDAY;  // 열거 타입 변수 today, 열거 상수 Week.SUNDAY : 서로 같은 Week 객체 참조
                break;
            case 2:
                today = Week.MONDAY;  // 열거 타입 변수 today, 열거 상수 Week.MONDAY : 서로 같은 Week 객체 참조
                break;
            case 3:
                today = Week.TUESDAY;
                break;
            case 4:
                today = Week.WEDNESDAY;
                break;
            case 5:
                today = Week.THURSDAY;
                break;
            case 6:
                today = Week.FRIDAY;
                break;
            case 7:
                today = Week.SATURDAY;
                break;
        }

        System.out.println("오늘 요일: " + today);

        if (today == Week.SUNDAY) {
            System.out.println("일요일에는 축구를 합니다.");
        } else {
            System.out.println("열심히 자바 공부합니다.");
        }
    }
}
```

<br>

### 열거 객체의 메소드

- 열거 객체는 열거 상수의 문자열을 내부 데이터로 가지고 있다.

- 메소드는 `java.lang.Enum` 클래스 사이에 선언된 메소드인데, 열거 객체에서 사용할 수 있는 이유는 모든 열거 타입은 컴파일 시에 Enum 클래스를 상속하게 되어 있기 때문이다.

|리턴 타입|메소드(매개 변수)|설명|
|---|---|---|
|String|name()|열거 객체의 문자열을 리턴|
|int|ordinal()|열거 객체의 순번(0부터 시작)을 리턴|
|int|compareTo()|열거 객체를 비교해서 순번 차이를 리턴|
|열거 타입|valueOf(String name)|주어진 문자열의 열거 객체를 리턴|
|열거 배열|values()|모든 열거 객체들을 배열로 리턴|

<br>

> 열거 객체의 메소드

```java
public class EnumMethodExample {
    public static void main(String[] args) {
        // name() 메소드
        Week today = Week.SUNDAY;
        String name = today.name();
        System.out.println(name);

        // ordinal() 메소드
        int ordinal = today.ordinal();
        System.out.println(ordinal);

        // compareTo() 메소드
        Week day1 = Week.MONDAY;
        Week day2 = Week.WEDNESDAY;
        int result1 = day1.compareTo(day2);
        int result2 = day2.compareTo(day1);
        System.out.println(result1);
        System.out.println(result2);

        // valueOf 메소드
        if (args.length == 1) {
            String strDay = args[0];
            Week weekDay = Week.valueOf(strDay);
            if (weekDay == Week.SATURDAY || weekDay == Week.SUNDAY) {
                System.out.println("주말 이군요");
            } else {
                System.out.println("평일 이군요");
            }
        }

        // values() 메소드
        Week[] days = Week.values();
        for (Week day : days) {
            System.out.println(day);
        }
    }
}
```

```
SUNDAY
6
-2
2
// 주말 이군요  라는 결과가 나와야하는데 나오지 않음. 확인 필요 
MONDAY
TUESDAY
WEDNESDAY
THURSDAY
FRIDAY
SATURDAY
SUNDAY
```

---

> 문제 1 : 괄호 `()` 안의  숫자 맞추기

```java
int[][] array = {
    {95, 86},
    {83, 21, 64},
    {12, 75, 21, 64, 71}
};
```

```
// 문제

array.length  →  ( 3 )

array[2].length  →  ( 5 )
```

<br>

> 문제 2 : for문을 이용해 주어진 배열의 최대값 구하기

```java
public class MaxOfArray {
    public static void main(String[] args) {
        int max = 0;
        int[] array = {1, 5, 3, 8, 2};

        for (int i = 0; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }
        System.out.println("max : " + max);
    }
}
```

<br>

> 문제 3 : 주어진 배열의 전체 항목의 합과 평균값을 구하시오.

- count 방식 1

```java
public class SumOfArrayAndAvg {
    public static void main(String[] args) {
        int[][] array = {
                {95, 86},
                {83, 92, 96},
                {78, 83, 93, 87, 88},
        };

        int sum = 0;
        int count = 0;
        double avg = 0.0;

        for (int i = 0; i < array.length; i++) {
            for (int k = 0; k < array[i].length; k++) {
                sum += array[i][k];
            }
            count += array[i].length;  // array[i].length = i행 항목의 갯수
        }
        avg = (double) sum / count;  // double 형으로 casting 중요

        System.out.println("sum: " + sum);
        System.out.println("avg: " + avg);
    }
}
```

- count 방식 2

```java
public class SumOfArrayAndAvg {
    public static void main(String[] args) {
        int[][] array = {
                {95, 86},
                {83, 92, 96},
                {78, 83, 93, 87, 88},
        };

        int sum = 0;
        int count = 0;
        double avg = 0.0;

        for (int i = 0; i < array.length; i++) {
            for (int k = 0; k < array[i].length; k++) {
                sum += array[i][k];
                count++;
            }
        }
        avg = (double) sum / count;

        System.out.println("sum: " + sum);
        System.out.println("avg: " + avg);
    }
}

```

<br>

> 문제 4 :

```java
import java.util.Scanner;

public class StudentMaxAndAvg {
    public static void main(String[] args) {
        boolean run = true;
        int studentNum = 0;
        int[] scores = null;
        Scanner scanner = new Scanner(System.in);

        while (run) {
            System.out.println("-------------------------------------------------------");
            System.out.println("1.학생수 | 2.점수입력 | 3.점수리스트 | 4.분석 | 5.종료");
            System.out.println("-------------------------------------------------------");
            System.out.print("선택> ");

            int selectNo = scanner.nextInt();

            if (selectNo == 1) {
                System.out.print("학생수> ");
                studentNum = scanner.nextInt();
                scores = new int[studentNum];  // 객체 생성 안하면 NullPointerException 발생
            } else if (selectNo == 2) {
                for (int i = 0; i < scores.length; i++) {
                    System.out.print("scores[" + i + "]> ");
                    scores[i] = scanner.nextInt();
                }
            } else if (selectNo == 3) {
                for (int i = 0; i < scores.length; i++) {
                    System.out.println("scores[" + i + "]: " + scores[i]);
                }
            } else if (selectNo == 4) {
                int max = 0;
                int sum = 0;
                double avg = 0;

                for (int i = 0; i < scores.length; i++) {
                    sum += scores[i];
                    if (max < scores[i]) {
                        max = scores[i];
                    }
                }

                avg = (double) sum / scores.length;

                System.out.println("최고 점수: " + max);
                System.out.println("평균 점수: " + avg);
            } else if (selectNo == 5) {
                run = false;
            }
        }
        System.out.println("프로그램 종료");
    }
}
```

```
-------------------------------------------------------
1.학생수 | 2.점수입력 | 3.점수리스트 | 4.분석 | 5.종료
-------------------------------------------------------
선택> 1
학생수> 3
-------------------------------------------------------
1.학생수 | 2.점수입력 | 3.점수리스트 | 4.분석 | 5.종료
-------------------------------------------------------
선택> 2
scores[0]> 85
scores[1]> 95
scores[2]> 93
-------------------------------------------------------
1.학생수 | 2.점수입력 | 3.점수리스트 | 4.분석 | 5.종료
-------------------------------------------------------
선택> 3
scores[0]: 85
scores[1]: 95
scores[2]: 93
-------------------------------------------------------
1.학생수 | 2.점수입력 | 3.점수리스트 | 4.분석 | 5.종료
-------------------------------------------------------
선택> 4
최고 점수: 95
평균 점수: 91.0
-------------------------------------------------------
1.학생수 | 2.점수입력 | 3.점수리스트 | 4.분석 | 5.종료
-------------------------------------------------------
선택> 5
프로그램 종료

Process finished with exit code 0
```

- `scores = new int[studentNum];` 객체 생성 안하면 NullPointerException 발생

```
-------------------------------------------------------
1.학생수 | 2.점수입력 | 3.점수리스트 | 4.분석 | 5.종료
-------------------------------------------------------
선택> 3
Exception in thread "main" java.lang.NullPointerException
	at StudentMaxAndAvg.main(StudentMaxAndAvg.java:28)
```