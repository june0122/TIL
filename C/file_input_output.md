# Chapter24. 파일 입출력

## 파일과 스트림(Stream), 그리고 기본적인 파일의 입출력

### 파일에 저장된 데이터를 읽고 싶을 때

<br>

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54546550-47a85d00-49e7-11e9-9bf9-2f7e1e23a883.PNG'>
</p>

<br>

- 프로그램상에서 파일에 저장되어 있는 파일을 참조하기 위해, 제일 먼저 할 일은?

    - 구현한 프로그램과 참조할 데이터가 저장되어 있는 파일 사이에 **데이터가 이동할 수 있는 다리**를 놓는 일이다.

    - 데이터 이동 경로가 되는 다리를 가리켜 **스트림(Stream)** 이라 한다.

- 스트림은 어디까지나 운영체제에 의해서 형성되는 소프트웨어적인 상태를 의미하는 것이다.

- 파일은 운영체제에 의해서 그 구조가 결정되고 관리되는 대상이기 때문에, 파일 뿐만 아니라 *스트림의 형성도 운영체제의 몫* 임을 기억하자.

<br>

### fopen 함수호출을 통한 파일과의 스트림 형성과 FILE 구조체

- 다음은 스트림을 형성할 대 호출하는 함수이다.
  
- 이 함수의 호출을 통해 프로그램상에서 파일과의 스트림을 형성할 수 있다.

```c
#include <stdio.h>
FILE * fopen(const char * filename, const char * mode)

    // 성공 시 해당 파일의 FILE 구조체 변수의 주소 값, 실패 시 NULL 포인터 반환
```

- 첫 번째 인자 : `스트림을 형성할 파일 이름`
    
- 두 번째 인자 : `형성할 스트림의 종류에 대한 정보를 문자열의 형태로 전달`

<br>

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54547734-aec71100-49e9-11e9-850b-485d60e8120c.jpg'>
</p>

> 프로그램상에서 fopen 함수를 호출했을 때 일어나는 일들

- fopen 함수가 호출되면 FILE 구조체 변수가 생성된다.
  
- 생성된 FILE 구조체 변수에는 파일에 대한 정보가 담긴다.

- FILE 구조체의 포인터는 사실상 파일을 가리키는 '지시자'의 역할을 한다.

<br>

### 입력 스트림과 출력 스트림의 생성

- 스트림은 *한 방향으로 흐르는 데이터의 흐름* 을 의미한다.

- 때문에 스트림은 `입력 스트림` 과 `출력 스트림` 으로 구분된다.

<br>

> fopen 함수를 호출할 때 전달되어야 하는 두 가지 인자

- 첫 번째 전달인자 : 스트림을 형성할 파일의 이름

- 두 번째 전달인자 : 형성하고자 하는 스트림의 종류

<br>

> 출력 스트림의 형성을 요청하는 fopen 함수의 호출문

```c
FILE * fp = fopen("data.txt", "wt");    // 출력 스트림의 형성

// 파일 data.txt와 스트림을 형성하되 wt 모드로 스트림을 형성해라
```

<br>

> 입력 스트림의 형성

```c
FILE * fp = fopen("data.txt", "rt");    // 입력 스트림의 형성

// 파일 data.txt와 스트림을 형성하되 rt 모드로 스트림을 형성해라 
```

- 참고로 fopen 함수 호출을 통해서 파일과의 스트림이 형성되었을 때, *파일이 개방(오픈)되었다* 라고 일반적으로 표현한다.

<br>

### 파일에 데이터를 써보자

> fputc('A', fp);

```c
  1 #include <stdio.h>
  2
  3 int main(void) {
  4   FILE * fp = fopen("data.txt", "wt");
  5   if(fp == NULL) {
  6     puts("파일 오픈 실패!");
  7     return -1;  // 비정상적 종료를 의미하기 위해서 -1을 반환
  8
  9
 10     fputc('A', fp);
 11     fputc('B', fp);
 12     fputc('C', fp);
 13     fclose(fp);
 14     return 0;
 15   }
 16 }
```

