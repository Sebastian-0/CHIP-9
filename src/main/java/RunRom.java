import emulator.CHIP9;
import ui.ComputerFrame;

import java.io.File;
import java.io.IOException;

public class RunRom {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage:");
            System.out.println("  RunRom bootloader_path program_path");
            return;
        }

        File bootLoader = new File(args[0]);
        File programRom = new File(args[1]);

        if (!bootLoader.exists()) {
            System.out.println("Boot loader rom does not exist: " + args[0]);
            return;
        }

        if (!programRom.exists()) {
            System.out.println("Program rom does not exist: " + args[1]);
            return;
        }

        CHIP9 computer = new CHIP9(new ComputerFrame());
        computer.load(bootLoader, 0x0000);
        computer.load(programRom, 0x0597);
        computer.execute();
    }
}
