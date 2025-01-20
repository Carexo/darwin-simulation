package util;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Configuration;

public class ConfigurationWriter {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String directoryPath = "src/main/resources/configurations/";
    private static Map<String, Configuration> configurations = new HashMap<>();


    public static void loadConfigurationsFromFile() throws IOException {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new FileNotFoundException("Configuration directory not found");
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) {
            throw new IOException("Failed to read configuration files");
        }

        for (File file : files) {
            try (InputStream inputStream = new FileInputStream(file)) {
                String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Configuration configuration = gson.fromJson(json, Configuration.class);
                configurations.put(file.getName().replace(".json", ""), configuration);
            } catch (IOException ex) {
                System.out.printf("Failed to read configuration file %s with error: %s\n", file.getName(), ex.getMessage());
            }
        }
    }

    public static void saveConfigurationToFiles() throws IOException {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (Map.Entry<String, Configuration> entry : configurations.entrySet()) {
            String fileName = entry.getKey() + ".json";
            File configurationFile = new File(directory, fileName);
            String json = gson.toJson(entry.getValue());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configurationFile))) {
                writer.write(json);
            } catch (IOException ex) {
                System.out.printf("Failed to write configuration file %s with error: %s\n", configurationFile.getPath(), ex.getMessage());
            }
        }
    }

    public static List<String> getConfigurationNames() {
        return new ArrayList<>(configurations.keySet());
    }

    public static Configuration getConfiguration(String name) {
        if (!configurations.containsKey(name)) {
            throw new IllegalArgumentException("Configuration with given name does not exist");
        }
        return configurations.get(name);
    }

    public static void addConfiguration(String name, Configuration configuration) {
        if (configurations.containsKey(name)) {
            throw new IllegalArgumentException("Configuration with given name already exists");
        }
        configurations.put(name, configuration);
    }

    public static void removeConfiguration(String name) {
        if (!configurations.containsKey(name)) {
            throw new IllegalArgumentException("Configuration with given name does not exist");
        }
        configurations.remove(name);
        File configurationFile = new File(directoryPath + name + ".json");
        if (configurationFile.exists()) {
            configurationFile.delete();
        }
    }
}
