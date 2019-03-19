# Chapter21. 문자와 문자열 관련 함수

### 스트림과 데이터의 이동

> 데이터의 이동수단이 되는 스트림

프로그램상에서 모니터로 문자열을 출력할 수 있는 것은 printf 함수가 존재하기 때문인데 *printf 함수호출 시 어떠한 경로 및 과정을 거쳐서 문자열이 출력될까?*

프로그램과 모니터, 그리고 프로그램과 키보드는 기본적으로 연결되어 있는 개체가 아닌, 서로 떨어져 있는 개체이다. 따라서 프로그램상서 모니터와 키보드를 대상으로 데이터를 입출력하기 위해서는 이들을 연결시켜주는 다리가 필요하다. 이러한 다리의 역할을 하는 매개체를 가리켜 `스트림(stream) : 한 방향으로 흐르는 데이터의 흐름`이라 한다.

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54111682-d1748b00-4427-11e9-8731-a572bbf33757.png'>
</p>

`스트림`은 운영체제에서 제공하는 소프트웨어적인(소프트웨어로 구현되어 있는) 가상의 다리이다. 다시 말해, 운영체제는 외부장치와 프로그램과의 데이터 송수신의 도구가 되는 스트림을 제공하고 있다.

<br>

> 스트림의 생성과 소멸

**콘솔 입출력을 위한 '입력 스트림'과 '출력 스트림'은 프로그램이 실행되면 자동으로 생성이 되고, 프로그램이 종료되면 자동으로 소멸되는 스트림이다.** 즉, 이 둘은 기본적으로 제공되는 `표준 스트림(standard stream)`이다. 그리고 표준 스트림에는 '에러 스트림'도 존재하며 이들 각각에는 다음과 같이 stdin, stdout, stderr라는 이름이 붙어있다.

- `stdin` : 표준 입력 스트림 - 키보드 대상으로 입력

- `stdout` : 표준 출력 스트림 - 모니터 대상으로 출력

- `stderr` : 표준 에러 스트림 - 모니터 대상으로 출력

<br>

### 문자 단위 입출력 함수

> 문자 출력 함수: `putchar` , `fputc`

```c
#include <stdio.h>

int putchar(int c);
int fputc(int c, FILE * stream);

// 함수호출 성공 씨 쓰여진 문자정보가, 실패 시 EOF 반환
```

- `putchar` : 인자로 전달된 문자정보를 stdout으로 표현되는 표준 출력 스트림으로 전송하는 함수. 인자로 전달된 문자를 모니터로 출력하는 함수.

- `fputc` : putchar 함수와 동일하나, **fputc 함수는 문자를 전송할 스트림을 지정할 수 있다.** 즉 fputc 함수를 이용하면 stdout 뿐만 아니라, *파일을 대상으로도* 데이터를 전송할 수 있다. fputc 함수의 두 번째 매개변수 stream은 문자를 출력할 스트림의 지정에 사용된다. 따라서 이 인자에 표준 출력 스트림을 의미하는 stdout을 전달하면, putchar 함수와 동일한 함수가 된다.

<br>

> 문자 입력 함수 : `getchar` , `fgetc`

```c
#include <stdio.h>
int getchar(void);
int fgetc(FILE * stream);

// 파일의 끝에 도달하거나 함수호출 실패 시 EOF 반환
```

