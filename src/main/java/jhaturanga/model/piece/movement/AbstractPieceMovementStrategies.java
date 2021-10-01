package jhaturanga.model.piece.movement;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import jhaturanga.commons.Pair;
import jhaturanga.commons.functional.TriFunction;
import jhaturanga.model.board.Board;
import jhaturanga.model.board.BoardPosition;
import jhaturanga.model.board.BoardPositionImpl;
import jhaturanga.model.piece.Piece;
import jhaturanga.model.piece.PieceType;
import one.util.streamex.StreamEx;

public abstract class AbstractPieceMovementStrategies implements PieceMovementStrategies {

    /**
     * Single increments are used for movement related calculation.
     */
    protected static final int SINGLE_INCREMENT = 1;
    /**
     * Double increments are used for movement related calculation.
     */
    protected static final int DOUBLE_INCREMENT = 2;

    private final BiFunction<BoardPosition, Pair<Integer, Integer>, BoardPosition> sumBoardPosWithPair = (pos,
            pair) -> new BoardPositionImpl(pos.getX() + pair.getX(), pos.getY() + pair.getY());

    private final Function<Pair<Integer, Integer>, UnaryOperator<BoardPosition>> unaryOperatorFromAxis = (
            axis) -> (p) -> this.sumBoardPosWithPair.apply(p, axis);

    private final Function<Board, Integer> fromBoardToMaximumLimit = (board) -> board.getRows() + board.getColumns();
    /**
     * If you need to call the fromFunction method twice for specular directions use
     * this TriFunction specularNoLimitDirection instead.
     */
    private final TriFunction<Piece, Vectors, Board, Set<BoardPosition>> specularNoLimitDirection = (piece, axis,
            board) -> StreamEx.of(
                    this.getDestinationsFromFunction(this.unaryOperatorFromAxis.apply(axis.getAxis()), piece, board,
                            this.fromBoardToMaximumLimit.apply(board)),
                    this.getDestinationsFromFunction(this.unaryOperatorFromAxis.apply(axis.getOpposite()), piece, board,
                            this.fromBoardToMaximumLimit.apply(board)))
                    .flatMap(Set::stream).toSet();

    private final Map<PieceType, Function<Piece, MovementStrategy>> fromPieceTypeToStrategy = new EnumMap<>(
            PieceType.class) {
        private static final long serialVersionUID = 1L;
        {
            put(PieceType.PAWN, AbstractPieceMovementStrategies.this::getPawnMovementStrategy);
            put(PieceType.ROOK, AbstractPieceMovementStrategies.this::getRookMovementStrategy);
            put(PieceType.KNIGHT, AbstractPieceMovementStrategies.this::getKnightMovementStrategy);
            put(PieceType.BISHOP, AbstractPieceMovementStrategies.this::getBishopMovementStrategy);
            put(PieceType.QUEEN, AbstractPieceMovementStrategies.this::getQueenMovementStrategy);
            put(PieceType.KING, AbstractPieceMovementStrategies.this::getKingMovementStrategy);
        }
    };

    protected final Set<BoardPosition> getDestinationsFromFunction(final UnaryOperator<BoardPosition> function,
            final Piece piece, final Board board, final int limit) {

        final Predicate<BoardPosition> isEnemyOrEmpty = (pos) -> board.getPieceAtPosition(pos)
                .map(p -> !p.getPlayer().equals(piece.getPlayer())).orElse(true);

        final Predicate<BoardPosition> isPositionEmpty = (pos) -> board.getPieceAtPosition(pos).isEmpty();

        return StreamEx.iterate(function.apply(piece.getPiecePosition()), function).takeWhile(board::contains)
                .takeWhileInclusive(isPositionEmpty).filter(isEnemyOrEmpty).limit(limit).toSet();
    }

    @Override
    public final MovementStrategy getPieceMovementStrategy(final Piece piece) {
        return this.fromPieceTypeToStrategy.get(piece.getType()).apply(piece);
    }

    /**
     * 
     * @return TriFunction<Piece, Vectors, Board, Set<BoardPosition>> used by
     *         sub-classes to more easily access the fromFunction method
     */
    protected final TriFunction<Piece, Vectors, Board, Set<BoardPosition>> getSpecularNoLimitDirection() {
        return this.specularNoLimitDirection;
    }

    /**
     * @param piece - the piece, in this case a Pawn, of which get it's
     *              MovementStrategy.
     * @return PieceMovementStrategy representing the movementStrategy of a Pawn
     */
    protected abstract MovementStrategy getPawnMovementStrategy(Piece piece);

    /**
     * @param piece - the piece, in this case a Rook, of which get it's
     *              MovementStrategy.
     * @return PieceMovementStrategy representing the movementStrategy of a Rook
     */
    protected abstract MovementStrategy getRookMovementStrategy(Piece piece);

    /**
     * @param piece - the piece, in this case a Knight, of which get it's
     *              MovementStrategy.
     * @return PieceMovementStrategy representing the movementStrategy of a Knight
     */
    protected abstract MovementStrategy getKnightMovementStrategy(Piece piece);

    /**
     * @param piece - the piece, in this case a Bishop, of which get it's
     *              MovementStrategy.
     * @return PieceMovementStrategy representing the movementStrategy of a Bishop
     */
    protected abstract MovementStrategy getBishopMovementStrategy(Piece piece);

    /**
     * @param piece - the piece, in this case a Queen, of which get it's
     *              MovementStrategy.
     * @return PieceMovementStrategy representing the movementStrategy of a Queen
     */
    protected abstract MovementStrategy getQueenMovementStrategy(Piece piece);

    /**
     * @param piece - the piece, in this case a King, of which get it's
     *              MovementStrategy.
     * @return PieceMovementStrategy representing the movementStrategy of a King
     */
    protected abstract MovementStrategy getKingMovementStrategy(Piece piece);
}
