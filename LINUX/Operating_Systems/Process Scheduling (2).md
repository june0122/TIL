# Process scheduling (2)

## [Starvation]()

- 프로세스에게 CPU를 할당하지 못하여 프로세스가 진행을 하지 못하고 기다려야 하는 상황
- 낮은 품질로 설계된 스케쥴링 정책이 starvation을 야기할 수 있다.

## [Preemptive Scheduling]()

> Preempt : to replace with something considered to be of greater value or priority; take precedence over

### Non-preemptive Scheduling

- 어떤 프로세스가 **CPU를 자발적으로 반환할 때까지 기다려주는** 스케쥴링
  - 프로세스끼리 서로 협력을 해가면서 동작을 하는 경우가 많다.

### Preemptive Scheduling

- 프로세스가 사용(점유)하고 있는 **CPU를 다른 프로세스에게 강제적으로 넘겨줄 수 있는** 스케쥴링

## Scheduling Algorithm Goals

- Starvation이 발생하지 않음
- Fairness : 프로세스들이 CPU를 공정하게 나눠가짐
- Balance : 여러 개의 CPU들이 모두 고르게 동작하도록 함

### Batch & Interactive Systems

- Batch systems(일괄 처리 시스템) : Throughput, CPU utilization 등이 중요
  - 한 번에 하나의 프로그램만 수행하므로 가능하면 많은 일을 수행하도록 스케줄링 해야 한다.
  - 예) 머신러닝과 같이 반응은 빠르지 않아도 되지만 최대한 많은 데이터를 처리해야하는 작업
- Interactive systems : Response time이 중요

### Scheduling Criteria <sup>특성</sup>

- CPU utilization
  - CPU busy-ness(CPU를 얼마나 많이 쓸 것인가)
- Throughput
  - 단위 시간 당 완료된 Task의 수

`↑ 값이 커질수록 성능 좋음`

--------------------

`↓ 값이  낮을수록 성능 좋음`

- Turnaround time
  - 특정 프로세스를 실행하여 완료가 될 때까지 걸리는 시간
- Waiting time
  - ready queue에서 프로세스가 대기하는 시간
- Response time
  - 프로세스가 응답을 할 때까지 걸리는 시간

## FCFS/FIFO <sup>First-Come, First-Served Scheduling</sup>

- 특징
  - Ready Queue에 들어온 순서대로 스케줄링
  - 일반적으로 **Non-preemptive Scheduling**
  - 작업이 동등하게 처리되므로 **starvation이 없음**
- 단점
  - **Convoy effect** <sup>호위 효과</sup> : 작업 시간이 짧은 프로세스라도 작업이 오래 걸리는 프로세스가 앞에 있다면 Average Turnaround time이 비대해진다.
  - Poor at I/O

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85176385-a9bfd400-b2b4-11ea-854b-fb93c3c5b09c.png'>
</p>
<br>

- 먼저 온 프로세스를 처리했을 때, 앞에 작업이 큰 프로세스가 들어오면 Convoy effect가 일어나니깐 CPU를 적게 소모하는 프로세스부터 미리 처리해보면 어떨까? → SJF <sup>Shortest Job First</sup>

## SJF <sup>Shortest Job First</sup>

- 특징
  - 가장 실행이 짧게 걸리는 프로세스부터 처리
  - average waiting time을 최소화할 수 있는 방법
  - Non-preemptive Scheduling
- 단점
  - **Starvation 발생할 수 있음** : 계속해서 task가 작은 프로세스가 올 경우, task가 큰 프로세스는 CPU를 할당받지 못하고 Starvation이 일어날 수 있다.
  - SJF 스케줄링은 프로세스들을 처리하는데 소모되는 **미래의** CPU burst time을 알아야하는데 **현실적으론 불가능하다**

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85176919-e6d89600-b2b5-11ea-9c9f-4fed4ecea253.png'>
</p>
<br>

## SRTF <sup>Shortest Remaining Time First</sup>

- 특징
  - 새로운 작업이 도착했을 때, 전체 작업들 중에서 **남아있는 실행 시간이 제일 짧은 작업부터** 처리
  - **A preemptive version** of SJF
- 단점
  - SJF와 마찬가지 이유로 Starvation이 발생할 수 있음

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85177913-1ab4bb00-b2b8-11ea-9fd5-64389564509e.png'>
</p>
<br>