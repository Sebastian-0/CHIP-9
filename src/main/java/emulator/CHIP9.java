package emulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

public class CHIP9 {
    private static final int INPUT_ADDRESS = 0xF000;

    public static final int KEY_MASK_UP     = 1 << 7;
    public static final int KEY_MASK_LEFT   = 1 << 6;
    public static final int KEY_MASK_DOWN   = 1 << 5;
    public static final int KEY_MASK_RIGHT  = 1 << 4;
    public static final int KEY_MASK_A      = 1 << 3;
    public static final int KEY_MASK_B      = 1 << 2;
    public static final int KEY_MASK_SELECT = 1 << 1;
    public static final int KEY_MASK_START  = 1;

    private final UI ui;

    private final Registry registry = new Registry();

    private final int[] memory = new int[64 * 1024]; // One byte at each position
    private int pc;

    private final Queue<Integer> serialOutput = new LinkedList<>();
    private final Queue<Integer> serialInput = new LinkedList<>();

    private boolean terminated;

    public CHIP9() {
        this(new UI() {
            @Override
            public void clearScreen() { }
            @Override
            public void draw(int x, int y, int mask) { }
            @Override
            public void setIoCallback(IOCallback callback) { }
        });
    }

    public CHIP9(UI ui) {
        this.ui = ui;
        ui.setIoCallback(this::setInputKey);
    }

    /**
     * Loads a program from a file.
     * @param inFile The file
     * @param address Target address in big endian
     */
    public void load(File inFile, int address) throws IOException {
        try (InputStream in = new FileInputStream(inFile)) {
            load(in, address);
        }
    }
    /**
     * Loads a program from a stream.
     * @param in The stream
     * @param address Target address in big endian
     */
    public void load(InputStream in, int address) throws IOException {
        byte[] bytes = new byte[1024];
        int read;
        while ((read = in.read(bytes)) != -1) {
            for (int i = 0; i < read; i++) {
                memory[address + i] = bytes[i] & 0xFF;
            }
            address += read;
        }
    }

    public int readSerialOutput() {
        if (serialOutput.isEmpty()) {
            return -1;
        }
        return serialOutput.poll();
    }

    public void writeSerialInput(int value) {
        synchronized (serialInput) {
            serialInput.offer(value);
        }
    }

    public void setInputKey(int key, boolean state) {
        synchronized (memory) {
            int current = readMemory(INPUT_ADDRESS);
            if (state) {
                current |= key;
            } else {
                current &= ~key;
            }
            writeMemory(INPUT_ADDRESS, current);
        }
    }

