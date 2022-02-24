# ViewModel

- **라이프 사이클**을 고려해서<small>(인식할 수 있는 방식으로)</small> UI 관련 데이터를 저장하고 관리하기 위해 설계된 클래스
- ViewModel 클래스를 사용하면 화면 회전과 같이 구성<small>(configuration)</small> 변경 시에도 데이터 유지 가능

#### UI 컨트롤러의 과도한 책임을 분리하기

> Android 프레임워크는 액티비티와 프래그먼트 같은 UI 컨트롤러의 수명주기를 관리한다.

시스템에서 UI 컨트롤러가 제거되거나 재생성되면, 컨트롤러에 저장된 모든 일시적인 UI 관련 데이터가 삭제된다.
- `onSaveInstanceState()` 메서드를 사용하여 `onCreate()`의 번들에서 데이터를 복원할 수 있다.
- 하지만 이 접근 방법은 사용자 목록이나 비트맵과 같은 대용량일 가능성이 높은 데이터가 아니라, 직렬화했다가 다시 역직렬화할 수 있는 소량의 데이터에만 적합하다는 문제점이 있다.

UI 컨트롤러의 기본적 목적
- UI 데이터를 표시
- 사용자 작업에 반응
- 권한 요청과 같은 운영체제 커뮤니케이션을 처리

UI 컨트롤러에 과도한 책임을 할당할 경우
- UI 컨트롤러에 데이터베이스나 네트워크에서 데이터 로드를 책임지도록 요구하면 **클래스가 팽창**된다.
- UI 컨트롤러에 과도한 책임을 할당하면 다른 클래스로 작업이 위임되지 않고, **단일 클래스가 혼자서 앱의 작업을 모두 처리**하게 될 수 있다.
- **테스트**가 훨씬 더 어려워진다.

그러므로 UI 컨트롤러 로직에서 **뷰 데이터 소유권을 분리**하는 방법이 훨씬 쉽고 효율적이다.