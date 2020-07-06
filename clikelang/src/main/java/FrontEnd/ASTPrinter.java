package FrontEnd;

import AST.*;

import java.io.PrintStream;

public class ASTPrinter implements IASTVisitor {
    private PrintStream out;
    private StringBuilder sb = new StringBuilder();

    public ASTPrinter(PrintStream out) {
        this.out = out;
    }

    private void indent() {
        sb.append("    ");
    }

    private void dedent() {
        sb.delete(sb.length() - 4, sb.length());
    }

    // 打印布尔常量节点
    @Override
    public void visit(BoolConst node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName() + ", value:" + node.value);
    }

    // break语句
    @Override
    public void visit(BreakStmt node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
    }

    // continue语句
    @Override
    public void visit(ContinueStmt node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
    }

    // 空语句
    @Override
    public void visit(EmptyExpr node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
    }

    // 原始类型节点
    @Override
    public void visit(PrimitiveTypeNode node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName() + ": " + node.getType().name());
    }

    // 访问while循环语句
    @Override
    public void visit(WhileLoop node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.cond);
        visit(node.body);
        dedent();
    }

    @Override
    public void visit(Expr node) {
        if (node == null) return;
        node.accept(this);
    }

    // 访问for循环
    @Override
    public void visit(ForLoop node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        if (node.initWithDecl != null) {
            for (VariableDecl iwd : node.initWithDecl) {
                visit(iwd);
            }
        }
        else {
            visit(node.init);
        }
        visit(node.cond);
        visit(node.step);
        visit(node.body);
        dedent();
    }

    // 函数声明
    @Override
    public void visit(FunctionDecl node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName() + ": " + node.name);
        indent();
        visit(node.returnType);
        for (VariableDecl at : node.argTypes) {
            visit(at);
        }
        visit(node.body);
        dedent();
    }

    // 访问标识符节点
    @Override
    public void visit(Identifier node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName() + ", name:" + node.name);
    }

    // 访问条件语句
    @Override
    public void visit(IfStmt node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.cond);
        visit(node.then);
        visit(node.otherwise);
        dedent();
    }

    // 访问整型常量
    @Override
    public void visit(IntConst node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName() + ", value:" + node.value);
    }

    // 访问新建表达式节点
    @Override
    public void visit(NewExpr node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.type);
        for (Expr expr : node.dim) {
            visit(expr);
        }
        dedent();
    }

    // 访问null字面量节点
    @Override
    public void visit(NullLiteral node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
    }

    @Override
    public void visit(Program node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        for (Decl decl : node.decls) {
            visit(decl);
        }
        dedent();
    }

    // 类成员访问节点
    @Override
    public void visit(MemberAccess node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.record);
        out.println(sb.toString() + "member: " + node.member);
        dedent();
    }

    // 类的声明
    @Override
    public void visit(ClassDecl node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName() + ": " + node.name);
        indent();
        for (VariableDecl member : node.members) {
            visit(member);
        }
        dedent();
    }

    // 一种类就是一种类型
    @Override
    public void visit(ClassTypeNode node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName() + ": " + node.name);
    }

    // 访问变量声明
    @Override
    public void visit(VariableDecl node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName() + ": " + node.name);
        indent();
        visit(node.type);
        visit(node.init);
        dedent();
    }

    // 变量声明语句
    @Override
    public void visit(VariableDeclStmt node) {
        if (node == null) return;
        out.print(sb.toString() + node.getClass().getSimpleName() + " -> ");
        if (node.decl != null) {
            visit(node.decl);
        } else {
            out.println();
        }
    }

    // 访问一元表达式
    @Override
    public void visit(UnaryExpr node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.op);
        visit(node.body);
        dedent();
    }

    // 访问字符串常量
    @Override
    public void visit(StringConst node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName() + ", value:" + node.value);
    }

    // 访问自增表达式节点
    @Override
    public void visit(SelfIncrement node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.self);
        dedent();
    }

    // 访问自减表达式
    @Override
    public void visit(SelfDecrement node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.self);
        dedent();
    }

    // 访问return表达式
    @Override
    public void visit(ReturnStmt node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.value);
        dedent();
    }

    // 函数调用节点
    @Override
    public void visit(FunctionCall node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.name);
        for (Expr parameter : node.parameters) {
            visit(parameter);
        }
        dedent();
    }

    // 访问数组类型节点
    @Override
    public void visit(ArrayTypeNode node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.baseType);
        dedent();
    }

    // 访问二元表达式
    @Override
    public void visit(BinaryExpr node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.op);
        visit(node.lhs);
        visit(node.rhs);
        dedent();
    }

    @Override
    public void visit(ASTNode node) {
        if (node == null) return;
        node.accept(this);
    }

    // 访问二元运算符
    @Override
    public void visit(BinaryExpr.BinaryOp node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName() + node);
    }

    @Override
    public void visit(Stmt node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(Decl node) {
        if (node == null) return;
        node.accept(this);
    }

    // 数组访问
    @Override
    public void visit(ArrayAccess node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        visit(node.array);
        visit(node.subscript);
        dedent();
    }

    @Override
    public void visit(UnaryExpr.UnaryOp node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName() + node);
    }

    @Override
    public void visit(FunctionTypeNode node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
    }

    @Override
    public void visit(CompoundStmt node) {
        if (node == null) return;
        out.println(sb.toString() + node.getClass().getSimpleName());
        indent();
        for (Stmt stmt : node.stmts) {
            visit(stmt);
        }
        dedent();
    }


}