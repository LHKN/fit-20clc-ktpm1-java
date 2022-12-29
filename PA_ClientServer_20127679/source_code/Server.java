import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.filechooser.FileSystemView;


public class Server implements ItemListener {
	static final int PORT = 3200;

	private static int width;
	private static int height;

	private static HashMap<String, ClientThread> clientList;
	private static ServerSocket ss;

	private static String cur_name = null;

	// provide nice icons and names for files
    private FileSystemView fileSystemView;

    // file-system tree 
    private JTree tree;

	private static JFrame noti = new JFrame("Notification from Server");

	Server() {
		clientList = new HashMap<>();
	}

	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try{
					createAndShowGUI();
				}catch(IOException | ClassNotFoundException e){}
			}
		});
	}

	private static void createAndShowGUI() throws IOException, ClassNotFoundException{
		// get monitor screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int)Math.round(screenSize.getWidth());
		height = (int)Math.round(screenSize.getHeight());

		// set Container
		JFrame.setDefaultLookAndFeelDecorated(true);

		JFrame frame = new JFrame("Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		Server s = new Server();
		s.addComponentToPane(frame, frame.getContentPane());

		frame.setLocation(width / 2, 0);
		frame.pack();
		frame.setVisible(true);
	}

	public void itemStateChanged(ItemEvent evt) {
		// CardLayout cl = (CardLayout)(states.getLayout());
		// cl.show(states, (String)evt.getItem());
	}

	public void addComponentToPane(JFrame frame, Container pane) throws IOException, ClassNotFoundException{
		// title
		JPanel top = new JPanel();

		JLabel title = new JLabel("<html> <h1> Press OPEN to open server~ </h1> </html>");
		title.setMinimumSize(new Dimension(200, 50));
		top.add(title);

		// functionality
		// CardLayout
		JPanel states = new JPanel(new CardLayout());

		// card: OPEN button
		JPanel button = new JPanel();

		JButton b_open_btn = new JButton("OPEN");
		b_open_btn.setMaximumSize(new Dimension(200, 50));
		button.add(b_open_btn);

		// card: connected
		JPanel connected = new JPanel();

		// connected: select client from list
		JPanel list = new JPanel();
		
		DefaultListModel<String> l_model = new DefaultListModel<String>();

		JList<String> l_list = new JList<String>(l_model);
        l_list.setLayoutOrientation(JList.VERTICAL);
        l_list.setVisibleRowCount(1);
		l_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		l_list.setSelectedIndex(0);

		JScrollPane lcb = new JScrollPane(l_list);
		lcb.setPreferredSize(new Dimension(200,50));

		JButton l_confirm_btn = new JButton("CONFIRM");

		list.add(lcb);
		list.add(l_confirm_btn);

		// connected: 
		// CardLayout
		JPanel confirmed = new JPanel();

		JPanel buttons = new JPanel();
		JButton chat_btn = new JButton("CHAT LOG");
		chat_btn.setMaximumSize(new Dimension(200, 50));

		JButton directory_btn = new JButton("DIRECTORY");
		directory_btn.setMaximumSize(new Dimension(200, 50));

		buttons.add(chat_btn);
		buttons.add(directory_btn);
		
		// CardLayout
		JPanel actions = new JPanel(new CardLayout());

		// chat:
		JPanel chat = new JPanel();
		JPanel chat_sendMessage = new JPanel();
		
		//chat: send message
		JTextField sendMessage_tf = new JTextField(10);
		sendMessage_tf.setMaximumSize(new Dimension(200, 30));

		JButton sendMessage_btn = new JButton("SEND");
		sendMessage_btn.setMaximumSize(new Dimension(200, 50));
		
		chat_sendMessage.add(sendMessage_tf);
		chat_sendMessage.add(sendMessage_btn);


		//chat: receive message
		JTextArea c_receive_ta = new JTextArea(10,10);
		c_receive_ta.setLineWrap(true);
		c_receive_ta.setWrapStyleWord(true);
		c_receive_ta.setEditable(false);
		c_receive_ta.setText("WELCOME TO SERVER CHAT\n");

		JScrollPane chat_receiveMessage = new JScrollPane(c_receive_ta);

		chat.add(chat_sendMessage);
		chat.add(chat_receiveMessage);
		chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));

		// directory:
		JPanel directory = new JPanel();
		JButton d_select_btn = new JButton("Select directory: ");
		JPanel d_select_panel = new JPanel();
		
		directory.add(d_select_btn);
		directory.add(d_select_panel);

		// add components to containers
		actions.add(chat, "chat");
		actions.add(directory, "directory");
		actions.setVisible(false);
		
		confirmed.add(buttons);
		confirmed.add(actions);
		confirmed.setVisible(false);
		confirmed.setLayout(new BoxLayout(confirmed, BoxLayout.Y_AXIS));

		connected.add(list);
		connected.add(confirmed);
		connected.setLayout(new BoxLayout(connected, BoxLayout.Y_AXIS));

		states.add(button, "button");
		states.add(connected, "connected");

		pane.add(top, BorderLayout.NORTH);
		pane.add(states, BorderLayout.SOUTH);


		//override buttons
		
		l_confirm_btn.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent arg0) {
				String temp = l_list.getSelectedValue();
				
				if (temp == null || clientList.get(temp) == null || !clientList.get(temp).checkConnection()) { //   	
					confirmed.setVisible(false);
					cur_name = null;
					return;
				}

				if (temp.equals(cur_name)) return;

				cur_name = temp;

				System.out.println("Connected to client "+cur_name);///

				confirmed.setVisible(true);
				frame.pack();
			}
		});

		chat_btn.addActionListener(new ActionListener(){
			@Override
            public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout)(actions.getLayout());
				cl.show(actions, "chat");
				actions.setVisible(true);
				frame.pack();
			}
		});
		
		directory_btn.addActionListener(new ActionListener(){
			@Override
            public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout)(actions.getLayout());
				cl.show(actions, "directory");
				actions.setVisible(true);
				frame.pack();
			}
		});

		d_select_btn.addActionListener(new ActionListener(){
			@Override
            public void actionPerformed(ActionEvent arg0) {		
				JFileChooser chooser = new JFileChooser(); 
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select Directory");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				//
				// disable the "All files" option.
				chooser.setAcceptAllFileFilterUsed(false);
				//    
				if (chooser.showOpenDialog(d_select_panel) == JFileChooser.APPROVE_OPTION) { 
					clientList.get(cur_name).sendToClient("SELECTED_DIRECTORY");

					clientList.get(cur_name).sendToClient(String.valueOf(chooser.getSelectedFile()));

					try{
						tree = clientList.get(cur_name).receiveDirectory();

						JFrame d_selected = new JFrame("View Selected Directory");

						d_selected.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						d_selected.setResizable(false);

						Server s = new Server();
						s.addComponentToPane(frame, frame.getContentPane());

						d_selected.setLocation(width / 2, height / 2);
						d_selected.pack();
						d_selected.setVisible(true);

						JPanel d_selected_panel = new JPanel();
						d_selected_panel.add(tree);
						d_selected.add(d_selected_panel);

					}catch(IOException | ClassNotFoundException e){}
					
				}
			}
		});

		sendMessage_btn.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent arg0) {
				String sm = sendMessage_tf.getText();
				if (sm.equals("")) 
					return;

				System.out.println("Sending to client "+ cur_name);///

				if(clientList.get(cur_name).sendToClient(sm)){
					c_receive_ta.setText(c_receive_ta.getText()+"Server: "+sm+"\n");
				}else{
					JOptionPane.showMessageDialog(noti, "Client has left! <0A0 >", "Server", JOptionPane.INFORMATION_MESSAGE);
					clientList.get(cur_name).setConnection(false);
				}
			}
		});

		b_open_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				title.setText("<html> <h1> Server is online~ </h1> </html>");
				CardLayout cl = (CardLayout) (states.getLayout());
				cl.show(states, "connected");
				openServer();

				new Thread(new Runnable() {
					public void run() {
						// update scroll pane-->
						while(true){				
							String name = connectToClient();
							if(name!=null){
								l_model.addElement(name);

								new Thread(new Runnable() {
									public void run(){
										while (clientList.get(name).checkConnection()){
											try{
												String rm = clientList.get(name).manageClient();
												
												if (rm == null){
													l_model.removeElement(name);
													confirmed.setVisible(false);
													clientList.get(name).setConnection(false);
												}else{
													c_receive_ta.setText(c_receive_ta.getText() + name + ": " + rm + "\n");
												}
											}catch(IOException | ClassNotFoundException e){
												e.printStackTrace();
											}
										}
									}
								}).start();
							}
						}
						//<--
					}
				}).start();
			}
		});
	}

	public static void openServer() {
		try {
			ss = new ServerSocket(PORT);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(noti, "Connection is interrupted! <0A0 >", "Server", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public static String connectToClient(){
		try{
			Socket s = ss.accept(); // synchronous
			String name = "client" + String.valueOf(s.getPort());
	
			System.out.println("Accepting client");
			System.out.println(name);
	
			//if (!clientList.keySet().contains(name)) {
				ClientThread ct = new ClientThread(name,s.getPort(),s);
				clientList.put(name, ct);
				ct.startClient();
			//}

			return name;
		} catch (IOException e) {
			return null;
		}
	}

	public static ClientThread chooseClient(String name) {
		return clientList.get(name);
	}
}