# Chapter23. 구조체와 사용자 정의 자료형2

## 구조체의 정의와 typedef 선언

### typedef 선언

- typedef 선언은 기존에 존재하는 자료형의 이름에 새 이름을 부여하는 것을 목적으로 하는 선언이다.

```c
typedef int INT;  // int의 또 다른 이름 INT를 부여
```
  
- **자료형의 이름 int에 INT라는 이름을 추가로 붙여준다** 는 의미를 컴파일러에게 전달

- 다음의 형태로 int형 변수와 int형 포인터 변수를 선언할 수 있다.

```c
INT num;  // int num; 과 동일한 선언

INT * ptr  // int * ptr; 과 동일한 선언
```

<br>

> typedef 선언의 예제

```c
  1 #include <stdio.h>
  2
  3 typedef int INT;
  4 typedef int * PTR_INT;
  5
  6 typedef unsigned int UINT;
  7 typedef unsigned int * PTR_UINT;
  8
  9 typedef unsigned char UCHAR;
 10 typedef unsigned char * PTR_UCHAR;
 11
 12 int main(void) {
 13   INT num1 = 120;          // int num1 = 120;
 14   PTR_INT pnum1 = &num1;   // int * pnum1 = &num1;
 15
 16   UINT num2 = 190;         // unsigned int num2 = 190;
 17   PTR_UINT pnum2 = &num2;  // unsigned int * pnum2 = &num2;
 18
 19   UCHAR ch = 'Z';          // unsigned char ch = 'Z';
 20   PTR_UCHAR pch = &ch;     // unsigned char * pch = &ch;
 21
 22   printf("%d, %u, %c \n", *pnum1, *pnum2, *pch);
 23   return 0;
 24 }
```

```
120, 190, Z
```

<br>

- typedef 선언에 있어서 새로운 이름의 부여는 가장 마지막에 등장하는 단어를 중심으로 이뤄진다. 다음의 형태로 typedef가 선언되면 가장 마지막에 등장한 num3가 'num1 num2'에 부여된 새로운 이름이 된다.

  ```c
  typedef num1 num2 num3;
  ```

- typedef 선언을 통해서, **복잡한 유형의 자료형 선언을 매우 간결히 처리**할 수 있다.

- typedef로 정의되는 자료형의 이름은 **대문자로 시작하는 것이 관례**이다.

<br>

### 구조체의 정의와 typedef 선언

> 구조체의 정의

```c
struct point {        // 구조체의 정의
    int xpos;
    int ypos;
};
```

> 일반적인 구조체의 선언
 
```c
struct point pos;
```

> typedef를 이용한 구조체의 선언

```c
typedef struct point Point;  // struct point에 Point라는 이름을 부여
```

- 이는 `struct point`를 대신할 수 있도록 `Point` 라는 이름을 정의한 셈이니, 이후로는 다음과 같이 struct 선언을 생략한 형태로 구조체 변수를 선언할 수 있다.
  
    ```c
    Point pos;
    ```
- 사실 모든 구조체의 이름을 대상으로 struct 선언의 생략을 위한 typedef 선언이 등장한다. 때문에 **다음과 같이 구조체의 정의와 typedef 선언을 한데 묶는 것이 보다 일반적**이다.

    ```c
    typedef struct point {
        int xpos;
        int ypos;
    } Point;
    ```
- 위의 선언은 아래의 정의와 선언을 한데 묶은 것이다.
  
  ```c
  struct point {
      int xpos;
      int ypos;
  };

  typedef struct point Point;
  ```

> tyypedef 선언 예제

```c

  1 #include <stdio.h>
  2
  3 struct point {
  4   int xpos;
  5   int ypos;
  6 };
  7
  8 typedef struct point Point;
  9
 10 typedef struct person {
 11   char name[20];
 12   char phoneNum[20];
 13   int age;
 14 } Person;
 15
 16 int main(void) {
 17   Point pos = {10, 20};  // typedef를 사용하지 않았다면 struct point pos = {10, 20};
 18   Person man = {"이승기", "010-2321-5152", 31};
 19   printf("%d %d \n", pos.xpos, pos.ypos);
 20   printf("%s %s %d \n", man.name, man.phoneNum, man.age);
 21   return 0;
 22 }
```

> ※ 구조체의 이름

```c
typedef struct simple {
    . . . .
} SoSimple;
```

