import java.io.*;
import java.util.*;

public class Day4 {
    private static List<List<Integer>> winningNumbersList = new ArrayList<>();
    private static List<Set<Integer>> cardNumbersList = new ArrayList<>();

    private static int calculatePartOne() {
        int ans = 0;
        for (int i = 0; i < winningNumbersList.size(); i++) {
            List<Integer> winningNumbers = winningNumbersList.get(i);
            Set<Integer> cardNumbers = cardNumbersList.get(i);
            int winCount = -1; // hacky because 2^-1 is 0.5 and it equates to 0
            for (int num : winningNumbers) {
                if (cardNumbers.contains(num)) {
                    winCount++;
                }
            }
            ans += (int) Math.pow(2, winCount);
        }
        return ans;
    }

    private static int calculatePartTwo() {
        int size = winningNumbersList.size();
        int[] cardsWonArray = new int[size];
        Arrays.fill(cardsWonArray, 1); // include original copy
        for (int i = 0; i < size; i++) {
            List<Integer> winningNumbers = winningNumbersList.get(i);
            Set<Integer> cardNumbers = cardNumbersList.get(i);
            int winCount = 0;
            for (int num : winningNumbers) {
                if (cardNumbers.contains(num)) {
                    winCount++;
                }
            }

            for (int j = 1; j <= winCount; j++) {
                cardsWonArray[i + j] += cardsWonArray[i]; 
            }
        }
        // accummulate all cards count
        int ans = 0;
        for (int count : cardsWonArray) {
            ans += count;
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day4.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine;
        while ((currLine = reader.readLine()) != null) {
            String[] cardAndNumbers = currLine.split(":");
            String numbers = cardAndNumbers[1];
            String[] winningAndCardNumbers = numbers.split("\\|");

            List<Integer> winningNumbers = new ArrayList<>();
            for (String numStr : winningAndCardNumbers[0].trim().split("\\s+")) {
                winningNumbers.add(Integer.parseInt(numStr));
            }
            winningNumbersList.add(winningNumbers);

            Set<Integer> cardNumbers = new HashSet<>();
            for (String numStr : winningAndCardNumbers[1].trim().split("\\s+")) {
                cardNumbers.add(Integer.parseInt(numStr));
            }
            cardNumbersList.add(cardNumbers);
        }
        System.out.printf("Answer for part one: %d%n", calculatePartOne());
        System.out.printf("Answer for part two: %d%n", calculatePartTwo());
            
        reader.close();
    }
}
