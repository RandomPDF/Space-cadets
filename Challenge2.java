import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

class Challenge2 {

    public static void main(String[] args) throws IOException {
        String fileString = "";

        try {
            fileString = new String(Files.readAllBytes(Paths.get("Challenge2Input")));
            System.out.println("Running program:\n" + fileString + "\n\n");
        } catch (Exception e) {
            System.out.println("The source file cannot be found!");
        }

        ArrayList<String> instructions = new ArrayList<String>();
        String instruction = "";
        for (int i = 0; i < fileString.length(); i++) {
            char character = fileString.charAt(i);

            if (character == ';') {
                instructions.add(instruction.trim());
                instruction = "";
                continue;
            }

            instruction += character;
        }

        HashMap<String, Integer> variables = new HashMap<String, Integer>();
        Run(instructions, variables, new Stack<Jump>(), 0);
    }

    private static void Run(ArrayList<String> instructions, HashMap<String, Integer> variables,
            Stack<Jump> jumps, Integer start) {

        for (int i = start; i < instructions.size(); i++) {
            String instructionLine = instructions.get(i);
            String[] parts = instructionLine.split(" ");

            if (!jumps.empty() && jumps.peek().condition[0] != null && instructionLine.equals("end")) {
                String[] condition = jumps.peek().condition;
                if (condition[1].equals("not")) {

                    if (variables.get(condition[0]) != Integer.parseInt(condition[2])) {
                        System.out.println(condition[0] + " != " + condition[2] + " so continue the loop.");
                        i = jumps.peek().jumpTo;
                        instructionLine = instructions.get(i);
                        parts = instructionLine.split(" ");

                    } else {
                        jumps.pop();
                    }

                }
            }

            if (parts[0].equals("clear")) {
                variables.put(parts[1], 0);

            } else if (parts[0].equals("incr")) {
                variables.put(parts[1], variables.get(parts[1]) + 1);

            } else if (parts[0].equals("decr")) {
                variables.put(parts[1], variables.get(parts[1]) - 1);

            } else if (parts[0].equals("while")) {
                System.out.println("Start loop: " + instructionLine + ".");

                jumps.push(new Jump(i + 1, new String[] { parts[1], parts[2], parts[3] }));
                Run(instructions, variables, jumps, i + 1);

            } else if (!parts[0].equals("end")) {
                System.out.println("The command: " + instructionLine + " isn't a valid command!");
                System.exit(0);
            }

            System.out.print(instructionLine + "\t\t");
            DisplayVariables(variables);
            System.out.println();

            if (jumps.size() == 0 && i == instructions.size() - 1) {
                System.exit(0);
            }
        }
    }

    private static void DisplayVariables(HashMap<String, Integer> variables) {
        for (String variable : variables.keySet()) {
            System.out.print(variable + ": " + variables.get(variable) + "\t");
        }
    }
}