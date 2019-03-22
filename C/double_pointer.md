# Chapter17. 포인터의 포인터

## 포인터의 포인터에 대한 이해

### 포인터 변수를 가리키는 이중 포인터 변수 (더블 포인터 변수)

```c
int ** dptr;    // int형 이중 포인터
```

포인터 변수는 종류에 상관없이 무조건 주소를 저장하는 변수이다. 다만 차이가 나는 것은 포인터 변수가 가리키는 대상이다.

```c
int main(void) {
    double num = 3.14;
    double * ptr = &num;  // 변수 num의 주소 값 저장
    . . . .
}
```

```c
double ** dptr = &ptr;
```

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54519876-dba80380-49aa-11e9-9a06-c2a64923b516.PNG'>
</p>

<br>

```c
*dptr = . . . .;        // *dptr은 포인터 변수 ptr을 의미함
*(*dptr) = . . . .;     // *(*dptr)은 변수 num을 의미함
```


<p align='center'>
<img src=''>
</p>

<p align='center'>
<img src=''>
</p>
