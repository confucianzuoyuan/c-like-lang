package AST;

// 空表达式节点
public class EmptyExpr extends Expr {
    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}