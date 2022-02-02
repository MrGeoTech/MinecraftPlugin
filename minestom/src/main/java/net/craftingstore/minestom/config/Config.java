package net.craftingstore.minestom.config;

import net.minestom.server.extensions.Extension;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

public class Config {

    private Extension extension;
    private Properties configuration;
    private File file;

    public Config(Extension extension, File file) {
        try {
            this.file = file;
            this.extension = extension;

            file.getParentFile().mkdirs();

            if (!file.exists()) {
                InputStream is = extension.getResource(file.toPath());
                extension.getLogger().info("Copying default config file.");
                try {
                    assert is != null;
                    Files.copy(is, file.toPath());
                } catch (IOException e) {
                    extension.getLogger().error("An error occurred while copying the default config file " + file.getName() + ".", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            extension.terminate();
        }

        reload();
    }

    public void reload() {
        try {
            configuration = new Properties();
            configuration.load(new FileInputStream(file));
        } catch (IOException e) {
            extension.getLogger().error("An error occurred while loading the config file " + file.getName() + ".", e);
        }
    }

    public void setToken(String token) {
        if (configuration == null) {
            reload();
        }
        configuration.setProperty("api-key", token);
    }

    public String getToken() {
        if (configuration == null) {
            reload();
        }
        return configuration.getProperty("api-key");
    }

    public void setDebug(boolean debug) {
        if (configuration == null) {
            reload();
        }
        configuration.setProperty("debug", String.valueOf(debug));
    }

    public boolean getDebug() {
        if (configuration == null) {
            reload();
        }
        return Boolean.getBoolean(configuration.getProperty("debug"));
    }

    public void saveConfig() {
        try {
            configuration.store(new FileOutputStream(file), "");
        } catch (IOException e) {
            extension.getLogger().error("An error occurred while saving the config file " + file.getName() + ".", e);
        }
    }

}
