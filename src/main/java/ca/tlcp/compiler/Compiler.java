package ca.tlcp.compiler;

import ca.tlcp.compiler.memory.MemoryManager;
import ca.tlcp.compiler.memory.Variable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Compiler {

    public static void compile(File TyFile, File pyFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TyFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(pyFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Process lines from file
                String processedLine = processLine(line);
                writer.write(processedLine);
                writer.newLine();
            }
            System.out.println("Compilation complete");
            System.out.println("VARIABLES: \n " + MemoryManager.VERIABLES);
        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
        }
    }

    private static String processLine(String line) {
        //  indentation
        int leadingSpaces = countLeadingSpaces(line);
        String trimmedLine = line.trim();

        // printf conversions
        if (trimmedLine.startsWith("printf(") && trimmedLine.endsWith(")")) {
            // Remove the 'printf(' at the beginning and the closing ')' at the end
            String content = trimmedLine.substring(7, trimmedLine.length() - 1).trim();

            // Remove surrounding quotes from format string if present
            if (content.startsWith("\"") && content.endsWith("\"")) {
                content = content.substring(1, content.length() - 1);
            }

            return String.format("%sprint(f\"%s\")", " ".repeat(leadingSpaces), content);
        }
        // detect veriable setting
        if (line.contains("=")) {
            if (line.startsWith("int") || line.startsWith("float") || line.startsWith("bool") || line.startsWith("str")) {
                String label = "";
                String type = "";
                var firstSpace = line.indexOf(' ');
                type = line.substring(0, firstSpace-1);
                System.out.println(line);
                var equalsSign = line.indexOf('=');
                line = line.substring(firstSpace, equalsSign).trim();
                System.out.printf("\"%s\"%n", line);
                System.out.println("= at index " + equalsSign);
                label = line;
                Variable var = new Variable(label, type);
                MemoryManager.addVar(var);
                System.out.printf("Variable \"%s\" of type %s added successfully%n", var.getLabel(), var.getDataType());
            }
        }

        return line;
    }

    private static int countLeadingSpaces(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) == ' ') {
            count++;
        }
        return count;
    }
}
