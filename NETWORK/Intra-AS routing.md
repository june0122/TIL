# Chapter 5.3 intra-AS routing in the Internet: OSPF

## Making routing scalable

> LS, DV 라우팅 알고리즘을 라우터에서 사용할 때 생기는 문제점

- 지금까지 다룬 라우팅에 대한 내용은 이상적인 내용 
  - 모든 라우터들이 동일하다. <sup>identical</sup>
  - 네트워크가 평등하다. <sup>flat</sup>
- 하지만 실제로는 그렇지 않다.
  - 라우터가 역할이 틀릴 수가 있다.
  - 네트워크에는 back-bone 네트워크도 있고, access 네트워크도 있고 네트워크의 계층(역할)이 틀리다.

### 수백만 개의 destination이 있다고 가정할 때

- 라우팅 테이블에 모든 destination들을 저장할 수 없다.
- 라우팅 테이블을 교환하는 것은 링크의 처리가 힘들 정도로 오버헤드가 생긴다.

> 결론 : scalable 하지 않다.

### administrative autonomy <sup>관리 자주성, 자율성, 자치권</sup> 

- 각각 네트워크의 admin은 자기 자신만의 네트워크 내에서만 라우팅을 컨트롤한다.

## scalable한 라우팅을 만들기 위한 Internet approach

> scalable한 라우팅을 만들기 위해서는 **자치 시스템, autonomous systems (AS, a.k.a. domains)**, 각각의 네트워크들을 AS로 구분하여 라우팅을 본다.

### intra-AS routing

- 같은 AS 내부에 호스트와 라우터들 간의 라우터 방식
- 모든 AS 내의 라우터들은 동일한 라우팅 알고리즘(intra-domain protocol)을 사용해야 한다.
- 다른 AS에 있는 라우터들은 각자의 알고리즘을 실행하면 된다. (LS / DV 중 네트워크 특성에 알맞은 것을 실행)
- 가장 바깥에 있는 gateway router <sup>edge</sup> : 자신의 AS 내에서 가장자리에 있는 라우터로 다른 AS의 라우터와 연결한다.

### inter-AS routing

- 서로 다른 AS 간의 라우팅 방식
- 게이트웨이 라우터가 intra-domain 라우팅 뿐만 아니라 inter-domain 라우팅도 수행한다.

## Interconnected ASes

- 포워딩 테이블은 Intra-AS와 Inter-AS 라우팅 알고리즘에 의해 설정된다.
  - Intra-AS 라우팅은 해당 AS 내부에 목적지들에 대한 포워딩 테이블의 Entry를 설정한다.
  - Inter-AS 와 Intra-AS 라우팅 둘 다 외부 목적지에 대한 포워딩 테이블의 Entry를 설정한다.

> 이를 통해 scalable issue를 해결할 수 있다.

## Inter-AS tasks

## Intra-AS Routing

- interior gateway protocols (IGP) 라고도 부름
- 가장 일반적인 intra-AS routing protocols
  - RIP
  - **OSPF**
  - IGRP

## OSPF (Open Shortest Path First)

- 개방형. 라우팅 프로토콜이 공용으로 이용할 수 있다.
- link-state 알고리즘을 사용한다.
  - 다이제스트라 알고리즘 이용하여 최소 비용 경로를 계산
  - 링크 상태 패킷 정보를 전파한다.
  - 각 노드에게 전체 AS의 토폴로지 맵이 주어진다.
- 라우터는 AS의 모든 라우터에게 OSPF 링크 상태 알림(라우팅 정보)을 브로드캐스트한다.
  - TCP, UDP가 아닌 IP를 통해 OSPF 메시지를 직접 전달한다. (IP 데이터그램에 OSPF 메시지가 헤더가 아닌 data에 들어있다.)
- IS-IS routing : OSPF와 거의 동일하다.

### OSPF의 advanced features

- security : 모든 OSPF 메세지는 인증되어야 하므로, 아무나 라우팅 정보를 보낼 수 없다.
- 다수의 동일 비용 경로 허용 : 한 목적지까지 동일한 비용의 여러 경로가 존재할 때, 여러 경로를 사용할 수 있다.
- 각 링크에 다른 서비스 유형(Type Of Service, TOS)에 대해 동일 비용의 여러 경로(ECMP)의 측정값을 매긴다.
- 유니캐스트 <sup>uni-cast</sup>와 멀티캐스트 <sup>multi-cast</sup>를 통합 지원한다.
- 넓은 도메인 영역에서는 계층적 OSPF를 지원한다.

### Hierarchical OSPF

- 두 개의 레벨로 계층을 나눔 : local area, backbone
  - area 별로만 link-state advertisement를 실행
  - 각 노드는 area에 대한 topology를 가진다. (오직 다른 area에 대한 가장 짧은 path, 방향만을 알 수 있다.)
- area border routers : 해당 area의 정보를 요약하며, 요약된 정보를 다른 area의 라우터에게도 알려준다.
- backbone routers : backbone 내에서만 OSPF routing을 실행
- boundary routers : 다른 AS로 연결하는 역할