import java.util.LinkedList;

/**
 * Creates a Vertex that represents a class and all of the classes 
 * that are adjacent to it in the graphh
 *   
 * @author VamsiG 
 * File: TopVert.java
 */
class TopVert 
{
    private String className;
    public int topNum;
    public int inDegree;
    
    private LinkedList<TopVert> adjacenctClasses = new LinkedList<TopVert>();

    /**
    * Constructs TopVert object.
    *
    * @param className name of class 
    */
    public TopVert(String className) 
    {
        this.className = className;
    }

    /**
    * Allows for addition of adjacent classes
    *
    * @param classAfter the adjacent class 
    */
    public void addClassAfter(final TopVert classAfter){
        adjacenctClasses.add(classAfter);
        //ensure degree of other class is changed
        classAfter.inDegree++;
    }

    /**
    * Provides user with LL of adjacent classes.
    *
    * @return adjacentClasses LL of adjacent classes 
    */
    public LinkedList<TopVert> getAdjactentClasses()
    {
        return adjacenctClasses;
    }
    
    /**
    * Provides user with value of indegree.
    *
    * @return inDegree int value of indegree 
    */
    public int getInDegree()
    {
        return inDegree;
    }
    
    /**
    * Provides user with class name.
    *
    * @return className the String representation of the class name. 
    */
    public String getClassName()
    {
        return className;
    }


}
