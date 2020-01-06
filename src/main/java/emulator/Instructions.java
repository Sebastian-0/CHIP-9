package emulator;

public class Instructions {
    public static final int LDI = 1;
    public static final int LDX = 2;

    public static final int PUSH = 3;
    public static final int PUSHW = 4;

    public static final int POP = 5;
    public static final int POPW = 6;

    public static final int MOV = 7;
    public static final int MOVW = 8;

    // Arithmetics
    public static final int CLEARFLAG = 9;
    public static final int SETFLAG = 10;

    public static final int ADD = 11;
    public static final int ADDI = 12;
    public static final int ADDW = 13;

    public static final int SUB = 14;
    public static final int SUBI = 15;

    public static final int INC = 16;
    public static final int INCW = 17;

    public static final int DEC = 18;

    // Logics
    public static final int AND = 19;
    public static final int ANDI = 20;

    public static final int OR = 21;
    public static final int ORI = 22;

    public static final int XOR = 23;
    public static final int XORI = 24;

    // Comparisons
    public static final int CMP = 25;
    public static final int CMPI = 26;
    public static final int CMPS = 27;

    // I/O
    public static final int SIN = 28;
    public static final int SOUT = 29;
    public static final int SOUTB = 291; // Made up, does not exist
    public static final int SOUTC = 292; // Made up, does not exist
    public static final int SOUTD = 293; // Made up, does not exist
    public static final int SOUTE = 294; // Made up, does not exist
    public static final int SOUTH = 295; // Made up, does not exist
    public static final int SOUTL = 296; // Made up, does not exist
    public static final int SOUTBC = 297; // Made up, does not exist
    public static final int SOUTDE = 298; // Made up, does not exist
    public static final int SOUTHL = 299; // Made up, does not exist
    public static final int SOUTSP = 300; // Made up, does not exist
    public static final int SOUTM = 301; // Made up, does not exist
    public static final int SOUTMHL = 302; // Made up, does not exist
    public static final int SOUTFS = 303; // Made up, does not exist

    public static final int CLRSCR = 30;
    public static final int DRAW = 31;

    // Branching
    public static final int JMP = 42; // Absolute jump
    public static final int JMPN = 46; // Near jump
    public static final int JMPNC = 47; // Near jump with condition
    public static final int JMPC = 50; // Absolute jump with condition

    public static final int CALL = 52;
    public static final int RET = 53;

    // Misc
    public static final int NOP = 54;
    public static final int HCF = 55;
}
