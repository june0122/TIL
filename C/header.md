# 헤더파일(Header File)

## 헤더파일의 정의

헤더 파일(header file) 또는 인클루드 파일(include file)은 컴파일러에 의해 다른 소스 파일에 자동으로 포함된 소스 코드의 파일이다. 일반적으로 헤더 파일들은 다른 소스 파일 속의 첫 부분에 포함된다.

비유하자면, 프로그램에서 어떤 함수들을 사용할 수 있는 지 한 눈에 파악할 수 있는 **메뉴판**이라고 볼 수 있다.

헤더파일에서는 사용하는 코드는 형을 알리는 형태가 가장 일반적이다. 프로세서가 실행 될 때 변수 공간을 잡는 형태는 중복에 의해 문제가 될 수도 있다. **([#ifndef를 이용한 헤더 중복 방지](https://dhhwang89.tistory.com/59)가 중요하다.)** 헤더파일의 사용은 여러 파일에서 중복으로 인클루드로 붙여서 사용하는 것이 일반적이기 때문이다.

형을 알리는 C/C++ 코드 :

- #define
- enum
- struct 헤더 부
- 클래스 선언 부
- 함수 형

등이 대표적이다. 따라서 이것들은 헤더 파일에 넣는 것이 일반적이다. 헤더 파일에 변수의 선언은 C/C++ 언어 입장에서는 문제가 없지만 프로그래밍 코드가 복잡해 지면 혼돈의 염려가 있으므로 습관된 헤더 파일 사용 방법 숙지가 필요하다.

<br>

## 헤더 파일과 라이브러리

### `main.c` 와 `add.c` 예시

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54333059-46340900-4664-11e9-9421-6bb4cc210b90.PNG'>
</p>

<br>

> main.c

```c
#include <stdio.h>

// 헤더 파일을 만들 때 주의할 점.
// => 중복 포함을 방지해야 한다.
#include "add.h"

// int add(int a, int b);
// 라이브러리의 설계자는 반드시 제공하는 모든 함수에 대한 선언을 제공해야한다.
// => 헤더파일

int main() {
	int n = add(10, 20);
	printf("%d\n", n);
}
```

> add.h

```c
// include guard
#ifndef _ADD_H_
#define _ADD_H_

// 선언
int add(int a, int b);

struct a {};

#endif
```

> add.c

```c
int add(int a, int b) {
	return a + b;
}
```

<br>

### `main.c` , `add.c` , `minus.c` 예시

<p align='center'>
<img src='https://user-images.githubusercontent.com/39554623/54333061-46cc9f80-4664-11e9-970e-07599ebab59c.PNG'>
</p>

> main.c

```c
#include <stdio.h>

#include "add.h"
#include "minus.h"

int main() {
	int n1 = add(10, 20);
    int n2 = minus(30, 10);
	printf("%d %d \n", n1, n2);
}
```

> add.h

```c
// include guard
#ifndef _ADD_H_
#define _ADD_H_

// 선언
int add(int a, int b);

struct a {};

#endif
```

> minus.h

```c
// include guard
#ifndef _MINUS_H_
#define _MINUS_H_

// 선언
int minus(int a, int b);

struct b {};

#endif
```

> add.c

```c
int add(int a, int b) {
	return a + b;
}
```

> minus.c

```c
int minus(int a, int b) {
	return a - b;
}
```