import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

class SessionThread extends Thread {
    private Socket clientSocket;
    private List<SessionThread> sessions;

    SessionThread(Socket clientSocket, List<SessionThread> sessions) {
        this.clientSocket = clientSocket;
        this.sessions = sessions;
        this.sessions.add(this);
    }

    private void broadcast(String message) throws IOException {
        for (SessionThread e : sessions) {
            OutputStream os = e.clientSocket.getOutputStream();
            os.write(message.getBytes());
        }
    }

    @Override
    public void run() {
        try (InputStream is = clientSocket.getInputStream()) {

            while (true) {
                byte[] buf = new byte[512];
                int len = is.read(buf);
                if (len == -1) {
                    System.out.println("Disconnected");
                    break;
                }
                String message = new String(buf, 0, len);
                broadcast(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Thread Terminated");
        sessions.remove(this);
    }
}
