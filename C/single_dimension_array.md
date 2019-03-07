# Chapter11. 1차원 배열

### 배열의 이해와 배열의 선언 및 초기화 방법

> 배열이란 무엇인가?

단순하게 '둘 이상의 변수를 모아 놓은 것'으로 설명할 수 있다. 하지만 선언방법부터 접근방법까지 일반적인 변수들과는 차이가 있다.

> 1차원 배열의 선언에 필요한 것 세 가지 : 배열이름, 자료형, 길이정보

```c
int oneDimArr [4];

// int형 변수 4개로 이뤄진 배열을 선언하되, 그 배열의 이름은 oneDimArr로 한다.
```

- int : 배열을 이루는 요소(변수)의 자료형

- oneDimArr : 배열의 이름

- [4] : 배열의 길이

<br>

| ※ 배열의 길이정보는 상수로

```c
int main(void)
{
  int len=20
  int arr[len];  // 변수 len을 이용한 배열의 길이 선언이 가능하다.
  . . . .
}
```

과거의 C 표준에서는 배열의 길이정보를 반드시 상수로 지정하도록 제한하였다. 그런데 이러한 제약사항을 여전히 고수하고 있는 컴파일러도 다수 존재하므로, 범용적인 컴파일을 위해서는 *가급적 배열의 길이를 상수로 지정해야 한다.*

<br>

> 선언된 1차원 배열의 접근

`arr[idx]=20` : 배열의 `idx+1`번째 요소에 20을 저장하라.

→ **배열의 위치 정보를 명시하는 인덱스 값은 1이 아닌 0에서부터 시작한다.**

```c
int arr[3];  // 길이가 3인 int형 1차원 배열 선언

// 위의 배열을 대상으로 값을 저장할 때에는 다음의 형태로 접근을 한다.

arr[0]=10;  // 배열 arr의 첫 번째 요소에 10을 저장한다.
arr[1]=12;  // 배열 arr의 두 번째 요소에 12를 저장한다.
arr[2]=25;  // 배열 arr의 세 번째 요소에 25를 저장한다.
```

<br>

> 배열, 선언과 동시에 초기화하기

기본 자료형 변수들은 선언과 동시에 초기화가 가능하다. 마찬가지로 배열도 선언과 동시에 원하는 값으로 초기화를 할 수 있다. 참고로 배열의 초기화를 목적으로 선언된, *중괄호로 묶인 부분* 을 가리켜 `초기화 리스트` 라 한다.

초기화의 방법은 세가지로 구분이 가능하다.

1. 중괄호 내에 초기화 할 값들을 나열하면, 해당 값들이 순서대로 저장된다.
```c
int arr1[5]={1, 2, 3, 4, 5};  // 순차적으로 1, 2, 3, 4, 5로 초기화
```

2. 초기화를 목적으로 `초기화 리스트`가 선언되면, 배열의 길이정보를 생략할 수 있다.
```c
int arr2[ ]={1, 2, 3, 4, 5, 6 , 7};  // 컴파일러에 의해서 자동으로 7이 삽입됨
```

3. 배열의 길이를 다 채울 만큼 초기 값이 선언되지 않은 경우. 이러한 경우 첫 번째 요소부터 순차적으로 값을 채워나가되 **채울 값이 존재하지 않는 요소들은 0으로 채워진다.**
```c
int arr3[5]={1, 2};  // 3, 4, 5번째 배열요소는 0으로 채워짐
```

<br>

| 배열의 이름을 대상으로 하는 sizeof 연산

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int arr1[5]={1, 2, 3, 4, 5};
6   int arr2[ ]={1, 2, 3, 4, 5, 6, 7};
7   int arr3[5]={1, 2};
8   int ar1Len, ar2Len, ar3Len, i;
9
10   printf("배열 arr1의 크기: %d \n", sizeof(arr1));  // 10행 ~ 12행 gcc 컴파일 경고가 발생하는 부분
11   printf("배열 arr2의 크기: %d \n", sizeof(arr2));
12   printf("배열 arr3의 크기: %d \n", sizeof(arr3));
13
14   ar1Len = sizeof(arr1) / sizeof(int);  // 배열 arr1의 길이 계산
15   ar2Len = sizeof(arr2) / sizeof(int);  // 배열 arr2의 길이 계산
16   ar3Len = sizeof(arr3) / sizeof(int);  // 배열 arr3의 길이 계산
17
18   for(i=0; i<ar1Len; i++)
19     printf("%d ", arr1[i]);
20   printf("\n");
21
22   for(i=0; i<ar2Len; i++)
23     printf("%d ", arr2[i]);
24   printf("\n");
25
26   for(i=0; i<ar3Len; i++)
27     printf("%d ", arr3[i]);
28   printf("\n");
29
30   return 0;
31 }
```

```
// gcc 컴파일 경고

