
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
    // mapping of primes to numbers in the list
    final private static Map<Integer, List<FactoredInteger>> factored1 = new HashMap<>();
    final private static Map<Integer, List<FactoredInteger>> factored2 = new HashMap<>();
    // store result of prime factor for possible faster query
    final private static Map<Integer, Set<Integer>> primed = new HashMap<>();

    public static void main(String args[]) throws IOException {
        try {
            // sample result - 1867
            FileInputStream is = new FileInputStream(new File("computer-game.txt"));
            System.setIn(is);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        long start = System.currentTimeMillis();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        //int n = Integer.parseInt(in.readLine());
        int n = 100000;
        String[] n1 = in.readLine().split("\\s");
        String[] n2 = in.readLine().split("\\s");
        logger.log(Level.INFO, "{0}ms for handling input", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        Integer num;
        for (int i = 0; i < n; i++) {
            //num = Integer.parseInt(n1[i]);
            num = ThreadLocalRandom.current().nextInt(800000000, 1000000000);
            Set<Integer> primeFactors = primeFactors(num);
            for (Integer f : primeFactors) {
                if (!factored1.containsKey(f))
                    factored1.put(f, new LinkedList<>());
                factored1.get(f).add(new FactoredInteger(num, primeFactors));
            }
        }
        logger.log(Level.INFO, "{0}ms for processing primes 1", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            //num = Integer.parseInt(n2[i]);
            num = ThreadLocalRandom.current().nextInt(800000000, 1000000000);
            Set<Integer> primeFactors = primeFactors(num);
            for (Integer f : primeFactors) {
                if (!factored2.containsKey(f))
                    factored2.put(f, new LinkedList<>());
                factored2.get(f).add(new FactoredInteger(num, primeFactors));
            }
        }
        logger.log(Level.INFO, "{0}ms for processing primes 2", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        List<Integer> k1 = new ArrayList<>(factored1.keySet());
        Comparator<Integer> factored1Comparator = (Integer o1, Integer o2) -> {
            List<FactoredInteger> f1 = factored1.get(o1);
            List<FactoredInteger> f2 = factored1.get(o2);
            int c = factoredScore(f1) - factoredScore(f2);
            return c;
        };
        k1.sort(factored1Comparator);
        System.out.println("starting order: " + k1);
        logger.log(Level.INFO, "{0}ms for sorting factors 1", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        int removals = 0;
        FactoredInteger f1, f2;
        List<FactoredInteger> factored1k, factored2k;
        for (int ki = 0; ki < k1.size(); ki++) {
            Integer k = k1.get(ki);
            //for (Integer k : k1) {
            if (factored2.containsKey(k)) {
                // factored1k, factored2k;
                factored1k = factored1.get(k);
                factored2k = factored2.get(k);
                // order both lists;
                Collections.sort(factored1k);
                Collections.sort(factored2k);
                // using length of smaller list;
                int shorter = Math.min(factored1k.size(), factored2k.size());
                // remove from factor1 and factor2 the first numbers respectively;
                for (int i = 0; i < shorter; i++) {
                    f1 = factored1k.get(0);
                    //System.out.println("removing1: " + f1);
                    for (Integer f : f1.factors)
                        if (!Objects.equals(f, k))
                            factored1.get(f).remove(f1);
                    factored1k.remove(0);
                    f2 = factored2k.get(0);
                    //System.out.println("removing2: " + f2);
                    for (Integer f : f2.factors)
                        if (!Objects.equals(f, k))
                            factored2.get(f).remove(f2);
                    factored2k.remove(0);
                }
                // add length of smaller list to result;
                removals += shorter;
                // resort after each removal?
//                if (shorter > 0) {
//                    k1.sort(factored1Comparator);
//                    ki = 0;
//                    //System.out.println("removed: " + shorter);
//                    //System.out.println("new order: " + k1);
//                }
            }
        }
        logger.log(Level.INFO, "{0}ms for processing removals", (System.currentTimeMillis() - start));
        System.out.flush();
        System.out.println(factored1);
        System.out.println(factored2);
        System.out.println();
        System.out.println(removals);
    }

    // TODO OPTIMIZE THIS - CACHE RESULTS;
    private static Set<Integer> primeFactors(int n) {
        final Set<Integer> primes = new HashSet<>();
        // Print the number of 2s that divide n
        while (n % 2 == 0) {
            primes.add(2);
            n /= 2;
        }
        // n must be odd at this point.  So we can
        // skip one element (Note i = i +2)
        for (int i = 3; i <= Math.sqrt(n); i += 2)
            // While i divides n, print i and divide n
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

    private static int factoredScore(List<FactoredInteger> factored) {
        int score = 0;
        for (FactoredInteger fi : factored) {
            int s = fi.factors.size();
            score = score > s ? score : s;
        }
        return score;
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
