package IR;

public class Move extends IRInstruction {
    private VirtualRegister dest; // 将source这个值存储到的目标寄存器
    private IntValue source; // 需要存储的值

    public Move(BasicBlock BB, VirtualRegister dest, IntValue source) {
        super(BB);
        this.dest = dest;
        this.source = source;
    }

    public VirtualRegister getDest() {
        return dest;
    }

    public IntValue getSource() {
        return source;
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }
}
