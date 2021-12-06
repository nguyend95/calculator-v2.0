import jdk.jshell.spi.ExecutionControl;

import java.util.Arrays;
import java.util.Optional;

public enum Operator{
    MINUS (1, '-'){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber){
            return secondNumber.subtract(firstNumber);
        }

        @Override
        public Optional<Boolean> combinationWithPlusMinus(Boolean isPositive){
            return Optional.of(!isPositive);
        }

        @Override
        public boolean isParenthesis() {
            return false;
        }
    },
    PLUS (1, '+'){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber){
            return firstNumber.add(secondNumber);
        }

        @Override
        public Optional<Boolean> combinationWithPlusMinus(Boolean isPositive) {
            return Optional.of(isPositive);
        }

        @Override
        public boolean isParenthesis() {
            return false;
        }
    },
    DIVISION (2, '/'){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber) {
            return secondNumber.divide(firstNumber);
        }

        @Override
        public Optional<Boolean> combinationWithPlusMinus(Boolean isPositive) throws ExecutionControl.NotImplementedException {
            throw new ExecutionControl.NotImplementedException("Operator '/' does not support combinationWithPlusMinus's method");
        }

        @Override
        public boolean isParenthesis() {
            return false;
        }
    },
    MULTIPLICATION (2, '*'){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber) {
            return secondNumber.multiply(firstNumber);
        }

        @Override
        public Optional<Boolean> combinationWithPlusMinus(Boolean isPositive) throws ExecutionControl.NotImplementedException {
            throw new ExecutionControl.NotImplementedException("Operator '*' does not support combinationWithPlusMinus's method");
        }

        @Override
        public boolean isParenthesis() {
            return false;
        }
    },
    LEFT_PARENTHESIS (-1, '('){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber) throws ExecutionControl.NotImplementedException {
            throw new ExecutionControl.NotImplementedException("Operator '(' is not supported for operation");
        }

        @Override
        public Optional<Boolean> combinationWithPlusMinus(Boolean isPositive) throws ExecutionControl.NotImplementedException {
            throw new ExecutionControl.NotImplementedException("Operator '(' does not support combinationWithPlusMinus's method");
        }

        @Override
        public boolean isParenthesis() {
            return true;
        }
    },
    RIGHT_PARENTHESIS(-1, ')'){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber) throws ExecutionControl.NotImplementedException {
            throw new ExecutionControl.NotImplementedException("Operator ')' does not support operation's method");
        }

        @Override
        public Optional<Boolean> combinationWithPlusMinus(Boolean isPositive) throws ExecutionControl.NotImplementedException {
            throw new ExecutionControl.NotImplementedException("Operator ')' does not support combinationWithPlusMinus's method");
        }

        @Override
        public boolean isParenthesis() {
            return true;
        }
    };

    private final int priority;
    private final char operator;

    Operator(final int priority, char operator){
        this.priority = priority;
        this.operator = operator;
    }

    public static boolean isOperator(int value) {
        return Arrays.stream(Operator.values())
                .anyMatch(operator -> operator.getOperator() == value);
    }

    public int getPriority() {
        return priority;
    }

    public char getOperator() {
        return operator;
    }

    public static Operator getTypeFromChar(char value){
        return Arrays.stream(Operator.values()).filter(
                operator -> operator.getOperator() == value
        ).findAny().orElseThrow(IllegalArgumentException::new);
    }

    public static boolean isParenthesis(char value){
        return LEFT_PARENTHESIS.isEqual(value)
                || RIGHT_PARENTHESIS.isEqual(value);
    }

    public boolean hasHigherPriority(Operator other){
        return other.getPriority() - this.priority > 0;
    }

    protected boolean isEqual(char value){
        return value == this.getOperator();
    }

    abstract RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber) throws ExecutionControl.NotImplementedException;

    public abstract Optional<Boolean> combinationWithPlusMinus(Boolean isPositive) throws ExecutionControl.NotImplementedException;

    public abstract boolean isParenthesis();
}