getchar 함수는 stdin으로 표현되는 표준 입력 스트림으로부터 하나의 문자를 입력 받아서 반환하는 함수이다. 따라서 키보드로부터 하나의 문자를 입력 받는 함수라 할 수 있다. 그리고 fgetc 함수도 하나의 문자를 입력 받는 함수이다. 다만 getchar 함수와 달리 문자를 입력 받을 스트림을 지정할 수 있다.

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int ch1, ch2;
6
7   ch1=getchar();  // 문자 입력
8   ch2=fgetc(stdin);  // 엔터 키 입력
9
10   putchar(ch1);  // 문자 출력
11   fputc(ch2, stdout);  // 엔터 키 출력
12   return 0;
13 }
```

소스코드상에서는 두 개의 문자를 입출력하고 있지만 실행결과만 놓고보면 하나의 문자가 입력되고 출력된 것처럼 보인다. 사실 두 개의 문자가 입출력되고 있지만 **두 번째 글자가 '엔터 키'이다보니 눈에 띄지 않았을 뿐** 이다. '엔터 키'도 아스키 코드 값이 10인 '＼n'으로 표현되는 문자이다. 따라서 입출력의 대상이 되는 것은 당연하다.

*그런데 위 예제에서 문자를 int형 변수에 저장하는 이유는 무엇일까?*

언뜻 생각하기엔 위 예제의 두 변수 ch1, ch2는 char형으로 선언되어야 할 것 같지만, int형으로 선언되어야 한다. **getchar 함수와 fgetc 함수의 반환형이 int이기 때문이다.**

<br>

> 문자 입출력에서의 **EOF**

`EOF`는 *End Of File* 의 약자로서, 파일의 끝을 표현하기 위해서 정의해놓은 상수이다.

키보드를 대상으로 하는 fgetc 함수와 getchar 함수는 언제 EOF를 반환할까? 이는 다음 두 가지 경우 중 하나가 만족되었을 때이다.

- 함수호출의 실패

- Windows에서 `CTRL+Z` 키, Linux에서 `CTRL+D` 키가 입력되는 경우

```c
1 #include <stdio.h>
2
3 int main(void)
4 {
5   int ch;
6
7   while(1)
8   {
9     ch=getchar();
10     if(ch==EOF)
11       break;
12     putchar(ch);
13   }
14   return 0;
15 }
```

<br>

> 반환형이 int이고, int형 변수에 문자를 담는 이유는?

```c
int getchar(void);
int fgetc(FILE * stream);
```

두 함수에서 반환되는 것은 1바이트 크기의 문자인데, 반환형이 int이다. 이유가 무엇일까?

> *char형은 다른 정수 자료형들과 달리 signed char와 다른 선언일 수도 있다. **char을 unsigned char로 처리하는 컴파일러도 존재**하기 때문이다. 이런 이유로 char형 변수를 선언해서 음의 정수를 저장하는 경우에는 signed 선언을 추가하기도 한다.*

그런데 위의 두 함수가 반환하는 값 중 하나인 ***EOF는 -1로 정의된 상수*** 이다. 따라서 반환형이 char형이라면, 그리고 char를 unsigned char로 처리하는 컴파일러에 의해서 컴파일이 되었다면, EOF는 반환의 과정에서 엉뚱한 양의 정수로 형 변환이 되어버리고 만다. 그래서 **어떠한 상황에서라도 -1을 인식할 수 있는 int형으로 반환형을 정의해 놓은 것** 이다. 물론 반환되는 값을 그대로 유지하기 위해서 우리도 int형 변수에 반환 값을 저장해야 한다. 앞서 예제를 통해서 보였듯이 말이다.

<br>

| 입력 받은 문자, 소문자 ↔ 대문자 변환 출력 예제

```c
1 #include <stdio.h>
2
3 int ConvCase(int ch)
4 {
5   int diff = 'a'- 'A';  // 모든 문자의 대소문자간 차의 크기는 32로 같다
6   // 아스키 값 : A~Z (65~90), a~z(97~122)
7   if(ch>='A' && ch<='Z')
8     return ch+diff;
9   else if (ch>='a' && ch<='z')
10     return ch-diff;
11   else
12     return -1;
13 }
14
15 int main(void)
16 {
17   int ch;
18   printf("문자 입력: ");
19   ch = getchar();  // 문자 입력
20   ch = ConvCase(ch);  // 문자 변환
21   if(ch==-1)
22   {
23     puts("범위를 벗어난 입력입니다.");
24     return -1;
25   }
26   putchar(ch);  // 변환된 문자 출력
27   return 0;
28 }
```

<br>

### 문자열 단위 입출력 함수

printf 함수와 scanf 함수를 이용해도 문자열의 입출력이 가능하다. 그러나 `puts, fputs` , `gets, fgets` 함수는 그 성격이 제법 다르다.

scanf 함수는 공백이 포함된 형태의 문자열을 입력 받는데 제한이 있었던 반면, `puts, fputs` 입력 함수는 공백을 포함하는 문자열도 입력 받을 수 있다.

<br>

> 문자열 출력 함수: `puts` , `fputs`

```c
#include <stdio.h>
int puts(const char * s);
int fputs(const char * s, FILE * stream);

