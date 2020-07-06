package Symbol;

import Utils.CompilerOptions;

public class ClassType extends VariableType {
    public String name;
    public SymbolTable members;

    public ClassType(String name) {
        this.type = Types.CLASS;
        this.name = name;
        // 新建一个符号表，并且这个符号表是在最外层
        this.members = new SymbolTable(null);
    }

    @Override
    public String toString() {
        return "<ClassType>" + name;
    }

    @Override
    public String toStructureString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append(toString()).append("   {\n");
        sb.append(members.toStructureString(indent + "    "));
        sb.append(indent).append("}\n");
        return sb.toString();
    }

    @Override
    public boolean isSameType(Type rhs) {
        return rhs.type == Types.NULL || this == rhs;
    }

    @Override
    public int getRegisterSize() {
        return CompilerOptions.getSizePointer();
    }

    @Override
    public int getMemorySize() {
        return members.getSize();
    }

    @Override
    public boolean isPointerType() {
        return true;
    }
}
