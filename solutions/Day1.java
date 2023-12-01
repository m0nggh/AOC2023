import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

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
        reader.close();
    }
}
