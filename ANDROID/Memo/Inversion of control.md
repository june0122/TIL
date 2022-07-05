# 제어의 역전(Inversion of Control, IoC)

***Inversion of Control<small>(IoC)</small>***, *제어의 역전*이라고도 번역되는 이 개념은 Dependency Injection<small>(DI)</small>에 대한 개념을 찾아보면 반드시 등장하는 개념입니다. 처음 접했을 땐 여러 블로그의 설명을 읽어도 확실하게 이해가 되지 않아서 사전적인 정의와 예시 코드 정도만 정리하고 넘어갔었는데 안드로이드의 DI 라이브러리인 Hilt에 대해 복습을 하던 중 IoC를 간단명료하게 설명해주는 [유튜브 영상](https://youtu.be/vFzP2SaMyA0)을 발견하여 스스로 IoC에 대해 다시 한 번 정리하고자 글을 작성합니다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/177264393-dbca8fc4-6a65-4e24-a2e5-bd4142f3de76.png'>
</p>

게임 엔진은 라이브러리가 아니라 왜 프레임워크로 분류하는 것이 적절한 지 IoC에 대한 개념을 함께 설명하는 2분도 안되는 짧은 영상인데, 이 영상에 대해 고마움을 표현하는 댓글들을 읽어보는 재미가 있습니다. 대충 과거에 이 주제에 대해 알아보기 위해 위키피디아를 검색했을 때 박사 과정에 등록하는 것과 같은 느낌을 받았다며 당시에 느낀 어려움을 표하는 것 같은데 저도 과거에 [위키피디아의 IoC 설명](https://en.wikipedia.org/wiki/Inversion_of_control)을 보면서 많이 난해하다고 생각했는데 다른 분들도 어려움을 느꼈던 것 같네요. 영상의 내용은 다음과 같습니다.

## 영상의 내용 번역

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/177293086-d1fbfb8b-8c53-4395-8eea-87ff037cfa12.png'>
</p>

게임 엔진은 라이브러리가 아니라 프레임워크로 분류하는 것이 더 적절합니다. 그리고 프레임워크는 ***inversion of control***이라는 중요한 개념을 가지고 있습니다.

우리가 **라이브러리**를 사용하는 경우, 자신의 코드가 대부분의 작업을 수행하게 되지만 도움이 필요한 곳이 몇 군데 있습니다. 따라서 몇 가지 다른 라이브러리들을 연결하면 코드가 함께 실행됩니다. 그림을 그리거나 소리를 재생하는 것과 같은 특정 작업에 대한 도움을 받기 위해 라이브러리 중 하나를 호출합니다. 그런 다음 코드가 제어를 재개하고 수행 중인 작업을 계속 수행합니다.

반면에 **프레임워크**를 사용할 때는 프레임워크가 대부분의 작업을 수행하고 자신의 코드를 삽입하는 특정 위치가 있습니다. 따라서 프레임워크는 리소스를 할당하고 호출되는 시점을 결정하면서 진행됩니다. 그리고 우리가 작성한 애플리케이션별 로직이 필요할 때 프레임워크는 우리의 코드를 호출하여 작업을 수행하고, 우리의 코드는 프레임워크에 제어를 다시 반환합니다. 이것이 바로 inversion of control입니다.

참고로 Inversion of control은 헐리우드<small>(Hollywood)</small> 원칙이라고도 합니다. "우리에게 전화하지 마세요. 우리가 당신을 부를 겁니다<small>(Do not call us, we call You)</small>."

## 반론 댓글

그런데 해당 영상의 댓글에 IoC의 본래 목적에 대해 충분히 설명하고 있지 않다는 의견들이 보였습니다. 한 댓글은 IoC는 일반적인 선형 의존 구조를 따르는 대신 공유 추상화<small>(shared abstraction)</small>를 통해 의존성을 처리하는 아이디어라고 요약하고 있는데 [위키피디아의 IoC 설명](https://en.wikipedia.org/wiki/Inversion_of_control)에서도 유사한 설명을 하고 있습니다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/177344747-fd85bc9d-66e8-42c0-b8e1-dd46a54b49b2.png'>
</p>

그리고 Thái An Lê가 남긴 댓글을 읽어보고 라이브러리와 프레임워크의 차이점을 IoC의 관점에서 설명한 좋은 영상일 순 있지만 IoC를 잘 설명한 영상은 아니라는 생각이 들었습니다. 해당 댓글의 내용이 영상에서 부족했던 설명을 잘 보충해주는 것 같아 간단한 번역과 약간 코드를 변경하여 옮겨봤습니다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/177280846-7239dfc6-651c-4874-9a13-9fed3c4af257.png'>
</p>

### Thái An Lê의 댓글 번역

이 비디오가 내가 알고 있는 Inversion of Control과 동일한 개념을 설명하는지 확실하지 않습니다. 만약 내 코드가 라이브러리를 사용하는 경우에 내 코드가 라이브러리를 호출하는 것은 지극히 정상이며 그 반대는 아닙니다<small>(즉, 라이브러리가 내 코드를 호출)</small>. 내가 이해하는 IoC는 **라이브러리가 내 코드에서 사용되는 방식**에 대한 것입니다.

여기서 말하는 제어<small>(control)</small>는 내 코드가 사용하는 이 라이브러리의 구체적인 구현에 대한 제어입니다.

내 코드에 다음과 같은 문이 있다고 가정해보자:

```kotlin
val lib = LibraryV1()
lib.doSomething()
```

이것은 내 코드가 결정을 내리고 있음을 의미합니다. 즉, 제어 권한을 가지고 있다는 것을 의미하죠. `LibraryV2`와 같이 해당 라이브러리 새로운 구현이 있는 경우 내 코드를 다음과 같이 변경해야 하기 때문에 위의 코드는 좋은 디자인이 아닙니다.

```kotlin
val lib = LibraryV2()
lib.doSomething()
```

'더 많은 통제'는 '더 많은 책임'을 의미하고 이는 '단일 책임 원칙<small>(Single Responsibility Principle, SRP)</small>의 위반'을 뜻합니다.

많은 컴포넌트들이 내 코드에 의존하는 경우에는 더욱 바람직하지 못합니다. 두 클라이언트가 내 코드를 사용하고 있다고 가정할 때, 내 코드가 `LibraryV2`로 변경되면 둘 다 `LibraryV2`를 사용해야하므로 문제가 발생할 수 있습니다.

이럴 때 inversion of control이 작동합니다. 이 제어<small>(어떤 구현을 사용할지에 대한 결정)</small>를 시스템의 다른 부분에 양도하고 싶습니다. 내 코드는 오직 라이브러리가 필요한지에만 관심이 있으며 구체적인 구현이 무엇인지는 중요하지 않습니다<small>(즉, 라이브러리가 `LibraryV1`인지 `LibraryV2`인지에 대한 여부)</small>. 이것은 **Dependency Injection**을 통해 수행되며 코드는 다음과 같습니다.

```kotlin
class MyCode(val lib: Library) {
    fun doMyCodeThing() {
        lib.doSomething()
    }
}

// Client code:
val lib = LibraryV1()
val myCode = MyCode(lib)
myCode.doMyCodeThing()
```

이제 클라이언트는 사용할 라이브러리의 구현을 제어하는 사람이고 이전의 코드에서는 `myCode`가 라이브러리의 구현을 제어했습니다.

제어가 역전되었기 때문에 inversion of control이라는 이름이 붙었습니다. `myCode`의 책임이 적을수록 `myCode`가 변경될 가능성이 줄어듭니다. 이것은 `myCode`의 재사용성을 증가시킵니다.

아키텍처 관점에서 dependency는 다음과 같습니다.

- IoC 이전: 클라이언트 -> `myCode` -> 라이브러리
  - 내 코드는 라이브러리에 따라 달라지며, 라이브러리가 변경되면 `myCode`도 변경해야 할 수 있으며 물론 클라이언트도 `myCode`에 의존하므로 변경해야 함.
- IoC 이후: 클라이언트 -> `myCode`
  - 내 코드는 더 이상 라이브러리에 의존하지 않습니다. 라이브러리가 변경되어도 여전히 괜찮습니다. 클라이언트는 여전히 변경해야 할 수 있지만 영향은 적습니다.

결론적으로 IoC는 변경 사항을 전체 시스템에 미치는 영향이 적은 위치로 옮기는데 도움이 됩니다. 클라이언트가 `LibraryV2`를 사용하기로 결정하여 몇 가지 문제가 있는 경우 해당 클라이언트에만 영향을 미치고 다른 클라이언트에는 영향을 미치지 않습니다.

## 안드로이드에서의 IoC 코드

#### Activty 수명주기 콜백 메서드

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
```

안드로이드에서 Activty의 코드를 작성할 때 `onCreate()`, `onResume()`과 같은 라이프사이클 콜백 메서드가 호출되었을 때의 동작만 정의하고 언제 이 콜백 메서드들을 호출할지는 신경쓰지 않습니다. 즉, Activty의 메인 흐름 제어권은 나의 코드가 아니라 안드로이드 플랫폼에서 쥐고 있는 것입니다.

#### Repository와 Dao

```kotlin
class UserRepositoryImpl(private val userDao: UserDao): UserRepository {  // Dao 객체를 생성자에 매개변수로 전달 (의존관계 주입)
    val allUsers: Flow<List<User>> = userDao.getUsers()

    override suspend fun insert(user: User) = withContext(Dispatchers.IO) {
        userDao.insert(user)
    }

    override suspend fun delete(user: User) = withContext(Dispatchers.IO) {
        userDao.delete(user)
    }
}
```

위의 코드는 Room 라이브러리와 Repository 패턴을 적용한 안드로이드 코드는 IoC를 적용하여 `user`에 대한 제어권을 UserDao 객체에게 이양하고 있습니다. 미리 UserRepositoryImpl 클래스 내부에 user에 대한 행위를 미리 구현해놓으면 UserRepositoryImpl 클래스는 UserDao와 결합하게 되므로 IoC의 목적인 작업의 구현과 수행의 분리<small>(decoupling)</small>에서 벗어나게 됩니다.

## 정리

IoC 란 코드의 흐름을 제어하는 주체가 바뀌는 것입니다.

전통적인 절차적 프로그래밍에선 자신의 코드가 직접 사용할 오브젝트를 결정하고, 결정한 오브젝트를 생성하고, 생성한 오브젝트의 메서드를 호출하는 식이지만 IoC를 적용하면 모든 제어 권한을 자신이 아닌 다른 대상에게 위임합니다. 이를 통해 작업의 구현과 수행을 분리<small>(decoupling)</small>할 수 있고, 코드에 변경사항이 생길 때 부수 효과를 방지할 수 있습니다.

## References

- Udacity Youtube: https://youtu.be/vFzP2SaMyA0
- Stackoverflow: [What is Inversion of Control?](https://stackoverflow.com/a/3140/12364882 )
- Wikipedia: https://en.wikipedia.org/wiki/Inversion_of_control
- https://johngrib.github.io/wiki/inversion-of-control/
- https://vagabond95.me/posts/about-ioc-dip-di/