# JAVA CODING CONVENTION (자바 코딩 규약)

> [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

<br>

## 1 소개

이 문서는 Java 프로그래밍 언어의 소스 코드에 대한 Google의 코딩 표준을 완전히 정의한 것입니다.

### 1.1 전문 용어(Terminology) 참고 사항

이 문서에서는 달리 명확히하지 않는 한 

  1. **클래스** 라는 용어는 "일반" 클래스, 열거형 클래스, 인터페이스 또는 어노테이션 타입( `@interface`) 을 의미하기 위해 포괄적으로 사용됩니다.
    
  2. **멤버 (클래스)** 라는 용어는 중첩된 클래스, 필드, 메서드 또는 생성자 를 의미하는 데 포괄적으로 사용됩니다 . 즉 초기화 자 및 주석을 제외한 클래스의 모든 최상위 내용

  3. **주석 (comment)** 이라는 용어는 항상 구현 주석을 나타냅니다 . "Javadoc"이라는 일반 용어를 사용하는 대신 "documentation comments"라는 구를 사용하지 않습니다.

<br>

## 2 소스 파일 기본 사항

### 2.1 파일 이름

소스 파일 이름은 포함하고있는 최상위 클래스의 `대·소문자를 구분(case-sensitive)`하는 이름과 `.java` 확장자로 구성됩니다.

<br>

### 2.2 파일 인코딩 : UTF-8

소스 파일은 UTF-8 로 인코딩됩니다.

<br>

### 2.3 특수 문자

#### 2.3.1 공백 문자

줄 종결자 시퀀스와는 별도로 ASCII 수평 공백 문자 ( 0x20 )는 소스 파일의 어느 위치에서나 나타날 수있는 유일한 공백 문자입니다. 이는 다음을 의미합니다.

  1. 문자열 및 문자 리터럴의 다른 모든 공백 문자는 이스케이프 처리됩니다.

     리터럴은 변수 및 상수에 저장되는 **값 자체**를 일컫는 말이다.

     이스케이프(escape) : 문자열이 가진 문법적인 역할에서 도망(escape)쳐서 문자로 인식하도록 하는 기법을 의미

  2. `탭 문자`는 들여 쓰기에 **사용 되지 않습니다.** → 스페이스를 사용

<br>

#### 2.3.2 특수 이스케이프 시퀀스

특수 이스케이프 시퀀스 (`\b`, `\t`, `\n`, `\f`, `\r`, `\"`, `\'` and `\\`)를 가진 어느 문자든, 해당 8진수 (예 : `\012`) 또는 유니코드 (예 : `\u000a` 줄바꿈) 이스케이프보다 특수 이스케이프 시퀀스가 사용됩니다.

<br>

#### 2.3.3 비 ASCII 문자

For the remaining non-ASCII characters, either the actual Unicode character (e.g. ∞) or the equivalent Unicode escape (e.g. \u221e) is used. The choice depends only on which makes the code easier to read and understand, although Unicode escapes outside string literals and comments are strongly discouraged.

> Tip: In the Unicode escape case, and occasionally even when actual Unicode characters are used, an explanatory comment can be very helpful.

|Example|Discussion|
|--------------------------------------|---|
|String unitAbbrev = "μs";|Best: perfectly clear even without a comment.|
|String unitAbbrev = "\u03bcs"; // "μs"|	Allowed, but there's no reason to do this.|
|String unitAbbrev = "\u03bcs"; // Greek letter mu, "s"|	Allowed, but awkward and prone to mistakes.|
|String unitAbbrev = "\u03bcs";|	Poor: the reader has no idea what this is.|
|return '\ufeff' + content; // byte order mark|	Good: use escapes for non-printable characters, and comment if necessary.|

> Tip: Never make your code less readable simply out of fear that some programs might not handle non-ASCII characters properly. If that should happen, those programs are broken and they must be fixed.


<br>

## 3 소스 파일 구조

소스 파일은 다음 순서로 구성됩니다.

  1. 라이센스 또는 저작권 정보 (있는 경우)

  2. Package 문

  3. Import 문

  4. 정확히 하나의 최상위 클래스

**정확히 하나의 공백 행**은 존재하는 각 섹션을 구분합니다.

<br>

### 3.1 라이센스 또는 저작권 정보 (있는 경우)

라이센스 또는 저작권 정보가 파일에 속하면 여기에 속합니다.

<br>

### 3.2 패키지 문 (Package statement)

package 문은 줄 바꿈되지 않습니다. 열 제한 (`4.4 절`, 열 제한 : 100)은 패키지 문에 적용되지 않습니다.

<br>

### 3.3 Import 문 (Import statements)

#### 3.3.1 와일드 카드 imports 사용 안됨

**와일드 카드 imports**, static 또는 기타 는 사용되지 않습니다.

<br>

#### 3.3.2 줄 바꾸기 안 됨 (No line-wrapping)

Import 문은 **줄 바꿈되지 않습니다.** 열 제한 (`4.4 절`, 열 제한 : 100)은 import 문에 적용되지 않습니다.

<br>

#### 3.3.3 순서 및 간격

imports는 다음과 같은 순서입니다 :

  1. 모든 static imports는 단일 블록으로 이루어집니다.
   
  2. 모든 non-static imports는 단일 블록으로 이루어집니다.

static 및 non-static imports가 둘 다 있는 경우, 하나의 빈 줄이 두 블록을 분리합니다. import 문 사이에 다른 빈 줄이 없습니다.

각 블록 내에서 import된 이름은 ASCII 정렬 순서로 나타납니다. (**주의** : 이것은 `.`가 `;` 앞에 정렬되기 때문에 ASCII 정렬 순서 로 되어있는 import 문과 동일하지 않습니다.)

<br>

#### 3.3.4 클래스에 대한 static import 사용 안함 (No static import for classes)

Static import는 정적 중첩 클래스(static nested class)에는 사용되지 않습니다. 일반 import문들과 같이 import 됩니다.

<br>

### 3.4 클래스 선언 (Class declaration)

#### 3.4.1 정확히 하나의 최상위 클래스 선언

각 최상위 클래스는 자체 원본 파일에 상주합니다. (Each top-level class resides in a source file of its own.)

<br>

#### 3.4.2 클래스 내용의 순서 (Ordering of class contents)

클래스의 멤버 및 초기화 프로그램에 대해 선택한 순서는 학습 가능성에 큰 영향을 줄 수 있습니다. 그러나 이를 어떻게 수행하는 지에 대한 단 하나의 올바른 방법은 없습니다. 다른 클래스는 다른 방법으로 내용을 배치할 수 있습니다.

중요한 것은 각 클래스가 **논리적인 순서**를 사용한다는 것인데, 그 순서를 묻는다면 관리자가 설명할 수 있을 것이란 것입니다. 예를 들어, 새로운 메소드는 클래스의 끝에 습관적으로 추가되는 것만은 아니라, 논리적인 순서가 아닌 추가된 날짜 순으로 정렬된 순서를 산출합니다. (What is important is that each class uses some logical order, which its maintainer could explain if asked. For example, new methods are not just habitually added to the end of the class, as that would yield "chronological by date added" ordering, which is not a logical ordering.)

<br>

##### 3.4.2.1 오버로드 : 절대 분할되지 않음

클래스에 여러 생성자가 있거나 동일한 이름을 가진 여러 메소드가 있는 경우, 이들 클래스는 순차적으로 나타나며 그 사이에 다른 코드는 없습니다. (private 멤버조차도 포함하지 않음)

<br>

## 4 서식 지정

용어 참고 : 블록과 유사한 구조 는 클래스, 메서드 또는 생성자의 본문을 참조합니다. 배열 이니셜 라이저 에 대한 4.8.3.1 절 에서 모든 배열 이니셜 라이저 는 선택적으로 블록 같은 구조로 처리 될 수 있습니다.

<br>

### 4.1 교정기

<br>

#### 4.1.1 중괄호는 선택적

교정기가 사용되어 if, else, for, do및 while문, 몸이 비어 있거나 단 하나의 문이 포함 된 경우에도.

<br>

#### 4.1.2 비어 있지 않는 구획 : K & R 작풍

중괄호는 Kernighan and Ritchie 스타일 ( " Egyptian brackets ")을 따라 비어 있지 않은 블록과 블록과 같은 구조 를 만듭니다 .

여는 중괄호 앞에 줄 바꿈이 없습니다.
여는 중괄호 뒤의 줄 바꿈
닫는 중괄호 앞에 줄 바꿈을하십시오.
해당 중괄호가 문을 종료하거나 메서드, 생성자 또는 명명 된 클래스 의 본문을 종료하는 경우에만 닫는 중괄호 뒤의 줄 바꿈 예를 들어 괄호 다음에 쉼표 가 오면 줄 바꿈 이 없습니다 .else
예 :

return () -> { while ( condition ()) { 
    method (); } };   
    
  


반환 새로운 MyClass에을 () { @Override 공공 무효 방법 () { 경우 ( 조건 ()) { 시도 { 
        뭔가를 (); } catch ( ProblemException e ) { 
        복구 (); } } else if ( otherCondition ()) { 
      somethingElse (); } else { 
      lastThing (); } } };   
     
      
       
         
      
        
      
    
  
enum 클래스에 대한 몇 가지 예외가 섹션 4.8.1, Enum 클래스에 나와 있습니다.

<br>

#### 4.1.3 빈 블록 : 간결함

빈 블록이나 블록과 같은 구조는 K & R 스타일 일 수있다 ( 4.1.2 절 에서 설명 ). 이 열린 후 또는, 더 자 (사이에서 줄 바꿈 즉시 폐쇄 될 수있다 {}) 않으면 그것의 일부인 다중 블록 명령문 (: 직접 여러 블록들을 포함하는 하나 또는 ).if/elsetry/catch/finally

예 :

  // 허용 가능 void doNothing () {}
   

  // 이것은 똑같이 받아 들일 수있다 void doNothingElse () { }
   
  
  // 허용되지 않습니다 : 다중 블록 문에서 간결한 빈 블록이 없습니다. try { 
    doSomething (); } catch ( Exception e ) {}
   
     
