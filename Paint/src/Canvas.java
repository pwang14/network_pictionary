import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Canvas extends JComponent {
	private static final long serialVersionUID = 1L;
	
	public Canvas(int setRad) {
		setSize(1080,640);	
		radius = setRad;
	}
	
	private ArrayList<Integer[]> circles = new ArrayList<Integer[]>();
	private int radius;
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		for (Integer[] coordinate : circles) {
			g.fillOval(coordinate[0], coordinate[1], 2*radius, 2*radius);
		}
	}
	
	public void addCircle(int x, int y) {
		Integer[] coordinate = {x, y};
		circles.add(coordinate);
		repaint();
	}
	
	public void clear() {
		circles.clear();
		repaint();
	}
	
	public void remove(int i) {
		if (i>circles.size()-1) i = circles.size()-1;
		else if (i<0) i=0;
		circles.remove(circles.size()-1-i);
		repaint();
	}
}
