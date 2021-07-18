# RecyclerView로 리스트 보여주기

> 본문에서 예시로 설명할 앱 CriminalIntent의 객체 다이어그램

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/119100962-713ff180-ba53-11eb-8175-b7cdc878cea9.jpeg'>
</p>

## 새로운 프래그먼트 및 ViewModel 추가하기

> CrimeListViewModel

```kotlin
class CrimeListViewModel: ViewModel() {

    val crimes = mutableListOf<Crime>()

    // 모의 데이터 채우기
    init {
        for (i in 0 until 100) {
            val crime = Crime()
            crime.title = "Crime #$i"
            crime.isSolved = i % 2 == 0
            crimes += crime
        }
    }
}
```

> CrimeListFragment 생성 및 구현

```kotlin
private const val TAG = "CrimeListFragment"

class CrimeListFragment: Fragment() {

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    companion object {
        fun newInstance() : CrimeListFragment {
            return CrimeListFragment()
        }
    }
}
```

동반 객체에서 정의된 `newInstance()` 함수에서는 CrimeListFragment 인스턴스를 생성하고 반환한다.
- 이때 `newInstance()` 함수는 CrimeListFragment 클래스의 인스턴스를 생성하지 않고 호출할 수 있다.  자바의 static 메서드와 유사
- 따라서 액티비티에서 CrimeListFragment 인스턴스를 생성하려면 `newInstance()` 함수를 호출하면 된다.

### 프래그먼트에 사용되는 ViewModel 생명주기

[[Android] ViewModel과 SIS](https://june0122.github.io/2021/05/13/android-bnr-04/)에서 액티비티와 함께 사용되는 ViewModel의 생명주기를 알아보았다. 그런데 ViewModel이 프래그먼트와 같이 사용되면 생명주기가 약간 달라진다. 이때도 두 개의 상태 <sub>('생성됨'또는 '소멸되어 존재하지 않음')</sub>만 갖는 것은 동일하나, 액티비티 대신 프래그먼트의 생명주기와 결합된다.

다시 말해, 프래그먼트의 뷰가 화면에 나타나 있는 한 ViewModel은 활성화된 상태를 유지하며, 장치 회전 시에도 유지되어 새로 생성된 프래그먼트 인스턴스가 이어서 사용할 수 있다.

ViewModel은 프래그먼트가 소멸할 때 같이 소멸한다. 예를 들어, 사용자가 백 버튼을 누르거나 호스팅 액티비티가 프래그먼트를 다른 것으로 교체할 때 해당 액티비티가 화면에 나타나 있더라도 프래그먼트 및 이것과 연관된 ViewModel은 더 이상 필요 없으므로 소멸한다.

한 가지 특별한 경우는 *프래그먼트 트랜잭션을 백 스택에 추가*할 때다. 만일 액티비티가 현재 프래그먼트를 다른 것으로 교체할 때 트랜잭션이 백 스택에 추가된다면 해당 프래그먼트 인스턴스와 이것의 ViewModel은 소멸되지 않는다. 따라서 사용자가 백 버튼을 누르면 프래그먼트 트랜잭션이 역으로 수행되어 교체되기 전의 프래그먼트 인스턴스가 다시 화면에 나타나고 ViewModel의 모든 데이터는 보존된다.

> 프래그먼트 트랜잭션을 사용해서 CrimeListFragment 추가하기

CrimeFragment 대신 CrimeListFragment의 인스턴스를 호스팅하도록 변경

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
//          val fragment = CrimeFragment()
            val fragment = CrimeListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
}
```

현재는 MainActivity가 항상 CrimeListFragment를 보여주도록 하드코딩되어 있지만, 후에는 사용자가 앱 화면을 이동하는 것에 따라 MainActivity가 CrimeListFragment와 CrimeFragment를 상호 교체하도록 변경할 것이다.

## RecyclerView 추가하기

RecyclerView 클래스는 다른 Jetpack 라이브러리에 있어서 사용하려면 우선 RecyclerView 라이브러리 의존성을 app/build.gradle 파일에 추가해야 한다.

> RecyclerView 의존성 추가하기

```kotlin
dependencies {
    ...
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
    implementation 'androidx.recyclerview:recyclerview:1.2.0'
    ...
}
```

> RecyclerView를 레이아웃 파일에 추가하기

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.recyclerview.widget.RecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/crime_recycler_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

CrimeListFragment의 뷰가 준비되었으니 이 뷰를 프래그먼트와 연결한다.

> CrimeListFragment의 뷰 설정하기

```kotlin
class CrimeListFragment: Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        
        return view
    }
    ...
}
```

RecyclerView가 생성된 후에는 곧바로 LayoutManager를 설정해야 하며, 여기서는 LinearLayoutManager를 설정했다. 만일 설정하지 않으면 RecyclerView가 작동하지 않는다.

RecyclerView는 항목들을 화면에 위치시키는 일을 직접하지 않고 LayoutManager에게 위임한다. 그러면 LayoutManager는 모든 항목들의 화면 위치를 처리하고 스크롤 동작도 정의한다. 따라서 LayoutManager가 설정되지 않으면 RecyclerView의 작동이 바로 중단된다.

LayoutManager는 안드로이드 프레임워크에 내장된 것 중 하나를 선택할 수 있으며, 서드파티 라이브러리의 것도 사용할 수 있다. 여기서는 LinearLayoutManager를 사용했는데, 이것은 리스트의 항목들을 수직 방향으로 배치한다. 이 책 후반부에서는 GridLayoutManager를 사용해서 격자 형태로 항목들을 배치할 것이다.

## 항목 뷰 레이아웃 생성하기

RecyclerView는 ViewGroup의 서브 클래스이며, **항목 뷰(item view)** 라고 하는 자식 View 객체들의 리스트를 보여준다. 각 항목 View는 RecyclerView의 행으로 나타나며 데이터 저장소에서 가져온 하나의 객체를 나타낸다. 객체가 갖는 데이터 중에서 어떤 것들을 화면에 보여주는가에 따라 항목 View가 복잡해질 수도 있고 간단할 수도 있다.

> 자식 뷰를 갖는 RecyclerView

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/119100956-70a75b00-ba53-11eb-9c88-ef18ada6bb37.jpeg'>
</p>

한 행으로 보이는 RecyclerView의 각 항목은 자신의 뷰 계층 구조를 가질 수 있다. 여기서는 각 항목의 View 객체가 두 개의 TextView를 포함하는 LinearLayout이 된다.

> 리스트 항목 레이아웃 변경하기 (layout/list_item_crime.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="8dp">

    <TextView
        android:id="@+id/crime_title"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Crime Title"/>

    <TextView
        android:id="@+id/crime_date"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Crime Date"/>
</LinearLayout>
```

