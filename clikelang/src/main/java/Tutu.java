import AST.Program;
import FrontEnd.ASTBuilder;
import FrontEnd.ASTPrinter;
import FrontEnd.SemanticChecker;
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

    private void printAST() {
        ast.accept(new ASTPrinter(out));
    }

    private void semanticChecker() {
        GlobalSymbolTable sym = new GlobalSymbolTable();
        ast.accept(new SemanticChecker(sym));
    }

    public void run() throws Exception {
        buildAST();
        printAST();
        semanticChecker();
    }

    public static void main(String[] args) throws Exception {
        // check options
        String inFile = "/Users/yuanzuo/Desktop/c-like-lang/clikelang/testcase/syntaxerror.tutu";
        String outFile = "out";
        // run compiler
        InputStream in = new FileInputStream(inFile);
        PrintStream out = new PrintStream(new FileOutputStream(outFile));
        new Tutu(in, out).run();
    }
}