<br>

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54549008-74ab3e80-49ec-11e9-805d-fc4ea86c774e.PNG'>
</p>

<br>

### 스트림의 소멸을 요청하는 fclose 함수

- fopen 함수가 스트림을 형성하는 함수라면, fclose 함수는 스트림을 해제하는 함수이다.

- 다시 말해, fopen 함수가 파일을 개방하는 함수라면, fclose 함수는 파일을 닫는 함수이다.

```c
#include <stdio.h>
int fclose(FILE * stream);

    // 성공 시 0, 실패 시 EOF를 반환
```

> **fclose 함수를 통해 개방된 파일을 닫아줘야 하는 이유**

- 운영체제가 할당한 자원의 반횐

- 버퍼링 되었던 데이터의 출력

<br>

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54549407-585bd180-49ed-11e9-96cd-e1a6ea813f35.PNG'>
</p>

<br>

- fclose 함수의 호출을 통해서 파일을 닫아주면 출력 버퍼에 저장되어 있던 데이터가 파일로 이동하면서 출력버퍼는 비워지게 된다. 물론 그 이후엔 컴퓨터의 전원이 꺼져도 파일에 저장된 데이터는 소멸되지 않는다. 때문에 사용이 끝난 파일은 곧바로 fclose 함수를 호출해주는 것이 좋다.

<br>

### Chapter 21에서 호출한 적 있는 fflush 함수

- **스트림을 종료하지 않고 버퍼만 비우고 싶을 때에는 `fflush 함수`를 호출** 하면 된다.

```c
#include <stdio.h>
int fflush(FILE * stream);

    // 함수호출 성공 시 0, 실패 시 EOF 반환
```

> 입출력 버퍼를 비운다는 것

- 출력버퍼를 비운다는 것은 출력버퍼에 저장된 데이터를 목적지로 전송한다느 의미

- 입력버퍼를 비운다는 것은 입력버퍼에 저장된 데이터를 소멸시킨다는 의미

- fflush 함수는 출력버퍼를 비우는 함수이다.

- fflush 함수는 입력버퍼를 대상으로 호출할 수 없다.


```c
int main(void) {
    FILE * fp = fopen("data.txt", "wt");  // 출력 스트림 형성
    . . . .
    fflush(fp);  // 출력 버퍼를 비우라는 요청
    . . . .
}
```

- 이렇듯 `fflush 함수`는 **출력버퍼를 위한 함수**이다.

    - 파일 스트림의 입력버퍼를 비우는 함수는 필요가 없다.

<br>

### 파일로부터 데이터를 읽어보자

> 파일을 열어 안에 저장된 문자를 읽어들이는 예제

- **int ch = fgetc(fp);**

```c
  1 #include <stdio.h>
  2
  3 int main(void) {
  4   int ch, i;
  5   FILE * fp = fopen("data.txt", "rt");
  6   if(fp == NULL) {
  7     puts("파일오픈 실패!");
  8     return -1;
  9   }
 10
 11   for(i = 0; i < 3; i++)
 12   {
 13     ch = fgetc(fp);
 14     printf("%c \n", ch);
 15   }
 16   fclose(fp);
 17   return 0;
 18 }
```

```
A
B
C
```

<br>

## 파일의 개방 모드(Mode)

### 스트림을 구분하는 기준1 : 읽기 위한 스트림? 쓰기 위한 스트림?

- 데이터의 이동방향을 기준으로 다음과 같이 4가지로 구분할 수 있다.
  
    - 데이터 READ 스트림 : 읽기만 가능

    - 데이터 WRITE 스트림 : 쓰기만 가능

    - 데이터 APPEND 스트림 : 쓰되 덧붙여 쓰기 가능

    - 데이터 READ/WRITE 스트림 : 읽기, 쓰기 모두 가능

- 그러나 C언어는 이를 바탕으로 총 6가지로 스트림을 세분화한다.
  
<br>

