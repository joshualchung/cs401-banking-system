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

public class OptionATMGUI implements ActionListener{
	
	private static JFrame frame = new JFrame();
	private static JButton withdrawal = new JButton("Withdrawal");
	private static JButton deposit = new JButton("Deposit");
	private static JButton transfer = new JButton("Transfer");
	private static JButton switchAcc = new JButton("Switch Accounts");
	private static JButton cancel = new JButton("Cancel");
	
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
	private int currentAccountPos;
	public OptionATMGUI(Request response) throws IOException{
//		this.customer = (Customer)response.getUser();
//		this.accounts = login.getAccounts();
		currentAccountPos = 0;
		
		withdrawal.setBounds(100, 70, 300, 70);
		withdrawal.setBackground(new Color(0xBF2620));
		withdrawal.setForeground(Color.WHITE);
		withdrawal.setFont(new Font("Arial", Font.PLAIN, 25));
		withdrawal.setFocusable(false);
		withdrawal.addActionListener(this);
		
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
		
		west.add(withdrawal);
		west.add(deposit);
		west.add(transfer);
		
		east.add(switchAcc);
		east.add(cancel);
		
		frame.setVisible(true);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}