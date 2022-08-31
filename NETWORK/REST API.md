# REST API

> 정의: REST 아키텍처 스타일에 부합하는 API

## REST API의 제약 조건 6가지

1. Server-Client(서버-클라이언트 구조)
2. Stateless(무상태)
3. Cacheable(캐시 처리 가능)
4. Layered System(계층화)
5. Code-On-Demand(optional)
6. **Uniform Interface(인터페이스 일관성)**

### **Uniform Interface의 4가지 제약 조건**

1. **identification of resources<small>(자원의 식별)</small>**
    - 자원(일종의 객체), 객체는 시간이 지남에 따라 생성되고 상태가 변화하고 파괴되는 특성을 지님
    - 그렇기 때문에 특정한 객체를 식별하기 위해서는 해당 객체의 현재 상태를 보는 것만으로는 부족함
    - 개별 객체에 대해 언제나 변하지 않는 불변값. 즉, **고유한 식별자를 부여**해줘야 한다.
    - 이 고유한 식별자가 URI(Uniform Resource Identifier)이며, **URI를 통해 자원을 식별해야 한다**는 뜻이다.
2. **manipulation of resources through representations<small>(표현을 통한 자원의 조작)</small>**
    - 표현이란? 특정한 상태에 있는 자원에 대한 표현
    - **RE**presentational **S**tate **T**ranster : 표현된 (자원의) 상태를 전송
        - 자원 자체를 전송하는 것이 아니라 특정 시점에 자원이 지니고 있는 상태를 특정한 형식으로 표현하고 그 표현을 클라이언트와 서버가 서로에게 전송하는 것
3. **self-descriptive messages<small>(자기 서술적인 메시지)</small>**
    - 클라이언트와 서버 사이를 오가는 메시지는 스스로에 대해 설명할 수 있어야 한다.
    - 누구에게 설명하는 것일까? → 이 메시지를 읽는 주체인 클라이언트-서버 사이의 컴포넌트들
    - 클라이언트-서버 사이의 통신이 이루어지는 것은 그 사이의 컴포넌트들의 순차적인 도움 덕분에 가능하다.
    - 클라이언트와 서버에서 보내는 **요청 및 응답 메시지들은 이 컴포넌트들에게 자신을 어떻게 처리해야 하는 지에 대해 제대로 설명해야 한다**는 것이 이 제약 조건의 의미이다.
        - 예시들
            - 요청 메시지의 경우 Host 헤더에 도메인 명을 기재하여 어디로 보낸 요청인지에 대한 정보를 서술
            - 캐쉬 관련 헤드를 통해 캐쉬 전략 지정 (HTTP/1.1: Etag, Cache-Control 등과 같은 헤더) : 개별 요청에 대한 응답을 클라이언트와 서버 사이의 특정 컴포넌트에 일정 기간동안 캐쉬에 넣는 경우 두번째 요청부터는 해당 컴포넌트로부터 캐쉬된 데이터를 바로 받아올 수 있다.
        - 이처럼 클라-서버 사이의 컴포넌트들이 참고할 수 있도록 적절한 정보를 제공하는 것이 **self-descriptive messages의 기본적인 의미**
4. **HATEOAS<small>(Hypermeida as the engine of application state, 헤이티오-에스)</small>**
    - 정의 : 하이퍼미디어(링크)를 통해서 앱의 상태를 변경
    - 장점 : 서버에서 URL를 변경해도 클라이언트에 영향이 없음 → HATEOAS의 궁극적인 목표: **서버와 클라이언트 분리시켜(사이의 의존성을 줄여) 클라이언트와 서버가 독립적으로 개발이 가능해짐**
    - 단점 : 전달되는 데이터의 크기가 커져 네트워크 오버헤드가 생길 수 있음

### API 설계의 방향성

1. REST의 제약 조건을 모두 충족하는 API 만들기 : RESTful API
2. REST 스타일의 API 만들기 : HTTP API
3. 다른 API 표준 선택하기 : GraphQL API

## References

- [10분 테코톡] 정의 REST API : https://youtu.be/Nxi8Ur89Akw