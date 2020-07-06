package AST;

import Symbol.Type;

// 数组类型节点
public class ArrayTypeNode extends TypeNode {
    public final TypeNode baseType; // 数组中元素的类型

    public ArrayTypeNode(TypeNode baseType) {
        this.baseType = baseType;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Type.Types getType() {
        return Type.Types.ARRAY;
    }
}