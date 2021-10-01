package jhaturanga.model.board;

import java.util.HashSet;
import java.util.Set;

import jhaturanga.model.piece.Piece;

public final class BoardBuilderImpl implements BoardBuilder {

    private final Set<Piece> piecesOnBoard = new HashSet<>();
    private int rows;
    private int columns;
    private boolean built;

    /**
     * {@inheritDoc}
     */
    @Override
    public BoardBuilder rows(final int rows) {
        this.rows = rows;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoardBuilder columns(final int columns) {
        this.columns = columns;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoardBuilder addPiece(final Piece piece) {
        this.piecesOnBoard.add(piece);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Board build() {

        if (this.built) {
            throw new IllegalStateException("Alredy Built");
        }

        if (this.columns == 0 || this.rows == 0) {
            throw new IllegalStateException("Builder not completed");
        }

        this.built = true;
        return new BoardImpl(this.piecesOnBoard, this.columns, this.rows);
    }

}
