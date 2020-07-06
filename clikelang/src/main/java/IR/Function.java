package IR;

import Symbol.FunctionType;

import java.util.HashMap;
import java.util.Map;

public class Function {
    private Map<String, VirtualRegister> varReg = new HashMap<>();
    private String name;
    private BasicBlock startBB; // 函数的开始基本块
    private FunctionType type;
    private int retSize;

    public FunctionType getType() {
        return type;
    }

    // 返回值使用8个字节大小的寄存器
    public Function(FunctionType type) {
        this.retSize = type.returnType.getRegisterSize();
        this.name = type.name;
        this.type = type;
        this.startBB = new BasicBlock(name + "_start");
    }

    // 函数的每一个形式参数对应一个虚拟寄存器
    public void defineVarReg(String name, VirtualRegister reg) {
        varReg.put(name, reg);
    }

    public VirtualRegister getVarReg(String name) {
        return varReg.get(name);
    }

    public String getName() {
        return name;
    }

    public BasicBlock getStartBB() {
        return startBB;
    }

    public int getRetSize() {
        return retSize;
    }

    public void setStartBB(BasicBlock startBB) {
        this.startBB = startBB;
    }

    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }
}