    public void execute() {
        while (!terminated) {
            int opCode = memory[pc];
            int instruction = InstructionMappings.OP_TO_INSTRUCTION[opCode];
            RegistryMappings.RegistryMapping reg = RegistryMappings.OP_TO_REG[opCode];
            switch (instruction) {
                case Instructions.LDI:
                    ldi(reg);
                    break;
                case Instructions.LDX:
                    ldx(reg);
                    break;
                case Instructions.PUSH:
                    push(reg);
                    break;
                case Instructions.PUSHW:
                    pushw(reg);
                    break;
                case Instructions.POP:
                    pop(reg);
                    break;
                case Instructions.POPW:
                    popw(reg);
                    break;
                case Instructions.MOV:
                    mov(reg);
                    break;
                case Instructions.MOVW:
                    movw(reg);
                    break;
                case Instructions.CLEARFLAG:
                    clrflag();
                    break;
                case Instructions.SETFLAG:
                    setflag(reg);
                    break;
                case Instructions.ADD:
                    add(reg);
                    break;
                case Instructions.ADDI:
                    addi(reg);
                    break;
                case Instructions.ADDW:
                    addw(reg);
                    break;
                case Instructions.SUB:
                    sub(reg);
                    break;
                case Instructions.SUBI:
                    subi(reg);
                    break;
                case Instructions.INC:
                    inc(reg);
                    break;
                case Instructions.INCW:
                    incw(reg);
                    break;
                case Instructions.DEC:
                    dec(reg);
                    break;
                case Instructions.AND:
                    and(reg);
                    break;
                case Instructions.ANDI:
                    andi(reg);
                    break;
                case Instructions.OR:
                    or(reg);
                    break;
                case Instructions.ORI:
                    ori(reg);
                    break;
                case Instructions.XOR:
                    xor(reg);
                    break;
                case Instructions.XORI:
                    xori(reg);
                    break;
                case Instructions.CMP:
                    cmp(reg);
                    break;
                case Instructions.CMPI:
                    cmpi(reg);
                    break;
                case Instructions.CMPS:
                    cmps(reg);
                    break;
                case Instructions.JMP:
                    jmp();
                    break;
                case Instructions.JMPC:
                    jmpc(reg);
                    break;
                case Instructions.JMPN:
                    jmpn();
                    break;
                case Instructions.JMPNC:
                    jmpnc(reg);
                    break;
                case Instructions.CALL:
                    call();
                    break;
                case Instructions.RET:
                    ret();
                    break;
                case Instructions.SOUT:
                case Instructions.SOUTB:
                case Instructions.SOUTC:
                case Instructions.SOUTD:
                case Instructions.SOUTE:
                case Instructions.SOUTH:
                case Instructions.SOUTL:
                case Instructions.SOUTMHL:
                    sout(reg);
                    break;
                case Instructions.SOUTBC:
                case Instructions.SOUTDE:
                case Instructions.SOUTHL:
                case Instructions.SOUTSP:
                    soutw(reg);
                    break;
                case Instructions.SOUTM:
                    soutm();
                    break;
                case Instructions.SOUTFS:
                    soutfs();
                    break;
                case Instructions.SIN:
                    sin(reg);
                    break;
                case Instructions.HCF:
                    hcf();
                    break;
                case Instructions.CLRSCR:
                    clrscr();
                    break;
                case Instructions.DRAW:
                    draw();
                    break;
                case Instructions.NOP:
                    nop();
                    break;
                default:
                    throw new IllegalStateException("Unknown instruction: 0x" + Integer.toString(opCode, 16));
            }
        }
    }

    private void ldi(RegistryMappings.RegistryMapping reg) {
        if (reg.r1Indirect) {
            writeMemory(registry.readReg(reg.r1), memory[pc+1]);
        } else {
            registry.writeReg(reg.r1, memory[pc+1]);
        }
        pc += 2;
    }

    private void ldx(RegistryMappings.RegistryMapping reg) {
        registry.writeReg(reg.r1, memory[pc+2] << 8 | memory[pc+1]);
        pc += 3;
    }

    private void push(RegistryMappings.RegistryMapping reg) {
        if (reg.r1Indirect) {
            writeMemory(registry.readReg(Registry.REG_SP), readMemory(registry.readReg(reg.r1)));
        } else {
            writeMemory(registry.readReg(Registry.REG_SP), registry.readReg(reg.r1));
        }
        addToStackPointer(-2);
        pc += 1;
    }

    private void pushw(RegistryMappings.RegistryMapping reg) {
        int sp = registry.readReg(Registry.REG_SP);
        int value = registry.readReg(reg.r1);
        writeMemory(sp, value & 0x00FF);
        writeMemory(sp + 1, (value & 0xFF00) >> 8);
        addToStackPointer(-2);
        pc += 1;
    }

    private void pop(RegistryMappings.RegistryMapping reg) {
        addToStackPointer(2);
        if (reg.r1Indirect) {
            writeMemory(registry.readReg(reg.r1), readMemory(registry.readReg(Registry.REG_SP)));
        } else {
            registry.writeReg(reg.r1, readMemory(registry.readReg(Registry.REG_SP)));
        }
        pc += 1;
    }

    private void popw(RegistryMappings.RegistryMapping reg) {
        addToStackPointer(2);
        int sp = registry.readReg(Registry.REG_SP);
        int value = readMemory(sp) | readMemory(sp + 1) << 8;
        registry.writeReg(reg.r1, value);
        pc += 1;
    }

