package jPlus.io.out;

public enum Resolution {
    SUCCESS(1),
    FAILURE(2);

    Resolution(int value) {
        this.value = value;
    }

    private final int value;

    public int value() {
        return value;
    }
}
