# State and Jetpack Compose

```kotlin
@Composable
fun HelloContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello!",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text(text = "Name") }
        )
    }
}
```

```kotlin
@Composable
fun HelloContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        // mutableStateOf() creates a mutable state, which is an observable type in Compose
        // 이 값이 변경되면 해당 값을 읽는 컴포저블 함수들의 재구성이 스케쥴 된다.
        var name by remember { mutableStateOf("") }
        
        Text(
            text = "Hello, $name!",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name") }
        )
    }
} 
```

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/129935091-fdd41b9b-4cf0-4a8c-ac36-1ed186edcd4f.gif'>
</p>

Remember helps us preserve the state across re-compositions. Otherwise, if we would just use mutableStateOf without remember, every time our HelloContent composable gets recomposed, the state gets reinitialized to an empty string.

We can use the remembered value as a parameter for other composables, or even as logic in statements to change what composable's already displayed.

For example, we don't want to display the greeting if the name is empty, so we can use the state in an if statement.

```kotlin
@Composable
fun HelloContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        var name by remember { mutableStateOf("") }

        if (name.isNotEmpty()) { // if문 추가
            Text(
                text = "Hello, $name!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name") }
        )
    }
}
```

While remember helps us retain state across re-compositions, the state won't be retained across configuration changes. For this, we'll have to use rememberSaveable. RememberSavable will automatically save any value that can be saved in a bundle. But if that's not the case, you can pass in a custom saver object.

```kotlin
@Composable
fun HelloContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        var name by rememberSaveable { mutableStateOf("") } // remember -> rememberSaveable

        if (name.isNotEmpty()) {
            Text(
                text = "Hello, $name!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name") }
        )
    }
}
```

Right now, our composable holds its own state. This makes the composable hard to reuse, hard to test, and keeps the composable tightly coupled to how the state is stored.

So we should name this a stateless composable: a composable that doesn't hold any state.

To do this, we can use state hoisting.

State hoisting is a programming pattern where you move the state to the caller of a composable.

A simple way to do it is by replacing the state with a parameter and use functions to represent events.

The parameter is the current value to be displayed, and the event is a lambda function that gets triggered whenever the state needs to be updated.

In our case, we extract the name and the onValueChange out of HelloContent and move them higher to a HelloScreen composable that calls HelloContent. HelloContent has access to the state as an immutable string parameter as well as a lambda onNameChange that it can call when it wants to request the state change.

```kotlin
@Composable
fun HelloScreen() {
    var name by rememberSaveable { mutableStateOf("") }
    HelloContent(name = name, onNameChange = { name = it })
}

@Composable
fun HelloContent(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {

        if (name.isNotEmpty()) {
            Text(
                text = "Hello, $name!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(text = "Name") }
        )
    }
}
```

Lambdas are the most common way to describe events on a composable.

Here, we're defining an event called onNameChange using a lambda that takes a string using Kotlin's function type syntax. We're using onNameChange, present tense, as the event doesn't mean that the state has already changed, but that the composable is requesting that the event handler change it.



```kotlin
 @Composable
fun HelloScreen() {
    var name by remember { mutableStateOf("") }
    HelloContent(name = name, onNameChange = { name = it })
}

@Composable
fun HelloContent(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello, $name!",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(text = "Name") }
        )
    }
}
```

Like this, the state goes down from HelloScreen to HelloContent, and events go up from HelloContent to HelloScreen, making HelloContent more reusable and testable than before.

But what if we want to use the name in other layers of our app?

Maybe we want to save it in a database. The best way to do this is to use a view model that will store the state in an observable holder and handle events.

So we can create a HelloViewModel that extends the ViewModel class.

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/129942318-16e83154-3698-48da-82c6-0bbcd9c24660.png'>
</p>

```build.gradle
implementation "androidx.compose.runtime:runtime-livedata:$compose_version"
```

```kotlin
class HelloViewModel : ViewModel() {

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name
}

@Composable
fun HelloScreen(helloViewModel: HelloViewModel = viewModel()) {
    val name by helloViewModel.name.observeAsState("")
    HelloContent(name = name, onNameChange = { name = it })
}
```

The view model holds and exposes the name in a LiveData, which then can be observed as state and passed to a composable.

Like this, every time the value in LiveData changes, it will automatically trigger a re-composition of HelloContent.

As the view model survives configuration changes, we don't need to do anything else to persist the UI state.

Under the hood, `observeAsState` will also remember the state for it so it can survive re-composition.

> event handing 호이스팅

```kotlin
class HelloViewModel : ViewModel() {

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    // hoist the event handling to the view model.
    fun onNameChange(newName: String) {
        _name.value = newName
    }
}

@Composable
fun HelloScreen(helloViewModel: HelloViewModel = viewModel()) {
    val name by helloViewModel.name.observeAsState("")
    HelloContent(name = name, onNameChange = { helloViewModel.onNameChange(it) })
}
```

Then, we also hoist the event handling to the view model.

Like this, the view model is the one that updates the state every time an event happens.

As the UI state can be changed from other layers of the app, like based on the response of a network call, the view model allows us to better encapsulate the state and create a single source of truth for the UI, which makes it less likely to create inconsistent states.

> 최종 코드

```kotlin
class HelloViewModel : ViewModel() {

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    // hoist the event handling to the view model.
    fun onNameChange(newName: String) {
        _name.value = newName
    }
}

@Composable
fun HelloScreen(helloViewModel: HelloViewModel = viewModel()) {
    val name by helloViewModel.name.observeAsState("")
    HelloContent(name = name, onNameChange = { helloViewModel.onNameChange(it) })
}

@Composable
fun HelloContent(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello, $name!",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(text = "Name") }
        )
    }
}
```

## 요약

- Persist state across re-composition: `remember`
- Persist state across configuration changes: `rememberSaveable`
- _Hoist the state_ for re-usability and testability
- Use ViewModel with LiveData and `observeAsState`

If your state is internal to a composable, make sure you use `remember` to persist the state across re-composition.

Use `rememberSaveable` to persist the state across configuration changes.

Where possible, try to hoist the state to make the composable more reusable and testable.

Finally, use the "ViewModel" to hold an exposed state in an observable state holder like LiveData and to handle events.

## References

- Android developers - [Jetpack Compose: State](https://www.youtube.com/watch?v=mymWGMy9pYI)
- Android developers Youtube - [State and Jetpack Compose](https://developer.android.com/jetpack/compose/state)