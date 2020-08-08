package FrontEnd;

import AST.*;
import Symbol.*;

public class ClassFunctionDeclarator implements IASTVisitor {
    private GlobalSymbolTable symbolTable;

    public ClassFunctionDeclarator(GlobalSymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public void visit(WhileLoop node) {

    }

    @Override
    public void visit(IfStmt node) {

    }

    @Override
    public void visit(Identifier node) {

    }

    @Override
    public void visit(IntConst node) {

    }

    @Override
    public void visit(NullLiteral node) {

    }

    @Override
    public void visit(BinaryExpr node) {

    }

    @Override
    public void visit(BoolConst node) {

    }

    @Override
    public void visit(EmptyExpr node) {

    }

    @Override
    public void visit(ContinueStmt node) {

    }

    @Override
    public void visit(BreakStmt node) {

    }

    @Override
    public void visit(ReturnStmt node) {

    }

    @Override
    public void visit(StringConst node) {

    }

    @Override
    public void visit(UnaryExpr node) {

    }

    @Override
    public void visit(ArrayAccess node) {

    }

    @Override
    public void visit(MemberAccess node) {

    }

    @Override
    public void visit(SelfIncrement node) {

    }

    @Override
    public void visit(SelfDecrement node) {

    }

    @Override
    public void visit(VariableDecl node) {

    }

    @Override
    public void visit(ArrayTypeNode node) {

    }

    @Override
    public void visit(VariableDeclStmt node) {

    }

    @Override
    public void visit(CompoundStmt node) {

    }

    @Override
    public void visit(FunctionCall node) {

    }

    @Override
    public void visit(FunctionDecl node) {
        if (symbolTable.globals.getType(node.name) != null) {
            throw new RuntimeException(node.name + "函数已经被声明过了！");
        }
        VariableType returnType = symbolTable.resolveVariableTypeFromAST(node.returnType);
        if (returnType == null) {
            throw new RuntimeException("返回值类型不存在！");
        }
        if (node.name.equals("main") && (returnType.type != Type.Types.INT || node.argTypes.size() != 0)) {
            throw new RuntimeException("`main`函数只能命名为`int main()`");
        }
        FunctionType func = new FunctionType(returnType, node.name);

        for (VariableDecl x : node.argTypes) {
            if (x.init != null) {
                throw new RuntimeException("参数声明中不允许对参数进行初始化！");
            }
            VariableType type = symbolTable.resolveVariableTypeFromAST(x.type);
            if (type == null) {
                throw new RuntimeException("类型不存在！");
            }
            func.argTypes.add(type);
            func.argNames.add(x.name);
        }
        node.functionType = func;
        symbolTable.globals.define(node.name, func);
    }

    @Override
    public void visit(NewExpr node) {

    }

    @Override
    public void visit(Program node) {
        node.decls.stream().forEachOrdered(x -> x.accept(this));
    }

    @Override
    public void visit(ClassDecl node) {
        ClassType c = (ClassType) symbolTable.resolveType(node.name);
        node.members.stream().forEachOrdered(x -> {
            if (x.init != null) {
                return;
            }
            if (c.members.getTypeCurrent(x.name) != null) {
                return;
            }
            VariableType type = symbolTable.resolveVariableTypeFromAST(x.type);
            if (type == null) {
                return;
            }
            c.members.define(x.name, type);
        });

    }

    @Override
    public void visit(ForLoop node) {

    }

    @Override
    public void visit(PrimitiveTypeNode node) {

    }

    @Override
    public void visit(ClassTypeNode node) {

    }

    @Override
    public void visit(Expr node) {

    }

    @Override
    public void visit(Stmt node) {

    }

    @Override
    public void visit(Decl node) {

    }

    @Override
    public void visit(ASTNode node) {

    }

    @Override
    public void visit(BinaryExpr.BinaryOp node) {

    }

    @Override
    public void visit(UnaryExpr.UnaryOp node) {

    }

    @Override
    public void visit(FunctionTypeNode node) {

    }
}