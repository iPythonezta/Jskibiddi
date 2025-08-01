package skibidi;


public abstract class Skib {

    public interface Visitor<R> {
        R visitDuoSkib(DuoSkib duoskib);
        R visitMonoSkib(MonoSkib monoskib);
        R visitNestSkib(NestSkib nestskib);
        R visitLiteralSkib(literalSkib literalSkib);
        R visitConditionalSkib(ConditionalSkib conditionalSkib);
        R visitBludSkib(bludSkib bludSkib);
        R vistiAssignBludSkib(AssignBludSkib assignBludSkib);
        R visitLogicalSkib(LogicalSkib logicalSkib);
        R visitCallSkib(CallSkib callSkib);
        R visitGetSkib(GetSkib getSkib);
        R visitSetSkib(SetSkib setSkib);
    }

    abstract <R> R accept(Visitor<R> visitor);

    public static class ConditionalSkib extends Skib {
        final Skib condition;
        final Skib thenBranch;
        final Skib elseBranch;

        public ConditionalSkib(Skib condition, Skib thenBranch, Skib elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitConditionalSkib(this);
        }
    }

    public static class DuoSkib extends Skib {
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

    public static class MonoSkib extends Skib {
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

    public static class NestSkib extends Skib {
        final Skib skib;
        public NestSkib(Skib skib) {
            this.skib = skib;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitNestSkib(this);
        }
    }

    public static class literalSkib extends Skib {
        final Object value;
        public literalSkib(Object value) {
            this.value = value;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralSkib(this);
        }
    }

    public static class bludSkib extends Skib {
        final Token name;

        public bludSkib(Token name) {
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBludSkib(this);
        }
    }

    public static class AssignBludSkib extends Skib {
        final Token name;
        final Skib value;

        public AssignBludSkib(Token name, Skib value) {
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.vistiAssignBludSkib(this); // Assuming you want to treat it as a bludSkib
        }
    }

    public static class LogicalSkib extends Skib {
        final Skib left;
        final Token operator;
        final Skib right;

        public LogicalSkib(Skib left, Token operator, Skib right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalSkib(this);
        }
    }


    public static class CallSkib extends Skib {
        final Skib callee;
        final java.util.List<Skib> arguments;
        final Token paren;

        public CallSkib(Skib callee, Token paren, java.util.List<Skib> arguments) {
            this.callee = callee;
            this.arguments = arguments;
            this.paren = paren;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallSkib(this); // Assuming you want to treat it as a DuoSkib
        }
    }

    public static class GetSkib extends Skib {
        final Skib object;
        final Token name;

        public GetSkib(Skib object, Token name) {
            this.object = object;
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetSkib(this);
        }
    }

    public static class SetSkib extends Skib {
        final Skib object;
        final Token name;
        final Skib value;

        public SetSkib(Skib object, Token name, Skib value) {
            this.object = object;
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSetSkib(this);
        }
    }

}
