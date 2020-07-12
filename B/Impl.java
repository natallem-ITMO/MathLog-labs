public class Impl extends BinaryOperation {

    public Impl(Expression left, Expression right) {
        super(left, right, "->");
    }

    @Override
    public Impl getImpl() {
        return this;
    }
}
