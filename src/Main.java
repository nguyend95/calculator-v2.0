import jdk.jshell.spi.ExecutionControl;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        readMathExpression();
    }

    private static void readMathExpression(){
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        while (true){
            System.out.println("Zadejte matematicky vyraz: ");

            if (!readInputAndCheck(calculator, scanner))
                continue;
            try{
                if (!calculator.readAndParseInput(scanner.nextLine()))
                    continue;
                calculator.calculate();
            }catch (NoSuchElementException e){
                return;
            }catch (ExecutionControl.NotImplementedException e){
                System.out.println("Unexpected error " + e.getMessage());
                continue;
            }

            calculator.printResult();
        }
    }

    private static boolean readInputAndCheck(Calculator calculator, Scanner scanner) {
        try {
            calculator.checkPattern(scanner);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            calculator.cleanup();
            scanner.nextLine();
            return false;
        }

        return true;
    }
}
