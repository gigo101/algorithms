package org.giccon.algorithms.challenges;

/* Solution: ad hoc */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Fmt {
    private interface Blockable {
        public int length();
    }

    private static class Element implements Blockable {
        private String elm;

        public Element(String elm) {
            this.elm = elm;
        }

        @Override
        public String toString() {
            return elm;
        }

        @Override
        public int length() {
            return elm.length();
        }
    }

    private static class Newline implements Blockable {
        private static final char SYMBOL = '\n';
        private static final String OS_SYMBOL = System
                .getProperty("line.separator");

        @Override
        public int length() {
            return 1;
        }

        @Override
        public String toString() {
            return String.valueOf(SYMBOL);
        }
    }

    private static class Space implements Blockable {
        private static final char SYMBOL = ' ';

        @Override
        public int length() {
            return 1;
        }

        @Override
        public String toString() {
            return String.valueOf(SYMBOL);
        }
    }

    private static final int MAXCHARACTERS = 72;

    public static void main(String[] args) {
        Fmt.begin();
    }

    private static void begin() {
        List<Blockable> blocks;

        // Read the input (also newlines).
        InputStream input = System.in;
        OutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[255];
        int n;
        try {
            while ((n = input.read(buffer)) >= 0) {
                output.write(buffer, 0, n);
            }
            output.close();
        } catch (IOException ignore) {
            return;
        }

        String text = output.toString();
        if (!Newline.OS_SYMBOL.equals(String.valueOf(Newline.SYMBOL))) {
            text = text.replaceAll(Newline.OS_SYMBOL,
                    String.valueOf(Newline.SYMBOL));
        }

        /*
         * Convert the input into blocks for easy parsing. Each block can be
         * either a sentence element, space or newline.
         */
        blocks = convertToBlocks(text);

        // Output the blocks into the correct representation.
        String line = "";
        for (int i = 0; i < blocks.size(); ++i) {
            Blockable b = blocks.get(i);
            if (b instanceof Element) {
                Element elm = (Element) b;
                if (line.length() + elm.length() <= MAXCHARACTERS) {
                    // Append the element to the line.
                    line += elm;
                } else if (line.isEmpty() && elm.length() > MAXCHARACTERS) {
                    // Element is longer than MAXCHARACTERS and must not be
                    // broken.
                    line += elm;
                } else if (line.length() + elm.length() > MAXCHARACTERS) {
                    // Don't append the line. Remove trailing spaces and output
                    // the line. Make sure to go back in the list of blocks.
                    line = removeTrailingSpaces(line);
                    System.out.println(line);
                    line = "";
                    i--;
                }
            } else if (b instanceof Newline) {
                if (line.isEmpty()) {
                    // Don't eliminate a newline when line is empty/blank.
                    System.out.println();
                } else if (line.length() < MAXCHARACTERS) {
                    // Eliminate the newline only when the next block is
                    // an element.
                    if (canEliminateNL(blocks, i + 1, line.length())) {
                        // Newline can be eliminated and must be replaced by
                        // space.
                        line += Space.SYMBOL;
                    } else {
                        // Newline can't be eliminated and hence one must output
                        // it.
                        System.out.println(line);
                        line = "";
                    }
                } else {
                    System.out.println(line);
                    line = "";
                }
            } else if (b instanceof Space) {
                Space t = (Space) b;
                line += t;
            }
        }
        if (line.length() > 0) {
            System.out.print(line);
        }
    }

    private static String removeTrailingSpaces(String line) {
        return line.replaceAll("\\s+$", "");
    }

    private static boolean canEliminateNL(List<Blockable> blocks, int i,
                                          int length) {

        if (i > blocks.size() - 1) {
            return false;
        }

        Blockable b = blocks.get(i);
        if (b instanceof Element) {
            Element elm = (Element) b;
            if (length + elm.length() <= MAXCHARACTERS) {
                return true;
            }
        }

        return false;
    }

    private static List<Blockable> convertToBlocks(String text) {
        List<Blockable> blocks = new ArrayList<Blockable>();

        String elm = "";
        boolean isStartElm = false;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == Newline.SYMBOL) {
                if (isStartElm) {
                    blocks.add(new Element(elm));
                    isStartElm = false;
                    elm = "";
                }
                blocks.add(new Newline());
            } else if (c == Space.SYMBOL) {
                if (isStartElm) {
                    blocks.add(new Element(elm));
                    isStartElm = false;
                    elm = "";
                }
                blocks.add(new Space());
            } else {
                isStartElm = true;
                elm += c;
            }
        }

        if (isStartElm) {
            blocks.add(new Element(elm));
        }

        return blocks;
    }
}
