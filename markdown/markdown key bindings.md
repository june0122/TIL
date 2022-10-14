# 마크다운 기깔나게 써보자! 😎

## VS CODE의 단축키 설정: `cmd + shift + p` → `기본 설정: 바로 가기 키 열기(JSON)` 

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/113880311/195754602-f09f4004-8302-43c2-a86f-647c9dfacf5d.png'>
</p>

```json
[
    {
    "key": "ctrl+cmd+shift+up",
    "command": "editor.action.insertSnippet",
    "args": {
    "snippet": " <sup>$1</sup>$0"
    },
    "when": "editorTextFocus"
    },
    
    {
    "key": "ctrl+cmd+shift+down",
    "command": "editor.action.insertSnippet",
    "args": {
    "snippet": " <sub>$1</sub>$0"
    },
    "when": "editorTextFocus"
    },

    ...
```

## 1. 수학 공식, 제곱을 표현하고 싶어!

#### 위첨자<small>(superscript)</small>: ctrl + cmd + shift + ↑

```html
(a+b)<sup>2</sup> = a<sup>2</sup> + 2ab + b<sup>2</sup>
```

- (a+b)<sup>2</sup> = a<sup>2</sup> + 2ab + b<sup>2</sup>

#### 아래첨자<small>(subscript)</small>: ctrl + cmd + shift + ↓

```html
H<sub>2</sub>O
```

- H<sub>2</sub>O

## 2. 이미지를 간편하게 커스텀 해보자!

#### 간단한 이미지 추가

```md
![이미지 내용](URL)
```

#### 커스텀한 이미지 추가: ctrl + cmd + shift + i <small>(image)</small>

```html
<p align = 'center'>
<img width = '' src = ''>
</p>
```

#### 이미지에 주석도 달고 싶어! : ctrl + cmd + shift + f <small>(figure)</small>

```html
<figure>
 <img width = '' src = ''>
 <figcaption>
 
 </figcaption>
</figure>
```

#### 마크다운의 줄바꿈 : ctrl + cmd + shift + l <small>(line break)</small>

마크다운에서는 줄과 줄 사이에 몇 줄이 존재하든 무조건 1줄만 띄워집니다.

가독성을 위해 줄을 많이 띄우고 싶을 때가 있는데 이게 은근 불편하답니다.

## 3. 특정 언어의 코드 블럭을 편하게 생성하고 싶어!

#### 기본 코드 블럭 : ctrl + cmd + shift + c <small>(code)</small>

#### 코틀린 코드 블럭 : ctrl + cmd + shift + k <small>(kotlin)</small>

#### 자바 코드 블럭 : ctrl + cmd + shift + j <small>(java)</small>

#### 스위프트 코드 블럭 : ctrl + cmd + shift + s <small>(swift)</small>

## 4. 중간 팁: 일일이 커서를 누르는건 불편해!

[[Android] Do not concatenate text displayed with setText](https://june0122.github.io/2021/05/18/android-memo-text-concatenatation/)

서식 인수의 argument index가 `1$`, `2$`로 표시되는 것처럼 VS Code의 단축키를 지정할 때 keybindings.json 파일에서 키보드의 커서가 위치할 지점을 custom snippet으로 `$1`, `$2`와 같이 지정할 수 있습니다.

```json
{
"key": "ctrl+cmd+shift+f",
"command": "editor.action.insertSnippet",
"args": {
"snippet": "<figure>\n <img width = '$1' src = '$2'>\n <figcaption>\n $3\n </figcaption>\n</figure>\n$0"
},
"when": "editorTextFocus"
},
```

## 5. 활용 예시: 알고리즘/코딩테스트 공부 정리

#### LeetCode : ctrl + cmd + shift + 8

- 예시 : https://github.com/june0122/algorithm_study/blob/master/LeetCode/1971.md

#### 백준 : ctrl + cmd + shift + 0

- 예시 : https://github.com/june0122/algorithm_study/blob/master/BOJ/1927.md

#### 프로그래머스 : ctrl + cmd + shift + -

## 6. 마크다운에서도 주석을!

#### ctrl + cmd + shift + f <small>(footnotes)</small>

- 각주<small>(footnotes)</small>: 각주를 넣을 단어가 있는 페이지의 하단에 위치
- 미주<small>(endnotes)</small>: 미주의 위치에 상관 없이 문서의 끝에 위치

```html
주석(미주)을 달고 싶은 단어<sup id = "a1">[주석 번호](#f1)</sup>

<b id = "f1"><sup> 주석 번호 </sup></b> 주석에 대한 내용 [ ↩](#a1)
```

- 예시 링크: https://june0122.github.io/2021/05/29/android-bnr-14/#f1

## 7. 자잘한 예시들

#### 빅 오 표시 : ctrl + cmd + shift + o

```
<i>O()</i>
```

#### 작은 괄호 : ctrl + cmd + shift + s

```
<small>()</small>
```

#### 말줄임표 : ctrl + cmd + shift + .

`…`

#### 홑화살괄호 `<>` : ctrl + cmd + shift + ,

- 마크다운을 작성하다보면 홑화살괄호가 이스케이프 처리가 되는 경우가 있어 html 태그로 작성해줘야하는 경우가 있습니다.

```
&lt;&gt;
```
