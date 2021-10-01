package jhaturanga.model.game;

import jhaturanga.model.game.controller.GameController;
import jhaturanga.model.game.type.GameType;
import jhaturanga.model.movement.manager.MovementManager;

/**
 * A generic type of game.
 */
public interface Game {

    /**
     * Get the type of this Game.
     * 
     * @return the game type
     */
    GameType getType();

    /**
     * Get the game controller for this game type.
     * 
     * @return the game controller.
     */
    GameController getController();

    /**
     * Get the Game's specific MovementManager.
     * 
     * @return MovementManager is the GameType's specific movementManager
     */
    MovementManager getMovementManager();

    /**
     * @return the game builder
     */
    static GameBuilder builder() {
        return new GameBuilderImpl();
    }

}
