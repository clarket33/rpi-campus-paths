

import java.util.*;
/**
 * A simple Path object that represents a set of edges
 * in a path between two nodes
 * Contains set of edges
 * @author clarkt5
 *
 */
public class Path implements Comparable<Path>{
	
	public List<Edge<String, Double>> edgesInPath;
	/**
	 * constructs a path
	 */
	public Path(){
		edgesInPath = new ArrayList<Edge<String, Double>>();
	}
	
	/**
	 * constructs a path from another path
	 * @param p path for which its edges we wish to copy over
	 */
	public Path(Path p){
		edgesInPath = new ArrayList<Edge<String, Double>>(p.edgesInPath);
	}
	
	
	/**
	 * 
	 * @return length of path
	 */
	public double getLength(){
		double sum = 0;
		for(Edge<String, Double> e : edgesInPath){
			sum = sum + e.getLabel();
		}
		return sum;
		
	}
	//aids in storage in the priority queue
	@Override
	public int compareTo(Path p) {
		if(this.getLength() < p.getLength()){
			return -1;
		}
		else if(this.getLength() > p.getLength()){
			return 1;
		}
        return 0;
	}
	

}