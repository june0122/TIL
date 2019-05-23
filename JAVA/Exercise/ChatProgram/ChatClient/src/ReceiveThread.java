import java.io.IOException;
import java.io.InputStream;

public class ReceiveThread extends Thread {
    private InputStream is;

    ReceiveThread(InputStream is) {
        this.is = is;
    }

    @Override
    public void run() {
        byte[] buf = new byte[512];
        try {
            while (true) {
                int len = is.read(buf);
                if (len == -1) {
                    break;
                }
                System.out.println("From Server : " + new String(buf, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}