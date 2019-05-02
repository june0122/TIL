# TCP/IP Socket

> ## TCP/IP 통신 함수 사용 순서

```
------------------------------------------
      [SERVER]      |       [CLIENT]
------------------------------------------
      socket()      |
         ↓          |
       bind()       |       socket()
         ↓          |          |
      listen()      |          |
         ↓          |          ↓
      accept()  ←---------  connect()
         ↓          |          ↓
   read()/write() ←---→  read()/write()
         ↓          |          ↓
       close()      |        close()
------------------------------------------
```

<br>

## socket() : 소켓 생성

> ### socket() 함수는 소켓을 생성하여 반환한다.

### 헤더 

- **#include** <sys/types.h>
- **#include** <sys/socket.h>

### 형태

- **int** socket(**int** domain, **int** type, **int** protocol);

### 인수

- **int** domain : 인터넷을 통해 통신할 지, 같은 시스템 내에서 프로세스끼리 통신할 지의 여부를 설정.

|domain|domain 내용|
|---|---|
|PF_INET, AF_INET|IPv4 인터넷 프로토콜을 사용|
|PF_INET6|IPv6 인터넷 프로토콜을 사용|
|PF_LOCAL, AF_UNIX|같은 시스템 내에서 프로세스끼리 통신|
|PF_PACKET|Low level socket을 인터페이스를 이용|
|PF_IPX|IPX 노벨 프로토콜을 사용|

- **int** type : 데이터의 전송 형태를 지정하며 아래와 같은 값을 사용할 수 있다.
  
|type|type 내용|
|---|---|
|SOCK_STREAM|TCP/IP 프로토콜을 이용|
|SOCK_DGRAM|UDP/IP 프로토콜을 이용|

- **int** protocol : 통신에 있어 특정 프로토콜 사용을 지정하기 하기 위한 변수이며, 보통 0값을 사용

### 반환

- -1 이외 : 소켓 식별자

- -1     : 실패

<br>

## bind() : 소켓에 IP 주소와 포트번호를 지정

> ### bind() 함수는 소켓에 IP 주소와 포트번호를 지정해준다. 이로써 소켓을 통신에 사용할 수 있도록 준비가 된다.

### 헤더

- **#include** <sys/types.h>
- **#include** <sys/socket.h>

### 형태

- **int** bind(**int** sockfd, **struct** sockaddr* myaddr, socklen_t addrlen);

### 인수

