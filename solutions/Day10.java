import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Day10 {
    private static List<List<Character>> matrix = new ArrayList<>();
    private static int rows;
    private static int cols;
    private static boolean[][] visited;

    private static int startRow;
    private static int startCol;

    // some constants to help get neighbours
    private static final int[] LEFT_DIR = new int[]{ 0, -1 };
    private static final int[] RIGHT_DIR = new int[]{ 0, 1 };
    private static final int[] TOP_DIR = new int[]{ -1, 0 };
    private static final int[] BOTTOM_DIR = new int[]{ 1, 0 };
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String TOP = "top";
    private static final String BOTTOM = "bottom";
    private static Map<String, int[]> directionsMap = new HashMap<>(){{
        put(LEFT, LEFT_DIR);
        put(RIGHT, RIGHT_DIR);
        put(TOP, TOP_DIR);
        put(BOTTOM, BOTTOM_DIR);
    }};
    private static Map<String, int[]> oppDirectionsMap = new HashMap<>(){{
        put(LEFT, RIGHT_DIR);
        put(RIGHT, LEFT_DIR);
        put(TOP, BOTTOM_DIR);
        put(BOTTOM, TOP_DIR);
    }};
    private static int[] invalidTile = new int[]{ -1, -1 };

    private static int[] formNeighbour(int[] tile, int[] dir) {
        int r = tile[0] + dir[0];
        int c = tile[1] + dir[1];
        // check if in range or visited
        if (r < 0 || r >= rows || c < 0 || c >= cols || visited[r][c]) {
            return invalidTile;
        }
        return new int[]{ r, c };
    }

    private static List<int[]> getNeighbourDirs(int[] tile) {
        int r = tile[0];
        int c = tile[1];
        char ch = matrix.get(r).get(c);
        List<int[]> neighbourDirs = new ArrayList<>();
        if (ch == 'S') {
            for (Map.Entry<String, int[]> entry : directionsMap.entrySet()) {
                boolean isValidNeighbour = false;
                String neighbourDirStr = entry.getKey();
                int[] neighbourDir = entry.getValue();
                int[] potentialNeighbour = formNeighbour(tile, neighbourDir);
                if (Arrays.equals(potentialNeighbour, invalidTile)) {
                    continue;
                }

                List<int[]> potentialNeighbourDirs = getNeighbourDirs(potentialNeighbour);
                for (int[] potentialNeighbourDir : potentialNeighbourDirs) {
                    // check if opposite direction exists for the potential neighbour
                    if (Arrays.equals(potentialNeighbourDir, oppDirectionsMap.get(neighbourDirStr))) {
                        isValidNeighbour = true;
                    }
                }
                if (isValidNeighbour) {
                    neighbourDirs.add(neighbourDir);
                }
            }
            // modify the letter for S to be the correct piece: proper method is to find its neighbours and do so...
            matrix.get(r).set(c, '7');
            return neighbourDirs;
        }

        switch (ch) {
            case '|':
                neighbourDirs.add(directionsMap.get(TOP));
                neighbourDirs.add(directionsMap.get(BOTTOM));
                break;
            case '-':
                neighbourDirs.add(directionsMap.get(LEFT));
                neighbourDirs.add(directionsMap.get(RIGHT));
                break;
            case 'L':
                neighbourDirs.add(directionsMap.get(TOP));
                neighbourDirs.add(directionsMap.get(RIGHT));
                break;
            case 'J':
                neighbourDirs.add(directionsMap.get(TOP));
                neighbourDirs.add(directionsMap.get(LEFT));
                break;
            case '7':
                neighbourDirs.add(directionsMap.get(BOTTOM));
                neighbourDirs.add(directionsMap.get(LEFT));
                break;
            case 'F':
                neighbourDirs.add(directionsMap.get(BOTTOM));
                neighbourDirs.add(directionsMap.get(RIGHT));
                break;
            default:
                // this is a . so do nothing
        }
        return neighbourDirs;
    }

    private static int calculatePartOne() {
        // perform good old bfs
        int distance = -1;
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{ startRow, startCol });
        visited[startRow][startCol] = true;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] tile = queue.poll();
                List<int[]> neighbourDirs = getNeighbourDirs(tile);
                for (int[] neighbourDir : neighbourDirs) {
                    int[] neighbour = formNeighbour(tile, neighbourDir);
                    if (Arrays.equals(neighbour, invalidTile)) {
                        continue;
                    }

                    int r = neighbour[0];
                    int c = neighbour[1];
                    // add to visited and queue
                    visited[r][c] = true;
                    queue.add(new int[]{ r, c });
                }
            }
            distance++;
        }
        return distance;
    }

    private static int calculatePartTwo() {
        // ray casting algorithm to check if point is inside an enclosed polygon
        int ans = 0;
        // for every tile in the matrix not part of the polygon, count the number of |, L, J on its left.
        // reasoning is 2 combinations count as 1 edge: F----J or L-----7, take either cross combination
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (visited[i][j]) {
                    continue;
                }

                int count = 0;
                for (int k = 0; k < j; k++) {
                    // only include those edges part of the polygon
                    if (!visited[i][k]) {
                        continue;
                    }
                    char edge = matrix.get(i).get(k);
                    if (edge == '|' || edge == 'L' || edge == 'J') {
                        count++;
                    }
                }
                // if count is an odd number, it must be inside the polygon
                if (count % 2 != 0) {
                    ans++;
                }
            }
        }
        return ans;
    }


    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day10.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine;
        int row = 0;
        while ((currLine = reader.readLine()) != null) {
            List<Character> charactersRow = new ArrayList<>();
            for (int col = 0; col < currLine.length(); col++) {
                char ch = currLine.charAt(col);
                charactersRow.add(ch);
                if (ch == 'S') {
                    startRow = row;
                    startCol = col;
                }
            }
            matrix.add(charactersRow);
            row++;
        }
        rows = matrix.size();
        cols = matrix.get(0).size();
        visited = new boolean[rows][cols];

        System.out.printf("Answer for part one: %d%n", calculatePartOne());
        System.out.printf("Answer for part two: %d%n", calculatePartTwo());

        reader.close();
    }
}