// 성공 시 음수가 아닌 값을, 실패 시 EOF 반환
```

`puts` 함수는 **출력의 대상이 stdout으로 결정** 되어 있지만, `fputs` 함수는 두 번째 인자를 통해서 출력의 대상을 결정할 수 있다.

그리고 둘 다 첫 번째 인자로 전달되는 주소 값의 문자열을 출력하지만, 출력의 형태에 있어 한가지 차이점이 있다.

```c
  1 #include <stdio.h>
  2
  3 int main(void)
  4 {
  5   char * str="Simple String";
  6
  7   printf("1. puts test ------ \n");
  8   puts(str);
  9   puts("So Simple String");
 10
 11   printf("2. fputs test ------ \n");
 12   fputs(str, stdout); printf("\n");
 13   fputs("So Simple String", stdout); printf("\n");
 14
 15   printf("3. end of main ---- \n");
 16   return 0;
 17 }
```

```
1. puts test ------
Simple String
So Simple String
2. fputs test ------
Simple String
So Simple String
3. end of main ----
```

**puts 함수가 호출되면 문자열 출력 후 자동으로 개행이 이뤄지지만, fputs 함수가 호출되면 문자열 출력 후 자동으로 개행이 이뤄지지 않는다.**

<br>

> 문자열 입력 함수 : `gets` , `fgets`

```c
#include <stdio.h>
char * gets(char * s);
char * fgets(char * s, int n, FILE * stream);

// 파일의 끝에 도달하거나 함수호출 실패 시 NULL 포인터 반환
```

| `gets` 함수의 호출

```c
int main(void)
{
  char str[7];  // 7바이트의 메모리 공간 할당
  gets(str);  // 입력 받은 문자열을 배열 str에 저장
  . . . .
}
```

`gets` 함수의 문장구성은 간단하지만, 미리 마련해 놓은 배열을 넘어서는 길이의 문자열이 입력되면, 할당 받지 않은 메모리 공간을 침범하여 실행 중 오류가 발생하는 단점이 있다. 그래서 가급적이면 다음의 형태로 `fgets` 함수를 호출한다.

<br>

| `fgets` 함수의 호출

*stdin으로부터 문자열을 입력 받아서 배열 str에 저장하되, sizeof(str)의 길이만큼만 저장하라*

```c
int main(void)
{
  char str[7];
  fgets(str, sizeof(str), stdin);  // stdin으로부터 문자열 입력 받아서 str에 저장
  . . . .
}
```

`fgets` 함수가 호출되면 `sizeof(str)`의 반환 값인 7보다 하나가 작은 6에 해당하는 길이의 문자열만 읽어서 str에 저장하게 된다. 아무리 공간이 부족해도 널 문자가 삽입되지 않으면 문자열이라 할 수 없다. 그래서 **문자열을 입력 받으면 문자열의 끝에 자동으로 널 문자가 추가된다.** 따라서 하나가 작은 길이의 문자열이 저장되는 것이다.

```c
  1 #include <stdio.h>
  2
  3 int main(void)
  4 {
  5   char str[7];
  6   int i;
  7
  8   for(i=0; i<3; i++)
  9   {
 10     fgets(str, sizeof(str), stdin);
 11     printf("Read %d: %s \n", i+1, str);
 12   }
 13   return 0;
 14 }
```

```
12345678901234567890
Read 1: 123456
Read 2: 789012
Read 3: 345678
```

```
we
Read 1: we

like
Read 2: like

you
Read 3: you

```

```
Y & I
Read 1: Y & I

ha ha
Read 2: ha ha

^^ --
Read 3: ^^ --

