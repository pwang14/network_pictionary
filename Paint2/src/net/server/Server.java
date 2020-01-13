package net.server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
	//word lists
	private String[] bempList = {"Nick","Sam","Mingde","Kevin","Terry","Jason","Patrick","Roham","Justin","JJ","Stephen","Vimal","Sean","Syed"};
	private String[] memeList = {"COMC","Bio Board","SHAD","IOP","stand aside","calm yourself","gachiGASM","ram ranch","NickBurgular","38C","To what extent","Byber bescurity","Encryption","loss","Peer2Peer","PROMEGALUL","pls gamble all","invade blue","hehexd","Playin DOTA 2","challenged ahri","It's okay to be thristy"};
	private String[] normalList = {"Apple","Computer","Coffee","Tree","Cheese","Pirate ship","Piano"};
	private String[] wordList;
	
	private String word;
	
	//game variables (if word has been guessed, time, drawer index, reward for correct guess)
	private boolean wordGuessed = false;
	private int timeCount = 180;
	private int playerCount = 0;
	private int scoreGiven = 100;
	
	private Timer timer = new Timer();
	
	//network variables
	private DatagramSocket socket;
	private int port;
	
	//data variables (ips, ports, usernames and scores)
	private ArrayList<InetAddress> clientIPList = new ArrayList<InetAddress>();
	private ArrayList<Integer> clientPortList = new ArrayList<Integer>();
	private ArrayList<String> clientUserList = new ArrayList<String>();
	private ArrayList<Integer> clientScoreList = new ArrayList<Integer>();
	
	private boolean running = false;
	private boolean playable = false;
	
	//create Server
	public Server(int port, String listSel) {
		try {
			this.port = port;
			//create socket to receive data
			socket = new DatagramSocket(this.port);
			running = true;
			receive();
			System.out.println("Server >> Started on port " + port);
			playerCount = (int) (Math.random()*clientIPList.size());
			
			//create timer
			timer.scheduleAtFixedRate(new TimerTask() {
				  @Override
				  public void run() {
				    if (playable) {
				    	String tMsg = "/t/"+timeCount;
				    	for (int i=0;i<clientIPList.size();i++) {
							send(tMsg,clientIPList.get(i),clientPortList.get(i));
						}
				    	timeCount--;
				    	if (timeCount<=0) {
							timeCount=180;
							wordGuessed=false;
							scoreGiven=100;
							playerCount = (playerCount+1)%clientIPList.size();
							word = wordList[(int) (Math.random()*wordList.length)];
							updateRound(clientUserList.get(playerCount),word);
						}
				    }
				  }
			}, Long.valueOf(0), Long.valueOf(1000));
			
			//set word list based on constructor parameters
			if (listSel.equals("names")) {
				wordList = bempList;
			}
			else if (listSel.equals("memes")) {
				wordList = memeList;
			}
			else if (listSel.equals("normal")) {
				wordList = normalList;
			}
			else {
				wordList = normalList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//server receives data from all clients
	private void receive() {
		//start thread that constantly listens for data
		Thread thread = new Thread() {
			public void run() {
				try {
					while(running) {
						byte[] rdata = new byte[1024];
						DatagramPacket packet = new DatagramPacket(rdata,rdata.length);
						socket.receive(packet);
						
						//msg is received
						String msg = new String(rdata);
						msg = msg.substring(0, msg.indexOf("/e/"));
						System.out.println(packet.getAddress().getHostAddress() + ":" + packet.getPort() + ">>" + msg);
						
						//new user is sent existing data
						if (msg.substring(0,3).equals("/u/")&&!clientIPList.contains(packet.getAddress())) {
							String uMsg = "/p/";
							for (int i=0;i<clientIPList.size();i++) {
								uMsg += clientUserList.get(i)+":"+clientScoreList.get(i)+",";
							}
							send(uMsg,packet.getAddress(),packet.getPort());
							
							clientIPList.add(packet.getAddress());
							clientPortList.add(packet.getPort());
							clientUserList.add(msg.substring(3));
							clientScoreList.add(0);
							
							if (clientIPList.size()>1) {
								playable = true;
								playerCount = (int) (Math.random()*clientIPList.size());
								word = wordList[(int) (Math.random()*wordList.length)];
								System.out.println(word);
								updateRound(clientUserList.get(playerCount),word);
								timeCount = 180;
							}
						}
						
						//checks if guesses are correct
						if (msg.substring(0,3).equals("/g/")&&playable) {
							msg = msg.substring(msg.indexOf(": ")+2);
							if (msg.toLowerCase().equals(word.toLowerCase())) {
								if (!wordGuessed) {
									wordGuessed = true;
									timeCount = 30;
								}
								int index = clientIPList.indexOf(packet.getAddress());
								clientScoreList.set(index, clientScoreList.get(index)+scoreGiven);
								if (scoreGiven<=25) {
									scoreGiven = 10;
								}
								else {
									scoreGiven = scoreGiven/2;
								}
								clientScoreList.set(playerCount, clientScoreList.get(playerCount)+25);
								DatagramPacket sentPacket = new DatagramPacket(packet.getData(), packet.getData().length, clientIPList.get(playerCount), clientPortList.get(playerCount));
								socket.send(sentPacket);
								DatagramPacket sentPacket2 = new DatagramPacket(packet.getData(), packet.getData().length, packet.getAddress(), packet.getPort());
								socket.send(sentPacket2);
								
								String sMsg = "/s/" + clientUserList.get(index) + "," + clientScoreList.get(index) + "|" + clientScoreList.get(playerCount);
								System.out.println(sMsg);
								for (int i=0;i<clientIPList.size();i++) {
									send(sMsg,clientIPList.get(i),clientPortList.get(i));
								}
							}
							else {
								for (int i=0;i<clientIPList.size();i++) {
									DatagramPacket sentPacket = new DatagramPacket(packet.getData(), packet.getData().length, clientIPList.get(i), clientPortList.get(i));
									socket.send(sentPacket);
								}
							}
						}
						//"end" indicates client has left
						else if (msg.equals("end")) {
							removeClient(packet.getAddress());
						}
						//for most msg, server just sends msg to all clients
						else {
							for (int i=0;i<clientIPList.size();i++) {
								DatagramPacket sentPacket = new DatagramPacket(packet.getData(), packet.getData().length, clientIPList.get(i), clientPortList.get(i));
								socket.send(sentPacket);
							}
						}
						
					}
					
					System.out.println("Server >> Server closed");
					try {
						socket.close();	
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} catch(Exception e) {
					e.printStackTrace();
					socket.close();
				}
			}
		}; thread.start();
	}
	
	//send msg to start new round
	public void updateRound(String player, String word) {
		String rMsg = "/r/"+player+","+word;
		for (int i=0;i<clientIPList.size();i++) {
			send(rMsg,clientIPList.get(i),clientPortList.get(i));
		}
	}
	
	//remove client from data lists (if no more clients, server closes)
	public void removeClient(InetAddress clientAddress) {
		for (int i=0;i<clientIPList.size();i++){
			if (clientIPList.get(i).equals(clientAddress)) {
				clientIPList.remove(i);
				clientPortList.remove(i);
			}
		}
		if (clientIPList.size()==0) {
			running=false;
			try {
				socket.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		else if (clientIPList.size()==1) {
			playable=false;
		}
	}
	
	//send string to ip address and port
	public void send(String msg, InetAddress ip, int port) {
		try {
			msg+="/e/";
			byte[] data = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
			
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
