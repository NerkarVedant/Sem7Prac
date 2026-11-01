import java.util.Scanner;

/**
 * DAA5: Binomial coefficients C(n, k) using Dynamic Programming.
 */
public class DAA5_BinomialCoefficients {

    // Returns the full DP table; C[i][j] = C(i, j)
    static int[][] binomialTable(int n, int k) {
        int[][] C = new int[n + 1][k + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= Math.min(i, k); j++) {
                if (j == 0 || j == i) {
                    C[i][j] = 1;                          // base cases
                } else {
                    C[i][j] = C[i - 1][j - 1] + C[i - 1][j];   // Pascal's rule
                }
            }
        }
        return C;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter n: ");
        int n = sc.nextInt();
        System.out.print("Enter k: ");
        int k = sc.nextInt();

        if (k > n || k < 0) {
            System.out.println("Invalid input: k must satisfy 0 <= k <= n");
            return;
        }

        int[][] C = binomialTable(n, k);

        System.out.println("\nBinomial coefficient table (rows i = 0.." + n + ", columns j = 0.." + k + "):");
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= Math.min(i, k); j++) {
                System.out.printf("%6d", C[i][j]);
            }
            System.out.println();
        }

        System.out.println("\nC(" + n + ", " + k + ") = " + C[n][k]);

        // Full Pascal's triangle for n
        System.out.println("\nPascal's triangle up to row " + n + ":");
        int[][] P = binomialTable(n, n);
        for (int i = 0; i <= n; i++) {
            for (int s = 0; s < (n - i) * 2; s++) System.out.print(" ");
            for (int j = 0; j <= i; j++) System.out.printf("%4d", P[i][j]);
            System.out.println();
        }
    }
}

/*
 * ===================== THEORY =====================
 *
 * Problem:
 *   Compute the binomial coefficient C(n, k) = n! / (k! * (n-k)!), the number
 *   of ways to choose k items from n items. These are the entries of Pascal's
 *   triangle and the coefficients in the expansion of (a + b)^n.
 *
 * Technique: Dynamic Programming
 *   The direct recursive formula
 *       C(n, k) = C(n-1, k-1) + C(n-1, k)
 *   recomputes the same subproblems many times (overlapping subproblems), which
 *   makes plain recursion exponential. DP computes each C(i, j) exactly once
 *   and stores it in a table, building the answer bottom-up.
 *
 * Recurrence (Pascal's rule):
 *   C(i, 0) = 1
 *   C(i, i) = 1
 *   C(i, j) = C(i-1, j-1) + C(i-1, j)   for 0 < j < i
 *
 * Algorithm / Steps:
 *   1. Create a table C of size (n+1) x (k+1).
 *   2. For each row i = 0..n and each column j = 0..min(i, k):
 *        - if j == 0 or j == i, set C[i][j] = 1
 *        - else set C[i][j] = C[i-1][j-1] + C[i-1][j]
 *   3. The answer is C[n][k].
 *
 * Example:
 *   C(5, 2): table rows
 *     i=0: 1
 *     i=1: 1 1
 *     i=2: 1 2 1
 *     i=3: 1 3 3 1
 *     i=4: 1 4 6 4 1
 *     i=5: 1 5 10 10 5 1
 *   C(5, 2) = 10
 *
 * Complexity:
 *   Time  : O(n * k)
 *   Space : O(n * k)   (reducible to O(k) by keeping only one row)
 *
 * Note:
 *   Using ints overflows for large n (about n > 33 for central coefficients).
 *   Use long or BigInteger for larger inputs.
 *
 * Conclusion:
 *   Dynamic programming computes binomial coefficients in O(nk), compared to
 *   exponential time for naive recursion.
 */

/*
 * ===================== EXPLANATION (how to explain to the teacher) =====================
 *
 * 1. What the program does, in plain words
 *    C(n, k) means "n choose k": the number of ways to pick k things out of n.
 *    The program builds Pascal's triangle row by row using addition only, and
 *    reads C(n, k) from the table. It does not use factorials, so there is no
 *    huge intermediate number.
 *
 * 2. The key idea (Pascal's rule)
 *    Pick one special item out of the n items. Either:
 *      - it IS chosen, so we pick k-1 more from the remaining n-1 -> C(n-1, k-1)
 *      - it is NOT chosen, so we pick all k from the remaining n-1 -> C(n-1, k)
 *    Therefore C(n, k) = C(n-1, k-1) + C(n-1, k).
 *    Base cases: C(i, 0) = 1 (one way to choose nothing) and C(i, i) = 1 (one
 *    way to choose everything).
 *
 * 3. How the code works
 *    - binomialTable(n, k) creates an (n+1) x (k+1) table C.
 *    - The outer loop goes over rows i = 0..n. The inner loop goes over columns
 *      j = 0..min(i, k). We never need j > i (it would be 0) and never need
 *      j > k.
 *    - If j == 0 or j == i, store 1. Otherwise store the sum of the cell
 *      diagonally up-left, C[i-1][j-1], and the cell directly above, C[i-1][j].
 *    - main() prints the table, then C(n, k), then the complete Pascal's
 *      triangle for the same n for a nicer picture.
 *
 * 4. Dry run: C(5, 2)
 *      i=0 :  1
 *      i=1 :  1  1
 *      i=2 :  1  2  1          (2 = 1 + 1)
 *      i=3 :  1  3  3          (3 = 1 + 2,  3 = 2 + 1)
 *      i=4 :  1  4  6          (4 = 1 + 3,  6 = 3 + 3)
 *      i=5 :  1  5  10         (5 = 1 + 4, 10 = 4 + 6)
 *    So C(5, 2) = 10. (Check with formula: 5! / (2! * 3!) = 120 / 12 = 10.)
 *
 * 5. Why dynamic programming?
 *    A plain recursive version recomputes the same C(i, j) values again and
 *    again, taking exponential time. The table stores each value once, so we
 *    do only n * k additions.
 *
 * 6. What to say in one minute
 *    "Binomial coefficients satisfy Pascal's rule, C(n,k) = C(n-1,k-1) +
 *    C(n-1,k), with C(n,0) = C(n,n) = 1. This has overlapping subproblems, so
 *    I use dynamic programming: I fill a table bottom-up and each cell is the
 *    sum of two earlier cells. The time is O(n*k) and the space is O(n*k),
 *    which can be reduced to O(k) by keeping only one row."
 *
 * 7. Likely viva questions
 *    Q: What is the time complexity?
 *    A: O(n * k).
 *    Q: Where are binomial coefficients used?
 *    A: Binomial theorem (a + b)^n, combinatorics, probability.
 *    Q: What is C(n, k) in terms of C(n, n-k)?
 *    A: They are equal (the triangle is symmetric).
 *    Q: Sum of row n of Pascal's triangle?
 *    A: 2^n.
 *    Q: Why can the value overflow?
 *    A: int holds up to about 2.1 billion. Use long or BigInteger for bigger n.
 */
