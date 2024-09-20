package ca.tlcp.compiler.memory;

import java.util.ArrayList;
import java.util.List;

public class MemoryManager {

    public static List<Variable> VERIABLES = new ArrayList<>();
    public static List<String> CONSTANTS = new ArrayList<>();

    public static void addVar(Variable variable) {
        if (variable != null) {
            VERIABLES.add(variable);
        } else {
            System.err.printf("Variable %s seams to be declared incorrectly. It has not been added to memory and cannot be type checked.%n", variable.getLabel());
        }
    }
}
