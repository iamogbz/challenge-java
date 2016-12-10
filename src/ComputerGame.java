
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComputerGame {

    final private static Logger logger = Logger.getLogger("ComputerGame");
    final private static DebugTimer timer = new DebugTimer();
    private static BufferedReader in;
    private static Integer n, pi, vn, source, dest, result;
    private static Map<Integer, Integer> primeNodes;
    private static List<Dinic.Edge>[] graph;

    public static void main(String args[]) throws IOException {
        try {
            // sample result - 1867
            FileInputStream is = new FileInputStream(new File("inputs/computer-game-09.txt"));
            System.setIn(is);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            return;
        }
        String[] s;
        int[] n1, n2;
        timer.start();
        in = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(in.readLine());
        n1 = new int[n];
        s = in.readLine().split("\\s");
        for (int i = 0; i < n; i++)
            n1[i] = Integer.parseInt(s[i]);
        n2 = new int[n];
        s = in.readLine().split("\\s");
        for (int i = 0; i < n; i++)
            n2[i] = Integer.parseInt(s[i]);
        timer.lap("reading input");

        source = 0;
        dest = 1;
        primeNodes = new HashMap<>();
        graph = Dinic.createGraph(300000);
        for (int i = 0; i < n; i++) {
            vn = i + 2;
            Dinic.addEdge(graph, source, vn, 1);
            for (Integer p : primeFactors(n1[i])) {
                pi = primeNodes.get(p);
                if (pi == null) {
                    pi = n * 2 + primeNodes.size() + 1;
                    primeNodes.put(p, pi);
                }
                Dinic.addEdge(graph, vn, pi, 1);
            }
        }
        for (int i = 0; i < n; i++) {
            vn = n + i + 2;
            Dinic.addEdge(graph, vn, dest, 1);
            for (Integer p : primeFactors(n2[i])) {
                pi = primeNodes.get(p);
                if (pi != null)
                    Dinic.addEdge(graph, pi, vn, 1);
            }
        }
        timer.lap("factoring graph");

        result = Dinic.maxFlow(graph, source, dest);
        timer.stop("solving max flow");

        System.out.println(result);
        logger.log(Level.INFO, timer.toString());
    }

    // TODO OPTIMIZE THIS - CACHE RESULTS?
    private static Set<Integer> primeFactors(int n) {
        final Set<Integer> primes = new HashSet<>();
        // While 2 divides n, add 2 and divide n
            while (n % 2 == 0) {
                primes.add(2);
                n /= 2;
            }
        // n must be odd at this point.  So we can
        // skip one element (Note i = i +2)
        for (int i = 3; i <= Math.sqrt(n); i += 2)
            // While i divides n, save i and divide n
            while (n % i == 0) {
                primes.add(i);
                    n /= i;
            }

        // This condition is to handle the case when
        // n is a prime number greater than 2
            if (n > 2)
                primes.add(n);
        return primes;
    }

}
