package skibidi;
import java.util.List;

public abstract class Stmt {

    abstract <R> R accept(Visitor<R> visitor);

    public interface Visitor<R> {
        R visitSkibStmt(SkibStmt skibStmt);
        R visitYap(YapStmt yap);
        R visitBludStmt(BludDeclr bludDeclr);
        R visitBlockStmt(BlockStmt blockStmt);
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
}