- 위의 구조체를 가리켜 simple 구조체라 부르기도 하고, SoSimple 구조체라 부르기도 한다. **typedef로 정의된 이름으로 해당 구조체를 부르는 것이 일반적**이긴 하다.

<br>

### 구조체의 이름 생략

앞서 정의한 구조체 person의 형태는 아래와 같다.

```c
typedef struct person {
    char name[20];
    char phoneNum[20];
    int age;
} Person;
```

  - 그런데 이렇게 정의가 되면, 구조체 변수를 선언할 때도 typedef에 의해 정의된 이름 Person을 사용하기 때문에 구조체의 이름 person은 사실상 별 의미를 지니지 않게 된다.
  
  - 따라서 다음과 같이 구조체의 이름을 생략하는 것이 가능하다.

> 구조체 이름의 생략

```c
typedef struct {
    char name[20];
    char phoneNum[20];
    int age;
} Person;
```

- 단, 다음과 같이 구조체의 이름을 생략하면, 다음의 형태로는 구조체 변수를 선언할 수 없게 된다.

    ```c
    struct person man;  // 불가능한 선언
    ```

<br>

## 함수로의 구조체 변수 전달과 반환

### 함수의 인자로 전달하고 return문에 의해 반환되는 구조체 변수

- 다음 코드를 실행하면, 인자로 전달되는 변수의 값은 매개변수 num에 전달(복사)된다.

```c
void SimpleFunc(int num) { . . . . }
int main(void) {
    int age = 24;
    SimpleFunc(age);  // age에 저장된 값이 매개변수 num에 전달(복사)
}
```

- 마찬가지로 구조체 변수도 함수의 인자로 전달될 수 있다.
  
- 이러한 인자를 전달받을 수 있도록 구조체 변수가 매개변수의 선언으로 올 수 있다.

- 그리고 구조체 변수의 값은 매개변수에 통째로 복사가 된다.

```c
  1 #include <stdio.h>
  2
  3 typedef struct point {
  4   int xpos;
  5   int ypos;
  6 } Point;
  7
  8 void ShowPosition(Point pos) {
  9   printf("[%d, %d] \n", pos.xpos, pos.ypos);
 10 }
 11
 12 Point GetCurrentPosition(void) {
 13   Point cen;  // 구조체 변수 cen 선언
 14   printf("Input current pos: ");
 15   scanf("%d %d", &cen.xpos, &cen.ypos);
 16   return cen;  // 구조체 변수 cen
 17 }
 18
 19 int main(void) {
 20   Point curPos = GetCurrentPosition();  // 함수가 반환 값으로 구조체 변수 curPos를 초기화
 21   ShowPosition(curPos);  // 함수 호출하며 curPos 인자를 ShowPosition 함수의 매개변수 pos에 전달하여 나란히 저장(복사)
 22   return 0;
 23 }
```

```
Input current pos: 2 4
[2, 4]
```

<br>

- 구조체의 멤버로 배열이 선언되어도 위 예제와 같은 동일한 형태의 복사가 진행된다.

- 즉 구조체의 멤버로 선언된 배열도 통째로 복사가 된다.

```c
  1 #include <stdio.h>
  2
  3 typedef struct person {
  4   char name[20];
  5   char phoneNum[20];
  6   int age;
  7 } Person;
  8
  9 void ShowPersonInfo(Person man){
 10   printf("name: %s \n", man.name);
 11   printf("phone: %s \n", man.phoneNum);
 12   printf("age: %d \n", man.age);
 13 }
 14
 15 Person ReadPersonInfo(void){
 16   Person man;
 17   printf("name? "); scanf("%s", man.name);
 18   printf("phone? "); scanf("%s", man.phoneNum);
 19   printf("age? "); scanf("%d", &man.age);
 20   return man;
 21 }
 22
 23 int main(void){
 24   Person man = ReadPersonInfo();
 25   ShowPersonInfo(man);
 26   return 0;
 27 }
```

```
name? Lim
phone? 01062226000
age? 20
name: Lim
phone: 01062226000
age: 20
```

<br>

> 구조체 변수를 대상으로 하는 **Call-by-reference** 예제

