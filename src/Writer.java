
import java.io.OutputStream;
import java.io.PrintWriter;

public class Writer {

    static PrintWriter printer;

    /**
     * call this method to initialise printer for OutputStream
     */
    static void init(OutputStream output) {
        printer = new PrintWriter(output);
    }

    static void print(String s) {
        printer.write(s);
        printer.flush();
    }

    static void println(String s) {
        print(s + "\n");
    }

}
