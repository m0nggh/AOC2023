import java.io.*;
import java.util.*;

public class Day11 {
    private static List<List<Character>> matrix = new ArrayList<>();
    private static int rows;
    private static int cols;
    private static List<int[]> galaxies = new ArrayList<>();
    private static List<Integer> emptyRows = new ArrayList<>(); // will be sorted
    private static List<Integer> emptyCols = new ArrayList<>(); // will be sorted

    private static int binarySearch(int target, List<Integer> list) {
        // find min k such that list.get(k) > target: this gives the number of elements strictly smaller than the target
        int l = 0;
        int r = list.size();
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (list.get(mid) > target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private static long calculatePartOne(int emptyMultiplier) {
        Set<Integer> markedRows = new HashSet<>();
        Set<Integer> markedCols = new HashSet<>();
        for (int i = 0; i < rows; i++) {
            List<Character> row = matrix.get(i);
            for (int j = 0; j < cols; j++) {
                char ch = row.get(j);
                if (ch == '#') {
                    markedRows.add(i);
                    markedCols.add(j);
                    galaxies.add(new int[]{ i, j });
                }
            }
        }

        // evaluate empty rows and cols based on marked rows and cols
        for (int i = 0; i < rows; i++) {
            if (!markedRows.contains(i)) {
                emptyRows.add(i);
            }
        }
        for (int i = 0; i < cols; i++) {
            if (!markedCols.contains(i)) {
                emptyCols.add(i);
            }
        }

        // iterate through galaxies and find the manhattan distance which will be the shortest
        int count = galaxies.size();
        long ans = 0;
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                // galaxies could be apart from each other in any orientation
                // so let r1 be the smaller row, c1 be the smaller col
                int[] first = galaxies.get(i);
                int[] second = galaxies.get(j);
                int r1 = Math.min(first[0], second[0]);
                int r2 = Math.max(first[0], second[0]);
                int c1 = Math.min(first[1], second[1]);
                int c2 = Math.max(first[1], second[1]);
                // count extra rows and cols using binary search and getting the differences between left and right boundaries
                int extraRows = binarySearch(r2, emptyRows) - binarySearch(r1, emptyRows);
                int extraCols = binarySearch(c2, emptyCols) - binarySearch(c1, emptyCols);
                long verticalDistance = r2 - r1 + extraRows * (long) emptyMultiplier;
                long horizontalDistance = c2 - c1 + extraCols * (long) emptyMultiplier;
                ans += verticalDistance + horizontalDistance;
            }
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day11.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine;
        while ((currLine = reader.readLine()) != null) {
            List<Character> row = new ArrayList<>();
            for (char ch : currLine.toCharArray()) {
                row.add(ch);
            }
            matrix.add(row);
        }
        rows = matrix.size();
        cols = matrix.get(0).size();

//        System.out.printf("Answer for part one: %d%n", calculatePartOne(1));
        System.out.printf("Answer for part two: %d%n", calculatePartOne(999999));

        reader.close();
    }
}
