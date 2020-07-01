# Chapter 4.3 IP: Internet protocol

## IP datagram 구조
 
> 전체 크기는 32 bits(4 bytes) 

### ver

- 4 bits, IPv4와 IPv6 두가지 버전이 존재한다. 현재로썬 1bit만 있으면 0과 1로 두가지 버전을 표현할 수 있지만 초기에 디자인할 때에는 향후 버전이 얼마나 늘어날지 알 수 없으니 넉넉하게 4 bits로 지정했다. (2<sup>4</sup> = 16 개의 버전 표시 가능)

### header length (HLEN)

- 4 bits, 헤더의 길이.
- 32 bit destination IP address까지의 길이가 default이다. (4 x 5 = 20 bytes)
- option이 있을 경우 option까지 포함한 길이가 된다. (4 x 15 = 60 bytes)

### type of service

- 8 bits, QoS를 통해 서비스를 차등해서 제공할 수 있도록 서비스의 종류를 표시한다.

### length

- 16 bits, datagram 전체의 길이
- 전체 길이가 1500 bytes가 넘지 않도록 운영해옴. 길이를 넘는 데이터가 오면 잘라버림.

### 16-bit identifier

- 흔히 말하는 ID, 각 조각이 flow(동일한 데이터그램)에 속하면 같은 id(일련번호)를 공유함

### flgs

- 3 bits로 상태를 표시
- ※ datagram 단편화와 관련된 필드, 단편화 여부와 단편화된 조각이 첫 번째, 두 번째, 세 번째 조각 중 어떠한 조각인지 알려준다 (RF, DF, MF)

### fragment offset

- 1500 bytes를 넘어서거나, datagram을 짤라서 만들 경우에, 자른 부분에 대한 offset. 만약 data를 자르고 54byte부터 시작한다면 offset은 54가 된다.

### time to live (TTL)

- 어떤 datagram을 전송하는데 라우터 알고리즘이 오작동하여 똑같은 라우터들끼리만 데이터를 주고 받아 무한루프를 도는 경우가 있다. 이를 방지하기 위해서 라우터를 거칠 때마다 1씩 감소하고 0이 되면 datagram을 폐기한다. (최대값은 255)
 
### upper layer

- Transport Layer의 protocol 정보를 넣는다. TCP인가 UDP인가에 관한 정보 등을 넣음.
- QoS 서비스를 원할히 해주기 위해 넣음
- 밑에 계층에서 위의 계층의 정보를 아는 것은 프로토콜 설계에 위배되는 내용이긴 함.

### header checksum

- bit들의 정보에 오류가 없는지 체크

### 32 bit source IP address, 32 bit destination IP address

- 출발, 목적지의 IP 주소


------------

> 여기까지가 IP header = 20 bytes (32 bits x 5 = 160 bits)

- TCP header도 20 bytes, IP header도 20 bytes로 동일하다.
- 그러므로 TCP/IP는 합쳐서 최소 **40 bytes**가 되고 뒤에 실제 전달하고자하는 payload 데이터가 전달된다. (40 bytes + app layer overhead)
- IoT에서 TCP/IP를 잘 사용하지 않는 이유는 IoT는 센서에서 처리하는 데이터의 크기가 몇 bit 밖에 되지 않는다. 이 작은 데이터를 전달하기 위해 40 bytes짜리 TCP/IP를 사용하는 것은 오버헤드가 크다. (물론 인터넷을 통해 전달할때는 TCP/IP를 사용할 수 있지만, 별도의 센서를 통해 전달할 때는 그럴 필요가 없다.)


### options (추가되면  존재함)

- timestamp, 경로에 대한 기록, 방문한 라우터에 대한 list
- ※ 데이터그램은 최대 40바이트의 옵션을 가질 수 있다. 옵션은 네트워크 테스트와 디버깅을 위해 사용된다.

### data

- 위의 계층 TCP나 UDP에서 내려온 정보들이 들어간다. data에는 TCP header나 UDP header가 포함된다.

<br>

## IP fragmentation, reassembly

