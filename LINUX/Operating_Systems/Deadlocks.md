# Deadlocks

<br>
<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/85934471-d6f82c00-b91d-11ea-9d14-ba6e4a6fe50c.png'>
</p>
<br>

## Resources

- A system consists of **resources**
  - CPU cycles, memory space, I/O devices, object, lock, buffer …
  - Can be hardware or software
- Each process/task utilizes a resource as;
  - Acquire, use, and release

> 서로 상대방이 가지고 있는 lock을 `acquire()` 하려는 상황 → Deadlock 발생

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85934453-84b70b00-b91d-11ea-9fd3-ad35597613ad.png'>
</p>
<br>

## Deadlock Problem

- Deadlocks can be happened when;
  - 두 개 이상의 Task들이 서로서로 **한 개 이상의** Resource Type·instance를 요청 (Tasks may require more than one type/instance of resources)
  - 어떤 Task가 어떤 Resource를 잡고 있고, 또 다른 Task가 그 Resource를 필요로 해서 allocate되기를 기다리고 있는 경우 (Some **blocked tasks holding a resource** are **waiting for a resource held by another tasks**)

- Deadlocks are everywhere
  - Can be happened between any two active instances requiring multiple resources
    - Process and process, thread and thread, car and car, train and train, among people,  …

## Conditions for Deadlock

> **4가지 조건을 모두 만족**해야만 데드락이 발생한다.

- **Mutual exclusion**
  - Only one task at a time can use a resource

- **Hold and wait**
  - A task holding at least one resource is waiting for additional resources held by other tasks
  
- **No preemption**
  - A resource can be released only voluntarily by the task holding it, after that task has completed its task

- **Circular wait**
  - Must exist a set of waiting tasks {P0, … Pn} such that P0 is waiting for a resource held by P1, P1 is for P2, … and Pn is for P0

## Strategies for Handling Deadlocks

- **Deadlock prevention**
  - Ensure that at lease one necessary condition cannot be hold
  - Restrain how requests for resources can be made
  
- **Deadlock avoidance**
  - Decide to approve or disapprove requests on the fly

> `↑ Deadlocks never happen`
---------------------------------------------------------------------
> `↓ Deadlocks may happen`

- **Deadlock detection and recovery**
  - Allow the system to enter a deadlock state and then recover

- Just **ignore** the problem altogether

## Deadlock Prevention

- Deadlock **only happens when all the necessary conditions** are satisfied
  - Mutual exclusion, hold and wait, no preemption, and circular wait

- We can prevent deadlocks by preventing one of the conditions

### Mutual exclusion

> Mutual exclusion 프로퍼티를 없애는 것은 불가능하거나 매우 어렵다.

- Non-sharable resources should be allocated mutual-exclusively
- Thus, **impossible to deny the mutual exclusion** condition for intrinsically non-sharable resource

### Hold and wait

> resource를 모두 가지고 있거나 아무것도 가지고 있지 않을 때만 resource를 요청할 수 있도록 함.<br>Hold and wait property 자체를 제거

- Guarantee that whenever a process requests a resource, it does not hold any other resources
  - Require process to request and be allocated all its resources before it begins execution
  - Allow process to request resources only when the process has none

- Problems
  - Low resource utilization
  - Starvation is possible
    - 26개의 리소스를 필요로하는 Task0와 3개의 리소스를 필요로하는 Task1이 있다고 가정
    - Task0는 리소스를 모두 잡기도 전에 Task1이 리소스를 가져가버려서 계속해서 계속 preemption되면 starvation 발생

### No preemption

> preemption이 되고 안되는 resource가 있고 (lock은 preemption이 되게 되면 제대로 동작하지 않음), preemption property를 없애는 것은 mutual exclusive property를 없애는 것만큼 어려운 작업이기 때문에 범용적으로 사용되지 않는다.

- No preemption
  - If a process must wait for another resource, all resources currently being held are implicitly released
  - If the requesting resources are allocated to some other process that is waiting for additional resources, preempt the desired resources from the waiting process
- Only applicable to the resources whose state can be easily saved and restored later
  - E.g., CPU registers, memory 


### Circular wait

