# Chapter09. C언어의 핵심, 함수

### 함수를 정의하고 선언하기

<br>

| 함수 정의의 전체적인 구조

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/53806704-064c9200-3f91-11e9-8d36-e7bf4d3143aa.jpg'>
</p>

> 함수의 입력과 출력 : printf 함수도 반환을 한다.

앞선 내용에선 입력(전달인자)와 그에 따른 적절한 출력(반환 값)이 존재하는 것이 함수라 하였지만, C언어의 함수는 **전달인자가 없거나 반환 값이 없는 경우도 있다.**

→ `입력 또는 출력이 없는 함수도 존재한다(만들 수 있다).`

<br>

printf 함수는 실제로 값을 반환하지 않을까? 아니다. printf 함수도 실제로 값을 반환한다.

```c
int main(void)
{
  int num1, num2;
  num1=printf("12345\n");
  num2=printf("I love my home\n");
  printf("%d %d \n", num1, num2);
  return 0;
}
```

```
12345
I love my home
6 15
```

실행결과에서 확인할 수 있듯이, `printf 함수`는 `\n` 문자를 포함하여 **모니터에 출력한 문자열의 길이를 반환** 한다.

<br>

> 전달인자의 유무와 반환 값의 유무에 따라서 함수를 네 개의 형태로 나누고, 각 유형별 함수를 하나씩 정의해 보자.

- 유형 1: 전달인자 (○), 반환 값 (○)

- 유형 2: 전달인자 (○), 반환 값 (Ⅹ)

- 유형 3: 전달인자 (Ⅹ), 반환 값 (○)

- 유형 4: 전달인자 (Ⅹ), 반환 값 (Ⅹ)

<br>

> `다양한 형태의 함수 정의 1` : 전달인자와 반환 값이 모두 있는 경우

가장 일반적인 함수의 형태로, 예시로 덧셈 기능을 지니는 함수를 보도록 한다.

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/53855462-16a45180-4011-11e9-846c-f1a5b9857352.jpg'>
</p>

```c
1 #include <stdio.h>
2
3 int Add(int num1, int num2)
4 {
5   return num1 + num2;
6 }
7
8 int main(void)
9 {
10   int result;
11   result = Add(3, 4);
12   printf("덧셈결과1: %d \n", result);
13   result = Add(5, 8);
14   printf("덧셈결과2: %d \n", result);
15   return 0;
16 }
```

함수호출 시 전달할 수 있는 인자의 수는 여러 개가 될 수 있지만 **반환할 수 있는 값의 수는 하나** 이다.

값이 반환된다는 것은 **함수의 호출문이 반환 값으로 대체되는 것으로 이해할 수 있다.**

<br>

> `다양한 형태의 함수 정의 2` : 전달인자나 반환 값이 존재하지 않는 경우

```c
void ShowAddResult(int num) // 인자전달 O, 반환 값 X
{
  printf("덧셈결과 출력: %d \n", num);
}
```

위의 함수 정의에서 반환형이 void로 선언되었다. **여기서 사용된 void에는 '반환하지 않는다'라는 뜻이 담겨있다.** 실제로 함수의 몸체부분을 보면 return문이 없음을 알 수 있다.

```c
int ReadNum(void)
{
  int num;
  scanf("%d", &num);
  return num;
}
```

위의 함수에선 매개변수의 선언 위치에 void 선언이 등장했다. **여기서 사용된 void는 '인자를 전달하지 않는다'라는 뜻이 담겨있다.** 따라서 함수를 호출할 때 실제로 인자를 전달하면 안된다.

```c
void HowToUseThisProg(void)
{
  printf("두 개의 정수를 입력하시면 덧셈결과가 출력됩니다. \n");
  printf("자! 그럼 두 개의 정수를 입력하세요 \n");
}
```

위의 함수는 단순히 메시지를 전달하는 함수이기 때문에 인자의 전달도 값의 반환도 불필요하다.

<br>

