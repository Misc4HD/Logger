package io.papermc.logger;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Create logs folder if it doesn't exist
        File logsFolder = new File(getDataFolder(), "logs");
        if (!logsFolder.exists()) {
            logsFolder.mkdirs(); // Create the folder for player logs if it doesn't exist
        }

        // Create an instance of PlayerLogger
        PlayerLogger playerLogger = new PlayerLogger(this, logsFolder);

        // Register the event listener
        getServer().getPluginManager().registerEvents(playerLogger, this);

        // Log a message when the plugin is enabled
        getLogger().info("PlayerLogger plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        // Log a message when the plugin is disabled
        getLogger().info("PlayerLogger plugin has been disabled.");
    }
}
