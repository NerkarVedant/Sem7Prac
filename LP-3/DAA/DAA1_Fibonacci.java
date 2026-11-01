import java.util.Scanner;

/**
 * DAA1: Fibonacci numbers with step count (iterative and recursive).
 */
public class DAA1_Fibonacci {

    static long steps; // global step counter

    // Iterative Fibonacci: counts every executed basic statement
    static long fibIterative(int n) {
        steps = 0;
        steps++;                       // if (n <= 1) comparison
        if (n <= 1) {
            steps++;                   // return n
            return n;
        }
        long a = 0, b = 1, c = 0;
        steps += 2;                    // a = 0, b = 1
        steps++;                       // i = 2 (loop initialisation)
        for (int i = 2; i <= n; i++) {
            steps++;                   // loop condition (true)
            c = a + b; steps++;
            a = b;     steps++;
            b = c;     steps++;
            steps++;                   // i++
        }
        steps++;                       // final loop condition (false)
        steps++;                       // return b
        return b;
    }

    // Recursive Fibonacci: every function call counts as one step
    static long fibRecursive(int n) {
        steps++;                       // one step per call
        if (n <= 1) return n;
        return fibRecursive(n - 1) + fibRecursive(n - 2);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter n: ");
        int n = sc.nextInt();

        System.out.println("\n--- Iterative approach ---");
        System.out.print("Series: ");
        for (int i = 0; i < n; i++) System.out.print(fibIterative(i) + " ");
        long result = fibIterative(n);
        System.out.println("\nFib(" + n + ") = " + result);
        System.out.println("Step count (iterative) = " + steps);

        System.out.println("\n--- Recursive approach ---");
        steps = 0;
        long r = fibRecursive(n);
        System.out.println("Fib(" + n + ") = " + r);
        System.out.println("Step count (recursive) = " + steps + "  (number of calls)");

        System.out.println("\nStep count table (n : iterative : recursive)");
        for (int i = 1; i <= Math.min(n, 30); i++) {
            fibIterative(i);
            long it = steps;
            steps = 0;
            fibRecursive(i);
            System.out.println(i + " : " + it + " : " + steps);
        }
    }
}

/*
 * ===================== THEORY =====================
 *
 * Problem:
 *   Fibonacci sequence: F(0) = 0, F(1) = 1, F(n) = F(n-1) + F(n-2) for n >= 2.
 *   Series: 0, 1, 1, 2, 3, 5, 8, 13, 21, ...
 *
 * Step count (DAA concept):
 *   Step count is a machine-independent way of measuring running time. We count
 *   the number of basic statements (assignments, comparisons, returns, calls)
 *   executed by the algorithm as a function of input size n. It lets us derive
 *   the time complexity without depending on hardware.
 *
 * Approach 1: Iterative (bottom-up)
 *   Steps:
 *     1. If n <= 1, return n.
 *     2. Initialise a = 0, b = 1.
 *     3. Loop i from 2 to n: c = a + b; a = b; b = c.
 *     4. Return b.
 *   Step count: initialisation = constant, loop body runs (n - 1) times with
 *   5 steps per iteration, so total ~ 5n + constant.
 *   Time complexity : O(n)
 *   Space complexity: O(1)
 *
 * Approach 2: Recursive (top-down)
 *   Recurrence: T(n) = T(n-1) + T(n-2) + 1, with T(0) = T(1) = 1.
 *   Number of calls = 2*F(n+1) - 1, which grows like the golden ratio power
 *   phi^n (phi ~ 1.618), hence exponential.
 *   Time complexity : O(2^n) (tight bound Theta(phi^n))
 *   Space complexity: O(n) (recursion stack depth)
 *   Reason for inefficiency: overlapping subproblems. fib(n-2) is computed
 *   again inside fib(n-1), and so on. Dynamic programming / memoization
 *   removes this repetition and brings the time down to O(n).
 *
 * Comparison:
 *   n = 5 : recursive calls = 15,  n = 10 : 177,  n = 20 : 21891
 *   Iterative grows linearly. Recursive grows exponentially.
 *
 * Conclusion:
 *   Iterative Fibonacci is O(n) time and O(1) space. Naive recursive Fibonacci
 *   is O(2^n) time. Step count analysis makes this difference visible.
 */

/*
 * ===================== EXPLANATION (how to explain to the teacher) =====================
 *
 * 1. What the program does, in plain words
 *    The user enters n. The program calculates the n-th Fibonacci number in two
 *    ways (iterative and recursive) and, for each way, also counts how many
 *    steps were executed. The aim is to show that two programs giving the SAME
 *    answer can need very different amounts of work.
 *
 * 2. How the code works
 *    - "static long steps" is a global counter. Every time a basic statement
 *      executes, we do steps++. At the end, steps is the step count.
 *    - fibIterative(n): keeps only the last two numbers (a and b). In each loop
 *      pass it computes c = a + b, then shifts: a = b, b = c. After the loop,
 *      b holds Fib(n).
 *    - fibRecursive(n): a direct copy of the definition. It calls itself for
 *      n-1 and n-2. steps++ runs once on every call, so steps = number of calls.
 *    - main(): reads n, prints the series, both answers and both step counts,
 *      then prints a table of step counts for n = 1, 2, 3, ... to show the
 *      growth.
 *
 * 3. Dry run (iterative, n = 5)
 *      start      : a = 0, b = 1
 *      i = 2      : c = 1, a = 1, b = 1
 *      i = 3      : c = 2, a = 1, b = 2
 *      i = 4      : c = 3, a = 2, b = 3
 *      i = 5      : c = 5, a = 3, b = 5
 *      return b = 5
 *    Step count formula for n >= 2: 5n + 1. For n = 5 this is 26, which matches
 *    the program output.
 *
 * 4. Dry run (recursive, n = 4): the call tree
 *                        fib(4)
 *                      /        \
 *                 fib(3)        fib(2)
 *                 /    \        /    \
 *            fib(2)  fib(1)  fib(1)  fib(0)
 *            /    \
 *        fib(1)  fib(0)
 *    Total calls = 9. Notice fib(2) is calculated twice and fib(1) three times.
 *    This repeated work is the reason recursion is slow.
 *
 * 5. Why the step counts differ
 *    Iterative steps grow by a fixed 5 for each increase of n (linear, O(n)).
 *    Recursive calls roughly multiply by 1.6 for each increase of n
 *    (exponential, O(2^n)). From the table: n = 5 -> 15 calls, n = 6 -> 25 calls.
 *
 * 6. What to say in one minute
 *    "Sir/Madam, step count measures the number of basic operations executed,
 *    independent of the machine. The iterative version takes about 5n + 1
 *    steps, so it is O(n). The recursive version makes about 2^n calls because
 *    it recomputes the same subproblems, so it is O(2^n). If we store results
 *    (memoization / dynamic programming) the recursive version also becomes
 *    O(n)."
 *
 * 7. Likely viva questions
 *    Q: What is step count?
 *    A: Number of basic operations executed as a function of input size.
 *    Q: Why is recursive Fibonacci slow?
 *    A: Overlapping subproblems; the same values are recomputed many times.
 *    Q: How can it be improved?
 *    A: Memoization or bottom-up DP (O(n) time), or matrix exponentiation
 *       (O(log n) time).
 *    Q: Space complexity of each?
 *    A: Iterative O(1). Recursive O(n) because of the call stack depth.
 */