1.c:10:34: warning: format ‘%d’ expects argument of type ‘int’, but argument 2 has type ‘long unsigned int’ [-Wformat=]
  printf("배열 arr1의 크기: %d \n", sizeof(arr1));
                                 ~^
                                 %ld
1.c:11:34: warning: format ‘%d’ expects argument of type ‘int’, but argument 2 has type ‘long unsigned int’ [-Wformat=]
  printf("배열 arr2의 크기: %d \n", sizeof(arr2));
                                 ~^
                                 %ld
1.c:12:34: warning: format ‘%d’ expects argument of type ‘int’, but argument 2 has type ‘long unsigned int’ [-Wformat=]
  printf("배열 arr3의 크기: %d \n", sizeof(arr3));
                                 ~^
                                 %ld
```

위의 코드 그대로 gcc 컴파일을 진행하면 위의 코드 블럭과 같은 경고가 뜬다. 이 경우, `unsigned int`의 출력을 위한 포맷 `%zu`를 사용하면 해결이 된다.

`size_t`는 `unsigned int` 이며, 문자열이나 메모리의 사이즈를 나타낼 때 사용한다. C99 표준에서 `size_t`는 printf 포맷이 `%zu`로 지정되어 있다. 기존의 코드를 아래와 같이 수정하면 경고가 사라진다.

- [size_t 레퍼런스 링크](https://en.cppreference.com/w/c/types/size_t)

- [size_t 나무위키 설명](https://namu.wiki/w/size_t)

``` c
10   printf("배열 arr1의 크기: %d \n", sizeof(arr1));  // 10행 ~ 12행 gcc 컴파일 경고가 발생하는 부분
11   printf("배열 arr2의 크기: %d \n", sizeof(arr2));
12   printf("배열 arr3의 크기: %d \n", sizeof(arr3));

//          %d 를 %zu로 수정한다.

10   printf("배열 arr1의 크기: %zu \n", sizeof(arr1));
11   printf("배열 arr2의 크기: %zu \n", sizeof(arr2));
12   printf("배열 arr3의 크기: %zu \n", sizeof(arr3));
```


```
// 출력결과

배열 arr1의 크기: 20
배열 arr2의 크기: 28
배열 arr3의 크기: 20
1 2 3 4 5
1 2 3 4 5 6 7
1 2 0 0 0
```

 `배열 크기 = 배열 길이 X 배열의 자료형 크기`

 배열 arr1의 크기 `20`은 길이가 `5`이고 자료형의 크기가 `4`바이트인 int형을 사용했기 때문에 나온 결과이다.

 배열의 길이를 구할 땐 `sizeof(arr1) / sizeof(int)` 대신 `sizeof(arr1) / sizeof(arr1[0])`도 사용이 가능하다. `sizeof(arr1[0])`와 같이 배열 첫 번째 요소의 바이트 단위 크기는 배열 자료형의 크기와 같기 때문이다.

**결론적으로, 배열의 이름을 대상으로 하는 sizeof 연산의 결과로는 `바이트 단위의 배열 크기`가 반환된다.**

<br>

| 정해진 수만큼 입력된 정수의 최댓값, 최솟값, 총 합 구하기

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int arr[5];
6   int max, min, sum;
7
8   for(int i=0; i<5; i++)
9   {
10     printf("정수를 입력하시오: ");
11     scanf("%d", &arr[i]);
12   }
13
14   max=min=sum=arr[0];
15   for(int i=0; i<5; i++)
16   {
17     sum += arr[i];
18
19     if(max<arr[i])
20       max = arr[i];
21     if(min>arr[i])
22       min = arr[i];
23   }
24
25   printf("입력된 정수 중에서 최댓값: %d \n", max);
26   printf("입력된 정수 중 최솟값: %d \n", min);
27   printf("입력된 정수의 총 합: %d \n", sum);
28   return 0;
29 }
```

<br>

| "Good time" 출력하기 (현재 챕터까지 배운 개념만을 이용하여)

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   char str[]={'G','o','o','d',' ','t','i','m','e    ' };
6   int arrLen = sizeof(str) / sizeof(char);
7   int i;
8
9   for(int i = 0; i<arrLen; i++)
10     printf("%c", str[i]);
11     printf("\n");
12
13   return 0;
14 }
```

<br>

### 배열을 이용한 문자열 변수의 표현

> char형 배열의 문자열 저장과 '널(null)' 문자

```c
char str[14]="Good morning!";

