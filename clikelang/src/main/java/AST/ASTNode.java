package AST;

import BackEnd.Environment;
import Symbol.SymbolTable;

// 抽象语法树的抽象类，供其他节点类型继承
public abstract class ASTNode {
    // 注意使用访问者模式的方法
    public abstract void accept(IASTVisitor visitor);

    // 每个抽象语法树节点都要有当前节点属于的作用域
    public SymbolTable scope;

    public Environment env;
}