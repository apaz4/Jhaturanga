package jhaturanga.model.piece;

import jhaturanga.model.board.BoardPosition;
import jhaturanga.model.player.Player;

public final class PieceImpl implements Piece {

    /**
     * 
     */
    private static final long serialVersionUID = -5598857933686438523L;
    private final PieceType pieceType;
    private BoardPosition pieceActualBoardPosition;
    private final Player piecePlayerOwner;
    private boolean moved;

    public PieceImpl(final PieceType pieceType, final BoardPosition pieceActualBoardPosition,
            final Player piecePlayerOwner) {
        this.pieceType = pieceType;
        this.pieceActualBoardPosition = pieceActualBoardPosition;
        this.piecePlayerOwner = piecePlayerOwner;
    }

    public PieceImpl(final Piece piece) {
        this.pieceType = piece.getType();
        this.pieceActualBoardPosition = piece.getPiecePosition();
        this.piecePlayerOwner = piece.getPlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PieceType getType() {
        return this.pieceType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdentifier() {
        return this.pieceType.toString() + "-" + this.getPlayer().toString() + "-" + this.getPiecePosition().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(final BoardPosition positionalDestination) {
        this.pieceActualBoardPosition = positionalDestination;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoardPosition getPiecePosition() {
        return this.pieceActualBoardPosition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayer() {
        return this.piecePlayerOwner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAlreadyBeenMoved() {
        return this.moved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHasMoved(final boolean moved) {
        this.moved = moved;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "PieceImpl [" + getIdentifier() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pieceActualBoardPosition == null) ? 0 : pieceActualBoardPosition.hashCode());
        result = prime * result + ((piecePlayerOwner == null) ? 0 : piecePlayerOwner.hashCode());
        result = prime * result + ((pieceType == null) ? 0 : pieceType.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PieceImpl other = (PieceImpl) obj;
        if (pieceActualBoardPosition == null) {
            if (other.pieceActualBoardPosition != null) {
                return false;
            }
        } else if (!pieceActualBoardPosition.equals(other.pieceActualBoardPosition)) {
            return false;
        }
        if (piecePlayerOwner == null) {
            if (other.piecePlayerOwner != null) {
                return false;
            }
        } else if (!piecePlayerOwner.equals(other.piecePlayerOwner)) {
            return false;
        }
        return pieceType == other.pieceType;
    }

}
