package Challenge3;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

class Challenge3 {
    public static HashMap<String, Object> variables = new HashMap<>();
    public static boolean conditions;
    public static String[] instructions;
    public static boolean debug = false;

    public static void main(String[] args) {
        instructions = getInstructions();
        Run(0, instructions.length);
    }

    private static String[] getInstructions() {
        String fileString = "";

        try {
            fileString = new String(Files.readAllBytes(Paths.get("Challenge3/Challenge3Input")));
            if(debug) System.out.println("Running program:\n" + fileString + "\n\n");
        } catch (Exception e) {
            System.out.println("The source file cannot be found!");
        }

        return fileString.split("(\\s*;\\s*)|\n|(\\s*\\{\\s*)");
    }

    private static void Run(Integer start, Integer end) {

        for (int i = start; i < end; i++) {
            String instructionLine = instructions[i].trim();

            if (instructionLine.startsWith("//") || instructionLine.isEmpty() || instructionLine.contains("}"))
                continue;

            else if (instructionLine.contains("++")) {
                int plusIndex = instructionLine.indexOf("++");
                String variable = instructionLine.substring(0, plusIndex).trim();
                Object value = performOperation(variables.get(variable), "add", 1);
                variables.put(variable, value);
            }
            else if (instructionLine.contains("--")) {
                int minusIndex = instructionLine.indexOf("--");
                String variable = instructionLine.substring(0, minusIndex).trim();
                Object value = performOperation(variables.get(variable), "sub", 1);
                variables.put(variable, value);
            }
            else if (instructionLine.contains("+=")) operate(instructionLine, "add");
            else if (instructionLine.contains("-=")) operate(instructionLine, "sub");
            else if (instructionLine.contains("*=")) operate(instructionLine, "multiply");
            else if (instructionLine.contains("/=")) operate(instructionLine, "sub");
            else if (instructionLine.contains("while")) i = parseWhile(i, instructionLine);
            else if (instructionLine.contains("if")) i = parseIf(i, instructionLine);
            else if (instructionLine.contains("else")) i = parseElse(i, instructionLine);
            else if (instructionLine.contains("print")){
                String input = findCondition(instructionLine);
                if(!variables.containsKey(input)) System.out.println(evaluateEquation(input));
                else System.out.println(variables.get(input));
            }
            else if (instructionLine.contains("=")) {
                int equalsIndex = instructionLine.indexOf("=");
                String variable = instructionLine.substring(0, equalsIndex).trim();
                String expression = instructionLine.substring(equalsIndex + 1).trim();
                Object result = evaluateEquation(expression);

                expression = String.valueOf(result);
                addVariable(variable, expression);
            }
            else {
                System.out.println("Invalid instruction: " + instructionLine);
                System.exit(0);
            }

            if(!instructionLine.contains("else") && !instructionLine.contains("if")) conditions = false;
            if (instructionLine.contains("while") || instructionLine.contains("if") || instructionLine.contains("else")) continue;

            if(!debug) continue;
            System.out.print(instructionLine + "\t\t\t\t\t\t->\t");
            displayVariables();
            System.out.println();
        }
    }

    private static int parseWhile(int i, String instructionLine) {

        int[] boundaries = findBoundaries(instructions, i, "while");
        String[] condition = findCondition(instructionLine).split("=");

        i = boundaries[2];

        while (testCondition(condition)) {
            if(debug) System.out.println("Loop: " + instructionLine + ".");
            Run(boundaries[0], boundaries[1]);
        }

        if(debug) System.out.println("End loop: " + instructionLine + ".");
        return i;
    }
    private static int parseIf(int i, String instructionLine) {

        if(debug) System.out.println("Check: " + instructionLine + ".");
        int[] boundaries = findBoundaries(instructions, i, "if");
        String[] condition = findCondition(instructionLine).split("=");

        i = boundaries[2];

        if(!instructionLine.contains("else")) conditions = false;

        if (testCondition(condition) && !conditions) {
            if(debug) System.out.println(instructionLine + " -> true");
            Run(boundaries[0], boundaries[1]);
            if(debug) System.out.println("End: " + instructionLine + ".");
            conditions = true;
        } else {
            if(debug) System.out.println(instructionLine + " -> false");
        }

        return i;
    }

