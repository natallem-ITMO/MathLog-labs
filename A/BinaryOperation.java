public class BinaryOperation implements Expression {

    Expression left, right;
    String symbol;

    public BinaryOperation(Expression left, Expression right, String symbol) {
        this.left = left;
        this.right = right;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "("+symbol+ ","+left+","+right+")";
    }
}
