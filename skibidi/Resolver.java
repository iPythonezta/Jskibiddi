package skibidi;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Function;



public class Resolver implements Stmt.Visitor<Void>, Skib.Visitor<Void> {
    private enum FunctionType {
        NONE,
        SAUCE
    }   
    private FunctionType currentFunction = FunctionType.NONE;
    private final Interpreter interpreter;
    private final Stack<Map<String, Boolean>> scopes;
    public Resolver(Interpreter interpreter) {
        this.interpreter = interpreter;
        this.scopes = new Stack<>();
    }

    @Override
    public Void visitBlockStmt(Stmt.BlockStmt blockStmt) {
        beginScope();
        resolve(blockStmt.statements);
        endScope();
        return null;
    }

    @Override
    public Void visitBludStmt(Stmt.BludDeclr bludStmt) {
        declare(bludStmt.name);
        if (bludStmt.initializer != null) {
            resolve(bludStmt.initializer);
        }
        define(bludStmt.name);
        return null;
    }

    
    @Override
    public Void visitBludSkib(Skib.bludSkib bludExpr) {
        if (!scopes.isEmpty() && scopes.peek().containsKey(bludExpr.name.lexeme) == false) {
            Skibidi.report(bludExpr.name, "Cannot use blud '" + bludExpr.name.lexeme + "' before declaration.");
        }
        resolveLocal(bludExpr, bludExpr.name);
        return null;
    }

    @Override
    public Void vistiAssignBludSkib(Skib.AssignBludSkib assignBludExpr) {
        resolve(assignBludExpr.value);
        resolveLocal(assignBludExpr, assignBludExpr.name);
        return null;
    }

    @Override
    public Void visitSauceDeclr(Stmt.SauceDeclr sauceDeclr) {
        declare(sauceDeclr.name);
        define(sauceDeclr.name);

        resolveSauceDeclr(sauceDeclr, FunctionType.SAUCE);
        return null;
    }

    @Override
    public Void visitSkibStmt(Stmt.SkibStmt skibStmt) {
        resolve(skibStmt.skib);
        return null;
    }

    @Override
    public Void visitVibeCheckStmt(Stmt.VibeCheckStmt vibeCheckStmt) {
        resolve(vibeCheckStmt.condition);
        resolve(vibeCheckStmt.thenBranch);
        if (vibeCheckStmt.elseBranch != null) {
            resolve(vibeCheckStmt.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitYap(Stmt.YapStmt yapStmt) {
        resolve(yapStmt.skib);
        return null;
    }

    @Override
    public Void visitYeetStmt(Stmt.YeetStmt yeetStmt) {
        if (currentFunction == FunctionType.NONE) {
            Skibidi.report(yeetStmt.name, "Cannot return from a top-level context.");
        }
        resolve(yeetStmt.value);
        return null;
    }

    @Override
    public Void visitCookStmt(Stmt.CookStmt cookStmt) {
        resolve(cookStmt.condition);
        resolve(cookStmt.body);
        return null;
    }
    
    @Override
    public Void visitDuoSkib(Skib.DuoSkib duoSkib) {
        resolve(duoSkib.leftSkib);
        resolve(duoSkib.rightSkib);
        return null;
    }

    @Override
    public Void visitCallSkib(Skib.CallSkib callSkib) {
        resolve(callSkib.callee);
        for (Skib argument : callSkib.arguments) {
            resolve(argument);
        }
        return null;
    }
    
    @Override
    public Void visitNestSkib(Skib.NestSkib nestedSkib) {
        resolve(nestedSkib.skib);
        return null;
    }

    @Override
    public Void visitLiteralSkib(Skib.literalSkib literalSkib) {
        return null;
    }

    @Override
    public Void visitLogicalSkib(Skib.LogicalSkib logicalSkib) {
        resolve(logicalSkib.left);
        resolve(logicalSkib.right);
        return null;
    }

    @Override
    public Void visitMonoSkib(Skib.MonoSkib unarySkib) {
        resolve(unarySkib.skib);
        return null;
    }

    private void resolveSauceDeclr(Stmt.SauceDeclr sauceDeclr, FunctionType type) {
        FunctionType enclosingFunction = currentFunction;
        currentFunction = type;
        beginScope();
        for (Token param : sauceDeclr.parameters) {
            declare(param);
            define(param);
        }
        resolve(sauceDeclr.body);
        endScope();
    }

    private void resolveLocal(Skib skib, Token name) {
        for (int i = scopes.size(); i >= 0; i--){
            if (scopes.get(i).containsKey(name.lexeme)) {
                interpreter.resolve(skib, scopes.size() - 1 - i);
                return;
            }
        }
    }

    private void declare(Token name) {
        if (scopes.isEmpty()) return;
        Map<String, Boolean> scope = scopes.peek();
        if (scope.containsKey(name.lexeme)) {
            Skibidi.report(name, "Variable with this name already declared in this scope.");
        }
        scope.put(name.lexeme, false);
    }

    private void define(Token name) {
        if (scopes.isEmpty()) return;
        Map<String, Boolean> scope = scopes.peek();
        scope.put(name.lexeme, true);
    }

    public void resolve(List<Stmt> statements) {
        for (Stmt statement : statements) {
            resolve(statement);
        }
    }

    private void resolve(Stmt statement) {
        statement.accept(this);
    }

    private void resolve(Skib skib) {
        skib.accept(this);
    }

    private void beginScope() {
        scopes.push(new HashMap<String, Boolean>());
    }

    private void endScope() {
        scopes.pop();
    }

    @Override
    public Void visitConditionalSkib(Skib.ConditionalSkib conditionalSkib) {
        resolve(conditionalSkib.condition);
        resolve(conditionalSkib.thenBranch);
        if (conditionalSkib.elseBranch != null) {
            resolve(conditionalSkib.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitRageQuitStmt(Stmt.RageQuitStmt rageQuitStmt) {
        return null;
    }

    @Override
    public Void visitGhostNextStmt(Stmt.GhostNextStmt ghostNextStmt) {
        return null;
    }

    @Override
    public Void visitRizzWalkStmt(Stmt.RizzWalkStmt rizzWalkStmt) {
        resolve(rizzWalkStmt.bludDeclr);
        resolve(rizzWalkStmt.skib);
        resolve(rizzWalkStmt.body);
        resolve(rizzWalkStmt.increment);
        return null;
    }

    @Override
    public Void visitLHandlerStmt(Stmt.LHandlerStmt stmt) {
        resolve(stmt.body);

        if (stmt.LHandler != null && stmt.LIdentifier != null) {
            beginScope();  
            declare(stmt.LIdentifier.name);
            define(stmt.LIdentifier.name);
            resolve(stmt.LHandler);
            endScope();
        }

        // Resolve the finally block (if any)
        if (stmt.GotchuCode != null) {
            resolve(stmt.GotchuCode);
        }

        return null;
    }


}
