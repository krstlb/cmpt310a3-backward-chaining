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
        String output = "";

        init(args[0]);

        System.out.println();
        System.out.println("First query: " + query);
        System.out.println();

        System.out.println("Starting backward chaining...");

        if (backwardChaining()) {
            output +=  query + " is TRUE\n";
            output +=  "order of entailment:\n";
            // append the entailed symbols in reverse to output
            for (int i = entailed.size() - 1; i >= 0; i--) {
                output += entailed.get(i);
                if (i != 0) {
                    output += ", ";
                }
            }
        } else {
            output += query + " is FALSE\n";
        }

        System.out.println(output);
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

            System.out.println("Reading input...");
            System.out.println();
            System.out.println("RULES:");
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

    /**
     * Works backwards from query q. Tries to prove q by checking if q is known or, proves recursively by BC
     * all the premises of some rule concluding q.
     * It avoid loops by checking if new suboal is already in the set of goals. It avoids repeating work by
     * checking if the new subgoal has already been proven true, or has already failed.
     * @return
     */
    private static boolean backwardChaining() {
        while (!goals.isEmpty()) {
            System.out.println();
            System.out.println("Goals to evaluate: " + goals);
            // Get the current predicate that is our goal
            String q = goals.remove(goals.size() - 1);

            System.out.println("Evaluating " + q + "...");
            // Add the entailment to keep track of processed predicates
            entailed.add(q);

            // If this predicate is not already a proven fact, we process it
            if (!facts.contains(q)) {
                ArrayList<String> predicatesToBeProcessed = new ArrayList<String>();
                // For each clause that contains the predicate as its conclusion
                // add the symbols to the list of predicates to be processed
                for (int i = 0; i < clauses.size(); i++) {
                    String c = clauses.get(i);
                    if (checkConclusionContains(c, q)) {
                        String[] predicates = c.split(" ");
                        System.out.println("Found " + q + " as conclusion in (" + c + ")");
                        for (int j = 1; j < predicates.length; j++) {
                            if (!predicatesToBeProcessed.contains(predicates[j])) {
                                predicatesToBeProcessed.add(predicates[j]);
                            }
                        }
                    }
                }

                // Since no predicates were found to process, and the query is not a fact,
                // we cannot prove this query
                if (predicatesToBeProcessed.size() == 0) {
                    return false;
                }
                // There are predicates to process. If they are not already entailed,
                // add them to the list of goals
                else {
                    for (int i = 0; i < predicatesToBeProcessed.size(); i++) {
                        if (!entailed.contains(predicatesToBeProcessed.get(i)) &&
                                !goals.contains(predicatesToBeProcessed.get(i))) {
                            goals.add(predicatesToBeProcessed.get(i));
                        }
                    }
                }
            } else {
                System.out.println(q + " evaluated to be TRUE");
            }
            System.out.println("Visited: " + entailed);
        }
        return true;
    }

    /**
     * Checks if query appears in the conclusion of a given clause.
     * @param c input clause
     * @param q query to check if it is contained in the given conclusion
     * @return true if conclusion contains the query, else false
     */
    private static boolean checkConclusionContains(String c, String q) {
        String conclusion = c.split(" ")[0];
        return conclusion.equals(q);
    }


}
