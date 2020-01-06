package emulator;

import java.util.HashSet;
import java.util.Set;

public class RegistryMappings {
    public static final RegistryMapping[] OP_TO_REG = lookup(
            /* LDI */
            0x20, mapping(Registry.REG_B), 0x30, mapping(Registry.REG_C), 0x40, mapping(Registry.REG_D),
            0x50, mapping(Registry.REG_E), 0x60, mapping(Registry.REG_H), 0x70, mapping(Registry.REG_L),
            0x80, mapping(Registry.REG_HL, true), 0x90, mapping(Registry.REG_A),
            /* LDX */
            0x21, mapping(Registry.REG_BC), 0x31, mapping(Registry.REG_DE), 0x41, mapping(Registry.REG_HL),
            0x22, mapping(Registry.REG_SP),
            /* PUSH */
            0x81, mapping(Registry.REG_B), 0x91, mapping(Registry.REG_C), 0xA1, mapping(Registry.REG_D),
            0xB1, mapping(Registry.REG_E), 0xC1, mapping(Registry.REG_H), 0xD1, mapping(Registry.REG_L),
            0xC0, mapping(Registry.REG_HL, true), 0xD0, mapping(Registry.REG_A),
            /* PUSHW */
            0x51, mapping(Registry.REG_BC), 0x61, mapping(Registry.REG_DE), 0x71, mapping(Registry.REG_HL),
            /* POP */
            0x82, mapping(Registry.REG_B), 0x92, mapping(Registry.REG_C), 0xA2, mapping(Registry.REG_D),
            0xB2, mapping(Registry.REG_E), 0xC2, mapping(Registry.REG_H), 0xD2, mapping(Registry.REG_L),
            0xC3, mapping(Registry.REG_HL, true), 0xD3, mapping(Registry.REG_A),
            /* POPW */
            0x52, mapping(Registry.REG_BC), 0x62, mapping(Registry.REG_DE), 0x72, mapping(Registry.REG_HL),
            /* MOV */
            0x09, mapping(Registry.REG_B, Registry.REG_B), 0x19, mapping(Registry.REG_B, Registry.REG_C),
                0x29, mapping(Registry.REG_B, Registry.REG_D), 0x39, mapping(Registry.REG_B, Registry.REG_E),
                0x49, mapping(Registry.REG_B, Registry.REG_H), 0x59, mapping(Registry.REG_B, Registry.REG_L),
                0x69, mapping(Registry.REG_B, Registry.REG_HL, true), 0x79, mapping(Registry.REG_B, Registry.REG_A),
            0x89, mapping(Registry.REG_C, Registry.REG_B), 0x99, mapping(Registry.REG_C, Registry.REG_C),
                0xA9, mapping(Registry.REG_C, Registry.REG_D), 0xB9, mapping(Registry.REG_C, Registry.REG_E),
                0xC9, mapping(Registry.REG_C, Registry.REG_H), 0xD9, mapping(Registry.REG_C, Registry.REG_L),
                0xE9, mapping(Registry.REG_C, Registry.REG_HL, true), 0xF9, mapping(Registry.REG_C, Registry.REG_A),
            0x0A, mapping(Registry.REG_D, Registry.REG_B), 0x1A, mapping(Registry.REG_D, Registry.REG_C),
                0x2A, mapping(Registry.REG_D, Registry.REG_D), 0x3A, mapping(Registry.REG_D, Registry.REG_E),
                0x4A, mapping(Registry.REG_D, Registry.REG_H), 0x5A, mapping(Registry.REG_D, Registry.REG_L),
                0x6A, mapping(Registry.REG_D, Registry.REG_HL, true), 0x7A, mapping(Registry.REG_D, Registry.REG_A),
            0x8A, mapping(Registry.REG_E, Registry.REG_B), 0x9A, mapping(Registry.REG_E, Registry.REG_C),
                0xAA, mapping(Registry.REG_E, Registry.REG_D), 0xBA, mapping(Registry.REG_E, Registry.REG_E),
                0xCA, mapping(Registry.REG_E, Registry.REG_H), 0xDA, mapping(Registry.REG_E, Registry.REG_L),
                0xEA, mapping(Registry.REG_E, Registry.REG_HL, true), 0xFA, mapping(Registry.REG_E, Registry.REG_A),
            0x0B, mapping(Registry.REG_H, Registry.REG_B), 0x1B, mapping(Registry.REG_H, Registry.REG_C),
                0x2B, mapping(Registry.REG_H, Registry.REG_D), 0x3B, mapping(Registry.REG_H, Registry.REG_E),
                0x4B, mapping(Registry.REG_H, Registry.REG_H), 0x5B, mapping(Registry.REG_H, Registry.REG_L),
                0x6B, mapping(Registry.REG_H, Registry.REG_HL, true), 0x7B, mapping(Registry.REG_H, Registry.REG_A),
            0x8B, mapping(Registry.REG_L, Registry.REG_B), 0x9B, mapping(Registry.REG_L, Registry.REG_C),
                0xAB, mapping(Registry.REG_L, Registry.REG_D), 0xBB, mapping(Registry.REG_L, Registry.REG_E),
                0xCB, mapping(Registry.REG_L, Registry.REG_H), 0xDB, mapping(Registry.REG_L, Registry.REG_L),
                0xEB, mapping(Registry.REG_L, Registry.REG_HL, true), 0xFB, mapping(Registry.REG_L, Registry.REG_A),
            0x0C, mapping(Registry.REG_HL, true, Registry.REG_B), 0x1C, mapping(Registry.REG_HL, true, Registry.REG_C),
                0x2C, mapping(Registry.REG_HL, true, Registry.REG_D), 0x3C, mapping(Registry.REG_HL, true, Registry.REG_E),
                0x4C, mapping(Registry.REG_HL, true, Registry.REG_H), 0x5C, mapping(Registry.REG_HL, true, Registry.REG_L),
                /* 0X6C is HCF */ 0x7C, mapping(Registry.REG_HL, true, Registry.REG_A),
            0x8C, mapping(Registry.REG_A, Registry.REG_B), 0x9C, mapping(Registry.REG_A, Registry.REG_C),
                0xAC, mapping(Registry.REG_A, Registry.REG_D), 0xBC, mapping(Registry.REG_A, Registry.REG_E),
                0xCC, mapping(Registry.REG_A, Registry.REG_H), 0xDC, mapping(Registry.REG_A, Registry.REG_L),
                0xEC, mapping(Registry.REG_A, Registry.REG_HL, true), 0xFC, mapping(Registry.REG_A, Registry.REG_A),
            /* MOVW */
            0xED, mapping(Registry.REG_HL, Registry.REG_BC), 0xFD, mapping(Registry.REG_HL, Registry.REG_DE),
            /* CLEARFLAG */
            0x08, mapping(),
            /* SETFLAG */
            0x18, mapping(Registry.FLAG_ZERO, true), 0x28, mapping(Registry.FLAG_ZERO),
                0x38, mapping(Registry.FLAG_NEGATIVE, true), 0x48, mapping(Registry.FLAG_NEGATIVE),
                0x58, mapping(Registry.FLAG_HALF_CARRY, true), 0x68, mapping(Registry.FLAG_HALF_CARRY),
                0x78, mapping(Registry.FLAG_CARRY, true), 0x88, mapping(Registry.FLAG_CARRY),
            /* ADD */
            0x04, mapping(Registry.REG_B), 0x14, mapping(Registry.REG_C), 0x24, mapping(Registry.REG_D),
                0x34, mapping(Registry.REG_E), 0x44, mapping(Registry.REG_H), 0x54, mapping(Registry.REG_L),
                0x64, mapping(Registry.REG_HL, true), 0x74, mapping(Registry.REG_A),
            /* ADDI */
            0xA7, mapping(Registry.REG_A),
            /* ADDW */
            0x83, mapping(Registry.REG_BC), 0x93, mapping(Registry.REG_DE), 0xA3, mapping(Registry.REG_HL),
            /* SUB */
            0x84, mapping(Registry.REG_B), 0x94, mapping(Registry.REG_C), 0xA4, mapping(Registry.REG_D),
                0xB4, mapping(Registry.REG_E), 0xC4, mapping(Registry.REG_H), 0xD4, mapping(Registry.REG_L),
                0xE4, mapping(Registry.REG_HL, true), 0xF4, mapping(Registry.REG_A),
            /* SUBI */
            0xB7, mapping(Registry.REG_A),
            /* INC */
            0x03, mapping(Registry.REG_B), 0x13, mapping(Registry.REG_C), 0x23, mapping(Registry.REG_D),
                0x33, mapping(Registry.REG_E), 0x43, mapping(Registry.REG_H), 0x53, mapping(Registry.REG_L),
                0x63, mapping(Registry.REG_HL, true), 0x73, mapping(Registry.REG_A),
            /* INCW */
            0xA8, mapping(Registry.REG_BC), 0xB8, mapping(Registry.REG_DE), 0xC8, mapping(Registry.REG_HL),
            /* DEC */
            0x07, mapping(Registry.REG_B), 0x17, mapping(Registry.REG_C), 0x27, mapping(Registry.REG_D),
                0x37, mapping(Registry.REG_E), 0x47, mapping(Registry.REG_H), 0x57, mapping(Registry.REG_L),
                0x67, mapping(Registry.REG_HL, true), 0x77, mapping(Registry.REG_A),
            /* AND */
            0x05, mapping(Registry.REG_B), 0x15, mapping(Registry.REG_C), 0x25, mapping(Registry.REG_D),
                0x35, mapping(Registry.REG_E), 0x45, mapping(Registry.REG_H), 0x55, mapping(Registry.REG_L),
                0x65, mapping(Registry.REG_HL, true), 0x75, mapping(Registry.REG_A),
            /* ANDI */
            0xC7, mapping(Registry.REG_A),
            /* OR */
            0x85, mapping(Registry.REG_B), 0x95, mapping(Registry.REG_C), 0xA5, mapping(Registry.REG_D),
                0xB5, mapping(Registry.REG_E), 0xC5, mapping(Registry.REG_H), 0xD5, mapping(Registry.REG_L),
                0xE5, mapping(Registry.REG_HL, true), 0xF5, mapping(Registry.REG_A),
            /* ORI */
            0xD7, mapping(Registry.REG_A),
            /* XOR */
            0x06, mapping(Registry.REG_B), 0x16, mapping(Registry.REG_C), 0x26, mapping(Registry.REG_D),
                0x36, mapping(Registry.REG_E), 0x46, mapping(Registry.REG_H), 0x56, mapping(Registry.REG_L),
                0x66, mapping(Registry.REG_HL, true), 0x76, mapping(Registry.REG_A),
            /* XORI */
            0xE7, mapping(Registry.REG_A),
            /* CMP */
            0x86, mapping(Registry.REG_B), 0x96, mapping(Registry.REG_C), 0xA6, mapping(Registry.REG_D),
                0xB6, mapping(Registry.REG_E), 0xC6, mapping(Registry.REG_H), 0xD6, mapping(Registry.REG_L),
                0xE6, mapping(Registry.REG_HL, true), 0xF6, mapping(Registry.REG_A),
            /* CMPI */
            0xF7, mapping(Registry.REG_A),
            /* CMPS */
            0x0D, mapping(Registry.REG_B), 0x1D, mapping(Registry.REG_C), 0x2D, mapping(Registry.REG_D),
            0x3D, mapping(Registry.REG_E), 0x4D, mapping(Registry.REG_H), 0x5D, mapping(Registry.REG_L),
            0x6D, mapping(Registry.REG_HL, true), 0x7D, mapping(Registry.REG_A),
            /* SIN */
            0xE0, mapping(Registry.REG_A),
            /* SOUT */
            0xE1, mapping(Registry.REG_A), 0xE2, mapping(Registry.REG_B), 0xE3, mapping(Registry.REG_C),
                0xE8, mapping(Registry.REG_D), 0xF2, mapping(Registry.REG_E), 0xF3, mapping(Registry.REG_H),
                0xF8, mapping(Registry.REG_L), 0x01, mapping(Registry.REG_BC), 0x02, mapping(Registry.REG_DE),
                0x10, mapping(Registry.REG_HL), 0x11, mapping(Registry.REG_SP), 0x12, mapping(),
                0x42, mapping(Registry.REG_HL, true), 0x3E, mapping(),
            /* CLRSCR */
            0xF0, mapping(),
            /* DRAW */
            0xF1, mapping(),
            /* JMP */
            0x0F, mapping(),
            /* JMPC */
            0x1F, mapping(Registry.FLAG_ZERO, true), 0x2F, mapping(Registry.FLAG_ZERO),
                0x3F, mapping(Registry.FLAG_NEGATIVE, true), 0x4F, mapping(Registry.FLAG_NEGATIVE),
                0x5F, mapping(Registry.FLAG_HALF_CARRY, true), 0x6F, mapping(Registry.FLAG_HALF_CARRY),
                0x7F, mapping(Registry.FLAG_CARRY, true), 0x8F, mapping(Registry.FLAG_CARRY),
            /* JMPN */
            0x9F, mapping(),
            /* JMPNC */
            0xAF, mapping(Registry.FLAG_ZERO, true), 0xBF, mapping(Registry.FLAG_ZERO),
                0xCF, mapping(Registry.FLAG_NEGATIVE, true), 0xDF, mapping(Registry.FLAG_NEGATIVE),
                0xEF, mapping(Registry.FLAG_HALF_CARRY, true), 0xFF, mapping(Registry.FLAG_HALF_CARRY),
                0xEE, mapping(Registry.FLAG_CARRY, true), 0xFE, mapping(Registry.FLAG_CARRY),
            /* CALL */
            0x1E, mapping(),
            /* RET */
            0x0E, mapping(),
            /* NOP */
            0x00, mapping(),
            /* HCF */
            0x6C, mapping()
    );

