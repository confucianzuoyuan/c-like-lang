package Symbol;

import Utils.CompilerOptions;

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

    @Override
    public boolean isPointerType() {
        switch (type) {
            case INT:
            case BOOL:
                return false;
            default:
                return true;
        }
    }

    @Override
    public int getRegisterSize() {
        switch (type) {
            case INT:
                return CompilerOptions.getSizeInt();
            case BOOL:
                return CompilerOptions.getSizeBool();
            default:
                return CompilerOptions.getSizePointer();
        }
    }

    @Override
    public int getMemorySize() {
        return getRegisterSize();
    }
}
