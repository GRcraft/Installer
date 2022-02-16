package me.grplayer.grcraft.gui;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class TextureLoader {

    public static @NotNull String getTexture(String name) {
        String path = "assets/" + name + ".png";
        try {
            File file = File.createTempFile(name, ".png");
            file.delete();

            InputStream stream = TextureLoader.class.getClassLoader().getResourceAsStream(path);
            if (stream == null) {
                throw new RuntimeException("Texture not found: " + path);
            }
            FileUtils.copyInputStreamToFile(stream, file);

            file.deleteOnExit();

            return file.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Could not load texture " + name, e);
        }
    }

}
