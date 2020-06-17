# Chapter 6 Link layer and LANs

> Link layer는 이웃한 두 개의 노드 사이의 링크에 대해서 어떻게 신뢰성있게 전달하고, 에러를 처리할 것인가를 다룬다.

- error detection, correction (에러 감지와 정정)
- 브로드캐스트 채널 공유 : 다중 접속
- link layer의 주소체계
- Local Area Networks (LAN, 근거리 네트워크) : Ethernet, VLANs, wireless LAN

## 6.1 introduction, services

### 용어 설명

- 노드(nodes) : 호스트와 라우터
- 링크(links) : 통신 경로 상의 인접한 노드들을 연결하는 통신 채널
  - 유선 링크
  - 무선 링크
  - LAN
- 프레임(frame) : link layer 패킷, 네트워크 계층 데이터그램을 캡슐화
  - datagram이 link layer로 내려오게 되면 frame라고 부름
- link layer(datalink layer)는 링크 상의 한 노드에서 물리적으로 인접한 노드로 데이터그램을 전송하는 책임을 가짐

### 두 개의 layer

- link layer는 **두 개의 계층**으로 이루어져있는데, 각각 **MAC과 LLC**이다.

### error detection, correction

- Transport layer의 TCP에서도 duplicate ACK을 감지해서 재전송을 하는 등 error detection, correction이 있는데, **TCP의 재전송은 end-end(end to end)이지만 Link layer에서의 재전송은 링크 별로(link-level에서) 이루어지는 것이다.**

### Link layer : context

- 각각의 Link protocol은 다른 서비스를 제공한다.
  - RDT(신뢰적 데이터 전송 프로토콜, Reliable data transfer protocol)가 제공될 수도, 제공이 안될 수도 있다.
  - 링크 프로토콜이 어떤게 사용되었고 어떤 특징을 가지고 있느냐에 따라 틀리다.

### transportation analogy(교통에 비유)

- 라우터까지 host가 직접 전송해야하기 때문에 Link layer는 모든 host에 존재
- 라우터까지 데이터를 보낼 때 IP address를 사용할 수 없음 -> MAC address가 필요
  - ex) 편지를 쓸때, 받는 사람의 주소와 우체국의 주소는 다름 -> 받는 사람의 주소 : host, 우체국의 위치 : router
- 프리스턴에서 로잔까지의 여행으로 비유해보면
  - 프리스턴에서 JFK까지는 리무진을 타고
  - JFK에서 제네바까지는 비행기를
  - 제네바에서 로잔까지 기차를 타고 감
  - 관광객 = 데이터 그램
  - 교통편 = 커뮤니케이션 링크
  - 운송 방식 = 링크 계층 프로토콜
  - 여행사 = 라우팅 프로토콜

### Link layer services

- framing : datagram을 frame 단위로 캡슐화하고 header와 **trailer**를 추가
  - trailer가 있는 이유는? error check를 위해 CRC(순환 중복 검사) 등을 뒤에다가 추가
  - frame = datagram + header + trailer
- link access
  - channel access :하나의 링크를 여러 유저가 동시에 사용하게 되면 충돌이 일어날 수 있으므로, 그것을 예방하거나 해결하기 위한 부분이 정의되어 있다.
  - 매체 접속 제어(medium access control, MAC) 프로토콜은 링크상으로 프레임을 전송하는 규칙에 대해서 명시, 하나의 브로드캐스트 링크를 여러 노드가 공유하는 경우(다중 접속), MAC 프로토콜은 여러 노드로부터의 프레임 전송을 조정
    - MAC address는 local하게 주소를 나타낼 때, IP address는 global하게 주소를 나타낼 때 사용한다.
    - **MAC address가 아닌 Layer 3인 IP address를 사용하는 것은 오버헤드가 있으므로 local하게는 MAC address를 사용하여 주소를 전송한다.**
- 이웃한 노드 간의 신뢰성 있는 전달
  - 무선 환경에서는 에러의 발생률이 높으므로 link layer에서 이를 복구할 수 있는 방식을 사용해주어야 한다.
  - 이러한 맥락에서 link layer에서 사용하는 error handling은 TCP에서 사용하는 것과는 다르다.
