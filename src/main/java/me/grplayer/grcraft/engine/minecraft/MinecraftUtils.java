package me.grplayer.grcraft.engine.minecraft;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Minecraft utilities
 *
 * @author Gersom
 */
public class MinecraftUtils {

    /**
     * Get the minecraft directory, based on the OS. Does not check if the directory exists.
     *
     * @return the minecraft directory
     */
    public static @Nullable File locateMinecraftDir() {
        switch (System.getProperty("os.name")) {
            case "Linux":
                return new File(System.getProperty("user.home") + "/.minecraft");
            case "Windows":
                return new File(System.getenv("APPDATA") + "\\.minecraft");
            case "Mac OS X":
                return new File(System.getProperty("user.home") + "/Library/Application Support/minecraft");
            default:
                return null;
        }
    }

    /**
     * Gets the version name from a jar file.
     *
     * @param jarFile The jar file to get the version name from.
     * @return The version name of the jar file.
     */
    public static @NotNull String versionNameFromJar(@NotNull File jarFile) {
        return "G_Rcraft_" + jarFile.getName().replace(".jar", "");
    }

}
