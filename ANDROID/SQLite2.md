# 안드로이드 데이터베이스 프로그래밍

## 1. 안드로이드에서 SQLite 사용하기

- SQLite는 비교적 작은 규모의 안드로이드 앱에서 사용하기 적절한 데이터베이스
- SQLite에서 제공하는 몇 가지 API 함수를 호출하는 것만으로 데이터베이스 기능을 사용할 수 있는 특징이 있다.
- 관련 클래스 및 API함수는 `android.database.sqlite` 패키지에 들어 있으며, 그 중 가장 중요한 클래스는 `SQLiteDatabase` 클래스이다.

> SQLite 데이터베이스 사용 과정 요약

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/70229219-151eda00-1799-11ea-9d3c-937b73a12767.png'>
</p>
<br>

### 1.1 SQLiteDatabase 클래스

- SQLite 데이터베이스에 데이터를 추가하거나 삭제, 수정, 조회를 하기 위해서는 `SQLiteDatabase` 클래스를 사용해야 한다.
  - 즉, `SQLiteDatabase` 클래스는 하나의 SQLite 데이터베이스를 다루기 위한 핵심 역할을 수행하는 클래스이다.
  - 그러므로 SQLite 데이터베이스 작업을 수행하기 전 반드시 `SQLiteDatabase` 클래스 객체의 참조를 획득해야 한다.

<br>

### 1.2 SQLite 데이터베이스 열기 (SQLiteDatabase 객체 참조 획득)

- `SQLiteDatabase` 객체의 참조를 획득하는 것은 SQLite 데이터베이스 파일을 열거나, 새로운 파일을 생성함으로써 획득할 수 있다.
  - 이는 `SQLiteDatabase` 클래스에 정의된 몇 가지 static 함수를 통해 수행될 수 있다.

|리턴 타입|메소드 이름|
|:---:|:----|
|SQLiteDatabase|static fun openDatabase(path: String, factory: SQLiteDatabase.CursorFactory?, flags: Int): SQLiteDatabase!|
|SQLiteDatabase|static fun openDatabase(path: File, openParams: SQLiteDatabase.OpenParams): SQLiteDatabase!|
|SQLiteDatabase|static fun openDatabase(path: String, factory: SQLiteDatabase.CursorFactory?, flags: Int, errorHandler: DatabaseErrorHandler?): SQLiteDatabase!|
|SQLiteDatabase|static fun openOrCreateDatabase(file: File, factory: SQLiteDatabase.CursorFactory?): SQLiteDatabase!|
|SQLiteDatabase|static fun openOrCreateDatabase(path: String, factory: SQLiteDatabase.CursorFactory?): SQLiteDatabase!|
|SQLiteDatabase|static fun openOrCreateDatabase(path: String, factory: SQLiteDatabase.CursorFactory?, errorHandler: DatabaseErrorHandler?): SQLiteDatabase!|

- 표로 확인할 수 있듯, 데이터베이스를 여는 과정에서 겪을 수 있는 상황은 크게 두 가지로 나뉜다.
  - 이미 데이터베이스 파일이 존재하는 경우 <sup>openDatabase()</sup>
  - 데이터베이스 파일이 없을 수도 있는 경우 <sup>openOrCreateDatabase()</sup>

- 주로 `openOrCreateDatabase()` 함수를 사용하여, 데이터베이스 파일 열기를 시도한 다음 만약 파일이 존재하지 않는다면 새로운 데이터베이스 파일을 생성하는 방법을 사용

> `sample.db` 파일 열기 예제 (해당 파일 존재하지 않으면 새로 생성)

```kotlin
private var sqliteDB: SQLiteDatabase? = null

...
val dbFileName = "example.db"

    sqliteDB = try {
        val databaseFile: File = getDatabasePath(dbFileName)
        SQLiteDatabase.openOrCreateDatabase(databaseFile, null)
    } catch (e: Exception) {
        val databasePath = filesDir.path + "/" + dbFileName
        val databaseFile = File(databasePath)
        SQLiteDatabase.openOrCreateDatabase(databaseFile, null)
    }
```

- DB path를 지정할 때, 하드 코딩이 아닌 `getFilesDir()` <sup>java</sup> (`filesDir()` <sup>kotlin</sup>) 를 이용하는 것이 좋다.
  - https://stackoverflow.com/questions/7316191/sqlite-returned-an-error-code-of-14

<br>

### 1.3 SQLite 데이터베이스에 테이블 생성하기 (CREATE TABLE)

>  정수형(INTEGER) 데이터를 저장하기 위한 "NUM" 필드와 문자열(TEXT) 데이터를 저장하기 위한 "NAME" 필드를 가지는 "ORDER_T"라는 테이블을 생성하는 코드

```kotlin
val sqlCreateTable = "CREATE TABLE ORDER_T (NUM INTEGER, NAME TEXT)"

sqliteDB?.execSQL(sqlCreateTable)
```

