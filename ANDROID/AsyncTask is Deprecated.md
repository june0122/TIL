# AsyncTask is Deprecated

![async_task_deprecated](https://user-images.githubusercontent.com/39554623/72038179-e9d16280-32e3-11ea-9c95-36e969fd3cf6.jpg)

  지난 10년동안 **AsyncTask** 는 안드로이드에서 concurrent <sup>동시성</sup> 코드를 작성하는데 매우 넓게 사용된 방법이지만 매우 많은 논란을 일으키기도 했다. AsyncTask는 많은 안드로이드 어플리케이션들을 동작시켰고 여전히 동작하지만, 한편으론 대부분의 전문적인 안드로이드 개발자들은 솔직히 이 API를 싫어한다. 그러나 안드로이드 오픈소스 프로젝트에서 AsyncTask가 deprecated 되었다는 커밋이 올라왔기 때문에 AsyncTask의 시대는 이제 곧 끝날 것이다.

## AsyncTask이 쓰이지 않게 되는 공식적인 이유

AsyncTask의 공식적인 deprecation은 본 결정에 대한 동기이기도한 [이 커밋](https://android-review.googlesource.com/c/platform/frameworks/base/+/1156409/6/core/java/android/os/AsyncTask.java)에 소개되어있다.

새롭게 추가된 자바문서의 첫번째 단락은 아래와 같다.

> AsyncTask was intended to enable proper and easy use of the UI thread. However, the most common use case was for integrating into UI, and that would cause Context leaks, missed callbacks, or crashes on configuration changes. It also has inconsistent behavior on different versions of the platform, swallows exceptions from doInBackground, and does not provide much utility over using Executors directly.

구글의 공식 성명은 이러하지만, 여기에는 지적할 가치가 있는 몇몇 정확하지 않은 부분이 있다.

가장 먼저, AsyncTask는 "UI 스레드를 적절하고 쉽게 사용하게" 해줄 의도가 없었다. 장시간 실행되는 작업을 UI 스레드에서 백그라운드 스레드로 오프로드 한 다음 이러한 작업의 결과를 다시 UI 스레드로 전달하기위한 것이다. 이 지원 중단 메시지에서 가장 흥미로운 부분은 **"Context 누수, 콜백 누락 또는 구성 변경시 충돌 발생"** 이다. 따라서 구글은 기본적으로 AsyncTask의 가장 일반적인 사용 사례가 자동으로 매우 심각한 문제를 초래한다고 말하는 것이다. 하지만 AsyncTask를 사용하고도 완벽하게 작동하는 고품질의 어플리케이션들이 많고, AOSP <sup>Android open source project</sup> 내부의 일부 클래스에서조차 AsyncTask를 사용한다. 어째서 이러한 문제들이 발생하지 않았을까?

이 질문에 대해 답하기 위해선, **AsyncTask와 메모리 누수의 관계** 에 대해 자세히 알아볼 필요가 있다.

## AsyncTask와 메모리 누수 <sup>Memory Leaks</sup>

> 아래의 AsyncTask는 둘러싼 Fragment <sup>혹은 Acticity</sup>의 객체를 영원히 누출시킨다.

```java
@Override
public void onStart() {
    super.onStart();
    new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            int counter = 0;
            while (true) {
                Log.d("AsyncTask", "count: " + counter);
                counter ++;
            }
        }
    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
}
```

> Android Studio에서 해당 코드를 입력하면 아래의 이미지와 같이 누수에 대한 경고가 뜬다.

![AsyncTask Memory Leak](https://user-images.githubusercontent.com/39554623/72042215-25265e00-32f1-11ea-826a-16bf7143219b.png)

`This AsyncTask class should be static or leaks might occur (anonymous android.os.AsyncTask) A static field will leak contexts. Non-static inner classes have an implicit reference to their outer class. If that outer class is for example a Fragment or Activity, then this reference means that the long-running handler/loader/task will hold a reference to the activity which prevents it from getting garbage collected. Similarly, direct field references to activities and fragments from these longer running instances can cause leaks. ViewModel classes should never point to Views or non-application Contexts.`

- 이 문제에 대한 논의와 해결 방법은 Stackoverflow에서 [요약 답변](https://stackoverflow.com/a/44309450/12364882)과 [해결방안](https://stackoverflow.com/a/46166223/12364882)을 참고하자.

## AsyncTask의 문제점들

### 1. 멀티스레딩을 더욱 복잡하게 만든다 (Makes Multithreading More Complex)

AsyncTask의 핵심 셀링 포인트 중 하나는 스레드 클래스나 멀티스레딩 primitives를 직접 처리할 필요가 없다는 약속이다. 특히 새로운 안드로이드 개발자들을 위해 멀티스레딩을 간단하게 만들었어야 했다. 이야기만 들으면 참 좋아보이지만, 이 **"간단함(simplicity)"** 이 역효과를 몇 배로 일으켰다.

AsyncTask의 클래스 문서는 "thread"라는 단어를 16번이나 사용한다. 그렇기 때문에 스레드에 대한 이해가 없다면 쉽게 AsyncTask를 이해할 수 없다. 거기다 이 자바 문서는 AsyncTask 특유의 제약이나 환경을 가지고 있다. 다시 말해, AsyncTask를 이해하기 위해서는 스레드를 이해해야할 뿐만 아니라 AsyncTask 자체의 많은 뉘앙스를 이해할 필요가 있다. 이것은 아무리 생각해도 "간단"하지 않다.

더욱이 멀티스레딩은 본질적으로 복잡한 주제이다. 다른 개념들과 달리 멀티스레딩은 간단한 방법을 취할 수 없다. 아주 작은 실수라도 매우 심각한 버그로 이어질 수 있고, 그것을 조사하기가 매우 어렵기 때문이다.

그러므로 요약하자면 concurrency(동시성)을 간단화하는 방법은 없고, AsyncTask의 야망은 첫 시작부터 실패할 수 밖에 없는 운명이었던 것이다.

### 2. 잘못된 문서 (Bad Documentation)

안드로이드 문서가 최적화되지 않았다는 것은 공공연한 사실이다. 해가 지나며 점점 발전하고 있지만 오늘날에서도 괜찮다고 말하기엔 어려움이 있다. 문제가 많은 AsyncTask의 역사는 잘못된 문서가 가장 큰 요인이다. 실제로 그렇듯, AsyncTask가 과잉 성능(over-engineered)이고, 복작합고 미묘한 차이만 있는 멀티스레딩 프레임워크이지만 좋은 문서가 함께 있었다면 안드로이드 생태계의 일부분으로 남아있었을 것이다. 안드로이드 개발자들에게 익숙해진 엉망인 API들에는 부족함이 없지만, AsyncTask의 문서는 끔찍하고 다른 모든 결점을 악화시켰다.

가장 나쁜 것은 예시들이다. 이것들은 멀티 스레딩 코드를 작성하는 가장 불행한 접근법을 보여주었다. Activity들 내부의 모든 코드, 수명주기에 대한 완전한 무시와, 취소 시나리오에 대한 논의가 없는 등.. 이러한 예제를 자신의 어플리케이션에 적용한다면 메모리 누수 및 잘못된 동작이 거의 보장된다 보면 된다.

또한 AsyncTask의 설명서에는 멀티 스레딩과 관련된 핵심 개념에 대한 설명이 포함되어 있지 않다. 앞에서 언급한 메모리 누수에 대한 미신을 확산시키는 Android Studio의 규칙 역시 문서의 일부이다. 따라서 문서는 불충분 했을뿐만 아니라 부정확한 정보까지 포함하고 있다.

### 3. 과도한 복잡성 (Excessive Complexity)

AsyncTask는 세 개의 제네릭 인자를 가지고 있다. **세 개나!** 그 어떤 클래스에서도 이렇게나 많은 제네릭들을 요구하지는 않는다.

### 4. 상속의 남용 (Inheritance Abuse)

AsyncTask의 철학은 상속에 기초한다. 백그라운드에서 작업을 실행할 때마다 AsyncTask를 상속해야한다.

좋지 않은 문서와 결합해서, 상속 철학은 개발자들이 멀티 스레딩과, 도메인, UI 로직이 가장 비효율적이고 유지 보수하기 힘든 방식으로 결합된 거대한 클래스를 만들도록 강요하였다. 결국 AsyncTask API가 스스로 자초한 일이다.

Effective Java <sup>item 18, 114p</sup>의 **"상속보다는 컴포지션을 사용하라(Favor composiotion over inheritance)"** 규칙은 AsyncTask의 경우에 실질적인 차이를 만들 것이다. 흥미롭게도 Effective Java의 저자 조슈아 블로크는 구글에서 일했으며, 비교적 초기의 안드로이드 개발에 참여하였다.

### 5. 안정성 (Reliability)

간단히 말해, AsyncTask를 지원하는 기본 `THREAD_POOL_EXECUTOR`가 잘못 구성되어 신뢰하기가 어렵다. 구글은 [이 커밋](https://github.com/aosp-mirror/platform_frameworks_base/commit/a9be47cea45c19f2869732252e9922bf88fa4d86)과 [이 커밋](https://github.xn--4pd/aosp-mirror/platform_frameworks_base/commit/719c44e03b97e850a46136ba336d729f5fbd1f47)에서 같은 구성을 수 년간 적어도 두 번 조정했지만, [여전히 공식 안드로이드 설정 어플리케이션과 충돌](https://twitter.com/ArtemR/status/912145880760717312)한다.

대부분의 Android 어플리케이션들은 이 수준의 동시성이 필요하지 않다. 그러나 지금으로부터 1년 뒤에 어떤 쓰임새가 있을 지 모르므로, 신뢰할 수 없는 솔루션에 의존하는 것은 문제가 된다.

### 6. 동시성 오해 (Concurrency Misconception)

2번 항목인 잘못된 문서와 관련이 있지만 그 자체로 글 머리 기호가 필요하다고 생각한다. `executeOnExecutor()` 메소드에 대한 자바 문서의 설명은 아래와 같다.

> Allowing multiple tasks to run in parallel from a thread pool is generally not what one wants, because the order of their operation is not defined. (...) Such changes are best executed in serial; to guarantee such work is serialized regardless of platform version you can use this function with SERIAL_EXECUTOR

하지만 이 설명은 잘못 되었다. 여러 작업을 동시에 실행하도록 허용하는 것은 UI 스레드에서 작업을 오프로드 할 때 대부분의 경우 원하는 것이다.

예를 들어, 네트워크 요청을 보내었는데 어떠한 이유로 시간이 초과했다고 가정해보자. OkHttp의 기본 시간 제한은 10초이다. 만약 주어진 순간에 하나의 작업만 실행하는 SERIAL_EXECUTOR을 실제로 사용하는 경우 앱의 모든 백그라운드 작업이 10초동안 중지된다. 그리고 두 개의 요청을 보내고 둘 다 시간이 초과된다면? 20초동안 백그라운드 처리가 없다. 결국 네트워크 요청 시간 초과는 예외가 아니며 데이터베이스 쿼리, 이미지 처리, 계산, IPC 등 거의 모든 다른 사용 사례에서 동일하다.

그렇다. 문서에 명시된 바와 같이 스레드 풀과 같이 오프로드된 작업 순서는 동시에 실행되므로 정의되지 않는다. 하지만 이것은 문제가 되지 않는다. 사실, 이것이 동시성의 정의에 꽤나 가깝다.

따라서 공식 문서의 설명은 AsyncTask의 저자들 사이에서 동시성에 대한 매우 심각한 오해가 있다는 것을 지적하고 있다할 수 있다.

## 안드로이드 멀티 스레딩의 미래

AsyncTask의 deprecation이 남긴 빈자리는 다른 멀티 스레딩 접근법으로 대체 되어야만 한다. 어떤 것이 그 빈자리를 채울 수 있을까?

**Java**를 통해서 안드로이드를 시작할 경우, 가장 기본적인 스레드 클래스 <sup>bare Thread class</sup>와 UI Hanlder를 이용하는 것을 이 본문의 필자는 추천한다. 많은 안드로이드 개발자들이 반대하겠지만 이러한 접근법은 AsyncTask보다 훨씬 더 효과적이었다. 실제로 많은 개발자들도 이에 대해 동의한다는 것을 [SNS 투표](https://twitter.com/VasiliyZukanov/status/1194538431776509954)에서 확인할 수 있었다.

만약 **Kotlin**을 사용할 경우엔 위에서 추천한 방법도 여전히 유효하지만, 고려할 수 있는 사항이 한 가지 더 존재한다. 바로 **Coroutuines 프레임워크**로, Kotlin 동시성의 공식 기본 개념 <sup>primitive</sup>으로 채택될 것으로 보인다. Kotlin이 안드로이드에서 스레드를 내부적으로 사용한다하더라도, Coroutines은 언어의 문서와 튜토리얼에서 추상화의 최소 단계가 될 것이다.

무슨 접근법을 선택하든 가장 중요한 것은, 멀티 스레딩의 기본을 습득하기 위해 시간을 투자해야한다. 본문에서 보았듯, 동시성 코드의 정확도는 프레임워크에 의해서 결정되지 않고, 근본적인 원리의 이해로 결정된다.