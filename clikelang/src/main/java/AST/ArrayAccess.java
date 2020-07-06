package AST;

// 访问数组中的元素
public class ArrayAccess extends Expr {

    public Expr array; // 数组
    public Expr subscript; // 下标

    public ArrayAccess(Expr array, Expr subscript) {
        this.array = array;
        this.subscript = subscript;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}