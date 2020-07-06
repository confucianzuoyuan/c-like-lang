package AST;

import Symbol.SymbolInfo;

public class Identifier extends Expr {
    public final String name;

    public Identifier(String name) {
        this.name = name;
    }

    public SymbolInfo symbolInfo;

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}