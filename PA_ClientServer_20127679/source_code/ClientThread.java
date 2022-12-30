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
    public static JTree tree = null;

    private BufferedReader br;
    private BufferedWriter bw;

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

    public JTree myTree() {
        return tree;
    }

    public void setConnection(boolean check) {
        connecting = check;
    }

    public void setDirectory(File directory) {
        selectedDirectory = directory;
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
				//receiveDirectory();

                System.out.println("pathDirectory4: " + selectedDirectory);
                return "SELECTED_DIRECTORY";           
			}
            return receivedMessage;
        } catch (IOException e) {
            return null;
        }
    }

    public void receiveDirectory() throws IOException, ClassNotFoundException { // wip
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        tree = (JTree)ois.readObject();
        System.out.println("tree: " + tree);
    }
}