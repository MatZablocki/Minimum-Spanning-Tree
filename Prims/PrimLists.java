// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.Scanner;


class GraphLists {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;
    
    // used for traversing graph
    private int[] visited;
    private Queue q;

    
    
    
    // default constructor
    public GraphLists(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        // create adjacency lists, initialised to sentinel node z      
        
        adj = new Node[V+1];//initialise list  
        
        for(v = 1; v <= V; ++v)
            adj[v] = z;             //initialise all elements of the list to z  
        
       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));    
            
            // code to put edge into adjacency list  
            //this code adds each node twice in order for each vertex to have each edge
            Node part = new Node();
            part.vert = v;
            part.wgt = wgt;
            part.next = z;
            Node part2 = new Node();
            part2.vert = u;
            part2.wgt = wgt;
            part2.next = z;
            
            Node temp = adj[u];
            Node temp2 = adj[v];
            if(adj[u] == z)
            {
                adj[u] = part;
            }
            else
            {
                while(temp.next != z)//when the next node is z that means you are at the last node
                {
                    temp = temp.next;
                }
                temp.next = part;
            }  
            if(adj[v] == z)
            {
                adj[v] = part2;
            }
            else
            {
                while(temp2.next != z)//when the next node is z that means you are at the last node
                {
                    temp2 = temp2.next;
                }
                temp2.next = part2;
            }         
            
        }
    }
   
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
    
    // method to display the graph representation
    public void display() {
        int v;
        Node n;
        
        for(v=1; v<=V; ++v){
            System.out.print("\nadj[" + toChar(v) + "]");
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print( " -> | " + toChar(n.vert) + " | " + n.wgt + " | ");    
        }
        System.out.println("");
    }

    public void displayOrderDF() {
        int v;
        System.out.print("Order Visited");
        for(v=1; v<=V; ++v){

            for(int i=1;i<=V;i++)
            {
                if(visited[i] == v)
                {
                    System.out.print(" --> "+toChar(i));
                }
            }
            
        }
        System.out.println("");
    }

    public void displayOrderBF() {
        int v;
        System.out.println("Order Visited(in layers)");
        int largest = visited[1];
        for(v=1; v<=V; ++v)
        {
            if(visited[v]>largest)
            {
                largest = visited[v];
            }
        }
        for(v=1; v<=largest; ++v){
            System.out.print(" --> "); 
            for(int i=1;i<=V;i++)
            {
                if(visited[i]==v)
                {
                    System.out.print(" | "+toChar(i) + " | ");//for nice printing
                }
            } 
        }
        System.out.println("");
    }

    //For prims, there are 2 distance arrays, one within prims that calculates the shortest distance between vertexes and another within the heap which holds 
    //the weight of that vertex, this is for the purpose of seeing which vertex has lowest weight.
    //this is done by passing the weight of the edge in the insert function
    public void MST_Prim(int s)
	{
        int v;
        int wgt_sum = 0;
        int[] dist, parent, hPos;
        

        //code here
        dist = new int[V+1];
        parent = new int[V+1];
        hPos = new int[V+1];

        for(v = 1; v <= V; ++v)
        {
            dist[v] = Integer.MAX_VALUE;//starts at max value so all initial travel between vertexes is of lesser weight.
            parent[v] = 0;
            hPos[v] = 0;
        }
        
        Heap pq =  new Heap(V, dist, hPos);
        pq.insert(s, 0);//inserts starting vertex with weight 0
        while (!pq.isEmpty())  
        {
            v = pq.remove();//processes lowest weight vertex from the heap
            wgt_sum+=dist[v];//calculates weight

            dist[v] = -dist[v];//makes the distance the lowest integer so that its not visited again
            Node temp = adj[v];
            while(temp!=z)
            {
                if(temp.wgt<dist[temp.vert])//checks weight of each edge and visits it if its the new lowest value
                {
                    dist[temp.vert] = temp.wgt;
                    parent[temp.vert] = v;
                    
                    if(!pq.isIn(temp.vert))//if the vertex is not in the heap, add it with the weight required to visit it
                    {
                        pq.insert(temp.vert, temp.wgt);
                    }
                    else//if its already in the heap then alter its weight to the new lowest
                    {
                        pq.alter(temp.vert, temp.wgt);
                    }
                }
                temp = temp.next;
            }
        }
        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");
        
        mst = parent;                      		
	}
    
    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning tree parent array is:\n");
            for(int v = 1; v <= V; ++v)
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
            System.out.println("");
    }

    public int count;

    public void DF(int x)
    {
        System.out.println("Beginning Depth First Search:");
        visited = new int[V+1];
        // Node k;
        for(int i=1;i<=V;i++)
        {
            visited[i] = 0;
        }
        count = 0;
        
        dfVisit(x);
        displayOrderDF();

    }

    //visits an unvisited vertex connected to the current one then recursively repeats this till its reached as far as it can go, then it goes back
    public void dfVisit(int v)
    {
        count++;
        visited[v] = count;
        Node temp = adj[v];
        while(temp != z)
        {
            if(visited[temp.vert] == 0)
            {
                System.out.println("Going to " + toChar(temp.vert));
                dfVisit(temp.vert);
            }
            temp = temp.next;
            
        }
    }

    public void BF(int s)
    {
        System.out.println("Beginning Bredth First Search:");
        q = new Queue();
        visited = new int[V+1];
        for(int v=1;v<=V;v++)
        {
            visited[v] = 0;
        }
        count = 2;//Starting count at 2 as its incremented after the function call and first vertex is hardcoded to 1
        visited[s] = 1;
        bfVisit(s);
        displayOrderBF();
    }

    //adds all vertexes connected to starting vertex to a queue and visiting them, then going through the queue and visiting those
    public void bfVisit(int v)
    {
        Node temp = adj[v];
        boolean visitedSomething = false;
        while(temp!=z)
        {

            if(visited[temp.vert] == 0)
            {
                q.enQueue(temp.vert);
                System.out.println("BF just visited vertex "+toChar(temp.vert)+" coming from "+toChar(v));
                visited[temp.vert] = count;
                visitedSomething = true;
            }
            temp = temp.next;
        }
        if(visitedSomething)
        {
            count++;
        }
        if(!q.isEmpty())
        {
            int last;
            last = q.deQueue();
            bfVisit(last);
        }
    }
}

