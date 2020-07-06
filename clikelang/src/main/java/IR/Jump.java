package IR;

public class Jump extends BranchInstruction {
    private BasicBlock target; // 跳转到的目标基本块

    public Jump(BasicBlock BB, BasicBlock target) {
        super(BB); // 当前基本块
        this.target = target;
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }

    public BasicBlock getTarget() {
        return target;
    }
}
