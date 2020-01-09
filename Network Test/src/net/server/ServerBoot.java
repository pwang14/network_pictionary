package net.server;
import java.util.Scanner;

public class ServerBoot {
	
	public ServerBoot(int port) {
		new Server(port);
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a port");
		String portIn = in.nextLine();
		
		int port;
		
		try {
			port = Integer.parseInt(portIn);
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("ServerBoot >> Invalid parameters. Please enter a port.");
			return;
		}
		
		new ServerBoot(port);
	}

}
