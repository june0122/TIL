# [[안드로이드 컨퍼런스] 코로나시대의 안드로이드 개발자들](https://event-us.kr/ted/event/43225)

## 1. 코드 품질 올리기 by pluu

### 처음 코드에 대해 설명할 때

> 코드, structure, DI...

- clean architecture
  - 클린 아키텍쳐의 핵심이란? : 소프트웨어를 계층으로 분리하고 종속성 규칙을 준수..
- MVC, MVP, MVVM, MVI(compose)
- 아키텍처를 도입하는데 있어 자신만의 기준이 필요
  - 더 많은 기능, 안전, 유명, 해보고 싶어서? 비즈니스 업무보다 우선시하는 이유는?


### List transform API

- DSL: buildList

## Code Sample: 랜덤 생성기

### STEP 1. Always run task

Build시 추가적으로 execute되는 task 수 확인

### STEP 2. 필요없는 gradle 옵션

### STEP 5

ViewModel의 Unit test를 작성해보시오라는 질문

ViewModel에 Context를 주입할 경우 테스트가 어려워짐. 레파지토리에 인터페이스를 만들어서 사용하는 방식?

### STEP 8

이벤트 리스너를 data class의 멤버로 정의해서 사용하는 방식 알아보기

### STEP 11. inline class

```kotlin
@JvmInline
value class ColorValue(@ColorInt val value: Int) {
    ...
}
```

### STEP 12. 

시간 복잡도를 고려하여 List가 아닌 TreeSet을 사용할 수도 있음

### STEP 13. 코루틴 Flow

### FINAL CHECK

- exclude git history 
- 메서드 길이 및 파라미터의 개수

## 2. UI State Model 어떤게 좋을까?

> ViewModel에서 UI State를 어떻게 처리하고 있나요?

### UI State Modeling을 처리하는 4가지 방법들

### 1. UI State 여러 개로 처리하기

- 장점
  - 각 상태들이 다른 상태들에 영향을 주지 않는다.
  - 코드 작성 난이도가 다소 쉽다

- 단점
  - 변경을 누라해서 개발자 실수로 이어지기 쉽다
  - 특정 이벤트가 어떤 상태 변화를 만드는지 파악이 어렵다.
  - 코드 가독성이 안좋다.

### 2. data class 하나로 만들기

동반 객체에 미리 Uninitialized 등의 상태를 snapshot처럼 정의

- 장점
  - **data class 특성을 이용할 수 있다**
  - 부분적인 업데이트가 편리하다
  - 상태에 필요한 로직을 클래스에 위임할 수 있다.

- 단점
  - 하나의 상태만 변경해도 모든 곳에 전파된다 (데이터 바인딩에 대한 이해와 숙련도 요구)
  - 클래스로 분리하기 위한 설계 능력 요구

### 3. sealed class로 상태 분리하기

> MVI에서 UI State를 sealed class로 표현하는 경우가 많이 있음

- 장점
  - seald class 자체를 사용하는 것
  - 표현하고자 하는 상태가 클래스와 일대일 대응된다
  - 가독성이 좋다

- 단점
  - 상태의 복잡도와 클래스 개수가 비례
  - 화면 전체를 만드는 경우보다 아이템 상태를 만드는데 유리
  - xml로 할 경우 커스텀 뷰를 만들거나 바인딩 어탭터 필요

### 4. UiState 일반화 하기

```kotlin
data class UiState<out T> {
    data class Sucess<T>(val value: T): UiState<T>()

    ...

}
```

- 장점
  - 시스템을 만들기 좋다 (팀 단위 작업에서의 장점)

- 단점
  - 구현 난이도가 높다.
  - 시스템과 약간만 달라져도 대응하기가 어려워진다.

## 3. Kotlin KSP 활용하기 by taehwan

## KSP<small>(Kotlin Symbol Processing API)</small>

- 기존 java 기반의 kapt 대응
- kapt 대비 2배 빠른 속도
  
## KSP 어디서 활용할까?

> Room, Moshi, Dagger...

## 사용 예시

Composable에서 파라미터로 전달하던 viewModel을 위치와 관계없이 viewModel() 사용 가능

```kotlin
fun View() {
    val data = vuewModel().data…
}
```

## 직접 만들어보기

1. KSP 모듈 추가
2. 초기화 코드 작성
3. Target할 Annotation 정의
4. 사용할 모듈에 build.gradle 적용하고, Annotation 정의
5. Annotation을 찾고
6. 생성할 코드 정의
7. 예외 처리

### Square의 KotlinPoet 라이브러리

> KSP Generate를 쉽게 도와주는 라이브러리

### (중요) KSP ProcessorProvider 지정

## 4. 불변 객체는 어떻게 활용할까? by 김태현

> 불변 객체란 무엇일까?

변하지 않는 객체 -> 변경할 수 없는 객체

### 변경할 수 없는 객체가 아님

