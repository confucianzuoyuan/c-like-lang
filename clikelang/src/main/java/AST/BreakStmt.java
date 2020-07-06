package AST;

// break语句
public class BreakStmt extends Stmt {
    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}