    private void mov(RegistryMappings.RegistryMapping reg) {
        int source;
        if (reg.r2Indirect) {
            source = readMemory(registry.readReg(reg.r2));
        } else {
            source = registry.readReg(reg.r2);
        }

        if (reg.r1Indirect) {
            writeMemory(registry.readReg(reg.r1), source);
        } else {
            registry.writeReg(reg.r1, source);
        }
        pc += 1;
    }

    private void movw(RegistryMappings.RegistryMapping reg) {
        registry.writeReg(reg.r1, registry.readReg(reg.r2));
        pc += 1;
    }

    private void clrflag() {
        registry.clearFlags();
        pc += 1;
    }

    private void setflag(RegistryMappings.RegistryMapping reg) {
        registry.setFlag(reg.r1, reg.r1Indirect);
        pc += 1;
    }

    private void add(RegistryMappings.RegistryMapping reg) {
        addTerm(reg, registry.readReg(Registry.REG_A));
    }

    private void addTerm(RegistryMappings.RegistryMapping reg, int term) {
        int oldValue;
        if (reg.r1Indirect) {
            oldValue = readMemory(registry.readReg(reg.r1));
        } else {
            oldValue = registry.readReg(reg.r1);
        }

        setFlags(oldValue, term, false);

        int result = term + oldValue;
        if (reg.r1Indirect) {
            writeMemory(registry.readReg(reg.r1), result & 0xFF);
        } else {
            registry.writeReg(reg.r1, result & 0xFF);
        }
        pc += 1;
    }

    private void addi(RegistryMappings.RegistryMapping reg) {
        int oldValue = registry.readReg(reg.r1);
        int adder = memory[pc + 1];
        setFlags(oldValue, adder, false);
        registry.writeReg(reg.r1, (oldValue + adder) & 0xFF);
        pc += 2;
    }

    private void addw(RegistryMappings.RegistryMapping reg) {
        int oldValue = registry.readReg(reg.r1); // Is byte order correct?
        int adder = registry.readReg(Registry.REG_A);
        setFlagsWide(oldValue, adder);
        registry.writeReg(reg.r1, (oldValue + adder) & 0xFFFF);
        pc += 1;
    }

    private void setFlags(int oldValue, int adder, boolean isSub) {
        int result = oldValue + adder;
        registry.setFlag(Registry.FLAG_ZERO, (result & 0xFF) == 0);
        registry.setFlag(Registry.FLAG_NEGATIVE, (result & 1 << 7) != 0);
        registry.setFlag(Registry.FLAG_HALF_CARRY, (((oldValue & 0xF) + (adder & 0xF)) & 0xF0) != 0);
        if (isSub) {
            int res = (oldValue & 0xFF) + (adder & 0xFF);
            registry.setFlag(Registry.FLAG_CARRY, (res & 1 << 8) == 0); // Is this correct? It's this way for i8080...
        } else {
            registry.setFlag(Registry.FLAG_CARRY, (result & 1 << 8) != 0);
        }
    }

    private void setFlagsWide(int oldValue, int adder) {
        int result = oldValue + adder;
        registry.setFlag(Registry.FLAG_ZERO, (result & 0xFFFF) == 0);
        registry.setFlag(Registry.FLAG_NEGATIVE, (result & 1 << 15) != 0);
        int lowestByteCarry = ((oldValue & 0xFF) + (adder & 0xFF)) & 0xF00;
        registry.setFlag(Registry.FLAG_HALF_CARRY, (((oldValue & 0xF00) + lowestByteCarry) & 0xF000) != 0);
        registry.setFlag(Registry.FLAG_CARRY, (result & 1 << 16) != 0);
    }

    private void sub(RegistryMappings.RegistryMapping reg) {
        subTerm(reg, registry.readReg(Registry.REG_A));
    }

    private void subTerm(RegistryMappings.RegistryMapping reg, int term) {
        int oldValue;
        if (reg.r1Indirect) {
            oldValue = readMemory(registry.readReg(reg.r1));
        } else {
            oldValue = registry.readReg(reg.r1);
        }

        setFlags(oldValue, (-term) & 0xFF, true);

        int result = oldValue - term;
        if (reg.r1Indirect) {
            writeMemory(registry.readReg(reg.r1), result & 0xFF);
        } else {
            registry.writeReg(reg.r1, result & 0xFF);
        }
        pc += 1;
    }

