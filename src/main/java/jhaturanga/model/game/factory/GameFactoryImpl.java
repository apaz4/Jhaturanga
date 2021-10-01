package jhaturanga.model.game.factory;

import jhaturanga.model.board.Board;
import jhaturanga.model.board.starting.StartingBoardFactory;
import jhaturanga.model.board.starting.StartingBoardFactoryImpl;
import jhaturanga.model.editor.StringBoard;
import jhaturanga.model.game.Game;
import jhaturanga.model.game.controller.ClassicGameController;
import jhaturanga.model.game.controller.GameController;
import jhaturanga.model.game.controller.PieceSwapVariantGameController;
import jhaturanga.model.game.type.GameType;
import jhaturanga.model.movement.manager.BombVariantMovementManager;
import jhaturanga.model.movement.manager.ChessProblemsMovementManager;
import jhaturanga.model.movement.manager.ClassicMovementManager;
import jhaturanga.model.movement.manager.PieceSwapVariantMovementManager;
import jhaturanga.model.piece.movement.ClassicNoCastlingPieceMovementStrategies;
import jhaturanga.model.piece.movement.ClassicWithCastlingPieceMovementStrategies;
import jhaturanga.model.piece.movement.EveryoneMovesLikeRooksPieceMovementStrategies;
import jhaturanga.model.piece.movement.KingAsQueenPieceMovementStrategies;
import jhaturanga.model.piece.movement.OneDimensionPieceMovementStrategies;
import jhaturanga.model.piece.movement.PawnVariantPieceMovementStrategies;
import jhaturanga.model.piece.movement.PieceMovementStrategies;
import jhaturanga.model.piece.movement.RookAndBishopVariantPieceMovementStrategies;
import jhaturanga.model.player.pair.PlayerPair;
import jhaturanga.model.problems.Problem;

public final class GameFactoryImpl implements GameFactory {

    private final StartingBoardFactory startingBoardFactory = new StartingBoardFactoryImpl();

    private Game allClassicApartFromMovementStrategy(final PlayerPair players,
            final PieceMovementStrategies pieceMovementStrategy, final GameType type) {
        final GameController gameController = new ClassicGameController(this.startingBoardFactory.classicBoard(players),
                pieceMovementStrategy, players);

        return Game.builder().type(type).gameController(gameController)
                .movementManager(new ClassicMovementManager(gameController)).build();
    }

    private Game allClassicDifferentBoard(final PlayerPair players, final Board startingBoard, final GameType type) {
        final GameController gameController = new ClassicGameController(startingBoard,
                new ClassicNoCastlingPieceMovementStrategies(), players);

        return Game.builder().type(type).gameController(gameController)
                .movementManager(new ClassicMovementManager(gameController)).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game classicGame(final PlayerPair players) {
        return this.allClassicApartFromMovementStrategy(players, new ClassicWithCastlingPieceMovementStrategies(),
                GameType.CLASSIC);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game pawnMovemementVariantGame(final PlayerPair players) {
        return this.allClassicApartFromMovementStrategy(players, new PawnVariantPieceMovementStrategies(),
                GameType.PAWN_MOVEMENT_VARIANT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game kingMovesAsQueenVariantGame(final PlayerPair players) {
        return this.allClassicApartFromMovementStrategy(players, new KingAsQueenPieceMovementStrategies(),
                GameType.KING_MOVES_LIKE_QUEEN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game everyPieceMovesLikeRooksVariantGame(final PlayerPair players) {
        return this.allClassicApartFromMovementStrategy(players, new EveryoneMovesLikeRooksPieceMovementStrategies(),
                GameType.EVERYONE_MOVES_LIKE_A_ROOK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game rookBishopMovementVariantGame(final PlayerPair players) {
        return this.allClassicApartFromMovementStrategy(players, new RookAndBishopVariantPieceMovementStrategies(),
                GameType.ROOK_AND_BISHOP_MOVEMENT_VARIANT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game pawnHordeVariantGame(final PlayerPair players) {
        return this.allClassicDifferentBoard(players, this.startingBoardFactory.pawnHordeBoard(players),
                GameType.PAWN_HORDE_VARIANT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game threeColumnsVariantGame(final PlayerPair players) {
        return this.allClassicDifferentBoard(players, this.startingBoardFactory.threeColumnsBoard(players),
                GameType.THREE_COLUMNS_VARIANT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game oneDimensionVariantGame(final PlayerPair players) {
        final GameController gameController = new ClassicGameController(
                this.startingBoardFactory.oneDimensionBoard(players), new OneDimensionPieceMovementStrategies(),
                players);

        return Game.builder().type(GameType.ONE_DIMENSION_VARIANT).gameController(gameController)
                .movementManager(new ClassicMovementManager(gameController)).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game pieceSwapVariantGame(final PlayerPair players) {
        final GameController gameController = new PieceSwapVariantGameController(
                this.startingBoardFactory.classicBoard(players), new ClassicNoCastlingPieceMovementStrategies(),
                players);

        return Game.builder().type(GameType.PIECE_SWAP_VARIANT).gameController(gameController)
                .movementManager(new PieceSwapVariantMovementManager(gameController)).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game bombVariantGame(final PlayerPair players) {
        final GameController gameController = new ClassicGameController(this.startingBoardFactory.classicBoard(players),
                new ClassicNoCastlingPieceMovementStrategies(), players);

        return Game.builder().type(GameType.BOMB_VARIANT).gameController(gameController)
                .movementManager(new BombVariantMovementManager(gameController)).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game chessProblemGameType(final PlayerPair players, final Problem chessProblem) {
        final PieceMovementStrategies pmsf = new ClassicNoCastlingPieceMovementStrategies();
        final GameController gameController = new ClassicGameController(chessProblem.getStartingBoard(), pmsf, players);

        return Game.builder().type(GameType.CHESS_PROBLEM).gameController(gameController)
                .movementManager(new ChessProblemsMovementManager(gameController, chessProblem.getCorrectMoves()))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game customizedBoardVariantGame(final PlayerPair players, final StringBoard startingBoardInfo) {
        return this.allClassicDifferentBoard(
                players, this.startingBoardFactory.customizedBoard(startingBoardInfo.getBoard(),
                        startingBoardInfo.getColumns(), startingBoardInfo.getRows(), players),
                GameType.CUSTOM_BOARD_VARIANT);
    }

}
