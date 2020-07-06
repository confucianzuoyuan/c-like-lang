package FrontEnd;

import AST.*;
import Parser.TutuBaseListener;
import Parser.TutuParser;
import Symbol.Type;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ASTBuilder extends TutuBaseListener {
    private ParseTreeProperty<Object> map = new ParseTreeProperty<>();
    private Program program; // 最终返回一个抽象语法树的根节点：Program

    public Program getProgram() {
        return program;
    }

    // program: programSection* EOF
    @Override
    public void exitProgram(TutuParser.ProgramContext ctx) {
        List<Decl> decls = new ArrayList<>();
        for (TutuParser.ProgramSectionContext psc : ctx.programSection()) {
            Object node = map.get(psc);
            if (node instanceof Decl) decls.add((Decl)node);
        }
        program = new Program(decls);
        map.put(ctx, program);
    }

    // programSection: classDeclaration # class
    @Override
    public void exitClass(TutuParser.ClassContext ctx) {
        map.put(ctx, map.get(ctx.classDeclaration()));
    }

    // programSection: functionDeclaration # func
    @Override
    public void exitFunc(TutuParser.FuncContext ctx) {
        map.put(ctx, map.get(ctx.functionDeclaration()));
    }

    // programSection: variableDeclaration # var
    @Override
    public void exitVar(TutuParser.VarContext ctx) {
        map.put(ctx, map.get(ctx.variableDeclaration()));
    }

    // statement: blockStatement # block
    @Override
    public void exitBlock(TutuParser.BlockContext ctx) {
        map.put(ctx, map.get(ctx.blockStatement()));
    }

    // statement: expressionStatement # expr
    @Override
    public void exitExpr(TutuParser.ExprContext ctx) {
        map.put(ctx, map.get(ctx.expressionStatement()));
    }

    // statement: selectionStatement # select
    @Override
    public void exitSelect(TutuParser.SelectContext ctx) {
        map.put(ctx, map.get(ctx.selectionStatement()));
    }

    // blockStatement: '{' blockItem* '}'
    @Override
    public void exitBlockStatement(TutuParser.BlockStatementContext ctx) {
        CompoundStmt.Builder builder = new CompoundStmt.Builder();
        ctx.blockItem().stream().map(map::get).forEachOrdered(builder::add);
        map.put(ctx, builder.build());
    }

    // blockItem: variableDeclaration
    @Override
    public void exitVardecl(TutuParser.VardeclContext ctx) {
        Object node = map.get(ctx.variableDeclaration());
        if (node instanceof List) {
            @SuppressWarnings("unchecked")
            List<VariableDeclStmt> decls = ((List<VariableDecl>)node).stream()
                    .map(VariableDeclStmt::new).collect(Collectors.toList());
            map.put(ctx, decls);
        } else {
            map.put(ctx, new VariableDeclStmt((VariableDecl) node));
        }
    }

    // selectionStatement: 'if' '(' expression ')' statement ('else' statement)?
    @Override
    public void exitSelectionStatement(TutuParser.SelectionStatementContext ctx) {
        Expr cond = (Expr)map.get(ctx.expression());
        Stmt then = (Stmt)map.get(ctx.statement(0));
        Stmt otherwise;
        if (ctx.statement(1) == null) {
            otherwise = null;
        } else {
            otherwise = (Stmt)map.get(ctx.statement(1));
        }
        map.put(ctx, new IfStmt(cond, then, otherwise));
    }

    // iterationStatement: 'while' '(' expression ')' statement # while
    @Override
    public void exitWhile(TutuParser.WhileContext ctx) {
        Expr cond = (Expr)map.get(ctx.expression());
        Stmt body = (Stmt)map.get(ctx.statement());
        map.put(ctx, new WhileLoop(cond, body));
    }

    // jumpStatement: 'continue' ';' # continue
    @Override
    public void exitContinue(TutuParser.ContinueContext ctx) {
        map.put(ctx, new ContinueStmt());
    }

    // jumpStatement: 'break' ';' # break
    // 由于语法定义已经到了最后一步了，也就是是字符串`break`了，所以不用map.get(ctx.xxx)了
    @Override
    public void exitBreak(TutuParser.BreakContext ctx) {
        map.put(ctx, new BreakStmt());
    }

    // jumpStatement: 'return' expression? ';' # return
    @Override
    public void exitReturn(TutuParser.ReturnContext ctx) {
        Expr value;
        if (ctx.expression() == null) {
            value = null;
        } else {
            value = (Expr)map.get(ctx.expression());
        }
        map.put(ctx, new ReturnStmt(value));
    }

    // typeSpecifier: typeSpecifier '[' ']'
    @Override
    public void exitArrayType(TutuParser.ArrayTypeContext ctx) {
        TypeNode baseType = (TypeNode)map.get(ctx.typeSpecifier());
        map.put(ctx, new ArrayTypeNode(baseType));
    }

    // typeSpecifier: nonArrayTypeSpecifier
    // 语法定义是一个单个的对象，所以直接put就行了，不用构建AST，再put
    @Override
    public void exitNonArrayType(TutuParser.NonArrayTypeContext ctx) {
        map.put(ctx, map.get(ctx.nonArrayTypeSpecifier()));
    }

    // expression: expression op=('++' | '--')
    // ctx代表冒号左边，put的是冒号右边构建的ast
    @Override
    public void exitPostfixIncDec(TutuParser.PostfixIncDecContext ctx) {
        Expr expr = (Expr)map.get(ctx.expression()); // 先获取expression对应的ast
        if (ctx.op.getType() == TutuParser.PlusPlus) // 判断op的类型
            map.put(ctx, new SelfIncrement(expr));
        else
            map.put(ctx, new SelfDecrement(expr));
    }

    // expression: expression '[' expression ']'
    // 通过数组下标访问数组元素
    @Override
    public void exitSubscript(TutuParser.SubscriptContext ctx) {
        Expr array = (Expr)map.get(ctx.expression(0));
        Expr subscript = (Expr)map.get(ctx.expression(1));
        map.put(ctx, new ArrayAccess(array, subscript));
    }

    // expression: expression '.' Identifier
    // Identifier已经是字符串token了
    @Override
    public void exitMemberAccess(TutuParser.MemberAccessContext ctx) {
        Expr record = (Expr)map.get(ctx.expression());
        String member = ctx.Identifier().getText();
        map.put(ctx, new MemberAccess(record, member));
    }

    // expression
    //     :   <assoc=right> op=('++'|'--') expression
    //     |   <assoc=right> op=('+' | '-') expression
    //     |   <assoc=right> op=('!' | '~') expression
    // 一元表达式
    @Override
    public void exitUnaryExpr(TutuParser.UnaryExprContext ctx) {
        UnaryExpr.UnaryOp op;
        switch (ctx.op.getType()) {
            case TutuParser.PlusPlus: op = UnaryExpr.UnaryOp.INC; break;
            case TutuParser.MinusMinus: op = UnaryExpr.UnaryOp.DEC; break;
            case TutuParser.Plus: op = UnaryExpr.UnaryOp.POS; break;
            case TutuParser.Minus: op = UnaryExpr.UnaryOp.NEG; break;
            case TutuParser.Not: op = UnaryExpr.UnaryOp.LOGICAL_NOT; break;
            case TutuParser.Tilde: op = UnaryExpr.UnaryOp.BITWISE_NOT; break;
            default: throw new RuntimeException("Unknown unary operator.");
        }
        map.put(ctx, new UnaryExpr(op, (Expr)map.get(ctx.expression())));
    }

    // expression
    //     :   expression op=('*' | '/' | '%') expression
    //     |   expression op=('+' | '-') expression
    //     |   expression op=('<<'|'>>') expression
    //     |   expression op=('<' | '>') expression
    //     |   expression op=('<='|'>=') expression
    //     |   expression op=('=='|'!=') expression
    //     |   expression op='&' expression
    //     |   expression op='^' expression
    //     |   expression op='|' expression
    //     |   expression op='&&' expression
    //     |   expression op='||' expression
    //     |   <assoc=right> expression op='=' expression
    // 二元表达式
    @Override
    public void exitBinaryExpr(TutuParser.BinaryExprContext ctx) {
        BinaryExpr.BinaryOp op;
        switch (ctx.op.getType()) {
            case TutuParser.Star: op = BinaryExpr.BinaryOp.MUL; break;
            case TutuParser.Div: op = BinaryExpr.BinaryOp.DIV; break;
            case TutuParser.Mod: op = BinaryExpr.BinaryOp.MOD; break;
            case TutuParser.Plus: op = BinaryExpr.BinaryOp.ADD; break;
            case TutuParser.Minus: op = BinaryExpr.BinaryOp.SUB; break;
            case TutuParser.LeftShift: op = BinaryExpr.BinaryOp.SHL; break;
            case TutuParser.RightShift: op = BinaryExpr.BinaryOp.SHR; break;
            case TutuParser.Less: op = BinaryExpr.BinaryOp.LT; break;
            case TutuParser.Greater: op = BinaryExpr.BinaryOp.GT; break;
            case TutuParser.LessEqual: op = BinaryExpr.BinaryOp.LE; break;
            case TutuParser.GreaterEqual: op = BinaryExpr.BinaryOp.GE; break;
            case TutuParser.Equal: op = BinaryExpr.BinaryOp.EQ; break;
            case TutuParser.NotEqual: op = BinaryExpr.BinaryOp.NE; break;
            case TutuParser.And: op = BinaryExpr.BinaryOp.BITWISE_AND; break;
            case TutuParser.Caret: op = BinaryExpr.BinaryOp.XOR; break;
            case TutuParser.Or: op = BinaryExpr.BinaryOp.BITWISE_OR; break;
            case TutuParser.AndAnd: op = BinaryExpr.BinaryOp.LOGICAL_AND; break;
            case TutuParser.OrOr: op = BinaryExpr.BinaryOp.LOGICAL_OR; break;
            case TutuParser.Assign: op = BinaryExpr.BinaryOp.ASSIGN; break;
            default: throw new RuntimeException("Unknown binary operator.");
        }
        Expr lhs = (Expr)map.get(ctx.expression(0));
        Expr rhs = (Expr)map.get(ctx.expression(1));
        map.put(ctx, new BinaryExpr(op, lhs, rhs));
    }

    // expression: Identifier
    // Identifier已经是字符串token了
    @Override
    public void exitIdentifier(TutuParser.IdentifierContext ctx) {
        map.put(ctx, new Identifier(ctx.Identifier().getText()));
    }

    // 循环语句，for，while
    // 对应的语法规则：statement: iterationStatement # iter
    @Override
    public void exitIter(TutuParser.IterContext ctx) {
        // 由于匹配到的是一个对象或者数组，复合元素的翻译结果拷贝到自身的语法分析树节点中
        // 将`ctx.iterationStatement()`节点对应的值，拷贝过来
        map.put(ctx, map.get(ctx.iterationStatement()));
    }

    // expression: Constant
    // 常量表达式
    @Override
    public void exitLiteral(TutuParser.LiteralContext ctx) {
        map.put(ctx, map.get(ctx.constant()));
    }

    // constant
    //     :   type=IntegerConstant
    //     |   type=CharacterConstant
    //     |   type=StringLiteral
    //     |   type=NullLiteral
    //     |   type=BoolConstant
    //     ;
    // 常量的字面量
    @Override
    public void exitConstant(TutuParser.ConstantContext ctx) {
        String s = ctx.type.getText();
        int type = ctx.type.getType();
        if (type == TutuParser.IntegerConstant) {
            map.put(ctx, new IntConst(Integer.parseInt(s)));
        } else if (type == TutuParser.NullLiteral) {
            map.put(ctx, new NullLiteral());
        } else if (type == TutuParser.BoolConstant) {
            map.put(ctx, new BoolConst(s.equals("true")));
        } else if (type == TutuParser.CharacterConstant || type == TutuParser.StringLiteral) {
            //s = unescape(s);
            s = s.substring(1, s.length()-1);
            if (type == TutuParser.CharacterConstant) {
                if (s.length() == 1) map.put(ctx, new IntConst(s.charAt(0)));
                else throw new RuntimeException("Invalid char literal.");
            } else {
                map.put(ctx, new StringConst(s));
            }
        } else {
            throw new RuntimeException("Unknown literal.");
        }
    }

    // statement: jumpStatement # jump
    @Override
    public void exitJump(TutuParser.JumpContext ctx) {
        map.put(ctx, map.get(ctx.jumpStatement()));
    }

    // blockItem: statement # stmt
    @Override
    public void exitStmt(TutuParser.StmtContext ctx) {
        map.put(ctx, map.get(ctx.statement()));
    }

    // expression: '(' expression ')'
    // (1+2)
    @Override
    public void exitSubExpression(TutuParser.SubExpressionContext ctx) {
        map.put(ctx, map.get(ctx.expression()));
    }

    // expressionStatement: expression? ';'
    @Override
    public void exitExpressionStatement(TutuParser.ExpressionStatementContext ctx) {
        map.put(ctx, ctx.expression() != null ? map.get(ctx.expression()) : new EmptyExpr());
    }

    // expression: <assoc=right> 'new' creator
    // 实例化创建语法，右结合
    @Override
    public void exitNew(TutuParser.NewContext ctx) {
        map.put(ctx, map.get(ctx.creator()));
    }

    // creator: nonArrayTypeSpecifier
    // 非数组类型的类型 int
    @Override
    public void exitCreatorNonArray(TutuParser.CreatorNonArrayContext ctx) {
        NewExpr.Builder builder = new NewExpr.Builder();
        builder.setType((TypeNode)map.get(ctx.nonArrayTypeSpecifier()));
        map.put(ctx, builder.build());
    }

    // creator: nonArrayTypeSpecifier ('[' expression ']')+ ('[' ']')*
    // 数组类型 int[2][]
    @Override
    public void exitCreatorArray(TutuParser.CreatorArrayContext ctx) {
        NewExpr.Builder builder = new NewExpr.Builder();
        builder.setType((TypeNode)map.get(ctx.nonArrayTypeSpecifier()));
        for (TutuParser.ExpressionContext ec : ctx.expression()) {
            Object node = map.get(ec);
            builder.addDimension(node);
        }
        int unspecified = ctx.getTokens(TutuParser.LBracket).size() - ctx.expression().size();
        for (int i = 0; i < unspecified; ++i) {
            builder.addDimension(null);
        }
        map.put(ctx, builder.build());
    }

    // iterationStatement:
    //    'for' '(' declinit=variableDeclaration
    //              cond=expression? ';'
    //              step=expression? ')'
    //        statement
    //    'for' '(' init=expression? ';'
    //              cond=expression? ';'
    //              step=expression? ')'
    //        statement

    @Override
    public void exitFor(TutuParser.ForContext ctx) {
        List<VariableDecl> initWithDecl = (List<VariableDecl>)map.get(ctx.declinit);
        Expr init = (Expr)map.get(ctx.init);
        Expr cond = (Expr)map.get(ctx.cond);
        Expr step = (Expr)map.get(ctx.step);
        Stmt body = (Stmt)map.get(ctx.statement());
        if (ctx.variableDeclaration() == null)
            map.put(ctx, new ForLoop(init, cond, step, body));
        else
            map.put(ctx, new ForLoop(initWithDecl, cond, step, body));
    }

    // nonArrayTypeSpecifier
    //     :   type='int'
    //     |   type='bool'
    //     |   type='string'
    //     |   type='void'
    //     |   type=Identifier
    //     ;
    @Override
    public void exitNonArrayTypeSpecifier(TutuParser.NonArrayTypeSpecifierContext ctx) {
        switch (ctx.type.getType()) {
            case TutuParser.Int: map.put(ctx, new PrimitiveTypeNode(Type.Types.INT)); break;
            case TutuParser.Bool: map.put(ctx, new PrimitiveTypeNode(Type.Types.BOOL)); break;
            case TutuParser.String: map.put(ctx, new PrimitiveTypeNode(Type.Types.STRING)); break;
            case TutuParser.Void: map.put(ctx, new PrimitiveTypeNode(Type.Types.VOID)); break;
            case TutuParser.Identifier:
                map.put(ctx, new ClassTypeNode(ctx.Identifier().getText()));
                break;
            default: throw new RuntimeException("Unhandled type in `nonArrayTypeSpecifier`");
        }
    }

    // variableDeclaration: typeSpecifier variableInitDeclarator (',' variableInitDeclarator)* ';'
    // variableInitDeclarator: Identifier ('=' expression)?
    @Override
    public void exitVariableDeclaration(TutuParser.VariableDeclarationContext ctx) {
        List<VariableDecl> decls = new ArrayList<>();
        TypeNode type = (TypeNode)map.get(ctx.typeSpecifier()); // 变量类型
        for (TutuParser.VariableInitDeclaratorContext vidc : ctx.variableInitDeclarator()) {
            VariableDecl decl = new VariableDecl(type, vidc.Identifier().getText(), vidc.expression() != null ? (Expr)map.get(vidc.expression()) : null);
            decls.add(decl);
        }
        map.put(ctx, decls);
    }

    // classDeclaration: 'class' Identifier '{' memberDeclaration* '}'
    // 结构体声明
    @Override
    public void exitClassDeclaration(TutuParser.ClassDeclarationContext ctx) {
        List<VariableDecl> members = new ArrayList<>();
        String name = ctx.Identifier().getText();
        for (TutuParser.MemberDeclarationContext md : ctx.memberDeclaration()) {
            Object node = map.get(md);
            if (node instanceof VariableDecl) {
                members.add((VariableDecl)node);
            } else {
                throw new RuntimeException("Invalid type");
            }
        }
        map.put(ctx, new ClassDecl(members, name));
    }

    // memberDeclaration: typeSpecifier Identifier ';'
    // 结构体成员声明
    @Override
    public void exitMemberDeclaration(TutuParser.MemberDeclarationContext ctx) {
        TypeNode type = (TypeNode)map.get(ctx.typeSpecifier());
        String name = ctx.Identifier().getText();
        map.put(ctx, new VariableDecl(type, name, null));
    }

    // functionDeclaration: typeSpecifier Identifier '(' parameterDeclarationList? ')' blockStatement
    // parameterDeclarationList: parameterDeclaration (',' parameterDeclaration)*
    @Override
    public void exitFunctionDeclaration(TutuParser.FunctionDeclarationContext ctx) {
        TypeNode returnType = (TypeNode)map.get(ctx.typeSpecifier()); // 返回值类型
        String name = ctx.Identifier().getText(); // 函数名
        CompoundStmt body = (CompoundStmt)map.get(ctx.blockStatement()); // 函数体
        List<VariableDecl> argTypes = new ArrayList<>(); // 参数列表
        if (ctx.parameterDeclarationList() != null) {
            for (TutuParser.ParameterDeclarationContext pdc : ctx.parameterDeclarationList().parameterDeclaration()) {
                Object node = map.get(pdc);
                if (node instanceof VariableDecl) {
                    argTypes.add((VariableDecl)node);
                } else {
                    throw new RuntimeException("Invalid type");
                }
            }
        }
        map.put(ctx, new FunctionDecl(returnType, name, argTypes, body));
    }

    // expression: Identifier '(' parameterList? ')'
    // parameterList: expression (',' expression)*
    @Override
    public void exitFunctionCall(TutuParser.FunctionCallContext ctx) {
        FunctionCall.Builder builder = new FunctionCall.Builder();
        builder.setName((Expr)map.get(ctx.expression()));
        if (ctx.parameterList() != null) {
            ctx.parameterList().expression().stream()
                    .map(map::get).forEachOrdered(builder::addArg);
        }
        map.put(ctx, builder.build());
    }

    // parameterDeclaration: typeSpecifier Identifier
    // 参数声明
    @Override
    public void exitParameterDeclaration(TutuParser.ParameterDeclarationContext ctx) {
        TypeNode type = (TypeNode)map.get(ctx.typeSpecifier());
        String name = ctx.Identifier().getText();
        map.put(ctx, new VariableDecl(type, name, null));
    }

}