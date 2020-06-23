# Synchronization (1)

> 실제 프로그램에서의 Synchronization 문제 예시

- 두 스레드의 실행이 preemptive scheduling에 의해 interleave 될 수 있다. (The execution of the two threads can be interleaved, assuming preemptive scheduling)

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85329200-061d3080-b50d-11ea-8b80-23ee742e67c3.png'>
</p>
<br>

## Synchronization Problem

- Concurrency may lead to non-deterministic results
  - 두 개 이상의 concurrent thread <sup>executable instance</sup>들이 shared resourse에 접근하거나 수정할 때 **race condition** 을 만들 수 있다.
  - 디버그가 매우 힘듬 <sup>Heisenbugs</sup>
- shared resourse에 대한 접근을 통제하는 synchronization (or coordination) mechanism 이 필요하다.
  - Synchronization restricts the concurrency
  - Scheduling is not under programmer’s control

## Critical Section

> 어디서 race condition이 생기는 것일까? → Critical Section

- Critical Section <sup>region</sup>은 shared resource에 동시에 접근하는 코드 부분을 말한다.
  - Usually a variable or data structure
  
<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85331190-a7f24c80-b510-11ea-948b-f0932add540a.png'>
</p>
<br>

- 여러 개의 스레드들이 Critical Section에 동시에 들어가지 못하도록 막아주면 (**mutual exclusion**) race condition이 해결된다.
  - 오직 하나의 스레드만이 critical section에서 실행될 수 있음
  - 다른 스레드들은 entry를 기다리도록 강제됨

## Locks

- Critical Section과 같은 데이터 코드 영역이 있는데, Mutual exclusion하게 접근할 수 있는 기능을 제공하는 data object를 **Lock** 이라 한다.
  - `acquire()` : Wait until lock is free, then grab it
  - `release()` : Unlock and wake up any thread waiting in `acquire()`
- Lock의 사용
  - Lock is initially free
  - Call `acquire()` before entering a critical section, and release() after leaving it
  - `acquire()` does not return until the caller holds the lock
  - On `acquire()`, a thread can spin (spinlock) or block (mutex)
  - At most one thread can hold a lock at a time

### Using Locks

<br>
<p align = 'center'>
<img width = '550' src = 'https://user-images.githubusercontent.com/39554623/85334171-bf800400-b515-11ea-8a74-91a523dc6b10.png'>
</p>
<br>

### Requirements for Locks

- Correctness
  - **Mutual exclusion** : only one thread in critical section at a time
  - **Progress** : if several threads want to enter the same critical section, one must be allowed to proceed
  - **Bounded waiting** : starvation-free; must eventually allow each waiting thread to enter
    - Lock을 기다리고 있는데 starvation때문에 Lock을 잡지 못하는 상태가 발생하지 않도록 해야함

- Fairness
  - Each thread gets a fair chance at acquiring the lock

- Performance
  - Time overhead for a lock without and with contentions (possibly on multiple
CPUs)?

### An Initial Attempt

- **spinlock** 구현의 첫 번째 시도
  - lock의 유무를 끊임없이 확인하는 lock을 spinlock이라 한다.

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85335401-ea6b5780-b517-11ea-8f4a-17f6f40c35ec.png'>
</p>
<br>

- 위의 구현은 제대로 동작을 할까?
  - 0, 1, 2 세 개의 스레드가 있다고 가정할 때, 0번 스레드가 lock을 잡고 있다고 가정하자.
  - 0번 스레드가 lock을 잡고 있는 동안 1, 2번 스레드는 0번 스레드가 lock을 release하기만을 동시에 기다리고 있다. (observation)
  - 0번 스레드가 lock을 release하면 1, 2번 스레드가 동시에 lock을 잡을 수도 있다.
  - 이렇게 되면 오직 하나의 스레드가 critical section에 있어야하는 **Mutual exclusion** property를 만족시키지 못한 것이다.
  - 고로 위의 구현은 잘못된 구현이다.

> Lock의 구현하는 것은 쉽지 않은 일이다. 그렇기에 이전부터 수많은 구현 방법이 있었는데 아래와 같다.

## Implementing Locks

- Software-only algorithms
  - Dekker’s algorithm (1962)
  - Peterson’s algorithm (1981)
  - Lamport’s Bakery algorithm for more than two tasks (1974)

- Hardware approaches
  - Disable interrupts
  - Hardware atomic instructions
    - Test-And-Set
    - Compare-And-Swap

### Software-only algorithms

- spinlock 구현의 두 번째 시도

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85336552-bdb83f80-b519-11ea-83e8-e8dedd0ea283.png'>
</p>
<br>

- 위의 구현 또한 잘못된 구현이다.
  - 2개의 스레드가 있고, 서로의 상태를 확인하여 lock을 얻는 구조이다.
  - 스레드의 초기값은 둘 다 0 이다.
  - 2개의 스레드가 동시에 lock을 얻으려하면 값이 둘 다 1이 되어 아무도 lock을 얻지 못하는 상황이 발생한다. 서로 다른 스레드가 lock을 얻으려는 줄 알고 진행하지 못하고 있는 상황.
  - resourse가 lock이 acquired 되어 있지 않은데, 아무도 lock을 acquire하지 못하고 block되어 있으므로 **progressive** property가 제공되고 있지 않은 상황이다.

### Peterson’s Algorithm

> while문의 조건을 만족하게 되면 `acquire()`에서 빠져나오지 못하고 계속 while loop를 돈다.

- Solves the critical section problem for two tasks

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85340940-0d9b0480-b522-11ea-8238-cbe226685ffc.png'>
</p>
<br>

- T<sub>0</sub>와 T<sub>1</sub> 두 개의 스레드가 존재한다고 가정.
  - T<sub>0</sub>와 T<sub>1</sub> 두 개의 스레드가 동시에 acquire할지라도 결국 `turn = other;` 코드 라인은 1개의 스레드가 먼저 쓰게 된다.
  - `while (interested[other] && turn == other);` 코드라인을 T<sub>0</sub>가 실행하려는 시점에 T<sub>1</sub>이 `turn = other;`
   코드 라인에서 전역변수 `turn`을 0으로 변경하므로 while문의 조건에서 `turn == other` 코드 부분이 `0 == 1`로 FALSE가 되면서 T<sub>0</sub>가 while문을 빠져나와 acquire하게 된다.
  - `turn` 이라는 변수를 이용하여 다른 스레드가 acquire할 것인지에 대한 의사를 확인함.(양보)
  - 먼저 양보한 스레드가 다른 스레드도 양보를 하면서 lock을 얻게 됨.

- Mutual exclusion
  - Only one thread in critical section at a time
- Progress
  - One will enter the critical section right after the other releases
- Bounded waiting
  - Eventually allow each waiting thread to enter

> Peterson’s Algorithm은 3가지 조건을 모두 만족하나 실질적으론 구현이 어려워서 하드웨어적 도움이 필요하다.

## 요약

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85342046-9a46c200-b524-11ea-82e0-8046292ce7c8.png'>
</p>
<br>

- critical Section / race condition
- synchronization
- Correctness / Mutual exclusion / Progress / Bounded waiting