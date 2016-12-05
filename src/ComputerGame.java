
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComputerGame {

    final private static Logger logger = Logger.getLogger("ComputerGame");
    // list of factored numbers
    final private static List<FactoredInteger> factored1 = new LinkedList<>();
    final private static List<FactoredInteger> factored2 = new LinkedList<>();

    public static void main(String args[]) throws IOException {
        try {
            // sample result - 1867
            FileInputStream is = new FileInputStream(new File("inputs/computer-game.txt"));
            System.setIn(is);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        long start = System.currentTimeMillis();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine());
        //int n = 100000;
        String[] n1 = in.readLine().split("\\s");
        String[] n2 = in.readLine().split("\\s");
        logger.log(Level.INFO, "{0}ms for handling input", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        Integer num1, num2;
        Set<Integer> primeFactors1, primeFactors2;
        for (int i = 0; i < n; i++) {
            num1 = Integer.parseInt(n1[i]);
            //num = ThreadLocalRandom.current().nextInt(800000000, 1000000000);
            factored1.add(new FactoredInteger(num1, primeFactors(num1)));
            num2 = Integer.parseInt(n2[i]);
            //num = ThreadLocalRandom.current().nextInt(800000000, 1000000000);
            factored2.add(new FactoredInteger(num2, primeFactors(num2)));
        }
        logger.log(Level.INFO, "{0}ms for factoring primes", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        boolean[][] bpGraph = new boolean[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                HashSet<Integer> factors = new HashSet<>(factored1.get(i).factors);
                factors.retainAll(factored2.get(j).factors);
                bpGraph[i][j] = !factors.isEmpty();
            }
        logger.log(Level.INFO, "{0}ms for building graph", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        int result = maxBPM(bpGraph, n);
        logger.log(Level.INFO, "{0}ms for calculating bpm", (System.currentTimeMillis() - start));
        System.out.println(result);
    }

    // TODO OPTIMIZE THIS - CACHE RESULTS;
    private static Set<Integer> primeFactors(int n) {
        final Set<Integer> primes = new HashSet<>();
        // save the number of 2s that divide n
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

    // A DFS based recursive function that returns true if a
    // matching for vertex u is possible
    private static boolean bpm(boolean bpGraph[][], int u, boolean seen[],
            int matchR[], int n) {
        // Try every job one by one
        for (int v = 0; v < n; v++)
            // If applicant u is interested in job v and v
            // is not visited
            if (bpGraph[u][v] && !seen[v]) {
                seen[v] = true; // Mark v as visited

                // If job 'v' is not assigned to an applicant OR
                // previously assigned applicant for job v (which
                // is matchR[v]) has an alternate job available.
                // Since v is marked as visited in the above line,
                // matchR[v] in the following recursive call will
                // not get job 'v' again
                if (matchR[v] < 0 || bpm(bpGraph, matchR[v],
                        seen, matchR, n)) {
                    matchR[v] = u;
                    return true;
                }
            }
        return false;
    }

    // Returns maximum number of matching from M to N
    private static int maxBPM(boolean bpGraph[][], int n) {
        // An array to keep track of the applicants assigned to
        // jobs. The value of matchR[i] is the applicant number
        // assigned to job i, the value -1 indicates nobody is
        // assigned.
        int matchR[] = new int[n];

        // Initially all jobs are available
        for (int i = 0; i < n; ++i)
            matchR[i] = -1;

        int result = 0; // Count of jobs assigned to applicants
        for (int u = 0; u < n; u++) {
            // Mark all jobs as not seen for next applicant.
            boolean seen[] = new boolean[n];
            for (int i = 0; i < n; ++i)
                seen[i] = false;

            // Find if the applicant 'u' can get a job
            if (bpm(bpGraph, u, seen, matchR, n))
                result++;
        }
        return result;
    }

    private static class FactoredInteger implements Comparable {

        private final int number;
        private final Set<Integer> factors;

        public FactoredInteger(int number, Set<Integer> factors) {
            this.number = number;
            this.factors = factors;
        }

        @Override
        public String toString() {
            return number + factors.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof FactoredInteger)
                return number == ((FactoredInteger) o).number;
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + this.number;
            //hash = 37 * hash + Objects.hashCode(this.factors);
            return hash;
        }

        @Override
        public int compareTo(Object o) {
            int result = 0;
            if (o instanceof FactoredInteger) {
                result = factors.size() - ((FactoredInteger) o).factors.size();
                if (result == 0)
                    result = number - ((FactoredInteger) o).number;
            }
            return result;
        }

    }

}
