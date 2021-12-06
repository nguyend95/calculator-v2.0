public class RationalNumber {
    private Double numerator;
    private Double denominator;

    public RationalNumber(Double number) {
        this.numerator = number;
        this.denominator = 1.0;
    }

    public Double getNumerator() {
        return numerator;
    }

    public Double getDenominator() {
        return denominator;
    }

    @Override
    public String toString() {
        return this.denominator != 1.0 ?
                this.numerator + "/" + this.denominator :
                this.numerator.toString();
    }

    private double findGcd(double x, double y) {
        return y == 0 ? x : findGcd(y, x % y);
    }

    private double findLcm(double x, double y) {
        return (x / findGcd(x, y)) * y;
    }

    public RationalNumber divide(RationalNumber other){
        this.numerator = this.numerator * other.getDenominator();
        this.denominator = this.denominator * other.getNumerator();
        this.simplify();

        return this;
    }

    public RationalNumber add(RationalNumber other){
        double lcm = findLcm(other.getDenominator(), this.denominator);
        this.numerator = lcm / this.denominator * this.numerator +
                         lcm / other.getDenominator() * other.getNumerator();
        this.denominator = lcm;
        this.simplify();

        return this;
    }

    public RationalNumber subtract(RationalNumber other){
        double lcm = findLcm(other.getDenominator(), this.denominator);
        this.numerator = lcm / this.denominator * this.numerator -
                lcm / other.getDenominator() * other.getNumerator();
        this.denominator = lcm;

        return this;
    }

    public RationalNumber multiply(RationalNumber other){
        this.numerator = this.numerator * other.getNumerator();
        this.denominator = this.denominator * other.getDenominator();
        this.simplify();

        return this;
    }

    public RationalNumber simplify(){
        double lcm = findGcd(this.denominator, this.numerator);

        this.denominator /= lcm;
        this.numerator /= lcm;

        if (this.denominator < 0){
            this.numerator *= -1.0;
            this.denominator *= -1.0;
        }

        return this;
    }
}
