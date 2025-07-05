package skibidi;

import skibidi.Skib.ConditionalSkib;
import skibidi.Skib.DuoSkib;
import skibidi.Skib.MonoSkib;
import skibidi.Skib.NestSkib;
import skibidi.Skib.literalSkib;

public class Interpreter implements Skib.Visitor<Object> {

    @Override
    public Object visitDuoSkib(DuoSkib duoskib) {

        Object left = evaluate(duoskib.leftSkib);
        Object right = evaluate(duoskib.rightSkib);
        switch (duoskib.operator.type){
            case MINUS:
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                if (left instanceof String && right instanceof Double) {
                    return (String) left + right.toString();
                }
                if (left instanceof Double && right instanceof String) {
                    return left.toString() + (String) right;
                }
            case SLASH:
                return (double) left / (double) right;
            case STAR:  
                return (double) left * (double) right;
            case GREATER:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left > (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return ((String) left).compareTo((String) right) > 0;
                }
            case GREATER_EQUAL:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left >= (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return ((String) left).compareTo((String) right) >= 0;
                }
            case LESS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left < (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return ((String) left).compareTo((String) right) < 0;
                }
            case LESS_EQUAL:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left <= (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return ((String) left).compareTo((String) right) <= 0;
                }
            case EQUAL_EQUAL:
                return isEquals(left, right);
        }
        return null;
    }

    @Override
    public Object visitMonoSkib(MonoSkib monoskib) {
        Object right = evaluate(monoskib.skib);
        switch (monoskib.operator.type) {
            case MINUS:
                return -(double) right;
            case BANG:
                return !isTruthy(right);
        }
        return null;
    }

    @Override
    public Object visitNestSkib(NestSkib nestskib) {

        return evaluate(nestskib.skib);
    }

    @Override
    public Object visitLiteralSkib(literalSkib literalSkib) {
        return literalSkib.value;
    }

    @Override
    public Object visitConditionalSkib(ConditionalSkib conditionalSkib) {

        throw new UnsupportedOperationException("Unimplemented method 'visitConditionalSkib'");
    }

    private Object evaluate(Skib skib) {
        return skib.accept(this);
    }   

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    private boolean isEquals(Object left, Object right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        return left.equals(right);
    }

} 
