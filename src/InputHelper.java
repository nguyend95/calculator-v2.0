import jdk.jshell.spi.ExecutionControl;

import java.util.Optional;

public class InputHelper {
    private StringBuilder currentNumber;
    private boolean stillNumber;
    private Optional<Boolean> isPositive;
    private Optional<Operator> previousOperator;

    public InputHelper() {
        this.currentNumber = new StringBuilder();
        this.stillNumber = false;
        this.isPositive = Optional.empty();
        this.previousOperator = Optional.empty();
    }

    public Optional<Operator> getPreviousOperator() {
        return previousOperator;
    }

    public void setPreviousOperator(Operator previousOperator) {
        this.previousOperator = Optional.of(previousOperator);
    }

    public String getCurrentNumber() {
        return this.currentNumber.toString();
    }

    public Optional<Boolean> getIsPositive() {
        return isPositive;
    }

    public void resetCurrentNumber(){
        this.currentNumber.setLength(0);
    }

    public void resetIsPositive(){
        this.isPositive = Optional.empty();
    }

    public boolean isStillNumber() {
        return stillNumber;
    }

    public void setStillNumber(boolean stillNumber) {
        this.stillNumber = stillNumber;
    }

    public void setIsPositive(Operator operator) throws ExecutionControl.NotImplementedException {
        if (this.isPositive.isPresent())
            this.isPositive = operator.combinationWithPlusMinus(this.isPositive.get());
        else
            this.isPositive = operator.combinationWithPlusMinus(true);
    }

    public Operator getRightOperator() {
        return isPositive.get() ? Operator.PLUS : Operator.MINUS;
    }

    public void currentNumberAppend(char value) {
        this.currentNumber.append(value);
    }

    public void resetPreviousOperator() {
        this.previousOperator = Optional.empty();
    }
}
