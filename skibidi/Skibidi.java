package skibidi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

enum TokenType {

    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR, QUESTION_MARK, COLON,

    // One or two character tokens.
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    AND, CLASS, VIBECHECKFAIL, L, FUN, RIZZWALK, VIBECHECK, NIL, OR,
    YAP, RETURN, SUPER, THIS, W, BLUD, COOK, RAGEQUIT, GHOSTNEXT,
    EOF, PRAY, ONL, GOTCHU
}

class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;
    final int column;
    final int length;

    Token(TokenType type, String lexeme, Object literal, int line, int column, int length) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.column = column;
        this.length = length;
    }
    
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}

public class Skibidi {

    private static boolean hadError = false;
    private static boolean hadRuntimeError = false;
    private static final Interpreter interpreter = new Interpreter();

    static String getErrorMessage(int line, int column, String message) {
        return String.format("Syntax Error in Line %d - Column %d: %s", line, column, message);
    }

    static String getRunTimeErrorMessage(RuntimeError error) {
        return String.format(
            "[Runtime Error] at line %d - column %d: %s",
            error.token.line, error.token.column, error.getMessage()
        );
    }
    
    static void report(int line, int column,  String message){
        System.out.printf(
            "Syntax Error in Line %d - Column %d%n%s%n", line, column, message
        );
        hadError = true;
    }

    static void runtimeError(RuntimeError error) {
        System.err.printf(
            "[Runtime Error] at line %d - column %d: %s%n",
            error.token.line, error.token.column, error.getMessage()
        );
        hadRuntimeError = true;
    }

    static void report(Token token,  String message){
        if (token.type == TokenType.EOF) {
            report(token.line, token.column, " At end: " + message);
        } 
        else {
            report(token.line, token.column, " at '" + token.lexeme + "'" + "\n" + message);
        }
    }

    private static void run(String code){
        Lexer lexer = new Lexer(code);
        List <Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        try {
            List<Stmt> statements = parser.parse();
            if (hadError) return;
            // System.out.println(new AstPrinter().print(expresion));
            interpreter.interpret(statements);
        }
        catch (NullPointerException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void runFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    private static void runPrompt() throws IOException{
        InputStreamReader inp = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inp);

        interpreter.setReplMode(true);

        for (;;){
            System.out.print(">> ");
            String line = reader.readLine();
            if (line == null || line.equals("exit()")) break;
            run(line);
            hadError = false;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length  > 1){
            System.out.println("Usage: skibidi [script]");
            System.exit(64);
        } else if (args.length == 1){
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }
}
