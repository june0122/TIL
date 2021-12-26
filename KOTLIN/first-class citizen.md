# 1급 시민(first-class citizen)이란?

프로그래밍에서 1급 시민(first-class citizen)이란 다음의 조건을 충족하는 것을 말한다.

1. 변수에 담을 수 있다.
2. 함수의 인자로 전달할 수 있다.
3. 함수의 반환값으로 전달할 수 있다.

## 1급 객체와 1급 함수?

1급 시민, 1급 객체, 1급 함수와 같이 비슷한 용어들이 존재하는데 그 미묘한 차이를 정리해보자.

`1급 객체`는 말 그대로 1급 시민의 조건을 충족하는 객체를 이야기한다. 코틀린의 함수는 객체로 사용할 수 있기 때문에 1급 객체에 속한다.

`1급 함수`는 1급 객체이면서 아래의 조건들은 추가로 만족하는 함수를 말한다.

- 런타임에 생성이 가능하다.
- 익명으로 생성이 가능하다.

## 코틀린과 1급 함수

코틀린의 함수는 1급<small>(first-class)</small>이다. 자바의 경우 함수는 클래스의 멤버의 역할만을 수행했지만, 코틀린에서는 함수 자체가 하나의 변수가 될 수 있다. 즉, 변수에 할당할 수 있으며 다른 고차 함수의 인자로 전달되고 반환될 수 있다. 

```kotlin
// 1. 변수나 데이터 구조에 할당
val function = { println("first-class test") }

// 2. 함수의 인자로 전달
fun function2(f: () -> Unit) {
    f.invoke()
}

// 3. 함수의 반환값으로 전달
fun function3() : () -> Unit {
    return function
}

fun main() {
    val a = function
    a.invoke()

    function2(a)

    function3().invoke()
}
```

1급 함수는 변수 뿐만 아니라 데이터 구조에도 할당이 가능

```kotlin
fun add(a: Int, b: Int) = a + b
fun subtract(a: Int, b: Int) = a - b
val functions = mutableListOf(::add, ::subtract)

fun main() {
    println("add : ${functions[0](10, 2)}, sub : ${functions[1](15, 5)}")
    // add : 12, sub : 10
}
```

## References

- 위키피디아 : https://en.wikipedia.org/wiki/First-class_citizen
- https://seungwoohong.tistory.com/15