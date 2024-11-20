package io.github.hangga.delvelin.properties;

public class Ansi {
    public static final String YELLOW = Config.outputFormat == OutputFormat.HTML? "" : "\033[0;33m";
    public static final String RESET = Config.outputFormat == OutputFormat.HTML? "" : "\033[0m";
    public static final String GREEN = Config.outputFormat == OutputFormat.HTML? "" : "\033[0;32m";
    public static final String PURPLE = Config.outputFormat == OutputFormat.HTML? "" : "\033[0;35m";
    public static final String BLUE = Config.outputFormat == OutputFormat.HTML? "" : "\033[0;34m";
    public static final String CYAN = Config.outputFormat == OutputFormat.HTML? "" : "\033[0;36m";
    public static final String PINK = Config.outputFormat == OutputFormat.HTML? "" : "\033[38;5;206m";
    public static final String BRIGHT_PINK = Config.outputFormat == OutputFormat.HTML? "" : "\033[38;5;212m";
    public static final String RED = Config.outputFormat == OutputFormat.HTML? "" : "\033[0;31m";
}
