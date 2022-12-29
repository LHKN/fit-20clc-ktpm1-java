import java.io.*;
import java.net.*;
import javax.swing.JTree;

public class ClientThread implements Runnable {
    private static String name;
    private static int port;
    private static Socket s;
    private static boolean connecting = false;

    public static Thread t;
    public static File selectedDirectory;

    private BufferedReader br;
    private BufferedWriter bw;
    private DataInputStream dis;

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

    public void setDirectory(File directory) {
        selectedDirectory = directory;
    }

    public void run() {
        try {
            dis = new DataInputStream(s.getInputStream());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (connecting) {
        
        }

        try {
            dis.close();
            br.close();
            bw.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String manageClient() throws IOException, ClassNotFoundException{
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

    public String receiveFromClient() throws IOException, ClassNotFoundException {
        try {
            String receivedMessage = br.readLine();
            if (receivedMessage.equals("SELECTED_DIRECTORY")){
				//receive dir tree from client
				receiveDirectory();                
			}
            return receivedMessage;
        } catch (IOException e) {
            return null;
        }
    }

    public JTree receiveDirectory() throws IOException, ClassNotFoundException { // wip
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        return (JTree)ois.readObject();
    }
}