# AVD에서 External storage 접근하기

- 안드로이드 에뮬레이터, 즉 AVD <sup>Android Virtual Device</sup> 에서 External storage <sup>외부 저장소</sup> 에 대한 접근은 기본적으로 허용되지 않는다.
  - AVD의 File directory는 Android Studio 4.0.1 버전 기준으로 우측 하단 모서리를 보면 `Device File Explorer` 버튼을 통해 확인할 수 있다.

- External storage 경로인 `/storage/emulated/0/...` 에 접근하려해도 **Permission denied** 라는 경고 메시지만 표시된다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/88516579-79dcbc80-d028-11ea-994c-35e7f8aaeff1.png'>
</p>
<br>

- 위의 문제를 해결하기 위해서는 크게 **3가지 단계** 를 거처야 한다.

## 1. Play 스토어를 포함하지 않는 하드웨어 프로필

> #### Play 스토어 열에 Google Play 로고가 없는 하드웨어 프로필 사용하기

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/88517477-db515b00-d029-11ea-91d9-ddcf621a574c.png'>
</p>
<br>

- AVD에는 하드웨어 프로필과 시스템 이미지, 저장소 영역, 스킨, 기타 속성이 포함되어 있다.
  
- 일부 하드웨어 프로필만 Play 스토어를 포함하는 것으로 표시되는데 이러한 표시는 하드웨어 프로필이 CTS를 완전히 준수하며 Play 스토어 앱을 포함하는 시스템 이미지를 사용할 수 있다는 의미이다.

- Google Play 스토어가 포함된 시스템 이미지는 앱 보안과 실제 기기의 일관성 있는 환경을 보장하기 위해 릴리스 키로 서명되어 있으므로 높은 권한(루트)을 얻는 데 사용할 수 없다.
  - 앱 문제해결에 도움이 되는 **높은 권한(루트)이 필요한 경우 Google 앱 또는 서비스가 포함되지 않은 Android 오픈소스 프로젝트(AOSP) 시스템 이미지를 사용해야 한다.**
  - 더 자세한 설명을 보고 싶다면 [여기](https://developer.android.com/studio/run/managing-avds?hl=ko#about)를 클릭

<br>

Include Google Play Store       |  Uninclude Google Play Store
:-------------------------:|:-------------------------:
![](https://user-images.githubusercontent.com/39554623/88517488-dee4e200-d029-11ea-811a-999b31f05a7f.png) | ![](https://user-images.githubusercontent.com/39554623/88517490-e0160f00-d029-11ea-83d6-e8439c6a895a.png)

- Google Play 스토어가 포함되지 않은 시스템 이미지는 좀 더 많은 Memory and Storage option들을 사용할 수 있다.

<br>

## 2. `WRITE_EXTERNAL_STORAGE` & `READ_EXTERNAL_STORAGE` Permissions

> `AndroidManifest.xml` 파일에 `WRITE_EXTERNAL_STORAGE` & `READ_EXTERNAL_STORAGE` permission을 추가한다.

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />    
```

<br>

## 3. Terminal에 `adb root` 입력하여 루트 권한 획득하기

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/88522566-80236680-d031-11ea-969e-9c4734672067.png'>
</p>
<br>

- AVD가 실행 중인 상태에서 터미널에 `adb root` 명령어를 입력하면 루트 권한을 획득할 수 있다. 

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/88522561-7ef23980-d031-11ea-85f7-2fd9387bd3ee.png'>
</p>
<br>

- Play 스토어를 포함하는 하드웨어 프로필을 사용했을 경우엔 위와 같은 문구가 뜬다.

<br>

## 결과

- 제대로 위의 과정을 따랐다면 아래의 이미지와 같이 AVD의 External Storage에 접근할 수 있다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/88522834-edcf9280-d031-11ea-99e1-17e6341bc1ee.png'>
</p>
<br>

### ※ File 관련 오류 참고사항

- File I/O와 관련된 App을 개발할 경우, 위의 내용과 같이 설정을 한 뒤에도 AVD에서 갑작스럽게 `Error: open failed: ENOENT (No such file or directory)` 와 같은 에러가 발생할 수 있는데 SD card storage와 관련된 오류일 가능성이 높다.

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/88750737-1f12a480-d191-11ea-9c61-6e14228dd038.png'>
</p>
<br>

- 그럴 경우 알림창을 보면 위 이미지와 달리 *SD card is missing...* 과 같은 알림이 있을 것이다.

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/88751121-ef17d100-d191-11ea-9a6c-78e293b750f4.png'>
</p>
<br>

- AVD를 생성할 때 SD card storage를 넉넉히 주면 문제를 해결 및 방지할 수 있다.