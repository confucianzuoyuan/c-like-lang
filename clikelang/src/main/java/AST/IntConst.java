package AST;

// 整形常量
public class IntConst extends Expr {
    public final int value;

    public IntConst(int value) {
        this.value = value;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}