- network link들은 MTU<sup>Maximum transfer size</sup>를 가지고 있다. 각 link들의 type이 다르고 MTU가 다른 경우가 있어, 경우에 따라서 IP datagram을 쪼갤 수 있게 만들었다.

### fragmentation

- 한 개의 datagram을 여러 개의 datagram으로 쪼개는 것. 만약 한 개의 datagram을 3개로 쪼갠다면, 3개의 header와 3개의 payload가 된다. 이 때 header는 동일한 부분도 있지만 fragmentation에 관련된 정보는 다르다.

### reassembly

- 쪼개진 datagram을 합치는 것. end host에서 쪼개진 data를 합쳐서 처리할 수도 있다.

### 예시

> 4000 bytes 짜리 datagram이 있고 MTU가 1500 bytes일 때 각각의 쪼개어진 datagram을 살펴보자.

- length : MTU가 1500 bytes이므로 1500 bytes씩 나뉘어진다. 그런데 3개의 datagram의 length의 합이 1500 + 1500 + 1040 = 4040으로 4000 bytes보다 40bytes가 더 많다. 이것은 1개의 datagram(1개의 header)에서 3개의 datagram(3개의 header)가 되었으니 2개의 header가 추가된 셈이다. 하나의 header는 20 bytes이므로 2 x 20 = 40 bytes만큼 추가되었다.

- ID : 모두 같은 flow에 있으니 ID는 같다.

- fragflag : 1은 쪼갰다는 의미. 쪼개진 datagram에서 0은 이 datagrma이 쪼개진 것 중 마지막 datagram이라는 뜻.

- offset : 0, 185, 370 bytes에서 정보가 시작된다는 뜻이다. **offset은 payload / 8** 이다. 1480<sup>header 20 bytes 제외한 수치</sup> / 8 = 185 으로 offset을 표시했다. 세번째는 +185 해서 370부터 시작.

<br>

## IPv4 addressing

> IPv4는 32-bit, IPv6는 64-bit로 이루어져 있다. host 또는 router interface는 전부 IP주소를 하나씩 갖고 있다. 예를 들어 IPv4의 IP주소 중, 233.1.1.1은 11011111 00000001 00000001 00000001 가 된다.

- sub network : 위 그림의 파란색 영역. 이 sub network에서는 IP주소가 동일한 부분이 있다. 이로 인해 네트워크의 관리가 용이해진다. 위에서는 223.1.1 이 하나의 sub network이고 223.1.2가 또 하나의 sub network이다.
  - ethernet switch : 라우터와 다르다. ethernet switch는 sub network 내에 들어가는 것이다. 라우터는 이 sub network들을 연결시켜줄 때 사용한다.
  - WiFi : sub network 내에 ethernet switch가 아니라 Wifi로 연결되기도 한다.
  - WiFi와 ethernet switch는 Layer 2 이다. L2 스위치라고 불린다. 라우터는 L3이다.


### Subnet과 host의 구분

- 위쪽 그림에서 왼쪽 subnet은 `223.1.1` 까지 같다. 32bit중 24bit가 같고, 8bit가 다르다. 이 24bit는 subnet part로 subnet을 구별하고 나머지 8bit는 host part로 이것으로 host를 구별한다. 총 2<sup>8</sup>(256)개의 host를 구분할 수 있는 것이다.
  - subnet 내에서는 라우터가 관여하지 않고 L2 스위치인 WiFi나 ethernet switch로 연결된다.
- Subnet을 알려주기 위해 IP주소는 `233.1.1.0/24`와 같이 표시한다. 즉 앞에서부터 24bit가 subnet을 의미한다는 것을 말해주는 것이다. 그리고 나머지 8bit가 host라는 것을 말해준다.
  - subnet mask `/24` : 앞의 24-bit를 가지고 동일하게 sub network를 구성하였다는 의미


### 예제<sup>4-40 page</sup>

- 라우터와 라우터의 network도 subnet이다. 그러므로 총 6개

<br>

## CIDR (Classless InterDomain Routing)

