package skibidi;

import java.util.List;
import skibidi.Skib.ConditionalSkib;
import skibidi.Skib.DuoSkib;
import skibidi.Skib.MonoSkib;
import skibidi.Skib.NestSkib;
import skibidi.Skib.literalSkib;

public class Interpreter implements Skib.Visitor<Object>, Stmt.Visitor<Void> {

    private Environment environment = new Environment();
    private boolean replMode = false;
    
    public void setReplMode(boolean replMode) {
        this.replMode = replMode;
    } 

    public void interpret(List<Stmt> statements) {
        for (Stmt statement : statements) {
            try {
                if (replMode && (statement instanceof Stmt.SkibStmt)) {
                    Object result = evaluate(((Stmt.SkibStmt) statement).skib);
                    System.out.println(stringify(result));
                }
                else {
                    execute(statement);
                }
            } catch (RuntimeError error) {
                Skibidi.runtimeError(error);
            }
        }
    }

    @Override
    public Object visitDuoSkib(DuoSkib duoskib) {

        Object left = evaluate(duoskib.leftSkib);
        Object right = evaluate(duoskib.rightSkib);
        switch (duoskib.operator.type){
            case COMMA:
                return right;
            case MINUS:
                checkNumberOperand(duoskib.operator, left, right);
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                if (left instanceof String && right instanceof Double) {
                    return (String) left + stringify(right);
                }
                if (left instanceof Double && right instanceof String) {
                    return stringify(left) + (String) right;
                }
                if (left == null || right == null) {
                    throw new RuntimeError(duoskib.operator, "Cannot concatenate 'none' values.");
                }
                throw new RuntimeError(duoskib.operator, String.format("The operator '+' cannot be applied to operands of types '%s' and '%s'.", 
                        left.getClass().getSimpleName(), right.getClass().getSimpleName()));
            case SLASH:
                checkNumberOperand(duoskib.operator, left, right);
                if ((double) right == 0) {
                    throw new RuntimeError(duoskib.operator, "Division by zero is not allowed.");
                }
                return (double) left / (double) right;
            case STAR:  
                checkNumberOperand(duoskib.operator, left, right);
                return (double) left * (double) right;
            case GREATER:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left > (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return ((String) left).compareTo((String) right) > 0;
                }
                throw new RuntimeError(duoskib.operator, String.format("The operator '>' cannot be applied to operands of types '%s' and '%s'.", 
                        left.getClass().getSimpleName(), right.getClass().getSimpleName()));
            case GREATER_EQUAL:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left >= (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return ((String) left).compareTo((String) right) >= 0;
                }
                throw new RuntimeError(duoskib.operator, String.format("The operator '>=' cannot be applied to operands of types '%s' and '%s'.", 
                        left.getClass().getSimpleName(), right.getClass().getSimpleName()));
            case LESS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left < (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return ((String) left).compareTo((String) right) < 0;
                }
                throw new RuntimeError(duoskib.operator, String.format("The operator '<' cannot be applied to operands of types '%s' and '%s'.", 
                        left.getClass().getSimpleName(), right.getClass().getSimpleName()));
            case LESS_EQUAL:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left <= (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return ((String) left).compareTo((String) right) <= 0;
                }
                throw new RuntimeError(duoskib.operator, String.format("The operator '<=' cannot be applied to operands of types '%s' and '%s'.", 
                        left.getClass().getSimpleName(), right.getClass().getSimpleName()));
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
                checkNumberOperand(monoskib.operator, right);
                return -(double) right;
            case BANG:
                return !isTruthy(right);
            default:
                return null;
        }
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
        Object condition = evaluate(conditionalSkib.condition);
        if (isTruthy(condition)) {
            return evaluate(conditionalSkib.thenBranch);
        } else if (conditionalSkib.elseBranch != null) {
            return evaluate(conditionalSkib.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitSkibStmt (Stmt.SkibStmt skibStmt) {
        evaluate(skibStmt.skib);
        return null;
    }

    @Override
    public Void visitYap(Stmt.YapStmt print) {
        Object value = evaluate(print.skib);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitBludStmt(Stmt.BludDeclr bludDeclr){
        Object value = null;
        if (bludDeclr.initializer != null) {
            value = evaluate(bludDeclr.initializer);
        }
        environment.define(bludDeclr.name.lexeme, value);
        return null;
    }

    @Override
    public Object visitBludSkib(Skib.bludSkib bludSkib) {
        return environment.get(bludSkib.name);
    }

    @Override
    public Object vistiAssignBludSkib(Skib.AssignBludSkib assignBludSkib) {
        Object value = evaluate(assignBludSkib.value);
        environment.assign(assignBludSkib.name, value);
        return value;
    }

    @Override
    public Void visitBlockStmt(Stmt.BlockStmt blockStmt) {
        Environment previous = environment;
        try {
            environment = new Environment(environment);
            for (Stmt statement : blockStmt.statements) {
                execute(statement);  
            }
        } finally {
            environment = previous;
        }
        return null;
    }

    @Override
    public Void visitVibeCheckStmt(Stmt.VibeCheckStmt vibeCheckStmt) {
        Object condition = evaluate(vibeCheckStmt.condition);
        if (isTruthy(condition)) {
            execute(vibeCheckStmt.thenBranch);
        } else if (vibeCheckStmt.elseBranch != null) {
            execute(vibeCheckStmt.elseBranch);
        }
        return null;
    }

    @Override
    public Object visitLogicalSkib(Skib.LogicalSkib logicalSkib) {
        Object left = evaluate(logicalSkib.left);
        if (logicalSkib.operator.type == TokenType.OR) {
            if (isTruthy(left)) return left;
        } else if (logicalSkib.operator.type == TokenType.AND) {
            if (!isTruthy(left)) return left;
        }
        return evaluate(logicalSkib.right);
    }

    @Override
    public Void visitCookStmt(Stmt.CookStmt whileStmt) {
        while (isTruthy(evaluate(whileStmt.condition))) {
            try {
                execute(whileStmt.body);
            }
            catch (RageQuitException e) {
                break;
            }
            catch (GhostNextException e) {
                continue;
            }
        }
        return null;
    }

    @Override
    public Void visitRizzWalkStmt(Stmt.RizzWalkStmt rizzWalkStmt){
        execute(rizzWalkStmt.bludDeclr);
        while (isTruthy(evaluate(rizzWalkStmt.skib))){
            try {
                execute(rizzWalkStmt.body);
            } catch (RageQuitException e) {
                break;
            } catch (GhostNextException e) {
                continue;
            }
            finally {
                if (rizzWalkStmt.increment != null) {
                    evaluate(rizzWalkStmt.increment);
                }
            }
        }
        return null;    
    }

    @Override
    public Void visitRageQuitStmt(Stmt.RageQuitStmt rageQuitStmt) {
        throw new RageQuitException();
    }

    @Override
    public Void visitGhostNextStmt(Stmt.GhostNextStmt ghostNextStmt) {
        throw new GhostNextException();
    }

    @Override
    public Void visitLHandlerStmt(Stmt.LHandlerStmt lHandlerStmt) {
        Environment previous = environment;
        try {
            environment = new Environment(environment);
            try {
                execute(lHandlerStmt.body);
            } 
            catch (RuntimeError e) {
                execute(lHandlerStmt.LIdentifier);
                String errorMsg = Skibidi.getRunTimeErrorMessage(e);
                Skib assignment = new Skib.AssignBludSkib(lHandlerStmt.LIdentifier.name, new Skib.literalSkib(errorMsg));
                evaluate(assignment);
                execute(lHandlerStmt.LHandler);
            }
            if (lHandlerStmt.GotchuCode != null) {
                execute(lHandlerStmt.GotchuCode);
            }
           
        } finally {
            environment = previous;
        }
        return null;
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

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperand(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    public String stringify(Object object) {
        if (object == null) return "none";
        if (object instanceof Boolean) return (boolean) object ? "W" : "L";
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                return text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

} 