<br>

### 4.2 블록 들여 쓰기 : +2 공백
새로운 블록 또는 블록과 같은 구조가 열릴 때마다 들여 쓰기가 두 칸 증가합니다. 블록이 끝나면 들여 쓰기가 이전 들여 쓰기 수준으로 돌아갑니다. 들여 쓰기 수준은 블록 전체에서 코드와 주석 모두에 적용됩니다. (4.1.2 비어 있지 않은 블록 : K & R 스타일 의 예를 참조하십시오 .)

<br>

### 4.3 한 줄에 하나의 문장
각 명령문 다음에 줄 바꿈이옵니다.

<br>

### 4.4 열 제한 : 100
Java 코드의 컬럼 제한은 100 자입니다. "문자"는 모든 유니 코드 코드 포인트를 의미합니다. 아래에 명시된 것을 제외하고,이 제한을 초과하는 모든 행은 4.5 절, 줄 바꿈 에서 설명 된 것처럼 줄 바꿈되어야합니다 .

각 유니 코드 코드 포인트는 표시 폭이 더 크거나 작더라도 하나의 문자로 계산됩니다. 예를 들어 전각 문자를 사용 하는 경우이 규칙이 엄격하게 요구하는 경우보다 먼저 줄을 감쌀 수 있습니다.

