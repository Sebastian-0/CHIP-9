package ui;

import emulator.CHIP9;
import emulator.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ComputerFrame extends JFrame implements UI {
    public static final int SIZE = 5;

    private byte[][] buffer = new byte[64][128];
    private byte[][] screen = new byte[64][128];

    private IOCallback ioCallback;

    public ComputerFrame() throws HeadlessException {
        add(new Pane());
        pack();

        addKeyListener(listener);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void clearScreen() {
        SwingUtilities.invokeLater(() -> {
            screen = buffer;
            buffer = new byte[64][128];
            repaint(); // Only repaint on clearscreen for proper double-buffering (not in spec. but works better)
        });

        // Artificially slow down the emulator
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(int x, int y, int mask) {
        for (int i = 0; i < 8; i++) {
            int rx = x + i;
            if (rx >= 0 && rx < buffer[0].length) {
                byte value = (byte) ((mask & 1 << (7 - i)) >> (7 - i));
                buffer[y][rx] = value;
            }
        }
    }

    @Override
    public void setIoCallback(IOCallback callback) {
        ioCallback = callback;
    }

    public class Pane extends JPanel {
        public Pane() {
            setPreferredSize(new Dimension(SIZE * screen[0].length, SIZE * screen.length));
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLACK);
            for (int y = 0; y < screen.length; y++) {
                for (int x = 0; x < screen[0].length; x++) {
                    if (screen[y][x] == 1) {
                        g.fillRect(x * SIZE, y * SIZE, SIZE, SIZE);
                    }
                }
            }
        }
    }

    private final KeyListener listener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            setKeyToState(e, true);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            setKeyToState(e, false);
        }

        private void setKeyToState(KeyEvent e, boolean newState) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                ioCallback.setInputKey(CHIP9.KEY_MASK_UP, newState);
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                ioCallback.setInputKey(CHIP9.KEY_MASK_DOWN, newState);
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                ioCallback.setInputKey(CHIP9.KEY_MASK_LEFT, newState);
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                ioCallback.setInputKey(CHIP9.KEY_MASK_RIGHT, newState);
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                ioCallback.setInputKey(CHIP9.KEY_MASK_A, newState);
            }
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                ioCallback.setInputKey(CHIP9.KEY_MASK_B, newState);
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                ioCallback.setInputKey(CHIP9.KEY_MASK_START, newState);
            }
            if (e.getKeyCode() == KeyEvent.VK_E) {
                ioCallback.setInputKey(CHIP9.KEY_MASK_SELECT, newState);
            }
        }
    };
}