|모드(mode)|스트림의 성격|파일이 없으면?|
|:---:|---|:---:|
|r|읽기 가능|에러|
|w|쓰기 가능|생성|
|a|파일의 끝에 덧붙여 쓰기 가능|생성|
|r+|읽기/쓰기 가능|에러|
|w+|읽기/쓰기 가능|생성|
|a+|읽기/덧붙여 쓰기 가능|생성|
<p align='center'>
<img src=''>
</p>

- 위 표를 참조하여 필요로 하는 스트림의 특성과 일치하는 `파일의 개방 모드(mode)`를 선택하면 된다.
  
- 그리고 모드의 이름이 fopen 함수의 두 번째 인자가 된다.

<br>

> 수월한 모드 선택을 위한 팁

- 모드의 +는 읽기, 쓰기가 모두 가능한 스트림의 형성을 의미한다.

- 모드의 a는 쓰기가 가능한 스트림을 형성하는데, 여기서 말하는 쓰기는 덧붙이기이다.

<br>

> 왠만하면 `r, w, a` 중에서 선택하자

- 파일의 개방 모드 중 `r+ , w+, a+`는 읽기와 동시에 쓰기가 가능하므로 더 좋은 모드라 생각할 수 있다. 하지만 이러한 모드를 기반으로 작업하는 경우에는 읽기에서 쓰기, 그리고 쓰기에서 읽기로 작업을 변경할 때마다 메모리 버퍼를 비워줘야 하는 등의 불편함과 더불어 잘못된 사용의 위험성도 따른다.

<br>

### 텍스트 파일과 바이너리 파일

- `텍스트 파일(text file)` : 사람이 인식할 수 있는 문자를 담고 있는 파일

- `바이너리 파일(binary file)` : 컴퓨터가 인식할 수 있는 데이터를 담고 있는 파일

데이터의 입출력을 위해서 스트림을 형성할 때 이와 관련해서 특별히 신경 쓸 부분은 무엇일까? 그것은 '문장의 끝을 의미하는 개행의 표현방식'이다.

<br>

### 개행은 `\n`이 아니다?

개행은 일반적인 문자 데이터와 성격이 조금 다르다. 개행은 줄이 바뀌었다는 일종의 현상이지 그 자체가 하나의 데이터로 존재하는 대상은 아니기 때문이다.

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54578681-fed2c180-4a43-11e9-847e-753fa6be9402.jpg'>
</p>

개행의 표시방법이 C언어와 다른 운영체제에서는 개행 정보를 파일에 저장하기 위해 위의 그림과 같은 형태의 변환이 필요하다. 그런데 이러한 형태의 변화을 직접하려니 번거로워 누군가 변환을 대신해줬으면 할때에는 **파일을 텍스트 모드로 개방하면 된다.**

- MS-DOS(Windows)의 파일 내 개행 : `\r\n`
- Mac(Mackintosh)의 파일 내 개행 : `\r`
- Unix 계열의 파일 내 개행 : `\n`

<br>

### 스트림을 구분하는 기준2 : 텍스트 모드와 바이너리 모드

> 바이너리 모드로 파일을 개방하면 아무런 변화도 발생하지 않는다.

> 텍스트 모드로 파일을 개방하면 운영체제에 따른 표현 차로 인한 변환이 발생한다.

<br>

- 파일을 **텍스트 모드로 개방**하면 위에서 말한 형태의 변환이 자동으로 이뤄진다.

- 예로 Windows 기반으로는 다음 두 가지의 변환이 자동으로 이루어진다.
  
    - C 프로그램애서 `\n`을 파일에 저장하면 `\r\n`으로 변환되어 저장됨.
  
    - 파일에 저장된 `\r\n`을 C 프로그램상에서 읽으면 `\n`으로 변환되어 읽혀짐.

- 때문에 직접 개행 문자의 변환을 신경 쓸 필요가 없다. 그저 텍스트 모드로 파일을 개방하고 fopen 함수의 두 번째 인자로 다음 중 하나를 전달하면 된다.
  
    - `rt, wt, at, r+t, w+t, a+t`
  
    - 이는 파일 개방 모드에 **텍스트 모드를 의미하는 `t`** 가 붙은 형태이다.
  
