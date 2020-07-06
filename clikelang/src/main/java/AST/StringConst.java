package AST;

// 字符串常量
public class StringConst extends Expr {
    public final String value;

    public StringConst(String value) {
        this.value = value;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}