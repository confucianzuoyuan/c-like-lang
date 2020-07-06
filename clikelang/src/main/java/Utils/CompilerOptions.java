package Utils;

public class CompilerOptions {
    private static final int SIZE_INT = 4;
    private static final int SIZE_BOOL = 1;
    private static final int SIZE_POINTER = 8;

    public static int getSizeInt() {
        return SIZE_INT;
    }

    public static int getSizeBool() {
        return SIZE_BOOL;
    }

    public static int getSizePointer() {
        return SIZE_POINTER;
    }
}
