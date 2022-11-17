import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
public class Server {
	private ServerSocket server = null;
	private HashMap<String, Customer> customers;
	private HashMap<Integer, Account> accounts;
	public Server(int port) {
		try {
			server = new ServerSocket(port);
			server.setReuseAddress(true);
			System.out.println("Server started");
			
			while (true) {
				Socket client = server.accept();
				
				ClientHandler clientSocket = new ClientHandler(client);
				new Thread(clientSocket).start();
			}
		}
		catch(IOException e) {
			System.out.println(e);
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// customers.txt format
	// first name, last name, card number, PIN, account numbers (checking/saving)
	public void loadCustomers() {
		try {
			File customerData = new File("customers.txt");
			Scanner reader = new Scanner(customerData);
			reader.useDelimiter(Pattern.compile("[\\r\\n,]+"));
			while (reader.hasNext()) {
				String first = reader.next().toUpperCase();
				String last = reader.next().toUpperCase();
				int cardNum = Integer.parseInt(reader.next());
				int PIN = Integer.parseInt(reader.next());
				List<Integer> customerAccounts = new ArrayList<Integer>();
				// customersAccounts[0] = checking, customerAccounts[1] = savings
				customerAccounts.add(Integer.parseInt(reader.next()));
				customerAccounts.add(Integer.parseInt(reader.next()));
			}
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// accounts.txt format
	// account number, balance, history
	// NO HISTORY YET
	public void loadAccounts() {
		try {
			File accountData = new File("accounts.txt");
			Scanner reader = new Scanner(accountData);
			reader.useDelimiter(Pattern.compile("[\\r\\n,]+"));
			while (reader.hasNext()) {
				int accNum = Integer.parseInt(reader.next());
				double balance = Double.parseDouble(reader.next());
			}
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		
		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}
		
		public void run() {
			ObjectInputStream objectIn = null;
			ObjectOutputStream objectOut = null;
			try {
				objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
				objectIn = new ObjectInputStream(clientSocket.getInputStream());
				Login loginRequest = (Login)objectIn.readObject();

				// verify card number with customer and PIN
					// if correct send back List of Accounts
					// else send back incorrect user/password error
				
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (objectIn != null) {
						objectIn.close();
					}
					if (objectOut != null) {
						objectOut.close();
					}
					if (clientSocket != null) {
						clientSocket.close();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String args[]) {
		Server server = new Server(1234);
	}
}