import java.util.Map;
import java.util.Set;

public interface Expression {

    Impl getImpl();

    Var getVar();

    Not getNot();

    BinaryOperation getBinOp();

    boolean equals(Expression expression);

    boolean evaluate(Map<String, Boolean> values);

    void getVarsNames(Set<String> names);

    void prove(Map<String, Boolean> values, Proof proof, ProofSchemes schemes);

    Expression insteadOf(Map<String, Expression> map);

    boolean isHypothesis(String name, boolean value);

}
