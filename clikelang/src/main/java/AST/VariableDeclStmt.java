package AST;

public class VariableDeclStmt extends Stmt {
    public final VariableDecl decl;

    public VariableDeclStmt(VariableDecl decl) {
        this.decl = decl;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}