
import java.awt.*;

import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

public class MazeCell extends JLabel {
	//class that represents each square of the maze
	private static final long serialVersionUID = -5793510411586773322L;
	boolean topWall, rightWall, botWall, leftWall;
	final Dimension dimSize = new Dimension(20,20); //size of cell
	boolean wall; //is the cell a wall or not a wall
	int x; //x coordinate
	int y; //y coordinate
	boolean visited = false; //whether the cell has been visited (when generating maze)
	char direction;
	
	public MazeCell() {
		super();
		this.wall = false;
		this.setSize(dimSize);
	}
	
	
	public MazeCell(boolean w) {
		//w = whether this cell is a wall
		super();
		this.wall = w;
		if (w) {
			this.setBackground(Color.black);
		} else {
			this.setBackground(Color.white);
		}
		this.setVisible(true);
		this.setSize(dimSize);
	}
	
	public MazeCell(String s, boolean w) {
		super(s);
		this.wall = w;
		if (w) {
			this.setBackground(Color.black);
		} else {
			this.setBackground(Color.white);
		}
		this.setVisible(true);
		this.setSize(dimSize);
	}
	
	public void setWalls(char[] walls) {
		//sets walls based on chars given. 'T' = top wall, 'B' = bottom wall and so on
		int[] temp = {0,0,0,0};
		for (char c : walls) {
			if (c == 'T') {
				temp[0] = 1;
				topWall = true;
			} else if (c == 'L') {
				temp[1] = 1;
				rightWall = true;
			} else if (c == 'B') {
				temp[2] = 1;
				botWall = true;
			} else if (c == 'R') {
				temp[3] = 1;
				leftWall = true;
			} else {
				System.out.println("not a valid wall");
			}
			this.setBorder(new MatteBorder(temp[0], temp[1], temp[2], temp[3], Color.black));
		}
	}
	
	public void destroyWalls(int yPos, int xPos) {
		//destroys the walls based on the coordinates of the neighbouring cell.
		//if the point (xPos, yPos) is one to the left of the current cell, destroy the left wall
		MatteBorder border = (MatteBorder) this.getBorder();
		Insets insets = border.getBorderInsets();
		char dir = ' ';
		if (xPos - this.x == 1) dir = 'S';
		if (xPos - this.x == -1) dir = 'N';
		if (yPos - this.y == 1) dir = 'E';
		if (yPos - this.y == -1) dir = 'W';
		
		switch (dir) {
		case 'E': 	this.setBorder(new MatteBorder(insets.top, insets.left, insets.bottom, 0, Color.black)); 
					this.rightWall = true; break;
		case 'S': 	this.setBorder(new MatteBorder(insets.top, insets.left, 0, insets.right, Color.black));
					this.botWall = true; break;
		case 'N': 	this.setBorder(new MatteBorder(0, insets.left, insets.bottom, insets.right, Color.black));
					this.topWall = true; break;
		case 'W': 	this.setBorder(new MatteBorder(insets.top, 0, insets.bottom, insets.right, Color.black));
					this.leftWall = true; break;
		}
	}

}