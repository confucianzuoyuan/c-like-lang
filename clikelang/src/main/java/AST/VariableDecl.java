package AST;

// 变量声明语句节点
public class VariableDecl extends Decl {
    public TypeNode type; // 变量类型
    public String name;  // 变量名
    public Expr init; // 初始值

    public VariableDecl(TypeNode type, String name, Expr init) {
        this.type = type;
        this.name = name;
        this.init = init;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}