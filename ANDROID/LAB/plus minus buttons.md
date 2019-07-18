# Plus Minus Buttons

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/61442029-c4d9e380-a981-11e9-9969-0c2382d61769.gif'>
</p>
<br>

## MainActivity 클래스

> 기존 형태 : `findViewById` 사용

- 아래의 코드처럼 Toast에 대한 옵션은 `MainActivity`에 넣는 것이 아니라, 별도의 xml 파일로 뽑아내는 것이 좋다.

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val errorToast: Toast = Toast.makeText(this, R.string.warning_text, Toast.LENGTH_SHORT)
        val toastView: View = errorToast.view
        toastView.setBackgroundResource(R.color.colorControlHighlight)
        errorToast.setGravity(Gravity.CENTER, 0, 80)



        var result = 0
        val tv: TextView = findViewById(R.id.resultView)

        plusButton.setOnClickListener {
            tv.text = (++result).toString()
        }

        minusButton.setOnClickListener {
            when {
                (result < 0 || result == 0) -> errorToast.show()
                else -> tv.text = (--result).toString()
            }
        }

    }

}
```

<br>

> 개선 1 : `findViewById`를 사용하지 않기

- `++` 혹은 `--` 와 같은 연산자보다는 `+=` 과 `-=`을 사용하는 것이 좋다.

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val errorToast: Toast = Toast.makeText(this, R.string.warning_text, Toast.LENGTH_SHORT)
        val toastView: View = errorToast.view
        toastView.setBackgroundResource(R.color.colorControlHighlight)
        errorToast.setGravity(Gravity.CENTER, 0, 80)

        var result = 0

        plusButton.setOnClickListener {
            result += 1
            resultView.text = "$result"
        }

        minusButton.setOnClickListener {
            when {
                (result < 0 || result == 0) -> errorToast.show()
                else -> {
                    result -= 1
                    resultView.text = "$result"
                }
            }
        }
    }

}
```

<br>

> 개선 2 : Delegation Pattern 적용

```kotlin
class MainActivity : AppCompatActivity() {
    private var resNum: Int by Delegates.observable(0) { _, _, new ->
        resultView.text = "$new"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val errorToast: Toast = Toast.makeText(this, R.string.warning_text, Toast.LENGTH_SHORT)
        val toastView: View = errorToast.view
        toastView.setBackgroundResource(R.color.colorControlHighlight)
        errorToast.setGravity(Gravity.CENTER, 0, 80)

        plusButton.setOnClickListener {
            resNum += 1
        }

        minusButton.setOnClickListener {
            when {
                (resNum < 0 || resNum == 0) -> errorToast.show()
                else -> {
                    resNum -= 1
                }
            }
        }
    }

}
```
<br>

## Resource 내부, ripple effect & selector 함께 적용

> `res/drawable/add_button.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:color="@color/colorAccent"
    android:radius="32dp">
    <item>
        <selector xmlns:android="http://schemas.android.com/apk/res/android"
            tools:ignore="RedundantNamespace">
            <item
                android:drawable="@drawable/ic_add_pressed"
                android:state_pressed="true" />
            <item
                android:drawable="@drawable/ic_add_default"
                android:state_pressed="false" />
        </selector>
    </item>
</ripple>
```

<br>

> `res/layout/activity_main.xml`

```xml
<ImageButton
        android:id="@+id/plusButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="72dp"
        android:layout_marginBottom="192dp"
        android:background="@drawable/add_button"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        tools:ignore="ContentDescription" />
```


<br>

> `ic_add_default.xml`

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="80dp"
    android:height="80dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#1565c0"
        android:pathData="M12,7c-0.55,0 -1,0.45 -1,1v3L8,11c-0.55,0 -1,0.45 -1,1s0.45,1 1,1h3v3c0,0.55 0.45,1 1,1s1,-0.45 1,-1v-3h3c0.55,0 1,-0.45 1,-1s-0.45,-1 -1,-1h-3L13,8c0,-0.55 -0.45,-1 -1,-1zM12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM12,20c-4.41,0 -8,-3.59 -8,-8s3.59,-8 8,-8 8,3.59 8,8 -3.59,8 -8,8z" />
</vector>
```
