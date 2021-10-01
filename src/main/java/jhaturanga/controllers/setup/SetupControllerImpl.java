package jhaturanga.controllers.setup;

import java.util.Optional;

import jhaturanga.controllers.BasicController;
import jhaturanga.model.game.type.GameType;
import jhaturanga.model.match.Match;
import jhaturanga.model.match.MatchImpl;
import jhaturanga.model.player.pair.PlayerPair;
import jhaturanga.model.timer.DefaultTimers;

/**
 * Basic implementation of SetupController.
 *
 */
public final class SetupControllerImpl extends BasicController implements SetupController {

    private GameType gameType;
    private DefaultTimers timer;
    private WhitePlayerChoice choice;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameType(final GameType gameType) {
        this.gameType = gameType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTimer(final DefaultTimers timer) {
        this.timer = timer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWhitePlayerChoice(final WhitePlayerChoice choice) {
        this.choice = choice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GameType> getSelectedGameType() {
        return Optional.ofNullable(this.gameType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DefaultTimers> getSelectedTimer() {
        return Optional.ofNullable(this.timer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<WhitePlayerChoice> getSelectedWhitePlayerChoice() {
        return Optional.ofNullable(this.choice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createMatch() {
        if (this.gameType == null || this.timer == null || this.choice == null) {
            return false;
        }
        final PlayerPair players = this.choice.getPlayers(this.getModel().getFirstUser().get(),
                this.getModel().getSecondUser().get());

        final Match match = new MatchImpl(this.gameType.getGameInstance(players), this.timer.getTimer(players));

        this.getModel().setMatch(match);
        return true;
    }

}
