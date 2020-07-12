import java.io.*;
import java.util.*;

public class Solver {
    Parser parser;
    Expression proofStatement;
    ProofSchemes schemes;

    public Solver(Parser parser) {
        this.parser = parser;
        schemes = new ProofSchemes(this.parser);
    }

    TruthTableLine makeTruthTable() {
        Set<String> varsNames = new LinkedHashSet<>();
        proofStatement.getVarsNames(varsNames);
        int varsCount = varsNames.size();
        int booleanVector = 1 << varsCount;
        TruthTableLine truthTableLineAll1 = getThruthTableLine(varsNames, booleanVector - 1, null);
        TruthTableLine resultTableLine = null;

        if (truthTableLineAll1.resultExpression) {
            resultTableLine = firstSolution(varsNames, booleanVector, truthTableLineAll1);
        } else {
            proofStatement = new Not(proofStatement);
            TruthTableLine truthTableLineAll0 = getThruthTableLine(varsNames, 0, null);
            if (!truthTableLineAll0.resultExpression) {
                return null;
            }
            resultTableLine = secondSolution(varsNames, booleanVector);
        }
        Proof proof = resultTableLine.solve(schemes);
        resultTableLine.proof = proof;
//        System.out.println("RESULT\n\n" + proof);
        return resultTableLine;
    }

    private TruthTableLine firstSolution(Set<String> varsNames, int booleanVector, TruthTableLine truthTableLineStart) {
        List<TruthTableLine> truthTableLines = new ArrayList<>();
        truthTableLines.add(truthTableLineStart);
        for (int i = 0; i < booleanVector - 1; i++) {
            TruthTableLine truthTableLine = getThruthTableLine(varsNames, i, true);
            if (truthTableLine != null) {
                truthTableLines.add(truthTableLine);
            }
        }
        List<List<TruthTableLine>> listOfAll = new ArrayList<>();
        listOfAll.add(truthTableLines);
        int num = 0;
        while (true) {
            List<TruthTableLine> currentList = listOfAll.get(num++);
            List<TruthTableLine> nextList = new ArrayList<>();
            for (int i = 0; i < currentList.size(); i++) {
                for (int j = i + 1; j < currentList.size(); j++) {
                    TruthTableLine truthTableLine1 = currentList.get(i);
                    TruthTableLine truthTableLine2 = currentList.get(j);
                    TruthTableLine compare = truthTableLine2.compare(truthTableLine1);
                    if (compare != null) {
                        nextList.add(compare);
                    }
                }
            }
            if (nextList.isEmpty()) {
                return pickTableLine(listOfAll, true);
            } else {
                listOfAll.add(nextList);
            }
        }
    }


    private TruthTableLine secondSolution(Set<String> varsNames, int booleanVector) {
        List<TruthTableLine> truthTableLines = new ArrayList<>();
        for (int i = 0; i < booleanVector ; i++) {
            TruthTableLine truthTableLine = getThruthTableLine(varsNames, i, true);
            if (truthTableLine != null) {
                truthTableLines.add(truthTableLine);
            }
        }
        List<List<TruthTableLine>> listOfAll = new ArrayList<>();
        listOfAll.add(truthTableLines);
        int num = 0;
        while (true) {
            List<TruthTableLine> currentList = listOfAll.get(num++);
            List<TruthTableLine> nextList = new ArrayList<>();
            for (int i = 0; i < currentList.size(); i++) {
                for (int j = i + 1; j < currentList.size(); j++) {
                    TruthTableLine truthTableLine1 = currentList.get(i);
                    TruthTableLine truthTableLine2 = currentList.get(j);
                    TruthTableLine compare = truthTableLine2.compare(truthTableLine1);
                    if (compare != null) {
                        nextList.add(compare);
                    }
                }
            }
            if (nextList.isEmpty()) {
                return pickTableLine(listOfAll, false);
            } else {
                listOfAll.add(nextList);
            }
        }
    }

