package skibidi;
import java.util.ArrayList;
import java.util.List;
/*
 Lang Grammar:
    program      → bludStmt* EOF ;
    bludStmt    → statement | bludDeclr | sauceDeclr ;
    sauceDeclr → "sauce" sauce ;
    sauce      → IDENTIFIER "(" parameters? ")" statement ;
    parameters  → IDENTIFIER ( "," IDENTIFIER )* ;
    bludDeclr   → "blud" IDENTIFIER ( "=" expression )? ";" ;
    statement  →  skibStatmnt | yapStatmnt | ragequitStatmnt | block | vibeCheckStmt | CookStmt | RizzWalkStmt | GhostNextStmt | LHandlerStmt | YeetStmt ;
    yeetStmt   → "yeet" skib? ";" ;
    LHandlerStmt → "pray"  statement  "onL" "(" IDENTIFIER ")" statement ("gotchu" statement)? ;
    CookStmt   → "cook" "(" skib ")" statement ;
    RizzWalkStmt → "RizzWalk" "(" (bludDeclr | skibStatmnt | ";") skib ? ";" skib? ")" statement ;
    vibeCheckStmt → "vibecheck" "(" skib ")" statement ("vibecheckfail" satatement)? ";" ;
    block      → "{" (statement)* "}" ;
    ragequitStatmnt → "ragequit" ";" ;
    GhostNextStmt → "ghostnext" ";" ;
    skibStatmnt  → skib ";" ;
    printStatmnt → "yap" skib ";" ;
    skib     → assignment ;
    assignment     → IDENTIFIER "=" assignment | conditional ;
    conditional -> logic_or ( "?" conditional ":" conditional )? ;
    logic_or      → logic_and ( "or" logic_and )* ;
    logic_and     → equality ( "and" equality )* ;
    equality       → comparison ( ( "!=" | "==" ) comparison )* ;
    comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
    term           → factor ( ( "-" | "+" ) factor )* ;
    factor         → unary ( ( "/" | "*" ) unary )* ;
    unary          → ( "!" | "-" ) unary | call;
    call          → primary ( "(" arguments? ")" )* ;
    arguments      → skib ( "," skib )* ;
    primary        → NUMBER | STRING | "true" | "false" | "nil"
                | "(" expression ")" | IDENTIFIER ;
*/

