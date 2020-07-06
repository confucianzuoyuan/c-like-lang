package AST;

import java.util.ArrayList;
import java.util.List;

public class Program extends ASTNode {
    public final List<Decl> decls; // 程序是由一堆声明组成的

    public Program(List<Decl> decls) {
        this.decls = decls;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}