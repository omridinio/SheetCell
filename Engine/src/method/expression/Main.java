package method.expression;
import method.expression.impl.*;
import method.expression.impl.Number;
//import builders.expressions.impl.Number;

//public class Main {
//    public static void main(String[] args) {
//        Expression e =
//                new Sum(
//                        new Exponent(
//                                new Number(2.0), new Number(3.0)),
//                        new Sum(
//                                new Number(1.0), new Number(-3.0)));
//
//        Expression s = new Str("hello");
//        if (s.evaluate() instanceof String) {
//            System.out.println(s);
//        }
//        if (e.evaluate() instanceof Double) {
//            System.out.println(e + " = " + e.evaluate());
//        }
//    }
//
//}
//public class Main {
//    public static void main(String[] args) {
//        // Example input string
//        String input = "{MINUS,{PLUS,4,5},{POW,2,3}} ";
//        //String input = "{PLUS,4,5}";
//        Expression e = new Minus(
//                new Plus(new Number(4), new Number(5)),
//                new Pow(new Number(2),new Number(3))
//                                                );
//        // Split the string by commas
//        String[] items = input.split(",");
//
//        // Iterate through the array and print each item
//        for (String item : items) {
//            System.out.println(item);
//        }
//    }
//}
import java.util.Stack;

public class Main {
    public static Expression parse(String input) {
        input = input.replaceAll("\\s+", "");
        Stack<Expression> stack = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == '{') {
                // Start of a new expression
                continue;
            } else if (ch == '}') {
                // End of an expression
                if (!operators.isEmpty()) {
                    String operator = operators.pop();
                    Expression right = stack.pop();
                    Expression left = stack.pop();
                    stack.push(createExpression(operator, left, right));
                }
            } else if (ch == ',') {
                // Delimiters between operands
                continue;
            } else if (Character.isDigit(ch)) {
                // Read the number
                int start = i;
                while (i + 1 < input.length() && Character.isDigit(input.charAt(i + 1))) {
                    i++;
                }
                int value = Integer.parseInt(input.substring(start, i + 1));
                stack.push(new Number(value));
            } else if (Character.isLetter(ch)) {
                // Read the operator
                int start = i;
                while (i + 1 < input.length() && Character.isLetter(input.charAt(i + 1))) {
                    i++;
                }
                String operator = input.substring(start, i + 1);
                operators.push(operator);
            }
        }

        return stack.isEmpty() ? null : stack.pop();
    }

    private static Expression createExpression(String operator, Expression left, Expression right) {
        switch (operator) {
            case "PLUS":
                return new Plus(left, right);
            case "MINUS":
                return new Minus(left, right);
            case "POW":
                return new Pow(left, right);
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    public static void main(String[] args) {
        String input = "{MINUS,{PLUS,4,5},{POW,2,3}}";
        Expression e = parse(input);
        System.out.println(e);
    }
}