    private void subi(RegistryMappings.RegistryMapping reg) {
        int oldValue = registry.readReg(reg.r1);
        int term = memory[pc + 1];
        setFlags(oldValue, (-term) & 0xFF, true);
        registry.writeReg(reg.r1, (oldValue - term) & 0xFF);
        pc += 2;
    }

    private void inc(RegistryMappings.RegistryMapping reg) {
        addTerm(reg, 1);
    }

    private void incw(RegistryMappings.RegistryMapping reg) {
        registry.writeReg(reg.r1, (registry.readReg(reg.r1) + 1) & 0xFFFF);
        pc += 1;
    }

    private void dec(RegistryMappings.RegistryMapping reg) {
        subTerm(reg, 1);
    }

    private void and(RegistryMappings.RegistryMapping reg) {
        int result = registry.readReg(Registry.REG_A);
        if (reg.r1Indirect) {
            result &= readMemory(registry.readReg(reg.r1));
        } else {
            result &= registry.readReg(reg.r1);
        }

        setLogicFlags(result);

        if (reg.r1Indirect) {
            writeMemory(registry.readReg(reg.r1), result);
        } else {
            registry.writeReg(reg.r1, result);
        }
        pc += 1;
    }

    private void andi(RegistryMappings.RegistryMapping reg) {
        int result = registry.readReg(reg.r1);
        result &= memory[pc + 1];
        setLogicFlags(result);
        registry.writeReg(Registry.REG_A, result);
        pc += 2;
    }

    private void or(RegistryMappings.RegistryMapping reg) {
        int result = registry.readReg(Registry.REG_A);
        if (reg.r1Indirect) {
            result |= readMemory(registry.readReg(reg.r1));
        } else {
            result |= registry.readReg(reg.r1);
        }

        setLogicFlags(result);

        if (reg.r1Indirect) {
            writeMemory(registry.readReg(reg.r1), result);
        } else {
            registry.writeReg(reg.r1, result);
        }
        pc += 1;
    }

    private void ori(RegistryMappings.RegistryMapping reg) {
        int result = registry.readReg(reg.r1);
        result |= memory[pc + 1];
        setLogicFlags(result);
        registry.writeReg(Registry.REG_A, result);
        pc += 2;
    }

    private void xor(RegistryMappings.RegistryMapping reg) {
        int result = registry.readReg(Registry.REG_A);
        if (reg.r1Indirect) {
            result ^= readMemory(registry.readReg(reg.r1));
        } else {
            result ^= registry.readReg(reg.r1);
        }

        setLogicFlags(result);

        if (reg.r1Indirect) {
            writeMemory(registry.readReg(reg.r1), result);
        } else {
            registry.writeReg(reg.r1, result);
        }
        pc += 1;
    }

    private void xori(RegistryMappings.RegistryMapping reg) {
        int result = registry.readReg(reg.r1);
        result ^= memory[pc + 1];
        setLogicFlags(result);
        registry.writeReg(Registry.REG_A, result);
        pc += 2;
    }

    private void cmp(RegistryMappings.RegistryMapping reg) {
        int value1 = registry.readReg(Registry.REG_A);
        int value2;
        if (reg.r1Indirect) {
            value2 = readMemory(registry.readReg(reg.r1));
        } else {
            value2 = registry.readReg(reg.r1);
        }

        setFlags(value2, -value1, true);
        pc += 1;
    }

    private void cmpi(RegistryMappings.RegistryMapping reg) {
        int value1 = registry.readReg(reg.r1);
        int value2 = memory[pc + 1];
        setFlags(value1, -value2, true);
        pc += 2;
    }

