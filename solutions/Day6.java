import java.io.*;
import java.util.*;

public class Day6 {
    private static int calculatePartOne(List<Integer> times, List<Integer> distances) {
        int ans = 1;
        for (int i = 0; i < times.size(); i++) {
            int time = times.get(i);
            int distance = distances.get(i);
            int ways = 0;
            for (int speed = 1; speed < time; speed++) {
                int travelTime = time - speed;
                int actualDistance = travelTime * speed;
                if (actualDistance > distance) {
                    ways++;
                }
            }
            ans *= ways;
        }
        return ans;
    }

    private static long calculatePartTwo(long time, long distance) {
        long ans = 0;
        for (long speed = 1; speed < time; speed++) {
            long travelTime = time - speed;
            long actualDistance = travelTime * speed;
            if (actualDistance > distance) {
                ans++;
            }
        }
        return ans;
    }

    public static void main(String[]args) throws IOException {
        String filepath = "../inputs/day6.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String firstLine = reader.readLine();
        String secondLine = reader.readLine();
        String[] firstLineSplit = firstLine.split(":");
        String[] secondLineSplit = secondLine.split(":");
        String[] timesArr = firstLineSplit[1].trim().split("\\s+");
        String[] distancesArr = secondLineSplit[1].trim().split("\\s+");

        List<Integer> times = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();
        for (int i = 0; i < timesArr.length; i++) {
            times.add(Integer.parseInt(timesArr[i]));
            distances.add(Integer.parseInt(distancesArr[i]));
        }
        System.out.printf("Answer for part one: %d%n", calculatePartOne(times, distances));

        // for part two
        String timeStr = "";
        String distanceStr = "";
        for (String time : timesArr) {
            timeStr += time;
        }
        for (String distance : distancesArr) {
            distanceStr += distance;
        }
        System.out.printf("Answer for part two: %d%n", calculatePartTwo(Long.parseLong(timeStr), Long.parseLong(distanceStr)));

        reader.close();
    }
}