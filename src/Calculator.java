import jdk.jshell.spi.ExecutionControl;

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

    public Boolean readAndParseInput(String line) throws ExecutionControl.NotImplementedException {
        if ((this.input = line).isEmpty())
            return false;

        return readInputAndModifyInfixToPostfix(this.removeWhiteSpace(line));
    }

    private String removeWhiteSpace(String line) {
        return line.replaceAll("\\s+", "");
    }

    public Boolean readInputAndModifyInfixToPostfix(String line) throws ExecutionControl.NotImplementedException {
        InputHelper inputHelper = new InputHelper();
        int value;

        for (int i = 0, lineSize = line.length(); i < lineSize; i++) {
            value = line.charAt(i);

            if (Operator.isOperator(value))
                this.valueIsOperatorOrParentheses((char) value, inputHelper);
            else
                this.valueIsNumber(value, inputHelper);
        }

        this.addAllToOutputQueue(inputHelper);
        return true;
    }

    private void valueIsOperatorOrParentheses(char value, InputHelper inputHelper) throws ExecutionControl.NotImplementedException {
        if (inputHelper.isStillNumber()) {
            this.addNumberToQueue(inputHelper);
            inputHelper.setStillNumber(false);
        }

        if (!Operator.isParenthesis(value)) {
            this.addOperatorToStack(Operator.getTypeFromChar(value), inputHelper);
            return;
        }

        if (Operator.LEFT_PARENTHESIS.isEqual(value))
            this.addLeftParenthesisToStack(inputHelper);

        if (Operator.RIGHT_PARENTHESIS.isEqual(value)) this.addAllUntilLeftParentheses();

        inputHelper.setPreviousOperator(Operator.getTypeFromChar(value));
    }

    private void addLeftParenthesisToStack(InputHelper inputHelper) {
        if (inputHelper.getIsPositive().isPresent()) {
            operatorsStack.push(inputHelper.getRightOperator());
            inputHelper.resetIsPositive();
        }

        operatorsStack.push(Operator.LEFT_PARENTHESIS);
    }

    private void addNumberToQueue(InputHelper inputHelper) {
        outputQueue.add(inputHelper.getCurrentNumber());
        inputHelper.resetCurrentNumber();
    }

    private void addAllToOutputQueue(InputHelper inputHelper) {
        if (!inputHelper.getCurrentNumber().isEmpty())
            outputQueue.add(inputHelper.getCurrentNumber());

        while (!operatorsStack.empty())
            outputQueue.add(String.valueOf(operatorsStack.pop().getOperator()));
    }

    private void addAllUntilLeftParentheses() {
        while (operatorsStack.peek() != Operator.LEFT_PARENTHESIS)
            outputQueue.add(String.valueOf(operatorsStack.pop().getOperator()));

        operatorsStack.pop();
    }

    private void addOperatorToStack(Operator operator, InputHelper inputHelper) throws ExecutionControl.NotImplementedException {
        if (Operator.PLUS == operator || Operator.MINUS == operator){
            inputHelper.setIsPositive(operator);
            return;
        }

        inputHelper.setPreviousOperator(operator);
        this.addOperatorWithHigherPriorityToStack(operator);
    }

    private void addOperatorWithHigherPriorityToStack(Operator operator) {
        while (!operatorsStack.empty() &&
                operator.hasHigherPriority(operatorsStack.peek()))
            outputQueue.add(String.valueOf(operatorsStack.pop().getOperator()));
        operatorsStack.push(operator);
    }

    private void valueIsNumber(int value, InputHelper inputHelper) {
        this.addSignToNumber(inputHelper);
        inputHelper.currentNumberAppend((char) value);
        inputHelper.setStillNumber(true);
        inputHelper.resetPreviousOperator();
    }

    private void addSignToNumber(InputHelper inputHelper) {
        if (inputHelper.getIsPositive().isPresent()){
            inputHelper.currentNumberAppend(inputHelper.getRightOperator().getOperator());
            inputHelper.resetIsPositive();
            this.addOperatorToStack(inputHelper);
        }
    }

    private void addOperatorToStack(InputHelper inputHelper) {
        if (inputHelper.getPreviousOperator().isEmpty())
            this.addOperatorWithHigherPriorityToStack(Operator.PLUS);
        else if (!inputHelper.getPreviousOperator().get().isParenthesis())
            this.addOperatorWithHigherPriorityToStack(inputHelper.getPreviousOperator().get());
    }

    @Override
    public String toString() {
        return String.format("%s = %s\n", this.input, this.result.toString());
    }

    public void calculate() throws ExecutionControl.NotImplementedException {
        Stack<RationalNumber> numberStack = new Stack<>();

        while (!this.outputQueue.isEmpty()){
            String ch = outputQueue.remove();
            try{
                numberStack.add(new RationalNumber(Double.valueOf(ch)));
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
            throw new IllegalArgumentException(CustomPattern.INVALID_COMBINATION.getText());

        if (scanner.findInLine(Pattern.compile(CustomPattern.UNSUPPORTED.getPattern())) != null)
            throw new IllegalArgumentException(CustomPattern.UNSUPPORTED.getText());

        if (scanner.findInLine(Pattern.compile(CustomPattern.INVALID_CHARACTER.getPattern())) != null)
            throw new IllegalArgumentException(CustomPattern.INVALID_CHARACTER.getText());
    }
}
