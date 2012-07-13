package org.giccon.algorithms.challenges;

/* Simple algorithm */

import java.text.DecimalFormat;
import java.util.*;

public class CDVII {
    public static void main(String[] args) {
        CDVII.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int noTestCases = Integer.parseInt(sc.nextLine());
        sc.nextLine(); // read in the blank line.

        for (int t = 0; t < noTestCases; t++) {
            // Read in the tolls.
            List<Toll> tolls = parseTolls(sc.nextLine());
            // Licenses (keys) will be stored in alphabetical order.
            Map<String, List<TravelData>> travelData = new TreeMap<String, List<TravelData>>();
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                if (s.isEmpty()) {
                    // End of in-between case. Print the result.
                    processOutput(tolls, travelData);

                    if (t < noTestCases - 1) {
                        System.out.println();
                    }
                    break;
                } else {
                    // Read in the travel data.
                    parseTravelData(s, travelData);
                }
            }
            if (t == noTestCases - 1) {
                // End of last case. Print the result.
                processOutput(tolls, travelData);
            }
        }
    }

    private static void processOutput(List<Toll> tolls, Map<String, List<TravelData>> travelData) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<TravelData>> entry : travelData.entrySet()) {
            List<TravelData> l = entry.getValue();
            Collections.sort(l);

            int totalPriceInCents = calcTotalPriceInCents(tolls, l);
            if (totalPriceInCents == 0) {
                continue;
            }

            // Format and store the result.
            sb.append(entry.getKey()).append(" ").append("$");
            DecimalFormat df = new DecimalFormat("0.00");
            sb.append(df.format((double) totalPriceInCents / 100.0)).append("\n");
        }

        // Print the result.
        System.out.print(sb.toString());
    }

    private static int calcTotalPriceInCents(List<Toll> tolls, List<TravelData> l) {
        Iterator<TravelData> itr = l.iterator();

        int totalPriceInCents = 0;
        int noTrips = 0;
        TravelData enterTD = null;
        while (itr.hasNext()) {
            TravelData exitTD = itr.next();
            if (exitTD.isEnter()) {
                // Store the first enter travel data or ignore the previous enter travel data.
                enterTD = exitTD;
                continue;
            }

            if (enterTD == null) {
                // Exit travel data without enter travel data.
                continue;
            }

            // Calculate the price in cents based on the absolute traveled distance.
            int traveledDist = Math.abs(exitTD.getLocation() - enterTD.getLocation());
            int hour = enterTD.getTime().getHour();
            totalPriceInCents += (tolls.get(hour).getPrice() * traveledDist);
            noTrips++;

            enterTD = null;
        }

        if (noTrips == 0) {
            return 0;
        }

        // Add 100 cents per trip charge plus 200 cents for account charge.
        return totalPriceInCents + noTrips * 100 + 200;
    }

    private static void parseTravelData(String s, Map<String, List<TravelData>> travelData) {
        StringTokenizer st = new StringTokenizer(s);
        String license = st.nextToken();
        String dateTime = st.nextToken();
        String action = st.nextToken();
        boolean isEnter = action.equals("enter");
        int location = Integer.parseInt(st.nextToken());

        List<TravelData> l = travelData.get(license);
        if (l == null) {
            l = new LinkedList<TravelData>();
            travelData.put(license, l);
        }

        st = new StringTokenizer(dateTime, ":");
        Date date = new Date(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        Time time = new Time(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        l.add(new TravelData(date, time, location, isEnter));
    }

    private static List<Toll> parseTolls(String s) {
        List<Toll> tolls = new ArrayList<Toll>();
        StringTokenizer st = new StringTokenizer(s);
        int index = 0;
        while (st.hasMoreTokens()) {
            Toll toll = new Toll(new Time(index, 0), new Time(index, 59), Integer.parseInt(st.nextToken()));
            tolls.add(toll);
            index++;
        }
        return tolls;
    }

    private static class TravelData implements Comparable<TravelData> {
        private Date date;
        private Time time;
        private int location;
        private boolean isEnter;

        public TravelData(Date date, Time time, int location, boolean isEnter) {
            this.date = date;
            this.time = time;
            this.location = location;
            this.isEnter = isEnter;
        }

        public Time getTime() {
            return time;
        }

        public int getLocation() {
            return location;
        }

        @Override
        public int compareTo(TravelData o) {
            if (o == null) {
                return -1;
            }

            int cmp = date.compareTo(o.date);
            if (cmp != 0) {
                return cmp;
            }

            return time.compareTo(o.time);
        }

        public boolean isEnter() {
            return isEnter;
        }
    }

    private static class Date implements Comparable<Date> {
        private int month;
        private int day;

        public Date(int day, int month) {
            this.day = day;
            this.month = month;
        }

        @Override
        public int compareTo(Date o) {
            if (o == null) {
                return -1;
            }

            if (month < o.month) {
                return -1;
            } else if (month > o.month) {
                return 1;
            }

            if (day < o.day) {
                return -1;
            } else if (day > o.day) {
                return 1;
            }

            return 0;
        }
    }

    private static class Time implements Comparable<Time> {
        private int hour;
        private int minute;

        public Time(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public int getHour() {
            return hour;
        }

        @Override
        public int compareTo(Time o) {
            if (o == null) {
                return -1;
            }

            if (hour < o.hour) {
                return -1;
            } else if (hour > o.hour) {
                return 1;
            }

            if (minute < o.minute) {
                return -1;
            } else if (minute > o.minute) {
                return 1;
            }

            return 0;
        }
    }

    private static class Toll implements Comparable<Toll> {
        private Time start;
        private Time end;
        private int price;

        public Toll(Time start, Time end, int price) {
            this.start = start;
            this.end = end;
            this.price = price;
        }

        @Override
        public int compareTo(Toll o) {
            if (o == null) {
                return -1;
            }

            return end.compareTo(o.start);
        }

        public int getPrice() {
            return price;
        }
    }
}
