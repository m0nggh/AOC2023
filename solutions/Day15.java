import java.io.*;
import java.util.*;

public class Day15 {
    private static List<String> sequences = new ArrayList<>();

    // for part two
    @SuppressWarnings("unchcecked")
    private static List<String>[] boxes = new List[256];
    private static Map<String, Integer> labelMap = new HashMap<>(); // key: label, value: focal length

    private static int performHashAlgo(String sequence) {
        int ans = 0;
        for (char ch : sequence.toCharArray()) {
            ans += ch; // casting to int from ch is redundant
            ans *= 17;
            ans %= 256;
        }
        return ans;
    }

    private static int calculatePartOne() {
        int ans = 0;
        for (String sequence : sequences) {
            ans += performHashAlgo(sequence);
        }
        return ans;
    }

    private static int calculatePartTwo() {
        // initialise boxes first
        for (int i = 0; i < 256; i++) {
            boxes[i] = new ArrayList<>();
        }

        for (String sequence : sequences) {
            if (sequence.indexOf('=') != -1) {
                // obtain label and focal length
                String[] parts = sequence.split("=");
                String label = parts[0];
                int focalLength = Integer.parseInt(parts[1]);
                int boxIndex = performHashAlgo(label);
                List<String> boxContent = boxes[boxIndex];
                if (!boxContent.contains(label)) {
                    boxContent.add(label); // add label if absent
                }
                labelMap.put(label, focalLength); // update focal length regardless
            } else {
                // obtain label
                String label = sequence.substring(0, sequence.length() - 1);
                int boxIndex = performHashAlgo(label);
                List<String> boxContent = boxes[boxIndex];
                if (boxContent.contains(label)) {
                    boxContent.remove(label); // remove label if present
                }
            }
        }

        // iterate through all boxes and calculate focusing power
        int ans = 0;
        for (int i = 0; i < 256; i++) {
            List<String> boxContent = boxes[i];
            for (int j = 0; j < boxContent.size(); j++) {
                String label = boxContent.get(j);
                ans += (i + 1) * (j + 1) * labelMap.get(label);
            }
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day15.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine = reader.readLine();
        for (String sequence : currLine.split(",")) {
            sequences.add(sequence);
        }

        System.out.printf("Answer for part one: %d%n", calculatePartOne());
        System.out.printf("Answer for part two: %d%n", calculatePartTwo());

        reader.close();
    }
}
