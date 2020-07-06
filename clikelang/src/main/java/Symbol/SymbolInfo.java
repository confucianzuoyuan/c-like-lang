package Symbol;

public class SymbolInfo {
    private Type type;
    private int offset;

    public SymbolInfo(Type type, int offset) {
        this.type = type;
        this.offset = offset;
    }

    public Type getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }
}
