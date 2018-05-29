package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientThread implements Runnable
{
	private Socket socket;
	private OutputStreamWriter clientOut;
	private Table table;
	private Player player;
	
	public ClientThread(Table table, Socket socket)
	{
		this.table = table;
		this.socket = socket;
	}
	
	private OutputStreamWriter getWriter()
	{
		return clientOut;
	}
	
	@Override
	public void run()
	{
		try {
			// setup
			this.clientOut = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
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