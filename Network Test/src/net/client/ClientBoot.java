package net.client;
import java.util.Scanner;

public class ClientBoot {
	
	private static String ip = "localhost";
	private static int port = 1234;
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		System.out.println("Enter an IP address");
		ip = in.nextLine();
		System.out.println("Enter a port");
		port = in.nextInt();
		
		Client client = new Client();
		String msg;
		
		while(client.getRunning()) {
			msg = in.nextLine();
			client.send(msg, ip, port);
		}
	}
}
