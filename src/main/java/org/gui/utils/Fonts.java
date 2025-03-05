package org.gui.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.*;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class Fonts {

    private static Fonts instance = null;
    public Font font = null;

    private Fonts() {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "input", "fonts",
                "Akrobat-ExtraBold.ttf");
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(path.toString()));
            this.font = Font.createFont(Font.TRUETYPE_FONT, is);
            this.font = this.font.deriveFont(Font.PLAIN, 24);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

    }

    static {
        try {
            instance = new Fonts();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred in creating singleton instance");
        }
    }

    public static Fonts getInstance() {
        return instance;
    }
}