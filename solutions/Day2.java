import java.io.*;
import java.util.*;

public class Day2 {
    // for part 1
    private static int RED_CUBES = 12;
    private static int GREEN_CUBES = 13;
    private static int BLUE_CUBES = 14;
    private static Map<String, Integer> cubesCount = new HashMap<>() {{
        put("red", RED_CUBES);
        put("green", GREEN_CUBES);
        put("blue", BLUE_CUBES);
    }};

    private static int partOne(List<String> inputs) {
        int ans = 0;
        for (int i = 0; i < inputs.size(); i++) {
            boolean shouldAdd = true;
            String text = inputs.get(i);
            String[] gameAndRemainder = text.split(":"); // [Game 1, ...]
            String records = gameAndRemainder[1];
            String[] recordsArr = records.split(";"); // ["1 blue, 1 red", "1 red", ...]
            for (String record : recordsArr) {
                // iterate and find count for each colour possible in each record
                String[] subrecords = record.split(","); // ["1 blue", "1 red", ...]
                for (String subrecord : subrecords) {
                    String[] countAndColour = subrecord.trim().split(" "); // ["1", "blue"]
                    int count = Integer.parseInt(countAndColour[0]);
                    String colour = countAndColour[1];
                    if (count > cubesCount.get(colour)) {
                        shouldAdd = false;
                        break;
                    }
                }
                if (!shouldAdd) {
                    break;
                }
            } 
            if (shouldAdd) {
                ans += i + 1;
            }
        }
        return ans;
    }

    private static int partTwo(List<String> inputs) {
        int ans = 0;
        for (int i = 0; i < inputs.size(); i++) {
            // keep track of the max for each colour
            Map<String, Integer> maxColourCountMap = new HashMap<>();
            String text = inputs.get(i);
            String[] gameAndRemainder = text.split(":"); // [Game 1, ...]
            String records = gameAndRemainder[1];
            String[] recordsArr = records.split(";"); // ["1 blue, 1 red", "1 red", ...]
            for (String record : recordsArr) {
                // iterate and find count for each colour possible in each record
                String[] subrecords = record.split(","); // ["1 blue", "1 red", ...]
                for (String subrecord : subrecords) {
                    String[] countAndColour = subrecord.trim().split(" "); // ["1", "blue"]
                    int count = Integer.parseInt(countAndColour[0]);
                    String colour = countAndColour[1];

                    // part 2: check and update max count
                    int currMaxCount = maxColourCountMap.getOrDefault(colour, 0);
                    maxColourCountMap.put(colour, Math.max(count, currMaxCount));
                }
            }
            // part 2: calculate the powers
            int power = 1;
            for (int count : maxColourCountMap.values()) {
                power *= count;
            }
            ans += power;
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day2.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine;
        List<String> inputs = new ArrayList<>();
        while ((currLine = reader.readLine()) != null) {
            inputs.add(currLine);
        }
        System.out.printf("Answer for part one: %d%n", partOne(inputs));
        System.out.printf("Answer for part two: %d%n", partTwo(inputs));
        reader.close();
    }
}
