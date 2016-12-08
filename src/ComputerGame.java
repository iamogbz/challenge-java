
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    private static Integer n, num, pi, vn, source, dest, result;
    private static String[] n1, n2;
    private static List<Integer> nodes1, nodes2;
    private static Map<Integer, Integer> primeNodes;

    public static void main(String args[]) throws IOException {
        try {
            // sample result - 1867
            FileInputStream is = new FileInputStream(new File("inputs/computer-game-09.txt"));
            System.setIn(is);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            return;
        }
        timer.start();

        in = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(in.readLine());
        n1 = in.readLine().split("\\s");
        n2 = in.readLine().split("\\s");
        timer.lap("reading input");

        source = 0; dest = 1;
        primeNodes = new HashMap<>();
        nodes1 = new ArrayList<>();
        nodes2 = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            vn = i + 2;
            nodes1.add(source);
            nodes2.add(vn);
            num = Integer.parseInt(n1[i]);
            primeFactors(num).stream().map((p) -> {
                pi = primeNodes.get(p);
                if(pi == null) {
                    pi = n * 2 + primeNodes.size() + 1;
                    primeNodes.put(p, pi);
                }
                return p;
            }).map((_item) -> {
                nodes1.add(vn);
                return _item;
            }).forEach((_item) -> {
                nodes2.add(pi);
            });
            vn = n + i + 2;
            nodes1.add(vn);
            nodes2.add(dest);
            num = Integer.parseInt(n2[i]);
            primeFactors(num).stream().map((p) -> {
                pi = primeNodes.get(p);
                if(pi == null) {
                    pi = n * 2 + primeNodes.size() + 1;
                    primeNodes.put(p, pi);
                }
                return p;
            }).map((_item) -> {
                nodes1.add(pi);
                return _item;
            }).forEach((_item) -> {
                nodes2.add(vn);
            });
        }
        timer.lap("factoring adjacency map");

        List<Dinic.Edge>[] graph = Dinic.createGraph(n * 2 + primeNodes.size() + 2);
        for (int i = 0; i < nodes1.size(); i++)
            Dinic.addEdge(graph, nodes1.get(i), nodes2.get(i), 1);
        timer.lap("building graph");

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
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            // While i divides n, save i and divide n
            while (n % i == 0) {
                primes.add(i);
                n /= i;
            }
        }

        // This condition is to handle the case when
        // n is a prime number greater than 2
        if (n > 2)
            primes.add(n);
        return primes;
    }

}
