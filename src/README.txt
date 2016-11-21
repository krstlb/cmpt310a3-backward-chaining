==========================
== CMPT310 Assignment 3 ==
==========================
Name:       Krystle Bulalakaw
Student ID: 301134933
Date:       November 21, 2016

How to Run
----------
** The executable (BackwardChaining.class) file should already be included in the submission,
   so Step 2 is optional.
   In case there are any issues, the entire project can be re-compiled and executable regenerated.
   Please compile this program with Java 1.8.

1. In command prompt, navigate to folder containing source files.
    cd code

2. Compile all .java files.
    javac *.

3. Run the main class.
    java BackwardChaining <filename>

   Some sample data is included, named "data1.txt", "data2.txt", and "data3.txt".
   The file should contain the data to be evaluated. The first line is expected
   to be the query, while the rest of the file are the rules in the knowledge base
   (read line-by-line).

4. Output is printed to console and can be inspected.

   You can first see that the program derives the rules given in the sample data and prints them.
   It will then display the ultimate goal (first query) to prove, which was the first line in the
   file. As the program evaluates each subgoal, the following diagnostics are printed: the list of
   goals, the current goal being evaluated, whether or not the current goal has been found as a
   conclusion/head within a clause/body in the KB, and goals visited so far.

   Finally, if the result of proving the ultimate goal is successful, it will print TRUE and the
   order of entailments to reach the query. If the result is unsuccessful, it will print FALSE.
