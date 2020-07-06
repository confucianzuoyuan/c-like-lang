package AST;

// `null`字面量
public class NullLiteral extends Expr {
    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}