- flow control
  - 인접한 송신 노드와 수신 노드 사이의 페이스를 맞춤
- error detection & correction
- half-duplex & full-duplex
  - half-duplex : 보내거나 받거나 할 수 있지만 보낼때는 보내기만, 받을때는 받기만 가능
  - full-duplex : 보내기와 받기 동시에 가능

### Link layer가 구현되어 있는 곳

<br>
<p align = 'center'>
<img width = "450" src = 'https://t1.daumcdn.net/cfile/tistory/99F2E84B5C04D2D302'>
</p>
<br>

- 모든 호스트에 구현되어 있음
- Link layer는 네트워크 인터페이스 카드(Network Interface Card, NIC)로 알려진 네트워크 어뎁터(network adapter)에 구현
  - 유선인 경우 Ethernet card (메인보드에 장착하는 유선 랜카드)
  - 무선인 경우 802.11 card (무선 랜카드)
- application / transport / network / link 계층을 CPU에서 처리하고 link / physical 계층이 network adapter card의 controller / physical 계층에서 처리된다. 두 link를 이어주는 부분이 link layer이다.

### Adaptors communicating

<br>
<p align = 'center'>
<img width = "450" src = 'https://t1.daumcdn.net/cfile/tistory/999797455C04D34230'>
</p>
<br>

- datagram이 캡슐화된 frame이 sending host의 네트워크 카드에서 receiving host의 네트워크 카드로 옮겨간다.
- sending side : 프레임에 데이터그램 캡슐화 + 에러 검사 비트, rdt, flow control 등을 추가
- receiving side : 에러, rdt, flow control 등을 확인, 데이터그램 추출, 수신 측 상위 계층 통과

## 6.2 Error detection

- EDC = Error Detection and Correction bits(redundancy) - 오류 감지 및 수정 비트(중복성)
- D = 오류 검사로 보호되는 데이터, 헤더 필드가 포함될 수 있음
- 과정
  - Data D에 오류 검출 및 정정 비트들(EDC)를 첨가
  - D에는 링크 프레임 헤더에 있는 링크 수준의 주소 정보와 순서번호 및 기타 필드들을 포함
  - D와 EDC는 링크 수준 프레임에 포함되어 수신 노드로 전송
  - 수신 노드에서 비트열 D'과 EDC'을 수신
  - 수신자는 D'과 EDC'만으로 원래의 D가 D'과 동일한지 결정
  - 수신자는 오류 검출과 정정 기술을 사용하여 항상은 아니지만 거의 모든 비트 오류를 검출할 수 있음 -> 여전히 미검출된 비트 오류가 있을 수 있음
- 100% 신뢰할 수 없는 오류 감지
  - 프로토콜이 일부 오류를 가끔씩 놓침
  - EDC가 커지면 검출 및 수정이 향상
- 전송 데이터 오류 검출 방법
  - Parity checking
  - Checksum
  - CRC

### Parity checking

#### single bit parity

> 한 비트의 에러를 검출

<br>
<p align = 'center'>
<img src = 'https://t1.daumcdn.net/cfile/tistory/9957444E5C04D8043B'>
</p>
<br>

- 짝수 패리티 : d+1개의 비트들 중에서 1의 총개수가 짝수가 되도록 선택
- 홀수 패리티 : 1의 개수가 홀수 개가 되도록 선택
- 수신자는 수신된 d+1개의 비트들에서 1의 개수를 계산
- 만약 짝수 패리티 기법에서 1의 개수가 홀수 개라면 최소한 하나의 비트 오류가 있음을 알게됨 -> 임의의 홀수개의 비트 오류
  - 그러나 짝수 개의 오류가 발생하면 검출하지 못함

#### two-dimensional bit parity : 2차원 패리티 기법

> 에러의 detection(검출) 뿐만 아니라 correction(정정)까지 가능. (단 1개일 경우에만 정정 가능)

