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

---

> ※ `math.h` include 시 컴파일 오류

수학라이브러리를 사용하는 경우, 즉 `math.h`를 include했다면 컴파일할때 반드시 `-lm` 을 붙여줘야 한다.

```
june0122@ubuntu:~/thethelab.io/0313$ gcc 1.c        // 1.c 파일 컴파일 실패
/tmp/ccB2EDty.o: In function `main':
1.c:(.text+0xce): undefined reference to `sqrt'
collect2: error: ld returned 1 exit status
june0122@ubuntu:~/thethelab.io/0313$ gcc 1.c -lm        // gcc 컴파일 명령 끝에 -lm 추가
june0122@ubuntu:~/thethelab.io/0313$ ./a.out        // 문제없이 컴파일이 된다
point1 pos: 1 3
point2 pos: 4 5
두 점의 거리는 3.60555 입니다.
```

<br>

> 리눅스 매뉴얼 `man` 페이지

```
SQRT(3)                  Linux Programmer's Manual                  SQRT(3)

NAME
       sqrt, sqrtf, sqrtl - square root function

SYNOPSIS
       #include <math.h>

       double sqrt(double x);
       float sqrtf(float x);
       long double sqrtl(long double x);

       Link with -lm.
       
 . . . .
```

터미널에서 `man [함수이름]` 으로 함수 매뉴얼 페이지를 읽어올 수 있다. 그 내용중에 `SYNOPSIS`라는 섹션을 확인하면 간단히 요약된 내용을 확인할 수 있다.

- 맨윗줄 SQRT(3) 에서 3은 `세션 번호`입니다. 동명이인 찾듯이 C함수, 쉘 명령어, 프로그램 이름이 같을 경우 세션으로 구분해 준다.

- `NAME`은 이 페이지에서 설명하는 함수의 목록이다.

- `SYNOPSIS`는 간단 요약이다. 함수 문법과 불러와야할 헤더파일, 라이브러리 목록을 알려준다.

- `ERRORS` 는 함수가 실패하는 경우를 설명한다.

- `RETURN` 섹션은 함수 리턴값의 의미를 설명하는데 쓰인다.

---
