package AST;

import IR.BasicBlock;
import IR.IntValue;
import Symbol.Type;

public abstract class Expr extends Stmt {
    public Type exprType;
    public boolean isLvalue = true; // 表达式是否为左值

    // for IR: condition check
    public BasicBlock ifTrue;
    public BasicBlock ifFalse;

    // for IR: expr value
    public IntValue intValue;
}