# Chapter19. 함수 포인터와 void 포인터

## 함수 포인터와 void 포인터

- 변수만 메모리 공간에 저장되는 것이 아니라, 함수들도 바이너리 형태로 메모리 공간에 저장되어서 호출 시 실행이 된다.

- 이렇게 메모리상에 저장된 함수의 주소 값을 저장하는 포인터 변수가 바로 **함수 포인터 변수** 이다.
  

### 함수 포인터의 이해

- 배열의 이름이 배열의 시작주소 값을 의미하듯, **함수의 이름도 함수가 저장된 메모리 공간의 주소 값을 의미**한다.

- 이러한 함수의 주소 값 저장을 위한 포인터 변수를 별도로 선언할 수 있으며, 이것을 **함수 포인터 변수**라 한다.

- **함수이름의 포인터 형은 반환형과 매개변수의 선언을 통해서 결정짓도록 약속되어 있다.**

<br>

> **함수 이름 SimpleFunc의 포인터 형**

```c
/* 반환형이 int이고 매개변수로 int형 변수가 하나 선언된 포인터 형(type) */

int SimpleFunc (int num) { . . . . }
```

<br>

> **함수 이름 ComplexFunc의 포인터 형**

```c
/* 반환형이 double이고 매개변수로 두 개의 double형 변수가 선언된 포인터 형 */

double ComplexFunc (double num1, double num2) { . . . . }
```

<br>

### 적절한 함수 포인터 변수의 선언

- int `(*fptr)` (int) : fptr은 포인터

- `int` (*fptr) (int) : 반환형이 int인 함수 포인터

- int (*fptr) `(int)` : 매개변수 선언이 int 하나인 함수 포인터

<br>

> 함수 포인터 변수 선언 예시

```c
int SoSimple (int num1, int num2) { . . . . }  // 함수 선언

int (*fptr) (int, int);  // 함수 포인터 변수 선언

fptr = SoSimple;  // 함수 포인터 변수에 함수 SoSimple의 주소 값을 저장
```

- 이렇듯 대입연산이 끝나고 나면 fptr과 SoSimple에는 동일한 값이 저장되어, 상수냐 변수냐가 이 둘의 유일한 차이점이 된다.

- 따라서 fptr을 이용해서도 다음과 같이 SoSimple 함수를 호출할 수 있다.

```c
fptr(3, 4);  // SoSimple(3, 4)와 동일한 결과를 보임
```

<br>

> 함수 포인터 예제 1

```c
  1 #include <stdio.h>
  2
  3 void SimpleAdder(int n1, int n2) {
  4   printf("%d + %d = %d \n", n1, n2, n1 + n2);
  5 }
  6
  7 void ShowString(char * str) {
  8   printf("%s \n", str);
  9 }
 10
 11 int main(void) {
 12   char * str = "Functuin Pointer";
 13   int num1 = 10, num2 = 20;
 14
 15   void (*fptr1)(int, int) = SimpleAdder;
 16   void (*fptr2)(char *) = ShowString;
 17
 18   /* 함수 포인터 변수에 의한 호출 */
 19   fptr1(num1, num2);
 20   fptr2(str);
 21   return 0;
 22 }
```

```
10 + 20 = 30
Functuin Pointer
```

<br>

> **매개 변수 선언으로 함수 포인터**가 오는 예제

- 전달되는 인자에 따라서 달리 동작하는 함수의 정의도 가능하다.

```c
  1 #include <stdio.h>
  2
  3 int WhoIsFirst(int age1, int age2, int (*cmp)(int n1, int n2)) {
  4   return cmp(age1, age2);
  5 }
  6
  7 int OlderFirst(int age1, int age2) {
  8   if(age1 > age2)
  9     return age1;
 10   else if(age1 < age2)
 11     return age2;
 12   else
 13     return 0;
 14 }
 15
 16 int YoungFirst(int age1, int age2) {
 17   if(age1 < age2)
 18     return age1;
 19   else if(age1 > age2)
 20     return age2;
 21   else
 22     return 0;
 23 }
 24
 25 int main(void) {
 26   int age1 = 20;
 27   int age2 = 30;
 28   int first;
 29
 30   printf("입장순서 1 \n");
 31   first = WhoIsFirst(age1, age2, OlderFirst);
 32   printf("%d세와 %d세 중 %d세가 먼저 입장! \n\n", age1, age2, first);
 33
 34   printf("입장순서 2 \n");
 35   first = WhoIsFirst(age1, age2, YoungFirst);
 36   printf("%d세와 %d세 중 %d세가 먼저 입장! \n\n", age1, age2, first);
 37   return 0;
 38 }
```

