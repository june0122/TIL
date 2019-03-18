# Chapter16. 다차원 배열

## 다차원 배열의 이해와 활용

### 2차원 배열의 메모리상 할당의 형태

<br>

2차원 배열도 **메모리상에는 1차원의 형태로 존재**한다.

<br>

### 배열의 크기를 알려주지 않고 초기화하기

```c
int arr1[][4] = { 1, 2, 3, 4, 5, 6, 7, 8 };
```

- 2차원 배열을 선언과 동시에 초기화하는 경우에는 **배열의 세로 길이만 생략이 가능**하다.

<br>

> 4층 빌라 거주 인원 수

```c
  1 #include <stdio.h>
  2
  3 int main(void) {
  4   int villa[4][2];
  5   int popu, i, j;
  6
  7   /* 가구별 거주인원 입력 받기 */
  8   for (i = 0; i < 4; i++)
  9   {
 10     for (j = 0; j <2; j++)
 11     {
 12       printf("%d층 %d호 인구수: ", i+1, j+1);
 13       scanf("%d", &villa[i][j]);
 14     }
 15   }
 16
 17   /* 빌라의 층별 인구수 출력하기 */
 18   for (i = 0; i < 4; i++)
 19   {
 20     popu = 0;
 21     for (j = 0; j < 2; j++)
 22     {
 23       popu += villa[i][j];
 24     }
 25     printf("%d층 인구수: %d \n", i+1, popu);
 26   }
 27   return 0;
 28 }
```

<br>

> 구구단 2~4단의 저장

```c
  1 #include <stdio.h>
  2
  3 int main(void) {
  4   int arr[3][9];
  5   int i, j;
  6
  7   for ( i = 0 ; i < 3 ; i++) {
  8     for ( j = 0 ; j < 9 ; j++) {
  9       arr[i][j] = (i+2) * (j+1);
 10       printf("%4d ", arr[i][j]);
 11     }
 12     printf("\n");
 13   }
 14   return 0;
 15 }
```

```
   2    4    6    8   10   12   14   16   18
   3    6    9   12   15   18   21   24   27
   4    8   12   16   20   24   28   32   36
```

<br>

> 다른 형태의 2차원 배열의 저장값 옮기기

```c
  1 #include <stdio.h>
  2
  3 int main() {
  4   int arr1[2][4] = { { 1, 2, 3, 4}, {5, 6, 7, 8} };
  5   int arr2[4][2];
  6   int i, j, temp;
  7
  8   for ( i = 0; i < 2; i++ )
  9     for ( j = 0; j < 4; j++ )
 10       arr2[j][i] = arr1[i][j];
 11
 12   for ( i = 0; i < 4; i++ ) {
 13     for ( j = 0; j < 2; j++ )
 14       printf("%2d", arr2[i][j]);
 15     printf("\n");
 16   }
 17 }
```

```
 1 5
 2 6
 3 7
 4 8
```

<br>

> 과목 점수 입력 프로그램

```c
  1 #include <stdio.h>
  2
  3 int record[5][5];
  4
  5 void WriteRecord(void)
  6 {
  7   int sum;
  8   int i, j;
  9
 10   for (i = 0; i < 4; i++) {
 11     sum = 0;
 12     printf("%d번째 학생의 성적 입력 \n", i+1);
 13
 14     for (j=0; j < 4; j++) {
 15       printf("과목 %d: ", j+1);
 16       scanf("%d", &record[i][j]);
 17       sum += record[i][j];
 18     }
 19     record[i][4] = sum;
 20   }
 21 }
 22
 23 void WriteSumRecord(void) {
 24   int sum = 0;
 25   int i, j;
 26
 27   for (i = 0; i < 4; i++) {
 28     sum = 0;
 29     for (j = 0; j < 4; j++)
 30       sum += record[j][i];
 31     record[4][i] = sum;
 32     record[4][4] += sum;
 33   }
 34 }
 35
 36 void ShowAllRecord(void) {
 37   int i, j;
 38   for (i = 0; i < 5; i++) {
 39     for(j = 0; j < 5; j++)
 40       printf("%3d ", record[i][j]);
 41       printf("\n");
 42   }
 43 }
 44
 45 int main(void) {
 46   WriteRecord();
 47   WriteSumRecord();
 48   ShowAllRecord();
 49   return 0;
 50 }
```

```
  5   2   7   1  15
  2   5   8   8  23
  2   1   7   6  16
  9   6   7   8  30
 18  14  29  23  84
```

<br>

## 3차원 배열

### 3차원 배열의 논리적 구조

- 논리적으로 직육면체 형태의 배열

```c
TYPE arr[높이][세로][가로];
```

**3차원 배열은 여러 개의 2차원 배열이 모여있는 형태로 이해하는 것이 더 합리적이다.**

<br>



<p align='center'>
<img src=''>
</p>

<p align='center'>
<img src=''>
</p>
