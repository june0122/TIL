# Synchronization (2)

## Synchronization Hardware

> **원자성(atomicity)** 깨지지 않는 성격이기에 즉, 중단되지 않는 연산이라고 볼 수 있다. 원자성(atomicity)은 데이터베이스 시스템에서 ACID 트랜잭션 특성중의 하나다. 하나의 원자 트랜잭션은 모두 성공하거나 또는 실패하는 데이터베이스 운용의 집합이다. 원자성의 보증은 데이터베이스의 부분적인 갱신으로 더 큰 문제가 야기되는 것을 방지한다. 항공 티켓 주문이 원자성의 한 예다. 티켓은 반드시 지불과 예약이 동시에 되거나 아니면 모두 되지 않아야 한다. 성공적으로 지불은 되었으나 좌석 예약은 되지 않은 경우는 허용되지 않는다. 하나의 트랜잭션은 항공 티켓 예약뿐 아니라 호텔, 운송, 현재 환율로 정확히 환전되는 데에도 적용된다.

- 현대의 컴퓨터 architecture는 너무나 복잡하기 때문에 많은 소프트웨어적 접근에 대한 가정들이 옳지 않다.
  - Peterson’s algorithm은 load & store <sup>어떤 변수에 대해 값을 읽고 저장하는 것</sup>가 atomic하게 일어나거나 일어나지 않는 것 <sup>all-or-nothing</sup>을 가정하고 있다.
    - 하지만 SPARCS와 같은 architecture는 non-atomic한 architecture들이 존재한다.
  - 또한 architecture가 최적화를 위해 instruction <sup>명령, 작업</sup>들을 reordered 할 수도 있다.
- 요즘의 컴퓨터들은 소프트웨어만으로 lock을 구현하는 것은 효과적인 방법이 아니다보니 하드웨어가 기본적인 기능 <sup>atomicity</sup>을 제공해주고 그 위에 일부 소프트웨어로 lock을 구현하는 것이 일반적인 방법이다.
  - We might be able to implement a correct locking mechanism based on hardware-provided atomicity

### Disabling Interrupts

> Interrupt를 끄기

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85343925-2fe45080-b529-11ea-9110-220d7aea2b0b.png'>
</p>
<br>

- 위와 같이 T<sub>1</sub>의 instruction이 실행되다가 T<sub>2</sub>의 instruction이 preemption해서 끼어드는 상황이 발생하지 않도록 Timer interrupt를 끈다.
  - Timer interrupt를 끄면 scheduler가 관여하지 않고, 결국 preemption도 발생하지 않는다.
  - 이렇게 되면 하나의 instruction이 atomic하게 처리가 되고, context switching을 prevent할 수 있다.
- 하지만 Interrupt를 끄기 위해서 고려해야 할 문제가 있다.
  - Only kernel can enable/disable interrupts
    - 그렇지 않으면 user program이 마음대로 interrupt를 끄거나 컴퓨터를 독차지할 수 있는 보안적인 문제가 발생할 수 있다.
    - interrupt를 끄고 키는 것은 **privileged instruction**(특권 명령, 시스템에 악영향일 끼칠수 있는 일부 명령)이다.
  - Not practical in multiprocessor environments (실용적이지 않다)
    - Take long to disable/enable interrupts from all processors

### Test-And-Set instruction

- Atomic instruction
  - Read-modify-write operations guaranteed to be executed “atomically”

- Test-And-Set instruction
  - Returns the old value of a memory location while simultaneously updating it to the new value (원래의 값을 return하는 동시에 새로운 값으로 업데이트 함)
  - e.g. **xchg** in x86: exchange register/memory with register

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85347768-8b680b80-b534-11ea-80b7-b50c4a5fcc22.png'>
</p>
<br>

### Using Test-And-Set

- A simple spinlock using Test-And-Set instruction

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85347843-c9652f80-b534-11ea-9121-31962acfd4eb.png'>
</p>
<br>

- 왼쪽의 예제는 Mutual exclusion property를 만족시키지 못했으나 Test-And-Set instruction을 이용하면 해결이 된다.
  - `TestAndSet()`은 atomic하기 때문에 held의 값이 동시에 1로 바뀌는 일이 없으므로 Mutual exclusion property 만족.

### Compare-And-Swap instruction

- Supported in x86, Sparc, etc.
  - Update the memory location with the new value only when its old value equals to the “expected” value (원래의 값을 return하고, 기존의 값이 `expected` 값과 동일할 경우에만 새로운 값으로 업데이트한다.)
  - Return the old value
  - e.g. **cmpxchg** in x86: compare and exchange

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85349672-0aac0e00-b53a-11ea-80fa-8a56d0e2f590.png'>
</p>
<br>

## Higher-level Synchronization

