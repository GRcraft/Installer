package me.grplayer.grcraft.engine;

import me.grplayer.grcraft.engine.arguments.ApplicationSettings;
import me.grplayer.grcraft.engine.arguments.ApplicationSettingsBuilder;
import org.jetbrains.annotations.NotNull;

public class ArgumentParser {

    public static ApplicationSettings parseSettings(String @NotNull [] args) {
        ApplicationSettingsBuilder builder = new ApplicationSettingsBuilder();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-l")) {
                builder.setLogFile(Boolean.parseBoolean(args[i + 1]));
            }
            if (args[i].equals("-v")) {
                builder.setVersion(args[i + 1]);
            }
            if (args[i].equals("-g")) {
                builder.setGui(Boolean.parseBoolean(args[i + 1]));
            }
        }

        return builder.createApplicationSettings();
    }

}
