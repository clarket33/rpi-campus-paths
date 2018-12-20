
import java.util.Iterator;
import java.util.Map;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
/** <b>Graph</b> represents an <b>mutable</b> directed labeled multi-graph.
It contains a collection of Nodes and edges, edges being the connection between two Nodes.
Each edge is directional,an edge e from Node A to Node B
indicates B that is directly reachable from A,
but does not necessarily mean that A is directly reachable from B.
Every edge has a label containing information of some sort, multiple edges may have the 
same label. Edges can be reflexive: edges from one Node to itself are allowed.
Duplicate edges: edges with the same starting Node,
ending Node, and label, may exist.

@specfield Node : String //vertex on the graph
@specfield  Edge : Edge //connection of two Nodes
@specfield label : object //description of edge


*/
public class Graph<T1 extends Comparable<T1>, T2 extends Comparable<T2>>{


private Map<T1, ArrayList<Edge<T1, T2>> > adj_lst;
	

//Abstraction Function:
//A Graph represents a mutable directed labeled multi-graph
//It is stored as A Map, with the keys being nodes and the values
//being edges where the associated node is the parent

//Representation invariant for every Graph g:
//for all i in adj_lst.keySet()
//adj_lst.get(i).size() >= 0 (must have 0 or more edges for a node in the graph) 
	
	/**
	 * creates an Empty Graph
	 * @effects Constructs a new graph
	 */
	public Graph() {
		//throw new RuntimeException("Method not yet implemented");
		adj_lst = new HashMap<T1, ArrayList<Edge<T1, T2>> >();
		checkRep();
		
	}

	/**
	 * creates a Graph with one Node
	 * @param n A node to initialize the graph with
	 * @effects constructs a new Graph
	 */
	public Graph(T1 n) {
		//throw new RuntimeException("Method not yet implemented");
		adj_lst = new HashMap<T1, ArrayList<Edge<T1, T2>>>();
		adj_lst.put(n, new ArrayList<Edge<T1, T2>>());
		checkRep();
	}

	/**
	 * Adds a node to the graph
	 * @param n A node that will be added
	 * @return true if the node was successfully added
	 * false if the node already exists or is NULL
	 */
	public boolean addNode(T1 n) {
		//throw new RuntimeException("Method not yet implemented"); 
		if(n == null) {
			return false;
		}
		if(adj_lst.get(n)== null) {
			adj_lst.put(n, new ArrayList<Edge<T1, T2>>());
			checkRep();
			return true;
		}
		else {
			return false;
		}
		
	}
	
	/**
	 * Adds an edge to the graph
	 * @param a the starting point node
	 * @param b the ending point Node
	 * @param edgeLabel the description of the edge
	 * @return true if the edge was successfully added
	 * false if either Node is null
	 */
	public boolean addEdge(T1 a, T1 b, T2 edgeLabel) {
		//throw new RuntimeException("Method not yet implemented"); 
		if(adj_lst.get(a) == null || adj_lst.get(b) == null) {
			return false;
		}
		Edge<T1, T2> e = new Edge<T1, T2>(a, b, edgeLabel);
		adj_lst.get(a).add(e);
		return true;
			
	}
	
	/**
	 * Gives the Nodes in alphabetical order
	 * @returns an iterator of a list of the Nodes in the Graph
	 */
	public Iterator<T1> listNodes(){
		//throw new RuntimeException("Method not yet implemented"); 
		SortedSet<T1> elts = new TreeSet<T1>();
		
		Iterator<T1> itr = adj_lst.keySet().iterator();
		
		while(itr.hasNext()){
			elts.add(itr.next());
		}
		
		return elts.iterator();
	}
	
	/**
	 * gives the Children of a specific node in alphabetical order
	 * @param parentNode
	 * @return an iterator of a list of all of the children of the inputed Node
	 * 
	 */
	public Iterator<String> listChildren(T1 parentNode){
		//throw new RuntimeException("Method not yet implemented"); 
		SortedSet<String> elts = new TreeSet<String>();
		Iterator<T1> itr = adj_lst.keySet().iterator();
		while(itr.hasNext()){
			T1 cur = itr.next();
			if(cur.equals(parentNode)) {
				Iterator<Edge<T1, T2>> itr1 = adj_lst.get(cur).iterator();
				
				while(itr1.hasNext()){
					Edge<T1, T2> n = itr1.next();
					elts.add(n.getChild() + "(" + n.getLabel() + ")");
				}
				
				return elts.iterator();
			}
		}
		return new ArrayList<String>().iterator();	
		
		
	}
	
	//	@param: node - The node of which the children are to be printed
	//	@effects: returns an iterator that returns all edges in lexiographical order
	public Iterator<Edge<T1, T2>> listEdges(T1 node){
		ArrayList<Edge<T1, T2>> edges = new ArrayList<Edge<T1, T2>>();
		edges = adj_lst.get(node);
		
		if(edges != null && edges.size() > 0){
			Collections.sort(edges);
			return edges.iterator();
		}
		
		return Collections.emptyIterator();

		
	}
	
	
	
	/**
     * Checks that the representation invariant holds (if any).
     **/
    // Throws a RuntimeException if the rep invariant is violated.
    private void checkRep() throws RuntimeException {  
    	Iterator<T1> itr = adj_lst.keySet().iterator();
    	
    	while(itr.hasNext()) {
    		if(adj_lst.get(itr.next()).size() >= 0) {
    			//Do nothing
    		}
    		else {
    			throw new RuntimeException("Node does not contain proper edge lists"); 
    		}
    	}
    }
}
	
	
	

