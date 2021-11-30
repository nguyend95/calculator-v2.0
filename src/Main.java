import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        readMathExpression(scanner);
    }

    private static void readMathExpression(Scanner scanner){
        Calculator calculator = new Calculator();

        while (true){
            System.out.println("Zadejte matematicky vyraz: ");

            if (!readInputAndCheck(calculator, scanner))
                continue;

            try{
                if (!calculator.readAndParseInput(scanner.nextLine()))
                    return;
            }catch (NoSuchElementException e){
                return;
            }

            calculator.calculate();
            calculator.printResult();
        }
    }

    private static boolean readInputAndCheck(Calculator calculator, Scanner scanner) {
        try {
            calculator.checkPattern(scanner);
        }catch (IllegalArgumentException e){
            System.out.println(e);
            calculator.cleanup();
            scanner.next();
            return false;
        }

        return true;
    }
}
