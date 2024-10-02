package ca.tlcp;

import ca.tlcp.compiler.Compiler;
import ca.tlcp.compiler.CompilerConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        int argI = 0;
        File tyFile = null;
        File pyFile = null;
        boolean runPostCompile = false;

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
                }
                else {
                    System.err.println("No file specified after '-O'");
                    System.exit(400);
                }
            } else if (args[argI].equals("--ignore-comments")) {
                CompilerConfiguration.setIgnoreComments(true);
            } else if (args[argI].equals("--run")) {
                runPostCompile = true;
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
        }

        Compiler.compile(tyFile, pyFile);

        if (runPostCompile) {
            System.out.println("Running file...");

            // Start the process to run the Python file in unbuffered mode (-u flag)
            ProcessBuilder processBuilder = new ProcessBuilder("python3", "-u", pyFile.getAbsolutePath());
            Process process = processBuilder.start();

            // Create a thread to read and display Python program output (stdout)
            Thread outputThread = new Thread(() -> {
                try (InputStream processInput = process.getInputStream()) {
                    int readByte;
                    while ((readByte = processInput.read()) != -1) {
                        System.out.write(readByte); // Write process output (including prompts) to the parent's output
                        System.out.flush(); // Ensure it's displayed immediately
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Create a thread to handle error output (stderr)
            Thread errorThread = new Thread(() -> {
                try (InputStream processError = process.getErrorStream()) {
                    int readByte;
                    while ((readByte = processError.read()) != -1) {
                        System.err.write(readByte); // Write process error output to the parent's error output
                        System.err.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Create a thread to handle user input and send it to the Python process (stdin)
            Thread inputThread = new Thread(() -> {
                try (OutputStream processOutput = process.getOutputStream();
                     Scanner scanner = new Scanner(System.in)) {
                    while (scanner.hasNextLine()) {
                        String input = scanner.nextLine();
                        processOutput.write((input + "\n").getBytes());
                        processOutput.flush(); // Ensure input is immediately sent to the Python process
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Start the threads to handle I/O
            outputThread.start();
            errorThread.start();
            inputThread.start();

            // Wait for the process to finish
            process.waitFor();

            // Wait for the threads to finish
            outputThread.join();
            errorThread.join();
            inputThread.join();
        }
    }
}
