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

            // four scenarios for comparing trio interval against [x, y]
            // scenario 1: a < x and b < y
            if (a < x && b < y) {
                // map range: [x, b]: treat x or b as the currSeed
                long diff = x - a;
                long mappedStart = trio.dest + diff;
                diff = b - a;
                long mappedEnd = trio.dest + diff;
                mappedIntervals.add(new long[]{ mappedStart, mappedEnd });
                x = b;
            }

            // scenario 2: a < x and b >= y
            if (a < x && b >= y) {
                // map range: [x, y]: treat x or y as the currSeed
                long diff = x - a;
                long mappedStart = trio.dest + diff;
                long mappedEnd = trio.dest + diff;
                mappedIntervals.add(new long[]{ mappedStart, mappedEnd });
                if (x == 12950548) {
                    System.out.println("mapped start: " + mappedStart + ", mapped end: " + mappedEnd);
                }
                return mappedIntervals; // no more intervals to map
            }

            // scenario 3: a > x and b < y
            if (a >= x && b < y) {
                // map range: [a, b]: treat a or b as the currSeed
                long mappedStart = trio.dest; // (a - a)
                long diff = b - a;
                long mappedEnd = trio.dest + diff;
                mappedIntervals.add(new long[]{ mappedStart, mappedEnd });
                if (a > x) {
                    mappedIntervals.add(new long[]{ x, a - 1 }); // add unmapped interval
                }
                x = b;
            }

            // scenario 4: a > x and b >= y
            if (a >= x && b >= y) {
                // map range: [a, y]: treat a or y as the currSeed
                long mappedStart = trio.dest; // (a - a)
                long diff = y - a;
                long mappedEnd = trio.dest + diff;
                mappedIntervals.add(new long[]{ mappedStart, mappedEnd });
                if (a > x) {
                    mappedIntervals.add(new long[]{ x, a - 1 }); // add unmapped interval
                }
                return mappedIntervals; // no more intervals to map
            }
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
            System.out.println("new seed");
            for (List<Trio> trioList : listOfMaps) {
                List<long[]> newIntervals = new ArrayList<>(); // this stores all newly mapped intervals with a new map
            //    System.out.println("old intervals length: " + intervals.size());
                for (long[] interval : intervals) {
                    // break down interval into multiple intervals based on map and add to new intervals
                    List<long[]> mappedIntervals = generateMappedIntervals(interval, trioList);
                    for (long[] mappedInterval : mappedIntervals) {
            //            System.out.println(Arrays.toString(mappedInterval));
                        newIntervals.add(mappedInterval);
                    }
                }
             //   System.out.println("new intervals length: " + newIntervals.size());
                // replace intervals with new intervals and repeat
                intervals = new ArrayList<>(newIntervals);
            }
            // check through intervals for the current seed range for minimum value by looking at the left of every interval
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
            System.out.println(trioList);
            mapCount++;
        }
        System.out.printf("Answer for part one: %d%n", calculatePartOne());
        System.out.printf("Answer for part two: %d%n", calculatePartTwo());

        reader.close();
    }
}
