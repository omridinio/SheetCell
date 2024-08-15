package expression;

import expression.api.Expression;
import expression.impl.*;
import expression.impl.Number;
import expression.impl.numeric.Minus;
import expression.impl.numeric.Plus;
import expression.impl.numeric.Pow;


public class testMain {
        public static void main(String[] args) {
            Expression e =
                    new Plus(
                            new Pow(
                                    new Number(2.0), new Number(3.0)),
                            new Plus(
                                    new Number(1.0), new Number(-3.0)));

            Expression s = new Str("hello");
            if (s.evaluate() instanceof String) {
                System.out.println(s);
            }
            if (e.evaluate() instanceof Double) {
                System.out.println(e + " = " + e.evaluate());
            }
            Expression t = new Minus(new Number("4"),new Number("1"));
        }
}
