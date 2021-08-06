# What's new in Jetpack Compose (Google I/O 2021)

## 선언적 UI 툴킷<small>(Declarative UI Toolkit)</small>

## 선언적<small>(Declarative)</small>

최근 앱들은 데이터가 동적이고 실시간으로 업데이트 됨.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/128521129-ab2d133b-b3ec-4fb6-8f4c-a7441dad113d.png'>
</p>

원래 Android Views를 사용하면 XML에 UI를 선언해야 하며, 데이터가 바뀌면 UI도 업데이트해야 하고 변형도 필요하다.
- 이를 위해서는 View를 조회하고 속성을 설정해야 한다.
- 애플리케이션 상태가 바뀔 때마다<small>(데이터베이스나 네트워크 호출이 로드되거나, 사용자 상호작용이 끝나면)</small> 이 새로운 정보로 UI를 업데이트해서 데이터를 동기화해야 하는데, View마다 상태가 다르고 각각 업데이트해야 하므로 그 과정이 복잡함 -> 버그가 엄청나게 발생 가능, 개발자가 책임지고 모든 걸 업데이트해야 함

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/128525224-cc8efb66-832b-44b8-b803-fafd1a75e614.gif'>
</p>

Compose와 같은 선언적 UI는 상태를 UI로 변환하는 다른 방식을 사용한다.
- UI는 변경 할 수 없고 한 번 생성하면 업데이트가 불가능함
- 앱 상태가 바뀌면 새로운 상태를 새로운 표현으로 변환함 -> 동기화 문제가 완전히 해결됨
- 즉, UI 전체를 다시 생성하는 것

Compose는 매우 지능적이고 효율적이어서 변경되지 않은 요소에 대한 작업은 건너 뛴다.
- 개념적으로는 특정 상태에 맞추어 UI를 새로 생성하는 것과 같다.
- 코드는 특정 상태에 대한 UI 형태를 설명할 뿐, 생성 방법을 지정하지 않는다.
 
상태를 UI로 어떻게 변환할까?

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/128526641-503403a2-36c4-48fa-9201-a413f368aa00.png'>
</p>

Composable 함수
<<<<<<< HEAD
- 값을 반환하는 대신 UI를 전달<small>(emit)</small>한다.
=======
- 값을 반환하는 대신 UI를 전달<small>(emit)</small>한다.
>>>>>>> 5737f72657e7e2693476a66763b4fedba3a6b410
