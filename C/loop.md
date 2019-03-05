# Chapter07. 반복실행을 명령하는 반복문

### while문에 의한 문장 반복

> C언어가 제공하는 반복문

- while문

- do~while문

- for문

<br>

> while문은 반복을 명령하는 문장이다.

while문은 특정조건을 주고 그 조건을 만족하는 동안, 특정영역을 계속해서 반복하는 구조이다.

```c
while(num<3)
{
  printf("Hello world! \n");
  num++;
}
```

반복문의 구성에 있어서 중요한 것 중 하나는 **반복의 조건을 무너뜨리기 위한 최소한의 연산** 이다.

<br>

> 무한루프의 구성

숫자 1은 `참`을 의미하는 대표적인 값이다. 따라서 이 값이 반복의 조건을 대신하면, 그 결과는 항상 `참`이 되어 빠져나가지 못하는 반복문이 구성된다.

```C
while(1)
{
  printf("%d X %d = %d \n", dan, num, dan*num);
  num++;
}
```

<br>

> while문의 중첩

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int i = 0, j = 0;
6
7   while(i<5)
8   {
9     while(j<i)
10     {
11       printf("o ");
12       j++;
13     }
14     j=0;
15     printf("* \n");
16     i++;
17   }
18
19   return 0;
20 }
```

```
*
o *
o o *
o o o *
o o o o *
```

<br>

### do~while문에 의한 문장의 반복

do~while문은 while문과 **반복의 조건을 검사하는 시점에 차이점** 이 있다.

do~while문은 while문과 달리 '반복조건'을 뒷부분에서 검사하기 때문에, **반복영역을 최소한 한번은 실행하는 구조** 이다.

```c
do
{
  printf("Hello world! \n");
  num++;
} while(num<3);

```

| do~while문 기반, 0이상 100이하의 정수 중에서 짝수의 합을 출력하는 프로그램

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int i = 0, sum = 0;
6
7   do
8   {
9     sum += i;
10     i += 2;
11   } while(i<=100);
12
13   printf("100 이하 짝수의 총합 : %d \n", sum);
14   return 0;
15 }
```

> do~while문의 중첩

| 구구단 2단부터 9단까지의 출력

```c
 1 #include <stdio.h>
 2
 3 int main(void)
 4 {
 5   int cur=2, is=0;
 6
 7   do
 8   {
 9     is=1;
10     do
11     {
12       printf("%dX%d=%d \n", cur, is, cur*is);
13       is++;
14     } while(is<10);
15     cur++;
16     printf("\n");
17   } while(cur<10);
18   return 0;
19 }
```

<br>

### for문에 의한 문장의 반복

> for문의 구조와 이해

```c
int main(void)
{
  int num=0;  // 필수요소 1. 반복을 위한 변수 선언
  while(num<3)  // 필수요소 2. 반복의 조건 검사
  {
    printf("HI~");
    num++;  // 필수요소 3. 반복의 조건을 '거짓'으로 만들기 위한 연산
  }
}
```

- 필수요소 1 : 초기식 - 반복을 위한 변수의 선언 및 초기화에 사용

- 필수요소 2 : 조건식 - 반복의 조건을 검사하는 목적으로 선언됨

- 필수요소 3 : 증감식 - 반복의 조건을 '거짓'으로 만드는 증가 및 감소연산


```c
for( 초기식 ; 조건식 ; 증감식)
{
      // 반복의 대상이 되는 문장들
}
```

**→ for문의 실행방식은 구조적으로 while문의 실행방식과 동일하다.**

반복의 횟수가 딱 정해진 경우라면 분명 for문이 최선이다. 하지만 반복의 횟수가 딱 정해지지 않은 경우엔 for문보다는 while문이 더 자연스럽니다.

예) 프로그램 사용자가 임의의 값을 입력하기만을 기다리는 상황

> for문의 중첩(앞의 while문 이용 예제와 비교해보자)

| 구구단 2단부터 9단까지의 출력

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int cur, is;
6
7   for(cur=2; cur<10; cur++)
8   {
9     for(is=1; is<10; is++)
10       printf("%d X %d = %d \n", cur, is, cur*is);
11    printf("\n");
12   }
13   return 0;
14 }
```
