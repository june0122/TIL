import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        List<SessionThread> sessions = new ArrayList<>();

        try {
            // 1. socket() : socket 하나를 생성
            // 2. bind()   : 생성한 socket을 server socket으로 등록
            // 3. listen() : server socket을 통해 클라이언트의 접속 요청을 확인하도록 설정
            ServerSocket serverSocket = new ServerSocket(3000);

            while (true) {
                // 4. accept() : 클라이언트 접속 요청 대기 및 허락
                // accept()로 접속 요청을 허락하게 되면 클라이언트와 통신을 하기 위해서 커널이 자동으로 소켓을 생성
                // 새로 생성한 socket을 client socket이라고 하자.
                Socket clientSocket = serverSocket.accept();

                // 5. read(), write()
                SessionThread sessionThread = new SessionThread(clientSocket, sessions);
                sessionThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class Server {
//    public static void main(String[] args) throws IOException {
//        List<SessionThread> sessions = new ArrayList<>();
//
//        try {
//            // 서버 소켓을 생성 후, 3000번 포트와 결합(bind) 시킨다.
//            ServerSocket serverSocket = new ServerSocket(3000);
//            System.out.println(getTime() + "서버 준비 완료");
//
//            while (true) {
//                System.out.println(getTime() + "연결 요청 대기");
//                // 클라이언트의 연결 요청이 올 때까지 서버 소켓은 실행을 멈추고 대기
//                // 클라이언트의 연결 요청이 오면 클라이언트 소켓과 통신할 새로운 소켓을 생성
//                Socket socket = serverSocket.accept();
//                System.out.println(getTime() + socket.getInetAddress() + "로 부터 연결 요청");
//                SessionThread sessionThread = new SessionThread(socket, sessions);
//                sessionThread.start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    static String getTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("[hh:mm:ss] ");
//        return dateFormat.format(new Date());
//    }
//}