# Process scheduling (4) - Advanced Scheduling Topics

- **Advanced Scheduling Topics**
  - Proportional share scheduling
  - Real-time CPU scheduling
- Issues in process scheduling
  - Processor affinity
  - Load balancing
  - Multiprocessor/multicore systems

## Proportional Share Scheduling

- 모든 프로세스들에게 T 개의 share <sup>시간 지분</sup>를 할당하여 동작한다.
- 한개의 프로세스가 N개의 share를 할당 받으면, 그 프로세스는 모든 프로세스 시간중 N/T 시간을 할당받게 된다.

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85228787-9df12080-b420-11ea-9134-0da730a8492b.png'>
</p>

### Example 1

> Linux의 scheduling policy인 CFS가 이 방식과 유사

- Recall the inter-queue scheduling example in MQ <sup>Multi Queue</sup>
- Initially give `n` credits to jobs
  - `n` might vary among processes
- Give n time quanta for a process
- Deduct one credit from a scheduled process
- Repeat until all jobs do not have a credit
- Re-distribute credits to processes

### Example 2 : Lottery scheduling <sup>복권</sup>

- The scheduler has [1. . N] <sup>N개의 shares</sup>
- Divide [1. . N] into P partitions where P is the number of processes
  - E.g. `N` = 10, `P1` = [1 . . 5], `P2` = [6 . . 8], `P3` = [9 . . 10]
- Each "number" is a ticket
  - The larger partition a process has, the more ticket the process has
  - 각 프로세스마다 티켓을 나눠준 개수에 비례해서 스케쥴될 확률이 결정됨

<br>

- The scheduler generates a random number from [1. . N] (lottery)
- Schedule the process having the number

## Real-time Systems

> 각각의 task들이 실행이 완료되어야 하는 **deadline이 있는 시스템**

### Soft real-time systems

- non-real-time 프로세스와 real-time 프로세스들이 혼재되어 있는 시스템
- real-time 프로세스가 non-critical 프로세스보다는 우선권 <sup>preferecne</sup>이 있어 deadline안에 처리되도록 노력은 하지만 **보증은 없다.**

### Hard real-time systems

- deadline이 반드시 준수되어야 함.
- E.g. 항공 전자 공학, 자율 주행, 로봇, 원자력 발전소 등

## Real-time CPU Scheduling

- **priority-based에 preemption을 지원해야 수월하게 구현이 됨**
  - preemption이 없이는 deadline을 만족시키기가 힘듬
- soft real-time만을 보증함
  - 수많은 알고리즘들이 best-effort 방식으로 보증을 한다
- 일정 주기를 가지고 <sup>periodic</sup> 반복해서 돌아가는 시스템이 일반적
  - 일정한 interval<sup>주기</sup> : `p` / processing time : `t` / deadline : `d`
    - 0 ≤ t ≤ d ≤ p
    - 단위시간 당 오는 프로세스의 개수 <sup>Rate of the periodic process</sup> : 1/p

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85230004-38089700-b428-11ea-8b1d-3e5ed3d8289c.png'>
</p>
<br>

### RMS <sup>Rate Monotonic Scheduling</sup>

> Rate : period의 역수 <sup>1/p</sup>, the inverse of its period

- 각 프로세스의 rate를 가지고 rate가 가장 높은 프로세스부터 스케쥴링을 하고, 필요시 preemption을 함
- deadline이 period의 값과 같다 (`d = p`)
  - The shorter periods(자주 도착하는 프로세스일수록, rate가 높을수록), the higher priority

#### Example 1

- P1은 50초마다 1번씩 도착하고, 처리하는데 20초가 걸림
- P2은 100초마다 1번씩 도착하고, 처리하는데 35초가 걸림
  - P1 CPU Utilization = 20 / 50 = 40 %
  - P2 CPU Utilization = 35 / 100 = 35 %
  - CPU Utilization = 40 + 35 = 75 %

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85230296-2f18c500-b42a-11ea-99c8-aeac3bb5e511.png'>
</p>
<br>

- P1이 P2보다 rate가 높음 (1/50 > 1/100)
- P1과 P2가 동시에 도착했을 때, rate가 높은 P1이 20초동안 작업을 모두 처리한 다음에 P2가 작업을 시작
- 작업에 35초가 소모되는 P2이지만 처음 시점으로부터 50초가 지난 시점에 다시 P1이 도착하므로 5초의 작업을 남겨두고 preemption함.
- P1의 작업이 모두 끝나고나서야 P2는 남은 5초간의 작업을 마무리
- 100초 뒤부터 똑같은 패턴을 반복

#### Example 2

- P1 (p = 50, t = 25), P2 (p = 80, t = 35)
  - P1 CPU Utilization = 25 / 50 = 50 %
  - P2 CPU Utilization = 35 / 80 = 43 %
  - CPU Utilization = 50 + 43 = 93 %

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85230300-3049f200-b42a-11ea-8ddf-10df1e798010.png'>
</p>
<br>

#### RMS의 특징들

- Theoretically, the worst-case CPU utilization for scheduling N processes with RMS is N * (2<sup>1/N</sup> - 1)
  - N : 현재 주어진 프로세스들의 Set에 들어있는 프로세스의 개수  
  - CPU utilization `U` = Process time / Period
    - E.g., P1 (t=20, p=50) → 20 / 50 = 40%
  - N = 2 일때, 83 %
  - N = ∞ 일때, 69 %로 수렴
  - `U > the worst-cast utilization` : 주어진 프로세스들의 CPU Utilization의 합이 worst-cast utilization (N * (2<sup>1/N</sup> - 1)) 보다 크면 RMS가 스케쥴링이 안되는 경우가 발생할 수 있다. 역은 성립하지 않음.
    - 따라서 위의 Example 2의 U는 93%이므로 RMS로 스케쥴링이 보증되지 않을 수 있음

### EDF <sup>Earliest Deadline First</sup>

- 매 프로세스들이 시스템에 도착했을 때, 각각의 deadline을 보고 **deadline이 가까운 프로세스**의 priority를 높게 설정하여 우선적으로 처리한다.
- 대학교 과제나 프로젝트의 마감 기한을 고려하며 진행하는 학생이 예시가 될 수 있음.
- 예시
  - P1 (p = 50, t = 25), P2 (p = 80, t = 35)

<br>
<p align = 'center'>
<img width = '450' src = 'https://user-images.githubusercontent.com/39554623/85231438-e06f2900-b431-11ea-8b6f-9b459e8eff50.png'>
</p>
<br>

- deadline만 가지고 있는 task들도 잘 처리함
  - period나 일정한 CPU burst time이 필요하지 않음
- 이론적으로 최적 (Theoretically optimal)
  - EDF로 스케줄링했을때, CPU untilization을 100%로 끌어올리지 않고도 deadline을 잘 맞출 수 있다는 것이 증명되어 있음.