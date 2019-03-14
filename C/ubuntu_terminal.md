Platform : 기반이 되는 환경

Kernel

- 알맹이
- OS 프로그램

Linux Platform : 리누스 토발즈 → 리눅스 / Git 개발 (성당과 시장 모델에 비유)

- 리눅스는 **오픈소스** 로 인해 엄청난 발전을 하였다.


#### 오픈소스 License
- GPL v2/v3
- BSD
- Apache
- MIT
- WTFPL

FSF - 자유 소프트웨어 재단 (**리차드 스톨만** : 해커)

GNU 오픈소스 소프트웨어 제공

→ GCC, emacs(with 제임스 고슬링)


### Linux Kernel (kernel.org)

- 배포판
  - Redhat
    - Redhat Enterprise
    - Fedora
    - Centos (네이버가 주로 사용)
    - Amazon Linux
  - Debian
    - Debian Linux
  - Canonical
    - Ubuntu


### Ubuntu

    매년 4월과 10월 새로운 버전을 발표함.

    2년동안 Support 지원 → LTS(Long Term Support) 4년

    LTS 버전을 2년 주기로 매년 4월 제공한다.

    18.04 LTS ver.


### 가상화(Virtualization)

CPU - mode

```
  0 (Supervisor), 1, 2, 3 (User)
                 ↓
  0 (Hypervisor), 1 (Supervisor), 2, 3 (User)
```

Virtual Machine
- Virtual Box(Open)
- Vmware
- Paralles


### 터미널 명령어

> Linux(Unix) : root

shutdown -f now

ifconfig

apt-get / apt : package manager

`sudo (switching user doing)`

sudo apt-get update

-y : 디폴트로 yes

env

cd : change directory

`.` : 현재 작업 디렉토리

echo

ls : 디렉토리 내 파일 리스트 출력

/etc : 리눅스 설정에 필요한 파일들을 모아놓은 디렉토리

현재 로그인한 계정 확인 : echo $USER

sudo su root : 강제로 루트 권한으로 스위치

cd
mkdir
rmdir

PWD(Present Working Directory)

```
$ : 프롬프트

$ ls : 사용자 계정

# ls : root 계정
```


### Editor
  - vim ★
  - emacs


1. 명령 모드
  - 입력 모드 → 명령 모드 : ESC
  - 커서 이동 방법 : h(Left) / l(Right) / k(Up) / j(down)


  > 명령 + 범위

```
명령

  y : 복사
  d : 잘라내기
  p : 붙여넣기
  u : Undo
  Ctrl + R : Redo

  yy, dd → 한 줄

범위

  w : 단어
  ^ : 시작
  $ : 끝
  gg : 문서의 시작
  G: 문서의 끝

```

2. 입력 모드
  - 명령 모드에서 입력 모드 전환하는 방법 : i, a, o, O


3. ex 명령 모드
  - 명령모드에서 `:`을 치면 된다.

```
w : 저장
q : 종료
```

vi 설정을 통해 개인화할 수 있습니다.
→ `~/.vimrc`

