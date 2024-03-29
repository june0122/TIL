# Intents and Intent Filters

## Intent

- `Intent`는 **메시징 객체**로, 다른 앱 컴포넌트부터 작업을 요청하는 데 사용할 수 있습니다.
- 인텐트가 구성 요소 사이의 통신을 촉진하는 데는 여러 가지 방식이 있지만 기본적인 사용 사례는 크게 세 가지로 나눌 수 있습니다.
  - Activity 시작
  - Service 시작
  - Broadcast 전달

## 인텐트 유형

### 명시적 인텐트

- 특정 컴포넌트나 액티비티가 명확하게 실행되어야할 경우에 사용되며, 주로 어플리케이션 내부에서 사용합니다.

### 암시적 인텐트

- 인텐트의 액션과 데이터를 지정하긴 했지만, 호출할 대상이 달라질 수 있는 경우에는 암시적 인텐트를 사용합니다.
  - Ex. 브라우저 접속에 대한 정보를 암시적 인텐트에 심어놓고 실행하면 어떤 브라우저 앱에서 접속할지 선택하는 위젯
- **Intent Filter** : 암시적 인텐트를 통해 사용자로 하여금 어느 앱을 사용할지 선택하도록 하고자 할때 Intent Filter가 필요합니다.
  - 앱의 Manifest 파일에 있는 `<intent-filter>`요소에서 정의하고, 이는 대응되는 앱 구성 요소에서 중첩됩니다
  - `<intent-filter>` 내부에서는 다음과 같은 세 가지 요소 중 하나 이상을 사용하여 허용할 인텐트 유형을 지정할 수 있습니다.
    - `<action>`, `data`, `category`

## Pending Intent

- PendingIntent 객체는 Intent 객체 주변을 감싸는 래퍼입니다.
- 외부 애플리케이션에 권한을 허가하여 안에 들어 있는 Intent를 마치 본인 앱의 자체 프로세스에서 실행하는 것처럼 사용하게 하는 것입니다.

### Pending Intent 주요 사용 사례

- 사용자가 여러분의 **알림**으로 어떤 작업을 수행할 때 인텐트가 실행되도록 선언 (Android 시스템의 `NotificationManagerIntent`를 실행)
- 사용자가 여러분의 앱 위젯으로 어떤 작업을 수행할 때 인텐트가 실행되도록 선언 (홈 스크린이 인텐트를 실행)
- 향후 **지정된 시간**에 인텐트가 실행되도록 선언 (Android 시스템의 `AlarmManager`가 Intent를 실행합니다)

```
안드로이드 앱을 구현할 때, 인터넷으로부터 파일을 다운로드 하는 로직은 대부분 서비스에서 이루어지도록 구성한다. 
그런데 서비스는 액티비티와 달리 화면에 나타나지 않는다. 따라서 서비스는 다운로드의 진행중이라는 사실 및 진행정도를 화면 상단에 위치한 노티피케이션의 상태바(Status Bar)를 통해서 표현한다. 다운로드가 현재 진행 중이라는 상황을 표시하는 아이콘 등으로 말이다. 그리고 다운로드가 완료된 후에는 아이콘으로 다운로드 완료의 상태를 보여주게 된다.

사용자가 상태바의 아이콘을 확인하고 안드로이드 화면의 상태바를 누르면서 나타나는 바를 잡아 아래로 끌어당기면 나타나는 화면을 노티피케이션 리스트(Notification List)  또는 확장 메시지라 한다. 그리고 만약 서비스가 이 Notification List에 '다운로드 완료' 를 표시를 추가해놓았고, 사용자가 이것을 클릭하면, 노티피케이션은 사전에 서비스에서 작성한 펜딩인텐트를 사용하여 다운로드된 파일을 읽을 수 있는 애플리케이션을 호출하고 다운로드 완료된 파일을 호출된 애플리케이션으로 재생(혹은 보여줌) 하게 된다. 
```