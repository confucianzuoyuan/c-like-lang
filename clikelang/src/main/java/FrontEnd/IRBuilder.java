package FrontEnd;

import AST.*;
import IR.*;
import IR.UnaryOperation.UnaryOp;
import IR.BinaryOperation.BinaryOp;
import IR.IntComparison.Condition;
import Symbol.*;

public class IRBuilder implements IASTVisitor {
    private BasicBlock curBB;
    private BasicBlock curLoopStepBB;
    private BasicBlock curLoopAfterBB;
    private Function curFunction;
    private boolean getAddress = false;
    private IRRoot irRoot = new IRRoot();
    private GlobalSymbolTable sym;

    public IRBuilder(GlobalSymbolTable sym) {
        this.sym = sym;
    }

    public IRRoot getIRRoot() {
        return irRoot;
    }

    @Override
    public void visit(WhileLoop node) {
        BasicBlock BBCond = new BasicBlock("while_cond");
        BasicBlock BBLoop = new BasicBlock("while_loop");
        BasicBlock BBAfter = new BasicBlock("while_after");

        // 保存当前上下文
        BasicBlock oldLoopCondBB = curLoopStepBB;
        BasicBlock oldLoopAfterBB = curLoopAfterBB;
        curLoopStepBB = BBCond;
        curLoopAfterBB = BBAfter;

        curBB.end(new Jump(curBB, BBCond));

        curBB = BBCond;
        node.cond.ifTrue = BBLoop;
        node.cond.ifFalse = BBAfter;
        visit(node.cond);

        curBB = BBLoop;
        visit(node.body);
        curBB.end(new Jump(curBB, BBCond));

        curLoopStepBB = oldLoopCondBB;
        curLoopAfterBB = oldLoopAfterBB;
        curBB = BBAfter;
    }

    @Override
    public void visit(IfStmt node) {
        // 条件为真的分支基本块
        BasicBlock BBTrue = new BasicBlock("if_true");
        // 条件为假的分支基本块
        BasicBlock BBFalse = new BasicBlock("if_false");
        // 执行完if语句后的基本块
        BasicBlock BBMerge = new BasicBlock("if_merge");

        // 逻辑表达式也必须添加分支指令
        node.cond.ifTrue = BBTrue;
        node.cond.ifFalse = node.otherwise != null ? BBFalse : BBMerge;
        visit(node.cond);

        // 条件表达式为真时，执行then语句，所以构建一个基本块
        curBB = BBTrue;
        visit(node.then);
        // 执行完then语句，跳转到BBMerge
        if (!BBTrue.isEnded())
            BBTrue.end(new Jump(curBB, BBMerge));

        // 条件表达式为假时，执行otherwise语句，所以构建一个基本块
        if (node.otherwise != null) {
            curBB = BBFalse;
            visit(node.otherwise);
        }
        if (!BBFalse.isEnded())
            BBFalse.end(new Jump(curBB, BBMerge));

        // if语句执行完后面的基本块
        curBB = BBMerge;
    }

    @Override
    public void visit(Identifier node) {
        // 在当前函数中查找标识符的值
        node.intValue = curFunction.getVarReg(node.name);
        // 使用分支指令终结当前基本块
        // ???
        if (node.ifTrue != null) {
            curBB.end(new Branch(curBB, node.intValue, node.ifTrue, node.ifFalse));
        }
    }

    @Override
    public void visit(IntConst node) {
        node.intValue = new IntImmediate(node.value);
    }

    @Override
    public void visit(NullLiteral node) {

    }

    @Override
    public void visit(BinaryExpr node) {
        switch (node.op) {
            // 赋值符号
            case ASSIGN:
                processAssign(node);
                break;

            // `&&` `||`
            case LOGICAL_OR:
            case LOGICAL_AND:
                processLogicalBinaryExpr(node);
                break;

            case EQ:
            case NE:
            case LT:
            case GT:
            case LE:
            case GE:
                if (node.lhs.exprType.type == Type.Types.INT)
                    processIntRelationalExpr(node);
                else
                    processStringRelationalExpr(node);
                break;

            case SHL:
            case SHR:
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
            case XOR:
            case BITWISE_OR:
            case BITWISE_AND:
                processArithmeticExpr(node);
                break;
        }
    }

