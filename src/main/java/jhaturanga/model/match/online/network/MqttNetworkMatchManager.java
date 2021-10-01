package jhaturanga.model.match.online.network;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.MqttException;

import jhaturanga.commons.ObjectSerializer;
import jhaturanga.model.movement.BasicMovement;
import jhaturanga.model.movement.BasicMovementImpl;
import jhaturanga.model.movement.PieceMovement;
import jhaturanga.model.player.Player;

public final class MqttNetworkMatchManager implements NetworkMatchManager {

    private static final int MATCH_ID_LENGTH = 3;
    private static final String GAME_CHANNEL_BASE = "jhaturanga/game/";

    private final MqttNetworkInstance network;
    private final Consumer<BasicMovement> onMovement;
    private final Runnable onResign;
    private Runnable onReady;

    private String gameUrl = "";

    private NetworkMatchData matchData;
    private Player joinedPlayer;

    public MqttNetworkMatchManager(final Consumer<BasicMovement> onMovement, final Runnable onResign)
            throws MqttException {
        this.network = new MosquittoMqttNetworkInstance();
        this.network.connect();
        this.onMovement = onMovement;
        this.onResign = onResign;
        this.network.setOnReceive(this::onMessage);
    }

    private void handleJoinMessage(final NetworkMessage message) {
        /**
         * A user joined to the game that I created.
         * 
         * I send him this game settings.
         */
        System.out.println("A user joined.");
        try {

            // Load the player
            this.joinedPlayer = (Player) ObjectSerializer.fromString(message.getContent());
            this.sendGameDataToJoinedUser();

            // Now I'm ready to start so i call onReady callback
            Optional.ofNullable(this.onReady).ifPresent(Runnable::run);

        } catch (ClassNotFoundException | IOException e1) {
            e1.printStackTrace();
        }
    }

    private void handleDataMessage(final NetworkMessage message) {
        /**
         * A user send me the data of the game, so i'm the player who joined.
         */
        System.out.println("The host send the game data.");
        try {

            // Load the match data
            this.matchData = (NetworkMatchData) ObjectSerializer.fromString(message.getContent());

            // Now I'm ready to start so i call onReady callback
            Optional.ofNullable(this.onReady).ifPresent(Runnable::run);

        } catch (ClassNotFoundException | IOException e1) {
            e1.printStackTrace();
        }
    }

    private void handleMoveMessage(final NetworkMessage message) {

        /**
         * The other player make a move.
         */
        System.out.println("A player sent a move.");
        try {

            // Call the callback of the onMovement
            this.onMovement.accept((BasicMovement) ObjectSerializer.fromString(message.getContent()));

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void handleResignMessage() {
        System.out.println("A player resign.");
        this.onResign.run();
    }

    private void onMessage(final NetworkMessage message) {
        switch (message.getMessageType()) {

        case JOIN:
            this.handleJoinMessage(message);
            break;
        case DATA:
            this.handleDataMessage(message);
            break;
        case MOVE:
            this.handleMoveMessage(message);
            break;
        case RESIGN:
            this.handleResignMessage();
            break;
        default:
            break;
        }

    }

    private void sendGameDataToJoinedUser() {
        try {
            /**
             * Send the game data to the player who joined.
             */
            this.network.sendData(this.gameUrl, ObjectSerializer.toString(this.matchData));
        } catch (MqttException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createMatch(final NetworkMatchData data, final Runnable onReady) {
        this.matchData = data;
        final String matchId = this.idGenerator(MATCH_ID_LENGTH);
        this.joinMatch(matchId, data.getPlayer(), onReady);
        return matchId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void joinMatch(final String matchId, final Player player, final Runnable onReady) {

        // Setup the callback
        this.onReady = onReady;

        this.gameUrl = GAME_CHANNEL_BASE + matchId;

        try {

            /**
             * Send a join message and subscribe.
             */
            this.network.sendJoin(this.gameUrl, ObjectSerializer.toString(player));
            this.network.subscribe(this.gameUrl);

        } catch (final MqttException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMove(final PieceMovement move) {
        try {
            final BasicMovement movement = new BasicMovementImpl(move.getOrigin(), move.getDestination());
            this.network.sendMove(this.gameUrl, ObjectSerializer.toString(movement));

        } catch (final MqttException | IOException e) {
            e.printStackTrace();
        }
    }

    private String idGenerator(final int length) {
        final String digits = "1234567890";
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        final Random rand = new Random();
        while (i < length) {
            sb.append(digits.charAt(rand.nextInt(digits.length())));
            i++;
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkMatchData getMatchData() {
        return this.matchData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getJoinedPlayer() {
        return this.joinedPlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        try {
            this.network.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendResign() {
        try {
            this.network.sendResign(this.gameUrl);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
