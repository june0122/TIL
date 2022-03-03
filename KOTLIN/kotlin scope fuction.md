# 코틀린의 Scope Function

코틀린 표준 라이브러리는 객체 컨텍스트 내에서 코드 블럭을 실행하는 것이 유일한 목적인 함수들을 포함하고 있다. 이 함수들을 람다식을 이용해서 호출하면 일시적인 범위<small>(scope)</small>가 생성되는데, 이 범위 내에서는 이름이 없어도 `this` 또는 `it` 키워드를 통해 객체에 접근할 수 있다. 이 함수들을 Scope Function<small>(범위 지정 함수)</small>이라 부르며, `let`, `run`, `with`, `apply`, `also` 5가지가 존재한다.

## Function selection

|함수|객체 참조|리턴 값|확장 함수 여부|
|:--|:--|:--|:--:|
|`let`|`it`|Lambda Result|O|
|`run`|`this`|Lambda Result|O|
|`run`|-|Lambda Result|X|
|`with`|`this`|Lambda Result|X|
|`apply`|`this`|Context Object|O|
|`also`|`it`|Context Object|O|

#### 각 함수가 의도하는 목적

- null이 아닌 객체에서 람다 실행 : `let`
- 가독성을 위해 코드 블럭 내에 지역 변수를 제공 : `let`
- 객체 구성 : `apply`
- 객체 구성 및 결과 계산 : `run`
- 표현식이 필요한 실행 문 : 확장 함수가 아닌 `run`
- 추가 효과 : `also`
- 객체에 함수 호출을 그룹화 : `with`

## 구분법

기본적으로 Scope Function들은 객체에서 코드 블럭을 실행한다는 점은 같지만 두 가지 주요 차이점이 있다.

1. 컨텍스트 객체<small>(Context Object)</small>를 블록 내에서 참조하는 방법 : `this` 또는 `it`
2. 반환 값<small>(Return Value)</small> : `Context Object` 또는 `Lambda Result`

### Context object: `this` 또는 `it`

Scope Function의 람다 내부에선 실제 객체의 이름 대신 `this` 또는 `it`을 사용해서 참조를 할 수 있다.
- `this` : lambda receiver
- `it` : lambda argument<small>(람다 인수)</small>

<i>※ 인자<small>(parameter)</small>는 함수 정의에 사용되는 변수, 인수<small>(argument)</small>는 함수 호출에 사용되는 변수</i>

```kotlin
fun main() {
    val str = "Hello"
    // this
    str.run {
        println("The string's length: $length") // this.length와 동일
    }

    // it
    str.let {
        println("The string's length is ${it.length}")
    }
}
```

#### `this`

- `run`, `with`, `apply`는 context object를 `this` 키워드를 통해 참조한다. 따라서 람다 내부에서는 객체를 일반 클래스 함수처럼 사용할 수 있다.
- `this`는 생략할 수 있지만 객체 내부 멤버와 외부의 객체 또는 함수와의 구분이 어려워질 수 있다. 따라서 `this`는 객체의 멤버에 대해 함수를 호출하거나 프로퍼티를 할당하는 람다식에서 사용하는 것이 권장된다.

```kotlin
val adam = Person("Adam").apply { 
    age = 20                       // this.age = 20 또는 adam.age = 20 과 동일
    city = "London"
}
```

#### `it`

- `let`과 `also`는 context object를 람다 인수로 가진다. 만약 인수의 이름이 정해지지 않으면 디폴트 이름인 `it`을 사용한다.
- `it`은 `this`보다 짧고 읽기 간단하다는 장점이 있지만, 객체의 함수와 프로퍼티를 호출할 땐 `this`처럼 생략할 수는 없다. 따라서 context object를 `it`으로 사용하는 것은 객체가 주로 함수 호출 시의 인수로 사용될 때까 더 좋다. 또한 코드 블럭 내에서 복수의 변수들을 사용할 때도 `it`이 더 좋다.

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/156332470-99c38029-5242-48ae-b9d1-427d40c48529.png'>
</p>

### Return Value

- `apply`, `also는` context object를 반환
- `let`, `run`, `with`는 lambda result<small>(람다식의 결과)</small>를 반환

#### Context object

`apply`와 `also`의 반환 값은 context object 그 자체이다. 따라서 체이닝 방식으로 계속해서 호출할 수 있다.

```kotlin
val numberList = mutableListOf<Double>()
numberList.also { println("Populating the list") }
    .apply {
        add(2.71)
        add(3.14)
        add(1.0)
    }
    .also { println("Sorting the list") }
    .sort()
```

