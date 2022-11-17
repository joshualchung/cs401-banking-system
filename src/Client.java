import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Client {
	private Socket socket = null;
	private ObjectInputStream objectIn = null;
	private ObjectOutputStream objectOut = null;
	
	public Client(String address, int port) {
		try {
			socket = new Socket(address, port);
			System.out.println("Client connected");
			
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
			
			Scanner scanner = new Scanner(System.in);
			
			// Login object created for client login request
			String accNum = "";
			int pin;
			System.out.println("Enter card number: ");
			accNum = scanner.nextLine();
			System.out.println("Enter PIN: ");
			pin = Integer.parseInt(scanner.nextLine());
			Login customerLogin = new Login(accNum, pin);
			objectOut.writeObject(customerLogin);
			
			// Server should return customers accounts to use for bank gui
			
		}
		catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectIn != null) {
					objectIn.close();
				}
				if (objectOut != null) {
					objectOut.close();
				}
				if (socket != null) {
					socket.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		Client client = new Client("localhost", 1234);
	}
}
