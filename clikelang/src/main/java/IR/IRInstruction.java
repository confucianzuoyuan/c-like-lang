package IR;

// IR应该是个链表
public abstract class IRInstruction {
    // 当前基本块，只有一个入口，只有一个出口，相当于顺序执行的一段代码
    protected BasicBlock curBB;
    // 基本块节点的前驱和后继
    private IRInstruction prev = null;
    private IRInstruction next = null;

    public IRInstruction(BasicBlock curBB, IRInstruction prev, IRInstruction next) {
        this.curBB = curBB;
        this.prev = prev;
        this.next = next;
    }

    public IRInstruction(BasicBlock curBB) {
        this.curBB = curBB;
    }

    public void linkNext(IRInstruction node) {
        next = node;
        node.prev = this;
    }

    public void linkPrev(IRInstruction node) {
        prev = node;
        node.next = this;
    }

    public abstract void accept(IIRVisitor visitor);

    public IRInstruction getPrev() {
        return prev;
    }

    public IRInstruction getNext() {
        return next;
    }

    public BasicBlock getBasicBlock() {
        return curBB;
    }


}
