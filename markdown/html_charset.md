# HTML 특수문자 리스트

- **마크다운 문법을 본문에서 사용하고 싶을 때**
- **HTML 상에서 특수문자가 제대로 나타나지 않을 때**

마크다운 테이블(표)에서 `|` , `#` 과 같은 마크다운 문법을 사용하면 글자로 인식하지 않고 문법으로 인식하여 곤란한 상황이 많이 발생한다.
그럴때는 아래의 예시와 같이 [HTML Coded Character Set](http://kor.pe.kr/util/4/charmap2.htm) 을 사용해보자.

테이블에서 `|` 를 인식하지 않으므로 HTML코드 `&#124;`를 사용하였다.

<br>

> 마크다운 코드

```
|연산자|연산자의 기능|결합방향|
|:---:|:---:|:---:|
|&#124;|비트단위로 OR 연산을 한다.|→|
```

<br>

> 결과물

|연산자|연산자의 기능|결합방향|
|:---:|:---:|:---:|
|&#124;|비트단위로 OR 연산을 한다.|→|

<br>

# HTML 특수문자 테이블

<p align="center">
<table cellspacing="1" cellpadding="3" width="600" align="center" border="0">
<tbody><tr>
<td colspan="4" align="center"><font size="2">HTML 상에서 특수문자가 제대로 나타나지 않을 수 있기 때문에 아래 문자들를 사용합니다.</font>
</td>
</tr>
<tr>
<td align="center" height="30">표현문자</td>
<td align="center" height="30">숫자표현</td>
<td align="center" height="30">문자표현</td>
<td align="center" height="30">설명</td>
</tr>
<tr><td>-</td><td>&amp;#00;-&amp;#08;</td><td>-</td><td>사용하지 않음</td></tr>
<tr><td>space</td><td>&amp;#09;</td><td>-</td><td>수평탭</td></tr>
<tr><td>space</td><td>&amp;#10;</td><td>-</td><td>줄 삽입</td></tr>
<tr><td>-</td><td>&amp;#11;-&amp;#31;</td><td>-</td><td>사용하지 않음</td></tr>
<tr><td>space</td><td>&amp;#32;</td><td>-</td><td>여백</td></tr>
<tr><td>!</td><td>&amp;#33;</td><td>-</td><td>느낌표</td></tr>
<tr><td>"</td><td>&amp;#34;</td><td>&amp;quot;</td><td>따옴표</td></tr>
<tr><td>#</td><td>&amp;#35;</td><td>-</td><td>숫자기호</td></tr>
<tr><td>$</td><td>&amp;#36;</td><td>-</td><td>달러</td></tr>
<tr><td>%</td><td>&amp;#37;</td><td>-</td><td>백분율 기호</td></tr>
<tr><td>&amp;</td><td>&amp;#38;</td><td>&amp;amp;</td><td>Ampersand</td></tr>
<tr><td>'</td><td>&amp;#39;</td><td>-</td><td>작은 따옴표</td></tr>
<tr><td>(</td><td>&amp;#40;</td><td>-</td><td>왼쪽 괄호</td></tr>
<tr><td>)</td><td>&amp;#41;</td><td>-</td><td>오른쪽 괄호</td></tr>
<tr><td>*</td><td>&amp;#42;</td><td>-</td><td>아스트릭</td></tr>
<tr><td>+</td><td>&amp;#43;</td><td>-</td><td>더하기 기호</td></tr>
<tr><td>,</td><td>&amp;#44;</td><td>-</td><td>쉼표</td></tr>
<tr><td>-</td><td>&amp;#45;</td><td>-</td><td>Hyphen</td></tr>
<tr><td>.</td><td>&amp;#46;</td><td>-</td><td>마침표</td></tr>
<tr><td>/</td><td>&amp;#47;</td><td>-</td><td>Solidus (slash)</td></tr>
<tr><td>0 - 9</td><td>&amp;#48;-&amp;#57;</td><td>-</td><td>0부터 9까지</td></tr>
<tr><td>:</td><td>&amp;#58;</td><td>-</td><td>콜론</td></tr>
<tr><td>;</td><td>&amp;#59;</td><td>-</td><td>세미콜론</td></tr>
<tr><td>&lt;</td><td>&amp;#60;</td><td>&amp;lt;</td><td>보다 작은</td></tr>
<tr><td>=</td><td>&amp;#61;</td><td>-</td><td>등호</td></tr>
<tr><td>&gt;</td><td>&amp;#62;</td><td>&amp;gt;</td><td>보다 큰</td></tr>
<tr><td>?</td><td>&amp;#63;</td><td>-</td><td>물음표</td></tr>
<tr><td>@</td><td>&amp;#64;</td><td>-</td><td>Commercial at</td></tr>
<tr><td>A - Z</td><td>&amp;#65;-&amp;#90;</td><td>-</td><td>A부터 Z까지</td></tr>
<tr><td>[</td><td>&amp;#91;</td><td>-</td><td>왼쪽 대괄호</td></tr>
<tr><td>\</td><td>&amp;#92;</td><td>-</td><td>역슬래쉬</td></tr>
<tr><td>]</td><td>&amp;#93;</td><td>-</td><td>오른쪽 대괄호</td></tr>
<tr><td>^</td><td>&amp;#94;</td><td>-</td><td>탈자부호</td></tr>
<tr><td>_</td><td>&amp;#95;</td><td>-</td><td>수평선</td></tr>
<tr><td>`</td><td>&amp;#96;</td><td>-</td><td>Acute accent</td></tr>
<tr><td>a - z</td><td>&amp;#97;-&amp;#122;</td><td>-</td><td>a부터 z까지</td></tr>
<tr><td>{</td><td>&amp;#123;</td><td>-</td><td>왼쪽 중괄호</td></tr>
<tr><td>|</td><td>&amp;#124;</td><td>-</td><td>수직선</td></tr>
<tr><td>}</td><td>&amp;#125;</td><td>-</td><td>오른쪽 중괄호</td></tr>
<tr><td>~</td><td>&amp;#126;</td><td>-</td><td>꼬리표</td></tr>
<tr><td>-</td><td>&amp;#127;-&amp;#159;</td><td>-</td><td>사용하지 않음</td></tr>
<tr><td><span style="COLOR: #333333"></span></td><td>&amp;#160;</td><td>&amp;nbsp;</td><td>Non-breaking space</td></tr>
<tr><td>¡</td><td>&amp;#161;</td><td>&amp;iexcl;</td><td>거꾸로된 느낌표</td></tr>
<tr><td>￠</td><td>&amp;#162;</td><td>&amp;cent;</td><td>센트 기호</td></tr>
<tr><td>￡</td><td>&amp;#163;</td><td>&amp;pound;</td><td>파운드</td></tr>
<tr><td>¤</td><td>&amp;#164;</td><td>&amp;curren;</td><td>현재 환율</td></tr>
<tr><td>￥</td><td>&amp;#165;</td><td>&amp;yen;</td><td>엔</td></tr>
<tr><td>|</td><td>&amp;#166;</td><td>&amp;brvbar;</td><td>끊어진 수직선</td></tr>
<tr><td>§</td><td>&amp;#167;</td><td>&amp;sect;</td><td>섹션 기호</td></tr>
<tr><td>¨</td><td>&amp;#168;</td><td>&amp;uml;</td><td>움라우트</td></tr>
<tr><td>ⓒ</td><td>&amp;#169;</td><td>&amp;copy;</td><td>저작권</td></tr>
<tr><td>ª</td><td>&amp;#170;</td><td>&amp;ordf;</td><td>Feminine ordinal</td></tr>
<tr><td>≪</td><td>&amp;#171;</td><td>&amp;laquo;</td><td>왼쪽 꺾인 괄호</td></tr>
<tr><td>￢</td><td>&amp;#172;</td><td>&amp;not;</td><td>부정</td></tr>
<tr><td>­</td><td>&amp;#173;</td><td>&amp;shy;</td><td>Soft hyphen</td></tr>
<tr><td>?</td><td>&amp;#174;</td><td>&amp;reg;</td><td>등록상표</td></tr>
<tr><td>&amp;hibar;</td><td>&amp;#175;</td><td>&amp;macr;</td><td>Macron accent</td></tr>
<tr><td>°</td><td>&amp;#176;</td><td>&amp;deg;</td><td>Degree sign</td></tr>
<tr><td>±</td><td>&amp;#177;</td><td>&amp;plusmn;</td><td>Plus or minus</td></tr>
<tr><td>²</td><td>&amp;#178;</td><td>&amp;sup2;</td><td>Superscript two</td></tr>
<tr><td>³</td><td>&amp;#179;</td><td>&amp;sup3;</td><td>Superscript three</td></tr>
<tr><td>´</td><td>&amp;#180;</td><td>&amp;acute;</td><td>Acute accent</td></tr>
<tr><td>μ</td><td>&amp;#181;</td><td>&amp;micro;</td><td>Micro sign (Mu)</td></tr>
<tr><td>¶</td><td>&amp;#182;</td><td>&amp;para;</td><td>문단기호</td></tr>
<tr><td>·</td><td>&amp;#183;</td><td>&amp;middot;</td><td>Middle dot</td></tr>
<tr><td>¸</td><td>&amp;#184;</td><td>&amp;cedil;</td><td>Cedilla</td></tr>
<tr><td>¹</td><td>&amp;#185;</td><td>&amp;sup1;</td><td>Superscript one</td></tr>
<tr><td>º</td><td>&amp;#186;</td><td>&amp;ordm;</td><td>Masculine ordinal</td></tr>
<tr><td>≫</td><td>&amp;#187;</td><td>&amp;raquo;</td><td>오른쪽 꺾인 괄호</td></tr>
<tr><td>¼</td><td>&amp;#188;</td><td>&amp;frac14;</td><td>4분의 1</td></tr>
<tr><td>½</td><td>&amp;#189;</td><td>&amp;frac12;</td><td>2분의 1</td></tr>
<tr><td>¾</td><td>&amp;#190;</td><td>&amp;frac34;</td><td>4분의 3</td></tr>
<tr><td>¿</td><td>&amp;#191;</td><td>&amp;iquest;</td><td>거꾸로된 물음표</td></tr>
<tr><td>A</td><td>&amp;#192;</td><td>&amp;Agrave;</td><td>Capital A, grave accent</td></tr>
<tr><td>A</td><td>&amp;#193;</td><td>&amp;Aacute;</td><td>Capital A, acute accent</td></tr>
<tr><td>A</td><td>&amp;#194;</td><td>&amp;Acirc; </td><td>Capital A, circumflex accent</td></tr>
<tr><td>A</td><td>&amp;#195;</td><td>&amp;Atilde;</td><td>Capital A, tilde</td></tr>
<tr><td>A</td><td>&amp;#196;</td><td>&amp;Auml;</td><td>Capital A, dieresis or umlaut mark</td></tr>
<tr><td>A</td><td>&amp;#197;</td><td>&amp;Aring;</td><td>Capital A, ring (Angstrom)</td></tr>
<tr><td>Æ</td><td>&amp;#198;</td><td>&amp;AElig;</td><td>Capital AE diphthong (ligature)</td></tr>
<tr><td>C</td><td>&amp;#199;</td><td>&amp;Ccedil;</td><td>Capital C, cedilla</td></tr>
<tr><td>E</td><td>&amp;#200;</td><td>&amp;Egrave;</td><td>Capital E, grave accent</td></tr>
<tr><td>E</td><td>&amp;#201;</td><td>&amp;Eacute;</td><td>Capital E, acute accent</td></tr>
<tr><td>E</td><td>&amp;#202;</td><td>&amp;Ecirc;</td><td>Capital E, circumflex accent</td></tr>
<tr><td>E</td><td>&amp;#203;</td><td>&amp;Euml;</td><td>Capital E, dieresis or umlaut mark</td></tr>
<tr><td>I</td><td>&amp;#204;</td><td>&amp;Igrave;</td><td>Capital I, grave accent</td></tr>
<tr><td>I</td><td>&amp;#205;</td><td>&amp;Iacute;</td><td>Capital I, acute accent</td></tr>
<tr><td>I</td><td>&amp;#206;</td><td>&amp;Icirc;</td><td>Capital I, circumflex accent</td></tr>
<tr><td>I</td><td>&amp;#207;</td><td>&amp;Iuml;</td><td>Capital I, dieresis or umlaut mark</td></tr>
<tr><td>Ð</td><td>&amp;#208;</td><td>&amp;ETH;</td><td>Capital Eth, Icelandic</td></tr>
<tr><td>N</td><td>&amp;#209;</td><td>&amp;Ntilde;</td><td>Capital N, tilde</td></tr>
<tr><td>O</td><td>&amp;#210;</td><td>&amp;Ograve;</td><td>Capital O, grave accent</td></tr>
<tr><td>O</td><td>&amp;#211;</td><td>&amp;Oacute;</td><td>Capital O, acute accent</td></tr>
<tr><td>O</td><td>&amp;#212;</td><td>&amp;Ocirc;</td><td>Capital O, circumflex accent</td></tr>
<tr><td>O</td><td>&amp;#213;</td><td>&amp;Otilde;</td><td>Capital O, tilde</td></tr>
<tr><td>O</td><td>&amp;#214;</td><td>&amp;Ouml;</td><td>Capital O, dieresis or umlaut mark</td></tr>
<tr><td>×</td><td>&amp;#215;</td><td>&amp;times;</td><td>Multiply sign</td></tr>
<tr><td>Ø</td><td>&amp;#216;</td><td>&amp;Oslash;</td><td>width="130"Capital O, slash</td></tr>
<tr><td>U</td><td>&amp;#217;</td><td>&amp;Ugrave;</td><td>Capital U, grave accent</td></tr>
<tr><td>U</td><td>&amp;#218;</td><td>&amp;Uacute;</td><td>Capital U, acute accent</td></tr>
<tr><td>U</td><td>&amp;#219;</td><td>&amp;Ucirc;</td><td>Capital U, circumflex accent</td></tr>
<tr><td>U</td><td>&amp;#220;</td><td>&amp;Uuml;</td><td>Capital U, dieresis or umlaut mark</td></tr>
<tr><td>Y</td><td>&amp;#221;</td><td>&amp;Yacute;</td><td>Capital Y, acute accent</td></tr>
<tr><td>Þ</td><td>&amp;#222;</td><td>&amp;THORN;</td><td>Capital Thorn, Icelandic</td></tr>
<tr><td>ß</td><td>&amp;#223;</td><td>&amp;szlig;</td><td>Small sharp s, German (sz ligature)</td></tr>
<tr><td>a</td><td>&amp;#224;</td><td>&amp;agrave;</td><td>Small a, grave accent</td></tr>
<tr><td>a</td><td>&amp;#225;</td><td>&amp;aacute;</td><td>Small a, acute accent</td></tr>
<tr><td>a</td><td>&amp;#226;</td><td>&amp;acirc;</td><td>Small a, circumflex accent</td></tr>
<tr><td>a</td><td>&amp;#227;</td><td>&amp;atilde;</td><td>Small a, tilde</td></tr>
<tr><td>a</td><td>&amp;#228;</td><td>&amp;auml;</td><td>Small a, dieresis or umlaut mark</td></tr>
<tr><td>a</td><td>&amp;#229;</td><td>&amp;aring;</td><td>Small a, ring</td></tr>
<tr><td>æ</td><td>&amp;#230;</td><td>&amp;aelig;</td><td>Small ae diphthong (ligature)</td></tr>
<tr><td>c</td><td>&amp;#231;</td><td>&amp;ccedil;</td><td>Small c, cedilla</td></tr>
<tr><td>e</td><td>&amp;#232;</td><td>&amp;egrave;</td><td>Small e, grave accent</td></tr>
<tr><td>e</td><td>&amp;#233;</td><td>&amp;eacute;</td><td>Small e, acute accent</td></tr>
<tr><td>e</td><td>&amp;#234;</td><td>&amp;ecirc;</td><td>Small e, circumflex accent</td></tr>
<tr><td>e</td><td>&amp;#235;</td><td>&amp;euml;</td><td>Small e, dieresis or umlaut mark</td></tr>
<tr><td>i</td><td>&amp;#236;</td><td>&amp;igrave;</td><td>Small i, grave accent</td></tr>
<tr><td>i</td><td>&amp;#237;</td><td>&amp;iacute;</td><td>Small i, acute accent</td></tr>
<tr><td>i</td><td>&amp;#238;</td><td>&amp;icirc;</td><td>Small i, circumflex accent</td></tr>
<tr><td>i</td><td>&amp;#239;</td><td>&amp;iuml;</td><td>Small i, dieresis or umlaut mark</td></tr>
<tr><td>ð</td><td>&amp;#240;</td><td>&amp;eth;</td><td>Small eth, Icelandic</td></tr>
<tr><td>n</td><td>&amp;#241;</td><td>&amp;ntilde;</td><td>Small n, tilde</td></tr>
<tr><td>o</td><td>&amp;#242;</td><td>&amp;ograve;</td><td>Small o, grave accent</td></tr>
<tr><td>o</td><td>&amp;#243;</td><td>&amp;oacute;</td><td>Small o, acute accent</td></tr>
<tr><td>o</td><td>&amp;#244;</td><td>&amp;ocirc;</td><td>Small o, circumflex accent</td></tr>
<tr><td>o</td><td>&amp;#245;</td><td>&amp;otilde;</td><td>Small o, tilde</td></tr>
<tr><td>o</td><td>&amp;#246;</td><td>&amp;ouml;</td><td>Small o, dieresis or umlaut mark</td></tr>
<tr><td>÷</td><td>&amp;#247;</td><td>&amp;divide;</td><td>Division sign</td></tr>
<tr><td>ø</td><td>&amp;#248;</td><td>&amp;oslash;</td><td>Small o, slash</td></tr>
<tr><td>u</td><td>&amp;#249;</td><td>&amp;ugrave;</td><td>Small u, grave accent</td></tr>
<tr><td>u</td><td>&amp;#250;</td><td>&amp;uacute;</td><td>Small u, acute accent</td></tr>
<tr><td>u</td><td>&amp;#251;</td><td>&amp;ucirc;</td><td>Small u, circumflex accent</td></tr>
<tr><td>u</td><td>&amp;#252;</td><td>&amp;uuml;</td><td>Small u, dieresis or umlaut mark</td></tr>
<tr><td>y</td><td>&amp;#253;</td><td>&amp;yacute;</td><td>Small y, acute accent</td></tr>
<tr><td>þ</td><td>&amp;#254;</td><td>&amp;thorn;</td><td>Small thorn, Icelandic</td></tr>
<tr><td>y</td><td>&amp;#255;</td><td>&amp;yuml;</td><td>Small y, dieresis or umlaut mark</td></tr>
</tbody></table>
</p>
