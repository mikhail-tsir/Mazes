/* Misha Tsirlin
 * Maze game - you can make mazes or generate random ones
 * 2018-06-11
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Test extends JFrame implements MouseListener, ActionListener{
	JPanel panel; //to display the JLabels
	JLabel random; //to generate random maze
	JLabel newMaze; //to start a new maze from scratch
	JTextField w; //width of new maze or random maze
	JTextField h; //height of new maze or random maze
	JComboBox<String> mazesList; //to open an existing maze

	public static void main(String[] args) throws IOException {
		Test test = new Test();
		test.setSize(new Dimension(1000, 600));
		test.setVisible(true);
	}
	
	public Test() throws IOException {
		super("choose what you want to do");
		initialize(); //logic
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/*Logic for test class*/
	public void initialize() throws IOException {
		Random r = new Random();
		//initializes the JPanel
		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(1000, 600);
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setLocation(0, 0);
		panel.setVisible(true);
		
		//initialize and place label to generate random maze
		random = new JLabel("Generate Random Maze");
		random.setSize(200, 25);
		random.setBackground(new Color(r.nextInt(156)+100,r.nextInt(156)+100,r.nextInt(156)+100));
		random.setOpaque(true);
		random.setBorder(BorderFactory.createBevelBorder(0));
		random.setLocation(200, 100);
		random.addMouseListener(this);
		
		panel.add(random); 
		
		//initialize and generate label to start a new maze
		newMaze = new JLabel("Start new maze");
		newMaze.setSize(200, 25);
		newMaze.setBackground(new Color(r.nextInt(156)+100,r.nextInt(156)+100,r.nextInt(156)+100));
		newMaze.setOpaque(true);
		newMaze.setBorder(BorderFactory.createBevelBorder(0));
		newMaze.setLocation(200,150);
		newMaze.addMouseListener(this);
		
		panel.add(newMaze);
		
		//label that indicates where to input width
		JLabel width = new JLabel("Width:");
		width.setBackground(new Color(r.nextInt(156)+100,r.nextInt(156)+100,r.nextInt(156)+100));
		width.setSize(40,25);
		width.setLocation(450, 95);
		width.setOpaque(true);
		panel.add(width);
		
		//text field that takes in a number as the width
		w = new JTextField();
		w.setSize(25, 25);
		w.setBackground(new Color(r.nextInt(156)+100,r.nextInt(156)+100,r.nextInt(156)+100));
		w.setOpaque(true);
		w.setLocation(450, 125);
		
		panel.add(w);
		
		//label that indicates where to input height
		JLabel height = new JLabel("Height");
		height.setBackground(new Color(r.nextInt(156)+100,r.nextInt(156)+100,r.nextInt(156)+100));
		height.setSize(40,25);
		height.setLocation(500, 95);
		height.setOpaque(true);
		panel.add(height);
		
		//text field that takes in a number as the height
		h = new JTextField();
		h.setSize(25, 25);
		h.setBackground(new Color(r.nextInt(156)+100,r.nextInt(156)+100,r.nextInt(156)+100));
		h.setOpaque(true);
		h.setLocation(500, 125);
		panel.add(h);
		
		//label that indicates the function of the combobox
		JLabel chooseMaze = new JLabel("Choose an existing maze:");
		chooseMaze.setSize(200, 25);
		chooseMaze.setLocation(200,200);
		
		panel.add(chooseMaze);
		
		//combo box that lets user open an existing maze
		JComboBox<String> mazesList = new JComboBox<String>(getMazes());
		mazesList.setSize(250, 50);
		mazesList.setLocation(200, 230);
		mazesList.setOpaque(true);
		mazesList.setBackground(new Color(r.nextInt(156)+100,r.nextInt(156)+100,r.nextInt(156)+100));
		mazesList.addActionListener(this);
		
		panel.add(mazesList);
		
		String information = ("<html> Welcome to my maze program! <br></br>\n"
				+ "You can create your own maze, or generate a random one. For this, you can enter dimensions for width and height \n"
				+ "or leave them blank, and the default size is 25x25. The width and the height cannot exceed 70. You can save these"
				+ "mazes as images to print later, or you can save the maze and come back to it again. To open a previously saved maze,"
				+ "simply select it from the drop-down list above. </html>");
		//label that stores information
		JLabel info = new JLabel(information );
		info.setBackground(new Color(r.nextInt(156)+100,r.nextInt(156)+100,r.nextInt(156)+100));
		info.setOpaque(true);
		info.setLocation(200, 400);
		info.setSize(600, 100);
		panel.add(info);
		//add the panel to the frame
		getContentPane().add(panel);
	}

	@Override
	/*when the mouse is clicked*/
	public void mouseClicked(MouseEvent e) {
		JLabel label = (JLabel) e.getSource();
		//gets whatever label was clicked
		
		//if label to generate random maze was clicked
		if (label.getText().equals("Generate Random Maze")) {
			//temporary width and height for maze
			int wid = 0, hei = 0;
			try {
				//if the user actually input a size
				wid = Integer.parseInt(w.getText());
				hei = Integer.parseInt(h.getText());
			} catch (Exception ex) {
				//if the user didn't input a size, or input something that isn't a number, generates the default maze which is 25x25
				RandomMaze randMaze = new RandomMaze();
				this.setVisible(false);
				//limits both the width and the height to 70.
				if (wid > 70) {
					wid = 70;
				}
				if (hei > 70) {
					hei = 70;
				}
				JOptionPane.showMessageDialog(null, "You did not enter a size so you're stuck with the default size");
				return;
			}
			//sets maze with the width and height that the user chose
			RandomMaze randMaze = new RandomMaze(wid, hei);
			this.setVisible(false);
		} else if (label.getText().equals("Start new maze")) {
			//if label to start a new maze was clicked
			
			//temporary width and height for maze
			int wid = 0, hei = 0;
			try {
				//if the user actually input a size
				wid = Integer.parseInt(w.getText());
				hei = Integer.parseInt(h.getText());
			} catch (Exception ex) {
				//if the user didn't input a size, or input something that isn't a number, generates the default maze which is 25x25
				BuilderMaze bMaze = new BuilderMaze();
				bMaze.startMaze();
				this.setVisible(false);
				JOptionPane.showMessageDialog(null, "You did not enter a size so you're stuck with the default size");
				return;
			}
			//limits both the width and the height to 70
			if (wid > 70) {
				wid = 70;
			}
			if (hei > 70) {
				hei = 70;
			}
			//sets up maze with the width and height that the user chose
			BuilderMaze bMaze= new BuilderMaze(wid, hei);
			bMaze.startMaze();
			this.setVisible(false);
		}
		
	}
	
	/*gets a list of the names of all the previously stored mazes*/
	public String[] getMazes() throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		File file = new File(Maze.fileLocation + "mazeList.txt");
		file.createNewFile();
		Scanner scan = new Scanner(new FileReader(file));
		
		//Reads list of mazes from the file mazeList.txt to the ArrayList<String> list
		while (scan.hasNext()) {
			list.add(scan.nextLine());
		}
		scan.close();
		
		//converts arraylist to array
		String[] array = list.toArray(new String[list.size()]);
		
		int n = array.length;
       
		//sorts the mazes alphabetically using selection sort
        for (int i = 0; i < n-1; i++) {
            int minIndex = i;
            for (int j = i+1; j < n; j++) {
                if (array[j].compareToIgnoreCase(array[minIndex]) < 1) {
                    minIndex = j;
                }
            }
            String temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }
        //returns the sorted array
		return array;
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
	public void actionPerformed(ActionEvent e) {
		//When the user selects a previously saves maze from the combobox
		@SuppressWarnings("unchecked")
		JComboBox<String> cb = (JComboBox<String>) (e.getSource());
		//gets the string chosen from the combobox
		String s = (String) cb.getSelectedItem();
		//gets the file that corresponds to the chosen maze
		File file = new File(Maze.fileLocation + s + ".txt");
		try {
			//attemps to open the file
			BuilderMaze bMaze= new BuilderMaze(file);
			bMaze.startMaze();
			bMaze.tag = s;
			this.setVisible(false);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Oops! Something went wrong");
			e1.printStackTrace();
		}
		
	}

}
