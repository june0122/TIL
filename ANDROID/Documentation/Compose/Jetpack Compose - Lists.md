# Jetpack Compose - Lists

많은 앱에서 항목의 컬렉션을 표시해야 합니다. 이 문서에서는 Jetpack Compose에서 이 작업을 효율적으로 처리하는 방법을 설명합니다.

스크롤이 필요하지 않은 경우 (방향에 따라) 간단한 `Column` 또는 `Row`를 사용하여 다음과 같이 목록을 반복하여 각 항목의 콘텐츠를 내보낼 수 있습니다.

```kotlin
@Composable
fun MessageList(messages: List<Message>) {
    Column {
        messages.forEach { message ->
            MessageRow(message)
        }
    }
}
```

`verticalScroll()` 수정자를 사용하여 `Column`을 스크롤 가능하게 만들 수 있습니다. 자세한 내용은 [Gestures](https://developer.android.com/jetpack/compose/gestures) 문서를 참조하세요.