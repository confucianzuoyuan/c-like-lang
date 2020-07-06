package AST;

// while循环语句，包括条件和循环体
public class WhileLoop extends Stmt {
    public Expr cond; // 条件表达式
    public Stmt body; // 循环体

    public WhileLoop(Expr cond, Stmt body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}