또한 함수의 return문에 context object를 반환하는데도 사용될 수 있다.

```kotlin
fun getRandomInt(): Int {
    return Random.nextInt(100).also {
        writeToLog("getRandomInt() generated value $it")
    }
}

val i = getRandomInt()
```

#### Lambda result

`let`, `run`, `with`는 람다식 결과를 반환한다. 따라서 결과를 변수에 할당하거나, 결과에 대해 추가적인 작업을 수행할 때 사용할 수 있다.

```kotlin
val numbers = mutableListOf("one", "two", "three")
val countEndsWithE = numbers.run { 
    add("four")
    add("five")
    count { it.endsWith("e") }
}
println("There are $countEndsWithE elements that end with e.")
```

추가적으로, 반환 값을 무시하고 scope function을 사용하여 변수에 대한 일시적인 범위를 생성할 수도 있다.

```kotlin
val numbers = mutableListOf("one", "two", "three")
with(numbers) {
    val firstItem = first()
    val lastItem = last()        
    println("First item: $firstItem, last item: $lastItem")
}
```

## Functions

각자의 사례에 적절한 Scope Function을 선택할 수 있도록 일반적인 사용 스타일을 예시들을 통해 알아보자.

### `let`

- Context object: `it`
- Return value: `lambda result`

#### 1. null이 아닌 값들로만 코드 블럭을 실행시키고 싶을 때 자주 사용

```kotlin
val str: String? = "Hello"   
// processNonNullString(str)       // compilation error: str can be null
val length = str?.let { 
    println("let() called on $it")        
    processNonNullString(it)      // OK: 'it'은 '?.let { }' 안에서 null이 아니다.
    it.length
}
```

#### 2. 메서드 체이닝의 결과에 대해 하나 이상의 함수를 호출할 때 사용

```kotlin
/** let 미사용 */
val numbers = mutableListOf("one", "two", "three", "four", "five")
val resultList = numbers.map { it.length }.filter { it > 3 }
println(resultList)    
/** let 사용 */
val numbers = mutableListOf("one", "two", "three", "four", "five")
numbers.map { it.length }.filter { it > 3 }.let { 
    println(it)
    // 필요할 경우 추가적으로 함수 호출
} 
/** 만약 코드 블럭 안에 'it'을 인수로 사용하는 하나의 함수만 있다면 람다식 대신 메서드 참조(::)를 사용할 수 있다. */
val numbers = mutableListOf("one", "two", "three", "four", "five")
numbers.map { it.length }.filter { it > 3 }.let(::println)
```

#### 3. 가독성을 향상시키기 위해서 코드 블럭 내에 지역 변수를 제공

- Context object에 대한 새로운 변수를 정의하려면 람다의 인수에 이름을 지정하여 디폴트인 `it` 대신 사용할 수 있다.

```kotlin
val numbers = listOf("one", "two", "three", "four")
val modifiedFirstItem = numbers.first().let { firstItem ->
    println("The first item of the list is '$firstItem'")
    if (firstItem.length >= 5) firstItem else "!" + firstItem + "!"
}.uppercase()
println("First item after modifications: '$modifiedFirstItem'")
```

### `with`

- 비확장 함수 : `fun <T, R> with(receiver: T, block: T.() -> R): R`
- Context object: `this`
- Return value: `lambda result`

#### 1. lambda result를 반환하지 않고 Context object에서 함수를 호출할 때 사용

```kotlin
val numbers = mutableListOf("one", "two", "three")
with(numbers) {
    println("'with' is called with argument $this")
    println("It contains $size elements")
}
```

#### 2. 프로퍼티와 함수가 값을 계산하는데 사용되는 도우미 객체<small>(helper object)</small>를 제공할 때

 ```kotlin
 val numbers = mutableListOf("one", "two", "three")
 val firstAndLast = with(numbers) {
     "The first element is ${first()}," +
     " the last element is ${last()}"
 }
 println(firstAndLast)
 ```

### `run`

- Context object: `this`
- Return value: `lambda result`

#### 1. 확장 함수 `run`: 람다 내부에 객체 초기화와 반환 값 계산을 둘 다 포함하고 있는 경우

```kotlin
val service = MultiportService(url, 80)
val result = service.run {
    port = 8080
    query(prepareRequest() + " to port $port")
}
// let 함수를 이용한 동일한 코드
val letResult = service.let {
    it.port = 8080
    it.query(it.prepareRequest() + " to port ${it.port}")
}
```

