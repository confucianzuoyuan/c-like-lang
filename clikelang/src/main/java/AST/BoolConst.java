package AST;

// 布尔常量
public class BoolConst extends Expr {
    public final boolean value;

    public BoolConst(boolean value) {
        this.value = value;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}