import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
public class Server {
	private ServerSocket server = null;
	private static HashMap<String, Customer> customers = new HashMap<String, Customer>();
	{
		loadCustomers();
	}
	private static HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();
	{
		loadAccounts();
	}

	public Server(int port) {
		try {
			server = new ServerSocket(port);
			server.setReuseAddress(true);
			System.out.println("Server started");
			loadCustomers();
			loadAccounts();
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
	public static void loadCustomers() {
		try {
			File customerData = new File("customers.txt");
			Scanner reader = new Scanner(customerData);
			reader.useDelimiter(Pattern.compile("[\\r\\n,]+"));
			while (reader.hasNext()) {
				String first = reader.next().toUpperCase();
				String last = reader.next().toUpperCase();
				String cardNum = reader.next();
				int PIN = Integer.parseInt(reader.next());
				List<Integer> customerAccounts = new ArrayList<Integer>();
				// customersAccounts[0] = checking, customerAccounts[1] = savings
				customerAccounts.add(Integer.parseInt(reader.next()));
				customerAccounts.add(Integer.parseInt(reader.next()));
				Customer customer = new Customer(first, last, cardNum, PIN, customerAccounts);
				customers.put(cardNum, customer);
			}
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// accounts.txt format
	// account number, balance, history
	// NO HISTORY YET
	public static void loadAccounts() {
		try {
			File accountData = new File("accounts.txt");
			Scanner reader = new Scanner(accountData);
			reader.useDelimiter(Pattern.compile("[\\r\\n,]+"));
			while (reader.hasNext()) {
				int accNum = Integer.parseInt(reader.next());
				double balance = Double.parseDouble(reader.next());
				Account account = new Account(accNum, balance);
				accounts.put(accNum, account);
			}
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			FileWriter accountsFile = new FileWriter("accounts.txt");
			FileWriter customersFile = new FileWriter("customers.txt");
			for (Customer customer : customers.values()) {
				// write customer to file
				customersFile.write(String.format("%s,%s,%s,%o,%o,%o\n",
						customer.getFirstName(),
						customer.getLastName(),
						customer.getCardNum(),
						customer.getPin(),
						customer.getAccounts().get(0),
						customer.getAccounts().get(1)));
			}
			for (Account account : accounts.values()) {
				// write customer to file
				// HISTORY NOT INCLUDED YET. TO BE INCLUDED LATER
				accountsFile.write(String.format("%o,%f\n",
						account.getAccount(),
						account.getBalance()));
			}
			accountsFile.close();
			customersFile.close();
		} catch (Exception e) {
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
				Request request = (Request)objectIn.readObject();
				System.out.println(request.getStatus());
				// READ CUSTOMER_LOGIN 
					// READ LOGIN OBJECT
						// SEND SUCCESS LOGIN 
						// SEND FAILED LOGIN
				while (request.getType().equals(RequestType.CUSTOMER_LOGIN)) {
					Login loginRequest = (Login)objectIn.readObject();
					System.out.println(loginRequest.getPin());
					String customerCard = loginRequest.getCardNum();
					int customerPIN = loginRequest.getPin();
					Customer customer = customers.get(customerCard);
					
					if (customer != null && customer.getPin() == (customerPIN)) {
						request.setStatus(Status.SUCCESS);
						System.out.println(request.getStatus());
						objectOut.writeObject(request);
					} else {
						request.setStatus(Status.FAIL);
						System.out.println(request.getStatus());
						objectOut.writeObject(request);
					}
					request = (Request)objectIn.readObject();
				}
				
				// TELLER_LOGIN
				
				
				
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