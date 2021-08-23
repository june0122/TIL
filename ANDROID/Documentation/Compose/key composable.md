# `key` 컴포저블

```kotlin
@Composable
inline fun <T : Any?> key(keys: vararg Any?, block: () -> T): @Composable T
```

`key`는 컴포지션 내부에서 실행 블록을 "그룹화"하거나 "key"하는데 사용되는 유틸리티 컴포저블이다. 이는 주어진 컴포저블 호출이 컴포지션 중에 두 번 이상 실행되도록 할 수 있는 제어 흐름 내부의 정확성을 위해 때때로 필요하다.

키 값은 *전역적으로 고유할 필요가 없으며*, 컴포지션의 해당 지점에서의 `key` 호출 사이에만 고유하면 된다.

예를 들어 다음 예를 보자.

```kotlin
import androidx.compose.runtime.key

for (user in users) {
    key(user.id) { UserPreview(user = user) }
}

for (user in users.filter { isAdmin }) {
    key(user.id) { Friend(friend = user) }
}
```

상단 루프와 하단 루프에 모두 동일한 id를 가진 사용자가 구성되어 있어도 `key`에 대한 호출이 다르기 때문에 복합 키<small>(compound key)</small>를 생성할 필요가 없다.

그러나 키는 컬렉션의 각 요소에 대해 고유해야 한다. 그렇지 않으면 자식<small>(children)</small> 및 로컬 상태가 의도하지 않은 방식으로 재사용될 수 있다.

예를 들어 다음 예를 보자.

```kotlin
import androidx.compose.runtime.key

for ((child, parent) in relationships) {
    key(parent.id) {
        User(user = child)
        User(user = parent)
    }
}
```

이 예에서는 `parent.id`가 컬렉션의 각 항목에 대한 고유 키라고 가정하지만, 이는 부모가 단 하나의 자식만 가질 수 있다고 가정하는 것이 타당할 경우에만 해당되며, 그렇지 않을 수도 있다. 대신 다음과 같이 하는 것이 더 정확할 수 있다.

```kotlin
import androidx.compose.runtime.key

for ((child, parent) in relationships) {
    key(parent.id to child.id) {
        User(user = child)
        User(user = parent)
    }
}
```

여러 인수를 전달하여 복합 키를 만들 수 있다:

```kotlin
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

for (element in elements) {
    val selected by key(element.id, parentId) { remember { mutableStateOf(false) } }
    ListItem(item = element, selected = selected)
}
```

|Parameters||
|:-|:-|
|keys: vararg Any?|복합 키를 만드는데 사용할 값들의 집합이다.<br>이는 equals 및 hashCode를 사용하여 이전 값과 비교된다.|
|block: () -> T|이 그룹의 자식 컴포저블<small>(The composable children for this group.)</small>|

## References

- Android developers : [androidx.compose.runtime.key](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary#key(kotlin.Array,kotlin.Function0))