```
입장순서 1
20세와 30세 중 30세가 먼저 입장!

입장순서 2
20세와 30세 중 20세가 먼저 입장!

```

<br>

### '형(Type)'이 존재하지 않는 void 포인터

> void 포인터 변수

```c
void * ptr;
```

- void형 포인터 변수는 무엇이든 담을 수 있는 바구니에 비유할 수 있다. void형 포인터 변수에는 어떠한 변수의 주소 값이든 담을 수 있다. 함수의 주소 값까지도 담을 수 있다.

<br>

> void 포인터 예제

```c
  1 #include <stdio.h>
  2
  3 void SoSimpleFunc(void) {
  4   printf("I'm so simple");
  5 }
  6
  7 int main(void) {
  8   int num = 20;
  9   void * ptr;
 10
 11   ptr = &num;  // 변수 num의 주소 값 저장
 12   printf("%p \n", ptr);
 13
 14   ptr = SoSimpleFunc;  // 함수 SoSimpleFunc의 주소 값 저장
 15   printf("%p \n", ptr);
 16   return 0;
 17 }
```

```
0x7ffd45001ebc
0x5561269106aa
```

- 무엇이든 담을 수 있다고 좋은 점만 있는 것은 아니다.


- **void형 포인트 변수를 가지고는 아무런 포인터 연산도 하지 못한다.**

    - 값의 변경이나 참조도 불가능

    - void형 포인터 변수에는 가리키는 대상에 대한 어떠한 형(type) 정보도 담겨있지 않으므로 이는 당연한 것이다.

- void형 포인터는 ***일단 주소 값에만 의미를 두고, 포인터의 형은 나중에 결정한다.*

    - **메모리 동적 할당**과 매우 깊은 관계가 있다.

<br>

## main 함수로의 인자전달

- 지금까지 우리가 보아온 main 함수의 정의형태는 모두 다음과 같았다.

```c
int main(void) { . . . . }
```

- 그러나 main 함수는 다음과 같이 정의할 수도 있다.

```c
int main(int argc, char * argv[]) { . . . . }
```

<br>

### main 함수를 통한 인자의 전달

### 인자의 형성과정

### char * argv[]

- main 함수의 다음 매개변수 선언에 대해서 알아보자.

```c
char * argv[]
```

> 다음 두 선언은 같은 선언

```c
void SimpleFunc(TYPE * arr) { . . . . }
void SimpleFunc(TYPE arr[]) { . . . . }
```

- 즉, 이는 TYPE형 1차원 배열의 이름(주소 값)을 인자로 전달받을 수 있는 매개변수 선언이다.
  
- 그럼 `TYPE` 을 `char*` 로 바꿔보도록 하자.
  
  - 그럼 char형 포인터 변수로 이뤄진 1차원 배열의 이름을 인자로 전달받을 수 있는 매개변수의 선언이 된다.

```c
void SimpleFunc(char ***arr) { . . . . }
void SimpleFunc(char * arr[]) { . . . . }
```

- main 함수의 매개변수 `argv` 는 **char형 더블 포인터 변수**이고, 이는 char형 포인터 변수로 이뤄진 1차원 배열의 이름을 전달받을 수 있는 매개변수이다.

<br>

```c
  1 #include <stdio.h>
  2
  3 void ShowAllString(int argc, char * argv[]) {
  4   int i;
  5   for(i = 0; i < argc; i++)
  6     printf("%s \n", argv[i]);
  7 }
  8
  9 int main(void) {
 10   char * str[3] = {
 11     "C Programming",
 12     "C++ Programming",
 13     "JAVA Programming"
 14   };
 15   ShowAllString(3, str);
 16   return 0;
 17 }
```

```
C Programming
C++ Programming
JAVA Programming
```

- 3행 : argv는 char형 더블 포인터 변수이다. 따라서 argv에는 char** 형 데이터가(주소 값이) 전달될 수 있다.

- 12행 : 1차원 배열이름의 포인터 형은 배열이름이 가리키는 대상을 통해서 결정된다. 그런데 배열 이름 str이 가리키는 첫 번째 요소가 `char*` 형 데이터이므로, 배열이름 str의 형은 `char**` 이다. 그래서 3행에 정의된 함수의 두 번째 인자로 전달이 가능하다.

    - 함수 첫 번째 인자 `argc`는 문자열의 수
