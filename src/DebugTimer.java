
import java.util.ArrayList;
import java.util.List;

/**
 * For measuring code time
 *
 * @author Emmanuel
 */
public class DebugTimer {

    private final List<Lap> laps;
    private long startTime, lapTime, stopTime;
    private boolean running;

    public DebugTimer() {
        this.laps = new ArrayList<>();
        this.running = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(running ? "Running" : "Total");
        sb.append(" time: ").append(stopTime - startTime).append("ms\n");
        laps.stream().forEach((l) -> {
            sb.append(l).append("\n");
        });
        return sb.toString();
    }

    /**
     * Start running the timer
     */
    public void start() {
        if (!running) {
            laps.clear();
            startTime = System.currentTimeMillis();
            lapTime = startTime;
            running = true;
        }
    }

    /**
     * Add a lap to the timer
     *
     * @param name the name of the lap
     * @return lap time, if the time is not running alway return 0
     */
    public long lap(String name) {
        long time = 0;
        if (running) {
            time = System.currentTimeMillis();
            laps.add(new Lap(time - this.lapTime, name));
            this.lapTime = time;
        }
        return time;
    }

    public void stop(String name) {
        stopTime = lap(name);
        running = false;
    }

    /**
     * Get string representation of last lap
     *
     * @return the last lap ran as a string
     */
    public Lap lastLap() {
        try {
            return laps.get(laps.size() - 1);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private String lapInfo(int n) {
        try {
            return laps.get(n - 1).toString();
        } catch (IndexOutOfBoundsException e) {
            return "No Lap Found";
        }
    }

    public class Lap {

        private final long time;
        private final String name;

        public Lap(long time, String name) {
            this.time = time;
            this.name = name == null ? "" : name;
        }

        @Override
        public String toString() {
            return time + "ms - " + name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass())
                return false;
            final Lap other = (Lap) obj;
            return (this.time == other.time) && this.name.equals(other.name);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = (int) (67 * hash + this.time);
            hash = 67 * hash + this.name.hashCode();
            return hash;
        }

    }

}
