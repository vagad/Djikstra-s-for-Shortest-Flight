import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.Math;

/**
 * Determines cities, computes the distances between cities, and finds the 
 * shortest distances between cities based on Dijkstra's algorithm. For 
 * Dijkstra's alg, a list was used instead of a Priority Queue as suggested
 * on Piazza. Additionally, in using Dijkstra's algorithm, the euclidean
 * distances are calculated first.
 *
 *   
 * @author VamsiG 
 * File: Dijkstra.java
 */
public class Dijkstra {

  // Keep a fast index to nodes in the map
  private Map<String, Vertex> vertexNames;

  /**
   * Construct an empty Dijkstra with a map. The map's key is the name of a vertex
   * and the map's value is the vertex object.
   */
  public Dijkstra() {
    vertexNames = new HashMap<String, Vertex>();
  }

  /**
   * Adds a vertex to the dijkstra. Throws IllegalArgumentException if two vertices
   * with the same name are added.
   * 
   * @param v
   *          (Vertex) vertex to be added to the dijkstra
   */
  public void addVertex(Vertex v) {
    if (vertexNames.containsKey(v.name))
      throw new IllegalArgumentException("Cannot create new vertex with existing name.");
    vertexNames.put(v.name, v);
  }

  /**
   * Gets a collection of all the vertices in the dijkstra
   * 
   * @return (Collection<Vertex>) collection of all the vertices in the dijkstra
   */
  public Collection<Vertex> getVertices() {
    return vertexNames.values();
  }

  /**
   * Gets the vertex object with the given name
   * 
   * @param name
   *          (String) name of the vertex object requested
   * @return (Vertex) vertex object associated with the name
   */
  public Vertex getVertex(String name) {
    return vertexNames.get(name);
  }

  /**
   * Adds a directed edge from vertex u to vertex v
   * 
   * @param nameU
   *          (String) name of vertex u
   * @param nameV
   *          (String) name of vertex v
   * @param cost
   *          (double) cost of the edge between vertex u and v
   */
  public void addEdge(String nameU, String nameV, Double cost) {
    if (!vertexNames.containsKey(nameU))
      throw new IllegalArgumentException(nameU + " does not exist. Cannot create edge.");
    if (!vertexNames.containsKey(nameV))
      throw new IllegalArgumentException(nameV + " does not exist. Cannot create edge.");
    Vertex sourceVertex = vertexNames.get(nameU);
    Vertex targetVertex = vertexNames.get(nameV);
    Edge newEdge = new Edge(sourceVertex, targetVertex, cost);
    sourceVertex.addEdge(newEdge);
  }

  /**
   * Adds an undirected edge between vertex u and vertex v by adding a directed
   * edge from u to v, then a directed edge from v to u
   * 
   * @param nameU
   *          (String) name of vertex u
   * @param nameV
   *          (String) name of vertex v
   * @param cost
   *          (double) cost of the edge between vertex u and v
   */
  public void addUndirectedEdge(String nameU, String nameV, double cost) {
    addEdge(nameU, nameV, cost);
    addEdge(nameV, nameU, cost);
  }

  // STUDENT CODE STARTS HERE

  /**
   * Computes the euclidean distance between two points as described by their
   * coordinates. Imported Math to allow for square root functionality
   * 
   * @param ux
   *          (double) x coordinate of point u
   * @param uy
   *          (double) y coordinate of point u
   * @param vx
   *          (double) x coordinate of point v
   * @param vy
   *          (double) y coordinate of point v
   * @return (double) distance between the two points
   */
  public double computeEuclideanDistance(double ux, double uy, double vx, double vy) 
  {
    // Determine x distace
    double xdist = (ux - vx);
    xdist = xdist * xdist;

    // Determine y distance
    double ydist = (uy - vy);
    ydist = ydist * ydist;

    // Sum and square root for euclidean distance
    double distance = Math.sqrt(xdist + ydist);

    return distance; 
  }

  /**
   * Calculates the euclidean distance for all edges in the map using the
   * computeEuclideanDistance method.
   */
  public void computeAllEuclideanDistances() 
  {
    //check all vertices and all edges adjacent to those vertices
    for(Vertex v: vertexNames.values())
    {
      for(Edge vEdge: v.adjacentEdges)
      {
        //in this case, determine values for computeEuclideanDistance()
        Vertex source = vEdge.source;
        Vertex target = vEdge.target;
        double ux = vEdge.source.x;
        double uy = vEdge.source.y;
        double vx = vEdge.target.x;
        double vy = vEdge.target.y;

        vEdge.distance = computeEuclideanDistance(ux, uy, vx, vy);
      }
    }
  }

