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

	private static JButton withdrawalChecking = new JButton("Withdrawal Checking");
	private static JButton depositChecking = new JButton("Deposit Checking");
	private static JButton withdrawalSaving = new JButton("Withdrawal Saving");
	private static JButton depositSaving = new JButton("Deposit Saving");
	private static JButton transferSaveToCheck = new JButton("Transfer Saving to Checking");
	private static JButton transferCheckToSave = new JButton("Transfer Checking to Saving");
	private static JButton switchAcc = new JButton("Switch Accounts");
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
	
	private static JFrame pin = new JFrame();
	private static JLabel dispense = new JLabel();
	private static JLabel cash = new JLabel("$");
	private static JPanel top = new JPanel();
	private static JPanel buttons = new JPanel();
	
	private double amount = 0;
	private int type = 0;
	private String input = "";
	private Customer customer;
	private Account checkings;
	private Account savings;
	private int currentAccountPos;
  
	public TellerOptionGUI(Socket socket, ObjectInputStream objectInputStream, 
						ObjectOutputStream objectOutputStream, 
						Customer customer) throws IOException{
		
		Request customerRequest = new Request(RequestType.GETALLCUSTOMERACCOUNTS);
		
		objectOutputStream.writeObject(customerRequest);
		try {
			checkings = (Account)objectInputStream.readObject();
			savings = (Account)objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(checkings.getAccount());
		
		// receive checking
		// receive savings
		
		currentAccountPos = 0;
		

		withdrawalChecking.setBounds(100, 200, 250, 70);
		withdrawalChecking.setBackground(new Color(0xBF2620));
		withdrawalChecking.setForeground(Color.WHITE);
		withdrawalChecking.setFont(new Font("Arial", Font.PLAIN, 15));
		withdrawalChecking.setFocusable(false);
		withdrawalChecking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double withdrawAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount: "));
				// check valid amount
				if (withdrawAmount > checkings.getBalance()) {
					JOptionPane.showMessageDialog(
		                    null, 
		                    "Insufficient Funds", 
		                    "Enter valid amount", 
		                    JOptionPane.ERROR_MESSAGE);
				} else {
					checkings.setBalance(checkings.getBalance() - withdrawAmount);
					// send withdraw Request
					try {
						Request withdrawRequest = new Request(RequestType.WITHDRAW);
						objectOutputStream.writeObject(withdrawRequest);
						Transaction withdrawal = new Transaction(checkings.getAccount(),
																 checkings.getAccount(),
																 withdrawAmount,
																 RequestType.WITHDRAW);
						objectOutputStream.writeObject(withdrawal);
						checkings = (Account)objectInputStream.readObject();
						savings = (Account)objectInputStream.readObject();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// send Transaction
					// send updated Account
					label2.setText("Checking: $" + checkings.getBalance());	
				}
			}
		});
		
		withdrawalSaving.setBounds(100, 280, 250, 70);
		withdrawalSaving.setBackground(new Color(0xBF2620));
		withdrawalSaving.setForeground(Color.WHITE);
		withdrawalSaving.setFont(new Font("Arial", Font.PLAIN, 15));
		withdrawalSaving.setFocusable(false);
		withdrawalSaving.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double withdrawAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount: "));
				// check valid amount
				if (withdrawAmount > checkings.getBalance()) {
					JOptionPane.showMessageDialog(
		                    null, 
		                    "Insufficient Funds", 
		                    "Enter valid amount", 
		                    JOptionPane.ERROR_MESSAGE);
				} else {
					savings.setBalance(savings.getBalance() - withdrawAmount);
					// send withdraw Request
					try {
						Request withdrawRequest = new Request(RequestType.WITHDRAW);
						objectOutputStream.writeObject(withdrawRequest);
						Transaction withdrawal = new Transaction(savings.getAccount(),
																 savings.getAccount(),
																 withdrawAmount,
																 RequestType.WITHDRAW);
						objectOutputStream.writeObject(withdrawal);
						checkings = (Account)objectInputStream.readObject();
						savings = (Account)objectInputStream.readObject();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// send Transaction
					// send updated Account
					label3.setText("Saving: $" + savings.getBalance());	
				}
			}
		});
		
		depositChecking.setBounds(100,40,250,70);
		depositChecking.setBackground(new Color(0x4cbfff));
		depositChecking.setForeground(Color.WHITE);
		depositChecking.setFont(new Font("Arial", Font.PLAIN, 15));
		depositChecking.setFocusable(false);
		depositChecking.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				double depositAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount: "));
				// check valid amount
				if (depositAmount > checkings.getBalance()) {
					JOptionPane.showMessageDialog(
		                    null, 
		                    "Insufficient Funds", 
		                    "Enter valid amount", 
		                    JOptionPane.ERROR_MESSAGE);
				} else {
					checkings.setBalance(checkings.getBalance() + depositAmount);
					// send withdraw Request
					try {
						Request depositRequest = new Request(RequestType.DEPOSIT);
						objectOutputStream.writeObject(depositRequest);
						Transaction deposit = new Transaction(checkings.getAccount(),
																 checkings.getAccount(),
																 depositAmount,
																 RequestType.DEPOSIT);

						objectOutputStream.writeObject(deposit);
						checkings = (Account)objectInputStream.readObject();
						savings = (Account)objectInputStream.readObject();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// send Transaction
					// send updated Account
					label2.setText("Checking: $" + checkings.getBalance());	
				}
			}
		});
		
		depositSaving.setBounds(100,120,250,70);
		depositSaving.setBackground(new Color(0x4cbfff));
		depositSaving.setForeground(Color.WHITE);
		depositSaving.setFont(new Font("Arial", Font.PLAIN, 15));
		depositSaving.setFocusable(false);
		depositSaving.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double depositAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount: "));
				// check valid amount
				if (depositAmount > checkings.getBalance()) {
					JOptionPane.showMessageDialog(
		                    null, 
		                    "Insufficient Funds", 
		                    "Enter valid amount", 
		                    JOptionPane.ERROR_MESSAGE);
				} else {
					savings.setBalance(savings.getBalance() + depositAmount);
					// send withdraw Request
					try {
						Request depositRequest = new Request(RequestType.DEPOSIT);
						objectOutputStream.writeObject(depositRequest);
						Transaction deposit = new Transaction(savings.getAccount(),
																 savings.getAccount(),
																 depositAmount,
																 RequestType.DEPOSIT);
						objectOutputStream.writeObject(deposit);
						checkings = (Account)objectInputStream.readObject();
						savings = (Account)objectInputStream.readObject();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// send Transaction
					// send updated Account
					label3.setText("Savings: $" + savings.getBalance());
				}	
			}
		});
		
		transferCheckToSave.setBounds(100,40,250,70);
		transferCheckToSave.setBackground(new Color(0x4cbfff));
		transferCheckToSave.setForeground(Color.WHITE);
		transferCheckToSave.setFont(new Font("Arial", Font.PLAIN, 15));
		transferCheckToSave.setFocusable(false);
		transferCheckToSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double transAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount: "));
				// check valid amount
				if (transAmount > checkings.getBalance()) {
					JOptionPane.showMessageDialog(
		                    null, 
		                    "Insufficient Funds", 
		                    "Enter valid amount", 
		                    JOptionPane.ERROR_MESSAGE);
				} else {
					checkings.setBalance(checkings.getBalance() - transAmount);
					savings.setBalance(savings.getBalance() + transAmount);
					
					try {
						Request transRequest = new Request(RequestType.TRANSFER);
						objectOutputStream.writeObject(transRequest);
						Transaction transfer = new Transaction(checkings.getAccount(),
																 savings.getAccount(),
																 transAmount,
																 RequestType.TRANSFER);
						objectOutputStream.writeObject(transfer);
						checkings = (Account)objectInputStream.readObject();
						savings = (Account)objectInputStream.readObject();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// send Transaction
					// send updated Account
					label2.setText("Checking: $" + checkings.getBalance());
					label3.setText("Saving: $" + savings.getBalance());
				}
				
			}
		});
		
		transferSaveToCheck.setBounds(100,120,250,70);
		transferSaveToCheck.setBackground(new Color(0x4cbfff));
		transferSaveToCheck.setForeground(Color.WHITE);
		transferSaveToCheck.setFont(new Font("Arial", Font.PLAIN, 15));
		transferSaveToCheck.setFocusable(false);
		transferSaveToCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double transAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount: "));
				// check valid amount	
				if (transAmount > savings.getBalance()) {
					JOptionPane.showMessageDialog(
		                    null, 
		                    "Insufficient Funds", 
		                    "Enter valid amount", 
		                    JOptionPane.ERROR_MESSAGE);
				} else {
					checkings.setBalance(checkings.getBalance() + transAmount);
					savings.setBalance(savings.getBalance() - transAmount);
					
					try {
						Request transRequest = new Request(RequestType.TRANSFER);
						objectOutputStream.writeObject(transRequest);
						Transaction transfer = new Transaction(savings.getAccount(),
																 checkings.getAccount(),
																 transAmount,
																 RequestType.TRANSFER);
						objectOutputStream.writeObject(transfer);
						checkings = (Account)objectInputStream.readObject();
						savings = (Account)objectInputStream.readObject();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// send Transaction
					// send updated Account
					label2.setText("Checking: $" + checkings.getBalance());
					label3.setText("Saving: $" + savings.getBalance());
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
		
		label2.setText("Checking: $" + checkings.getBalance());					//insert account 1 name and money amount here
		label2.setBounds(50,100,200,25);
		label2.setForeground(Color.white);
		label2.setFont(new Font("Arial", Font.BOLD, 15));
		
		label3.setText("Savings $" + savings.getBalance());					//insert account 2 name and money amount here
		label3.setBounds(400,100,200,25);
		label3.setForeground(Color.white);
		label3.setFont(new Font("Arial", Font.BOLD, 15));
		
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
		
		west.add(withdrawalChecking);
		west.add(depositChecking);
		east.add(transferSaveToCheck);
		east.add(transferCheckToSave);
		west.add(withdrawalSaving);
		west.add(depositSaving);
		east.add(switchAcc);
		east.add(logout);
		
		frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
	}
	
}
