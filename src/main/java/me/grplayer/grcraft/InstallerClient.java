package me.grplayer.grcraft;

import me.grplayer.grcraft.engine.ArgumentParser;
import me.grplayer.grcraft.engine.arguments.ApplicationSettings;
import me.grplayer.grcraft.gui.GuiManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * The installer client, entry point of the application
 *
 * @author Gersom
 * @version 1.0
 */
public class InstallerClient {

    private final static Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        ApplicationSettings applicationSettings = ArgumentParser.parseSettings(args);

        if (applicationSettings.enableLogFile()) {
            final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            final Configuration config = ctx.getConfiguration();
            Layout layout = PatternLayout.createLayout(PatternLayout.SIMPLE_CONVERSION_PATTERN, null, config, null,
                    null, true, true, null, null);
            Appender appender = FileAppender.createAppender("installer.log", "false", "false", "File", "true",
                    "false", "false", "4000", layout, null, "false", null, config);
            appender.start();
            ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addAppender(appender);
            LOGGER.info("Logging to file!");
        }

        try {
            LOGGER.info("Parsed settings, starting installer...");
            Installer installer = new Installer(applicationSettings);
            GuiManager guiManager = new GuiManager(installer);
            guiManager.start();
        } catch (Exception e) {
            LOGGER.fatal("Failed to install!", e);
            LOGGER.fatal("Exiting...");
        }
    }
}
