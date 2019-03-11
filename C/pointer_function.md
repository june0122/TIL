# Chapter14. 포인터와 함수에 대한 이해

### 함수의 인자로 배열 전달하기

> 인자전달의 기본방식은 `값의 복사`

***함수호출 시 전달되는 인자의 값은 매개변수에 `복사`가 된다.***

```c
int SimpleFunc(int num){ . . . . }
int main(void)
{
  int age=17;
  SimpleFunc(age);  // age에 저장된 값이 매개변수 num에 복사됨
}
```

위 코드의 SimpleFunc 함수의 호출을 통해서 인자로 age를 전달하고 있다. 그러나 **실제로 전달되는 것은 age가 아닌, age에 저장된 값이다!** 그리고 그 값이 매개변수 num에 복사되는 것이다. 따라서 age와 num은 값을 주고받은 사이일 뿐 그 이상은 아무런 관계도 아니다.

함수를 호출하면서 매개변수에 배열을 통째로 넘겨주는 방법은 존재하지 않는다. 이유는 다음과 같다.

**매개변수로 배열을 선언할 수 없다!**

***배열을 통째로 넘겨받으려면 매개변수로 배열을 선언할 수 있어야 한다.*** 하지만 이것이 허용되지 않으니 불가능한 일이다. 대신 함수 내에서 배열에 접근할 수 있도록 배열의 주소 값을 전달하는 것은 가능하다.

<br>

> 배열을 함수의 인자로 전달하는 방식

다음과 같이 선언된 배열

```c
  int arr[3]={1, 2, 3};
```

다음의 형태로 함수를 호출하면서 배열의 주소 값 전달 가능

```c
  SimpleFunc(arr);  // SimpleFunc 함수를 호출하면서 배열 arr의 주소 값 전달
```

SimpleFunc 함수의 매개변수 선언

```c
int main(void)
{
  int arr[3]={1, 2, 3};
  int * ptr=arr;   // 배열이름 arr은 int형 포인터
  . . . .
}
```

따라서 SimpleFunc 함수의 매개변수는 다음과 같이 int형 포인터 변수로 선언되어야 한다.

```c
  void SimpleFunc(int * param) { . . . . }
```

그럼 매개변수 param을 이용해서 배열에는 어떻게 접근할까? 포인터 변수를 이용해도 배열의 형태로 접근이 가능하니, 다음과 같이 접근이 가능하다.

```c
  printf("%d %d", param[1], param[2]);  // 두 번째, 세 번째 요소 출력!
```

<br>

> 매개변수로 받은 배열의 요소 변경

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54096641-02d16480-43f0-11e9-88c7-b2e5182b48cd.png'>
</p>

<br>

> **※** 복합 리터럴(compound literal) 사용하기

