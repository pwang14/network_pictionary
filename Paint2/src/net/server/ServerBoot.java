package net.server;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ServerBoot {
	
	public ServerBoot(int port, String wordSel) {
		new Server(port, wordSel);
	}
	
	//create new Server (server)
	//ensure server has port that is not in use
	//choose word selection (usually just "normal")
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a port");
		String portIn = in.nextLine();
		System.out.println("Enter word selection");
		String wordList = in.nextLine();
		try {
			System.out.println("IP Address (after /): " + String.valueOf(InetAddress.getLocalHost()));
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int port;
		
		try {
			port = Integer.parseInt(portIn);
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("ServerBoot >> Invalid parameters. Please enter a port.");
			return;
		}
		
		new ServerBoot(port, wordList);
	}

}