- `CREATE TABLE` SQL문은 데이터베이스 파일이 생성되고 나서 최초에 한 번만 실행할 수 있다. 만약 생성하고자하는 테이블과 같은 이름의 테이블이 이미 존재하는 상황에서 `CREATE TABLE` 명령을 실행하면 예외가 발생한다.
  - 테이블 중복 생성으로 인한 예외를 막기 위해서는 테이블이 존재하지 않는 경우에만 테이블을 새로 만들도록 해야한다.
  - 이를 위해서는 `CREATE TABLE` 문에 `IF NOT EXISTS` 옵션을 추가하여 실행해야 한다.

```kotlin
val sqlCreateTable = "CREATE TABLE IF NOT EXISTS ORDER_T (NUM INTEGER, NAME TEXT)"

sqliteDB?.execSQL(sqlCreateTable)
```

<br>

### 1.4 테이블에 데이터 `추가`, `수정`, `삭제` 하기

#### 테이블에 데이터 추가 : `INSERT`

> 앞서 만든 `ORDER_T` 테이블의 `NUM`, `NAME` 필드에 각각 `1` 과 `GUNDAM` 값을 추가하는 코드

```kotlin
 val sqlInsert = "INSERT INTO ORDER_T (NUM, NAME) VALUES (1, 'GUNDAM')"

 sqliteDB?.execSQL(sqlInsert)
```

- `INSERT` 문을 실행하면 테이블에 새로운 데이터가 추가된다.
  - 테이블 내에 동일한 값을 가진 Row의 존재 여부와 관계 없이 새로운 Row로 추가된다.
- `INSERT` 명령을 실행할 때 조건에 맞는 데이터가 이미 존재한다면, 새로운 Row를 추가하지 않고도 기존의 Row값을 수정할 수 있다.
  - 이때 `INSERT OR REPLACE` 문을 사용한다.

#### 테이블 데이터 수정 : `UPDATE`

```kotlin
val sqlUpdate = "UPDATE ORDER_T SET NUM=2, NAME='POKEMON'"

sqliteDB?.execSQL(sqlUpdate)
```

- 위와 같이 실행될 데이터에 대한 조건이 지정되지 않은 `UPDATE`문을 실행하면 테이블 내의 모든 행의 값이 수정된다.
- 모든 Row가 아닌 특정 Row의 값만 수정하고 싶다면 아래와 같이 `UPDATE`문에 `WHERE`를 사용하여 조건을 추가한다.

```kotlin
val sqlUpdate = "UPDATE ORDER_T SET NUM=2, NAME='POKEMON' WHERE NUM = 1"

sqliteDB?.execSQL(sqlUpdate)
```

#### 테이블에 데이터 삭제 : `DELETE`

```kotlin
val sqlDelete = "DELETE FROM ORDER_T WHERE NUM = 2"

sqliteDB?.execSQL(sqlDelete)
```

- `WHERE`를 추가하지 않으면 테이블의 모든 데이터가 삭제된다.

<br>

### 1.5 테이블 데이터 조회하기

- 테이블에 저장된 데이터를 조회하려면 `SELECT`문을 사용한다.
  - 하지만 앞의 데이터 추가, 수정, 삭제를 위한 SQL 문장 실행과는 다르게 추가적으로 두 가지 요소를 알아두어야 한다.
  - 바로 **쿼리(Query)** 와 **커서(Cursor)** 다.

#### 1.5.1 쿼리(Query)

> 쿼리(Query)란, 원하는 데이터를 얻기 위해 데이터베이스에 정보를 요청(Request)하는 것을 말하며, SQLite 데이터베이스에서 그 요청(Request)은 "SELECT" 문을 사용하여 작성할 수 있다.

- 쿼리라는 단어는 우리 말로 표현하면 **`질의`** 라는 용어를 사용한다.
  - `질의`의 사전적 의미는 '의심나거나 모르는 점을 묻는 것'이다.
  - 즉, 자신이 모르는 정보를 알기 위해, 누군가에게 질문하여 정보를 요청하는 것이 바로 쿼리라는 단어의 의미이다.

- 데이터베이스적인 관점에서, 데이터베이스에 저장된 데이터는 그 정보를 획득하기 전까지는 사용자가 모르는(가지고 있지 않은) 정보이다.
  - 그래서 그 정보를 얻기 위해 데이터베이스 시스템에 정보를 요청한다면, 그것을 **"데이터베이스에 쿼리"** 한다고 말할 수 있다.