- 반대로 바이너리 데이터를 저장하고 있는 파일의 경우에는 이러한 형태의 변환이 일어나면 안되기 때문에(아무런 변환이 일어나면 안된다) **바이너리 모드**로 파일을 개방해야 한다.

- 이를 위해선 fopen 함수의 두 번째 인자로 다음 중 하나를 전달해야 한다.

    - `rb, wb, ab, r+b, w+b, a+b`

    - 개방 모드에 **바이너리 모드를 의미하는 `b`** 가 붙은 형태이다.

<br>

- 개방 모드에 `t도` `b`도 붙여주지 않으면 파일은 텍스트 모드로 개방된다.
  
- `w+t` , `wt+` 는 동일한 의미이다.
  
  - `t`나 `b`는 개방 모드의 가운데에도 올 수 있고, 또는 끝에도 올 수 있다.

<br>

## 파일 입출력 함수의 기본

### 파일 입출력 함수들

```c
int futc(int c, FILE * stream);  // 문자 출력
int fgetc(FILE * stream);  // 문자 입력
int fputs(const char * s, FILE * stream);  // 문자열 출력 
char * fgets(char *s, int n, FILE * stream);  // 문자열 입력
```

<br>

> 문자와 문자열의 저장

```c
  1 #include <stdio.h>
  2
  3 int main(void) {
  4   FILE * fp = fopen("simple.txt", "wt");  // 개행문자가 있으므로 텍스트 모드로 파일 개방
  5   if(fp == NULL) {
  6     puts("파일 오픈 실패!");
  7     return -1;
  8   }
  9
 10   fputc('A', fp);
 11   fputc('B', fp);
 12   fputs("My name is Hong \n", fp);
 13   fputs("Your name is Yoon \n", fp);
 14   fclose(fp);
 15   return 0;
 16 }
```

```
ABMy name is Hong
Your name is Yoon
```

12, 13행에서 문자열을 출력하고 있는데, 그 문자열에는 개행문자도 포함되어 있으므로 4행에서 보이듯 **반드시 텍스트 모드로 파일을 개방**해야 한다.

<br>

> 위에서 생성된 파일 읽기

```c
  1 #include <stdio.h>
  2
  3 int main(void) {
  4   char str[30];
  5   int ch;
  6   FILE * fp = fopen("siple.txt", "rt");
  7   if(fp == NULL) {
  8     puts("파일 오픈 실패!");
  9     return -1;
 10   }
 11
 12   ch = fgetc(fp);
 13   printf("%c \n", ch);
 14   ch = fgetc(fp);
 15   printf("%c \n", ch);
 16
 17   fgets(str, sizeof(str), fp);
 18   printf("%s", str);
 19   fgets(str, sizeof(str), fp);
 20   printf("%s", str);
 21
 22   fclose(fp);
 23   return 0;
 24 }
```

```
A
B
My name is Hong
Your name is Yoon
```

- 문자열이 파일에 저장될 때에는 문자열의 끝을 의미하는 널 문자는 저장되지 않는다. 때문에 *파일에서는 개행을 기준으로 문자열을 구분* 한다.

- **fgets 함수**의 호출을 통해서 읽어 들일 **문자열의 끝에는 반드시 `\n` 문자가 존재**해야 한다.

<br>

### feof 함수 기반의 파일복사 프로그램

- 때론 파일의 마지막에 저장된 데이터까지 모두 읽어 들여야 하는 상황이 존재한다. 파일의 끝을 확인하는 목적으로 정의된 함수가 바로 `feof 함수`이다.

- 이 함수는 인자로 전달된 파일 구조체의 포인터를 대상으로, 더 이상 읽어 들일 데이터가 존재하지 않으면(파일의 끝까지 모두 읽어 들인 상태이면) 0이 아닌 값을 반환한다.

```c
#include <stdio.h>
int feof(FILE * stream);
    // 파일의 끝에 도달한 경우 0이 아닌 값 반환
```

