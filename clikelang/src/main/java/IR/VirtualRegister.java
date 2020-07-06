package IR;

// 虚拟寄存器可以有无数个
public class VirtualRegister extends IntValue {
    private String hintName; // 虚拟寄存器的名字

    public VirtualRegister(String hintName) {
        this.hintName = hintName;
    }

    @Override
    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }

    public String getHintName() {
        return hintName;
    }
}
