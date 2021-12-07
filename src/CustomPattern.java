public enum CustomPattern {
    INVALID_CHARACTER(
            "[^\\d/\\-+*()]",
            "Valid inputs are +, -, numbers, *, / and parenthesis"),
    UNSUPPORTED(
            "(\\*-\\()|(/-\\()|" +
            "(\\(-\\()|(\\(\\+\\()",
            "(-(), *-(), /-(), (+( is not supported yet."),
    INVALID_COMBINATION(
            "(\\*\\*)|(//)|" +
            "(\\+\\*)|(-\\*)|" +
            "(/\\*)|(\\*/)",
            "//, **, -+, +*, /*, */, -*, ++ patterns cannot be accepted.");

    private final String pattern;
    private final String text;

    CustomPattern(String pattern, String text) {
        this.pattern = pattern;
        this.text = text;
    }

    public String getPattern() {
        return pattern;
    }

    public String getText() {
        return text;
    }
}
