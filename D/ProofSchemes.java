import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProofSchemes {
    List<ProofLine> axioms;

    private Proof a_b_pr_a_and_b;
    private Proof not_a_b_pr_not_a_and_b;
    private Proof a_not_b_pr_not_a_and_b;

    private Proof a_b_pr_a_or_b;
    private Proof not_a_b_pr_a_or_b;
    private Proof a_not_b_pr_a_or_b;
    private Proof not_a_not_b_pr_not_a_or_b;

    private Proof a_b_pr_a_ld_b;
    private Proof not_a_b_pr_a_ld_b;
    private Proof a_not_b_pr_not_a_ld_b;

    private Proof a_pr_not_not_a;
    private Proof pr_a_or_not_a;
    
    private Proof pr_a_ld_a;
    private Proof pr_a_ld_not_not_a;

    private Proof deduction_axiom;
    private Proof deduction_hypothesis;
    private Proof deduction_mp;
    private Proof combine_after_deduction_start;

    private Proof contrPosition;

    Parser parser;

    public ProofSchemes(Parser parser) {
        this.parser = parser;
        initializeAxioms();
    }



    public Proof get_a_b_pr_a_and_b() {// A, B |- (A&B)
        if (a_b_pr_a_and_b == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getAxiom(5, "A", "B"));
            list.add(getMP("B->(A&B)", "A", -1));
            list.add(getMP("(A&B)", "B", -1));
            a_b_pr_a_and_b = new Proof(list);
        }
        return a_b_pr_a_and_b;
    }

    public Proof get_a_not_b_pr_not_a_and_b() {// A, !B |- !(A&B)
        if (a_not_b_pr_not_a_and_b == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getAxiom(1, "!B", "(A&B)"));
            list.add(getMP("(A&B)->!B", "!B", -1));
            list.add(getAxiom(4, "A", "B"));
            list.add(getAxiom(9, "A&B", "B"));
            list.add(getMP("((A&B)->!B)->!(A&B)", -2, -1));
            list.add(getMP("!(A&B)", -4, -1));
            a_not_b_pr_not_a_and_b = new Proof(list);
        }
        return a_not_b_pr_not_a_and_b;
    }

    public Proof get_not_a_b_pr_not_a_and_b() {// !A, B |- !(A&B) or !A, !B |- !(A&B)
        if (not_a_b_pr_not_a_and_b == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getAxiom(1, "!A", "(A&B)"));
            list.add(getMP("(A&B)->!A", "!A", -1));
            list.add(getAxiom(3, "A", "B"));
            list.add(getAxiom(9, "A&B", "A"));
            list.add(getMP("((A&B)->!A)->!(A&B)", -2, -1));
            list.add(getMP("!(A&B)", -4, -1));
            not_a_b_pr_not_a_and_b = new Proof(list);
        }
        return not_a_b_pr_not_a_and_b;
    }

    public Proof get_not_a_not_b_pr_not_a_and_b() {// !A, B |- !(A&B) or !A, !B |- !(A&B)
        return get_not_a_b_pr_not_a_and_b();
    }


    public Proof get_a_b_pr_a_or_b() {
        if (a_b_pr_a_or_b == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getAxiom(6, "A", "B"));
            list.add(getMP("A|B", "A", -1));
            a_b_pr_a_or_b = new Proof(list);
        }
        return a_b_pr_a_or_b;
    }

    public Proof get_a_not_b_pr_a_or_b() {
        if (a_not_b_pr_a_or_b == null) {
            a_not_b_pr_a_or_b = get_a_b_pr_a_or_b();
        }
        return a_not_b_pr_a_or_b;
    }

    public Proof get_not_a_b_pr_a_or_b() {
        if (not_a_b_pr_a_or_b == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getAxiom(7, "A", "B"));
            list.add(getMP("A|B", "B", -1));
            not_a_b_pr_a_or_b = new Proof(list);
        }
        return not_a_b_pr_a_or_b;
    }

    public Proof get_not_a_not_b_pr_not_a_or_b() {
        if (not_a_not_b_pr_not_a_or_b == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getAxiom(1, "!B", "!A"));
            list.add(getMP("!A->!B", "!B", -1));
            Proof proof = get_contrPosition().insertInScheme(BinaryOperation.makeMap(parser.parse("!A"), parser.parse("!B")));
//            System.out.println("HERE IS CONTRAPOSITION");
//            System.out.println(proof);
            list.addAll(proof.proof);
            list.add(getMP("!!B->!!A", "!A->!B", -1));
            list.add(getAxiom(2, "B", "!!B", "A"));
            list.addAll(get_pr_a_ld_not_not_a().insertInScheme(BinaryOperation.makeMap(parser.parse("B"))).proof);
            list.add(getAxiom(1, "!!B->A", "B"));
            list.add(getAxiom(2, "!!B", "!!A", "A"));
            list.add(getAxiom(1, "!!A->A", "!!B"));
            list.add(getAxiom(10, "A"));
            list.add(getMP("!!B->(!!A->A)", -1, -2));
            list.add(getMP("(!!B->!!A->A)->(!!B->A)", "!!B->!!A", -4));
            list.add(getMP("!!B->A", -2, -1));
            list.add(getMP("B->!!B->A", -1, -7));
            list.add(getMP("(B->!!B->A)->B->A",  "B->!!B",getAxiom(2, "B", "!!B", "A").expression));
            list.add(getMP("B->A", -2, -1));
            list.add(getAxiom(8, "A", "B", "A"));
            Proof alpha_alpha = get_pr_a_ld_a().insertInScheme(BinaryOperation.makeMap(parser.parse("A")));
            list.addAll(alpha_alpha.proof);
            list.add(getMP("(B->A)->(A|B->A)", -1, -(alpha_alpha.proof.size() + 1)));
            list.add(getMP("(A|B->A)", "B->A", -1));
            list.addAll(get_contrPosition().insertInScheme(BinaryOperation.makeMap(parser.parse("A|B"), parser.parse("A"))).proof);
            list.add(getMP("!A->!(A|B)", "(A|B->A)", -1));
            list.add(getMP("!(A|B)", "!A", -1));
            not_a_not_b_pr_not_a_or_b = new Proof(list);
        }
        return not_a_not_b_pr_not_a_or_b;
    }


    public Proof get_a_b_pr_a_ld_b() {
        if (a_b_pr_a_ld_b == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getAxiom(1, "B", "A"));
            list.add(getMP("A->B", "B", -1));
            a_b_pr_a_ld_b = new Proof(list);
        }
        return a_b_pr_a_ld_b;
    }

    public Proof get_a_not_b_pr_not_a_ld_b() {
        if (a_not_b_pr_not_a_ld_b == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getAxiom(1, "A->B", "A->B"));
            list.add(getAxiom(1, "A->B", "(A->B)->(A->B)"));
            list.add(getAxiom(2, "A->B", "(A->B)->(A->B)", "A->B"));
            list.add(getMP("((A->B)->((A->B)->(A->B))->(A->B))->(A->B)->(A->B)", -3, -1));
            list.add(getMP("(A->B)->(A->B)", -3, -1));
            list.add(getAxiom(1, "A", "A->B"));
            list.add(getMP("(A->B)->A", "A", -1));
            list.add(getAxiom(2, "A->B", "A", "B"));
            list.add(getMP("((A->B)->A->B)->(A->B)->B", -2, -1));
            list.add(getMP("(A->B)->B", -5, -1));
            list.add(getAxiom(1, "!B", "A->B"));
            list.add(getMP("(A->B)->!B", "!B", -1));
            list.add(getAxiom(9, "A->B", "B"));
            list.add(getMP("((A->B)->!B)->!(A->B)", -4, -1));
            list.add(getMP("!(A->B)", -3, -1));
            a_not_b_pr_not_a_ld_b = new Proof(list);
        }
        return a_not_b_pr_not_a_ld_b;
    }

    public Proof get_not_a_b_pr_a_ld_b() {
        if (not_a_b_pr_a_ld_b == null) {
            not_a_b_pr_a_ld_b = get_a_b_pr_a_ld_b();
        }
        return not_a_b_pr_a_ld_b;
    }

    public Proof get_not_a_not_b_pr_a_ld_b() {
        LinkedList<ProofLine> list = new LinkedList<>();
        list.add(getAxiom(1, "!A", "!B"));
        list.add(getMP("!B->!A", "!A", -1));
        list.addAll(get_contrPosition().insertInScheme(BinaryOperation.makeMap(parser.parse("!B"), parser.parse("!A"))).proof);
        list.add(getMP("!!A->!!B", "!B->!A", -1));
        list.add(getAxiom(2, "A", "!!A", "B"));
        list.addAll(get_pr_a_ld_not_not_a().insertInScheme(BinaryOperation.makeMap(parser.parse("A"))).proof);
        list.add(getAxiom(1, "!!A->B", "A"));
        list.add(getAxiom(2, "!!A", "!!B", "B"));
        list.add(getMP("(!!A->!!B->B)->(!!A->B)", "!!A->!!B", -1));
        list.add(getAxiom(1, "!!B->B", "!!A"));
        list.add(getAxiom(10, "B"));
        list.add(getMP("!!A->!!B->B", -1, -2));
        list.add(getMP("!!A->B", -1, -4));
        list.add(getMP("A->!!A->B", -1, -7));
        list.add(getMP("(A->!!A->B)->(A->B)", "A->!!A", "(A->!!A)->(A->!!A->B)->(A->B)"));
        list.add(getMP("A->B", -2, -1));
        return new Proof(list);
    }


    public Proof get_a_pr_not_not_a() {
        if (a_pr_not_not_a==null){
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getAxiom(1, "A","!A"));
            list.add(getMP("!A->A", "A", -1));
            list.add(getAxiom(9, "!A","A"));
            list.add(getMP("(!A->!A)->!!A", -2,-1));
            list.addAll(get_pr_a_ld_a().insertInScheme(BinaryOperation.makeMap(parser.parse("!A"))).proof);
            list.add(getMP("!!A", -1,"(!A->!A)->!!A"));
            a_pr_not_not_a= new Proof(list);
        }
        return a_pr_not_not_a;
    }


    public Proof get_pr_a_ld_not_not_a() {
        if (pr_a_ld_not_not_a==null){
            Proof proof = get_a_pr_not_not_a().insertInScheme(BinaryOperation.makeMap(parser.parse("A")));
            proof.proof.addFirst(getHypothesis("A"));
            proof.makeDeduction(this, parser.parse("A"));
            pr_a_ld_not_not_a= proof;
        }
        return pr_a_ld_not_not_a;

    }

    public Proof get_pr_a_ld_a() {
        if (pr_a_ld_a == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getAxiom(1, "A", "A"));
            list.add(getAxiom(1, "A","A->A"));
            list.add(getAxiom(2, "A","A->A","A"));
            list.add(getMP("(A->(A->A)->A)->(A->A)",-3,-1));
            list.add(getMP("(A->A)", -3, -1));
            pr_a_ld_a = new Proof(list);
        }
        return pr_a_ld_a;
    }

    public Proof get_pr_a_or_not_a() {

        if (pr_a_or_not_a == null) {
            Proof contrPosition = get_contrPosition();

            Proof proof = contrPosition.insertInScheme(BinaryOperation.makeMap(parser.parse("A"), parser.parse("A|!A")));
            proof.proof.addFirst(getAxiom(6, "A", "!A"));
            Expression step1 = parser.parse("!(A|!A)->!A");
            proof.proof.add(new ProofLine(step1, ProofType.MP, proof.proof.getFirst().expression, proof.proof.getLast().expression));

            Proof proof2 = contrPosition.insertInScheme(BinaryOperation.makeMap(parser.parse("!A"), parser.parse("A|!A")));
            proof2.proof.addFirst(getAxiom(7, "A", "!A"));
            Expression step2 = parser.parse("!(A|!A)->!!A");
            proof2.proof.add(new ProofLine(step2, ProofType.MP, proof2.proof.getFirst().expression, proof2.proof.getLast().expression));

            proof.writeInEndOfProof(proof2);

            Proof endProof = new Proof();
            endProof.proof.add(getAxiom(9, "!(A|!A)", "!A"));
            endProof.proof.add(new ProofLine(parser.parse("(!(A|!A)->!!A)->!!(A|!A)"), ProofType.MP, step1, -1));
            endProof.proof.add(new ProofLine(parser.parse("!!(A|!A)"), ProofType.MP, step2, -1));
            endProof.proof.add(getAxiom(10, "(A|!A)"));
            endProof.proof.add(new ProofLine(parser.parse("(A|!A)"), ProofType.MP, -2, -1));
            Proof proof1 = endProof.insertInScheme(BinaryOperation.makeMap(parser.parse("A")));
            proof.writeInEndOfProof(proof1);

            pr_a_or_not_a = proof;
        }
        return pr_a_or_not_a;
    }


    private Proof get_contrPositionPattern() {
        LinkedList<ProofLine> list = new LinkedList<>();
        list.add(getAxiom(9, "A","B"));
        list.add(getHypothesis("A->B"));
        list.add(getMP("(A->!B)->!A",-1, -2));
        list.add(getAxiom(1, "!B","A"));
        list.add(getHypothesis("!B"));
        list.add(getMP("(A->!B)",-1, -2));
        list.add(getMP("!A",-1, -4));
        return new Proof(list);
    }

    public Proof get_contrPosition() {
        if (contrPosition == null) {
            contrPosition = get_contrPositionPattern().insertInScheme(BinaryOperation.makeMap(parser.parse("A"), parser.parse("B")));
            contrPosition.makeDeduction(this, new Not(parser.parse("B")));
            contrPosition.makeDeduction(this, parser.parse("A->B"));
        }
        return contrPosition;
    }


    public Proof get_deduction_alpha() {
        return get_pr_a_ld_a();
    }

    public Proof get_deduction_axiom() {//B is axiom
        if (deduction_axiom == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(new ProofLine(parser.parse("B"), ProofType.AXIOM, -1));
            list.add(getAxiom(1,"B","A"));
            list.add(getMP("A->B","B", -1));
            deduction_axiom = new Proof(list);
        }
        return deduction_axiom;
    }

    public Proof get_deduction_mp() {//A=a->dj B=a->(dj->di) C=(a->di)
        if (deduction_mp == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(new ProofLine(parser.parse("A->B->C"), ProofType.AXIOM,20));
            list.add(getMP("B->C", "A", -1));
            list.add(getMP("C", "B", -1));
            deduction_mp = new Proof(list);
        }
        return deduction_mp;
    }

    public Proof get_deduction_hypothesis() {
        if (deduction_hypothesis == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getHypothesis("B"));
            list.add(getAxiom(1,"B","A"));
            list.add(getMP("A->B","B", -1));
            deduction_hypothesis = new Proof(list);
        }
        return deduction_hypothesis;
    }

     /*
    if (==null){
                LinkedList<ProofLine> list = new LinkedList<>();
                = new Proof(list);
            }
            return ;
     */


    /**
     * "A->B->((C|!C)->D)"
     * "B->((C|!C)->D)"
     * "(C|!C)->D"
     */
    public Proof get_combine_after_deduction_start() {
        if (combine_after_deduction_start == null) {
            LinkedList<ProofLine> list = new LinkedList<>();
            list.add(getAxiom(8, "A", "!A", "B"));
            list.add(getMP("(!A->B)->((A|!A)->B)", "A->B", -1));
            list.add(getMP("((A|!A)->B)", "!A->B", -1));
            list.addAll(get_pr_a_or_not_a().proof);
            list.add(getMP("B", "A|!A", "(A|!A)->B"));
            combine_after_deduction_start = new Proof(list);
        }
        return combine_after_deduction_start;
    }


    private void initializeAxioms() {
        axioms = new ArrayList<>();
        axioms.add(new ProofLine(parser.parse("A->B->A"), ProofType.AXIOM));
        axioms.add(new ProofLine(parser.parse("(A->B)->(A->B->C)->A->C"), ProofType.AXIOM));
        axioms.add(new ProofLine(parser.parse("A&B->A"), ProofType.AXIOM));
        axioms.add(new ProofLine(parser.parse("A&B->B"), ProofType.AXIOM));
        axioms.add(new ProofLine(parser.parse("A->B->A&B"), ProofType.AXIOM));
        axioms.add(new ProofLine(parser.parse("A->A|B"), ProofType.AXIOM));
        axioms.add(new ProofLine(parser.parse("B->A|B"), ProofType.AXIOM));
        axioms.add(new ProofLine(parser.parse("(A->C)->(B->C)->(A|B->C)"), ProofType.AXIOM));
        axioms.add(new ProofLine(parser.parse("(A->B)->(A->!B)->!A"), ProofType.AXIOM));
        axioms.add(new ProofLine(parser.parse("!!A->A"), ProofType.AXIOM));
        for (int i = 1; i<= axioms.size(); i++){
            axioms.get(i-1).axiom_number = i;
        }
    }

    public ProofLine getAxiom(int num, String A, String B, String C) {
        return axioms.get(num - 1).insertInProofLine(BinaryOperation.makeMap(parser.parse(A), parser.parse(B), parser.parse(C)));
    }

    public ProofLine getAxiom(int num, String A, String B) {
        return axioms.get(num - 1).insertInProofLine(BinaryOperation.makeMap(parser.parse(A), parser.parse(B)));
    }

    public ProofLine getAxiom(int num, String A) {
        return axioms.get(num - 1).insertInProofLine(BinaryOperation.makeMap(parser.parse(A)));
    }

    public ProofLine getAxiom(int num, Expression A, Expression B, Expression C) {
        return axioms.get(num - 1).insertInProofLine(BinaryOperation.makeMap(A, B, C));
    }

    public ProofLine getAxiom(int num, Expression A, Expression B) {
        return axioms.get(num - 1).insertInProofLine(BinaryOperation.makeMap(A, B));
    }

    public ProofLine getAxiom(int num, Expression A) {
        return axioms.get(num - 1).insertInProofLine(BinaryOperation.makeMap(A));
    }

    private ProofLine getMP(String expr, String dj, String dj_i) {
        return new ProofLine(parser.parse(expr), ProofType.MP, parser.parse(dj), parser.parse(dj_i));
    }

    private ProofLine getMP(String expr, Expression dj, String dj_i) {
        return new ProofLine(parser.parse(expr), ProofType.MP, dj, parser.parse(dj_i));
    }
    private ProofLine getMP(String expr, String dj, Expression dj_i) {
        return new ProofLine(parser.parse(expr), ProofType.MP,  parser.parse(dj), dj_i);
    }

    private ProofLine getMP(String expr, int dj, String dj_i) {
        return new ProofLine(parser.parse(expr), ProofType.MP, dj, parser.parse(dj_i));
    }

    private ProofLine getMP(String expr, String dj, int dj_i) {
        return new ProofLine(parser.parse(expr), ProofType.MP, parser.parse(dj), dj_i);
    }

    private ProofLine getMP(String expr, int dj, int dj_i) {
        return new ProofLine(parser.parse(expr), ProofType.MP, dj, dj_i);
    }

    private ProofLine getHypothesis(String a) {
        return new ProofLine(parser.parse(a), ProofType.HYPOTHESIS);
    }
}