    private void cmps(RegistryMappings.RegistryMapping reg) {
        int value1 = (byte) registry.readReg(Registry.REG_A);
        int value2;
        if (reg.r1Indirect) {
            value2 = (byte) readMemory(registry.readReg(reg.r1));
        } else {
            value2 = (byte) registry.readReg(reg.r1);
        }

        // TODO Set the carry flags correctly
        registry.setFlag(Registry.FLAG_ZERO, value1 == value2);
        registry.setFlag(Registry.FLAG_NEGATIVE, value1 > value2);
        pc += 1;
    }

    private void setLogicFlags(int value) {
        registry.clearFlags();
        registry.setFlag(Registry.FLAG_ZERO, value == 0);
        registry.setFlag(Registry.FLAG_NEGATIVE, (value & 1 << 7) != 0);
    }

    private void sout(RegistryMappings.RegistryMapping reg) {
        int out;
        if (reg.r1Indirect) {
            out = readMemory(registry.readReg(reg.r1));
        } else {
            out = registry.readReg(reg.r1);
        }
        serialOutput.add(out);
        System.out.println(out);
        pc += 1;
    }

    private void soutw(RegistryMappings.RegistryMapping reg) {
        int out = registry.readReg(reg.r1);
        serialOutput.add(out);
        System.out.println(out);
        pc += 1;
    }

    private void soutm() {
        int out = readMemory(memory[pc+2] << 8 | memory[pc+1]);
        serialOutput.add(out);
        System.out.println(out);
        pc += 3;
    }

    private void soutfs() {
        int out = registry.getFlags();
        serialOutput.add(out);
        System.out.println(out);
        pc += 1;
    }

    private void sin(RegistryMappings.RegistryMapping reg) {
        synchronized (serialInput) {
            if (!serialInput.isEmpty()) {
                registry.writeReg(reg.r1, serialInput.poll() & 0xFF);
            } else {
                throw new IllegalStateException("No input available!");
            }
        }
        pc += 1;
    }

    private void clrscr() {
        ui.clearScreen();
        pc += 1;
    }

    private void draw() {
        int x = (byte) registry.readReg(Registry.REG_C);
        int y = (byte) registry.readReg(Registry.REG_B);
        int mask = registry.readReg(Registry.REG_A);
        ui.draw(x, y, mask);
        pc += 1;
    }

    private void jmp() {
        pc = memory[pc + 2] << 8 | memory[pc + 1];
    }

    private void jmpc(RegistryMappings.RegistryMapping reg) {
        int value = registry.getFlag(reg.r1);
        if ((value == 1) == reg.r1Indirect) {
            pc = memory[pc + 2] << 8 | memory[pc + 1];
        } else {
            pc += 3;
        }
    }

    private void jmpn() {
        pc += 2 + (byte) memory[pc + 1];
    }

    private void jmpnc(RegistryMappings.RegistryMapping reg) {
        int value = registry.getFlag(reg.r1);
        if ((value == 1) == reg.r1Indirect) {
            pc += 2 + (byte) memory[pc + 1];
        } else {
            pc += 2;
        }
    }

    private void hcf() {
        terminated = true;
        pc += 1;
    }

    private void call() {
        writeMemory(registry.readReg(Registry.REG_SP), pc + 3);
        addToStackPointer(-2);
        pc = memory[pc + 2] << 8 | memory[pc + 1];
    }

    private void ret() {
        addToStackPointer(2);
        pc = readMemory(registry.readReg(Registry.REG_SP));
    }

    private void nop() {
        /* Do nothing */
        pc += 1;
    }

    private int readMemory(int littleEndianAddress) {
        if (littleEndianAddress == INPUT_ADDRESS) {
            synchronized (memory) {
                return memory[littleEndianAddress];
            }
        } else {
            return memory[littleEndianAddress];
        }
    }

    private void writeMemory(int littleEndianAddress, int value) {
        if (littleEndianAddress == INPUT_ADDRESS) {
            synchronized (memory) {
                memory[littleEndianAddress] = value;
            }
        } else {
            memory[littleEndianAddress] = value;
        }
    }

    private void addToStackPointer(int amount) {
        int sp = registry.readReg(Registry.REG_SP);
        sp += amount;
        registry.writeReg(Registry.REG_SP, sp);
    }
}
