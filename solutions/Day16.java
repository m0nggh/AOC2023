import java.io.*;
import java.util.*;

public class Day16 {
    private static List<List<Character>> matrix = new ArrayList<>();
    private static int rows;
    private static int cols;
    private static boolean[][] energised;
    private static boolean[][][] visited; // need to include direction

    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String TOP = "top";
    private static final String BOTTOM = "bottom";
    private static final int[] LEFT_DIR = new int[]{ 0, -1 };
    private static final int[] RIGHT_DIR = new int[]{ 0, 1 };
    private static final int[] TOP_DIR = new int[]{ -1, 0 };
    private static final int[] BOTTOM_DIR = new int[]{ 1, 0 };
    private static final Map<String, Integer> DIR_INDEX_MAP = new HashMap<>() {{
        put(LEFT, 0);
        put(RIGHT, 1);
        put(TOP, 2);
        put(BOTTOM, 3);
    }};
    private static final Map<String, int[][]> EMPTY_SPACE_DIRECTIONS_MAP = new HashMap<>() {{
        put(LEFT, new int[][]{ LEFT_DIR });
        put(RIGHT, new int[][]{ RIGHT_DIR });
        put(TOP, new int[][]{ TOP_DIR });
        put(BOTTOM, new int[][]{ BOTTOM_DIR });
    }};
    private static final Map<String, int[][]> VERTICAL_SPLITTER_DIRECTIONS_MAP = new HashMap<>() {{
        put(LEFT, new int[][]{ TOP_DIR, BOTTOM_DIR });
        put(RIGHT, new int[][]{ TOP_DIR, BOTTOM_DIR });
        put(TOP, new int[][]{ TOP_DIR });
        put(BOTTOM, new int[][]{ BOTTOM_DIR });
    }};
    private static final Map<String, int[][]> HORIZONTAL_SPLITTER_DIRECTIONS_MAP = new HashMap<>() {{
        put(LEFT, new int[][]{ LEFT_DIR });
        put(RIGHT, new int[][]{ RIGHT_DIR });
        put(TOP, new int[][]{ LEFT_DIR, RIGHT_DIR });
        put(BOTTOM, new int[][]{ LEFT_DIR, RIGHT_DIR });
    }};
    private static final Map<String, int[][]> MAIN_DIAGONAL_MIRROR_DIRECTIONS_MAP = new HashMap<>() {{
        put(LEFT, new int[][]{ TOP_DIR });
        put(RIGHT, new int[][]{ BOTTOM_DIR });
        put(TOP, new int[][]{ LEFT_DIR });
        put(BOTTOM, new int[][]{ RIGHT_DIR });
    }};
    private static final Map<String, int[][]> ANTI_DIAGONAL_MIRROR_DIRECTIONS_MAP = new HashMap<>() {{
        put(LEFT, new int[][]{ BOTTOM_DIR });
        put(RIGHT, new int[][]{ TOP_DIR });
        put(TOP, new int[][]{ RIGHT_DIR });
        put(BOTTOM, new int[][]{ LEFT_DIR });
    }};
    private static final Map<int[], String> DIR_TO_STRING_MAP = new HashMap<>(){{
        put(LEFT_DIR, LEFT);
        put(RIGHT_DIR, RIGHT);
        put(TOP_DIR, TOP);
        put(BOTTOM_DIR, BOTTOM);
    }};

    private static void dfs(int r, int c, String direction) {
        int dirIndex = DIR_INDEX_MAP.get(direction);
        if (r < 0 || r >= rows || c < 0 || c >= cols || visited[r][c][dirIndex]) {
            return;
        }

        char ch = matrix.get(r).get(c);
        visited[r][c][dirIndex] = true;
        energised[r][c] = true;
        int[][] dirs;
        if (ch == '.') {
            dirs = EMPTY_SPACE_DIRECTIONS_MAP.get(direction);
        } else if (ch == '|') {
            dirs = VERTICAL_SPLITTER_DIRECTIONS_MAP.get(direction);
        } else if (ch == '-') {
            dirs = HORIZONTAL_SPLITTER_DIRECTIONS_MAP.get(direction);
        } else if (ch == '\\') {
            dirs = MAIN_DIAGONAL_MIRROR_DIRECTIONS_MAP.get(direction);
        } else {
            dirs = ANTI_DIAGONAL_MIRROR_DIRECTIONS_MAP.get(direction);
        }
        for (int[] dir : dirs) {
            dfs(r + dir[0], c + dir[1], DIR_TO_STRING_MAP.get(dir));
        }
    }

    private static int calculatePartOne(int startRow, int startCol, String direction) {
        visited = new boolean[rows][cols][4];
        energised = new boolean[rows][cols];
        dfs(startRow, startCol, direction);
        int ans = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (energised[i][j]) {
                    ans++;
                }
            }
        }
        return ans;
    }

    private static int calculatePartTwo() {
        // attempt from every tile on the edge and get the max
        int max = 0;
        for (int i = 0; i < rows; i++) {
            max = Math.max(calculatePartOne(i, 0, RIGHT), max);
            max = Math.max(calculatePartOne(i, cols - 1, LEFT), max);
        }
        for (int i = 0; i < cols; i++) {
            max = Math.max(calculatePartOne(0, i, BOTTOM), max);
            max = Math.max(calculatePartOne(rows - 1, i, TOP), max);
        }
        return max;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day16.txt";
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

        System.out.printf("Answer for part one: %d%n", calculatePartOne(0, 0, RIGHT));
        System.out.printf("Answer for part two: %d%n", calculatePartTwo());

        reader.close();
    }
}