```c
  1 #include <stdio.h>
  2
  3 typedef struct point {
  4   int xpos;
  5   int ypos;
  6 } Point;
  7
  8 void OrgSymTrans(Point * ptr) {     // 원점대칭
  9   ptr->xpos = (ptr->xpos) * -1;
 10   ptr->ypos = (ptr->ypos) * -1;
 11 }
 12
 13 void ShowPosition(Point pos) {
 14   printf("[%d, %d] \n", pos.xpos, pos.ypos);
 15 }
 16
 17 int main(void) {
 18   Point pos = {7, -5};
 19   OrgSymTrans(&pos);  // pos의 값을 원점 대칭이동 시킨다.
 20   ShowPosition(pos);
 21   OrgSymTrans(&pos);  // pos의 값을 원점 대칭이동 시킨다.
 22   ShowPosition(pos);
 23   return 0;
 24 }
```

```
[-7, 5]
[7, -5]
```

<br>

### 구조체 변수를 대상으로 가능한 연산

- 기본 자료형 변수와는 달리, *구조체 변수* 를 대상으로는 **매우 제한된 형태의 연산만 허용**된다.

- 하용되는 가장 대표적인 연산은 `대입연산`이며, 그 외로는 `& 연산: 주소 값 반환 목적`이나 `sizeof : 구조체 변수의 크기 반환` 정도의 연산만 허용된다.

<br>

> 구조체 변수간 `대입연산`

```c
  1 #include <stdio.h>
  2
  3 typedef struct point {
  4   int xpos;
  5   int ypos;
  6 } Point;
  7
  8 int main(void) {
  9   Point pos1 = {1, 2};
 10   Point pos2;
 11   pos2 = pos1;  // pos1의 멤버 내 pos2의 멤버간 복사가 진행됨
 12
 13   printf("크기: %d \n", sizeof(pos1));  // pos1의 전체 크기 반환
 14   printf("[%d, %d] \n", pos1.xpos, pos1.ypos);
 15   printf("크기: %d \n", sizeof(pos2));  // pos2의 전체 크기 반환
 16   printf("[%d, %d] \n", pos2.xpos, pos2.ypos);
 17   return 0;
 18 }
```

```
크기: 8
[1, 2]
크기: 8
[1, 2]
```

- 구조체 변수간 **대입연산의 결과로 멤버 대 멤버의 복사가 이뤄짐**을 확인

<br>

> 구조체 변수 대상의 `덧셈과 뺄셈` → **함수를 정의**

```c
  1 #include <stdio.h>
  2
  3 typedef struct point {
  4   int xpos;
  5   int ypos;
  6 } Point;
  7
  8 Point AddPoint(Point pos1, Point pos2) {
  9   Point pos = {pos1.xpos + pos2.xpos, pos1.ypos + pos2.ypos};
 10   return pos;
 11 }
 12
 13 Point MinPoint(Point pos1, Point pos2) {
 14   Point pos = {pos1.xpos - pos2.xpos, pos1.ypos - pos2.ypos};
 15   return pos;
 16 }
 17
 18 int main(void) {
 19   Point pos1 = {5, 6};
 20   Point pos2 = {2, 9};
 21   Point result;
 22
 23   result = AddPoint(pos1, pos2);
 24   printf("[%d, %d] \n", result.xpos, result.ypos);
 25   result = MinPoint(pos1, pos2);
 26   printf("[%d, %d] \n", result.xpos, result.ypos);
 27   return 0;
 28 }
```

```
[7, 15]
[3, -3]
```

<br>

> ★ SwapPoint : 두 구조체 변수의 멤버를 서로 바꾸기

```c
  1 #include <stdio.h>
  2
  3 typedef struct point {
  4   int xpos;
  5   int ypos;
  6 } Point;
  7
  8 void SwapPoint(Point * ptr1, Point * ptr2) {
  9   Point temp = *ptr1;
 10   *ptr1 = *ptr2;
 11   *ptr2 = temp;
 12 }
 13
 14 int main(void) {
 15   Point pos1 = {2, 4};
 16   Point pos2 = {5, 7};
 17
 18   SwapPoint(&pos1, &pos2);
 19   printf("pos1: [%d, %d] \n", pos1.xpos, pos1.ypos);
 20   printf("pos2: [%d, %d] \n", pos2.xpos, pos2.ypos);
 21   return 0;
 22 }
```

```
pos1: [5, 7]
pos2: [2, 4]
```

<br>


## 구조체의 유용함에 대한 논의와 중첩 구조체

### 구조체를 정의하는 이유

> 구조체를 통해서 연관 있는 데이터를 하나로 묶을 수 있는 자료형을 정의하면, 데이터의 표현 및 관리가 용이해지고 , 그만큼 합리적인 코드를 작성할 수 있게 된다.

<br>

