# Understanding Compose (Android Dev Summit '19)

Compose는 어떤 문제를 해결해줄까? 이를 설명하기 위해선 **관심사의 분리**에 대해 이야기해야 한다.

### 관심사의 분리<small>(seperation of concerns)</small>

- 오래되고 널리 알려진 소프트웨어 디자인 원칙
- 결합도<small>(Coupling)</small> vs 응집도<small>(Cohesion)</small>의 관점에서 설명해야 Compose를 더 명확하고 쉽게 이해할 수 있다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129649874-169354e6-ffdd-48d5-9089-0373f7e4405c.png'>
</p>

추상적으로 말해서, 우리는 코드를 쓸 때 모듈을 염두에 두고 앱을 만든다. 그리고 모듈은 다수의 유닛으로 구성된다.

따라서 앱에는 여러 모듈이 들어있고 우리는 이 *모듈 간의 의존도*를 **결합도**라고 부른다.

> 결합도<small>(Coupling)</small>

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129650052-035fcee1-17cb-431c-afec-42ff5511a483.png'>
</p>

기본적으로 한 모듈의 어떤 부분이 다른 모듈에 영향을 주는 방법들이 있다.

어떤 코드에 변화를 주면 다른 파일들에 얼마나 영향을 주게 될 것인지가 결합도에 대한 부분이다.

일반적으로 결합도는 가능한 한 낮아야 한다. 가끔은 결합도가 드러나지 않기도 한다<small>(implicit)</small>.

우리가 생각하는 어떤 의존도가 확실히 보이지 않는데도 한 부분에서 변화가 발생했을 때 또 다른 한 쪽이 무너진다거나 하는 것이다.

> 응집도<small>(Cohesion)</small>

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129650066-053d1828-d3e5-4dca-8e30-6adbacfa8359.png'>
</p>

한편 응집도는 어떤 모듈 안의 유닛들이 어떻게 서로에게 속해있느냐에 대한 것이다. 유닛들은 서로 연관되어 있다.

응집도는 일반적으로 좋은 것으로 여겨진다.

그럼 관심사의 분리는 사실 관련된 코드를 가능한 한 같은 그룹으로 분류하는 것인데, 그러면 시간이 지나도 코드를 유지 관리할 수 있고 앱의 크기가 커져도 맞춰서 조정할 수 있게 된다.

> 일반적인 상황을 들어 설명

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129661863-57aeeb91-4c3d-4201-b844-3d7c15b2b998.png'>
</p>

뷰모델과 XML 레이아웃이 존재할 때, 뷰모델이 이 레이아웃으로 데이터를 보내주므로 뭔가를 표시해줘야 하는 뷰가 있는 것이다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129650163-d6b9ce34-5901-4c77-8407-5e3e0430c4e2.png'>
</p>

알고보니, 다음과 같이 여기에는 눈에 보이지 않는 의존도가 많이 있었다. 뷰모델과 레이아웃 사이에 결합도가 많이 있는 것이다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129650185-b78bdb24-9d2f-4604-8bb2-f780ec6ee59a.png'>
</p>

이걸 확인할 수 있는 더 익숙한 방법은 `findViewByID`이다. 우리는 XML 레이아웃이 무엇을 정의하는지 이해하고 관련된 특정 요소를 찾고 또 데이터를 주입하려 한다. 심지어는 XML 레이아웃에서 벌어지는 일들에 의존하기도 한다. 또 여기서 정의된 특정 구조에 의존할 수도 있다.

따라서 앱이 커져감에 따라 이들의 싱크가 맞도록 유지해야 한다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129650309-479c5241-ab7d-4e33-b7c5-f80b92ad75ba.png'>
</p>

앱은 꽤 커질 수 있으므로 이 XML 레이아웃도 엄청나게 커질 수 있다. 그러면 몹시 크고 복잡하며, 역동적이기까지한 UI가 생긴다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129650273-178f16f4-fbb5-46b6-8627-6f0709372cce.png'>
</p>


가끔씩 하나의 요소가 런타임에 비정적<small>(not statically)</small>으로 뷰 계층 구조를 떠나면 null reference exception이나 그 비슷한 결과로 이어지기도 한다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129650286-ef8fad28-5ec6-4329-98e8-b8620974aa1b.png'>
</p>

또 다른 기본적인 내용으로는 뷰모델은 코틀린으로 정의되어 있는 반면, XML 레이아웃은 XML로 정의되어 있다. 뷰모델과 XML 레이아웃은 아주 긴밀하게 연관될 때가 있는데도 불구하고 사용 언어가 다르기 때문에 강제로 구분선이 생기게 된다. 다시 말해 **결합도가 높다**는 것이다.

그럼 레이아웃, 즉 UI 구조를 같은 언어인 코틀린으로 정의하면 어떨까?

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129650299-042d1643-8712-4f88-b2bd-4f605f20a19f.png'>
</p>

이제 언어가 같기 때문에 의존도가 더 명시적으로 드러날 수 있다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129650514-358027f5-b952-4d0e-9aad-23bdcfc2aa54.gif'>
</p>

나아가서 코드를 리팩토링할 수 있고 요소들을 자신이 속하는 제 자리로 옮겨서 결합도를 낮추고 응집도를 높일 수 있다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129650545-df49c5e3-4e92-4795-9dc2-3f159eb298b0.gif'>
</p>

