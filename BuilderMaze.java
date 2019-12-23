import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
public class BuilderMaze extends Maze implements MouseListener{
	int[][] maze; //array of ints representing the squares of the maze. Used mostly when opening maze from file and when generating solution
	boolean[][] wasHere = new boolean[width][height]; //used in method to generate solution.
	boolean[][] correctPath = new boolean[width][height]; // The solution to the maze
	int startX, startY; // Starting X and Y values of maze
	int endX = width - 1, endY = height - 1; //end values of maze, needed for solution method
	JPanel myPanel = new JPanel(); //panel to store maze
	
	public BuilderMaze() {
		//default constructor, makes an empty maze that is 25 by 25
		width = 25;
		height = 25;
		this.tag = "defaultMaze";
		this.wasHere = new boolean[width][height];
		this.correctPath = new boolean[width][height];
		this.startX = 0;
		this.startY = 0;
		this.endX = width - 1;
		this.endY = height - 1;
		setNewMaze(25, 25);
	}
	
	public BuilderMaze(int w, int h) {
		//takes a width and a height parameter
		this.width = w;
		this.height = h;
		setNewMaze(w, h);
		this.wasHere = new boolean[width][height];
		this.correctPath = new boolean[width][height];
		this.startX = 0;
		this.startY = 0;
		this.endX = width - 1;
		this.endY = height - 1;
	}
	
	public BuilderMaze(File file) throws FileNotFoundException {
		//takes a file as a parameter and generates a maze from the file.
		//the file contains the width, height and a whole bunch of 1s and 2s representing either walls or not walls
		Scanner in = new Scanner(new FileReader(file));
		this.width = in.nextInt();
		this.height = in.nextInt();
		int[][] fileMaze = new int[width][height];
		
		for (int i = 0; i < fileMaze.length; i++) {
			for (int j = 0; j < fileMaze[i].length; j++) {
				fileMaze[i][j] = in.nextInt();
			}
		}
		in.close();
		this.maze = fileMaze;
		this.wasHere = new boolean[width][height];
		this.correctPath = new boolean[width][height];
		this.startX = 0;
		this.startY = 0;
		this.endX = width - 1;
		this.endY = height - 1;
	}
	
	public void startMaze() {
		//generate new maze
	    for (int row = 0; row < maze.length; row++) { 
	        // Sets boolean Arrays to default values
	        for (int col = 0; col < maze[row].length; col++){
	            wasHere[row][col] = false;
	            correctPath[row][col] = false;
	        }
	    }
	    boolean b = solve(0, 0);
	    // Will leave you with a boolean array (correctPath) 
	    // with the path indicated by true values.
	    // If b is false, there is no solution to the maze
	    buildMaze();
	}
	
	public boolean solve(int x, int y) {
		//recursively solves maze
		if (maze[maze.length -1][maze[0].length -1] == 2) return false;
	    if (x == endX && y == endY) return true; // If you reached the end
	    if (maze[x][y] == 2 || wasHere[x][y]) return false;  
	    // If you are on a wall or already were here
	    wasHere[x][y] = true;
	    if (x != 0) // Checks if not on left edge
	        if (solve(x-1, y)) { // Recalls method one to the left
	            correctPath[x][y] = true; // Sets that path value to true;
	            return true;
	        }
	    if (x != width - 1) // Checks if not on right edge
	        if (solve(x+1, y)) { // Recalls method one to the right
	            correctPath[x][y] = true;
	            return true;
	        }
	    if (y != 0)  // Checks if not on top edge
	        if (solve(x, y-1)) { // Recalls method one up
	            correctPath[x][y] = true;
	            return true;
	        }
	    if (y != height - 1) // Checks if not on bottom edge
	        if (solve(x, y+1)) { // Recalls method one down
	            correctPath[x][y] = true;
	            return true;
	        }
	    return false;
	}
	
	public void buildMaze() {
		//actually builds maze using JLabels
		JFrame frame = new JFrame("Maze");
		mazeArray = new MazeCell[maze.length][maze[1].length];
		//fills mazeArray with labels using maze[][]. Label is black if wall, white if not wall
		for (int row = 0; row< maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				mazeArray[row][col] = new MazeCell(maze[row][col]==2);
			}
		}
		
