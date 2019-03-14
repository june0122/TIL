# Chapter22. 구조체와 사용자 정의 자료형1

### 구조체란 무엇인가?

> 구조체의 정의

**구조체** 라는 것은 하나 이상의 변수(포인터 변수와 배열 포함)를 묶어서 새로운 자료형을 정의하는 도구이다. 즉, 구조체를 기반으로 우리는 새로운 자료형을 정의할 수 있다.

프로그램 상에서 마우스의 좌표정보를 저장하고 관리해야한다고 가정하면, 마우스의 위치라는 **하나의 정보**를 표현하기 위해 `int xpos;` (마우스의 x 좌표)와 `int ypos` (마우스의 y 좌표)와 같은 두 개의 변수를 선언해야 한다. 데이터의 표현과 관리가 용이해지도록 이러한 변수들을 하나로 묶어두기 위해 등장한 것이 구조체이다.

```c
struct point  // point라는 이름의 구조체 정의
{
    int xpos;  // point 구조체를 구성하는 멤버 xpos
    int ypos;  // point 구조체를 구성하는 멤버 ypos 
};
```

위의 정의는 point라는 이름의 구조체를 정의한 결과이다. 이 때 **point라는 이름이 int나 double과 같은 자료형의 이름이 되는 것**이다. 다음은 또 하나의 구조체 정의의 예시이다.

```c
struct person  // person이라는 이름의 구조체
{
    char name[20];  // 이름 저장을 위한 멤버
    char phoneNum[20];  // 전화번호 저장을 위한 멤버
    int age;  // 나이 저장을 위한 멤버
};
```

위의 구조체 정의에서 보이듯이 배열도 값의 저장이 가능한, 사실상 변수의 성격을 띠기 때문에 구조체의 멤버가 될 수 있다.

<br>

> 구조체 변수의 선언과 접근

```c
struct type_name val_name ;  // 구조체 변수선언의 기본 형태
```

구조체의 변수를 선언할 때에는 맨 앞에 `struct` 선언을 추가해야 하며, 이어서 `구조체의 이름`과 `구조체 변수의 이름`을 선언해야 한다.

```c
struct point pos;  // 앞서 정의한 point 구조체의 변수 pos 선언
struct person man;  // 앞서 정의한 person 구조체의 변수 man 선언
```

<br>

*구조체 변수 안에 존재하는 멤버에 접근할 때의 기본형식* 은 다음과 같다.

<br>

`구조체 변수의 이름` . `구조체 멤버의 이름`

<br>

예를 들어 구조체 변수 pos의 멤버 xpos에 20을 저장하고, 구조체 변수 man의 멤버 name에 저장된 내용을 출력하려면 다음과 같이 문장을 구성해야 한다.

```c
pos.xpos=20;  // 구조체 변수 pos의 멤버 xpos에 20을 저장

printf("%s \n", man.name);  // 구조체 변수 man의 멤버 name에 저장된 내용을 출력
```

<br>

| 두 점 사이의 거리

```c
  1 #include <stdio.h>
  2 #include <math.h>
  3
  4 struct point  // 구조체 point의 정의
  5 {
  6   int xpos;
  7   int ypos;
  8 };
  9
 10 int main(void)
 11 {
 12   struct point pos1, pos2;
 13   double distance;
 14
 15   fputs("point1 pos: ", stdout);
 16   scanf("%d %d", &pos1.xpos, &pos1.ypos);
 17
 18   fputs("point2 pos: ", stdout);
 19   scanf("%d %d", &pos2.xpos, &pos2.ypos);
 20
 21   /* 두 점간의 거리 계산 공식 */
 22   distance = sqrt((double)((pos1.xpos - pos2.xpos) * (pos1.xpos - pos2.xp    os) + (pos1.ypos - pos2.ypos) * (pos1.ypos - pos2.ypos)));
 23
 24   printf("두 점의 거리는 %g 입니다. \n", distance);
 25   return 0;
 26 }
```

```
point1 pos: 1 3
point2 pos: 4 5
두 점의 거리는 3.60555 입니다.
```

<br>

**math.h** 를 include 시 컴파일 오류

