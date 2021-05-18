# [Android] Do not concatenate text displayed with setText

## 문제 개요

<p align = 'center'>
<img width = '800' src = 'https://user-images.githubusercontent.com/39554623/118576969-83f0c700-b7c4-11eb-956c-cebca0709b8d.png'>
</p>

TextView의 내용을 `setText()` 메서드를 이용해 설정할 때  **Do not concatenate text displayed with setText. Use resource string with placeholders.** 라는 경고문을 자주 볼 수 있다.

대략 setText로 표시된 텍스트를 `+`로 연결해서 사용하지 말고 리소스 문자열을 placeholder와 함께 사용하라는 내용인데 어떻게 해결할 수 있을까? 

## 해결

답은 [**문자열 서식 지정**](https://developer.android.com/guide/topics/resources/string-resource?hl=ko#%EB%AC%B8%EC%9E%90%EC%97%B4-%EC%84%9C%EC%8B%9D-%EC%A7%80%EC%A0%95)에 있다. 아래의 코드와 같이 문자열 리소스에서 원하는 자리에 들어갈 값을 서식 인수 <sup>format argument</sup>를 추가하여 지정하는 것이다.

```xml
<string name="welcome_messages">Hello, %1$s! You have %2$d new messages.</string>
```

위의 예시는 문자열에 두 개의 서식 인수가 존재하는데 `%1$s`에는 문자열이, `%2$d`에는 10진수 값이 들어간다. 여기서 주의해야 할 부분은 서식 인수의 syntax인데 아래와 같은 형태이다.

> %[`인자의 인덱스`$]`서식 지정자`

- 인자의 인덱스 <sup>argument index</sup>는 문자열 리소스에서 서식 **인수의 인덱스**를 선택할 수 있게 해준다. 첫 번째 인자는 `1$`, 두 번째는 `2$`, 세 번째는 `3$`와 같은 식이다.
- 서식 지정자 <sup>format specifier</sup>는 **데이터 타입**을 지정하는 것인데, C언어 등에서 흔히 사용하는 `%d`, `%s`와 같은 서식 지정자와 동일한 개념이다.

문자열 리소스에서 서식 인수를 지정했다면 [`getString(int, Object...)`](https://developer.android.com/reference/android/content/Context?hl=ko#getString(int,%20java.lang.Object...))을 통해 아래의 코드와 같이 매개변수의 첫 번째 인자로 해당 문자열 리소스를 전달하고 차례로 서식 인수들에 들어갈 값을 전달하면 된다.

```kotlin
var text = getString(R.string.welcome_messages, username, mailCount)
```

결과적으로 `+`로 연결된 텍스트가 아닌 문자열 서식 지정을 이용하여 `getString(int, Object...)`의 값을 `setText()`에 넣어주면 된다.

```kotlin
testTextView.text = getString(R.string.welcome_messages, username, mailCount)
```

## References

- Android Developers 가이드 | [문자열 서식 지정](https://developer.android.com/guide/topics/resources/string-resource?hl=ko#%EB%AC%B8%EC%9E%90%EC%97%B4-%EC%84%9C%EC%8B%9D-%EC%A7%80%EC%A0%95)

- Android Developers 가이드 | [getString](https://developer.android.com/reference/android/content/Context?hl=ko#getString(int,%20java.lang.Object...))

- StackOverflow | [Android TextView : “Do not concatenate text displayed with setText”](https://stackoverflow.com/questions/33164886/android-textview-do-not-concatenate-text-displayed-with-settext)