package skibidi;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();
    final Environment enclosing;

    Environment(){
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    void define (String name, Object value) {
        values.put(name, value);
    }

    Object get(Token name) {
        // System.out.println("Getting variable: " + name.lexeme);
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }
        if (enclosing != null) {
            return enclosing.get(name);
        }
        throw new RuntimeError(name, String.format("Undefined variable '%s'.", name.lexeme));
    }

    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeError(name, String.format("Undefined variable '%s'.", name.lexeme));
    }
}