```

`fgets` 함수는 `\n` 을 만날 때까지 문자열을 읽어 들이는데, 중간에 삽입된 **공백문자도 문자열의 일부** 로 읽어 들이고, **\n을 제외시키거나 버리지 않고 문자열의 일부로 받아들인다.** 

<br>

### 표준 입출력과 버퍼

> 표준 입출력 기반의 버퍼

ANSI C의 표준에서 정의된 printf와 scanf 그리고 fputc, fgetc 등과 같은 입출력 함수들을 가리켜 `표준 입출력 함수`라 한다. 이러한 표준 입출력 함수를 통해서 데이터를 입출력 하는 경우, 해당 데이터들은 **운영체제가 제공하는 `메모리 버퍼 : 데이터를 임시로 저장하는 메모리 공간`를 중간에 통과** 하게 된다.

<br>

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54168581-979c9680-44b2-11e9-88e1-eb58169cbb39.jpg'>
</p>

</br>

> `버퍼링(Buffering)`을 하는 이유는 무엇인가?

데이터를 목적지로 바로 전송하지 않고 중간에 버퍼를 둬서 전송하려는 데이터를 임시 저장하는 `데이터 버퍼링`의 가장 큰 이유는 *데이터 전송의 효율성* 과 관련이 있다. 버퍼링 없이 키보드가 눌릴 때마다 눌린 문자의 정보를 목적지로 바로 이동시키는 것보다 중간에 메모리 버퍼를 둬서 데이터를 한데 묶어서 이동시키는 것이 보다 효율적이고 빠르다.

<br>

> 출력버퍼를 비우는 `fflush 함수`

출력버퍼가 비워진다는 것은 **출력버퍼에서 저장된 데이터가 버퍼를 떠나서 목적지로 이동됨**을 뜻한다. 그런데 시스템과 버퍼의 성격에 따라 출력버퍼가 비워지는 시점이 다르므로 다음 함수를 알아둘 필요가 있다.

```c
#include <stdio.h>
int fflush(FILE * stream);

// 함수호출 성공 시 0, 실패 시 EOF 반환
```
<br>

<p align='center'>
`fflush(stdout);` : 표준 출력버퍼를 비워라!
</p>

<br>

fflush 함수는 인자로 전달된 스트림의 버퍼를 비우는 기능을 제공한다. 어떠한 시스템의 어떠한 표준 출력버퍼라 할지라도 버퍼에 저장된 내용이 비워지면서 데이터가 목적지로 이동한다.

<br>

> 입력버퍼는 어떻게 비울까?

- `출력버퍼의 비워짐` : 데이터가 목적지로 **전송** 됨
- `입력버퍼의 비워짐` : 데이터의 **소멸**

<br>

<p align='center'>
`fflush(stdin);` : 어떠한 의미로 해석이 될까?
</p>

<br>

fflush 함수는 출력버퍼를 대상으로 호출하는 함수이며, C언어의 표준에서는 위의 결과에 대해 정의하고 있지 않다. 따라서 위의 함수호출 결과는 예측이 불가능하다. Windows 계열과 같은 일부 컴파일러는 위의 형태로 함수가 호출되면 입력버퍼를 비워주기도 한다.

<br>

| 입력버퍼에 저장된 불필요한 데이터의 소멸

```c
  1 #include <stdio.h>
  2
  3 int main(void)
  4 {
  5   char perID[7];
  6   char name[10];
  7
  8   fputs("주민번호 앞 6자리 입력: ", stdout);
  9   fgets(perID, sizeof(perID), stdin);  // decay 적용의 예외
     : sizeof 연산자
 10
 11   fputs("이름 입력: ", stdout);
 12   fgets(name, sizeof(name), stdin);
 13
 14   printf("주민번호: %s \n", perID);
 15   printf("이름: %s \n", name);
 16   return 0;
 17 }
```

```
주민번호 앞 6자리 입력: 950122
이름 입력: 주민번호: 950122
이름:                           // 입력 기회 얻지 못함
```

<br>

분명히 6자리만 입력을 했는데도 이름을 입력할 기회를 얻지 못한 문제가 생겼다.

`950915\n`

*이렇듯 엔터 키를 포함하여 총 7문자가 입력되었다. 그런데 9행의 fgets 함수의 인자로 7이 전달되었으니, 널 문자를 제외하고 최대 6문자를 읽어 들인다. 따라서 \n을 제외한 나머지 여섯 문자만 읽혀지고 **\n은 입력버퍼에 남아있게 된다.** 그리고 이어서 12행의 fgets 함수가 호출된다. 그런데 **fgets 함수는 \n을 만날 때까지 읽어 들이는 함수이니, 버퍼에 남아있는 \n만 읽어버리고 만다.** 때문에 위와 같은 실행결과를 보이는 것이다. 이러한 문제를 해결하기 위해서는 예제 실행 중간에, 입력버퍼에 남아있는 \n 문자 하나만 지워버리면 된다.*

하지만 주민번호 앞 6자리만이 아닌, 주민번호 전체를 입력하는 등 명시하지 않는대로 행동하는 사용자를 고려한다면, 주민번호 앞 6자리를 제외한 나머지 문자들을 입력버퍼에서 지워줘야 한다. 아래가 바로 위의 예제가 정상적으로 작동하는데 필요한 함수이다.

```c
void ClearLineFromReadBuffer(void)
{
  while(getchar()!='\n');
}
```

입력버퍼에 저장된 문자들은 읽어 들이면 지워진다. 그래서 \n을 만날 때까지 문자를 읽어 들이는 함수를 정의하였다. 그럼 다시 예제를 작성해보자.

```c
  1 #include <stdio.h>
  2
  3 void ClearLineFromReadBuffer(void)
  4 {
  5   while(getchar()!='\n');
  6 }
  7
  8 int main(void)
  9 {
 10   char perID[7];
 11   char name[10];
 12
 13   fputs("주민번호 앞 6자리 입력: ", stdout);
 14   fgets(perID, sizeof(perID), stdin);
 15   ClearLineFromReadBuffer();  // 입력버퍼 비우기
 16
 17   fputs("이름 입력: ", stdout);
 18   fgets(name, sizeof(name), stdin);
 19
 20   printf("주민번호: %s \n", perID);
 21   printf("이름: %s \n", name);
 22   return 0;
 23 }
