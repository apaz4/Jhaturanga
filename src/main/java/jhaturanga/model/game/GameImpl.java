package jhaturanga.model.game;

import jhaturanga.model.game.controller.GameController;
import jhaturanga.model.game.type.GameType;
import jhaturanga.model.movement.manager.MovementManager;

public final class GameImpl implements Game {

    private final GameType type;
    private final GameController gameController;
    private final MovementManager movementManager;

    public GameImpl(final GameType type, final GameController gameController, final MovementManager movementManager) {
        this.type = type;
        this.gameController = gameController;
        this.movementManager = movementManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameController getController() {
        return this.gameController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MovementManager getMovementManager() {
        return this.movementManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameType getType() {
        return this.type;
    }

}
