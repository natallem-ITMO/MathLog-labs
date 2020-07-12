import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Prover {

    List<Expression> axioms;
    Parser parser;
    Map<Expression, Integer> hypothesises;
    Map<Expression, List<Integer>> provedStatements = new HashMap<>();
    Map<Expression, Map<Expression, Integer>> rightPart = new HashMap<>();
    Expression proofStatement;

    private void initializeAxioms() {
        axioms = new ArrayList<>();
        axioms.add(parser.parse("A->B->A"));
        axioms.add(parser.parse("(A->B)->(A->B->C)->A->C"));
        axioms.add(parser.parse("A->B->A&B"));
        axioms.add(parser.parse("A&B->A"));
        axioms.add(parser.parse("A&B->B"));
        axioms.add(parser.parse("A->A|B"));
        axioms.add(parser.parse("B->A|B"));
        axioms.add(parser.parse("(A->C)->(B->C)->(A|B->C)"));
        axioms.add(parser.parse("(A->B)->(A->!B)->!A"));
        axioms.add(parser.parse("!!A->A"));
    }

    public Prover(Parser parser) {
        this.parser = parser;
        initializeAxioms();
    }

    public static void main(String[] args) throws IOException {
        new Prover(new Parser()).prove();
//        Parser parser = new Parser();
//        Expression expr = parser.parse("(!B)");
//        Expression expr1 = parser.parse("B->!B");
//        Impl e = expr1.getImpl();
//        System.out.println(expr.hashCode());
//        System.out.println(e.right.hashCode());
//        System.out.println(e.right.equals(expr));

    }

    public void prove() throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        hypothesises = parser.parseHypothesis(reader.readLine());
        proofStatement = parser.parseProofStatement();
        String line = reader.readLine();
        int firstProved = -1;
        int counter = 1;
        while (line != null) {
            Expression expr = parser.parse(line);
            counter = proveStatement(expr, counter);
            if (counter == 0) {
                incorrect();
                return;
            }
            if (counter < 0) {
                firstProved = -counter;
                counter = firstProved;
            }
            counter++;
            line = reader.readLine();
            if (line == null && !expr.equals(proofStatement)) firstProved = -1;
        }
        if (firstProved != -1) {
            showOutput(firstProved);
        } else
            incorrect();
    }

    private int proveStatement(Expression expression, int counter) {
//        мб это гипотеза, потом проверяю, вдруг это аксиома и если нет, то пытаюсь доказать как МП
        if (provedStatements.containsKey(expression)) return counter - 1;

        boolean proved = false;
        Integer integer = hypothesises.get(expression);
        if (integer != null) {
            List<Integer> list = new ArrayList<>();
            list.add(counter);
            list.add(integer);
            provedStatements.put(expression, list);
            futureMP(expression, counter);
            proved = true;
        }

        if (!proved && tryAxiom(expression, counter)) {
            proved = true;
        }

        if (!proved && tryMP(expression, counter)) {
            proved = true;
        }
        if (!proved) return 0;
        if (expression.equals(proofStatement)) return -counter;
        return counter;
    }

    private void futureMP(Expression expression, int number) {
        Impl impl = expression.getImpl();
        if (impl == null) return;
        Map<Expression, Integer> leftPart = rightPart.computeIfAbsent(impl.right, (x) -> new HashMap<>());
        leftPart.put(impl.left, number);
    }

    private boolean tryAxiom(Expression expression, int counter) {
        for (int i = 0; i < axioms.size(); i++) {
            if (recurse(axioms.get(i), expression, new HashMap<>())) {
                List<Integer> list = new ArrayList<>();
                list.add(counter);
                list.add(-(i+1));
                provedStatements.put(expression, list);

                futureMP(expression, counter);
                return true;
            }
        }
        return false;
    }

    boolean recurse(Expression ax, Expression current, Map<Var, Expression> map) {
        Var v = ax.getVar();
        if (v != null) {
            Expression need = map.computeIfAbsent(v, (x) -> current);
            return need.equals(current);
        }
        BinaryOperation binOp = ax.getBinOp();
        if (binOp != null) {
            BinaryOperation binaryOperation = current.getBinOp();
            if (binaryOperation == null) return false;
            if (binOp.symbol.equals(binaryOperation.symbol)) {
                if (!recurse(binOp.left, binaryOperation.left, map)) return false;
                return recurse(binOp.right, binaryOperation.right, map);
            }
        }
        Not n = ax.getNot();
        if (n != null) {
            Not not = current.getNot();
            if (not == null) return false;
            return recurse(n.expression, not.expression, map);
        }
        return false;
    }

    private boolean tryMP(Expression expression, int counter) {
        Map<Expression, Integer> possible = rightPart.get(expression);
//        System.out.println(expression.hashCode());
//        rightPart.forEach((x)-> System.out.println(x));
        if (possible == null) return false;

        for (Map.Entry<Expression, Integer> mp : possible.entrySet()) {
            List<Integer> list = provedStatements.get(mp.getKey());
            if (list != null) {
                List<Integer> list1 = new ArrayList<>();
                list1.add(counter);
                list1.add(mp.getValue());
                list1.add(list.get(0));
                provedStatements.put(expression, list1);
                futureMP(expression, counter);
                return true;
            }
        }
        return false;
    }

    private void incorrect() {

        System.out.println("Proof is incorrect");
//        System.out.println(proofStatement);
//        System.out.println(hypothesises);
//        System.out.println(provedStatements);
    }

    void showOutput(int start) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (Map.Entry<Expression, List<Integer>> expr : provedStatements.entrySet()) {
            if (expr.getValue().size() != 3) continue;
//            List<Integer> list1 = new ArrayList<>();
//            list1.add(expr.getValue().get(1));
//            list1.add( expr.getValue().get(2));
            graph.put(expr.getValue().get(0), expr.getValue().subList(1,3));
        }
        List<Integer> visited = new ArrayList<>(10);
        for (int i = 0; i <= provedStatements.size(); i++) {
            visited.add(-1);
        }
        dfs(start, graph, visited);
        int counter = 1;
        for (int i = 1; i < visited.size(); i++) {
            if (visited.get(i) == 1) {
                visited.set(i, counter++);
            }
        }
        List<String> output = new ArrayList<>();
        for (int i = 0; i < counter; i++) {
            output.add("");
        }
        for (Map.Entry<Expression, List<Integer>> expr : provedStatements.entrySet()) {
            int num = expr.getValue().get(0);
            if (visited.get(num) != -1) {
                String source;
                String numSource;
                if (expr.getValue().size() == 3) {
                    source = "M.P.";
                    numSource = visited.get(expr.getValue().get(1)) + ", " + visited.get(expr.getValue().get(2));
                } else {
                    int axNumOrHepNum = expr.getValue().get(1);
                    if (axNumOrHepNum > 0) {
                        source = "Hypothesis";
                        numSource = Integer.toString(axNumOrHepNum);
                    } else {
                        source = "Ax. sch.";
                        numSource = Integer.toString(-axNumOrHepNum);
                    }
                }

                output.set(visited.get(num), String.format(
                        "[%d. %s %s] %s",
                        visited.get(num), source, numSource, expr.getKey().toString()));
            }
        }
        String hypothesises = "";
        if (!this.hypothesises.isEmpty())
            hypothesises = this.hypothesises.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(ent -> ent.getKey().toString()).collect(Collectors.joining(", ")) + " ";
        System.out.println(hypothesises + "|- " + proofStatement);
        for (int i = 1; i < counter; i++) {
            System.out.println(output.get(i));
        }

    }

    private void dfs(int v, Map<Integer, List<Integer>> graph, List<Integer> visited) {
        visited.set(v, 1);
        if (graph.get(v) == null) return;
        for (Integer i : graph.get(v)) {
            if (visited.get(i) == -1) {
                dfs(i, graph, visited);
            }
        }
    }

}
