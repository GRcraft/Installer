package me.grplayer.grcraft.engine.downloader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import me.grplayer.grcraft.Release;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static me.grplayer.grcraft.utils.CommonUtils.stringFromStream;

/**
 * The downloader class is used to fetch the releases from the GitHub API, and to download the jar files.
 * The class is used by the {@link me.grplayer.grcraft.Installer} class.
 *
 * @author Gersom
 */
public class Downloader {

    private final static Logger LOGGER = LogManager.getLogger();

    private final Gson gson;
    private final String apiHost;
    private final String repository;
    private Release[] versions;

    /**
     * Constructor of the Downloader class.
     *
     * @param apiHost    The host of the GitHub API.
     * @param repository The name of the GitHub repository. (ex. GRcraft/Client)
     * @param gson       A Gson instance.
     */
    public Downloader(String apiHost, String repository, Gson gson) {
        this.apiHost = apiHost;
        this.repository = repository;
        this.gson = gson;
    }

    /**
     * This method is used to fetch all the releases from the GitHub API.
     */
    public void fetchVersions() {
        LOGGER.info("Downloader started");
        LOGGER.debug("Using api host: " + apiHost);
        LOGGER.info("Fetching versions...");
        Release[] versions = getVersions();
        LOGGER.info("Fetched {} versions.", versions.length);

        for (Release version : versions) {
            LOGGER.debug("Download url of {}: {}", version.getVersion(), version.getDownload());
        }

        this.versions = versions;
    }

    /**
     * Internal method used to fetch the releases from the GitHub API.
     *
     * @return All fetched releases.
     */
    private Release @NotNull [] getVersions() {
        try {
            URL url = new URL(apiHost + "/repos/" + repository + "/releases");
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();
            String rawJSON = stringFromStream(stream);
            JsonArray releases = gson.fromJson(rawJSON, JsonArray.class);

            Release[] versions = new Release[releases.size()];
            for (int i = 0; i < releases.size(); i++) {
                String version = releases.get(i).getAsJsonObject().get("tag_name").getAsString();
                String description = releases.get(i).getAsJsonObject().get("body").getAsString();
                JsonArray assets = releases.get(i).getAsJsonObject().get("assets").getAsJsonArray();

                String download = "";
                String[] hashes = new String[2];
                for (int j = 0; j < assets.size(); j++) {
                    String download_url = assets.get(j).getAsJsonObject().get("browser_download_url").getAsString();
                    String name = assets.get(j).getAsJsonObject().get("name").getAsString();
                    if (name.endsWith(".jar")) {
                        download = download_url;
                    } else if (name.endsWith(".sha1")) {
                        // Get the hash from the sha1 file
                        URL hash_url = new URL(download_url);
                        InputStream hash_stream = hash_url.openConnection().getInputStream();
                        hashes[0] = stringFromStream(hash_stream);
                    } else if (name.endsWith(".md5")) {
                        // Get the hash from the md5 file
                        URL hash_url = new URL(download_url);
                        InputStream hash_stream = hash_url.openConnection().getInputStream();
                        hashes[1] = stringFromStream(hash_stream);
                    }
                }

                versions[i] = new Release(version, description, download, hashes);
            }

            return versions;
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to: " + apiHost, e);
        }
    }

    /**
     * This method is used to download the jar file from the GitHub API.
     *
     * @param release The release to download.
     * @return The downloaded jar file.
     */
    public File downloadVersion(@NotNull Release release) {
        LOGGER.info("Downloading version: {}", release.getVersion());
        try {
            return download(release);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download: " + release.getVersion(), e);
        }
    }

    /**
     * Internal method used to download the jar file from the GitHub API.
     *
     * @param version The release to download.
     * @return The downloaded jar file.
     * @throws IOException
     */
    private @NotNull File download(@NotNull Release version) throws IOException {
        // Get the system's temp directory
        File tempFile = File.createTempFile("tmp", ".tmp");
        File tempDir = new File(tempFile.getParent());
        tempFile.delete();

        File destFile = new File(tempDir, version.getVersion() + ".jar");
        destFile.deleteOnExit();
        LOGGER.info("Downloading to: {}", destFile.getAbsolutePath());

        URL url = new URL(version.getDownload());
        FileUtils.copyURLToFile(url, destFile);

        if (version.isValid(destFile)) {
            return destFile;
        } else {
            throw new IOException("Downloaded file did not match the hashes!");
        }
    }

    /**
     * This method is used to get all the fetched releases.
     *
     * @return
     */
    public Release[] listVersions() {
        return this.versions;
    }
}
