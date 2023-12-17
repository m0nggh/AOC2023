import java.io.*;
import java.util.*;

class Node implements Comparable<Node> {
    int sum;
    int r;
    int c;
    String direction;
    int count;

    Node(int sum, int r, int c, String direction, int count) {
        this.sum = sum;
        this.r = r;
        this.c = c;
        this.direction = direction;
        this.count = count;
    }

    @Override
    public int compareTo(Node other) {
        return sum - other.sum;
    }

    @Override
    public String toString() {
        return String.format("Sum: %d, row: %d, col: %d, direction: %s, count: %d", sum, r, c, direction, count);
    }
}

public class Day17 {
    private static List<List<Integer>> matrix = new ArrayList<>();
    private static int rows;
    private static int cols;
    private static boolean[][][][] visited;
    private static final int DIRECTIONS_COUNT = 4;
    private static final int MAX_COUNT_P1 = 3;
    private static final int MAX_COUNT_P2 = 10;

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
    private static final Map<String, int[]> DIRECTIONS_MAP = new HashMap<>() {{
        put(LEFT, LEFT_DIR);
        put(RIGHT, RIGHT_DIR);
        put(TOP, TOP_DIR);
        put(BOTTOM, BOTTOM_DIR);
    }};
    private static final Map<String, int[]> OPP_DIRECTIONS_MAP = new HashMap<>() {{
        put(LEFT, RIGHT_DIR);
        put(RIGHT, LEFT_DIR);
        put(TOP, BOTTOM_DIR);
        put(BOTTOM, TOP_DIR);
    }};

    private static int dijkstraP2() {
        // part two changes: endpoint check + visiting neighbours check
        PriorityQueue<Node> pq = new PriorityQueue<>();
        visited = new boolean[rows][cols][DIRECTIONS_COUNT][MAX_COUNT_P2];
        Node rightNode = new Node(matrix.get(0).get(1), 0, 1, RIGHT, 1);
        Node bottomNode = new Node(matrix.get(1).get(0), 1, 0, BOTTOM, 1);
        pq.add(rightNode);
        pq.add(bottomNode);

        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            // FIRST CHANGE IN PART TWO
            if (curr.r == rows - 1 && curr.c == cols - 1 && curr.count >= 4) {
                return curr.sum;
            }
            if (visited[curr.r][curr.c][DIR_INDEX_MAP.get(curr.direction)][curr.count - 1]) {
                continue;
            }
            visited[curr.r][curr.c][DIR_INDEX_MAP.get(curr.direction)][curr.count - 1] = true;
            int[] currDir = DIRECTIONS_MAP.get(curr.direction);
            // iterate through possible neighbours
            for (Map.Entry<String, int[]> entry : DIRECTIONS_MAP.entrySet()) {
                String nextDirStr = entry.getKey();
                int[] nextDir = entry.getValue();
                if (Arrays.equals(nextDir, OPP_DIRECTIONS_MAP.get(curr.direction))) {
                    continue; // cannot reverse direction
                }

                // check for invalid range and count
                int newR = curr.r + nextDir[0];
                int newC = curr.c + nextDir[1];
                if (newR < 0 || newR >= rows || newC < 0 || newC >= cols) {
                    continue;
                }
                int nextHeat = matrix.get(newR).get(newC);

                Node nextNode = new Node(curr.sum + nextHeat, newR, newC, nextDirStr, 1);
                // SECOND CHANGE IN PART TWO
                if (Arrays.equals(currDir, nextDir)) {
                    if (curr.count == 10) {
                        continue;
                    }
                    nextNode.count = curr.count + 1; // same direction needs to be avoided after 10 consecutive times
                } else {
                    if (curr.count < 4) {
                        continue; // same direction needs to be continued 4 times in a row
                    }
                }
                pq.add(nextNode);
            }
        }
        return -1; // not possible
    }

    private static int dijkstra() {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        visited = new boolean[rows][cols][DIRECTIONS_COUNT][MAX_COUNT_P1];
        Node rightNode = new Node(matrix.get(0).get(1), 0, 1, RIGHT, 1);
        Node bottomNode = new Node(matrix.get(1).get(0), 1, 0, BOTTOM, 1);
        pq.add(rightNode);
        pq.add(bottomNode);

        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            if (curr.r == rows - 1 && curr.c == cols - 1) {
                return curr.sum;
            }
            if (visited[curr.r][curr.c][DIR_INDEX_MAP.get(curr.direction)][curr.count - 1]) {
                continue;
            }
            visited[curr.r][curr.c][DIR_INDEX_MAP.get(curr.direction)][curr.count - 1] = true;
            int[] currDir = DIRECTIONS_MAP.get(curr.direction);
            // iterate through possible neighbours
            for (Map.Entry<String, int[]> entry : DIRECTIONS_MAP.entrySet()) {
                String nextDirStr = entry.getKey();
                int[] nextDir = entry.getValue();
                if (Arrays.equals(nextDir, OPP_DIRECTIONS_MAP.get(curr.direction))) {
                    continue; // cannot reverse direction
                }

                // check for invalid range and count
                int newR = curr.r + nextDir[0];
                int newC = curr.c + nextDir[1];
                if (newR < 0 || newR >= rows || newC < 0 || newC >= cols) {
                    continue;
                }
                int nextHeat = matrix.get(newR).get(newC);

                Node nextNode = new Node(curr.sum + nextHeat, newR, newC, nextDirStr, 1);
                if (Arrays.equals(currDir, nextDir)) {
                    if (curr.count == 3) {
                        continue;
                    }
                    nextNode.count = curr.count + 1; // same direction needs to be avoided after 3 consecutive times
                }
                pq.add(nextNode);
            }
        }
        return -1; // not possible
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day17.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine;
        while ((currLine = reader.readLine()) != null) {
            List<Integer> row = new ArrayList<>();
            for (String s : currLine.split("")) {
                row.add(Integer.parseInt(s));
            }
            matrix.add(row);
        }
        rows = matrix.size();
        cols = matrix.get(0).size();

        System.out.printf("Answer for part one: %d%n", dijkstra());
        System.out.printf("Answer for part two: %d%n", dijkstraP2());

        reader.close();
    }
}