이러한 설명에 **로직**과 **UI**를 혼합한다는 말인가? 하고 의구심을 가질 수 있다.

프레임워크는 프로그래머의 관심사를 완벽하게 분리시키지는 **못한다**. 그건 오직 프로그래머 자신이 할 수 있는 일이다.

프로그래머의 UI에서 떨어질 수 없는 로직이 존재할 것이다. 프레임워크가 그걸 막을 순 없지만 관심사를 더 쉽게 분리할 수 있는 도구를 제공해줄 순 있다. 그 도구가 바로 <b>컴포저블 함수<small>(composable function)</small></b>이다.

## 컴포저블 함수의 구조와 동작원리

### 선언형 <small>(Declarative)</small>

보통 선언형이라고 하면 명령형<small>(Imperative)</small> 프로그래밍의 반대말로 쓰곤 한다.

예시를 통해 두 패러다임의 차이점을 알아보도록 하자!

> 읽지 않은 메시지 아이콘을 쓰는 UI

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/129670807-c64d8ae3-188e-4bd6-8142-fa6615f2dc8d.jpeg'>
</p>

읽지 않은 메시지 아이콘을 쓰는 UI가 있다고 가정할 때, 만약 메시지가 없다면 빈 봉투 모양을 렌더링하고, 메시지가 있다면 봉투 안에 종이 모양을 보여준다. 읽지 않은 메일이 100개 이상이면 종이 위에 불 모양을 추가한다.

명령형 인터페이스에서는 다음과 비슷한 updateCount 함수를 사용할 것이다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129671296-c3190e93-ef77-493c-a64b-dcbe101cf549.png '>
</p>

새 카운트 숫자를 받는 기능으로, 프로그래머는 이 UI가 적합한 상태를 표시하도록 하려면 어떻게 건드려야 할지 생각하게 된다. 여기엔 많은 코너 케이스들이 존재하는데, 비교적 쉬운 예시인데도 로직이 간단하지는 않다.

하지만 이 로직을 가져다 선언형 인터페이스에서 쓴다면 다음과 같은 결과가 나온다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/129671549-25453cb2-fda0-40d0-974a-d1ed6a5bd26b.png'>
</p>

위 코드는 다음과 같은 의미를 지닌다.

- 카운트가 99개 이상이면 불이 보이게 하고
- 또 카운트가 0개 이상이면 종이가 보이게 하고
- 카운트가 0개 이상이면 그 카운트 숫자의 뱃지가 보이게 한다.

선언형 API는 바로 이런 뜻이다.

### UI 개발자의 고려사항들

UI 개발자들은 다음과 같은 생각을 할 것이다.
- 나는 이 데이터들로 어떤 UI를 보여주고자 하는가?
- 이벤트에 어떻게 반응하고 또 어떻게 내 UI를 인터랙티브하게 만들까?
- <s>시간이 지남에 따라 UI가 어떻게 바뀔까?</s>

이제는 시간이 남에 따라 UI가 어떻게 바뀔지 생각할 필요가 없다. 이제는 데이터를 받을 때 그게 어떻게 표시되어야 하는지 알려주는 것이다. 즉, 다음 상태를 보여주는 것이다. 그리고 프레임워크가 어떻게 하나의 상태에서 다음으로 넘어가는지 제어한다. 그렇기 때문에 시간에 따른 UI의 변화에 대해 생각할 필요가 없는 것이다. 이것이 매우 중요한 점이다.

주어진 매개변수들을 가지고 UI를 표현하고 컴포저블 함수는 하나의 함수 정의에 불과하지만 UI의 모든 가능한 상태를 한 곳에서 표현할 수 있음을 이해해야 한다. 즉, 로컬로 정의된 것이다.

## Composition

Compose란 이름이나 `@Composable` 어노테이션에서 알 수 있듯이 컴포지션도 중요한 개념이다.

Compose에서의 컴포지션 모델은 상속이 따라오는 컴포지션과는 다르다. 둘 다 컴포지션 종류이지만 본문에서 다룰 컴포지션은 다른 종류이다.

예시를 통해 살펴보자.

View가 있다고 가정하고, Input을 만들고 싶다. 그래서

> 상속<small>(Inheritance)</small>

```kotlin
class Input : View() { /* ... */ }

class ValidatedInput : Input() { /* ... */ }

class DateInput : ValidatedInput() { /* ... */ }

class DateRangeInput : ??? { /* ... */ }
```

> Compose의 컴포지션 모델

```kotlin
@Composable
fun <T> Input(value: T, onChange: (T) -> Unit) { 
    /* ... */ 
}

@Composalbe
fun ValidatedInput(value: T, onChange: (T) -> Unit, isValid: Boolean) {
    InputDecoration(color = if (isValid) blue else red) {
        Input(value, onChange)
    }
}

@Composable
fun DateInput(value: DateTime, onChange: (DateTime) -> Unit) {
    ValidatedInput(
        value,
        onChange = { ... onChange(...) },
        isValid = isValidDate(value)
    )
}

@Composable
fun DateRangeInput(value: DateRange, onChange: (DateRange) -> Unit) {
    DateInput(value = value.start, ...)
    DateInput(value = value.end, ...)
}
```
