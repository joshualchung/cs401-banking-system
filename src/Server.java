import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
public class Server {
	private ServerSocket server = null;
	
	// card number : Customer
	private static HashMap<String, Customer> customers = new HashMap<String, Customer>();
	{
		loadCustomers();
	}
	
	// account number : Account
	private static HashMap<String, Account> accounts = new HashMap<String, Account>();
	{
		loadAccounts();
	}
	
	// teller user : teller password
	private static HashMap<String, TellerLogin> tellers = new HashMap<String, TellerLogin>();
	{
		loadTellers();
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
				List<String> customerAccounts = new ArrayList<String>();
				// customersAccounts[0] = checking, customerAccounts[1] = savings
				customerAccounts.add(reader.next());
				customerAccounts.add(reader.next());
				System.out.println(customerAccounts.get(0));
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
				String accNum = reader.next();
				double balance = Double.parseDouble(reader.next());
				Account account = new Account(accNum, balance);
				accounts.put(accNum, account);
			}
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// tellers.txt
	// teller user, teller password
	public static void loadTellers() {
		try {
			File tellerData = new File("tellers.txt");
			Scanner reader = new Scanner(tellerData);
			reader.useDelimiter(Pattern.compile("[\\r\\n,]+"));
			while (reader.hasNext()) {
				String user = reader.next();
				String password = reader.next();
				TellerLogin teller = new TellerLogin(user, password);
				tellers.put(user, teller);
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
		
		public List<Account> getAccounts(String checkings, String savings) {
			List<Account> customerAccounts = new ArrayList();
			customerAccounts.add(accounts.get(checkings));
			customerAccounts.add(accounts.get(savings));
			return customerAccounts;
			
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
					String customerCard = loginRequest.getCardNum();
					int customerPIN = loginRequest.getPin();
					Customer customer = customers.get(customerCard);
					
					if (customer != null && customer.getPin() == (customerPIN)) {
						request.setStatus(Status.SUCCESS);
						System.out.println(request.getStatus());
						objectOut.writeObject(request);
						objectOut.writeObject(customer);
						
						// while handling customer actions
							// change request type to logout and break
						Request customerReq = (Request)objectIn.readObject();
						System.out.println(customerReq.getType());
						while (!customerReq.getType().equals(RequestType.LOGOUT)) {
							List<Account> customerAccounts = getAccounts(customer.getAccounts().get(0), 
																   		 customer.getAccounts().get(1));
							objectOut.writeObject(customerAccounts.get(0));
							objectOut.writeObject(customerAccounts.get(1));
							// handle requests (DEPOSIT/WITHDRAWAL/TRANSFER)
						}
						
					} else {
						request.setStatus(Status.FAIL);
						System.out.println(request.getStatus());
						objectOut.writeObject(request);
					}
					request = (Request)objectIn.readObject();
				}
				
				// TELLER_LOGIN
				while (request.getType().equals(RequestType.TELLER_LOGIN)) {	
					TellerLogin loginRequest = (TellerLogin)objectIn.readObject();
					String username = loginRequest.getUsername();
					String password = loginRequest.getPassword();
					TellerLogin teller = tellers.get(username);
					
					if (teller != null && teller.getPassword().equals(password)) {
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