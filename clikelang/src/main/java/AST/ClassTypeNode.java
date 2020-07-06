package AST;

import Symbol.Type;

// 类的类型抽象语法树节点
public class ClassTypeNode extends TypeNode {
    public final String name; // 类的名字

    public ClassTypeNode(String name) {
        this.name = name;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Type.Types getType() {
        return Type.Types.CLASS;
    }
}