```c
1 #include <stdio.h>
 2
 3 int Add(int num1, int num2)  // 전달인자 (○), 반환 값 (○)
 4 {
 5   return num1+num2;
 6 }
 7
 8 void ShowAddResult(int num)  // 전달인자 (○), 반환 값 (Ⅹ)
 9 {
10   printf("덧셈결과 출력: %d \n", num);
11 }
12
13 int ReadNum(void)  // 전달인자 (Ⅹ), 반환 값 (○)
14 {
15   int num;
16   scanf("%d", &num);
17   return num;
18 }
19
20 void HowToUseThisProg(void)  // 전달인자 (Ⅹ), 반환 값 (Ⅹ)
21 {
22   printf("두 개의 정수를 입력하면 덧셈결과가 출력됩니다. \n");
23   printf("자! 그럼 두 개의 정수를 입력하세요. \n");
24 }
25
26 int main(void)
27 {
28   int result, num1, num2;
29   HowToUseThisProg();
30   num1=ReadNum();
31   num2=ReadNum();
32   result = Add(num1, num2);
33   ShowAddResult(result);
34   return 0;
35 }
```

<br>

> return이 지니는 두 가지 의미 중 한가지 의미만 살리기

- 함수를 빠져나간다.

- 값을 반환한다.

간혹 반환형이 void로 선언된 함수에서는 return문을 사용할 수 없는 것으로 아는 경우가 있는데, 반환형이 void인 함수에서도 다음의 형태로 return문을 삽입할 수 있다.

```c
void NoReturnType(int num)
{
  int(num<0)
      return;  // 값을 반환하지 않는 return문!
  . . . .
}
```

위의 return문에는 반환할 값이 명시되어 있지 않은데, 이는 return의 두 가지 의미 중 다음 한 가지만 담아서 선언한 것이다.

**값의 반환 없이 그냥 함수를 빠져나간다!**

<br>

> 함수의 정의와 그에 따른 원형의 선언

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/53855904-fffefa00-4012-11e9-88fe-0450bf7fdf6f.jpg'>
</p>

위의 두 가지 형태의 프로그램 구성 중, 왼쪽은 Add 함수의 정의가 Add 함수의 호출문보다 먼저 등장하고 있다.

하지만 오른쪽은 Add 함수의 정의에 앞서 Add 함수의 호출문이 먼저 등장하여 컴파일 에러를 일으킨다.

**그러므로 함수는 호출되기 전에 미리 정의되어야 한다.** C언어는 절차 지향 언어이므로 함수의 정의가 main 함수 뒤에 오면 컴파일러가 인식을 하지 못한다.(컴파일 에러를 일으킨다.)


**하지만 `함수의 선언`을 해주면 함수의 정의가 main 함수의 뒤에 올 수 있다.**

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/53855558-7864bb80-4011-11e9-900b-8e9abdbbf966.jpg'>
</p>

`함수의 시그니쳐(function signature)` : 함수 시그니처란 **함수의 원형에 명시되는 반환 타입과 매개변수 리스트** 를 가리킨다. 만약 두 함수가 반환 타입과 매개변수의 개수와 그 타입이 모두 같다면, 이 두 함수의 시그니처는 같다고 할 수 있다.

※ 함수의 선언에는 매개변수의 갯수 및 자료형 정보만 포함되면 되기 때문에 다음과 같이 매개변수의 이름을 생략해서 선언하는 것이 가능하다.

```c
int Add(int a, int b);  // 매개변수의 이름을 포함한 선언

int Add(int, int);  // 매개변수의 이름을 생략한 선언
```

<br>

> 다양한 종류의 함수 정의하기

```c
1 #include <stdio.h>
2 int AbsoCompare(int, int);   // 매개변수의 이름 생략
3 int GetAbsoValue(int);  // 매개변수의 이름 생략
4
5 int main(void)
6 {
7   int num1, num2;
8   printf("두 개의 정수 입력: ");
9   scanf("%d %d", &num1, &num2);
10   printf("%d와 %d중 절댓값이 큰 정수: %d \n", num1, num2, AbsoCompare(num1, num2));
11   return 0;
12 }
13
14 int AbsoCompare(int num1, int num2)
15 {
16   if(GetAbsoValue(num1) > GetAbsoValue(num2))
17     return num1;
18   else
19     return num2;
20 }
21
22 int GetAbsoValue(int num)
23 {
24   if(num<0)
25     return num*(-1);
26   else
27     return num;
28 }
```

