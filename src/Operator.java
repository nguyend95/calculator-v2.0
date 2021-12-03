import java.util.Arrays;

public enum Operator{
    MINUS (1, '-'){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber){
            return secondNumber.subtract(firstNumber);
        }
    },
    PLUS (1, '+'){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber){
            return firstNumber.add(secondNumber);
        }
    },
    DIVISION (2, '/'){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber) {
            return secondNumber.divide(firstNumber);
        }
    },
    MULTIPLICATION (2, '*'){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber) {
            return secondNumber.multiply(firstNumber);
        }
    },
    LEFT_PARENTHESIS (-1, '('){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber) {
            return null;
        }
    },
    RIGHT_PARENTHESIS(-1, ')'){
        @Override
        RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber) {
            return null;
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

    abstract RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber);
}