- 그런데 앞서 살펴본 데이터 추가(INSERT), 수정(UPDATE), 삭제(DELETE)는 데이터베이스로 전달되는 단-방향 명령인데 반해, 조회(SELECT)를 위한 쿼리(Query)는 데이터베이스로부터 결과 데이터 전달이 필요한 양-방향 명령이다.
  - 그러므로 아무런 값도 리턴하지 않는 `execSQL()` 함수 대신, `SELECT` 문의 조건에 따라 선택된 레코드 집합(RecordSet)을 리턴하는 `query()` 함수 또는 `rawQuery()` 함수를 사용해야 한다.
  - 많은 종류의 쿼리(Query) 함수가 제공되긴 하지만 많은 경우에 `SELECT` 문 전체를 한번에 전달할 수 있는 `rawQuery()` 함수가 주로 사용된다. 또한 모든 쿼리(Query) 함수가 리턴하는 레코드 집합(RecordSet)은 Cursor 인터페이스 타입으로 전달된다.

    ```
    레코드 집합(RecordSet) : 쿼리 결과에 포함된 레코드(Record)의 묶음(Set)을 뜻한다.

    보통 데이터를 쿼리한 결과는 하나 또는 하나 이상의 레코드를 포함하고 있는데, 이렇게 리턴된 결과 레코드들의 집합을 레코드 집합이라고 지칭한다.

    또한 "로우(Row) = 레코드(Record)"이므로 레코드 집합(RecordSet)을 다른 용어로 로우 집합(RowSet)이라고 부르기도 한다.
    ```

#### 1.5.2 커서(Cursor)

- 일반적인 컴퓨터 환경에서 커서란 화면에 표시된 내용에 사용자가 내용을 입력하거나 확인하기 위해, **'현재 주시하고 있는 위치에 대한 표시'** 를 말한다.
  - 키보드 커서, 마우스 커서 등이 대표적

- 데이터베이스의 쿼리 결과로 리턴된 데이터에서 현재 그 내용을 확인하고 있는 위치르 나타내는 정보를 **커서(Cursor)** 라는 용어를 사용하여 지칭한다.
  - 데이터베이스에 저장된 데이터를 쿼리하면 그 결과 데이터는, 한 개의 레코드만 가지거나, 또는 여러 개의 레코드가 포함된 레코드 집합(RecordSet)이다. 이 때 레코드 집합(RecordSet)에 들어 있는 개별 레코드에 접근하여 그 값을 확인할 수 있는 기능을 제공해주는 것이 바로 커서(Cursor)가 하는 역할이다. 그래서 SQLiteDatabase가 제공하는 모든 쿼리(Query) 관련 함수들은 Cursor 타입을 리턴하도록 되어 있다. 

#### 1.5.3 데이터베이스 데이터 조회하기 (SELECT)

> 쿼리 함수 및 커서를 사용하여 테이블에 저장된 모든 데이터를 조회

```kotlin
val sqlSelect = "SELECT * FROM ORDER_T"

val cursor: Cursor? = sqliteDB?.rawQuery(sqlSelect, null)

if (cursor != null) {
    while (cursor.moveToNext()) {
        val num: Int = cursor.getInt(0)
        val name: String = cursor.getString(1)
    }
}
```

> `WHERE`로 조건을 추가하여 특정 데이터만 조회

```kotlin
val sqlSelect = "SELECT * FROM ORDER_T WHERE NAME='GUNDAM'"

val cursor: Cursor? = sqliteDB?.rawQuery(sqlSelect, null)

if (cursor != null) {
    while (cursor.moveToNext()) {
        val num: Int = cursor.getInt(0)
        val name: String = cursor.getString(1)
    }
}
```

<br>


### 1.6 SQLite 데이터베이스 테이블 삭제하기

- 테이블을 삭제하기 위해서는 `DROP TABLE` 문을 사용한다.

```kotlin
val sqlDropTable = "DROP TABLE ORDER_T"

sqliteDB?.execSQL(sqlDropTable)
```

<br>

### 1.7 android 앱 내부의 SQLite 테이블 보는 방법

- 기기 내의 SQLite 파일을 찾아서 저장해야 한다. 이를 위해 `View - Tool Windows - Device File Explorer` 창을 연다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/70417390-7b12a680-1aa4-11ea-975c-b19e40a04707.png'>
</p>
<br>

- Device File Explorer 창을 열면 현재 연결되어 있는 디바이스와 에뮬레이터의 저장소를 볼 수 있다.
  - `/data/data/패키지 이름/databases/` 경로로 들어가면 `.db` 파일을 찾을 수 있다.
  - 원하는 데이터베이스 파일은 마우스 우클릭 옵션 중 `Save As...` 를 통해 원하는 경로에 저장할 수 있다.
  - 외부에 저장한 데이터베이스 파일은 [DB Browser](https://sqlitebrowser.org/) 와 같은 프로그램을 이용하여 테이블을 살펴볼 수 있다.

<br>
<p align = 'center'>
<img src = 'https://user-images.githubusercontent.com/39554623/70417475-aa291800-1aa4-11ea-959c-d3abb9272bdb.png'>
</p>
<br>