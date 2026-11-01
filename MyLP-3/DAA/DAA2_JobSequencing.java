import java.util.Arrays;
import java.util.Scanner;

/**
 * DAA2: Job Sequencing with Deadlines using the Greedy method.
 */
public class DAA2_JobSequencing {

    static class Job {
        int id, deadline, profit;
        Job(int id, int deadline, int profit) {
            this.id = id;
            this.deadline = deadline;
            this.profit = profit;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of jobs: ");
        int n = sc.nextInt();

        Job[] jobs = new Job[n];
        int maxDeadline = 0;
        for (int i = 0; i < n; i++) {
            System.out.print("Enter deadline and profit for job J" + (i + 1) + ": ");
            int d = sc.nextInt();
            int p = sc.nextInt();
            jobs[i] = new Job(i + 1, d, p);
            maxDeadline = Math.max(maxDeadline, d);
        }

        // Step 1: sort jobs by profit in descending order (greedy choice)
        Arrays.sort(jobs, (a, b) -> b.profit - a.profit);

        // Step 2: slot[t] holds the job scheduled in time slot t (1-indexed)
        int[] slot = new int[maxDeadline + 1];
        Arrays.fill(slot, -1);

        int totalProfit = 0, count = 0;

        // Step 3: for each job, place it in the latest free slot <= its deadline
        for (Job job : jobs) {
            for (int t = Math.min(job.deadline, maxDeadline); t >= 1; t--) {
                if (slot[t] == -1) {
                    slot[t] = job.id;
                    totalProfit += job.profit;
                    count++;
                    break;
                }
            }
        }

        System.out.println("\nSelected job sequence:");
        for (int t = 1; t <= maxDeadline; t++) {
            if (slot[t] != -1) System.out.print("J" + slot[t] + " ");
        }
        System.out.println("\nNumber of jobs scheduled = " + count);
        System.out.println("Maximum total profit     = " + totalProfit);
    }
}

/*
 * ===================== THEORY =====================
 *
 * Problem:
 *   Given n jobs, each with a deadline d(i) and profit p(i). Each job takes one
 *   unit of time and only one job can run at a time. A job earns its profit
 *   only if it finishes on or before its deadline. Find the subset of jobs and
 *   their order that gives maximum total profit.
 *
 * Technique: Greedy method
 *   A greedy algorithm builds the solution step by step, always taking the
 *   locally best choice (here, the job with the highest profit) hoping to reach
 *   the global optimum. It works here because the problem has the greedy-choice
 *   property and optimal substructure (feasible job sets form a matroid).
 *
 * Algorithm / Steps:
 *   1. Sort all jobs in descending order of profit.
 *   2. Find the maximum deadline; create that many empty time slots.
 *   3. For each job in sorted order, look for a free slot from its deadline
 *      going backwards to slot 1 (latest free slot, so earlier slots stay open
 *      for other jobs).
 *   4. If a free slot is found, assign the job there and add its profit.
 *      Otherwise, discard the job.
 *   5. The filled slots, read left to right, give the schedule.
 *
 * Example:
 *   Jobs (deadline, profit): J1(2,100), J2(1,19), J3(2,27), J4(1,25), J5(3,15)
 *   Sorted by profit: J1(100), J3(27), J4(25), J2(19), J5(15)
 *   J1 -> slot 2, J3 -> slot 1, J4 -> no free slot (rejected),
 *   J2 -> rejected, J5 -> slot 3.
 *   Sequence: J3, J1, J5   Profit = 27 + 100 + 15 = 142
 *
 * Complexity:
 *   Sorting         : O(n log n)
 *   Slot assignment : O(n * d), where d = max deadline, so O(n^2) in worst case
 *   Overall time    : O(n^2)   (can be reduced to near O(n log n) with
 *                               Disjoint Set Union)
 *   Space           : O(d)
 *
 * Conclusion:
 *   Sorting by profit and filling the latest free slot gives an optimal
 *   solution for job sequencing with deadlines.
 */

/*
 * ===================== EXPLANATION (how to explain to the teacher) =====================
 *
 * 1. What the program does, in plain words
 *    There are n jobs. Each job takes 1 unit of time, has a deadline, and gives
 *    a profit only if finished on or before the deadline. We can run only one
 *    job at a time, so we cannot do all jobs. The program chooses which jobs to
 *    do and in which time slot, so that the total profit is maximum.
 *
 * 2. How the code works
 *    - Job class: stores id, deadline and profit of one job.
 *    - Input: reads all jobs and also finds maxDeadline (the number of time
 *      slots we can possibly use).
 *    - Arrays.sort(... b.profit - a.profit): sorts jobs from highest profit to
 *      lowest. This is the greedy choice: consider the most valuable job first.
 *    - slot[] array: slot[t] tells which job is placed at time t. -1 means the
 *      slot is empty.
 *    - For each job, the inner loop starts at the job's deadline and moves
 *      backwards (t--) looking for an empty slot. The first empty slot found is
 *      used. If no slot is empty up to time 1, the job is rejected.
 *    - Finally it prints the filled slots from left to right and the total
 *      profit.
 *
 * 3. Why place a job in the LATEST free slot?
 *    A job with deadline 3 can run at time 1, 2 or 3. If we put it at time 1 we
 *    block a slot that a job with a tight deadline (say deadline 1) might need.
 *    Using the latest possible slot keeps early slots free for other jobs.
 *
 * 4. Dry run
 *    Input (id: deadline, profit): J1: 2, 100   J2: 1, 19   J3: 2, 27
 *                                  J4: 1, 25    J5: 3, 15
 *    maxDeadline = 3, so slots 1, 2, 3.
 *    After sorting by profit: J1(100), J3(27), J4(25), J2(19), J5(15)
 *
 *      J1 (deadline 2): slot 2 is free         -> slot[2] = J1
 *      J3 (deadline 2): slot 2 taken, slot 1 free -> slot[1] = J3
 *      J4 (deadline 1): slot 1 taken, nothing earlier -> rejected
 *      J2 (deadline 1): slot 1 taken           -> rejected
 *      J5 (deadline 3): slot 3 is free         -> slot[3] = J5
 *
 *    Slot table:   time 1 | time 2 | time 3
 *                    J3   |   J1   |   J5
 *    Total profit = 27 + 100 + 15 = 142.
 *
 * 5. What to say in one minute
 *    "This is a greedy algorithm. We sort jobs by profit in decreasing order.
 *    For each job we try to schedule it in the latest free slot on or before its
 *    deadline. If no slot is free, we skip the job. Since we always consider the
 *    highest-profit job first and never remove a scheduled job, the final
 *    profit is maximum. Sorting takes O(n log n) and the slot search takes up
 *    to O(n) per job, so the total is O(n^2)."
 *
 * 6. Likely viva questions
 *    Q: Why is greedy correct here?
 *    A: Feasible sets of jobs form a matroid, so picking by highest profit
 *       gives an optimal set.
 *    Q: What if two jobs have the same profit?
 *    A: Any order works; the total profit stays the same.
 *    Q: How to make it faster?
 *    A: Use Disjoint Set Union to find the free slot, giving about O(n log n).
 *    Q: What if jobs take different durations?
 *    A: That is a different problem; this algorithm assumes unit time jobs.
 */
