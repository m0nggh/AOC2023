import java.io.*;
import java.util.*;

public class Day14 {
    private static List<List<Character>> matrix = new ArrayList<>();
    private static int rows;
    private static int cols;

    private static void swap(int r1, int c1, int r2, int c2) {
        char temp = matrix.get(r1).get(c1);
        matrix.get(r1).set(c1, matrix.get(r2).get(c2));
        matrix.get(r2).set(c2, temp);
    }

    private static void rotateLeft() {
        // flip vertically
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols / 2; c++) {
                swap(r, c, r, cols - c - 1);
            }
        }
        // transpose ([0,0], [1,1], ...)
        for (int r = 0; r < rows - 1; r++) {
            for (int c = r + 1; c < cols; c++) {
                swap(r, c, c, r);
            }
        }
    }

    private static void rotateToSpecificOrientation(int times) {
        for (int i = 0; i < times; i++) {
            rotateLeft();
        }
    }

    private static void tiltUp() {
        for (int col = 0; col < cols; col++) {
            int row = 0;
            while (row < rows) {
                while (row < rows && matrix.get(row).get(col) == '#') {
                    row++; // keep moving down
                }
                // now row is at . or O
                int startRow = row;
                int rockCount = 0;
                while (row < rows && matrix.get(row).get(col) != '#') {
                    if (matrix.get(row).get(col) == 'O') {
                        rockCount++;
                    }
                    row++;
                }
                // fill the row based on rock count then empty dots
                for (int i = startRow; i < startRow + rockCount; i++) {
                    matrix.get(i).set(col, 'O');
                }
                for (int i = startRow + rockCount; i < row; i++) {
                    matrix.get(i).set(col, '.');
                }
            }
        }
    }

    private static void rotateOneCycle() {
        int directions = 4; // go four directions
        for (int i = 0; i < directions; i++) {
            // rotate, tilt and rotate back
            rotateToSpecificOrientation((directions - i) % directions);
            tiltUp();
            rotateToSpecificOrientation(i);
        }
    }

    private static int countLoad() {
        int load = 0;
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                if (matrix.get(row).get(col) == 'O') {
                    load += rows - row;
                }
            }
        }
        return load;
    }

    private static int calculatePartTwo() {
        int cycle = 0;
        int maxCycles = 1_000_000_000;
        // use a set and list to check for pattern
        Set<String> matrixSet = new HashSet<>();
        List<String> matrixStorage = new ArrayList<>();
        String matrixStr = matrix.toString();
        matrixSet.add(matrixStr);
        matrixStorage.add(matrixStr);
        while (cycle < maxCycles) {
            cycle++;
            rotateOneCycle();
            matrixStr = matrix.toString();
            if (matrixSet.contains(matrixStr)) {
                break;
            }
            matrixSet.add(matrixStr);
            matrixStorage.add(matrixStr);
        }

        int startCycle = matrixStorage.indexOf(matrixStr); // find first occurrence
        // the 1 billionth cycle = (maxCycles - start) % (cycle - startCycle) + start
        int correctCycle = (maxCycles - startCycle) % (cycle - startCycle) + startCycle;
        // modern day requires modern solutions... to get the string back to arraylist, just iterate till that cycle
        for (int i = startCycle; i < correctCycle; i++) {
            rotateOneCycle();
        }
        return countLoad();
    }

    private static int calculatePartOne() {
        int ans = 0;
        for (int col = 0; col < cols; col++) {
            int currLoad = rows;
            int colLoad = 0;
            for (int row = 0; row < rows; row++) {
                char ch = matrix.get(row).get(col);
                if (ch == 'O') {
                    colLoad += currLoad;
                    currLoad--;
                } else if (ch == '#') {
                    currLoad = rows - row - 1;
                }
            }
            ans += colLoad;
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day14.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine;
        while ((currLine = reader.readLine()) != null) {
            List<Character> row = new ArrayList<>();
            for (String s : currLine.split("")) {
                row.add(s.charAt(0));
            }
            matrix.add(row);
        }
        rows = matrix.size();
        cols = matrix.get(0).size();

        System.out.printf("Answer for part one: %d%n", calculatePartOne());
        System.out.printf("Answer for part two: %d%n", calculatePartTwo());

        reader.close();
    }
}
