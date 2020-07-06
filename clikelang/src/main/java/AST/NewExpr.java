package AST;

import java.util.ArrayList;
import java.util.List;

// 新建表达式节点
public class NewExpr extends Expr {
    public final TypeNode type; // 表达式的类型
    public final List<Expr> dim; // 表达式列表

    public NewExpr(TypeNode type, List<Expr> dim) {
        this.type = type;
        this.dim = dim;
    }

    public static class Builder {
        private TypeNode type;
        private final List<Expr> dim = new ArrayList<>();

        public void setType(TypeNode type) {
            this.type = type;
        }

        public void addDimension(Object node) {
            if (node instanceof Expr) dim.add((Expr)node);
            else if (node == null) dim.add(null);
            else throw new RuntimeException("Invalid type");
        }

        public NewExpr build() {
            return new NewExpr(type, dim);
        }
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}