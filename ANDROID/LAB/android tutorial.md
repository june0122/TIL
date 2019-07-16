# 안드로이드 튜토리얼

## 안드로이드 개발 도구

- [과거] Eclipse의 ADT(Android Development Toolkit) 

- [현재] Jetbrains과 Google의 **Android Studio**
  
<br>

## Android 'Framework' - Android Application
  
- Library : 기능을 모아 놓는 것 (함수, 클래스)

- Framework : 라이브러리 집합, 실행의 **흐름**이 존재한다.

  - ※ [Processing](https://processing.org/) 은 `setup()`, `settings`, `draw()`와 같이 실행의 흐름이 존재하지만 지엽적인 부분이므로 프레임워크가 아닌 라이브러리로 본다.

<br>

## 안드로이드의 핵심(Core)

- 흐름에 대한 기본적인 이해

  - 화면을 구성하는 방법 : XML
      
    - Inflate<sup> 부풀게 하다, 올리다</sup> : xml에 작성된 view의 정의를 실제 view 객체로 만드는 것

- MVC, MVVM 패턴

- Network : '서비스'를 구성하는 방법  

- Server

<br>

## 권한 요청<sup> Permission Request</sup> 

> 안드로이드 초기에는 사용자가 인지하지 못한 상태에서 권한을 부여해서 개인정보가 유출되는 사고가 있었다. 그런 이슈들이 반복되자 안드로이드 6.0 마시멜로우(api 23) 이상부터는 앱에서 일부권한을 사용할때는 사용자로부터 (동적으로) 동의를 받아야 한다.
   
   - 정적 권한 요청 : AndroidManifest.xml
   
   - 동적 권한 요청 : 코드로 실행 시간에 요청하는 것

<br>

## 빌드 환경

> Java(Kotlin) : Ant -> Maven -> Gradle

```
Project(build.gradle)        -----    build.gradle(.)

  └ Module(build.gradle)     -----    build.gradle(app)
```

- **Minimum API Level** : 앱을 개발하였을 때 구동할 수 있는 최소 버전의 SDK

  - Pie (API 28)    
  
  - Q (API 29)

<br>

## 코드 난독화 : [ProGuard & R8](https://www.guardsquare.com/ko/blog/proguard-and-r8-comparison-optimizers)

### [ProGuard](https://developer.android.com/studio/build/shrink-code?hl=ko)

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/61290511-ff6b4100-a806-11e9-924c-4c526bc34362.png'>
</p>
<br>

#### ProGuard 사용 이유

- 코드 난독화를 통해 디컴파일시 본인의 코드가 노출되는 것을 방지할 수 있다.

- 불필요한 메소드를 제거하여 멀티덱스를 피할 수 있다.

  - 안드로이드 앱을 구성하는 코드는 컴파일되면 덱스<sup> dex</sup> 파일로 만들어 진다. 하나의 덱스 파일에는 최대 65536개의 메소드만 참조할 수 있는데, 만약 프로젝트의 코드가 65536개의 메소드를 초과하게 되면 덱스 파일이 여러 개가 생성된다. 이럴 경우, 멀티덱스를 사용하여 컴파일할 수 있지만, 빌드 과정에서 앱 내의 파일을 여러 개의 덱스 파일로 나누어야 하므로 빌드 속도가 느려지고 APK의 용량이 커지게 된다.

### R8

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/61290510-ff6b4100-a806-11e9-9b75-c5fb3524e681.png'>
</p>
<br>

- R8은 ProGuard를 대체하는 코드 축소 및 난독화를 위한 새로운 도구이다.

<br>

## 앱 구성

> java 소스 파일의 `MainActivity` 클래스

```java
setContentView(R.layout.activity_main)

// R -> res 폴더
// layout -> res 내부 layout 폴더
// activity_main -> layout 폴더 내부의 activity_main.xml 파일
```

<br>

> build.gradle(.)

```
android {
    compileSdkVersion 29
    buildToolsVersion "29.0.1"
    defaultConfig {
        applicationId "com.example.testapp"
        minSdkVersion 15
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

<br>

## [Material Design Color Tool](https://material.io/tools/color/#!/?view.left=0&view.right=0)

- `color.xml` 에 적용
