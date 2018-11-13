package domain;

public abstract class Scanner {
    int scanDelay;

    public Scanner(int scanDelay) {
        setScanDelay(scanDelay);
    }

    public int getScanDelay() {
        return scanDelay;
    }

    private void setScanDelay(int scanDelay) {
        this.scanDelay = scanDelay;
    }
}
