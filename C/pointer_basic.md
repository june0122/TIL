# Chapter12. 포인터의 이해

### 포인터란?

포인터는 C언어가 Low 레벨 언어의 특성을 지닌다고 이야기하게 만든 장본인이다.
포인터를 이용하면 **메모리에 직접 접근** 이 가능하기 때문이다.

> 주소 값의 저장을 목적으로 선언되는 포인터 변수

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/53940739-c0f7a400-40f9-11e9-9442-0727e826bf59.png'>
</p>

좌측 코드와 같이 변수가 선언되었다고 가정하면, 총 6바이트(1바이트 + 1바이트 + 4바이트)가 메모리 공간에 우측 그림과 같이 할당된다.(나란히 할당되지 않을 수도 있다.)

*"int형 변수 num은 0x12ff76번지에 할당되어 있다."*

C언어에서는 시작번지만을 가지고 위치를 표현한다. 왜냐하면 int형 변수는 4바이트이므로 변수의 끝이
어딘지는 쉽게 계산이 가능하기 때문이다. 그런데 위의 문장의 주소 값 0x12ff76 역시 정수이다.
따라서 이것도 저장이 가능한 값이며, 이의 저장을 위해 마련된 변수가 바로 `포인터 변수`이다.

**포인터 변수란 메모리의 주소 값을 저장하기 위한 변수이다.**

<br>

> 포인터 변수와 & 연산자에 대해 간단히 알아보기

*정수 7이 저장된 int형 변수 num을 선언하고 이 변수의 주소 값 저장을 위한 포인터 변수 pnum을 선언하자. 그리고 나서 pnum에 변수 num의 주소 값을 저장하자.*

```c
// 위의 문장을 코드로 옮긴 것

int main(void)
{
  int num=7;
  int * pnum;   // 포인터 변수 pnum의 선언
  pnnum = &num;  // num의 주소 값을 포인터 변수 pnum에 저장
}
```

`int * pnum`

- pnum  : 포인터 변수의 이름
- int * : int형 변수의 주소 값을 저장하는 포인터 변수의 선언

`pnum = &num`

**& 연산자** 는 *오른쪽에 등장하는 피연산자의 주소 값을 반환하는 연산자* 이다.

<br>

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/53942140-58122b00-40fd-11e9-8b7b-85d63938bb92.png'>
</p>

<br>

위의 그림에서 보이듯 포인터 변수 pnum에는 변수 num의 시작번지 주소 값이 저장된다.
그리고 이 상황을 다음과 같이 표현한다.

**포인터 변수 pnum이 int형 변수 num을 가리킨다.**

※ 위 그림에서 포인터 변수 pnum의 크기는 8바이트(64비트)로 묘사되어 있다. 포인터 변수의 크기는 4바이트가 될 수도 있고 8바이트가 될 수도 있다. 32비트 시스템에서는 주소 값을 32비트로 표현하기 때문에 포인터 변수의 크기가 4바이트인 반면, 64비트 시스템에서는 주소 값을 64비트로 표현하기 때문에 포인터 변수의 크기가 8바이트이다.

이렇듯 포인터 변수의 크기가 주소 값의 크기와 일치해야 주소 값을 저장할 수 있으므로 두 값이 동일한 것은 당연하다 할 수 있다.

<br>

> 포인터 변수 선언하기

포인터 변수는 가리키고자 하는 변수의 자료형에 따라서 선언하는 방법이 달라진다. 주소 값은 동일한 시스템에서 그 크기가 동일하며 모두 정수의 형태를 띤다.

**| 포인터 변수 선언의 기본 공식**

```
type * ptr;

    type형 변수의 주소 값을 저장하는 포인터 변수 ptr의 선언
```

- int * pnum1;

  -  int * 는 int형 변수를 가리키는 pnum1의 선언을 의미함


- double * pnum2;

  - double * 는 double형 변수를 가리키는 pnum2의 선언을 의미함


- unsigned int * pnum3;

  - unsigned int * 는 unsigned int형 변수를 가리키는 pnum3의 선언을 의미함

