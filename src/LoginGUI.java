import javax.swing.*;
import javax.awt.*;
import javax.awt.event;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginGUI {
	//private Client client;
	private JFrame frame;
	private JPanel panel;
	private JLabel userLabel, pinLabel;
	private JButton submit, cancel;
	private JTextField userText, pinText;
	
	public LoginGUI() {
		frame = new JFrame();
		panel = new JPanel();
		frame.setSize(350, 200);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.add(panel);
		
		panel.setLayout(null);
		
		userLabel = new JLabel("Username");
		userLabel.setBounds(10, 20, 80, 25);
		panel.add(userLabel);
		
		userText = new JTextField(20);
		userText.setBounds(100, 20, 165, 25);
		panel.add(userText);
		
		pinLabel = new JLabel("Password");
		pinLabel.setBounds(10, 50, 80, 25);
		panel.add(pinLabel);
		
		pinText = new JTextField(20);
		pinText.setBounds(100, 50, 165, 25);
		panel.add(pinText);
		
		submit = new JButton("Login");
		submit.setBounds(10, 80, 80, 25);
		panel.add(submit);
		
		cancel = new JButton("Cancel");
		cancel.setBounds(10, 120, 80, 25);
		panel.add(cancel);
		
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
	      new LoginGUI();
	   }
}
