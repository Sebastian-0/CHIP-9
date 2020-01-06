package emulator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compiler {
    private List<Instruction> instructions;
    private List<Instruction> waitingForLabel;
    private int totalSize;
    private boolean handledLastInstruction;

    public InputStream compileToStream(File program) throws FileNotFoundException {
        return compileToStream(new FileInputStream(program));
    }
    public InputStream compileToStream(InputStream stream) {
        Map<String, Integer> labelToAddress = new HashMap<>();
        instructions = new ArrayList<>();
        waitingForLabel = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
            String originalLine;
            while ((originalLine = in.readLine()) != null) {
                handledLastInstruction = false;

                String line = originalLine.trim()
                        .toLowerCase()
                        .replaceAll(",\\s+", ",")
                        .replaceAll("\\s+", " ");

                if (line.matches("\\w+:")) {
                    String label = line.substring(0, line.indexOf(':'));
                    labelToAddress.put(label, totalSize);
                } else if (!line.isEmpty() && !line.startsWith("#")) {
                    if (line.startsWith("ldi")) {
                        byte value = readByte(line.substring(line.indexOf(",") + 1));
                        if (line.startsWith("ldi b")) {
                            instruction((byte) 0x20, value);
                        } else if (line.startsWith("ldi c")) {
                            instruction((byte) 0x30, value);
                        } else if (line.startsWith("ldi d")) {
                            instruction((byte) 0x40, value);
                        } else if (line.startsWith("ldi e")) {
                            instruction((byte) 0x50, value);
                        } else if (line.startsWith("ldi h")) {
                            instruction((byte) 0x60, value);
                        } else if (line.startsWith("ldi l")) {
                            instruction((byte) 0x70, value);
                        } else if (line.startsWith("ldi (hl)")) {
                            instruction((byte) 0x80, value);
                        } else if (line.startsWith("ldi a")) {
                            instruction((byte) 0x90, value);
                        }
                    }
                    if (line.startsWith("ldx")) {
                        byte[] values = readShort(line.substring(line.indexOf(",") + 1));
                        if (line.startsWith("ldx bc")) {
                            instruction((byte) 0x21, values[0], values[1]);
                        } else if (line.startsWith("ldx de")) {
                            instruction((byte) 0x31, values[0], values[1]);
                        } else if (line.startsWith("ldx hl")) {
                            instruction((byte) 0x41, values[0], values[1]);
                        } else if (line.startsWith("ldx sp")) {
                            instruction((byte) 0x22, values[0], values[1]);
                        }
                    }

                    if (line.startsWith("push")) {
                        if (line.equals("push b")) {
                            instruction((byte) 0x81);
                        } else if (line.equals("push c")) {
                            instruction((byte) 0x91);
                        } else if (line.equals("push d")) {
                            instruction((byte) 0xA1);
                        } else if (line.equals("push e")) {
                            instruction((byte) 0xB1);
                        } else if (line.equals("push h")) {
                            instruction((byte) 0xC1);
                        } else if (line.equals("push l")) {
                            instruction((byte) 0xD1);
                        } else if (line.equals("push (hl)")) {
                            instruction((byte) 0xC0);
                        } else if (line.equals("push a")) {
                            instruction((byte) 0xD0);
                        } else if (line.equals("push bc")) {
                            instruction((byte) 0x51);
                        } else if (line.equals("push de")) {
                            instruction((byte) 0x61);
                        } else if (line.equals("push hl")) {
                            instruction((byte) 0x71);
                        }
                    }

                    if (line.startsWith("pop")) {
                        if (line.equals("pop b")) {
                            instruction((byte) 0x82);
                        } else if (line.equals("pop c")) {
                            instruction((byte) 0x92);
                        } else if (line.equals("pop d")) {
                            instruction((byte) 0xA2);
                        } else if (line.equals("pop e")) {
                            instruction((byte) 0xB2);
                        } else if (line.equals("pop h")) {
                            instruction((byte) 0xC2);
                        } else if (line.equals("pop l")) {
                            instruction((byte) 0xD2);
                        } else if (line.equals("pop (hl)")) {
                            instruction((byte) 0xC3);
                        } else if (line.equals("pop a")) {
                            instruction((byte) 0xD3);
                        } else if (line.equals("pop bc")) {
                            instruction((byte) 0x52);
                        } else if (line.equals("pop de")) {
                            instruction((byte) 0x62);
                        } else if (line.equals("pop hl")) {
                            instruction((byte) 0x72);
                        }
                    }

                    if (line.startsWith("mov")) {
                        if (line.equals("mov b,b")) {
                            instruction((byte) 0x09);
                        } else if (line.equals("mov b,c")) {
                            instruction((byte) 0x19);
                        } else if (line.equals("mov b,d")) {
                            instruction((byte) 0x29);
                        } else if (line.equals("mov b,e")) {
                            instruction((byte) 0x39);
                        } else if (line.equals("mov b,h")) {
                            instruction((byte) 0x49);
                        } else if (line.equals("mov b,l")) {
                            instruction((byte) 0x59);
                        } else if (line.equals("mov b,(hl)")) {
                            instruction((byte) 0x69);
                        } else if (line.equals("mov b,a")) {
                            instruction((byte) 0x79);
                        }
                        
                        else if (line.equals("mov c,b")) {
                            instruction((byte) 0x89);
                        } else if (line.equals("mov c,c")) {
                            instruction((byte) 0x99);
                        } else if (line.equals("mov c,d")) {
                            instruction((byte) 0xA9);
                        } else if (line.equals("mov c,e")) {
                            instruction((byte) 0xB9);
                        } else if (line.equals("mov c,h")) {
                            instruction((byte) 0xC9);
                        } else if (line.equals("mov c,l")) {
                            instruction((byte) 0xD9);
                        } else if (line.equals("mov c,(hl)")) {
                            instruction((byte) 0xE9);
                        } else if (line.equals("mov c,a")) {
                            instruction((byte) 0xF9);
                        } 

                        else if (line.equals("mov d,b")) {
                            instruction((byte) 0x0A);
                        } else if (line.equals("mov d,c")) {
                            instruction((byte) 0x1A);
                        } else if (line.equals("mov d,d")) {
                            instruction((byte) 0x2A);
                        } else if (line.equals("mov d,e")) {
                            instruction((byte) 0x3A);
                        } else if (line.equals("mov d,h")) {
                            instruction((byte) 0x4A);
                        } else if (line.equals("mov d,l")) {
                            instruction((byte) 0x5A);
                        } else if (line.equals("mov d,(hl)")) {
                            instruction((byte) 0x6A);
                        } else if (line.equals("mov d,a")) {
                            instruction((byte) 0x7A);
                        }

                        else if (line.equals("mov e,b")) {
                            instruction((byte) 0x8A);
                        } else if (line.equals("mov e,c")) {
                            instruction((byte) 0x9A);
                        } else if (line.equals("mov e,d")) {
                            instruction((byte) 0xAA);
                        } else if (line.equals("mov e,e")) {
                            instruction((byte) 0xBA);
                        } else if (line.equals("mov e,h")) {
                            instruction((byte) 0xCA);
                        } else if (line.equals("mov e,l")) {
                            instruction((byte) 0xDA);
                        } else if (line.equals("mov e,(hl)")) {
                            instruction((byte) 0xEA);
                        } else if (line.equals("mov e,a")) {
                            instruction((byte) 0xFA);
                        }

                        else if (line.equals("mov h,b")) {
                            instruction((byte) 0x0B);
                        } else if (line.equals("mov h,c")) {
                            instruction((byte) 0x1B);
                        } else if (line.equals("mov h,d")) {
                            instruction((byte) 0x2B);
                        } else if (line.equals("mov h,e")) {
                            instruction((byte) 0x3B);
                        } else if (line.equals("mov h,h")) {
                            instruction((byte) 0x4B);
                        } else if (line.equals("mov h,l")) {
                            instruction((byte) 0x5B);
                        } else if (line.equals("mov h,(hl)")) {
                            instruction((byte) 0x6B);
                        } else if (line.equals("mov h,a")) {
                            instruction((byte) 0x7B);
                        }

                        else if (line.equals("mov l,b")) {
                            instruction((byte) 0x8B);
                        } else if (line.equals("mov l,c")) {
                            instruction((byte) 0x9B);
                        } else if (line.equals("mov l,d")) {
                            instruction((byte) 0xAB);
                        } else if (line.equals("mov l,e")) {
                            instruction((byte) 0xBB);
                        } else if (line.equals("mov l,h")) {
                            instruction((byte) 0xCB);
                        } else if (line.equals("mov l,l")) {
                            instruction((byte) 0xDB);
                        } else if (line.equals("mov l,(hl)")) {
                            instruction((byte) 0xEB);
                        } else if (line.equals("mov l,a")) {
                            instruction((byte) 0xFB);
                        }

                        else if (line.equals("mov (hl),b")) {
                            instruction((byte) 0x0C);
                        } else if (line.equals("mov (hl),c")) {
                            instruction((byte) 0x1C);
                        } else if (line.equals("mov (hl),d")) {
                            instruction((byte) 0x2C);
                        } else if (line.equals("mov (hl),e")) {
                            instruction((byte) 0x3C);
                        } else if (line.equals("mov (hl),h")) {
                            instruction((byte) 0x4C);
                        } else if (line.equals("mov (hl),l")) {
                            instruction((byte) 0x5C);
                        } else if (line.equals("mov (hl),a")) {
                            instruction((byte) 0x7C);
                        }

                        else if (line.equals("mov a,b")) {
                            instruction((byte) 0x8C);
                        } else if (line.equals("mov a,c")) {
                            instruction((byte) 0x9C);
                        } else if (line.equals("mov a,d")) {
                            instruction((byte) 0xAC);
                        } else if (line.equals("mov a,e")) {
                            instruction((byte) 0xBC);
                        } else if (line.equals("mov a,h")) {
                            instruction((byte) 0xCC);
                        } else if (line.equals("mov a,l")) {
                            instruction((byte) 0xDC);
                        } else if (line.equals("mov a,(hl)")) {
                            instruction((byte) 0xEC);
                        } else if (line.equals("mov a,a")) {
                            instruction((byte) 0xFC);
                        }

                        else if (line.equals("mov hl,bc")) {
                            instruction((byte) 0xED);
                        } else if (line.equals("mov hl,de")) {
                            instruction((byte) 0xFD);
                        }
                    }

                    if (line.equals("clrflag")) {
                        instruction((byte) 0x08);
                    }

                    if (line.startsWith("setflag")) {
                        if (line.equals("setflag z,1")) {
                            instruction((byte) 0x18);
                        } else if (line.equals("setflag z,0")) {
                            instruction((byte) 0x28);
                        } else if (line.equals("setflag n,1")) {
                            instruction((byte) 0x38);
                        } else if (line.equals("setflag n,0")) {
                            instruction((byte) 0x48);
                        } else if (line.equals("setflag h,1")) {
                            instruction((byte) 0x58);
                        } else if (line.equals("setflag h,0")) {
                            instruction((byte) 0x68);
                        } else if (line.equals("setflag c,1")) {
                            instruction((byte) 0x78);
                        } else if (line.equals("setflag c,0")) {
                            instruction((byte) 0x88);
                        }
                    }

                    if (line.startsWith("add")) {
                        if (line.equals("add b")) {
                            instruction((byte) 0x04);
                        } else if (line.equals("add c")) {
                            instruction((byte) 0x14);
                        } else if (line.equals("add d")) {
                            instruction((byte) 0x24);
                        } else if (line.equals("add e")) {
                            instruction((byte) 0x34);
                        } else if (line.equals("add h")) {
                            instruction((byte) 0x44);
                        } else if (line.equals("add l")) {
                            instruction((byte) 0x54);
                        } else if (line.equals("add (hl)")) {
                            instruction((byte) 0x64);
                        } else if (line.equals("add a")) {
                            instruction((byte) 0x74);
                        }

                        else if (line.startsWith("addi")) {
                            byte value = readByte(line.substring(line.indexOf(" ") + 1));
                            instruction((byte) 0xA7, value);
                        }

                        else if (line.equals("addx bc")) {
                            instruction((byte) 0x83);
                        } else if (line.equals("addx de")) {
                            instruction((byte) 0x93);
                        } else if (line.equals("addx hl")) {
                            instruction((byte) 0xA3);
                        }
                    }

                    if (line.startsWith("sub")) {
                        if (line.equals("sub b")) {
                            instruction((byte) 0x84);
                        } else if (line.equals("sub c")) {
                            instruction((byte) 0x94);
                        } else if (line.equals("sub d")) {
                            instruction((byte) 0xA4);
                        } else if (line.equals("sub e")) {
                            instruction((byte) 0xB4);
                        } else if (line.equals("sub h")) {
                            instruction((byte) 0xC4);
                        } else if (line.equals("sub l")) {
                            instruction((byte) 0xD4);
                        } else if (line.equals("sub (hl)")) {
                            instruction((byte) 0xE4);
                        } else if (line.equals("sub a")) {
                            instruction((byte) 0xF4);
                        }

                        else if (line.startsWith("subi")) {
                            byte value = readByte(line.substring(line.indexOf(" ") + 1));
                            instruction((byte) 0xB7, value);
                        }
                    }

                    if (line.equals("inc b")) {
                        instruction((byte) 0x03);
                    } else if (line.equals("inc c")) {
                        instruction((byte) 0x13);
                    } else if (line.equals("inc d")) {
                        instruction((byte) 0x23);
                    } else if (line.equals("inc e")) {
                        instruction((byte) 0x33);
                    } else if (line.equals("inc h")) {
                        instruction((byte) 0x43);
                    } else if (line.equals("inc l")) {
                        instruction((byte) 0x53);
                    } else if (line.equals("inc (hl)")) {
                        instruction((byte) 0x63);
                    } else if (line.equals("inc a")) {
                        instruction((byte) 0x73);
                    }

                    if (line.equals("inx bc")) {
                        instruction((byte) 0xA8);
                    } else if (line.equals("inx de")) {
                        instruction((byte) 0xB8);
                    } else if (line.equals("inx hl")) {
                        instruction((byte) 0xC8);
                    }

                    if (line.equals("dec b")) {
                        instruction((byte) 0x07);
                    } else if (line.equals("dec c")) {
                        instruction((byte) 0x17);
                    } else if (line.equals("dec d")) {
                        instruction((byte) 0x27);
                    } else if (line.equals("dec e")) {
                        instruction((byte) 0x37);
                    } else if (line.equals("dec h")) {
                        instruction((byte) 0x47);
                    } else if (line.equals("dec l")) {
                        instruction((byte) 0x57);
                    } else if (line.equals("dec (hl)")) {
                        instruction((byte) 0x67);
                    } else if (line.equals("dec a")) {
                        instruction((byte) 0x77);
                    }

                    if (line.startsWith("and")) {
                        if (line.equals("and b")) {
                            instruction((byte) 0x05);
                        } else if (line.equals("and c")) {
                            instruction((byte) 0x15);
                        } else if (line.equals("and d")) {
                            instruction((byte) 0x25);
                        } else if (line.equals("and e")) {
                            instruction((byte) 0x35);
                        } else if (line.equals("and h")) {
                            instruction((byte) 0x45);
                        } else if (line.equals("and l")) {
                            instruction((byte) 0x55);
                        } else if (line.equals("and (hl)")) {
                            instruction((byte) 0x65);
                        } else if (line.equals("and a")) {
                            instruction((byte) 0x75);
                        }

                        else if (line.startsWith("andi")) {
                            byte value = readByte(line.substring(line.indexOf(" ") + 1));
                            instruction((byte) 0xC7, value);
                        }
                    }

                    if (line.startsWith("or")) {
                        if (line.equals("or b")) {
                            instruction((byte) 0x85);
                        } else if (line.equals("or c")) {
                            instruction((byte) 0x95);
                        } else if (line.equals("or d")) {
                            instruction((byte) 0xA5);
                        } else if (line.equals("or e")) {
                            instruction((byte) 0xB5);
                        } else if (line.equals("or h")) {
                            instruction((byte) 0xC5);
                        } else if (line.equals("or l")) {
                            instruction((byte) 0xD5);
                        } else if (line.equals("or (hl)")) {
                            instruction((byte) 0xE5);
                        } else if (line.equals("or a")) {
                            instruction((byte) 0xF5);
                        }

                        else if (line.startsWith("ori")) {
                            byte value = readByte(line.substring(line.indexOf(" ") + 1));
                            instruction((byte) 0xD7, value);
                        }
                    }

                    if (line.startsWith("xor")) {
                        if (line.equals("xor b")) {
                            instruction((byte) 0x06);
                        } else if (line.equals("xor c")) {
                            instruction((byte) 0x16);
                        } else if (line.equals("xor d")) {
                            instruction((byte) 0x26);
                        } else if (line.equals("xor e")) {
                            instruction((byte) 0x36);
                        } else if (line.equals("xor h")) {
                            instruction((byte) 0x46);
                        } else if (line.equals("xor l")) {
                            instruction((byte) 0x56);
                        } else if (line.equals("xor (hl)")) {
                            instruction((byte) 0x66);
                        } else if (line.equals("xor a")) {
                            instruction((byte) 0x76);
                        }

                        else if (line.startsWith("xori")) {
                            byte value = readByte(line.substring(line.indexOf(" ") + 1));
                            instruction((byte) 0xE7, value);
                        }
                    }

                    if (line.startsWith("cmp")) {
                        if (line.equals("cmp b")) {
                            instruction((byte) 0x86);
                        } else if (line.equals("cmp c")) {
                            instruction((byte) 0x96);
                        } else if (line.equals("cmp d")) {
                            instruction((byte) 0xA6);
                        } else if (line.equals("cmp e")) {
                            instruction((byte) 0xB6);
                        } else if (line.equals("cmp h")) {
                            instruction((byte) 0xC6);
                        } else if (line.equals("cmp l")) {
                            instruction((byte) 0xD6);
                        } else if (line.equals("cmp (hl)")) {
                            instruction((byte) 0xE6);
                        } else if (line.equals("cmp a")) {
                            instruction((byte) 0xF6);
                        }

                        else if (line.startsWith("cmpi")) {
                            byte value = readByte(line.substring(line.indexOf(" ") + 1));
                            instruction((byte) 0xF7, value);
                        }
                        
                        else if (line.equals("cmps b")) {
                            instruction((byte) 0x0D);
                        } else if (line.equals("cmps c")) {
                            instruction((byte) 0x1D);
                        } else if (line.equals("cmps d")) {
                            instruction((byte) 0x2D);
                        } else if (line.equals("cmps e")) {
                            instruction((byte) 0x3D);
                        } else if (line.equals("cmps h")) {
                            instruction((byte) 0x4D);
                        } else if (line.equals("cmps l")) {
                            instruction((byte) 0x5D);
                        } else if (line.equals("cmps (hl)")) {
                            instruction((byte) 0x6D);
                        } else if (line.equals("cmps a")) {
                            instruction((byte) 0x7D);
                        }
                    }

                    if (line.equals("sin a")) {
                        instruction((byte) 0xE0);
                    }

                    if (line.startsWith("sout")) {
                        if (line.equals("sout a")) {
                            instruction((byte) 0xE1);
                        } else if (line.equals("sout b")) {
                            instruction((byte) 0xE2);
                        } else if (line.equals("sout c")) {
                            instruction((byte) 0xE3);
                        } else if (line.equals("sout d")) {
                            instruction((byte) 0xE8);
                        } else if (line.equals("sout e")) {
                            instruction((byte) 0xF2);
                        } else if (line.equals("sout h")) {
                            instruction((byte) 0xF3);
                        } else if (line.equals("sout l")) {
                            instruction((byte) 0xF8);
                        } else if (line.equals("sout bc")) {
                            instruction((byte) 0x01);
                        } else if (line.equals("sout de")) {
                            instruction((byte) 0x02);
                        } else if (line.equals("sout hl")) {
                            instruction((byte) 0x10);
                        } else if (line.equals("sout sp")) {
                            instruction((byte) 0x11);
                        } else if (line.startsWith("sout m")) {
                            byte[] values = readShort(line.substring(line.indexOf(" ") + 1));
                            instruction((byte) 0x12, values[0], values[1]);
                        } else if (line.equals("sout (hl)")) {
                            instruction((byte) 0x42);
                        } else if (line.equals("sout flags")) {
                            instruction((byte) 0x3E);
                        }
                    }

                    if (line.equals("clrscr")) {
                        instruction((byte) 0xF0);
                    }

                    if (line.equals("draw")) {
                        instruction((byte) 0xF1);
                    }

                    if (line.startsWith("jmpnear")) {
                        byte offset = readByte(line.substring(line.indexOf(" ") + 1));
                        if (line.startsWith("jmpnearz ")) {
                            instruction((byte) 0xAF, offset);
                        } else if (line.startsWith("jmpnearnz ")) {
                            instruction((byte) 0xBF, offset);
                        } else if (line.startsWith("jmpnearn ")) {
                            instruction((byte) 0xCF, offset);
                        } else if (line.startsWith("jmpnearnn ")) {
                            instruction((byte) 0xDF, offset);
                        } else if (line.startsWith("jmpnearh ")) {
                            instruction((byte) 0xEF, offset);
                        } else if (line.startsWith("jmpnearnh ")) {
                            instruction((byte) 0xFF, offset);
                        } else if (line.startsWith("jmpnearc ")) {
                            instruction((byte) 0xEE, offset);
                        } else if (line.startsWith("jmpnearnc ")) {
                            instruction((byte) 0xFE, offset);
                        } else {
                            instruction((byte) 0x9F, offset);
                        }
                    } else if (line.startsWith("jmp")) {
                        byte[] address;
                        String label;
                        int missingBytes;
                        String valueString = line.substring(line.indexOf(" ") + 1);
                        if (valueString.matches("[0-9xb]+")) {
                            address = readShort(valueString);
                            label = null;
                            missingBytes = 0;
                        } else {
                            address = new byte[2];
                            label = valueString;
                            missingBytes = 2;
                        }
                        if (line.startsWith("jmpz ")) {
                            instruction(label, missingBytes, (byte) 0x1F, address[0], address[1]);
                        } else if (line.startsWith("jmpnz ")) {
                            instruction(label, missingBytes, (byte) 0x2F, address[0], address[1]);
                        } else if (line.startsWith("jmpn ")) {
                            instruction(label, missingBytes, (byte) 0x3F, address[0], address[1]);
                        } else if (line.startsWith("jmpnn ")) {
                            instruction(label, missingBytes, (byte) 0x4F, address[0], address[1]);
                        } else if (line.startsWith("jmph ")) {
                            instruction(label, missingBytes, (byte) 0x5F, address[0], address[1]);
                        } else if (line.startsWith("jmpnh ")) {
                            instruction(label, missingBytes, (byte) 0x6F, address[0], address[1]);
                        } else if (line.startsWith("jmpc ")) {
                            instruction(label, missingBytes, (byte) 0x7F, address[0], address[1]);
                        } else if (line.startsWith("jmpnc ")) {
                            instruction(label, missingBytes, (byte) 0x8F, address[0], address[1]);
                        } else {
                            instruction(label, missingBytes, (byte) 0x0F, address[0], address[1]);
                        }
                    }

                    if (line.startsWith("call")) {
                        byte[] address;
                        String label;
                        int missingBytes;
                        String valueString = line.substring(line.indexOf(" ") + 1);
                        if (valueString.matches("[0-9xb]+")) {
                            address = readShort(valueString);
                            label = null;
                            missingBytes = 0;
                        } else {
                            address = new byte[2];
                            label = valueString;
                            missingBytes = 2;
                        }
                        instruction(label, missingBytes, (byte) 0x1E, address[0], address[1]);
                    }

                    if (line.equals("ret")) {
                        instruction((byte) 0x0E);
                    }

                    if (line.equals("nop")) {
                        instruction((byte) 0x00);
                    }

                    if (line.startsWith("hcf")) {
                        instruction((byte) 0x6C);
                    }

                    if (!handledLastInstruction) {
                        throw new IllegalArgumentException("Unknown instruction: " + originalLine);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Resolve labels
        for (Instruction instruction : waitingForLabel) {
            Integer address = labelToAddress.get(instruction.missingBytesLabel);
            if (address == null) {
                throw new IllegalStateException("No label '" + instruction.missingBytesLabel +  "' found in code");
            }
            instruction.data[instruction.data.length - 2] = (byte) (address & 0x00FF);
            instruction.data[instruction.data.length - 1] = (byte) ((address & 0xFF00) >> 8);
        }

        // Build result
        byte[] result = new byte[totalSize];
        int idx = 0;
        for (Instruction instruction : instructions) {
            System.arraycopy(instruction.data, 0, result, idx, instruction.data.length);
            idx += instruction.data.length;
        }
        return new ByteArrayInputStream(result);
    }

    private byte readByte(String byteString) {
        int value = readNumber(byteString);
        if (value > 255) {
            throw new IllegalArgumentException("Operator can max be " + 255 + " but was " + value);
        }
        return (byte) value;
    }

    private byte[] readShort(String shortString) {
        int value = readNumber(shortString);
        if (value > 65535) {
            throw new IllegalArgumentException("Operator can max be " + 65535 + " but was " + value);
        }
        return new byte[]{(byte) ((value & 0x00FF)), (byte) ((value & 0xFF00) >> 8)};
    }

    private int readNumber(String number) {
        if (number.contains("0b")) {
            number = number.substring(number.indexOf('b') + 1);
            return Integer.parseInt(number, 2);
        }
        if (number.contains("0x")) {
            number = number.substring(number.indexOf('x') + 1);
            return Integer.parseInt(number, 16);
        }
        return Integer.parseInt(number);
    }

    private void instruction(byte... bytes) {
        instruction(null, 0, bytes);
    }

    private void instruction(String label, int missingBytes, byte... bytes) {
        if (missingBytes > 0) {
            Instruction instruction = new Instruction(missingBytes, label, bytes);
            instructions.add(instruction);
            waitingForLabel.add(instruction);
        } else {
            instructions.add(new Instruction(bytes));
        }
        totalSize += bytes.length;
        handledLastInstruction = true;
    }

    private static class Instruction {
        private byte[] data;
        private int missingBytes;
        private String missingBytesLabel;

        public Instruction(byte... data) {
            this.data = data;
        }

        public Instruction(int missingBytes, String missingBytesLabel, byte... data) {
            this.missingBytes = missingBytes;
            this.missingBytesLabel = missingBytesLabel;
            this.data = data;
        }

        public void addMissing(Byte... newData) {
            if (newData.length != missingBytes) {
                throw new IllegalArgumentException("Expected " + missingBytes + " extra bytes but got " + newData.length);
            }
            System.arraycopy(newData, 0, data, data.length - missingBytes, missingBytes);
        }
    }
}
