# OSI 7 Layer

> 국제 표준화 기구 ISO에서 개발한 모델로 컴퓨터 네트워크 프로토콜 디자인과 통신을 계층으로 나누어 설명한 것. 소프트웨어 아키텍처 중에서 Layered Architecture가 있는데, 이것을 따르는 대표적인 예가 네트워크 시스템. 즉, 네트워크 시스템은 하나의 커다란 소프트웨어라고 할 수 있다.

#### 1. Physical Layer

> 케이블, 허브, 리피터 등

- 0과 1의 나열을 아날로그 신호로 바꾸어 전선으로 흘려 보내고 <sup>encoding</sup>
- 아날로그 신호가 들어오면 0과 1의 나열로 해석하여 <sup>decoding</sup>
- 물리적으로 연결된 두 대의 컴퓨터가 0과 1의 나열을 주고받을 수 있게 해주는 모듈 <sup>module</sup>
- Physical Layer의 기술은 **PHY 칩**에 구현되어 있음. <sup>1계층 모듈은 하드웨어적으로 구현</sup>
  - PHY 칩 : Physical layer에 있는 칩으로 일반적인 **아날로그 채널 신호를 디지털 신호로** 적당히 가공해 출력해 주는 칩. 실제 필드에서는 신호의 최종앞단에서 쓰는 칩은 죄다 PHY 칩이라 부른다.
  - ※ MAC은 주로 ethernet관련하는 칩들에서 많이 사용. 가끔 다른 경우에도 쓰는 용어이긴 하지만 그리 일반적이진 않다. 이 칩은 Data Link layer 기능 일부와 physical layer기능을 담당한다. 통신에서 OSI 7 Layer를 정확히 구분하여 구현하는 경우는 별로 없다.

#### 2. Data Link Layer

> 랜카드, 스위치, 라우터

- 같은 네트워크에 있는 여러 대의 컴퓨터들이 데이터를 주고 받기 위해서 필요한 모듈
- 2계층 모듈도 1계층 모듈처럼 하드웨어적으로 구현


- 스위치 : 원하는 목적지에 데이터 패킷을 전송하는 장치
- 라우터 : 최종 목적지에 가기 위하여 거쳐야하는 많은 경로들 중에 적절한 통신 경로를 결정하기 위하여 한 통신망에서 다른 통신망으로 데이터 패킷을 전송하는 장치. 스위치와 스위치를 연결해서 서로 다른 네트워크에 속한 컴퓨터끼리 통신이 가능하게 해주는 장비

#### 3. Network Layer

> 라우터

- 수많은 네트워크들의 연결로 이루어진 inter-network 속에서
- 어딘가에 있는 목적지 컴퓨터로 데이터를 전송하기 위해
- IP 주소를 이용해서 길을 찾고 <sup>routing</sup>
- 자신 다음의 라우터에게 데이터를 넘겨주는 것 <sup>forwarding</sup>
- 운영체제의 커널에 소프트웨어적으로 구현되어 있다.


- 데이터를 목적지까지 가장 안전하고 빠르게 전달하는 기능을 담당한다.
- 라우터를 통해 이동할 경로를 선택하여 IP 주소를 지정하고, 해당 경로에 따라 패킷을 전달해준다.
- 라우팅, 흐름 제어, 오류 제어, 세그먼테이션 등을 수행한다.

#### 4. Transport Layer

> TCP, UDP

- TCP와 UDP 프로토콜을 통해 통신을 활성화한다. 포트를 열어두고, 프로그램들이 전송을 할 수 있도록 제공해준다.
- Port 번호를 사용하여 도착지 컴퓨터의 최종 도착지인 **프로세스**에 까지 데이터가 도달하게 하는 모듈
  - `포트 번호` : 하나의 컴퓨터에서 동시에 실행되고 있는 프로세스들이 서로 겹치지 않게 가져야하는 정수 값
- 운영체제의 커널에 소프트웨어적으로 구현되어 있다.

#### 5. Session Layer

#### 6. Presentation Layer

#### 7. Application Layer



### OSI 모델 vs TCP/IP 모델

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/115102287-e9168a00-9f84-11eb-84da-f3418cc28ee0.png'>
</p>
<br>

- 현대의 인터넷은 OSI 모델이 아닌 TCP/IP 모델을 따르고 있다.
  - OSI 모델이 TCP/IP 모델과의 시장 점유 싸움에서 졌기 때문.
- TCP/IP 모델은 우리가 범용적으로 사용하는 TCP 프로토콜과 IP 프로토콜을 OSI 7계층 형식에 맞추어 더 추상화(혹은 간략화) 시킨 모델이다.
  - OSI 7 Layer의 5, 6, 7 계층이 합쳐져있는 형태
  - TCP/IP Original 설계에서 TCP/IP Updated 설계로 변경되며 OSI 모델과 유사해짐.

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/115102397-7f4ab000-9f85-11eb-8c94-ce1bc1fcfac9.png'>
</p>
<br>


----

### References

- https://youtu.be/1pfTxp25MA8
- https://velog.io/@amuse/OSI-7-Layers
- https://medium.com/harrythegreat/osi%EA%B3%84%EC%B8%B5-tcp-ip-%EB%AA%A8%EB%8D%B8-%EC%89%BD%EA%B2%8C-%EC%95%8C%EC%95%84%EB%B3%B4%EA%B8%B0-f308b1115359
- http://melonicedlatte.com/network/2019/12/21/154500.html#fnref:1