import java.io.*;
import java.util.*;

public class Day3 {
    private static List<List<Character>> inputMatrix;
    private static int rows;
    private static int cols;
    private static final String REGEX_STRING = ".0123456789";
    private static final int[][] DIRS = {
        {0,-1}, {1,-1}, {1,0}, {1,1}, 
        {0,1}, {-1,1}, {-1,0}, {-1,-1}
    };
    // for part two
    private static Map<String, List<Integer>> symbolsMap = new HashMap<>(); // key is the coordinates of the symbol concatenated, value is the numbers associated

    private static boolean hasSymbol(int row, int start, int end) {
        for (int col = start; col < end; col++) {
            // sweep all 8 directions
            for (int[] dir : DIRS) {
                int r = row + dir[0];
                int c = col + dir[1];
                if (r < 0 || r >= rows || c < 0 || c >= cols) {
                    continue; // out of range
                }
                char ch = inputMatrix.get(r).get(c);
                if (REGEX_STRING.indexOf(ch) == -1) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int calculatePartOne() {
        // iterate through row by row, whenever a number is chanced upon, accummulate the whole number, sweep the surrounding for symbols
        int ans = 0;
        for (int row = 0; row < rows; row++) {
            int col = 0;
            while (col < cols) {
                char ch = inputMatrix.get(row).get(col);
                if (!Character.isDigit(ch)) {
                    col++;
                    continue;
                }

                int start = col;
                int end = col;
                while (end < cols && Character.isDigit(inputMatrix.get(row).get(end))) {
                    end++;
                }
                if (hasSymbol(row, start, end)) {
                    StringBuilder sb = new StringBuilder();
                    while (start < end) {
                        sb.append(inputMatrix.get(row).get(start));
                        start++;
                    }
                    ans += Integer.parseInt(sb.toString());
                }
                col = end;
            }
        }
        return ans;
    }

    // duplicated to compare similarity with part one
    private static void updateSurroundingSymbols(int row, int start, int end) {
        // accumulate number first
        StringBuilder sb = new StringBuilder();
        for (int col = start; col < end; col++) {
            sb.append(inputMatrix.get(row).get(col));
        }
        int num = Integer.parseInt(sb.toString());

        // store in symbols map
        Set<String> coordinatesSeen = new HashSet<>(); // IMPT: need to not store back in the same coordinate
        for (int col = start; col < end; col++) {
            // sweep all 8 directions
            for (int[] dir : DIRS) {
                int r = row + dir[0];
                int c = col + dir[1];
                if (r < 0 || r >= rows || c < 0 || c >= cols) {
                    continue; // out of range
                }
                char ch = inputMatrix.get(r).get(c);
                // ONLY * symbols
                if (ch == '*') {
                    String coordinate = String.valueOf(r) + "," + String.valueOf(c); // IMPT: sneaky way to ensure uniqueness of coordinates
                    if (coordinatesSeen.contains(coordinate)) {
                        continue;
                    }
                    coordinatesSeen.add(coordinate);
                    List<Integer> numbers = symbolsMap.getOrDefault(coordinate, new ArrayList<>());
                    numbers.add(num);
                    symbolsMap.put(coordinate, numbers);
                }
            }
        }
    }

    private static int calculatePartTwo() {
        // iterate through row by row, whenever a number is chanced upon, sweep the surrounding for symbols, update numbers associated with symbol,
        for (int row = 0; row < rows; row++) {
            int col = 0;
            while (col < cols) {
                char ch = inputMatrix.get(row).get(col);
                if (!Character.isDigit(ch)) {
                    col++;
                    continue;
                }

                int start = col;
                int end = col;
                while (end < cols && Character.isDigit(inputMatrix.get(row).get(end))) {
                    end++;
                }
                // update numbers associated with symbol
                updateSurroundingSymbols(row, start, end);
                col = end;
            }
        }
        // perform computation for symbol if count of numbers is exactly 2
        int ans = 0;
        for (List<Integer> numbers : symbolsMap.values()) {
            if (numbers.size() == 2) {
                ans += numbers.get(0) * numbers.get(1);
            }
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day3.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        inputMatrix = new ArrayList<>();
        String currLine;
        while ((currLine = reader.readLine()) != null) {
            List<Character> currRow = new ArrayList<>();
            for (char ch : currLine.toCharArray()) {
                currRow.add(ch);
            }
            inputMatrix.add(currRow);
        }
        rows = inputMatrix.size();
        cols = inputMatrix.get(0).size();
        
        System.out.printf("Answer for part one: %d%n", calculatePartOne());
        System.out.printf("Answer for part two: %d%n", calculatePartTwo());
        reader.close();
    }
}