<br>

| 세 개의 정수를 받아서 가장 큰 정수를 반환하는 함수와 가장 작은 수를 반환하는 함수를 정의하고, 이 함수들을 호출하는 main 함수도 작성하자.

```c
// 내 코드

1 #include <stdio.h>
2
3 int ReturnMaxNum(int num1, int num2, int num3)
4 {
5   if(num1>num2)
6     return (num1>num3) ? num1 : num3;
7   else
8     return (num2>num3) ? num2 : num3;
9 }
10
11 int ReturnMinNum(int num1, int num2, int num3)
12 {
13   if(num1<num2)
14     return (num1<num3) ? num1 : num3;
15   else
16     return (num2<num3) ? num2 : num3;
17 }
18
19 int main(void)
20 {
21   int num1, num2, num3;
22
23   printf("세 개의 정수를 입력하시오 : ");
24   scanf("%d %d %d", &num1, &num2, &num3);
25
26   printf("가장 큰 수 : %d \n", ReturnMaxNum(num1, num2, num3));
27   printf("가장 작은 수 : %d \n", ReturnMinNum(num1, num2, num3));
28   return 0;
29 int ReturnMinNum(int num1, int num2, int num3)
30 {
31   if(num1<num2 && num1<num3)
32     return num1;
33   else if(num2<num1 && num2<num3)
34     return num2;
35   else
36     return num3;
37 }
```

```c
// 해설지 코드 → 조건 연산자 이용

1 #include <stdio.h>
2
3 int ReturnMaxNum(int num1, int num2, int num3)
4 {
5   if(num1>num2)
6     return (n1>n3) ? n1 : n3;
7   else
8     return (n2>n3) ? n2 : n3;
9 }
10
11 int ReturnMinNum(int num1, int num2, int num3)
12 {
13   if(num1<num2)
14     return (num1<num3) ? num1 : num3;
15   else
16     return (num2<num3) ? num2 : num3;
17 }
18
19 int main(void)
20 {
21   int num1, num2, num3;
22
23   printf("세 개의 정수를 입력하시오 : ");
24   scanf("%d %d %d", &num1, &num2, &num3);
25
26   printf("가장 큰 수 : %d \n", ReturnMaxNum(num1, num2, num3));
27   printf("가장 작은 수 : %d \n", ReturnMinNum(num1, num2, num3));
28   return 0;
29 }
30
```

<br>

| 섭씨, 화씨 변환 프로그램을 만들어보자. (Fah=1.8*Cel+32)

```c
1 #include <stdio.h>
2
3 double CelToFah(double c)
4 {
5   return 1.8*c+32;
6 }
7
8 double FahToCel(double f)
9 {
10   return (f-32)/1.8;
11 }
12
13 int main(void)
14 {
15   int sel;
16   double num;
17
18   printf("1. 섭씨를 화씨로 | 2. 화씨를 섭씨로 \n");
19   printf("선택 >> ");
20   scanf("%d", &sel);
21
22   if(sel==1)
23   {
24     printf("섭씨 입력: ");
25     scanf("%lf", &num);
26     printf("화씨로의 변환값 : %lf \n", CelToFah(num));
27   }
28   else if(sel==2)
29   {
30     printf("화씨 입력: ");
31     scanf("%lf", &num);
32     printf("섭씨로의 변환값 : %lf \n", FahToCel(num));
33   }
34   else
35     printf("입력 오류 \n");
36
37   return 0;
38 }
```

<br>

| 인자로 전달한 수만큼 피보나치 수열을 출력하는 함수를 정의해보자.

