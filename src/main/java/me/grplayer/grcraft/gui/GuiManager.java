package me.grplayer.grcraft.gui;

import me.grplayer.grcraft.Installer;
import me.grplayer.grcraft.InstallerProgress;
import me.grplayer.grcraft.Release;
import me.grplayer.grcraft.gui.frames.IntroFrame;
import me.grplayer.grcraft.gui.frames.LoadingFrame;
import me.grplayer.grcraft.gui.frames.VersionSelectorFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiManager extends Thread {

    private final static Logger LOGGER = LogManager.getLogger();

    private final Installer installer;

    private final IntroFrame introFrame;
    private final LoadingFrame loadingFrame;
    private VersionSelectorFrame versionSelectorFrame;

    public GuiManager(Installer installer) {
        this.installer = installer;
        this.introFrame = new IntroFrame(this);
        ;
        this.loadingFrame = new LoadingFrame();
    }

    @Override
    public void start() {
        LOGGER.info("Started GuiManager");
        this.introFrame.show();
        boolean completed = false;
        boolean hasShownLoadingFrame = false;
        while (!completed) {
            LOGGER.debug("Waiting for installer to complete");
            if (this.installer.isLoading()) {
                if (this.versionSelectorFrame != null) {
                    this.versionSelectorFrame.setVisible(false);
                }
                if (!hasShownLoadingFrame) {
                    this.loadingFrame.setVisible(true);
                    hasShownLoadingFrame = true;
                }
            } else {
                if (hasShownLoadingFrame) {
                    hasShownLoadingFrame = false;
                    this.loadingFrame.setVisible(false);
                }

                InstallerProgress progress = this.installer.getProgress();

                if (progress.equals(InstallerProgress.VERSION_SELECTOR)) {
                    if (this.versionSelectorFrame == null) {
                        this.versionSelectorFrame = new VersionSelectorFrame(this, this.installer.getReleases());
                        continue;
                    }
                    if (!this.versionSelectorFrame.isVisible()) {
                        this.versionSelectorFrame.setVisible(true);
                    }
                }
            }
            completed = this.installer.isCompleted();
        }
        LOGGER.info("Shutting down GuiManager");
        this.introFrame.dispose();
        this.loadingFrame.dispose();
        if (this.versionSelectorFrame != null) {
            this.versionSelectorFrame.dispose();
        }
        this.interrupt();
    }

    public void startInstallation() {
        this.installer.start();
    }

    public void submitRelease(Release release) {
        this.installer.setSelectedRelease(release);
    }
}
