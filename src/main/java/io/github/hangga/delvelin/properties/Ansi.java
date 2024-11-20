package io.github.hangga.delvelin.properties;

public class Ansi {
    public static final String YELLOW = Config.outputFileFormat == OutputFileFormat.HTML? "" : "\033[0;33m";
    public static final String RESET = Config.outputFileFormat == OutputFileFormat.HTML? "" : "\033[0m";
    public static final String GREEN = Config.outputFileFormat == OutputFileFormat.HTML? "" : "\033[0;32m";
    public static final String PURPLE = Config.outputFileFormat == OutputFileFormat.HTML? "" : "\033[0;35m";
    public static final String BLUE = Config.outputFileFormat == OutputFileFormat.HTML? "" : "\033[0;34m";
    public static final String CYAN = Config.outputFileFormat == OutputFileFormat.HTML? "" : "\033[0;36m";
    public static final String PINK = Config.outputFileFormat == OutputFileFormat.HTML? "" : "\033[38;5;206m";
    public static final String BRIGHT_PINK = Config.outputFileFormat == OutputFileFormat.HTML? "" : "\033[38;5;212m";
    public static final String RED = Config.outputFileFormat == OutputFileFormat.HTML? "" : "\033[0;31m";
}