*** 중첩된 구조체의 정의와 변수의 선언

- 배열이나 포인터 변수가 구조체의 멤버로 선언될 수 있듯이, **구조체 변수도 구조체의 멤버로 선언될 수 있다.**
  
  - 이렇듯 구조체 안에 구조체 변수가 멤버로 존재하는 경우를 **구조체의 중첩**이라 한다.


```c
  1 #include <stdio.h>
  2
  3 typedef struct point {
  4   int xpos;
  5   int ypos;
  6 } Point;
  7
  8 typedef struct circle {
  9   Point cen;
 10   double rad;
 11 } Circle;
 12
 13 void ShowCircleInfo(Circle * cptr) {
 14   printf("[%d, %d] \n", (cptr->cen).xpos, (cptr->cen).ypos);
 15   printf("radius: %g \n\n", cptr->rad);
 16 }
 17
 18 int main(void) {
 19   Circle c1 = {{1, 2}, 3.5};  // 중괄호를 통해 구조체 멤버의 초기화를 구분 지을 수 있다.
 20   Circle c2 = {2, 4, 3.9};  // 중괄호로 구조체 변수의 초기화를 구분 짓지 않으면, 순서대로 초기화된다.
 21   ShowCircleInfo(&c1);
 22   ShowCircleInfo(&c2);
 23   return 0;
 24 }
```

```
[1, 2]
radius: 3.5

[2, 4]
radius: 3.9

```

- 참고로 구조체 변수를 초기화하는 경우에도 배열의 초기화와 마찬가지로 **초기화하지 않은 일부 멤버에 대해서는 0으로 초기화가 진행**된다.

<br>

## 공용체(Union Type)의 정의와 의미

### 구조체 vs. 공용체

> 구조체 sbox의 정의

```c
typedef struct sbox {
    int mem1;
    int mem2;
    double mem3;
} SBox;
```

<br>

> 공용체 ubox의 정의

```c
typedef union ubox {
    int mem1;
    int mem2;
    double mem3;
} UBox;
```

- 위의 코드에서 보이듯, 정의 방식에서 유일한 차이점은 `struct` 선언을 하느냐, `union` 선언을 하느냐이다.

- 하지만 각각의 변수가 **메모리 공간**에 `할당되는 방식`과 `접근의 결과`에는 **많은 차이가 있다.**

- 위의 구조체와 공용체를 대상으로 각각 다음 연산을 하면 16과 8이 출력된다.

    ```c
    printf("%d \n", sizeof(SBox));  // 16 출력
    printf("%d \n", sizeof(Ubox));  // 8 출력
    ```

    - `구조체`의 16은 **모든 멤버의 크기를 합한 결과**
  
    - `공용체`의 8은 **멤버 중에서 가장 크기가 큰 double의 크기**만 계산된 결과

<br>

> 구조체와 공용체의 특성 이해 예제

```c
  1 #include <stdio.h>
  2
  3 typedef struct sbox {  // 구조체 sbox의 정의
  4   int mem1;
  5   int mem2;
  6   double mem3;
  7 } SBox;
  8
  9 typedef union ubox {  // 공용체 ubox의 정의
 10   int mem1;
 11   int mem2;
 12   double mem3;
 13 } UBox;
 14
 15 int main(void) {
 16   SBox sbx;
 17   UBox ubx;
 18   printf("%p %p %p \n", &sbx.mem1, &sbx.mem2, &sbx.mem3);
 19   printf("%p %p %p \n", &ubx.mem1, &ubx.mem2, &ubx.mem3);
 20   printf("%d %d \n", sizeof(SBox), sizeof(UBox));
 21   return 0;
 22 }
```

```
0x7ffccb6f6ac0 0x7ffccb6f6ac4 0x7ffccb6f6ac8
0x7ffccb6f6ab8 0x7ffccb6f6ab8 0x7ffccb6f6ab8
16 8
```

- 실행결과에서 가장 주목할 부분은 **UBox형 변수를 구성하는 멤버** `mem1` , `mem2` , `mem3` 의 **주소 값이 동일하다**는 사실이다.

<br>

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54385241-7f5a9080-46d9-11e9-8e42-717530661d34.png'>
</p>

<br>

- `구조체 변수`가 선언되면, 구조체를 구성하는 멤버는 각각 할당이 된다.
  
- `공용체 변수`가 선언되면, 공용체를 구성하는 멤버는 각각 할당되지 않고, 그 중 **크기가 가장 큰 멤버의 변수만 하나 할당**되어 **이를 공유**하게 된다.

