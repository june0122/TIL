# 데이터베이스와 Romm 라이브러리

거의 모든 애플리케이션은 장시간 동안 데이터를 저장할 곳이 필요하다. 본문에서는 **Room 라이브러리**를 사용해서 앱의 데이터베이스를 쿼리할 수 있는 코드를 구현하고, 데이터베이스로부터 데이터를 가져와서 리스트에 보여주도록 한다.

[ViewModel과 SIS](https://june0122.github.io/2021/05/13/android-bnr-04/)에서 장치 회전 및 프로세스 종료 시에 ViewModel과 SIS <sup>Saved Instance Status</sup>를 사용해서 일시적인 UI 상태 데이터를 지속하는 방법을 기술했다. ViewModel과 SIS는 UI와 관련된 적은 수의 데이터에는 아주 좋지만 UI와 결부되지 않는 데이터의 저장에는 사용할 수 없다. 또한, 액티비티나 프래그먼트 인스턴스에 연관되지 않으면서 UI 상태와 무관하게 영구적으로 지속할 필요가 있는 데이터의 저장에도 사용할 수 없다.

따라서 이런 앱 데이터는 ViewModel이나 SIS 대신 로컬 파일 시스템이나 로컬 데이터베이스 또는 웹 서버에 저장해야 한다.

## Room 아키텍처 컴포넌트 라이브러리

Room은 Jetpack의 아키텍처 컴포넌트 라이브러리로, 데이터베이스 설정과 사용을 쉽게 해준다. Room을 사용하면 애노테이션이 지정된 코틀린 클래스로 데이터베이스 구조와 쿼리를 정의할 수 있다.

Room은 **API, 애노테이션 <sup>annotation</sup>, 컴파일러로 구성**되어 있다.

- 데이터베이스를 정의하고 인스턴스를 생성하기 위해 상속받는 클래스들이 **API**에 포함되어 있다.
- 데이터베이스에 저장할 필요가 있는 클래스, 데이터베이스를 나타내는 클래스, 데이터베이스 테이블을 사용하는 함수들을 정의하는 클래스 등을 나타내기 위해 **애노테이션**을 사용한다.
- **컴파일러**는 지정한 애노테이션을 컴파일해서 데이터베이스 구현체 <sub>(클래스나 인터페이스 등)</sub>를 생성한다.

> Room을 사용하는데 필요한 의존성 추가

```kotlin
plugins {
    id 'com.android.application'
    id 'kotlin-android'

    // Kotlin annotation processor tool 추가
    id 'kotlin-kapt'
}

...

dependencies {
    ...
    implementation 'androidx.core:core-ktx:1.3.2'

    // room-runtime과 room-compiler 추가
    implementation 'androidx.room:room-runtime:2.3.0'
    kapt 'androidx.room:room-compiler:2.3.0'
    
    ...
}
```

먼저 안드로이드 스튜디오의 **플러그인 <sup>plug-in</sup>** 으로 **kotlin-kapt**를 추가한다. 플러그인은 안드로이드 스튜디오 같은 IDE에 기능을 추가하는 방법이다.

kotlin-kapt는 코틀린 애노테이션 처리 도구 <sup>Kotlin annotation processor tool</sup>다. 라이브러리에서 생성한 클래스를 코드에서 직접 사용하고자 할 때가 있다. 그런데 기본적으로 라이브러리가 생성한 클래스들은 안드로이드 스튜디오에서 알 수 가 없어 이런 클래스들을 import해서 사용하려고 하면 에러가 발생한다. kotlin-kapt 플러그인을 추가하면 라이브러리가 생성한 클래스들을 안드로이드 스튜디오에서 알 수 있으므로 import해서 사용할 수 있다.

위에서 추가한 첫 번째 의존성인 *room-runtime*은 데이터베이스를 정의하는 데 필요한 모든 클래스와 애노테이션을 포함하는 Room API다.

두 번째 의존성인 *room-compiler*는 지정한 애노테이션을 컴파일해서 데이터베이스 구현채를 생성한다. room-compiler 의존성을 지정할 때는 `implementation` 대신 `kapt` 키워드를 사용해 Room 컴파일러가 생성한 클래스들을 안드로이드 스튜디오가 알 수 있게 한다.

## 데이터베이스 생성하기

Room으로 데이터베이스를 생성할 때는 다음 세 단계로 한다.

1. 모델 클래스에 애노테이션을 지정해 데이터베이스 **엔터티 <sup>entity</sup>** 로 만든다.
2. 데이터베이스 자체를 나타내는 클래스를 생성한다.
3. 데이터베이스가 모델 데이터를 처리할 수 있게 타입 변환기 <sup>type converter</sup>를 생성한다.

Room은 이 단계가 수월하게 이루어지도록 해준다.

### Entity 정의하기

Room은 우리가 정의한 **엔터티**를 기반으로 앱의 데이터베이스 테이블 구조를 만든다. 엔터티는 우리가 생성하는 모델 클래스로, `@Entity` 애노테이션으로 지정한다. 그러면 이 애노테이션으로 Room이 해당하는 클래스의 데이터베이스 테이블을 생성한다.

본문의 예시로는 Crime 객체를 데이터베이스에 저장할 것이므로 Crime 클래스를 엔터티로 변경한다.

> Crime을 엔터티로 만들기

```kotlin
@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false
)
```

`@Entity` 애노테이션은 클래스 수준에 적용된다. 이 엔터티 애노테이션은 Crime 클래스가 데이터베이스 테이블의 구조를 정의함을 나타낸다. 따라서 테이블의 **각 행 <sup>row</sup>이 하나의 Crime 객체를 나타내며, 클래스에 정의된 각 속성은 테이블의 열 <sup>column</sup>이므로 속성 이름은 열의 이름**이 된다. 따라서 Crime 객체 데이터를 저장하는 테이블은 id, title, date, isSolved 열을 갖는다.

id 속성에는 `@PrimaryKey` 애노테이션이 추가되었다. 이 애노테이션은 **기본 키 <sup>primary key</sup>** 열을 지정한다. 기본 키는 테이블의 모든 행에 고유한 데이터를 갖는 열이므로 각 행을 검색하는데 사용할 수 있다. 여기서는 id 속성값이 모든 Crime 객체에 고유하다. 따라서 id 속성에 `@PrimaryKey`를 지정하면 이 속성을 사용해 특정 Crime 객체 데이터를 쿼리할 수 있다.

### 데이터베이스 클래스 생성하기

엔터티 클래스는 데이터베이스 테이블의 구조를 정의한다. 앱에 여러 개의 데이터베이스가 있을 때는 특정 엔터티 클래스는 여러 데이터베이스에서 사용될 수 있다. 이런 경우가 흔하지는 않지만 가능하다. 이런 이유로 엔터티 클래스를 데이터베이스와 연관시켜 주어야 Room이 테이블을 생성하는데 사용할 수 있다.

우선 데이터베이스에 특정한 코드를 모아둘 패키지인 database를 생성하여 데이터베이스 클래스를 생성한다.

> 초기의 CrimeDatabase 클래스 (database/CrimeDatabase.kt)

```kotlin
@Database(entities = [Crime::class], version = 1)
abstract class CrimeDatabase : RoomDatabase() {
}
```

`@Database` 애노테이션은 이 클래스가 앱의 데이터베이스를 나태난다고 Room에게 알려준다. 이 애노테이션에는 두 개의 매개변수를 지정해야 한다. 

1. 첫 번째 매개변수에는 이 데이터베이스의 테이블들을 생성하고 관리하는 데 사용할 엔터티 클래스들을 지정한다
   - 여기서는 앱에서 유일한 엔터티 클래스인 Crime 클래스만 지정하였다.
2. 두 번째 매개변수에는 데이터베이스의 버전을 지정한다.
   - 데이터베이스를 처음 생성했을 때는 버전이 `1`이다.
   - 앱을 계속 개발하는 동안 새로운 엔터티를 추가하거나 기존 엔터티에 새로운 속성을 추가할 수 있다. 추가할 대는 `@Database` 애노테이션의 엔터티를을 변경하거나 데이터벵스 버전 번호를 증가시켜야 한다.

데이터베이스 클래스인 CrimeDatabase는 **RoomDatabase**의 서브 클래스다. 현재는 아무것도 갖고 있지 않으며, `abstract`로 지정되어서 이 클래스의 인스턴스를 생성할 수 없다. Room을 사용해서 데이터베이스 인스턴스를 얻는 방법은 이번 장 뒤에서 기술한다.

### 타입 변환기 생성하기

Room은 내부적으로 SQLite를 사용한다. SQLite는 MySQL이나 PostgreSQL 같은 오픈 소스 관계형 데이터베이스다 <sub>(SQL은 Structured Query Language의 약어이며 표준 데이터베이스 언어다)</sub>. 다른 데이터베이스와 달리 SQLite는 라이브러리를 사용해서 데이터를 읽거나 데이터를 쓸 수 있는 파일에 저장한다. 안드로이드는 추가 도우미 클래스들과 함께 SQLite 라이브러리를 표준 라이브러리에 포함하고 있다.

Room은 코틀린 객체와 데이터베이스 사이에서 객체-관계 매핑 <sup>ORM, Object-Relational Mapping</sup>계층의 역할을 하면서 SQLite 사용을 쉽게 해준다. Room을 사용하면 대부분의 경우에 SQLite를 자세히 알 필요 없다.

Room은 기본 데이터 타입을 SQLite 데이터베이스 테이블에 쉽게 저장할 수 있지만, 이외의 다른 타입은 문제가 생길 수 있다. Crime 클래스에는 Room이 저장 방법을 모르는 Date와 UUID 타임 속성이 있다. 따라서 이런 타입의 데이터를 데이터베이스 테이블에 저장하거나 가져오는 방법을 Room에게 알려줘야 한다.

데이터 타입을 변환하는 방법을 Room에게 알려주려면 **타입 변환기**를 지정하면 된다. 타입 변환기는 Room에게 특정 타입을 데이터베이스에 저장되는 타입으로 변환하는 방법을 알려준다. 이때 각 타입에 대해서 **`@TypeConverter` 애노테이션이 지정된 두 개의 함수가 필요**하다.

1. 데이터베이스에 데이터를 저장하기 위해 타입을 변환하는 함수
2. 데이터베이스로부터 읽은 데이터를 우리가 원하는 타입으로 변환하는 함수

> 타입 변환 클래스와 함수 추가하기 (database/CrimeTypeConverters.kt)

Date, UUID 타입에 대해 변환을 처리하는 두 개의 함수를 각각 추가한다.

```kotlin
class CrimeTypeConverters {
    
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
    
    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let { 
            Date(it)
        }
    }
    
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}
```

> 타입 변환기 활성화하기 (database/CrimeDatabase.kt)

변환기 클래스를 데이터베이스 클래스에 추가한다.

```kotlin
@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeConverters::class) // 변환기 클래스 추가
abstract class CrimeDatabase : RoomDatabase() {
}
```

이처럼 `@TypeConverters` 애노테이션을 추가하면서 CrimeTypeConverters 클래스를 전달하면, Room은 타입을 변환할 때 해당 클래스의 함수들을 사용한다.

이것으로 데이터베이스와 테이블 정의는 완료되었다.

## DAO 정의하기

데이터베이스 테이블의 데이터를 액세스하려면 DAO <sup>Data Access Object</sup>를 생성해야 한다. DAO는 데이터베이스 작업을 수행하는 함수들을 포함하는 인터페이스다.

본문에서의 예시의 DAO에는 두 개의 쿼리 함수가 필요하다. 바로 데이터베이스의 모든 범죄 데이터를 반환하는 함수와 지정된 UUID를 갖는 하나의 범죄 데이터를 반환하는 함수다.

> DAO 인터페이스 (database/CrimeDao.kt)

```kotlin
@Dao
interface CrimeDao {
}
```

이처럼 `@Dao` 애노테이션을 지정하면 CrimeDao가 DAO 중 하나임을 Room이 알게 된다. 그리고 데이터베이스 클래스에 CrimeDao를 등록하면 이 인터페이스에 추가된 함수들의 실행 코드를 Room이 생성한다.

> DAO에 두 개의 쿼리 함수 추가하기 (database/CrimeDao.kt)

```kotlin
@Dao
interface CrimeDao {
    
    @Query("SELECT * FROM crime")
    fun getCrimes(): List<Crime>
    
    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): Crime?
}
```

`@Query` 애노테이션은 `getCrimes()`와 `getCrime(UUID)` 함수가 데이터베이스의 데이터를 읽는다는 것을 나타낸다(추가, 변경, 삭제가 아님). 이 DAO에 정의된 각 쿼리 함수의 반환 타입은 쿼리가 반환하는 결과의 타입을 반영한다.

`@Query` 애노테이션은 문자열로 된 SQL 명령을 받는다. 대부분의 경우에는 간단한 SQL만 알면 되지만, SQL에 관한 더 자세한 내용은 https://www.sqlite.org 사이트의 [SQL Syntax](https://www.sqlite.org/lang.html)에서 참고할 수 있다.

`SELECT * FROM crime`은 crime 데이터베이스 테이블에 저장된 모든 행의 모든 열을 가져오며, `SELECT * FROM crime WHERE id=(:id)`는 id 값이 일치하는 행의 모든 열만 가져온다.

기존 데이터를 변경하는 함수를 인터페이스에 추가하는 것은 [링크 추가 예정]()에서, 새로운 데이터를 추가하는 함수의 추가는 [링크 추가 예정]()에서 다룬다.

다음으로 CrimeDao 인터페이스를 데이터베이스 클래스에 등록해야 한다. CrimeDao는 인터페이스이므로 이것을 구현하는 클래스를 Room이 생성한다. 하지만 이렇게 되려면 CrimeDao의 인스턴스를 생성하게 데이터베이스 클래스에 알려주어야 한다.

아래와 같이 CrimeDao를 반환 타입으로 갖는 추상 함수를 CrimeDatabase.kt에 추가한다.

> 데이터베이스에 CrimeDao 등록하기 (database/CrimeDatabase.kt)

```kotlin
@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {

    // CrimeDao 등록
    abstract fun crimeDao(): CrimeDao
}
```

이제는 데이터베이스가 생성되면 우리가 사용할 수 있는 CrimeDao 구현 클래스를 Room이 생성한다. 따라서 CrimeDao의 참조를 가지면 CrimeDao에 정의된 함수들을 호출해서 데이터베이스를 사용할 수 있다.

## 리포지터리 패턴으로 데이터베이스 액세스하기

> 리포지터리 패턴 다이어그램

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/119275355-7e293480-bc4f-11eb-9c51-a4b6cb604d21.png'>
</p>

데이터베이스 액세스를 위해 본문에서는 [구글의 앱 아키텍처 지침](https://developer.android.com/jetpack/guide)에서 권장하는 **리포지터리 패턴 <sup>repository pattern</sup>** 을 사용한다.

**리포지터리** 클래스는 리포지터리 <sup>데이터 저장소</sup>를 구현한다. 또한 단일 또는 여러 소스로부터 데이터를 액세스하는 로직을 캡슐화하고, 로컬 데이터베이스나 원격 서버로부터 특정 데이터 셋을 가져오거나 저장하는 방법을 결정한다. 그리고 UI 코드에서는 리포지터리에 모든 데이터를 요청한다. UI는 어떻게 데이터를 저장하거나 가져오는지에 관여하지 않으므로 이런 일은 리포지터리에서 구현한다.

> <b id = "f1">리포지터리 구현하기</b>  [ ↩](#a1) (CrimeRepository.kt)

```kotlin
class CrimeRepository private constructor(context: Context){
    companion object {
        private var INSTANCE: CrimeRepository? = null
        
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }
        
        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}
```

CrimeRepository는 **싱글톤 <sup>singleton</sup>** 이다. 즉, 앱이 실행되는 동안 하나의 인스턴스만 생성된다는 의미다.

싱글톤은 앱이 메모리에 있는 한 계속 존재하므로, 싱글톤이 갖는 속성은 액티비티나 프래그먼트의 생명주기 상태가 변경되어도 계속 유지될 수 있다. 그러나 안드로이드 운영체제가 메모리에서 앱을 제거하면 싱글톤도 같이 소멸한다. 따라서 CrimeRepository 싱글톤은 데이터를 장기간 저장하기 위한 해결책이 될 수 없지만, 대신에 CriminalIntent 앱에서 범죄 데이터를 갖고 있으면서 컨트롤러 클래스 간의 데이터를 쉽게 전달하는 방법을 제공한다.

CrimeRepository를 싱글톤으로 만들고자 여기서는 두 개의 함수를 동반 객체에 추가하였다. 하나는 CrimeRepository의 인스턴스를 생성하는 함수이고, 다른 하나는 기존에 생성된 CrimeRepository 인스턴스를 반환하는 함수다. 그리고 CrimeRepository의 생성자를 `private`로 지정해서 외부에서 `CrimeRepository.initialize(Context)`를 호출해야만 CrimeRepository 인스턴스를 생성할 수 있게 했다.

게터 함수인 `get()`에서는 CrimeRepository 인스턴스가 생성되지 않으면 IllegalStateException을 발생시킨다. 따라서 앱이 시작될 대 리포지터리인 CrimeRepository 인스턴스를 생성해야 한다.

이렇게 하려면 **Application**의 서브 클래스를 생성하면 된다.

> Application 서브 클래스 생성하기

```kotlin
class CriminalIntentApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}
```

`Activity.onCreate(...)`와 유사하게 `Application.onCreate(...)`는 앱이 최초로 메모리에 로드될 때 안드로이드 시스템이 자동 호출한다. 따라서 한번만 초기화되는 작업을 수행하는 데 적합하다.

액티비티나 프래그먼트와는 달리 Application 인스턴스 즉, CriminalIntentApplication 인스턴스는 CriminalIntent 앱이 최초 실행될 때 생성되거나 앱 프로세스 소멸로 인해 CriminalIntentApplication 인스턴스가 소멸된 후 CriminalIntent 앱이 다시 실행될 때 재생성된다.

CriminalIntentApplication 인스턴스를 안드로이드 시스템에서 사용하려면 매니페스트에 등록해야 한다. AndroidManifest.xml에 `android:name` 속성을 추가해서 앱을 설정하면 이후부터는 CriminalIntentApplication 인스턴스가 앱의 Context 객체로 사용된다.

> CriminalIntentApplication을 등록하기

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.june0122.criminalintent">

    <application
        android:name=".CriminalIntentApplication"
        android:allowBackup="true"
        ...>
        ...
    </application>

</manifest>
```

이제는 CriminalIntent 앱이 실행될 때 안드로이드 운영체제가 CriminalIntentApplication 인스턴스를 생성하고 이 인스턴스의 `onCreate()`를 호출한다. 따라서 CrimeRepository 인스턴스가 생성되어 사용할 수 있다.

다음으로 데이터베이스 객체와 DAO 객체의 참조를 저장하는 두 개의 속성을 CrimeRepository에 추가한다.

> 리포지터리 속성 설정하기

```kotlin
private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context){
    
    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()
    
    private val crimeDao = database.crimeDao()
    
    companion object {
        ...
    }
}
```

`Room.databaseBuilder()`는 세 개의 매개변수를 사용해서 CrimeDatabase의 실체 클래스를 생성한다.

1. 데이터베이스가 안드로이드 장치의 파일 시스템을 액세스하므로 첫 번째 매개변수는 데이터베이스의 컨텍스트로, 주로 앱의 Context <sub>(여기서는 CriminalIntentApplication)</sub> 객체를 전달한다.
2. 두 번째 매개변수는 Room으로 생성하고자 하는 데이터베이스 클래스 <sub>(여기서는 CrimeDatabase)</sub>
3. 세 번째 매개변수는 Room으로 생성하고자 하는 데이터베이스 파일 이름이다.

다른 컴포넌트에서는 CrimeDatabase를 액세스할 필요가 없으므로 여기서는 `private` 문자열로 지정하였다 <sub>(SQLite에서는 한 데이터베이스가 하나의 파일로 생성된다)</sub>.

다음으로 DAO의 데이터베이스 액세스 함수들을 CrimeRepository에서 사용하기 위한 함수를 추가한다.

> 리포지터리 함수 추가하기

```kotlin
class CrimeRepository private constructor(context: Context){
    ...

    private val crimeDao = database.crimeDao()

    fun getCrimes(): List<Crime> = crimeDao.getCrimes()
    
    fun getCrime(id: UUID): Crime? = crimeDao.getCrime(id)
    
    companion object {
        ...
    }
}
```

Room은 DAO에 쿼리를 구현하므로 리포지터리에 DAO의 함수를 호출하는 함수가 필요하다. 이렇게 하면 리포지터리에서 DAO <sub>(여기서는 CrimeDao)</sub>의 함수를 호출만 하면 되므로 코드가 간단해지고 이해하기 쉬워진다.

## 쿼리 테스트하기

리포지터리가 준비되었지만 쿼리 함수를 테스트하기 전에 할 일이 하나 더 있다. 현재 데이터베이스에는 범죄 데이터가 하나도 없다. 코드에서 모의 데이터를 생성해 데이터베이스에 추가할 수 있찌만, 데이터를 추가하는 DAO 함수를 아직 구현하지 않았기 때문에 [데이터베이스 파일](https://github.com/Jpub/AndroidBNR4/tree/main/Ch11/databases)을 이용한다. 안드로이드 스튜디오의 장치 파일 탐색기(Device File Explorer)를 사용해서 에뮬레이터에 파일을 업로드하면 된다.

참고로 에뮬레이터는 루트 권한으로 사용할 수 있어서 데이터베이스 파일의 업로드 및 앱에서의 사용이 가능하지만, 실제 장치에서는 데이터베이스 파일을 복사하지 못하거나 복사가 되더라도 권한이 없어서 앱에서 사용하지 못할 수 있기 때문에 데이터베이스 파일을 사용할 경우 에뮬레이터에서 테스트한다.

<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/119275895-a23a4500-bc52-11eb-91af-6688896e4fd8.png'>
</p>

리포지터리를 사용해서 퀴리할 수 있는 데이터가 준비되었으니 CrimeListViewModel에서 기존의 모의 데이터 코드를 삭제하고 CrimeRepository의 `getCrimes()` 함수를 호출하는 코드로 교체한다.

> CrimeListViewModel에서 리포지터리 액세스하기

```kotlin
class CrimeListViewModel: ViewModel() {

//    val crimes = mutableListOf<Crime>()
//
//    init {
//        for (i in 0 until 100) {
//            val crime = Crime()
//            crime.title = "Crime #$i"
//            crime.isSolved = i % 2 == 0
//            crimes += crime
//        }
//    }

    private val crimeRepository = CrimeRepository.get()
    val crimes = crimeRepository.getCrimes()
}

```

변경 후 앱을 실행하면 앱이 중단이 된다..!

LogCat의 에러 메시지를 보면 아래와 같다.

<p align = 'center'>
<img width = '800' src = 'https://user-images.githubusercontent.com/39554623/119277726-3fe64200-bc5c-11eb-81d6-055d961f8e93.png'>
</p>

이 에러는 Room 라이브러리에서 발생된 것으로, 긴 시간 동안 UI를 차단하는 데이터베이스 액세스를 main 스레드에서 하려고 했기 때문이다. 따라서 데이터베이스 액세스와 같이 시간이 걸리는 작업은 백그라운드에서 수행해야 한다.

## 애플리케이션의 스레드

데이터베이스로부터 데이터를 가져오는 것은 즉시 처리되지 않고 오래 걸릴 수 있다. Room은 메인 스레드에서의 데이터베이스 액세스를 허용하지 않으며, 액세스하려고 하면 IllegalStateException을 발생시킨다.

이것을 이해하려면 스레드가 무엇인지, 그리고 메인 스레드는 무엇이고 무슨 일을 하는지 알아야 한다.

스레드는 단일의 실행 시퀀스다. 스레드 내부의 코드는 한 단계씩 실행된다. 모든 안드로이드 앱은 **main 스레드**로 시작된다. 그러나 main 스레드는 미리 정해진 순서로 실행되지 않는다. 대신에 무한 루프에 머물면서 사용자나 시스템이 유발한 이벤트를 기다린다. 그리고 이벤트가 발생하면 응답하는 코드를 실행한다.

> 일반 스레드 vs main 스레드

<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/119277908-36110e80-bc5d-11eb-8a3e-d58264bd7fa5.jpeg'>
</p>

main 스레드는 UI를 변경하는 모든 코드들을 실행하며, 액티비티 시작, 버튼 클릭 등 서로 다른 UI 관련 이벤트들에 대한 응답으로 실행되는 코드들도 포함된다. 이벤트들은 어떤 형태로든 모두 UI와 관련이 있어서 main 스레드를 **UI 스레드**라고도 한다.

이벤트 루프에서는 UI 코드를 순서대로 유지한다. 따라서 시기 적절하게 코드가 실행되면서도 상호 충돌이 생기지 않는다. 지금까지 작성했던 모든 코드는 main 스레드에서 실행되었다.

### 백그라운드 스레드

데이터베이스 액세스는 다른 일보다 시간이 많이 걸린다. 따라서 이 시간 동안 UI는 완전히 응답 불가능이 될 것이고, 결국은 **애플리케이션이 응답하지 않는(ANR, Application Not Responding)** 결과를 초래하게 된다.

main 스레드가 중요한 이벤트에 대한 응답에 실패했다고 안드로이드의 와치독 <sup>watchdog</sup>이 판단하면 ANR이 발생하며, 애플리케이션이 중단된다 <sub>(10초 이내에 UI 응답이 없으면 사용자가 불편하지 않도록 안드로이드 시스템에서 ANR을 발생시킨다)</sub>.

이를 해결하기 위해 **백그라운드 스레드**를 생성해서 데이터베이스를 액세스해야 한다. 백그라운드 스레드를 앱에 추가할 때 중요한 규칙이 두 가지 있다.

1. **장시간 실행되는 모든 작업은 백그라운드 스레드로 실행되어야 한다.**
   - 이렇게 하면 main 스레드가 UI 관련 작업을 자유롭게 처리할 수 있어서 사용자를 위한 UI 응답을 지속적으로 처리할 수 있다.

2. **UI는 main 스레드에서만 변경할 수 있다.**
   - 백그라운드 스레드에서 UI를 변경하려고 하면 에러가 발생한다. 따라서 백그라운드 스레드에서 생성되는 모든 데이터는 main 스레드에 전달해서 UI를 변경하게 해야 한다.

안드로이드에서 백그라운드 스레드로 작업을 실행하는 방법에는 여러 가지가 있다. 비동기 네트워크를 요청하는 방법은 [링크 추가 예정]()에서, 핸들러 <sup>Handler</sup>를 사용해서 많은 소규모 백그라운드 작업을 수행하는 방법은 [링크 추가 예정]()에서, **WorkManager**를 사용해서 주기적인 백그라운드 작업을 수행하는 방법은 [링크 추가 예정]()에서 알아본다.

백그라운드에서 데이터베이스 작업을 실행하기 위해 두 가지 방법을 사용하는데, 본문에서는 쿼리 데이터를 포함하는 **LiveData**를 사용하고, 후에 **Executor**를 사용해서 데이터를 추가 및 변경한다.

## LiveData 사용하기

**LiveData**는 Jetpack의 *lifecycle-extensions* 라이브러리에 있는 데이터 홀더 클래스이며, Room에서 LiveData를 사용한다.

LiveData의 목적은 **앱의 서로 다른 부분 간에 데이터 전달을 쉽게 만드는 것**이다.
- 범죄 데이터를 보여줘야하는 프래그먼트로 CrimeRepository에서 데이터를 전달할 수 있으며
- 스레드 간에도 데이터를 전달할 수 있어서 백그라운드 스레드에서 main 스레드로 데이터를 전달할 수 있다.

Room DAO의 쿼리에서 LiveData를 반환하도록 구성하면, Room이 백그라운드 스레드에서 쿼리 작업을 자동 실행한 후 그 결과를 LiveData 객체로 반환한다. 따라서 액티비티나 프래그먼트에서는 LiveData 객체를 관찰하도록 설정만 하면 된다. 그리고 LiveData 객체가 준비되면 main 스레드의 액티비티나 프래그먼트에 통보되므로 이때 LiveData를 사용해서 데이터를 처리하면 된다.

> DAO에서 LiveData 반환하기

원래의 반환 타입을 포함하는 LiveData 객체를 반환하도록 쿼리 함수들의 반환 타입을 변경한다.

```kotlin
@Dao
interface CrimeDao {

    @Query("SELECT * FROM crime")
//    fun getCrimes(): List<Crime>
    fun getCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
//    fun getCrime(id: UUID): Crime?
    fun getCrime(id: UUID): LiveData<Crime?>
}
```

CrimeDao를 구현한 자동 생성된 클래스에서 LiveData의 인스턴스를 반환하므로 Room은 백그라운드 스레드에서 쿼리를 실행한다. 그리고 쿼리가 완료되면 범죄 데이터가 main 스레드로 전달되고 LiveData 객체를 관찰하는 옵저버 <sup>observer</sup>에 통보된다. (본문에서는 CrimeListFragment가 옵저버)

다음으로 CrimeRepository의 쿼리 함수들이 LiveData를 반환하도록 변경한다.

> 리포지터리에서 LiveData 반환하기

```kotlin
class CrimeRepository private constructor(context: Context){
    ...
    private val crimeDao = database.crimeDao()

//    fun getCrimes(): List<Crime> = crimeDao.getCrimes()
    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()

//    fun getCrime(id: UUID): Crime? = crimeDao.getCrime(id)
    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)
    ...
}
```

## LiveData 관찰하기

데이터베이스의 범죄 데이터를 범죄 리스트 화면에 보고자 `CrimeRepository.getCrimes()`로부터 반환되는 LiveData 객체를 관찰하도록 CrimeListFragment를 변경한다.

> 속성에서 무슨 데이터를 저장하는지 알기 쉽게 하기 위해 이름 변경

```kotlin
class CrimeListViewModel: ViewModel() {
    private val crimeRepository = CrimeRepository.get()
//    val crimes = crimeRepository.getCrimes()
    val crimeListLiveData = crimeRepository.getCrimes()
}
```

LivaData는 데이터베이스로부터 반환되는 범죄 데이터들을 포함한다. 그리고 CrimeListFragment는 데이터베이스로부터 결과가 반환되는 것을 기다렸다가 LiveData가 반환되면 RecyclerView를 범죄 데이터로 채울 수 있다. 따라서 RecyclerView 어댑터를 비어 있는 List로 초기화해야 한다. 그리고 새로운 범죄 데이터가 LiveData로 반환될 때 RecyclerView 어댑터가 새로운 범죄 List를 갖도록 설정한다.

> LiveData 옵저버와 RecyclerView 어댑터 설정하기

```kotlin
class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
//    private var adapter: CrimeAdapter? = null
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    ...

    override fun onCreateView(
        ...
    ): View? {
        ...
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            })
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }
```

`LiveData.observe(LifecycleOwner, Observer)` 함수는 LiveData 인스턴스에 옵저버를 등록하기 위해 사용된다.

`observe(...)` 함수의 첫 번째 인자로 지정된 viewLifeCycleOwner는 코틀린의 속성이며, *androidx.fragment.app.fragment*의 `getViewLifeCycleOwner()` 함수를 호출하는 것과 같다. viewLifeCycleOwner는 프래그먼트 뷰의 생명주기를 나타내는 LifecycleOwner 구현 객체를 반환한다. 따라서 여기서는 CrimeListFragment의 생명주기가 아닌 CrimeListFragment의 **뷰의 생명주기**에 맞춰 옵저버가 LiveData 인스턴스의 변경을 관찰하고 변경이 생기면 실행된다는 의미다.

`observe(...)` 함수의 두 번째 인자는 Observer 인터페이스를 구현하는 객체, 즉 옵저버이며 여기서는 람다식으로 구현되었다. 옵저버는 LiveData의 새로운 데이터를 처리하며, 여기서는 LiveData의 범죄 데이터 리스트가 변경될 때마다 실행된다. 즉, LiveData의 범죄 List를 받아서 로그 메시지를 출력하고 `updateUI()` 함수를 호출한다.

만일 LiveData의 변경이 생길 때마다 옵저버가 실행되면 프래그먼트의 뷰가 유효한 상태가 아닐 때(예로, 화면에 보이지 않을 때)도 `updateUI(crimes)` 함수에서 프래그먼트의 뷰를 변경하려 할 것이고 결국 앱이 중단될 것이다.

그러므로 `LiveData.observe(...)` 함수의 첫 번째 인자로 LifecycleOwner 객체를 지정하는 것이다. 이렇게 하면 옵저버의 생명주기는 지정한 LifecycleOwner 객체가 나타내는 안드로이드 컴포넌트의 생명주기에 한정된다.

따라서 프래그먼트의 뷰가 유효한 상태일 때만 LiveData 객체가 옵저버에게 변경 <sub>(새로운 데이터를 받았음)</sub>을 통보해 UI를 변경할 수 있다. 이처럼 LiveData는 프래그먼트 뷰의 생명주기에 따라 반응해서 이런 컴포넌트를 **생명주기-인식 컴포넌트 <sup>lifecycle-aware component</sup>** 라고 한다. 이 내용은 [링크 추가 예정]()에서 자세히 다룬다.

LifecycleOwner 인터페이스를 구현하는 객체는 Lifecycle 객체를 포함한다. Lifecycle은 안드로이드 생명주기의 현재 상태를 유지 관리하는 객체다 <sub>(액티비티, 프래그먼트, 뷰, 심지어 앱 자체도 자신의 생명주기를 갖는다)</sub>. '생성'이나 '실행 재개'와 같은 생명주기 상태는 `Lifecycle.State` 열거형 <sup>enum</sup>에 정의되어 있으며, `Lifecycle.getCurrentState()`를 사용하면 Lifecycle 객체의 상태를 알 수 있다.

AndroidX의 **Fragment** 클래스는 **LifecycleOwner** 인터페이스를 구현하며, 프래그먼트 인스턴스의 생명주기 상태를 나타내는 **Lifecycle** 객체를 갖고 있다.

프래그먼트 뷰의 생명주기는 **FragmentViewLifecycleOwner**가 별개로 유지 및 관리한다. 각 프래그먼트는 자신의 뷰의 생명주기를 유지 및 관리하는 **FragmentViewLifecycleOwner** 인스턴스를 갖는다.

위의 코드에서는 `observe(...)` 함수의 첫 번째 인자로 viewLifecycleOwner를 전달하므로 옵저버의 실행이 프래그먼트 자신이 아닌 프래그먼트 뷰의 생명주기와 연동된다. 프래그먼트 뷰의 생명주기는 프래그먼트 인스턴스의 생명주기와 별개지만 프래그먼트의 생명주기를 반영한다. 그런데 프래그먼트의 **유보 <sup>retaining</sup>** 로 이를 변경할 수 있다. 뷰의 생명주기와 프래그먼트 유보는 [링크 추가 예정]()에서 더 자세히 다룬다.

`Fragment.onViewCreated(...)`는 `Fragment.OnCreateView(...)`가 실행된 후 호출되므로 프래그먼트 뷰의 계층 구조에 맞춰 모든 뷰들이 형성된 후 실행된다. 따라서 `onCreated(...)`에서 LiveData 변경을 관찰하면 프래그먼트 뷰가 범죄 데이터를 보여줄 준비가 되었음을 확신할 수 있다. `observe()` 함수의 첫 번째 인자로 프래그먼트 자신이 아닌 viewLifecycleOwner를 전달하는 것도 이 때문이다. 여기서 원하는 것은 프래그먼트의 뷰가 좋은 상태일 때 LiveData로부터 범죄 리스트를 받는 것이다. 따라서 프래그먼트 뷰의 LifecycleOwner 객체를 사용하면 뷰가 화면에 보이지 않을 때는 LiveData 변경 데이터를 받지 않는다.

> 데이터베이스의 범죄 데이터 보여주기

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/119278756-4f688980-bc62-11eb-9810-ef978bfa885b.png'>
</p>

## 싱글톤

<a id = "a1">[CrimeRepository에서 사용](#f1)</a>된 것처럼 싱글톤 패턴은 안드로이드에서 매우 자주 사용된다. 그런데 싱글톤이 앱의 유지 보수를 어렵게 만들 수도 있다.

싱글톤은 프래그먼트나 액티비티보다 더 오래 존재한다. 또한, 싱글톤은 장치를 회전시키더라도 여전히 존재하며 앱의 액티비티와 프래그먼트를 오갈 때도 계속 남는다.

싱글톤을 사용하면 모델 객체를 소유하는 클래스를 편리하게 만들 수 있다. 범죄 데이터(Crime 객체)를 변경하는 많은 액티비티와 프래그먼트를 갖고 있는 더 복잡한 앱을 생각해보자. 한 컨트롤러가 범죄 데이터를 변경할 때 변경된 데이터를 어떻게 다른 컨트롤러에 전달할 수 있을까?

이때 만일 CrimeRepository가 범죄 데이터의 소유 클래스이면서 모든 변경 데이터가 CrimeRepository에 전달된다면 변경 데이터의 전달을 훨씬 쉽게 할 수 있다. 즉, 컨트롤러 사이를 이동하는 동안 특정 범죄의 식별자로 범죄 ID를 전달할 수 있으며, 각 컨트롤러는 이 ID를 사용해서 CrimeRepository로부터 해당 범죄 객체의 모든 데이터를 가져올 수 있다.

그러나 싱글톤은 몇 가지 단점을 가지고 있다.

1. 싱글톤은 컨트롤러보다 더 오랜 생애 동안 데이터를 저장하게 해주지만 **싱글톤 자신도 생애가 있다.** 즉, 앱이 종료된 후 어떤 시점에서 안드로이드 운영체제가 메모리를 회수하면 싱글톤은 자신의 인스턴스 변수와 함께 소멸된다. 따라서 싱글톤은 장기간에 걸쳐 데이터를 저장할 수 있는 방법은 아니며, 데이터를 디스크에 쓰거나 웹 서버로 전송하는 것만이 해결책이다.

2. **싱글톤은 코드의 단위 테스트를 어렵게 만들 수 있다.** 안드로이드 개발자들은 주로 **의존성 주입 <sup>dependency injection</sup>** 으로 이 문제를 해결한다. 이렇게 하면 객체들을 싱글톤을 공유할 수 있다.

3. **싱글톤은 잘못 사용될 수 있다.** 편리하다고 아무 생각 없이 모든 것에 싱글톤을 사용하려고 할 수 있다. 왜냐하면 싱글톤은 코드 어디서나 사용할 수 있으며, 나중에 사용할 데이터가 어떤 것이든 싱글톤에 저장할 수 있기 때문이다. 그러나 '이 데이터는 어디에 사용되는가? 이 기능이 중요한 곳은 어디인가?'라는 매우 중요한 질문을 스스로에게 해야 한다.

싱글톤은 잘 구성된 안드로이드 앱의 핵심 컴포넌트다. 단, 올바르게 사용될 때만 그렇다!