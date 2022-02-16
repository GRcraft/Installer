package me.grplayer.grcraft.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class CommonUtils {

    /**
     * Reads the contents of a stream into a string.
     *
     * @param stream
     * @return The contents of the stream in a string.
     * @throws IOException
     */
    public static @NotNull String stringFromStream(InputStream stream) throws IOException {
        Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8.name());
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        return builder.toString();
    }

    /**
     * Reads a stream into base64
     *
     * @param stream The stream to read.
     * @return The contents of the stream in base64.
     * @throws IOException
     */
    public static String base64FromStream(@NotNull InputStream stream) throws IOException {
        byte[] bytes = new byte[(int) stream.available()];
        stream.read(bytes);
        stream.close();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Converts a byte array into a hex string.
     *
     * @param byteArray The byte array to convert.
     * @return The hex string.
     */
    @Contract(pure = true)
    public static String hexFromBytes(byte @NotNull [] byteArray) {
        String hex = "";

        // Iterating through each byte in the array
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }

        return hex;
    }

}
