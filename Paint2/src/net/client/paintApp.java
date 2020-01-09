package net.client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.event.KeyListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class paintApp extends JFrame implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	private static final long serialVersionUID = 1L;
	
	//network variables
	private static DatagramSocket socket;
	private String ip;
	private int port;
	private String username;
	
	//data variables (usernames and scores)
	private ArrayList<String> clientUserList = new ArrayList<String>();
	private ArrayList<Integer> clientScoreList = new ArrayList<Integer>();
	
	private boolean running = false;
	
	//game variables (brush width, drawer username, time, chosen word)
	private int radius = 8;
	private String drawerStr;
	private int timeCount = 180;
	private String word;
	private boolean drawer = false;
	
	//new canvas to draw on
	Canvas mainCanv = new Canvas(radius);
	
	JPanel tools, botDisplay;
	
	//create paintApp, with proper properties and listeners
	public paintApp(String ip, int port, String username) {
		setSize(1350,720);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		
		add(mainCanv);
		mainCanv.setLocation(this.getWidth()-mainCanv.getWidth(),80);
		mainCanv.addMouseListener(this);
		mainCanv.addMouseMotionListener(this);

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				send("end",ip,port);
			}
		});
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		
		topContainer();
		bottomContainer();
		textFields();
		
		drawer = false;
		botDisplay.setVisible(true);
		tools.setVisible(false);
		
		this.ip = ip;
		this.port = port;
		this.username = username;
		
		try {
			//open socket to receive data
			running = true;
			socket = new DatagramSocket();
			receive();
			System.out.println("Client >> Started");
			send("/u/" + username,ip,port);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	JLabel timeLabel, wordLabel;
	
	//top container (timer, word chosen)
	public void topContainer() {
		JPanel timerDisplay = new JPanel();
		timerDisplay.setLayout(null);
		timerDisplay.setSize(mainCanv.getWidth(),mainCanv.getY()+2);
		timerDisplay.setBackground(Color.WHITE);
		add(timerDisplay);
		timerDisplay.setLocation(this.getWidth()-mainCanv.getWidth(),-1);
		timerDisplay.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
		
		Font font1 = new Font("Sans Serif", Font.PLAIN, 30);
		timeLabel = new JLabel();
		timerDisplay.add(timeLabel);
		timeLabel.setFont(font1);
		timeLabel.setVerticalAlignment(JLabel.TOP);
		timeLabel.setBounds(timerDisplay.getWidth()/2,20,100,40);
		timeLabel.setForeground(Color.BLUE);
		timeLabel.setText("3:00");
		
		Font font2 = new Font("Sans Serif", Font.PLAIN, 15);
		wordLabel = new JLabel();
		timerDisplay.add(wordLabel);
		wordLabel.setFont(font2);
		wordLabel.setVerticalAlignment(JLabel.TOP);
		wordLabel.setBounds(20,20,500,50);
		wordLabel.setForeground(Color.BLACK);
		wordLabel.setText("<html>Word:<br></html>");
		wordLabel.setVisible(false);
	}
	
	JButton redDark, red, orange, orangeLight, yellow, greenLight, green, blueLight, blue, blueDark, pink, purple, skinLight, skinTan, skinDark, grayVLight, grayLight, gray, grayDark, black;
	JButton brushVSmall, brushSmall, brushMedSmall, brushMedLarge, brushLarge, brushVLarge;
	JButton paintBrush, eraser, delete;
	
	private JButton[] colorButtons = {redDark, red, orange, orangeLight, yellow, greenLight, green, blueLight, blue, blueDark, pink, purple, skinLight, skinTan, skinDark, grayVLight, grayLight, gray, grayDark, black};
	private JButton[] brushButtons = {brushVSmall, brushSmall, brushMedSmall, brushMedLarge, brushLarge, brushVLarge};
	private String[] brushImg = {"VSmall","Small","SMed","LMed","Large","VLarge"};
	
	private int[] colors = {155,0,0,255,0,0,225,107,0,255,165,0,255,255,0,165,255,64,0,128,0,173,216,230,40,119,215,0,0,255,215,0,215,108,0,108,255,226,174,241,194,125,94,64,13,150,150,150,104,104,104,69,69,69,15,15,15,0,0,0};
	private int[] brushes = {2,4,8,16,32,64};
	
	int color=19;
	int prevColor=19;
	int brush=8;
	
	//bottom container (drawing tools)
	public void bottomContainer() {
		tools = new JPanel();
		tools.setLayout(null);
		tools.setSize(mainCanv.getWidth(),this.getHeight()-mainCanv.getY()-mainCanv.getHeight());
		tools.setBackground(Color.WHITE);
		add(tools);
		tools.setLocation(this.getWidth()-mainCanv.getWidth(),mainCanv.getY()+mainCanv.getHeight());
		tools.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
		
		for (int i=0;i<10;i++) {
			colorButtons[i] = new JButton();
			tools.add(colorButtons[i]);
			colorButtons[i].setBounds(i*50+20,10,40,40);
			colorButtons[i].setBackground(new Color(colors[i*3],colors[i*3+1],colors[i*3+2]));
			colorButtons[i].addActionListener(this);
			
			colorButtons[i+10] = new JButton();
			tools.add(colorButtons[i+10]);
			colorButtons[i+10].setBounds(i*50+20,60,40,40);
			colorButtons[i+10].setBackground(new Color(colors[i*3+30],colors[i*3+31],colors[i*3+32]));
			colorButtons[i+10].addActionListener(this);
		}
		
		for (int i=0;i<3;i++) {
			brushButtons[i] = new JButton();
			tools.add(brushButtons[i]);
			brushButtons[i].setBounds(i*50+530,10,40,40);
			brushButtons[i].setBackground(Color.GRAY);
			brushButtons[i].addActionListener(this);
			
			brushButtons[i+3] = new JButton();
			tools.add(brushButtons[i+3]);
			brushButtons[i+3].setBounds(i*50+530,60,40,40);
			brushButtons[i+3].setBackground(Color.GRAY);
			brushButtons[i+3].addActionListener(this);
			
			try {
				Image img = ImageIO.read(new File("images//"+brushImg[i]+".png")).getScaledInstance(40, -1, Image.SCALE_SMOOTH);
				brushButtons[i].setIcon(new ImageIcon(img));
				Image img2 = ImageIO.read(new File("images//"+brushImg[i+3]+".png")).getScaledInstance(40, -1, Image.SCALE_SMOOTH);
				brushButtons[i+3].setIcon(new ImageIcon(img2));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		paintBrush = new JButton();
		tools.add(paintBrush);
		paintBrush.setBounds(695, 15, 80, 80);
		paintBrush.setBackground(Color.GRAY);
		paintBrush.addActionListener(this);
		
		eraser = new JButton();
		tools.add(eraser);
		eraser.setBounds(790, 15, 80, 80);
		eraser.setBackground(Color.LIGHT_GRAY);
		eraser.addActionListener(this);
		
		delete = new JButton();
		tools.add(delete);
		delete.setBounds(885, 15, 80, 80);
		delete.setBackground(Color.DARK_GRAY);
		delete.addActionListener(this);
		
		try {
			Image img = ImageIO.read(new File("images//Paintbrush.png")).getScaledInstance(80, -1, Image.SCALE_SMOOTH);
			paintBrush.setIcon(new ImageIcon(img));
			Image img2 = ImageIO.read(new File("images//Eraser.png")).getScaledInstance(80, -1, Image.SCALE_SMOOTH);
			eraser.setIcon(new ImageIcon(img2));
			Image img3 = ImageIO.read(new File("images//Delete.png")).getScaledInstance(80, -1, Image.SCALE_SMOOTH);
			delete.setIcon(new ImageIcon(img3));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	JTextField inputField;
	JLabel chatDisplay, userDisplay;
	String chatLog = "<html></html>", userLog = "<html></html>";
	
	//text fields (chat, user display)
	public void textFields() {
		Font font1 = new Font("Sans Serif", Font.PLAIN, 20);
		Font font2 = new Font("Sans Serif", Font.PLAIN, 15);
		
		botDisplay = new JPanel();
		botDisplay.setLayout(null);
		botDisplay.setSize(this.getWidth()-mainCanv.getWidth()+2, this.getHeight()-mainCanv.getY()-mainCanv.getHeight());
		botDisplay.setBackground(Color.WHITE);
		add(botDisplay);
		botDisplay.setLocation(-1,mainCanv.getY()+mainCanv.getHeight());
		botDisplay.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
		
		inputField = new JTextField();
		botDisplay.add(inputField);
		inputField.setFont(font1);
		inputField.setBounds(20, 35, this.getWidth()-mainCanv.getWidth()-38, 40);
		inputField.setForeground(Color.DARK_GRAY);
		inputField.addKeyListener(this);
		
		JPanel midDisplay = new JPanel();
		midDisplay.setLayout(null);
		midDisplay.setSize(this.getWidth()-mainCanv.getWidth()+2, mainCanv.getHeight()-74);
		midDisplay.setBackground(Color.WHITE);
		add(midDisplay);
		midDisplay.setLocation(-1,mainCanv.getY()+75);
		midDisplay.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,1));
		
		chatDisplay = new JLabel();
		midDisplay.add(chatDisplay);
		chatDisplay.setFont(font2);
		chatDisplay.setVerticalAlignment(JLabel.TOP);
		chatDisplay.setBounds(20,5,this.getWidth()-mainCanv.getWidth()-38, mainCanv.getHeight()-114);
		chatDisplay.setForeground(Color.DARK_GRAY);
		chatDisplay.setText("<html></html>");
		chatDisplay.addMouseWheelListener(this);
		
		JPanel topDisplay = new JPanel();
		topDisplay.setLayout(null);
		topDisplay.setSize(this.getWidth()-mainCanv.getWidth()+2, 157);
		topDisplay.setBackground(Color.WHITE);
		add(topDisplay);
		topDisplay.setLocation(-1,-1);
		topDisplay.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,1));
		
		userDisplay = new JLabel();
		topDisplay.add(userDisplay);
		userDisplay.setFont(font2);
		userDisplay.setVerticalAlignment(JLabel.TOP);
		userDisplay.setBounds(20,5,this.getWidth()-mainCanv.getWidth()-38,mainCanv.getHeight()-154);
		userDisplay.setForeground(Color.DARK_GRAY);
		userDisplay.setText("<html></html>");
		userDisplay.addMouseWheelListener(this);
	}
	
	int oldX, oldY;
	int count=0;
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	//start line (command sent to server)
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (drawer) {
			//mainCanv.clearLine(color,brush);
			String clMsg = "/d/cL"+color+","+brush+"|";
			send(clMsg,ip,port);
			//mainCanv.updateLine(e.getX(), e.getY(), e.getX(), e.getY());
			String ulMsg = "/d/uL"+e.getX()+","+e.getY()+","+e.getX()+","+e.getY()+"|";
			send(ulMsg,ip,port);
			count=1;
			oldX = e.getX();
			oldY = e.getY();
		}
	}

	//publish line (command sent to server)
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (drawer) {
			//mainCanv.publishLine();
			String plMsg = "/d/pL|";
			send(plMsg,ip,port);
		}
	}
	
	//update line (command sent to server)
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (drawer) {
			//mainCanv.updateLine(e.getX(), e.getY(), oldX, oldY);
			String ulMsg = "/d/uL"+e.getX()+","+e.getY()+","+oldX+","+oldY+"|";
			send(ulMsg,ip,port);
			oldX = e.getX();
			oldY = e.getY();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	//drawing tools (change color, clear canvas)
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (drawer) {
			for (int i=0;i<colorButtons.length;i++) {
				if (e.getSource()==colorButtons[i]) {
					prevColor=color;
					color=i;
				}
			}
			for (int i=0;i<brushButtons.length;i++) {
				if (e.getSource()==brushButtons[i]) {
					brush=brushes[i];
				}
			}
			if (e.getSource()==paintBrush) {
				if (color==20) {
					color=prevColor;
				}
			}
			else if (e.getSource()==eraser) {
				prevColor=color;
				color=20;
			}
			else if (e.getSource()==delete) {
				String tcMsg = "/c/tc|";
				send(tcMsg,ip,port);
			}
			this.requestFocus();
		}
	}
	
	boolean ctrl = false;
	
	//undo (ctrl-z) (command sent to server)
	//update chat log (command sent to server)
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (drawer) {
			if (e.getKeyCode()==KeyEvent.VK_CONTROL){
				ctrl = true;
			}
			else if (e.getKeyCode()==KeyEvent.VK_Z){
				if (ctrl) {
					String reMsg = "/c/re|";
					send(reMsg,ip,port);
				}
			}
		}
		else {
			if (e.getKeyCode()==KeyEvent.VK_ENTER) {
				if (inputField.getText().length()>0) {
					String gsMsg = "/g/" + username + ": " + inputField.getText();
					send(gsMsg,ip,port);
					inputField.setText("");
				}
			}
		}
	}

	//check for valid undo (ctrl-z)
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode()==KeyEvent.VK_CONTROL){
			ctrl = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//scroll through chat or user display
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		if (!drawer) {
			if (e.getSource()==chatDisplay) {
				if (chatDisplay.getHeight()>mainCanv.getHeight()-114) {
					chatDisplay.setLocation(chatDisplay.getX(),chatDisplay.getY()-10*e.getWheelRotation());
					if (chatDisplay.getY()>=5) {
						chatDisplay.setLocation(chatDisplay.getX(), 5);
					}
					else if (chatDisplay.getY()+chatDisplay.getHeight()<=mainCanv.getHeight()-109) {
						chatDisplay.setLocation(chatDisplay.getX(), mainCanv.getHeight()-109-chatDisplay.getHeight());
					}
				}
			}
			else if (e.getSource()==userDisplay) {
				if (userDisplay.getHeight()>mainCanv.getHeight()-154) {
					userDisplay.setLocation(userDisplay.getX(),userDisplay.getY()-10*e.getWheelRotation());
					if (userDisplay.getY()>=5) {
						userDisplay.setLocation(userDisplay.getX(), 5);
					}
					else if (userDisplay.getY()+userDisplay.getHeight()<=mainCanv.getHeight()-149) {
						userDisplay.setLocation(userDisplay.getX(), mainCanv.getHeight()-149-userDisplay.getHeight());
					}
				}
			}
		}
	}
	
	//send string to ip address and port (server receives specifically coded messages)
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
	
	int count2 = 18, count3 = 5;
	
	//server sends received data to all clients
	//data starts with specific characters indicating how the data is to be processed
	private void receive() {
		//start thread that constantly listens for data
		Thread thread = new Thread() {
			public void run() {
				try {
					running = true;
					while(running) {
						byte[] rdata = new byte[1024];
						DatagramPacket packet = new DatagramPacket(rdata,rdata.length);
						socket.receive(packet);
						
						//msg is received
						String msg = new String(rdata);
						msg = msg.substring(0, msg.indexOf("/e/"));
						
						//"/d/" indicates line data (uL: update line, cL: clear line, pL: publish line)
						if (msg.substring(0,3).equals("/d/")) {
							msg = msg.substring(3);
							if (msg.substring(0,2).equals("uL")) {
								msg = msg.substring(2);
								int x1 = Integer.parseInt(msg.substring(0,msg.indexOf(",")));
								msg = msg.substring(msg.indexOf(",")+1);
								int y1 = Integer.parseInt(msg.substring(0,msg.indexOf(",")));
								msg = msg.substring(msg.indexOf(",")+1);
								int x2 = Integer.parseInt(msg.substring(0,msg.indexOf(",")));
								msg = msg.substring(msg.indexOf(",")+1);
								int y2 = Integer.parseInt(msg.substring(0,msg.indexOf("|")));
								mainCanv.updateLine(x1, y1, x2, y2);
							}
							else if (msg.substring(0,2).equals("cL")) {
								msg = msg.substring(2);
								int recColor = Integer.parseInt(msg.substring(0,msg.indexOf(",")));
								msg = msg.substring(msg.indexOf(",")+1);
								int recBrush = Integer.parseInt(msg.substring(0,msg.indexOf("|")));
								mainCanv.clearLine(recColor,recBrush);
							}
							else if (msg.substring(0,2).equals("pL")) {
								mainCanv.publishLine();
							}
						}
						//"/c/" indicates clearing strokes (re: single stroke, tc: total clear)
						else if (msg.substring(0,3).equals("/c/")) {
							msg = msg.substring(3);
							if (msg.substring(0,2).equals("re")) {
								mainCanv.remove();
							}
							else if (msg.substring(0,2).equals("tc")) {
								mainCanv.clearTotal();
							}
						}
						//"/g/" indicates any message sent to chat (a guess)
						else if (msg.substring(0,3).equals("/g/")) {
							msg = msg.substring(3);
							updateChat(msg);
						}
						//"/p/" indicates existing user scores (when joining server)
						else if (msg.substring(0,3).equals("/p/")){
							msg = msg.substring(3);
							userLog=userLog.substring(0, userLog.indexOf("</html>"));
							while(msg.length()>0) {
								String username = msg.substring(0, msg.indexOf(":"));
								clientUserList.add(username);
								userLog+="<br>"+username+":";
								msg = msg.substring(msg.indexOf(":")+1);
								int score = Integer.valueOf(msg.substring(0,msg.indexOf(",")));
								clientScoreList.add(score);
								userLog+=" "+score;
								msg = msg.substring(msg.indexOf(",")+1);
								count3--;
								if (count3<0) {
									userDisplay.setSize(userDisplay.getWidth(),userDisplay.getHeight()+20);
								}
							}
							userLog+="</html>";
							userDisplay.setText(userLog);
						}
						//"/u/" indicates a new user
						else if (msg.substring(0,3).equals("/u/")) {
							msg = msg.substring(3);
							clientUserList.add(msg);
							clientScoreList.add(0);
							userLog=userLog.substring(0, userLog.indexOf("</html>"));
							userLog+="<br>"+msg+": 0</html>";
							userDisplay.setText(userLog);
							count3--;
							if (count3<0) {
								userDisplay.setSize(userDisplay.getWidth(),userDisplay.getHeight()+20);
							}
							
							updateChat("Server >> "+msg+" has joined the game.");
						}
						
						//"/s/" indicates a new player score
						else if (msg.substring(0,3).equals("/s/")) {
							msg = msg.substring(3);
							clientScoreList.set(clientUserList.indexOf(msg.substring(0, msg.indexOf(","))), Integer.valueOf(msg.substring(msg.indexOf(",")+1,msg.indexOf("|"))));
							clientScoreList.set(clientUserList.indexOf(drawerStr),Integer.valueOf(msg.substring(msg.indexOf("|")+1)));
							userLog = "<html>";
							for (int i=0;i<clientUserList.size();i++) {
								userLog += "<br>"+clientUserList.get(i)+": "+clientScoreList.get(i);
							}
							userLog += "</html>";
							userDisplay.setText(userLog);
							
							updateChat("Server >> "+msg.substring(0,msg.indexOf(","))+" guessed the word.");
						}
						//"/t/" indicates updated time
						else if (msg.substring(0,3).equals("/t/")) {
							msg = msg.substring(3);
							timeCount = Integer.valueOf(msg);
							String time;
							if (timeCount%60>=10) {
								time = String.valueOf(timeCount/60)+":"+String.valueOf(timeCount%60);
							}
							else {
								time = String.valueOf(timeCount/60)+":0"+String.valueOf(timeCount%60);
							}
							timeLabel.setText(time);
						}
						//"/r/" indicates round over
						else if (msg.substring(0,3).equals("/r/")) {
							String wMsg = "Round over. The word was " + word;
							updateChat(wMsg);
							
							msg = msg.substring(3);
							drawerStr = msg.substring(0,msg.indexOf(","));
							if (drawerStr.equals(username)) {
								drawer = true;
								botDisplay.setVisible(false);
								tools.setVisible(true);
								wordLabel.setVisible(true);
								mainCanv.clearTotal();
							}
							else {
								drawer = false;
								botDisplay.setVisible(true);
								tools.setVisible(false);
								wordLabel.setVisible(false);
								mainCanv.clearTotal();
							}
							String uMsg = "It is " + drawerStr + "'s turn to draw.";
							updateChat(uMsg);
							word = msg.substring(msg.indexOf(",")+1); 
							String wDis = "<html>Word:<br>"+word+"</html>";
							wordLabel.setText(wDis);
							timeCount = 180;
						}
						//if msg has no specific prefix, simply display msg in chat
						else {
							updateChat(msg);
						}
						
						//if msg is "end", stop client
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
	
	//update chat with proper formatting
	public void updateChat(String msg) {
		chatLog=chatLog.substring(0, chatLog.indexOf("</html>"));
		while (msg.length()>40) {
			chatLog+="<br>"+msg.substring(0, 40);
			msg = msg.substring(40);
			count2--;
		}
		if (msg.length()>0) {
			chatLog+="<br>"+msg;
			count2--;
		}
		chatLog+="</html>";
		chatDisplay.setText(chatLog);
		int height = 20;
		String tempLog = chatLog;
		while (tempLog.indexOf("<br>")>=0) {
			tempLog = tempLog.substring(tempLog.indexOf("<br>")+4);
			height += 20;
		}
		if (count2<0) {
			chatDisplay.setBounds(chatDisplay.getX(),40+this.getWidth()-mainCanv.getWidth()-height,chatDisplay.getWidth(),height);
		}
	}
	
}
