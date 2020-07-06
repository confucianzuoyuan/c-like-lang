package AST;

import java.util.List;

import Symbol.FunctionType;

// 函数声明
public class FunctionDecl extends Decl {
    public final TypeNode returnType; // 返回值类型
    public final String name; // 函数名
    public final List<VariableDecl> argTypes; // 参数类型
    public final CompoundStmt body; // 函数体
    public FunctionType functionType; // 函数的类型

    public FunctionDecl(TypeNode returnType, String name, List<VariableDecl> argTypes, CompoundStmt body) {
        this.returnType = returnType;
        this.name = name;
        this.argTypes = argTypes;
        this.body = body;
    }

    @Override
    public void accept(IASTVisitor visitor) {
        visitor.visit(this);
    }
}