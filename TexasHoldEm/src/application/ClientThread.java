package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable
{
	private Socket socket;
	private PrintWriter clientOut;
	private Table table;
	private Player player;
	
	public ClientThread(Table table, Socket socket)
	{
		this.table = table;
		this.socket = socket;
	}
	
	private PrintWriter getWriter()
	{
		return clientOut;
	}
	
	@Override
	public void run()
	{
		try {
			// setup
			this.clientOut = new PrintWriter(socket.getOutputStream(), false);
			@SuppressWarnings("resource")
			Scanner in = new Scanner(socket.getInputStream());
			
			// start communicating
			while(!socket.isClosed())
			{
				if(in.hasNextLine())
				{
					String input = in.nextLine();
					// NOTE: if you want to check server can read input, uncomment next line and check server file console
					// System.out.println(input);
					for(ClientThread thatClient : table.getClients())
					{
						PrintWriter thatClientOut = thatClient.getWriter();
						if(thatClientOut != null)
						{
							thatClientOut.write(input + "\r\n");
							thatClientOut.flush();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getPlayer() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()); // used to receive info about new players
			
			if(ois.readObject().getClass().equals("Player")) {
				this.player = (Player) ois.readObject();
				System.out.println(player.getName());
			}
		} catch (IOException e) {
			// Prints the error that cause the Try statement to fail
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
