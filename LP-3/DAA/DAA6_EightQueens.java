import java.util.Scanner;

/**
 * DAA6: 8-Queens with the first queen pre-placed, remaining queens placed
 * using Backtracking.
 */
public class DAA6_EightQueens {

    static final int N = 8;
    static int[][] board = new int[N][N];
    static int fixedRow;     // row of the pre-placed queen

    // Checks whether placing a queen at (row, col) conflicts with any queen
    // already on the board (column and both diagonals).
    static boolean isSafe(int row, int col) {
        for (int r = 0; r < N; r++) {
            if (r == row) continue;
            // same column
            if (board[r][col] == 1) return false;
            // diagonal 1: col - c == row - r
            int d = row - r;
            if (col - d >= 0 && col - d < N && board[r][col - d] == 1) return false;
            // diagonal 2: col + (row - r)
            if (col + d >= 0 && col + d < N && board[r][col + d] == 1) return false;
        }
        return true;
    }

    // Place queens row by row. Skip the row that holds the fixed first queen.
    static boolean solve(int row) {
        if (row == N) return true;                // all queens placed

        if (row == fixedRow) return solve(row + 1);   // already has a queen

        for (int col = 0; col < N; col++) {
            if (isSafe(row, col)) {
                board[row][col] = 1;              // place
                if (solve(row + 1)) return true;  // recurse
                board[row][col] = 0;              // backtrack
            }
        }
        return false;                             // no column works, backtrack
    }

    static void printBoard() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(board[i][j] == 1 ? " Q " : " . ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter row (1-8) of first queen: ");
        int r = sc.nextInt() - 1;
        System.out.print("Enter column (1-8) of first queen: ");
        int c = sc.nextInt() - 1;

        if (r < 0 || r >= N || c < 0 || c >= N) {
            System.out.println("Invalid position.");
            return;
        }

        fixedRow = r;
        board[r][c] = 1;                          // place first queen

        if (solve(0)) {
            System.out.println("\nFinal 8-Queens matrix (first queen at row "
                    + (r + 1) + ", column " + (c + 1) + "):\n");
            printBoard();

            System.out.println("\nMatrix form (1 = queen):");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) System.out.print(board[i][j] + " ");
                System.out.println();
            }
        } else {
            System.out.println("No solution exists with the first queen at that position.");
        }
    }
}

/*
 * ===================== THEORY =====================
 *
 * Problem:
 *   Place 8 queens on an 8 x 8 chessboard so that no two queens attack each
 *   other, i.e. no two share the same row, column or diagonal. Here the first
 *   queen is already placed at a given position, and the remaining 7 queens
 *   must be placed around it.
 *
 * Technique: Backtracking
 *   Backtracking builds the solution incrementally, one component at a time,
 *   and abandons (backtracks from) a partial solution as soon as it cannot
 *   lead to a valid complete solution. It is a depth-first search of the
 *   state-space tree with pruning, which is far better than brute force
 *   (C(64, 8) ~ 4.4 billion placements, or 8^8 ~ 16.7 million with one queen
 *   per row).
 *
 * Constraints:
 *   - Row   : exactly one queen per row (enforced by placing one per row).
 *   - Column: no two queens share a column.
 *   - Diagonal: for queens at (r1, c1) and (r2, c2), they attack diagonally if
 *       |r1 - r2| == |c1 - c2|.
 *
 * Algorithm / Steps:
 *   1. Read the position of the first queen and place it on the board.
 *   2. Start from row 0. If the row is the fixed row, skip to the next row.
 *   3. For each column in the current row, check isSafe(row, col) against
 *      all queens on the board (including the fixed one).
 *   4. If safe, place the queen and recurse to the next row.
 *   5. If the recursive call fails, remove the queen (backtrack) and try the
 *      next column.
 *   6. If no column works in a row, return false so the previous row moves its
 *      queen.
 *   7. When row == 8, all queens are placed. Print the matrix.
 *
 * Example output (first queen at row 1, column 1):
 *   Q . . . . . . .
 *   . . . . Q . . .
 *   . . . . . . . Q
 *   . . . . . Q . .
 *   . . Q . . . . .
 *   . . . . . . Q .
 *   . Q . . . . . .
 *   . . . Q . . . .
 *
 * Complexity:
 *   Worst case time : O(N!) (pruned heavily in practice; each isSafe call is
 *                     O(N) here)
 *   Space           : O(N^2) for the board, O(N) recursion depth
 *
 * Note:
 *   The 8-Queens problem has 92 distinct solutions (12 unique up to rotation
 *   and reflection). Which solution is printed depends on the first queen's
 *   position and the order columns are tried.
 *
 * Conclusion:
 *   Backtracking with a safety check prunes invalid placements early and finds
 *   a valid 8-Queens arrangement containing the given first queen.
 */