- [GCC 컴파일러에서의 라이브러리 링킹](https://github.com/june0122/TIL/blob/master/C/library.md)
- [헤더 파일](https://github.com/june0122/TIL/blob/master/C/header.md)

<br>


| person 구조체 기반의 예제

구조체의 멤버로 배열이 선언되면 배열의 접근방식을 취하면 되고, 구조체의 멤버로 포인터 변수가 선언되면 포인터 변수의 접근방식을 취하면 된다.

```c
  1 #include <stdio.h>
  2 #include <string.h>
  3
  4 struct person {
  5   char name[20];
  6   char phoneNum[20];
  7   int age;
  8 };
  9
 10 int main(void) {
 11   struct person man1, man2;
 12   strcpy(man1.name, "안성준");  // 배열에 문자열 저장을 위해선 strcpy 함수 이용
 13   strcpy(man1.phoneNum, "010-1122-3344");
 14   man1.age = 23;
 15
 16   printf("이름 입력: "); scanf("%s", man2.name);
 17   printf("번호 입력: "); scanf("%s", man2.phoneNum);
 18   printf("나이 입력: "); scanf("%d", &(man2.age));  // 배열의 이름 앞에는 & 연산자 안붙임
 19
 20   printf("이름: %s \n", man1.name);
 21   printf("번호: %s \n", man1.phoneNum);
 22   printf("나이: %d \n", man1.age);
 23
 24   printf("이름: %s \n", man2.name);
 25   printf("번호: %s \n", man2.phoneNum);
 26   printf("나이: %d \n", man2.age);
 27   return 0;
 28 }
```

<br>

자주 쓰이는 방법은 아니지만, 다음과 같이 구조체를 정의함과 동시에 변수를 선언할 수도 있다. 다음의 코드들은 결과적으로 동일하다.

```c
struct point  // 구조체의 정의와 변수의 선언
{
    int xpos;
    int ypos;
} pos1, pos2, pos3;
```

```c
struct point  // 구조체의 정의
{
    int xpos;
    int ypos;
};

struct point pos1, pos2, pos3;  // 구조체 변수의 선언
```

<br>

> 구조체 변수의 초기화

int형 변수를 선언과 동시에 초기화할 수 있듯이 구조체 변수도 선언과 동시에 초기화 할 수 있다. 그리고 초기화 방법은 배열의 초기화와 유사하다(동일하다).

```c
  1 #include <stdio.h>
  2
  3 struct point {
  4   int xpos;
  5   int ypos;
  6 };
  7
  8 struct person {
  9   char name[20];
 10   char phoneNum[20];
 11   int age;
 12 };
 13
 14 int main(void) {
 15   struct point pos = {10, 20};
 16   struct person man = {"이승기", "010-1321-1231", 35};
 17   printf("%d %d \n", pos.xpos, pos.ypos);
 18   printf("%s %s %d \n", man.name, man.phoneNum, man.age);
 19   return 0;
 20 }
```

**초기화 과정에서는** 문자열 저장을 위해서 **strcpy 함수를 호출하지 않고**, 멤버에 저장할 **데이터를 단순히 나열만하면 된다**. 그것이 문자열이라 하더라도 말이다.

<br>

### 구조체 배열 그리고 포인터

> 구조체 배열의 선언과 접근

```c
struct Point2D p[3];
```

위와 같이 구조체를 선언하면, 다음의 구조로 배열이 할당된다.

<br>

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54340870-b26e3700-467b-11e9-8901-4a63c76c73fc.png'>
</p>

<br>

> 구조체 배열의 초기화

구조체 변수를 선언과 동시에 초기화할 때에는 다음과 같이 배열의 길이만큼 중괄호를 이용해서 초기화를 진행했다.

```c
struct person man = {"이승기", "010-1232-1232", 29};
```

따라서 구조체 배열을 선언과 동시에 초기화할 때에는 다음과 같이 배열의 길이만큼 중괄호를 이용해서 초기화를 진행하면 된다.

```c
struct person arr[3] = {
    {"이승기", "010-1232-1232", 29};    // 첫 번째 요소의 초기화
    {"정지영", "010-3112-6432", 59};    // 두 번째 요소의 초기화
    {"한지수", "010-5162-2132", 59};    // 세 번째 요소의 초기화
};
```

<br>

> 구조체 변수와 포인터

구조체 포인터 변수의 선언 및 연산의 방법은 일반적인 포이터 변수의 선언 및 연산의 방법과 다르지 않다.

- int형 포인터 변수의 선언과 초기화
    
    ```c
    int num = 10;
    int * iptr = &num;
    ```

- point형 포인터 변수의 선언과 초기화
    
    ```c
    struct point pos = {11, 12};  // xpos, ypos를 각각 11, 12로 초기화
    struct point * pptr = &pos;  // 포인터 변수 pptr이 구조체 변수 pos를 가리킴
    ```

- int형 포인터 변수 iptr을 이용하여 변수 num에 접근

    ```c
    *iptr = 20;
    ```

- point형 포인터 변수 pptr을 이용해서 다음과 같이 구조체 변수 pos에 접근

    ```c
    (*pptr).xpos = 10;  // pptr이 가리키는 구조체 변수의 멤버 xpos에 10 저장
    (*pptr).ypos = 20;  // pptr이 가리키는 구조체 변수의 멤버 ypos에 20 저장
    ```

    - 접근을 위해 포인터 변수를 대상으로 * 연산을 하는 것은 동일하다. 다만 구조체 포인터 변수의 경우 접근하고자 하는 멤버의 선택을 위해서 `.` 연산을 추가했을 뿐이다. 위의 두 문장은 각각 다음과 같이도 쓸 수 있다.

        ```c
        pptr->xpos = 10;  // pptr이 가리키는 구조체 변수의 멤버 xpos에 10 저장
        pptr->ypos = 20;  // pptr이 가리키는 구조체 변수의 멤버 ypos에 20 저장
        ```

        즉 `*` 연산과 `.` 연산을 하나의 `->` 연산으로 대신할 수 있다. 많은 프로그래머들이 편의상 -> 연산자의 사용을 즐기는 편이다.

<br>

```c
  1 #include <stdio.h>
  2
  3 struct point {
  4   int xpos;
  5   int ypos;
  6 };
  7
  8 int main(void) {
  9   struct point pos1 = {1, 2};
 10   struct point pos2 = {100, 200};
 11   struct point * pptr = &pos1;
 12
 13   (*pptr).xpos += 4;
 14   (*pptr).ypos += 5;
 15   printf("[%d, %d] \n", pptr->xpos, pptr->ypos);
 16
 17   pptr=&pos2;
 18   pptr->xpos += 1;
 19   pptr->ypos += 2;
 20   printf("[%d, %d] \n", (*pptr).xpos, (*pptr).ypos);
 21   return 0;
 22 }
```

```
[5, 7]
[101, 202]
```

<br>

> 포인터 변수를 구조체의 멤버로 선언하기

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54353285-f884c400-4696-11e9-9e34-b3f5d5df879b.png'>
</p>

```c
  1 #include <stdio.h>
  2
  3 struct point {
  4   int xpos;
  5   int ypos;
  6 };
  7
  8 struct circle {
  9   double radius;
 10   struct point * center;
 11 };
 12
 13 int main(void) {
 14   struct point cen = {2, 7};
 15   double rad = 5.5;
 16
 17   struct circle ring = {rad, &cen};
 18   printf("원의 반지름: %g \n", ring.radius);
 19   printf("원의 중심 [%d, %d] \n", (ring.center)->xpos, (ring.center)->ypos);
 20   return 0;
 21 }
```

```
원의 반지름: 5.5
원의 중심 [2, 7]
```

<br>

- **TYPE형 구조체 변수의 멤버로 TYPE형 포인터 변수를 둘 수 있다.**

    즉 다음과 같은 선언이 가능하다는 것이다.

```c
struct point {
    int xpos;
    int ypos;
    struct point * ptr;  // 구조체 point의 변수 선언
}
```

위 개념을 통해 삼각형을 이루는 세 점의 연결관계도 표현이 가능하다.

```c
  1 #include <stdio.h>
  2
  3 struct point {
  4   int xpos;
  5   int ypos;
  6   struct point * ptr;
  7 };
  8
  9 int main(void) {
 10   struct point pos1 = {1, 1};
 11   struct point pos2 = {2, 2};
 12   struct point pos3 = (3, 3);
 13
 14   pos1.ptr = &pos2;  // pos1과 pos2를 연결
 15   pos2.ptr = &pos3;  // pos2와 pos3를 연결
 16   pos3.ptr = &pos1;  // pos3를 pos1과 연결
 17
 18   printf("점의 연결관계... \n");
 19   printf("[%d, %d]와(과) [%d, %d] 연결 \n", pos1.xpos, pos1.ypos, pos1.ptr->xpos, pos1.ptr->ypos);
 20   printf("[%d, %d]와(과) [%d, %d] 연결 \n", pos2.xpos, pos2.ypos, pos2.ptr->xpos, pos2.ptr->ypos);
 21   printf("[%d, %d]와(과) [%d, %d] 연결 \n", pos3.xpos, pos3.ypos, pos3.ptr->xpos, pos3.ptr->ypos);
 22   return 0;
 23 }
```

```
점의 연결관계...
[1, 1]와(과) [2, 2] 연결
[2, 2]와(과) [3, 3] 연결
[3, 3]와(과) [1, 1] 연결
```

<br>

> 구조체 변수의 주소 값과 첫 번째 멤버의 주소 값

- **구조체 변수의 주소 값은 구조체 변수의 첫 번째 멤버의 주소 값과 동일하다.**

```c
  1 #include <stdio.h>
  2
  3 struct point {
  4   int xpos;
  5   int ypos;
  6 };
  7
  8 struct person {
  9   char name[20];
 10   char phoneNum[20];
 11   int age;
 12 };
 13
 14 int main(void) {
 15   struct point pos = {10, 20};
 16   struct person man = {"이승기", "010-1321-1231", 35};
 17   printf("%p %p \n", &pos, &pos.xpos);  // int 변수의 이름 앞에는 & 연산자가 붙는다.
 18   printf("%p %p \n", &man, man.name);  // 배열의 이름 앞에는 & 연산자가 붙지 않는다.
 19   return 0;
 20 }
```

```
0x7ffe5cc42a88 0x7ffe5cc42a88
0x7ffe5cc42a90 0x7ffe5cc42a90
```
