
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emmanuel
 */
public class Superman {

    private static int[][] grid;
    private static LinkedList<Integer[]> scoreMem;
    private static int bestJumpScore, bestJumpScoreBuilding, nextBestJumpScore;
    private static int jumpHeight, numFloors, numBuildings;
    private static Logger logger = Logger.getLogger("Superman");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        try {
            FileInputStream is = new FileInputStream(new File("superman-input16.txt"));
            System.setIn(is);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        long start = System.currentTimeMillis();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] s = in.readLine().split("\\s");
        numBuildings = Integer.parseInt(s[0]); // number of buildings
        numFloors = Integer.parseInt(s[1]); // height of buildings
        jumpHeight = Integer.parseInt(s[2]); // floors dropped in jump
        grid = new int[numBuildings][numFloors];
        scoreMem = new LinkedList<>(); // only cache traversable levels
        for (int i = 0; i < numBuildings; i++) {
            String[] p = in.readLine().split("\\s");
            for (int j = 0; j < Integer.parseInt(p[0]); j++) {
                int floor = Integer.parseInt(p[j + 1]) - 1; // floor count
                grid[i][floor] += 1;
            }
        }
        logger.log(Level.INFO, "{0}ms for input processing", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        Integer[] scores = calculateScores();
        long diff = System.currentTimeMillis() - start;
        logger.log(Level.INFO, "{0}ms for routing ({1}ms floor avg)",
                new Object[]{diff, diff / numFloors});
        start = System.currentTimeMillis();
        int max = 0;
        for (Integer score : scores)
            max = score > max ? score : max;
        System.out.println(max); // test input16 expected - 3610000
        logger.log(Level.INFO, "{0}ms for getting route", (System.currentTimeMillis() - start));
    }

    // calculate scores from bottom up filling up score memory as well
    private static Integer[] calculateScores() {
        Integer[] scores = null;
        for (int j = 0; j < numFloors; j++) {
            scores = new Integer[numBuildings];
            calculateBestJumpScore();
            for (int i = 0; i < numBuildings; i++)
                scores[i] = grid[i][j] + bestScore(i);
            scoreMem.add(scores);
            if (scoreMem.size() > jumpHeight)
                scoreMem.removeFirst();
            resetBestJumpScoreMem();
        }
        return scores;
    }

    // best score using current score memory
    private static int bestScore(int building) {
        int maxScore = 0;
        if (!scoreMem.isEmpty()) {
            int next = scoreMem.getLast()[building];
            int jumpScore = bestJumpScoreBuilding == building ? nextBestJumpScore : bestJumpScore;
            maxScore = next > jumpScore ? next : jumpScore;
        }
        return maxScore;
    }

    // calculate the best jump score and store the results for quick access
    private static void calculateBestJumpScore() {
        // if can jump
        if (scoreMem.size() == jumpHeight) {
            Integer score = 0;
            Integer[] jumpLevel = scoreMem.getFirst();
            for (int i = 0; i < numBuildings; i++) {
                score = jumpLevel[i];
                if (score > bestJumpScore) {
                    nextBestJumpScore = bestJumpScore;
                    bestJumpScoreBuilding = i;
                    bestJumpScore = score;
                } else if (score > nextBestJumpScore)
                    nextBestJumpScore = score;
            }
        }
    }

    // do this after processing each floor
    private static void resetBestJumpScoreMem() {
        bestJumpScore = 0;
        bestJumpScoreBuilding = 0;
        nextBestJumpScore = 0;
    }

}
