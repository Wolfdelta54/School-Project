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
				System.out.println(socket.getInputStream().read());
				if(in.hasNextLine())
				{
					String input = in.nextLine();
					// NOTE: if you want to check server can read input, uncomment next line and check server file console
					// System.out.println("Server: " + input);
					for(ClientThread thatClient : table.getClients())
					{
						PrintWriter thatClientOut = thatClient.getWriter();
						if(thatClientOut != null)
						{
							thatClientOut.write(input + "\r\n");
							thatClientOut.flush();
						}
					}
					this.table.doAction(input);
				}
			}
			System.out.println("While loop as been closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}