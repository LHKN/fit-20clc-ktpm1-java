import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client implements ItemListener {
	static final int PORT = 3200;

	private static String pathDirectory;

	private Socket s;
	private Client c;
	private boolean connecting = false;

	private BufferedReader br;
	private BufferedWriter bw;
	private DataOutputStream dos;

	private static JFrame noti = new JFrame("Notification from Client");

	Client() {
		c = this;
	}

	public String getDirectory() {
		return pathDirectory;
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

		// connected: sendMessage panel
		JPanel sendMessage = new JPanel();
		JTextField sendMessage_tf = new JTextField(15);
		sendMessage_tf.setMaximumSize(new Dimension(200, 30));
		sendMessage.add(sendMessage_tf);

		JButton sendMessage_btn = new JButton("SEND");
		sendMessage_btn.setMaximumSize(new Dimension(200, 50));
		sendMessage.add(sendMessage_btn);

		// connected: receive label
		JTextArea receiveMessage_ta = new JTextArea(10, 10);
		receiveMessage_ta.setLineWrap(true);
		receiveMessage_ta.setWrapStyleWord(true);
		receiveMessage_ta.setEditable(false);
		receiveMessage_ta.setText("WELCOME TO SERVER CHAT\n");

		JScrollPane receiveMessage = new JScrollPane(receiveMessage_ta);

		// connected: button
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

				if (sendToServer(sm)) {
					receiveMessage_ta.setText(receiveMessage_ta.getText() + "Client: " + sm + "\n");
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

					JOptionPane.showMessageDialog(noti, "Connected to server! (OwO )", "Client",
							JOptionPane.INFORMATION_MESSAGE);

					new Thread(new Runnable() {
						public void run() {
							while (connecting) {
								if (s == null || s.isClosed()) {
									connecting = false;
									break;
								}
								String rm = receiveFromServer();
								if (rm == null || rm.equals("")) {
									connecting = false;
									break;
								}
								else if (!rm.equals("SELECTED_DIRECTORY")) {
									receiveMessage_ta.setText(receiveMessage_ta.getText() + "Server: " + rm + "\n");
								}
							}
							JOptionPane.showMessageDialog(noti, "Disconnected from server! (OwO )", "Client",
									JOptionPane.INFORMATION_MESSAGE);
							CardLayout cl = (CardLayout) (cards.getLayout());
							cl.show(cards, "connect");
						}
					}).start();
				}
			}
		});
	}

	public boolean connectToServer() {
		try {
			// initialize variables
			// server's socket and IO streams
			if (s != null) {
				s.close();
			}

			s = new Socket("localhost", PORT);

			br = new BufferedReader(new InputStreamReader(s.getInputStream()));

			dos = new DataOutputStream(s.getOutputStream());
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(noti, "Server unavailable! <0A0 >", "Client",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
	}

	public void disconnectFromServer() {
		c.sendToServer("CLIENT_DISCONNECT");

		try {
			dos.close();
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

	public String receiveFromServer() { //
		try {
			String receivedMessage = br.readLine();
			if (receivedMessage == null)
				return null;
			if (receivedMessage.equals("SELECTED_DIRECTORY")) {
				String tempPathDirectory = br.readLine();
				// File tempPathDirectory = oos.readObject();
				if (!tempPathDirectory.equals(pathDirectory)) {
					pathDirectory = tempPathDirectory;

					// send dir tree to server
					sendDirectoryTree();

					// send notifications to server
					new Thread(new ClientFile(c)).start();

					System.out.println("pathDirectory1: " + pathDirectory);
				}
				return "SELECTED_DIRECTORY";
			}
			return receivedMessage;
		} catch (IOException e) {
			return null;
		}
	}

	public void sendDirectoryTree() throws IOException {
		JTree model = new FileBrowser().getTree();
		//JTree model = new FileBrowser().getPreciseTree(pathDirectory);
		
		// // JFrame d_selected = new JFrame("Client: View Directory");

		// // d_selected.setResizable(true);
		
		// // JPanel d_selected_panel = new JPanel();
		// // d_selected_panel.add(model);
		// // d_selected.add(d_selected_panel);
		
		// // d_selected.pack();
		// // d_selected.setVisible(connecting);
		
		// sendToServer("SELECTED_DIRECTORY");

		// ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		// oos.writeObject(model);
		// oos.flush();
		// System.out.println("pathDirectory2: " + pathDirectory);
	}
}