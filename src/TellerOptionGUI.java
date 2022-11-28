import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.util.Random;
import javax.swing.SwingUtilities;

public class TellerOptionGUI implements ActionListener{
	
	private static JFrame frame = new JFrame();

	private static JButton withdraw = new JButton("Withdrawal Checking");
	private static JButton deposit = new JButton("Deposit Saving");
	private static JButton transfer = new JButton("Transfer Saving to Checking");
	private static JButton createCust = new JButton("Create Customer");
	private static JButton deleteAcc = new JButton("Delete Account");
	private static JButton logout = new JButton("Logout");

	
	private static JPanel north = new JPanel();
	private static JPanel east = new JPanel();
	private static JPanel west = new JPanel();
	
	private static JLabel label1 = new JLabel();		//title
	private static JLabel label2 = new JLabel();		//current account
	private static JLabel label3 = new JLabel();		//money 
	private static JLabel label4 = new JLabel();
	
	JTextField userID;
	JPasswordField passwordText;
	
	private Customer customer;
	private Account checkings;
	private Account savings;
  
	public TellerOptionGUI(Socket socket, ObjectInputStream objectInputStream, 
						ObjectOutputStream objectOutputStream) throws IOException{
		
		withdraw.setBounds(100, 200, 250, 70);
		withdraw.setBackground(new Color(0xBF2620));
		withdraw.setForeground(Color.WHITE);
		withdraw.setFont(new Font("Arial", Font.PLAIN, 15));
		withdraw.setFocusable(false);
		withdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String accountNum = JOptionPane.showInputDialog("Enter account number: ");
				double withdrawAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount: "));
				Account account;
				try {
					objectOutputStream.writeObject(new Account(accountNum, 1234));
					account = (Account)objectInputStream.readObject();
					if (withdrawAmount > account.getBalance()) {
						JOptionPane.showMessageDialog(
			                    null, 
			                    "Insufficient Funds", 
			                    "Enter valid amount", 
			                    JOptionPane.ERROR_MESSAGE);
					} else {
						account.setBalance(account.getBalance() - withdrawAmount);
						// send withdraw Request
						try {
							Request withdrawRequest = new Request(RequestType.WITHDRAW);
							objectOutputStream.writeObject(withdrawRequest);
							Transaction withdrawal = new Transaction(account.getAccount(),
																	 account.getAccount(),
																	 withdrawAmount,
																	 RequestType.WITHDRAW);
							objectOutputStream.writeObject(withdrawal);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
					
				} catch (IOException | ClassNotFoundException e2) {
					e2.printStackTrace();
				}
			}
		});
		
