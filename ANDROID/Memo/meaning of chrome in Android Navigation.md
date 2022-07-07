# Android Navigation에서 `chrome`의 의미

## 요약

> `chorme`은 사용자에게 화면의 콘텐츠(해당 콘텐츠의 일부가 아님)에 대한 정보 또는 작업 명령을 제공하는 시각적 디자인 요소입니다. 이 디자인 요소는 운영체제, 웹사이트, 애플리케이션과 같은 기본 시스템에서 제공되며 사용자 데이터를 둘러싸고 있습니다.

## 용어 이해하기

안드로이드의 Navigation 관련 문서들을 보다보면 `chrome`이라는 단어가 종종 등장합니다.

#### [NavigationUI](https://developer.android.com/reference/androidx/navigation/ui/NavigationUI?hl=en)

> Class which hooks up elements typically in the `chrome` of your application such as global navigation patterns like a navigation drawer or bottom nav bar with your NavController.

#### [NavHostFragment](https://developer.android.com/reference/androidx/navigation/fragment/NavHostFragment)

> NavHostFragment is intended to be used as the content area within a layout resource defining your app's `chrome` around it.

가장 먼저 떠오르는 것은 구글의 웹브라우저인 크롬이겠지만 문맥상 해석이 이상하게 되어버리기 때문에 해당 단어에 대한 다른 설명을 찾아봤습니다. 구글 크롬때문에 원하는 검색 결과를 찾기가 좀 번거로웠지만 stackoverflow에 [What does 'chrome' mean?](https://stackoverflow.com/q/5071905/12364882)이라는 글이 딱 하나 있었습니다.

아래는 채택된 답변의 일부입니다.

> It is a euphemism for the graphical framework and elements surrounding the content, and thus means different things depending on the context. In the context of a web browser it is the navigation, toolbar etc.

대충 해석해보자면 콘텐츠를 둘러싸고 있는 그래픽 프레임워크와 요소를 지칭하는 단어로, 기반 시스템이 무엇이냐에 따라 그 의미가 달라질 수 있다고 합니다.

이 설명을 Android Navigation 문서의 `chrome`에 대입해도 문제는 없을 것 같긴 하지만 개인적으론 명확하지 않은 설명인 것 같아 위의 글의 중복일 수 있음을 미리 명시하고 [What does "chrome" mean in Android Navigation?](https://stackoverflow.com/q/72887297/12364882)이라는 질문을 직접 stackoverflow에 해봤습니다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/177752280-a34ebbed-7d3d-492d-b678-86e8baa00ae6.png'>
</p>

빠르게 하나의 코멘트가 달렸지만 첨부한 링크의 글에서 그닥 와닿지 않은 설명을 재인용하는 글이었습니다. 그래서 답변을 기다리면서 레이아웃과 관련된 단어임을 인지하고 키워드를 바꿔 검색하던 중 [원하던 글](https://ux.stackexchange.com/a/73488)을 찾게 됩니다.

마이크로소프트 디자인 가이드라인에서 <b>크롬보다 콘텐츠를 먼저 배치한다<small>(put content before chrome)</small></b>가 가장 중요한 원칙 중 하나인데 여기서 chrome이 정확히 무엇을 의미하는지를 질문하는 글입니다. 채택된 답변은 [이 글](https://www.nngroup.com/articles/browser-and-gui-chrome/)의 내용을 인용하여 정의와 예시를 통해 **크롬보다 콘텐츠를 먼저 배치**를 설명해줍니다.

> `chorme`은 사용자에게 화면의 콘텐츠(해당 콘텐츠의 일부가 아님)에 대한 정보 또는 작업 명령을 제공하는 시각적 디자인 요소입니다. 이 디자인 요소는 운영체제, 웹사이트, 애플리케이션과 같은 기본 시스템에서 제공되며 사용자 데이터를 둘러싸고 있습니다.

> 예를 들어 사용자가 사진 테이블을 보고 있고 사진을 맨 아래에서 맨 위로 이동시키고 싶을 경우 "이 사진을 이동"이라는 버튼을 클릭하는 대신 해당 사진을 클릭한 상태에서 원하는 위치로 드래그 앤 드롭하면 됩니다. "이 사진을 이동"과 같은 추가 명령으로 화면을 어지럽히지 않기 때문에 **크롬보다 콘텐츠를 먼저 배치**하는 것입니다. 사용자는 단순히 콘텐츠와 직접 상호 작용할 뿐입니다.

이제 Android Navigation의 문서에서 등장하던 크롬의 정의가 무엇인지 이해가 되지 않나요?

`NavigationUI` 클래스의 설명에 적용해보면, 크롬을 NavigationDrawer 또는 BottomNavigationBar와 같은 global navigation pattern이 적용된 디자인 요소라 지칭하고 있습니다. 즉, BottomNavigationBar가 앱의 Fragment<small>(화면의 콘텐츠)</small>를 전역적으로 탐색<small>(작업 명령)</small>하는 시각적 디자인 요소라고 이해하면 될 것 같습니다.

### 번외: 용어의 기원

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/177754590-a22e894b-e964-4f24-8071-b5cc5f418c32.png'>
</p>

크롬이라는 용어를 누가 생각해 냈는지는 모르겠지만 1950년대의 미국 자동차에 크롬을 사용하는 것에서 유추한 것으로 보인다고 말하고 있습니다. 위의 사진을 보면 차체가 범퍼나 테일 핀<small>(Tail fin)</small> 등 반짝이는 크롬으로 둘러싸여 있습니다.

자동차의 크롬 부분과 비슷하게, 대부분의 최신 GUI에서 크롬이 사용자 데이터 전용인 중간 영역을 둘러싸고 화면의 가장자리 주위에 있는 것을 생각하면 이해가 가는 비유인 것 같습니다.

### 다양한 시스템 레벨에서의 크롬

다음은 기본 시스템<small>(underlying system)</small>에 따라 달라지는 크롬 정의의 몇 가지 예시입니다.

- Windows PC에서 기본 시스템은 Windows 운영체제입니다. Windows에서 크롬은 시작 버튼, 작업 표시줄, 시스템 트레이 및 휴지통으로 구성됩니다.
- 워드 프로세서와 같은 응용 프로그램 소프트웨어를 사용할 때 크롬은 메뉴바, 도구 모음, 눈금자, 스크롤 막대 및 동의어 사전 표시줄이나 포토샵의 색상 팔레트와 같은 특수한 창에서 찾을 수 있습니다.
- 웹 브라우저에서 크롬은 URL 영역, 브라우저 툴바, 브라우저 버튼, 탭, 스크롤바, 상태 영역<small>(status field)</small>를 포함합니다.
- 모바일 앱에서 크롬은 종종 화면 상단의 상태 표시줄을 포함하고 하단에 명령 아이콘이 있는 탭 표시줄을 포함합니다. 때로는 상태 표시줄 아래에도 내비게이션 바가 있습니다.
- 웹 사이트에서 크롬은 내비게이션 바, 바닥글, 로고, 브랜딩, 검색 상자 등이 포함됩니다.

## References

- https://www.nngroup.com/articles/browser-and-gui-chrome/
- https://ux.stackexchange.com/q/73486
- https://stackoverflow.com/q/5071905/12364882