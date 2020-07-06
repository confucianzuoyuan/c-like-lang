package AST;

// 二元表达式
public class BinaryExpr extends Expr {
    // 二元运算符
    public enum BinaryOp {
        ASSIGN, // 赋值
        LOGICAL_OR, LOGICAL_AND, // `||`, `&&`
        BITWISE_OR, BITWISE_AND, XOR, // `|`, `&`, `^`
        EQ, NE, LT, GT, LE, GE, // `==`, `!=`, `<`, `>`, `>=`, `<=`
        SHL, SHR, // `<<`, `>>`
        ADD, SUB, // `+`, `-`
        MUL, DIV, MOD // `*`, `/`, `%`
    }

    public final BinaryOp op;
    public Expr lhs; // 二元运算符左边的表达式
    public Expr rhs; // 二元运算符右边的表达式

    public BinaryExpr(BinaryOp op, Expr lhs, Expr rhs) {
        this.op = op;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}