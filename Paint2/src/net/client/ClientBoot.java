package net.client;

import java.util.Scanner;

public class ClientBoot {
	
	private static String ip = "localhost";
	private static int port = 1234;
	private static String username = "gustav";
	
	//create a new paintApp (client)
	//ensure client is connected to proper ip address and port
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		System.out.println("Enter a username");
		username = in.nextLine();
		System.out.println("Enter an IP address");
		ip = in.nextLine();
		System.out.println("Enter a port");
		port = in.nextInt();
		
		paintApp client = new paintApp(ip,port,username);
		client.setVisible(true);
	}
}
