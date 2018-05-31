package application;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable 
{
	private Socket socket; // Required to connect to a server
	private String userName; // The username of the player
	private final LinkedList<String> actionsToSend; // Used to store the players actions
	private boolean hasMessages = false; // Stores actionsToSend.isEmpty()
	private Player player; // Stores a player object
	private PlayerGUI gui;
	private String message = "";
	private boolean notSent = true;
	
	public ServerThread(Socket socket, String userName)
	{
		this.socket = socket;
		this.userName = userName;
		actionsToSend = new LinkedList<String>();
	}
	
	// Send the player's actions to the server
	public void addNextAction(String message)
	{
		synchronized (actionsToSend) {
			hasMessages = true;
			actionsToSend.push(message);
			this.message = message;
		}
	}
	
	// Send the new Player's info to the server
	public void addPlayer(Player player) {
		this.player = player;
	}
	
	public void addPlayerGUI(PlayerGUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void run() 
	{
		System.out.println("Local Port : " + socket.getLocalPort());
		System.out.println("Server = " + socket.getRemoteSocketAddress());
		
		try {
			OutputStreamWriter serverOut = new OutputStreamWriter(socket.getOutputStream(), "UTF-8"); // Creates a new printwriter from the socket's output stream
			InputStream serverInStream = socket.getInputStream(); // Creates a new input stream to send action messages to the server
			@SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Creates a bufferedreader that will store the actions until they are sent to the server
			// BufferedReader userBr = new BufferedReader(new InputStreamReader(userInStream);
			// Scanner userIn = new Scanner(userInStream);
			
			// A loop that runs while the player is connected to a server
			while(!socket.isClosed())
			{
				if(in.ready())
				{
					// Prints the player's action to the client's terminal
					String change = in.readLine().replace("\n", "");
					System.out.println(change);
					System.out.println("Change received from ClientThread and sent to GUI");
					this.gui.applyChange(change);
				}
				if(hasMessages)
				{
					String nextSend = "";
					
					synchronized(actionsToSend) {
						// determines if the player as done anything
						 nextSend = actionsToSend.pop();
						 hasMessages = !actionsToSend.isEmpty();
						 System.out.println(nextSend);
					}
					// Prints the other players' actions to the client's terminal
					serverOut.write(nextSend + "\n");
					serverOut.flush();
				}
			}
		} catch (IOException e) {
			// Prints the error that cause the Try statement to fail
			e.printStackTrace();
		}
	}

}
