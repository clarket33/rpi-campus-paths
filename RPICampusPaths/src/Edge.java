

/**
 * A simple Edge object that represents a directed
 * edge between two nodes
 * Contains a parent, a child, and the edge's label
 * @author clarkt5
 *
 */

public class Edge<T1 extends Comparable<T1>, T2 extends Comparable<T2>> implements Comparable<Edge<T1, T2>>{
	
	
	private T2 label;
	private T1 start;
	private T1 end;
	
	public Edge(T1 a, T1 b, T2 theLabel) {
		start = a;
		end = b;
		label = theLabel;
	}
	/**
	 * gets description of edge
	 * @return this.label
	 */
	public T2 getLabel() {
		return this.label;
	}
	/**
	 * gets parent of the edge relationship
	 * @return starting node in the edge
	 */
	public T1 getParent() {
		return this.start;
	}
	/**
	 * gets child of the edge relationship
	 * @return target node in the edge
	 */
	public T1 getChild() {
		return this.end;
	}
	/**
	 * changes the label of an edge
	 * @param aLabel
	 * @modifies label of edge
	 */
	public void editLabel(T2 aLabel) {
		label = aLabel;
	}
	
	
	@Override
	public int compareTo(Edge<T1, T2> e) {
		if (this.getChild().compareTo(e.getChild()) == 0) {
			return this.getLabel().compareTo(e.getLabel());
		}
		return this.getChild().compareTo(e.getChild());
	}

}