- Sorry, (almost) no one synchronizes threads using those primitives
  - Useful only for very short and simple critical sections
    - Spinlock takes too much CPU resources
  - Need to block threads when lock is held by others
  - Need to wait until a certain condition is met
  
- Mutex locks, semaphores, monitor, condition variables

<br>

### Mutex Lock

- **Mut**(ual) **ex**(clusive) lock
  - acquire() to lock, release() to unlock

- May **busy-wait** or **block** (2개의 Mutex lock으로 구분)
  - Busy-wait version is called **spinlock** (`acquire()`를 끊임없이 돌리고 있음)

- Usually implemented using the hardware-supported atomic instructions

#### Spinlock

> busy-waiting mutex lock

- A lock mechanism that keeps spinning until it acquires the lock

- Advantage
  - Simple to implement
  - Does not require a **context switch**
    - spinlock에선 resource를 특정 task가 가져가서 사용하고 있어도 그것을 acquire하려는 나머지 task들이 schedule out되지 않는다.
    - Good for protecting **short** critical sections (보호해야하는 critical section, instruction이 짧은 경우 spinlock으로 충분히 보호가 가능하다.)
    - overhead가 낮아 성능상 이득이 있을 수 있다.
 
- Disadvantage
  - Busy-waiting burns system resources (CPU resource 낭비)
  - What if the **lock holder is preempted?**
    - lock을 hold하고 있는 task가 preemption 된다면 다른 task를 처리하는 동안에도 lock holder를 계속해서 observation하고 있던 task들은 lock을 hold했던 task가 schedule in 될 때까지의 긴 시간동안 많은 CPU resource를 계속해서 태우고 있는 큰 문제가 발생한다.
    - 보완법 : Lock holder가 preemption 되는 것을 방지하기 위해 **Disabling Interrupts**를 사용한다.

<br>
<p align = 'center'>
<img width = '200' src = 'https://user-images.githubusercontent.com/39554623/85350361-ecdfa880-b53b-11ea-9612-60a2961c6821.png'>
</p>
<br>

#### Blocking mutex

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85351771-76dd4080-b53f-11ea-975d-f014bcb20c8b.png'>
</p>
<br>

- 프로세스나 스레드가 critical section에 접근할 때, lock이 acquire되어 있을 경우 wait queue로 가면서 shedule out 됨.
- release 될 경우 wait 중인 프로세스나 스레드에 signal이 가서 wait였던 status가 ready로 바뀌어 ready queue로 가게 되고 scheduler가 해당 프로세스나 스레드를 CPU에 넣어 작업을 처리한다.
- 다른 프로세스나 스레드를 실행하는 context switch로 인한 overhead가 존재함.

<br>

### Semaphore

- Lock은 오직 2개만 mutual exclusive하게 돌아가는 반면, Semaphore는 lock보다 높은 수준에서 synchronization을 제공하는 primitive이다. (A synchronization primitive higher level than locks)
  - Invented by Dijkstra in 1968, as part of the THE OS

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85353585-8d859680-b543-11ea-9fb6-0d9ba45eab1b.png'>
</p>

- Has an integer value S indicating its state
  - Determines the behavior of semaphore operations
  - Up to S tasks can grab the semaphore simultaneously (초기값 S만큼의 entitiy가 동시에 resource를 잡을 수 있음)

- Operations
  - `Wait()` : decrease S by one, and wait until `S >= 0`
    - `S < 0` 일 경우 계속 wait, `S >= 0` 일 경우에 진행
    - P() (proberen(test), passeren(pass)), down(), or sem_wait()
  - `Signal()` : increase S by one
    - V() (verhogen(increment)), up(), or sem_post()

#### Implementing Busy-waiting Semaphore

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85354262-21a42d80-b545-11ea-99ed-6ded84288db5.png'>
</p>
<br>

- `wait()` 와 `signal()`은 atomic하지 않다.
  - 서로 다른 스레드들이 내부로 동시에 들어와 동작할 경우, 정상적으로 동작하지 않는다.
  - 해결법 : 각 함수마다 synchronization method (mutex)를 넣는다.

#### Implementing Blocking Semaphore

<br>
<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/85354594-e35b3e00-b545-11ea-965f-e218f70ae588.png'>
</p>
<br>

#### Types of Semaphores

- Binary semaphore (≈ mutex)
  - Semaphore value is initialized to 1 (S값을 1로 주면 Lock이랑 같아진다.)
  - Guarantee mutually exclusive access to resource

- Counting semaphore
  - 어떠한 critical section 안에 주어진 개수만큼의 task만 동시에 진입할 수 있도록 만들어 주는 것.
  - Semaphore value is initialized to N
  - Represent a resource with many units available
  - Allow threads to enter as long as some units are available

## 참고

- [Atomic Operation이란?](https://mygumi.tistory.com/111)