import java.io.*;
import java.util.*;

public class Day9 {
    private static List<List<Integer>> historyRows = new ArrayList<>();

    private static int partOneRecursion(List<Integer> currRow) {
        // recursion: new item of curr row = last item of curr row + last item of next row (differences)
        List<Integer> nextRow = new ArrayList<>();
        for (int i = 1; i < currRow.size(); i++) {
            nextRow.add(currRow.get(i) - currRow.get(i - 1));
        }
        // base case: if all items are 0, return last item
        int lastItem = currRow.get(currRow.size() - 1);
        int zeroCount = 0;
        for (int item : nextRow) {
            if (item == 0) {
                zeroCount++;
            }
        }
        if (zeroCount == nextRow.size()) {
            return lastItem;
        }
        return lastItem + partOneRecursion(nextRow);
    }

    private static int partTwoRecursion(List<Integer> currRow) {
        // recursion: new item of curr row from the front = first item of curr row - first item of next row (differences)
        List<Integer> nextRow = new ArrayList<>();
        for (int i = 1; i < currRow.size(); i++) {
            nextRow.add(currRow.get(i) - currRow.get(i - 1));
        }
        // base case: if all items are 0, return first item
        int firstItem = currRow.get(0);
        int zeroCount = 0;
        for (int item : nextRow) {
            if (item == 0) {
                zeroCount++;
            }
        }
        if (zeroCount == nextRow.size()) {
            return firstItem; // difference for part two
        }
        return firstItem - partTwoRecursion(nextRow); // difference for part two
    }

    private static int calculatePartOne() {
        int ans = 0;
        for (List<Integer> historyRow : historyRows) {
            ans += partOneRecursion(historyRow);
        }
        return ans;
    }

    private static int calculatePartTwo() {
        int ans = 0;
        for (List<Integer> historyRow : historyRows) {
            ans += partTwoRecursion(historyRow);
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day9.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine;
        while ((currLine = reader.readLine()) != null) {
            List<Integer> historyRow = new ArrayList<>();
            for (String item : currLine.split("\\s+")) {
                historyRow.add(Integer.parseInt(item));
            }
            historyRows.add(historyRow);
        }
        System.out.printf("Answer for part one: %d%n", calculatePartOne());
        System.out.printf("Answer for part two: %d%n", calculatePartTwo());

        reader.close();
    }
}