```

```
주민번호 앞 6자리 입력: 950122
이름 입력: 임준섭
주민번호: 950122
이름: 임준섭
```

```
주민번호 앞 6자리 입력: 950122-1234567
이름 입력: 임준섭
주민번호: 950122
이름: 임준섭
```

<br>

### 입출력 이외의 문자열 관련 함수

헤더파일 `string.h`에 선언된 문자열 관련 함수들 중 사용 빈도수가 높은 몇몇 함수를 알아본다.

<br>

> 문자열의 길이를 반환하는 함수 : `strlen`

```c
#include <string.h>
size_t strlen(const char * s);

// 전달된 문자열의 길이를 반환하되, 널 문자는 길이에 포함되지 않는다.
```

위 함수의 반환형 size_t는 일반적으로 다음과 같이 선언되어 있다.

```c
typedef unsigned int size_t;  // unsigned int의 선언을 size_t로 대신할 수 있다.
```

즉, 위의 선언으로 size_t가 unsigned int를 대신할 수 있게 된 것이다. 따라서 다음 두 선언은 완전히 동일하다.

```c
size_t len;
unsigned int len;
```

<br>

| `strlen` 함수의 호출방법

```c
int main(void)
{
  char str[]="1234567";
  printf("%u \n", strlen(str));  // 문자열의 길이 7이 출력
}
```

**참고로 `strlen` 함수의 반환형은 `size_t`이니. 이 함수의 반환 값을 `unsigned int`형 변수에 저장하고 서식문자 `%u` 로 출력하는 것이 정확하다.**

하지만 문자열이 아무리 길어도 문자열의 길이정보는 `int`형 변수에 저장이 가능하기 때문에, `strlen` 함수의 반환 값을 `int`형 변수에 저장하고 서식문자 `%d` 로 출력하는 것도 가능할 뿐만 아니라 이것이 더 일반적이다.

<br>

| `fgets` 함수호출을 통해서 문자열을 입력 받을 때, 같이 딸려 들어오는 `\n` 문자는 문자열에서 제외시키기 

```c
  1 #include <stdio.h>
  2 #include <string.h>
  3
  4 void RemoveBSN(char str[])
  5 {
  6   int len=strlen(str);
  7   str[len-1]=0;
  8 }
  9
 10 int main(void)
 11 {
 12   char str[100];
 13   printf("문자열 입력: ");
 14   fgets(str, sizeof(str), stdin);
 15   printf("길이: %d, 내용: %s \n", strlen(str), str);
 16
 17   RemoveBSN(str);
 18   printf("길이: %d, 내용: %s \n", strlen(str), str);
 19   return 0;
 20 }
```

```
문자열 입력: Good Morning
길이: 13, 내용: Good Morning

길이: 12, 내용: Good Morning
```

<br>

15행을 통한 출력에서는 개행이 두 번, 18행에서는 개행이 한 번 이뤄졌다. 이는 **`RemoveBSN` 함수호출
을 통해서 `\n` 문자가 소멸되었기 때문이다.**

<br>

> 문자열을 복사하는 함수를: `strcpy` , `strncpy`

```c
#include <string.h>
char * strcpy(char * dest, const char * src);
char * strncpy(char * dest, const char * src, size_t n);

