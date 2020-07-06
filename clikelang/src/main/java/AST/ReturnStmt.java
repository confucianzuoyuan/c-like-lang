package AST;

public class ReturnStmt extends Stmt {
    public Expr value; // 返回值表达式

    public ReturnStmt(Expr value) {
        this.value = value;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}