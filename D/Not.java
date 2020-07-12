import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Not implements Expression {
    Expression expression;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Not) {
            Not n = (Not) obj;
            return expression.equals(n.expression);
        }
        return false;
    }

    public Not(Expression right) {
        expression = right;
    }

    @Override
    public String toString() {
        return "!" + expression;
    }

    @Override
    public Impl getImpl() {
        return null;
    }

    @Override
    public Var getVar() {
        return null;
    }

    @Override
    public Not getNot() {
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash("!".hashCode(), expression.hashCode());
    }

    @Override
    public BinaryOperation getBinOp() {
        return null;
    }

    @Override
    public boolean equals(Expression expression) {
        Not n = expression.getNot();
        if (n == null) return false;
        return this.expression.equals(n.expression);
    }

    @Override
    public boolean evaluate(Map<String, Boolean> values) {
        return !expression.evaluate(values);
    }

    @Override
    public void getVarsNames(Set<String> names) {
        expression.getVarsNames(names);
    }

    @Override
    public boolean isHypothesis(String name, boolean value) {
        if (!value) {
            if (expression.getVar() != null) {
                return expression.getVar().var.equals(name);
            }
        }
        return false;
    }

    @Override
    public void prove(Map<String, Boolean> values, Proof proof, ProofSchemes schemes) {
        if (expression.getVar() != null) {
            Var var = expression.getVar();
            if (values.get(var.var) != null && values.get(var.var)) {
                throw new IllegalArgumentException("In !var not true");
            }
            proof.addOneLine(this, ProofType.HYPOTHESIS);
            return;
        }
        if (expression instanceof Not) {
            Not expr = (Not) expression;
            expr.expression.prove(values, proof, schemes);
            proof.writeInEndOfProof(schemes.get_a_pr_not_not_a().insertInScheme(BinaryOperation.makeMap(expr.expression)));
        }

        if (expression instanceof And) {

            And expr = (And) expression;

            boolean l = expr.left.evaluate(values);
            boolean r = expr.right.evaluate(values);
            Expression newLeft = expr.left;
            Expression newRight = expr.right;
            Proof scheme = null;
            if (l && r) {
                throw new IllegalArgumentException("In not(and) l and r are true");
            }
            if (l && !r) {
                scheme = schemes.get_a_not_b_pr_not_a_and_b();
                newRight = new Not(expr.right);
            }
            if (!l && r) {
                scheme = schemes.get_not_a_b_pr_not_a_and_b();
                newLeft = new Not(expr.left);
            }
            if (!l && !r) {
                scheme = schemes.get_not_a_not_b_pr_not_a_and_b();
                newLeft = new Not(expr.left);
                newRight = new Not(expr.right);
            }
            newLeft.prove(values, proof, schemes);
            newRight.prove(values, proof, schemes);
            proof.writeInEndOfProof(scheme.insertInScheme(BinaryOperation.makeMap(expr.left, expr.right)));
        }

        if (expression instanceof Or) {
            Or expr = (Or) expression;

            boolean l = expr.left.evaluate(values);
            boolean r = expr.right.evaluate(values);
            Expression newLeft = expr.left;
            Expression newRight = expr.right;
            Proof scheme = null;
            if (l || r) {
                throw new IllegalArgumentException("In not(or) l or r are true");
            }
            if (!l && !r) {
                scheme = schemes.get_not_a_not_b_pr_not_a_or_b();
                newLeft = new Not(expr.left);
                newRight = new Not(expr.right);
            }
            newLeft.prove(values, proof, schemes);
            newRight.prove(values, proof, schemes);
            proof.writeInEndOfProof(scheme.insertInScheme(BinaryOperation.makeMap(expr.left, expr.right)));
        }

        if (expression instanceof Impl) {
            Impl expr = (Impl) expression;

            boolean l = expr.left.evaluate(values);
            boolean r = expr.right.evaluate(values);
            Expression newLeft = expr.left;
            Expression newRight = expr.right;
            Proof scheme = null;
            if (l && !r) {
                scheme = schemes.get_a_not_b_pr_not_a_ld_b();
                newRight = new Not(expr.right);
            } else {
                throw new IllegalArgumentException("In not(or) l or r are true");
            }
            newLeft.prove(values, proof, schemes);
            newRight.prove(values, proof, schemes);
            proof.writeInEndOfProof(scheme.insertInScheme(BinaryOperation.makeMap(expr.left, expr.right)));
        }
    }

    @Override
    public Expression insteadOf(Map<String, Expression> map) {
        return new Not(expression.insteadOf(map));
    }
}