#### 2. 비확장 함수 `run` : 여러 개의 문으로 이루어진 블럭을 실행하여 식으로 사용할 때

- 수신 객체<small>(receiver object)</small>에서 `run`을 호출하는 것이 아닌, 비확장 함수 `run`도 사용할 수 있다.

 ```kotlin
 // 여러 개의 지역 변수의 범위를 제한하는 의도로도 사용
 val hexNumberRegex = run {
     val digits = "0-9"
     val hexDigits = "A-Fa-f"
     val sign = "+-"
     Regex("[$sign]?[$digits$hexDigits]+")
 }

 for (match in hexNumberRegex.findAll("+123 -FFFF !%*& 88 XYZ")) {
     println(match.value)
 }
 ```

### `apply`

- Context object: `this`
- Return value: `context object`

#### 1. 객체 구성<small>(configuration)</small>

- 객체 초기화
- 코드 블럭이 값을 반환하지 않고 주로 receiver object의 멤버들로 동작하는 경우에 사용

```kotlin
val adam = Person("Adam").apply {
    age = 32
    city = "London"        
}
println(adam)
```

### `also`

- Context object: `it`
- Return value: `context object`

#### 1. 

- context object를 인수로 사용하는 일부 작업을 수행하는데 적합
- 객체의 프로퍼티나 함수에 대한 참조가 아니라 객체 자체에 대한 참조가 필요한 경우에 유용
- 외부 범위로부터 `this` 참조를 가리지<small>(shadow)</small> 않길 원할 때

```kotlin
val numbers = mutableListOf("one", "two", "three") 

// 아래의 'also'는 "객체를 가지고 다음과 같은 작업을 수행하라"고 해석할 수 있다.
numbers
    .also { println("The list elements before adding new one: $it") }
    .add("four")
```

#### 2. swap 함수

```kotlin
var a = 1
var b = 2

a = b.also { b = a }

println(a) // print 2
println(b) // print 1
```

## `takeIf` 와 `takeUnless`

Scope function들 외에도 표준 라이브러리는 `takeIf` 와 `takeUnless` 함수를 포함하고 있다. 이 함수들은 연쇄 호출<small>(call chains)</small>에 객체의 상태를 확인하는 작업을 추가하는데, 즉 단일 객체에 대한 필터링 함수이다.

- `takeIf`는 객체가 술부<small>(predicate)</small>와 일치하는 경우 이 객체를 반환하고, 그렇지 않으면 `null`을 반환한다.
- `takeUnless`는 `takeIf`와 반대로 일치하면 `null`, 일치하지 않으면 해당 객체를 반환한다.
- 객체는 람다 인수 `it`으로 사용할 수 있다.

```kotlin
val number = Random.nextInt(100)

val evenOrNull = number.takeIf { it % 2 == 0 }
val oddOrNull = number.takeUnless { it % 2 == 0 }
println("even: $evenOrNull, odd: $oddOrNull")
```

두 함수는 `null`을 반환할 수 있기 때문에 뒤에 다른 함수를 연결할 때는 null check를 하거나 safe call<small>(`?.`)</small>을 사용해야 한다.

```kotlin
val str = "Hello"
val caps = str.takeIf { it.isNotEmpty() }?.uppercase()
//val caps = str.takeIf { it.isNotEmpty() }.uppercase() //compilation error
println(caps)
```

두 함수는 scope function과 함께 사용할 때 유용하다. 좋은 경우는 주어진 술부와 일치하는 객체에서 코드 블럭을 실행하기 위해 `let`을 사용하여 연결하는 것이다. 이렇게 하려면 객체에 대해 `takeIf`를 호출한 다음 safe call<small>(`?.`)</small>로 `let`을 호출한다. 술부와 일치하지 않는 객체의 경우 `takeIf`는 `null`을 반환하고 `let`은 호출되지 않는다.

```kotlin
/** 표준 라이브러리 함수 미사용 */
fun displaySubstringPosition(input: String, sub: String) {
    val index = input.indexOf(sub)
    if (index >= 0) {
        println("The substring $sub is found in $input.")
        println("Its start position is $index.")
    }
}

/** 표준 라이브러리 함수 사용 */
fun displaySubstringPosition(input: String, sub: String) {
    input.indexOf(sub).takeIf { it >= 0 }?.let {
        println("The substring $sub is found in $input.")
        println("Its start position is $it.")
    }
}

displaySubstringPosition("010000011", "11")
displaySubstringPosition("010000011", "12")
```

## References

- Kotlin Docs: https://kotlinlang.org/docs/scope-functions.html