<br>

> 문자 단위 복사 진행  : `fputc`

```c
  1 #include <stdio.h>
  2
  3 int main(void) {
  4   FILE * src = fopen("src.txt", "rt");
  5   FILE * des = fopen("dst.txt", "wt");
  6   int ch;
  7
  8   if(src == NULL || des == NULL) {
  9     puts("파일 오픈 실패!");
 10     return -1;
 11   }
 12
 13   while((ch = fgetc(src))!=EOF)  // 파일의 내용을 한 문자씩 복사
 14     fputc(ch, des);
 15
 16   if(feof(src) != 0)  // EOF의 반환 원인 확인
 17     puts("파일 복사 완료!");
 18   else
 19     puts("파일 복사 실패!");
 20
 21   fclose(src);
 22   fclose(des);
 23   return 0;
 24 }
```

```
june0122@ubuntu:~/thethelab.io/0319$ vi 3.c
june0122@ubuntu:~/thethelab.io/0319$ gcc 3.c
june0122@ubuntu:~/thethelab.io/0319$ ls
1.c  2.c  3.c  a.out  siple.txt
june0122@ubuntu:~/thethelab.io/0319$ ./a.out
파일 오픈 실패!                                       // src.txt 파일이 없으므로 오류 발생
june0122@ubuntu:~/thethelab.io/0319$ vi src.txt
june0122@ubuntu:~/thethelab.io/0319$ ./a.out
파일 복사 완료!                                       // src.txt 파일이 있으므로 복사 완료
june0122@ubuntu:~/thethelab.io/0319$ ls
1.c  2.c  3.c  a.out  dst.txt  siple.txt  src.txt    // dst.txt 생성 확인
```

```
/* dst.txt 내용 */

  1 안녕하세요~
  2
  3 짱구 할아버지에요.
  4
  5 하나 둘 셋 야!
  6
  7 천방지축 어리둥절
  8
  9 빙글빙글 돌아가는
 10
 11 짱구의 하루
```

- 16행 : fgetc 함수가 EOF를 반환했다고 해서 무조건 파일의 끝에 도달했다고 판단할 수 없다. 오류가 발생하는 경우에도 EOF가 반환되기 때문이다.

    - 따라서 feof 함수의 호출을 통해서 EOF의 반환 원인을 확인할 필요가 있다.


<br>

> 문자열 단위 복사 진행 : `fputs`

```c
  1 #include <stdio.h>
  2
  3 int main(void) {
  4   FILE * src = fopen("src.txt", "rt");
  5   FILE * des = fopen("dst.txt", "wt");
  6   char str[20];  // 문자 단위 복사일 때는  int ch;
  7
  8   if(src == NULL || des == NULL) {
  9     puts("파일 오픈 실패!");
 10     return -1;
 11   }
 12
 13   /* fgetc 함수와 달리 fgets 함수는 EOF 대신 NULL을 반환 */
 14
 15   while(fgets(str, sizeof(str), src) != NULL)  // 파일의 내용을 문자열 단위로 복사
 16     fputs(str, des);
 17
 18   if(feof(src) != 0)  // EOF의 반환 원인 확인
 19     puts("파일 복사 완료!");
 20   else
 21     puts("파일 복사 실패!");
 22
 23   fclose(src);
 24   fclose(des);
 25   return 0;
 26 }
```

```
/* dst.txt 내용 */

  1 안녕하세요~
  2
  3 짱구 할아버지에요.
  4
  5 하나 둘 셋 야!
  6
  7 천방지축 어리둥절
  8
  9 빙글빙글 돌아가는
 10
 11 짱구의 하루
```

<br>

### '바이너리' 데이터의 입출력 : fread, fwrite

앞의 입출력 함수들은 텍스트 데이터의 입출력을 진행하는 함수들이었다면, 이번에 설명하는 함수들은 **바이너리 데이터의 입출력을 진행하는 함수** 들이다.

