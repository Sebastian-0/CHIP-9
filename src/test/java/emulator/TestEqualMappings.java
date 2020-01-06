package emulator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

public class TestEqualMappings {
    @Test
    public void testMappingsMatch() {
        assertAll(
                () -> {
                    for (int i = 0; i < InstructionMappings.OP_TO_INSTRUCTION.length; i += 1) {
                        if (InstructionMappings.OP_TO_INSTRUCTION[i] != 0) {
                            if (i >= RegistryMappings.OP_TO_REG.length || RegistryMappings.OP_TO_REG[i] == null) {
                                fail("Missing reg mapping for op-code: 0x" + Integer.toString(i, 16));
                            }
                        }
                    }
                },

                () -> {
                    for (int i = 0; i < RegistryMappings.OP_TO_REG.length; i += 1) {
                        if (RegistryMappings.OP_TO_REG[i] != null) {
                            if (i >= InstructionMappings.OP_TO_INSTRUCTION.length || InstructionMappings.OP_TO_INSTRUCTION[i] == 0) {
                                fail("Missing instruction mapping for op-code: 0x" + Integer.toString(i, 16));
                            }
                        }
                    }
                }
        );
    }
}
