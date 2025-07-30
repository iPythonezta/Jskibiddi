package skibidi;
import java.util.HashMap;
import java.util.Map;

public class SkibidiInstance {
    private SkibidiGang gang;
    private Map<String, Object> fields = new HashMap<>();

    public SkibidiInstance(SkibidiGang gang) {
        this.gang = gang;
    }

    Object get(Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }
        Skibidi.runtimeError(new RuntimeError(name, "Undefined property '" + name.lexeme + "'."));
        return null; // or throw an exception
    }

    void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }

    @Override
    public String toString() {
        return "SkibidiInstance of " + gang.toString();
    }
}
