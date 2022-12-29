import java.io.*;
import java.net.*;

public class ClientThread implements Runnable {
    private static String name;
    private static int port;
    private static Socket s;
    private static boolean connecting = false;

    public static Thread t;

    private static BufferedReader br;
    private static BufferedWriter bw;

    ClientThread(String name, int port, Socket s) {
        ClientThread.name = name;
        ClientThread.port = port;
        ClientThread.s = s;
        t = new Thread(this, name);
    }

    public void startClient() {
        connecting = true;
        t.start();
    }

    public boolean checkConnection() {
        return connecting;
    }

    public void setConnection(boolean check) {
        connecting = check;
    }

    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (connecting) {
        
        }

        try {
            br.close();
            bw.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String manageClient() {
        String rm = receiveFromClient();
        if (rm == null || rm.equals("CLIENT_DISCONNECT")) {
            setConnection(false);
            return null;
        } else {
            return rm;
        }
    }

    public boolean sendToClient(String message) {
        try {
            String sentMessage = message;

            bw.write(sentMessage);
            bw.newLine();
            bw.flush();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String receiveFromClient() {
        try {
            String receivedMessage = br.readLine();
            return receivedMessage;
        } catch (IOException e) {
            return null;
        }
    }

    public String receiveDirectory() { // wip
        try {
            String receivedMessage = br.readLine();
            return receivedMessage;
        } catch (IOException e) {
            return null;
        }
    }
}