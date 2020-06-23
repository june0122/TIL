# Process scheduling (5) - Issues in Process Scheduling

## Multiple-Processor Scheduling

> Symmetric : 대칭형

- Asymmetric multiprocessing
  - **Master 프로세서 <sup>node</sup>** 가 각각의 프로세서에게 프로세스를 할당하고 load balanciong을 한다.

- Symmetric multiprocessing <sup>SMP</sup>
  - 모든 프로세서들이 독립적으로 각자 자신의 상황에 맞춰 처리를 한다. <sup>self-scheduling</sup>
  - 모든 프로세서가 하나의 ready-queue를 공유하여 처리하는 경우와 각각의 프로세서가 자신만의 ready-queue를 가지고 처리하는 경우가 있음
  - 현재 Asymmetric보다 Symmetric이 일반적으로 사용되어지는 방식

### NUMA and CPU Scheduling

> NUMA Architecture (Non-Uniform Memory Access, 불균일 기억 장치 접근)

- 멀티프로세서 시스템에서 사용되고 있는 컴퓨터 메모리 설계 방법중의 하나로, 메모리에 접근하는 시간이 메모리와 프로세서간의 상대적인 위치에 따라 달라진다.
- 각각의 CPU마다 별도의 메모리가 있는데, 이와 같이 메모리에 접근하는 방식을 Local Access 라고 한다. 그리고 이렇게 CPU와 메모리를 합쳐서 노드 <sup>Node</sup>라고 부른다. NUMA에서는 자신의 메모리가 아닌 다른 노드에 있는 메모리에도 접근할 수 있으며 이것을 Remote Access라고 부른다.

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85232005-ef0c0f00-b436-11ea-9553-6b8b6d56e083.png'>
</p>
<br>

- NUMA architecture에서는 process scheduling을 할때, 특정 노드가 포화 상태라고 프로세스를 아무 노드로 이동시켜서 처리하면 안된다!
  - 해당 프로세스의 메모리가 이동하기 전 노드의 local 메모리에 남아있기에 CPU의 활용도는 높아질수 있어도 다른 노드의 메모리에 접근 <sup>Remote Access</sup> 하는 것이기 때문에 메모리 접근 성능이 급격히 떨어질 수 있다.
  - 그렇기 때문에 최근의 NUMA architecture를 지원하는 CPU scheduler들은 프로세스를 새로 만들때 어디에 만들지를 고려하고, 노드가 돌 때 memory allocation을 어디에다 어떻게 할지 모두 고민해야한다.
  - 어떠한 프로세스를 실행할 때, 어떤 CPU(프로세서)에서 돌릴지 우선순위를 정해주는 기능들을 modern operating systems에서 지원 → **Processor Affinity**

### Processor Affinity <sup>선호도</sup>

- 프로세스는 현재 실행되고 있는 프로세서에 Affinity를 가지고 있다.
  - Soft affinity : Affinity가 100% 보장되진 않지만 우선순위를 고려해주는 시스템
  - Hard affinity : 지정된 노드에서만 프로세스가 실행
- *c.f.* Linux의 Processor Affinity : `Taskset`

### Load Balancing

> 어떠한 노드에 task가 너무 많이 assign되어 다른 노드에 비해 많은 computing을 한다면, 노드의 일부 내용을 빼서 다른 노드로 옮겨주는 역할을 함

- migration : 어떤 프로세서에 mapping되어 있던 프로세스를 다른 프로세서로 옮겨주는 것

#### Push migration

- overload된 프로세서가 다른 프로세서에게 task를 push하는 것
- 자신의 overload된 task를 처리하는 것도 바쁜데 다른 프로세서의 상태를 확인하고 push하는 것은 쉽지 않음 → Pull migration이 더 많이 사용되는 이유
- load가 안정적으로 돌아가지 않고 oscillation<sup>진동</sup> 하는 가능성이 잘 발생할 수 있다.

#### Pull migration

- Idle 상태의 프로세서가 overload가 되어있는 프로세서에서 waiting task를 가져오는 것
- 현재 더 많이 쓰이는 방식

### Multicore Processors

- 멀티코어 프로세서에서 코어들은 독립적으로 자신의 일을 처리하는 것이 아닌, 어느 정도 system resource를 공유하며 돌아간다.
  - E.g. the last level cache <sup>LLC</sup>
- 스케줄러가 멀티코어 구조를 충분히 활용하기 위해서는 위와 같은 코어의 편성 <sup>organization</sup>을 고려해야한다.
- 하나의 코어에 여러 개의 스레드 : **하이퍼스레딩** (hyperthreading in Intel)
  - Memory stall: 프로세서가 메모리에 접근할 때, 메모리를 사용할 수 있을 때까지 많은 시간이 소모된다.
  - Memory stall을 해결하는 하이퍼스레딩 : 각 프로세서가 두 개 이상의 thread를 가지도록 해서 하나의 thread에 다른 thread를 interleave해서 Memory stall cycle일 때, 다른 thread에 Compute cycle을 돌려서 하나의 코어를 바쁘게 유지시킴
    - interleave : 성능을 높이기 위해 데이터가 서로 인접하지 않도록 배열하는 방법

<br>
<p align = 'center'>
<img width = '550' src = 'https://user-images.githubusercontent.com/39554623/85233566-58454f80-b442-11ea-9e8e-512ea725c4bd.png'>
</p>
<br>

## Schedulers

### Short-term scheduler (or CPU scheduler)

- 어떤 프로세서에다가 어떤 프로세스를 mapping해서 돌릴지를 처리하는 스케줄러
- 빠르고, 오버헤드가 낮게 구현이 되야함
  - Invoked frequently, in the order of milliseconds

### Long-term scheduler (or job scheduler)

- job을 CPU에 넣어서 돌릴지 말지, 어떤 CPU로 job을 mapping해서 돌릴지를 처리하는 스케쥴러
  - the degree of multiprogramming(어떤 CPU에 job을 몇 개까지 돌릴 것인가)를 결정

### Medium-term scheduler

- 어떤 프로세서(CPU)에 job이 너무 많이 load 되어 있어서 일시적으로 빼는 스케줄러 (the degree of multiprogramming를 낮춰주는 스케줄러)
- Remove process from memory, store on disk, bring back in from disk to continue execution : **swapping** 을 통해 처리

## Multitasking in Mobile Systems

> foreground & background : Multi level Queue, Multi level feedback queue의 구조와 유사 → mobile system에서 적극적으로 사용

- Usually, single **foreground** process owns the screen estate
- Multiple **background** processes run, but not on the display

## Algorithm Evaluation

- Deterministic evaluation
- Queueing Models
- Simulation
- Implementation

## 요약

- Advanced scheduling topics
  - Proportional share scheduling
  - Real-time CPU scheduling
    - RMS / EDF
- Issues in scheduling
  - Processor affinity
  - Load balancing
  - Multiprocessor/multicore systems
- Short-term vs long-term vs medium-term schedulers
- Algorithm evaluation

## 참고

- [NUMA 메모리 관리 아키텍처](https://12bme.tistory.com/537)