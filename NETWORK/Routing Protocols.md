# Chapter 5. Network layer control plane

> two network-layer functions

- forwarding - data plane
- routing - control plane (5장에서 다룰 내용)

## Control plane의 두 가지 형태

- per-router control
  - 기존에 사용하던 방식으로 라우터마다 라우팅 알고리즘이 구현되어 있다.
- logically centralized control
  - 최근에 등장한 SDN<sup>softward defined networking</sup>

### Per-router control

- 각각의 라우터 별로 라우팅 알고리즘을 수행
- 라우터 별로 forwarding table을 유지
- 네비게이션과 유사. 최단경로 또는 최저비용 또는 가장 혼잡하지 않는 경로를 알려준다. 즉 어떤 목적에 따라 경로를 정하는 것이다. 라우터에서도 똑같이 목적에 따라 Routing Algorithm을 설계할 수 있다.

### Logically centralized control plane

- remote하게 떨어져 있는 controller가 각각의 local control agent(CA)와 interact한다. CA에서는 forwarding table을 계산한다. 라우터별로 CA가 있고, 중앙 Remote Controller에서는 CA로부터 정보를 받아서 Routing Algorithm을 돌린다. CA와 Remote Controller는 서로 interact하면서 각각 라우터의 정보를 주고받을 수 있다.

## Routing Protocol

> sending hosts로부터 receiving hosts까지 어떤 라우터들을 경유해서 가는 것이 가장 좋은 경로인가? → 비용이 적고, 빠르며, 덜 혼잡해야 한다.

- link state
- distance vector

### Graph abstraction of the network

- cost는 bandwidth에 반비례하며, congestion에 비례한다.
- c(x,x') = cost of link (x,x')
  - c(w,z) = 5

## 라우팅 알고리즘의 분류

> ### global or decentralized information

### global

- **link state** algorithms : 모든 라우터가 topology<sup>컴퓨터 네트워크의 요소들(링크, 노드 등)을 물리적으로 연결해 놓은 것, 또는 그 연결 방식을 말한다.</sup> 정보와 각각에 대한 link cost 정보를 가지고 있다.

### decentralized

- **distance vector** algorithms : 각각의 라우터가 물리적으로 연결된 이웃 라우터와의 정보만을 안다.
   - 말 그대로 Distance<sup>거리</sup>와 Vector<sup>방향</sup>만을 위주로 만들어진 라우팅 알고리즘이다. 따라서 라우터는 목적지까지의 모든 경로를 자신의 라우팅 테이블 안에 저장하는 것이 아니라 목적지까지의 거리 (Hop Count 등)와 그 목적지까지 가려면 어떤 인접 라우터(Neighbor Router)를 거쳐서 가야 하는 방향만을 저장한다.

> ### static or dynamic

### static

- 경로가 천천히 변함

### dynamic

- 경로가 빠르게 변화함.


## A link-state routing algorithm

### Dijkstra's algorithm<sup>다이제스트라 알고리즘</sup>

- network topology와 link cost를 모든 노드들이 알게 한다.
  - link state를 계속 broadcast함으로써 모든 노드가 같은 정보를 가짐.
- source node로부터 모든 다른 노드들로 가는 가장 비용이 적은 path를 계산
  - source node로 가기 위한 forwarding table을 해당 노드에 제공
- k번 iteration<sup>반복</sup> 후, k destination까지 가는 최소의 경로를 알 수 있다.

### 표기법

- c(x, y) : 노드 x에서 y로 가는 cost / 만약 ∞ 값이 나오면 direct한 neighbor가 아니다.
- D(v) : source로부터 dest. v 까지 가는 경로의 cost
- p(v) : source에서 v까지 가는데 해당 경로 상에서 v에 바로 선행하는 node
- N' : 최소 cost path가 알려진 node들의 집합

## Dijkstra's algorithm

### algorithm complexity : n nodes

- n(n + 1) / 2 노드 검사 : **O(n<sup>2</sup>)** 
- 더 효율적인 자료구조를 사용하여 복잡성을 감소(트리<sup>힙</sup> 이용) : **O(nlogn)**

### oscillations<sup>진동</sup> possible

① 링크 비용 = 전송되는 트래픽 양

② 초기에는 시계반대방향 경로 선택
  - D->A, C->B->A

③ 링크 상태 알고리즘을 한 번 더 수행하면 시계방향 선택
  - B->C->D->A

④ 다시 수행하면 시계반대방향, 또 다시 수행하면 시계방향 경로를 선택하여 진동