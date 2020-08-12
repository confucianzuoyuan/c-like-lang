import AST.Program;
import BackEnd.ASTInterpreter;
import BackEnd.GlobalEnvironment;
import BackEnd.IRPrinter;
import FrontEnd.*;
import IR.IRRoot;
import Parser.TutuLexer;
import Parser.TutuParser;
import Symbol.GlobalSymbolTable;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;

public class Tutu {
    private InputStream in;
    private PrintStream out;
    private Program ast;
    private IRRoot ir;

    public Tutu(InputStream in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    private void buildAST() throws IOException {
        TutuParser parser = new TutuParser(new CommonTokenStream(new TutuLexer(new ANTLRInputStream(in))));
        parser.setErrorHandler(new BailErrorStrategy());
        ASTBuilder astBuilder = new ASTBuilder();
        new ParseTreeWalker().walk(astBuilder, parser.program());
        ast = astBuilder.getProgram();
        in = null;
    }

    private void buildIR() {
        GlobalSymbolTable sym = new GlobalSymbolTable();
        ClassSymbolScanner classSymbolScanner = new ClassSymbolScanner(sym);
        ClassFunctionDeclarator classFunctionDeclarator = new ClassFunctionDeclarator(sym);
        SemanticChecker semanticChecker = new SemanticChecker(sym);
        IRBuilder irBuilder = new IRBuilder(sym);

        ir = irBuilder.getIRRoot();
        ast.accept(classSymbolScanner);
        ast.accept(classFunctionDeclarator);
        ast.accept(semanticChecker);
        ast.accept(irBuilder);
    }

    private void printAST() {
        ast.accept(new ASTPrinter(out));
    }

    private void interpretAST() {
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        ASTInterpreter astInterpreter = new ASTInterpreter(globalEnvironment);
        ast.accept(astInterpreter);
        astInterpreter.executeMain();
    }

    private void printIR() {
        ir.accept(new IRPrinter(out));
    }

    public void run() throws Exception {
        buildAST();
        printAST();
        interpretAST();
//        buildIR();
//        printIR();
    }

    public static void main(String[] args) throws Exception {
        // check options
        String inFile = "/Users/yuanzuo/Desktop/c-like-lang/clikelang/testcase/test1.tutu";
        String outFile = "out";
        // run compiler
        InputStream in = new FileInputStream(inFile);
        PrintStream out = new PrintStream(new FileOutputStream(outFile));
        new Tutu(in, out).run();
    }
}