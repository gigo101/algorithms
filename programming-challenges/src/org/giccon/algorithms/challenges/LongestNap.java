package org.giccon.algorithms.challenges;

/* Simple algorithm */

import java.util.*;

public class LongestNap {

    private static class Duration implements Comparable<Duration> {
        private int hours;
        private int minutes;

        public Duration(int hours, int minutes) {
            this.hours = hours;
            this.minutes = minutes;
        }

        public int getHours() {
            return hours;
        }

        public int getMinutes() {
            return minutes;
        }

        @Override
        public int compareTo(Duration o) {
            if (o == null) {
                return -1;
            }

            if (hours < o.hours) {
                return -1;
            } else if (hours > o.hours) {
                return 1;
            }

            if (minutes < o.minutes) {
                return -1;
            } else if (minutes > o.minutes) {
                return 1;
            }

            return 0;
        }
    }

    private static class Time implements Comparable<Time> {
        private int hour;
        private int minute;

        public Time(String time) {
            StringTokenizer st = new StringTokenizer(time, ":");

            this.hour = Integer.parseInt(st.nextToken());
            this.minute = Integer.parseInt(st.nextToken());
        }

        public Duration minus(Time t) {
            int totalMinutes = hour * 60 + minute;
            int totalMinutes2 = t.hour * 60 + t.minute;
            int diff = Math.abs(totalMinutes2 - totalMinutes);
            return new Duration(diff / 60, diff % 60);
        }

        @Override
        public String toString() {
            StringBuilder hourStr = new StringBuilder();
            if (hour < 10) {
                hourStr.append("0");
            }
            hourStr.append(hour);

            StringBuilder minuteStr = new StringBuilder();
            if (minute < 10) {
                minuteStr.append("0");
            }
            minuteStr.append(minute);

            return hourStr.toString() + ":" + minuteStr.toString();
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

    private static class Appointment implements Comparable<Appointment> {
        private Time start;
        private Time end;

        public Appointment(String a) {
            StringTokenizer st = new StringTokenizer(a);
            start = new Time(st.nextToken());
            end = new Time(st.nextToken());
        }

        @Override
        public int compareTo(Appointment o) {
            if (o == null) {
                return -1;
            }

            return start.compareTo(o.start);
        }

        public Time getStart() {
            return start;
        }

        public Time getEnd() {
            return end;
        }
    }

    public static void main(String[] args) {
        LongestNap.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int noTestCases = 0;
        while (sc.hasNextLine()) {
            // Read in and store the appointments.
            noTestCases++;
            int noAppointments = Integer.parseInt(sc.nextLine());
            List<Appointment> appointments = new ArrayList<Appointment>();
            for (int i = 0; i < noAppointments; i++) {
                appointments.add(new Appointment(sc.nextLine()));
            }

            processLongestNap(appointments, noTestCases);
        }
    }

    private static void processLongestNap(List<Appointment> appointments,
                                          int testCase) {
        // Sort the appointments based on start time.
        Collections.sort(appointments);

        Time longestNapStart = new Time("10:00");
        Duration longestNapDuration = new Duration(0, 0);

        if (appointments.size() == 0) {
            System.out
                    .printf("Day #%d: the longest nap starts at %s and will last for 8 hours and 0 minutes.\n",
                            testCase, longestNapStart);
        } else {
            Appointment prevAppointment = new Appointment("10:00 10:00"); // dummy
            appointments.add(new Appointment("18:00 18:00")); // dummy

            // Find the longest nap.
            for (Appointment a : appointments) {
                Duration duration = a.getStart()
                        .minus(prevAppointment.getEnd());
                if (duration.compareTo(longestNapDuration) > 0) {
                    longestNapDuration = duration;
                    longestNapStart = prevAppointment.getEnd();
                }

                prevAppointment = a;
            }

            // Print the longest nap info.
            StringBuilder output = new StringBuilder();
            output.append("Day #").append(testCase)
                    .append(": the longest nap starts at ")
                    .append(longestNapStart);
            output.append(" and will last for ");
            if (longestNapDuration.getHours() > 0) {
                output.append(longestNapDuration.getHours()).append(
                        " hours and ");
            }

            output.append(longestNapDuration.getMinutes()).append(" minutes.");

            System.out.println(output.toString());
        }
    }
}
