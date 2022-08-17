# [Android] 코루틴 Dispatchers, 하드코딩하지 말고 주입하자

## 개요

구글의 Codelab의 예제나 안드로이드 공식 문서, 블로그의 코루틴 예시들을 보면 아래의 코드처럼 새로운 코루틴을 생성하거나 `withContext`를 호출할 때 `Dispatchers`를 하드코딩하는 경우를 많이 찾아볼 수 있습니다.

하지만 [Android의 코루틴 권장사항](https://developer.android.com/kotlin/coroutines/coroutines-best-practices) 문서를 보면 [_Don't hardcode `Dispatchers` when creating new coroutines_](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#inject-dispatchers) 라는 내용이 제일 먼저 등장합니다.

```kotlin
// DO inject Dispatchers
class NewsRepository(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun loadNews() = withContext(defaultDispatcher) { /* ... */ }
}

// DO NOT hardcode Dispatchers
class NewsRepository {
    // DO NOT use Dispatchers.Default directly, inject it instead
    suspend fun loadNews() = withContext(Dispatchers.Default) { /* ... */ }
}
```

공식 문서의 코드 스니펫에서 하드코딩된 디스패처가 보이는 이유는 샘플 코드를 단순하게 유지하기 위해서이고 실제 애플리케이션에서는 **디스패처를 주입해야 한다**고 권장하고 있습니다.

Dependency Injection<small>(의존 관계 주입, DI)</small>은 객체를 직접 생성하지 않고 외부로부터 필요한 객체를 받아서 사용함으로써 **객체의 생성과 사용을 분리**할 수 있게 해줍니다. 디스패처를 주입하면 <b>디스패처를 구성할 수 있게 되고<small>(configurable)</small></b>, 단위 테스트<small>(Unit Test)</small>와 계측 테스트<small>(Instrumentation Test)</small>에서의 디스패처를 [테스트 디스패처](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#test-coroutine-dispatcher)로 쉽게 교체하여 테스트를 더 확정적으로 만들 수 있으므로 **테스트하기가 더욱 쉬워진다는 장점**이 있습니다.

```kotlin
@InstallIn(SingletonComponent::class)
@Module
object CoroutinesScopesModule {

    @Singleton
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}
```

위의 코드처럼 애플리케이션의 생명주기를 따르는 새로운 코루틴을 생성하거나 특정 작업이 호출자의 범위보다 오래 지속되도록 하기 위해 application 범위의 CoroutineScope를 Hilt를 통해 주입하는 코드는 어렵지 않게 찾아볼 수 있습니다.

하지만 위의 코드는 본문의 개요에서 언급했듯이 **코루틴의 디스패처를 하드코딩하고 있다는 문제점**을 가지고 있습니다. 이러한 문제점을 해결하여 디스패처를 구성 가능하고 테스트하기 쉽게 만들기 위해선 앞서 말했듯 디스패처를 주입해야 합니다. 새로운 Hilt 모듈을 생성해서 상황에 따라 `Dispatchers.Default`, `Dispatchers.Main`, `Dispatchers.IO` 중 어떤 디스패처를 주입할 지 알릴 수 있습니다.

제가 개발 중인 프로젝트에서 **애플리케이션 범위의 CoroutineScope** 는 Hilt를 통해서 생성하고 있긴 했지만 `Dispatchers`는 직접 코드에 작성하고 있었으므로 구글의 안드로이드 개발자 Manuel Vivo의 글 [Create an application CoroutineScope using Hilt](https://medium.com/androiddevelopers/create-an-application-coroutinescope-using-hilt-dd444e721528)를 기반으로 **CoroutinesDispatchersModule** 을 통해서 `Dispatchers`를 주입하는 코드로 수정해보려 합니다.

## Hilt 관련 개념 간단 정리

Hilt를 통해 `Dispatchers`를 주입하기 전에 Hilt의 몇 가지 개념을 복습하는 차원에서 살펴보려고 합니다. 

#### 생성자 주입

생성자 주입은 생성자에 `@Inject` 어노테이션만 추가하면 되므로 클래스의 생성자에 접근할 수 있는 경우 Hilt에 타입의 인스턴스를 제공하는 방법을 알려주는 가장 쉬운 방법입니다.

```kotlin
@Singleton // Scopes this type to the SingletonComponent
class MyRepository @Inject constructor(
   private val externalScope: CoroutineScope
) { 
    /* ... */ 
}
```

이를 통해 Hilt는 `MyRepository` 클래스의 인스턴스를 제공하기 위해 `CoroutineScope`의 인스턴스가 의존성으로 전달되어야 함을 알 수 있습니다. Hilt는 타입의 인스턴스를 생성할 때 의존성이 충족되고 전달되는지 확인하거나 정보가 충분하지 않은 경우 오류를 제공하기 위해 **컴파일 타임에 코드를 생성**합니다. <small>(참고로 Kotlin Injection Framework인 Koin은 런타임에 의존성을 주입하므로 런타임 성능이 떨어질 수 있고, 컴파일 타임이 아닌 런타임에 오류가 발생하는 단점이 있습니다.)</small>

이 시점에 Hilt는 `CoroutineScope` 의존성을 충족하는 방법을 알지 못합니다. 이제 Hilt에 의존성으로 전달할 항목을 알리는 방법에 대해 알아봅시다.

#### Binding

Hilt에서 일반적으로 사용되는 용어인 <b>바인딩<small>(binding, 결합)</small></b>은 **Hilt가 타입의 인스턴스를 의존성으로 제공하는 방법에 대해 알고 있는 정보**입니다. 위의 코드에서 `@Inject` 어노테이션을 사용해서 Hilt에 바인딩을 추가하고 있다고 말할 수 있습니다.

바인딩은 Hilt의 [구성 요소 계층 구조<small>(components hierarchy)</small>](https://developer.android.com/training/dependency-injection/hilt-android#component-hierarchy)를 통해 전달됩니다. `SingletonComponent`에서 사용할 수 있는 바인딩은 `ActivityComponent`에서도 사용할 수 있습니다.

범위가 지정되지 않은 타입에 대한 바인딩은 모든 Hilt 구성 요소에서 사용할 수 있지만, 위의 코드에서 `@Singleton`으로 어노테이션이 달린 `MyRepository`와 같이 구성 요소로 범위가 지정된 바인딩은 계층 구조에서 범위가 지정된 구성 요소와 그 아래에 있는 구성 요소에서 사용할 수 있습니다. 말이 조금 어렵게 느껴질 수 있는데 공식 문서에 나와 있는 Hilt의 구성 요소 계층 구조 이미지를 참고하면 이해에 도움이 될 것 같습니다.

#### @Provides: 모듈을 통해 타입 제공하기

모듈을 통해 타입을 제공하기 위해선 Hilt에 `CoroutineScope` 의존성을 충족시키는 방법을 알려야 합니다. 하지만 `CoroutineScope`는 외부 라이브러리에서 가져온 인터페이스 타입이므로 `MyRepository` 클래스에서 했던 것처럼 생성자 주입을 사용할 수 없습니다. 이를 위한 대안은 모듈을 사용하여 타입의 인스턴스를 제공할 뗴 실행할 코드를 Hilt에게 알리는 것입니다.

```kotlin
@InstallIn(SingletonComponent::class)
@Module
object CoroutinesScopesModule {

    @Singleton // Provide always the same instance 
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        // Run this code when providing an instance of CoroutineScope
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}
```

`@Provides` 메서드는 `@Singleton`으로 어노테이션 처리되어 Hilt가 항상 해당 `CoroutineScope`의 동일한 인스턴스를 반환하도록 합니다. 애플리케이션의 수명을 따라야 하는 작업은 애플리케이션의 수명 주기를 따르는 동일한 `CoroutineScope` 인스턴스를 사용하여 생성되어야 하기 때문입니다.

Hilt 모듈에는 바인딩이 설치된 Hilt 구성 요소(및 계층 구조 아래의 구성 요소)를 나타내는 `@InstallIn`으로 어노테이션이 지정됩니다. 예시 코드의 경우 `SingletonComponent`로 범위가 지정된 `MyRepository`에서 애플리케이션 `CoroutineScope`가 필요하므로 이 바인딩도 `SingletonComponent`에 설치되어야 합니다.

Hilt의 전문 용어로 말하자면 `CoroutineScope` 바인딩을 추가했다고 말할 수 있습니다. 이제 Hilt는 `CoroutineScope` 인스턴스를 제공하는 방법을 알고 있습니다.

## CoroutineDispatcher의 구현을 제공하기 

`Dispatchers.Default`, `Dispatchers.Main`, `Dispatchers.IO`와 같이 디스패처에는 여러 종류가 있습니다. 그런데 모두 `CoroutineDispatcher`로 같은 타입이므로 각각의 디스패처에 대해 다른 구현을 제공해야 합니다. 즉, 동일한 타입에 대해 다른 바인딩이 필요합니다.

이때 [*한정자<small>(qualifiers)</small>*](https://developer.android.com/training/dependency-injection/hilt-android#multiple-bindings)를 사용해서 Hilt에 매번 사용할 바인딩 또는 구현을 알릴 수 있습니다. 한정자는 프로그래머와 Hilt가 특정 바인딩을 식별하기 위해 사용하는 어노테이션일 뿐입니다. `CoroutineDispatcher`의 구현마다 하나의 한정자를 생성해 봅시다.

```kotlin
// CoroutinesQualifiers.kt file

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainImmediateDispatcher
```

이 한정자들은 다른 `@Provides` 메서드에 어노테이션으로 달아 Hilt 모듈의 특정 바인딩을 식별합니다. 예시로 `Dispatchers.Default`를 반환하는 메서드에는 `@DefaultDispatcher` 한정자를 어노테이션으로 달면 됩니다.

```kotlin
@InstallIn(SingletonComponent::class)
@Module
object CoroutinesDispatchersModule {

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @MainImmediateDispatcher
    @Provides
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}
```

참고로 `@Retention` 어노테이션은 어노테이션의 Scope를 제한하는데 사용이 되는데, 3가지의 파라미터가 존재합니다.

- SOURCE
  - compile time에만 유용하며 빌드된 binary 에는 포함되지 않습니다.
  - 개발중에 warning이 뜨는 걸 보이지 않도록 하는 `@suppress` 와 같이 개발 중에만 유용하고, binary에 포함될 필요는 없는 경우에 사용합니다.
- BINARY
  - compile time과 binary에도 포함되지만 reflection을 통해 접근할 수는 없습니다.
- RUNTIME
  - compile time과 binary에도 포함되고, reflection을 통해 접근 가능합니다.
  - Custom Annotation에 `@Retention`을 표시해주지 않을 경우, 디폴트로 RUNTIME이 됩니다.

이와 관련해 더 자세한 내용을 보고 싶다면 [AnnotationRetention](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.annotation/-annotation-retention/) 문서와 강남언니 기술 블로그의 [Annotation 안에서 무슨 일이 일어나는 거지?](https://blog.gangnamunni.com/post/kotlin-annotation/) 글을 참고하면 좋을 것 같습니다.

## 애플리케이션 범위의 CoroutineScope 제공하기

위에서 본 애플리케이션 범위의 CoroutineScope 코드에서 하드코딩된 CoroutineDispatcher를 제거하려면 Hilt에서 제공하는 default dispatcher를 주입해야 합니다. 이를 위해 애플리케이션 CoroutineScope를 제공하는 메서드의 의존성으로 `@DefaultDispatcher` 한정자를 사용하면 주입하려는 타입인 `CoroutineDispatcher`를 전달할 수 있습니다.

```kotlin
@InstallIn(SingletonComponent::class)
@Module
object CoroutinesScopesModule {

    @Singleton
    @Provides
    fun providesCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)
}
```

Hilt가 `CoroutineDispatcher` 타입에 대한 여러 바인딩을 가지고 있으므로 `CoroutineDispatcher`가 의존성으로 사용될 때 `DefaultDispatcher` 어노테이션을 사용하여 어떤 바인딩을 사용할 지에 대한 모호함을 없앨 수 있습니다.

## ApplicationScope에 대한 한정자

비록 `CoroutineScope`에 대한 다중 바인딩이 필요하지 않지만<small>(UserCoroutineScope와 같은 것이 필요할 경우는 향후 변경될 수도 있음)</small>, 애플리케이션 CoroutineScope에 한정자를 추가하면 의존성으로 주입할 때 가독성적인 측면에서 도움이 됩니다.

```kotlin
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesScopesModule {

    @Singleton
    @ApplicationScope
    @Provides
    fun providesCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)
}
```

`MyRepository`는 이 범위에 의존하므로 어떤 외부 범위가 구현으로 사용되는지 매우 명확하게 알 수 있습니다.