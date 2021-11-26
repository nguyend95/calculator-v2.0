import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> mathExpressions = Stream.of(
                        "5+ 2",
                        "4+3*3",
                        "4*2+(3+41*1)",
                        "2*11+((2+1)*2+1)",
                        "2/11+((2+1)/2+1)",
                        "(2+1)-4",
                        "2*(3/7+2/14)",
                        "2/3+5*(3/12-5/5)",
                        "2--1",
                        "2++2")
                .collect(Collectors.toList());
        Calculator calculator = new Calculator();

        mathExpressions.forEach(m -> {
            Reader inputString = new StringReader(m);
            BufferedReader reader = new BufferedReader(inputString);

            try {
                calculator.readInputAndModifyInfixToPostfix(reader);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            try {
                System.out.print(calculator.checkInput());
            } catch (RuntimeException e) {
                e.printStackTrace();
                return;
            }

            calculator.calculate();
            calculator.printResult();
        });
    }
}
