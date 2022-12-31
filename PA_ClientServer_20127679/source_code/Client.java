import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.beans.XMLEncoder;

public class Client implements ItemListener {
	static final String HOST = "localhost"; // configurable
	static final int PORT = 3200; // configurable

	private static String selectedDirectory;

	private Socket s;
	private Client c;
	private static boolean connecting;
	private Thread myFileThread;
	private ClientFile myFile;

	private BufferedReader br;
	private BufferedWriter bw;

	private static JFrame noti;
	private static JTextArea receiveMessage_ta;
	private JPanel sendMessage;
	private JTree model;

	Client() {
		c = this;
		connecting = false;
		noti = new JFrame("Notification from Client");
		model = new FileBrowser().getTree();
	}

	public String getDirectory() {
		return selectedDirectory;
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

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				try {
					if (connecting) {
						c.sendToServer("CLIENT_DISCONNECT");
						connecting = false;
						c.s.close();
					}
				} catch (IOException exc) {
					exc.printStackTrace();
				}
			}
		});

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
		sendMessage = new JPanel();
		JTextField sendMessage_tf = new JTextField(15);
		sendMessage_tf.setMaximumSize(new Dimension(200, 30));
		sendMessage.add(sendMessage_tf);

		JButton sendMessage_btn = new JButton("SEND");
		sendMessage_btn.setMaximumSize(new Dimension(200, 50));
		sendMessage.add(sendMessage_btn);

		// sendMessage.setVisible(false);

		// connected: receive label
		receiveMessage_ta = new JTextArea(10, 10);
		receiveMessage_ta.setLineWrap(true);
		receiveMessage_ta.setWrapStyleWord(true);
		receiveMessage_ta.setEditable(false);
		receiveMessage_ta.setText("WELCOME TO SERVER CHAT\n\n");

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
								String rm = receiveFromServer();
								if (rm == null || rm.equals("")) {
								} else if (rm.equals("SERVER_DISCONNECT")) {
									connecting = false;
									break;
								} else if (!rm.equals("SELECTED_DIRECTORY")) {
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

			s = new Socket(HOST, PORT);

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						sendDirectoryTree();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();

			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
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

	public String receiveFromServer() {
		try {
			String receivedMessage = br.readLine();
			if (receivedMessage == null)
				return null;
			if (receivedMessage.equals("SELECTED_DIRECTORY")) {
				String tempPathDirectory = br.readLine();
				if (!tempPathDirectory.equals(selectedDirectory)) {
					selectedDirectory = tempPathDirectory;

					// send notifications to server
					if (myFileThread != null) {
						myFile = null;
						myFileThread.interrupt();
						myFileThread = null;
					}
					myFile = new ClientFile(c);
					myFileThread = new Thread(myFile);
					myFileThread.start();

					System.out.println("selected directory: " + selectedDirectory);
				}
				return "SELECTED_DIRECTORY";
			}
			return receivedMessage;
		} catch (IOException e) {
			return null;
		}
	}

	public void sendDirectoryTree() throws IOException {
		// DefaultTreeModel treeModel = (DefaultTreeModel) model.getModel();
		// XMLEncoder encoder = new XMLEncoder(s.getOutputStream());
		// encoder.writeObject(treeModel);
		// encoder.flush();
		// encoder.close();

		System.out.println("sent directory tree ");
		// sendMessage.setVisible(true);
	}
}