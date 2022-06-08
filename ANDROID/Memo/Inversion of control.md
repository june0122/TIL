# 제어 반전(Inversion of Control, IoC)

> 프로그래머가 작성한 프로그램이 재사용 라이브러리의 흐름 제어를 받게 되는 소프트웨어 디자인 패턴을 말한다.
 
전통적인 프로그래밍에서 흐름은 프로그래머가 작성한 프로그램이 외부 라이브러리의 코드를 호출해 이용한다. 하지만 제어 반전이 적용된 구조에서는 외부 라이브러리의 코드가 프로그래머가 작성한 코드를 호출한다.

설계 목적상 제어 반전의 목적은 다음과 같다:
- 작업을 구현하는 방식과 작업 수행 자체를 분리한다.
- 모듈을 제작할 때, 모듈과 외부 프로그램의 결합에 대해 고민할 필요 없이 모듈의 목적에 집중할 수 있다.
- 다른 시스템이 어떻게 동작할지에 대해 고민할 필요 없이, 미리 정해진 협약대로만 동작하게 하면 된다.
- 모듈을 바꾸어도 다른 시스템에 부작용을 일으키지 않는다.

## 예시

```java
class TextEditor {
    private val checker: SpellChecker

    init {
        checker = SpellChecker()
    }
}
```

```java
public class TextEditor {

    private IocSpellChecker checker;

    public TextEditor(IocSpellChecker checker) {
        this.checker = checker;
    }
}
```

------

아래의 Room 라이브러리와 Repository 패턴을 적용한 안드로이드 코드는 IoC를 적용하여 `user`에 대한 제어권을 UserDao 객체에게 이양하고 있다. 미리 UserRepository  클래스 내부에 user에 대한 행위를 미리 구현해놓으면 UserRepository 클래스는 UserDao와 결합하게 된다.

```kotlin
class UserRepository(private val userDao: UserDao) {  // Dao 객체를 생성자에 매개변수로 전달 (의존성 주입)
    val allUsers: Flow<List<User>> = userDao.getUsers()

    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @WorkerThread
    suspend fun delete(user: User) {
        userDao.delete(user)
    }
}
```

## References

- https://stackoverflow.com/a/3140/12364882
- https://en.wikipedia.org/wiki/Inversion_of_control