import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client implements ItemListener {
	static final int PORT = 3200;

	public static String pathDirectory = "";

	public static Socket s;
	private static Client c;
	private static boolean connecting = false;

	private static BufferedReader br;
	private static BufferedWriter bw;

	private static JFrame noti = new JFrame("Notification from Client");

	Client(){
		c = this;
	}

	public Socket getSocket() {
		return s;
	}

	public boolean checkConnection() {
		return connecting;
	}


	public static void main(String args[]) throws IOException {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		// set Container
		JFrame.setDefaultLookAndFeelDecorated(true);

		JFrame frame = new JFrame("Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setResizable(false);

		Client c = new Client();
		c.addComponentToPane(frame.getContentPane());

		frame.pack();
		frame.setVisible(true);
	}

	public void itemStateChanged(ItemEvent evt) {
		// CardLayout cl = (CardLayout)(cards.getLayout());
		// cl.show(cards, (String)evt.getItem());
	}

	public void addComponentToPane(Container pane) {
			// CardLayout
		JPanel cards = new JPanel(new CardLayout());

			// card: CONNECT
		// button
		JPanel connect = new JPanel();
		JButton connect_btn = new JButton("CONNECT");
		connect_btn.setMaximumSize(new Dimension(200, 50));
		connect.add(connect_btn);

		cards.add(connect, "connect");

			// card: CONNECTED
		JPanel connected = new JPanel();
			
		//connected: sendMessage panel
		JPanel sendMessage = new JPanel();
		JTextField sendMessage_tf = new JTextField(15);
		sendMessage_tf.setMaximumSize(new Dimension(200,30));
		sendMessage.add(sendMessage_tf);

		JButton sendMessage_btn = new JButton("SEND");
		sendMessage_btn.setMaximumSize(new Dimension(200, 50));
		sendMessage.add(sendMessage_btn);


		//connected: receive label
		JTextArea receiveMessage_ta = new JTextArea(10,10);
		receiveMessage_ta.setLineWrap(true);
		receiveMessage_ta.setWrapStyleWord(true);
		receiveMessage_ta.setEditable(false);
		receiveMessage_ta.setText("WELCOME TO SERVER CHAT\n");

		JScrollPane receiveMessage = new JScrollPane(receiveMessage_ta);

		//connected: button
		JButton disconnect_btn = new JButton("DISCONNECT");
		disconnect_btn.setMaximumSize(new Dimension(200, 50));
		
		disconnect_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				disconnectFromServer();
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, "connect");
			}
		});

		connected.add(sendMessage);
		connected.add(receiveMessage);
		connected.add(disconnect_btn);
        connected.setLayout(new BoxLayout(connected, BoxLayout.Y_AXIS));

		cards.add(connected, "connected");

		pane.add(cards, BorderLayout.SOUTH);


		// override buttons
		
		sendMessage_btn.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent arg0) {
				String sm = sendMessage_tf.getText();

				if (sm.equals("")) 
					return;

				if (sendToServer(sm)){
					receiveMessage_ta.setText(receiveMessage_ta.getText()+"Client: "+sm+"\n");
				}
			}
		});

		connect_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (connectToServer()) {
					connecting = true;

					CardLayout cl = (CardLayout) (cards.getLayout());
					cl.show(cards, "connected");

					JOptionPane.showMessageDialog(noti, "Connected to server! (OwO )", "Client", JOptionPane.INFORMATION_MESSAGE);

					new Thread(new Runnable() {
						public void run() {
							while (connecting){
								if(s.isClosed()||s==null){
									connecting = false;
									break;
								}
								String rm = receiveFromServer();
								if(!rm.equals("SELECTED_DIRECTORY")){
									if(!rm.equals("") && rm != null){
										receiveMessage_ta.setText(receiveMessage_ta.getText()+"Server: "+rm+"\n");
									}
									else {
										connecting = false;
										break;
									}
								}
							}
							JOptionPane.showMessageDialog(noti, "Disconnected from server! (OwO )", "Client", JOptionPane.INFORMATION_MESSAGE);
							CardLayout cl = (CardLayout) (cards.getLayout());
							cl.show(cards, "connect");
						}
					}).start();
					new Thread(new ClientFile(c)).start();
				}
			}
		});
	}

	public static boolean connectToServer() {
		try {
			// initialize variables
			// server's socket and IO streams
			s = new Socket("localhost", PORT);

			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(noti, "Server unavailable! <0A0 >", "Client", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
	}

	public static void disconnectFromServer() {
		c.sendToServer("CLIENT_DISCONNECT");

		try {
			bw.close();
			br.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean sendToServer(String message) { //
		try {
			bw.write(message);
			bw.newLine();
			bw.flush();

			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static String receiveFromServer() { //
		try {
			String receivedMessage = br.readLine();
			if (receivedMessage.equals("SELECTED_DIRECTORY")){
				pathDirectory = br.readLine();
				//send dir tree to server
				System.out.println("pathDirectory: "+pathDirectory);
			}
			return receivedMessage;
		} catch (IOException e) {
			return "";
		}
	}
}