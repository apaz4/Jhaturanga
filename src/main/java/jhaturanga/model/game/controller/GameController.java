package jhaturanga.model.game.controller;

import jhaturanga.model.board.Board;
import jhaturanga.model.game.GameStatus;
import jhaturanga.model.movement.PieceMovement;
import jhaturanga.model.piece.movement.PieceMovementStrategies;
import jhaturanga.model.player.Player;
import jhaturanga.model.player.pair.PlayerPair;

/**
 * The controller for the chess game.
 */
public interface GameController {

    /**
     * Get the actual game status.
     * 
     * @param playerTurn - The Player who's turn it is. We in fact need to know
     *                   who's turn it is to determine the outcome of certain
     *                   situations such as draw conditions.
     * @return EndGameType - The situation of the match.
     */
    GameStatus getGameStatus(Player playerTurn);

    /**
     * Control if the king is under check.
     * 
     * @param player is the player of which to check if the move puts his king in a
     *               check state
     * @return true if the king is under check
     */
    boolean isInCheck(Player player);

    /**
     * Control if the king is under check.
     * 
     * @param movement - the movement to test whether it wouldn't put the executing
     *                 player in check.
     * @return true if the movement wouldn't result in a check by the same player
     *         who executed the movement.
     */
    boolean wouldNotBeInCheck(PieceMovement movement);

    /**
     * Return a boolean that states if the player passed as parameter won the game.
     * 
     * @param player is the player of which check if he won the game
     * @return true if player won false otherwise
     */
    boolean isWinner(Player player);

    /**
     * Return the state of the Board.
     * 
     * @return Board representing the actual state of the board of the match
     */
    Board getBoard();

    /**
     * Return the list of the players.
     * 
     * @return List representing the players of the game
     */
    PlayerPair getPlayers();

    /**
     * Return the PieceMovementStrategyFactory of the match's GameType that's been
     * controlled.
     * 
     * @return PieceMovementStrategyFactory representing the
     *         PieceMovementStrategyFactory of the game
     */
    PieceMovementStrategies getPieceMovementStrategies();
}
