package jhaturanga.model.problems;

import java.util.List;

import jhaturanga.model.board.Board;
import jhaturanga.model.movement.BasicMovement;

public interface Problem {
    /**
     * 
     * @return List<BasicMovement> - the sequence of moves the problem is made of.
     */
    List<BasicMovement> getCorrectMoves();

    /**
     * 
     * @return Board - the starting board representing a situation in medias-res of
     *         a match.
     */
    Board getStartingBoard();
}
