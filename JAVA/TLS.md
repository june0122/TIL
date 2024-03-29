# TLS (Thread Local Storage) : 스레드 로컬 저장소

## 요약

TLS는 스레드 별로 **고유한 저장공간**을 가질 수 있는 방법이다.

## 왜 사용할까?

- 스택(Stack)에 저장되는 지역 변수는 스레드마다 별도의 스택을 사용하므로 당연히 다른 값을 가지지만, 정적 혹은 전역 변수의 경우에는 모든 스레드가 공유하므로 접근 시에 **Race Condition(경쟁 상태)**<sup id = "a1">[1](#f1)</sup> 이 발생할 수 있다.

- 따라서 **스레드마다 개별적으로 사용할 수 있는**(thread-local) **변수**를 사용하여 안정성 및 성능을 높일 수 있다.

## TLS의 도입 배경

- 전통적으로 이러한 TLS의 개념은 **POSIX Threads** ([**PThread**](https://www.joinc.co.kr/w/Site/Thread/Beginning/PthreadApiReference#AEN230), 병렬적으로 작동하는 소프트웨어의 작성을 위해서 제공되는 표준 API)가 제공하는 [**TSD**](https://www.joinc.co.kr/w/Site/Thread/Advanced/DeepThread#AEN62)(Thread-Specific Data) 관련 API를 통해 구현되었다.

- 하지만 이는 프로그래머가 직접 key를 생성·관리해야 하며 ***(보다 중요한 문제는)*** `void*` 타입의 데이터만 저장이 가능하므로 별도의 메모리 공간을 동적 할당한 뒤에 해당 포인터를 저장하는 방식으로 사용하여 **메모리 누수 문제를 발생**시킬 수 있는 가능성이 다분하다.

- 이러한 문제를 간단히 해결할 수 있는 기법으로 도입된 것이 바로 **TLS**이다.

  - TLS를 이용하면 **컴파일러 및 링커**가 복잡한 처리를 도맡아 주기 때문에 프로그래머는 일반적인 변수를 이용하는 방식과 동일하게 이용할 수 있다.

  - 오직 필요한 것은 변수 선언 시 다음과 같이 (GNU 확장) **`__thread`** 키워드를 써서 이 변수가 TLS에 저장됨을 알려주는 일이다.

  > GNU C

  ```c
  __thread int x;
  ```

  > C and C++

  ```c
  #include <threads.h>
  thread_local int foo = 0;
  ```

  > java

  ```java
  private static final ThreadLocal<Integer> myThreadLocalInteger = new ThreadLocal<Integer>();
  ```

## 본문

모든 프로세스 내의 스레드들은 가상 메모리(가상 주소 공간)을 공유한다. 함수의 지역 변수는 함수를 실행하는 각 스레드마다 고유하다. 하지만 정적(static) 변수와 전역(global) 변수는 프로세스의 모든 스레드가 공유한다. TLS(스레드 로컬 저장소)를 사용하면, 프로세스가 글로벌 인덱스를 사용하여 액세스할 수 있는 각 스레드에 대해 고유한 데이터를 제공할 수 있다. 하나의 스레드는 다른 스레드가 인덱스와 연관된 고유 데이터를 검색(retrieve)하는데 사용할 수 있는 인덱스를 할당한다.

상수 `TLS_MINIMUM_AVAILABLE`은 각 프로세스에서 사용할 수있는 **최소 TLS 인덱스 수**를 정의한다. 이 최소값은 모든 시스템에서 최소한 **64**가 되도록 보장된다. 프로세스 당 최대 인덱스 수는 **1,088** 이다.

스레드가 생성되면 시스템은 `NULL`로 초기화되는 TLS에 대한 **LPVOID 값의 배열**을 할당한다. 인덱스를 사용하려면 스레드 중 하나에 의해 인덱스가 할당되어야 한다. 각 스레드는 TLS 인덱스에 대한 데이터를 배열의 TLS 슬롯에 저장한다. 인덱스와 연관된 데이터가 **LPVOID** 값에 맞으면, TLS 슬롯에 직접 데이터를 저장할 수 있다. 그러나 이와 같이 많은 수의 인덱스를 사용하는 경우, 별도의 스토리지를 할당하고, 데이터를 통합하여(consolidate), 사용중인 TLS 슬롯의 수를 최소화하는 것이 좋다.

<br>

> **TLS의 작동 방식**<br>TLS 사용을 보여주는 코드 예시는 [이곳](https://docs.microsoft.com/ko-kr/windows/desktop/ProcThread/using-thread-local-storage)에서 확인한다.

<br>

<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/56490378-017f7580-6520-11e9-915a-43df07c42630.png'>
</p>

<br>

이 프로세스에는 Thread 1과 Thread 2라는 두 개의 스레드가 있다.

- 프로세스는 TLS와 함께 사용할 두 개의 `인덱스`, `gdwTlsIndex1` 및 `gdwTlsIndex2`를 할당한다.

- 각 스레드는 데이터를 저장할 두 개의 메모리 블록 (각 인덱스에 하나씩)을 할당하고 이러한 메모리 블록에 대한 포인터를 해당하는 TLS 슬롯에 저장한다.

- 인덱스와 관련된 데이터에 액세스하기 위해, 스레드는 TLS 슬롯에서 메모리 블록에 대한 포인터를 검색하여 **lpvData 로컬 변수**에 저장한다.

TLS는 [동적 연결 라이브러리](https://github.com/june0122/TIL/blob/master/C/library.md)(DLL)에서 사용하는 것이 이상적이다. 예를 보려면 [동적 링크 라이브러리에서 스레드 로컬 저장소 사용](https://docs.microsoft.com/ko-kr/windows/desktop/Dlls/using-thread-local-storage-in-a-dynamic-link-library)을 참조하자.

<br>

## 참고 자료

MS DOC, TLS : https://docs.microsoft.com/ko-kr/windows/desktop/ProcThread/thread-local-storage

wikipedia : https://en.wikipedia.org/wiki/Thread-local_storage

Pthread API Reference : https://www.joinc.co.kr/w/Site/Thread/Beginning/PthreadApiReference

[ELF] TLS (Thread Local Storage) : http://egloos.zum.com/studyfoss/v/5259841

정적, 동적 TLS : http://egloos.zum.com/sweeper/v/1985738

경쟁 상태 : https://wonjayk.tistory.com/251

---

## 각주

### <b id = "f1"><sup>1</sup></b> 경쟁 상태 (Race Condition)[ ↩](#a1)

둘 이상의 입력이나 조작이 동시에 일어나 의도하지 않은 결과를 가져오는 경우를 말한다.

- 파일 또는 변수와 같은 공유 자원을 접근하는, 하나 또는 그 이상의 프로세스들의 **다중 접근이 제대로 제어되지 않은 것**을 말한다.

- 프로세스들끼리 **하나의 자원을 갖기 위해 싸우는 것**, 하나의 자원을 동시에 요청

### 라이브락 (Livelock)

**한 프로세스가 이미 자원을 점유한 상태에서 다른 프로세스가 그 자원을 사용하기 위해 무한정 대기 상태에 빠지는 것**을 라이브락이라 한다.

### 교착상태 (Deadlock)

- 프로세스들이 더 이상 진행을 못하고 영구적으로 블록되어 있는 상태로, 시스템 자원에 대한 경쟁 도중에 발생할 수도 있고 프로세스 간의 통신 과정에서도 발생할 수 있다.

- **두 개 이상의 작업이 서로 상대방의 작업이 끝나기만을 기다리고 있기 때문에 결과적으로는 아무것도 완료되지 못하는 상태**를 말한다.

  #### 교착상태 조건

  1. `상호배제 (Mutual Exclusion)` : 한 순간에 한 프로세스만이 자원을 사용할 수 있다. 즉, 한 프로세스에 의해 점유된 자원을 다른 프로세스들이 접근할 수 없다.

  2. `점유대기 (Hold and Wait)` : 이미 자원을 보유한 프로세스가 다른 자원을 요청하며 기다리고 있다.

  3. `비선점 (No preemption)` : 프로세스에 의해 점유된 자원을 다른 프로세스가 강제적으로 빼앗을 수 없다.

  4. `환형 대기 (Circular Wait)` : 프로세스간에 닫힌 연결이 존재하는 경우이다. 블록된 프로세스가 자원을 점유하고 있는데 이 자원을 다른 프로세스가 원하며 대기하고 있는 상태.

     - `환형 대기`의 경우 `ⅰ ~ ⅲ`의 결과에 의해 발생하게 된다.