```c
#include <stdio.h>
size_t fread(void * buffer, size_t size, size_t count, FILE * stream);
    
    // 성공 시 전달인자 count, 실패 또는 파일의 끝 도달 시 count보다 작은 값 반환
```

위 함수는 다음과 같이 호출이 된다.

```c
int buf[12];
size_t fread((void*)buf, sizeof(int), 12, fp);  // fp는 FILE 구조체 포인터
```

> sizeof(int) 크기의 데이터 12개를 fp로부터 읽어 들여서 배열 buf에 저장하라

- 즉 fread 함수는 두 번째 전달인자와 세 번째 전달인자의 곱의 바이트 크기만큼 데이터를 읽어들이는 함수이다.

- 따라서 위의 fread 함수호출을 통해서 int형 데이터 12개를 fp로부터 읽어서 배열 buf에 저장하게 된다.

  - 그리고 fread 함수는 읽어 들인 데이터의 갯수를 반환하는데(읽어 들인 바이트 수가 아니라 갯수이다), 위 문장은 sizeof(int)의 크기의 데이터를 12개 읽어 들이는 경우이니, **함수의 호출이 성공을 하고 요청한 분량의 데이터가 모두 읽혀지면 12가 반환된다.**

  - 반면 함수의 호출이 성공을 했지만 파일 끝에 도달해서 **12개를 모두 읽어 들이지 못했거나 오류가 발생하는 경우에는 12보다 작은 값이 반환된다.**

- 인수 buf는 파일에 기록할 데이터가 저장되어 있는 메모리 영역에 대한 포인터이다. 포인터의 형은 void이므로 어떤 데이터형에 대해서든 포인터가 될 수 있다.

```c
#include <stdio.h>
size_t fwrite(const void * buffer, size_t size, size_t count, FILE * stream);

    // 성공 시 전달인자 count, 실해 시 count보다 작은 값 반환
```
위 함수는 다음과 같이 호출을 한다.

```c
int buf[7] = { 1, 2, 3, 4, 5, 6, 7};
fwrite((void*)buf, sizeof(int), 7 ,fp);
```

> sizeof(int)의 크기의 데이터 7개를 buf로부터 읽어서 fp에 저장해라

<br>

> fread, fwrite 함수의 이해

```c
  1 #include <stdio.h>
  2
  3 int main(void) {
  4   FILE * src = fopen("src.bin", "rb");
  5   FILE * des = fopen("dst.bin", "wb");
  6   char buf[20];
  7   int readCnt;
  8
  9   if(src == NULL || des == NULL) {
 10     puts("파일 오픈 실패!");
 11     return -1;
 12   }
 13
 14   while(1) {
 15     readCnt = fread((void*)buf, 1, sizeof(buf), src);
 16
 17     if(readCnt < sizeof(buf)) {
 18       if(feof(src) != 0) {
 19         fwrite((void*)buf, 1, readCnt, des);
 20         puts("파일 복사 완료");
 21         break;
 22       }
 23       else
 24         puts("파일 복사 실패");
 25
 26       break;
 27     }
 28     fwrite((void*)buf, 1, sizeof(buf), des);
 29   }
 30
 31   fclose(src);
 32   fclose(des);
 33   return 0;
 34 }
```

- 15행 : fread 함수를 이용해서 파일로부터 데이터를 읽고 있다. 두 번째 전달인자가 1, 세 번째 전달인자가 sizeof(buf)이니, 읽어 들이는 데이터의 크기는 총 1 × sizeof(buf) 바이트가 된다.

- 17행 : 이 if문은 fread 함수가 sizeof(buf)의 반환 값보다 작은 값을 반환했을 때 '참'이 된다. 그런데 이는 오류가 발생했거나 파일의 끝에 도달한 상황이니 feof 함수의 호출을 통해서 어떠한 상황인지를 판단해야 한다.