		deposit.setBounds(100,120,250,70);
		deposit.setBackground(new Color(0x4cbfff));
		deposit.setForeground(Color.WHITE);
		deposit.setFont(new Font("Arial", Font.PLAIN, 15));
		deposit.setFocusable(false);
		deposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String accountNum = JOptionPane.showInputDialog("Enter account number: ");
				double depositAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount: "));
				Account account;
				try {
					objectOutputStream.writeObject(new Account(accountNum, 1234));
					account = (Account)objectInputStream.readObject();
					account.setBalance(account.getBalance() + depositAmount);
					// send withdraw Request
					try {
						Request withdrawRequest = new Request(RequestType.DEPOSIT);
						objectOutputStream.writeObject(withdrawRequest);
						Transaction withdrawal = new Transaction(account.getAccount(),
																 account.getAccount(),
																 depositAmount,
																 RequestType.DEPOSIT);
						objectOutputStream.writeObject(withdrawal);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (IOException | ClassNotFoundException e2) {
					e2.printStackTrace();
				}
			}
		});
		
		transfer.setBounds(100,120,250,70);
		transfer.setBackground(new Color(0x4cbfff));
		transfer.setForeground(Color.WHITE);
		transfer.setFont(new Font("Arial", Font.PLAIN, 15));
		transfer.setFocusable(false);
		transfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String accountNum = JOptionPane.showInputDialog("Enter account number: ");
				String transferNum = JOptionPane.showInputDialog("Enter account number to transfer: ");
				double transferAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount: "));
				Account account;
				Account transferAcc;
				try {
					objectOutputStream.writeObject(new Account(accountNum, 1234));
					objectOutputStream.writeObject(new Account(transferNum, 1234));
					account = (Account)objectInputStream.readObject();
					transferAcc = (Account)objectInputStream.readObject();
					if (transferAmount > account.getBalance()) {
						JOptionPane.showMessageDialog(
			                    null, 
			                    "Insufficient Funds", 
			                    "Enter valid amount", 
			                    JOptionPane.ERROR_MESSAGE);
					} else {
						try {
							Request withdrawRequest = new Request(RequestType.TRANSFER);
							objectOutputStream.writeObject(withdrawRequest);
							Transaction withdrawal = new Transaction(account.getAccount(),
																	 account.getAccount(),
																	 transferAmount,
																	 RequestType.TRANSFER);
							objectOutputStream.writeObject(withdrawal);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
					
				} catch (IOException | ClassNotFoundException e2) {
					e2.printStackTrace();
				}

			}
		});
		
		logout.setBounds(100,360,250,70);
		logout.setBackground(Color.RED);
		logout.setForeground(Color.WHITE);
		logout.setFont(new Font("Arial", Font.PLAIN, 15));
		logout.setFocusable(false);
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Request logout = new Request(RequestType.LOGOUT);
					objectOutputStream.writeObject(logout);
					Request response = (Request)objectInputStream.readObject();
					if (response.getStatus().equals(Status.SUCCESS)) {
//						frame.dispose();
//						ATMGUI atmgui = new ATMGUI();
						objectOutputStream.writeObject(logout);
						System.exit(0);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		createCust.setBounds(100, 200, 250, 70);
		createCust.setBackground(new Color(0x4cbfff));
		createCust.setForeground(Color.white);
		createCust.setFont(new Font("Arial", Font.PLAIN, 15));
		createCust.setFocusable(false);
		createCust.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String createFirstName = JOptionPane.showInputDialog("Enter first name for account: ");
				String createLastName = JOptionPane.showInputDialog("Enter last name for account: ");
				int createPin = Integer.parseInt(JOptionPane.showInputDialog("Enter 4 number pin: "));
				try {
					Request createCustomer = new Request(RequestType.CREATECUSTOMER);
					objectOutputStream.writeObject(createCustomer);
					Customer newCust = new Customer(createFirstName, createLastName, createPin);
					objectOutputStream.writeObject(newCust);
					checkings = (Account)objectInputStream.readObject();
					savings = (Account)objectInputStream.readObject();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		label1.setText("Welcome");
		label1.setBounds(150, 50, 500, 25);
		label1.setBackground(Color.YELLOW);
		label1.setFont(new Font("Arial", Font.BOLD, 40));
		label1.setHorizontalAlignment(JLabel.CENTER);
		
		label4.setText("Current: ");
		label4.setBounds(50,50,200,25);
		label4.setForeground(Color.orange);
		label4.setFont(new Font("Arial", Font.BOLD, 15));
		
		frame.setSize(1000, 750);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		north.setBackground(new Color(0x123456));
		east.setBackground(new Color(0x006299));
		west.setBackground(new Color(0x006299));
		
		
		north.setPreferredSize(new Dimension(1000,250));
		east.setPreferredSize(new Dimension(500,625));
		west.setPreferredSize(new Dimension(500,625));
		
		north.setLayout(new BorderLayout());
		east.setLayout(null);
		west.setLayout(null);
		
		frame.add(north,BorderLayout.NORTH);
		frame.add(east,BorderLayout.EAST);
		frame.add(west,BorderLayout.WEST);
		
		JPanel subn1 = new JPanel();
		subn1.setPreferredSize(new Dimension(1000,150));
		subn1.setLayout(null);
		subn1.setBackground(new Color(0x123456));
		north.add(subn1,BorderLayout.SOUTH);
		north.add(label1);			
		
		subn1.add(label2);
		subn1.add(label3);
		subn1.add(label4);
		
		west.add(withdraw);
		east.add(transfer);;
		west.add(deposit);
		east.add(logout);
		
		frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
	}
	
}
