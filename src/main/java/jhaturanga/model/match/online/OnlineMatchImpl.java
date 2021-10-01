package jhaturanga.model.match.online;

import java.util.Optional;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.MqttException;

import jhaturanga.model.board.Board;
import jhaturanga.model.board.BoardPosition;
import jhaturanga.model.game.Game;
import jhaturanga.model.game.type.GameType;
import jhaturanga.model.history.History;
import jhaturanga.model.match.Match;
import jhaturanga.model.match.MatchEndType;
import jhaturanga.model.match.MatchImpl;
import jhaturanga.model.match.MatchStatus;
import jhaturanga.model.match.online.network.MqttNetworkMatchManager;
import jhaturanga.model.match.online.network.NetworkMatchData;
import jhaturanga.model.match.online.network.NetworkMatchManager;
import jhaturanga.model.movement.BasicMovement;
import jhaturanga.model.movement.MovementResult;
import jhaturanga.model.movement.PieceMovement;
import jhaturanga.model.movement.PieceMovementImpl;
import jhaturanga.model.piece.Piece;
import jhaturanga.model.player.Player;
import jhaturanga.model.player.PlayerColor;
import jhaturanga.model.player.PlayerImpl;
import jhaturanga.model.player.pair.PlayerPair;
import jhaturanga.model.player.pair.PlayerPairImpl;
import jhaturanga.model.timer.DefaultTimers;
import jhaturanga.model.timer.Timer;
import jhaturanga.model.user.User;

public final class OnlineMatchImpl implements OnlineMatch {

    private final NetworkMatchManager network;
    private String matchID;

    private final User localUser;
    private Player localPlayer;
    private Player otherPlayer;

    private NetworkMatchData data;

    private final Runnable onReady;
    private Runnable onResign;
    private MovementHandler onMovementHandler;

    private Match match;
    private final DefaultTimers timer = DefaultTimers.NO_LIMIT;

    /**
     * Setup a NetworkMatch.
     * 
     * @param user    - the user
     * @param onReady - the callback to call when the game is ready
     * @throws MqttException
     */
    public OnlineMatchImpl(final User user, final Runnable onReady) throws MqttException {
        this.localUser = user;
        this.onReady = onReady;
        this.network = new MqttNetworkMatchManager(this::onMovement, this::onResignHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnMovementHandler(final MovementHandler onMovementHandler) {
        this.onMovementHandler = onMovementHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exit() {
        this.network.disconnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void join(final String matchID) {
        // For now the player which join is the black player.
        this.localPlayer = new PlayerImpl(PlayerColor.BLACK, this.localUser);
        this.matchID = matchID;
        this.network.joinMatch(matchID, this.localPlayer, this::onDataReceived);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String create(final GameType gameType) {
        // For now the player which create is the white player.
        this.localPlayer = new PlayerImpl(PlayerColor.WHITE, this.localUser);
        this.data = new NetworkMatchData(gameType, this.timer, this.localPlayer);
        this.matchID = this.network.createMatch(this.data, this::onUserJoined);
        return this.matchID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWhitePlayer() {
        return Optional.ofNullable(this.localPlayer).map(x -> x.getColor().equals(PlayerColor.WHITE)).orElse(false);
    }

    private void onDataReceived() {
        this.data = this.network.getMatchData();
        this.otherPlayer = this.data.getPlayer();
        final PlayerPair players = new PlayerPairImpl(this.otherPlayer, this.localPlayer);
        this.match = new MatchImpl(this.data.getGameType().getGameInstance(players),
                this.data.getTimer().getTimer(players));

        Optional.ofNullable(this.onReady).ifPresent(Runnable::run);
    }

    private void onUserJoined() {
        this.otherPlayer = this.network.getJoinedPlayer();
        final PlayerPair players = new PlayerPairImpl(this.localPlayer, this.otherPlayer);
        this.match = new MatchImpl(this.data.getGameType().getGameInstance(players),
                this.data.getTimer().getTimer(players));
        Optional.ofNullable(this.onReady).ifPresent(Runnable::run);
    }

    private void onMovement(final BasicMovement movement) {
        final PieceMovement realMovement = new PieceMovementImpl(
                this.getBoard().getPieceAtPosition(movement.getOrigin()).get(), movement.getDestination());
        final MovementResult res = this.match.move(realMovement);
        if (!res.equals(MovementResult.INVALID_MOVE)) {
            Optional.ofNullable(this.onMovementHandler).ifPresent(x -> x.handleMovement(realMovement, res));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnResign(final Runnable onResign) {
        this.onResign = onResign;
    }

    private void onResignHandler() {
        this.match.resign(this.otherPlayer);
        Optional.ofNullable(this.onResign).ifPresent(Runnable::run);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MovementResult move(final PieceMovement movement) {
        final MovementResult res = this.match.move(movement);
        if (!res.equals(MovementResult.INVALID_MOVE)) {
            this.network.sendMove(movement);
        }
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMatchID() {
        return this.matchID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerPair getPlayers() {
        return this.match.getPlayers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game getGame() {
        return this.match.getGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timer getTimer() {
        return this.match.getTimer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public History getHistory() {
        return this.match.getHistory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Board getBoard() {
        return this.match.getBoard();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        this.match.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Player> getWinner() {
        return this.match.getWinner();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<BoardPosition> getPiecePossibleMoves(final Piece piece) {
        return this.match.getPiecePossibleMoves(piece);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MatchStatus getMatchStatus() {
        return this.match.getMatchStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MatchEndType> getEndType() {
        return this.match.getEndType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resign(final Player player) {
        this.network.sendResign();
        this.match.resign(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getLocalPlayer() {
        return this.localPlayer;
    }

}