[cppreference 복합 리터럴 설명](https://en.cppreference.com/w/c/language/compound_literal)

함수에 배열을 넘겨줄 때, 복합 리터럴을 사용하면 따로 배열을 선언하지 않아도 된다.

| Syntax

```
(자료형[]) { 값1, 값2, 값3 }

(자료형[크기]) { 값1, 값2, 값3 }
```

| 예시

```
1 #include <stdio.h>
2
3 void printArray(int arr[], int count)
4 {
5   for(int i = 0; i < count; i++)
6     printf("%d ", arr[i]);
7   printf("\n");
8 }
9
10 int main()
11 {
12   // 복합 리터럴 방식으로 배열을 넘겨줌
13   printArray ((int[]){1, 2, 3, 4, 5, 6},     6);
14
15   return 0;
16 }
```

<br>

| 함수 내에서 외부(main 내에 선언된) 배열에 접근하여 값을 `출력`하기

```c
1 #include <stdio.h>
2
3 void ShowArayElem(int * param, int len)
4 {
5   int i;
6   for(i=0; i<len; i++)
7     printf("%d ", param[i]);
8   printf("\n");
9 }
10
11 int main(void)
12 {
13   int arr1[3]={1, 2, 3};
14   int arr2[5]={4, 5, 6, 7, 8};
15   ShowArayElem(arr1, sizeof(arr1) / sizeof(int));
16   ShowArayElem(arr2, sizeof(arr2) / sizeof(int));
17   return 0;
18 }
```

```
1 2 3
4 5 6 7 8
```

<br>

| 함수 내에서 외부(main 내에 선언된) 배열에 접근하여 값을 `변경`하기

```c
1 #include <stdio.h>
2
3 void ShowArayElem(int * param, int len)
4 {
5   int i;
6   for(i=0; i<len; i++)
7     printf("%d ", param[i]);
8   printf("\n");
9 }
10
11 void AddArayElem(int * param, int len, int add)
12 {
13   int i;
14   for(i=0; i<len; i++)
15     param[i] += add;
16 }
17
18 int main(void)
19 {
20   int arr[3]={1, 2, 3};
21   AddArayElem(arr, sizeof(arr) / sizeof(int), 1);
22   ShowArayElem(arr, sizeof(arr) / sizeof(int));
23
24   AddArayElem(arr, sizeof(arr) / sizeof(int), 2);
25   ShowArayElem(arr, sizeof(arr) / sizeof(int));
26
27   AddArayElem(arr, sizeof(arr) / sizeof(int), 3);
28   ShowArayElem(arr, sizeof(arr) / sizeof(int));
29   return 0;
30 }
```

```
2 3 4
4 5 6
7 8 9
```

<br>

> 배열을 함수의 인자로 전달받는 함수의 또 다른 선언

위의 예제 두 함수에는 int 배열의 주소 값을 인자로 전달받을 수 있도록 int형 포인터 변수가 선언되어 있다.

```c
void ShowArayElem (int * param, int len) { . . . . }
void AddArayElem (int * param, int len, int add) { . . . . }
```

그런데 이를 대신하여 다음과 같은 방법으로도 선언이 가능하다.

```c
void ShowArayElem (int param[], int len) { . . . . }
void AddArayElem (int param[], int len, int add) { . . . . }
```

**즉, `int param[]`과 `int * param`은 완전히 동일한 선언이다.** 하지만 전자의 선언이 배열이 인자로 전달된다는 것을 직관적으로 보여주므로 더 많이 사용된다. **하지만 이 둘이 같은 선언으로 간주되는 경우는 매개변수의 선언으로 제한된다.**

다음의 코드에서 main 함수의 두 번째 문장인 `int * ptr=arr`을 `int ptr[]=arr`으로 대체할 수 없다.

```c
int main(void)
{
  int arr[3]={1, 2, 3};
  int * ptr=arr;  // int ptr[]=arr;로 대체 불가능
}
```

<br>

> **※** 함수 내에서는 인자로 전달된 배열의 길이를 계산할 수 없다.

```c
void SimpleAryFunc1(int * arr)
{
  int sz = sizeof(arr);  // 변수 sz에는 포인터 변수 arr의 크기가 저장
  . . . .
}
```

**배열의 주소 값을 인자로 전달받는 매개변수는 포인터 변수이기 때문에 이를 대상으로 `sizeof` 연산을 할 경우 배열의 크기가 반환되지 않고 포인터 변수의 크기가 반환된다.** 이렇듯 함수 내에서는 인자로 전달된 배열의 길이를 계산할 수 없기 때문에 배열의 크기나 길이정보도 함께 인자로 전달해야 한다.

<br>

### Call-by-value vs Call-by-reference

- 단순히 값을 전달하는 형태의 함수호출을 가리켜 `Call-by-value`

- 메모리의 접근에 사용되는 주소 값을 전달하는 형태의 함수호출을 가리켜 `Call-by-reference`

<br>

> 값을 전달하는 형태의 함수호출: Call-by-value

```c
  1 #include <stdio.h>
  2
  3 void Swap(int n1, int n2)
  4 {
  5   int temp=n1;
  6   n1=n2;
  7   n2=temp;
  8   printf("n1 n2: %d %d \n", n1, n2);
  9 }
 10
 11 int main(void)
 12 {
 13   int num1=10;
 14   int num2=20;
 15   printf("num1 num2: %d %d \n", num1, num2);
 16
 17   Swap(num1, num2);
 18   printf("num1 num2: %d %d \n", num1, num2);
 19   return 0;
 20 }
```

<br>

> 주소 값을 전달하는 형태의 함수호출 : Call by reference

```c
1 #include <stdio.h>
2
3 void Swap(int * ptr1, int * ptr2)
4 {
5   int temp=*ptr1;
6   *ptr1=*ptr2;
7   *ptr2=temp;
8 }
9
10 int main(void)
11 {
12   int num1=10;
13   int num2=20;
14   printf("num1 num2: %d %d \n", num1, num2);
15   Swap(&num1, &num2);
16   printf("num1 num2: %d %d \n", num1, num2);
17   return 0;
18 }
```

<br>

### 포인터 대상의 *const* 선언

> 포인터 변수가 참조하는 대상의 변경을 허용하지 않는 const 선언


```c
int main(void)
{
   int num=20;
   const int * ptr=&num;
   *ptr=30;  // 컴파일 에러
   num=40;  // 컴파일 성공
   . . . .
}
```

**맨 앞부분에서의 const 선언** 은 포인터 변수 ptr을 이용해서 ptr이 가리키는 변수에 저장된 값을 변경하는 것을 허용하지 않는다. 위의 const 선언은 값을 변경하는 방법에 제한을 두는 것이지 무엇인가를 상수로 만드는 선언은 아니다.

<br>

> 포인터 변수의 상수화

```c
int main(void)
{
  int num1=20;
  int num2=30;
  int * const ptr = &num1;
  ptr=&num2;  // 컴파일 에러
  *ptr=40; // 컴파일 성공
  . . . .  
}
```

상수화된 포인터 변수 ptr이 num1을 가리키고 있다. 그런데 다음 행에서, 가리키는 대상을 num2으로 바구기 위한 연산을 진행하고 있다. 하지만 ptr은 상수이기 때문에 이 부분에서 컴파일 에러가 발생한다. 물론 ptr이 상수일 뿐이니 , `*ptr=40;` 같이 ptr이 가리키는 대상에 저장된 값을 변경하는 연산은 문제되지 않는다.


<br>

```c
const int * const ptr = &num;

*ptr=20;  // 컴파일 에러
ptr=&age;  // 컴파일 에러
```

하나의 포인터 변수를 대상으로 이 두 가지 형태의 const 선언을 동시에 할 수도 있다. 이렇게 되면 `*ptr=40;`, `ptr=&age;` **두 연산 모두 불가능**해진다.

<br>

> const 선언이 갖는 의미

```c
int main(void)
{
  const double PI=3.1415;
  double rad;
  PI=3.07;  // 컴파일 시 발견되는 오류상황
  scanf("%lf", &rad);
  printf("cirlce area %f \n", rad*rad*PI);
  return 0;
}
```

```c
const double PI=3.1415;
```

위의 코드에서 const가 없다면 변경되면 안되는 PI값이 변경되는 사태가 발생한다. `const` 선언은 이러한 상황을 막아주고 **코드의 안정성을 높여준다.**
