# Process scheduling (1) - Sceduler

> 여러 개의 프로세스를 그것보다 적은 수의 프로세서로 맵핑을 해서 concurrent하게 실행시키는 방법

- 운영체제 내부의 **스케쥴러(scheduler)** 를 이용하여 프로세스의 context를 변경한다. 각각의 프로세서에게 가상의 프로세스가 가지고 있는 **CPU와 메모리(illusion)** 를 제공한다.
- 내부적으로는 concurren하게 처리되지만 크개 봤을때는 parrell하게 처리되는 것처럼 보여주고, 그렇게 함으로써 CPU를 더욱 빡빡하게 사용할 수 있게 만들어준다.
- 프로세스 스케쥴러는 **현재 사용 가능한 프로세스(ready process)** 중에서 어떤 프로세스를 실행할지 선택해준다.
  - 프로세스의 목록을 관리하고 그 중 어떤 프로세스가 준비가 되었는지 확인
  - 다음에 어떤 프로세스를 실행할 것인가.
  - 프로세스 간의 illusion을 어떻게 switch 할 수 있는가.

## 2가지의 Process Execution Characteristics

### CPU-bound 프로세스

- 긴 CPU burst를 가지고 있고 중간중간에 I/O를 위한 짧은 Idle한 구간이 있다.

### I/O-bound 프로세스

- CPU burst가 짧고 중간중간에 긴 I/O wait time을 가진다.

## CPU Switch from Process to Process

<br>
<p align = 'center'>
<img width="400" src = 'https://user-images.githubusercontent.com/39554623/84634975-8819c200-af2d-11ea-9566-4b5f369f2169.png'>
</p>
<br>

- 해당 프로세스의 PCB에 state를 저장한다. (process P0의 상태는 PCB0에 저장)
- state를 저장하고 불러오는 과정은 운영체제 내부에서 처리된다.

## Context Switch

- CPU가 다른 프로세스로 switch할 때, os는 **현재 프로세스의 상태를 저장**하고 **다음 프로세스의 상태를 불러와야한다**.
- Context의 save와 load는 PCB에서 이루어진다.
- Context-switch를 너무 자주하게 되면 overhead가 발생한다.
  - OS는 적당한 context switch time을 가져야한다.
- 어떤 하드웨어(UntraSPARC)는 복수의 register set을 가지고 있어 여러 개의 context를 CPU 안에 유지할 수 있다. 이러한 방법을 통해 context switch overhead를 줄이려는 시도를 하였다.


### Scheduling points

<br>
<p align = 'center'>
<img width="450" src = 'https://user-images.githubusercontent.com/39554623/84637374-93222180-af30-11ea-87e3-6c3e6e39abc2.png'>
</p>
<br>

- 모든 프로세스의 state transition에는 OS의 process scheduler가 관여해야 한다.
  - 프로세스의 상태를 잘 관리해야하는데 ready queue와 I/O queue를 이용한다.

### Process Scheduling Overview

<br>
<p align = 'center'>
<img width="450" src = 'https://user-images.githubusercontent.com/39554623/84637908-3ecb7180-af31-11ea-945e-3a061925d775.png'>
</p>
<br>

- ready queue의 내부 구현은 Queue 혹은 Linked list인데, 일반적으로 **Linked list로 구현**된다.

<br>
<p align = 'center'>
<img width="450" src = 'https://user-images.githubusercontent.com/39554623/84639361-3b38ea00-af33-11ea-8628-d896b51fefa1.png'>
</p>
<br>