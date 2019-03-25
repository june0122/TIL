# 챕터 04. 조건문과 반복문

## 조건문

### switch문

```java
public class SwitchExample {
    public static void main(String[] args) {
        int num = (int) (Math.random() * 6) + 1;
        // Math.random()
        // 0.0 이상에서 1.0 미만의 double형 실수 값을 반환

        switch (num) {
            case 1:
                System.out.println(+num + "번이 나왔습니다.");
                break;
            case 2:
                System.out.println(+num + "번이 나왔습니다.");
                break;
            case 3:
                System.out.println(+num + "번이 나왔습니다.");
                break;
            case 4:
                System.out.println(+num + "번이 나왔습니다.");
                break;
            case 5:
                System.out.println(+num + "번이 나왔습니다.");
                break;
            default:
                System.out.println(+num + "번이 나왔습니다.");
                break;
        }
    }
}
```

- 자바 6까지는 switch문의 괄호에는 정수 타입(byte, char, short, int, long) 변수나 정수값을 산출하는 연산식만 올 수 있었다. 자바 7부터는 String 타입의 변수도 올 수 있다.

> 직급별 월급 switch문 출력

```java
public class SwitchStringExample {
    public static void main(String[] args) {
        String position = "과장";

        switch (position) {
            case "부장":
                System.out.println("700만원");
                break;
            case "과장":
                System.out.println("500만원");
                break;
            default:
                System.out.println("300만원");
        }
    }
}
```

<br>

## 반복문(for문, while문, do-while문)

### while문

> 키보드로 while문을 제어

```java
public class WhileKeyControlExample {
    public static void main(String[] args) throws Exception {
        boolean run = true;
        int speed = 0;
        int keyCode = 0;

        while (run) {
            if (keyCode != 13 && keyCode != 10) {
                System.out.println("----------------------------------");
                System.out.println("1.증속 | 2.감속 | 3.중지");
                System.out.println("----------------------------------");
                System.out.println("선택: ");
            }

            keyCode = System.in.read();  // 키보드의 키코드를 읽음

            if (keyCode == 49) {  // 1을 읽었을 경우
                speed++;
                System.out.println("현재 속도=" + speed);
            } else if (keyCode == 50) {  // 2를 읽었을 경우
                speed--;
                System.out.println("현재 속도=" + speed);
            } else if (keyCode == 51) {  // 3을 읽었을 경우
                run = false;
            }
        }

        System.out.println("프로그램 종료");
    }
}
```

```
...
----------------------------------
1.증속 | 2.감속 | 3.중지
----------------------------------
선택: 
1
현재 속도=1
----------------------------------
1.증속 | 2.감속 | 3.중지
----------------------------------
선택: 
1
현재 속도=2
----------------------------------
1.증속 | 2.감속 | 3.중지
----------------------------------
선택: 
1
현재 속도=3
----------------------------------
1.증속 | 2.감속 | 3.중지
----------------------------------
선택: 
2
현재 속도=2
----------------------------------
1.증속 | 2.감속 | 3.중지
----------------------------------
선택: 
3
프로그램 종료
```

<br>

### do-while문

```java
Scanner scanner = new Scanner(System.in);  // Scanner 객체 생성
String inputString = scanner.nextline();  // nextLine() 메소드 호출

! inputString.equals("q");
```

- 위의 코드를 활용하여 키보드로부터 문자열을 입력받고 출력시키는 코드를 작성해보자

```java
import java.io.InputStream;
import java.util.Scanner;

public class DoWhileExample {
    public static void main(String[] args) {
        System.out.println("메시지를 입력하세요.");
        System.out.println("프로그램을 종료하려면 'q'를 입력하세요.");

        Scanner scanner = new Scanner(System.in);
        String inputString;

        do {
            System.out.print(">");  // println이 아닌 print -> 개행 X
            inputString = scanner.nextLine();
            System.out.println(inputString);
        } while (!inputString.equals("q"));

        System.out.println();
        System.out.println("프로그램 종료");
    }
}
```

```
메시지를 입력하세요.
프로그램을 종료하려면 'q'를 입력하세요.
>안녕하세요 짱구 할아버지에요~
안녕하세요 짱구 할아버지에요~
>안녕하세요
안녕하세요
>q
q

프로그램 종료
```

<br>

### break문

- 반복문이 중첩되어 있을 경우 break문은 가장 가까운 반복문만 종료하고 바깥쪽 반복문은 종료시키지 않는다.

- 중첩된 반복문에서 바깥쪽 반복문까지 종료시키려면 바깥쪽 반복문에 이름(라벨)을 붙이고, `break 이름;` 을 사용하면 된다.

```java
public class BreakOutterExample {
    public static void main(String[] args) {
        Outter:   // 이름(라벨)
        for (char upper = 'A'; upper <= 'Z'; upper++) {
            for (char lower = 'a'; lower <= 'z'; lower++) {
                System.out.println(upper + "-" + lower);
                if (lower == 'g') {
                    break Outter;  // 이름(라벨) 탈출
                }
            }
        }
        System.out.println("프로그램 실행 종료");
    }
}
```

<br>

### continue문

```java
public class ContinueExample {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            if (i % 2 != 0) {
                continue;
            }
            System.out.println(i);
        }
    }
}
```