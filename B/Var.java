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
}

