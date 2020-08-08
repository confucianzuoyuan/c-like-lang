package BackEnd;

import IR.*;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class IRPrinter implements IIRVisitor {
    private PrintStream out;
    private Map<VirtualRegister, String> regMap;
    private Map<String, Integer> counterReg;
    private Map<BasicBlock, String> labelMap = new IdentityHashMap<>();
    private Map<String, Integer> counterBB = new HashMap<>();
    private Map<BasicBlock, Boolean> BBVisited = new IdentityHashMap<>();

    public IRPrinter(PrintStream out) {
        this.out = out;
    }

    public String newId(String name, Map<String, Integer> counter) {
        int cnt = counter.getOrDefault(name, 0) + 1;
        counter.put(name, cnt);
        if (cnt == 1) {
            return name;
        }
        return name + "_" + cnt;
    }

    public String regId(VirtualRegister reg) {
        String id = regMap.get(reg);
        if (id == null) {
            id = newId(reg.getHintName() == null ? "t" : reg.getHintName(), counterReg);
            regMap.put(reg, id);
        }
        return id;
    }

    public String labelId(BasicBlock BB) {
        String id = labelMap.get(BB);
        if (id == null) {
            id = newId(BB.getHintName(), counterBB);
            labelMap.put(BB, id);
        }
        return id;
    }

    @Override
    public void visit(IRRoot node) {
        node.functions.values().forEach(this::visit);
    }

    @Override
    public void visit(BasicBlock node) {
        if (BBVisited.containsKey(node)) return;
        BBVisited.put(node, true);
        out.println("%" + labelId(node) + ":");
        for (IRInstruction i = node.getHead(); i != null; i = i.getNext()) {
            i.accept(this);
        }
    }

    @Override
    public void visit(Function node) {
        regMap = new IdentityHashMap<>();
        counterReg = new HashMap<>();
        out.printf("func %s ", node.getName());
        node.getType().argNames.forEach(x -> {
            VirtualRegister reg = node.getVarReg(x);
            out.printf("$%s ", regId(reg));
        });
        out.print("{\n");

        // 会调用visit(BasicBlock node)
        // 然后访问所有的BB
        visit(node.getStartBB());

        out.print("}\n\n");
    }

    @Override
    public void visit(BinaryOperation node) {
        out.print("    ");
        String op = null;
        switch (node.getOp()) {
            case ADD: op = "add"; break;
            case SUB: op = "sub"; break;
            case MUL: op = "mul"; break;
            case DIV: op = "div"; break;
            case MOD: op = "rem"; break;
            case SHL: op = "shl"; break;
            case SHR: op = "ashr"; break;
            case AND: op = "and"; break;
            case OR: op = "or"; break;
            case XOR: op = "xor"; break;
        }

        visit(node.getDest());
        out.printf(" = %s ", op);
        node.getLhs().accept(this);
        out.print(" ");
        node.getRhs().accept(this);
        out.println();
    }

    @Override
    public void visit(UnaryOperation node) {
        out.print("    ");
        String op = null;
        switch (node.getOp()) {
            case NEG: op = "neg";
            case NOT: op = "not";
        }

        visit(node.getDest());
        out.printf(" = %s ", op);
        node.getOperand().accept(this);
        out.println();
    }

    @Override
    public void visit(IntComparison node) {
        out.print("    ");
        String op = null;
        switch (node.getCond()) {
            case EQ: op = "seq"; break;
            case NE: op = "sne"; break;
            case GT: op = "sgt"; break;
            case GE: op = "sge"; break;
            case LT: op = "slt"; break;
            case LE: op = "sle"; break;
        }

        visit(node.getDest());
        out.printf(" = %s ", op);
        node.getLhs().accept(this);
        out.print(" ");
        node.getRhs().accept(this);
        out.println();
    }

    @Override
    public void visit(IntImmediate node) {
        out.print(node.getValue());
    }

    @Override
    public void visit(Call node) {
        out.print("    ");
        if (node.getDest() != null) {
            visit(node.getDest());
            out.print(" = ");
        }
        out.printf("call %s ", node.getFunc().getName());
        node.getArgs().forEach(x -> {
            x.accept(this);
            out.print(" ");
        });
        out.println();
    }

    @Override
    public void visit(PhiInstruction node) {

    }

    @Override
    public void visit(Branch node) {
        out.print("    br ");
        node.getCond().accept(this);
        out.println(" %" + labelId(node.getThen()) + " %" + labelId(node.getElse()));
        out.println();

        visit(node.getThen());
        visit(node.getElse());
    }

    @Override
    public void visit(Return node) {
        out.print("    ret ");
        node.getRet().accept(this);
        out.println();
        out.println();
    }

    @Override
    public void visit(Jump node) {
        out.printf("    jump %%%s\n\n", labelId(node.getTarget()));

        visit(node.getTarget());
    }

    @Override
    public void visit(VirtualRegister node) {
        out.print("$" + regId(node));
    }

    @Override
    public void visit(HeapAllocate node) {
        out.print("    ");
        visit(node.getDest());
        out.print(" = alloc ");
        node.getAllocSize().accept(this);
        out.println();
    }

    @Override
    public void visit(Load node) {
        out.print("    ");
        visit(node.getDest());
        out.printf(" = load %d ", node.getSize());
        node.getAddress().accept(this);
        out.println();
    }

    @Override
    public void visit(Store node) {
        out.printf("    store %d ", node.getSize());
        node.getAddress().accept(this);
        out.print(" ");
        node.getValue().accept(this);
        out.println();
    }

    @Override
    public void visit(Move node) {
        out.print("    ");
        visit(node.getDest());
        out.print(" = move ");
        node.getSource().accept(this);
        out.println();
    }
}
