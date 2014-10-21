package judger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
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

    public static String[] getAllFiles(String path) {
        File dir = new File(path);
        return dir.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".scm");
            }
        });
    }

    public static boolean testCase(String script, String caseExp, String result) {
        try {
            Object r = scheme.eval(script + "\n" + caseExp);
            boolean test = r.toString().equals(result);

            if (test) {
                System.out.println("Case: " + caseExp + "...Passed");
                return test;
            } else {
                System.out.println("Case: " + caseExp + "...Failed. Expected: " + result + ", Result: " + r);
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
        if (args.length != 1) {
            System.out.println("To run this judger: java -jar judger.jar ROOT_OF_A1");
            return;
        }
        String path = args[0];
        //LinkedHashMap<String, String> score = new LinkedHashMap<>();
        TreeMap<String, String> score = new TreeMap<>();

        for (String filename : getAllFiles(path)) {
            String id = filename.replace(".scm", "");
            System.out.println("ID:" + id);
            String script = loadScript(path + "/" + filename);

            int successCount = 0;

            for (Map.Entry<String, String> entry : testCases.entrySet()) {
                String caseExp = entry.getKey();
                String result = entry.getValue();

                if (testCase(script, caseExp, result)) {
                    successCount++;
                }
            }
            score.put(id, successCount + "/" + testCases.size());
            System.out.println(successCount + "/" + testCases.size());
        }

        System.out.println("Writing Result to file: a1.txt");

        PrintWriter writer = null;
        
        try {
            writer = new PrintWriter(new File("a1.txt"));
            for(Map.Entry<String, String> entry : score.entrySet()){
                writer.println(entry.getKey() + "\t" + entry.getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

            writer.close();
        }

    }

    public static Scheme scheme = new Scheme();
    public static Map<String, String> testCases = new HashMap<>();

    static {
        testCases.put("(calculator '(1 + 2))", "3");
        testCases.put("(calculator '(1 - 2))", "-1");
        testCases.put("(calculator '(1 + 2 + 3 - 4))", "2");
        testCases.put("(calculator '(1 + 2 - 3 + 4))", "4");
        testCases.put("(calculator '(1))", "1");
    }
}
