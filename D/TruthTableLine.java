import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TruthTableLine {
    public Expression provableStatement;
    public Map<String, Boolean> hypothesisVars;
    public boolean resultExpression;
    public String sharedVar;
    public TruthTableLine trueTruthTableLine;
    public TruthTableLine falseTruthTableLine;
    private Parser parser;
    public Proof proof;


    public TruthTableLine(Expression provableStatement, Map<String, Boolean> hypothesisVars, boolean resultExpression, Parser parser) {
        this.provableStatement = provableStatement;
        this.hypothesisVars = hypothesisVars;
        this.resultExpression = resultExpression;
        this.parser = parser;
    }

    public TruthTableLine(Expression provableStatement, Map<String, Boolean> hypothesisVars, boolean resultExpression,
                          String sharedVar, TruthTableLine trueTruthTableLine,
                          TruthTableLine falseTruthTableLine, Parser parser) {
        this.provableStatement = provableStatement;
        this.hypothesisVars = hypothesisVars;
        this.resultExpression = resultExpression;
        this.sharedVar = sharedVar;
        this.trueTruthTableLine = trueTruthTableLine;
        this.falseTruthTableLine = falseTruthTableLine;
        this.parser = parser;
    }

    public TruthTableLine compare(TruthTableLine line) {
        String different = "";
        for (Map.Entry<String, Boolean> entry : line.hypothesisVars.entrySet()) {
            String key = entry.getKey();
            if (!hypothesisVars.containsKey(key)) return null;
            boolean otherBool = entry.getValue();
            boolean curBool = hypothesisVars.get(key);
            if ((curBool != otherBool)) {
                if (different.isEmpty()) {
                    different = key;
                } else {
                    return null;
                }
            }
        }
        if (different.isEmpty()) return null;
        Map<String, Boolean> sonMap = new HashMap<>();
        for (Map.Entry<String, Boolean> entry : line.hypothesisVars.entrySet()) {
            if (entry.getKey().equals(different)) continue;
            sonMap.put(entry.getKey(), entry.getValue());
        }
        TruthTableLine trueTableLine = this;
        TruthTableLine falseTableLine = line;
        if (!this.hypothesisVars.get(different)) {
            trueTableLine = line;
            falseTableLine = this;
        }
        return new TruthTableLine(provableStatement, sonMap, resultExpression, different, trueTableLine, falseTableLine, parser);
    }

    public boolean isAllHypothesisTrue() {
        for (Map.Entry<String, Boolean> entry : hypothesisVars.entrySet()) {
            if (!entry.getValue()) return false;
        }
        return true;
    }
    public boolean isAllHypothesisFalse() {
        for (Map.Entry<String, Boolean> entry : hypothesisVars.entrySet()) {
            if (entry.getValue()) return false;
        }
        return true;
    }

    public Proof solve(ProofSchemes schemes) {
        if (sharedVar == null) {
            Proof proof = new Proof();
            provableStatement.prove(hypothesisVars, proof, schemes);
            this.proof = proof;
//            System.out.println("Simple proof\n");
        } else {
            trueTruthTableLine.solve(schemes);
            falseTruthTableLine.solve(schemes);
            this.proof = combine(trueTruthTableLine, falseTruthTableLine, schemes);
//            System.out.println("After combination\n");
        }
//        System.out.println(this.toString());
        return this.proof;
    }

    private Proof combine(TruthTableLine trueProof, TruthTableLine falseProof, ProofSchemes schemes) {
//        System.out.println("\ncombine "+sharedVar+"\n");
        Expression var = parser.getVar(sharedVar);
        trueProof.proof.makeDeduction(schemes, var);
        trueProof.deleteFromHepothesis(sharedVar, var);
//        System.out.println("\nAfter deduction true\n"+trueProof);

        Expression notVar = new Not(var);
        falseProof.proof.makeDeduction(schemes, new Not(var));
        falseProof.deleteFromHepothesis(sharedVar, notVar);
//        System.out.println("\nAfter deduction false\n"+falseProof);

        trueProof.proof.writeInEndOfProof(falseProof.proof);

        Proof combination = schemes.get_combine_after_deduction_start();
        trueProof.proof.writeInEndOfProof(combination.insertInScheme(BinaryOperation.makeMap(var, provableStatement)));
        return trueProof.proof;
    }

    private void deleteFromHepothesis(String sharedVar, Expression expression) {
        hypothesisVars.remove(sharedVar);
        provableStatement = new Impl(expression, provableStatement);
    }

    @Override
    public String toString() {
        if (proof == null) {
            throw new IllegalArgumentException("No proof");
        }
        String start = head();
        return start + proof + "\n\n\n";
    }

    private String head() {
        String res = hypothesisVars.entrySet().stream().map(x -> {
            if (x.getValue()) {
                return x.getKey();
            } else {
                return "!" + x.getKey();
            }
        }).collect(Collectors.joining(", "));
        res += " |- " + provableStatement + "\n";
        return res;

    }
}