    private void processStringRelationalExpr(BinaryExpr node) {
    }

    private void processIntRelationalExpr(BinaryExpr node) {
        visit(node.lhs);
        visit(node.rhs);

        Condition cond = null;
        switch (node.op) {
            case EQ:
                cond = Condition.EQ;
                break;
            case NE:
                cond = Condition.NE;
                break;
            case LT:
                cond = Condition.LT;
                break;
            case GT:
                cond = Condition.GT;
                break;
            case LE:
                cond = Condition.LE;
                break;
            case GE:
                cond = Condition.GE;
                break;
        }

        // 初始化一个寄存器，用来保存整数比较结果？
        VirtualRegister reg = new VirtualRegister(null);
        curBB.append(new IntComparison(curBB, reg, cond, node.lhs.intValue, node.rhs.intValue));
        if (node.ifTrue != null) {
            // 如果表达式在条件表达式中，例如if(e), for(;e;)等
            curBB.end(new Branch(curBB, reg, node.ifTrue, node.ifFalse));
        } else {
            // 如果表达式是赋值语句等号的右面
            node.intValue = reg;
        }
    }

    private void processAssign(BinaryExpr node) {
        // 如果等号右边的表达式是二元逻辑表达式：e1 && e2
        if (isLogicalExpression(node.rhs)) {
            node.rhs.ifTrue = new BasicBlock(null);
            node.rhs.ifFalse = new BasicBlock(null);
        }
        visit(node.rhs);

        // 等号左边的左值需要访问内存吗？例如：a[1]或者a.name这种
        // 访问内存需要get address，取址
        boolean isMemOp = needMemoryAccess(node.lhs);
        getAddress = isMemOp;
        visit(node.lhs);
        getAddress = false;

        // 构建赋值语句
        // 需要知道等号右边表达式所需要占用的内存
        // `node.lhs.intValue`是左值的地址
        assign(isMemOp, node.rhs.exprType.getMemorySize(), node.lhs.intValue, node.rhs);
        // 赋值语句的值就是等号右边表达式的值
        node.intValue = node.rhs.intValue;
    }

    @Override
    public void visit(BoolConst node) {
        node.intValue = new IntImmediate(node.value ? 1 : 0);
    }

    @Override
    public void visit(EmptyExpr node) {

    }

    @Override
    public void visit(ContinueStmt node) {
        // 当前块的结束IR是跳转到循环条件块的IR指令
        curBB.end(new Jump(curBB, curLoopStepBB));
    }

    @Override
    public void visit(BreakStmt node) {
        // 当前块的结束IR是跳转到循环后的块的IR指令
        curBB.end(new Jump(curBB, curLoopAfterBB));
    }

    @Override
    public void visit(ReturnStmt node) {
        visit(node.value);
        curBB.end(new Return(curBB, node.value.intValue));
    }

    @Override
    public void visit(StringConst node) {

    }

    @Override
    public void visit(UnaryExpr node) {
        if (node.op == UnaryExpr.UnaryOp.LOGICAL_NOT) {
            node.body.ifTrue = node.ifFalse;
            node.body.ifFalse = node.ifTrue;
            visit(node.body);
            return;
        }

        visit(node.body);
        VirtualRegister reg;
        switch (node.op) {
            case INC:
                processSelfIncDec(node.body, node, true, false);
                break;
            case DEC:
                processSelfIncDec(node.body, node, false, false);
                break;

            case POS:
                node.intValue = node.body.intValue;
                break;

            case NEG:
                reg = new VirtualRegister(null);
                node.intValue = reg;
                curBB.append(new UnaryOperation(curBB, reg, UnaryOp.NEG, node.body.intValue));
                break;

            case BITWISE_NOT:
            default:
                reg = new VirtualRegister(null);
                curBB.append(new UnaryOperation(curBB, reg, UnaryOp.NOT, node.body.intValue));
                break;
        }
    }

