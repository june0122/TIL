# [Fragment (프래그먼트)](https://developer.android.com/guide/components/fragments?hl=ko)

### 코틀린 프래그먼트에서 뷰에 접근할 때 NullPointerException이 발생하는 이유

> 코틀린 프래그먼트에서 뷰들에 접근할 때, `onViewCreated` 메소드 내에서 원소들을 초기화해주는 것이 더 좋은 접근 방법이다.

- 코틀린 합성 프로퍼티<sup id = "a1">[1](#f1)</sup> 는 마법처럼 간단하게 작동하지 않는다.

  - 예로 xml 파일에 생성한 버튼 `testButton`에 접근할 때, 이것은 `getView().findViewById(R.id.testButton)`을 호출한다. 이럴 경우의 문제는 너무 빠르게 접근하기 때문에 `getView()`는 `onCreateView` 메소드 내에서 `null`을 리턴한다.

- 이를 해결하기 위해선 `onViewCreated` 메소드를 이용하면 된다.
 
```kotlin
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    testButton.setOnClickListener { Log.d(TAG, "onViewCreated(): hello world"); }
}
```

<br>

> Android Acticty/Fragment Lifecycle

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/64147614-ce4cdb80-ce5b-11e9-9e91-b34b44150df7.png'>
</p>
<br>

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/64147612-cdb44500-ce5b-11e9-8539-e94dc90a0da2.png'>
</p>
<br>

## 각주

### <b id = "f1"><sup> 1 </sup></b> 코틀린 합성 프로퍼티 [ ↩](#a1)

코틀린 코드에서 뷰 인스턴스에 접근할 수 있도록 지원하기 위해 **코틀린 안드로이드 익스텐션<sup> Kotlin Android Extension</sup>** 은 클래스 내에 뷰 ID 이름으로 된 가상의 프로퍼티를 생성하며, 이렇게 코틀린 코드 외부의 요소와의 조합을 통해 만들어진 프로퍼티를 **합성 프로퍼티<sup> synthetic property</sup>** 라 한다.