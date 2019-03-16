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

   - ※ gcc compile process <sup id="a1">[1](#f1)</sup>

<br>

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


-  ※ -fpic, -shared option <sup id="a2">[2](#f2)</sup>

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

## <b id="f1"><sup>1</sup></b> GCC Compile Process [↩](#a1)<br>


<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/54405237-493d0100-4719-11e9-99ef-0f22f9e43a65.gif'>
</p>

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/54411195-dc823080-4731-11e9-8670-9f60fea91051.png'>
</p>

<br>

> `gcc -v filename.c` : gcc 컴파일 과정을 모두 볼 수 있다.

```
june0122@ubuntu:~/thethelab.io/0314_2$ gcc -v 10.c
Using built-in specs.
COLLECT_GCC=gcc
COLLECT_LTO_WRAPPER=/usr/lib/gcc/x86_64-linux-gnu/7/lto-wrapper
OFFLOAD_TARGET_NAMES=nvptx-none
OFFLOAD_TARGET_DEFAULT=1
Target: x86_64-linux-gnu
Configured with: ../src/configure -v --with-pkgversion='Ubuntu 7.3.0-27ubuntu1~18.04' --with-bugurl=file:///us                                                r/share/doc/gcc-7/README.Bugs --enable-languages=c,ada,c++,go,brig,d,fortran,objc,obj-c++ --prefix=/usr --with                                                -gcc-major-version-only --program-suffix=-7 --program-prefix=x86_64-linux-gnu- --enable-shared --enable-linker                                                -build-id --libexecdir=/usr/lib --without-included-gettext --enable-threads=posix --libdir=/usr/lib --enable-n                                                ls --with-sysroot=/ --enable-clocale=gnu --enable-libstdcxx-debug --enable-libstdcxx-time=yes --with-default-l                                                ibstdcxx-abi=new --enable-gnu-unique-object --disable-vtable-verify --enable-libmpx --enable-plugin --enable-d                                                efault-pie --with-system-zlib --with-target-system-zlib --enable-objc-gc=auto --enable-multiarch --disable-wer                                                ror --with-arch-32=i686 --with-abi=m64 --with-multilib-list=m32,m64,mx32 --enable-multilib --with-tune=generic                                                 --enable-offload-targets=nvptx-none --without-cuda-driver --enable-checking=release --build=x86_64-linux-gnu                                                 --host=x86_64-linux-gnu --target=x86_64-linux-gnu
Thread model: posix
gcc version 7.3.0 (Ubuntu 7.3.0-27ubuntu1~18.04)
COLLECT_GCC_OPTIONS='-v' '-mtune=generic' '-march=x86-64'
 /usr/lib/gcc/x86_64-linux-gnu/7/cc1 -quiet -v -imultiarch x86_64-linux-gnu 10.c -quiet -dumpbase 10.c -mtune=                                                generic -march=x86-64 -auxbase 10 -version -fstack-protector-strong -Wformat -Wformat-security -o /tmp/ccmMqWX                                                c.s
GNU C11 (Ubuntu 7.3.0-27ubuntu1~18.04) version 7.3.0 (x86_64-linux-gnu)
        compiled by GNU C version 7.3.0, GMP version 6.1.2, MPFR version 4.0.1, MPC version 1.1.0, isl version                                                 isl-0.19-GMP

GGC heuristics: --param ggc-min-expand=97 --param ggc-min-heapsize=126139
ignoring nonexistent directory "/usr/local/include/x86_64-linux-gnu"
ignoring nonexistent directory "/usr/lib/gcc/x86_64-linux-gnu/7/../../../../x86_64-linux-gnu/include"
#include "..." search starts here:
#include <...> search starts here:
 /usr/lib/gcc/x86_64-linux-gnu/7/include
 /usr/local/include
 /usr/lib/gcc/x86_64-linux-gnu/7/include-fixed
 /usr/include/x86_64-linux-gnu
 /usr/include
End of search list.
GNU C11 (Ubuntu 7.3.0-27ubuntu1~18.04) version 7.3.0 (x86_64-linux-gnu)
        compiled by GNU C version 7.3.0, GMP version 6.1.2, MPFR version 4.0.1, MPC version 1.1.0, isl version                                                 isl-0.19-GMP

GGC heuristics: --param ggc-min-expand=97 --param ggc-min-heapsize=126139
Compiler executable checksum: c8081a99abb72bbfd9129549110a350c
COLLECT_GCC_OPTIONS='-v' '-mtune=generic' '-march=x86-64'
 as -v --64 -o /tmp/cc1u9aCd.o /tmp/ccmMqWXc.s
GNU assembler version 2.30 (x86_64-linux-gnu) using BFD version (GNU Binutils for Ubuntu) 2.30
COMPILER_PATH=/usr/lib/gcc/x86_64-linux-gnu/7/:/usr/lib/gcc/x86_64-linux-gnu/7/:/usr/lib/gcc/x86_64-linux-gnu/                                                :/usr/lib/gcc/x86_64-linux-gnu/7/:/usr/lib/gcc/x86_64-linux-gnu/
LIBRARY_PATH=/usr/lib/gcc/x86_64-linux-gnu/7/:/usr/lib/gcc/x86_64-linux-gnu/7/../../../x86_64-linux-gnu/:/usr/                                                lib/gcc/x86_64-linux-gnu/7/../../../../lib/:/lib/x86_64-linux-gnu/:/lib/../lib/:/usr/lib/x86_64-linux-gnu/:/us                                                r/lib/../lib/:/usr/lib/gcc/x86_64-linux-gnu/7/../../../:/lib/:/usr/lib/
COLLECT_GCC_OPTIONS='-v' '-mtune=generic' '-march=x86-64'
 /usr/lib/gcc/x86_64-linux-gnu/7/collect2 -plugin /usr/lib/gcc/x86_64-linux-gnu/7/liblto_plugin.so -plugin-opt                                                =/usr/lib/gcc/x86_64-linux-gnu/7/lto-wrapper -plugin-opt=-fresolution=/tmp/ccIYLGhe.res -plugin-opt=-pass-thro                                                ugh=-lgcc -plugin-opt=-pass-through=-lgcc_s -plugin-opt=-pass-through=-lc -plugin-opt=-pass-through=-lgcc -plu                                                gin-opt=-pass-through=-lgcc_s --sysroot=/ --build-id --eh-frame-hdr -m elf_x86_64 --hash-style=gnu --as-needed                                                 -dynamic-linker /lib64/ld-linux-x86-64.so.2 -pie -z now -z relro /usr/lib/gcc/x86_64-linux-gnu/7/../../../x86                                                _64-linux-gnu/Scrt1.o /usr/lib/gcc/x86_64-linux-gnu/7/../../../x86_64-linux-gnu/crti.o /usr/lib/gcc/x86_64-lin                                                ux-gnu/7/crtbeginS.o -L/usr/lib/gcc/x86_64-linux-gnu/7 -L/usr/lib/gcc/x86_64-linux-gnu/7/../../../x86_64-linux                                                -gnu -L/usr/lib/gcc/x86_64-linux-gnu/7/../../../../lib -L/lib/x86_64-linux-gnu -L/lib/../lib -L/usr/lib/x86_64                                                -linux-gnu -L/usr/lib/../lib -L/usr/lib/gcc/x86_64-linux-gnu/7/../../.. /tmp/cc1u9aCd.o -lgcc --push-state --a                                                s-needed -lgcc_s --pop-state -lc -lgcc --push-state --as-needed -lgcc_s --pop-state /usr/lib/gcc/x86_64-linux-                                                gnu/7/crtendS.o /usr/lib/gcc/x86_64-linux-gnu/7/../../../x86_64-linux-gnu/crtn.o
COLLECT_GCC_OPTIONS='-v' '-mtune=generic' '-march=x86-64'

```

<br>

## <b id="f2"><sup>2</sup></b> -fpic, -shared option [↩](#a2)<br>

> `man gcc` 명령어를 통한 man-page에서 `-fpic` , `-shared` 항목 검색

- `-fpic` : PIC를 생성한다. shared library 같은 경우 메모리의 어느 위치에 로딩이 되는지가 컴파일 타임에 결정되지 않기 때문에 절대주소 대신 상대주소(offset)를 사용하도록 컴파일하는 옵션.

  즉, **다른 바이너리에서 이 라이브러리의 함수를 호출할 때 PLT와 GOT를 사용하도록 하는 옵션**이라 생각하면 된다.

    - PLT와 GOT의 개념 <sup id="a3">[3](#f3)</sup>

  `library의 mapping 주소` + `offset` = `해당 symbol의 주소`

  `mapping 주소`는 `ldd` 로, `offset`은 `nm`으로 확인 가능하다.

<br>

- `-shared` : 가능한 한 공유 라이브러리와 링크하고 공유 라이브러리가 없는 경우에는 정적 라이브러리와 링크

  
  - `PIC (position-independent code, 위치 독립 코드)`: 메모리의 어딘가에 위치한 기계어 코드의 몸체로서 절대 주소와 관계 없이 적절히 실행된다. PIC는 주로 공유 라이브러리에 사용돼서, 같은 라이브러리 코드도 (사용되는 메모리 공간에 겹쳐써지지 않으면서) 각 프로그램의 주소 공간에 로드될 수 있게 된다.

```
-fpic
           Generate position-independent code (PIC) suitable for use in a
           shared library, if supported for the target machine.  Such code
           accesses all constant addresses through a global offset table
           (GOT).  The dynamic loader resolves the GOT entries when the
           program starts (the dynamic loader is not part of GCC; it is part
           of the operating system).  If the GOT size for the linked
           executable exceeds a machine-specific maximum size, you get an
           error message from the linker indicating that -fpic does not
           work; in that case, recompile with -fPIC instead.  (These
           maximums are 8k on the SPARC, 28k on AArch64 and 32k on the m68k
           and RS/6000.  The x86 has no such limit.)

           Position-independent code requires special support, and therefore
           works only on certain machines.  For the x86, GCC supports PIC
           for System V but not for the Sun 386i.  Code generated for the
           IBM RS/6000 is always position-independent.

           When this flag is set, the macros "__pic__" and "__PIC__" are
           defined to 1.

       -fPIC
           If supported for the target machine, emit position-independent
           code, suitable for dynamic linking and avoiding any limit on the
           size of the global offset table.  This option makes a difference
           on AArch64, m68k, PowerPC and SPARC.

           Position-independent code requires special support, and therefore
           works only on certain machines.

           When this flag is set, the macros "__pic__" and "__PIC__" are
           defined to 2.

Options for Linking
       These options come into play when the compiler links object files
       into an executable output file.  They are meaningless if the compiler
       is not doing a link step.

       object-file-name
           A file name that does not end in a special recognized suffix is
           considered to name an object file or library.  (Object files are
           distinguished from libraries by the linker according to the file
           contents.)  If linking is done, these object files are used as
           input to the linker.

       -c
       -S
       -E  If any of these options is used, then the linker is not run, and
           object file names should not be used as arguments.

           . . . .

       -llibrary
       -l library
           Search the library named library when linking.  (The second
           alternative with the library as a separate argument is only for
           POSIX compliance and is not recommended.)

           It makes a difference where in the command you write this option;
           the linker searches and processes libraries and object files in
           the order they are specified.  Thus, foo.o -lz bar.o searches
           library z after file foo.o but before bar.o.  If bar.o refers to
           functions in z, those functions may not be loaded.

           The linker searches a standard list of directories for the
           library, which is actually a file named liblibrary.a.  The linker
           then uses this file as if it had been specified precisely by
           name.

           The directories searched include several standard system
           directories plus any that you specify with -L.

           Normally the files found this way are library files---archive
           files whose members are object files.  The linker handles an
           archive file by scanning through it for members which define
           symbols that have so far been referenced but not defined.  But if
           the file that is found is an ordinary object file, it is linked
           in the usual fashion.  The only difference between using an -l
           option and specifying a file name is that -l surrounds library
           with lib and .a and searches several directories.

       -static
           On systems that support dynamic linking, this overrides -pie and
           prevents linking with the shared libraries.  On other systems,
           this option has no effect.

       -shared
           Produce a shared object which can then be linked with other
           objects to form an executable.  Not all systems support this
           option.  For predictable results, you must also specify the same
           set of options used for compilation (-fpic, -fPIC, or model
           suboptions) when you specify this linker option.[1]

           . . . .
```

<br>

- gcc 옵션 요약 <sup id="a4">[4](#f4)</sup>

<br>

## <b id="f3"><sup>3</sup></b> PLT와 GOT의 개념 [↩](#a3)<br>

- **PLT (Procedure Linkage Table)** : 외부 프로시저를 연결해주는 테이블. PLT를 통해 다른 라이브러리에 있는 프로시저를 호출해 사용할 수 있다.

- **GOT (Global Offset Table)** : PLT가 참조하는 테이블. 프로시저들의 주소가 들어있다.

  - `프로시저(procedure)`: 사전적 의미로 어떤 행동을 수행하기 위한 일련의 작업순서 또는 특정 작업을 수행하기 위한 작은 단위의 프로그램의 일부를 뜻한다. 다른 말로는 함수(function)나 메소드(method)라 한다. (경우에 따라 값을 반환하면 함수, 값을 반환하지 않으면 프로시저로 구분하기도 한다. 하지만 PLT, GOT 개념에서는 '프로시저 = 함수' 이다.)

<br>

> PLT와 GOT에 대해 일반적으로 알려진 내용

```
함수를 호출하면(PLT를 호출하면) GOT로 점프하는데 GOT에는 함수의 실제 주소가 쓰여있다.

첫 번째 호출이라면 GOT는 함수의 주소를 가지고 있지 않고 ‘어떤 과정’을 거쳐 주소를 알아낸다.

두 번째 호출 부터는 첫 번째 호출 때 알아낸 주소로 바로 점프한다.
```

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/54417458-9b941700-4745-11e9-910b-4cfae301ab7c.png'>
</p>

<br>

> 조금 더 의문을 가져보자

   - 왜 함수의 주소로 바로 점프하지 않고 PLT와 GOT를 사용하는 것일까?

   - GOT가 함수의 주소를 알아내는 '어떤 과정'은 무엇일까? 

<br>

## <b id="f4"><sup>4</sup></b> gcc 옵션 요약 [↩](#a4)<br>

- -o 옵션: gcc에서 만들 실행 파일명을 정하는 것. 안지정하면 a.out

  - %gcc -o filter filter_driver.c define_stack.c global_var.c

- -c 옵션 : 컴파일하지만 링크하지는 않는 다는 것을 의미 

  - %gcc -c filter_driver.c


- -D 옵션 : 소스코드의 #define 문과 같다. 심볼에 대한 값을 지정

  - % gcc -c -DDOC_FILE=\"info\" -DUSE_POLL filter_driver.c

  - 첫 번째 -D 옵션은 DOC_FILE 을 info 라는 문자열로 대치한다는 것

  - 두 번째 -D 옵션은 USE_POLL 심볼을 정의한다.


- -U 옵션 : 외부에서 #undef ex_ -UDEBUG

- -I 옵션 : 헤더파일이 위치한 디렉토리 지정

  - % gcc -c -I../headers filter_driver.c

- -l 옵션 : 라이브러리를 지정. 파일명과 함께 붙여 쓴다.

  - % gcc -o plot main.o plot_line.o -lm

  - -lm 옵션은 수학 라이브러리를 포함한다는 것을 지정

- -L 옵션 : 라이브러리 폴더를 찾아서 지정

  - % gcc -o plot -L/src/local/lib main.o plot_line.o -lm

- -E 옵션 : `전처리` , 프로그램을 컴파일 하는 대신 전처리된 코드를 표준 출력으로 내보냄

- -S 옵션 :  `컴파일` , 어셈블리 언어로 나온 결과를 저장 .c 대신 .s 가 붙는 파일을 만들어 낸다.

- -c 옵션 : `어셈블` , .o 로 끝나는 오브젝트 파일 생성

<br>

> C 언어 옵션

- -v 옵션 : (verbose) 컴파일러의 버전과 각 단계에서 실행하는 자세한 사항을 출력 (어떤 옵션으로 컴파일 하였는지)

- -w 옵션 : 모든 경고 메시지가 나오지 않도록

- -W 옵션 : 합법적이지만 다소 모호한 코딩에 대하여 부가적인 경고 메시지를 출력

- -Wall 옵션 : 모호한 코딩에 대하여 훨씬 더 자세한 경고 메시지를 출력

 <br>

> 라이브러리 지정 옵션

- -static 옵션 : 공유 라이브러리가 아닌 정적 라이브러리와 링크

- -shared 옵션 : 가능한 한 공유 라이브러리와 링크하고 공유 라이브러리가 없는 경우에는 정적 라이브러리와 링크

<br>

> 디버깅, 프로파일링 옵션

- -p 옵션 : 프로그램을 prof 로 프로파일링 할 수 있도록 링크. mon.out 파일 생성됨. 프로그램 실행 통계 수치를 담고 있다.

<br>

---


## 참고

https://bpsecblog.wordpress.com/2016/03/07/about_got_plt_1/

https://goodgid.github.io/Static-VS-Dynamic-Libray/

http://egloos.zum.com/jmbae940/v/4295316

http://egloos.zum.com/program/v/1373351
