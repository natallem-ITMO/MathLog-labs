import java.util.Map;

public class Or extends BinaryOperation {

    public Or(Expression left, Expression right) {
        super(left, right, "|");
    }

    @Override
    public boolean evaluate(Map<String, Boolean> values) {
        return (left.evaluate(values) || right.evaluate(values));
    }

    @Override
    public void prove(Map<String, Boolean> values, Proof proof, ProofSchemes schemes) {
        boolean l = left.evaluate(values);
        boolean r = right.evaluate(values);
        Expression newLeft = left;
        Expression newRight = right;
        Proof scheme = null;
        if (l && r) {
            scheme = schemes.get_a_b_pr_a_or_b();
        }
        if (l && !r) {
            scheme = schemes.get_a_not_b_pr_a_or_b();
            newRight = new Not(right);
        }
        if (!l && r) {
            scheme = schemes.get_not_a_b_pr_a_or_b();
            newLeft = new Not(left);
        }
        if (!l && !r) {
            throw new IllegalArgumentException("In or l or r not true");
        }
        newLeft.prove(values, proof, schemes);
        newRight.prove(values, proof, schemes);
        proof.writeInEndOfProof(scheme.insertInScheme(makeMap(left, right)));
    }

    @Override
    public Expression insteadOf(Map<String, Expression> map) {
        return new Or(left.insteadOf(map), right.insteadOf(map));
    }
}
