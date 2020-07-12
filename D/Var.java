import java.util.Map;
import java.util.Set;

public class Var implements Expression {
    public String var;

    @Override
    public int hashCode() {
        return var.hashCode();
    }

    public Var(String var) {
//        System.out.println("create var" + var);
        this.var = var;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public Impl getImpl() {
        return null;
    }

    @Override
    public Var getVar() {
        return this;
    }

    @Override
    public Not getNot() {
        return null;
    }

    @Override
    public BinaryOperation getBinOp() {
        return null;
    }

    @Override
    public boolean equals(Expression expression) {
        return this == expression;
    }

    @Override
    public boolean evaluate(Map<String, Boolean> values) {
        return values.get(var);
    }

    @Override
    public void getVarsNames(Set<String> names) {
        names.add(var);
    }

    @Override
    public void prove(Map<String, Boolean> values, Proof proof, ProofSchemes schemes) {
        proof.addOneLine(this, ProofType.HYPOTHESIS);
    }

    @Override
    public boolean isHypothesis(String name, boolean value) {
        return value && var.equals(name);
    }

    @Override
    public Expression insteadOf(Map<String, Expression> map) {
        return map.get(var);
    }
}

