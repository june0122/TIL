# Chapter 5.4 routing among the ISPs : BGP

## BGP <sup>Border Gateway Protocol</sup>

> 사실상 **inter-AS routing을 의미하며, 널리 사용되는 protocol**이다. 전체 인터넷을 하나로 붙여주는 역할을 한다.

- 두 가지 타입의 BGP
  - eBGP : 이웃 AS 간에 도달할 수 있는가 등에 대한 정보들을 이웃AS끼리 주고받는 것이다.
  - iBGP : 한 AS 내부에서 어디에 갈 수 있는가 등에 대한 정보들을 내부에서 전파하는 것이다.
- 도달성 정보와 AS 정책을 기반으로 다른 네트워크에 '좋은 경로'를 결정한다.
- 서브넷이 자신의 존재를 외부 네트워크에게 알리도록 한다.

### eBGP, iBGP connections

- Border router에서는 eBGP, iBGP 모두 수행된다.

### BGP session

- 두 개의 BGP 라우터가 semi-permanent TCP connection을 통해서 서로 메시지를 교환하는 것. 이것을 통해 다른 destination으로 가는 경로를 advertising할 수 있다.
-  BGP를 **"path vector"** 라고도 한다.

### Path attributes

> [인터넷 참고 자료] BGP는 서로 다른 종류의 AS에서 동작하는 라우터가 라우팅 정보를 교환할 수 있도록 해주는 프로토콜을
말한다. 인터넷은 여러 개의 자율 시스템(autonomous system, AS)으로 구성되어 있다. 현재 시점에는
64000여 개 정도 된다. 각각의 자율 시스템에는 ‘자율 시스템 번호(ASN)’라는 것이 할당된다. 이 번호를 할
당하는 건 인터넷 할당 번호 관리 기관(Internet Assigned Numbers Authority, IANA)이다. ISP들마다 최
소 한 개 이상의 AS를 보유하고 있다. 구글처럼 큰 회사의 경우는 자신들만의 경계 경로 설정을 위한 기반
시설과 고유의 ASN을 보유하고 있다. AS는 옆에 있는 AS들과 연결된다. 이렇게 이웃에 있는 AS를 ‘피어
(peer)’라고도 부른다. 이런 ‘피어들과의 연결’을 통해 AS들은 자신들이 보유하고 있는 경로를 바깥으로 알
린다. 이렇게 각 AS가 보유하고 있으며, 다른 AS에 알려주는 경로를 ‘네트워크 프리픽스(network prefix)’
라고 부른다. AS가 네트워크 프리픽스를 이웃 AS인 피어들에 전파하면, 전파를 받은 피어들은 이 정보를
다시 자신들의 피어에 전달한다. 이렇게 해서 인터넷 상의 경로들이 이어진다. 

- prefix + attributes = **route**
- 내가 붙어있는 것에 대한 속성에 대해서 하나의 route를 형성하게 된다. 그래서 이 속성에는 두가지 종류가 있다.
  - AS-PATH : prefix advertisement하는 것을 통해서 AS들의 list가 붙는 것.
  - NEXT-HOP : 어떤 특정 내부의 AS 라우터가 그 다음 hop-AS로 간다는 정보.

### Policy-based routing

- 어떤 route에 대한 advertisement를 gateway가 받았을 때, 이 path를 수용할 건지 policy를 가지고 결정할 수 있다. (이 prefix를 붙일지 안붙일지. 붙여서 알릴지 말지.) AS policy를 통해서 다른 이웃한 AS로 가는 path를 전달할 건지 결정하게 된다.

### BGP path advertisement

<br>
<p align = 'center'>
<img src = 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FJmCn0%2FbtqEJim2T2C%2FEvmJkFJfQvUnVdLKl5nYdK%2Fimg.png'>
</p>
<br>

- X가 AS3에 붙은 상황이다.
- 3a는 X와 연결되었다는 정보(AS3,X)를 2c에게 알려주고 2c는 이를 받는다. AS2는 AS2 policy를 가지고 이것을 받아서 X로 가는 경로를 AS2 내부에 퍼트릴지 말지 결정한다. 퍼트린다고 하면 AS2 routers는 AS3,X를 accept한다.
- 그러면 같은 policy를 가지고 2a는 이것을 다른 AS에게 보낼지 말지 결정한다. 결정되면은 바깥에 advertise를 한다. 그래서 AS2,AS3,X를 전파한다. 경로를 하나씩 붙여나가는 것이다. AS1은 X로 보내는 데이터는 우선 AS2로 보내면 되겠다고 알게 된다.

<br>
<p align = 'center'>
<img src = 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FJiAxQ%2FbtqEGf0hykK%2FhEXuaqpFqcxkZvyMAX2KX1%2Fimg.png'>
</p>
<br>

- AS1에서 AS3로 가는 길이 있는 경우. 1c 입장에서는 X로 가는 경로를 AS2와 AS3로부터 받아 X로 가는 경로가 두개가 있다. AS2와 AS3로부터 정보를 받았다. 둘 중에 어느 경로로 보낼까? 1c는 어떤 경로를 선택해서 iBGP를 통해 내부에 알린다. 자세한 것은 뒤에서 배운다.

