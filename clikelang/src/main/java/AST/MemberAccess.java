package AST;

// 访问类的成员变量
public class MemberAccess extends Expr {
    public final Expr record; // 类表达式
    public final String member; // 成员

    public MemberAccess(Expr record, String member) {
        this.record = record;
        this.member = member;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}