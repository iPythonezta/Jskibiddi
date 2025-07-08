package skibidi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static skibidi.TokenType.*;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private static final Map<String, TokenType> keywords;
	private static TokenType put;

    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("vibecheckfail", VIBECHECKFAIL);
        keywords.put("L", L);
        keywords.put("fun", FUN);
        keywords.put("rizzwalk", RIZZWALK);
        keywords.put("vibecheck", VIBECHECK);
        keywords.put("none", NIL);
        keywords.put("or", OR);
        keywords.put("yap", YAP);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("W", W);
        keywords.put("blud", BLUD);
        keywords.put("cook", COOK);
        keywords.put("ragequit", RAGEQUIT);
        keywords.put("ghostnext", GHOSTNEXT);
        keywords.put("pray", PRAY);
        keywords.put("onL", ONL);
        keywords.put("gotchu", GOTCHU);
    }


    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int col = 0;


    Lexer(String source){
        this.source = source;
    }

    private boolean isAtEnd(){
        return current >= source.length();
    }

    private char advance(){
        current ++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type){
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal){
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line, col, text.length()));
        col += text.length();
    }

    private boolean match(char expectedChar){
        if (isAtEnd()) return false;
        if (source.charAt(current) != expectedChar ){
            return false;
        }
        current ++;
        return true;
    }

    private char peek(){
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private void string(){
        while (peek() != '"' && !isAtEnd()){
            if (peek() == '\n') line ++;
            advance();
        }

        if (isAtEnd()) {
            Skibidi.report(line, col, "Unterminated String");
            return;
        }

        advance();
        String raw = source.substring(start+1, current-1);

        // Initial support for some escape characters  -- will definitely be a thing to work on in future 
        // We need to add support for more backspace chars
        // Add support for escaping quotes
        // Another TODO is to move this code above with the peek() thingy

        StringBuilder value = new StringBuilder();
        for (int i = 0; i < raw.length(); i++){
            char c = raw.charAt(i);
            if (c == '\\' && i + 1 < raw.length()){
                char next = raw.charAt(i+1);
                switch (next) {
                    case 'n':
                        value.append('\n');
                        break;
                    case 't':
                        value.append('\t');
                        break;
                    case '"':
                        value.append('"');
                        break;
                    case '\\':
                        value.append('\\');
                        break;
                    default:
                        value.append(next);
                        break;
                }
                i ++;
            }
            else {
                value.append(c);
            }

        }
        addToken(STRING, value.toString());
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number (){
        while (isDigit(peek())) advance();
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();
            while (isDigit(peek())) advance();
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    } 

    private boolean isAlpha (char c){
        return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_');
    }

    private boolean isAlphaNumeric (char c){
        return isDigit(c) || isAlpha(c);
    }

    private void identifier(){
        while (isAlphaNumeric(peek())){
            advance();
        }
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        // System.out.println("Identifier: " + text);
        // System.out.println("Type: " + type);
        if (type == null){
            type = IDENTIFIER;
        }
        addToken(type);
    }

    private void scanToken(){
        char c = advance();
        switch (c){
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break; 
            case '?': addToken(QUESTION_MARK); break;
            case ':': addToken(COLON); break;
            case '/': 
                if (match('/')){
                    while (peek() != '\n' && !isAtEnd()){
                        advance();
                    } 
                }
                else if (match('*')){
                    while (!(peek() == '*' && peekNext() == '/')){
                        if (isAtEnd()){
                            Skibidi.report(line, col, "Unterminated Block Comment");
                            return;
                        }
                        if (peek() == '\n') line ++;
                        advance();
                    }
                    advance();
                    advance(); 
                }
                else {
                    addToken(SLASH); 
                }
                break;
            case '#':
                // # Comments
                while (peek() != '\n' && !isAtEnd()){
                    advance();
                } 
                break;
            case '!': 
                addToken(match('=') ? BANG_EQUAL : EQUAL);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=')? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=')? GREATER_EQUAL : GREATER);
                break;
            case ' ':
            case '\r':
                col ++;
                break;
            case '\t':
                col += 4;
                break;
            case '\n':
                line ++;
                col = 0;
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(c)){
                    number();
                }
                else if (isAlpha(c)){
                    identifier();
                }
                else {
                    Skibidi.report(line, col, String.format("Unexpected Character %s encontered", c));
                    col ++;
                    break;
                }
        }
    }

    List<Token> scanTokens(){
        while (!isAtEnd()){
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line, col, 0 ));
        return tokens;
    }
}
