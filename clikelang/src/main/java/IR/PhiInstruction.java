package IR;

public class PhiInstruction extends IRInstruction {
    public PhiInstruction(BasicBlock BB) {
        super(BB);
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }
}
