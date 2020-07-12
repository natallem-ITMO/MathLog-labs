import java.util.Objects;

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
}
