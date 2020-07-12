import java.io.BufferedReader;
import java.util.*;

public class Parser {

    private String expression;
    private Token token;
    private int pointer;
    private String nameOfVar;

    Map<String, Var> vars;

    public enum Token {
        VAR, IMPL, NOT, OR, AND, OPEN_BRACE, CLOSE_BRACE, END, BEG;
    }

    private void skipSpaces() {
        while (pointer < expression.length() && Character.isWhitespace(expression.charAt(pointer))) {
            pointer++;
        }
    }

    private String readIndentefier() {
        int beginOfLetters = pointer;
        char charr = expression.charAt(pointer);
        while (Character.isLetter(charr) || Character.isDigit(charr) || charr == '\'') {
            if (++pointer >= expression.length()) break;
            charr = expression.charAt(pointer);
        }
        pointer--;
        return expression.substring(beginOfLetters, pointer + 1);
    }

    private void getToken() {
        skipSpaces();
        char curChar;
        if (pointer >= expression.length()) {
            token = Token.END;
        } else {
            curChar = expression.charAt(pointer);
            switch (curChar) {
                case '(':
                    token = Token.OPEN_BRACE;
                    break;

                case ')':
                    token = Token.CLOSE_BRACE;
                    break;

                case '!':
                    token = Token.NOT;
                    break;

                case '&':
                    token = Token.AND;
                    break;
                case '-':
                    token = Token.IMPL;
                    pointer++;
                    break;
                case ',':
                    token = Token.END;
                    break;
                case '|':
                    if (expression.charAt(pointer + 1) == '-') {
                        token = Token.END;
                        pointer--;
                        break;
                    }
                    token = Token.OR;
                    break;

                default:
                    if (Character.isLetter(curChar)) {
                        nameOfVar = readIndentefier();
                        token = Token.VAR;
                    }
                    break;
            }
            pointer++;
        }
    }

    private Expression getUnaryExp() {
        getToken();
        switch (token) {
            case VAR:
                getToken();
                return getVar();
            case NOT:
                return new Not(getUnaryExp());
            case OPEN_BRACE:
                Expression result = getAnyExpression();
                getToken();
                return result;
            default:
                return null;
        }

    }

    private Expression getVar() {
        return vars.computeIfAbsent(nameOfVar, Var::new);
    }

    private Expression getAnd() {
        Expression result = getUnaryExp();
        do {
            if (token == Token.AND) {
                result = new And(result, getUnaryExp());
            } else {
                return result;
            }
        } while (true);
    }


    private Expression getOr() {
        Expression result = getAnd();
        do {
            if (token == Token.OR) {
                result = new Or(result, getAnd());
            } else {
                return result;
            }
        } while (true);
    }

    private Expression getImpl() {
        Expression result = getOr();
        do {
            if (token == Token.IMPL) {
                result = new Impl(result, getImpl());
            } else {
                return result;
            }
        } while (true);
    }

    public Parser() {
        vars = new HashMap<>();
    }

    private Expression getAnyExpression() {
        Expression res = getImpl();
        skipSpaces();
        return res;
    }

    public Expression parse(String expression) {
        this.expression = expression;
        pointer = 0;
        token = Token.BEG;

        return getAnyExpression();
    }

    private Expression parsingNow() {
        token = Token.BEG;
        return getAnyExpression();
    }


    public Expression parseProofStatement() {
        return parsingNow();
    }

    public Map<Expression, Integer> parseHypothesis(String line) {
        expression = line;
        pointer = 0;
        skipSpaces();
        Map<Expression, Integer> result = new HashMap<>();
        skipSpaces();
        int counter = 1;
        if (expression.charAt(pointer) != '|') {
            while (true) {
                result.put(parsingNow(), counter++);
                if (expression.charAt(pointer) == '|') break;
//                else pointer+=1;//get rid of ','
            }
        }
        pointer += 2;
        return result;
    }
}