		//sets the x value and y value for labels in mazeArray as well as sets the size for them
		for (int row = 0; row < maze.length; row++) { 
	        for (int col = 0; col < maze[row].length; col++){
	           mazeArray[row][col].x = row;
	           mazeArray[row][col].y = col;
	           if (width > 30 || height > 30) {
	        		mazeArray[row][col].setSize(new Dimension(10, 10));
	        	}
	        }
	    }
		
		//add each square in the maze to the jpanel
		for (int row = 0; row < maze.length; row++) { 
	        for (int col = 0; col < maze[row].length; col++){
	           myPanel.add(mazeArray[row][col]);
	        }
	    }
		
		//adds mouselistener for each square in the maze
		for (int row = 0; row < maze.length; row++) { 
	        for (int col = 0; col < maze[row].length; col++){
	           mazeArray[row][col].addMouseListener(this);
	        }
	    }
		
		//actually makes each square in the maze visible and sets its location
		for (int row = 0; row < maze.length; row++) { 
	        for (int col = 0; col < maze[row].length; col++){
	        	
				mazeArray[row][col].setVisible(true);
				mazeArray[row][col].setOpaque(true);
				mazeArray[row][col].setLocation(row * mazeArray[row][col].getHeight(), col * mazeArray[row][col].getWidth());
				myPanel.add(mazeArray[row][col]);
				//adds the label array to the JPanel as well as sets the location of each label on the JPanel
			}
		}
		
		//button to export maze
		MazeCell label = new MazeCell("Export Maze", false);
		label.setSize(100, 25);
		label.setBackground(Color.CYAN);
		label.setOpaque(true);
		label.setLocation((width * mazeArray[0][0].getHeight()) + 50, 200);
		label.addMouseListener(this);
		
		//button to show solution
		MazeCell solution = new MazeCell("Solution", false);
		solution.setSize(100,25);
		solution.setBackground(Color.RED);
		solution.setOpaque(true);
		solution.setLocation((width * mazeArray[0][0].getHeight()) + 50, 75);
		solution.addMouseListener(this);
		
		//button to save to image
		MazeCell saveImage = new MazeCell("Save to Image", false);
		saveImage.setSize(100, 25);
		saveImage.setBackground(Color.MAGENTA);
		saveImage.setOpaque(true);
		saveImage.setLocation((width * mazeArray[0][0].getHeight()) + 50, 325);
		saveImage.addMouseListener(this);
		
		Random r = new Random();
		//label with information
		JLabel info = new JLabel("<html> Note that the 'Solution' button will give you an accurate solution if your maze is complete, i.e. there are no big open empty spaces </html>");
		info.setSize(400, 70);
		info.setOpaque(true);
		info.setBackground(new Color(r.nextInt(156)+100,r.nextInt(156)+100,r.nextInt(156)+100));
		info.setLocation(width * mazeArray[0][0].getHeight() + 50, 400);
	
