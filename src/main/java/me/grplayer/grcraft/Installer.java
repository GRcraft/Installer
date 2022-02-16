package me.grplayer.grcraft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.grplayer.grcraft.engine.arguments.ApplicationSettings;
import me.grplayer.grcraft.engine.downloader.Downloader;
import me.grplayer.grcraft.engine.generator.VersionGenerator;
import me.grplayer.grcraft.engine.minecraft.MinecraftLauncher;
import me.grplayer.grcraft.engine.minecraft.MinecraftUtils;
import me.grplayer.grcraft.engine.minecraft.MinecraftValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * The installer implementation
 *
 * @author Gersom
 */
public class Installer extends Thread {

    private final static Logger LOGGER = LogManager.getLogger();

    private final static Gson GSON = new GsonBuilder().create();

    private final static String API_HOST = "https://api.github.com";
    private final static String VERSION_MANIFEST = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    private final static String REPOSITORY = "GRcraft/Client";
    private final static String PATCHES_DIR = "patches" + File.separator;

    private final ApplicationSettings settings;

    private boolean isLoading = false;
    private boolean isCompleted = false;

    private InstallerProgress progress = InstallerProgress.STARTING;
    private Release[] releases;
    private Release selectedRelease;

    public Installer(ApplicationSettings settings) {
        this.settings = settings;
    }


    /**
     * The installer main method
     */
    @Override
    public void start() {
        super.start();
        LOGGER.info("Installing...");
        LOGGER.debug("Creating download thread...");
        this.isLoading = true;
        Downloader downloader = new Downloader(API_HOST, REPOSITORY, GSON);
        new Thread(() -> {
            LOGGER.debug("Hello from download thread!");
            downloader.fetchVersions();

            this.isLoading = false;
            this.releases = downloader.listVersions();

            this.progress = InstallerProgress.VERSION_SELECTOR;

            while (this.selectedRelease == null) {
                // Wait for the user to select a release
                LOGGER.debug("Waiting for user to select a release...");
            }

            this.isLoading = true;

            File file = downloader.downloadVersion(this.selectedRelease);
            LOGGER.info("Downloaded latest version to " + file.getAbsolutePath());

            String minecraftVersion = this.selectedRelease.getVersion().split("-")[0];
            LOGGER.debug("Minecraft version: " + minecraftVersion);
            LOGGER.info("Making sure minecraft version is valid & exists...");
            this.progress = InstallerProgress.DOWNLOADING;
            new Thread(() -> {
                LOGGER.debug("Hello from minecraft validator thread!");
                MinecraftValidator validator = new MinecraftValidator(minecraftVersion, VERSION_MANIFEST, GSON);
                boolean installed = validator.isInstalled();
                boolean released = validator.isMojangRelease();
                LOGGER.debug("Minecraft version is " + (installed ? "installed" : "not installed") + " & " + (released ? "released" : "not released"));

                if (installed) {
                    if (released) {
                        LOGGER.info("Minecraft version is valid!");
                        LOGGER.info("version.json is ready to be generated!");
                        LOGGER.info("Generating version.json...");
                        new Thread(() -> {
                            this.progress = InstallerProgress.INSTALLING;
                            LOGGER.debug("Hello from generator thread!");
                            File versionDir = new File(new File(MinecraftUtils.locateMinecraftDir(), "versions"), minecraftVersion);
                            VersionGenerator generator = new VersionGenerator(file, versionDir, GSON);
                            File versionJSON = generator.generateVersionJSON();
                            LOGGER.debug("Generated version.json to " + versionJSON.getAbsolutePath());
                            LOGGER.info("New version.json is ready!");
                            new Thread(() -> {
                                LOGGER.debug("Hello from installer thread!");
                                LOGGER.info("Installing...");
                                MinecraftLauncher minecraftLauncher = new MinecraftLauncher(MinecraftUtils.locateMinecraftDir(), GSON);
                                minecraftLauncher.copyVersion(file, versionJSON);
                                LOGGER.info("Installed version " + minecraftVersion + "!");
                                LOGGER.info("Adding version to launcher profiles...");
                                minecraftLauncher.addVersionToLauncherProfile(MinecraftUtils.versionNameFromJar(file));
                                LOGGER.info("Added version to launcher profiles!");
                                LOGGER.info("Done!");
                                this.isLoading = false;
                                this.isCompleted = true;
                            }).start();
                        }).start();
                    }
                }
            }).start();
        }).start();
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public InstallerProgress getProgress() {
        return this.progress;
    }

    public void setSelectedRelease(Release selectedRelease) {
        this.selectedRelease = selectedRelease;
    }

    public Release[] getReleases() {
        return this.releases;
    }
}
