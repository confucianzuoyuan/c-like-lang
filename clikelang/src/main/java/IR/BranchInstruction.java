package IR;

public abstract class BranchInstruction extends IRInstruction {
    public BranchInstruction(BasicBlock BB, IRInstruction prev, IRInstruction next) {
        super(BB, prev, next);
    }

    public BranchInstruction(BasicBlock BB) {
        super(BB);
    }
}