    @Override
    public void visit(ArrayAccess node) {
        boolean getaddr = getAddress;
        getAddress = false;
        visit(node.array);
        getAddress = getaddr;

        IntValue tmp1 = new IntImmediate(((ArrayType)node.array.exprType).bodyType.getRegisterSize());
        VirtualRegister reg = new VirtualRegister(null);
        node.intValue = reg;
        curBB.append(new BinaryOperation(curBB, reg, BinaryOp.MUL, node.subscript.intValue, tmp1));
        curBB.append(new BinaryOperation(curBB, reg, BinaryOp.ADD, node.array.intValue, reg));
        if (!getAddress) {
            curBB.append(new Load(curBB, reg, node.exprType.getRegisterSize(), reg));
        }

    }

    @Override
    public void visit(MemberAccess node) {
        boolean getaddr = getAddress;
        getAddress = false;
        visit(node.record);
        getAddress = getaddr;

        // record的地址？
        IntValue addr = node.record.intValue;
        ClassType t = (ClassType)node.record.exprType;
        SymbolInfo info = t.members.getInfo(node.member);
        VirtualRegister reg = new VirtualRegister(null);
        node.intValue = reg;
        curBB.append(new BinaryOperation(curBB, reg, BinaryOp.ADD, addr, new IntImmediate(info.getOffset())));
        if (!getaddr) {
            curBB.append(new Load(curBB, reg, info.getType().getRegisterSize(), reg));
            node.intValue = reg;
        }

        if (node.ifTrue != null) {
            curBB.end(new Branch(curBB, node.intValue, node.ifTrue, node.ifFalse));
        }
    }

    @Override
    public void visit(SelfIncrement node) {
        processSelfIncDec(node.self, node, true, true);
    }

    @Override
    public void visit(SelfDecrement node) {
        processSelfIncDec(node.self, node, false, true);
    }

    private void processSelfIncDec(Expr body, Expr node, boolean isInc, boolean isPostfix) {
        boolean isMemOp = needMemoryAccess(body);
        // get address
        boolean getaddr = getAddress;
        getAddress = isMemOp;
        visit(body);
        IntValue addr = body.intValue;

        // get value
        getAddress = false;
        visit(body);
        getAddress = getaddr;

        // stuffs
        BinaryOp op = isInc ? BinaryOp.ADD : BinaryOp.SUB;
        IntImmediate one = new IntImmediate(1);
        VirtualRegister reg;

        // if postfix, backup old value
        if (isPostfix) {
            reg = new VirtualRegister(null);
            curBB.append(new Move(curBB, reg, body.intValue));
            node.intValue = reg;
        } else {
            node.intValue = body.intValue;
        }

        // if need memory operation, introduce temporary register
        if (isMemOp) {
            reg = new VirtualRegister(null);
            curBB.append(new BinaryOperation(curBB, reg, op, body.intValue, one));
            curBB.append(new Store(curBB, body.exprType.getMemorySize(), addr, reg));
        } else {
            curBB.append(new BinaryOperation(curBB, (VirtualRegister) body.intValue, op, body.intValue, one));
        }
    }
    // 声明变量
    @Override
    public void visit(VariableDecl node) {
        // 新建一个虚拟寄存器，用来存储变量的值
        VirtualRegister reg = new VirtualRegister(node.name);
        // 将虚拟寄存器的名字和寄存器存入当前函数的字典
        curFunction.defineVarReg(node.name, reg);

        if (node.init != null) {
            // 如果初始化的表达式是逻辑表达式的话，ifTrue和ifFalse两个分支的基本块先初始化为null
            // int a = b || c
            if (isLogicalExpression(node.init)) {
                node.init.ifTrue = new BasicBlock(null);
                node.init.ifFalse = new BasicBlock(null);
            }
            // 然后访问node.init节点
            node.init.accept(this);
            // isMemOp为什么是false？
            assign(false, node.init.exprType.getMemorySize(), reg, node.init);
        }
    }

