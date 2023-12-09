import java.io.*;
import java.util.*;

public class Day8 {
    private static String[] instructions;
    private static Map<String, String[]> networkMap = new HashMap<>(); // key: node, value: [left node, right node]
    private static final int NO_ANSWER = 0;

    private static int calculatePartOne(String source, String dest) {
        Map<String, Integer> instructionsMapping = new HashMap<>(){{
            put("L", 0);
            put("R", 1);
        }};
        int ans = 1;
        // reasonably huge number to exit would be a million, if not hit, return 0
        int exitNumber = 1000000;
        while (!source.equals(dest)) {
            for (String instruction : instructions) {
                String[] nextNodes = networkMap.get(source);
                int nextNodesIndex = instructionsMapping.get(instruction);
                if (nextNodes[nextNodesIndex].equals(dest)) {
                    return ans;
                }
                source = nextNodes[nextNodesIndex];
                ans++;
                if (ans == exitNumber) {
                    return NO_ANSWER;
                }
            }
        }
        return ans;
    }

    private static long getGCD(long a, long b) {
        if (b == 0) {
            return a;
        }
        return getGCD(b, a % b);
    }

    private static long getLCM(long a, long b) {
        return (a * b) / getGCD(a, b);
    }

    private static long calculatePartTwo() {
        // ASSUMPTION: only one source node will hit one dest node...
        List<String> sourceNodes = new ArrayList<>();
        List<String> destNodes = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : networkMap.entrySet()) {
            String node = entry.getKey();
            if (node.endsWith("A")) {
                sourceNodes.add(node);
            }
            if (node.endsWith("Z")) {
                destNodes.add(node);
            }
        }

        List<Integer> counts = new ArrayList<>(); // count for each pair of source and dest node
        for (String source : sourceNodes) {
            for (String dest : destNodes) {
                int count = calculatePartOne(source, dest);
                if (count == NO_ANSWER) {
                    continue;
                }
                counts.add(count);
            }
        }
        System.out.println(counts);

        // assume more than one pair of nodes: java -ea Day8 to enable assertion
        assert(counts.size() > 1): "Only one pair of nodes!";
        long ans = getLCM(counts.get(0), counts.get(1));
        int i = 2;
        while (i < counts.size()) {
            ans = getLCM(ans, counts.get(i++));
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day8.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        instructions = reader.readLine().split("");
        reader.readLine();
        String currLine;
        while ((currLine = reader.readLine()) != null) {
            String[] nodeAndDestinations = currLine.split("=");
            String node = nodeAndDestinations[0].trim();

            String[] leftAndRightNodes = nodeAndDestinations[1].split(",");
            String leftNode = leftAndRightNodes[0].trim().substring(1); // remove open bracket
            String temp = leftAndRightNodes[1].trim();
            String rightNode = temp.substring(0, temp.length() - 1); // remove close bracket
            networkMap.put(node, new String[]{ leftNode, rightNode });
        }

        // for part one
        String source = "AAA";
        String dest = "ZZZ";
        System.out.printf("Answer for part one: %d%n", calculatePartOne(source, dest));
        System.out.printf("Answer for part two: %d%n", calculatePartTwo());
        reader.close();
    }
}
