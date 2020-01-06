package emulator;

import java.util.HashSet;
import java.util.Set;

import static emulator.Instructions.*;

public class InstructionMappings {
    public static final int[] OP_TO_INSTRUCTION = lookup(
            0x20, LDI, 0x30, LDI, 0x40, LDI, 0x50, LDI, 0x60, LDI, 0x70, LDI, 0x80, LDI, 0x90, LDI,
            0x21, LDX, 0x31, LDX, 0x41, LDX, 0x22, LDX,
            0x81, PUSH, 0x91, PUSH, 0xA1, PUSH, 0xB1, PUSH, 0xC1, PUSH, 0xD1, PUSH, 0xC0, PUSH, 0xD0, PUSH,
            0x51, PUSHW, 0x61, PUSHW, 0x71, PUSHW,
            0x82, POP, 0x92, POP, 0xA2, POP, 0xB2, POP, 0xC2, POP, 0xD2, POP, 0xC3, POP, 0xD3, POP,
            0x52, POPW, 0x62, POPW, 0x72, POPW,
            0x09, MOV, 0x19, MOV, 0x29, MOV, 0x39, MOV, 0x49, MOV, 0x59, MOV, 0x69, MOV, 0x79, MOV,
                0x89, MOV, 0x99, MOV, 0xA9, MOV, 0xB9, MOV, 0xC9, MOV, 0xD9, MOV, 0xE9, MOV, 0xF9, MOV,
                0x0A, MOV, 0x1A, MOV, 0x2A, MOV, 0x3A, MOV, 0x4A, MOV, 0x5A, MOV, 0x6A, MOV, 0x7A, MOV,
                0x8A, MOV, 0x9A, MOV, 0xAA, MOV, 0xBA, MOV, 0xCA, MOV, 0xDA, MOV, 0xEA, MOV, 0xFA, MOV,
                0x0B, MOV, 0x1B, MOV, 0x2B, MOV, 0x3B, MOV, 0x4B, MOV, 0x5B, MOV, 0x6B, MOV, 0x7B, MOV,
                0x8B, MOV, 0x9B, MOV, 0xAB, MOV, 0xBB, MOV, 0xCB, MOV, 0xDB, MOV, 0xEB, MOV, 0xFB, MOV,
                0x0C, MOV, 0x1C, MOV, 0x2C, MOV, 0x3C, MOV, 0x4C, MOV, 0x5C, MOV,            0x7C, MOV,
                0x8C, MOV, 0x9C, MOV, 0xAC, MOV, 0xBC, MOV, 0xCC, MOV, 0xDC, MOV, 0xEC, MOV, 0xFC, MOV,
            0xED, MOVW, 0xFD, MOVW,
            0x08, CLEARFLAG,
            0x18, SETFLAG, 0x28, SETFLAG, 0x38, SETFLAG, 0x48, SETFLAG, 0x58, SETFLAG, 0x68, SETFLAG, 0x78, SETFLAG, 0x88, SETFLAG,
            0x04, ADD, 0x14, ADD, 0x24, ADD, 0x34, ADD, 0x44, ADD, 0x54, ADD, 0x64, ADD, 0x74, ADD,
            0xA7, ADDI,
            0x83, ADDW, 0x93, ADDW, 0xA3, ADDW,
            0x84, SUB, 0x94, SUB, 0xA4, SUB, 0xB4, SUB, 0xC4, SUB, 0xD4, SUB, 0xE4, SUB, 0xF4, SUB,
            0xB7, SUBI,
            0x03, INC, 0x13, INC, 0x23, INC, 0x33, INC, 0x43, INC, 0x53, INC, 0x63, INC, 0x73, INC,
            0xA8, INCW, 0xB8, INCW, 0xC8, INCW,
            0x07, DEC, 0x17, DEC, 0x27, DEC, 0x37, DEC, 0x47, DEC, 0x57, DEC, 0x67, DEC, 0x77, DEC,
            0x05, AND, 0x15, AND, 0x25, AND, 0x35, AND, 0x45, AND, 0x55, AND, 0x65, AND, 0x75, AND,
            0xC7, ANDI,
            0x85, OR, 0x95, OR, 0xA5, OR, 0xB5, OR, 0xC5, OR, 0xD5, OR, 0xE5, OR, 0xF5, OR,
            0xD7, ORI,
            0x06, XOR, 0x16, XOR, 0x26, XOR, 0x36, XOR, 0x46, XOR, 0x56, XOR, 0x66, XOR, 0x76, XOR,
            0xE7, XORI,
            0x86, CMP, 0x96, CMP, 0xA6, CMP, 0xB6, CMP, 0xC6, CMP, 0xD6, CMP, 0xE6, CMP, 0xF6, CMP,
            0xF7, CMPI,
            0x0D, CMPS, 0x1D, CMPS, 0x2D, CMPS, 0x3D, CMPS, 0x4D, CMPS, 0x5D, CMPS, 0x6D, CMPS, 0x7D, CMPS,
            0xE0, SIN,
            0xE1, SOUT, 0xE2, SOUTB, 0xE3, SOUTC, 0xE8, SOUTD, 0xF2, SOUTE, 0xF3, SOUTH, 0xF8, SOUTL, 0x01, SOUTBC,
            0x02, SOUTDE, 0x10, SOUTHL, 0x11, SOUTSP, 0x12, SOUTM, 0x42, SOUTMHL, 0x3E, SOUTFS,
            0xF0, CLRSCR,
            0xF1, DRAW,
            0x0F, JMP,
            0x1F, JMPC, 0x2F, JMPC, 0x3F, JMPC, 0x4F, JMPC, 0x5F, JMPC, 0x6F, JMPC, 0x7F, JMPC, 0x8F, JMPC,
            0x9F, JMPN,
            0xAF, JMPNC, 0xBF, JMPNC, 0xCF, JMPNC, 0xDF, JMPNC, 0xEF, JMPNC, 0xFF, JMPNC, 0xEE, JMPNC, 0xFE, JMPNC,
            0x1E, CALL,
            0x0E, RET,
            0x00, NOP,
            0x6C, HCF
            );

    private static int[] lookup(int... values) {
        int maxOp = 0;
        Set<Integer> seenOpCodes = new HashSet<>();
        for (int i = 0; i < values.length; i += 2) {
            int opCode = values[i];
            if (!seenOpCodes.add(opCode)) {
                throw new IllegalArgumentException("Cannot have duplicated op-codes: 0x" + Integer.toString(opCode, 16));
            }
            maxOp = Math.max(maxOp, opCode);
        }

        int[] table = new int[maxOp + 1];
        for (int i = 0; i < values.length; i += 2) {
            table[values[i]] = values[i + 1];
        }
        return table;
    }
}