## ViewHolder 구현하기

RecyclerView는 항목 View가 ViewHolder 인스턴스에 포함되어 있다고 간주한다. ViewHolder는 항목 View의 참조를 갖는다.

RecyclerView.ViewHolder의 서브 클래스인 CrimeHolder를 CrimeListFragment의 내부 클래스로 정의하자.

> 초기 버전의 CrimeHolder (CrimeListFragment)

```kotlin
class CrimeListFragment: Fragment() {
    ...
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ...
    }
    
    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {
        
    }
    ...
}
```



> ViewHolder와 이것의 itemView 속성

<p align = 'center'>
<img width = '150' src = 'https://user-images.githubusercontent.com/39554623/119100959-713ff180-ba53-11eb-8aef-6381a025e70d.jpeg'>
</p>

CrimeHolder의 생성자는 항목 View를 인자로 받으며, CrimeHolder 인스턴스가 생성될 대 이 View는 슈퍼 클래스인 RecyclerView.ViewHolder의 생성자 인자로 전달된다. 그리고 RecyclerView.ViewHolder 슈퍼 클래스로부터 상속받은 itemView 속성이 생성자로 전달된 항목 View의 참조를 갖는다.

RecyclerView는 자체적으로 View를 생성하지 않으며, 항상 항목 View를 참조하는 ViewHolder를 생성한다.

> #### ViewHolder

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/119100937-6e450100-ba53-11eb-9320-522a7522d30d.jpeg'>
</p>

