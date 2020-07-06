package AST;

// `--`表达式
public class SelfDecrement extends Expr {
    public final Expr self;

    public SelfDecrement(Expr self) {
        this.self  = self;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}