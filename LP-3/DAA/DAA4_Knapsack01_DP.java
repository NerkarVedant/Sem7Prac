import java.util.Scanner;

/**
 * DAA4: 0-1 Knapsack using Dynamic Programming (bottom-up tabulation).
 */
public class DAA4_Knapsack01_DP {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of items: ");
        int n = sc.nextInt();

        int[] wt = new int[n + 1];
        int[] val = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            System.out.print("Enter weight and value of item " + i + ": ");
            wt[i] = sc.nextInt();
            val[i] = sc.nextInt();
        }
        System.out.print("Enter knapsack capacity: ");
        int W = sc.nextInt();

        // dp[i][w] = max value using first i items with capacity w
        int[][] dp = new int[n + 1][W + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                if (wt[i] <= w) {
                    // max of (exclude item i, include item i)
                    dp[i][w] = Math.max(dp[i - 1][w], val[i] + dp[i - 1][w - wt[i]]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Print DP table
        System.out.println("\nDP table:");
        for (int i = 0; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                System.out.printf("%4d", dp[i][w]);
            }
            System.out.println();
        }

        System.out.println("\nMaximum value = " + dp[n][W]);

        // Trace back to find the selected items
        System.out.print("Selected items: ");
        int w = W;
        for (int i = n; i >= 1; i--) {
            if (dp[i][w] != dp[i - 1][w]) {   // item i was included
                System.out.print(i + " ");
                w -= wt[i];
            }
        }
        System.out.println();
    }
}

/*
 * ===================== THEORY =====================
 *
 * Problem:
 *   Given n items with weights w(i) and values v(i), and a knapsack of capacity
 *   W, select a subset of items to maximise total value so that total weight
 *   <= W. Each item is either taken completely (1) or not taken (0). Items
 *   cannot be split.
 *
 * Technique: Dynamic Programming
 *   DP solves a problem by breaking it into overlapping subproblems, solving
 *   each once and storing the results in a table. It applies when the problem
 *   has:
 *     1. Optimal substructure
 *     2. Overlapping subproblems
 *   Greedy fails for 0-1 knapsack (ratio-based choice can be suboptimal), so DP
 *   is used.
 *
 * Recurrence:
 *   Let dp[i][w] = best value using the first i items with capacity w.
 *     dp[0][w] = 0 and dp[i][0] = 0                (base cases)
 *     if wt[i] > w : dp[i][w] = dp[i-1][w]         (item does not fit)
 *     else         : dp[i][w] = max( dp[i-1][w],                (exclude)
 *                                    val[i] + dp[i-1][w - wt[i]] ) (include)
 *   Answer = dp[n][W].
 *
 * Algorithm / Steps:
 *   1. Create a table dp of size (n+1) x (W+1) filled with 0.
 *   2. For i = 1..n and w = 0..W, fill the table with the recurrence.
 *   3. dp[n][W] is the maximum value.
 *   4. Trace back from dp[n][W]: if dp[i][w] != dp[i-1][w], item i was taken;
 *      reduce w by wt[i]. Repeat for i = n down to 1.
 *
 * Example:
 *   Items (w, v): (1,1), (3,4), (4,5), (5,7), W = 7
 *   Best choice: items with weights 3 and 4 give value 9. Maximum value = 9.
 *
 * Complexity:
 *   Time  : O(n * W)    (pseudo-polynomial, since it depends on the numeric
 *                        value of W, not just input length)
 *   Space : O(n * W)    (can be reduced to O(W) using a 1-D array when the
 *                        selected items need not be traced)
 *
 * Note (Branch and Bound alternative):
 *   Branch and Bound explores a state-space tree, computes an upper bound at
 *   each node (using the fractional knapsack value) and prunes nodes whose
 *   bound is not better than the best solution found so far.
 *
 * Conclusion:
 *   Dynamic programming gives the optimal 0-1 knapsack solution in O(nW) time.
 */

