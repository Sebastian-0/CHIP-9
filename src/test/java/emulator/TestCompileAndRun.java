package emulator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestCompileAndRun {
    private static final File ROOT_DIR = new File("src/test/resources/emulator");

    public static Stream<String> listAllTests() {
        File[] codeFiles = ROOT_DIR.listFiles(f -> f.getName().endsWith(".chip"));
        if (codeFiles != null) {
            return Arrays.stream(codeFiles).map(File::getName).map(n -> n.substring(0, n.length() - ".chip".length()));
        } else {
            throw new IllegalStateException("No tests were found!");
        }
    }

    @ParameterizedTest
    @MethodSource("listAllTests")
    public void testAllPrograms(String programName) throws IOException {
        runProgram(programName);
    }

    @Test
    public void testSerialInput() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] {
                (byte) 0xE0, 0x73, (byte) 0xE1, 0x6C
        });

        CHIP9 computer = new CHIP9();
        computer.load(in, 0);
        computer.writeSerialInput(123);
        computer.execute();
        assertEquals(124, computer.readSerialOutput());
    }

    @Test
    @Disabled
    public void testSpecificProgram() throws IOException {
        runProgram("small_test");
    }

    private void runProgram(String programName) throws IOException {
        File expectedFile = new File(ROOT_DIR, programName + ".out");
        if (!expectedFile.exists()) {
            fail("No output file present for " + programName);
        }

        List<Integer> expectedOutput = Files.readAllLines(expectedFile.toPath())
                .stream()
                .filter(l -> !l.isEmpty())
                .map(v -> {
                        if (v.contains("0x")) {
                            return Integer.parseInt(v.substring(v.indexOf('x') + 1), 16);
                        }
                        if (v.contains("0b")) {
                            return Integer.parseInt(v.substring(v.indexOf('b') + 1), 2);
                        }
                        return Integer.parseInt(v);
                })
                .collect(Collectors.toList());

        Compiler compiler = new Compiler();
        InputStream stream = compiler.compileToStream(new File(ROOT_DIR, programName + ".chip"));
        CHIP9 computer = new CHIP9();
        computer.load(stream, 0);
        computer.execute();

        List<Integer> actualOutput = new ArrayList<>();
        while (true) {
            int out = computer.readSerialOutput();
            if (out == -1) {
                break;
            }
            actualOutput.add(out);
        }

        assertEquals(expectedOutput, actualOutput);
    }
}
