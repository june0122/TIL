# 의존성 주입(Dependency injection, DI)

> 외부에서 하나의 객체가 다른 객체의 의존성을 제공하는 디자인 패턴

- 의존성<small>(dependency)</small>: 클래스가 참조를 필요로 하는 다른 클래스를 ***의존성***이라 한다.
<!-- 의존성: 클래스가 다른 클래스의 참조를 필요로 하는 경우, 이 필요한 클래스를 ***의존성***이라 한다. -->

- 주입<small>(injection)</small>: 외부에서 객체를 생성해서 사용하려는 객체에게 전달하는 것

- 의존성 주입의 의도: 객체 생성과 사용의 관심을 분리하는 것
  - 객체 사이의 결합도를 느슨하게 하고 의존 관계 역전 원칙과 단일 책임 원칙을 따르도록 하여 객체의 생성에 대한 부분을 객체의 사용<small>(행위)</small>와 분리하도록 한다.
  - 단일 책임 원칙<small>(Single Responsibility Principle, SRP)</small> : 모든 클래스는 하나의 책임만 가지며, 클래스는 그 책임을 완전히 캡슐화해야 한다.
  - 의존 관계 역전 원칙<small>(Dependency Inversion Principle, DIP)</small> : 상위 모듈은 하위 모듈에 의존해서는 안되며, 모두 추상화에 의존해야 한다.

## 의존성 주입이란?

예시로 `Engine` 클래스의 참조가 필요한 `Car` 클래스가 있다고 가정하자. `Engine`처럼 필요한 클래스를 *의존성*이라 하며, `Car` 클래스가 실행되기 위해서는 `Engine` 클래스의 인스턴스가 필요하다.

클래스가 필요한 객체를 얻는 세 가지 방법

1. 클래스가 필요한 의존성을 직접 구성 : `Car`가 자체 `Engine` 인스턴스를 생성하여 초기화
2. 다른 곳에서 객체를 가져오기 : `Context` getter 및 `getSystemService()`와 같은 일부 Android API는 이러한 방식으로 작동
3. 객체를 매개변수로 제공 받기 : 클래스가 구성될 때 앱이 이러한 의존성을 제공하거나 각 의존성이 필요한 함수에 전달할 수 있다. 위의 예에서 `Car` 생성자는 `Engine`을 매개변수로 받는다.

여기서 세번째 옵션이 바로 의존성 주입으로, 이러한 접근 방법을 사용하면 클래스 인스턴스가 의존성을 자체적으로 얻는 대신 받아서 제공한다.

#### 의존성 주입 없이 코드 자체에서 의존성 생성

<p align = 'center'>
<img width = '200' src = 'https://user-images.githubusercontent.com/39554623/159155543-968183c0-38de-45af-8f8d-3d0f41b66473.png'>
</p>

```kotlin
class Car {

    private val engine = Engine()

    fun start() {
        engine.start()
    }
}

fun main(args: Array) {
    val car = Car()
    car.start()
}
```

위의 예제는 `Car` 클래스가 자체 `Engine`을 구성하기 때문에 의존성 주입의 예시가 아니다. 이러한 방식은 다음과 같은 이유로 문제가 될 수 있다.

- `Car`와 `Engine`이 강하게 결합되어 있다.
  - `Car` 인스턴스는 한 가지 타입의 `Engine`만을 사용하므로 서브 클래스 또는 대체 구현을 쉽게 사용할 수 없다.
  - `Car`가 자체 `Engine`을 구성했다면 `Gas` 및 `Electric` 타입의 엔진에 동일한 `Car`를 재사용할 수 없고 두 가지 타입의 `Car`를 생성해야만 한다.
- `Engine`에 대한 강한 의존성은 테스트를 더욱 어렵게 만든다.
  - `Car`는 실제 `Engine` 인스턴스를 사용하므로 다양한 테스트 사례에서 [테스트 더블](https://en.wikipedia.org/wiki/Test_double)을 사용하여 `Engine`을 수정할 수 없게 된다.

#### 의존성 주입 사용

<p align = 'center'>
<img width = '200' src = 'https://user-images.githubusercontent.com/39554623/159155575-6b34214a-3ff6-4c8f-85a3-e89f84d3fa12.png'>
</p>

```kotlin
class Car(private val engine: Engine) { // 객체를 생성자의 매개변수로 받음
    fun start() {
        engine.start()
    }
}

fun main(args: Array) {
    val engine = Engine()
    val car = Car(engine)
    car.start()
}
```

`Engine` 인스턴스를 생성해서 이를 통해 `Car` 인스턴스를 구성한다. 이러한 DI 기반 접근 방법의 이점은 다음과 같다.

- `Car`의 재사용 가능
  - `Engine`의 다양한 구현을 `Car`에 전달할 수 있다. 예를 들어 `Car`를 변경하고 싶을 때 `ElectricEngine`이라는 `Engine`의 서브클래스를 정의해서 의존성 주입을 통해 업데이트된 `ElectricEngine` 인스턴스를 전달하기만 하면 `Car`는 추가 변경 없이도 계속 작동한다.
- `Car`의 테스트 편의성
  - 테스트 더블을 전달하여 다양한 시나리오를 테스트할 수 있다. 예를 들어 `FakeEngine`이라는 `Engine`의 테스트 더블을 생성하여 다양한 테스트에 맞게 구성할 수 있다.

## Anroid에서 의존성 주입을 실행하는 두 가지 방법

- **생성자 삽입** : 클래스의 의존성을 생성자에 전달
- **필드 삽입(또는 setter 삽입)** : 액티비티 및 프래그먼트와 같은 특정 Android 프레임워크 클래스는 시스템에서 인스턴스화하므로 생성자 삽입이 불가능하다. 필드 삽입을 사용하면 의존성은 클래스가 생성된 후에 인스턴스화된다.

#### 필드 삽입을 통한 의존성 주입

```kotlin
class Car {
    lateinit var engine: Engine

    fun start() {
        engine.start()
    }
}

fun main(args: Array) {
    val car = Car()
    car.engine = Engine()
    car.start()
}
```

> 의존성 주입은 라이브러리가 프로그래머가 작성한 코드를 제어하는 <b>제어 반전<small>(Inversion of Control, IoC)</small> 원칙</b>을 기반으로 한다. 전통적인 프로그래밍 방법으로 프로그래머가 작성한 코드가 외부 라이브러리의 코드를 호출해서 이용한다.

IoC의 목적

- 작업을 구현하는 방식과 작업 수행 자체를 분리한다.
- 모듈을 제작할 때, 모듈과 외부 프로그램의 결합에 대해 고민할 필요 없이 모듈의 목적에 집중할 수 있다.
- 다른 시스템이 어떻게 동작할지에 대해 고민할 필요 없이, 미리 정해진 협약대로만 동작하게 하면 된다.
- 모듈을 바꾸어도 다른 시스템에 부작용을 일으키지 않는다.

## 정리

의존성 주입의 이점

- 코드의 재사용 및 의존성 분리 :
- 리팩토링 편의성 :
- 테스트 편의성 : 


## References

- https://en.wikipedia.org/wiki/Inversion_of_control
- https://en.wikipedia.org/wiki/Dependency_injection
- https://developer.android.com/training/dependency-injection#di-alternatives