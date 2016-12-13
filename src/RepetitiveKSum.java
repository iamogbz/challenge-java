
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepetitiveKSum {

    final private static Logger logger = Logger.getLogger("RKSum");
    final private static DebugTimer timer = new DebugTimer();
    private static int t, n, k;
    private static List<Long> s;
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
        for (int i = 0; i < t; i++) {
            n = Reader.nextInt();
            k = Reader.nextInt();
            s = new ArrayList<>();
            f = new ArrayList<>();
            do s.add(Reader.nextLong());
            while (Reader.hasNext());
            timer.lap("reading input");
            Collections.sort(s);
            timer.lap("sorting list");
            //Writer.println(n+" "+k+" "+s.toString());
            int size = s.size();
            int limit = n % 2 == 0 ? n / 2 : n / 2 + 1;
            long a = s.get(0) / k;
            long b = s.get(size - 1) / k;
            f.add(a);
            if(f.size() < n) f.add(b);
            for (int j = 1; j < limit; j++) {
                f.add(s.get(j) - a * (k - 1));
                if(f.size() < n) f.add(s.get(size - j - 1) - b * (k - 1));
            }
            timer.lap("extracting sequence");
            Collections.sort(f);
            timer.lap("sorting extracted");
            //Writer.println(f.stream().map(Object::toString).collect(Collectors.joining(" ")));
            Writer.println(toString(f));
            timer.lap("printing sequence");
        }
        timer.stop(null);
        Writer.println(timer.toString());
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
