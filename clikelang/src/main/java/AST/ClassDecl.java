package AST;

import java.util.List;

// 类的声明
public class ClassDecl extends Decl {
    public final List<VariableDecl> members; // 成员变量声明列表
    public final String name;

    public ClassDecl(List<VariableDecl> members, String name) {
        this.members = members;
        this.name = name;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}