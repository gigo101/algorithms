package org.giccon.algorithms.challenges;

/* Solution: ad hoc, simulation */

import java.util.Scanner;

public class Interpreter {
    private static enum Opcode {
        GOTO, HALT, SET, ADD, MULTIPLY, SETR, ADDR, MULTIPLYR, SETRRAM, SETRAM;

        public static Opcode getOpcode(int instr) {
            for (Opcode oc : values()) {
                if (instr == oc.ordinal()) {
                    return oc;
                }
            }

            return null;
        }

        public static Opcode extractOpcode(int instr) {
            return getOpcode(instr / 100);
        }

        public static int extractOperand1(int instr) {
            return (instr % 100) / 10;
        }

        public static int extractOperand2(int instr) {
            return instr % 10;
        }
    }

    private static final int NBR_RAM = 1000;
    private static final int NBR_REGISTERS = 10;
    private static final int MAX_NUMBER = 1000;

    private int[] ram = new int[NBR_RAM];
    private int[] registers = new int[NBR_REGISTERS];
    private int pc = 0; // program counter.
    private int nbrExecInstrs = 0; // number of executed instructions.

    public static void main(String[] args) {
        new Interpreter().begin();
    }

    private void begin() {
        Scanner sc = new Scanner(System.in);
        int totalTC = Integer.parseInt(sc.nextLine());
        if (sc.hasNextLine()) {
            // Process the empty string.
            sc.nextLine();
        }

        for (int i = 0; i < totalTC; ++i) {
            loadInstrs(sc);
            processInstrs();
            System.out.println(nbrExecInstrs);
            if (i < totalTC - 1) {
                System.out.println();
            }
            reset();
        }
    }

    private void reset() {
        pc = 0;
        nbrExecInstrs = 0;

        for (int i = 0; i < registers.length; ++i) {
            registers[i] = 0;
        }
    }

    private void processInstrs() {
        Opcode oc;
        for (pc = 0; pc < NBR_RAM; ++pc) {
            oc = Opcode.extractOpcode(ram[pc]);
            nbrExecInstrs++;
            switch (oc) {
                case GOTO:
                    handleGOTO(ram[pc]);
                    break;
                case HALT:
                    return;
                case SET:
                    handleSET(ram[pc]);
                    break;
                case ADD:
                    handleADD(ram[pc]);
                    break;
                case MULTIPLY:
                    handleMULTIPLY(ram[pc]);
                    break;
                case SETR:
                    handleSETR(ram[pc]);
                    break;
                case ADDR:
                    handleADDR(ram[pc]);
                    break;
                case MULTIPLYR:
                    handleMULTIPLYR(ram[pc]);
                    break;
                case SETRRAM:
                    handleSETRRAM(ram[pc]);
                    break;
                case SETRAM:
                    handleSETRAM(ram[pc]);
                    break;
            }
        }
    }

    private void handleSETRAM(int instr) {
        int s = Opcode.extractOperand1(instr);
        int a = Opcode.extractOperand2(instr);

        ram[registers[a]] = registers[s];
    }

    private void handleSETRRAM(int instr) {
        int d = Opcode.extractOperand1(instr);
        int a = Opcode.extractOperand2(instr);

        registers[d] = ram[registers[a]];
    }

    private void handleMULTIPLYR(int instr) {
        int d = Opcode.extractOperand1(instr);
        int s = Opcode.extractOperand2(instr);

        registers[d] *= registers[s];
        registers[d] %= MAX_NUMBER;
    }

    private void handleADDR(int instr) {
        int d = Opcode.extractOperand1(instr);
        int s = Opcode.extractOperand2(instr);

        registers[d] += registers[s];
        registers[d] %= MAX_NUMBER;
    }

    private void handleSETR(int instr) {
        int d = Opcode.extractOperand1(instr);
        int s = Opcode.extractOperand2(instr);

        registers[d] = registers[s];
    }

    private void handleMULTIPLY(int instr) {
        int d = Opcode.extractOperand1(instr);
        int n = Opcode.extractOperand2(instr);

        registers[d] *= n;
        registers[d] %= MAX_NUMBER;
    }

    private void handleADD(int instr) {
        int d = Opcode.extractOperand1(instr);
        int n = Opcode.extractOperand2(instr);

        registers[d] += n;
        registers[d] %= MAX_NUMBER;
    }

    private void handleSET(int instr) {
        int d = Opcode.extractOperand1(instr);
        int n = Opcode.extractOperand2(instr);

        registers[d] = n;
    }

    private void handleGOTO(int instr) {
        int d = Opcode.extractOperand1(instr);
        int s = Opcode.extractOperand2(instr);

        if (registers[s] != 0) {
            pc = registers[d];
            pc--;
        }
    }

    private void loadInstrs(Scanner sc) {
        int i = 0;
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equals("")) {
                break;
            }

            ram[i++] = Integer.parseInt(input);
        }
    }
}
