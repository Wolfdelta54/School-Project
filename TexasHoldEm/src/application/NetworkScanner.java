package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class NetworkScanner {
	// Solely used to Ping an IP and avoid clutter in the other files
	public NetworkScanner() {
		
	}
	
	// Ping the given IP
	public String ping(String ip) {
		String toReturn = "";
		boolean isAlive = false; // Storage for port connection
		SocketAddress socketAddress = new InetSocketAddress(ip, 4444);
		Socket socket = new Socket();
		int timeout = 2000; // Connection time out
		System.out.println("hostName: " + ip + ", port: " + 4444);
		
		try {
			socket.connect(socketAddress, timeout); // Attempt connection to port of ip
			socket.close(); // close socket to reduce computer processing
			isAlive = true; // update isAlive
			toReturn = "Connecting";
		} catch (SocketTimeoutException ex) {
			ex.printStackTrace();
			toReturn = "Server not available";
		} catch (IOException ex) {
			ex.printStackTrace();
			toReturn = "Server not available";
		}
		
		// If able to connect
		if(isAlive == true) {
			try {
				InetAddress inet = InetAddress.getByName(ip); // Test ip structure is valid
			
				System.out.println("Sending Ping Request To " + ip);
			
				long finish = 0;
				long start = new GregorianCalendar().getTimeInMillis(); // Start of connection, based on client time
				
				// If latency time is at most 1500
				if(inet.isReachable(1500)) {
					finish = new GregorianCalendar().getTimeInMillis(); // End of connection test, when client receives ping back
					long latency = finish - start; // Actual latency time of ping/connection
					System.out.println("Ping RTT: " + latency + "ms");
					toReturn = "Connecting";
				}
				else {
					System.out.println(ip + " Not reachable");
					toReturn = "Server not available";
				}
			} catch (Exception e) {
				e.printStackTrace();
				toReturn = "Server not available";
			}
		}
		
		return toReturn;
	}
}
