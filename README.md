# calculator-v2.0

This calculator printout rational number instead of real number.

Does not work with mathematical expressions:
--, *-, /-, +-

Example:
2/11+((2+1)/2+1) = 59.0/22.0 instead of 2.68...
2/3+5*(3/12-5/5) = 37.0/-12.0
2*(3/7+2/14) = 8.0/7.0

#Generating jar file and using it

1. generating class files ``javac .\src\*.java -d./out/jar``
2. creating jar file ``jar cvfm .\Calculator.jar .\META-INF\MANIFEST.MF .\out\jar\*.class``
3. start jar app ``java -jar .\Calculator.jar``