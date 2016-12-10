
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static Integer[] primeSieve;
    private static List<Dinic.Edge>[] graph;

    public static void main(String args[]) throws IOException {
        try {
            // sample result - 1867
            FileInputStream is = new FileInputStream(new File("inputs/computer-game-10.txt"));
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

        primeSieve = primeSieve(1000000000);
        timer.lap("sieving primes");

        source = 0;
        dest = 1;
        primeNodes = new HashMap<>();
        graph = Dinic.createGraph(300000);
        for (int i = 0; i < n; i++) {
            vn = i + 2;
            Dinic.addEdge(graph, source, vn, 1);
            for (Integer p : primeFactors(n1[i], primeSieve)) {
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
            for (Integer p : primeFactors(n2[i], primeSieve)) {
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

    private static Integer[] primeSieve(int max) {
        List<Integer> sieve = new ArrayList<>();
        int[] divisor = new int[(int) Math.sqrt(max) - 2];
        boolean[] prime = new boolean[(int) Math.sqrt(max) + 3];
        Arrays.fill(prime, true);
        for (int i = 2; i < prime.length; i++) {
            if (!prime[i])
                continue;
            divisor[i] = i;
            int j = i * i;
            while (j < prime.length) {
                prime[j] = false;
                if (j < divisor.length)
                    divisor[j] = i;
                j += i;
            }
            sieve.add(i);
        }
        return sieve.toArray(new Integer[0]);
    }

    private static Set<Integer> primeFactors(int n, Integer[] sieve) {
        final Set<Integer> primes = new HashSet<>();
        for (Integer p : sieve) {
            if(p > Math.sqrt(n) + 1) break;
            while (n % p == 0) {
                primes.add(p);
                n /= p;
            }
        }
        if (n > 2)
            primes.add(n);
        return primes;
    }

}
