# Android Context의 정의와 종류

```kotlin
abstract class Context
```

> Interface to global information about an application environment. This is an abstract class whose implementation is provided by the Android system. It allows access to application-specific resources and classes, as well as up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc.

애플리케이션 환경의 전역 정보에 대한 인터페이스. 안드로이드 시스템에서 구현을 제공하는 추상 클래스이다. 이를 통해 애플리케이션 특화 리소스 및 클래스에 대한 액세스할 수 있을 뿐만 아니라 액티비티 시작, 브로드캐스팅 및 인텐트 수신과 같은 애플리케이션 수준 작업에 대한 호출을 허용한다.



## References

- Android Docs : https://developer.android.com/reference/kotlin/android/content/Context
- MindOrks : https://blog.mindorks.com/understanding-context-in-android-application-330913e32514
- Stackoverflow : https://stackoverflow.com/questions/3572463/what-is-context-on-android