예외 :

열의 제한에 따르는 라인은 불가능합니다 (예를 들어, Javadoc의 긴 URL 또는 긴 JSNI 메소드 참조).
package그리고 import문 (섹션 3.2 참조 패키지 문 3.3 가져 오기 문을 ).
쉘에 잘라 붙일 수있는 주석의 명령 행.

<br>

### 4.5 줄 바꿈

용어 참고 : 그렇지 않으면 한 줄을 합법적으로 차지할 수있는 코드를 여러 줄로 나누면이 활동을 줄 바꿈 이라고 합니다 .

모든 상황에서 정확히 줄 바꿈하는 방법을 정확하게 보여주는 포괄적이고 결정적인 공식은 없습니다 . 매우 자주 동일한 코드를 줄 바꿈하는 여러 가지 유효한 방법이 있습니다.

참고 : 줄 바꿈의 일반적인 이유는 열 제한이 넘치지 않도록하기위한 것이지만 실제로 열 제한 내에 들어갈 수있는 코드조차도 작성자의 재량에 따라 줄 바꿈 될 수 있습니다.

팁 : 메서드 또는 로컬 변수를 추출하면 줄 바꿈없이 문제를 해결할 수 있습니다.

<br>

#### 4.5.1 중단 할 위치

줄 바꿈의 주요 지시어는 다음과 같습니다 . 더 높은 통사론 수준 에서 깨는 것을 선호 합니다 . 또한:

