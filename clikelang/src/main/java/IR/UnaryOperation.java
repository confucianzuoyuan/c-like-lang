package IR;

public class UnaryOperation extends IRInstruction {
    public enum UnaryOp {
        NEG, NOT
    }

    private VirtualRegister dest;
    private UnaryOp op; // 操作符
    private IntValue operand; // 被操作数

    public UnaryOperation(BasicBlock BB, VirtualRegister dest, UnaryOp op, IntValue operand) {
        super(BB);
        this.dest = dest;
        this.op = op;
        this.operand = operand;
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }

    public VirtualRegister getDest() {
        return dest;
    }

    public UnaryOp getOp() {
        return op;
    }

    public IntValue getOperand() {
        return operand;
    }
}
