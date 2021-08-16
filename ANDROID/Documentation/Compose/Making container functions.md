# 컨테이너 함수 만들기

> 수정 전 코드

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    BasicsCodelabTheme {
        Surface(color = Color.Yellow) {
            Greeting(name = "Android")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!", modifier = Modifier.padding(24.dp))
}

@Preview
@Composable
fun DefaultPreview() {
    MyApp()
}
```

앱의 모든 공통 구성<small>(configuration)</small>이 포함된 컨테이너를 만들고 싶다면 어떻게 해야 할까?

범용적인 컨테이너를 만들려면, `Unit`을 반환하는 컴포저블 함수<small>(여기서는 `content`라고 함)</small>를 매개변수로 사용하는 컴포저블 함수를 만들어야 한다.

컴포저블 함수는 UI 컴포넌트를 반환하지 않고 방출<small>(emit)</small>하기 때문에 `Unit`을 반환한다. 이것이 컴포저블 함수가 `Unit`을 반환하는 이유이다.

```kotlin
@Composable
fun MyApp(content: @Composable () -> Unit) {
    BasicsCodelabTheme {
        Surface(color = Color.Yellow) {
            content()
        }
    }
}
```

> 참고 : 컴포저블 함수를 매개변수로 사용할 때, `@Composable ()`의 괄호에 주의한다. 애노테이션은 함수에 적용되므로 괄호가 꼭 필요하다!

```kotlin
fun MyApp(content: @Composable () -> Unit) { ... }
```

함수 내에서 컨테이너가 제공할 공유 구성<small>(shared configuration)</small>을 정의한 다음 전달된 자식 컴포저블을 호출한다. 예시에서는 `MaterialTheme`와 노란색 Surface를 적용하고 `content()`를 호출하려고 한다.

참고로 코틀린의 [trailing lambda syntax](https://kotlinlang.org/docs/reference/lambdas.html#passing-a-lambda-to-the-last-parameter)를 활용하여 다음과 같이 작성할 수 있다.

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Greeting("Android")
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    BasicsCodelabTheme {
        Surface(color = Color.Yellow) {
            content()
        }
    }
}

@Preview("Text preview")
@Composable
fun DefaultPreview() {
    MyApp {
        Greeting("Android")
    }
}
```

이 코드는 변경 전에 사용한 코드와 동일하지만 더욱 유연하다. 컨테이너 컴포저블 함수를 만드는 것은 가독성을 높이고 코드 재사용을 권장하는 좋은 방법이다.

## References

- Codelabs : [Jetpack Compose basics](https://developer.android.com/codelabs/jetpack-compose-basics?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fcompose%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-basics#3)