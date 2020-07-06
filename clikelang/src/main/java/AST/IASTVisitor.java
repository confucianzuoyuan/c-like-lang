package AST;

// 访问者模式的接口
public interface IASTVisitor {
    void visit(WhileLoop node);
    void visit(IfStmt node);
    void visit(Identifier node);
    void visit(IntConst node);
    void visit(NullLiteral node);
    void visit(BinaryExpr node);
    void visit(BoolConst node);
    void visit(EmptyExpr node);
    void visit(ContinueStmt node);
    void visit(BreakStmt node);
    void visit(ReturnStmt node);
    void visit(StringConst node);
    void visit(UnaryExpr node);
    void visit(ArrayAccess node);
    void visit(MemberAccess node);
    void visit(SelfIncrement node);
    void visit(SelfDecrement node);
    void visit(VariableDecl node);
    void visit(ArrayTypeNode node);
    void visit(VariableDeclStmt node);
    void visit(CompoundStmt node);
    void visit(FunctionCall node);
    void visit(FunctionDecl node);
    void visit(NewExpr node);
    void visit(Program node);
    void visit(ClassDecl node);
    void visit(ForLoop node);
    void visit(PrimitiveTypeNode node);
    void visit(ClassTypeNode node);

    void visit(Expr node);
    void visit(Stmt node);
    void visit(Decl node);
    void visit(ASTNode node);
    void visit(BinaryExpr.BinaryOp node);
    void visit(UnaryExpr.UnaryOp node);

    void visit(FunctionTypeNode node);
}