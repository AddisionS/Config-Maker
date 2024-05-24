
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ConfigMaker {

    public static Logger logger;

    //declare configuration node for all the config files you wanna add
    public static ConfigurationNode config;

    /*method to create yml file along with folder inside of plugins folder
    * and copy the data from the yml in resources folder to the configuration node
    * while using this method you can put your plugin name in place of folder
    * and the name of yml file in your resource package
    * Example: config = createConfig("XYZplugin","config.yml")*/
    public ConfigurationNode createConfig(String folderName, String fileName) {
        Path pluginFolder = Paths.get("plugins", folderName);

        try {
            if (!Files.exists(pluginFolder)) {
                Files.createDirectories(pluginFolder);
                logger.info("Created plugin folder: " + pluginFolder);
            }

            Path configFile = pluginFolder.resolve(fileName);
            if (!Files.exists(configFile)) {
                Files.copy(getClass().getResourceAsStream("/" + fileName), configFile);
                logger.info("Created " + fileName + " from resources.");
            }

            ConfigurationLoader<?> loader = YamlConfigurationLoader.builder()
                    .path(configFile)
                    .build();

            return loader.load();
        } catch (IOException e) {
            logger.error("Failed to create or load configuration: " + fileName);
            e.printStackTrace();
            return null;
        }
    }

    /*method to copy the data of the configuration node into actual yml file
    * here you need to define the configuration node that holds the data and the path to the yml file
    * Example: saveConfig("config", Paths.get("XYZplugin","config.yml")) */

    public void saveConfig(ConfigurationNode config, Path configFile) {
        ConfigurationLoader<?> loader = YamlConfigurationLoader.builder()
                .path(configFile)
                .build();
        try {
            loader.save(config);
        } catch (IOException e) {
            logger.error("Failed to save configuration: " + configFile.getFileName());
            e.printStackTrace();
        }
    }

    /*method to obtain a list from the yml file */
    public static List<String> getListFromConfig(ConfigurationNode con, String nodePath) {
        List<String> resultList = new ArrayList<>();
        ConfigurationNode subNode = con.node(nodePath);
        if (subNode.isList()) {
            List<? extends ConfigurationNode> list = subNode.childrenList();
            for (ConfigurationNode item : list) {
                resultList.add(item.getString());
            }
        }
        return resultList;
    }

    /*method to get boolean value from yml file*/
    public static Boolean getBoolenFromConfig(ConfigurationNode con, String nodePath) {
        Boolean aBoolean = con.node(nodePath).getBoolean();
        return aBoolean;
    }

    /*method to get integer value from yml file*/
    public static Integer getIntegerFromConfig(ConfigurationNode con, String nodePath) {
        Integer integer = con.node(nodePath).getInt();
        return integer;
    }

    /*method to get string from yml file*/
    public static String getStringFromConfig(ConfigurationNode con, String nodePath) {
        String string = con.node(nodePath).getString();
        return string;
    }
}