public class Parser {
    private static class ParseError extends RuntimeException {}
    private final List<Token> tokens;
    private int current = 0;
    private int loopDepth = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse(){
        List <Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(bludStmt());
        }
        return statements;
    }

    private Stmt bludStmt(){
        try {
            if (match(TokenType.BLUD)) return bludDeclr();
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt bludDeclr(){
        Token name = consume(TokenType.IDENTIFIER, "Expected identifier after 'blud'.");
        Skib initializer = null;
        if (match(TokenType.EQUAL)) {
            initializer = expression();
        }
        consume(TokenType.SEMICOLON, "Expected ';' after blud declaration.");
        return new Stmt.BludDeclr(name, initializer);
    }

    private Stmt statement() {
        if (match(TokenType.YAP)) {
            return yappingStmt();
        }
        if (match(TokenType.LEFT_BRACE)) {
            return blockStmt();
        }
        if (match(TokenType.VIBECHECK)) {
            return vibeCheckStmt();
        }
        if (match(TokenType.COOK)) {
            return cookStmt();
        }
        if (match(TokenType.RIZZWALK)) {
            return rizzWalkStmt();
        }
        if (match(TokenType.RAGEQUIT)) {
            return rageQuitStmt();
        }
        if (match(TokenType.GHOSTNEXT)) {
            return ghostNextStmt();
        }
        if (match(TokenType.PRAY)){
            return lHandlerStmt();
        }
        if (match(TokenType.SAUCE)){
            return sauceDeclr();
        }
        if (match(TokenType.YEET)) {
            return yeetStmt();
        }
        return skibStatement();
    }

    private Stmt yeetStmt(){
        Token keyword = previous();
        Skib skib = null;
        if (!check(TokenType.SEMICOLON)) {
            skib = expression();
        }
        consume(TokenType.SEMICOLON, "Expect ';' after 'yeet' statement.");
        return new Stmt.YeetStmt(keyword, skib);
    }

    private Stmt sauceDeclr(){
        Token name = consume(TokenType.IDENTIFIER, "Expect identifier after 'sauce'.");
        consume(TokenType.LEFT_PAREN, "Expect '(' after sauce identifier.");
        List<Token> parameters = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                parameters.add(consume(TokenType.IDENTIFIER, "Expect identifier in sauce parameters."));
            } while (match(TokenType.COMMA));
        }
        consume(TokenType.RIGHT_PAREN, "Expect ')' after sauce parameters.");
        Stmt body = statement();
        return new Stmt.SauceDeclr(name, parameters, body);
    }

    private Stmt lHandlerStmt (){
        Stmt body = statement();
        consume(TokenType.ONL, "You must handle any Ls with 'onL'.");
        consume(TokenType .LEFT_PAREN, "Expect '(' after 'onL'.");
        Token identifier = consume(TokenType.IDENTIFIER, "Expect identifier after 'onL'.");
        consume(TokenType.RIGHT_PAREN, "Expect ')' after identifier in 'onL'.");
        Stmt onlBody = statement();
        Stmt gotchuBody = null;
        if (match(TokenType.GOTCHU)) {
            gotchuBody = statement();
        }
        return new Stmt.LHandlerStmt(body, onlBody, gotchuBody, new Stmt.BludDeclr(identifier, null));
        
    }

    private Stmt ghostNextStmt() {
       if (loopDepth == 0) {
            throw error(peek(), "You can only use 'ghostnext' inside a rizzwalk or cook statement.");
       }
       consume(TokenType.SEMICOLON, "Expect ';' after 'ghostnext'.");
       return new Stmt.GhostNextStmt();
    }

    private Stmt rageQuitStmt() {
        if (loopDepth == 0) {
            throw error(peek(), "You can only use 'ragequit' inside a rizzwalk or cook statement.");
        }
        consume(TokenType.SEMICOLON, "Expect ';' after 'ragequit'.");
        return new Stmt.RageQuitStmt();
    }

    private Stmt rizzWalkStmt() {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'rizzwalk'.");
        Stmt initializer = null;
        if (match(TokenType.SEMICOLON)) {
            initializer = null;
        }
        else if (match(TokenType.BLUD)) {
            initializer = bludDeclr();
        }
        else {  
            initializer = skibStatement();
        }
        Skib condition = null;
        if (!check(TokenType.SEMICOLON)) {
            condition = expression();
        }
        consume(TokenType.SEMICOLON, "Expect ';' after rizzwalk condition.");
        Skib increment = null;
        if (!check(TokenType.RIGHT_PAREN)) {
            increment = expression();
        }
        consume(TokenType.RIGHT_PAREN, "Expect ')' after rizzwalk condition.");
        loopDepth++;
        Stmt body = statement();
        if (condition == null) {
            condition = new Skib.literalSkib(true);
        }

        
        loopDepth--;
        return new Stmt.RizzWalkStmt(initializer, condition, increment, body);
    }

    private Stmt cookStmt() {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'cook'.");
        Skib condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expect ')' after condition.");
        loopDepth++;
        Stmt body = statement();
        loopDepth--;
        return new Stmt.CookStmt(condition, body);
    }

    private Stmt blockStmt() {
        return new Stmt.BlockStmt(block());
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(bludStmt());
        }
        consume(TokenType.RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

    private Stmt yappingStmt() {
        Skib skib = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after value.");
        return new Stmt.YapStmt(skib);
    }

    private Stmt skibStatement() {
        Skib skib = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new Stmt.SkibStmt(skib);
    }

    private Stmt vibeCheckStmt() {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'vibecheck'.");
        Skib skib = logicOr();
        consume(TokenType.RIGHT_PAREN, "Expect ')' after vibecheck expression.");
        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(TokenType.VIBECHECKFAIL)) {
            elseBranch = statement();
        }
        // consume(TokenType.SEMICOLON, "Expect ';' after vibecheck statement.");
        return new Stmt.VibeCheckStmt(skib, thenBranch, elseBranch);
    }
    
    private Skib expression(){
        return assignment();
    }

    private Skib assignment() {
        Skib skib = conditional();
        if (match(TokenType.EQUAL)) {
            Token equals = previous();
            Skib value = assignment();
            if (skib instanceof Skib.bludSkib) {
                Token name = ((Skib.bludSkib) skib).name;
                return new Skib.AssignBludSkib(name, value);
            }
            throw error(equals, "Invalid assignment target.");
        }
        return skib;
    }

    private Skib conditional (){
        Skib skib = logicOr();
        if (match(TokenType.QUESTION_MARK)) {
            Skib thenBranch = conditional();
            consume(TokenType.COLON, "Expect ':' after '?' in conditional expression.");
            Skib elseBranch = conditional();
            return new Skib.ConditionalSkib(skib, thenBranch, elseBranch);
        }
        return skib;
    }

    private Skib logicOr() {
        Skib skib = logicAnd();
        while (match(TokenType.OR)) {
            Token operator = previous();
            Skib right = logicAnd();
            skib = new Skib.LogicalSkib(skib, operator, right);
        }
        return skib;
    }

    private Skib logicAnd() {
        Skib skib = equality();
        while (match(TokenType.AND)) {
            Token operator = previous();
            Skib right = equality();
            skib = new Skib.LogicalSkib(skib, operator, right);
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
        return call();
    }

    private Skib call(){
        Skib skib = primary();
        while (true) {
            if (match(TokenType.LEFT_PAREN)) {
                skib = finishCall(skib);
            } else {
                break;
            }
        }
        return skib;
    }

    private Skib finishCall(Skib callee) {
        List<Skib> arguments = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                arguments.add(expression());
            } while (match(TokenType.COMMA));
        }
        Token paren = consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments.");
        return new Skib.CallSkib(callee, paren, arguments);
    }

    private Skib primary(){

        if (match(TokenType.L)) {
            return new Skib.literalSkib(false);
        }
        else if (match(TokenType.W)) {
            return new Skib.literalSkib(true);
        }
        else if (match(TokenType.NIL)) {
            return new Skib.literalSkib(null);
        }
        else if (match(TokenType.IDENTIFIER)) {
            return new Skib.bludSkib(previous());
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
            case BLUD:
            case RIZZWALK:
            case VIBECHECK:
            case COOK:
            case YAP:
            case YEET:
            return;
        }

        advance();
        }
  }
}
