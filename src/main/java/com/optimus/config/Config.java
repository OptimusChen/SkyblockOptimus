package com.optimus.config;

import com.optimus.util.Util;
import lombok.Getter;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Getter
public class Config {

    private static final String FILE_NAME = "skyblockoptimus.json";
    private JSONObject json;
    private final File file;

    public Config() {
        this.file = new File(Util.getMinecraftDir().getPath() + "/config/" + FILE_NAME);
        if (file.exists()) this.json = new JSONObject(file);
    }

    public boolean getBoolean(String path) {
        return (boolean) get(path);
    }

    public float getFloat(String path) {
        return (float) get(path);
    }

    public int getInt(String path) {
        return (int) get(path);
    }

    public double getDouble(String path) {
        return (double) get(path);
    }

    public String getString(String path) {
        return (String) get(path);
    }

    public Object get(String path) {
        return json.get(path);
    }

    public void set(String path, Object value) {
        try {
            json.put(path, value);
            saveToDisk();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToDisk() throws IOException {
        FileWriter writer = new FileWriter(file, false);
        writer.write(json.toString());
        writer.close();
    }

    public void initialize() {
        if (!file.exists()) return;

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.json = new JSONObject(file);
    }
}