- **int** sockfd : 소켓 [**디스크립터**](#f1)<sup id = "a1">[ 1](#f1)</sup>



- **struct** sockaddr* myaddr : 주소 정보로 인터넷을 이용하는 **AF_INET**인지, 시스탬 내에서 통신하는 **AF_UNIX**에 따라서 달라진다.
  
  1. 인터넷을 통해 통신하는 **AF_INET**인 경우에는 `struct sockaddr_in`을 사용한다.

        ```c
        struct sockaddr_in {
        sa_family_t		sin_family;       // Address family	
        unsigned short int	sin_port          // Port number			
        struct in_addr	sin_addr;                 // Internet address		
        
        /* Pad to size of ´struct sockaddr'. */
        unsigned char  __pad[__SOCK_SIZE__ - sizeof(short int) -
        sizeof(unsigned short int) - sizeof(struct in_addr)];
        };
        ```

  2. 시스템 내부 통신인 **AF_UNIX**인 경우에는 `struct sockaddr`을 사용한다.

        ```c
        struct sockaddr {
        sa_family_t  sa_family;   // address family, AF_xxx	
        char        sa_data[14];  // 14 bytes of protocol address	
        };
        ```

- socklen_t addrlen : myaddr 구조체의 크기

### 반환

- 0 : 성공

- -1 : 실패

<br>

## listen() : 클라이언트 접속 요청을 받을 수 있도록 설정

> ### listen() 함수는 소켓을 통해 클라이언트의 접속 요청을 기다리도록 설정한다.

### 헤더

- **#include** <sys/socket.h>

### 형태

- **int** listen(int s, int backlog);

### 인수

- **int** s	: 소켓 디스크립터

- **int** backlog : 대기 메시지 큐의 개수

  - backlog 의 최대값은 **<sys/socket.h>** 에 정의된 `SOMAXCONN` 을 참조한다. 아래와 같이 `sysctl` 명령어를 이용하면 값을 확인하고 원하는 개수로 수정할 수 있다. 참고로 `SOMAXCONN` 은 **socket max connection** 의 약어이다. 아래는 각 운영체제 별로 `SOMAXCONN` 값을 확인할 수 있는 방법이다.

<br>

  |OS|Command to specify the backlog|Notes|
  |---|---|---|
  |AIX|/usr/sbin/no -o somaxconn=1024|Default = 1024|
  |Solaris|/usr/sbin/ndd -set /dev/tcp<br>tcp_conn_req_max_q 1024|Default = 128|
  |Windows|HKLM<br>&nbsp;\System<br>&nbsp;&nbsp;\CurrentControlSet<br>&nbsp;&nbsp;&nbsp;\Services<br>&nbsp;&nbsp;&nbsp;\NetBt<br>&nbsp;&nbsp;&nbsp;&nbsp;\Parameters<br>MaxConnBacklog (REG_DWORD)|Default=1000|
  |Linux Kernel 2.2|# SOMAXCONN 값 확인하기<br>/sbin/sysctl -a &#124; grep soma<br><br># SOMAXCONN 값 변경하기<br>/sbin/sysctl -w net.core.somaxconn = 2048 |메모리가 128MB 보다 작을 경우 128이 기본이고, 클 경우 1024가 기본이다.| 

### 반환

- 0 : 성공

- -1 : 실패

<br>

## connect() : 서버로 접속 요청

> ### connect() 함수는 생성한 소켓을 통해 서버로 접속을 요청한다.

### 헤더

- **#include** <sys/types.h> 

- **#include** <sys/socket.h>

### 형태

- **int** connect(**int** sockfd, **const struct** sockaddr *serv_addr, socklen_t addrlen);

### 인수

- **int** sockfd	            : 소켓 디스크립터
  
- **struct** sockaddr *serv_addr    : 서버 주소 정보에 대한 포인터
  
- socklen_t addrlen	            : struct sockaddr *serv_addr 포인터가 가르키는 구조체의 크기

### 반환

- 0 : 성공

- -1 : 실패

<br>

## accept() : 클라이언트 접속 요청 수락

> ### accept() 함수는 클라이언트의 접속 요청을 받아들이고 클라이언트와 통신하는 전용 소켓을 생성한다.

### 헤더

- **#include** <sys/types.h> 

- **#include** <sys/socket.h>

### 형태

- **int** accept(**int** s, **struct** sockaddr *addr, socklen_t *addrlen);

### 인수

- **int** s	: 소켓 디스크립터

- **struct** sockaddr *addr	: 클라이언트 주소 정보를 가지고 있는 포인터

- socklen_t addrlen	: struct sockaddr *addr 포인터가 가르키는 구조체의 크기

### 반환

- -1 이외	: 새로운 소켓 디스크립터

- -1	: 실패


<br>


> ## Echo Server in C

> ### SocketServer.c

```c
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>

#include <stdio.h>
#include <string.h>


// process of making server

// 1. socket
// 2. bind
// 3. listen
// 4. accept

int main() {
        int sd = socket(PF_INET, SOCK_STREAM, 0);  // int socket(int domain, int type, int protocol);
        if (sd == -1) {
                perror("socket");
                return 1;
        }

        struct sockaddr_in saddr = {0, };
        saddr.sin_family = AF_INET;
        saddr.sin_addr.s_addr = INADDR_ANY;
        saddr.sin_port = htons(5000);
        bind(sd, (struct sockaddr*)&saddr, sizeof saddr);

        listen(sd, SOMAXCONN);

        struct sockaddr_in caddr = {0, };
        socklen_t caddrlen = sizeof caddr;

        int sock = accept(sd, (struct sockaddr *)&caddr, &caddrlen);
        printf("%s\n", inet_ntoa(caddr.sin_addr));

        while(1) {
                char buf[128];
                int ret = read(sock, buf, sizeof buf);
                if (ret <= 0) {  // EOF
                        break;
                }
                write(sock, buf, ret);
        }
        close(sock);
        close(sd);
}
```

> ### SocketClient.c

```c
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#include <stdio.h>

int main() {
        int sock = socket(PF_INET, SOCK_STREAM, 0);
        if (sock == -1) {
                perror("socket");
                return 1;
        }

        struct sockaddr_in saddr = {0, };
        saddr.sin_family = AF_INET;
        saddr.sin_addr.s_addr = inet_addr("192.168.56.102"); // 192.168.11.20
        saddr.sin_port = htons(5000);
        if (connect(sock, (struct sockaddr *)&saddr, sizeof saddr) == -1) {
                perror("connect");
                return 1;
                }

                char buf[128];
                while (1) {
                        int ret = read(0, buf, sizeof buf);
                        write(sock, buf, ret);

                        ret = read(sock, buf, sizeof buf);
                        if (ret <= 0)
                                break;

                                write(1, buf, ret);
                }
}
```

<br>

> ## EchoServer

> ### EchoServer.class

```java
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) {
        /*
        ServerSocket 클래스의 생성자 세부사항을 보면 오버로딩된 생성자 중 아래와 같은 형태가 있다.
        public ServerSocket(int port, int backlog, InetAddress bindAddr)
          즉 ServerSocket 클래스는
             1. 소켓을 지정하는 socket()
             2. IP 주소와 포트번호를 지정하는 bind()
             3. 클라이언트의 접속 요청을 받는 listen()
           3개의 역할을 수행한다.
        */
        try {
            ServerSocket serverSocket = new ServerSocket(5000);

            while (true) {
                Socket socket = serverSocket.accept();
                SessionThread sessionThread = new SessionThread(socket);
                sessionThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

> ### SessionThread.class

```java
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SessionThread extends Thread {
    private Socket socket;

    public SessionThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (OutputStream os = socket.getOutputStream();
             InputStream is = socket.getInputStream();) {

            while (true) {
                byte[] buf = new byte[512];
                int len = is.read(buf);
                // read() 함수가 -1 을 반환할 때는
                // stream의 끝에 도달했을 때, 더 이상 데이터가 없을 때이다.
                if (len == -1) {
                    System.out.println("Disconnected");
                    break;
                }
                os.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Thread has terminated");
    }
}
```

> ## EchoClient

> ### EchoClient.class

```java
```


---

## <b id = "f1"><sup>1</sup></b> 파일 디스크립터(File Descriptor) [ ↩](#a1)

> ### 운영체제가 만든 파일 또는 소켓을 지칭하기 위해 부여한 숫자

- 파일을 관리하기 위한 운영체제가 필요로 하는 파일의 정보를 가지고 있다.<br> **FCB**(File Control Block) 이라고 하며 다음의 정보를 가진다.

  1) 파일 이름

  2) 보조 기억 장치에서의 파일 위치

  3) 파일 구조 : 순차 파일, 색인 순차 파일, 색인 파일

  4) 액세스 제어 정보 

  5) 파일 유형

  6) 생성 날짜와 시간, 제거 날짜와 시간

  7) 최종 수정 날짜 및 시간

  8) 액세스한 횟수

- 파일 디스크립터는 **정수형으로 차례로 부여**되고 **0, 1, 2는 이미 할당**되어 있으므로 3부터 부여된다.
  
  |파일 디스크립터|대상|
  |:---:|:---:|
  |0|표준 입력 : Standard Input|
  |1|표준 출력 : Standard Output|
  |2|표준 에러 : Standard Error|

### 파일 디스크립터와 소켓

- 리눅스 환경에서는 **소켓 조작과 파일 조작이 동일하게 간주**되기 때문에 자세히 알 필요가 있다.

- 리눅스는 **소켓을 파일의 일종으로 구분**하여 파일 입출력 함수를 소켓 데이터 송수신에 사용할 수 있다.

### 리눅스 환경에서 소켓 & 파일 입출력의 파일 디스크립터 예시

> descriptor.c

```c
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>

int main() {
        int fd1, fd2, fd3;

        fd1 = socket(PF_INET, SOCK_STREAM, 0);
        fd2 = open("test.dat", O_CREAT | O_WRONLY | O_TRUNC);
        fd3 = socket(PF_INET, SOCK_DGRAM, 0);

        printf("file descriptor 1: %d\n", fd1);
        printf("file descriptor 2: %d\n", fd2);
        printf("file descriptor 3: %d\n", fd3);

        close(fd1);
        close(fd2);
        close(fd3);

        return 0;
}
```

> 실행 결과

```c
file descriptor 1: 3
file descriptor 2: 4
file descriptor 3: 5
```

- 실행결과에서 보이듯이 파일 입출력 함수인 `open()`과 `socket()` 둘 중 어떤 것을 써도 파일 디스크립터가 순차적으로 증가하는 것을 알 수 있다.
