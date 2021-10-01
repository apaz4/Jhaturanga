package jhaturanga.controllers.settings;

import java.io.IOException;

import jhaturanga.commons.settings.SettingMediator;
import jhaturanga.commons.settings.config.ApplicationStyleConfigStrategy;
import jhaturanga.commons.settings.config.PieceStyleConfigStrategy;
import jhaturanga.commons.settings.media.sound.Sound;
import jhaturanga.commons.settings.media.style.ApplicationStyle;
import jhaturanga.commons.settings.media.style.PieceStyle;
import jhaturanga.controllers.BasicController;

/**
 * Basic implementation for the SettingsController.
 */
public final class SettingsControllerImpl extends BasicController implements SettingsController {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationStyle(final ApplicationStyleConfigStrategy style) {
        try {
            SettingMediator.setAndSaveApplicationStyle(style);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationStyleConfigStrategy getCurrentApplicationStyle() {
        try {
            return SettingMediator.getSavedApplicatioStyle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApplicationStyle.getApplicationStyle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPieceStyle(final PieceStyleConfigStrategy style) {
        try {
            SettingMediator.setAndSavePieceStyle(style);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PieceStyleConfigStrategy getCurrentPieceStyle() {
        try {
            return SettingMediator.getSavedPieceStyle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PieceStyle.getPieceStyle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationVolume(final double volume) {
        try {
            SettingMediator.setAndSaveSoundVolume(volume);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getCurrentApplicationVolume() {
        try {
            return SettingMediator.getSavedSoundVolume();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Sound.getVolume();
    }

}
