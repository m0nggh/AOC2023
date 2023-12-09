import java.io.*;
import java.util.*;

class Cards implements Comparable<Cards> {
    final Map<String, Integer> LABEL_RANK = new HashMap<>(){{
        put("A", 14);
        put("K", 13);
        put("Q", 12);
        put("J", 11); // for part one
        put("T", 10);
        put("9", 9);
        put("8", 8);
        put("7", 7);
        put("6", 6);
        put("5", 5);
        put("4", 4);
        put("3", 3);
        put("2", 2);
        put("J", 1); // for part two
    }};

    final int FIVE_OF_A_KIND_RANK = 7;
    final int FOUR_OF_A_KIND_RANK = 6;
    final int FULL_HOUSE_RANK = 5;
    final int THREE_OF_A_KIND_RANK = 4;
    final int TWO_PAIRS_RANK = 3;
    final int ONE_PAIR_RANK = 2;
    final int HIGH_CARD_RANK = 1;

    String[] cards;
    int bid;

    Cards(String[] cards, int bid) {
        this.cards = cards;
        this.bid = bid;
    }

    // compare first card to the last card
    private int compareLabels(Cards other) {
        for (int i = 0; i < cards.length; i++) {
            String card = cards[i];
            String otherCard = other.cards[i];
            if (LABEL_RANK.get(card) != LABEL_RANK.get(otherCard)) {
                return LABEL_RANK.get(card) - LABEL_RANK.get(otherCard);
            }
        }
        return 0;
    }

    // compare different combinations for part one
    private int getRank() {
        Map<String, Integer> countMap = new HashMap<>();
        for (String card : cards) {
            countMap.put(card, countMap.getOrDefault(card, 0) + 1);
        }
        if (countMap.size() == 1) {
            return FIVE_OF_A_KIND_RANK;
        }

        if (countMap.size() == 2) {
            for (int count : countMap.values()) {
                if (count == 4) {
                    return FOUR_OF_A_KIND_RANK;
                }
            }
            return FULL_HOUSE_RANK;
        }

        if (countMap.size() == 3) {
            for (int count : countMap.values()) {
                if (count == 3) {
                    return THREE_OF_A_KIND_RANK;
                }
            }
            return TWO_PAIRS_RANK;
        }

        if (countMap.size() == 4) {
            return ONE_PAIR_RANK;
        }
        return HIGH_CARD_RANK;
    }

    // compare different combinations for part two
    private int getRankWithJoker() {
        Map<String, Integer> countMap = new HashMap<>();
        int jokerCount = 0;
        for (String card : cards) {
            if (card.equals("J")) {
                jokerCount++;
                continue;
            }
            countMap.put(card, countMap.getOrDefault(card, 0) + 1);
        }
        // find max count in countMap and update with joker count
        String charWithMaxCount = "J"; // default to J
        int max = 0;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            String ch = entry.getKey();
            int count = entry.getValue();
            if (count > max) {
                max = count;
                charWithMaxCount = ch;
            }
        }
        countMap.put(charWithMaxCount, countMap.getOrDefault(charWithMaxCount, 0) + jokerCount);

        if (countMap.size() == 1) {
            return FIVE_OF_A_KIND_RANK;
        }

        if (countMap.size() == 2) {
            for (int count : countMap.values()) {
                if (count == 4) {
                    return FOUR_OF_A_KIND_RANK;
                }
            }
            return FULL_HOUSE_RANK;
        }

        if (countMap.size() == 3) {
            for (int count : countMap.values()) {
                if (count == 3) {
                    return THREE_OF_A_KIND_RANK;
                }
            }
            return TWO_PAIRS_RANK;
        }

        if (countMap.size() == 4) {
            return ONE_PAIR_RANK;
        }
        return HIGH_CARD_RANK;
    }

    @Override
    public int compareTo(Cards other) {
        // for part two
        if (getRankWithJoker() != other.getRankWithJoker()) {
            return getRankWithJoker() - other.getRankWithJoker();
        }

        // for part one
//        if (getRank() != other.getRank()) {
//            return getRank() - other.getRank();
//        }
        return compareLabels(other);
    }

    @Override
    public String toString() {
        return String.format("Cards: %s, Bid: %d", Arrays.toString(cards), bid);
    }
}

public class Day7 {
    private static List<Cards> cardsList = new ArrayList<>();

    private static int calculateAnswer() {
        int ans = 0;
        int rank = 1;
        for (Cards cards : cardsList) {
            ans += rank * cards.bid;
            rank++;
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day7.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine;
        while ((currLine = reader.readLine()) != null) {
            String[] cardsAndBid = currLine.split("\\s+");
            Cards cards = new Cards(cardsAndBid[0].trim().split(""), Integer.parseInt(cardsAndBid[1].trim()));
            cardsList.add(cards);
        }
        Collections.sort(cardsList);
//        System.out.printf("Answer for part one: %d", calculateAnswer());
        System.out.printf("Answer for part two: %d", calculateAnswer());
//        System.out.println(cardsList);

        reader.close();
    }
}
