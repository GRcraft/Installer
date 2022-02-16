package me.grplayer.grcraft.engine.arguments;

public class ApplicationSettings {

    private boolean logFile;
    private boolean gui;
    private String version;

    public ApplicationSettings(boolean logFile, boolean gui, String version) {
        this.logFile = logFile;
        this.gui = gui;
        this.version = version;
    }

    public boolean enableLogFile() {
        return logFile;
    }

    public boolean isGui() {
        return gui;
    }

    public String getVersion() {
        return version;
    }
}
