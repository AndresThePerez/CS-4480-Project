import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeListener;
import javax.swing.SwingUtilities;

public class Maze extends JFrame{

    final static int X = 1;
    //wall
    final static int C = 0;
    //empty space
    final static int W = 2;
    //empty wall used for random generator
    final static int S = 3;
    //Start Tile
    final static int E = 8;
    //end tile
    final static int V = 9;
	//Path of agent

    int DELAY = 0;
    //The Default Delay is set to 0
    
    JTextField delay = new JTextField();
    Font font1 = new Font("Verdana", Font.BOLD , 20);
    JLabel label = new JLabel();

	public static void main(String args[]) {
		new Menu();
	}
	
	int col = 40;
	int row = 40;
	//Size of the maze
	
	final static int START_I = 39, START_J = 38;
	//start tile location
	final static int END_I = 0, END_J = 1;
	//end tile location
	
	String str = Menu.lvlList.getSelectedItem().toString();
	//This is to be able to load maps from the lvllist menu
	
	public int map[][] = loadMap(str);
	//Loads map that is chosen from Lvllist
	
	
	JButton dfs = new JButton("DFS search");
	JButton bfs = new JButton("BFS search");
	JButton clear = new JButton("Clear");
	JButton menu = new JButton("Menu");
	JButton close = new JButton("Close");
	JButton Load = new JButton("Load");
	JButton setDelay = new JButton("Enter");
	JButton rand = new JButton("Random");
	//All buttons being initialized
	
	Stack<AgentPos> stack = new Stack<AgentPos>();
	//creating a new stack to be used for DFS search
	
	LinkedList<AgentPos> list = new LinkedList<AgentPos>();
	//Creating linked list to be used for BFS search
	
	Timer timer = null;
	//Timer that controls DFS steps
	Timer timer2 = null;
	//Timer that controls BFS steps

	AgentPos next;
    AgentPos crt;
    //Agent positions that are used for search algorithms
    
    private Image dbImage;
    private Graphics dbg;
    //These are used to double buffer images so the image doesn't skip
    
	int[][] savedMaze = loadMap(str);
	//copy of array used for printing.
	
