package IR;

public interface IIRVisitor {
    void visit(IRRoot node);
    void visit(BasicBlock node);
    void visit(Function node);

    void visit(BinaryOperation node);
    void visit(UnaryOperation node);
    void visit(IntComparison node);
    void visit(IntImmediate node);
    void visit(Call node);
    void visit(PhiInstruction node);

    void visit(Branch node);
    void visit(Return node);
    void visit(Jump node);

    void visit(VirtualRegister node);
    void visit(HeapAllocate node);
    void visit(Load node);
    void visit(Store node);
    void visit(Move node);
}
