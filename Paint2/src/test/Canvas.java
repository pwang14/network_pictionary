package test;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Canvas extends JComponent {
	private static final long serialVersionUID = 1L;
	
	public Canvas(int setRad) {
		setSize(1000,500);	
		radius = setRad;
	}
	
	private Color[] colorList = {new Color(155,0,0),new Color(255,0,0),new Color(225,107,0),new Color(255,165,0),new Color(255,255,0),new Color(165,255,64),new Color(0,128,0),new Color(173,216,230),new Color(40,119,215),new Color(0,0,255),new Color(215,0,215),new Color(108,0,108),new Color(255,226,174),new Color(241,194,125),new Color(94,64,13),new Color(150,150,150),new Color(104,104,104),new Color(69,69,69),new Color(15,15,15),new Color(0,0,0),Color.WHITE};
	private ArrayList<Integer[]> tempLine = new ArrayList<Integer[]>();
	private ArrayList<ArrayList<Integer[]>> totalLines = new ArrayList<ArrayList<Integer[]>>();
	private int radius;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setStroke(new BasicStroke(radius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(Color.BLACK);
		for (ArrayList<Integer[]> line : totalLines) {
			g2.setColor(colorList[line.get(0)[0]]);
			g2.setStroke(new BasicStroke(line.get(0)[1], BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			for (int i=1;i<line.size();i++) {
				g2.drawLine(line.get(i)[0], line.get(i)[1], line.get(i)[2], line.get(i)[3]);
			}
		}
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.GRAY);
		g2.drawRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	public void clearLine(int color, int width) {
		tempLine.clear();
		Integer[] strokeInd = {color, width};
		tempLine.add(strokeInd);
		totalLines.add(tempLine);
	}
	
	public void updateLine(int x1, int y1, int x2, int y2) {
		Integer[] coordinate = {x1, y1, x2, y2};
		tempLine.add(coordinate);
		totalLines.set(totalLines.size()-1, tempLine);
		repaint();
	}
	
	public void publishLine() {
		ArrayList<Integer[]> finLine = new ArrayList<Integer[]>();
		for (Integer[] coordinates : tempLine) {
			Integer[] finCoord = new Integer[coordinates.length];
			for (int i=0;i<coordinates.length;i++) {
				finCoord[i] = coordinates[i];
			}
			finLine.add(finCoord);
		}
		totalLines.set(totalLines.size()-1, finLine);
	}
	
	public void clearTotal() {
		totalLines.clear();
		repaint();
	}
	
	public void remove() {
		if (totalLines.size()>0) {
			totalLines.remove(totalLines.size()-1);
			repaint();
		}
	}
}
