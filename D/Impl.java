import java.util.Map;

public class Impl extends BinaryOperation {

    public Impl(Expression left, Expression right) {
        super(left, right, "->");
    }

    @Override
    public Impl getImpl() {
        return this;
    }

    @Override
    public boolean evaluate(Map<String, Boolean> values) {
        boolean a = left.evaluate(values);
        boolean b = right.evaluate(values);
        return !(a && !b);
    }

    @Override
    public void prove(Map<String, Boolean> values, Proof proof, ProofSchemes schemes) {
        boolean l = left.evaluate(values);
        boolean r = right.evaluate(values);
        Expression newLeft = left;
        Expression newRight = right;
        Proof scheme = null;
        if (l && r) {
            scheme = schemes.get_a_b_pr_a_ld_b();
        }
        if (l && !r) {
            throw new IllegalArgumentException("In lead l or r not true");
        }
        if (!l && r) {
            scheme = schemes.get_not_a_b_pr_a_ld_b();
            newLeft = new Not(left);
        }
        if (!l && !r) {
            scheme = schemes.get_not_a_not_b_pr_a_ld_b();
            newLeft = new Not(left);
            newRight = new Not(right);
        }
        newLeft.prove(values, proof, schemes);
        newRight.prove(values, proof, schemes);
        proof.writeInEndOfProof(scheme.insertInScheme(makeMap(left, right)));
    }

    @Override
    public Expression insteadOf(Map<String, Expression> map) {
        return new Impl(left.insteadOf(map), right.insteadOf(map));
    }
}
