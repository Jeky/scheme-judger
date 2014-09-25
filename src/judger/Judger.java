package judger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import kawa.standard.Scheme;

/**
 *
 * @author jeky
 */
public class Judger {

    public static String loadScript(String filename) {
        BufferedReader reader = null;
        StringBuilder buf = new StringBuilder();

        try {
            reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line);
                buf.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
            }
        }

        return buf.toString();
    }

    public static String[] getAllFiles() {
        File dir = new File(PATH);
        return dir.list();
    }

    public static boolean testCase(String script, String caseExp, String result) {
        try {
            Object r = scheme.eval(script + "\n" + caseExp);
            boolean test = r.toString().equals(result);

            if (test) {
                return test;
            } else {
                System.out.println("Case: " + caseExp + ", Expected: " + result + ", Result: " + r);
                return test;
            }
        } catch (Throwable ex) {
            System.out.println("Case: " + caseExp + ", Expected: " + result + ", Exception: " + ex);
            return false;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (String filename : getAllFiles()) {
            System.out.println("ID:" + filename);
            String script = loadScript(PATH + filename);

            boolean success = true;

            for (Map.Entry<String, String> entry : testCases.entrySet()) {
                String caseExp = entry.getKey();
                String result = entry.getValue();

                if (!testCase(script, caseExp, result)) {
                    success = false;
                    break;
                }
            }

            if (success) {
                System.out.println("Full Mark!");
            }
        }
    }

    public static Scheme scheme = new Scheme();
    public static Map<String, String> testCases = new HashMap<>();
    public static final String PATH = "/Users/jeky/Documents/440/a1/";

    static {
        testCases.put("(calculator '(1 + 2))", "3");
        testCases.put("(calculator '(1 - 2))", "-1");
        testCases.put("(calculator '(1 + 2 + 3 - 4))", "2");
        testCases.put("(calculator '(1))", "1");
    }
}