    private static RegistryMapping[] lookup(Object... values) {int maxOp = 0;
        Set<Integer> seenOpCodes = new HashSet<>();
        for (int i = 0; i < values.length; i += 2) {
            int opCode = (int) values[i];
            if (!seenOpCodes.add(opCode)) {
                throw new IllegalArgumentException("Cannot have duplicated op-codes: 0x" + Integer.toString(opCode, 16));
            }
            maxOp = Math.max(maxOp, opCode);
        }

        RegistryMapping[] table = new RegistryMapping[maxOp + 1];
        for (int i = 0; i < values.length; i += 2) {
            table[(int) values[i]] = (RegistryMapping) values[i + 1];
        }
        return table;
    }

    private static RegistryMapping mapping() {
        return mapping(0, false);
    }

    private static RegistryMapping mapping(int reg) {
        return mapping(reg, false);
    }

    private static RegistryMapping mapping(int reg, boolean regIndirect) {
        return mapping(reg, regIndirect, 0);
    }

    private static RegistryMapping mapping(int reg1, boolean reg1Indirect, int reg2) {
        return mapping(reg1, reg1Indirect, reg2, false);
    }

    private static RegistryMapping mapping(int reg1, int reg2) {
        return mapping(reg1, reg2, false);
    }

    private static RegistryMapping mapping(int reg1, int reg2, boolean reg2Indirect) {
        return new RegistryMapping(reg1, false, reg2, reg2Indirect);
    }

    private static RegistryMapping mapping(int reg1, boolean reg1Indirect, int reg2, boolean reg2Indirect) {
        return new RegistryMapping(reg1, reg1Indirect, reg2, reg2Indirect);
    }

    public static class RegistryMapping {
        public final int r1;
        public final boolean r1Indirect;
        public final int r2;
        public final boolean r2Indirect;

        public RegistryMapping(int r1, boolean r1Indirect, int r2, boolean r2Indirect) {
            this.r1 = r1;
            this.r1Indirect = r1Indirect;
            this.r2 = r2;
            this.r2Indirect = r2Indirect;
        }
    }
}
