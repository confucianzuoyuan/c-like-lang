package IR;

public class Return extends BranchInstruction {
    private IntValue ret; // 将要return的结果

    public Return(BasicBlock BB, IntValue ret) {
        super(BB);
        this.ret = ret;
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }

    // 获取返回值
    public IntValue getRet() {
        return ret;
    }
}
