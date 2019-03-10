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




<p align='center'>
<img src=''>
</p>

<p align='center'>
<img src=''>
</p>

<p align='center'>
<img src=''>
</p>
