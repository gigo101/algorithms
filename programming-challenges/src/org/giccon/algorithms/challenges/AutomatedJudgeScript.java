package org.giccon.algorithms.challenges;

/* Solution: ad hoc, simulation */

import java.util.Arrays;
import java.util.Scanner;

public class AutomatedJudgeScript {
    public static void main(String[] args) {
        AutomatedJudgeScript.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);

        int run = 0;
        while (sc.hasNextLine()) {
            run++;

            int nInputs = Integer.parseInt(sc.nextLine());
            if (nInputs == 0) {
                break;
            }

            // Read in the correct lines.
            String[] inputLines = new String[nInputs];
            for (int i = 0; i < nInputs; ++i) {
                inputLines[i] = sc.nextLine();
            }

            // Read in the submitted lines.
            int nOutput = Integer.parseInt(sc.nextLine());
            String[] outputLines = new String[nOutput];
            for (int i = 0; i < nOutput; ++i) {
                outputLines[i] = sc.nextLine();
            }

            // Check whether the answer is correct.
            if (nInputs == nOutput) {
                boolean isAC = true;
                for (int i = 0; i < nInputs; ++i) {
                    if (!inputLines[i].equals(outputLines[i])) {
                        isAC = false;
                        break;
                    }
                }

                if (isAC) {
                    System.out.printf("Run #%d: Accepted\n", run);
                    continue;
                }
            }

            String inputLine = Arrays.toString(inputLines);
            String outputLine = Arrays.toString(outputLines);

            StringBuilder inputNum = new StringBuilder();
            StringBuilder outputNum = new StringBuilder();

            // Extract the digits from the correct lines.
            for (int i = 0; i < inputLine.length(); ++i) {
                char c = inputLine.charAt(i);
                if (Character.isDigit(c)) {
                    inputNum.append(c);
                }
            }

            // Extract the digits from the submitted lines.
            for (int i = 0; i < outputLine.length(); ++i) {
                char c = outputLine.charAt(i);
                if (Character.isDigit(c)) {
                    outputNum.append(c);
                }
            }

            if (inputNum.toString().equals(outputNum.toString())) {
                System.out.printf("Run #%d: Presentation Error\n", run);
            } else {
                System.out.printf("Run #%d: Wrong Answer\n", run);
            }
        }
    }
}