itemView가 참조하는 항목 View가 간단할 때는 ViewHolder가 할 일이 적다. 그러나 복잡해지면 ViewHolder가 항목 View의 서로 다른 부분(자식 View)을 Crime 객체에 더 쉽고 효율적으로 연결해준다.

ViewHolder의 인스턴스가 처음 생성될 때 항목 View에 포함된 TextView의 참조를 알아내어 속성에 저장하도록 CrimeHolder를 변경한다.

> 항목 View의 TextView 참조를 속성에 저장하기

```kotlin
private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {
    val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
    val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
}
```

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/119100951-700ec480-ba53-11eb-80e0-e6056c6293d2.jpeg'>
</p>


여기서 CrimeHolder의 기본 생성자에 정의된 인자 겸 속성인 `view`는 `itemView` 속성과 동일한 항목 View의 참조 값을 갖는다. 따라서 새로 추가한 코드에서 `itemView` 대신 `view`를 사용해도 된다.

이제는 변경된 CrimeHolder가 항목 View의 TextView들에 대한 참조를 보존할 수 있으므로 이 TextView들의 값을 쉽게 보여줄 수 있게 되었다.

그런데 CrimeHolder의 인스턴스는 누가 또는 어디서 생성하는 것일까?

## 어댑터를 구현해 RecyclerView에 데이터 채우기

