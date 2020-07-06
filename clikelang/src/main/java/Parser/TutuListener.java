// Generated from /Users/yuanzuo/Desktop/c-like-lang/clikelang/design/Tutu.g4 by ANTLR 4.8

package Parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TutuParser}.
 */
public interface TutuListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TutuParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(TutuParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(TutuParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by the {@code class}
	 * labeled alternative in {@link TutuParser#programSection}.
	 * @param ctx the parse tree
	 */
	void enterClass(TutuParser.ClassContext ctx);
	/**
	 * Exit a parse tree produced by the {@code class}
	 * labeled alternative in {@link TutuParser#programSection}.
	 * @param ctx the parse tree
	 */
	void exitClass(TutuParser.ClassContext ctx);
	/**
	 * Enter a parse tree produced by the {@code func}
	 * labeled alternative in {@link TutuParser#programSection}.
	 * @param ctx the parse tree
	 */
	void enterFunc(TutuParser.FuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code func}
	 * labeled alternative in {@link TutuParser#programSection}.
	 * @param ctx the parse tree
	 */
	void exitFunc(TutuParser.FuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code var}
	 * labeled alternative in {@link TutuParser#programSection}.
	 * @param ctx the parse tree
	 */
	void enterVar(TutuParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code var}
	 * labeled alternative in {@link TutuParser#programSection}.
	 * @param ctx the parse tree
	 */
	void exitVar(TutuParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code block}
	 * labeled alternative in {@link TutuParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBlock(TutuParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code block}
	 * labeled alternative in {@link TutuParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBlock(TutuParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expr}
	 * labeled alternative in {@link TutuParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExpr(TutuParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expr}
	 * labeled alternative in {@link TutuParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExpr(TutuParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code select}
	 * labeled alternative in {@link TutuParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSelect(TutuParser.SelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code select}
	 * labeled alternative in {@link TutuParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSelect(TutuParser.SelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code iter}
	 * labeled alternative in {@link TutuParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIter(TutuParser.IterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code iter}
	 * labeled alternative in {@link TutuParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIter(TutuParser.IterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jump}
	 * labeled alternative in {@link TutuParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterJump(TutuParser.JumpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jump}
	 * labeled alternative in {@link TutuParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitJump(TutuParser.JumpContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(TutuParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(TutuParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code vardecl}
	 * labeled alternative in {@link TutuParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void enterVardecl(TutuParser.VardeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code vardecl}
	 * labeled alternative in {@link TutuParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void exitVardecl(TutuParser.VardeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stmt}
	 * labeled alternative in {@link TutuParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void enterStmt(TutuParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stmt}
	 * labeled alternative in {@link TutuParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void exitStmt(TutuParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(TutuParser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(TutuParser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#selectionStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectionStatement(TutuParser.SelectionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#selectionStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectionStatement(TutuParser.SelectionStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code while}
	 * labeled alternative in {@link TutuParser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhile(TutuParser.WhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code while}
	 * labeled alternative in {@link TutuParser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhile(TutuParser.WhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code for}
	 * labeled alternative in {@link TutuParser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void enterFor(TutuParser.ForContext ctx);
	/**
	 * Exit a parse tree produced by the {@code for}
	 * labeled alternative in {@link TutuParser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void exitFor(TutuParser.ForContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continue}
	 * labeled alternative in {@link TutuParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinue(TutuParser.ContinueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continue}
	 * labeled alternative in {@link TutuParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinue(TutuParser.ContinueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code break}
	 * labeled alternative in {@link TutuParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreak(TutuParser.BreakContext ctx);
	/**
	 * Exit a parse tree produced by the {@code break}
	 * labeled alternative in {@link TutuParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreak(TutuParser.BreakContext ctx);
	/**
	 * Enter a parse tree produced by the {@code return}
	 * labeled alternative in {@link TutuParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturn(TutuParser.ReturnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code return}
	 * labeled alternative in {@link TutuParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturn(TutuParser.ReturnContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#nonArrayTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterNonArrayTypeSpecifier(TutuParser.NonArrayTypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#nonArrayTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitNonArrayTypeSpecifier(TutuParser.NonArrayTypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayType}
	 * labeled alternative in {@link TutuParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(TutuParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayType}
	 * labeled alternative in {@link TutuParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(TutuParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nonArrayType}
	 * labeled alternative in {@link TutuParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterNonArrayType(TutuParser.NonArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nonArrayType}
	 * labeled alternative in {@link TutuParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitNonArrayType(TutuParser.NonArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(TutuParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(TutuParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#variableInitDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitDeclarator(TutuParser.VariableInitDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#variableInitDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitDeclarator(TutuParser.VariableInitDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(TutuParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(TutuParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMemberDeclaration(TutuParser.MemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMemberDeclaration(TutuParser.MemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(TutuParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(TutuParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#parameterDeclarationList}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclarationList(TutuParser.ParameterDeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#parameterDeclarationList}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclarationList(TutuParser.ParameterDeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclaration(TutuParser.ParameterDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclaration(TutuParser.ParameterDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code New}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNew(TutuParser.NewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code New}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNew(TutuParser.NewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Identifier}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(TutuParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Identifier}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(TutuParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MemberAccess}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMemberAccess(TutuParser.MemberAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MemberAccess}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMemberAccess(TutuParser.MemberAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Literal}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(TutuParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Literal}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(TutuParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryExpr}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpr(TutuParser.BinaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryExpr}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpr(TutuParser.BinaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Subscript}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSubscript(TutuParser.SubscriptContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Subscript}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSubscript(TutuParser.SubscriptContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunctionCall}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(TutuParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunctionCall}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(TutuParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PostfixIncDec}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixIncDec(TutuParser.PostfixIncDecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PostfixIncDec}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixIncDec(TutuParser.PostfixIncDecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryExpr}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(TutuParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryExpr}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(TutuParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SubExpression}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSubExpression(TutuParser.SubExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SubExpression}
	 * labeled alternative in {@link TutuParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSubExpression(TutuParser.SubExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code creatorError}
	 * labeled alternative in {@link TutuParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreatorError(TutuParser.CreatorErrorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code creatorError}
	 * labeled alternative in {@link TutuParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreatorError(TutuParser.CreatorErrorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code creatorArray}
	 * labeled alternative in {@link TutuParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreatorArray(TutuParser.CreatorArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code creatorArray}
	 * labeled alternative in {@link TutuParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreatorArray(TutuParser.CreatorArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code creatorNonArray}
	 * labeled alternative in {@link TutuParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreatorNonArray(TutuParser.CreatorNonArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code creatorNonArray}
	 * labeled alternative in {@link TutuParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreatorNonArray(TutuParser.CreatorNonArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(TutuParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(TutuParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TutuParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(TutuParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link TutuParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(TutuParser.ConstantContext ctx);
}