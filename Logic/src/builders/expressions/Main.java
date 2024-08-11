package builders.expressions;
import builders.expressions.impl.*;
import builders.expressions.impl.Number;
//import builders.expressions.impl.Number;

public class Main {
    public static void main(String[] args) {
        Expression e =
                new Sum(
                        new Exponent(
                                new Number(2.0), new Number(3.0)),
                        new Sum(
                                new Number(1.0), new Number(-3.0)));

        Expression s = new Str("hello");
        if (s.evaluate() instanceof String) {
            System.out.println(s);
        }
        if (e.evaluate() instanceof Double) {
            System.out.println(e + " = " + e.evaluate());
        }
    }
}