```c
1 #include <stdio.h>
2
3 void ShowFiboSeries(int num)
4 {
5   int f1=0, f2=1, f3, i;
6
7   if(num==1)
8     printf("%d ", f1);
9   else
10     printf("%d %d ", f1, f2);
11
12   for(i=0; i<num-2; i++)
13   {
14     f3=f1+f2;
15     printf("%d ", f3);
16     f1=f2;
17     f2=f3;
18   }
19 }
20
21 int main(void)
22 {
23   int n;
24
25   printf("출력하고자 하는 피보나치 수열의 갯수: ");
26   scanf("%d", &n);
27   if(n<1)
28   {
29     printf("1 이상의 값을 입력하시오. \n")    ;
30     return -1;
31   }
32   ShowFiboSeries(n);
33   return 0;
34 }
```

<br>

### 변수의 존재기간과 접근범위 1 : 지역변수

변수는 선언되는 위치에 따라 크게 `전역변수`와 `지역변수`로 나뉜다. 그리고 이 둘은 다음 두 가지에 대해서 차이점을 보인다.

- 메모리상에 존재하는 기간

- 변수에 접근할 수 있는 범위

<br>

> 함수 내에서만 존재 및 접근 가능한 지역변수(Local Variable)

'지역변수'에서 말하는 '지역'이란 중괄호에 의해 형성되는 영역을 뜻한다. **중괄호 내에 선언되는 변수는 모두 지역변수이다.**

```c
#include <stdio.h>

int SimplerFuncOne(void)
{
  int num=10;  // SimpleFuncOne의 지역변수 num
  num++;
  printf("SimpleFuncOne num: %d \n", num);
  return 0;  // SimpleFuncOne의 num이 유효한 마지막 문장
}

int simpleFunTwo(void)
{
  int num1=20;  // 이후부터 num1 유효
  int num2=30;  // 이후부터 num2 유효
  num1++, num2--;
  printf("num1 & num2: %d %d \n", num1, num2);
  return 0;  // num1, num2 유효한 마지막 문장
}

int main(void)
{
  int num=17;  // 이후부터 main의 num 유효
  SimpleFuncOne();
  simpleFunTwo();
  printf("main num: %d \n", num);
  return 0;  // main의 num이 유효한 마지막 문장
}

```

**지역변수는 해당지역을 벗어나면 자동으로 소멸** 해당 함수가 호출될 때마다 지역변수는 그때마다 새롭게 메모리 공간에 할당된다.

**지역변수는 선언된 지역 내에서만 유효하기 때문에 선언된 지역이 다르면 이름이 같아도 문제가 되지 않는다.** 때문에 위 예제의 main 함수 내에도, 그리고 SimpleFuncOne 함수 내에도 동일한 이름의 변수 `num`이 선언될 수 있는 것이다.

<br>

> 지역변수의 할당과 소멸

*프로그램의 실행에 따른 지역변수의 생성 및 소멸의 과정을 이해하는 것은 지역변수 자체를 이해하는 것이기 때문에 매우 중요하다.*

- 지역변수는 '스택(stack)' 메모리 영역에 할당된다.

- 지역변수는 접시에 쌓듯이 할당된다.

**지역변수는 해당 선언문이 실행될 때 메모리 공간에 할당되었다가, 선언문이 존재하는 함수가 반환을 하면(종료를 하면) 메모리 공간에서 소멸된다.**

<br>

> 다양한 형태의 지역변수

중괄호는 함수의 정의만이 아니라 반복문이나 조건문에도 존재할 수 있다. 즉, **지역변수는 반복문이나 조건문에도 선언이 가능하다.**

```c
      중괄호 진입
    ┌─────┐
    │     ↓
    │  for(cnt=0; cnt<3; cnt++)
    │  {
    │    int num=0;
    │    num++;
    │    . . . .
    │  }  │
    │     │
    ┖─────┘ 중괄호 탈출
```

