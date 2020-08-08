package FrontEnd;

import AST.*;
import Symbol.*;

import java.util.Stack;

import static Symbol.GlobalSymbolTable.*;

public class SemanticChecker implements IASTVisitor {
    private final GlobalSymbolTable globalSymbolTable;
    private SymbolTable currentScope;

    private final Stack<ASTNode> loopASTStack = new Stack<>();
    private ASTNode currentFunction;
    private FunctionType currentFunctionType;

    public SemanticChecker(GlobalSymbolTable globalSymbolTable) {
        this.globalSymbolTable = globalSymbolTable; // 全剧符号表
        this.currentScope = globalSymbolTable.globals; // 当前作用域为globals
    }

    @Override
    public void visit(Program node) {
        node.scope = globalSymbolTable.globals;
        node.decls.stream().forEachOrdered(x -> x.accept(this));
    }

    @Override
    public void visit(ClassDecl node) {
        node.scope = globalSymbolTable.globals;
    }

    @Override
    public void visit(CompoundStmt node) {
        node.scope = currentScope;
        node.stmts.stream().forEachOrdered(x -> x.accept(this));
    }

    @Override
    public void visit(VariableDeclStmt node) {
        node.scope = currentScope;
        visit(node.decl);
    }

    @Override
    public void visit(VariableDecl node) {
        node.scope = currentScope;
        if (currentScope.getTypeCurrent(node.name) != null) {
            throw new RuntimeException("当前作用域中已经定义了这个变量！");
        }
        // 确定变量类型是：int，array，class，bool，void之类的基本数据类型
        Type type = globalSymbolTable.resolveVariableTypeFromAST(node.type);
        if (type == null) {
            throw new RuntimeException("无法确定这个变量的类型是什么！");
        }
        if (type.type == Type.Types.VOID) {
            throw new RuntimeException("不能使用void关键字声明变量！");
        }

        if (node.init != null) {
            visit(node.init); // 访问初始化的表达式，将表达式里的东西放进各自的作用域
            if (!type.isSameType(node.init.exprType)) {
                throw new RuntimeException("变量类型和初始化变量的表达式的类型必须一致！");
            }
        }

        currentScope.define(node.name, type);
    }

    @Override
    public void visit(FunctionDecl node) {
        node.scope = currentScope;
        currentFunction = node.body;
        currentFunctionType = (FunctionType)globalSymbolTable.globals.getType(node.name);
        // 新建符号表，并将globals顶层作用域作为外层作用域
        currentScope = new SymbolTable(globalSymbolTable.globals);
        node.argTypes.stream().forEachOrdered(x -> {
            currentScope.define(x.name, globalSymbolTable.resolveVariableTypeFromAST(x.type));
            x.scope = currentScope;
        });

        visit(node.body);

        currentFunction = null;
        currentFunctionType = null;
        currentScope = currentScope.getEnclosingScope();
    }

    @Override
    public void visit(BreakStmt node) {
        node.scope = currentScope;
        if (loopASTStack.empty()) {
            throw new RuntimeException("break语句必须在循环体内！");
        }
    }

    @Override
    public void visit(ContinueStmt node) {
        node.scope = currentScope;
        if (loopASTStack.empty()) {
            throw new RuntimeException("continue语句必须在循环体内！");
        }
    }

    @Override
    public void visit(ReturnStmt node) {
        node.scope = currentScope;
        if (currentFunction == null) {
            throw new RuntimeException("return语句必须在函数体内！");
        }
        Type retType;
        if (node.value != null) {
            visit(node.value);
            retType = node.value.exprType;
        } else {
            retType = voidType;
        }

        if (!currentFunctionType.returnType.isSameType(retType)) {
            throw new RuntimeException("返回值类型和函数定义中的返回值类型不一致！");
        }
    }

    @Override
    public void visit(IfStmt node) {
        node.scope = currentScope;
        visit(node.cond); // 访问条件表达式
        Type condType = node.cond.exprType; // 条件表达式的类型
        if (!condType.isSameType(boolType)) {
            throw new RuntimeException("条件表达式的类型必须是bool类型！");
        }
        currentScope = new SymbolTable(currentScope); // 进入新的作用域
        visit(node.then);
        currentScope = currentScope.getEnclosingScope(); // 退出作用域
        if (node.otherwise != null) {
            currentScope = new SymbolTable(currentScope); // 进入otherwise分支的作用域
            visit(node.otherwise);
            currentScope = currentScope.getEnclosingScope(); // 退出otherwise分支的作用域
        }
    }

