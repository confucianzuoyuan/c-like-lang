package AST;

// continue语句的抽象语法树
public class ContinueStmt extends Stmt {
    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}