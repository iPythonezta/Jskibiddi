package skibidi;

/*
 Base Lang Grammar:
    skib     → literalskib
                | monoskib
                | duoskib
                | nestskib ;

    literalskib        → NUMBER | STRING | "true" | "false" | "nil" ;
    nestskib       → "(" skib ")" ;
    monoskib          → ( "-" | "!" ) skib ;
    duoskib         → skib operator skib ;
    operator       → "==" | "!=" | "<" | "<=" | ">" | ">="
                | "+"  | "-"  | "*" | "/" ;
*/


abstract class Skib {

    public interface Visitor<R> {
        R visitDuoSkib(DuoSkib duoskib);
        R visitMonoSkib(MonoSkib monoskib);
        R visitNestSkib(NestSkib nestskib);
        R visitLiteralSkib(literalSkib literalSkib);
    }

    abstract <R> R accept(Visitor<R> visitor);

    static class DuoSkib extends Skib {
        final Token operator;
        final Skib leftSkib;
        final Skib rightSkib;
        public DuoSkib(Skib leftSkib, Token operator, Skib rightSkib) {
            this.leftSkib = leftSkib;
            this.operator = operator;
            this.rightSkib = rightSkib;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitDuoSkib(this);
        }
    }

    static class MonoSkib extends Skib {
        final Token operator;
        final Skib skib;
        public MonoSkib(Token operator, Skib skib) {
            this.operator = operator;
            this.skib = skib;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitMonoSkib(this);
        }
    }

    static class NestSkib extends Skib {
        final Skib skib;
        public NestSkib(Skib skib) {
            this.skib = skib;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitNestSkib(this);
        }
    }

    static class literalSkib extends Skib {
        final Object value;
        public literalSkib(Object value) {
            this.value = value;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralSkib(this);
        }
    }
}