- 18~22행 : 파일의 끝에 도달해서 sizeof(buf)의 반환 값보다 적은 수의 바이트가 읽혀졌을 때 실행되는 영역이다. 비록 적은 수의 바이트가 읽혀졌다 할지라도 이 역시 파일의 일부이므로 fwrite 함수호출을 통해서 복사를 해야 한다. 그래서 19행에서 마지막 데이터를 출력하고 있다.

- 28행 : 15행에서 읽어 들인, 배열 buf를 꽉 채운 데이터를 그대로 파일에 저장하고 있다.

<br>

## 텍스트 데이터와 바이너리 데이터를 동시에 입출력 하기

### 서식에 따른 입출력 데이터 : fprintf, fscanf

- fprintf와 fscanf 함수는 printf, scanf 함수와 유사하다. 다만 입출력의 대상이 콘솔이 아닌 파일이라는 점에서 차이가 있다.

> fprintf 함수의 호출방법

```c
char name[10] = "홍길동";   // 텍스트 데이터
char sex = 'M';     // 텍스트 데이터
int age = 24;       // 바이너리 데이터
fprintf(fp, "%s %c %d", name, sex, age);    // fp는 FILE 구조체 포인터
```

```
홍길동 M 24
```

위의 fprintf 함수의 호출문이 printf 함수와 차이를 보이는 부분은 FILE 구조체의 포인터가 첫 번째 전달인자라는 점이다. 그래서 printf 함수와 달리 fprintf 함수는 첫 번재 인자로 전달된 FILE 구조체의 포인터가 지칭하는 파일로 출력이 이뤄진다.

- 결국 텍스트 데이터와 바이너리 데이터를 하나의 문자열로 묶어서 저장하는 셈이다.

<br>

> fscanf 함수의 호출문

```c
char name[10];
char sex;
int age;
fscanf(fp, "%s %c %d", name, &sex, &age);
```

- 첫 번째 인자로 전달된 포인터가 지칭하는 파일로 데이터를 읽어 들인다.

- fscanf 함수는 파일의 끝에 도달하거나 오류가 발생하면 EOF를 반환한다.

<br>

### 텍스트와 바이너리 데이터의 집합체인 `구조체 변수`의 입출력

- 앞서 보인 예제에서는 다음의 데이터들을 대상으로 파일 입출력을 진행하였다.

```c
char namep[10], char sex, int age
```

- 그런데 실제 프로그램에서는 이들을 다음과 같이 **구조체로 묶어서 정의하는 것이 보통**이다.

```c
typedef struct fren {
    char name[10];
    char sex;
    int age;
} Friend;
```
- 어떻게 하면 구조체 변수를 통째로 저장하고 통째로 읽어 들일 수 있겠는가?
    
    - ***구조체 변수를 하나의 바이너리 데이터로 인식*** 하고 처리하면 가능하다.

<br>

> 구조체 변수의 입출력 예제

```c
  1 #include <stdio.h>
  2
  3 typedef struct fren {
  4   char name[10];
  5   char sex;
  6   int age;
  7 } Friend;
  8
  9 int main(void) {
 10   FILE * fp;
 11   Friend myfren1;
 12   Friend myfren2;
 13
 14   /*** file write  ***/
 15   fp = fopen("friend.bin", "wb");
 16   printf("이름, 성별, 나이 순 입력: ");
 17   scanf("%s %c %d", myfren1.name, &(myfren1.sex), &(myfren1.age));
 18   fwrite((void*)&myfren1, sizeof(myfren1), 1, fp);
 19   fclose(fp);
 20
 21   /*** file read  ***/
 22   fp = fopen("friend.bin", "rb");
 23   fread((void*)&myfren2, sizeof(myfren2), 1, fp);
 24   printf("%s %c %d \n", myfren2.name, myfren2.sex, myfren2.age);
 25   fclose(fp);
 26   return 0;
 27 }
```

```
이름, 성별, 나이 순 입력: JAMES M 56
JAMES M 56
```


<br>

## 임의 접근을 위한 '파일 위치 지시자'의 이동



<p align='center'>
<img src=''>
</p>

<p align='center'>
<img src=''>
</p>


<p align='center'>
<img src=''>
</p>

