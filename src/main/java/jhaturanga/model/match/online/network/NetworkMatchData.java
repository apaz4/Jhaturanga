package jhaturanga.model.match.online.network;

import java.io.Serializable;

import jhaturanga.model.game.type.GameType;
import jhaturanga.model.player.Player;
import jhaturanga.model.timer.DefaultTimers;

/**
 * Data Transfer Object for an online match that will be shared from the creator
 * of the match to the user who join.
 */
public final class NetworkMatchData implements Serializable {

    private static final long serialVersionUID = -1356536529148341191L;

    private final GameType game;
    private final Player player;
    private final DefaultTimers timer;

    public NetworkMatchData(final GameType game, final DefaultTimers timer, final Player player) {
        this.game = game;
        this.timer = timer;
        this.player = player;
    }

    /**
     * Get the Game Type of the match.
     * 
     * @return the game type
     */
    public GameType getGameType() {
        return this.game;
    }

    /**
     * Get the player of the match.
     * 
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the timer of the match.
     * 
     * @return the timer
     */
    public DefaultTimers getTimer() {
        return this.timer;
    }

}