// 복사된 문자열의 주소 값 반환
```

| `strcpy` 함수

```c
int main(void)
{
  char str1[30]="Simple String";
  char str2[30];
  strcpy(str2, str1);  // str1의 문자열을 str2에 복사
}
```

<br>

| `strncpy` 함수

```c
int main(void)
{
  char str1[30]="Simple String";
  char str2[30];
  strncpy(str2, str1, sizeof(str2));
  . . . .
}
```

위의 `strncpy` 함수 호출문이 의미하는 바는 str1에 저장된 문자열을 str2에 복사하되, str1의 길이가 매우 길다면, sizeof(str2)가 반환한 값에 해당하는 문자의 수 만큼만 복사를 진행한다.

<br>

> 문자열을 덧붙이는 함수들 : `strcat` , `strncat`

```c
#include <string.h>
char * strcat(char * dest, const char * src);
char * strncat(char * dest, const char * src, size_t n);

// 덧붙여진 문자열의 주소 값 반환
```

<br>

| `strcat` 함수 호출

```c
int main(void)
{
  char str1[30]="First~";
  char str2[30]="Second";
  strcat(str1, str2);  // str1의 문자열 뒤에 str2를 복사 -> First~Second
  . . . .
}
```

`덧붙임이 시작되는 위치`는 널 문자 다음이 아닌, **널 문자가 저장된 위치에서부터**이다.

<br>

| `strncat` 함수의 호출

```c
strnat(str1, str2, 8);  // str의 문자열 중 최대 8개를 str1의 뒤에 덧붙여라.
```

즉 str2의 길이가 8을 넘어선다면 8개의 문자까지만 str1에 덧붙이라는 의미인데, **이 8개의 문자에는 널 문자가 포함되지 않는 사실에 주목**하자. 따라서 **널 문자를 포함하여 실제로는 총 9개의 문자가 str1에 덧붙여진다.** 이렇듯 `strncpy` 함수와 달리 `strncat` 함수는 문자열의 끝에 널 문자를 자동으로 삽입해준다.

<br>

> 문자열을 비교하는 함수들: `strcmp` , `strncmp`

먼저 문자열 비교와 관련해 많이들 착각하는 코드를 보도록 하자.

```c
  1 #include <stdio.h>
  2
  3 int main(void)
  4 {
  5   char str1[] = "My String";
  6   char str2[] = "My String";
  7   if(str1==str2)  // 주의해야 하는 부분
  8     puts("equal");
  9   else
 10     puts("not equal");
 11   return 0;
 12 }

 // 결과는 not equal
```

위의 코드는 문자열 str1과 str2의 내용을 비교하는 것이 아니라 배열 str1과 str2의 **주소 값을 비교하는 것이다.** 배열 이름은 배열의 주소 값을 의미하므로 이는 배열의 주소 값 비교로 이어지고, 때문에 위 예제의 실행결과로는 결코 문자열 "equal"가 출력되지 않는다.

```c
#include <string.h>
int strcmp(const char * s1, const char * s2);
int strncmp(const char * s1, const char * s2, size_t n);

// 두 문자열의 내용이 같으면 0, 같지 않으면 0이 아닌 값 반환
```

위의 두 함수 모두 인자로 전달된 두 문자열의 내용을 비교하여 다음의 결과를 반환한다. 단, `strncmp` 함수를 호출하면 앞에서부터 시작해서 중간부분까지 부분적으로만 문자열을 비교할 수 있다.

- s1이 더 크면 0보다 큰 값 반환

- s2가 더 크면 0보다 작은 값 반환

- s1과 s2의 내용이 모두 같으면 0 반환

여기서 말하는 문자열의 크고 작음은 **아스키 코드 값을 기준으로 결정**된다. 일반적으로 `strcmp` 함수를 호출할 때에는 다음 두 가지 사실에만 근거하여 코드를 작성한다. 사전편찬 순서를 기준으로 앞에 위치하는 문자열이 작은 문자열이고, 뒤에 위치하는 문자열이 큰 문자열이다.

```
0이 반환되면 동일한 문자열, 0이 아닌 값이 반환되면 동일하지 않은 문자열
```

<br>

| `strcmp` 함수와 `strncmp` 함수의 호출 예

```c
  1 #include <stdio.h>
  2 #include <string.h>
  3
  4 int main(void)
  5 {
  6   char str1[20];
  7   char str2[20];
  8   printf("문자열 입력 1: ");
  9   scanf("%s", str1);
 10   printf("문자열 입력 2: ");
 11   scanf("%s", str2);
 12
 13   if(!strcmp(str1, str2))
 14   {
 15     puts("두 문자열은 완벽히 동일합니다.");
 16   }
 17   else
 18   {
 19     puts("두 문자열은 동일하지 않습니다.");
 20
 21     if(!strncmp(str1, str2, 3))
 22       puts("그러나 앞 세 글자는 동일합니다.");
 23   }
 24   return 0;
 25 }
