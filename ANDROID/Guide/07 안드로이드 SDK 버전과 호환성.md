# 안드로이드 SDK 버전과 호환성

## 안드로이드 SDK 버전

> 2021년 5월 기준 각 버전별 점유율

<br>
<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/118430781-c7d5c480-b70f-11eb-89ce-bc336c40bf1d.png'>
</p>

각 '코드명' 배포 다음에는 증분적 <sup>incremental</sup> 배포가 뒤따른다.

- 예를 들어, 아이스크림 샌드위치 <sup>ICS</sup>는 처음에 안드로이드 4.0 <sup>API 레벨 14</sup>로 배포되었다. 그리고 곧바로 증분적 배포인 안드로이드 4.0.3과 4.0.4 <sup>API 레벨 15</sup>로 교체되었다.

## 호환성과 안드로이드 프로그래밍

### 최소 안드로이드 버전


```gradle
compileSdkVersion 30
...
defaultConfig {
    applicationId "com.june0122.geoquiz"
    minSdkVersion 21
    targetSdkVersion 30
    ...
}
```
