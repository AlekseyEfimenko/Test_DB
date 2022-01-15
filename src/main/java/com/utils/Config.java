package com.utils;

import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class Config {
    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());
    private static Config instance;
    private final Properties properties = new Properties();

    private Config() {}

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String getProperties(String key) {
        return System.getProperty(key);
    }

    public String getDataProperties(String key) {
        loadFile("data.properties");
        return properties.getProperty(key);
    }

    public String getRootPath() {
        String path = "";
        try {
            path = Paths.get(Objects.requireNonNull(getClass().getResource("/")).toURI()).getParent().toString();
        } catch (URISyntaxException ex) {
            LOGGER.error(ex.getMessage());
        }
        return path;
    }

    public String getSQLQuery(String src) {
        StringBuilder query = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getFile(src)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                query.append(line);
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return query.toString();
    }

    private void loadFile(String src) {
        try (FileInputStream fileInputStream = new FileInputStream(getFile(src))) {
            properties.load(fileInputStream);
        } catch (IOException ex) {
            LOGGER.error(String.format("File %1$s is not found%n%2$s", src, ex.getMessage()));
        }
    }

    private File getFile(String src) {
        File file = null;
        try {
            file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource(src)).toURI());
        } catch (URISyntaxException ex) {
            LOGGER.error(ex.getMessage());
        }
        return file;
    }
}

