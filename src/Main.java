//import com.hgroupeight.interpreter.ast.ProgramNode;
//import com.hgroupeight.interpreter.interpreter.Interpreter;

import com.hgroupeight.interpreter.ast.ProgramNode;
import com.hgroupeight.interpreter.interpreter.Interpreter;
import com.hgroupeight.interpreter.lexer.Lexer;
import com.hgroupeight.interpreter.parser.Parser;
import com.hgroupeight.interpreter.symboltable.SymbolTable;

import java.text.ParseException;

// THIS MAIN CODE BUTANGAN NEWLINE2
public class Main {

    public static void main(String[] args) throws Exception {
        // 1. put CODE here
        //        String code =
        //                """
        //                BEGIN CODE
        //                # comment only
        //
        //                END CODE
        //                        """;
        String code =
                """
                BEGIN CODE
                INT a;
                a = 4;
                #comment only
                DISPLAY a;


       
                
                
                END CODE
                """;


        // sample code to plug in for test huhu
        //                INT x, y, z=5
        //                CHAR a_1=’n’
        //                BOOL t=”TRUE”
        //                x=y=4
        //                a_1=’c’
        //                # this is a comment
        //                DISPLAY: x & t & z & $ & a_1 & [#] & “last”


        // 2. lexer, parser, symbol table, and interpreter
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);
        SymbolTable symbolTable = new SymbolTable();
        Interpreter interpreter = new Interpreter();

        // parse program
        try {
            ProgramNode program = parser.parse();
            System.out.println("PROGRAM NODE" + program);
            // 4. AST
            interpreter.interpret(program, symbolTable);
//            System.out.println("Symbol Table " + symbolTable);
        } catch (ParseException e) {
            System.err.println("Parsing error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
//SCANNER TYPE NGA READER/INTERPRETER PLSPLSPLS DONT REMOVE OR MODIFY
//
//import java.util.Scanner;
//
//public class Main {
//
//    public static void main(String[] args) throws Exception {
//        Scanner scan = new Scanner(System.in);
//
//        while (true) {
//            System.out.println("Enter code:");
//            String code = scan.nextLine();
//            Lexer lexer = new Lexer(code);
//            lexer.lex();
//        }
//    }
//}
//import com.hgroupeight.interpreter.lexer.Lexer;
//
//import com.hgroupeight.interpreter.interpreter.Interpreter;
//import com.hgroupeight.interpreter.lexer.Lexer;
//import com.hgroupeight.interpreter.parser.Parser;
//import com.hgroupeight.interpreter.symboltable.SymbolTable;
//
//import java.text.ParseException;
//import java.util.Scanner;

//public class Main {
//
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        Interpreter interpreter = new Interpreter();
//
//        while (true) {
//            System.out.println("Enter CODE program (type 'exit' to quit):");
//            StringBuilder codeBuilder = new StringBuilder();
//
//            while (true) {
//                String line = scanner.nextLine().trim();
//                if (line.equalsIgnoreCase("END CODE")) {
//                    break;
//                }
//                codeBuilder.append(line).append("\n");
//            }
//
//            String code = codeBuilder.toString();
//
//            if (code.equalsIgnoreCase("exit")) {
//                System.out.println("Exiting...");
//                break;
//            }
//
//            try {
//                Lexer lexer = new Lexer(code);
//                Parser parser = new Parser(lexer);
//                SymbolTable symbolTable = new SymbolTable();
//                interpreter.interpret(parser.parse(), symbolTable);
//            } catch (ParseException e) {
//                System.err.println("Parsing error: " + e.getMessage());
//            } catch (Exception e) {
//                System.err.println("Unexpected error: " + e.getMessage());
//            }
//        }
//    }
//}
