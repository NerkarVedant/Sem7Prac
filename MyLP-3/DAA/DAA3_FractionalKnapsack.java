import java.util.Arrays;
import java.util.Scanner;

/**
 * DAA3: Fractional Knapsack using the Greedy method.
 */
public class DAA3_FractionalKnapsack {

    static class Item {
        int id;
        double weight, value, ratio;
        Item(int id, double weight, double value) {
            this.id = id;
            this.weight = weight;
            this.value = value;
            this.ratio = value / weight;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of items: ");
        int n = sc.nextInt();

        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Enter weight and value of item " + (i + 1) + ": ");
            double w = sc.nextDouble();
            double v = sc.nextDouble();
            items[i] = new Item(i + 1, w, v);
        }
        System.out.print("Enter knapsack capacity: ");
        double capacity = sc.nextDouble();

        // Step 1: sort by value/weight ratio in descending order (greedy choice)
        Arrays.sort(items, (a, b) -> Double.compare(b.ratio, a.ratio));

        double remaining = capacity;
        double totalValue = 0;

        System.out.println("\nItem\tWeight\tValue\tRatio\tFraction taken");
        // Step 2: take items greedily
        for (Item it : items) {
            if (remaining <= 0) break;
            double fraction;
            if (it.weight <= remaining) {
                fraction = 1.0;                       // take the whole item
                remaining -= it.weight;
                totalValue += it.value;
            } else {
                fraction = remaining / it.weight;     // take only a fraction
                totalValue += it.value * fraction;
                remaining = 0;
            }
            System.out.printf("%d\t%.2f\t%.2f\t%.2f\t%.2f%n",
                    it.id, it.weight, it.value, it.ratio, fraction);
        }
        System.out.printf("%nMaximum value in knapsack = %.2f%n", totalValue);
    }
}

/*
 * ===================== THEORY =====================
 *
 * Problem:
 *   Given n items with weights w(i) and values v(i), and a knapsack of capacity
 *   W, choose items (or fractions of items) to maximise total value without
 *   exceeding W. In the fractional version an item can be broken, so x(i) is
 *   in [0, 1].
 *   Maximise  sum( v(i) * x(i) )  subject to  sum( w(i) * x(i) ) <= W.
 *
 * Technique: Greedy method
 *   The greedy choice is the item with the highest value-per-weight ratio
 *   (v/w). Taking the best ratio first always leaves the most room for further
 *   valuable items. This is provably optimal for the fractional version
 *   (greedy-choice property + optimal substructure).
 *   It does NOT work for 0-1 Knapsack, where items cannot be split.
 *
 * Algorithm / Steps:
 *   1. Compute ratio = value / weight for every item.
 *   2. Sort items in descending order of ratio.
 *   3. Initialise remaining capacity = W, total value = 0.
 *   4. For each item in sorted order:
 *        a. If item weight <= remaining capacity, take it whole, subtract its
 *           weight, add its value.
 *        b. Otherwise take the fraction (remaining / weight), add
 *           value * fraction, set remaining = 0 and stop.
 *   5. Output total value.
 *
 * Example:
 *   Items (w, v): A(10,60), B(20,100), C(30,120), W = 50
 *   Ratios: A = 6, B = 5, C = 4
 *   Take A (10) -> remaining 40, value 60
 *   Take B (20) -> remaining 20, value 160
 *   Take 20/30 of C -> value 160 + 80 = 240
 *   Maximum value = 240
 *
 * Complexity:
 *   Sorting      : O(n log n)
 *   Selection    : O(n)
 *   Overall time : O(n log n)
 *   Space        : O(1) extra (O(n) for the item array)
 *
 * Conclusion:
 *   Sorting items by value/weight ratio and filling greedily gives the optimal
 *   solution of the fractional knapsack problem in O(n log n).
 */

/*
 * ===================== EXPLANATION (how to explain to the teacher) =====================
 *
 * 1. What the program does, in plain words
 *    We have a bag (knapsack) with limited capacity and several items, each with
 *    a weight and a value. We want the highest total value in the bag. In the
 *    FRACTIONAL version we are allowed to cut an item and take part of it (like
 *    taking half a bag of gold dust).
 *
 * 2. How the code works
 *    - Item class: stores weight, value and ratio = value / weight. The ratio
 *      tells how much value we get per unit of weight.
 *    - Arrays.sort(... compare(b.ratio, a.ratio)): sorts items by ratio from
 *      highest to lowest. This is the greedy choice.
 *    - "remaining" tracks the free capacity of the bag.
 *    - Loop over sorted items:
 *        * If the whole item fits (weight <= remaining), take all of it,
 *          fraction = 1, reduce remaining, add its full value.
 *        * Else take only a fraction = remaining / weight, add value * fraction,
 *          set remaining = 0. The bag is now full, so we stop.
 *    - Prints a table with the fraction taken of each item and the maximum
 *      value.
 *
 * 3. Why sort by value/weight?
 *    Every unit of capacity is precious. The item with the best value per unit
 *    weight gives the most return for that space, so it must be taken first.
 *
 * 4. Dry run
 *    Items (weight, value): A(10, 60)  B(20, 100)  C(30, 120), capacity = 50
 *    Ratios: A = 60/10 = 6   B = 100/20 = 5   C = 120/30 = 4
 *    Sorted order: A, B, C
 *
 *      Take A fully : remaining = 50 - 10 = 40, value = 60
 *      Take B fully : remaining = 40 - 20 = 20, value = 60 + 100 = 160
 *      C weighs 30 but only 20 is left, so take fraction 20/30 = 0.67
 *                     value = 160 + 120 * (20/30) = 160 + 80 = 240
 *    Bag is full. Maximum value = 240.
 *
 * 5. Why greedy does NOT work for 0-1 knapsack (good viva point)
 *    Same items, capacity 50, but no cutting allowed. Greedy by ratio takes
 *    A and B = value 160 (weight 30, and C does not fit). The true best is
 *    B + C = weight 50, value 220. So for 0-1 knapsack we need dynamic
 *    programming (that is DAA4).
 *
 * 6. What to say in one minute
 *    "This is a greedy algorithm. We compute value per weight for each item,
 *    sort in decreasing order, and keep adding whole items while they fit. For
 *    the first item that does not fit, we add the fraction that fills the
 *    remaining capacity. This is optimal because the best-ratio item always
 *    gives the most value per unit capacity. Time complexity is O(n log n)
 *    because of sorting."
 *
 * 7. Likely viva questions
 *    Q: Difference between fractional and 0-1 knapsack?
 *    A: Fractional allows taking part of an item (greedy works). 0-1 does not
 *       (needs DP or branch and bound).
 *    Q: Why O(n log n)?
 *    A: Sorting dominates; the selection loop is O(n).
 *    Q: Can the answer be larger than the 0-1 answer?
 *    A: Yes, it is always >= the 0-1 answer, since it has more freedom.
 *    Q: At most how many items are taken partially?
 *    A: At most one.
 */
