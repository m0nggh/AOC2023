import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Day1 {
    private static int partOne(List<String> inputs) {
        int ans = 0;
        for (String text : inputs) {
            // use two pointers to find the leftmost and rightmost
            int l = 0;
            int r = text.length() - 1;
            while (!Character.isDigit(text.charAt(l))) {
                l++;
            }
            while (!Character.isDigit(text.charAt(r))) {
                r--;
            }
            int val = (int) (text.charAt(l) - '0') * 10 + (int) text.charAt(r) - '0';
            ans += val;
        }
        return ans;
    }

    private static int partTwo(List<String> inputs) {
        Map<String, Integer> wordToDigitMap = new HashMap<>() {{
            put("one", 1);
            put("two", 2);
            put("three", 3);
            put("four", 4);
            put("five", 5);
            put("six", 6);
            put("seven", 7);
            put("eight", 8);
            put("nine", 9);
        }};
        int ans = 0;
        for (String text : inputs) {
            // take from part 1: use two pointers to find the leftmost and rightmost
            int l = 0;
            int r = text.length() - 1;
            while (!Character.isDigit(text.charAt(l))) {
                l++;
            }
            while (!Character.isDigit(text.charAt(r))) {
                r--;
            }
            // continue with part 2: find first and last word occurrences at its own digit index
            int[] firstWordIndexes = new int[10];
            int[] lastWordIndexes = new int[10];
            for (Map.Entry<String, Integer> entry : wordToDigitMap.entrySet()) {
                String word = entry.getKey();
                int digit = entry.getValue();
                firstWordIndexes[digit] = text.indexOf(word);
                lastWordIndexes[digit] = text.lastIndexOf(word);
            }
            // find first and last digit indexes using the first and last word position
            int firstWordPos = Integer.MAX_VALUE;
            int firstDigit = -1;
            int lastWordPos = Integer.MIN_VALUE;
            int lastDigit = -1;
            for (int i = 1; i < 10; i++) {
                if (firstWordIndexes[i] != -1) {
                    if (firstWordIndexes[i] < firstWordPos) {
                        firstWordPos = firstWordIndexes[i];
                        firstDigit = i;
                    }
                }
                if (lastWordIndexes[i] != -1) {
                    if (lastWordIndexes[i] > lastWordPos) {
                        lastWordPos = lastWordIndexes[i];
                        lastDigit = i;
                    }
                }
            }
            // compare and take the smaller digit for first and last
            int nearerFirstDigit = l < firstWordPos ? (int) text.charAt(l) - '0' : firstDigit;
            int furtherLastDigit = r > lastWordPos ? (int) text.charAt(r) - '0' : lastDigit;
            ans += nearerFirstDigit * 10 + furtherLastDigit; 
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String file = "../inputs/day1.txt";
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String currLine;
        List<String> inputs = new ArrayList<>();
        while ((currLine = reader.readLine()) != null) {
            inputs.add(currLine);
        }
        String partOneAnswer = String.format("Part one answer: %d", partOne(inputs));
        System.out.println(partOneAnswer);
        System.out.printf("Part two answer: %d%n", partTwo(inputs));
        reader.close();
    }
}
