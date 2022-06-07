# ViewModel에서 MutableLiveData object의 NullPointerException 문제

```console
java.lang.NullPointerException: Attempt to invoke virtual method 'void androidx.lifecycle.MutableLiveData.setValue(java.lang.Object)' on a null object reference
```

#### 에러 발생 코드

```kotlin
@HiltViewModel
class DarkModeViewModel @Inject constructor(
    private val themeManager: ThemeManager,
    private val preferencesRepositoryImpl: PreferencesRepositoryImpl
) : ViewModel(), DrakModeClickListener {

    init {
        viewModelScope.launch {
            _mode.value = preferencesRepositoryImpl.flowDarkModeIds().first()
        }
    }

    private val _mode = MutableLiveData<Int>()
    val mode: LiveData<Int> = _mode

    override fun onModeClick(mode: Int) {
        themeManager.saveDarkMode(mode)
        _mode.value = mode
    }
}
```

#### 수정 코드

```kotlin
@HiltViewModel
class DarkModeViewModel @Inject constructor(
    private val themeManager: ThemeManager,
    private val preferencesRepositoryImpl: PreferencesRepositoryImpl
) : ViewModel(), DrakModeClickListener {

    private val _mode = MutableLiveData<Int>().also {
        viewModelScope.launch {
            it.value = preferencesRepositoryImpl.flowDarkModeIds().first()
        }
    }
    val mode: LiveData<Int> = _mode

    override fun onModeClick(mode: Int) {
        themeManager.saveDarkMode(mode)
        _mode.value = mode
    }
}
```

## References

- https://stackoverflow.com/a/69780098/12364882