

import java.awt.*;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class RandomMaze extends Maze{
	//randomly generated maze
	private JPanel panel;
	public RandomMaze(int w, int h) {
		this.height = h;
		this.width = w;
		setNewMaze(h, w);
	}
	
	public RandomMaze() {
		this.height = 25;
		this.width = 25;
		setNewMaze(25, 25);
	}
	
	@Override
	public void setNewMaze(int w, int h) {
		//initializes mazeArray
		mazeArray = new MazeCell[h][w];
		for (int i = 0; i < mazeArray.length; i++) {
			for (int j = 0; j < mazeArray[i].length; j++) {
				mazeArray[i][j] = new MazeCell(false);
			}
		}
		
		//gives every cell 4 walls
		for (int i = 0; i < mazeArray.length; i++) {
			for (int j = 0; j < mazeArray[i].length; j++) {
				mazeArray[i][j].setWalls(new char[]{'B','R','T','L'});
				mazeArray[i][j].x = j;
				mazeArray[i][j].y = i;
			}
		}
		//generates the actual maze
		backtracker();
		JFrame frame = new JFrame("Maze");
		panel = new JPanel();
		
		//puts the maze on the frame
		for (int row = 0; row < mazeArray.length; row++) { 
	        for (int col = 0; col < mazeArray[row].length; col++){
	        	if (width > 30 || height > 30) {
	        		mazeArray[row][col].setSize(new Dimension(10, 10));
	        	} else {
	        		mazeArray[row][col].setSize(new Dimension(20, 20));
	        	}
	        }
	    }
		
		for (int row = 0; row < mazeArray.length; row++) { 
	        for (int col = 0; col < mazeArray[row].length; col++) {
				mazeArray[row][col].setVisible(true);
				mazeArray[row][col].setOpaque(true);
				mazeArray[row][col].setLocation(row * mazeArray[row][col].getHeight(), col * mazeArray[row][col].getWidth());
				panel.add(mazeArray[row][col]);
				//adds the label array to the JPanel as well as sets the location of each label on the JPanel
			}
		}
		
		//create label to save image
		JLabel saveImage = new JLabel("Save to Image");
		saveImage.setSize(100, 25);
		saveImage.setBackground(Color.magenta);
		saveImage.setOpaque(true);
		saveImage.setLocation((width * mazeArray[0][0].getHeight()) + 50,200);
		saveImage.addMouseListener(this);
		
		//add all components to the frame
		panel.setLayout(null);
		frame.add(saveImage);
		panel.setSize((mazeArray[0][0].getWidth() * this.mazeArray[0].length), (mazeArray[0][0].getHeight() * this.mazeArray.length));
		panel.setLocation(0,0);
		frame.getContentPane().add(panel);
		
		frame.setSize(1000, 1000);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		System.out.println(panel.getHeight() + " " + panel.getWidth() + "$");
	}
	
	
	public void saveToImage() {
		//saves the maze to a png image using the jpanel it's located on
		BufferedImage image = new BufferedImage((mazeArray[0][0].getWidth() * this.mazeArray[0].length), (mazeArray[0][0].getHeight() * this.mazeArray.length), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		panel.paint(g2);
		try {
			ImageIO.write(image, "png", new File(fileLocation + tag + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void backtracker() {
		//iterative backtracker algorithm to generate maze (similar to recursive backtracker except using a loop instead of recursion)
		MazeCell curCell = mazeArray[0][0];
		curCell.visited = true;
		Stack<MazeCell> visited = new Stack<MazeCell>();
		boolean start = false;
		while (visited.isEmpty() == false || start == false) {
			start = true;
			ArrayList<MazeCell> neighbours = availableNeighbours(curCell);
			MazeCell tempCell = new MazeCell();
			
			if (neighbours.size() > 0) {
				Random r = new Random();
				int rand = r.nextInt(neighbours.size());
				for (int i = 0; i < neighbours.size(); i++) {
					if (rand == i) {
						tempCell = neighbours.get(i);
					}
				}
				
				visited.push(curCell);
				
				curCell.destroyWalls(tempCell.y, tempCell.x);
				tempCell.destroyWalls(curCell.y, curCell.x);
				
				curCell = tempCell;
				curCell.visited = true;
			} else if (visited.isEmpty() == false) {
				curCell = visited.pop();
			}
		}
	}
	
	public int getSize() {
		//gets the size of the maze
		int size = 0;
		for (int i = 0; i < mazeArray.length; i++) {
			for (int j = 0; j < mazeArray[i].length; j++) {
				size++;
			}
		}
		return size;
	}
	
	public int getNumVisited() {
		//get number of visited cells
		int size = 0;
		for (int i = 0; i < mazeArray.length; i++) {
			for (int j = 0; j < mazeArray[i].length; j++) {
				if (mazeArray[i][j].visited) {
					size++;
				}
			}
		}
		return size;
	}
	
	ArrayList<MazeCell> availableNeighbours(MazeCell cell) {
		//gets list of all available neighbours of a cell;
		//an available neighbour is a cell that is adjacent to the given cell and has not been visited yet
		ArrayList<MazeCell> list = new ArrayList<MazeCell>();
		int xPos = cell.x, yPos = cell.y;
		int xMax = mazeArray[0].length - 1;
		int yMax = mazeArray.length - 1;
		if (xPos == 0 && yPos == 0) {
			list.add(mazeArray[yPos][xPos+1]);
			list.add(mazeArray[yPos+1][xPos]);
		} else if (xPos == xMax && yPos == 0) {
			list.add(mazeArray[yPos][xPos-1]);
			list.add(mazeArray[yPos+1][xPos]);
		} else if (xPos == xMax && yPos == yMax) {
			list.add(mazeArray[yPos-1][xPos]);
			list.add(mazeArray[yPos][xPos -1]);
		} else if (xPos == 0 && yPos == yMax) {
			list.add(mazeArray[yPos-1][xPos]);
			list.add(mazeArray[yPos][xPos+1]);
		} else if (xPos == 0) {
			list.add(mazeArray[yPos][xPos+1]);
			list.add(mazeArray[yPos+1][xPos]);
			list.add(mazeArray[yPos-1][xPos]);
		} else if (xPos == xMax) {
			list.add(mazeArray[yPos+1][xPos]);
			list.add(mazeArray[yPos-1][xPos]);
			list.add(mazeArray[yPos][xPos-1]);
		} else if (yPos == yMax) {
			list.add(mazeArray[yPos-1][xPos]);
			list.add(mazeArray[yPos][xPos+1]);
			list.add(mazeArray[yPos][xPos-1]);
		} else if (yPos == 0) {
			list.add(mazeArray[yPos+1][xPos]);
			list.add(mazeArray[yPos][xPos+1]);
			list.add(mazeArray[yPos][xPos-1]);
		} else {
			list.add(mazeArray[yPos][xPos+1]);
			list.add(mazeArray[yPos][xPos-1]);
			list.add(mazeArray[yPos-1][xPos]);
			list.add(mazeArray[yPos+1][xPos]);
		}
		
		int i = 0;
		while (i < list.size()) {
			if (list.get(i).visited == true) {
				list.remove(i);
			} else {
				i++;
			}
		}
		
		return list;
	}

	@Override
	public void showSolution() {
		//solution is not shown because user does not need to check for a solution, it automatically exists
	}

	@Override
	public void hideSolution() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//if the save to image label is clicked
		do {
			tag = JOptionPane.showInputDialog("Enter a name for your maze so you can access it later");
		} while (tag == null);
		//save the maze to image
		saveToImage();
	}

}