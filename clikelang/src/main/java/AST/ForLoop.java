package AST;

import java.util.List;

public class ForLoop extends Stmt {
    public List<VariableDecl> initWithDecl;
    public Expr init;
    public Expr cond;
    public Expr step;
    public Stmt body;

    public ForLoop(Expr init, Expr cond, Expr step, Stmt body) {
        this.initWithDecl = null;
        this.init = init;
        this.cond = cond;
        this.step = step;
        this.body = body;
    }

    public ForLoop(List<VariableDecl> initWithDecl, Expr cond, Expr step, Stmt body) {
        this.initWithDecl = initWithDecl;
        this.init = null;
        this.body = body;
        this.cond = cond;
        this.step = step;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}
