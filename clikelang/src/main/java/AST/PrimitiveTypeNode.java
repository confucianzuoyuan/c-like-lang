package AST;

import Symbol.Type;

// 原始数据类型的抽象语法树的节点
public class PrimitiveTypeNode extends TypeNode {
    private Type.Types type;

    public PrimitiveTypeNode(Type.Types type) {
        this.type = type;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Type.Types getType() {
        return type;
    }
}
