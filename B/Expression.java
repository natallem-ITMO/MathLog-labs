public interface Expression {

    Impl getImpl();

    Var getVar();

    Not getNot();

    BinaryOperation getBinOp();

    boolean equals(Expression expression);

}
