package me.grplayer.grcraft.engine.minecraft;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static me.grplayer.grcraft.utils.CommonUtils.stringFromStream;

/**
 * Validator class for Minecraft versions.
 *
 * @author Gersom
 */
public class MinecraftValidator {

    private final static Logger LOGGER = LogManager.getLogger();

    private final String versionToValidate;
    private final String versionManifest;
    private final Gson gson;

    /**
     * Constructor for the MinecraftValidator class.
     *
     * @param versionToValidate The version to validate.
     * @param versionManifest   The URL of the version manifest provided by Mojang.
     * @param gson              A Gson instance.
     */
    public MinecraftValidator(String versionToValidate, String versionManifest, Gson gson) {
        this.versionToValidate = versionToValidate;
        this.versionManifest = versionManifest;
        this.gson = gson;
    }

    /**
     * Checks if the version is installed on the system.
     *
     * @return true if the version is installed, false otherwise.
     */
    public boolean isInstalled() {
        return getMinecraftJar().exists();
    }

    /**
     * Returns the file where the version is installed. Does not check if the version is installed.
     *
     * @return the file where the version is installed.
     */
    public File getMinecraftJar() {
        return new File(new File(new File(MinecraftUtils.locateMinecraftDir(), "versions"), versionToValidate), versionToValidate + ".jar");
    }

    /**
     * Contacts the Mojang servers to check if the version is a valid release.
     *
     * @return true if the version is valid, false otherwise.
     */
    public boolean isMojangRelease() {
        try {
            URL versionManifestURL = new URL(versionManifest);
            URLConnection urlConnection = versionManifestURL.openConnection();
            urlConnection.connect();
            InputStream stream = urlConnection.getInputStream();

            String versionManifestString = stringFromStream(stream);
            JsonObject versionManifestJson = gson.fromJson(versionManifestString, JsonObject.class);
            JsonArray versions = versionManifestJson.getAsJsonArray("versions");

            for (int i = 0; i < versions.size(); i++) {
                JsonObject version = versions.get(i).getAsJsonObject();
                if (version.get("id").getAsString().equals(versionToValidate)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not validate Minecraft version", e);
        }

        return false;
    }

}
