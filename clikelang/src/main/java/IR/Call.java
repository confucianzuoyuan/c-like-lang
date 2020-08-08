package IR;

import java.util.ArrayList;
import java.util.List;

public class Call extends IRInstruction {
    private VirtualRegister dest; // 函数调用结果的寄存器地址？
    private Function func;
    private List<IntValue> args = new ArrayList<>();

    public Call(BasicBlock BB, VirtualRegister dest, Function func) {
        super(BB);
        this.dest = dest;
        this.func = func;
    }

    public VirtualRegister getDest() {
        return dest;
    }

    public Function getFunc() {
        return func;
    }

    public Call appendArg(IntValue arg) {
        args.add(arg);
        return this;
    }

    public List<IntValue> getArgs() {
        return args;
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }
}
