import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BackwardChaining {
    private static String query;
    private static ArrayList<String> goals;
    private static ArrayList<String> facts;
    private static ArrayList<String> clauses;
    private static ArrayList<String> entailed;

    public static void main(String[] args) {

        init(args[0]);

        System.out.println();
        System.out.println("query: " + query);
        System.out.println();

        //backwardChaining();

    }

    /**
     * Reads the rules file indicated by the user in command prompt, then sets up the initial values for
     * backward chaining. It does this by splitting the file into rules. It will add the rule to either a
     * set of facts (e.g. "p") or clauses (e.g. "q p" or "r p q"), depending on the rule. It also gets the
     * query to start the backward chaining with. If the file cannot be found, the rule file contains invalid
     * syntax, or the user enters incorrect execution arguments, an error will be printed and the program
     * will exit.
     * @param filename file to read rules from
     */
    private static void init(String filename) {
        goals = new ArrayList<String>();
        facts = new ArrayList<String>();
        clauses = new ArrayList<String>();
        entailed = new ArrayList<String>();

        try {
            File file = new File(filename);
            Scanner scan = new Scanner(file);
            System.out.println("Filename: " + filename);
            System.out.println();

            // get query and add to goals
            query = scan.next();
            goals.add(query);
            // go to next line (start of rules)
            scan.nextLine();

            System.out.println("Reading rules...");
            System.out.println();

            // get rules
            int numLines = 1;
            while (scan.hasNextLine()) {
                numLines++;
                String line = scan.nextLine();
                String[] atoms = line.split(" ");
                //System.out.println(line);
                //System.out.println(Arrays.toString(atoms));

                if (atoms.length == 1) {
                    System.out.println("fact: " + atoms[0] + " is TRUE");
                    facts.add(line);
                } else if (atoms.length == 2) {
                    System.out.println("implication: IF " + atoms[1] + " THEN " + atoms[0]);
                    clauses.add(line);
                } else if (atoms.length == 3) {
                    System.out.println("conjunction: IF " + atoms[1] + " AND " + atoms[2] + " THEN " + atoms[0]);
                    clauses.add(line);
                } else {
                    System.out.println("Invalid input at line " + numLines + ", please check syntax in rules file");
                    System.exit(0);
                }
            }
        } catch (FileNotFoundException e1) {
            System.out.println("Unable to find file: " + filename);
            System.exit(0);
        } catch (ArrayIndexOutOfBoundsException e2) {
            System.out.println("Usage: java BackwardChaining <filename>");
            System.exit(0);
        }
    }

    private static boolean backwardChaining() {
        while (!goals.isEmpty()) {

        }
        return true;
    }
}
