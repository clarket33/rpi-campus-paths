
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;  
import javax.swing.*;

import com.sun.javafx.geom.Ellipse2D;

import java.util.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.color.*;
import java.awt.font.*;

import java.io.File;
import java.io.IOException;


public class RPICampusPathsMain {
	
	/**
	 * 
	 * @param args
	 * creates the frame where the user is allowed to input two buildings and the program
	 * outputs the path between them on the campus map
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Campus Paths");
		frame.setSize(400, 400);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLayout(new GridLayout(1, 2));
	    
	    
	    Panel options = new Panel();
	    options.setLayout(new GridLayout(5,1));
	    Font f = new Font("BOLD", 1, 25);
	    Label l = new Label();
	    l.setText("Path from: ");
	    l.setFont(f);
	    l.setAlignment(Label.CENTER);
	    Label l1 = new Label();
	    l1.setFont(f);
	    l1.setText("to: ");
	    l1.setAlignment(Label.CENTER);
	    options.add(l);
	    
	    //create combo boxes with all the buildings in each
	    String file = "data/RPI_map_data_Nodes.csv";
    	String edgeFile = "data/RPI_map_data_Edges.csv";
    	CampusModel cp = new CampusModel();
    	cp.createGraph(file, edgeFile);
    	Iterator<String> itr = cp.listBuildings();
    	ArrayList<String> buildings = new ArrayList<>();
    	while(itr.hasNext()) {
    		buildings.add(itr.next());
    	}
    	JComboBox<String> buildingList = new JComboBox<String>(new Vector<String>(buildings));
    	JComboBox<String> buildingList2 = new JComboBox<String>(new Vector<String>(buildings));
    	
    	options.add(buildingList);
    	options.add(l1);
    	options.add(buildingList2);
    	
	    Button submitBut = new Button();
	    submitBut.setLabel("Submit");
	    submitBut.setFont(f);
	    submitBut.setBackground(new Color(100, 100, 100));
	    
	    options.add(submitBut);
	    frame.add(options);
	    
	    //display the map on original frame
	    BufferedImage map = null;
	    try {
	    	map = ImageIO.read(new File("data/RPI_campus_map_2010_extra_nodes_edges.png"));
	    }
	    catch(IOException e){
	    	e.printStackTrace();
	    }
	    BufferedImage map2 = map;
	    BufferedImage fit = resize(map, 1000, 900);
	    fit = fit.getSubimage(0, 0, 900,600);
	    
	    JLabel j = new JLabel(new ImageIcon(fit));
	    frame.add(j);
	    frame.pack();
	    frame.setVisible(true);
	    //when submit gets clicked, display map with path using a Jpanel extended object that overloads
	    submitBut.addActionListener(new ActionListener()
	    {
	    	@Override
	    	/**
	    	 * takes in the buildings selected, finds the paths and outputs the path on the map
	    	 */
	    	public void actionPerformed(ActionEvent e) {
	    		String a = buildingList.getSelectedItem().toString();
	    		String b = buildingList2.getSelectedItem().toString();
	    		LinkedHashMap<String, Double[]> path = new LinkedHashMap<String, Double[]>();
	            path.put(a, null);
	    		String s = cp.findPath(a, b);
	    		int start = s.indexOf("(", 0);
	    		int end = s.indexOf(")", 0);
	    		while(start != -1) {
	    			path.put(s.substring(start+1, end), null);
	    			start = s.indexOf("(", end+1);
	    			end = s.indexOf(")", end+1);
	    		}
	    		for(String i : path.keySet()) {
	    			Double[] coord = new Double[2];
	    			coord[0] = cp.getCoordinates(i)[0];
	    			coord[1] = cp.getCoordinates(i)[1];
	    			path.replace(i, coord);
	    		}
	    		//set up the new frame that displays the path
	    		JFrame newMap = new JFrame("Shortest Path between " + a + " and " + b);
	    		BufferedImage map3 = resize(map2, 1700,1950);
	    		JPanel l = new PathPainting(map3, path);
	    		l.setPreferredSize(new Dimension(1900,1000));
	    		JButton reset = new JButton("Reset");
	    		JLabel np = new JLabel("No Path Found");
	    		JLabel samePlace = new JLabel("You are already there!");
	    		newMap.add(reset);
	    		newMap.add(np);
	    		newMap.add(samePlace);
	    		newMap.add(l);
	    		reset.setBounds(600, 200, 200, 100);
	    		np.setBounds(600, 300, 200, 100);
	    		samePlace.setBounds(500, 300, 300, 100);
	    		np.setFont(f);
	    		samePlace.setFont(f);
	    		reset.setFont(f);
	    		frame.setVisible(false);
	    		np.setVisible(false);
	    		samePlace.setVisible(false);
	    		newMap.pack();
	    		newMap.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    		boolean done = false;
	    		if(s.contains("There is no path")) {
	    			np.setVisible(true);
	    			done = true;
	    		}
	    		if(path.keySet().size() == 1 && done == false) {
	    			samePlace.setVisible(true);
	    		}
	    		newMap.setVisible(true);
	    		reset.addActionListener(new ActionListener()
	    	    {
	    			@Override
	    			/**
	    			 * reset button hit, pulls up originally frame
	    			 */
	    	    	public void actionPerformed(ActionEvent e) {
	    				newMap.setVisible(false);
	    				frame.pack();
	    	    		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	    		frame.setVisible(true);
	    			}
	    	    });
	    	}
	    });
	    
	    
	}
	/**
	 * 
	 * @param img the image
	 * @param height desired height of image
	 * @param width desired width of image
	 * @return resized image to desired height and width
	 */
	private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
/**
 * 
 * @author clarkt5
 *new class for the purpose of overloading the paintComponent function
 */
class PathPainting extends JPanel {
	private BufferedImage map;
	private LinkedHashMap<String, Double[]> coords;
	 
	public PathPainting(BufferedImage aMap, LinkedHashMap<String, Double[]> theCoords) {
		map = aMap;
		coords = theCoords;
	}
	/**
	 * prints circles at each building and draws lines at each path
	 */
	public void paintComponent(Graphics g) {
   
   
		// ensure any background belonging to container is painted
   
		super.paintComponent(g);
   
		// Cast g to its actual class to make graphics2d methods available.
		// draw a couple of shapes
		
		Graphics2D g2 = (Graphics2D) g;
		
		Iterator<String> itr = coords.keySet().iterator();
		String prev = itr.next();
		g2.drawImage(map, 0, 0, this);
		g2.setColor(Color.BLUE);
		//aids in scaling the coordinates to fit the image
		Double width = .89886;
		Double height = .5;
		
		Double x = coords.get(prev)[0] * width;
		Double y = coords.get(prev)[1] * height;
		int fx = x.intValue();
		int fy = y.intValue();
		
		g2.fillOval(fx, fy, 25, 25);
		while(itr.hasNext()) {
			String cur = itr.next();
			x = coords.get(cur)[0] * width;
			y = coords.get(cur)[1] * height;
			int sx = x.intValue();
			int sy = y.intValue();
			g2.setStroke(new BasicStroke(10));
			g2.setColor(Color.GREEN);
			g2.drawLine(fx, fy, sx, sy);
			prev = cur;
			fx = sx;
			fy = sy;
		}
		g2.setColor(Color.BLUE);
		g2.fillOval(fx, fy, 25, 25);
		
		
		
	}
	
 }

