// Kruskal's Minimum Spanning Tree Algorithm
// Union-find implemented using disjoint set trees without compression

import java.io.*;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

import org.w3c.dom.UserDataHandler;    
 
class Edge {
    public int u, v, wgt;

    public Edge() {
        u = 0;
        v = 0;
        wgt = 0;
    }

    //initialises edge
    public Edge( int x, int y, int w) {
        this.u = x;
        this.v = y;
        this.wgt = w;
        // missing lines
    }
    
    public void show() {
        System.out.print("Edge " + toChar(u) + "--" + wgt + "--" + toChar(v) + "\n") ;
    }
    
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
}


class Heap
{
	private int[] h;
    int N, Nmax;
    Edge[] edge;


    // Bottom up heap construct
    public Heap(int _N, Edge[] _edge) {
        int i;
        Nmax = N = _N;
        h = new int[N+1];
        edge = _edge;
       
        // initially just fill heap array with 
        // indices of edge[] array.
        for (i=0; i <= N; ++i) //0 value doesnt matter as only sift down is used
        {
            h[i] = i;
        }  
        // Then convert h[] into a heap
        // from the bottom up.
        for(i = N/2; i > 0; --i)
        {
            siftDown(i);// this code finds every part of the heap that has a child and sifts it down
        }
    }



    


    private void siftDown( int k) {
        int e, j, child;

        e = h[k];
        j = edge[e].wgt;
        child = k*2;
        while( child <= N) //while there is a child
        {
            if(child<N && edge[h[child+1]].wgt < edge[h[child]].wgt)//if there is a right child and its weight is smaller than the left
            {
                child++;//switch to the right child
            }
            if(j<edge[h[child]].wgt)//if value sorted is less than the child value then break
            {
                break;
            }
            h[k] = h[child];//swap to now reference child as testing value and set a new child
            k = child;
            child = child*2;
            // missing lines
        }
        h[k] = e;//move the element to its correct position
    }


    public int remove() {
        h[0] = h[1];
        h[1] = h[N--];
        siftDown(1);
        return h[0];
    }

}

/****************************************************
*
*       UnionFind partition to support union-find operations
*       Implemented simply using Discrete Set Trees
*
*****************************************************/

class UnionFindSets
{
    private int[] treeParent, rank;
    private int N;
    
    public UnionFindSets( int V)
    {
        N = V;
        treeParent = new int[V+1];
        rank = new int[V+1];
        for(int i=1;i<=N;i++)
        {
            treeParent[i] = i;//sets every element to be its own parent
            rank[i] = 0;
        }
        // missing lines
    }

    public int findSet( int vertex)
    {   
        // missing lines
        //System.out.print("Vertex "+toChar(vertex)+" | current group is: ");
        if (treeParent[vertex]!=vertex)//if it is already part of a tree
        {
            //System.out.print(treeParent[vertex]+" -> ");
            treeParent[vertex] = findSet(treeParent[vertex]);//set all elements in that tree to point to the root of it
        }
        //System.out.print(treeParent[vertex]);
        //System.out.println();
        return treeParent[vertex];//return the root
    }
    
    public void union( int set1, int set2)
    {
        // this checks which tree is bigger and connects the smaller tree to the larger tree
        if(rank[set1]<rank[set2])
        {
            treeParent[set1] = set2;
        }
        else if(rank[set1]>rank[set2])
        {
            treeParent[set2] = set1;
        }
        else
        {
            treeParent[set2] = set1;
            rank[set1]= rank[set1]+1;
        }        
    }
    
    public void showTrees()
    {
        int i;
        for(i=1; i<=N; ++i)
            System.out.print(toChar(i) + "->" + toChar(treeParent[i]) + "  " );
        System.out.print("\n");
    }
    
    public void showSets()
    {
        int u, root;
        int[] shown = new int[N+1];
        for (u=1; u<=N; ++u)
        {   
            root = findSet(u);
            if(shown[root] != 1) {
                showSet(root);
                shown[root] = 1;
            }            
        }   
        System.out.print("\n");
    }

    private void showSet(int root)
    {
        int v;
        System.out.print("Set{");
        for(v=1; v<=N; ++v)
            if(findSet(v) == root)
                System.out.print(toChar(v) + " ");
        System.out.print("}  ");
    
    }
    
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
}

class Graph 
{ 
    private int V, E;
    private Edge[] edge;
    private Edge[] mst;        

    public Graph(String graphFile) throws IOException
    {
        int u, v;
        int w, e;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create edge array
        edge = new Edge[E+1];   
        
       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            w = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + w + ")--" + toChar(v));                         
            edge[e] = new Edge(u,v,w);//adds all edges to the edge array
            // create Edge object  
        }
    }


/**********************************************************
*
*       Kruskal's minimum spanning tree algorithm
*
**********************************************************/
public Edge[] MST_Kruskal() 
{
    int i = 1;
    Edge e;
    int uSet, vSet;
    UnionFindSets partition;
    
    // create edge array to store MST
    // Initially it has no edges.
    mst = new Edge[V+1];

    // priority queue for indices of array of edges
    Heap h = new Heap(E, edge);
    
    // create partition of singleton sets for the vertices
    partition = new UnionFindSets(V);
    while(i<V)//loops untill all vertexes have an edge
    {
        e = edge[h.remove()];//e is the edge with the smallest weight as heap is sorted that way
        uSet = partition.findSet(e.u);//find the root of both vertexes
        vSet = partition.findSet(e.v);
        if(uSet!=vSet)//if they are not already connected, connect them and add them to the mst
        {
            partition.union(uSet, vSet);
            mst[i++] = e;
            System.out.println("\nAdding edge: "+toChar(e.u)+" -- "+e.wgt+" -> "+toChar(e.v));
            partition.showSets();
            System.out.print("---------");
        }
    }
    
    
    return mst;
}


    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }

    public void showMST()
    {
        System.out.print("\nMinimum spanning tree build from following edges:\n");
        int total=0;
        for(int e = 1; e < V; ++e) {
            mst[e].show(); 
            total+=mst[e].wgt;
        }
        System.out.println("Total weight of MST = "+total);
       
    }

} // end of Graph class
    
    // test code
class KruskalTrees {
    public static void main(String[] args) throws IOException
    {
        
        String fname = "wGraph3.txt";

        Graph g = new Graph(fname);

        g.MST_Kruskal();

        g.showMST();
        
    }
}    