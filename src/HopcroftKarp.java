
/**
 * Java Program to Implement Hopcroft Kraft Algorithm
 *
 */
import java.util.*;

/**
 * Class Hopcroft Kraft
 */
public class HopcroftKarp {

    private final int NIL = 0;
    private final int INF = Integer.MAX_VALUE;
    private ArrayList<Integer>[] Adj;
    private int[] Pair;
    private int[] Dist;
    private int cx, cy;

    /**
     * Function BFS * breadth first search
     *
     * @return boolean
     */
    public boolean BFS() {
        Queue<Integer> queue = new LinkedList<>();
        for (int v = 1; v <= cx; ++v)
            if (Pair[v] == NIL) {
                Dist[v] = 0;
                queue.add(v);
            } else
                Dist[v] = INF;

        Dist[NIL] = INF;

        while (!queue.isEmpty()) {
            int v = queue.poll();
            if (Dist[v] < Dist[NIL])
                for (int u : Adj[v])
                    if (Dist[Pair[u]] == INF) {
                        Dist[Pair[u]] = Dist[v] + 1;
                        queue.add(Pair[u]);
                    }
        }
        return Dist[NIL] != INF;
    }

    /**
     * Function DFS * depth first search
     *
     * @param v vertex index
     * @return boolean
     */
    public boolean DFS(int v) {
        if (v != NIL) {
            for (int u : Adj[v])
                if (Dist[Pair[u]] == Dist[v] + 1)
                    if (DFS(Pair[u])) {
                        Pair[u] = v;
                        Pair[v] = u;
                        return true;
                    }

            Dist[v] = INF;
            return false;
        }
        return true;
    }

    /**
     * Function to get maximum matching
     *
     * @return number of maximum matches
     */
    public int maxMatch() {
        Pair = new int[cx + cy + 1];
        Dist = new int[cx + cy + 1];
        int matching = 0;
        while (BFS())
            for (int v = 1; v <= cx; ++v)
                if (Pair[v] == NIL)
                    if (DFS(v))
                        matching = matching + 1;
        return matching;
    }
    
    /**
     * Set adjacency matrix size
     * @param x
     * @param y 
     */
    public void setSize(int x, int y){
        cx = x;
        cy = y;
    }

    /**
     * Function to make graph with vertices x, y
     *
     * @param x list of vertices
     * @param y list of 
     * @param E length of lists
     */
    public void makeGraph(Integer[] x, Integer[] y, int E) {
        Adj = new ArrayList[cx + cy + 1];
        for (int i = 0; i < Adj.length; ++i)
            Adj[i] = new ArrayList<>();
        /*
         * adding edges
         */
        for (int i = 0; i < E; ++i)
            addEdge(x[i] + 1, y[i] + 1);
    }

    /**
     * Function to add an edge between 
     *
     * @param u vertex one
     * @param v vertex two
     */
    public void addEdge(int u, int v) {
        Adj[u].add(cx + v);
        Adj[cx + v].add(u);
    }
}
