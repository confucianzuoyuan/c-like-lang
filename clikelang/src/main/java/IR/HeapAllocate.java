package IR;

public class HeapAllocate extends IRInstruction {
    private VirtualRegister dest;
    private IntValue allocSize; // 分配的内存大小

    public HeapAllocate(BasicBlock BB, VirtualRegister dest, IntValue allocSize) {
        super(BB);
        this.dest = dest;
        this.allocSize = allocSize;
    }

    public IntValue getAllocSize() {
        return allocSize;
    }

    public VirtualRegister getDest() {
        return dest;
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }
}