```

```
문자열 입력 1: Simple
문자열 입력 2: Simon
두 문자열은 동일하지 않습니다.
그러나 앞 세 글자는 동일합니다.
```

<br>

> 그 이외의 변환 함수들 : `ASCII TO ~` 함수들

헤더파일 `<stdlib.h>`에 선언된 함수들이다. 문자열로 표현된 정수나 실수의 값을 해당 정수나 실수의 데이터로 변환해야 하는 경우가 간혹 있다. 문자열 "123"을 정수 123으로 변환하거나 문자열 "7.15"를 실수 7.15로 변환해야 하는 경우에 유용하다.

- `int atoi(const char * str);` : 문자열의 내용을 `int`형으로 변환
  
- `long atoi(const char * str);` : 문자열의 내용을 `long`형으로 변환

- `double atof(const char * str);` : 문자열의 내용을 `double`형으로 변환

<br>

```c
  1 #include <stdio.h>
  2 #include <stdlib.h>
  3
  4 int main(void)
  5 {
  6   char str[20];
  7   printf("정수 입력: ");
  8   scanf("%s", str);
  9   printf("%d \n", atoi(str));
 10
 11   printf("실수 입력: ");
 12   scanf("%s", str);
 13   printf("%g \n", atof(str));
 14   return 0;
 15 }
```

```
정수 입력: 15
15
실수 입력: 12.456
12.456
```

<br>

> 문자열 처리 예제

| 문자열 안에 존재하는 숫자의 총합 계산 후 출력

```c
  1 #include <stdio.h>
  2
  3 int ConvToInt(char c)
  4 {
  5   static int diff = 1-'1';
  6   return c + diff;
  7 }
  8
  9 int main(void)
 10 {
 11   char str[50];
 12   int len, i;
 13   int sum = 0;
 14
 15   printf("문자열 입력: ");
 16   fgets(str, sizeof(str), stdin);
 17   len = strlen(str);
 18
 19   for(i = 0; i < len; i++)
 20   {
 21     if('1' <= str[i] && str[i] <= '9')
 22       sum += ConvToInt(str[i]);
 23   }
 24   printf("숫자의 총 합: %d \n", sum);
 25   return 0;
 26 }
```

```
문자열 입력: A15#43
숫자의 총 합: 13
```

<br>

| str1, str2를 fgets 함수를 통해 입력받아 str1의 문자열을 str3에 복사, str2의 문자열을 str3 문자열 뒤에 덧붙인다. 마지막으로 str3의 문자열을 출력한다. 단, str1과 str2 문자열에서 \n은 소멸시켜야 한다.

```c
  1 #include <stdio.h>
  2 #include <string.h>
  3
  4 int main(void)
  5 {
  6   char str1[20];
  7   char str2[20];
  8   char str3[40];
  9
 10   printf("첫 번째 문자열을 입력하시오 : ");
 11   fgets(str1, sizeof(str1), stdin);
 12   str1[strlen(str1) - 1] = 0;  // \n 문자의 삭제
 13
 14   printf("두 번째 문자열을 입력하시오 : ");
 15   fgets(str2, sizeof(str2), stdin);
 16   str2[strlen(str2) - 1] = 0;  // \n 문자의 삭제
 17
 18   strcpy(str3, str1);
 19   strcat(str3, str2);
 20   printf("조합의 결과: %s \n", str3);
 21   return 0;
 22 }
```

```
첫 번째 문자열을 입력하시오 : Simple
두 번째 문자열을 입력하시오 : String
조합의 결과: SimpleString
```