> 리소스를 잡는 순서를 리소스의 크기가 작은 것부터 큰 것 순서대로 한 방향으로만 lock을 잡아가도록 만들면 circular wait를 방지할 수 있다.

- Impose a **total ordering** of all resource types
- F: R → N, where;
  - R = {R1, R2, ..., Rn} is the set of resource types
  - N is the set of natural numbers
  - E.g., F(disk)=1, F(usb)=5, F(printer)=8, etc.
- Each process requests resources in an increasing order of enumeration
- Whenever a process acquired an instance of Ri, it will not request for any
resources Rj such that F(Rj) <= F(Ri)

> Two-phase locking (2PL)

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85934820-7d92fb80-b923-11ea-8036-fac4b82e4d41.png'>
</p>
<br>

> ### Discussion of Deadlock Prevention

- **Witness** : Resource allocation을 모니터링하고 있고 4가지 조건을 성립하는지 검사를 하고 있으면 해당 시스템이 데드락을 발생시킬 수 있는지 없는지를 알아낼 수 있다. (You can implement a witness to check possible-deadlocks by tracking the order of lock acquiring)
  - Linux kernel에서 구현되어 있음

## Deadlock Avoidance

- Assume the system is in the **safe state** initially, and
- The system avoids entering the unsafe state

### Safe state

- The system is in the **safe state** if the system can allocate resources to each process in some order and still avoid a deadlock
- 현재 리소스 상황이 있고, 각 프로세스들이 필요로하는 리소스의 할당 utillization,scheduling이 존재. 이에 맞게 리소스를 잘 나누어주어 모든 프로세스가 문제 없이 잘 실행되는 resource allocation 순서를 **safe sequence**라고 한다.
  - safe sequence가 하나라도 있으면 safe state라고 한다.
- safe sequence가 없다면 특정 프로세스가 resource를 요청해도 resource allocation이 가능해질때까지 기다린다.

### Avoidance Algorithms

#### Resource-Allocation Graph

> trivial한 방법이라 간단히 짚고 넘어감

- If graph contains no cycle(circular wait) → No deadlock

<br>
<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/85935084-740b9280-b927-11ea-9a73-5f85fe045e4c.png'>
</p>
<br>

#### Banker’s Algorithm

> Process들이 resource allocation program을 가지고 있고, 이에 따라 resource를 가져가서 모두 사용하고 나면 해당 resource를 한번에 release한다. 만약 resource를 process에게 allocation 해주려하는데 unsafe-state로 가게되면 allocation해주지 않고 가능할때까지 프로세스는 대기해야 한다.

- Developed by Dijkstra, 1965

- For resources having multiple instances

- What bankers do to lend money to customers
  - An extended and formulated version of the previous example

- Assumption
  - Each process must a priori claim maximum use
  - When a process requests a resource, it may have to wait
  - When a process gets all its resources, it must return them within a finite time frame

- **Check every time if granting a request can lead to an unsafe state**

- Safety checking algorithm
  - Choose a process whose resource needs are all smaller than or equal to the available resources
  - If there is no such process, the state is unsafe
  - If there is, assume the process requests all the resources it needs and finishes
  - Mark that process as terminated and add all its resources to the available resources
  - Repeat until all processes are marked terminated

<br>
<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/85935228-622aef00-b929-11ea-8956-7d37bf144756.png'>
</p>
<br>

- 장단점
  - Processes can dynamically join and leave
    - Like we can open and close bank accounts dynamically
  - So does for the resources
    - Adding: Always allowed
    - Removal: As long as the removal does not lead to the unsafe state
  - However, processes rarely know in advance what their maximum resourceneeds will be
    - resourceneeds를 미리 알기가 어렵다


## Deadlock Recovery

> 프로세스의 데드락이 발생을 탐지한 후 그것을 해결함. 현실적으론 사용이 힘들다.

### Process termination

- Reclaims all resources allocated to the terminated processes

### Resource preemption

## Ignoring Deadlock

### The “Ostrich” algorithm

<br>
<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/85935451-df576380-b92b-11ea-88af-4c0d08c6e9df.png'>
</p>
<br>

- 많은 모던 OS들이 취하고 있는 접근 방법
- 합리적이다
  - Deadlocks occur very rarely
  - Cost of prevention is high
- Trade-off between convenience and correctness
