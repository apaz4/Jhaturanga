package jhaturanga.model.editor;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jhaturanga.model.board.Board;
import jhaturanga.model.board.BoardBuilder;
import jhaturanga.model.board.BoardBuilderImpl;
import jhaturanga.model.board.BoardPosition;
import jhaturanga.model.piece.Piece;
import jhaturanga.model.piece.PieceType;
import jhaturanga.model.player.PlayerColor;

public final class EditorImpl implements Editor {

    private static final int DEFAULT_COLUMNS = 8;
    private static final int DEFAULT_ROWS = 8;
    private static final int MAX_NUMBER_OF_ROWS_AND_COLS = 26;
    private Board board;
    private StringBoard stringBoard;

    private final Map<PieceType, String> pieceTypeToLetter = Map.of(PieceType.KING, "k", PieceType.QUEEN, "q",
            PieceType.BISHOP, "b", PieceType.ROOK, "r", PieceType.PAWN, "p", PieceType.KNIGHT, "n");

    public EditorImpl() {
        final BoardBuilder boardBuilder = new BoardBuilderImpl();
        this.board = boardBuilder.columns(DEFAULT_COLUMNS).rows(DEFAULT_ROWS).build();
    }

    private boolean checkNewBoardDimension(final int cols, final int rows) {
        return cols > 0 && rows > 0 && cols <= MAX_NUMBER_OF_ROWS_AND_COLS && rows <= MAX_NUMBER_OF_ROWS_AND_COLS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPieceToBoard(final Piece piece) {
        this.board.add(piece);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Board getBoardStatus() {
        return this.board;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeBoardDimensions(final int columns, final int rows) {
        if (this.checkNewBoardDimension(columns, rows)) {
            final BoardBuilder boardBuilder = new BoardBuilderImpl();
            this.board = boardBuilder.columns(columns).rows(rows).build();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changePiecePosition(final Piece piece, final BoardPosition position) {
        if (this.board.getPieceAtPosition(position).isPresent()) {
            return false;
        }
        piece.setPosition(position);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePiece(final BoardPosition position) {
        this.getBoardStatus().removeAtPosition(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createStartingBoard() {
        this.stringBoard = this.fromBoard(this.board);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringBoard stringBoardFromNormal(final Board startingBoard) {
        return this.fromBoard(startingBoard);
    }

    private StringBoard fromBoard(final Board board) {
        return new StringBoardImpl(
                board.getPieces().stream()
                        .map(i -> this.getPieceStringCap(i) + "," + i.getPiecePosition().getX() + ","
                                + i.getPiecePosition().getY() + "/")
                        .collect(Collectors.joining()),
                board.getColumns(), board.getRows());
    }

    private String getPieceStringCap(final Piece piece) {
        final String letter = this.pieceTypeToLetter.get(piece.getType());
        return piece.getPlayer().getColor().equals(PlayerColor.WHITE) ? letter.toUpperCase(Locale.ITALIAN)
                : letter.toLowerCase(Locale.ITALIAN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StringBoard> getCreatedBoard() {
        return Optional.ofNullable(this.stringBoard);
    }

}