<br>
<p align = 'center'>
<img width = "450" src = 'https://t1.daumcdn.net/cfile/tistory/99957F505C0735A21B'>
</p>
<br>

- 반전된 비트를 포함하는 열과 행에 대한 패리티에 오류가 생김
- 따라서 단일 비트 오류의 발생을 검출할 수 있을 뿐만 아니라, 패리티 오류가 있는 열과 행의 인덱스값을 사용해 잘못된 비트를 실제로 식별해서 오류를 정정할 수도 있음
- 임의의 2개의 오류도 검출할 수 있으나 정정은 하지 못함. 오류가 1개인 경우에만 정정 가능.
  - 에러가 

### Internet checksum

- 앞에서 다룬 내용
- Layer 3에서 사용되는 방식

### CRC (Cyclic redundancy check, 순환 중복 검사)

- Layer2(link layer)에서 가장 널리 사용되는 방식
- 위 두 개의 방법보다 error 검출 overhead가 적다.

#### CRC 동작 원리

<br>
<p align = 'center'>
<img src = 'https://mblogthumb-phinf.pstatic.net/MjAxNzA2MjBfMTE0/MDAxNDk3OTUyMTU5MDc5.MHgqN1bph6iOxyoGnI5SJYGU9rzTU4a1zYndce4z4csg.2lzsVq1a1LnU2eHgrB6srN63EbdeZxjyvZp9vh52ODkg.PNG.wndudrpdla/CRC_%EC%86%A1%EC%8B%A0%EC%B8%A1.png?type=w2'>
</p>
<br>

<br>
<p align = 'center'>
<img src = 'https://mblogthumb-phinf.pstatic.net/MjAxOTA4MjNfOTAg/MDAxNTY2NTY2MDAzODgx.BmQBrSDwc25XxeJpzrq81Aq5rw-n_SMJYmBEzvg1vN8g.5rdklOVQCpCpbruErZrYUUWZ6Uj2mDZwXFL2AilPMLsg.PNG.wndudrpdla/%EA%B7%B8%EB%A6%BC1.png?type=w2'>
</p>
<br>

- patter `G` 값이 `r+1` 이라 가정할때 CRC는 `r` bit의 크기를 가지며 CRC 값을 계산하기위해 `D` <sup>data bits to be send</sup>  에다가 **10<sup>r</sup>** 을 곱한다.
- D에다가 **10<sup>r</sup>** 을 곱한 값을 G로 나눈 나머지 `R` <sup>CRC bits</sup> 을 CRC bit로 붙여서 보내게 되었을 때,
  - error가 없다면 수신측에서 똑같은 G로 나눗셈을 했을 때 나머지가 0이다.
  - 나머지가 0이 아니라면 error가 발생한 것.

## 6.3 Multiple access protocols

> Layer 2의 핵심적 기능

### Multiple access links, protocols (다중 접속 링크와 프로토콜)

- point-to-point link
  - 전화선을 통해 인터넷을 접속 (과거에 사용하던 방식) → 현재는 대부분 broadcast 기반의 link를 사용
- broadcast link (shared wire or medium)
  - 동일한 하나의 공유된 브로드캐스트 채널에 다수의 송신 노드 및 수신 노드들이 연결 -> 브로드캐스트는 임의의 한 노드가 프레임을 전송하면 채널이 그 프레임을 방송(broadcast)해서 다른 모든 노드들이 그 프레임의 복사본을 수신하기 때문에 사용된 것
  - shared wire(cabled Ethernet), shared RF(802.11 WiFi), shared RF(위성)...

### multiple access protocols

- 단일 공유 브로드 캐스트 채널의 경우 2개 이상의 정보를 전송하면 충돌(collision)과 간섭이 일어나는 문제가 있음
- **multiple access protocols(다중 접속 프로토콜)**
  - 노드가 채널을 공유하는 방식을 결정하는 분산 알고리즘, 즉 노드가 언제 전송할 수 있는지 결정
  - in-band channel : 현재 사용(공유)하고 있는 채널을 통해 coordination을 한다
  - 대부분의 경우 out-of-band channel은 주어져 있지 않다.

### 이상적인 multiple access protocols

