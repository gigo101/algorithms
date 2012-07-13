package org.giccon.algorithms.challenges;

/* Simple algorithm */

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Wertyu {
    private static final String[] KEYBOARD = {"`1234567890-=",
            "QWERTYUIOP[]\\", "ASDFGHJKL;'", "ZXCVBNM,./"};

    public static void main(String[] args) {
        Wertyu.begin();
    }

    private static void begin() {
        Map<Character, Character> c2cMap = new HashMap<Character, Character>();
        // Setup the character to character map.
        for (String row : KEYBOARD) {
            for (int i = 1; i < row.length(); ++i) {
                c2cMap.put(row.charAt(i), row.charAt(i - 1));
            }
        }
        c2cMap.put(' ', ' ');

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            StringBuffer output = new StringBuffer();
            // Correct the input.
            for (int i = 0; i < line.length(); ++i) {
                char c = line.charAt(i);
                if (c2cMap.containsKey(c)) {
                    output.append(c2cMap.get(c));
                }
            }
            System.out.println(output);
        }
    }
}