    @Override
    public void visit(ForLoop node) {
        node.scope = currentScope;
        // init, cond, step在同一个作用域内
        currentScope = new SymbolTable(currentScope);

        if (node.init != null) {
            visit(node.init);
        } else if (node.initWithDecl != null) {
            for (VariableDecl v : node.initWithDecl) {
                v.accept(this);
            }
        }

        if (node.cond != null) {
            visit(node.cond);
            if (!node.cond.exprType.isSameType(boolType)) {
                throw new RuntimeException("For循环的条件表达式必须是bool类型！");
            }
        }

        if (node.step != null) {
            visit(node.step);
        }

        // 处理循环
        loopASTStack.push(node);
        visit(node.body);
        loopASTStack.pop();

        currentScope = currentScope.getEnclosingScope(); // 退出作用域

    }

    @Override
    public void visit(WhileLoop node) {
        node.scope = currentScope;
        currentScope = new SymbolTable(currentScope);
        visit(node.cond);
        if (!node.cond.exprType.isSameType(boolType)) {
            throw new RuntimeException("While循环的条件表达式必须是bool类型！");
        }
        loopASTStack.push(node);
        visit(node.body);
        loopASTStack.pop();

        currentScope = currentScope.getEnclosingScope();
    }

    @Override
    public void visit(ArrayAccess node) {
        // 使用数组下标访问数组元素
        node.scope = currentScope;
        visit(node.array);
        visit(node.subscript);

        if (node.array.exprType.type != Type.Types.ARRAY) {
            throw new RuntimeException("非数组的表达式不能使用数组下标语法访问！");
        }

        if (node.subscript.exprType.type != Type.Types.INT) {
            throw new RuntimeException("数组下标必须为整型！");
        }

        // node的类型就是数组中元素的类型
        node.exprType = ((ArrayType)node.array.exprType).bodyType;
        node.isLvalue = true; // a[1]可以是左值
    }

    @Override
    public void visit(UnaryExpr node) {
        node.scope = currentScope;
        visit(node.body);
        switch (node.op) {
            case INC: // ++i
            case DEC: // --i
                if (!node.body.isLvalue) {
                    throw new RuntimeException("自增自减表达式必须是左值！");
                }
                if (node.body.exprType.type != Type.Types.INT) {
                    throw new RuntimeException("自增自减表达式必须是整型！");
                }
                node.exprType = intType;
                node.isLvalue = true;
                return;

            case POS:
            case NEG:
            case BITWISE_NOT:
                if (node.body.exprType.type != Type.Types.INT) {
                    throw new RuntimeException("+/-/~只能在整型上进行使用！");
                }
                node.exprType = intType;
                node.isLvalue = false;
                return;

            case LOGICAL_NOT:
                if (node.body.exprType.type != Type.Types.BOOL) {
                    throw new RuntimeException("逻辑非只能使用在bool类型上面！");
                }
                node.exprType = boolType;
                node.isLvalue = false;
                return;
        }
    }

    @Override
    public void visit(BinaryExpr node) {
        node.scope = currentScope;
        visit(node.lhs);
        visit(node.rhs);
        if (!node.lhs.exprType.isSameType(node.rhs.exprType)) {
            throw new RuntimeException("二元表达式的左右表达式的类型必须相同！");
        }
        Type.Types operandType = node.lhs.exprType.type;
        switch (node.op) {
            case ASSIGN:
                if (!node.lhs.isLvalue) {
                    throw new RuntimeException("赋值表达式的左边必须是左值！");
                }
                node.exprType = node.rhs.exprType;
                node.isLvalue = true; // a = b = 1; 赋值表达式也可以被赋值
                return;
            case LOGICAL_OR:
            case LOGICAL_AND:
                if (operandType != Type.Types.BOOL) {
                    throw new RuntimeException("二元运算符必须是布尔运算符！");
                }
                node.exprType = boolType;
                node.isLvalue = false;
                return;
            case EQ:
            case NE:
                node.exprType = boolType;
                node.isLvalue = false;
                return;

            case LT:
            case GT:
            case LE:
            case GE:
                if (operandType != Type.Types.INT && operandType != Type.Types.STRING) {
                    throw new RuntimeException("二元运算符必须用来比较整型或者字符串类型！");
                }
                node.exprType = boolType;
                node.isLvalue = false; // `a > b`不能被赋值
                return;

            case BITWISE_OR:
            case BITWISE_AND:
            case XOR:
            case SHL:
            case SHR:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
                if (operandType != Type.Types.INT) {
                    throw new RuntimeException("位运算必须用于整型！");
                }
                node.exprType = intType;
                node.isLvalue = false; // `a & b` 不能被赋值
                return;

            case ADD:
                if (operandType != Type.Types.INT && operandType != Type.Types.STRING) {
                    throw new RuntimeException("加法运算符必须用来对整型或者字符串类型进行相加！");
                }
                node.exprType = operandType == Type.Types.INT ? intType : stringType;
                node.isLvalue = false;
        }
    }

