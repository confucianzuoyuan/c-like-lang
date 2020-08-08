package FrontEnd;

import AST.*;
import Symbol.ClassType;
import Symbol.GlobalSymbolTable;

public class ClassSymbolScanner implements IASTVisitor {

    private GlobalSymbolTable symbolTable;

    public ClassSymbolScanner(GlobalSymbolTable symbolTable) {
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
        if (symbolTable.resolveType(node.name) != null) {
            return;
        }
        symbolTable.defineType(node.name, new ClassType(node.name));
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