위는 for문에 의한 반복이 중괄호 내에서 이뤄지는 것이 아니라, **중괄호 진입과 탈출을 반복하면서 이뤄지는 것** 임을 말하고 있다. 따라서 반복이 이뤄질 때마다 변수 num은 메모리상에 할당되고 소멸된다.

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int num=1;
6
7   if(num==1)
8   {
9     int num=7;  // if문 내 지역 변수!
10     num+=10;
11     printf("if문 내 지역변수 num: %d /n", num);
12   }
13   printf("main 함수 내 지역변수 num: %d \n", num);
14   return 0;
15 }
```

**지역변수는 외부에 선언된 동일한 이름의 변수를 가리게 된다.(덮어쓴다)**

<br>

> 지역변수의 일종, `매개변수`

함수를 정의할 때 선언하는 매개변수도 지역변수의 일종이다. 따라서 매개변수 역시 지역변수의 다음 특성을 지니낟.

- 선언된 함수 내에서만 접근이 가능하다.

- 선언된 함수가 반환을 하면, 지역변수와 마찬가지로 소멸이 된다.

```
따라서 "매개변수는 지역변수이다."라고 이야기해도 틀리지 않는다.
```

하지만 그 **역은 성립하지 않는다.** 지역변수는 매개변수를 포함하는 개념이다.

<br>

### 변수의 존재기간과 접근범위 2: 전역변수, static 변수, register 변수

`전역변수`는 지역변수와 그 성격이 비교되는 변수이다. 프로그램이 처음 실행되면 메모리 공간에 할당되어서 프로그램이 종료될 때까지 메모리 공간에 남아있는 변수다.

> 전역변수의 이해와 선언방법

전역변수는 그 이름이 의미하듯이 어디서든 접근이 가능한 변수로써 지역변수와 달리 중괄호 내에 선언되지 않는다.

```c
#include <stdio.h>
void Add(int val);
int num;  // 전역변수는 기본 0으로 초기화됨

int main(void)
{
  printf("num: %d \n", num);
  Add(3);
  printf("num: %d \n", num);
  num++;
  printf("num: %d \n", num);
  return 0;
}

