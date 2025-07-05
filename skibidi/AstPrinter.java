package skibidi;

public class AstPrinter implements Skib.Visitor {
    
    String print(Skib skib) {
        return (String) skib.accept(this);
    }

    @Override
    public String visitConditionalSkib(Skib.ConditionalSkib conditionalSkib) {
        StringBuilder builder = new StringBuilder();
        builder.append("if (").append(conditionalSkib.condition.accept(this)).append(") ");
        builder.append(conditionalSkib.thenBranch.accept(this));
        if (conditionalSkib.elseBranch != null) {
            builder.append(" else ").append(conditionalSkib.elseBranch.accept(this));
        }
        return builder.toString();
    }

    @Override
    public String visitDuoSkib(Skib.DuoSkib duoskib) {
        return parenthesize(duoskib.operator.lexeme, duoskib.leftSkib, duoskib.rightSkib);
    }
    
    @Override
    public String visitMonoSkib(Skib.MonoSkib monoskib) {
        return parenthesize(monoskib.operator.lexeme, monoskib.skib);
    }
    
    @Override
    public String visitNestSkib(Skib.NestSkib nestskib) {
        return parenthesize("nest", nestskib.skib);
    }
    
    @Override
    public String visitLiteralSkib(Skib.literalSkib literalSkib) {
        if (literalSkib.value == null) {
            return "none";
        }
        return literalSkib.value.toString();
    }

    public String parenthesize(String name, Skib... skibs) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for (Skib skib : skibs) {
            builder.append(" ").append(skib.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }

}