	public Maze(String str) {
		
		this.setResizable(false);
		this.setSize(1300, 1035);
		this.setTitle("CS4480 Artificial Intelligence Maze Program");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		//layout of the main panel
		
		this.add(delay);
		this.add(setDelay);
		this.add(Menu.lvlList);
		this.add(rand);
		this.add(Load);
		this.add(dfs);
		this.add(bfs);
		this.add(clear);
		//adding all buttons to the panel
		
		delay.setVisible(true);
		delay.setSize(250, 70);
		delay.setLocation(1000, 600);
		delay.setFont(font1);
		delay.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) {
						DELAY = Integer.parseInt(delay.getText());
						label.setText(String.valueOf(DELAY));
					}
				}
				);
		//setting the parameters to the Delay text box as the DELAY variable initialized before. This is the text box that is added.

		setDelay.setSize(250,70);
		setDelay.setLocation(1000, 700);
		setDelay.setFont(font1);
		setDelay.setVisible(true);
		setDelay.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0) {
						DELAY = Integer.parseInt(delay.getText());
						label.setText(String.valueOf(DELAY));
					}
			
				}
				
				);


		Menu.lvlList.setSize(250, 70);
		Menu.lvlList.setLocation(1000,400);
		Menu.lvlList.setFont(font1);
		
		rand.setLocation(1000,800);
		rand.setSize(250, 70);
		rand.setFont(font1);
		rand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			stopTimer();
			int arr[][] = GenerateArray();
			restore(arr);
			repaint();
			}
		});
		
		Load.setLocation(1000,500);
		Load.setSize(250,70);
		Load.setFont(font1);
		Load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			stopTimer();
			loadMap(Menu.lvlList.getSelectedItem().toString());
			repaint();
				}
		});
		
		dfs.setSize(250, 70);
		dfs.setLocation(1000, 100);
		dfs.setFont(font1);
		dfs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
 
					stopTimer();
	                   restore(map);
	                   if(DELAY == 0)
	                   	{
	                       solveStack();
	                       repaint();
	                   	}
	                   else
	                   	{
                       solveStackStep();	
	                   	}

                }

            
	
			
		});
		bfs.setSize(250, 70);
		bfs.setLocation(1000, 200);
		bfs.setFont(font1);
		bfs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
   
				stopTimer();
                restore(map);
	               if(DELAY == 0)
	                   {
	                	   solveQueue();
	                	   repaint();
	                   }
	               else
	                   {
	                	   solveQueueStep();	
	                   }
           
                    
                    repaint();
                }

            
			
		});
		clear.setSize(250,70);
		clear.setLocation(1000,300);
		clear.setFont(font1);
	    clear.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	stopTimer();
	                    restore(map);
	                    repaint();
	                }

	                
	        });

	}
	
	public void restore(int[][] savedMazed) {

        for (int i = 0; i < Size(); i++) {
            for (int j = 0; j < Size(); j++) {
            	if(map[i][j] == V)
            	{
            		map[i][j] = C;
            	}
                map[i][j] = savedMazed[i][j];
            }
        }
        map[START_I][START_J] = S;
        map[END_I][END_J] = E;
    }
	
    public int Size() {
        return map.length;
    }
    
    public void Print() {
        for (int i = 0; i < Size(); i++) {      //go to every row
            for (int j = 0; j < Size(); j++) {  //go to every element in the row
                System.out.print(map[i][j]);   //print the element
                System.out.print(' ');          //print space
            }
            System.out.println();               // go to new line
        }
    }
    
    @Override
    public void paint(Graphics g)
    {
 
    	dbImage = createImage(getWidth(), getHeight());
    	dbg = dbImage.getGraphics();
    	paintComponent(dbg);
    	g.drawImage(dbImage, 0, 0, this);
  
    	
    }
    public void paintComponent(Graphics g) {
	        super.paint(g);
	        g.translate(50, 75);      //move the maze to begin at 70 from x and 70 from y
	        
	       // Print();
	        // draw the maze
            for (int row = 0; row < map.length; row++) {
                for (int col = 0; col < map[0].length; col++) {
                    Color color = null;
                    switch (map[row][col]) {
                        case 1:
                            color = Color.darkGray;     // block (black)
                            break;
                        case 8:
                            color = Color.GREEN;
                            break;
                        case 2:
                        	color = Color.WHITE;
                        	break;
                        case 3:
                            color = Color.YELLOW;
                            break;
                        case 9:
                            color = Color.RED;   
                            break;
                        default:
                            color = Color.WHITE;  
         
                    }
                    g.setColor(color);
                    g.fillRect(23 * col, 23 * row, 23, 23);    // fill rectangular with color 
                    g.setColor(Color.BLACK);                   //the border rectangle color
                    g.drawRect(23 * col, 23 * row, 23, 23);    //draw rectangular with color

                }

            }

        }

	    public boolean isInMaze(int i, int j) {  //parameters are the position (i and j) of the cell

	        if (i >= 0 && i < Size() && j >= 0 && j < Size()) {
	            return true;
	        } else {
	            return false;
	        }
	    }
	    

	    public boolean isInMaze(AgentPos pos) {   //overloaded of isInMaze(int i, int j) , parameter is the node itself
	        return isInMaze(pos.i(), pos.j());   //extract the position of the cell (i and j) and call the first method isInMaze(int i, int j)
	    }
	    
	    // return true if the node is equal to 0 (White, Unexplored)
	    public boolean isClear(int i, int j) {
	        assert (isInMaze(i, j));
	        return (map[i][j] != X && map[i][j] != V);

	    }

	    public boolean isClear(AgentPos pos) {   //overloaded of isClear(int i, int j) , parameter is the node itself
	        return isClear(pos.i(), pos.j());   //extract the position of the cell (i and j) and call the first method isClear(int i, int j)
	    }
	    
	    
	    public int mark(int i, int j, int value) {
	        //assert (isInMaze(i, j));  // it is used for test. if the condition is false it will throw an error named AssertionError.
	        int temp = map[i][j];
	        // store the original value in temp;
	        map[i][j] = value;
	        // put the value from the parameter in maze cell with corresponding i,j
	        return temp;        
	        // return original value
	    }
	    
	    public int mark(AgentPos pos, int value) {   //overloaded of mark(int i, int j, int value) , parameter is the node itself and the value we want to insert
	        return mark(pos.i(), pos.j(), value);   //extract the position of the cell (i and j) and call the first method mark(int i, int j, int value)
	    }
	    

	    public boolean isFinal(int i, int j) {

	        return (i == Maze.END_I && j == Maze.END_J);
	    }
	    
	    public boolean isFinal(AgentPos pos) {  //overloaded of isFinal(int i, int j) , parameter is the node itself
	        return isFinal(pos.i(), pos.j());  //extract the position of the cell (i and j) and call the first method isFinal(int i, int j)
	    }
	    
	    public void solveStackStep() {
	    	stack.clear();
//	    	Stack<AgentPos> stack = new Stack<AgentPos>();
	    	stack.push(new AgentPos(START_I,START_J));
            timer = new Timer(DELAY,new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Step();
                    mark(crt, V);
                    repaint();
                }
            });
            timer.start();
	    }


	    
	    
	    public void Step()
	    {
            crt = stack.pop();
            if (isFinal(crt)) {
				timer.stop();
		        JOptionPane.showMessageDialog(rootPane, "You Got it");
		        } 
            
	    	repaint();
            next = crt.south();
            if (isInMaze(next) && isClear(next)) { 
                stack.push(next);
            }
            next = crt.east();    //go right from the current node
            if (isInMaze(next) && isClear(next)) {
                stack.push(next);
            }
            next = crt.north();    //go left from the current node
            if (isInMaze(next) && isClear(next)) {
                stack.push(next);
            }
            next = crt.west();  // go down from the current node
            if (isInMaze(next) && isClear(next)) {
                stack.push(next);
            }
            if(stack.isEmpty() && !isFinal(crt))
            {
    	            JOptionPane.showMessageDialog(rootPane, "You Are stuck in the maze");
    	        
            }

	    	 
	    }
	    

		public void solveStack() {
	         stack.clear();       
	        
	        Stack<AgentPos> stack = new Stack<AgentPos>();

	        //insert the start node
	        stack.push(new AgentPos(START_I, START_J));
			        
	        AgentPos crt;   //current node
	        AgentPos next;  //next node
	        while (!stack.empty()) {
	            crt = stack.pop();
	            if (isFinal(crt)) {
	            JOptionPane.showMessageDialog(rootPane, "You Got it");
	               break;
	            }
	            
	            mark(crt, V);
			    
	            
	            next = crt.south();
	            if (isInMaze(next) && isClear(next)) {  
	                stack.push(next);
	            }
	            next = crt.east();    
	            if (isInMaze(next) && isClear(next)) {
	                stack.push(next);
	            }
	            next = crt.north();    
	            if (isInMaze(next) && isClear(next)) {
	                stack.push(next);
	            }
	            next = crt.west();  
	            if (isInMaze(next) && isClear(next)) {
	                stack.push(next);
	            }
	            if(stack.isEmpty() && !isFinal(crt))
	            {
	    	            JOptionPane.showMessageDialog(rootPane, "You Are stuck in the maze");
	    	        
	            }
			        
	        }
			        
	        
			

	        //System.out.println("\nFind Goal By DFS : ");
	        //Print();
	        // stop time
	       
	        //duration = stopTime - startTime;    //calculate the elapsed time

	        //dfsTime =  (double)duration / 1000000;   //convert to ms
	        //System.out.println(String.format("Time %1.3f ms", dfsTime));

	       // textDfs.setText(String.format("%1.3f ms", dfsTime));
	    }
	    public void solveQueueStep() {
	    	list.clear();
	    	list.add(new AgentPos(START_I, START_J));
            timer2 = new Timer(DELAY,new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Step2();
                    mark(crt, V);
                    repaint();
                }
            });
            timer2.start();

	    }

	    public void Step2()
	    {
	    	crt = list.removeFirst();
            if (isFinal(crt)) { 
				timer2.stop();
		        JOptionPane.showMessageDialog(rootPane, "You Got it");
	            }
	    	next = crt.north();   
            if (isInMaze(next) && isClear(next)) { 
                list.add(next);
            }
            next = crt.east();    
            if (isInMaze(next) && isClear(next)) {
                list.add(next);
            }
             next = crt.west();    
            if (isInMaze(next) && isClear(next)) {
                list.add(next);
            }
            next = crt.south();   
            if (isInMaze(next) && isClear(next)) {
                list.add(next);
            }
            if(list.isEmpty() && !isFinal(crt))
            {
    	            JOptionPane.showMessageDialog(rootPane, "You Are stuck in the maze");
    	        
            }

	    	 
	    }
	    
		
	    public void solveQueue() { //BFS correspond to Queue.
	    	list.clear();
	        list.add(new AgentPos(START_I, START_J));

	        AgentPos crt, next;
	        while (!list.isEmpty()) {
	        	
	            crt = list.removeFirst();
	            
	            if (isFinal(crt)) { 
	            	JOptionPane.showMessageDialog(rootPane, "You Got it");
	            	break;
	            }

	            mark(crt, V);
	            
	            next = crt.north();    
	            if (isInMaze(next) && isClear(next)) { //isClear() function is used to implement Graph Search
	                list.add(next);
	            }
	            next = crt.east();    //move right
	            if (isInMaze(next) && isClear(next)) {
	                list.add(next);
	            }
	             next = crt.west();    //move left 
	            if (isInMaze(next) && isClear(next)) {
	                list.add(next);
	            }
	            next = crt.south();   //move down
	            if (isInMaze(next) && isClear(next)) {
	                list.add(next);
	            }
	            if(list.isEmpty() && !isFinal(crt))
	            {
	    	            JOptionPane.showMessageDialog(rootPane, "You Are stuck in the maze");
	    	        
	            }

	        }


	        
	    }

	
	public int[][] loadMap(String str) {
		try {
			map = new int[row][col];
			BufferedReader br = new BufferedReader(new FileReader(str));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String mapStr = sb.toString();

			int counter = 0;
			for (int y = 0; y < col; y++) {
				for (int x = 0; x < row; x++) {
					String mapChar = mapStr.substring(counter, counter + 1);
					if (!mapChar.equals("\r\n") && !mapChar.equals("\n")
							&& !mapChar.equals("\r")) {
						map[y][x] = Integer.parseInt(mapChar);
					} else {
						x--;
					}
					counter++;
				}
			}
		} catch (Exception e) {
			System.out
					.println("Unable to load existing map(if exists), creating new map.");
		}
		return map;
	}
	

public int[][] GenerateArray()
{
	map = new int[row][col];
	Random rnd = new Random();
    int min = 0;
    int high = 2;
    AgentPos now = new AgentPos(0,0);
    AgentPos later;
	for(int i = 0; i < col; i ++)
	{
		for(int j = 0; j<row; j++)
		{
			 int n = rnd.nextInt(3) + min;
             map[i][j] = n;
             if(isInMaze(now) && isClear(now) && !isFinal(now))
 			{
 				later = now.east();
 				if(isInMaze(later) && isClear(later))
 				{
 					mark(later, X);
 					now = later;
 				}
		}
	}
	}
	return map;
	
}

public void stopTimer()
{
	if(timer != null)
	{
		timer.stop();
	}
	if(timer2 != null)
	{
		timer2.stop();
	}
}
}


