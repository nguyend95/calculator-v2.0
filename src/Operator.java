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
    };

    private final int priority;
    private final char operator;

    Operator(final int priority, char operator){
        this.priority = priority;
        this.operator = operator;
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

    public boolean hasHigherPriority(Operator other){
        return other.getPriority() - this.priority > 0;
    }

    abstract RationalNumber operation(RationalNumber firstNumber, RationalNumber secondNumber);
}
