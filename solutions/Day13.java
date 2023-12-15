import java.io.*;
import java.util.*;

public class Day13 {
    private static int calculatePatternForPartOne(List<List<Character>> pattern) {
        int rows = pattern.size();
        int cols = pattern.get(0).size();
        int lines = 0;
        boolean isVertical = true;
        // check reflection between rows
        for (int row = 1; row < rows; row++) {
            int l = row - 1;
            int r = row;
            boolean isValidReflection = true;
            while (l >= 0 && r < rows) {
                // check entire row content for reflection
                for (int col = 0; col < cols; col++) {
                    if (pattern.get(l).get(col) != pattern.get(r).get(col)) {
                        isValidReflection = false;
                        break;
                    }
                }
                l--;
                r++;
            }
            if (isValidReflection) {
                lines = row;
                isVertical = false;
                break;
            }
        }

        // check for cols
        for (int col = 1; col < cols; col++) {
            int l = col - 1;
            int r = col;
            boolean isValidReflection = true;
            while (l >= 0 && r < cols) {
                // check entire row content for reflection
                for (int row = 0; row < rows; row++) {
                    if (pattern.get(row).get(l) != pattern.get(row).get(r)) {
                        isValidReflection = false;
                        break;
                    }
                }
                l--;
                r++;
            }
            if (isValidReflection) {
                lines = col;
                break;
            }
        }
        return isVertical ? lines : lines * 100;
    }

    private static int calculatePatternForPartTwo(List<List<Character>> pattern) {
        int rows = pattern.size();
        int cols = pattern.get(0).size();
        int lines = 0;
        boolean isVertical = true;
        // check reflection between rows
        for (int row = 1; row < rows; row++) {
            int l = row - 1;
            int r = row;
            int discrepancies = 0; // part two change
            while (l >= 0 && r < rows) {
                // check entire row content for reflection
                for (int col = 0; col < cols; col++) {
                    if (pattern.get(l).get(col) != pattern.get(r).get(col)) {
                        discrepancies++;
                    }
                }
                l--;
                r++;
            }
            if (discrepancies == 1) {
                lines = row;
                isVertical = false;
                break;
            }
        }

        // check for cols
        for (int col = 1; col < cols; col++) {
            int l = col - 1;
            int r = col;
            int discrepancies = 0; // part two change
            while (l >= 0 && r < cols) {
                // check entire row content for reflection
                for (int row = 0; row < rows; row++) {
                    if (pattern.get(row).get(l) != pattern.get(row).get(r)) {
                        discrepancies++;
                    }
                }
                l--;
                r++;
            }
            if (discrepancies == 1) {
                lines = col;
                break;
            }
        }
        return isVertical ? lines : lines * 100;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day13.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine;
        int ansP1 = 0;
        int ansP2 = 0;
        while ((currLine = reader.readLine()) != null) {
            List<List<Character>> pattern = new ArrayList<>();
            while (currLine != null && currLine.length() > 0) {
                List<Character> row = new ArrayList<>();
                for (String s : currLine.split("")) {
                    row.add(s.charAt(0));
                }
                pattern.add(row);
                currLine = reader.readLine();
            }
            // calculate part one for each pattern
            ansP1 += calculatePatternForPartOne(pattern);;
            ansP2 += calculatePatternForPartTwo(pattern);
        }

        System.out.printf("Answer for part one: %d%n", ansP1);
        System.out.printf("Answer for part two: %d%n", ansP2);

        reader.close();
    }
}
