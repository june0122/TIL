import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket("127.0.0.1", 3000);
            ReceiveThread receiveThread = new ReceiveThread(socket.getInputStream());
            receiveThread.start();

            try (OutputStream os = socket.getOutputStream()) {
                while (scanner.hasNext()) {
                    String line = scanner.next();
                    os.write(line.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class Client {
//    public static void main(String[] args) {
//        try {
//            Scanner scanner = new Scanner(System.in);
//
//            // 1. socket()  : socket 하나를 생성
//            // 2. connect() : server로 연결 시도
//            Socket socket = new Socket("127.0.0.1", 3000);
//            ReceiveThread receiveThread = new ReceiveThread(socket.getInputStream());
//            receiveThread.start();
//
//            // write() 접속에 성공하면 데이터를 전송
//            try (OutputStream os = socket.getOutputStream()) {
//                while (scanner.hasNext()) {
//                    String line = scanner.next();
//                    os.write(line.getBytes());
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}