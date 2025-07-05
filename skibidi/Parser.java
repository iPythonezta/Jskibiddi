package skibidi;
import java.util.List;
/*
 Lang Grammar:
    skib     → conditional ;
    conditional -> comma ( "?" conditional ":" conditional )? ;
    comma   → equality ( "," equality )* ;
    equality       → comparison ( ( "!=" | "==" ) comparison )* ;
    comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
    term           → factor ( ( "-" | "+" ) factor )* ;
    factor         → unary ( ( "/" | "*" ) unary )* ;
    unary          → ( "!" | "-" ) unary
                | primary ;
    primary        → NUMBER | STRING | "true" | "false" | "nil"
                | "(" expression ")" ;
*/

public class Parser {
    private static class ParseError extends RuntimeException {}
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Skib parse(){
        try {
            return expression();
        }
        catch (ParseError error){
            return null;
        }
    }
    
    private Skib expression(){
        return conditional();
    }

    private Skib conditional (){
        Skib skib = comma();
        if (match(TokenType.QUESTION_MARK)) {
            Skib thenBranch = conditional();
            consume(TokenType.COLON, "Expect ':' after '?' in conditional expression.");
            Skib elseBranch = conditional();
            return new Skib.ConditionalSkib(skib, thenBranch, elseBranch);
        }
        return skib;
    }

    private Skib comma(){
        Skib skib = equality();
        while (match(TokenType.COMMA)) {
            Token operator = previous();
            Skib right = equality();
            skib = new Skib.DuoSkib(skib, operator, right);
        }
        return skib;
    }


    private Skib equality(){
        Skib skib = comparison();
        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            Skib right = comparison();
            skib = new Skib.DuoSkib(skib, operator, right);
        }
        return skib;
    }

    private Skib comparison(){
        Skib skib = term();
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            Skib right = term();
            skib = new Skib.DuoSkib(skib, operator, right);
        }
        return skib;
    }

    private Skib term(){
        Skib skib = factor();
        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Skib right = factor();
            skib = new Skib.DuoSkib(skib, operator, right);
        }
        return skib;
    }

    private Skib factor(){
        Skib skib = unary();
        while (match(TokenType.SLASH, TokenType.STAR)){
            Token operator = previous();
            Skib right = unary();
            skib = new Skib.DuoSkib(skib, operator, right);
        }
        return skib;
    }

    private Skib unary(){
        if (match(TokenType.BANG, TokenType.MINUS)){
            Token operator = previous();
            Skib right = unary();
            return new Skib.MonoSkib(operator, right);
        }
        return primary();
    }

    private Skib primary(){

        if (match(TokenType.FALSE)) {
            return new Skib.literalSkib(false);
        }
        else if (match(TokenType.TRUE)) {
            return new Skib.literalSkib(true);
        }
        else if (match(TokenType.NIL)) {
            return new Skib.literalSkib(null);
        }
        else if (match(TokenType.IDENTIFIER)) {
            return new Skib.literalSkib(previous().lexeme);
        }
        else if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new Skib.literalSkib(previous().literal);
        }
        else if (match(TokenType.LEFT_PAREN)) {
            Skib skib = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Skib.NestSkib(skib);
        }
        throw error(peek(), "Expected an Expression");
        
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type){
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }
    
    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }
    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        if (current != 0) Skibidi.report(token, message);
        return new ParseError();
    }
    
    private void synchronize() {
        advance();

        while (!isAtEnd()) {
        if (previous().type == TokenType.SEMICOLON) return;

        switch (peek().type) {
            case CLASS:
            case FUN:
            case VAR:
            case FOR:
            case IF:
            case WHILE:
            case PRINT:
            case RETURN:
            return;
        }

        advance();
        }
  }
}
