import java.util.*;
import java.util.stream.Collectors;

public class Proof {

    public LinkedList<ProofLine> proof;

    public Proof() {
        proof = new LinkedList<>();
    }

    public Proof(LinkedList<ProofLine> otherProof) {
        proof = otherProof;
    }

    public Proof insertInScheme(Map<String, Expression> name_expression) {
        LinkedList<ProofLine> proofLines = new LinkedList<>();
        for (ProofLine schemeLine : proof) {
            ProofLine proofLine = schemeLine.insertInProofLine(name_expression);
            if (schemeLine.proofType == ProofType.MP) {
                proofLine.dj = getModusPonens(schemeLine.dj, proofLines, name_expression);
                proofLine.dj_i = getModusPonens(schemeLine.dj_i, proofLines, name_expression);
            }
            if (schemeLine.proofType == ProofType.AXIOM) {
                proofLine.axiom_number = schemeLine.axiom_number;
            }
            proofLines.add(proofLine);
        }
        return new Proof(proofLines);
    }

    private LinkExpression getModusPonens(LinkExpression s, LinkedList<ProofLine> proofLines, Map<String, Expression> name_expression) {
        if (s.expression == null) {
            Iterator<ProofLine> proofLineIterator = proofLines.descendingIterator();
            for (int i = 0; i > s.number + 1; i--) {
                proofLineIterator.next();
            }
            return new LinkExpression(proofLineIterator.next().expression);
        } else {
            Expression expression = s.expression.insteadOf(name_expression);
            return new LinkExpression(expression);
        }
    }

    public Expression getProofExpression() {
        return proof.getLast().expression;
    }


    public void writeInEndOfProof(Proof otherProof) {
        proof.addAll(otherProof.proof);
        otherProof.proof.clear();
    }

    public void addOneLine(Expression expr, ProofType type) {
        proof.add(new ProofLine(expr, type));
    }

    public void makeDeduction(ProofSchemes schemes, Expression alpha) {
        ListIterator<ProofLine> itr = proof.listIterator();
        while (itr.hasNext()) {
            ProofLine line = itr.next();
            if (line.proofType == ProofType.AXIOM) {
                makeAxiom(itr, schemes, alpha, line.expression);
                itr.remove();
            } else if (line.proofType == ProofType.MP) {
                makeMP(itr, schemes, alpha, line.dj.expression, line.dj_i.expression, line.expression);
                itr.remove();
            } else {
                if (line.expression.equals(alpha)) {
                    makeAlpha(itr, schemes, alpha);
                    itr.remove();
                } else {
                    makeHypothesis(itr, schemes, alpha, line.expression);
                    itr.remove();
                }
            }
        }
    }

    private void makeHypothesis(ListIterator<ProofLine> itr, ProofSchemes schemes, Expression alpha, Expression expression) {
        itr.previous();
        Proof proofAxiom = schemes.get_deduction_hypothesis();
        Proof proof = proofAxiom.insertInScheme(BinaryOperation.makeMap(alpha, expression));
        for (ProofLine v : proof.proof) {
            itr.add(v);
        }
        itr.next();
    }

    private void makeAlpha(ListIterator<ProofLine> itr, ProofSchemes schemes, Expression alpha) {
        itr.previous();
        Proof proofAxiom = schemes.get_deduction_alpha();
        Proof proof = proofAxiom.insertInScheme(BinaryOperation.makeMap(alpha));
        for (ProofLine v : proof.proof) {
            itr.add(v);
        }
        itr.next();
    }

    private void makeMP(ListIterator<ProofLine> itr, ProofSchemes schemes, Expression alpha, Expression dj, Expression dj_di, Expression di) {
        itr.previous();
        Proof proofAxiom = schemes.get_deduction_mp();//A=a->dj B=a->(dj->di) C=(a->di)
        Proof proof = proofAxiom.insertInScheme(BinaryOperation.makeMap(
                new Impl(alpha, dj), new Impl(alpha, dj_di), new Impl(alpha, di)));
        for (ProofLine v : proof.proof) {
            itr.add(v);
        }
        itr.next();
    }

    private void makeAxiom(ListIterator<ProofLine> itr, ProofSchemes schemes, Expression alpha, Expression axiom) {
        itr.previous();
        Proof proofAxiom = schemes.get_deduction_axiom();
        Proof proof = proofAxiom.insertInScheme(BinaryOperation.makeMap(alpha, axiom));
        for (ProofLine v : proof.proof) {
            itr.add(v);
        }
        itr.next();
    }

    @Override
    public String toString() {
        return proof.stream().map(ProofLine::toString).collect(Collectors.joining("\n"));
    }
}