    // 是否需要访问内存，比如访问类的成员变量或者访问数组中的元素
    private boolean needMemoryAccess(Expr type) {
        return type instanceof MemberAccess || type instanceof ArrayAccess;
    }

    private void assign(boolean isMemOp, int size, IntValue addr, Expr rhs) {
        if (rhs.ifTrue != null) {
            // 短路求值：true || false
            BasicBlock merge = new BasicBlock(null);
            if (isMemOp) {
                rhs.ifTrue.append(new Store(curBB, size, addr, new IntImmediate(1)));
                rhs.ifFalse.append(new Store(curBB, size, addr, new IntImmediate(0)));
            } else {
                rhs.ifTrue.append(new Move(curBB, (VirtualRegister) addr, new IntImmediate(1)));
                rhs.ifFalse.append(new Move(curBB, (VirtualRegister) addr, new IntImmediate(0)));
            }
            rhs.ifTrue.end(new Jump(curBB, merge));
            rhs.ifFalse.end(new Jump(curBB, merge));
            curBB = merge;
        } else {
            if (isMemOp) {
                curBB.append(new Store(curBB, size, addr, rhs.intValue));
            } else {
                curBB.append(new Move(curBB, (VirtualRegister) addr, rhs.intValue));
            }
        }
    }

    // 处理二元逻辑表达式：e1 || e2; e1 && e2
    private void processLogicalBinaryExpr(BinaryExpr node) {
        // check lhs
        if (node.op == BinaryExpr.BinaryOp.LOGICAL_AND) {
            node.lhs.ifTrue = new BasicBlock("lhs_true");
            node.lhs.ifFalse = node.ifFalse;
            visit(node.lhs);
            curBB = node.lhs.ifTrue;
        } else {
            node.lhs.ifTrue = node.ifTrue;
            node.lhs.ifFalse = new BasicBlock("lhs_false");
            visit(node.lhs);
            curBB = node.lhs.ifFalse;
        }

        // check rhs
        node.rhs.ifTrue = node.ifTrue;
        node.rhs.ifFalse = node.ifFalse;
        visit(node.rhs);
    }

    // node是否是逻辑表达式
    private boolean isLogicalExpression(Expr node) {
        if (node instanceof BinaryExpr) {
            BinaryExpr.BinaryOp op = ((BinaryExpr) node).op;
            return op == BinaryExpr.BinaryOp.LOGICAL_AND || op == BinaryExpr.BinaryOp.LOGICAL_OR;
        } else if (node instanceof UnaryExpr) {
            return ((UnaryExpr) node).op == UnaryExpr.UnaryOp.LOGICAL_NOT;
        } else {
            return false;
        }
    }

    @Override
    public void visit(ArrayTypeNode node) {

    }

    @Override
    public void visit(VariableDeclStmt node) {
        visit(node.decl);
    }

    @Override
    public void visit(CompoundStmt node) {
        for (Stmt stmt : node.stmts) {
            stmt.accept(this);
        }
    }

    @Override
    public void visit(FunctionCall node) {
        node.parameters.forEach(x -> x.accept(this));
        FunctionType type = (FunctionType) node.name.exprType;
        Function func = irRoot.functions.get(type.name);
        VirtualRegister reg = new VirtualRegister(null);
        Call call = new Call(curBB, reg, func);
        node.parameters.forEach(x -> call.appendArg(x.intValue));
        curBB.append(call);
        node.intValue = reg;
    }

    @Override
    public void visit(FunctionDecl node) {
        curFunction = new Function(node.functionType);
        irRoot.functions.put(node.name, curFunction); // 根节点中保存当前定义的函数
        curBB = curFunction.getStartBB(); // 当前基本块是函数的开始基本块

        for (VariableDecl v : node.argTypes) {
            v.accept(this);
        }
        node.body.accept(this);
        curFunction = null; // 重置当前处理的函数这个变量
    }

