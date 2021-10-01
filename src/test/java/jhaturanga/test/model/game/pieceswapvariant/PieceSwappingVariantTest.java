package jhaturanga.test.model.game.pieceswapvariant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jhaturanga.model.board.BoardPositionImpl;
import jhaturanga.model.game.Game;
import jhaturanga.model.game.type.GameType;
import jhaturanga.model.match.Match;
import jhaturanga.model.match.MatchImpl;
import jhaturanga.model.movement.MovementResult;
import jhaturanga.model.movement.PieceMovementImpl;
import jhaturanga.model.piece.PieceType;
import jhaturanga.model.player.Player;
import jhaturanga.model.player.PlayerColor;
import jhaturanga.model.player.PlayerImpl;
import jhaturanga.model.player.pair.PlayerPair;
import jhaturanga.model.player.pair.PlayerPairImpl;
import jhaturanga.model.timer.DefaultTimers;
import jhaturanga.model.timer.Timer;
import jhaturanga.model.user.management.UsersManager;
import jhaturanga.test.model.commons.Constants;

class PieceSwappingVariantTest {

    private Player whitePlayer;
    private Player blackPlayer;

    @BeforeEach
    public void init() {
        this.whitePlayer = new PlayerImpl(PlayerColor.WHITE, UsersManager.GUEST);
        this.blackPlayer = new PlayerImpl(PlayerColor.BLACK, UsersManager.GUEST);
    }

    @Test
    void pieceSwapBasicTest() {
        final PlayerPair players = new PlayerPairImpl(this.whitePlayer, this.blackPlayer);
        final Game gameType = GameType.PIECE_SWAP_VARIANT.getGameInstance(players);
        final Timer timer = DefaultTimers.NO_LIMIT.getTimer(players);
        // new TimerFactoryImpl().equalTimer(List.of(whitePlayer, blackPlayer), 10);
        final Match match = new MatchImpl(gameType, timer);
        match.start();

        /**
         * In this variant every time you move a piece, if it's a Rook,Knight or Bishop
         * it changes it's PieceType. Rook -> Bishop, Bishop -> Knight, Knight -> Rook.
         */

        // White Knight moves
        assertFalse(match.move(new PieceMovementImpl(
                match.getBoard().getPieceAtPosition(new BoardPositionImpl(Constants.ONE, Constants.ZERO)).get(),
                new BoardPositionImpl(Constants.TWO, Constants.TWO))).equals(MovementResult.INVALID_MOVE));

        // The Knight is now a Rook
        assertEquals(PieceType.ROOK, match.getBoard()
                .getPieceAtPosition(new BoardPositionImpl(Constants.TWO, Constants.TWO)).get().getType());

        // Random black move for turn purpose
        assertFalse(match.move(new PieceMovementImpl(
                match.getBoard().getPieceAtPosition(new BoardPositionImpl(Constants.ZERO, Constants.SIX)).get(),
                new BoardPositionImpl(Constants.ZERO, Constants.FIVE))).equals(MovementResult.INVALID_MOVE));

        // The Rook moves
        assertFalse(match.move(new PieceMovementImpl(
                match.getBoard().getPieceAtPosition(new BoardPositionImpl(Constants.TWO, Constants.TWO)).get(),
                new BoardPositionImpl(Constants.TWO, Constants.FOUR))).equals(MovementResult.INVALID_MOVE));

        // The Rook is now a Bishop
        assertEquals(PieceType.BISHOP, match.getBoard()
                .getPieceAtPosition(new BoardPositionImpl(Constants.TWO, Constants.FOUR)).get().getType());

        // Random black move for turn purpose
        assertFalse(match.move(new PieceMovementImpl(
                match.getBoard().getPieceAtPosition(new BoardPositionImpl(Constants.ZERO, Constants.FIVE)).get(),
                new BoardPositionImpl(Constants.ZERO, Constants.FOUR))).equals(MovementResult.INVALID_MOVE));

        // The Bishop moves
        assertFalse(match.move(new PieceMovementImpl(
                match.getBoard().getPieceAtPosition(new BoardPositionImpl(Constants.TWO, Constants.FOUR)).get(),
                new BoardPositionImpl(Constants.THREE, Constants.THREE))).equals(MovementResult.INVALID_MOVE));

        // The Bishop is now a Knight
        assertEquals(PieceType.KNIGHT, match.getBoard()
                .getPieceAtPosition(new BoardPositionImpl(Constants.THREE, Constants.THREE)).get().getType());

    }

}
