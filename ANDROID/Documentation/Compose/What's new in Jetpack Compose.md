# What's new in Jetpack Compose (Google I/O 2021)

## Jetpack Compose의 등장 배경

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128816157-8c2f051a-b69a-482c-a6a7-03aa66eb8aa7.png'>
</p>

새로운 UI 툴킷이 필요한 이유는 무엇일까?
- 뷰 기반의 기존 툴킷은 벌써 10년 넘게 사용해왔다. 그 당시와는 기술이 달라졌다.
- 기기 성능은 향상됐고 사용자들은 앱에 대한 더 높은 기대를 가지고 있다.
- UI는 훨씬 동적이고 표현이 풍부해졌다.

Views로도 멋진 앱을 개발할 수 있지만 최신 아키텍처를 기반으로 하고 Kotlin을 활용하는 현대적 툴킷을 원한다는 의견이 많았다.
- 구글에서도 기능과 개선 사항을 더욱 빠르게 적용하고 싶어했다.
- 그래서 Jetpack Compose라는 선언형 독립형 툴킷<small>(declarative unbundled toolkit)</small>을 3년 전부터 개발하기 시작했다.
- API Surface가 안정화되어 2021년 2월에 첫 베타를 출시하였다.
- 2021년 7월 28일, Jetpack Compose 1.0이 안정화 버전이 출시되었다. <small>[릴리즈 노트](https://developer.android.com/jetpack/androidx/versions/all-channel#july_28_2021)</small>

## 선언적 UI 툴킷<small>(Declarative UI Toolkit)</small>

## 1. 선언적<small>(Declarative)</small>

최근 앱들은 데이터가 동적이고 실시간으로 업데이트 됨.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128815112-3838bc87-5064-439d-8fc2-28311c32df03.png'>
</p>

원래 Android Views를 사용하면 XML에 UI를 선언해야 하며, 데이터가 바뀌면 UI도 업데이트해야 하고 변형도 필요하다.
- 이를 위해서는 View를 조회하고 속성을 설정해야 한다.
- 애플리케이션 상태가 바뀔 때마다<small>(데이터베이스나 네트워크 호출이 로드되거나, 사용자 상호작용이 끝나면)</small> 이 새로운 정보로 UI를 업데이트해서 데이터를 동기화해야 하는데, View마다 상태가 다르고 각각 업데이트해야 하므로 그 과정이 복잡함 -> 버그가 엄청나게 발생 가능, 개발자가 책임지고 모든 걸 업데이트해야 함

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128525224-cc8efb66-832b-44b8-b803-fafd1a75e614.gif'>
</p>

Compose와 같은 선언적 UI는 상태를 UI로 변환하는 다른 방식을 사용한다.
- UI는 변경 할 수 없고 한 번 생성하면 업데이트가 불가능함
- 앱 상태가 바뀌면 새로운 상태를 새로운 표현으로 변환함 -> 동기화 문제가 완전히 해결됨
- 즉, UI 전체를 다시 생성하는 것

Compose는 매우 지능적이고 효율적이어서 변경되지 않은 요소에 대한 작업은 건너 뛴다.
- 개념적으로는 특정 상태에 맞추어 UI를 새로 생성하는 것과 같다.
- 코드는 특정 상태에 대한 UI 형태를 설명할 뿐, 생성 방법을 지정하지 않는다.
 
### 상태를 UI로 어떻게 변환할까?

> 문자열 리스트를 표시하는 간단한 구성 요소<small>(component)</small>

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128526641-503403a2-36c4-48fa-9201-a413f368aa00.png'>
</p>

Compose에서 UI 구성 요소는 `@Composable` 애노테이션이 달린 함수일 뿐이다.
- 이는 UI 구성 요소를 빠르고 쉽게 생성할 수 있게 해주며,
- 재사용 가능한 요소로 구성된 라이브러리로 UI를 나누는 것을 장려한다.


<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128626022-3561ec1a-4e40-4267-b40a-b1ed8167a67b.png'>
</p>


컴포저블 함수는 값을 반환하는 대신 UI를 전달<small>(emit)</small>한다.
- 예시 코드는 Compose 라이브러리의 `Column`과 `Text` 컴포저블을 사용하는 컴포저블 함수
- 텍스트를 수직으로 배열하고 간단한 텍스트 레이블을 표시

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128626267-09db2334-13b4-4e7b-826c-2145b38393c0.png'>
</p>

완전한 Kotlin 구문과 제어 플로우로 UI를 생성하기
- 기존의 메시지를 반복 실행하는 코드에서 조건에 따라 요소를 표시하는 코드로 변경

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128626399-9b0fae89-10cf-41c9-8bf6-46728c2769bb.png'>
</p>

**컴포저블은 매개변수를 받을 수 있고 받아야만 한다.**
- 데이터를 UI로 변환한다는 것이 그런 뜻이다.
- 컴포저블은 데이터를 함수 매개변수로 받아서 UI를 전달한다.
- 이렇게 함으로써, UI가 동기화 상태<small>(sync)</small>에서 벗어나지 않는다.
  - 메시지가 존재하는데도 "No message" 텍스트를 제거하지 않는 실수를 하지 않을 수 있다.

상태가 바뀌거나 메시지 리스트가 바뀌었을 때, 컴포저블 함수를 실행하면 새 UI가 생성된다 -> <b>재생성<small>(re-composing)</small></b>

메시지 목록은 어떻게 바뀔까?

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128627037-b4f2db7d-2640-44ea-a07b-c3800f424cfa.png'>
</p>

콜 스택을 처리하는 동안 ViewModel이 메시지의 LiveData를 노출한다.
- 이 데이터를 관찰할 수 있고, messages 필드를 읽는 컴포저블은 새 데이터가 입력될 때마다 재생성된다.
- 직접 observer를 설정할 필요가 없다.
- Compose 컴파일러는 어느 컴포저블이 상태를 읽는지 추적하고, 상태가 바뀌면 자동으로 다시 실행한다.
- 컴파일러가 지능적이어서 입력이 변경된 컴포저블만 다시 실행하고 나머지는 건너뛰기에 매우 효율적이다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128627179-ad4f61a2-3f52-4d37-8393-7a13a61c5a52.png'>
</p>

각 컴포저블은 변경할 수 없다.
- 컴포저블을 참조하거나 나중에 쿼리하거나 내용을 업데이트할 수 없다.
- 정보를 입력할 때는 모두 매개변수로 컴포저블에 전달해야 한다.
  - 하지만 이것이 컴포저블이 동적일 수 없다는 것을 의미하진 않는다.
  - 모든 메시지를 선택하는 Checkbox를 더해 자세히 알아보자.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128627253-8c7ef68b-d74c-4b54-82b5-1346cdd5c100.png'>
</p>

Checkbox는 선택되지 않은 상태가 기본이다.
- View에서와 달리 확인란을 클릭하더라도 시각적인 변화는 없다.
  - 상태를 나타내는 상수로 전달했기 때문이다.
  - 상태를 바꾸고 싶다면 코드에 적용해야 한다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128627316-71f0ad1d-5b32-4462-93e0-389bb6424cae.png'>
</p>

Checkbox 선택 여부를 결정하는 로컬 변수를 넣어보자.
- Checkbox를 클릭하면 `onCheckChange` 이벤트를 제공한다. 이 콜백에서 로컬 상태를 업데이트할 수 있다.
  - 이렇게 하면 상태를 읽을 때 `checkbox`를 다시 받는다.
  - 이 콜백에서 상태를 바꾸지 않으면 Checkbox에 시각적 변화는 없다는 걸 이해하는 것이 중요하다.
- 처음에는 코드를 작성해야 Checkbox를 눌렀을 때 선택된다는게 직관적이지 못하다고 생각할 수 있지만 이것이 선언적 UI의 핵심 개념이다.
- 요소는 전달되는 매개변수가 완전히 통제한다. 이렇게 <b>단일 진실 공급원<small>(single source of truth)</small></b>을 생성하는 것이다.
  - 동기화해야 할 상태를 없애는 것!
- Checkbox를 누르는 사용자에게 어떻게 반응할지는 개발자에게 달려 있다.
- 검증을 실행해서 유효할 때만 디스플레이를 업데이트하고 싶은 경우, 코드로 완전히 통제하기 때문에 검증이 실패한 시점에 다시 돌아와서 변경을 취소할 필요가 없다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128627697-b1f37a27-6c10-453f-b2ab-951439161b9b.png'>
</p>

Compose는 입력 데이터가 바뀌면 컴포저블 함수를 다시 실행한다고 하였다. 하지만 다시 실행하거나 재생성을 호출하더라도 유지하고 싶은 변수가 있을 것이다.
- 컴포저블 함수는 `remember` 함수를 사용하면 이전 실행에서 얻은 값을 기억할 수 있다.
- 그러면 값을 다시 사용해 재할당을 방지하거나 상태에 고정할 수 있다.

위 예시에서는 이벤트 핸들링을 인라인으로 구현했지만, 다음 예시와 같이 대신에 state와 update lambda를 컴포저블의 매개변수로 전달하고 <b>단일 진실 공급원<small>(single source of truth)</small></b>으로 로직을 올릴 수 있다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128627828-312166bc-e7e5-4b65-b7df-bcaec67eedc4.png'>
</p>

선언적 UI의 핵심은 특정 상태에서 UI의 형태를 완전히 <b>설명하고</b> 상태가 바뀌면 프레임워크에서 UI <b>업데이트를 처리</b>하는 것이다.
- You completely <b>describe</b> your UI for a given state.
- The framework <b>updates</b> your UI when the state changes.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128628063-ee977792-00b0-40db-9fc9-913e023961a0.png'>
</p>

Compose는 여러 가지 애플리케이션 아키텍처와 호환되지만 단방향 데이터 플로우를 따르는 아키텍처와 잘 맞는다.
- ViewModel이 화면 상태의 단일 스트림을 노출하면 Compose UI에서 관찰하고, 각 구성 요소의 매개변수로 전달한다.
- 각 구성 요소는 필요한 상태만 수신하므로 데이터를 바꿀 때만 업데이트하면 된다.
- ViewState 객체의 단일 스트림을 생성하면 상태 변경을 한 곳에서 처리하는 데 도움이 된다.
- 전체적 화면 상태를 추론하고 오류를 낮추기 쉽다.
- 이 패턴을 사용하면 간단하게 컴포저블을 테스트할 수 있다. 입력에 따라 완전히 제어되기 때문이다.

## 2. UI 툴킷<small>(Toolkit)</small>

선언적 패러다임을 사용하면 UI를 쉽게 사용할 수 있는 이유를 알아보자.

위에서 설명한 원칙에 따라 개발된 Compose는 풍부한 UI 구성 요소 툴킷을 제공한다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128806100-da1987b0-b6c4-4cde-8243-be8f80580252.gif'>
</p>

Jetpack Compose는 머티리얼 디자인 구성 요소와 테마 시스템을 구현한다.

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/128806251-852b8cd0-37cc-4ad6-9fcb-a4603693ae81.png'>
</p>

애플리케이션을 조립하는 데 필요한 구성 요소도 제공한다.
- `Button`, `Card`, `FAB`, `AppBar` 등

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/128806465-0897fe3c-1dc0-4a6a-8825-d50ed64f7fc8.png'>
</p>

모든 구성 요소는 기본적으로 머티리얼 스타일링을 따른다.
- 머티리얼 테마를 구현하기 때문에 모든 구성 요소를 자신의 브랜드에 맞게 체계적으로 커스터마이즈할 수 있다.
- 원하는 색, 도형, 서체 스타일을 지정 가능

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128806739-08dc3efc-ddb4-4a92-9b3d-cf95c8caf4c7.png'>
</p>

Compose는 간단하지만 강력한 새로운 레이아웃 시스템을 제공한다.
- 행<small>(row)</small>과 열<small>(column)</small>을 기반으로 하며 horizontal, vertical linear layout과 비슷하다고 볼 수 있다.
- 하지만 View 시스템과 달리, Compose 레이아웃 모델은 여러 척도<small>(measure)</small>를 전달할 수 없어서 중첩된<small>(nested)</small> 레이아웃에 적합하다.

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/128807597-2feee562-a11d-4925-8e68-90a15688e603.png'>
</p>

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/128807554-0a3c6242-fbf8-4533-8536-e6d8e4894dce.png'>
</p>



새로운 Compose DSL<small>(Domain-specific language, 도메인 특화 언어)</small>을 적용한 ConstraintLayout을 사용하면 더욱 복잡한 레이아웃을 표현할 수 있지만 맞춤형 레이아웃도 훨씬 간단하게 구현이 된다.
- 척도와 배치를 직접 설정해서 적절한 레이아웃을 만들고 싶다면 함수를 구현하기만 하면 된다.

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/128807928-6c04f4d3-7a06-4a1c-b574-5a4994730128.png'>
</p>

가장 기대가 큰 개선 사항은 새로운 애니메이션 시스템이다.
- 훨씬 간단하게 사용 가능
- 보다 효과적이고 간단하게 UI에 모션을 적용할 수 있다.
- Compose에 [MotionLayout](https://developer.android.com/training/constraint-layout/motionlayout)을 가져오는 작업도 진행 중이다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128808428-3d25ee12-72fe-4d8c-a365-7132e8fb6f07.png'>
</p>

Compose에서는 테스트와 접근성이 1급 객체<small>(first-class citizens)</small>이다.
- UI에 병렬 트리를 생성하는 시맨틱<small>(semantic)</small> 시스템을 기반으로 한다.
- 접근성 서비스에 더 많은 정보를 제공하거나, UI 요소를 매칭해서 어설션<small>(assertion)</small>하는 데 도움이 된다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128808455-3d85430b-47e5-4a65-98ff-4fbf65d62668.png'>
</p>

Compose는 테스트 기능을 극대화하는 전용 테스트 아티팩트를 제공하고 독립적으로 컴포저블을 테스트하는 간편한 API를 제공한다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128812945-ddc462a9-5592-413b-b952-b6097fea578a.png'>
</p>

Compose는 Kotlin으로만 개발됐고 우수한 언어 기능을 활용하여 강력하고 간결하면서도 직관적인 API를 구축하였다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128813059-334d2320-cb46-441e-8614-ead691c30b60.png'>
</p>

코루틴을 사용하면 간단한 비동기식 API를 작성할 수 있다.
- 제스처, 애니메이션, 스크롤링 등을 설명
  - 제스처를 애니메이션으로 핸드오프하는 것처럼 비동기식 이벤트를 결합한 코드를 간단하게 작성할 수 있다.
- 구조적 동시성<small>(structured concurrency)</small>을 통해 이런 취소와 정리를 제공한다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128813448-1c52c9b0-3c3d-4464-be48-a179940a5add.png'>
</p>

Kotlin은 툴링<small>(tooling)</small>으로 구성된 강력한 에코시스템이 있다.
- UI 구성 요소를 새로운 함수로 추출해서 간단하게 재사용할 수 있다.

## 3. Built for interop

> interoperability : 상호 운용성

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128814913-a4cb5f4e-490a-417b-87cc-8456cae208e0.png'>
</p>

Jetpack Compose는 기존 View 시스템과 호환된다.
- Compose는 필요에 따라 점진적으로 도입할 수 있다.
  - 화면의 작은 요소부터 시작해서 UI의 큰 부분이나 화면 전체를 교체하면 된다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128814927-385840ad-8d1e-427d-9f9c-9a245f75efc6.png'>
</p>

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128814933-5ee2c524-6124-4b7b-a26b-217c136ca506.png'>
</p>

Compose는 필요한 단계를 진행할 수 있도록 도와준다.
  - Views에 Compose를 포함할 수 있고, 반대로 Compose에서 Views를 호스팅할 수도 있다.
  - MapView의 광고처럼 아직 Compose로 구축하지 않은 콘텐츠를 표시할 때 유용하다.
- Compose로 점차 migrate하면서 원하는 속도대로 도입할 수 있다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/128814954-0e0f0c4a-9504-4e87-ac78-e5534903b75f.png'>
</p>

View와 상호 운용성 이외에도 다른 주요 라이브러리와의 통합을 제공한다.
- Compose는 기존 애플리케이션 아키텍처와도 호환돼서 처음부터 시작할 필요가 없다.
  - 공통 이미지 로딩 라이브러리에는 래퍼를 제공하고 머티리얼 또는 AppCompat XML 테마를 Compose로 변환할 때는 어댑터를 제공한다.

## References

- [What's new in Jetpack Compose | Session](https://www.youtube.com/watch?v=7Mf2175h3RQ)