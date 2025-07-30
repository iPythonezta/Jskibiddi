package skibidi;

public class SkibidiGang implements Interpreter.SkibidiCallable {
    final String name;

    SkibidiGang(String name) {
        this.name = name;
    }

    @Override
    public Object call(Interpreter interpreter, java.util.List<Object> arguments) {
        SkibidiInstance skibidiInstance = new SkibidiInstance(this);
        return skibidiInstance;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public String toString(){
        return name;
    }
}
