package AST;

// 一元表达式
public class UnaryExpr extends Expr {

    public enum UnaryOp {
        INC, DEC, POS, NEG, // `+`, `-`, `++`, `--`
        LOGICAL_NOT, BITWISE_NOT // `!`, `~`
    }

    // -1
    public UnaryOp op; // -
    public Expr body;  // 1

    public UnaryExpr(UnaryOp op, Expr body) {
        this.op = op;
        this.body = body;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}