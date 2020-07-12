import java.util.Objects;

public class BinaryOperation implements Expression {

    Expression left, right;
    String symbol;


    public BinaryOperation(Expression left, Expression right, String symbol) {
        this.left = left;
        this.right = right;
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation){
            BinaryOperation b = (BinaryOperation) obj;
            return b.symbol.equals(symbol) && b.left.equals(left) && b.right.equals(right);
        }
        return false;
    }

    @Override
    public int hashCode() {
         return Objects.hash(left.hashCode(), symbol.hashCode(), right.hashCode());
    }

    @Override
    public String toString() {
//        return "("+symbol+ ","+left+","+right+")";
        return "("+left+symbol+right+")";
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
        return null;
    }

    @Override
    public BinaryOperation getBinOp() {
        return this;
    }

    @Override
    public boolean equals(Expression expression) {
        BinaryOperation b = expression.getBinOp();
        if (b == null) return false;
        return b.symbol.equals(symbol) && b.left.equals(left) && b.right.equals(right);
    }
}
