# Chapter 4.2 What's inside a router

bottleneck은 routing processor에서 생긴다. routing processor는 nanosecond가 소요되는 hardware에 비해서 millisecond로 느리다. forwarding 하는 시간에 비해 오랜시간을 소요해서 processing을 한 다음, 어디로 보내야할지 결정해야하다보니 input port 쪽의 queue에는 datagram이 쌓이고 queueing delay가 발생하게 된다.


## Input port functions

> datagram이 들어오면 physical layer(line termination) → data link layer → queue → switch fabric 으로 밑의 계층부터 하나씩 processing 된다.

- physical layer
- data link layer
- decentralized switching
  - queue에 datagram이 있는 동안, 첫 번째 데이터부터 IP header를 읽어들여 look up(주소에 대한 테이블에서 어디로 가야할 것인지를 찾는다)을 한 뒤 forwarding을 한다.
  - input port에 들어온 것을 hearder field의 value를 이용해서 forwarding table을 보고 어느 output port로 가야할 지를 찾는다.
    - forwarding table은 input port memory에 존재한다.
    - datagram이 switch fabric으로 forward되는 시간보다 더 빠른 속도로 datagram이 도착하게 될 경우 결국 queueing이 된다.

### 메모리를 읽어 forwarding하는 두 가지 방식

#### 1. destination-based forwarding
   - 전통적으로 사용되는 방식 

- forwarding table에는 해당 범위의 IP 주소에 대해서 어떤 Link Interface로 보내야할 지 명시되어 있다.
- routing 알고리즘을 통해 IP 주소의 범위가 결정되어 어떤 link interface를 통해 나가야 빠르게 갈 수 있는지 학습을 한다.
- 전세계 수많은 IP들에 대해 모두 처리하기 때문에 routing 알고리즘은 오버헤드가 크다. 계속 학습해나가면서 table도 업데이트한다. 특정 국가에서 사용하는 IP 주소의 범위가 대략적으로 나누어져 있다. 

#### Longest prefix matching

- 어떠한 datagram이 들어왔을 때 Destination Address를 읽은 뒤, **가장 길게 매칭되는 entry(longest prefix matching)**를 찾아서 뒤에 어떤 bit들이 있든, 해당하는 Link Interface로 보낸다.
- 주소에 대한 정보들을 ternary content addressable memories (TCAMs)에 저장한다.
  - 한 번의 clock cycle에 하나의 주소를 가지고 있다.
  - Cisco Catalyst 1M(백만개)의 routing table entry까지를 넣을 수 있는 메모리를 정착해놓음.


#### 2. generalized forwarding
   - header field의 set들을 가지고 forwarding 하는 방식이다.

## Switching fabrics

- 패킷을 input buffer에서 output buffer로 전달하는 부분이다.
- switching rate : input에서 output으로 초당 몇 개의 패킷을 보내는 지에 대한 전송률. 이 수치가 높을수록 switch의 성능이 좋다. 하지만 switching rate는 이미 충분히 빠르지만 software쪽에서 가져오는 부분(routing processor)에서 bottleneck이 걸린다.
- N개의 input이 있다면 line의 switching rate는 N배가 된다.

<br>
<p align = 'center'>
<img src = ''>
</p>
<br>

### 3가지 타입의 switching fabric
1. memory : 가장 오래된 1세대 방식
2. bus : line을 공유하는 bus를 통해 한번에 하나씩 보내는 방식
   - bus는 한번에 하나씩만 전송할 수 있다. 동시에 보내면 충돌이 일어난다. 이 문제를 해결하기 위해 crossbar 방식이 나왔다.
3. crossbar : line을 많이 사용하여 동시에 여러 개 보낼 수 있는 방식
   - switching rate는 올라가나 하드웨어 구현 비용이 올라가게 된다.

### 1. memory

<br>
<p align = 'center'>
<img src = ''>
</p>
<br>

- input port의 데이터를 memory에 넣었다가 output port로 보내는 방식. (bus를 2번 가로지른다.)
- memory의 bandwidth에 의해서 속도가 제한된다. router 구조에서 software쪽(메모리)의 속도는 느리므로, memory를 사용한 방식도 결국 느리다는 것을 알 수 있다.

### 2. bus

<br>
<p align = 'center'>
<img src = ''>
</p>
<br>

- bus contention: bus에서는 한번에 하나씩 만 데이터가 전송되어야 한다. 결국 switching rate는 bus의 bandwidth에 의해 결정된다.
- Cisco 5600: Cisco는 전세계의 라우터를 독점하고 있는 기업이다. Cisco 5600에서는 32Gbps의 bus를 붙여놓았다. 그래서 Switching rate가 32Gbps이다. 이 speed로 access network나 enterprise router에서 충분하다고 한다.

