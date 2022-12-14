package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Graph {
    private int V; // No. of vertices in graph.

    private LinkedList<Integer>[] adj; // Adjacency List
    // representation

    ArrayList<ArrayList<Integer>> components = new ArrayList<>();

    /***
     * Graph constructor
     * @param v - number of vertices in graph
     */
    public Graph(int v)
    {
        V = v;
        adj = new LinkedList[v+1];

        for (int i = 1; i <= v; i++)
            adj[i] = new LinkedList();
    }

    /***
     * add edges in graph
     * @param u - node
     * @param w - node
     */
    public void addEdge(int u, int w)
    {
        adj[u].add(w);
        adj[w].add(u);
    }

    /***
     *
     * @param v - vertice
     * @param visited - determinate if a vertice is visited or not
     * @param al - list
     */
    void DFSUtil(int v, boolean[] visited, ArrayList<Integer> al)
    {
        visited[v] = true;
        al.add(v);
        Iterator<Integer> it = adj[v].iterator();

        while (it.hasNext()) {
            int n = it.next();
            if (!visited[n])
                DFSUtil(n, visited, al);
        }
    }

    /***
     * DFS
     */
    public void DFS()
    {
        boolean[] visited = new boolean[V+1];

        for (int i = 1; i <= V; i++) {
            ArrayList<Integer> al = new ArrayList<>();
            if (!visited[i]) {
                DFSUtil(i, visited, al);
                components.add(al);
            }
        }
    }

    /***
     * connected components in a graph
     * @return int - number of connected components
     */
    public int ConnectedComponents() {
        return components.size();
    }

    /***
     * list of connected components
     * @return list of lists of integers
     */
    public ArrayList<ArrayList<Integer> > returnComponents() {
        return components;
    }
}



