public enum CustomPattern {
    INVALID_CHARACTER(
            "[^\\d/\\-+*()]"
    ),
    UNSUPPORTED(
            "(\\*-)|(/-)|" +
            "(\\(-\\()|(\\(\\+\\()"
    ),
    INVALID_COMBINATION(
            "(\\*\\*)|(//)|" +
            "(\\+\\*)|(-\\*)|" +
            "(/\\*)|(\\*/)"
    );

    private final String pattern;

    CustomPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
