import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BackwardChaining {
    private static String query;
    private static ArrayList<String> goals;
    private static ArrayList<String> knowledgeBase;
    private static ArrayList<String> visited;
    private static ArrayList<String> truthTable;

    public static void main(String[] args) {
        String output = "";

        try {
            init(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java BackwardChaining <filename>");
            System.exit(0);
        }

        System.out.println();
        System.out.println("BACKWARD CHAINING DIAGNOSTICS:");
        System.out.println("First query: " + query);

        if (backwardChaining()) {
            // append the entailed symbols in reverse to output
            for (int i = truthTable.size() - 1; i >= 0; i--) {
                String[] sentence = truthTable.get(i).split(" ");
                if (sentence.length == 1) {
                    output += sentence[0] + " is true\n";
                } else if (sentence.length == 2) {
                    output += sentence[1] + " implies " + sentence[0] + "\n";
                } else if (sentence.length >= 3) {
                    output += sentence[1] + " and " + sentence[2] + " implies " + sentence[0] + "\n";
                }
            }
            output += "Therefore " + query + " is TRUE\n";
        } else {
            output += query + " could not be proven in the knowledge base.\n";
            output += "Therefore " + query + " is FALSE\n";
        }

        System.out.println();
        System.out.println("RESULT:");
        System.out.println(output);
    }

    /**
     * Reads the rules file indicated by the user in command prompt, then sets up the initial values for
     * backward chaining. It does this by splitting the file into rules. It will add the rule to a list,
     * knowledgeBase. It also gets the query to start the backward chaining with. If the file cannot be
     * found, the rule file contains invalid * syntax, or the user enters incorrect execution arguments,
     * an error will be printed and the program will exit.
     * @param filename file to read rules from
     */
    private static void init(String filename) {
        goals = new ArrayList<String>();
        knowledgeBase = new ArrayList<String>();
        visited = new ArrayList<String>();
        truthTable = new ArrayList<String>();

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

            System.out.println("RULES:");
            // get rules
            int numLines = 1;
            while (scan.hasNextLine()) {
                numLines++;
                String line = scan.nextLine();
                String[] atoms = line.split(" ");

                if (atoms.length == 1) {
                    System.out.println("fact: " + atoms[0] + " is TRUE");
                } else if (atoms.length == 2) {
                    System.out.println("implication: IF " + atoms[1] + " THEN " + atoms[0]);
                } else if (atoms.length == 3) {
                    System.out.println("conjunction: IF " + atoms[1] + " AND " + atoms[2] + " THEN " + atoms[0]);
                } else {
                    System.out.println("error: Invalid input at line " + numLines + ", please check syntax in rules file");
                    System.exit(0);
                }
                knowledgeBase.add(line);
            }
        } catch (FileNotFoundException e1) {
            System.out.println("error: Unable to find file: " + filename);
            System.exit(0);
        } catch (NoSuchElementException e) {
            System.out.println("error: Missing rules in rules file " + filename);
            System.exit(0);
        }
    }

    /**
     * Works backwards from query q. Tries to prove q by checking if q is known or, proves recursively by BC
     * all the premises of some rule concluding q.
     * It avoid loops by checking if new subgoal is already in the set of goals. It avoids repeating work by
     * checking if the new subgoal has already been proven true, or has already failed.
     * @return
     */
    private static boolean backwardChaining() {
        // the list of subgoals to evaluate
        ArrayList<String> body = new ArrayList<String>();

        // If the list of goals is empty, stop recursion
        if (goals.isEmpty()) {
            return true;
        }

        // Else, use backward chaining algorithm
        System.out.println();
        System.out.println("Goals to evaluate: " + goals);
        // Get the current predicate that is our goal (subgoal)
        String subgoal = goals.remove(goals.size() - 1);

        System.out.println("Evaluating " + subgoal + "...");
        // Add the entailment to keep track of processed subgoals
        visited.add(subgoal);

        // Iterate through each clause in knowledge base and check if its head matches subgoal body
        // For each clause that contains the body as its head
        // add the symbols to the list of predicates to be processed
        for (int i = 0; i < knowledgeBase.size(); i++) {
            String clause = knowledgeBase.get(i);
            if (checkHeadContains(clause, subgoal)) {
                if (!truthTable.contains(clause)) {
                    truthTable.add(clause);
                }

                String[] predicates = clause.split(" ");
                System.out.println("Found " + subgoal + " as conclusion in (" + clause + ")");
                for (int j = 1; j < predicates.length; j++) {
                    if (!body.contains(predicates[j])) {
                        body.add(predicates[j]);
                    }
                }
                System.out.println("Truth table: " + truthTable);
            }

            // There is a body of predicates to evaluate, so we perform recursive backward
            // chaining on it
            if (body.size() != 0) {
                for (int k = 0; k < body.size(); k++) {
                    if (!goals.contains(body.get(k)) &&
                            !truthTable.contains(body.get(k)) &&
                            !visited.contains(body.get(k))) {
                        goals.add(body.get(k));
                    }
                }

                // recursion
                if (backwardChaining()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if body/query appears in the head/conclusion of a given clause.
     * @param c input clause
     * @param body query to check if it is contained in the given head
     * @return true if head contains the body, else false
     */
    private static boolean checkHeadContains(String c, String body) {
        String head = c.split(" ")[0];
        return head.equals(body);
    }
}
