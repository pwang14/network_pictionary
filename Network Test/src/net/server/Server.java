package net.server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {
	
	private DatagramSocket socket;
	private int port;
	
	private ArrayList<InetAddress> clientIPList = new ArrayList<InetAddress>();
	private ArrayList<Integer> clientPortList = new ArrayList<Integer>();
	
	private boolean running = false;
	
	public Server(int port) {
		try {
			this.port = port;
			socket = new DatagramSocket(port);
			running = true;
			receive();
			System.out.println("Server >> Started on port " + port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void receive() {
		Thread thread = new Thread() {
			public void run() {
				try {
					while(running) {
						byte[] rdata = new byte[1024];
						DatagramPacket packet = new DatagramPacket(rdata,rdata.length);
						socket.receive(packet);
						
						if (!clientIPList.contains(packet.getAddress())) {
							clientIPList.add(packet.getAddress());
							clientPortList.add(packet.getPort());
						}
						
						String msg = new String(rdata);
						msg = msg.substring(0, msg.indexOf("/e/"));
						System.out.println(packet.getAddress().getHostAddress() + ":" + packet.getPort() + ">>" + msg);
						
						for (int i=0;i<clientIPList.size();i++) {
							DatagramPacket sentPacket = new DatagramPacket(packet.getData(), packet.getData().length, clientIPList.get(i), clientPortList.get(i));
							socket.send(sentPacket);
						}
						
						if (msg.equals("end")) {
							running = false;
						}
					}
					
					System.out.println("Server >> Server closed");
					socket.close();
					
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}; thread.start();
	}

}
