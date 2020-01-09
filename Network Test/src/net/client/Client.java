package net.client;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
	
	private static DatagramSocket socket;
	private String ip;
	private int port;
	
	private boolean running = false;
	
	public Client() {
		try {
			running = true;
			socket = new DatagramSocket();
			receive();
			System.out.println("Client >> Started");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg, String ip, int port) {
		try {
			msg+="/e/";
			byte[] data = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(ip), port);
			
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void receive() {
		Thread thread = new Thread() {
			public void run() {
				try {
					running = true;
					while(running) {
						byte[] rdata = new byte[1024];
						DatagramPacket packet = new DatagramPacket(rdata,rdata.length);
						socket.receive(packet);
						
						String msg = new String(rdata);
						msg = msg.substring(0, msg.indexOf("/e/"));
						System.out.println("Client >> " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " >> " + msg);
						
						if (msg.equals("end")) {
							running = false;
						}
					}
					System.out.println("Client >> Server closed");
					socket.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}; thread.start();
	}
	
	public boolean getRunning() {
		return running;
	}

}