### 3. interconnection network (crossbar)

<br>
<p align = 'center'>
<img src = ''>
</p>
<br>

- bus의 bandwidth를 극복하기 위해서 나온 방식. 라인들을 여러 개 교차시켜 동시에 보낼 수 있는 데이터의 흐름을 늘림.
- Cisco 12000 : crossbar를 이용해서 switching rate를 60Gbps까지 끌어올림.

## Input port queueing

<br>
<p align = 'center'>
<img src = ''>
</p>
<br>

- Input port에서 datagram이 들어오는 것이 fabric로 보내는 것보다 빠르다면 input port에서 queueing이 생기게 된다. router의 input buffer가 overflow 날 경우 drop 시켜버린다.
  - packet switching 형태의 인터넷에서 delay가 생기는 원인.
- Head-of-the-Line(HOL) blocking : queue 맨 앞에 있는 것이 line의 head부분인데, 이 head가 나가지 못하면 뒤의 datagram들도 나가지 못하는 것을 말한다.

## Output ports

<br>
<p align = 'center'>
<img src = ''>
</p>
<br>

### buffering

- Output port에서도 Input과 마찬가지로 buffering이 된다. transmission하는 속도보다 들어오는 속도가 빠르면 buffering이 된다. 마찬가지로 손실도 날 수 있다.
- 버퍼링은 얼마나 주어야 하는가? buffer를 너무 많이 써서 queueing delay가 커지게 되면, 사용자 입장에선 손실이 나더라도 drop 시키는게 낫다.
- buffer의 크기는 `RTT * C(capacity)` 로 정한다. 최근의 recommendation은 N개의 flow가 있다면 `(RTT*C)/sqrt(N)` 로 buffer의 크기를 설정하는 것이다.

## Scheduling mechanisms

>  Output port에는 queueing 되어 있는 것들 중에 어떤 것부터 내보내는지에 대한 Scheduling 이슈가 있다.

- 통상적으로 사용하는 스케쥴링은 FIFO(first in first out)이다.

### queue에서 어떤 packet을 drop할지 판단하는 3가지 방법

1. tail drop : 마지막으로 도착한 packet을 drop
2. priority : 우선순위 낮은 packet을 drop
3. **random** : **실제 라우터에서 사용하는 방식**. queue가 거의 찼을 때, 새로운 패킷이 온다면 원래 쌓여있던 packet들 중 랜덤하게 뽑아서 drop.
   - 끝에 온 패킷을 드랍하게 되면, 어떠한 flow에서 데이터를 heavy하게 보냈다면 해당 데이터들이 큐에 쌓여있는 상태인데 다른 flow에서 하나의 데이터를 보냈는데 drop이 되버리면 많은 데이터를 보낸 애들이 큐를 다 차지하고 있는 상태가 되어 형평성에 어긋나게 된다. drop이 연쇄적으로 생길 수 있는 상황이 올 수도 있으므로 flow별로 골고루 drop이 생기도록 하기 위해 random한 방식으로 drop을 하는 것이다.

## Scheduling policies

### priority scheduling

<br>
<p align = 'center'>
<img src = ''>
</p>
<br>

- priority별로 분류하여 queue에 쌓고 high priority 중심으로 보내준다.
- 여러 개의 priority, 즉 multiple classes도 가능하다. 높은 클래스부터 보낸다.
- IP에서는 이런 priority scheduling이 될수 있다고 생각해서 protocol을 설계했었다. 어느 큐에 보낼 것인지 IP Header의 ToS field를 보고 판단하여 빨리 보낼 수 있는 줄에 세운다. 실제 인터넷에서는 사용되지 않는다.

### Round Robin (RR) scheduling

<br>
<p align = 'center'>
<img src = ''>
</p>
<br>

- 여러 개의 class들이 있을 때 class queue들을 스캔해서 각각 class별로 한 개씩 끝내는 것이다. 돌려가면서 순차적으로 처리한다. 다만 class priority가 높은 경우 여러 번 돌리고, class priority가 낮은 경우 적게 돌린다.

### Weighted Fair Queuing (WFQ) scheduling

<br>
<p align = 'center'>
<img src = ''>
</p>
<br>

- queue를 복수 개를 만들었을 떄, Round Robin인데 weight를 부여해서 fair하게 하겠다. 빨간색은 priority가 높아서 weight를 두배로 주고, 녹색은 낮으니 한번만 주는 방식이다.