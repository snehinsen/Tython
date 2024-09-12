package ca.tlcp;

import ca.tlcp.compiler.Compiler;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        int argI = 0;
        File tyFile = null;
        File pyFile = null;

        while (argI < args.length) {
            if (args[argI].equals("-f")) {
                if (argI + 1 < args.length) {
                    tyFile = new File(args[++argI]);
                    if (!tyFile.exists()) {
                        System.err.println("File not found: " + tyFile.getAbsolutePath());
                        System.exit(404);
                    }
                } else {
                    System.err.println("No file specified after '-f'");
                    System.exit(400);
                }
            } else if (args[argI].equals("-O")) {
                if (argI + 1 < args.length) {
                    pyFile = new File(args[++argI]);
                } else {
                    System.err.println("No file specified after '-O'");
                    System.exit(400);
                }
            } else {
                System.err.println("Unknown argument: " + args[argI]);
                System.exit(400);
            }
            argI++;
        }

        if (tyFile == null) {
            System.err.println("Input file (-f) is required.");
            System.exit(400);
        }

        if (pyFile == null) {
            String inputFileName = tyFile.getName();
            String outputFileName = inputFileName.substring(0, inputFileName.lastIndexOf('.')) + ".py";
            pyFile = new File(tyFile.getParentFile(), outputFileName);
        } else {

        }
        Compiler.compile(tyFile, pyFile);
    }
}
