package emulator;

public class Registry {
    public static final int REG_A = 0xFFFF0;
    public static final int REG_B = 0xFF001;
    public static final int REG_C = 0x00FF1;
    public static final int REG_D = 0xFF002;
    public static final int REG_E = 0x00FF2;
    public static final int REG_H = 0xFF003;
    public static final int REG_L = 0x00FF3;

    public static final int REG_BC = 0xFFFF1;
    public static final int REG_DE = 0xFFFF2;
    public static final int REG_HL = 0xFFFF3;

    public static final int REG_SP = 0xFFFF4;

    public static final int FLAG_ZERO = 7;
    public static final int FLAG_NEGATIVE = 6;
    public static final int FLAG_HALF_CARRY = 5;
    public static final int FLAG_CARRY = 4;


    // 0 - A, 1 - BC, 2 - DE, 3 - HL
    // 4 - SP: 2-byte aligned, starting at 0xFFFE (top byte)
    private int[] registers = new int[5];
    private int flags;

    public int readReg(int mask) {
        int idx = mask & 0xF;
        mask = mask >> 4;
        if (mask == 0xFF00) {
            return (registers[idx] & mask) >> 8;
        } else {
            return registers[idx] & mask;
        }
    }

    public void writeReg(int mask, int value) {
        // TODO Maybe make faster by only checking 0xFF00 and 0x00FF?
        int idx = mask & 0xF;
        mask = mask >> 4;
        if (mask == 0xFFFF) {
            registers[idx] = value;
        } else if (mask == 0xFF00) {
            registers[idx] = registers[idx] & 0x00FF | value << 8;
        } else {
            registers[idx] = registers[idx] & 0xFF00 | value;
        }
    }

    public int getFlags() {
        return flags;
    }

    public void clearFlags() {
        flags = 0;
    }

    public int getFlag(int flag) {
        return (flags & 1 << flag) >> flag;
    }

    public void setFlag(int flag, boolean value) {
        if (value) {
            flags |= 1 << flag;
        } else {
            flags &= ~(1 << flag);
        }
    }
}