- 하나의 노드가 전송하기를 원할 때, 그것은 레이트 R로 전송할 수 있다.
- M 개의 노드가 전송하기를 원할 때, 각각은 평균 비율로 전송할 수 있다. R / M
- fully decentraliozed
  - 전송 조정을 위한 특별한 노드가 없다
  - clock, slot의 동기화 없음
- 단순하다


### MAC(Media Access Control) protocols의 3가지 종류

> Shared channel을 다수의 사용자가 접속할 수 있도록 해줌.

- channel partitioning
  - **time slots(TDMA), frequency(FDMA), code(CDMA)** 등으로 분할하여 여러 개의 sub channel로 만듦.
  - 분할한 채널을 사용자들에게 할당함.
- random access
  - 여러 사용자들이 경쟁하듯이 나누어 쓰는 방식
  - collision이 발생할 수 있다 → 어떻게 recover 할 것인가가 중요한 포인트
- taking turns
  - 서로 번갈아가며 보내는 방식
  - 참여하고 있는 모든 노드들이 보낼 것이 있다는 전제 하에 타당한 방식이므로 보낼 것이 없을 경우 해당 리소스는 날아가므로 그렇게 좋은 방식은 아니다.


### Channel partitioning MAC protocols : TDMA

> TDMA : time division multiple access

- 시간을 시간 프레임으로 나누고 각 시간 프레임을 N개의 시간 슬롯으로 나눈다.

<br>
<p align = 'center'>
<img width = "450" src = 'https://t1.daumcdn.net/cfile/tistory/99F799445C073BCD26'>
</p>
<br>

- 각 슬롯에 해당하는 사용자만 전송을 할 수 있으므로 충돌이 발생하지 않게 된다
- 하지만 슬롯을 배정받은 한 사용자가 전송을 모두 마친 상태에서도 여전히 그 슬롯은 비어있기 때문에 시간적인 낭비가 생길 수 있다.
- cellular system에서는 slot을 누가 사용할지 배정해주는 역할을 기지국이 담당하였다.
  - 전화를 거는 사람에게 기지국이 slot 한 개를 배정

### Channel partitioning MAC protocols : FDMA

> FDMA : frequency division multiple access

-  Rbps의 채널을 다른 주파수로 나눠서 각 주파수를 N개 노드 중 하나에게 할당

<br>
<p align = 'center'>
<img width = "450" src = 'https://t1.daumcdn.net/cfile/tistory/99AB6F4F5C073D492C'>
</p>
<br>

- 슬롯을 기다리는 시간은 없어지겠지만 여전히 R/Nbps로 고정된 대역폭을 가진다.
- FDMA는 한 사용자에게 지속적으로 하나의 band를 할당하는 것이기 때문에 리소스를 끊임없이 잡아먹는 경우에 유리 → 전화 통화에 유리

### Random access protocols

- 어떤 노드가 언제 데이터를 전송할지 명확하지 않음. 사전에 coordination이 되어있지 않다.
- 둘 이상의 노드가 같은 리소스를 사용해서 전송하면 collision이 발생한다.
- Random access MAC protocol specifies
  - collision을 어떻게 감지할 것인가
  - collision을 어떻게 recover할 것인가
- Random access MAC protocol의 종류
  - ALOHA
  - slotted ALOHA → 현재에도 사용하고 있는 개념
  - CSMA, CSMA/CD(유선 Ethernet, 유선 LAN에서 쓰는 방식), CSMA/CA(무선 LAN, 802.11)

### Slotted ALOHA

- assumptions
  - 모든 프레임은 정확히 같은 사이즈의 L 비트로 구성
  - 시간은 L/R초의 슬롯들로 나뉜다 -> 한 슬롯은 한 프레임 전송에 걸리는 시간과 같다
  - 노드는 슬롯의 시작점에서만 프레임을 전송하기 시작
  - 각 노드는 언제 슬롯이 시작하는지 알 수 있게끔 동기화되어 있다
  - 한 슬롯에서 2개 이상의 프레임이 충돌하면, 모든 노드는 그 슬롯이 끝나기 전에 충돌 발생을 알게 된다

