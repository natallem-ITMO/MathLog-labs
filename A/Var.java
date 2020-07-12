public class Var implements Expression {
    String var;

    @Override
    public int hashCode() {
        return var.hashCode();
    }

    public Var(String var) {
        this.var = var;
    }


    @Override
    public String toString() {
        return var;
    }
}