char str[ ]="Good morning!";  // 배열의 길이는 14로 결정
```

문자열의 끝에는 `＼0` 이라는 특수문자(escape sequence)가 자동으로 삽입이 된다. 문자열의 저장을 목적으로 char형 배열을 선언할 경우에는 특수문자 `＼0`이 저장될 공간까지 고려해서 배열의 길이를 결정해야 한다.

이렇듯 문자열의 끝에 자동으로 삽입되는 문자 `＼0`을 가리켜 `널(null)` 문자라고 한다. 널 문자의 아스키 코드 값은 0이다. 그리고 이를 문자의 형태로 출력할 경우, 아무런 출력이 발생하지 않는다.

※ `널 문자`와 `공백 문자`의 차이

```c
int main(void)
{
  char nu = '\0';  // 널 문자 저장
  char sp = ' ';  // 공백 문자 저장
  printf("%d %d", nu, sp);  // 0과 32 출력
  return 0;
}
```

<br>

> scanf 함수를 이용한 문자열의 입력

**문자열을 입력 받는 배열의 이름 앞에는 `& 연산자` 를 붙이면 안 된다.**

```c
scanf("s", str);  // str 앞에 & 연산자를 삽입하지 않는다.
```

scanf 함수 호출문 구성 시, 데이터를 저장할 변수의 이름 앞에는 & 연산자를 붙여줘야 한다. 그러나 문자열을 입력 받는 배열의 이름 앞에는 & 연산자를 붙이지 않는다.


scanf 함수호출을 통해서 입력 받은 문자열의 끝에도 널 문자가 삽입되어 있다. **C 언어에서 표현하는 모든 문자열의 끝에는 널 문자가 자동으로 삽입된다.**

문자열에 있어서 널 문자의 존재는 매우 중요하다. **널 문자가 존재하면 문자열이고 널 문자가 존재하지 않으면 문자열이 아니다.**

```c
char arr1[] = {'H', 'i', '~'};  // 마지막에 널 문자가 없으므로 문자 배열

char arr2[] = {'H', 'i', '~', '\0'};  // 마지막에 널 문자가 있으므로 문자열
```

※ 널 문자의 아스키 코드 값은 0이므로 다음 두 문장은 동일한 문장이다.

```
  str[8] = '\0';

  str[8] = 0;
```

문자열의 판단여부에 있어서 선언방법은 중요하지 않다. 어떻게 선언이 되든 널 문자가 마지막에 존재하면 이는 C언어의 관점에서 문자열이 되는 것이다.

<br>

| 입력받은 단어의 길이를 계산하여 출력하는 프로그램

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   char voca[100];
6   int len = 0;
7
8   printf("영단어를 입력하시오 :");
9   scanf("%s", voca);
10
11   while(voca[len] != '\0')   // while(voca[len] != 0) 과 동일
12     len++;
13
14   printf("영단어의 길이: %d \n", len);
15   return 0;
16 }
17
```

<br>

| 입력받은 단어를 역순으로 뒤집는 프로그램

두 문자를 바꾸는 횟수는 단어의 길이에 따라 달라지게 되며, 이를 위해서 영단어 길이의 반에 해당하는 수 만큼 두 문자를 바꾸는 과정을 거치도록 반복문을 구성하였다.

`for(i=0; i<len/2; i++)` // len의 자료형이 int이므로 len/2의 소수점 뒤 자릿수는 없어진다.

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   char voca[100];
6   int len = 0, i;
7   char temp;
8
9   printf("영단어를 입력하시오 : ");
10   scanf("%s", voca);
11
12   while(voca[len] != 0)  // 영단어의 길이 계산
13     len++;
14
15   for(i=0; i<len/2; i++)
16   {
17     temp=voca[i];  // 앞의 문자 임시로 저장
18     voca[i]=voca[(len-i)-1];  // 뒤의 문자를 앞>    으로
19     voca[(len-i)-1]=temp;  // 앞의 문자를 뒤로
20   }
21   printf("뒤집힌 영단어: %s \n", voca);
22   return 0;
23 }
```

<br>

| 영단어를 입력받아 아스키 코드 값이 제일 큰 문자 출력

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   char voca[100];
6   char max=0;
7   int len=0, i;
8
9   printf("영단어를 입력하시오 : ");
10   scanf("%s", voca);
11
12   while(voca[len] != 0)
13     len++;
14
15   for(i=0; i<len; i++)
16     if(max<voca[i])
17       max=voca[i];
18
19   printf("아스키 코드 값이 가장 큰 문자: %c \n",     max);
20   return 0;
21 }
```