    @Override
    public void visit(FunctionTypeNode node) {
    }

    @Override
    public void visit(ClassTypeNode node) {
    }


    @Override
    public void visit(EmptyExpr node) {
        node.scope = currentScope;
        node.exprType = voidType;
        node.isLvalue = false;
    }

    @Override
    public void visit(ArrayTypeNode node) {
    }

    @Override
    public void visit(PrimitiveTypeNode node) {
    }

    @Override
    public void visit(FunctionCall node) {
        node.scope = currentScope;
        visit(node.name);

        FunctionType functionType = (FunctionType)node.name.exprType;

        if (functionType.type != Type.Types.FUNCTION) {
            throw new RuntimeException("只有函数类型可以被调用！");
        }

        node.exprType = functionType.returnType;
        node.isLvalue = false;
    }

    @Override
    public void visit(NewExpr node) {
        node.scope = currentScope;
        VariableType type = globalSymbolTable.resolveVariableTypeFromAST(node.type);
        for (int i = 0; i < node.dim.size(); ++i) {
            Expr x = node.dim.get(i);
            if (x == null) break;
            visit(x);
            if (x.exprType.type != Type.Types.INT) {
                throw new RuntimeException("类似于int a[1][]这样的声明，类型必须是整型！");
            }
        }
        for (int i = 0; i < node.dim.size(); ++i)
            type = new ArrayType(type);
        node.exprType = type;
        node.isLvalue = false;
    }

    @Override
    public void visit(MemberAccess node) {
        node.scope = currentScope;
        visit(node.record);
        Type.Types recordType = node.record.exprType.type;
        if (recordType == Type.Types.CLASS) {
            ClassType s = (ClassType)node.record.exprType;
            VariableType t = (VariableType)s.members.getTypeCurrent(node.member);
            if (t == null) {
                throw new RuntimeException("没有名为" + node.member + "的成员！");
            }
            node.exprType = t;
            node.isLvalue = true; // 成员变量可以被赋值
        } else {
            throw new RuntimeException("成员访问只允许在类语法中！");
        }
    }

    @Override
    public void visit(SelfDecrement node) {
        node.scope = currentScope;
        visit(node.self);

        if (!node.self.isLvalue) {
            throw new RuntimeException("自减运算符只能在左值上运算！"); // i--
        }

        if (node.self.exprType.type != Type.Types.INT) {
            throw new RuntimeException("表达式的类型必须是int！");
        }

        node.exprType = intType;
        node.isLvalue = false;
    }

    @Override
    public void visit(SelfIncrement node) {
        node.scope = currentScope;
        visit(node.self);

        if (!node.self.isLvalue) {
            throw new RuntimeException("自增运算符只能在左值上运算！"); // i++
        }

        if (node.self.exprType.type != Type.Types.INT) {
            throw new RuntimeException("表达式的类型必须是int！");
        }

        node.exprType = intType;
        node.isLvalue = false;
    }

    @Override
    public void visit(Identifier node) {
        node.scope = currentScope;
        SymbolInfo info = currentScope.getInfo(node.name);
        if (info == null) {
            throw new RuntimeException("找不到变量！");
        }

        Type t = info.getType();
        node.symbolInfo = info;
        node.exprType = t;
        node.isLvalue = t.type != Type.Types.FUNCTION;
    }

    @Override
    public void visit(BoolConst node) {
        node.scope = currentScope;
        node.exprType = boolType;
        node.isLvalue = false;
    }

    @Override
    public void visit(IntConst node) {
        node.scope = currentScope;
        node.exprType = intType;
        node.isLvalue = false;
    }

    @Override
    public void visit(StringConst node) {
        node.scope = currentScope;
        node.exprType = stringType;
        node.isLvalue = false; // string也不是左值
    }

    @Override
    public void visit(NullLiteral node) {
        node.scope = currentScope;
        node.exprType = nullType;
        node.isLvalue = false; // null不可以被赋值，所以不是左值
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
}