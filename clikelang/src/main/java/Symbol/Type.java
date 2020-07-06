package Symbol;

public abstract class Type {
    public enum Types {
        INT, BOOL, STRING, VOID, ARRAY, CLASS, FUNCTION, NULL
    }

    public Types type;

    public abstract String toStructureString(String indent);

    public abstract boolean isSameType(Type rhs);

    public abstract int getRegisterSize();

    public abstract int getMemorySize();

    public abstract boolean isPointerType();
}