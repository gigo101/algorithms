package org.giccon.algorithms.challenges;

/* Solution: ad hoc */

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class CryptKicker2 {
    private static class Line {
        private List<String> words = new LinkedList<String>();
        private int size;

        public Line(String s) {
            size = s.length();
            StringTokenizer st = new StringTokenizer(s);
            while (st.hasMoreTokens()) {
                words.add(st.nextToken());
            }
        }

        public String decrypt(Map<Character, Character> dict) {
            StringBuilder sb = new StringBuilder();
            Iterator<String> it = words.iterator();
            while (it.hasNext()) {
                String w = it.next();
                for (int i = 0; i < w.length(); ++i) {
                    sb.append(dict.get(w.charAt(i)));
                }
                if (it.hasNext()) {
                    sb.append(" ");
                }
            }

            return sb.toString();
        }

        public Map<Character, Character> getDict(Line line) {
            if (size != line.size || words.size() != line.words.size()) {
                return null;
            }

            Map<Character, Character> dict = new HashMap<Character, Character>();
            Iterator<String> it1 = words.iterator();
            Iterator<String> it2 = line.words.iterator();

            /*
             * Check whether the words in line map to these words.
             * If so, then use the mapping as a dictionary for
             * decryption purposes.
             */
            while (it1.hasNext()) {
                String w1 = it1.next();
                String w2 = it2.next();
                if (w1.length() != w2.length()) {
                    return null;
                }

                for (int i = 0; i < w1.length(); ++i) {
                    char c1 = w1.charAt(i);
                    char c2 = w2.charAt(i);
                    if (!dict.containsKey(c1)) {
                        dict.put(c1, c2);
                    } else if (dict.get(c1) != c2) {
                        return null;
                    }
                }
            }

            return dict;
        }
    }

    public static void main(String[] args) {
        CryptKicker2.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int nTestCases = Integer.parseInt(sc.nextLine());
        sc.nextLine(); // process the empty line.

        Line decryptedLine = new Line(
                "the quick brown fox jumps over the lazy dog");

        for (int t = 0; t < nTestCases; ++t) {
            List<Line> lines = new LinkedList<Line>();
            Map<Character, Character> dict = null;

            // Read in the encrypted lines.
            while (sc.hasNextLine()) {
                String input = sc.nextLine();
                if (input.isEmpty()) {
                    break;
                }

                Line line = new Line(input);
                lines.add(line);

                if (dict == null) {
                    dict = line.getDict(decryptedLine);
                }
            }

            // Decrypt the lines if possible.
            if (dict == null) {
                System.out.println("No solution.");
            } else {
                for (Line l : lines) {
                    System.out.println(l.decrypt(dict));
                }
            }

            if (t < nTestCases - 1) {
                System.out.println();
            }
        }
    }
}