    // 这个要好好研究一下
    @Override
    public void visit(NewExpr node) {
        Type type = node.exprType;
        VirtualRegister reg = new VirtualRegister(null);
        if (type.type == Type.Types.CLASS) {
            ClassType t = (ClassType) type;
            // 结构体需要占用多少内存
            curBB.append(new HeapAllocate(curBB, reg, new IntImmediate(t.getMemorySize())));
        } else {
            // array
            Expr dim = node.dim.get(0);
            boolean getaddr = getAddress;
            getAddress = false;
            visit(dim);
            getAddress = getaddr;

            ArrayType t = (ArrayType) type;
            curBB.append(new BinaryOperation(curBB, reg, BinaryOp.MUL, dim.intValue,
                    new IntImmediate(t.bodyType.getRegisterSize())));
            curBB.append(new HeapAllocate(curBB, reg, reg));
        }
        node.intValue = reg;
    }

    @Override
    public void visit(Program node) {
        for (Decl decl : node.decls) {
            decl.accept(this);
        }
    }

    @Override
    public void visit(ClassDecl node) {

    }

    @Override
    public void visit(ForLoop node) {
        BasicBlock BBCond = new BasicBlock("for_cond");
        BasicBlock BBLoop = new BasicBlock("for_loop");
        BasicBlock BBStep = new BasicBlock("for_step");
        // for循环执行完后的基本块
        BasicBlock BBAfter = new BasicBlock("for_after");

        // 将旧的基本块先保存下来
        BasicBlock oldLoopCondBB = curLoopStepBB;
        BasicBlock oldLoopAfterBB = curLoopAfterBB;
        curLoopStepBB = BBStep;
        curLoopAfterBB = BBAfter;

        // 访问初始条件
        if (node.init != null) {
            visit(node.init);
        } else {
            node.initWithDecl.forEach(x -> x.accept(this));
        }

        // 当前基本块的结束IR指令是跳转到循环条件生成的基本块
        curBB.end(new Jump(curBB, BBCond));

        // 针对循环条件生成基本块
        curBB = BBCond;
        node.cond.ifTrue = BBLoop;
        node.cond.ifFalse = BBAfter;
        visit(node.cond);

        // 针对循环体生成基本块
        curBB = BBLoop;
        visit(node.body);
        curBB.end(new Jump(curBB, BBStep));

        // for(;i < 100; i++)
        // 针对`i++`生成基本块
        curBB = BBStep;
        if (node.step != null) {
            visit(node.step);
        }
        // 基本块以跳转到循环条件的基本块的Jump指令作为结束
        curBB.end(new Jump(curBB, BBCond));

        // 退出循环
        curLoopStepBB = oldLoopCondBB;
        curLoopAfterBB = oldLoopAfterBB;
        curBB = BBAfter;
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

    private void processArithmeticExpr(BinaryExpr node) {
        visit(node.lhs);
        visit(node.rhs);
        BinaryOp op = null;
        switch (node.op) {
            case SHL:
                op = BinaryOp.SHL;
                break;
            case SHR:
                op = BinaryOp.SHR;
                break;
            case ADD:
                op = BinaryOp.ADD;
                break;
            case SUB:
                op = BinaryOp.SUB;
                break;
            case MUL:
                op = BinaryOp.MUL;
                break;
            case DIV:
                op = BinaryOp.DIV;
                break;
            case MOD:
                op = BinaryOp.MOD;
                break;
            case XOR:
                op = BinaryOp.XOR;
                break;
            case BITWISE_OR:
                op = BinaryOp.OR;
                break;
            case BITWISE_AND:
                op = BinaryOp.AND;
                break;
        }
        VirtualRegister reg = new VirtualRegister(null);
        node.intValue = reg; // 二元表达式的值是寄存器！
        curBB.append(new BinaryOperation(curBB, reg, op, node.lhs.intValue, node.rhs.intValue));
    }
}
