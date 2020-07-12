public class Not implements Expression {
    Expression expression;

    public Not(Expression right) {

        expression = right;
    }

    @Override
    public String toString() {
        return "(!"+expression+")";
    }
}
