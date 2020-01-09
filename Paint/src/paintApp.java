import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

public class paintApp extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		paintApp application = new paintApp();
		application.setVisible(true);
	}
	
	int radius = 5;
	
	Canvas mainCanv = new Canvas(radius);
	
	public paintApp() {
		setSize(1080,720);
		setLayout(null);
		setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.DARK_GRAY);
		
		JButton reset = new JButton("Reset");
		add(reset);
		reset.setBounds(500, 20, 80, 40);
		reset.addActionListener(this);
		
		add(mainCanv);
		mainCanv.setLocation(0,80);
		mainCanv.addMouseListener(this);
		mainCanv.addMouseMotionListener(this);
	}
	
	boolean released;
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
		mainCanv.addCircle(e.getX()-radius, e.getY()-radius);
		count=1;
		oldX = e.getX();
		oldY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	double slope, yinter;
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		mainCanv.addCircle(e.getX()-radius, e.getY()-radius);
		slope = (double) (e.getY()-oldY)/(e.getX()-oldX);
		yinter = oldY - oldX*slope;
		for (int i=1;i<Math.abs(e.getX()-oldX);i++) {
			int x,y;
			if (e.getX()>oldX) x = oldX+i;
			else x = oldX-i;
			y = (int) (slope*x+yinter);
			mainCanv.addCircle(x-radius, y-radius);
		}
		for (int i=1;i<Math.abs(e.getY()-oldY);i++) {
			int x,y;
			if (e.getY()>oldY) y = oldY+i;
			else y = oldY-i;
			x = (int) ((y-yinter)/slope);
			if (Math.abs(e.getX()-oldX)<0.1) x = oldX;
			mainCanv.addCircle(x-radius, y-radius);
		}
		oldX = e.getX();
		oldY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		mainCanv.clear();
	}
	
}
