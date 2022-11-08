import java.net.*;
import java.io.*;
public class Server {
	private ServerSocket server = null;
	
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
	
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		
		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}
		
		public void run() {
			BufferedReader reader = null;
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
				reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				String accNum = "";
				String pin = "";
				accNum = reader.readLine();
				pin = reader.readLine();
				System.out.println("Client acc: " + accNum);
				System.out.println("Client PIN: " + pin);
				writer.write("Received info");
				writer.newLine();
				writer.flush();
				
//				while (true) {
//					s = reader.readLine();
//					System.out.println("Client: " + s);
//					writer.write("Sent Message");
//					writer.newLine();
//					writer.flush();
//					if (s.equals("logout")) {
//						break;
//					}
//				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (writer != null) {
						writer.close();
					}
					if (reader != null) {
						reader.close();
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