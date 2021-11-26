import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Calculator {
    private RationalNumber result;
    private String input;
    Queue<String> outputQueue = new LinkedList<>();

    private void cleanup(){
        outputQueue = new LinkedList<>();
    }

    private Boolean hasHigherPriority(char firstOperator,char secondOperator){
        return priority(firstOperator) - priority(secondOperator) > 0;
    }

    private int priority(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '/', '*' -> 2;
            default -> -1;
        };
    }

    public void readInputAndModifyInfixToPostfix(BufferedReader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        Stack<Character> operatorsStack = new Stack<>();
        StringBuilder currentNumber = new StringBuilder();
        int value;
        boolean isNumber = false;

        while ((value = reader.read()) != -1) {
            if (Character.isWhitespace(value)){
                continue;
            }

            if (!checkValue((char) value)){
                throw new IllegalArgumentException("This ->" + value + "<- cannot be accepted.");
            }

            if (value >= '0' && value <='9'){
                currentNumber.append((char) value);
                isNumber = true;
            } else if (isNumber) {
                outputQueue.add(currentNumber.toString());
                currentNumber = new StringBuilder();
                isNumber = false;
            }

            if (isOperator(value)){
                while (!operatorsStack.empty() &&
                        hasHigherPriority(operatorsStack.peek(), (char) value)){

                    outputQueue.add(String.valueOf(operatorsStack.pop()));
                }

                operatorsStack.push((char) value);
            }

            if (value == '('){
                operatorsStack.add((char) value);
            }

            if (value == ')'){
                while (operatorsStack.peek() != '('){
                    outputQueue.add(String.valueOf(operatorsStack.pop()));
                }
                operatorsStack.pop();
            }

            content.append((char) value);
        }

        if (!currentNumber.isEmpty()){
            outputQueue.add(currentNumber.toString());
        }

        while (!operatorsStack.empty()){
            outputQueue.add(String.valueOf(operatorsStack.pop()));
        }

        this.input = content.toString();
    }

    private boolean isOperator(int value) {
        return value == '/' || value == '*' || value == '+' || value == '-';
    }

    private boolean checkValue(char value) {
        return value == '/' || value == '*' || value == '('
                || value == ')' || value == '+' || value == '-'
                || (value >= '0' && value <= '9');
    }

    @Override
    public String toString() {
        return String.format("%s = %s\n", this.input, this.result.toString());
    }

    public void calculate() {
        Stack<RationalNumber> numberStack = new Stack<>();
        while (!this.outputQueue.isEmpty()){
            String ch = outputQueue.remove();
            try{
                Double number = Double.valueOf(ch);
                numberStack.add(new RationalNumber(number));
            } catch (NumberFormatException e){
                numberStack.add(evaluate(ch, numberStack.pop(), numberStack.pop()));
            }
        }

        this.cleanup();
        result = numberStack.pop().simplify();
    }

    private RationalNumber evaluate(String operator, RationalNumber firstNumber, RationalNumber secondNumber) {
        return switch (operator){
            case "*" -> firstNumber.multiply(secondNumber);
            case "/" -> secondNumber.divide(firstNumber);
            case "+" -> firstNumber.add(secondNumber);
            case "-" -> secondNumber.subtract(firstNumber);
            default -> throw new IllegalArgumentException("This " + operator + " cannot be used.");
        };
    }

    public void printResult() {
        this.cleanup();
        System.out.printf("%s = %s\n", this.input, this.result);
    }

    public String checkInput() throws RuntimeException {
        if (this.input.matches(".*\\*\\*.*") ||
                this.input.matches(".*//.*") ||
                this.input.matches(".*-\\+.*") ||
                this.input.matches(".*\\+\\*.*") ||
                this.input.matches(".*/\\*.*") ||
                this.input.matches(".*\\*/.*") ||
                this.input.matches(".*-\\*.*") ||
                this.input.matches(".*\\+\\+.*")){
            throw new RuntimeException("//, **, -+, +*, /*, */, -*, ++ patterns cannot be accepted." +
                    " Provide input: " + input);
        }

        if (this.input.matches(".*\\*-.*") ||
                this.input.matches(".*\\+-.*") ||
                this.input.matches(".*--.*") ||
                this.input.matches(".*/-.*")){
            throw new RuntimeException("*-, +-, /- and -- is not supported yet." +
                    " Provide input: " + input);
        }

        return "";
    }
}
