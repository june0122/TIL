# [What is the exact meaning of 'emit' in Android Jetpack Compose?](https://stackoverflow.com/questions/68798924/what-is-the-exact-meaning-of-emit-in-android-jetpack-compose/68825083#68825083)

## Question

The word emit is often used in Jetpack Compose's documentation or codelabs, as follows:

> The function doesn't return anything. Compose functions that "emit" UI do not need to return anything, because they describe the desired screen state instead of constructing UI widgets.

What is the exact meaning of emit in Android Jetpack Compose?

Who handles the UI emitted by the Compose function? Does the Compose framework detect and process the emitted UI?

Is there documentation with information on how and by whom the emitted UI is handled?

## Answer by [EpicPandaForce](https://stackoverflow.com/a/68825083/12364882)

"Emit" means that Compose inserts a new group into the current composition.

See the [source code](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/runtime/runtime/src/commonMain/kotlin/androidx/compose/runtime/Composables.kt;l=245?q=applier%20compose&start=21):

```kotlin
@Suppress("NONREADONLY_CALL_IN_READONLY_COMPOSABLE", "UnnecessaryLambdaCreation")
@Composable inline fun <T : Any, reified E : Applier<*>> ReusableComposeNode(
    noinline factory: () -> T,
    update: @DisallowComposableCalls Updater<T>.() -> Unit
) {
    if (currentComposer.applier !is E) invalidApplier()
    currentComposer.startReusableNode()   // <--- EMITTING THE NODE
    if (currentComposer.inserting) {
        currentComposer.createNode { factory() }
    } else {
        currentComposer.useNode()
    }
    currentComposer.disableReusing()
    Updater<T>(currentComposer).update()
    currentComposer.enableReusing()
    currentComposer.endNode()
}
```