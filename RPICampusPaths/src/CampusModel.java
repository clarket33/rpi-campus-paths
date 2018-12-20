

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * 
 * @author clarkt5
 *Implementation of the graph class
 *Contains a graph and a way to find the shortest paths between buildings
 *Not an ADT
 */
public class CampusModel {
	public Graph<String, Double> g;
	private Map<String, Double[]> coordinates;
	private Map<String, Set<String>> edges;
	private Map<String, String> idToName;
	private Map<String, String> nameToId;
	
	
	/**
	 * 
	 * @param file - file of all buildings
	 * @param edgeFile - file containing all edges
	 * @effects creates a graph to store all buildings and paths between them
	 */
	public void createGraph(String file, String edgeFile) {
		g = new Graph<String, Double>();
		coordinates = new HashMap<String, Double[]>();
		edges = new HashMap<String, Set<String>>();
		idToName = new HashMap<String, String>();
		nameToId = new HashMap<String, String>();
		
		try {
    		CampusParsing.readFile(file, edgeFile, idToName, nameToId, coordinates, edges);
    		

    	}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
    		e.printStackTrace();
    	}
		
		
		for(String i : coordinates.keySet()) {
			g.addNode(i);
			g.addEdge(i, i, new Double(0));
		}
		for(String i : edges.keySet()) {
			for(String j: edges.get(i)) {
				Double distance = 0.0;
				Double tempX = (coordinates.get(j)[0] - coordinates.get(i)[0])*(coordinates.get(j)[0] - coordinates.get(i)[0]);
				Double tempY = (coordinates.get(j)[1] - coordinates.get(i)[1])*(coordinates.get(j)[1] - coordinates.get(i)[1]);
				distance = Math.sqrt(tempX + tempY);
				
				g.addEdge(i, j, distance);
				g.addEdge(j, i, distance);
			}
		}
		
	}
	
	
	/**
	 * 
	 * @param id - id of a building
	 * @return the name of the building associated with that id
	 */
	public String idConvert(String id) {
		return idToName.get(id);
	}
	
	
	/**
	 * 
	 * @param name name of the building
	 * @return the id associated with that name
	 */
	public String nameConvert(String name) {
		return nameToId.get(name);
	}
	
	
	/**
	 * 
	 * @return an iterator to the list of buildings in the graph
	 */
	public Iterator<String> listBuildings() {
		ArrayList<String> buildings = new ArrayList<>();
		for(String i: coordinates.keySet()) {
			if(!idConvert(i).isEmpty()) {
				buildings.add(idConvert(i));
			}
		}
		Collections.sort(buildings);
		Iterator<String> itr = buildings.iterator();
		return itr;
	}
	
	
	/**
	 * 
	 * @param a starting point of the path
	 * @param b ending point of the path
	 * @return a string representation of the shortest path bewteen the two nodes
	 */
	public String findPath(String a, String b) {
		String s = "";
		String temp = a;
		String tempb = b;
		boolean unk = false;
		if(isId(a)) {	
			a = idConvert(a);
		}
		if(a == null || a.isEmpty() || !nameToId.keySet().contains(a)) {
			s += "Unknown building: [" + temp + "]\n";
			unk = true;
		}
		if(isId(b)) {
			b = idConvert(b);
		}
		if(b == null || b.isEmpty() || !nameToId.keySet().contains(b)) {
			if(!tempb.equals(temp)) {
				s += "Unknown building: [" + tempb + "]\n";
			}
			unk = true;
		}
		if(unk) {
			return s;
		}
		s += "Path from " + a + " to " + b + ":\n";
		a = nameConvert(a);
		b = nameConvert(b);
		if(a.equals(b)) {
			s += "Total distance: 0.000 pixel units.\n";
			return s;
		}
		
		Path endPath = dijkstra(a, b);
		
		
		if(endPath.edgesInPath.size() != 0) {
			Iterator<Edge<String, Double>> itr = endPath.edgesInPath.iterator();
			String prevNode = a;
			Edge<String, Double> fullPath;
			while(itr.hasNext()){
				fullPath = itr.next();
				
				double angle = Math.toDegrees(Math.atan2(coordinates.get(prevNode)[1]-coordinates.get(fullPath.getChild())[1], 
						coordinates.get(fullPath.getChild())[0] - coordinates.get(prevNode)[0]));
				String dir = "";
				
				if(angle < 0){
					angle += 360;
				}
				
				
				if((angle >= 0 && angle < 22.5) ||(angle >= 337.5 && angle <= 360)){
					dir = "East";
				}
				
				else if(angle >= 22.5 && angle < 67.5){
					dir = "NorthEast";
				}
				
				else if(angle >= 67.5 && angle < 112.5){
					dir = "North";
				}
				
				else if(angle >= 112.5 && angle < 157.5){
					dir = "NorthWest";
				}
				
				else if(angle >= 157.5 && angle < 202.5){
					dir = "West";
				}
				
				else if(angle >= 202.5 && angle < 247.5){
					dir = "SouthWest";
				}
							
				else if(angle >= 247.5 && angle < 292.5){
					dir = "South";
				}
							
				else if(angle >= 292.5 && angle < 337.5){
					dir = "SouthEast";
				}
				String k = "";
				if(idToName.get(fullPath.getChild()).isEmpty()) {
					k = "Intersection " + fullPath.getChild();
				}
				s = s + "\tWalk " + dir + " to (" + k + idToName.get(fullPath.getChild()) + ")\n";
				prevNode = fullPath.getChild();
			}
			String totalWeight = String.format("%.3f", endPath.getLength());
			s = s + "Total distance: " + totalWeight + " pixel units.\n";
		}
		else {
			a = idConvert(a);
			b = idConvert(b);
			s = "There is no path from " + a + " to " + b + ".";
		}
		return s;
	}
	
	/**
	 * 
	 * @param node1 start node
	 * @param node2 end node
	 * @return iterator to the shortest path between two nodes, empty path if none exists
	 */
	public Path dijkstra(String node1, String node2) {
		String start = node1;
		String dest = node2;
		PriorityQueue<Path> active = new PriorityQueue<Path>();
		HashSet<String> finished = new HashSet<String>();
		active.add(new Path());//empty path, no edges, total cost zero
		
		while(active.size() != 0) {
			//lowest-cost path
			Path minPath = active.poll();
			String minDest;
			if(minPath.getLength() != 0) {
				minDest = minPath.edgesInPath.get(minPath.edgesInPath.size()-1).getChild();
			}
			else {
				minDest = start;
			}
			
			if(minDest.equals(dest)) {
				return minPath;
			}
			
			if(finished.contains(minDest)) {
				continue;
			}
			
			Iterator<Edge<String, Double>> edgeItr = g.listEdges(minDest);
			while(edgeItr.hasNext()) {
				Edge<String, Double> curEdge = edgeItr.next();
				if(!finished.contains(curEdge.getChild())) {
					Path newPath = new Path(minPath);
					newPath.edgesInPath.add(curEdge);
					active.add(newPath);
				}
			}
			finished.add(minDest);
		}
		return new Path();
	}
	
	/**
	 * 
	 * @param a - a string
	 * @return true if the given string is an id
	 * false if the given string is not an id
	 */
	public boolean isId(String a) {
		try{
	    	double d = Double.parseDouble(a);
	    } catch(NumberFormatException nfe){
			return false;
		}
		return true;
	}
	
	public Double[] getCoordinates(String building) {
		if(isId(building)) {
			return coordinates.get(building);
		}
		if(building.contains("Intersection")) {
			String s = building.substring(13);
			return coordinates.get(s);
		}
		return coordinates.get(nameConvert(building));
	}
}