/*
 * ===================== EXPLANATION (how to explain to the teacher) =====================
 *
 * 1. What the program does, in plain words
 *    A queen attacks along its row, its column and both diagonals. We must put
 *    8 queens on an 8 x 8 board so none can attack another. The user decides
 *    where queen number 1 goes. The program then finds positions for the other
 *    7 queens by trying, and undoing (backtracking) when stuck.
 *
 * 2. How the code works
 *    - board[8][8]: 1 means a queen, 0 means empty.
 *    - fixedRow: the row where the user placed the first queen. That row
 *      already has its queen, so the algorithm skips it.
 *    - solve(row): places one queen in the given row and then calls itself for
 *      the next row.
 *        * If row == 8, all rows are done, so we have a solution (return true).
 *        * If row == fixedRow, skip to the next row.
 *        * Otherwise try each column 0..7:
 *            - if isSafe(row, col): PLACE the queen, then call solve(row + 1).
 *              If that returns true, we are finished. If it returns false,
 *              REMOVE the queen (board[row][col] = 0) and try the next column.
 *        * If no column works, return false. The caller then moves its own
 *          queen to a different column. This undoing step is the backtracking.
 *    - isSafe(row, col): checks every other row r:
 *        * same column -> board[r][col] == 1
 *        * diagonals   -> with d = row - r, the squares in row r that attack
 *          (row, col) diagonally are column col - d and column col + d.
 *      We scan ALL rows (not only the rows above) because the pre-placed queen
 *      may be below the current row.
 *    - We never check rows because we place exactly one queen per row.
 *
 * 3. Why it is backtracking and not brute force
 *    Brute force would try all ways of putting 8 queens on 64 squares (billions
 *    of ways). Backtracking stops as soon as a partial placement is already
 *    wrong, so whole groups of bad boards are never explored.
 *
 * 4. Start of a trace (first queen at row 1, column 1; 0-indexed (0,0))
 *      Row 0 is the fixed row, so it is skipped.
 *      Row 1: col 0 same column as (0,0) -> unsafe
 *             col 1 diagonal with (0,0)  -> unsafe
 *             col 2 safe                 -> place queen at (1,2)
 *      Row 2: col 0 unsafe (same column as (0,0))
 *             col 1 unsafe (diagonal with (1,2))
 *             col 2 unsafe (same column as (1,2))
 *             col 3 unsafe (diagonal with (1,2))
 *             col 4 safe                 -> place queen at (2,4)
 *      ... the program continues the same way. If at some row no column is
 *      safe, it returns to the previous row, removes that queen and tries its
 *      next column. In this case the search is forced to go back and change
 *      earlier choices, and finally reaches this board:
 *        Q . . . . . . .
 *        . . . . Q . . .
 *        . . . . . . . Q
 *        . . . . . Q . .
 *        . . Q . . . . .
 *        . . . . . . Q .
 *        . Q . . . . . .
 *        . . . Q . . . .
 *    You can verify: no two queens share a row, column or diagonal.
 *
 * 5. What to say in one minute
 *    "I place the first queen where the user says. Then I use backtracking row
 *    by row. For each row I try each column, and I place a queen only if it is
 *    safe from all existing queens, meaning no queen in the same column or on
 *    either diagonal. I then move to the next row. If a row has no safe column,
 *    I go back, remove the previous queen and try its next column. When all 8
 *    rows have a queen, I print the matrix. Worst-case time is O(N!), but
 *    pruning makes it fast in practice."
 *
 * 6. Likely viva questions
 *    Q: What is backtracking?
 *    A: A depth-first search of the solution space that abandons a partial
 *       solution as soon as it violates a constraint.
 *    Q: How many solutions does 8-Queens have?
 *    A: 92 in total (12 distinct ignoring rotations and reflections).
 *    Q: How do you detect a diagonal attack?
 *    A: Two queens (r1, c1) and (r2, c2) attack diagonally if
 *       |r1 - r2| == |c1 - c2|.
 *    Q: Why one queen per row?
 *    A: 8 queens in 8 rows with no row sharing means exactly one per row, which
 *       also shrinks the search space.
 *    Q: Does a solution exist for every first-queen position?
 *    A: Yes for the 8 x 8 board; every square is used in at least one of the 92
 *       solutions.
 */
