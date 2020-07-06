package IR;

public class Store extends IRInstruction {
    private int size; // 占用内存的大小
    private IntValue address; // 将value保存在某个地址对应的内存
    private IntValue value; // 保存的值

    public Store(BasicBlock BB, int size, IntValue address, IntValue value) {
        super(BB);
        this.size = size;
        this.address = address;
        this.value = value;
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }

    public int getSize() {
        return size;
    }

    public IntValue getAddress() {
        return address;
    }

    public IntValue getValue() {
        return value;
    }
}