		//adds the maze and labels to the frame
		myPanel.setLayout(null);
		frame.add(label);
		frame.add(solution);
		frame.add(saveImage);
		frame.add(info);
		frame.getContentPane().add(myPanel);
		frame.setSize(1300, 1000);
		myPanel.setSize((width * this.maze[0].length) + 200, (mazeArray[0][0].getHeight() * this.maze.length) + 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//gets the label that was clicked
		MazeCell curCell = ((MazeCell) (e.getSource()));
		
		//if the cell that was clicked is part of the maze
		if (curCell.getSize().equals(mazeArray[0][0].getSize())) {
			//if the square was a wall, change it to not a wall, if it's not a wall, change it to a wall
			//walls are black and not walls are white
			if (curCell.getBackground() == Color.white) {
				curCell.setBackground(Color.black);
				curCell.wall = true;
				maze[curCell.x][curCell.y] = 2;
				wasHere[curCell.x][curCell.y] = !(wasHere[curCell.x][curCell.y]);
				correctPath[curCell.x][curCell.y] = !(correctPath[curCell.x][curCell.y]);
				clearArrays();
			} else if (curCell.getBackground() == Color.black) {
				curCell.setBackground(Color.white);
				curCell.wall = false;
				maze[curCell.x][curCell.y] = 1;
				clearArrays();
			}
		}
		
		//if the label that was clicked was the export maze label
		if (((JLabel) e.getSource()).getText().equals("Export Maze")) {
			if (JOptionPane.showConfirmDialog(null, "Are you sure you want to save?") == JOptionPane.OK_OPTION) {
				try {
					//save the maze to a file
					this.saveToFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			//if the label that was clicked was the solution label
		} else if (curCell.getText().equals("Solution")) {
			if (solve(0,0) == false) {
				//if there is no solution
				JOptionPane.showMessageDialog(null, "This maze has no solution!");
				clearArrays();
			} else {
				//if theer is a solution
				showSolution();
				curCell.setText("Clear Solution");
				clearArrays();
			}
		} else if (curCell.getText().equals("Clear Solution")) {
			//clears the solution off the board and changes the label text back to "Solution"
			solve(0,0);
			hideSolution();
			curCell.setText("Solution");
			clearArrays();
		} else if (curCell.getText().equals("Save to Image")) {
			//if the label that was clicked was the save to image label
			if (tag != null) {
				while (tag == null) {
					tag = JOptionPane.showInputDialog("Give your maze a name so you can access it later on");
				}
			}
			//saves thing to image
			saveToImage();
		}
	}
	
	public void clearArrays() {
		wasHere = new boolean[width][height];
		correctPath = new boolean[width][height];
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setNewMaze(int w, int h) {
		//sets a new maze with dimensions given by the parameters
		maze = new int[w][h];
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (i == 0 || i == maze.length - 1 || j == 0 || j == maze[i].length - 1) {
					maze[i][j] = 2;
				} else {
					maze[i][j] = 1;
				}
			}
		}
	}
	
	/**
	 * Saves maze to text file
	 * @param fileLocation - set this to wherever you want the file to be saved (e.g. "P:/Mazes/").
	 * 						 do not include the file name.
	 * @throws IOException
	 */
	public void saveToFile() throws IOException {
		//saves maze to file. the first 2 lines of the file are the width and the height.
		//each next line is just either a 1 or a 2, 1 being not a wall, 2 being a wall
		
			do {
				tag = JOptionPane.showInputDialog("Enter a name for your maze so you can access it later");
			} while (tag == null);
		//in case the solution was showing, hide the solution
		hideSolution();
		//creates file with the name for the maze
		File file = new File(Maze.fileLocation + tag + ".txt");
		file.createNewFile();
		
		//outputs the maze info to the file
		PrintWriter out = new PrintWriter(new FileWriter(file, false));
		out.println(this.width);
		out.println(this.height);
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				out.println(maze[i][j]);
			}
		}
		out.close();
		
		//outputs the name of the maze to the mazeList file
		PrintWriter out2 = new PrintWriter(new FileWriter(new File(Maze.fileLocation + "mazeList.txt"), true));
		out2.println(this.tag);
		out2.close();
		out.close();
	}
	
	public void showSolution() {
		//shows the solution to the maze
		if (mazeArray == null) {
			System.out.println("The maze doesn't exist yet.");
			return;
		}
		solve(0,0);
		for (int i = 0; i < mazeArray.length; i++) {
			for (int j = 0; j < mazeArray[i].length; j++) {
				if (correctPath[i][j]) {
					mazeArray[i][j].setBackground(Color.RED);
				}
			}
		}
		mazeArray[mazeArray.length - 1][mazeArray[0].length - 1].setBackground(Color.RED);
	}
	
	public void hideSolution() {
		//hides solution to maze
		for (MazeCell[] arr: mazeArray) {
			for (MazeCell cell: arr) {
				if (cell.getBackground().equals(Color.RED)) {
					cell.setBackground(Color.white);
				}
			}
		}
	}

	@Override
	public void saveToImage() {
		//saves a png image of the maze using the jpanel/
		//the name of the file is [tag].png
		BufferedImage image = new BufferedImage((mazeArray[0][0].getWidth() * this.mazeArray[0].length), (mazeArray[0][0].getHeight() * this.mazeArray.length), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		myPanel.paint(g2);
		try {
			ImageIO.write(image, "png", new File(Maze.fileLocation + tag + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}