package AST;

// 自增表达式节点，`++`
public class SelfIncrement extends Expr {
    public final Expr self;

    public SelfIncrement(Expr self) {
        this.self = self;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}