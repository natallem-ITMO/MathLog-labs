import java.util.Map;

public class And extends BinaryOperation {

    public And(Expression left, Expression right) {
        super(left, right, "&");
    }

    @Override
    public boolean evaluate(Map<String, Boolean> values) {
        return (left.evaluate(values) && right.evaluate(values));
    }

    @Override
    public void prove(Map<String, Boolean> values, Proof proof, ProofSchemes schemes) {
        boolean l = left.evaluate(values);
        boolean r = right.evaluate(values);
        if (!(l && r)) throw new IllegalArgumentException("In and l and r not true");
        left.prove(values, proof, schemes);
        right.prove(values, proof, schemes);
        Proof a_b_1_a_and_b = schemes.get_a_b_pr_a_and_b();
        proof.writeInEndOfProof(a_b_1_a_and_b.insertInScheme(makeMap(left, right)));
    }



    @Override
    public Expression insteadOf(Map<String, Expression> map) {
        return new And(left.insteadOf(map), right.insteadOf(map));
    }


}
