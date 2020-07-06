package Symbol;

import Utils.CompilerOptions;

import java.util.ArrayList;
import java.util.List;

public class FunctionType extends Type {
    public VariableType returnType;
    public String name;
    public List<VariableType> argTypes = new ArrayList<>();
    public List<String> argNames = new ArrayList<>();

    public FunctionType(VariableType returnType, String name) {
        this.returnType = returnType;
        this.name = name;
        this.type = Types.FUNCTION;
    }

    public void addArg(String name, VariableType type) {
        argNames.add(name);
        argTypes.add(type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<FunctionType>");
        sb.append("name: ").append(name);
        sb.append(", retType: ").append(returnType);
        sb.append(", argTypes: [");
        argTypes.stream().forEachOrdered(x -> sb.append(x).append(", "));
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String toStructureString(String indent) {
        return indent + toString() + "\n";
    }

    @Override
    public boolean isSameType(Type rhs) {
        return this == rhs;
    }

    // 寄存器的大小：8个字节
    @Override
    public int getRegisterSize() {
        return CompilerOptions.getSizePointer();
    }

    // 占用内存的大小：8个字节
    @Override
    public int getMemorySize() {
        return CompilerOptions.getSizePointer();
    }

    @Override
    public boolean isPointerType() {
        return true;
    }
}