public class PrimLists {
    public static void main(String[] args) throws IOException
    {
        Scanner scan = new Scanner(System.in);
        String fname = "wGraph3.txt";
        GraphLists g = new GraphLists(fname);

         System.out.println("Please the starting vertex in integer form: ");
         int s = scan.nextInt();
        
        scan.close();
        //String fname = "wGraph3.txt";               

 
        g.DF(s); 
        
        g.BF(s);
        
        g.MST_Prim(s);
        
        g.showMST();       
        
    }
    
    
}


/*

Heap Code for efficient implementationofPrim's Alg

*/

class Heap
{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N <= 0;
    }


    //sift up needed to be set up to accept the value in the heap rather than the position so that alter() could use it
    public void siftUp( int k) 
    {
        dist[0] = Integer.MIN_VALUE;
        int v = a[hPos[k]];
        
        hPos[v] = hPos[k];
        while(dist[v]<dist[a[hPos[v]/2]])//dist array never changes in sorting as its index based on value and not position
        {
            a[hPos[v]] = a[hPos[v]/2];
            hPos[a[hPos[v]/2]] = hPos[v];//This uses hPos to find the parent 
            hPos[v] /= 2;
        }
        
        a[hPos[v]] = v;

        // code yourself
        // must use hPos[] and dist[] arrays
    }


    public void siftDown( int k) 
    {
        int value, child;
       
        value = a[k];  
        hPos[value] = k;
        child = hPos[value]*2;
        while(child<=N)
        {
            if(child<N && dist[a[child+1]] < dist[a[child]])
            {
                child++;
            }
            if(dist[value]<=dist[a[child]])
            {
                break;
            }
            a[hPos[value]] = a[child];
            hPos[a[child]] = hPos[value];//have to change hpos of both values being changed 
            hPos[value] = child;
            child = hPos[value]*2;
        }
        a[hPos[value]] = value;
        

        // code yourself 
        // must use hPos[] and dist[] arrays
    }

    //uses value based indexing so that the distances stay attached to their respective vertex
    public void insert( int x, int distance) 
    {
        a[++N] = x;
        dist[x] = distance;
        hPos[x] = N;

        siftUp( x);
    }
    
    //needed for prims
    public void alter(int target, int value)
    {
        dist[target] = value;
        siftUp(target);
    }


    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap        
        a[1] = a[N--];
        
        siftDown(1);
        
        a[N+1] = 0;  // put null node into empty spot
        
        return v;
    }

    //checks every element of array and sees if value passed is in it
    public boolean isIn(int x)
    {
        boolean isIt = false;
        for(int i=1;i<=N;i++)
        {
            if(a[i] == x)
            {
                isIt = true;
            }
        }
        return isIt;
    }

    //can display structure of heap, this was used for testing and clutters the output if ran so was left out
    public void display()
    {
        System.out.println("The Tree Structure of the Heap is:");
        System.out.println("| "+a[1] + " / "+dist[a[1]]+" | ");
        for(int i=1;i<=N/2;i=i*2)
        {
            for(int j=2*i;j<4*i && j<=N;j++)
            {
                System.out.print("| "+a[j] + " / "+dist[a[j]]+" | ");
            }
            System.out.println("");
        }
        System.out.println("");
        
    }

}

//Simple queue implementation for Breadth first search

class Queue
{
    private class Node
    {
        int data;
        Node next;
    }

    Node z,head,tail;

    public Queue()
    {
        z = new Node();
        z.next = z;
        head = z;
        tail = null;
    }

    public void enQueue(int x)
    {
        Node temp;
        temp = new Node();

        temp.data = x;
        temp.next = z;

        if(head == z)
        {
            head = temp;
        }
        else
        {
            tail.next = temp;
        }
        tail = temp;
    }

    public int deQueue()
    {
        
        int value = head.data;
        head = head.next;
        return value;
       
    }

    public boolean isEmpty()
    {
        return head == head.next;
    }
}