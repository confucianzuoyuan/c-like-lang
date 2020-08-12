package BackEnd;

import AST.*;
import Symbol.Type;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ASTInterpreter implements IASTVisitor {
    private Environment currentEnv;
    private final GlobalEnvironment globalEnvironment;

    public ASTInterpreter(GlobalEnvironment globalEnvironment) {
        this.globalEnvironment = globalEnvironment;
        this.currentEnv = globalEnvironment.globals;
    }

    @Override
    public void visit(WhileLoop node) {

    }

    @Override
    public void visit(IfStmt node) {
        node.env = currentEnv;
        visit(node.cond);

        currentEnv = new Environment(currentEnv);
        visit(node.then);
        currentEnv = currentEnv.getEnclosingEnv();

        if (node.otherwise != null) {
            currentEnv = new Environment(currentEnv);
            visit(node.otherwise);
            currentEnv = currentEnv.getEnclosingEnv();
        }
    }

    @Override
    public void visit(Identifier node) {
        node.env = currentEnv;
    }

    @Override
    public void visit(IntConst node) {
        node.env = currentEnv;
    }

    @Override
    public void visit(NullLiteral node) {
        node.env = currentEnv;
    }

    @Override
    public void visit(BinaryExpr node) {
        node.env = currentEnv;
        visit(node.lhs);
        visit(node.rhs);
    }

    @Override
    public void visit(BoolConst node) {
        node.env = currentEnv;
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
        node.env = currentEnv;
        visit(node.value);
    }

    @Override
    public void visit(StringConst node) {
        node.env = currentEnv;
    }

    @Override
    public void visit(UnaryExpr node) {
        node.env = currentEnv;
        visit(node.body);
    }

    @Override
    public void visit(ArrayAccess node) {
        node.env = currentEnv;
        visit(node.array);
        visit(node.subscript);
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
        node.env = currentEnv;
        if (node.init != null) {
            visit(node.init);
            currentEnv.define(node.name, node.init);
        } else {
            currentEnv.define(node.name, new IntConst(0));
        }
    }

    @Override
    public void visit(ArrayTypeNode node) {

    }

    @Override
    public void visit(VariableDeclStmt node) {
        node.decl.accept(this);
    }

    @Override
    public void visit(CompoundStmt node) {
        node.env = currentEnv;
        node.stmts.stream().forEachOrdered(x -> x.accept(this));
    }

    @Override
    public void visit(FunctionCall node) {
        node.env = currentEnv;
        visit(node.name);
    }

    @Override
    public void visit(FunctionDecl node) {

        this.globalEnvironment.defineObject(node.name, node);

        node.env = currentEnv;

        currentEnv = new Environment(currentEnv);
        node.argTypes.stream().forEachOrdered(x -> {
            x.env = currentEnv;
        });

        visit(node.body);

        currentEnv = currentEnv.getEnclosingEnv();
    }

    @Override
    public void visit(NewExpr node) {
        node.env = currentEnv;
        for (int i = 0; i < node.dim.size(); ++i) {
            Expr x = node.dim.get(i);
            if (x == null) break;
            visit(x);
        }
    }

    @Override
    public void visit(Program node) {
        node.env = globalEnvironment.globals;
        node.decls.stream().forEachOrdered(x -> x.accept(this));
    }

    @Override
    public void visit(ClassDecl node) {

    }

    @Override
    public void visit(ForLoop node) {
        node.env = currentEnv;
        node.initWithDecl.stream().forEachOrdered(x -> x.accept(this));
        visit(node.init);
        visit(node.cond);
        visit(node.step);
        visit(node.body);
    }

    @Override
    public void visit(PrimitiveTypeNode node) {

    }

    @Override
    public void visit(ClassTypeNode node) {

    }

    @Override
    public void visit(Expr node) {
        node.accept(this);
    }

    @Override
    public void visit(Stmt node) {
        node.accept(this);
    }

    @Override
    public void visit(Decl node) {
        node.accept(this);
    }

    @Override
    public void visit(ASTNode node) {
        node.accept(this);
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

    public void executeMain() {
        FunctionDecl main = (FunctionDecl) this.globalEnvironment.resolveObject("main");
        for (Stmt stmt : main.body.stmts) {
            executeStmt(stmt);
        }
    }

    public void executeStmt(Stmt stmt) {
        if (stmt instanceof VariableDeclStmt) {
            VariableDecl s = ((VariableDeclStmt) stmt).decl;

            if (s.init != null) {
                s.env.setInfo(s.name, evaluate(s.init));
            }
        }
        if (stmt instanceof CompoundStmt) {
            for (Stmt s : ((CompoundStmt) stmt).stmts) {
                executeStmt(s);
            }
        }
        if (stmt instanceof BinaryExpr) {
            if (((BinaryExpr) stmt).op == BinaryExpr.BinaryOp.ASSIGN) {
                if (((BinaryExpr) stmt).lhs instanceof Identifier) {
                    Identifier lhs = (Identifier) ((BinaryExpr) stmt).lhs;
                    IntConst rhs = (IntConst) ((BinaryExpr) stmt).rhs;

                    String name = lhs.name;
                    Integer value = rhs.value;

                    stmt.env.setInfo(name, value);
                }
                if (((BinaryExpr) stmt).lhs instanceof ArrayAccess) {
                    Identifier id = (Identifier)((ArrayAccess) ((BinaryExpr) stmt).lhs).array;
                    IntConst subscript = (IntConst) ((ArrayAccess) ((BinaryExpr) stmt).lhs).subscript;

                    Integer[] array = (Integer[]) stmt.env.getInfo(id.name);

                    array[subscript.value] = ((IntConst)((BinaryExpr) stmt).rhs).value;
                }
            }
        }
        if (stmt instanceof IfStmt) {
            if ((Boolean) evaluate(((IfStmt) stmt).cond)) {
                executeStmt(((IfStmt) stmt).then);
            } else {
                executeStmt(((IfStmt) stmt).otherwise);
            }
        }
        if (stmt instanceof ReturnStmt) {
            System.out.println(evaluate(((ReturnStmt) stmt).value));
        }
    }

    public Object evaluate(Expr expr) {
        if (expr instanceof Identifier) {
            return expr.env.getInfoCurrent(((Identifier) expr).name);
        }
        if (expr instanceof ArrayAccess) {
            Integer[] array = (Integer[]) expr.env.getInfo(((Identifier) ((ArrayAccess) expr).array).name);
            Integer subscript = (Integer) evaluate(((ArrayAccess) expr).subscript);
            return array[subscript];
        }
        if (expr instanceof BinaryExpr) {
            if (((BinaryExpr) expr).op == BinaryExpr.BinaryOp.DIV) {
                Integer lhs = (Integer) evaluate(((BinaryExpr) expr).lhs);
                Integer rhs = (Integer) evaluate(((BinaryExpr) expr).rhs);

                return lhs / rhs;
            }
            if (((BinaryExpr) expr).op == BinaryExpr.BinaryOp.ADD) {
                Integer lhs = (Integer) evaluate(((BinaryExpr) expr).lhs);
                Integer rhs = (Integer) evaluate(((BinaryExpr) expr).rhs);

                return lhs + rhs;
            }
            if (((BinaryExpr) expr).op == BinaryExpr.BinaryOp.NE) {
                Object lhs = evaluate(((BinaryExpr) expr).lhs);
                Object rhs = evaluate(((BinaryExpr) expr).rhs);

                return lhs == rhs;
            }
        }
        if (expr instanceof IntConst) {
            return ((IntConst) expr).value;
        }
        if (expr instanceof NewExpr) {
            TypeNode type = ((NewExpr) expr).type;
            if (type.getType() == Type.Types.INT) {
                int length = ((NewExpr) expr).dim.size();
                int[] dimensions = new int[length];
                for (int i = 0; i < length; i++) {
                    dimensions[i] = (Integer) evaluate(((NewExpr) expr).dim.get(i));
                }
                Object array = Array.newInstance(Integer.class, dimensions);
                return array;
            }
        }
        return null;
    }
}