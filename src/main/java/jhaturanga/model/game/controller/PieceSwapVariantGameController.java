package jhaturanga.model.game.controller;

import jhaturanga.model.board.Board;
import jhaturanga.model.piece.PieceType;
import jhaturanga.model.piece.movement.PieceMovementStrategies;
import jhaturanga.model.player.pair.PlayerPair;

/**
 * The Game Controller for the Piece Swap variant.
 */
public final class PieceSwapVariantGameController extends ClassicGameController {

    public PieceSwapVariantGameController(final Board board, final PieceMovementStrategies pieceMovementStrategies,
            final PlayerPair players) {
        super(board, pieceMovementStrategies, players);
    }

    /**
     * {@inheritDoc}
     * 
     * The PieceSwapVariantGameType can't rely fully on the ClassicGameController
     * due to some differences in the draw conditions caused by insufficient
     * material.
     * 
     * @return true only if the game is in a status where there are only to kings,
     *         any other situation may lead to check-mate.
     */
    @Override
    protected boolean insufficientMaterialToWin() {
        return this.getBoard().getPieces().stream().filter(i -> !i.getType().equals(PieceType.KING)).count() == 0;

    }

}
