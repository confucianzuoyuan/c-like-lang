package AST;

import java.util.LinkedList;
import java.util.List;

// 复合语句；也就是多条语句，所以放在列表里面
public class CompoundStmt extends Stmt {
    public final List<Stmt> stmts;

    public static class Builder {
        private final List<Stmt> stmts = new LinkedList<>();

        public void add(Object node) {
            if      (node instanceof Stmt) stmts.add((Stmt) node);
            else if (node instanceof List) {
                for (Object o : (List)node) {
                    add(o);
                }
            } else {
                throw new RuntimeException("CompoundStmt accepts Stmt only.");
            }
        }

        public CompoundStmt build() { return new CompoundStmt(stmts); }
    }

    public CompoundStmt(List<Stmt> stmts) {
        this.stmts = stmts;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}