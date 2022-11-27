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

public class BankTellerGUI implements ActionListener{
	JFrame frame = new JFrame();
	private static JButton login = new JButton("Login");
	
	private static JPanel north = new JPanel();
	private static JPanel south = new JPanel();
	private static JPanel east = new JPanel();
	private static JPanel west = new JPanel();
	private static JPanel center = new JPanel();
	
	private static JLabel bannerLabel = new JLabel();
	private static JLabel idLabel = new JLabel();
	private static JLabel passwordLabel = new JLabel();
	
	JTextField userID;
	JPasswordField passwordText;
	
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	
	public BankTellerGUI() throws UnknownHostException,IOException{
		
		//creating frame
		frame.setSize(1000, 750); 					//sets frame size
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);  				//prevents frame from being resized 
		frame.setUndecorated(true);   //remove the title bar
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//exits program
		frame.setBackground(Color.LIGHT_GRAY);
		
		//set dimensions of panels
		north.setPreferredSize(new Dimension(1000,125));
		south.setPreferredSize(new Dimension(1000,125));
		east.setPreferredSize(new Dimension(225,750));
		west.setPreferredSize(new Dimension(225,750));
		center.setPreferredSize(new Dimension(550,500));
		
		north.setBackground(Color.DARK_GRAY);
		south.setBackground(Color.DARK_GRAY);
		east.setBackground(Color.DARK_GRAY);
		west.setBackground(Color.DARK_GRAY);
		
		//add panels to frame
		frame.add(north,BorderLayout.NORTH);
		frame.add(south,BorderLayout.SOUTH);
		frame.add(east,BorderLayout.EAST);
		frame.add(west,BorderLayout.WEST);
		frame.add(center,BorderLayout.CENTER);
		
		//login button
		login.setBounds(115, 200, 65, 25);
		login.setFocusable(false);
		login.addActionListener(this);
		
		userID = new JTextField(20);
		userID.setBounds(15, 90, 165, 25);
		
		passwordText = new JPasswordField();
		passwordText.setBounds(15, 125, 165, 25);
		
		//label designs
		bannerLabel.setText("Bank Teller System Login");
		bannerLabel.setHorizontalAlignment(JLabel.CENTER);
		bannerLabel.setFont(new Font("Ariel", Font.BOLD, 40));
		
		idLabel.setText("ID:");
		idLabel.setFont(new Font("Arial", Font.BOLD, 12));
		idLabel.setBounds(175, 90, 80, 25);
		
		passwordLabel.setText("Password:");
		passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
		passwordLabel.setBounds(130, 125, 80, 25);
		
		JPanel subn = new JPanel();
		JPanel subw = new JPanel();
		JPanel subc = new JPanel();
		
		//set color of sub-panels
		subn.setBackground(Color.DARK_GRAY);
		subw.setBackground(Color.DARK_GRAY);
		subc.setBackground(Color.DARK_GRAY);
		
		center.setLayout(new BorderLayout());
		
		//set dimensions of sub-panels
		subn.setPreferredSize(new Dimension(200,165));
		subw.setPreferredSize(new Dimension(200,335));
		subc.setPreferredSize(new Dimension(350,335));
		
		//add sub-panels to center panel
		center.add(subn,BorderLayout.NORTH);
		center.add(subw,BorderLayout.WEST);
		center.add(subc,BorderLayout.CENTER);
		
		//set layout 
		subn.setLayout(new BorderLayout());
		subw.setLayout(null);
		subc.setLayout(null);

		//Center panel's sub-panel top 
		subn.add(bannerLabel);		//adding ATM title
		
		//Center panel's sub-panel left side
		subw.add(idLabel);		//user id label
		subw.add(passwordLabel);		//password label 
		
		//Center panel's right side
		subc.add(userID);
		subc.add(passwordText);
		subc.add(login);
		
		frame.setVisible(true);
		
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
		if(e.getSource() == login) {
			String username = userID.getText();
			@SuppressWarnings("deprecation")
			String password = passwordText.getText();
			
			//removes userId and password after pressing login button
			userID.setText("");		
			passwordText.setText("");			
			
			TellerLogin teller = new TellerLogin(username, password);
			try {
				// send customer login request
				Request loginRequest = new Request(RequestType.TELLER_LOGIN);
				objectOutputStream.writeObject(loginRequest);
				objectOutputStream.flush();
				objectOutputStream.writeObject(teller);
				Request response = (Request)objectInputStream.readObject();
				System.out.println(response.getStatus());
				
				if (response.getStatus() == Status.SUCCESS) {
					frame.dispose();
					System.out.println("Successful teller login responded");
					Teller customer = (Customer)objectInputStream.readObject();
					BankTellerUserGUI option = new BankTellerUserGUI(this);
				} else {
					JOptionPane.showMessageDialog(
		                    null, 
		                    "Login Failed", 
		                    "The user ID or password is incorrect.", 
		                    JOptionPane.ERROR_MESSAGE);
				}

			} catch (IOException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				try {
					if (objectInputStream != null) {
						objectInputStream.close();
					}
					if (objectOutputStream != null) {
						objectOutputStream.close();
					}
					if (socket != null) {
						socket.close();
					}
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		try {
			BankTellerGUI gui = new BankTellerGUI();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}