
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComputerGame {

    final private static Logger logger = Logger.getLogger("ComputerGame");

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
        int[] lineA = new int[n], lineB = new int[n];
        for (int i = 0; i < n; i++) {
            lineA[i] = Integer.parseInt(n1[i]);
            //num = ThreadLocalRandom.current().nextInt(800000000, 1000000000);
            lineB[i] = Integer.parseInt(n2[i]);
            //num = ThreadLocalRandom.current().nextInt(800000000, 1000000000);
        }
        logger.log(Level.INFO, "{0}ms for handling input", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        boolean[][] bpGraph = new boolean[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                bpGraph[i][j] = !coprime(lineA[i], lineB[j]);
        logger.log(Level.INFO, "{0}ms for building graph", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        int result = maxBPM(bpGraph, n);
        logger.log(Level.INFO, "{0}ms for calculating bpm", (System.currentTimeMillis() - start));
        System.out.println(result);
    }

    // TODO OPTIMIZE THIS - CACHE RESULTS?
    private static boolean coprime(int a, int b) {
        int t;
        while (b != 0) {
            t = a;
            a = b;
            b = t % b;
        }
        return a == 1; // gcd == 1 = coprime
    }

    // Returns maximum number of matching from M to N
    private static int maxBPM(boolean bpGraph[][], int n) {
        // An array to keep track of the applicants assigned to
        // jobs. The value of matches[i] is the applicant number
        // assigned to job i, the value -1 indicates nobody is
        // assigned.
        int matches[] = new int[n];

        // Initially all jobs are available
        for (int i = 0; i < n; ++i)
            matches[i] = -1;

        int result = 0; // Count of jobs assigned to applicants
        for (int u = 0; u < n; u++) {
            // Mark all jobs as not seen for next applicant.
            boolean seen[] = new boolean[n];
            for (int i = 0; i < n; ++i)
                seen[i] = false;

            // Find if the applicant 'u' can get a job
            if (bpm(bpGraph, u, seen, matches, n))
                result++;
        }
        return result;
    }

    // A DFS based recursive function that returns true if a
    // matching for vertex u is possible
    private static boolean bpm(boolean bpGraph[][], int u, boolean seen[],
            int matches[], int n) {
        // Try every item one by one
        for (int v = 0; v < n; v++)
            // If u is not coprime of v and v is not visited
            if (bpGraph[u][v] && !seen[v]) {
                seen[v] = true; // mark v as visited

                // If item 'v' is not matched to any item 'u' OR previously 
                // assigned match for v (which is matches[v]) has an alternate 
                // job available. Since v is marked as visited in the above line
                // matches[v] the recursive call below will not check v again
                if (matches[v] < 0 || bpm(bpGraph, matches[v], seen, 
                        matches, n)) {
                    matches[v] = u;
                    return true;
                }
            }
        return false;
    }

}
