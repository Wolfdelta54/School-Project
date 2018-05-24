package application;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable 
{
	private Socket socket;
	private String userName;
	@SuppressWarnings("unused")
	private static boolean isAlive;
	private final LinkedList<String> actionsToSend;
	private boolean hasMessages = false;
	
	public ServerThread(Socket socket, String userName)
	{
		this.socket = socket;
		this.userName = userName;
		actionsToSend = new LinkedList<String>();
	}
	
	public void addNextAction(String message)
	{
		synchronized (actionsToSend) {
			hasMessages = true;
			actionsToSend.push(message);
		}
	}
	
	@Override
	public void run() 
	{
		System.out.println("Local Port : " + socket.getLocalPort());
		System.out.println("Server = " + socket.getRemoteSocketAddress());
		
		try {
			PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false); // Creates a new printwriter from the socket's output stream
			InputStream serverInStream = socket.getInputStream(); // Creates a new input stream to send action messages to the server
			@SuppressWarnings("resource")
			Scanner serverIn = new Scanner(serverInStream); // Creates a new scanner that will store the actions until they are sent to the server
			// BufferedReader userBr = new BufferedReader(new InputStreamReader(userInStream);
			// Scanner userIn = new Scanner(userInStream);
			
			// A loop that runs while the player is connected to a server
			while(!socket.isClosed())
			{
				if(serverInStream.available() > 0)
				{
					if(serverIn.hasNextLine())
					{
						// Prints the player's action to the client's terminal
						System.out.println(serverIn.nextLine());
					}
				}
				if(hasMessages)
				{
					String nextSend = "";
					
					synchronized(actionsToSend) {
						// determines if the player as done anything
						nextSend = actionsToSend.pop();
						hasMessages = !actionsToSend.isEmpty();
					}
					// Prints the other players' actions to the client's terminal
					serverOut.println(userName + " > " + nextSend);
					serverOut.flush();
				}
			}
		} catch (IOException e) {
			// Prints the error that cause the Try statement to fail
			e.printStackTrace();
		}
	}

}