import java.util.*;
import java.util.regex.Pattern;

public class Calculator {
    private RationalNumber result;
    private String input;
    Queue<String> outputQueue = new LinkedList<>();
    Stack<Operator> operatorsStack = new Stack<>();

    public void cleanup(){
        outputQueue.clear();
        operatorsStack.clear();
    }

    public Boolean readAndParseInput(String line){
        if ((this.input = line).isEmpty())
            return false;

        return readInputAndModifyInfixToPostfix(this.removeWhiteSpace(line));
    }

    private String removeWhiteSpace(String line) {
        return line.replaceAll("\\s+", "");
    }

    public Boolean readInputAndModifyInfixToPostfix(String line){
        StringBuilder currentNumber = new StringBuilder();
        int value;
        boolean stillNumber = false;

        for (int i = 0, lineSize = line.length(); i < lineSize; i++) {
            value = line.charAt(i);

            if (Operator.isOperator(value))
                stillNumber=this.valueIsOperatorOrParentheses((char) value, stillNumber, currentNumber);
            else
                stillNumber=this.valueIsNumber(value, currentNumber);
        }

        this.addAllToOutputQueue(currentNumber);
        return true;
    }

    private boolean valueIsOperatorOrParentheses(char value, boolean isNumber, StringBuilder currentNumber) {
        if (isNumber) {
            outputQueue.add(currentNumber.toString());
            currentNumber.setLength(0);
        }

        if (!Operator.isParenthesis(value))
            this.addOperatorToStack(Operator.getTypeFromChar(value));

        if (Operator.LEFT_PARENTHESIS.isEqual(value))
            operatorsStack.add(Operator.LEFT_PARENTHESIS);

        if (Operator.RIGHT_PARENTHESIS.isEqual(value)) this.addAllUntilLeftParentheses();

        return false;
    }

    private void addAllToOutputQueue(StringBuilder currentNumber) {
        if (!currentNumber.isEmpty())
            outputQueue.add(currentNumber.toString());

        while (!operatorsStack.empty())
            outputQueue.add(String.valueOf(operatorsStack.pop().getOperator()));
    }

    private void addAllUntilLeftParentheses() {
        while (operatorsStack.peek() != Operator.LEFT_PARENTHESIS)
            outputQueue.add(String.valueOf(operatorsStack.pop().getOperator()));

        operatorsStack.pop();
    }

    private void addOperatorToStack(Operator operator) {
        while (!operatorsStack.empty() &&
                operator.hasHigherPriority(operatorsStack.peek()))
            outputQueue.add(String.valueOf(operatorsStack.pop().getOperator()));

        operatorsStack.push(operator);
    }

    private boolean valueIsNumber(int value, StringBuilder currentNumber) {
        currentNumber.append((char) value);
        return true;
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
                numberStack.add(Operator.getTypeFromChar(ch.charAt(0))
                        .operation(numberStack.pop(), numberStack.pop()));
            }
        }

        this.cleanup();
        result = numberStack.pop().simplify();
    }

    public void printResult() {
        this.cleanup();
        System.out.printf("%s = %s\n", this.input, this.result);
    }

    public void checkPattern(Scanner scanner) {
        if (scanner.findInLine(Pattern.compile(CustomPattern.INVALID_COMBINATION.getPattern())) != null)
            throw new IllegalArgumentException("//, **, -+, +*, /*, */, -*, ++ patterns cannot be accepted.");

        if (scanner.findInLine(Pattern.compile(CustomPattern.UNSUPPORTED.getPattern())) != null)
            throw new IllegalArgumentException("*-, +-, /- and -- is not supported yet.");

        if (scanner.findInLine(Pattern.compile(CustomPattern.INVALID_CHARACTER.getPattern())) != null)
            throw new IllegalArgumentException("Valid inputs are +, -, numbers, *, / and parenthesis");
    }
}
