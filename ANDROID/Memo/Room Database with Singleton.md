# Android Room 라이브러리의 Database 객체 생성과 싱글턴

Android Room 라이브러리를 사용할 때, Room 데이터베이스 클래스는 다음 3가지 조건을 만족해야 한다.

1. `@Database` 어노테이션에서 데이터베이스와 관련된 모든 `Entity`를 나열한다.
2. `RoomDatabase`를 상속하는 추상 클래스여야 한다.
3. `DAO`를 반환하고 인수가 존재하지 않는 추상 함수가 있다.

```kotlin
@Database(entities = [User::class], version = 1) // 조건 1
abstract class UserRoomDatabase : RoomDatabase() { // 조건 2

    abstract fun userDao(): UserDao // 조건 3

    companion object {
        private const val databaseName = "user_database"
        private var appDatabase: UserRoomDatabase? = null

        fun getDatabase(context: Context): UserRoomDatabase? {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(
                    context,
                    UserRoomDatabase::class.java,
                    databaseName
                ).build()
            }

            return appDatabase
        }
    }
}
```

companion object를 사용하여 만약 appDatabase가 null이면 객체를 생성하고 null이 아니면 기존 객체를 반환하는 싱글턴 패턴 `getInstance()` 함수를 구현한다. 싱글턴 패턴을 사용하는 이유는 데이터베이스 객체 생성에 큰 비용이 들기 때문이다. 그래서 데이터베이스를 쓸 때마다 객체를 생성하기보다는 한 객체를 여러 클래스에서 글로벌하게 쓰는 싱글턴 패턴을 이용하는 것이 더 효율적이다.

## 2가지 싱글턴 패턴

### Eager initialization<small>(이른 초기화 방식)</small>

```kotlin

```

### Lazy initialization<small>(늦은 초기화 방식)</small>

```kotlin
@Database(entities = [User::class], version = 1) // 조건 1
abstract class UserRoomDatabase : RoomDatabase() { // 조건 2

    abstract fun userDao(): UserDao // 조건 3

    companion object {
        private const val databaseName = "user_database"
        private var appDatabase: UserRoomDatabase? = null

        fun getDatabase(context: Context): UserRoomDatabase? {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(
                    context,
                    UserRoomDatabase::class.java,
                    databaseName
                ).build()
            }

            return appDatabase
        }
    }

}
```

### Thread safe initalization<small>(스레드 안전한 늦은 초기화)</small>

```kotlin
@Database(entities = [User::class], version = 1)
abstract class UserRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): UserRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

## 참고

- java singleton pattern : https://blog.seotory.com/post/java-singleton-pattern
- Kotlin Singleton Pattern : https://bonoogi.postype.com/post/3591846
- JOYCE의 안드로이드 앱프로그래밍, 344p