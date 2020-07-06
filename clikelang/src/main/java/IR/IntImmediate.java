package IR;

// 立即数，比如：1，2，3
public class IntImmediate extends IntValue {
    private int value;

    public IntImmediate(int value) {
        this.value = value;
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }

    public int getValue() {
        return value;
    }
}
