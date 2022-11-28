import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class BankTellerUserGUI implements ActionListener{

	JFrame frame = new JFrame();
	private static JButton login = new JButton("Login");
	private static JButton newCustomer = new JButton("New Customer");
	private static JButton signOut = new JButton("Sign Out");
	
	private static JPanel north = new JPanel();
	private static JPanel south = new JPanel();
	private static JPanel center = new JPanel();
	
	private static JLabel label1 = new JLabel();
	private static JLabel label2 = new JLabel();
	private static JLabel label3 = new JLabel();
	private static JLabel label4 = new JLabel();
	
	JTextField userID;
	JPasswordField password;
	
	Customer currentCustomer;
	
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	
	public BankTellerUserGUI(loginRequest request) throws UnknownHostException, IOException{
		currentCustomer = loginRequest.getUser();
		login.setBounds(700, 150, 100, 40);
		login.setBackground(Color.BLUE);
		login.setFocusable(false);
		login.addActionListener(this);
		
		newCustomer.setBounds(50, 40, 150, 50);
		newCustomer.setBackground(Color.GREEN);
		newCustomer.setFocusable(false);
		newCustomer.addActionListener(this);
		
		signOut.setBounds(900, 40, 150, 50);
		signOut.setBackground(Color.RED);
		signOut.setFocusable(false);
		signOut.addActionListener(this);
		
		userID = new JTextField(20);
		userID.setBounds(200, 90, 300, 25);
		
		password = new JPasswordField();
		password.setBounds(200, 155, 300, 25);
		
		label1.setText("Welcome");
		label1.setHorizontalAlignment(JLabel.LEFT);			//placement within panel 
		label1.setVerticalAlignment(JLabel.CENTER);
		label1.setForeground(Color.WHITE);
		label1.setFont(new Font("Arial", Font.BOLD, 50));
		
		label2.setText("Sign in User");
		label2.setBounds(50, 5, 300, 50);
		label2.setForeground(Color.WHITE);
		label2.setFont(new Font("Arial", Font.BOLD, 20));
		
		label2.setText("Account Number: ");
		label2.setBounds(115, 90, 80, 25);
		label2.setForeground(Color.WHITE);
		label2.setFont(new Font("Arial", Font.BOLD, 20));
		
		label2.setText("Password: ");
		label2.setBounds(100, 90, 80, 25);
		label2.setForeground(Color.WHITE);
		label2.setFont(new Font("Arial", Font.BOLD, 20));
		
		frame.setSize(1000, 750); 					//sets frame size
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);  				//prevents frame from being resized 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//exits program 
		frame.setBackground(Color.LIGHT_GRAY);
		
		north.setBackground(Color.DARK_GRAY);
		south.setBackground(Color.DARK_GRAY);
		center.setBackground(Color.DARK_GRAY);
		
		north.setPreferredSize(new Dimension(1000,125));
		south.setPreferredSize(new Dimension(1000,125));
		center.setPreferredSize(new Dimension(1000,500));
		
		frame.add(north,BorderLayout.NORTH);
		frame.add(south,BorderLayout.SOUTH);
		frame.add(center,BorderLayout.CENTER);
		
		north.setLayout(new BorderLayout());
		south.setLayout(null);
		center.setLayout(null);
		
		north.add(label1);
		center.add(label2);
		south.add(newCustomer);
		south.add(signOut);
		
		JPanel subp = new JPanel();
		subp.add(label3);
		subp.add(label4);
		subp.add(userID);
		subp.add(password);
		subp.add(login);
		
		frame.setVisible(true);	//makes frame visible
		
		connectToServer();
    }
	    public void connectToServer() {
	    	try {
				socket = new Socket("localhost", 1234);
				System.out.println("Client connected to " + socket.getPort());
				objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				objectInputStream = new ObjectInputStream(socket.getInputStream());
				
			}
			catch(IOException e) {
				e.printStackTrace();
			}
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == login) {
			String id = userID.getText();
			String password = password.getText();
			Login login = new Login(id, password);
			
			//removes userId and password after pressing login button
			userID.setText("");		
			password.setText("");			
			
			Request request = new loginRequest(login);
			try {
				objectOutputStream.writeObject(request);
				loginRequest response = (loginRequest)objectInputStream.readObject();
				if (response.getStatus() == Status.SUCCESS) {
					if (response.getUser() instanceof Customer) {
						frame.dispose();
						System.out.println(((loginRequest)response).getUser().getName());
						OptionATMGUI usersignin = new OptionATMGUI(response);
						socket.close();
					} else {
						JOptionPane.showMessageDialog(
			                    null, 
			                    "Login Failed", 
			                    "The user ID or password is incorrect.", 
			                    JOptionPane.ERROR_MESSAGE);
					}
				}
			} catch (IOException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		if(e.getSource() == newCustomer) {
			
		}
		
		if(e.getSource() == signOut) {
			System.exit(0);
		}

	
		
	}
	

	

}
