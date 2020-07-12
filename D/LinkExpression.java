public class LinkExpression {
    public Expression expression;
    public int number;

    public LinkExpression(Expression expression) {
        this.expression = expression;
        number = 0;
    }

    public LinkExpression(int number) {
        this.number = number;
        this.expression = null;
    }

    public void change(Expression expr){
        number = 0;
        expression = expr;
    }

}
