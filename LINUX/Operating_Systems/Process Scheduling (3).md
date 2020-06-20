# Process scheduling (3)

## Priority Scheduling

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85179806-ccee8180-b2bc-11ea-9e81-0e651ba838a7.png'>
</p>

- 특징
  - 작업에 Priority <sup>우선순위</sup>를 매김
  - 가장 높은 Priority에 CPU를 할당
  - preemptive / non-preemptive 두 가지 모두 가능
- 단점
  - **Starvation problem**
    - 높은 priority의 작업이 끊임없이 올 경우, 낮은 priority의 프로세스들은 절대 실행되지 않을수도 있다
    - 해결책 : **Priority boosting** 또는 **Aging** → priority를 동적으로 조정하여 starvation 문제를 해결
      - Aging : Increase the priority of the process as it gets aged
  - [**Priority inversion problem**]()
    - low priority의 task가 high priority의 task가 필요로하는 resource를 hold하고 있을 때, 제 3자인 또다른 task(high priority task보다 priority 낮음)가 high priority task의 진행을 간접적으로 방해, 즉 preemtion 시켜버린 상황
    - Mars Pathfinder and Sojourner (1997) 예시

  <br>
  <p align = 'center'>
  <img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85183533-06c48580-b2c7-11ea-9661-18c50bc9e725.jpg'>
  </p>
  <br>

  <br>
  <p align = 'center'>
  <img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85181919-22795d00-b2c2-11ea-86e6-a8be3bd7dd73.png'>
  </p>
  <br>

  > A : Bus management task / B : Communications task / C : Meteorological data gathering task

  - A task는 C의 task이 끝날때 까지 lock을 얻지 못해 기다리는데 C task중 B에 의해 선점당하면서 A는 더 오랜 시간을 기다려야 한다.
  - A task가 B task보다 우선 순위가 높은 상황인데도 위와 같은 상황에서 우선 순위가 더 낮은 B task가 먼저 처리되는 불합리한 상황이 벌어진다.
    - cf) Watchdog Timer(WDT) : 시스템을 주기적으로 감시하며 시간을 쟤는 계수 회로. 일정 시간동안 입력값을 받지 못 하는 경우 시스템의 오동작 상황으로 간주하고 초기화 수행

### Solution for Priority Inversion

#### Priority inheritance protocol (PIP)

- high priority task가 low priority task가 사용하고 있는 resourse가 필요할 때, **낮은 priority를 가지고 있는 task의 priority를 high priority task의 레벨만큼 높여준다**
- resourse를 release했을 때는 priority를 원래대로 복구시켜야 한다

> 이미지의 오른쪽이 PIP를 적용시켰을 때의 예시

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85182785-ae8c8400-b2c4-11ea-87f7-48db205e9db8.png'>
</p>
<br>

- Priority Inversion 상황과 다르게 A task가 lock을 얻다 실패하는 경우 현재 해당 리소스 lock을 얻어 동작하는 C task의 우선 순위를 A task와 같이 높은 우선 순위로 상속시키면서 그 보다 낮은 우선 순위의 B task에게 선점되지 않게 막는다. 결국 A task는 보다 빠르게 Shared resourse를 할당을 받아 처리할 수 있다.


#### Priority ceiling protocol (PCP)

- low priority task가 **shared resourse를 잡을 때** 우선순위를 순간적으로 크게 올려주고, resourse를 모두 사용하고 난 뒤에 우선순위를 원래대로 복구시킨다
- 장점 : 구현이 쉽다
- 단점 : 어디까지 priority를 높여줘야하는지 기준을 결정하기가 어려움

> 따라서 PCP에서는 미래에 누가 resourse를 잡던 말던 관계없이 boosting이 되고, PIP에서는 inherit이 필요한 시점에만 boosting 된다.

## Round Robin <sup>RR</sup>

- Run queue is treated as a circular FIFO queue
- Each job is given a **time slice (or scheduling quantum)**
  - Too short → higher context switch overhead
  - Too long → less responsive (너무 길면 FIFO랑 유사해지게 됨)
  - Usually 10 – 100ms (대부분의 프로세스들이 사용하는 CPU burst time이 10 – 100ms이면 충분)

> Example with quantum = 4

<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85185495-22cc2500-b2cf-11ea-8afa-3ce1c4363e8c.png'>
</p>
<br>

- Preemptive
- No starvation
- Improve response time: great for time-sharing

#### ※ RR에 Priority Scheduling을 적용할 수 있다. 한 번 생각해볼 것.

## Multilevel Queue Scheduling

- There are different types of jobs in the system
- Partition the ready queue into separate queues
  - Foreground queue, background queue, …
- Assign jobs to the queue according to its characteristics
  - Foreground for interactive, background for batch
- Each queue has its own scheduling algorithm
  - RR for foreground, FCFS for background

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85185484-18aa2680-b2cf-11ea-90e2-4174f5ebb407.png'>
</p>
<br>

- 주의점 : Must consider the scheduling between the queues (큐 간의 scheduling policy를 고민해야 한다)
  - Fixed priority scheduling
  - Time slice
- 문제점 : It is hard to predict the workloads of jobs (작업의 특징을 미리 판별하는 것이 쉽지 않음)
  - 개선 : 작업을 조금 돌려보고 상황에 맞춰 판별하는 것은 어떤가? → MLFQ

## Multilevel feedback queue <sup>MLFQ</sup>

- 작업이 들어오면 일단 적당한 상태의 queue에 넣어두고, 해당 작업의 행동을 본 뒤 그에 맞게 다른 queue로 옮겨준다

<br>
<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/85186233-da166b00-b2d2-11ea-8a86-9884eaa64ab2.png'>
</p>
<br>

> 상황에 맞게 queue를 옮겨가며 작업을 처리한다

- Q0 – RR with time quantum 8 milliseconds
- Q1 – RR time quantum 16 milliseconds
- Q2 – FCFS

## Linux Scheduler

> CFS <sup>Completely Fair Scheduler</sup> 방식으로 동작함

- **MLFQ**
  - **Preemptive** priority scheduling
  - Time-shared based on time slice **(타임 슬라이스를 가진 RR 방식으로 동작)**
  - Processes **dynamically change priority**
  - 3~4 classes spanning ~170 priority levels
- Favor interactive processes over CPU-bound processes
- **Use aging: no starvation**
  - Increase priority as a function of wait time
  - Decrease priority as a function of CPU time
- Many ugly heuristics for voo-doo constants