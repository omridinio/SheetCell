package method.expression;
import method.expression.impl.*;
import method.expression.impl.Number;

//=============================================================================================
//public class MainTest {
//    public static void main(String[] args) {
//        // Example input string
//        String input = "{MINUS,hello ,4}";
//        //String input =  "{MINUS, {PLUS,4,5},{POW,2,3}}";
//        //Expression y = ImplCell.stringToExpression(input);
//        try{
//            System.out.println(y.evaluate());
//        }catch(Exception e){
//            System.out.println("please enter valid expressions");
//            System.out.println(e.getMessage());
//        }
//        Expression e = new Minus(
//                new Plus(new Number(4), new Number(5)),
//                new Pow(new Number(2),new Number(3)));
//
//    }
//}


//================================================================================================
//public class Main {
//    public static Expression parse(String input) {
//        input = input.replaceAll("\\s+", "");
//        Stack<Expression> expressionStack = new Stack<>();
//        Stack<String> operatorsStack = new Stack<>();
//
//        for (int i = 0; i < input.length(); i++) {
//            char ch = input.charAt(i);
//
//            if (ch == '{') {
//                // Start of a new expression
//                continue;
//            } else if (ch == '}') {
//                // End of an expression
//                if (!operatorsStack.isEmpty()) {
//                    String operator = operatorsStack.pop();
//                    Expression right = expressionStack.pop();
//                    Expression left = expressionStack.pop();
//                    expressionStack.push(createExpression(operator, left, right));
//                }
//            } else if (ch == ',') {
//                // Delimiters between operands
//                continue;
//            } else if (Character.isDigit(ch)) {
//                // Read the number
//                int start = i;
//                while (i + 1 < input.length() && Character.isDigit(input.charAt(i + 1))) {
//                    i++;
//                }
//                int value = Integer.parseInt(input.substring(start, i + 1));
//                expressionStack.push(new Number(value));
//            } else if (Character.isLetter(ch)) {
//                // Read the operator
//                int start = i;
//                while (i + 1 < input.length() && Character.isLetter(input.charAt(i + 1))) {
//                    i++;
//                }
//                String operator = input.substring(start, i + 1);
//                operatorsStack.push(operator);
//            }
//        }
//
//        return expressionStack.isEmpty() ? null : expressionStack.pop();
//    }
//
//    private static Expression createExpression(String operator, Expression left, Expression right) {
//        switch (operator) {
//            case "PLUS":
//                return new Plus(left, right);
//            case "MINUS":
//                return new Minus(left, right);
//            case "POW":
//                return new Pow(left, right);
//            default:
//                throw new IllegalArgumentException("Unknown operator: " + operator);
//        }
//    }
//
//    public static void main(String[] args) {
//        String input = "{MINUS,{PLUS,4,5},{POW,2,3}}";
//        Expression e = parse(input);
//        System.out.println(e);
//    }
//}
