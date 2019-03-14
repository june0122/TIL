# 라이브러리(Library)

## 라이브러리란?

- 라이브러리란 소프트웨어를 만들 때 쓰이는 클래스나 함수(서브루틴)들의 모임을 가리키는 말이다.

- 라이브러리는 다른 프로그램들과 링크되기 위하여 존재하는, 하나 이상의 함수(function)들의 집합 파일이다.

- 링크(link)될 수 있도록 보통 **컴파일된 형태인 목적 코드(object code)** 형태로 존재한다.

- 미리 컴파일 되어 있어서 컴파일 시간도 단축된다.

<br>

> 확장자별 라이브러리 구분

||정적 라이브러리|동적 라이브러리|
|:---:|:---:|:---:|
|윈도우|.lib|.dll|
|리눅스|.a|.so|

<br>

## 라이브러리의 구분

### 정적 라이브러리(Static Library)

> 특징

- 실행파일 안에 함수의 구현이 포함된다.
  
  - 추가적인 로드가 필요없다.
 
- 실행 파일의 크기가 커진다.

- 라이브러리가 변경되면 컴파일을 다시 해야 한다.

- 배포가 쉽다.

<br>

> 정적 라이브러리 만드는 법

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54333059-46340900-4664-11e9-9421-6bb4cc210b90.PNG'>
</p>

- 리눅스 상에서 GCC컴파일러를 이용해 라이브러리를 만드려면 다음과 같은 컴파일 과정을 통해서 오브젝트 파일을 생성한다.

    ```
  $ gcc -c filename.c      // .cpp 파일을 컴파일 할 경우 g++ -c filename.cpp
    ```

    ```
  /*** 'gcc'와 'gcc -c'의 차이 ***/

  $ gcc filename.c         // 링킹까지 해준다.
  $ gcc -c filename.c      // 링킹을 해주지 않는다. -> 오브젝트 파일 (.o) 생성
    ```

- 컴파일이 제대로 끝났다면 확장자가 `.o` 인 `filename.o` 파일이 만들어 질것이다.
  
- 다음은 생성된 오브젝트 파일을 이용하여 라이브러리를 만들도록 한다. 이 때 사용하는 명령어는 `ar` (archives) 로 라이브러리 작성에 사용된다.

   - ar 명령어를 자세히 알고 싶다면 터미널에 `man ar` 을 입력해서 내용을 확인하자.

- 아래와 같이 `ar` 옵션 그리고 생성하고자 하는 `라이브러리의 이름`, 마지막으로 `오브젝트 파일들의 이름`을 나열한후 실행 한다. (※ 윈도우에서 라이브러리 파일의 확장자는 .lib이지만 리눅스에서는 .a확장자를 지닌다.)

    ```
    $ ar rcv libfile.a filename.o
    ```

- 각 옵션의 의미는 다음과 같다.

  - r : 라이브러리에 새로운 object를 추가하겠다는 옵션

  - c : Archive 가 존재하지 않으면 추가하겠다는 옵션

  - v : tar의 옵션과 같이 library 과정을 보여주겠다는 옵션

- 라이브러리 파일의 사용

    ```
    gcc (filename).o -l(libraryfile name) -L.
    ```
  - 이때 라이브러리의 이름은 lib라는 말과 확장자 .a를 생략하여 사용할수 있다. 즉, libadd.a의 경우 그냥 add이라고 쓰면 된다.
  
  - 이때 만일 라이브러리 파일이 /home/user 디렉토리 밑에 있다면 -L 옵션을 다음과 같이 주면 된다.

<br>

### 동적 라이브러리(Dynamic Library)

- 실행 중에 필요한 라이브러리를 동적으로 메모리에 로드해서 사용한다.
 
  - 실행 파일 안에 함수의 구현이 존재하지 않음.

  - 추가적인 로드가 필요하다.

- 실행파일의 크기가 크지 않다.

- 라이브러리가 변경되어도 다시 컴파일 할 필요가 없다.


```
$ gcc add.o -shared -fpic -o libadd.so
```

```
$ LD_LIBRARY_PATH=. ./a.out
```

실행 중에 동적라이브러리를 로드할 수 있다.

<br>

---

일반적으로 gcc 컴파일 할때, C 언어 표준 라이브러리에 대한 **암묵적인 링킹(linking)이 수행**된다. → printf, scanf ···

하지만 gcc 컴파일러는 디폴트로 mathematical 라이브러리를 링크할 당시 포함시키지 않는다.

<br>

> ※ `math.h` include 시 컴파일 오류

수학라이브러리를 사용하는 경우, 즉 `math.h`를 include했다면 컴파일할때 반드시 `-lm` 을 붙여줘야 한다. 정상적으로 코드 내 헤더 파일을 include 시켰음에도 특정 라이브러리 호출이 안된다면 라이브러리를 링크해주도록 하자.

```
june0122@ubuntu:~/thethelab.io/0313$ gcc 1.c        // 1.c 파일 컴파일 실패
/tmp/ccB2EDty.o: In function `main':
1.c:(.text+0xce): undefined reference to `sqrt'
collect2: error: ld returned 1 exit status
june0122@ubuntu:~/thethelab.io/0313$ gcc 1.c -lm        // gcc 컴파일 명령 끝에 -lm 추가
june0122@ubuntu:~/thethelab.io/0313$ ./a.out        // 문제없이 컴파일이 된다
point1 pos: 1 3
point2 pos: 4 5
두 점의 거리는 3.60555 입니다.
```

<br>

> 리눅스 매뉴얼 `man` 페이지

```
SQRT(3)                  Linux Programmer's Manual                  SQRT(3)

NAME
       sqrt, sqrtf, sqrtl - square root function

SYNOPSIS
       #include <math.h>

       double sqrt(double x);
       float sqrtf(float x);
       long double sqrtl(long double x);

       Link with -lm.
       
 . . . .
```

터미널에서 `man [함수이름]` 으로 함수 매뉴얼 페이지를 읽어올 수 있다. 그 내용중에 `SYNOPSIS`라는 섹션을 확인하면 간단히 요약된 내용을 확인할 수 있다.

- 맨윗줄 SQRT(3) 에서 3은 `세션 번호`입니다. 동명이인 찾듯이 C함수, 쉘 명령어, 프로그램 이름이 같을 경우 세션으로 구분해 준다.

- `NAME`은 이 페이지에서 설명하는 함수의 목록이다.

- `SYNOPSIS`는 간단 요약이다. 함수 문법과 불러와야할 헤더파일, 라이브러리 목록을 알려준다.

- `ERRORS` 는 함수가 실패하는 경우를 설명한다.

- `RETURN` 섹션은 함수 리턴값의 의미를 설명하는데 쓰인다.

---

<br>

### 참고

https://goodgid.github.io/Static-VS-Dynamic-Libray/

http://egloos.zum.com/jmbae940/v/4295316