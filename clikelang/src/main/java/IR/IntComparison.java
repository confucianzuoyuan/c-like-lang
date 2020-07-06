package IR;

// 整数比较IR指令
public class IntComparison extends IRInstruction {
    public enum Condition {
        EQ, NE, GT, GE, LT, LE
    }

    private VirtualRegister dest; // 比较结果将保存在目标寄存器？
    private Condition cond;
    private IntValue lhs;
    private IntValue rhs;

    public IntComparison(BasicBlock BB, VirtualRegister dest, Condition cond, IntValue lhs, IntValue rhs) {
        super(BB);
        this.dest = dest;
        this.cond = cond;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public VirtualRegister getDest() {
        return dest;
    }

    public Condition getCond() {
        return cond;
    }

    public IntValue getLhs() {
        return lhs;
    }

    public IntValue getRhs() {
        return rhs;
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }
}
