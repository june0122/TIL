# Google Maps API를 다른 환경에서 사용 시 설정법

프로젝트에 Google Maps API를 사용한다면 다른 환경(컴퓨터)에서 빌드할 경우 구글 지도가 아무런 내용 없이 빈 화면만 나옵니다. 저같은 경우엔 Git에 맥북에서 작업한 소스파일을 Windows 데스크탑에 클론해서 사용할 때 생긴 문제입니다.

그럼 아래의 오류 메세지를 한 번 살펴봅시다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/76517439-a9878f80-64a0-11ea-9da1-78c7ac18ab25.PNG'>
</p>
<br>

```
E/Google Maps Android API: Authorization failure.  Please see https://developers.google.com/maps/documentation/android-api/start for how to correctly set up the map.
E/Google Maps Android API: In the Google Developer Console (https://console.developers.google.com)
                       Ensure that the "Google Maps Android API v2" is enabled.
                       Ensure that the following Android Key exists:
                        API Key: ****
                        Android Application (<cert_fingerprint>;<package_name>): ****************************
```

이런 오류가 발생하면 Google Maps Android API v2가 활성화 되어있는가, API 키나 SHA-1 값이 제대로 입력이 되었는가 확인해주면 되는데요. 다른 환경에서 사용 시엔 SHA-1 값이 추가적으로 등록이 되어있지 않기 때문에 이런 오류가 발생합니다.

## 해결방법

- 먼저 [Google Cloud Platform Console](https://console.developers.google.com/apis) 에 접속하여 자신의 프로젝트를 선택하고 좌측 패널의 **사용자 인증 정보** 항목을 클릭하여 등록된 API 키의 이름이나 우측의 수정 아이콘을 클릭합니다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/76518682-ece2fd80-64a2-11ea-893d-c9f4379b9865.PNG'>
</p>
<br>

- 그 다음 'Android 앱의 사용량 제한'에서 **항목 추가** 버튼을 클릭하여 현재 사용하려는 환경의 SHA-1을 새롭게 등록해줍니다.
  
  - 항목 추가 후 **저장** 을 꼭 잊지마세요.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/76518502-8a89fd00-64a2-11ea-95ae-2ca540118280.PNG'>
</p>
<br>

- 참고로 컴퓨터의 SHA-1 값을 간단히 알아보는 방법은 Android Studio의 맨 우측의 **Gradle** 탭을 누릅니다.
  
    - 그 다음 `프로젝트 - Tasks - android - signingReport`를 더블 클릭하면 하단의 Gradle Console 창에 SHA-1 값이 표시가 됩니다.

<br>
<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/76517463-b0ae9d80-64a0-11ea-95c0-3ac7c1ef807d.PNG'>
</p>
<br>

<br>
<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/76517468-b1dfca80-64a0-11ea-9844-6112bf0b3a2e.PNG'>
</p>
<br>

- 새로운 환경의 항목을 저장하고 나면 구글 지도가 제대로 표시되는 걸 확인할 수 있습니다.

<br>
<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/76517461-af7d7080-64a0-11ea-95fc-7d02371ddcff.PNG'>
</p>
<br>