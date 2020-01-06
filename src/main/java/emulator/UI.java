package emulator;

public interface UI {
    void clearScreen();
    void draw(int x, int y, int mask);

    void setIoCallback(IOCallback callback);

    interface IOCallback {
        void setInputKey(int key, boolean state);
    }
}