- operation
  - 노드가 새로운 프레임을 획득 할 때, 다음 슬롯이 시작할 때까지 기다렸다가 그 슬롯에 전체 프레임을 전송
  - 만약 충돌하지 않으면, 노드는 성공적으로 자신의 프레임을 전송한 것
  - 만약 충돌하면, 노드는 그 슬롯이 끝나기 전에 충돌을 검출
    - 노드는 그 프레임이 충돌없이 전송될 때까지 **확률 p**로 해당 프레임을 다음 슬롯들에게 **재전송**
    - 재전송을 할지말지 랜덤으로 결정하며 재전송을 하더라도 또 다시 충돌할 가능성이 많다

<br>
<p align = 'center'>
<img width = "450" src = 'https://t1.daumcdn.net/cfile/tistory/99111F435C07401D2D'>
</p>
<br>

- 장점 : 단일 노드인 경우에는 단순하고 채널의 최대 속도로 연속적으로 전송할 수 있음
- 단점
  - 다중 노드일 경우에는 위의 그림처럼 충돌이 많이 발생하고 대기하는 빈 슬롯들도 발생함
  - collision을 바로바로 탐지할 수 없음
  - clock synchronization

### Slotted ALOHA의 최대 효율 유도

> 모든 노드들이 항상 p의 확률로 재전송 된다고 가정했을 떄, (1-p)의 확률로 대기하게 됨

- 우선 N개의 노드가 있다고 가정
- 그렇다면 주어진 슬롯이 성공적인 슬롯일 확률은 1개만 전송되고 나머지 N-1개의 노드는 전송되지 않을 확률
- 주어진 노드가 전송될 확률은 p, 나머지 노드가 전송되지 않을 확률은 (1-p)^(N-1)이고 N개의 노드가 있으므로

<br>
<p align = 'center'>
<img src = 'https://t1.daumcdn.net/cfile/tistory/99EC193F5C0743102D'>
</p>
<br>

- 이 확률이 최대가 되기 위해서 E(p)값을 미분하여 그 값이 0이 될 때의 p값을 구해보자
- 그러면 p = 1/N이 나온다 -> 1도 함께 나오는데 이는 무조건 충돌없이 모두 성공했을 경우

<br>
<p align = 'center'>
<img src = 'https://t1.daumcdn.net/cfile/tistory/990DAD395C07452530'>
</p>
<br>

- 활성 노드가 매우 많을 경우의 최대 효율을 구하고 싶은 것이므로 N의 값(노드의 개수)을 무한대로 보내면 공식에 의해 **1/e = 0.37**이라는 값이 도출(37%)

<br>
<p align = 'center'>
<img src = 'https://t1.daumcdn.net/cfile/tistory/99BF1D385C0748AB08'>
</p>
<br>

### Pure (unslotted) ALOHA

- 처음의 알로하 프로토콜은 슬롯이 없고 완전히 분산된 프로토콜이었다
  - **slotted ALOHA 프로토콜**은 모든 노드가 슬롯의 시작점에서 전송을 시작할 수 있도록 **동기화되어 있기를 요구**
  - unslotted ALOHA는 시작하는 시점이 동기화 되어있지 않으므로 collision이 일어날 확률이 훨씬 높음
- 위의 최대효율 공식을 적용시킨 **pure ALOHA의 최대 효율은 1/2e = 0.18 밖에 되지 않는다**
  - slotted ALOHA보다 성능이 많이 떨어짐

### CSMA(carrier sense multiple access)

> 전송하기 전에 누군가가 데이터를 보내고 있는가 아닌가 channel을 listen할 수 있음 (채널 사용 여부 조사)

- 만약 channel이 idel 하다면 프레임을 전송
- 만약 channel이 busy 하다면 전송을 연기

#### 충돌 발생

- **채널의 전파 지연(propagation delay)** 으로 두 노드가 다른 노드의 전송을 감지하지 못하는 경우가 발생
  - 이렇게 충돌이 났을 때 어떻게 할 것인가? → 이 방법에 따라 여러 CSMA 프로토콜이 있음

