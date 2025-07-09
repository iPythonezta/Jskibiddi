package skibidi;


class RuntimeError extends RuntimeException {
    final Token token;

    public RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }
}

class RageQuitException extends RuntimeException {}

class GhostNextException extends RuntimeException {}

class Yeet extends RuntimeException {
    final Object value;
    public Yeet(Object value) {
        super(null, null, false, false);
        this.value = value;
    }
}