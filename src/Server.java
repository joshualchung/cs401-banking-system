import java.net.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	
	// account number : list of transactions
	private static HashMap<String, List<Transaction>> transactions = new HashMap<String, List<Transaction>>();
	{
		try {
			loadTransactions();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Server(int port) {
		try {
			server = new ServerSocket(port);
			server.setReuseAddress(true);
			System.out.println("Server started");
			System.out.println(transactions.size());
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
				Customer customer = new Customer(first, last, cardNum, PIN, customerAccounts);
				customers.put(cardNum, customer);
			}
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// accounts.txt format
	// account number, balance
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
	
	// transactions.txt
	// account number, target, amount, request, date
	public static void loadTransactions() throws ParseException {
		try {
			File transactionData = new File("transactions.txt");
			Scanner reader = new Scanner(transactionData);
			reader.useDelimiter(Pattern.compile("[\\r\\n,]+"));
			DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
			while (reader.hasNext()) {
				String account = reader.next();
				String target = reader.next();
				double amount = Double.parseDouble(reader.next());
				RequestType request = RequestType.valueOf(reader.next());
				LocalDateTime date = LocalDateTime.parse(reader.next(), dateFormatter);
				Transaction transaction = new Transaction(account, target, amount, request, date);
				addTransaction(account, transaction);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static void addTransaction(String account, Transaction transaction) {
		List<Transaction> transactionList = transactions.get(account);
		if (transactionList == null) {
			transactionList = new ArrayList<Transaction>();
			transactionList.add(transaction);
			transactions.put(account, transactionList);
		} else {
			transactionList.add(transaction);
		}
	}
	
	
	public static void save() {
		try {
			FileWriter accountsFile = new FileWriter("accounts.txt");
			FileWriter customersFile = new FileWriter("customers.txt");
			FileWriter tellersFile = new FileWriter("tellers.txt");
			FileWriter transactionsFile = new FileWriter("transactions.txt");
			for (Customer customer : customers.values()) {
				// write customer to file
				customersFile.write(String.format("%s,%s,%s,%s,%s,%s\n",
						customer.getFirstName(),
						customer.getLastName(),
						customer.getCardNum(),
						customer.getPin(),
						customer.getAccounts().get(0),
						customer.getAccounts().get(1)));
			}
			for (Account account : accounts.values()) {
				// write account to file
				// write account transactions to file
				List<Transaction> transactionList = transactions.get(account.getAccount());
				accountsFile.write(String.format("%s,%f\n",
						account.getAccount(),
						account.getBalance()));
				if (transactionList != null) {
					transactionList.forEach(transaction -> {
						try {
							transactionsFile.write(String.format("%s,%s,%f,%s,%s\n",
									transaction.getAccount(),
									transaction.getTarget(),
									transaction.getAmount(),
									transaction.getRequest(),
									transaction.getDate()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				}
				
			}
			for (TellerLogin teller : tellers.values()) {
				tellersFile.write(String.format("%s,%s\n",
						teller.getUsername(),
						teller.getPassword()));
			}
			transactionsFile.close();
			accountsFile.close();
			customersFile.close();
			tellersFile.close();
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
			List<Account> customerAccounts = new ArrayList<Account>();
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
						
						Request customerReq = (Request)objectIn.readObject();
						System.out.println(customerReq.getType());
						while (!customerReq.getType().equals(RequestType.LOGOUT)) {
							List<Account> customerAccounts = getAccounts(customer.getAccounts().get(0), 
																   		 customer.getAccounts().get(1));
							objectOut.writeObject(customerAccounts.get(0));
							objectOut.writeObject(customerAccounts.get(1));
							
							// handle requests (DEPOSIT/WITHDRAWAL/TRANSFER)
							customerReq = (Request)objectIn.readObject();
							System.out.println(customerReq.getType());
							if (customerReq.getType().equals(RequestType.WITHDRAW)) {
								Transaction withdrawal = (Transaction)objectIn.readObject();
								System.out.println(withdrawal.getAmount());
								addTransaction(withdrawal.getAccount(), withdrawal);
								Account account = accounts.get(withdrawal.getAccount());
								account.setBalance(account.getBalance() - withdrawal.getAmount());
								System.out.println(accounts.get(withdrawal.getAccount()).getBalance());
							}
							
							if (customerReq.getType().equals(RequestType.DEPOSIT)) {
								Transaction deposit = (Transaction)objectIn.readObject();
								System.out.println(deposit.getAmount());
								addTransaction(deposit.getAccount(), deposit);
								Account account = accounts.get(deposit.getAccount());
								account.setBalance(account.getBalance() + deposit.getAmount());
								System.out.println(accounts.get(deposit.getAccount()).getBalance());
							}
							
							if (customerReq.getType().equals(RequestType.TRANSFER)) {
								Transaction transfer = (Transaction)objectIn.readObject();
								System.out.println(transfer.getAmount());
								addTransaction(transfer.getAccount(), transfer);
								addTransaction(transfer.getTarget(), transfer);
								Account account = accounts.get(transfer.getAccount());
								Account targetAcc = accounts.get(transfer.getTarget());
								targetAcc.setBalance(account.getBalance() + transfer.getAmount());
								account.setBalance(account.getBalance() - transfer.getAmount());
								System.out.println(accounts.get(transfer.getAccount()).getBalance());
								System.out.println(accounts.get(transfer.getTarget()).getBalance());
							}
							
							if (customerReq.getType().equals(RequestType.LOGOUT)) {
								customerReq.setStatus(Status.SUCCESS);
								objectOut.writeObject(customerReq);
								save();
								break;
							}
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
						Request tellerReq = new Request(RequestType.TELLER_LOGIN);
					
						while (!tellerReq.getType().equals(RequestType.LOGOUT)) {
							Object o = objectIn.readObject();
							Account account = null;
							if (o instanceof Request) {
								tellerReq = (Request)o;
							} else {
								account = (Account)o;
							}
							
							if (tellerReq.getType().equals(RequestType.LOGOUT)) {
								tellerReq.setStatus(Status.SUCCESS);
								objectOut.writeObject(tellerReq);
								save();
								break;
							}
							
							if (!accounts.containsKey(account.getAccount())) {
								Request response = new Request(RequestType.GETACCOUNT);
								response.setStatus(Status.FAIL);
								objectOut.writeObject(response);
							} else {
								Request response = new Request(RequestType.GETACCOUNT);
								response.setStatus(Status.SUCCESS);
								objectOut.writeObject(response);
								account = accounts.get(account.getAccount());
								objectOut.writeObject(account);
								tellerReq = (Request)objectIn.readObject();
								System.out.println(tellerReq.getType());
								
								if (tellerReq.getType().equals(RequestType.WITHDRAW)) {
									Transaction withdrawal = (Transaction)objectIn.readObject();
									System.out.println(withdrawal.getAmount());
									addTransaction(withdrawal.getAccount(), withdrawal);
									account = accounts.get(withdrawal.getAccount());
									account.setBalance(account.getBalance() - withdrawal.getAmount());
									accounts.put(account.getAccount(), account);
									System.out.println(accounts.get(withdrawal.getAccount()).getBalance());
								}
								
								if (tellerReq.getType().equals(RequestType.DEPOSIT)) {
									Transaction deposit = (Transaction)objectIn.readObject();
									System.out.println(deposit.getAmount());
									addTransaction(deposit.getAccount(), deposit);
									account = accounts.get(deposit.getAccount());
									account.setBalance(account.getBalance() + deposit.getAmount());
									accounts.put(account.getAccount(), account);
									System.out.println(accounts.get(deposit.getAccount()).getBalance());
								}
								
								if (tellerReq.getType().equals(RequestType.TRANSFER)) {
									Account transferAcc = (Account)objectIn.readObject();
									if (!accounts.containsKey(transferAcc.getAccount())) {
										response = new Request(RequestType.GETACCOUNT);
										response.setStatus(Status.FAIL);
										objectOut.writeObject(response);
									} else {
										response.setStatus(Status.SUCCESS);
										objectOut.writeObject(response);
										transferAcc = accounts.get(transferAcc.getAccount());
										objectOut.writeObject(transferAcc);
										Transaction transfer = (Transaction)objectIn.readObject();
										System.out.println(transfer.getAmount());
										addTransaction(transfer.getAccount(), transfer);
										addTransaction(transfer.getTarget(), transfer);
										account = accounts.get(transfer.getAccount());
										Account targetAcc = accounts.get(transfer.getTarget());
										targetAcc.setBalance(account.getBalance() + transfer.getAmount());
										account.setBalance(account.getBalance() - transfer.getAmount());
										accounts.put(account.getAccount(), account);
										accounts.put(transferAcc.getAccount(), transferAcc);
										System.out.println(transfer.getAccount());
										System.out.println(transfer.getTarget());
									}
								}
								
								if (tellerReq.getType().equals(RequestType.CREATECUSTOMER)) {
									
								}
								
							}
						}
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