    private static int parseElse(int i, String instructionLine) {

        if(debug) System.out.println("Check: " + instructionLine + ".");
        int[] boundaries = findBoundaries(instructions, i, "else");

        i = boundaries[2];

        if (!conditions) {
            if(debug) System.out.println(instructionLine + " -> true");
            Run(boundaries[0], boundaries[1]);
            if(debug) System.out.println("End: " + instructionLine + ".");
        } else {
            if(debug) System.out.println(instructionLine + " -> false");
        }

        return i;
    }
    private static int[] findBoundaries(String[] instructions, int i, String keyWord) {
        int count = 1;
        int _start = i + 1;
        int _end = i;

        while (count > 0) {
            i++;

            if (instructions[i].contains(keyWord)) count++;
            if (instructions[i].contains("}")) count--;

            _end++;
        }

        return new int[]{_start, _end, i};
    }
    private static String findCondition(String instructionLine) {
        int conditionStart = instructionLine.indexOf("(");
        int conditionEnd = instructionLine.indexOf(")");
        return instructionLine.substring(conditionStart + 1, conditionEnd);
    }
    private static boolean testCondition(String[] condition) {
        boolean Continue = false;

        Object compareTo = evaluateEquation(condition[condition.length - 1].trim());
        if (condition[0].endsWith("!")) {
            String variable = condition[0].substring(0, condition[0].length() - 1).trim();
            Object variableObject = variables.get(variable);

            Continue = !variableObject.toString().equals(compareTo) &&
                    Float.parseFloat(variableObject.toString()) != Float.parseFloat(compareTo.toString());
        }
        else if (condition.length > 2) {
            String variable = condition[0].trim();
            Object variableObject = variables.get(variable);

            Continue = variableObject.toString().equals(compareTo) &&
                    Float.parseFloat(variableObject.toString()) == Float.parseFloat(compareTo.toString());
        }

        return Continue;
    }
    private static void addVariable(String variable, String value) {
        String type;
        String[] startInfo = variable.split(" ");

        if (startInfo.length == 1) {
            type = variables.get(variable).getClass().getSimpleName().toLowerCase();
        } else {
            type = startInfo[0];
            variable = startInfo[1];
        }

        if (type.equals("int") || type.equals("integer")) variables.put(variable, Math.round(Float.parseFloat(value.substring(0, value.length() - 2))));
        if (type.equals("float")) variables.put(variable, Float.parseFloat(value));
        if (type.equals("string")) variables.put(variable, value);
    }

    private static Object evaluateEquation(String equation){
        StringBuilder result = new StringBuilder();

        try {return Float.parseFloat(equation);}
        catch(NumberFormatException nfe1){
            Object[] keys = variables.keySet().toArray();

            String[] expressions = equation.split("(\\s*\\++\\s*\")|(\"\\s*\\++\\s*)");

            int firstString = 1;
            for (int i = 0; i < expressions.length; i++) {
                if (expressions[i].isEmpty()) continue;
                if (expressions[i].contains("\"")) {
                    firstString = i;
                    result.append(expressions[i].replace('"', '\0'));
                    continue;
                }
                if(i%2==firstString) {
                    result.append(expressions[i]);
                    continue;
                }

                Expression finalExpression = new ExpressionBuilder(expressions[i]).variables(variables.keySet()).build();

                for (Object key : keys) {
                    double value;

                    try{
                        value = Double.parseDouble(variables.get(key).toString());
                    }
                    catch (NumberFormatException nfe2){
                        continue;
                    }

                    finalExpression.setVariable((String) key, value);
                }
                result.append(finalExpression.evaluate());
            }

        }

        return result.toString();
    }
    private static Object performOperation(Object value, String operation, float changeBy) {
        String type = value.getClass().getSimpleName();

        if (type.equals("Integer")) {
            return switch (operation) {
                case "add" -> (int) ((int) value + changeBy);
                case "sub" -> (int) ((int) value - changeBy);
                case "multiply" -> (int) ((int) value * changeBy);
                case "divide" -> (int) ((int) value / changeBy);
                default -> null;
            };
        } else if (type.equals("Float")) {
            return switch (operation) {
                case "add" -> (float) value + changeBy;
                case "sub" -> (float) value - changeBy;
                case "multiply" -> (float) value * changeBy;
                case "divide" -> (float) value / changeBy;
                default -> null;
            };
        }

        return null;
    }
    private static void operate(String instructionLine, String operation) {
        int equalsIndex = instructionLine.indexOf("=");
        String variable = instructionLine.substring(0, equalsIndex - 1).trim();
        String equation = instructionLine.substring(equalsIndex + 1).trim();
        float value = (float)evaluateEquation(equation);
        Object result = performOperation(variables.get(variable), operation, value);
        variables.put(variable, result);
    }
    private static void displayVariables() {
        for (String variable : variables.keySet()) {
            System.out.print(variable + ": " + variables.get(variable) + "\t\t");
        }
    }
}