→ 포인터 변수의 선언형태만 보고도 이 포인터가 현재 가리키는 변수의 자료형을 짐작할 수 있다.

<br>

> 포인터의 형(Type)

int, char, double과 같이 변수의 선언 및 구분에 사용되는 키워드를 '자료형'이라 하듯이 포인터 변수의 선언 및 구분에 사용되는 `int *`, `char *`, `double *` 등을 가리켜 **포인터 형(type)** 이라 한다. 포인터 변수도 값을 저장하는 변수이므로 '포인터 형' 역시 '자료형'의 범주에 포함시킨다.

```
type *          type형 포인터

type * ptr;     type형 포인터 변수 ptr
```

<br>

### 포인터와 관련 있는 연산자: & 연산자와 * 연산자를

> 변수의 주소 값을 반환하는 `& 연산자`

`& 연산자`는 피연산자의 주소 값을 반환하는 연산자이다. 그러므로 다음과 같이 연산문을 구성한다.

```c
int main)(void)
{
  int num = 5;
  int * pnum = &num;  // num의 주소 값을 반환해서 포인터 변수 pnum을 초기화
}
```

`& 연산자의 피연산자`는 **변수** 이어야 하며, 상수는 피연산자가 될 수 없다. 그리고 자료형에 맞게 포인터 변수의 선언을 해야 한다.

<br>

> 포인터가 가리키는 메모리를 참조하는 `* 연산자`

`* 연산자`는 포인터가 가리키는 메모리 공간에 접근할 때 사용하는 연산자이다.

```C
int main(void)
{
  int num=10;
  int * pnum = &num;  // 포인터 변수 pnum이 변수 num을 가리키게 하는 문장
  *pnum=20;  // pnum이 가리키는 변수에 20을 저장하라
  printf("%d", *pnum);  // pnum이 가리키는 변수를 부호 있는 정수로 출력하라
  . . . .
}
```

`*pnum` : "포인터 변수 pnum이 가리키는 메모리 공간인 변수 num에 접근을 해서..."

```
*pnum=20;

    포인터 변수 pnum이 가리키는 메모리 공간인 변수 num에 정수 20을 저장하라.

printf("%d", *pnum);

    포인터 변수 pnum이 가리키는 메모리 공간이 변수 num에 저장된 값을 출력하라.
```

이렇듯 사실상 *pnum은 포인터 변수 pnum이 가리키는 변수 num을 의미하는 것이다.

<br>

| 변수와 포인터 변수의 관계 이해

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int num=10;
6   int* ptr1=&num;
7   int* ptr2=ptr1;
8
9   (*ptr1)++;
10   (*ptr2)++;
11   printf("%d \n", num);
12   return 0;
13 }
```

```
prt1과 ptr2가 동시에 변수 num을 가리키는 형태가 된다.

최종 출력값은 12
```

<br>

| 두 포인터 변수가 가리키는 대상을 서로 바꾸기

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int num1=10, num2=20;
6   int* ptr1=&num1;
7   int* ptr2=&num2;
8   int* temp;
9
10   (*ptr1)+=10;
11   (*ptr2)-=10;
12
13   temp=ptr1;
14   ptr1=ptr2;
15   ptr2=temp;
16
17   printf("%d %d \n", *ptr1, *ptr2);
18
19   return 0;
20 }
```



<br>

> 다양한 '포인터 형'이 존재하는 이유

포인터 기반의 **메모리 접근기준을 마련하기 위함**이다. 포인터에 형이 존재하지 않는다면 * 연산을 통한 메모리의 접근은 불가능하다.

> 포인터 변수의 초기화

포인터 변수를 우선 선언만 해 놓고, 이후에 유효한 주소 값을 채워 넣을 생각이라면 다음과 같이 초기화를 하는 것이 좋다.

```c
int main(void)
{
  int * ptr1=0;
  int * ptr2=NULL;  // NULL은 사실상 0을 의미함
  . . . .
}
```

위에서 ptr1을 초기화하는 값 0을 가리켜 `널 포인터` 라 한다. 이것은 0번지를 의미하는 것이 아닌, **아무데도 가리키지 않는다** 는 의미를 지닌다.