
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphComponents {

    final private static Logger logger = Logger.getLogger("GraphComponents");
    final private static DebugTimer timer = new DebugTimer();
    final private static Map<Integer, Set<Integer>> graph = new HashMap<>();

    public static void main(String[] args) throws IOException {
        try {
            FileInputStream is = new FileInputStream(new File("inputs/graph-component-test.txt"));
            System.setIn(is);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            return;
        }
        Reader.init(System.in);
        Writer.init(System.out);
        timer.start();
        int n = Reader.nextInt(); // number of edges;
        while (n-- > 0) {
            int a = Reader.nextInt();
            int b = Reader.nextInt();
            addEdge(a, b);
        }
        timer.lap("Reading input and populating graph");
        //TreeSet<Integer> comps = getComponents();
        Set<Integer> comps, seen = new HashSet<>();
        int max = 0, min = Integer.MAX_VALUE;
        for (Integer k : graph.keySet())
            if (!seen.contains(k)) {
                comps = DFS(k, new HashSet<>());
                max = comps.size() > max ? comps.size() : max;
                min = comps.size() < min ? comps.size() : min;
                seen.addAll(comps);
            }
        timer.stop("getting components");
        Writer.println(min + " " + max);
        logger.log(Level.INFO, timer.toString());
    }

    private static void addEdge(int n1, int n2) {
        Set<Integer> e1 = graph.get(n1);
        if (e1 == null) {
            e1 = new TreeSet<>();
            graph.put(n1, e1);
        }
        e1.add(n2);
        Set<Integer> e2 = graph.get(n2);
        if (e2 == null) {
            e2 = new TreeSet<>();
            graph.put(n2, e2);
        }
        e2.add(n1);
    }

    private static HashSet<Integer> DFS(int root, HashSet<Integer> seen) {
        Set<Integer> next = new HashSet<>();
        next.addAll(graph.get(root));
        next.removeAll(seen);
        seen.add(root);
        for (Integer n : next) seen.addAll(DFS(n, seen));
        return seen;
    }

}
