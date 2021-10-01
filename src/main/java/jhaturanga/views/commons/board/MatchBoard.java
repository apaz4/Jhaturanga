package jhaturanga.views.commons.board;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javafx.application.Platform;
import jhaturanga.commons.settings.media.sound.Sound;
import jhaturanga.commons.settings.media.sound.SoundsEnum;
import jhaturanga.controllers.match.MatchController;
import jhaturanga.model.board.Board;
import jhaturanga.model.board.BoardPosition;
import jhaturanga.model.match.MatchStatus;
import jhaturanga.model.movement.MovementResult;
import jhaturanga.model.movement.PieceMovement;
import jhaturanga.model.piece.Piece;
import jhaturanga.views.commons.board.strategy.history.HistoryNavigationController;
import jhaturanga.views.commons.board.strategy.history.NormalHistoryKeyHandlerStrategy;
import jhaturanga.views.commons.board.strategy.movement.NormalMatchPieceMovementStrategy;
import jhaturanga.views.commons.component.Tile;

/**
 * The graphical board used for play a match.
 */
public class MatchBoard extends GraphicalBoard {

    private final Runnable onMatchFinish;
    private final MatchController matchController;

    public MatchBoard(final MatchController matchController, final Runnable onMatchFinish) {
        super(matchController.getBoard().getRows(), matchController.getBoard().getColumns());

        this.matchController = matchController;
        this.onMatchFinish = onMatchFinish;
    }

    /**
     * Setup the board.
     */
    public final void setup() {
        this.setGraphicPieceMovementStrategy(new NormalMatchPieceMovementStrategy(this, this.matchController));
        this.setHistoryKeyHandlerStrategy(new NormalHistoryKeyHandlerStrategy(this, new HistoryNavigationController() {
            @Override
            public Optional<Board> getPreviousBoard() {
                return matchController.getPreviousBoard();
            }

            @Override
            public Optional<Board> getNextBoard() {
                return matchController.getNextBoard();
            }
        }));
        this.createBoard();
        this.redraw(this.matchController.getBoard());
        Platform.runLater(() -> this.getGrid().requestFocus());
    }

    /**
     * Handle a movement.
     * 
     * @param newBoard       - the new board
     * @param movement       - the movement that has been made.
     * @param movementResult - the result of the movement.
     */
    public final void onMovement(final Board newBoard, final PieceMovement movement,
            final MovementResult movementResult) {
        this.resetHightlightedPositions();
        this.redraw(newBoard);
        this.highlightMovement(movement);
        Sound.play(SoundsEnum.fromMovementResult(movementResult));
        this.checkMatchStatus();
    }

    /**
     * Highlight a movement of a piece.
     * 
     * @param movement - the movement to be highlighted
     */
    public final void highlightMovement(final PieceMovement movement) {
        this.resetHighlightedMovements();
        final Predicate<Tile> isPositionInvoledInMovement = (
                tile) -> tile.getBoardPosition().equals(movement.getOrigin())
                        || tile.getBoardPosition().equals(movement.getDestination());
        this.getTiles().stream().filter(isPositionInvoledInMovement).forEach(Tile::highlightMovement);
    }

    /**
     * Reset the highlighted movement.
     */
    public final void resetHighlightedMovements() {
        this.getTiles().stream().forEach(Tile::resetHighlightMovement);
    }

    /**
     * Highlight positions, used for showing the possible moves of a piece.
     * 
     * @param positions - the position to be highlighted.
     */
    public final void hightlightPositons(final Set<BoardPosition> positions) {
        this.getTiles().stream().filter(x -> positions.contains(x.getBoardPosition())).forEach(x -> x.highlightPosition(
                this.matchController.getBoard().getPieceAtPosition(x.getBoardPosition()).isPresent()));
    }

    /**
     * Reset the hightlighted positions.
     */
    public final void resetHightlightedPositions() {
        this.getTiles().forEach(Tile::resetHighlightPosition);
    }

    /**
     * Draw all possible destionation of a selected piece.
     * 
     * @param piece - the peice which we want to draw all the possible moves.
     */
    public final void drawPossibleDestinations(final Piece piece) {
        this.resetHightlightedPositions();
        this.hightlightPositons(this.matchController.getPiecePossibleMoves(piece));

    }

    private void checkMatchStatus() {
        if (this.matchController.getStatus().equals(MatchStatus.ENDED)) {
            this.onMatchFinish.run();
        }
    }

}