/*
 * ===================== EXPLANATION (how to explain to the teacher) =====================
 *
 * 1. What the program does, in plain words
 *    Same bag-and-items problem as before, but now each item must be taken
 *    completely or left behind (0-1: either 0 or 1 copy). We build a table that
 *    stores the best possible value for every smaller version of the problem,
 *    and use those stored answers to solve the bigger problem.
 *
 * 2. What the table means
 *    dp[i][w] = the maximum value we can get using only the first i items with a
 *    bag of capacity w.
 *      - Row 0 (no items) is all zeros.
 *      - Column 0 (capacity 0) is all zeros.
 *      - The final answer is the bottom-right cell dp[n][W].
 *
 * 3. How the code works
 *    - Reads n, then weight and value of each item (arrays are 1-indexed so
 *      item i sits at index i), then capacity W.
 *    - Two nested loops fill the table row by row. For each cell (i, w):
 *        * If item i is heavier than w, it cannot be taken, so copy the value
 *          from the row above: dp[i-1][w].
 *        * Otherwise choose the better of two options:
 *            EXCLUDE item i : dp[i-1][w]
 *            INCLUDE item i : val[i] + dp[i-1][w - wt[i]]
 *          (its value, plus the best we can do with the leftover capacity using
 *          earlier items).
 *    - The table is printed, then dp[n][W] is the maximum value.
 *    - Trace back to find WHICH items were chosen: start at the last cell. If
 *      dp[i][w] differs from dp[i-1][w], item i must have been taken, so print
 *      it and reduce w by its weight. If equal, item i was not needed. Move up
 *      one row and repeat.
 *
 * 4. Dry run
 *    Items (weight, value): 1:(1,1)  2:(3,4)  3:(4,5)  4:(5,7), capacity W = 7
 *
 *    Table (columns are capacity 0..7):
 *            w: 0  1  2  3  4  5  6  7
 *      i=0      0  0  0  0  0  0  0  0
 *      i=1      0  1  1  1  1  1  1  1
 *      i=2      0  1  1  4  5  5  5  5
 *      i=3      0  1  1  4  5  6  6  9
 *      i=4      0  1  1  4  5  7  8  9
 *
 *    A few cells worked out:
 *      dp[2][4]: item 2 (w=3, v=4) fits. max( dp[1][4] = 1, 4 + dp[1][1] = 5 )
 *                = 5
 *      dp[3][7]: item 3 (w=4, v=5) fits. max( dp[2][7] = 5, 5 + dp[2][3] = 9 )
 *                = 9
 *      dp[4][7]: item 4 (w=5, v=7) fits. max( dp[3][7] = 9, 7 + dp[3][2] = 8 )
 *                = 9
 *
 *    Trace back from dp[4][7] = 9, w = 7:
 *      i=4: dp[4][7] = dp[3][7] -> not taken
 *      i=3: dp[3][7] = 9, dp[2][7] = 5 differ -> take item 3, w = 7 - 4 = 3
 *      i=2: dp[2][3] = 4, dp[1][3] = 1 differ -> take item 2, w = 3 - 3 = 0
 *      i=1: dp[1][0] = dp[0][0] -> not taken
 *    Selected items: 3 and 2. Weight 4 + 3 = 7, value 5 + 4 = 9.
 *
 * 5. Why DP and not greedy?
 *    Items cannot be cut, so the best-ratio-first rule can fail. DP tries both
 *    choices (take / skip) for every item but avoids repeated work by storing
 *    results in the table.
 *
 * 6. What to say in one minute
 *    "This is dynamic programming. dp[i][w] is the best value using the first i
 *    items with capacity w. For each item we either skip it, giving dp[i-1][w],
 *    or take it, giving its value plus dp[i-1][w - weight]. We take the
 *    maximum. We fill the table bottom-up, and dp[n][W] is the answer. The time
 *    and space are both O(nW). Tracing back through the table tells us which
 *    items were selected."
 *
 * 7. Likely viva questions
 *    Q: Why is O(nW) called pseudo-polynomial?
 *    A: It depends on the numeric value of W, not on the number of bits needed
 *       to write W, so it is exponential in the input length.
 *    Q: How to reduce space?
 *    A: Use a single 1-D array and loop w from W down to the item's weight.
 *    Q: What are the two properties needed for DP?
 *    A: Optimal substructure and overlapping subproblems.
 *    Q: Explain branch and bound for this problem.
 *    A: Build a decision tree (take / skip each item), compute an upper bound
 *       at each node using the fractional knapsack value, and prune a branch if
 *       its bound is not better than the best complete solution found so far.
 */
