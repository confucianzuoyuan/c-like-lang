package IR;

// 把address所表示的内存地址中的字数据装载到目标寄存器dest中
public class Load extends IRInstruction {
    private VirtualRegister dest;
    private int size;
    private IntValue address;

    public Load(BasicBlock BB, VirtualRegister dest, int size, IntValue address) {
        super(BB);
        this.dest = dest;
        this.size = size;
        this.address = address;
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }

    public VirtualRegister getDest() {
        return dest;
    }

    public int getSize() {
        return size;
    }

    public IntValue getAddress() {
        return address;
    }
}