    private TruthTableLine pickTableLine(List<List<TruthTableLine>> listOfAll, boolean needTrue) {
        if (needTrue) {
            for (int i = listOfAll.size() - 1; i >= 0; i--) {
                for (TruthTableLine table : listOfAll.get(i)) {
                    if (table.isAllHypothesisTrue()) return table;
                }
            }
        } else {
            for (int i = listOfAll.size() - 1; i >= 0; i--) {
                for (TruthTableLine table : listOfAll.get(i)) {
                    if (table.isAllHypothesisFalse()) return table;
                }
            }
        }
        return null;
    }

    private TruthTableLine getThruthTableLine(Set<String> varNames, int i, Boolean predicate) {
        Map<String, Boolean> varsValues = new HashMap<>();
        int j = 1;
        for (String name : varNames) {
            boolean value = (i & j) != 0;
            varsValues.put(name, value);
            j <<= 1;
        }
        boolean value = proofStatement.evaluate(varsValues);
        if (predicate != null && predicate != value) return null;
        return new TruthTableLine(proofStatement, varsValues, value, parser);
    }


    public void solve(String s) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        proofStatement = parser.parse(s);
        TruthTableLine truthTableLine = makeTruthTable();


        BufferedWriter writer = new BufferedWriter(new FileWriter("aba.txt"));
        if (truthTableLine==null){
            System.out.println(":(");
        }else {
            System.out.println(truthTableLine);
            writer.write(truthTableLine.toString());
        }
        writer.close();
    }
    public  void solve() throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        proofStatement = parser.parse(reader.readLine());
        TruthTableLine truthTableLine = makeTruthTable();


//        BufferedWriter writer = new BufferedWriter(new FileWriter("aba.txt"));
        if (truthTableLine==null){
            System.out.println(":(");
        }else {
            System.out.println(truthTableLine);
//            writer.write(truthTableLine.toString());
        }
//        writer.close();
    }

    public static void main(String[] args) throws IOException {
//        check();
        new Solver(new Parser()).solve();
//        solve_main(args[0]);
    }

    private static void check() throws IOException {
        Parser parser = new Parser();
        ProofSchemes schemes = new ProofSchemes(parser);

        String expression = "A|!A";
        Map<String, Boolean> map = new HashMap<>();
//        map.put("A", true);
        Proof proof = schemes.get_pr_a_or_not_a().insertInScheme(BinaryOperation.makeMap(parser.getVar("A"), parser.getVar("B")));

        for (Map.Entry<String, Boolean> b : map.entrySet()) {
            if (!b.getValue()) {
                proof.proof.addFirst(new ProofLine(new Not(parser.parse(b.getKey())), ProofType.HYPOTHESIS));
            } else {
                proof.proof.addFirst(new ProofLine(parser.parse(b.getKey()), ProofType.HYPOTHESIS));
            }
        }
        TruthTableLine truthTableLine = new TruthTableLine(parser.parse(expression), map, true, parser);
        truthTableLine.proof = proof;
        BufferedWriter writer = new BufferedWriter(new FileWriter("aba.txt"));
        writer.write(truthTableLine.toString());
        writer.close();

    }

    public static void test() {
        List<Integer> list = new LinkedList<>();
        ListIterator<Integer> itr = list.listIterator();
        for (int i = 0; i <= 5; i++) {
            itr.add(i);
        }
        itr = list.listIterator();
        for (int i = 0; i < 5; i++) {
            int a = itr.next();
            if (a % 2 == 0) {
                itr.previous();
                for (int j = 1; j <= 4; j++) {
                    itr.add(j * 100);
                }
                itr.next();
            }
        }
        itr = list.listIterator();

        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }

    private static void solve_main(String s) throws IOException {
//        ProofSchemes schemes = new ProofSchemes(new Parser());
//        Proof A_not_A = schemes.get_a_or_not_a();
//        System.out.println(A_not_A.insertInScheme(BinaryOperation.makeMap(new Var("C"))));
        new Solver(new Parser()).solve(s);
    }


}