### BGP message

- BGP message는 **TCP**로 라우터들 간에 연결되어 있다. BGP message에는 네가지 타입이 있다.
  - **OPEN** : TCP conntection을 open하는 메시지. 다른 라우터와 연결하게 되는 경우, 상호 인증을 하게 된다.
  - **UPDATE** : 새로운 path에 대한 advertise를 업데이트라는 메시지를 통해 전달된다.
  - **KEEPALIVE** : 업데이트가 없는 경우에 서로 주고받는게 없다면, 서로 살아있는지 죽어있는지 알 수가 없다. 그래서 서로 살아있다는 사실을 주기적으로 가끔 메시지를 보내서 알려준다.
  - **NOTIFICATION** : 이런 메시지를 보냈는데 에러가 나거나, connection을 닫을 때 사용한다.

### BGP & OSPF, forwarding table entries

>  BGP, OSPF가 적용됐을 때 forwarding table entries를 살펴보기.

<br>
<p align = 'center'>
<img src = 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2Fb3AJgI%2FbtqEIoIijWU%2FHkE9cn72Zw8WIWcNrafuX1%2Fimg.png'>
</p>

<p align = 'center'>
<img src = 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FyeDI4%2FbtqEGUOSkel%2FeIQy3PcjD5vdnI5ZxktTHk%2Fimg.png'>
</p>
<br>

- 1a, 1b, 1d는 전부 X로 가는 경로를 1c를 통해 알게 된다.(iBGP). 1d에서 OSPF를 통해 X로 가려면 1번 인터페이스로 가는 것이 기록되어 있다. 1a는 X로 가는 것은 2로 가면 된다는 table이 있다.
  - 그 다음 경로 선택을 BGP에서는 어떻게 해줄까? 복수의 경로가 있는데 어느 경로를 선택할까. 이것은 여러가지 기술로 가능하다.
    1. 어떤 policy를 가지고 결정한다. 내부 policy를 만들어서 이 규칙으로 결정한다.
    2. 가장 가까운 AS-PATH를 통해 간다.
    3. hot potato routing이라고 해서 자신의 AS를 기준으로 가장 빠른 경로로 결정되는 것.
    4. 다른 추가적인 기준을 가지고 할 수 있다. 기타 등등..

### Hot Potato Routing

<br>
<p align = 'center'>
<img src = 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2Fbw1KXA%2FbtqEHydyXTY%2FCJheFOf3cuYfVXTkC2lIqK%2Fimg.png'>
</p>
<br>

- AS1과 AS3로부터 X의 연결 정보를 받는 상황.
  - 2d는 2a와 2c로부터 정보를 받아, 2d는 2a와 2c 양쪽으로 보내도 된다는 것을 알게 된다. Hot Potato Routing에서는 자신의 라우터를 기준으로 적은 cost의 길을 선택한다. 여기선 2a의 cost가 적으니 2a에게 보내게 된다. 그러면 최종 경로는 2d-2a-1c-3a-3d-X 가 된다.
  - 뜨거운 감자를 손에 쥐면 빨리 내던지게 된다. 이것과 같은 원리로 2d도 빨리 내던져야 하는데, 일단 cost가 가장 적은 2a한테 던져버리는 것이다.

### BGP : Policy

<br>
<p align = 'center'>
<img src = 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FVHo2X%2FbtqEGB9WfV0%2FCiPIWk07OP0oyqDZjQEyuk%2Fimg.png'>
</p>
<br>

- A는 w와 연결되어 있다는 것을 B,C에게 알려준다. (Aw). 그러면 B,C는 w한테 가기 위해선 A한테 가면 되겠다는 것을 알게 된다.
- B는 BAw라는 것을 C한테 알려줄 수도 있는데, 이것을 advertise하지 않도록 정할 수 있다. B는 B의 customer만 서비스해주면 되는데, 내가 왜 남의 customer까지 라우팅 해 줄 필요가 없다는 policy를 가질 수 있다. 그래서 이 policy에 의해서 B는 전파를 하지 않는다.
- 그러면 C는 CBAw라는 path가 있는지 알지 못한다. 물리적으로 연결되어 있지만, 경로를 알지 못한다. B가 전파하지 않아도 라우팅에는 문제가 되지 않는다. C는 CAw를 알고 있기 때문이다.

<br>

- policy는 inter-AS 간에 유효한 것이다. 자기를 통과하는 것에 대한 control을 어떻게 해줄것인지에 대해 inter-AS간에 policy를 통해서 control할 수 있다. intra-AS는 같은 서비스 사업자가 하기 때문에 이런 policy에 대해 필요가 없다.
- inter-AS와 intra-AS routing이 구분되어 있기 때문에 scale문제. 즉 table size가 너무 커지는 것을 해결할 수 있게 되었다. hierarchical routing. 구분해서 라우팅 해놨기 때문에 업데이트가 되었을 때의 오버헤드를 줄일 수 있다.
- performance 측면에서도 intra-AS는 내부에서 performace만 가지고 하면 된다. inter-AS는 performance는 모르겠고, policy로 결정한다. 어차피 inter-AS는 AS 간에 라우팅이기 때문에 performance를 같이 해줄 이유가 없다.