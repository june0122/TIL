# Chapter08. 조건에 따른 흐름의 분기

### 조건적 실행과 흐름의 분기

> 조건 연산자 : 피 연산자가 세 개인 `삼 항 연산자`

- if~else문을 이부 대체할 수 있는 조건 연산자.

- 기호 `?`와 `:` 으로 이루어져 있다.

```c
int num3 = (num1>num2) ? (num1) : (num2);  // 소괄호 생략 가능
```

<br>

```c
(조건) ? data1 : data2
```
위의 조건이 '참'이면 연산결과로 data1이 반환되고, 조건이 '거짓'이면 연산결과로 data2가 반환된다.

| 삼 항 연산자를 이용한 절댓값 구하기

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int num, abs;
6   printf("정수 입력: ");
7   scanf("%d", &num);
8
9   abs = num>0 ? num : num*(-1);
10   printf("절댓값 : %d \n", abs);
11   return 0;
12 }
```

<br>

### 반복문의 생략과 탈출 : continue & break

> break : 이제 그만 빠져나가자

**break문이 실행되면 break문을 가장 가까이서 감싸고 있는 반복 하나를 빠져 나오게 된다.**

> continue : 나머지 생략하고 반복조건 확인하러 가자

**continue문이 실행되면 continue문의 이후는 생략을 하고, 다시 실행을 하게 된다.**

| 1이상 20미만의 정수를 출력하되, 2의 배수와 3의 배수를 출력에서 제외

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int num;
6   printf("start! ");
7
8   for(num=1; num<20; num++)
9   {
10     if(num%2==0 || num%3==0)
11       continue;
12       printf("%d ", num);
13   }
14   printf("end! \n");
15   return 0;
16 }
```

<br>

> break & continue

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/53800150-689c9700-3f7f-11e9-844c-952e471a17b9.png'>
</p>

> break & continue 활용

| 구구단을 출력하되 짝수 단만 출력하고, 2단은 2X2까지, 4단은 4X4, 6단은 6X6, 8단은 8X8까지만 출력되도록 break, continue문을 사용해서 문제를 풀어보자.

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int i, j;
6
7   for(i=2; i<10; i++)
8   {
9     if(i%2!=0)
10       continue;
11
12     for(j=1; j<10; j++)
13     {
14       if(j>i)
15         break;
16       printf("%d X %d = %d \n", i, j, i*j);
17     }
18     printf("\n");
19   }
20   return 0;
21 }
```

| 다음 식을 만족하는 모든 A와 Z를 구하는 프로그램을 작성

```
  A Z
+ Z A
------
  9 9
```

```c
// 내 코드

1 #include <stdio.h>
2
3 int main(void)
4 {
5   int A, Z = 0;
6
7   for(A=0; A<10; A++)
8   {
9     for(Z=0; Z<10; Z++)
10     {
11     if(((10*A) + Z) + ((Z*10) + A) == 99)
12       printf("%d%d + %d%d = 99 \n", A, Z, Z, A);
13     }
14   }
15   return 0;
16 }
```

```c
// 해설지 코드

1 #include <stdio.h>
2
3 int main(void)
4 {
5   int A, Z;
6   int result;
7
8   for(A=0; A<10; A++)
9   {
10     for(Z=0; Z<10; Z++)
11     {
12       if(A==Z)
13         continue;
14       result = (A*10+Z)+(Z*10+A);
15       if(result == 99)
16         printf("%d%d + %d%d = %d \n", A, Z, Z, A, result);
17     }
18   }
19   return 0;
20 }
```

<br>

### switch문에 의한 선택적 실행과 goto문

> switch문의 구성과 기본기능

switch문은 if..else if..else와 유사한 측면이 있으나 사용할 수 있는 영역은 if..else if..else에 비해 제한적이다.

```c
int main(void)
{
  . . . .
  switch(n)
  {
  case 1:
    printf("A1");
    printf("A2");
    break;

  case 2:
    printf("B1");
    printf("B2");
    break;

  default;
    printf("default");
  }
  . . . .
}
```

위 코드에서 보이는 switch(n)에서의 `n`은 switch문으로 전달되는 인자의 정보이다. 이 n은 정수형 변수이어야 하는데( *이 정수형 변수에는 char형도 포함된다* ), 대표적으로 int형 변수가 위치하게 된다. 그리고 이 n에 저장된 값에 따라서 실행할 영역이 결정된다.

`break문`은 실행영역을 묶어주는 역할을 한다.

<br>

> `switch` vs `if..else if..else`

- 분기의 경우 수가 많아지면 가급적 switch문으로 구현

- switch문으로 구현할 수 있는 조건의 구성에는 한계가 있다. 따라서 switch문으로 표현하기 애매한 상황에서는 if..else if..else를 사용해야 한다.

<br>

> `goto`문

C언어와 같은 절차지향 프로그래밍 언어에서 프로그램의 흐름을 방해하거나 복잡하게 하는 것은 아주 큰 단점이 된다. 그만큼 단순한 흐름이 중요하기 때문이다. goto문을 써서 개선 시킬 수 있는 상황이 아예 없는 것은 아니지만, 극히 제한적이며 설득력이 강한 것도 아니다. 그래서 goto문의 사용은 가급적 자제하거나 아예 사용하지 말자고 사람들 사이에서 결론이 났다.

```c
int main(void)
{
  . . . .
rabbit:       // 위치지정에 사용된 rabbit이라는 이름의 레이블
  . . . .
  goto rabbit;    // 레이블 rabbit의 위치로 이동
}
```
