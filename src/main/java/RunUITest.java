import emulator.CHIP9;
import emulator.Compiler;
import ui.ComputerFrame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RunUITest {
    public static void main(String[] args) throws IOException {
        ComputerFrame frame = new ComputerFrame();

        ByteArrayInputStream rawCode = new ByteArrayInputStream(
                ("ldi A, 0x5A\n" +
                        "ldi B, 5\n" +
                        "ldi C, 0x0\n" +
                        "clrscr\n" +
                        "draw\n" +
                        "clrscr\n" +
                        "hcf\n").getBytes());
        Compiler compiler = new Compiler();
        InputStream code = compiler.compileToStream(rawCode);
        CHIP9 computer = new CHIP9(frame);
        computer.load(code, 0);
        computer.execute();
    }
}