[ViewHolder](####ViewHolder) 이미지에 간단하게 나타나 있지만, RecyclerView는 자신이 ViewHolder를 생성하지 않는다. 대신에 이 일을 **어댑터(adapter)** 에 요청한다. 어댑터는 컨트롤러 객체로, RecyclerView와 RecyclerView가 보여줄 데이터 사이에 위치한다.

**Adapter**는 다음의 일을 처리한다.
- 필요한 ViewHolder 인스턴스들을 생성한다.
- 모델 계층의 데이터를 ViewHolder들과 바인딩한다.

그리고 **RecyclerView**는 다음의 일을 처리한다.
- 새로운 ViewHolder 인스턴스의 생성을 어댑터에게 요청한다.
- 지정된 위치의 데이터 항목에 ViewHolder를 바인딩하도록 어댑터에게 요청한다.

이제는 어댑터를 생성할 때가 되었다. 아래의 코드와 같이 CrimeAdapter라는 이름의 내부 클래스를 CrimeListFragment에 추가하고 RecyclerView.Adapter의 서브 클래스로 지정한다. 그리고 Crime 객체가 저장된 List를 인자로 받아 crimes 속성에 저장하는 기본 생성자도 추가한다.

> CrimeAdapter 생성하기

```kotlin
class CrimeListFragment: Fragment() {
    ...

    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {
        ...
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        override fun getItemCount(): Int = crimes.size

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.apply { 
                titleTextView.text = crime.title
                dateTextView.text = crime.date.toString()
            }
        }
    }
    ...
}
```

`Adapter.onCreateViewHolder(...)`는 보여줄 뷰 <sub>(여기서는 list_item_view.xml)</sub>를 인플레이트한 후 이 뷰를 처리하는 ViewHolder <sub>(여기서는 CrimeHolder)</sub> 인스턴스를 생성하고 반환한다. `onCreateViewHolder(...)`의 매개변수는 **서로 다른 타입의 뷰를 보여줄 때 필요**한데, 아래 [RecyclerView의 ViewType](##RecyclerView의-ViewType)에서 볼 수 있다.

`Adapter.onBindViewHolder(holder: CrimeHolder, position: Int)`는 인자로 전달된 위치에 있는 Crime 객체의 범좌 제목과 발생일자를 CrimeHolder 인스턴스가 참조하는 TextView의 text 속성에 지정한다. 여기서는 Crime 객체가 List에 저장되어 있으므로 인자로 전달된 위치가 List 인덱스로 사용된다.

데이터가 저장된 데이터 셋에 몇 개의 데이터가 있는지 RecyclerView가 알아야 할 때 `Adapter.getItemCount()`를 호출해 어댑터에게 요청한다. 여기서는 `getItemCount()`가 List에 저장된 Crime 객체의 수를 반환한다.

화면에 보여줄 Crime 객체 또는 이 객체가 저장된 List를 RecyclerView는 모르며, CrimeAdapter가 안다.


> CrimeAdapter는 RecyclerView와 List&lt;Crime&gt; 사이에 위치

<p align = 'center'>
<img width = '800' src = 'https://user-images.githubusercontent.com/39554623/119100927-6b4a1080-ba53-11eb-8a4f-a3201333d84e.jpeg'>
</p>

RecyclerView는 화면에 보여줄 뷰 객체가 필요하면 자신과 연관된 Adapter에게 요청한다.

> RecyclerView와 CrimeAdapter 간의 소통 과정

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/119100930-6d13d400-ba53-11eb-9f47-f066719fea09.jpeg'>
</p>

RecyclerView는 CrimeAdapter의 `onCreateViewHolder(ViewGroup, Int)` 함수를 호출해 CrimeHolder 인스턴스를 생성한다. 이때 화면에 보여줄 항목 View를 생성자 인자로 전달한다. 그러나 CrimeAdapter가 생성해 RecyclerView에게 반환하는 CrimeHolder <sub>(그리고 이것의 itemView 속성)</sub>는 아직 데이터가 바인딩되지 않았다.

그런 다음에 RecyclerView는 CrimeAdapter의 `onBindViewHolder(ViewHolder, Int)` 함수를 호출한다. 이때 CrimeHolder의 데이터 셋 내부의 Crime 객체 위치를 인자로 전달한다. 그리고 CrimeAdapter는 이 함수에서 해당 위치의 모델 데이터인 Crime 객체를 찾아 이것을 CrimeHolder의 항목 View와 **바인딩(binding)** 즉, Crime 객체의 데이터를 항목 View에 채운다.

## RecyclerView의 어댑터 설정하기

이제는 어댑터가 준비되었으니 RecyclerView에 연결해야 한다. CrimeListFragment의 UI를 설정하는 `updateUI` 함수를 구현할 것인데, 지금은 이 함수에서 CrimeAdapter를 생성해 RecyclerView에 설정한다.

> 어댑터 설정하기

```kotlin
class CrimeListFragment: Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null
    ...
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()
        
        return view
    }
    
    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }
    ...
}
```

맨 끝에 추가한 `crimeRecyclerView.adapter`에서 adapter는 RecyclerView의 속성이다. 코틀린에서는 속성의 값을 보존하는 필드를 내부적으로 유지하는데, 이것을 후원 필드 <sup>backing field</sup>라고 한다. 또한, 필드의 값을 반환하는 게터와 값을 변경하는 세터가 자동 생성되고 자동 호출된다.(자바와는 다르다) 즉, 속성의 값을 참조할 때는 게터가 자동으로 호출되며, 이 코드처럼 속성의 값을 지정/변경할 때는 세터가 자동 호출된다. 그러므로 게터 <sub>`getAdapter()`</sub>나 세터 <sub>`setAdapter(RecyclerView.Adapter)`</sub>를 호출할 필요가 없다. `crimeRecyclerView.adapter = adapter` 대신 `crimeRecyclerView.setAdapter(adapter)`로 해도 결과는 같다. 두 가지 모두 adapter 속성의 세터를 호출하는 것이기 때문이다. 그러나 코틀린 속성의 특성을 잘 활용하는 코드는 `crimeRecyclerView.adapter = adapter`이다.

> 더미 데이터로 채워진 RecyclerView

`onBindViewHolder(...)` 함수가 최소한의 꼭 필요한 일만 수행하도록 작고 효율적으로 만들었기 때문에 상하 스크롤 시 매우 부드럽게 움직일 것이다.

<p align = 'center'>
<img width = '200' src = 'https://user-images.githubusercontent.com/39554623/119213129-f11f9780-baf7-11eb-8815-2f2875105d36.jpeg'>
</p>

## 뷰의 재활용: RecyclerView

위의 이미지에서는 한 화면에 14개의 View를 보여준다. 그리고 화면을 스크롤하면 100개의 View를 볼 수 있다. 그렇다면 메모리에 100개의 View를 모두 갖고 있는 것일까? 그렇지 않다. 이게 다 RecyclerView 덕분이다.

List에 저장된 모든 Crime 객체에 대해 하나씩 항목 View를 생성한다면 앱이 제대로 실행되지 않는다. List가 100개보다 훨씬 많은 데이터를 가질 수 있지만, 화면에서는 하나의 Crime이 하나의 항목 View만 필요하므로 한꺼번에 100개의 항목 View를 미리 생성하고 기다릴 필요가 없다. 그러므로 필요할 때만 항목 View 객체를 생성하는 것이 좋다.

RecyclerView가 바로 이런 일을 수행한다. 100개의 항목 View를 생성하는 대신에 한 화면을 채우는데 충분한 개수만 생성해, 화면이 스크롤되면서 항목 View가 화면을 벗어날 때 RecyclerView는 해당 항목 View를 버리지 않고 재활용한다. 이름 그대로 RecyclerView는 끊임없이 항목 View를 재활용한다.

이런 이유로 `onCreateViewHolder(ViewGroup, Int)` 함수는 `onBindViewHolder(ViewHolder, Int)`보다 덜 호출된다. 일단 충분한 수의 ViewHolder가 생성되면 RecyclerView는 `onCreateViewHolder(...)`의 호출을 중단하고 기존의 ViewHolder를 재활용해 `onBindViewHolder(ViewHolder, Int)`에 전달함으로써 시간과 메모리를 절약한다.

## 리스트 항목의 바인딩 개선하기

현재는 CrimeAdapter가 `Adapter.onBindViewHolder(...)` 함수에서 Crime 객체의 데이터를 CrimeHolder가 참조하는 TextView로 직접 바인딩한다. 이렇게 해도 앱은 잘 실행된다. 그렇지만 CrimeHolder와 CrimeAdapter 간의 기능 분담을 명쾌하게 하는 것이 좋다. CrimeAdapter는 CrimeHolder가 내부적으로 하는 일을 모르는 것이 좋기 때문이다.

데이터 바인딩 작업을 수행하는 모든 코드는 CrimeHolder 내부에 두는 것이 좋다. 이렇게 하려면 우선 바인딩되는 Crime 객체의 참조를 보존하는 속성을 추가하면 된다. 그리고 내친 김에 기존의 TextView 참조 속성들을 `private`으로 변경하고 `bind(Crime)` 함수를 CrimeHolder에 추가한다. 이 함수에서는 바인딩되는 Crime 객체의 참조를 새로 추가하는 속성에 보존하며, TextView의 text 속성값을 현재 참조되는 Crime 객체의 속성값으로 설정한다.

> `bind(Crime)` 함수 작성하기

```kotlin
    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var crime: Crime

        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }
    }
```

이제는 바인딩할 Crime 객체가 전달되면 CrimeHolder가 이 객체의 데이터를 반영해 제목 TextView와 발생일자 TextView의 text 속성값을 변경한다.

다음으로 RecyclerView가 요청할 때마다 `bind(Crime)` 함수를 호출하도록 변경해서 지정된 CrimeHolder를 특정 Crime 객체와 바인딩하자.

> `bind(Crime)` 함수 호출하기

```kotlin
private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
        return CrimeHolder(view)
    }
    override fun getItemCount(): Int = crimes.size
    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]

//        holder.apply {
//            titleTextView.text = crime.title
//            dateTextView.text = crime.date.toString()
//        }

        holder.bind(crime)
    }
}
```

## 리스트 항목 선택에 응답하기

사용자가 RecyclerView의 리스트 항목을 누르면 앱이 응답하도록 간단하게 Toast 메시지를 보여주자.

RecyclerView는 강력하고 기능도 좋지만 실질적인 책임은 거의 없다. 여기서도 그렇다. 터치 이벤트를 처리하는 것은 우리의 몫이다.

따라서 **OnClickListener**를 설정해 터치 이벤트를 처리하면 된다. 이때 각 항목 View는 자신과 연관된 CrimeHolder를 갖고 있으므로 CrimeHolder에서 항목 View의 **OnClickListener**를 구현하면 된다.

리스트 항목의 모든 View에 클릭 이벤트를 처리하도록 CrimeHolder를 변경한다.

> CrimeHolder에서 클릭 이벤트 처리하기

```kotlin
private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    private lateinit var crime: Crime
    val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
    val dateTextView: TextView = itemView.findViewById(R.id.crime_date)

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(crime: Crime) {
        this.crime = crime
        titleTextView.text = this.crime.title
        dateTextView.text = this.crime.date.toString()
    }
    override fun onClick(v: View) {
        Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
    }
}
```

CrimeHolder 자신이 **OnClickListener** 인터페이스를 구현하고 있다. 그리고 리스트 행의 항목 View <sub>(itemView 속성이 참조함)</sub>에 발생하는 클릭 이벤트를 CrimeHolder가 받도록 설정되어 있다.

## ListView와 GridView

안드로이드 운영체제에는 ListView, GridView, Adapter 클래스가 포함되어 있다. 안드로이드 5.0까지는 이 클래스들을 사용해서 리스트나 그리드 형태로 항목들을 생성했다.

이 컴포넌트들의 API는 RecyclerView의 것과 매우 유사하다. ListView나 GridView 클래스는 리스트의 항목들을 스크롤하지만 각 항목에 관해서는 잘 알지 못한다. 그리고 리스트의 각 항목 View를 생성하는 일은 Adapter가 수행하지만, ListView나 GridView에서는 ViewHolder 패턴을 사용하도록 강제하지 않는다 <sub>(하지만 사용하는 것이 좋다)</sub>. 

이런 기존 컴포넌트들은 RecyclerView로 대체되었다. ListView나 GridView의 작동 방식을 변경하려면 복잡하기 때문이다.

예를 들어, 수평 방향으로 스크롤 가능한 ListView를 생성하는 기능은 ListView API에 포함되어 있지 않아 많은 작업이 필요하다. 커스텀 레이아웃을 갖고 스크롤 가능한 RecyclerView를 생성하는 것도 여전히 많은 작업이 필요하다. 그러나 RecyclerView는 기능 확장이 되도록 설계되었으므로 나쁘지 않다.

RecyclerView의 또 다른 주요 기능은 리스트 항목의 애니메이션이다. ListView나 GridView의 경우 항목을 추가하거나 삭제할 때 생동감 있게 보이도록 하려면 구현이 복잡하고 에러가 생기기 쉽ㄴ다. 그러나 RecyclerView는 몇 가지 애니메이션 기능이 내장되어 있어서 훨씬 쉬우며, 이런 애니메이션 기능을 쉽게 커스터마이징 할 수 있다.

## RecyclerView의 ViewType

RecyclerView에 평범한 범죄를 보여주는 행과 심각한 범죄를 보여주는 행, 두 가지 타입 행을 생성한다.

<p align = 'center'>
<img width = '150' src = 'https://user-images.githubusercontent.com/39554623/119218988-027b9a80-bb1e-11eb-8e9e-042f8542e9eb.jpeg'>
</p>

```kotlin
data class Crime(
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false,
    var requirePolice: Int = VIEW_TYPE_NORMAL
) {
    companion object {
        const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_SERIOUS = 1
    }
}
```

```kotlin
class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var crime: Crime

        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class SeriousCrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var crime: Crime

        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            val crime = crimes[position]

            return when (crime.requirePolice) {
                0 -> Crime.VIEW_TYPE_NORMAL
                1 -> Crime.VIEW_TYPE_SERIOUS
                else -> throw RuntimeException("Unknown View Type Error")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view: View?

            return when(viewType) {
                Crime.VIEW_TYPE_NORMAL -> {
                    view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                    CrimeHolder(view)
                }
                Crime.VIEW_TYPE_SERIOUS -> {
                    view = layoutInflater.inflate(R.layout.list_item_serious_crime, parent, false)
                    SeriousCrimeHolder(view)
                }
                else -> throw RuntimeException("Unknown View Type Error")
            }
        }

        override fun getItemCount(): Int = crimes.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val crime = crimes[position]

            when (crime.requirePolice) {
                Crime.VIEW_TYPE_NORMAL -> {
                    (holder as CrimeHolder).bind(crime)
                }
                Crime.VIEW_TYPE_SERIOUS -> {
                    (holder as SeriousCrimeHolder).bind(crime)
                }
            }
        }
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}
```