비 할당 연산자 에서 줄이 끊어 지면 기호 앞에 나누기가옵니다 . (C ++ 및 JavaScript와 같은 다른 언어의 경우 Google 스타일에서 사용되는 것과 동일한 방법은 아닙니다.)
이것은 "연산자와 같은"기호에도 적용됩니다.
도트 분리 자 ( .)
메서드 참조 ( ::) 의 2 개의 콜론
바인딩 된 형식의 앰퍼샌드 ( )<T extends Foo & Bar>
catch 블록의 파이프 ( ).catch (FooException | BarException e)
선가에서 파괴 될 때 할당 연산자 휴식은 일반적으로 제공 후 기호,하지만 어느 쪽이든이 허용됩니다.
이는 확장 for( "foreach") 문 에서 "할당 연산자와 유사한"콜론에도 적용됩니다 .
메서드 또는 생성자 이름은 그 뒤에 오는 여는 괄호 ( ()에 연결되어 있습니다.
쉼표 ( ,)는 앞에 오는 토큰에 연결되어 있습니다.
람다의 몸체가 하나의 칭찬을받지 않은 표현으로 구성되어 있다면, 화살표 바로 다음에 틈이 생길 수 있다는 점을 제외하고는, 람다의 화살표 근처에서 선이 절대로 끊어지지 않습니다. 예 :
MyLambda < String , Long , Object > lambda = ( 문자열 레이블 , Long 값 , Object obj ) -> { ... };  
        
        
    

술어 < String > 술어 = str -> 
    longExpressionInvolving ( str );
참고 : 행 줄 바꿈의 주된 목표는 명확한 코드를 갖는 것 입니다. 가장 적은 수의 행에 들어 맞는 코드 일 필요 는 없습니다 .

<br>

#### 4.5.2 연속 선을 적어도 +4 공백으로 들여 씁니다.

줄 바꿈을 할 때 첫 줄 (각 연속 줄 ) 뒤의 각 줄 은 원래 줄에서 적어도 +4 줄 들여 쓰여 있습니다.

연속 선이 여러 개인 경우 들여 쓰기가 원하는대로 +4 이상으로 변경 될 수 있습니다. 일반적으로 두 개의 연속 줄은 구문 상 병렬 요소로 시작하는 경우에만 동일한 들여 쓰기 수준을 사용합니다.

수평 정렬 에 대한 4.6.3 절 에서는 가변적 인 수의 공백을 사용하여 특정 토큰을 이전 줄과 정렬하는 연습을 권장하지 않습니다.

<br>

### 4.6 공백

<br>

#### 4.6.1 수직 공백

하나의 빈 줄이 항상 나타납니다.

사이 필드, 생성자, 메소드, 중첩 클래스, 정적 초기화 및 인스턴스 초기화 : 연속 구성원 또는 클래스의 초기화.
예외 : 두 개의 연속 된 필드 사이에 빈 줄 (다른 코드가없는)은 선택 사항입니다. 이러한 공백 라인은 필요에 따라 필드의 논리적 그룹 을 만드는 데 사용됩니다 .
예외 : 열거 형 상수 사이의 빈 줄은 4.8.1 절 에서 다룹니다 .
이 문서의 다른 섹션 (예 : 섹션 3, 소스 파일 구조 및 섹션 3.3, 가져 오기 문 )에서 요구하는대로
단일 빈 줄은 코드를 논리적 하위 섹션으로 구성하는 문과 같이 가독성을 향상시키는 곳이면 어디에서나 나타날 수 있습니다. 첫 번째 멤버 나 이니셜 라이저 앞에있는 빈 줄 또는 클래스의 마지막 멤버 나 이니셜 라이저 뒤에 오는 빈 줄은 권장되거나 권장되지 않습니다.

여러 개의 연속적인 공백 행은 허용되지만 절대 필요하지는 않습니다 (또는 권장 됨).

<br>

#### 4.6.2 가로 공백

리터럴, 주석 및 Javadoc을 제외하고 언어 또는 기타 스타일 규칙이 필요한 곳을 넘어서는 경우 단일 ASCII 공간이 다음 장소 에만 나타납니다 .

같은 임의 예약어 분리 if, for또는 catch열린 괄호 (에서, (그 라인을 다음)
해당 줄 앞에 오는 중괄호 ( ) 와 같이 예약어 (예 : elseor )를 구분합니다.catch}
{두 가지 예외가 있는 열린 중괄호 ( ) 전에 :
@SomeAnnotation({a, b}) (사용 공간 없음)
String[][] x = {{"foo"}};( {{아래 항목 8에 의해 공간이 필요하지 않음 )
임의의 2 항 또는 3 항 연산자의 양측에. 이것은 "연산자와 같은"기호에도 적용됩니다.
결합 형 바인드의 앰퍼샌드 : <T extends Foo & Bar>
복수의 예외를 처리하는 catch 블록의 파이프 catch (FooException | BarException e)
:향상된 for( "foreach") 문 에서 콜론 ( )
람다 식의 화살표 : (String str) -> str.length()
하지만
::메소드 참조 의 두 개의 콜론 ( )은 다음과 같이 작성됩니다.Object::toString
점 분리 기호 ( .)는 다음과 같이 작성됩니다. object.toString()
캐스팅 후 ,:;또는 닫는 괄호 ( ))
//줄 끝 주석을 시작하는 이중 슬래시 ( ) 의 양면에 . 여기에는 여러 개의 공백이 허용되지만 필수는 아닙니다.
선언의 유형과 변수 사이 : List<String> list
배열 초기화 자의 양쪽 중괄호 안의 선택 사항.
new int[] {5, 6}그리고 모두 유효new int[] { 5, 6 }
형식 주석과 []또는 사이 ....
이 규칙은 라인의 시작이나 끝에서 추가 공간을 요구하거나 금지하는 것으로 결코 해석되지 않습니다. 그것은 내부 공간 만을 다룹니다 .

<br>

#### 4.6.3 수평 정렬 : 필요 없음

용어 참고 : 가로 맞춤 은 이전 줄의 특정 토큰 아래에 특정 토큰을 직접 표시하려는 목적으로 코드에 가변 개수의 추가 공백을 추가하는 연습입니다.

이 방법은 허용되지만 Google 스타일 에서는 절대로 필요하지 않습니다 . 이미 사용 된 장소에서 수평 정렬 을 유지할 필요가 없습니다 .

다음은 정렬이없는 예입니다. 그런 다음 정렬을 사용합니다.

개인 int x ; // 이것은 아주 사적인 색상 입니다 . // this too too  
  

개인 int    x ; // 허용되지만 나중에는 private color 색을 편집 합니다 . // 정렬되지 않은 상태로 둘 수 있습니다.       
   
팁 : 정렬은 가독성을 높일 수 있지만 향후 유지 관리에 문제가됩니다. 한 줄만 만져야 할 미래의 변화를 생각해보십시오. 이 변경으로 인해 이전에 기쁘게하는 서식이 엉망으로 남을 수 있으며 이는 허용 됩니다. 더 자주 그것은 코더 (아마도 당신)에게 주변 선상의 공백을 조정하도록 촉구하며 계단식 직렬 변환을 유발할 수 있습니다. 그 한줄짜리 변경은 이제 "폭발 반경"을 갖습니다. 이것은 최악의 경우 혼잡하지 않은 상황이 될 수 있지만 근본적으로 버전 히스토리 정보가 손상되고 검토자가 느려지고 충돌이 병합됩니다.

<br>

### 4.7 괄호 그룹화 : 권장

선택적 그룹 괄호는 작성자와 검토자가 코드가 없으면 잘못 해석 될 가능성이 없으며 코드를 읽기 쉽게 만든다는 데 동의하지 않는 경우에만 생략됩니다. 이다 없는 모든 독자가 기억 전체 자바 연산자 우선 순위 테이블이 있다고 가정하는 것이 합리적.

<br>

### 4.8 특정 구성

<br>

#### 4.8.1 열거 형 클래스

enum 상수 뒤에 오는 각 쉼표 뒤에 줄 바꿈은 선택 사항입니다. 추가 공백 행 (대개 하나만 허용)이 허용됩니다. 이것은 한 가지 가능성입니다.

private enum Answer { 
  YES { @Override public String toString () { return "예" ; } },   
       
       
    
  

  아니오 ,
  아마도
}
이 배열 이니셜 (4.8.3.1 절에 참조 것처럼없는 방법 및 상수 없음 서적 열거 클래스 임의로 포맷 될 수 어레이를 초기화 ).

개인 열거 정장 { 클럽 , 하트 , 스페이드 , 다이아몬드 }   
enum 클래스 는 클래스 이므로 서식 지정 클래스의 다른 모든 규칙이 적용됩니다.

<br>

#### 4.8.2 변수 선언

##### 4.8.2.1 선언 당 하나의 변수

모든 변수 선언 (필드 또는 로컬)은 하나의 변수 만 선언합니다. 선언 int a, b;은 사용되지 않습니다.

예외 :for 루프 의 헤더에서 여러 변수 선언을 사용할 수 있습니다.

<br>

##### 4.8.2.2 필요할 때 선언

지역 변수는 포함하는 블록이나 블록과 같은 구조의 시작시에 습관적으로 선언 되지 않습니다 . 대신 로컬 변수는 범위를 최소화하기 위해 처음 사용 된 시점 (이유 내에서) 가까이에 선언됩니다. 로컬 변수 선언에는 일반적으로 초기화자가 있거나 선언 직후에 초기화됩니다.

<br>

#### 4.8.3 배열

<br>

##### 4.8.3.1 배열 이니셜 라이저 : "블록과 같은"

임의의 배열 이니셜 라이저는 "블록 형 구조"처럼 선택적 으로 포맷팅 될 수 있습니다. 예를 들어, 다음 (모든 유효 하지 완전한 목록) :

새로운 INT [] { 새로운 INT [] { 0 , 1 , 2 , 3 0 , } 1 , 2 , 새로운 INT [] { 3 , 0 , 1 , } 2 , 3 } 새로운 INT [] { 0 , 1 , 2 , 3 }               
                 
                                            
<br>

##### 4.8.3.2 C 스타일 배열 선언 없음

대괄호 는 변수가 아니라 유형 의 일부를 형성 합니다. , not .String[] argsString args[]

<br>

#### 4.8.4 스위치 문

용어 참고 : 스위치 블록 의 중괄호 안에는 하나 이상의 명령문 그룹이 있습니다. 각 명령문 그룹은 하나 이상의 스위치 레이블 ( 또는 ), 하나 이상의 명령문 (또는 마지막 명령문 그룹의 경우 0 개 이상의 명령문)으로 구성됩니다.case FOO:default:

<br>

##### 4.8.4.1 들여 쓰기
다른 블록과 마찬가지로 스위치 블록의 내용은 들여 쓰기 +2됩니다.

스위치 레이블 다음에 줄 바꿈이 있고 들여 쓰기 수준은 블록이 열리는 것과 똑같이 +2 증가합니다. 다음 스위치 레이블은 블록이 닫힌 것처럼 이전 들여 쓰기 수준으로 돌아갑니다.

<br>

##### 4.8.4.2 추락 : 주석 처리 됨
스위치 블록 내에서 각 문 그룹의 (a와 함께 돌연 종료하거나 break, continue, return또는 발생한 예외), 또는 또는 것이다 실행 표시하기 위해 코멘트와 함께 표시되어 있습니다 다음 명령문 그룹에 계속합니다. 폴스 스루 (fall-through)라는 아이디어를 전달하는 주석은 충분합니다 (일반적으로 // fall through). 이 특수 주석은 스위치 블록의 마지막 명령문 그룹에 필요하지 않습니다. 예:

스위치 ( 입력 ) { 케이스 1 : 케이스 2 : 
    prepareOneOrTwo (); // 사례 3을 통해 빠져 
    나온다 : handleOneTwoOrThree (); 휴식 ; 기본값 : 
    handleLargeNumber ( 입력 ); }  
   
   
    
   
    
  
명령문 그룹 끝에 만 주석을 입력 할 필요는 없습니다 .case 1:

<br>

##### 4.8.4.3 default경우가 존재

각 switch 문에는 default코드가없는 경우에도 명령문 그룹이 포함됩니다.

예외 :enum 유형에 대한 switch 문 은 해당 유형의 가능한 모든 값을 포함하는 명시적인 경우를 포함하는 경우 명령문 그룹을 생략 할 수 있습니다 . 이렇게하면 IDE 또는 기타 정적 분석 도구에서 모든 사례를 놓친 경우 경고를 표시 할 수 있습니다. default

<br>

#### 4.8.5 주석

클래스, 메소드 또는 생성자에 적용되는 주석은 문서 ​​블록 바로 다음에 표시되며 각 주석은 고유 한 행 (즉, 한 행에 하나의 주석)에 나열됩니다. 이러한 줄 바꿈은 줄 바꿈 (4.5 절, 줄 바꿈 )을 구성하지 않으므로 들여 쓰기 수준이 증가하지 않습니다. 예:

@Override @Nullable 공용 문자열 getNameIfPresent () { ... }

    
예외 : 하나의 매개 변수가 주석이 수 대신 예를 들어, 서명의 첫 번째 줄과 함께 나타납니다 :

@Override public int hashCode () { ... }     
필드에 적용되는 주석은 문서 ​​블록 바로 다음에 나타납니다.이 경우 여러 주석 (매개 변수가있는 경우도 있음)이 같은 줄에 나열 될 수 있습니다. 예 :

@Partial @Mock DataLoader 로더 ;  
매개 변수, 지역 변수 또는 유형에 대한 주석 서식 지정에 대한 특정 규칙은 없습니다.

<br>

#### 4.8.6 주석

이 섹션에서는 구현 주석을 다룹니다 . Javadoc은 7 장, Javadoc 에서 별도로 다루어 집니다.

임의의 줄 바꿈 앞에는 임의의 공백 문자와 그 뒤에 구현 주석이 올 수 있습니다. 이러한 주석은 행을 비어 있지 않게 만듭니다.

<br>

##### 4.8.6.1 블록 코멘트 스타일

블록 코멘트는 주변 코드와 같은 레벨로 들여 쓰기됩니다. /* ... */스타일이나 // ...스타일이 다를 수 있습니다 . 여러 줄 /* ... */주석의 경우 후속 줄은 이전 줄의 줄 과 *정렬 되어야합니다 *.

/ *
 * 이것은 //입니다. / / 또는
 * 괜찮아. // 이것입니다. * 이렇게해도. * / * /
 
주석은 별표 또는 기타 문자로 그려진 상자에 넣지 않습니다.

팁 : 여러 줄로 된 주석을 쓸 때 /* ... */자동 코드 작성자가 필요에 따라 줄을 다시 감싸는 스타일을 사용하려면이 스타일을 사용하십시오 (단락 스타일). 대부분의 포맷터는 // ...스타일 주석 블록 에서 줄 바꿈을하지 않습니다 .

<br>

#### 4.8.7 수정 자

클래스 및 멤버 수식자가 있으면, Java 언어 사양으로 추천되고있는 순서로 출현합니다.

공용 보호 된 개인 추상 기본 정적 정적 최종 일시적 휘발성 동기화 네이티브 strictfp

<br>

#### 4.8.8 숫자 리터럴

long값이 큰 정수 리터럴은 대문자 L접미사를 사용하며 소문자는 사용 하지 않습니다 (숫자와 혼동하지 않기 위해 1). 예를 들어, 3000000000L 보다는 3000000000l.

<br>

## 5 이름 지정

> ### 케이스 네이밍 컨벤션(case naming convention)의 종류

- 카멜 표기법

    - "camel**C**ase"

    - 각 단어의 첫문자를 대문자로 표기하고 붙여쓰되, 맨처음 문자는 소문자로 표기함.

    - 띄어쓰기 대신 대문자로 단어를 구분하는 표기 방식.

    - `lowerCamelCase`와 `UpperCamelCase`로 세분화할 수 있다.
      
      - UpperCamelCase는 CamelCase에서 맨 앞글자를 대문자로 표기하는 것을 뜻하며, PascalCase라고도 부른다.

- 파스칼 표기법

    - "**P**ascal**C**ase"

    - 첫 단어를 대문자로 시작하는 표기법.

    - UpperCamelCase 라고도 한다.

- 스네이크 표기법

    - "snake`_`case"

    - 단어를 밑줄문자(`-`)로 구분하는 표기법

- 케밥 표기법

    - "kebab`-`case"

    - 하이픈(`-`)으로 단어를 연결하는 표기법

    - HTML 태그의 id, class 속성으로 흔히 쓰임

- 헝가리언 표기법 (Hungarian notation)

    - 접두어를 사용하는 표기법

    - 예시 : strName, bBusy, szName

<br>

### 5.1 모든 식별자에 공통적인 규칙

식별자는 ASCII 문자와 숫자만 사용하며, 아래에서 언급하는 소수의 경우에는 밑줄 표시됩니다. 따라서 각 유효한 식별자 이름은 정규 표현식과 일치합니다.

  - `\w+` : (한 개 이상의 알파벳 또는 숫자).

Google 스타일에서는 특수 접두사 또는 접미사가 사용 되지 않습니다. 예를 들어, 이 이름들은 구글 스타일이 아닙니다.

  - `name_`, `mName`, `s_name`와 `kName`.

### 5.2 식별자 유형별 규칙

#### 5.2.1 패키지 이름 (Package names)

> 패키지 이름은 **모두 소문자**이며 연속 단어는 단순히 연결됩니다. (**밑줄 없음**)

  - 예로 `com.example.deepspace`은 가능하지만 `com.example.deepSpace` 또는 `com.example.deep_space`는 불가능

<br>

#### 5.2.2 클래스 이름 (Class names)

> 클래스 이름은 **UpperCamelCase (= PascalCase)** 로 작성됩니다.

클래스 이름은 일반적으로 명사 또는 명사구입니다. ( 예 : `Character`또는 `ImmutableList`). 인터페이스 이름은 명사 또는 명사구 (예 : `List`) 일 수도 있지만 때로는 형용사 또는 형용사가 필요할 수도 있습니다 (예 : `Readable`).

어노테이션 유형을 명명하기 위한 특정 규칙이 없으며, 잘 정립된 규칙조차 없습니다.

테스트 클래스의 이름은 테스트 할 클래스의 이름으로 시작하고 `Test`로 끝납니다.

- `HashTest`또는 `HashIntegrationTest`.

<br>

#### 5.2.3 메소드 이름 (Method names)

> 메서드 이름은 **lowerCamelCase**로 작성됩니다.

메서드 이름은 일반적으로 동사 또는 동사구입니다. (예 : `sendMessage` 또는 `stop`)

밑줄은 JUnit 테스트 메소드 이름에 나타날 수 있으며 , lowerCamelCase에 쓰여진 각 컴포넌트 와 함께 이름의 논리적 구성 요소를 구분할 수 있습니다. 하나의 전형적인 패턴은 `<methodUnderTest>_<state>`으로, `pop_emptyStack` 가 예시이다. 테스트 방법의 이름을 짓는 단 하나의 올바른 방법은 없습니다.

<br>

#### 5.2.4 상수 이름 (Constant names)

> 상수 이름은 `CONSTANT_CASE` **모두 대문자를 사용** 하며, 각 단어는 하나의 **밑줄**로 분리됩니다.

그리고 상수의 이름은 일반적으로 명사 또는 명사구입니다.

그런데 상수는 정확히 무엇일까요?

**상수**는 내용이 [깊은 불변성(deeply immutable)](https://stackoverflow.com/questions/12053945/is-guavas-immutablexxx-really-immutable)을 가지고, **메소드**에서는 부수 효과(side effect)를 발견할 수 없는 `static final 필드`입니다. 여기에는 프리미티브, 문자열, 변경 불가능한 유형 및 변경 불가능한 유형의 변경 불가능한 콜렉션(primitives, Strings, immutable types, and immutable collections of immutable types)이 포함됩니다. 인스턴스의 관찰 가능 상태가 변경 될 수 있으면 상수가 아닙니다.

> 단순히 객체를 절대 변형시키지 않으려는 것은 충분하지 않습니다. (Merely intending to never mutate the object is not enough.)

```java
// Constants
static final int NUMBER = 5;
static final ImmutableList<String> NAMES = ImmutableList.of("Ed", "Ann");
static final ImmutableMap<String, Integer> AGES = ImmutableMap.of("Ed", 35, "Ann", 32);
static final Joiner COMMA_JOINER = Joiner.on(','); // because Joiner is immutable
static final SomeMutableType[] EMPTY_ARRAY = {};
enum SomeEnum { ENUM_CONSTANT }

// Not constants
static String nonFinal = "non-final";
final String nonStatic = "non-static";
static final Set<String> mutableCollection = new HashSet<String>();
static final ImmutableSet<SomeMutableType> mutableElements = ImmutableSet.of(mutable);
static final ImmutableMap<String, SomeMutableType> mutableValues =
    ImmutableMap.of("Ed", mutableInstance, "Ann", mutableInstance2);
static final Logger logger = Logger.getLogger(MyClass.getName());
static final String[] nonEmptyArray = {"these", "can", "change"};
```

<br>

#### 5.2.5 상수가 아닌 필드 이름 (Non-constant field names)

상수가 아닌 필드 이름 (static 또는 기타)은 **lowerCamelCase**로 작성됩니다.

이 이름은 일반적으로 명사 또는 명사구입니다.

  - 예 : `computedValues` 또는 `index`

<br>

#### 5.2.6 매개 변수 이름 (Parameter names)

매개 변수 이름은 **lowerCamelCase**로 작성됩니다.

공용 메서드에서 한 문자 매개 변수 이름은 피해야합니다.

<br>

#### 5.2.7 지역 변수 이름 (Local variable names)

지역 변수 이름은 **lowerCamelCase**로 작성됩니다.

비록 final과 immutable일 때에도, 지역 변수는 상수로 간주되지 않으며 상수와 같은 방식이 되면 안됩니다.

<br>

#### 5.2.8 타입 변수 이름 (Type variable names)

타입 변수의 이름은 두 가지 스타일 중 하나로 지정됩니다.

  1. 단일 대문자는 임의로 하나 번호 뒤에 (예 : `E`, `T`, `X`, `T2`)
  
  2. 클래스에 사용되는 형식의 이름 (`5.2.2 절`, 클래스 이름 참조)과 그 뒤에 대문자 `T`(예 : `RequestT`, `FooBarT`)가 옵니다 .

<br>

### 5.3 카멜 케이스 : 정의된

때때로 **두음문자, 약자**(acronyms) 또는 "IPv6"또는 "iOS"와 같이 **특이한 구조**가 있는 경우, 영어 문구를 camelCase로 변환하는 합리적인 방법이 여러 가지 있습니다. 예측 가능성을 높이기 위해 Google 스타일은 다음과 같은 (거의) 결정론적 계획을 지정합니다.

> 산문 형태의 이름으로 시작 :

1. 구문을 일반 ASCII로 변환하고 아포스트로피를 제거하십시오. 예를 들어, "Müller 's algorithm"은 "Muellers algorithm"이 될 수 있습니다.
  
2. 이 결과를 단어, 공백 및 나머지 구두점(일반적으로 하이픈)으로 나눕니다.
    
     - 권장 사항 : 일반적인 사용법에서 이미 기존의 camelCase 사례가 있는 단어가 있는 경우 해당 단어를 구성 부분으로 분리합니다 (예 : 'AdWords'는 'ad words'). "iOS"와 같은 단어는 실제로 camelCase에 있는 것이 아니라는 점에 유의합니다. 이것은 어떠한 규약으로도 묘사가 거의 불가하므로, 본 권고안은 적용되지 않습니다.

3. 이제 모든 것을 소문자로 (약어 포함), 다음의 대문자만 첫 번째 문자로 만듭니다 :
    
     - ... 각 단어, 위 camelCase를 산출하기 위해, 또는
    
     - ... 첫 번째 단어를 제외한 각 단어, lowerCamelCase 를 산출하기 위해

4. 마지막으로 모든 단어를 하나의 식별자로 결합하십시오.

<br>

원래 단어의 대소문자는 **거의 전적으로 무시**됩니다.

<br>

> 예시

|산문 형태에서 시작|Correct|Incorrect|
|---|---|---|
|"XML HTTP request"|`XmlHttpRequest`|`XMLHTTPRequest`|
|"new customer ID"|`newCustomerId`|`newCustomerID`|
|"inner stopwatch"|`innerStopwatch`|`innerStopWatch`|
|"supports IPv6 on iOS?"|`supportsIpv6OnIos`|`supportsIPv6OnIOS`|
|"YouTube importer"|`YouTubeImporter`<br>`YoutubeImporter`*||

`*` 허용은 되지만 추천하지 않습니다.


> 영어에는 모호하게 하이픈으로 연결된 몇몇의 단어가 있습니다 : 예를 들어 "nonempty"와 "non-empty" 모두 올바르기 때문에, 메소드 이름 `checkNonempty`과 `checkNonEmpty` 마찬가지로 모두 올바릅니다.

<br>

## 6 프로그래밍 실습

<br>

## 7 Javadoc