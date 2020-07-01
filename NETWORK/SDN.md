# Chapter 5.5 SDN (Software Defined Networking)

### SDN control plane 등장 배경

- 역사적으로 네트워크는 distributed, per-router 로 구현이 되어왔다.
  - monolithic router는 switching hardware를 가지고 있고, 자신만의 구현을 하는데 여러가지 프로토콜을 가진다.
  - 그런데 각기 다른 middleboxes가 존재한다. firewall, load balancers, NAT boxes, 등등… 이것을 어떻게 해결해줄까? 
- 그래서 network control plane에 대한 고민의 해결책으로 SDN이 등장했다.

### per-router contol plane

<br>
<p align = 'center'>
<img src = 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2F4bZa0%2FbtqEGCgekTa%2F29JaXrEcUa0CULpeokzb11%2Fimg.png'>
</p>
<br>

- per-router contol plane은 각각 라우터들이 개별적으로 Routing Algorithm을 돌리면서 정보를 주고받는다. 그래서 각 **라우터별로 control plane을 돌리는 것**이다. forwarding table을 각자 계산해서 갖고 있는 것이다. Routing Algorithm이 메시지를 서로 주고받고 있는 것이다. 그래서 이 알고리즘을 토대로 forwarding table이 만들어진다.

### logically centralized control plane

<br>
<p align = 'center'>
<img src = 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2Fm7til%2FbtqEIJLLwWN%2FqB102hEaLZSm2NEhXXlwB1%2Fimg.png'>
</p>
<br>

- 하나의 logical하게 **중앙화된 Remote Controller에서 모든 계산을 해준다.** 각자 있던 control plane을 떼다가 한 곳에 몰아넣었다고 보면된다. 그러면 CA가 Remote Controller하고 이야기 해서 계산된 결과만 받아오면 된다.
- 이것을 발전시켜보면, **data plane은 각 라우터별로 있고, control plane만 떼다가 묶어서 하나로 만들어주고, 이것을 프로그래밍 하는 것이 SDN이다.**

> SDN은 위와 같은 배경으로 등장하였다.

  - 이전에는 라우터가 잘못 구성되는 경우도 있었는데 SDN을 사용함으로써 이를 피할 수 있고, traffic flow에 대한 유연성을 더 좋게 만들 수 있었다.
  - 그래서 table-based forwarding 프로그래밍을 하게 된 것이다. (centralized 프로그래밍). 지금까지는 분리된 프로그래밍이었다.

- 그래서 table-based forwarding에 API를 하나 둬가지고 이부분을 OpenFlow API를 제공해주어 프로그래밍을 줄 수 있었다. 이 OpenFlow는 control plane을 open(누구나 쓸 수 있게)으로 구현하였다.

### open

<br>
<p align = 'center'>
<img src = 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2Fwu1MM%2FbtqEFXZEs8u%2FS4Kc5D7wZtgKtHx4e4hLvK%2Fimg.png'>
</p>
<br>

- PC를 생각했을 때 PC는 맨 밑에 하드웨어가 있고, 그 위에 OS, 그 위에 Application이 있다. 하드웨어도 여러 종류, OS도 여러 종류, Application도 여러 종류이다. 이럴 때 **맨 아래 Microprocessor 위에 Open Interface를 만들면, 그 위에 어떤 OS가 돌아가도 작동**한다. **OS와 Application 사이도 마찬가지**이다.
- innovation이 급격하게 이루어지고 있고, industry도 상당히 커서 Open Interface는 꼭 필요하다.


### Traffic engineering

> traffic이 한번에 많이 들어 왔을 때 잘 관리해주기 위한 기술

<br>
<p align = 'center'>
<img src = 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FbqzXQL%2FbtqEHwTUw1Y%2FRf2ZCiJhj4HivmGjolKFR0%2Fimg.png'>
</p>
<br>

- u에서 z로 보내려면은, 기존에는 라우팅 돌려서 경로를 결정해야한다. 그런데 역으로 생각해서 경로를 먼저 정해주고 보내주는 것은 어떨까?
  - 예를 들면 u-z로 보낼 때 uvwz를 통해서만 traffic을 지나갈 수 있게 경로를 만들어주려고 한다. 그리고 x-z로 보낼 때 xwyz를 통해서만 지나갈 수 있게 만드려고 한다. 이것은 **기존 라우팅 프로토콜로 불가능**하다.
  - **이런 경로를 SDN에서 프로그래밍 해주는 것이다.**
- u-z로 보내는데 traffic을 분할하고 싶다. 절반은 uvwz로, 절반은 uxyz로. 이것은 load balancing하는 것인데, 라우팅 알고리즘이 두개 필요로 하게 된다. 그래서 **기존 라우팅 프로토콜은 불가능**하다.
  - **이런 경로를 SDN에서 프로그래밍 해주는 것이다.**