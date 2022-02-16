package me.grplayer.grcraft.engine.arguments;

public class ApplicationSettingsBuilder {
    private boolean logFile;
    private boolean gui;
    private String version;

    public ApplicationSettingsBuilder setLogFile(boolean logFile) {
        this.logFile = logFile;
        return this;
    }

    public ApplicationSettingsBuilder setGui(boolean gui) {
        this.gui = gui;
        return this;
    }

    public ApplicationSettingsBuilder setVersion(String version) {
        this.version = version;
        return this;
    }

    public ApplicationSettings createApplicationSettings() {
        return new ApplicationSettings(logFile, gui, version);
    }
}