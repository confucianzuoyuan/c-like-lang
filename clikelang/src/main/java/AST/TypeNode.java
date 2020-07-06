package AST;

import Symbol.Type;

// 类型节点，int, void, ...
public abstract class TypeNode extends ASTNode {
    public abstract Type.Types getType();
}