package test;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class paintApp extends JFrame implements ActionListener, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
	
	private String username = "gustav";
	
	private ArrayList<String> clientUserList = new ArrayList<String>(Arrays.asList(username,"ben","kevin","andrew","willard","william","alex","frank","sayohn","garnet"));
	private ArrayList<Integer> clientScoreList = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,0,0));
	
	public static void main(String[] args) {
		paintApp application = new paintApp();
		application.setVisible(true);
		
	}
	
	private int radius = 8;
	
	Canvas mainCanv = new Canvas(radius);
	
	public paintApp() {
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
		
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		
		topContainer();
		bottomContainer();
		textFields();

	}
	
	JLabel timeLabel, wordLabel;
	
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
		wordLabel.setText("<html>Word:<br>Lord of the spuds</html>");
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
	
	public void bottomContainer() {
		JPanel tools = new JPanel();
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
	int count3 = 5;
	
	public void textFields() {
		Font font1 = new Font("Sans Serif", Font.PLAIN, 20);
		Font font2 = new Font("Sans Serif", Font.PLAIN, 15);
		
		JPanel botDisplay = new JPanel();
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
		
		for (int i=0;i<clientUserList.size();i++) {
			String username = clientUserList.get(i);
			int score = clientScoreList.get(i);
			userLog=userLog.substring(0, userLog.indexOf("</html>"));
			userLog+="<br>"+username+": "+score+"</html>";
			userDisplay.setText(userLog);
			count3--;
			if (count3<0) {
				userDisplay.setBounds(userDisplay.getX(),userDisplay.getY(),userDisplay.getWidth(),userDisplay.getHeight()+20);
			}
		}
		
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

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		mainCanv.clearLine(color,brush);
		mainCanv.updateLine(e.getX(), e.getY(), e.getX(), e.getY());
		count=1;
		oldX = e.getX();
		oldY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mainCanv.publishLine();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		mainCanv.updateLine(e.getX(), e.getY(), oldX, oldY);
		oldX = e.getX();
		oldY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
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
			mainCanv.clearTotal();
		}
		this.requestFocus();
	}
	
	boolean ctrl = false;
	int count2=18;
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode()==KeyEvent.VK_CONTROL){
			ctrl = true;
		}
		else if (e.getKeyCode()==KeyEvent.VK_Z){
			if (ctrl) {
				mainCanv.remove();
			}
		}
		else if (e.getKeyCode()==KeyEvent.VK_ENTER) {
			if (inputField.getText().length()>0) {
				String gsMsg = username+": "+inputField.getText();
				updateChat(gsMsg);
				inputField.setText("");
			}
		}
	}

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

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
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