```kotlin
val mutableList: MutableList<Int> = mutableListOf(1, 2, 3)
```

### Side effect 제거하기

```kotlin
private val mutableNumbers: MutableList<Int> = mutableListOf(1, 2, 3)
val numbers: List<Int>
    get() = mutableNumbers
```

- 내부에서 변경한 변경점이 외부로 전달됨
  - 내부 -> 와부
- 외부에ㅓ MutableList로 캐스팅해서 값을 변경하면 내부로 전달됨
  - 내부 <- 외부

```kotlin
private val mutableNumbers: MutableList<Int> = mutableListOf(1, 2, 3)
val numbers: List<Int>
    get() = mutableNumbers.toList() // 방어적 복사
```

### 방어적 복사

- 기존 객체와 똑같은 객체를 만든다.
- 같은 객체끼리의 벼경이 공유되는 것을 막기 위해서 사용
- 불변 객체를 만드는 방법이 아니다.

```kotlin
val list1 = mutableListOf(1, 2, 3)
val list2 = list1.toList() // 1, 2, 3

list1.add(4)
println(list1) // 1, 2, 3, 4
println(list2) // 1, 2, 3
```

#### 가변

```kotlin
data class User(var name: String, var email: String)
```

#### 불변

```kotlin
data class User(val name: String, val email: String)

fun main() {
    val user = User(name = "임준섭", email = "saber0122@gmail.com")
    val newUser = user.copy(email = "june0122@naver.com")
}
```

### 유효성 검사

```kotlin
data class User(val name: String, val email: String) {
    init { // 생성 시점에 유효성 체크
        require(name.isNotBlank())
        require(email.foo())
    }
}
```

```kotlin
class SomethingRepository {
    fun doSometing(user: User) {
        TODO()
    }
}
```

### 불변 객체를 통해서 가독성 올리기

```kotlin
data class Book(
    val name: String,
    val publishedAt: LocalDateTime,
    val type: BookType,
)

enum class BookType {
    Cartoon, Novel, FairyTale
}

class BooksViewModel: ViewModel() {
    private val _books = MutableLiveData<Books>()
    val books: LiveData<Books>
        get() = _books


    val cartoonBooks: LiveData<List<Book>> ...

}
```

```kotlin
// 순수 함수로 만듦으로써 단위 테스트를 돌리기 더 쉬워짐
// ViewModel 내부에 있다면 테스트를 위해서 ViewModel 인스턴스를 만드는 등의 작업이 필요
fun List<Book>.filterCartoon(): List<Book> {
    return filter { it.type == BookType.Cartoon }
}

class BooksViewModel: ViewModel() {
    private val _books = MutableLiveData<Books>()
    val books: LiveData<Books>
        get() = _books

    val cartoonBooks: LiveData<List<Book>> = books.map(List<Book>::filterCartoon)

}
```

```kotlin
data class Books(private val values: List<Book>) { // class로 만들었을 때의 장점?
    fun filterCartoon(): Books {
        return Books(values.filter { it.type == BookType.Cartoon})
    }
}

class BooksViewModel: ViewModel() {
    private val _books = MutableLiveData<Books>()
    val books: LiveData<Books>
        get() = _books

    val cartoonBooks: LiveData<List<Book>> = books.map(List<Book>::filterCartoon)

}
```

```kotlin
data class Books(private val values: List<Book>) { // class로 만들었을 때의 장점?
    fun filterCartoon(): CartoonBooks { TODO() }

    fun filterLatest(): LatestBooks { TODO() }
}

data class CartoonBooks(private val values: List<Book>) {
    init {
        TODO("모두 Cartoon 타입인지 체크")
    }
}

data class LatestBooks(private val values: List<Book>) {
    init {
        TODO("최근 1년 사이에 출간되었는지 체크")
    }
}

// add, remove를 사용하기 보다는 plus, minus를 활용하면 operator overriding도 활용할 수 있고
// 새로운 리스트를 생성하는 형태이다.
```

https://stackoverflow.com/a/57771637/12364882

plus creates a new List out of an existing list and a given item or another list and returns the result (the new created List), while the input List is never changed. The item is not added to the existing List.

add is only defined on modifiable Lists (while the default in Kotlin is an ImmutableList) and added the item to the existing List and returns true to indicate that the item was added successfully.

### 완벽하게 불변으로 만들기

- `value class` 활용하기

## 5. 헤이딜러 안드로이드팀은 어떻게 일하나요?(3) — 프로젝트 구조 

## 6. Becoming an Android Librarian

## Q&A

과제 코드 검토 시 중요하게 보는 것
- 가독성
- 코드 분리
- 코드를 얼마나 고민하고 짰는가
- 모르고 사용하는 것을 주의
- ReadMe를 통해 자기 생각을 알려주는 것
- 과제에 명시되지 않는 디테일들 (클릭 효과, 예외처리?, 요구 사항에 없는 것들)


