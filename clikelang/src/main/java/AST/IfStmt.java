package AST;

public class IfStmt extends Stmt {
    public Expr cond; // 条件表达式
    public Stmt then; // 条件表达式为true时，执行的语句
    public Stmt otherwise; // 条件表达式为false，执行的语句

    public IfStmt(Expr cond, Stmt then, Stmt otherwise) {
        this.cond = cond;
        this.then = then;
        this.otherwise = otherwise;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}