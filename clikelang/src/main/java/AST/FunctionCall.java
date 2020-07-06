package AST;

import java.util.ArrayList;
import java.util.List;

// 函数调用表达式
public class FunctionCall extends Expr {
    public final Expr name; // 函数的名字
    public final List<Expr> parameters; // 参数列表

    public FunctionCall(Expr name, List<Expr> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public static class Builder {
        private Expr name;
        private List<Expr> parameters = new ArrayList<>();

        public void setName(Expr name) {
            this.name = name;
        }


        public void addArg(Object arg) {
            if (arg instanceof Expr) parameters.add((Expr)arg);
            else throw new RuntimeException("Invalid type");
        }


        public FunctionCall build() {
            return new FunctionCall(name, parameters);
        }
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}