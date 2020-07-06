package IR;

public class BasicBlock {
    private IRInstruction head = null; // 基本块的第一条ir指令
    private IRInstruction last = null; // 基本块的最后一条ir指令
    private boolean ended = false; // 基本块结束了没有
    private String hintName; // 基本块的名字

    // 基本块的名字是什么
    public BasicBlock(String hintName) {
        this.hintName = hintName != null ? hintName : "block";
    }

    public void append(IRInstruction next) {
        if (ended) {
            throw new RuntimeException("在基本块的结尾无法添加新指令！");
        }
        if (last != null) {
            last.linkNext(next);
            last = next;
        } else {
            head = last = next;
        }
    }

    // 将跳转指令添加到基本块的最后，因为将要从当前基本块跳转到其他基本块了
    // 所以跳转指令肯定是基本块的最后一条指令
    public void end(BranchInstruction next) {
        append(next);
        ended = true;
    }

    // 基本块结束了没有
    public boolean isEnded() {
        return ended;
    }

    public void setHead(IRInstruction head) {
        this.head = head;
    }

    public void setLast(IRInstruction last) {
        this.last = last;
    }

    public IRInstruction getHead() {
        return head;
    }

    public IRInstruction getLast() {
        return last;
    }

    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }

    public String getHintName() {
        return hintName;
    }
}
