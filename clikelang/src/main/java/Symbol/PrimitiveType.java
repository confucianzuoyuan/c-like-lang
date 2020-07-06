package Symbol;

public class PrimitiveType extends VariableType {
    public PrimitiveType(Types type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "<PrimitiveType>" + this.type.name();
    }


    @Override
    public String toStructureString(String indent) {
        return indent + toString() + "\n";
    }

    @Override
    public boolean isSameType(Type rhs) {
        return type == rhs.type;
    }
}