- subnet은 어떤식으로 IP address를 만들어 network를 형성할까?
 - CIDR은 IP주소의 법칙이다. IP주소를 만드는 체계에서 한 가지 포인트는 Classless이다.
 - address format은 a.b.c.d/x 이다. x는 subnet의 주소개수를 표현한 것이다. 위 예시를 보면 23bit까지가 subnet part이다.
 - 예를 들어 학교에 host IP주소를 만드려는데, 8bit(256개)를 가지고 host 개수를 표현하지 못한다. 그래서 1bit 더해서 9bit(512개)의 host를 둘 수 있게 해 host를 추가적으로 두게 할 수 있다. 즉, host part는 가변적으로 둘 수 있다.

## DHCP (Dynamic Host Configuration Protocol)

- IP주소는 어떤식으로 부여 받을까?
 - IP주소는 제한적이여서 보통 집에서 사용하는 IP는 유동적으로 받게 된다. 이 유동주소를 할당하는 방법이 DHCP이다. plug-and-play 라고 말하는데, 컴퓨터를 꽂으면 주소를 가져온다는 뜻이다.
 - IP주소 부족하니까 network에 접속할 때 IP주소를 한 개 주는 방식이다. network의 모든 client는 항상 IP주소가 필요하지 않다. 이 때는 회수하고 다른 사용자에게 부여한다. (moblie 유저는 DHCP를 쓰지 않는다)
 - DHCP는 보통 먼저 요청한 client에게 주소를 제공한다.  이 때 주소는 first-hop router의 주소를 준다. DNS server에 대한 IP주소, network mask도 알려준다. 이러한 정보들을 알아야 network에 접속할 수 있기 떄문이다.

<br>

## IP 주소의 관리

> 보통 network는 IP주소를 가지고 subnet을 관리한다.

- 위 예시에서 ISP가 20bits를 subnet part로 사용한다. 기관들의 subnet part 부분은 23이다. 즉, ISP는 21~23번째 bit로, 총 2<sup>3</sup> = 8개의 기관을 관리할 수 있게 된다. 각 기관들은 나머지 bit들로 host들을 관리할 수 있다.
  - 이런 식으로 어떤 IP주소를 계층체계로 구성하면 관리가 용이하다.

- ISP는 20개 bit가 자기 숫자에 포함되는 것이라면, 우리 ISP에서 관리하는 8개의 기관 중에 하나에 갈 데이터니까, 나한테도 보내라고 요청할 수도 있다. 라우터가 요청을 받고, 20bit가 맞으면 ISP한테 Forwarding하는 것을 라우터의 알고리즘을 업데이트하게 된다.
  - 이런 식으로 각자 ISP들이 알려주고, 라우터들이 이 정보를 가지고 업데이트 하게 된다.

- 기관1이 아래쪽으로 옮겨간 상황. 기관 1의 IP주소는 200.23.18.0/23. 앞의 23개 bit가 기관1과 일치하면 나한테 보내라고 요청할 수 있다. 즉, 위쪽으로 보내는 것이 아닌, 아래쪽으로 보내게 된다.
  - 물론 기관들이 모두 위쪽에 있어 한번에 처리하면 좋지만, IP 쪼개다보면 항상 그렇지 않기 때문에 이와 같은 상황이 생기게 된다. 이런 상황들을 감안해서 라우팅 프로토콜을 잘 설계해야 한다.

### 예시

- DHCP 서버에 클라이언트가 요청을 하여 DHCP를 보낼 때 내부에선 UDP/IP 를 사용한다. UDP/IP 위에 Application data로 DHCP 메세지를 올려서 보낸다. (DHCP encapsulated in UDP, encapsulated in IP, encapsulated in 802.1 Ethernet)
- DHCP 서버가 요청에 대한 응답을 해주는데, Ethernet frame은 destination FFFFFFFFFFFF(255.255.255.255) broadcast 함.

### 그렇다면 ISP는 IP주소를 어디서 가져올까?

- ICANN<sup>Internet Corporation for Assigned</sup>이라는 글로벌 기구가 있다. 여기서 전체 IP주소에 대한 관리라던지, DNS 등 전체에 대한 관리를 하게 된다.

## NAT (Network Address Translation)

