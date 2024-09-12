package ca.tlcp.compiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Compiler {

    public static void compile(File TythonFile, File pyFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TythonFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(pyFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Process each line from the Tython file
                String processedLine = processLine(line);
                // Write the processed line to the output file
                writer.write(processedLine);
                writer.newLine(); // Write a new line character
            }

            System.out.println("Compilation complete");

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

            // Remove surrounding quotes from the format string if present
            if (content.startsWith("\"") && content.endsWith("\"")) {
                content = content.substring(1, content.length() - 1);
            }

            // Construct the Python print statement
            return String.format("%sprint(f\"%s\")", " ".repeat(leadingSpaces), content);
        }

        // Return line unchanged if it's not a printf statement
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