<br>

> 공용체의 메모리 공간 공유 증명 예제

```c
  1 #include <stdio.h>
  2
  3 typedef union ubox {  // 공용체 ubox의 정의
  4   int mem1;
  5   int mem2;
  6   double mem3;
  7 } UBox;
  8
  9 int main(void) {
 10   UBox ubx;   // 8바이트 메모리 할당
 11   ubx.mem1 = 20;  // 상위 4바이트의 메모리 공간에 20을 저장
 12   printf("%d \n", ubx.mem2);  // mem2는 int형 변수이므로 상위 4바이트의 메모리 공간을 참조 → 20
 13
 14   ubx.mem3 = 7.15;  // 저장된 값을 실수 7.15로 덮어써버린다.
 15   printf("%d \n", ubx.mem1);  // 15, 16행은 14행에서 실수를 저장하며 덮어써버렸기에,
 16   printf("%d \n", ubx.mem2);  // 상위 4바이트를 읽어서 출력하면 알 수 없는 값이 출력된다.
 17   printf("%g \n", ubx.mem3);
 18   return 0;
 19 }
```

```
20
-1717986918
-1717986918
7.15
```

- 실행결과는 `공용체의 멤버들`이 **메모리 공간을 공유**하고 있음을 확인시켜 준다.

<br>

### 공용체의 유용함은 다양한 접근방식을 제공하는데 있다

- 공용체의 유용함은 간단히 설명되지 않는다.
  
- 결과적으로는 **하나의 메모리 공간을 둘 이상의 방식으로 접근할 수 있다**는 것으로 정리가 되지만, 유용하게 사용이 되는 상황은 분야별로 약간씩 차이가 있다.

<br>

> 공용체의 유용성 이해를 위한 예제

```c
  1 #include <stdio.h>
  2
  3 typedef struct dbshort {  // 두 개의 unsigned short형 변수를 멤버로 지니는 4바이트 크기의 구조체 정의
  4   unsigned short upper;  // unsigned short는 2바이트
  5   unsigned short lower;
  6 } DBShort;
  7
  8 typedef union rdbuf {  // 정의된 공용체의 모든 멤버의 크기가 4바이트 → 공용채 변수 선언 시, 4바이트 할당
  9   int iBuf;
 10   char bBuf[4];
 11   DBShort sBuf;  // 공용체의 멤버로 구조체 변수를 선언
 12 } RDBuf;
 13
 14 int main(void) {
 15   RDBuf buf;
 16   printf("정수 입력: ");
 17   scanf("%d", &(buf.iBuf));
 18
 19   printf("상위 2바이트: %u \n", buf.sBuf.upper);  // 양의 정수 형태로 출력
 20   printf("하위 2바이트: %u \n", buf.sBuf.lower);
 21   printf("상위 1바이트 아스키 코드: %c \n", buf.bBuf[0]);  // 아스키 코드 문자로 출력
 22   printf("하위 1바이트 아스키 코드: %c \n", buf.bBuf[3]);
 23   return 0;
 24 }

```

```
정수 입력: 1145258561
상위 2바이트: 16961
하위 2바이트: 17475
상위 1바이트 아스키 코드: A
하위 1바이트 아스키 코드: D
```

- 아래와 같은 형태로 공용체 변수들이 할당되고 공유된다.

<br>

<p align='center'>
<img width= '500', src='https://user-images.githubusercontent.com/39554623/54387471-d747c600-46de-11e9-93ec-5a3320fb25c7.jpg'>
</p>

<br>

## 열거형(Enumerated Type)의 정의와 의미

- 열거형도 구조체나 공용체와 마찬가지로 자료형을 정의하는 방법으로 사용이 된다.

- 따라서 열거형 기반의 자료형 정의방법은 구조체 및 공용체와 유사
  
- 정의된 열거형 기반의 변수 선언 방법은 구조체 및 공용체와 완전히 동일

<br>

### 열거형의 정의와 변수의 선언

- 열거형으로 `syllable` 이라는 이름의 자료형을 정의한다는 것은 다음의 의미를 지닌다.

```
"syllable형 변수에 저장이 가능한 정수 값들을 결정하겠다."
```

- 앞서 보인 `구조체와 공용체`의 경우, (자료형의 선언을 통해) **멤버에 저장할 값의 유형을 결정**하였다.

