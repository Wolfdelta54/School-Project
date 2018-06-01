package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class ClientThread implements Runnable
{
	private Socket socket;
	private OutputStreamWriter clientOut;
	private PrintWriter out;
	private Table table;
	private Player player;
	private final LinkedList<String> changesToSend; // Used to store the players actions
	private boolean hasMessages = false; // Stores actionsToSend.isEmpty()
	private String change;
	private ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
	
	public ClientThread(Table table, Socket socket)
	{
		changesToSend = new LinkedList<String>();
		this.table = table;
		this.socket = socket;
	}
	
	private OutputStreamWriter getWriter()
	{
		return clientOut;
	}
	
	public void addNextChange(String change) {
		synchronized (changesToSend) {
			hasMessages = true;
			changesToSend.push(change);
			this.change = change;
		}
	}
	
	@Override
	public void run()
	{
		try {
			// setup
	//		this.clientOut = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
	//		OutputStreamWriter thatClientOut = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			@SuppressWarnings("resource")
	//		Scanner in = new Scanner(socket.getInputStream()).useDelimiter("\\A");
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	//		InputStreamReader in = new InputStreamReader(socket.getInputStream(), "UTF-8");
			
			// start communicating
			while(!socket.isClosed())
			{
			//	System.out.println(socket.getInputStream().read());
				if(in.ready())
				{
				//	String input = in.hasNext() ? in.next() : "";
					String input = in.readLine().replace("\n", "");
					// NOTE: if you want to check server can read input, uncomment next line and check server file console
					 System.out.println("Server: " + input);
				/*	for(ClientThread thatClient : table.getClients())
					{
						OutputStreamWriter thatClientOut = thatClient.getWriter();
						if(thatClientOut != null)
						{
							thatClientOut.write(input);
							thatClientOut.flush();
						}
					} */
					 
					this.table.doAction(input);
				}
				if(hasMessages)
				{
					String nextSend = "";
				//	OutputStreamWriter thatClientOut = this.table.getClients().get(this.table.getClients().indexOf(this)).getWriter();
				//	OutputStreamWriter thatClientOut = this.getWriter();
				//	OutputStreamWriter thatClientOut = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					
					synchronized(changesToSend) {
						// determines if the player as done anything
						 nextSend = changesToSend.pop();
						 hasMessages = !changesToSend.isEmpty();
						 System.out.println(nextSend);
					}
					// Prints the other players' actions to the client's terminal
					out.println(nextSend + "\n");
				}
			}
			System.out.println("While loop as been closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getPlayer() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()); // used to receive info about new players
			
			if(ois.readObject().getClass().equals("Player")) {
				this.player = (Player) ois.readObject();
				System.out.println(player.getName());
				this.table.addPlayer(this.player);
			}
			ois.close();
		} catch (IOException e) {
			// Prints the error that cause the Try statement to fail
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}