import java.io.*;
import java.util.*;

class Trio {
    long dest;
    long source;
    long range;

    Trio(long dest, long source, long range) {
        this.dest = dest;
        this.source = source;
        this.range = range;
    }

    public String toString() {
        return String.format("Source: %d, Dest: %d, Range: %d", source, dest, range);
    }
}

public class Day5 {
    private static int MAPS = 7;
    private static List<Long> seeds = new ArrayList<>();
    private static List<List<Trio>> listOfMaps = new ArrayList<>();

    private static long calculatePartOne() {
        long min = Long.MAX_VALUE;
        for (long seed : seeds) {
            long currSeed = seed;
            for (List<Trio> trioList : listOfMaps) {
                for (Trio trio : trioList) {
                    long rightEnd = trio.source + trio.range;
                    if (currSeed >= trio.source && currSeed <= rightEnd) {
                        long diff = currSeed - trio.source;
                        currSeed = trio.dest + diff;
                        break;
                    }
                }
            }
            min = Math.min(currSeed, min);
        }
        return min;
    }

    private static List<long[]> generateMappedIntervals(long[] interval, List<Trio> trioList) {
        // let [x, y] be the range for the interval in the list of intervals to be broken down further
        long x = interval[0];
        long y = interval[1];
        List<long[]> mappedIntervals = new ArrayList<>();
        for (Trio trio : trioList) {
            // let [a, b] be the range for the interval in the map to compare against [x, y]
            long a = trio.source;
            long b = trio.source + trio.range;
            // no more intervals can be found
            if (a > y) {
                break;
            }

            // find next interval if b is smaller than x
            if (b < x) {
                continue; 
            }

            // overlapping interval confirmed
            long leftEnd = Math.max(a, x);
            long rightEnd = Math.min(b, y);
            // map range: [leftEnd, rightEnd]: treat left or right as the currSeed
            long mappedStart = trio.dest + leftEnd - a;
            long mappedEnd = trio.dest + rightEnd - a;
            mappedIntervals.add(new long[]{ mappedStart, mappedEnd });
            // add unmapped intervals if present
            if (a > x) {
                mappedIntervals.add(new long[]{ x, a - 1 });
            }
            x = b; // update left interval
        }
        // add last unmapped interval
        mappedIntervals.add(new long[]{ x, y });
        return mappedIntervals;
    }

    private static long calculatePartTwo() {
        long min = Long.MAX_VALUE;
        // deal with intervals...
        for (int i = 0; i < seeds.size(); i += 2) {
            long start = seeds.get(i);
            long end = start + seeds.get(i + 1);
            List<long[]> intervals = new ArrayList<>();
            intervals.add(new long[]{ start, end });
            System.out.println("Seed start: " + start + ", number of values: " + seeds.get(i + 1));
            for (List<Trio> trioList : listOfMaps) {
                List<long[]> newIntervals = new ArrayList<>(); // this stores all newly mapped intervals with a new map
                for (long[] interval : intervals) {
                    // break down interval into multiple intervals based on map and add to new intervals
                    List<long[]> mappedIntervals = generateMappedIntervals(interval, trioList);
                    for (long[] mappedInterval : mappedIntervals) {
                        newIntervals.add(mappedInterval);
                    }
                }
                // replace intervals with new intervals and repeat
                intervals = new ArrayList<>(newIntervals);
            }
            // check through intervals for the current seed range for minimum value by looking at the left of every interval
            System.out.println("Number of intervals: " + intervals.size());
            for (long[] interval : intervals) {
                min = Math.min(interval[0], min);
            }
        }
        return min;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "../inputs/day5.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String currLine;
        currLine = reader.readLine();
        String[] splitted = currLine.split(":"); // ["seeds", remaining...]
        String seedsToParse = splitted[1].trim();
        for (String seed : seedsToParse.split("\\s+")) {
            seeds.add(Long.parseLong(seed));
        }
        reader.readLine();

        // decipher maps (total of 7)
        int mapCount = 0;
        while (mapCount < MAPS) {
            reader.readLine(); // reads map name
            
            List<Trio> trioList = new ArrayList<>();
            while ((currLine = reader.readLine()) != null) {
                if (currLine.length() == 0) {
                    break;
                }
                String[] trio = currLine.split("\\s+");

                long dest = Long.parseLong(trio[0]);
                long source = Long.parseLong(trio[1]);
                long range = Long.parseLong(trio[2]);
                Trio curr = new Trio(dest, source, range);
                trioList.add(curr);
            }
            // sort by source for part 2
            Collections.sort(trioList, (x, y) -> Long.compare(x.source, y.source));
            listOfMaps.add(trioList);
            mapCount++;
        }
        System.out.printf("Answer for part one: %d%n", calculatePartOne());
        System.out.printf("Answer for part two: %d%n", calculatePartTwo());

        reader.close();
    }
}