void Add(int val)
{
  num += val;  // 전역변수 num의  값 val만큼 증가
}
```

```
num: 0
num: 3
num: 4
```

*전역변수의 특징*

- 프로그램의 시작과 동시에 메모리 공간에 할당되어 종료 시까지 존재한다.

- **별도의 값으로 초기화하지 않으면 0으로 초기화** 된다.

- 프로그램 전체 영역 어디서든 접근이 가능하다.

<br>

> 전역변수와 동일한 이름이 지역변수가 선언되면?

해당 지역 내에서는 전역변수가 가리워지고, 지역변수로의 접근이 이뤄진다. → 컴파일 에러 발생

** 전역변수와 지역변수의 이름은 달리하는 것이 좋다. **

<br>

> 전역변수! 편리해 보이니 많이 써도 될까?

전역변수의 선언은 가급적 제한해야 한다. 왜냐하면 **전역변수는 프로그램의 구조를 복잡하게 만드는 주범** 이기 때문이다.

<br>

> 지역변수에 `static` 선언을 추가해서 만드는 `static 변수`

전역변수와 지역변수 모두에 static 선언을 추가할 수 있다. 그러나 본 챕터에서는 지역변수의 static 선언에 대해서만 설명한다.

지역변수에 `static` 선언이 붙게 되면, 이는 전역변수의 성격을 지니는 변수가 된다. 지역변수의 본래 특성은 다음 두 가지이다.

- 선언된 함수 내에서만 접근이 가능하다.

- 함수 내에 선언된 지역변수는 해당 함수가 반환되면 소멸된다.

그런데 함수 내에 선언된 지역변수에 `static` 선언이 붙게 되면, 다음의 특성을 지니게 된다.

- 선언된 함수 내에서만 접근이 가능하다. (지역변수 특징)

- **딱 1회 초기화** 되고 프로그램 종료 시까지 메모리 공간에 존재한다. (전역변수 특성)

static으로 선언된 지역변수는 전역변수와 동일한 시기에 할당되고 소멸된다. 단, 지역변수와 마찬가지로 선언된 함수 내에서만 접근이 가능하다. 따라서 프로그램이 실행되면 SimpleFunc 함수의 실제 모습은 다음과 같다.

```c
void SimpleFunc(void)
{
  static int num1=0;  // 사실은 존재하지 않는다!
  int num2=0;
  num++, num2++;
  printf("static: %d, local: %d \n", num1, num2);
}
```

<br>

> static 지역변수는 사용해도 좋나?

static 지역변수는 전역변수보다 안정적이다. 전역변수와 마찬가지로 프로그램이 종료될 때까지 메모리 공간에 남아있지만, 접근할 수 있는 범위를 하나의 함수로 제한했기 때문이다.

static 지역변수를 전역변수로 대체하는 일은 없어야 하지만, 반대로 **전역변수를 static 지역변수로 대체할 수 있다면 대체해서 프로그램의 안정성을 높여야 한다.**

<br>

> 보다 빠르게! `regiser 변수`

**지역변수에는**  다음과 같이 `regiser`라는 선언을 추가할 수 있다. 그리고 이렇게 해서 선언된 변수를 가리켜 **레지스터 변수** 라 한다.

```c
int SoSimple(void)
{
  regiser int num=3;
  . . . .  
}
```

위와 같이 선언이 되면 변수 num은 CPU 내에 존재하는 '레지스터'라는 메모리 공간에 저장될 확률이 높아진다.

레지스터는 CPU내에 존재하는 그 크기가 매우 작은 메모리이다. 하지만 CPU내에 존재하기 때문에 이 메모리에 저장된 데이터를 대상으로 하는 연산은 매우 빠르다. 바로 이러한 레지스터의 활용과 관련해서 컴파일러에게 힌트를 주는 선언이 바로 `regiser 선언`이다.

레지스터는 하나의 변수가 죽치고 자리하기엔 너무나도 중요하고 비싼 메모리 공간이다. 따라서 **전역변수를 레지스터에 할당하는 것은 있을 수 없는 일이다.** 따라서 전역변수에는 regiser 선언을 추가할 수 없다.

<br>

### 재귀함수에 대한 이해

> 재귀함수의 기본적인 이해

재귀함수란 함수 내에서 자기 자신을 다시 호출하는 함수를 의미한다.

```c
void Recursive(void)
{
  printf("Recursive call! \n");
  Recursive();  // 나 자신을 재 호출한다.
}
```

재귀함수의 이해는 '재진입'의 형태가 아니라 **'복사본의 실행'** 으로 이해하는 것이 좋다.

**| 재귀함수의 호출** : 재귀 호출은 스택(stack)의 전형적인 예시이다.

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/53877356-4c1a6080-404c-11e9-8e0d-b95887c86606.png'>
</p>

Recursive 함수를 실행하는 중간에 다시 Recursive 함수가 호출되면, Recursive 함수의 복사본을 하나 더 만들어서 복사본을 실행하게 된다. 그림과 같이 복사본(stack)을 하나씩 쌓아가는 형태.

**| 재귀함수의 반환**

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/53877355-4c1a6080-404c-11e9-914c-c35bba853ff1.png'>
</p>

재귀적으로 호출이 이뤄진 Recursive 함수에 0이 전달되면서 `재귀의 탈출조건`이 성립되어 함수가 반환하기 시작한다. 반환하는 과정이 마치 엉켜있는 실타래의 한 매듭이 풀리자 전체가 풀리는 듯한 느낌을 준다. *이렇듯 재귀함수의 정의에서 탈출조건을 구성하는 것은 매우 중요한 일이다.*

**| 재귀함수의 흐름**

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/53877354-4c1a6080-404c-11e9-9e53-0df9c95cb057.png'>
</p>

재귀함수를 구현하는데 있어 팩토리얼(factorial)은 중요한 모델이 되니 과정을 이해할 수 있도록 하자.

```c
1 #include <stdio.h>
2
3 int Factorial(int n)
4 {
5   if(n==0)
6     return 1;
7     else
8       return n * Factorial(n-1);
9 }
10
11 int main(void)
12 {
13   printf("1! = %d \n", Factorial(1));
14   printf("2! = %d \n", Factorial(2));
15   printf("3! = %d \n", Factorial(3));
16   printf("4! = %d \n", Factorial(4));
17   printf("5! = %d \n", Factorial(5));
18   return 0;
19 }
```
