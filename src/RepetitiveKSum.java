
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepetitiveKSum {

    final private static Logger logger = Logger.getLogger("RKSum");
    final private static DebugTimer timer = new DebugTimer();
    private static int t, n, k;
    private static TreeMap<Long, Integer> s;
    private static List<Long> f;

    public static void main(String args[]) throws IOException {
        try {
            FileInputStream is = new FileInputStream(new File("inputs/repeat-ksum-08.txt"));
            System.setIn(is);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            return;
        }
        Reader.init(System.in);
        Writer.init(System.out);
        timer.start();
        t = Reader.nextInt();
        while (t-- > 0) {
            n = Reader.nextInt();
            k = Reader.nextInt();
            //Writer.println(n + " " + k);
            s = new TreeMap<>();
            f = new ArrayList<>();
            do put(Reader.nextLong());
            while (Reader.hasNext());
            timer.lap("reading input");
            //Writer.println(s.toString());
            long a = pollFirst() / k, b;
            f.add(a);
            for (int j = 1; j < n; j++) {
                b = s.firstKey() - a * (k - 1);
                f.add(b);
                removeKSums(1, j, b);
            }
            timer.lap("extracting sequence");
            //Writer.println(f.stream().map(Object::toString).collect(Collectors.joining(" ")));
            Writer.println(toString(f));
            timer.lap("printing sequence");
        }
        timer.stop(null);
        Writer.println(timer.toString());
    }

    private static int get(long x) {
        Integer ret = s.get(x);
        return ret == null ? 0 : ret;
    }

    private static void put(long x) {
        s.put(x, get(x) + 1);
    }

    private static long pollFirst() {
        long x = s.firstKey();
        remove(x);
        return x;
    }

    private static void remove(long x) {
        int c = s.get(x);
        if (c == 1) s.remove(x);
        else s.put(x, c - 1);
    }

    private static void removeKSums(int start, int end, long sum) {
        Writer.println(start + " " + end + " " + sum);
        if (start == k) {
            remove(sum);
            return;
        }
        for (int i = end; i >= 0; i--)
            removeKSums(start + 1, i, sum + f.get(i));
    }

    private static String toString(Collection c) {
        StringBuilder sb = new StringBuilder();
        c.stream().forEach((o) -> {
            sb.append(o.toString());
            sb.append(" ");
        });
        return sb.toString();
    }
}
