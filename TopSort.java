import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Sorts the classes in a given document using a topological sort. Classes 
 * are treated as vertices with unweighted edges.  
 * Based upon Weiss' Pseudocode in DS textbook.
 * LinkedHashMap was used to preserve both order and HashMap functionality.
 *   
 * @author VamsiG (uni:vg2383)
 * File: TopSort.java
 */
public class TopSort 
{

    LinkedHashMap<String, TopVert> classListing= new LinkedHashMap<String, TopVert>();
    private int NUM_VERTICES;
    
    /**
    * Constructs TopSort object.
    *
    * @param classListing the LinkedHashMap that contains all the classes
    */
    public TopSort(LinkedHashMap<String, TopVert> classListing) 
    {
        this.classListing = classListing;
        NUM_VERTICES = classListing.size();
    }

    /**
    * A method that sorts all the classes by a topological sort. 
    * Objects in the LinkedHashMap are sorted based on prerequisites.
    *
    * @throws CycleFoundException if there is a cycle in the graph
    */
    public void topsort( ) throws CycleFoundException
    {
        String[] orderedClasses = new String[NUM_VERTICES];
        Queue<TopVert> q = new ArrayDeque<>();
        int counter = 0;
        
        //checks all vertices
        for(TopVert v: classListing.values())
        {
            //adds to queue if indegree is 0
            if( v.getInDegree() == 0 )
            {
                q.add( v );
            }
        }
        
        //when queue is not empy
        while( !q.isEmpty( ) )
        {
            //dequeue and place in ordered array
            TopVert v = q.poll( );
            v.topNum = ++counter;
            //array based on counter values so 1 is subtracted
            //to ensure size is correct
            orderedClasses[counter - 1] = v.getClassName();

            LinkedList<TopVert> adjacent = v.getAdjactentClasses();
            //decrease indegree for all adjacent classes
            for(TopVert w: adjacent)
            {
                if( --w.inDegree == 0 )
                {
                    //add when inDegree becomes zero for these classes
                    q.add( w );
                }
            }
        }
        
        
        //throw error if all vertices are not ordered
        if( counter != NUM_VERTICES )
        {
            throw new CycleFoundException("Cycle has been found.");
        }

        //print topological listing
        print_classes(orderedClasses);


    }

    /**
    * Prints classes from an ordered array
    *
    * @param orderedClasses the array of ordered classnames
    */
    public void print_classes(String[] orderedClasses)
    {
        for(int x = 0; x < NUM_VERTICES; x++)
        {
            if (x == 0)
            {
                System.out.print("Topological Order: {");
            }
            
            System.out.print(orderedClasses[x]);
            
            if (x == NUM_VERTICES - 1)
            {
               System.out.print("}");
            }
            else
            {
               System.out.print("--> ");
            }                
        }
        System.out.println();

    }


    /**
    * Tests TopSort on CS classes from csmajor.txt file. 
    *
    * @param args unused
    */
    public static void main(String[] args) 
    {   
        try
        {

            LinkedHashMap<String, TopVert> csClasses = new LinkedHashMap<>();  
            
            Scanner classRead = new Scanner(new FileInputStream("csmajor.txt"));
            
            while (classRead.hasNextLine()) 
            {
                String classLine = classRead.nextLine();
                String[] classes = classLine.split(" ");

                //Form new Vertex
                TopVert newClass = new TopVert(classes[0]);
                csClasses.put(classes[0], newClass);

                //when there are multiple classes on a line
                //ensure they are shown as prerequisites
                if(classes.length > 1)
                {
                    for(int i = 1; i < classes.length; i++)
                    {
                        TopVert prereq = csClasses.get(classes[i]);
                        prereq.addClassAfter(newClass);
                    }
                }
            }
            
            //close file to avoid errors
            classRead.close();

            TopSort test = new TopSort(csClasses);
            
            //sort and print classes
            try
            {
                test.topsort();
            }
            //handle cycle exception
            catch(CycleFoundException e)
            {
                System.out.println("The file's contents created a cycle");
            }
  
        }
        //handle IO exception
        catch (IOException e)
        {
            System.out.println("The file could not be opened!");
        }
    }

    //Exception that is to be thrown when there is a cycle present
    public class CycleFoundException extends Exception 
    {

        public CycleFoundException(String message)
        {
            super(message);
        }

    }

}
