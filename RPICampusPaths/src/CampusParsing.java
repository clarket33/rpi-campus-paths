

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CampusParsing {
	/**
	 * 
	 * @param filename - contains all buildings and intersections
	 * @param edgeFile - contains list of all edges in the graph
	 * @param idMatch - map of id-name matches
	 * @param nameMatch - map of name-id matches
	 * @param coordinates - map of ids to their x & y coordinates
	 * @param edges - map of ids to a set of their children
	 * @throws IOException if file is not valid format
	 * @effects - reads in the two files and stores ids and their x & y coordinatesin a map and 
	 * stores all ids and their children nodes in a map
	 * Not an ADT
	 */
	public static void readFile(String filename, String edgeFile, Map<String,String> idMatch, Map<String,String> nameMatch, Map<String, Double[]> coordinates,  
			Map<String, Set<String>> edges) throws IOException {
		
		
    	BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = null;

        while ((line = reader.readLine()) != null) {
           
             String[] data = line.split(",");
             
             if ((data.length == 1)) {
            	 throw new IOException("File "+filename+" not a CSV (Name, id, x-coordinate, y-coordinate) file.");
             }
             String name = data[0];
             String id = data[1];
             Double x = Double.parseDouble(data[2]);
             Double y = Double.parseDouble(data[3]);
             idMatch.put(id, name);
             nameMatch.put(name, id);
             Double[] coord = new Double[2];
             coord[0] = x;
             coord[1] = y;
             coordinates.put(id, coord);
        }
        
        try {
    		readEdges(edgeFile, edges);
    		

    	} catch (IOException e) {
    		throw new IOException("Invalid File");
    	}
        
        
        
    }
	
	public static void readEdges(String edgeFile, Map<String, Set<String>> edges) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(edgeFile));
        String line = null;
        
        
        while((line = reader.readLine()) != null) {
        	String[] data = line.split(",");
        	if ((data.length == 1)) {
           	 throw new IOException("File "+edgeFile+" not a CSV (id, id) file.");
            }
        	if(edges.get(data[0]) == null) {
        		edges.put(data[0], new HashSet<String>());
        	}
        	edges.get(data[0]).add(data[1]);
        }
	}

}