### CSMA/CD (collision detection)

- 짧은 시간 내에 충돌 검출
- **충돌 시 전송을 중단**하여 채널 낭비 줄임
- collision detection(충돌 검출)
  - **유선 LAN**에서는 신호 세기를 측정하고, 전송된 신호와 수신된 신호를 비교하여 **쉽게 검출**
  - **무선 LAN**에서는 보내는 신호가 받는 신호의 세기를 압도하기 때문에 **검출이 쉽지 않다.**
    - 무선 LAN에서는 CSMA/CD 사용하지 않음 → CSMA/CA 사용
- CSMA/CD efficiency
  - **ALOHA, slotted ALOHA 보다 훨씬 효율성이 높다**
<br>
<p align = 'center'>
<img width = "350" src = 'https://t1.daumcdn.net/cfile/tistory/990B99415C0753EB10'>
</p>
<br>

### Ehternet CSMA/CD algorithm

- 어댑터(NIC)는 네트워크 계층으로부터 데이터그램을 받아서 이더넷 프레임을 생성
- 채널이 유휴(idle)하면 프레임 전송을 시작, 채널이 바쁘면(busy) 채널이 유휴할 때까지 기다림
- 어댑터가 전체 프레임을 전송하는 동안 다른 어탭터의 전송 신호가 감지되지 않았으면 프레임 전송 완료
- 전송 중 다른 어댑터의 전송 신호가 감지되면 프레임 전송을 중단하고 jam signal 전송
- 중단 후 어댑터는 **이진 지수적 백오프(binary exponentional backoff)** 단계 진입
  - m번 충돌 후 어댑터는 {0, 1, 2, .., 2<sup>m</sup>-1} 중에서 임의의 K값을 선택 ( m = min(n, 10) )
  - 어탭터는 K*512 bit 시간을 기다렸다가 단계로 돌아간다.

### Taking turns MAC protocols (순번 프로토콜)

#### 앞선 프로토콜들과 비교

- channel partitioning MAC protocols
  - high load에서 채널을 아주 공평하게 나누어 쓸 수 있다.
  - low load 상황에서는 효율성이 떨어진다. (1개의 활성 노드만 있다하더라도 채널을 여러 개로 나누어쓰면 효율이 매우 내려감)
- random access MAC protocols
  - low load에서는 효율적
  - high load에서는 collision이 발생
- Taking turns MAC protocols
  - 두 가지 장점을 취함

#### polling(폴링)

<br>
<p align = 'center'>
<img width = "350" src = 'https://t1.daumcdn.net/cfile/tistory/9915EE3B5C07554D1A'>
</p>
<br>

- 노드들 중 master node를 지정
- 마스터 노드는 차례대로 slave node들에게 전송하도록 폴링
- 단점
  - 폴링 오버헤드가 존재
  - latency(지연)
  - 마스터 노드 고장 시 전체 채널의 동작이 멈춤

#### token passing(토큰 전달)

<br>
<p align = 'center'>
<img width = "350" src = 'https://t1.daumcdn.net/cfile/tistory/999BA6415C0756920C'>
</p>
<br>

- 토큰이 순서대로 노드들 간에 전달
- 전송할 프레임이 있을 때 노드는 토큰을 잡고, 아니면 다음 노드로 전달
- 단점
  - 토큰 오버헤드
  - 지연
  - 노드 하나가 실패하면 전체 채널이 동작 멈춤

### Cable access network

<br>
<p align = 'center'>
<img src = 'https://t1.daumcdn.net/cfile/tistory/277E655058C7CC5E1E'>
</p>
<br>

- downstream
  - 인터넷, TV 등 채널에 따라 다른 주차수로 분리하여 데이터 전송
- upstream
  - 마찬가지로 다른 주파수로 분리하여 데이터 전송
  - 여러 유저가 같은 bandwidth를 통해 동시에 업로드하면 multiple access issue가 있으므로 사용자가 upstream 요청을 하면 **CMTS**가 리소스를 할당하는 방식을 사용한다.