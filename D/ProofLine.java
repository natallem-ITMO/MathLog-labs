import java.util.Map;

public class ProofLine {
    public static boolean showSource = false;
    public Expression expression;
    public ProofType proofType;
    /*
    if m.p. then
    dj
    dj->di
    di //this is our proofLine
     */
    public LinkExpression dj;
    public LinkExpression dj_i;
    public int axiom_number;


    public ProofLine(Expression expression, ProofType proofType, LinkExpression link_dj, LinkExpression link_dj_di) {
        this.expression = expression;
        this.proofType = proofType;
        this.dj = link_dj;
        this.dj_i = link_dj_di;
    }

    public ProofLine(Expression expression, ProofType proofType, int axiom_number) {
        this.expression = expression;
        this.proofType = proofType;
        this.axiom_number = axiom_number;
    }

    public ProofLine(Expression expression, ProofType proofType, Expression link_dj, int link_dj_di) {
        this.expression = expression;
        this.proofType = proofType;
        this.dj = new LinkExpression(link_dj);
        this.dj_i = new LinkExpression(link_dj_di);
    }

    public ProofLine(Expression expression, ProofType proofType, int link_dj, Expression link_dj_di) {
        this.expression = expression;
        this.proofType = proofType;
        this.dj = new LinkExpression(link_dj);
        this.dj_i = new LinkExpression(link_dj_di);
    }

    public ProofLine(Expression expression, ProofType proofType, int link_dj, int link_dj_di) {
        this.expression = expression;
        this.proofType = proofType;
        this.dj = new LinkExpression(link_dj);
        this.dj_i = new LinkExpression(link_dj_di);
    }

    public ProofLine(Expression expression, ProofType proofType, Expression link_dj, Expression link_dj_di) {
        this.expression = expression;
        this.proofType = proofType;
        this.dj = new LinkExpression(link_dj);
        this.dj_i = new LinkExpression(link_dj_di);
    }

    public ProofLine(Expression expression, ProofType proofType) {
        this.expression = expression;
        this.proofType = proofType;
    }

    @Override
    public String toString() {
        String result = expression.toString();
        if (showSource) {
            if (proofType == ProofType.MP) {
                result += " [" + proofType.toString() + " {" + dj.expression + " , " + dj_i.expression + "} ]";
            } else if (proofType == ProofType.AXIOM) {
                result += " [" + proofType.toString() + " {№" + axiom_number + "} ]";
            } else {
                result += " [" + proofType.toString() + "]";
            }
        }
        return result;
    }

    public ProofLine(Expression expression, ProofType proofType, LinkExpression dj, LinkExpression dj_i, int axiom_number) {
        this.expression = expression;
        this.proofType = proofType;
        this.dj = dj;
        this.dj_i = dj_i;
        this.axiom_number = axiom_number;
    }

    public ProofLine insertInProofLine(Map<String, Expression> name_expression) {
        return new ProofLine(expression.insteadOf(name_expression), proofType, dj, dj_i, axiom_number);
    }
}