  /**
   * Dijkstra's Algorithm. 
   * As mentioned above, an ArrayList was used instead of a Priority Queue
   * Based upon pseudocode provided by Weiss' in DS textbook.
   * Euclidean values are computed first to ensure that all distances are 
   * correct between cities. 
   * @param s (String) starting city name
   */
  public void doDijkstra(String s) 
  {
    computeAllEuclideanDistances();
    
    //List of all vertices that serves function of Priority Queue
    ArrayList<Vertex> allVertices = new ArrayList<Vertex>();
    
    Vertex current;
    Vertex currentAdj;
    
    //reset distance of all vertices (known is false by default)
    for(Vertex v: vertexNames.values())
    {
      v.distance = Double.MAX_VALUE;
      allVertices.add(v);
    }
    
    //ensure vertex with s as a name is shortest and has no prev
    current = vertexNames.get(s);
    current.distance = 0;
    current.prev = null;
    
    //determine initial number of vertices
    int unknownVertexNum = allVertices.size();
    while(unknownVertexNum > 0)
    {
      //start off by using the smallest vertex in the list
      current = determineSmallest(allVertices);
      //this vertex is now known and will be analyzed
      current.known = true;
      unknownVertexNum--;
      
      for(Edge i: current.adjacentEdges)
      {
        currentAdj = i.target;
        
        //if path is shorter, replace it and set new previous to ensure shortest path
        if(currentAdj.distance > (current.distance + i.distance) && currentAdj.known == false)
        {
          currentAdj.distance = current.distance + i.distance;
          currentAdj.prev = current;
        }
      }
      
    }

  }

  /**
   * Determines smallest vertex in list to be used by Dijkstra's.  
   * @return smallest vertex in the List
   */
  public static Vertex determineSmallest(List<Vertex> allVertices)
  {
    
    //intialize to no Vertex and largest possible double
    Vertex minVertex = null;
    double minDist = Double.MAX_VALUE;
    
    //search all vertices in list
    for(Vertex v : allVertices)
    {
      //if still basic initialization, replace
      if(minVertex == null && v.known == false)
      {
        minVertex = v;
        minDist = v.distance;
      }

      //if distance is smaller, replace
      else if(v.known == false && v.distance < minDist)
      {
        minDist = v.distance;
        minVertex = v;
      }
    }
    
    return minVertex;
  }

  /**
   * Returns a list of edges for a path from city s to city t. This will be the
   * shortest path from s to t as prescribed by Dijkstra's algorithm
   * 
   * @param s
   *          (String) starting city name
   * @param t
   *          (String) ending city name
   * @return (List<Edge>) list of edges from s to t
   */
  public List<Edge> getDijkstraPath(String s, String t) 
  {
    doDijkstra(s);
    
    //use LinkedList as Stack for LIFO storage
    LinkedList<Edge> formPath = new LinkedList<>();
    //finalPath represents List to be returned
    List<Edge> finalPath = new ArrayList<>();
    
    Vertex current = vertexNames.get(t);
    Vertex prev = current.prev;

    double edgeDistance = 0;
  
    while(prev != null)
    {
      for(Edge e: prev.adjacentEdges)
      {
        //Determine edge distance
        if(e.target.name.equals(current.name))
        {
          formPath.push(e);
        }
      }
      
      current = prev;
      prev = current.prev;
    }
    
    while(formPath.size()!=0)
    {
      finalPath.add(formPath.pop());
    }
    
    return finalPath; 
  }

  // STUDENT CODE ENDS HERE

  /**
   * Prints out the adjacency list of the dijkstra for debugging
   */
  public void printAdjacencyList() {
    for (String u : vertexNames.keySet()) {
      StringBuilder sb = new StringBuilder();
      sb.append(u);
      sb.append(" -> [ ");
      for (Edge e : vertexNames.get(u).adjacentEdges) {
        sb.append(e.target.name);
        sb.append("(");
        sb.append(e.distance);
        sb.append(") ");
      }
      sb.append("]");
      System.out.println(sb.toString());
    }
  }


  /** 
   * A main method that illustrates how the GUI uses Dijkstra.java to 
   * read a map and represent it as a graph. 
   * You can modify this method to test your code on the command line. 
   */
  public static void main(String[] argv) throws IOException {
    String vertexFile = "cityxy.txt"; 
    String edgeFile = "citypairs.txt";

    Dijkstra dijkstra = new Dijkstra();
    String line;

    // Read in the vertices
    BufferedReader vertexFileBr = new BufferedReader(new FileReader(vertexFile));
    while ((line = vertexFileBr.readLine()) != null) {
      String[] parts = line.split(",");
      if (parts.length != 3) {
        vertexFileBr.close();
        throw new IOException("Invalid line in vertex file " + line);
      }
      String cityname = parts[0];
      int x = Integer.valueOf(parts[1]);
      int y = Integer.valueOf(parts[2]);
      Vertex vertex = new Vertex(cityname, x, y);
      dijkstra.addVertex(vertex);
    }
    vertexFileBr.close();

    BufferedReader edgeFileBr = new BufferedReader(new FileReader(edgeFile));
    while ((line = edgeFileBr.readLine()) != null) {
      String[] parts = line.split(",");
      if (parts.length != 3) {
        edgeFileBr.close();
        throw new IOException("Invalid line in edge file " + line);
      }
      dijkstra.addUndirectedEdge(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
    edgeFileBr.close();

    // Compute distances. 
    // This is what happens when you click on the "Compute All Euclidean Distances" button.
    dijkstra.computeAllEuclideanDistances();
    
    // print out an adjacency list representation of the graph
    dijkstra.printAdjacencyList();

    // This is what happens when you click on the "Draw Dijkstra's Path" button.

    // In the GUI, these are set through the drop-down menus.
    String startCity = "SanFrancisco";
    String endCity = "Boston";

    // Get weighted shortest path between start and end city. 
    List<Edge> path = dijkstra.getDijkstraPath(startCity, endCity);
    
    System.out.print("Shortest path between "+startCity+" and "+endCity+": ");
    System.out.println(path);
  }

}
