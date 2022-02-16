package me.grplayer.grcraft.engine.generator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.grplayer.grcraft.engine.minecraft.MinecraftUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Class used to generate version information for a G_Rcraft jar file.
 *
 * @author Gersom
 */
public class VersionGenerator {

    private final static Logger LOGGER = LogManager.getLogger();

    private final File jarFile;
    private final File versionDir;
    private final Gson gson;

    /**
     * Constructs a new VersionGenerator.
     *
     * @param jarFile    The G_Rcraft jar file.
     * @param versionDir The directory where the original version file is located.
     * @param gson       A gson instance.
     */
    public VersionGenerator(File jarFile, @NotNull File versionDir, Gson gson) {
        this.jarFile = jarFile;
        this.versionDir = versionDir;
        this.gson = gson;
    }

    /**
     * Generates the version information for a G_Rcraft jar file.
     *
     * @return A json file containing the version information.
     */
    public @Nullable File generateVersionJSON() {
        // This is the name we'll use in the version file.
        String name = MinecraftUtils.versionNameFromJar(this.jarFile);
        try {
            // Create the version file.
            File targetFile = File.createTempFile(name, ".json", this.versionDir);
            targetFile.deleteOnExit();

            // Read the original version file.
            File originalFile = new File(this.versionDir, this.versionDir.getName() + ".json");
            if (!originalFile.exists()) {
                throw new RuntimeException("Version file not found: " + originalFile.getAbsolutePath());
            }

            String original = FileUtils.readFileToString(originalFile, StandardCharsets.UTF_8);
            JsonObject versionInfo = gson.fromJson(original, JsonObject.class);
            LOGGER.info("Loaded version file: " + originalFile.getAbsolutePath());
            LOGGER.info("Replacing old version information.");

            versionInfo.remove("id");
            versionInfo.addProperty("id", name);
            versionInfo.remove("downloads");

            Files.write(targetFile.toPath(), gson.toJson(versionInfo).getBytes(StandardCharsets.UTF_8));
            return targetFile;
        } catch (IOException e) {
            throw new RuntimeException("Could not create version file.", e);
        }
    }

}
