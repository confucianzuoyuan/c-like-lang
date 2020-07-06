package AST;

import Symbol.Type;

public class FunctionTypeNode extends TypeNode {
    @Override
    public Type.Types getType() {
        return Type.Types.FUNCTION;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}