- `열거형`의 경우, 저장이 가능한 값 자체를 정수의 형태로 결정한다.

```
"syllable형 변수에는 1, 2, 3, 4, 5, 6, 7이 저장 가능하다."
```

<br>

> 열거형의 정의

```c
enum syllable {    // syllable이라는 이름의 열거형 정의
    Do = 1, Re = 2, Mi = 3, Fa = 4, So = 5, La = 6, Ti = 7
};
```

- 정의 방식이 구조체와 차이가 있어 보이나 그 기본적인 구성은 동일

- struct 대신에 `enum`이 왔고, 구조체의 정의방식과 마찬가지로 `enum`에 이어 `자료형의 이름` `syllable`이 등장하였다. 그리고 syllable에 관련된 내용은 중괄호 내에 선언되었다.

<br>

> 열거형의 선언

```c
enum syllable tone;  // 열거형 syllable형 변수 tone의 선언
```

<br>

> typedef 선언을 추가한 열거형 정의 예제

```c
  1 #include <stdio.h>
  2
  3 typedef enum syllable {
  4   Do = 1, Re = 2, Mi = 3, Fa = 4, So = 5, La = 6, Ti = 7
  5 } Syllable;
  6
  7 void Sound(Syllable sy) {
  8   switch(sy) {
  9     case Do:
 10       puts("도는 하얀 도라지 "); return;  // puts는 자동으로 개행이 이루어짐
 11     case Re:
 12       puts("레는 둥근 레코드 "); return;
 13     case Mi:
 14       puts("미는 파란 미나리 "); return;
 15     case Fa:
 16       puts("파는 강한 파동권 "); return;
 17     case So:
 18       puts("솔은 작은 솔방울 "); return;
 19     case La:
 20       puts("라는 오뚜기 라면이고요 "); return;
 21     case Ti:
 22       puts("시는 졸졸 시냇물 "); return;
 23   }
 24   puts("다 함께 부르세~ 도레미파 솔라시도~");
 25 }
 26
 27 int main(void) {
 28   Syllable tone;
 29   for(tone = Do; tone <= Ti; tone += 1)
 30     Sound(tone);
 31   return 0;
 32 }
```

```
도는 하얀 도라지
레는 둥근 레코드
미는 파란 미나리
파는 강한 파동권
솔은 작은 솔방울
라는 오뚜기 라면이고요
시는 졸졸 시냇물
```

- 위 예제 29행의 반복문에서 Do와 Ti는 각각 1과 7을 의미하는 상수이니, 이 반복문을 다음과 같이 대신해도 결과는 같다.

    ```c
    for(tone = 1; tone <= 7; tone += 1)
    Sound(tone);    
    ```

- case 레이블에 사용된 상수 Do, Re, Mi ... 를 대신해서 1, 2, 3 ... 을 삽입해도 결과는 동일하다.
  
    - Do, Re, Mi ... 와 같은 **열거형 상수(enumeration constants)**들은 int형으로 표현되는 상수이기 때문이다.

<br>

### 열거형의 상수 값이 결정되는 방식

- 열거형을 정의하는데 있어서 상수의 값을 명시하지 않으면, **0에서부터 시작해서 1씩 증가하는 형태**로 결정이 된다.

```c
enum color {RED, BLUE, WHITE, BLACK};  // 이름만 선언되고 상수의 값은 선언 X

enum color {RED = 0, BLUE = 1, WHITE = 2, BLACK = 3};  // 0부터 시작해 1씩 증가된 값 할당
```

- 중간의 상수의 값이 선언되지 않으면 **앞서 선언된 상수보다 1이 증가된 값이 할당**된다.

```c
enum color {RED = 3, BLUE, WHITE = 6, BLACK};

enum color {RED = 3, BLUE = 4, WHITE = 6, BLACK = 7};
```

<br>

### 열거형의 유용함은 이름있는 상수의 정의를 통한 의미의 부여에 있다


> 열거형의 유용함은 둘 이상의 **연관이 있는 이름**을 상수로 선언함으로써 프로그램의 **가독성을 높이는데** 있다.

- 구조체와 공용체는 자료형의 정의에 의미가 있지만, 열거형은 일반적으로 연관 있는 이름을 상수로 선언하는데 의미가 있다.

- 다음과 같이 자료형의 이름을 생략한 형태로 열거형을 정의할 수 있다.

```c
enum {Do = 1, Re = 2, Mi = 3, Fa = 4, So = 5, La = 6, Ti = 7};
```