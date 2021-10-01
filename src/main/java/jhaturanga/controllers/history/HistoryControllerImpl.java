package jhaturanga.controllers.history;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jhaturanga.controllers.BasicController;
import jhaturanga.model.replay.ReplayData;
import jhaturanga.model.replay.SavedReplay;
import jhaturanga.model.replay.SavedReplayImpl;

/**
 * Basic implementation of the HistoryController.
 *
 */
public final class HistoryControllerImpl extends BasicController implements HistoryController {

    private final SavedReplay savedMatch = new SavedReplayImpl();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReplayData> getAllSavedReplaysOrdered() {
        return this.savedMatch.getAllBoards().stream().sorted(Comparator.comparing(ReplayData::getDate))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setReplay(final ReplayData boards) {
        this.getModel().setReplay(boards);
    }

}
