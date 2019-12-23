import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

public abstract class Maze implements MouseListener{
	//Abstract maze class that all other mazes will inherit from
	public int width; //width of maze
	public int height; //height of maze
	public String tag; //used to save maze to file
	public static final String fileLocation = "C:/Users/bluma/"; //Change this depending on your file location (add the slash at the end)
	public MazeCell[][] mazeArray; //represents the actual maze with JLabels
	
	public Maze() {
		this.width = 25;
		this.height = 25;
	}
	
	public Maze(int w, int h) {
		this.width = mazeArray[0].length - 1;
		this.height = mazeArray.length - 1;
	}
	
	//creates a new maze
	public abstract void setNewMaze(int h, int w);
	
	//saves maze to image
	public abstract void saveToImage();
	
	//shows the solution for the maze
	public abstract void showSolution();
	
	//hides the solution for the maze
	public abstract void hideSolution();
	
	//mouseclicked will be implemented in the respective classes.
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
}
