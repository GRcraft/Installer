package me.grplayer.grcraft;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static me.grplayer.grcraft.utils.CommonUtils.hexFromBytes;

/**
 * The Release class is used to store the release information.
 * Generated by the {@link me.grplayer.grcraft.engine.downloader.Downloader} class.
 *
 * @author Gersom
 */
public class Release {

    private final String version;
    private final String description;
    private final String download;
    private final String sha1;
    private final String md5;

    @Contract(pure = true)
    public Release(String version, String description, String download, String @NotNull [] hashes) {
        this.version = version;
        this.description = description;
        this.download = download;
        this.sha1 = hashes[0].toLowerCase();
        this.md5 = hashes[1].toLowerCase();
    }

    public String getVersion() {
        return this.version;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDownload() {
        return this.download;
    }

    public boolean isValid(File downloadedJar) throws IOException {
        byte[] fileData = FileUtils.readFileToByteArray(downloadedJar);
        try {
            MessageDigest md_sha1 = MessageDigest.getInstance("SHA-1");
            MessageDigest md_md5 = MessageDigest.getInstance("MD5");
            byte[] sha1 = md_sha1.digest(fileData);
            byte[] md5 = md_md5.digest(fileData);

            return this.sha1.equals(hexFromBytes(sha1).toLowerCase()) && this.md5.equals(hexFromBytes(md5).toLowerCase());
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("SHA-1 algorithm not found");
        }
    }
}
