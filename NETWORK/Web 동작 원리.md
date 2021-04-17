# 주소창에 `www.naver.com`을 치면 일어나는 일

## IP 주소와 도메인에 대한 사전 지식

### IP 주소

- IP 주소란 많은 컴퓨터들이 인터넷 상에서 서로을 인식하기 위해 지정받은 식별용 번호라고 생각하면 된다.
- 현재는 IPv4 버전 <sup>32비트</sup>로 구성되어 있으며, `127.0.0.1`과 같은 주소를 말한다.
- 시간이 갈수록 IPv4 주소의 부족으로 IPv6가 생겼는데, 128비트로 구성되어 IP주소가 부족하지 않다는 특징이 있습니다.

### 도메인 네임 <sup>Domain Name</sup>

- IP 주소는 12자리의 숫자로 되어 있기 때문에 사람이 외우기 힘들다는 단점이 있다.
- 그렇기 때문에 12자리의 IP 주소를 문자로 표현한 주소를 **도메인 네임**이라고 한다.
- 다시 말해, 도메인 네임은 `naver.com`처럼 몇 개의 의미있는 문자들과 `.`의 조합으로 구성된다.
- 도메인 네임은 사람의 편의성을 위해 만든 주소이므로 실제로는 컴퓨터가 이해할 수 있는 IP 주소로 변환하는 작업이 필요하다.
- 이때, 사용할 수 있도록 미리 도메인 네임과 함께 해당하는 IP 주소값을 한 쌍으로 저장하고 있는 데이터베이스를 **DNS <sup>Domain Name System</sup>** 이라고 부른다.
- **도메인 네임으로 입력하면 DNS를 이용해 컴퓨터는 IP 주소를 받아 찾아갈 수 있다.**

## 작동 방식

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/115104227-f8e89b00-9f91-11eb-94e8-95b728fd2af0.png'>
</p>
<br>

1. 사용자가 브라우저에 도메인 네임 `www.naver.com`을 입력한다.
2. 사용자가 입력한 URL 주소 중에서 도메인 네임 <sup>Domain Name</sup> 부분을 DNS 서버에서 검색하고, DNS 서버에서 해당 도메인 네임에 해당하는 IP 주소를 찾아 사용자가 입력한 URL 정보와 함께 전달한다.
3. 페이지 URL 정보와 전달받은 IP 주소는 HTTP 프로토콜을 사용하여 HTTP 요청 메시지를 생성하고, 이렇게 생성된 HTTP 요청 메시지는 TCP 프로토콜을 사용하여 인터넷을 거쳐 해당 IP 주소의 컴퓨터로 전송된다.
4. 이렇게 도착한 HTTP 요청 메시지는 HTTP 프로토콜을 사용하여 웹 페이지 URL 정보로 변환되어 웹 페이지 URL 정보에 해당하는 데이터를 검색한다.
5. 검색된 웹 페이지 데이터는 또 다시 HTTP 프로토콜을 사용하여 HTTP 응답 메시지를 생성하고 TCP 프로토콜을 사용하여 인터넷을 거쳐 원래 컴퓨터로 전송된다.
6. 도착한 HTTP 응답 메시지는 HTTP 프로토콜을 사용하여 웹 페이지 데이터로 변환되어 웹 브라우저에 의해 출력되어 사용자가 볼 수 있게 된다.

이렇게 말하면 어느 정도 설명할 수 있지만, 뭔가 부족하다. 조금 더 알아보자.


### DHCP & ARP

대부분의 가정집에서는 `DHCP`로 인터넷 접속을 하고 있다. DHCP는 Dynamic Host Configuration Protocol의 약자로, 호스트의 IP 주소 및 TCP / IP 설정을 클라이언트에 **자동으로** 제공하는 프로토콜이다. 사용자의 PC는 DHCP 서버에서 **사용자 자신의 IP 주소, 가장 가까운 라우터의 IP 주소, 가장 가까운 DNS 서버의 IP 주소**를 받는다. 이후, ARP 프로토콜을 이용하여 IP 주소를 기반으로 가장 가까운 라우터의 MAC 주소를 알아낸다.

> DHCP 서버의 동작 개념도 - 클라이언트가 인터넷 접속을 시도하면 IP와 기본 정보를 제공해준다.

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/115104226-f7b76e00-9f91-11eb-9a04-0d9738b352f4.png'>
</p>
<br>

### IP 정보 수신

위의 과정을 통해 외부와 통신할 준비를 마쳤으므로, DNS Query를 DNS 서버에 전송한다. DNS 서버는 이에 대한 결과로 웹 서버의 IP 주소를 사용자 PC에 돌려준다. DNS 서버가 도메인에 대한 IP 주소를 송신하는 과정은 약간 복잡하다.

> 과정 (`www.naver.com` 이라고 가정하자.)

사용자의 PC는 가장 먼저 지정된 DNS 서버(우리나라의 경우, 통신사별로 지정된 DNS 서버가 있다.)에 DNS Query를 송신한다. 그 후 지정된 DNS 서버는 Root 네임서버에 `www.naver.com`을 질의하고, Root 네임서버는 `.com` 네임서버의 ip 주소를 알려준다.

그 후 `.com` 네임서버에 `www.naver.com`을 질의하면 `naver.com` 네임서버의 ip 주소를 받고 그곳에 질의를 또 송신하면 `www.naver.com`의 IP 주소를 수신하게 된다.

이와 같이 여러번 왔다갔다 하는 이유는, 도메인의 계층화 구조에 따라 DNS 서버도 계층화 되어있기 때문이다. 이렇게 계층화되어 있으므로 도메인의 가장 최상단, 즉 가장 뒷쪽(`.com`, `.kr` 등등)을 담당하는 DNS 서버는 전세계에 13개 뿐이다.

### 웹 서버 접속

이제 웹 서버의 IP 주소까지 알았다. Http Request를 위헤 TCP Socket을 개방하고 연결한다. 이 과정에서 3-way hand shaking 과정이 일어난다. TCP 연결에 성공하면, Http Request가 TCP Socket을 통해 보내진다. 이에 대한 응답으로 웹 페이지의 정보가 사용자의 PC로 들어온다.

-----

## References

[주소창에 naver.com을 치면 일어나는 일](https://github.com/WooVictory/Ready-For-Tech-Interview/blob/master/Network/%EC%A3%BC%EC%86%8C%EC%B0%BD%EC%97%90%20naver.com%EC%9D%84%20%EC%B9%98%EB%A9%B4%20%EC%9D%BC%EC%96%B4%EB%82%98%EB%8A%94%20%EC%9D%BC.md)

