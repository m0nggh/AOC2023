import java.io.*;
import java.util.*;

public class Day12 {
    private static List<List<Character>> recordList = new ArrayList<>();
    private static List<List<Integer>> groupList = new ArrayList<>();
    private static List<Character> record;
    private static List<Integer> group;

    // add memoisation for part two
    private static Map<String, Long> dp = new HashMap<>(); // key is recordIndex, groupIndex, groupCount

    private static long helper(int recordIndex, int groupIndex, int groupCount) {
        String key = "" + recordIndex + "," + groupIndex + "," + groupCount;
        if (dp.containsKey(key)) {
            return dp.get(key);
        }

        int rSize = record.size();
        int gSize = group.size();
        // handle base cases
        if (recordIndex == rSize) {
            // ends with . or ?
            if (groupIndex == gSize && groupCount == 0) {
                return 1;
            }
            // ends with # or ?
            if (groupIndex == gSize - 1 && groupCount == group.get(gSize - 1)) {
                return 1;
            }
            return 0;
        }
        long ans = 0;
        // handle recursive cases: iterate through possible inputs: '.' and '#'
        char ch = record.get(recordIndex);
        if (ch == '.' || ch == '?') {
            if (groupCount == 0) {
                // case 1: group count = 0, just move record index
                ans += helper(recordIndex + 1, groupIndex, groupCount);
            } else {
                // case 2: group count > 0, ensure group index < gSize and group count is equals to latest group
                if (groupIndex < gSize && groupCount == group.get(groupIndex)) {
                    ans += helper(recordIndex + 1, groupIndex + 1, 0); // reset group count and move both indexes
                }
            }
        }

        if (ch == '#' || ch == '?') {
            // only case is to add to group count
            ans += helper(recordIndex + 1, groupIndex, groupCount + 1);
        }

        dp.put(key, ans);
        return ans;
    }

    private static int calculatePartOne() {
        int ans = 0;
        for (int i = 0; i < recordList.size(); i++) {
            record = recordList.get(i);
            group = groupList.get(i);
            int count = (int) helper(0, 0, 0);
            ans += count;
            dp.clear(); // remember to clear dp
        }
        return ans;
    }

    private static long calculatePartTwo() {
        // transform data from part one
        for (int i = 0; i < recordList.size(); i++) {
            List<Character> currRecord = recordList.get(i);
            List<Character> recordCopy = new ArrayList<>(currRecord);
            // duplicate 4 more times with a ?
            for (int j = 0; j < 4; j++) {
                currRecord.add('?');
                for (char ch : recordCopy) {
                    currRecord.add(ch);
                }
            }

            List<Integer> currGroup = groupList.get(i);
            List<Integer> groupCopy = new ArrayList<>(currGroup);
            // duplicate 4 more times
            for (int j = 0; j < 4; j++) {
                for (int num : groupCopy) {
                    currGroup.add(num);
                }
            }
        }

        long ans = 0;
        for (int i = 0; i < recordList.size(); i++) {
            record = recordList.get(i);
            group = groupList.get(i);
            long count = helper(0, 0, 0);
            ans += count;
            dp.clear(); // remember to clear dp
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day12.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine;
        while ((currLine = reader.readLine()) != null) {
            String[] recordAndGroup = currLine.split(" ");
            List<Character> record = new ArrayList<>();
            for (String s : recordAndGroup[0].split("")) {
                record.add(s.charAt(0));
            }
            List<Integer> group = new ArrayList<>();
            for (String s : recordAndGroup[1].split(",")) {
                group.add(Integer.parseInt(s));
            }
            recordList.add(record);
            groupList.add(group);
        }

        // Solution inspired by: https://github.com/jonathanpaulson/AdventOfCode/blob/master/2023/12.py
        System.out.printf("Answer for part one: %d%n", calculatePartOne());
        System.out.printf("Answer for part two: %d%n", calculatePartTwo());

        reader.close();
    }
}
