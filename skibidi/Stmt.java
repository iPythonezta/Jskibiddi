package skibidi;
import java.util.List;

public abstract class Stmt {

    abstract <R> R accept(Visitor<R> visitor);

    public interface Visitor<R> {
        R visitSkibStmt(SkibStmt skibStmt);
        R visitYap(YapStmt yap);
        R visitBludStmt(BludDeclr bludDeclr);
        R visitBlockStmt(BlockStmt blockStmt);
        R visitVibeCheckStmt(VibeCheckStmt vibeCheckStmt);
        R visitCookStmt(CookStmt whileStmt);
        R visitRageQuitStmt(RageQuitStmt rageQuitStmt);
        R visitGhostNextStmt(GhostNextStmt ghostNextStmt);
        R visitRizzWalkStmt(RizzWalkStmt rizzWalkStmt);
        R visitLHandlerStmt(LHandlerStmt lHandlerStmt);
    }

    public static class SkibStmt extends Stmt {
        final Skib skib;

        public SkibStmt(Skib skib) {
            this.skib = skib;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSkibStmt(this);
        }
    }

    public static class YapStmt extends Stmt {
        final Skib skib;

        public YapStmt(Skib skib) {
            this.skib = skib;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitYap(this);
        }
    }

    public static class BludDeclr extends Stmt{
        final Token name;
        final Skib initializer;
        public BludDeclr(Token name, Skib initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBludStmt(this); 
        }
    }

    public static class BlockStmt extends Stmt {
        final List<Stmt> statements;

        public BlockStmt(List<Stmt> statements) {
            this.statements = statements;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }
    }

    public static class VibeCheckStmt extends Stmt {
        final Skib condition;
        final Stmt thenBranch;
        final Stmt elseBranch;

        public VibeCheckStmt(Skib condition, Stmt thenBranch,  Stmt elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVibeCheckStmt(this);
        }
    }

    public static class CookStmt extends Stmt {
        final Skib condition;
        final Stmt body;

        public CookStmt(Skib condition, Stmt body) {
            this.condition = condition;
            this.body = body;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitCookStmt(this);
        }
    }

    public static class RizzWalkStmt extends Stmt {
        final Stmt bludDeclr;
        final Skib skib;
        final Stmt body;
        final Skib increment;

        public RizzWalkStmt(Stmt bludDeclr, Skib skib, Skib increment, Stmt body) {
            this.bludDeclr = bludDeclr;
            this.skib = skib;
            this.body = body;
            this.increment = increment;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitRizzWalkStmt(this);
        }
    }

    public static class RageQuitStmt extends Stmt {

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitRageQuitStmt(this);
        }
    }

    public static class GhostNextStmt extends Stmt {

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGhostNextStmt(this);
        }
    }

    public static class LHandlerStmt extends Stmt {
        final Stmt body;
        final Stmt LHandler;
        final Stmt GotchuCode;
        final BludDeclr LIdentifier;

        public LHandlerStmt(Stmt body, Stmt LHandler, Stmt GotchuCode, BludDeclr LIdentifier) {
            this.body = body;
            this.LHandler = LHandler;
            this.GotchuCode = GotchuCode;
            this.LIdentifier = LIdentifier;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLHandlerStmt(this);
        }
    }

}
