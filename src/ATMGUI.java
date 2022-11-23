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
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ATMGUI implements ActionListener{
	JFrame frame = new JFrame();
	private static JButton login = new JButton("Login");
	
	private static JPanel north = new JPanel();
	private static JPanel south = new JPanel();
	private static JPanel east = new JPanel();
	private static JPanel west = new JPanel();
	private static JPanel center = new JPanel();
	
	private static JLabel label1 = new JLabel();
	private static JLabel label2 = new JLabel();
	private static JLabel label3 = new JLabel();
	
	JTextField cardNumber;
	JPasswordField userPIN;
	
	private Socket socket = null;
	private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    
    public ATMGUI() throws IOException{
    	
    	login.setBounds(115, 200, 65, 25);
    	login.setFocusable(false);
    	login.addActionListener(this);
    	
    	cardNumber = new JTextField(20);
    	cardNumber.setBounds(15, 90, 165, 25);
    	
    	userPIN = new JPasswordField();
    	userPIN.setBounds(15, 155, 165, 25);
    	
    	label1.setText("ATM");								//text
		label1.setHorizontalAlignment(JLabel.CENTER);		//placement within panel 
		label1.setVerticalAlignment(JLabel.BOTTOM);
		label1.setForeground(Color.white);
		label1.setFont(new Font("Arial", Font.BOLD, 60));

		
		label2.setText("ID:");
		label2.setFont(new Font("Arial", Font.BOLD, 12));
		label2.setForeground(Color.white);
		label2.setBounds(175,90,80,25);
		
		label3.setText("Password:");
		label3.setFont(new Font("Arial", Font.BOLD, 12));
		label3.setForeground(Color.white);
		label3.setBounds(130,145,80,25);
		
		frame.setSize(1000, 750);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);  				//prevents frame from being resized 
		frame.setUndecorated(true);   //remove the title bar
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//exits program 
		frame.setBackground(Color.LIGHT_GRAY);
		
		north.setPreferredSize(new Dimension(1000,125));
		south.setPreferredSize(new Dimension(1000,125));
		east.setPreferredSize(new Dimension(225,750));
		west.setPreferredSize(new Dimension(225,750));
		center.setPreferredSize(new Dimension(550,500));
		
		north.setBackground(Color.DARK_GRAY);
		south.setBackground(Color.DARK_GRAY);
		east.setBackground(Color.DARK_GRAY);
		west.setBackground(Color.DARK_GRAY);
		
		frame.add(north,BorderLayout.NORTH);
		frame.add(south,BorderLayout.SOUTH);
		frame.add(east,BorderLayout.EAST);
		frame.add(west,BorderLayout.WEST);
		frame.add(center,BorderLayout.CENTER);
		
		JPanel subn = new JPanel();
		JPanel subw = new JPanel();
		JPanel subc = new JPanel();
		
		center.setLayout(new BorderLayout());
		
		//set dimensions of sub-panels
		subn.setPreferredSize(new Dimension(200,165));
		subw.setPreferredSize(new Dimension(200,335));
		subc.setPreferredSize(new Dimension(350,335));
		
		//set color of sub-panels
		subn.setBackground(Color.LIGHT_GRAY);
		subw.setBackground(Color.LIGHT_GRAY);
		subc.setBackground(Color.LIGHT_GRAY);
		
		center.setLayout(new BorderLayout());
		
		//add sub-panels to center panel
		center.add(subn,BorderLayout.NORTH);
		center.add(subw,BorderLayout.WEST);
		center.add(subc,BorderLayout.CENTER);
		
		//set layout 
		subn.setLayout(new BorderLayout());
		subw.setLayout(null);
		subc.setLayout(null);

		//Center panel's sub-panel top 
		subn.add(label1);		//adding ATM title
		
		//Center panel's sub-panel left side
		subw.add(label2);		//user id label
		subw.add(label3);		//password label 
		
		//Center panel's right side
		subc.add(cardNumber);
		subc.add(userPIN);
		subc.add(login);
		
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
		if(e.getSource() == login) {
			@SuppressWarnings("deprecation")
			int pin = Integer.parseInt(userPIN.getText());
			String cardNum = cardNumber.getText();
			
			//removes userId and password after pressing login button
			cardNumber.setText("");		
			userPIN.setText("");			
			
			Login customerLogin = new Login(cardNum, pin);
			try {
				// send customer login request
				Request loginRequest = new Request(RequestType.CUSTOMER_LOGIN);
				objectOutputStream.writeObject(loginRequest);
				objectOutputStream.flush();
				objectOutputStream.writeObject(customerLogin);
				Request response = (Request)objectInputStream.readObject();
				System.out.println(response.getStatus());
				
				if (response.getStatus() == Status.SUCCESS) {
					frame.dispose();
					System.out.println("Successful login responded");
					OptionATMGUI option = new OptionATMGUI(response);
				} else {
					JOptionPane.showMessageDialog(
		                    null, 
		                    "Login Failed", 
		                    "The user ID or password is incorrect. This is easily corrected by typing the correct user name and password.", 
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
			ATMGUI gui = new ATMGUI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
