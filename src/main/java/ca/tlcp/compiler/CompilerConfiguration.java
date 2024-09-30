package ca.tlcp.compiler;


public class CompilerConfiguration {

    private static boolean ignoreComments = false;

    public static boolean willIgnoreComments() {
        return ignoreComments;
    }

    public static void setIgnoreComments(boolean ignoreComments) {
        CompilerConfiguration.ignoreComments = ignoreComments;
    }
}
