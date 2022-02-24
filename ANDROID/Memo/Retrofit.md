# Retrofit

## 사전 지식

#### 1. 클라이언트와 서버

- 클라이언트: 요청하는 역할을 하는 프로그램
- 서버: 주는 역할을 하는 프로그램

클라이언트가 데이터를 달라고 서버에게 <b>요청<small>(request)</small></b> 메시지를 보내면, 서버는 DB에서 데이터를 불러와 <b>응답<small>(response)</small></b> 메시지에 정보를 담아 클라이언트에게 전달한다.

#### 2. HTTP

- *HyperText Transfer Protocol*의 약자
- 프로토콜: 요청과 응답을 보내는 형식에 대한 약속

서버에 연결된 수많은 클라이언트들이 각기 다른 형식으로 요청 메시지를 보내면 어떻게 될까? 서버가 제대로 응답하려면 각기 다른 형식의 요청을 해석하는 방식을 알아야 하는데 이는 사실상 불가능하다. 그래서 HTTP 통신 프로토콜이 자리잡게 된다. HTTP 프로토콜을 사용하면 처음 보는 서버에 요청을 보낼 때도 어떤 형식으로 보낼지 큰 고민을 할 필요가 없다. 응답 메시지도 마찬가지다.

#### 3. URL

- *Uniform Resource Locator*의 약자
- 인터넷에서의 자원 위치를 나타낸다.

URI<small>(Uniform Resource Identifier, 통합 자원 식별자)</small>는 모든 물리적, 논리적 리소스를 식별하는 고유의 문자열이다. URI와 URL이 혼동될 수 있는데 URL은 그중에서도 웹에서 리소스의 위치를 나타내므로 URI에 포함되는 하위 개념이다.

```
|-①-|  |---------②-------||-------③--------|
https://june0122.github.io/android/index.html
```

- ① : 클라이언트가 어떤 프로토콜을 사용해야 하는지 나타낸다.
  - 보통의 서버는 http나 https<small>(HTTP 프로토콜의 보안 버전)</small>을 사용
  - ftp<small>(파일 전송)</small>, mailto<small>(이메일)</small> 프로토콜 등이 존재
- ② : 도메인 이름
  - IP 주소를 직접 사용해도 되지만 도메인 이름을 사용하면 더 편리하다.
  - 서버 위치를 알려준다고 생각하면 된다.
- ③ : 자원이 존재하는 경로 정보
  - 궁극적으로 도착할 html 파일 위치를 지정
  - android 안에 있는 index.html 파일을 요청하고 있다.

#### 4. HTTP 요청 메서드

HTTP 요청 메서드는 자원에 어떤 행동을 원하는지를 나타낸다. 대표적인 요청 메서드로는 `GET`, `POST`, `PUT`, `DELETE`가 있다.

|메서드|설명|
|:--|:--|
|GET|대상 자원을 요청할 때 사용<br><small>예) 사용자 정보 요청</small>|
|POST|클라이언트에서 서버로 어떤 정보를 제출할 때 사용<br><small>예) 사용자 정보 추가</small>|
|PUT|대상 자원을 대체하고자 할 때 사용<br><small>예) 사용자 정보 수정</small>|
|DELETE|대상 자원을 삭제하고자 할 때 사용<br><small>예) 사용자 정보 삭제</small>|


## Retrofit

> 안드로이드와 자바를 위한 타입 안전한 HTTP 클라이언트<small>(A type-safe HTTP Client for Android and Java)</small>

- API로 정보를 받아오기 때문에 HTTP 클라이언트이다.
- 요청 바디값<small>(Request Body)</small>과 응답 바디값<small>(Response Body)</small>을 원하는 타입으로 안전하게 바꾸어주기 때문에 타입 안전하다.
- 네트워크 관련 스레딩, 캐싱, 에러 핸들링, 응답 파싱에 필요한 보일러 플레이트를 줄여주고 개발자가 읽기 편한 코드를 작성할 수 있게 도와준다.

Retrofit을 사용할 때는 반드시 다음과 같은 3가지 요소를 구현해야 한다.

1. HTTP 메서드들을 정의한 **인터페이스**
2. Retrofit 클라이언트 객체를 생성하는 **Retrofit 클래스**
3. JSON 데이터를 담을 **데이터 클래스**

## References

- Joyce의 안드로이드 앱 프로그래밍, 376p
