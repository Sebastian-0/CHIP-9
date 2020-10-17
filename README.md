# CHIP-9

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Sebastian-0_CHIP-9&metric=alert_status)](https://sonarcloud.io/dashboard?id=Sebastian-0_CHIP-9)

This is my emulator for the CHIP9 computer in the 2019 ed. of the X-MAS CTF. My focus was to write the emulator in a way
that makes it both fast to implement but also fast to run. I achieved this by extensively using lookup-tables based on
the op-codes (see `RegistryMappings` and `InstructionMappings`). The end result was too fast so I added a sleep of 30
millis every time `clrscr` is called.

I also wrote unit tests for most instructions to make sure everything works correctly. To ease this process I wrote a
compiler and added some extra output instructions.

## Compiling & Running
To compile your program you can do the following:
```java
Compiler compiler = new Compiler();
InputStream compiledBytes = compiler.compileToStream(new File("my_program.chip"));
```
The compiler supports all the instructions in the CHIP9 specification as well as some extra output, labels for
jumps/calls and comments.

When you have compiled your program you can run it using:
```java
CHIP9 computer = new CHIP9();
computer.load(compiledBytes, 0x0000);
computer.execute();
```
You can of course invoke `load` several times, for instance if you want to use the boot rom. Additionally you can
specify a UI to render into when draw/clrscr are invoked:
```java
CHIP9 computer = new CHIP9(new ComputerFrame());
computer.load(new File(bootLoader), 0x0000);
computer.load(compiledBytes, 0x0597);
computer.execute();
```

To run pre-compiled roms you can use the utility `RunRom bootloader_path program_path`.

## Implementation
The emulator implements the specification found in 'CHIP9 Manual.pdf', and additionally extends it with some extra
output instructions:

| Op code | Instruction | Meaning |
| ------- | ----------- | ------- |
| 0xE1 | SOUT A | Print reg. A |
| 0xE2 | SOUT B | Print reg. B |
| 0xE3 | SOUT C | Print reg. C |
| 0xE8 | SOUT D | Print reg. D |
| 0xF2 | SOUT E | Print reg. E |
| 0xF3 | SOUT H | Print reg. H |
| 0xF8 | SOUT L | Print reg. L |
| 0x01 | SOUT BC | Print reg. BC |
| 0x02 | SOUT DE | Print reg. DE |
| 0x10 | SOUT HL | Print reg. HL |
| 0x11 | SOUT SP | Print stack pointer |
| 0x12 | SOUT M | Print memory at specified address |
| 0x42 | SOUT (HL) | Print memory at address in HL |
| 0x3E | SOUT FLAGS | Print flags (as a byte number) |

The compiler supports the extra output instructions, labels for jumps/calls and comments. Numbers can also be specified both as
integers (`123`), hexadecimal (`0x7B`) and binary (`0b01111011`). An example of accepted source code:
```
# Program to sum values from 1..10

main:
    ldi A, 10
    call sum
    sout B
    jmp end

sum:
    ldi B, 0
    add B
    subi 1
    jmpnearnz 0xFB
    ret

end:
    hcf
```
Note that my compiler uses the prefix `jmpnear` instead of only `jmp` for near jumps.

## License
This code is shared under the MIT license, see LICENSE for details.
