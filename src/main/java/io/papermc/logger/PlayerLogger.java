package io.papermc.logger;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerLogger implements Listener {

  private final File logsFolder;
  private final Map<UUID, String> lastKnownNames = new HashMap<>();

  public PlayerLogger(Main plugin, File logsFolder) {
    this.logsFolder = logsFolder;
  }

  @EventHandler
  public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
    UUID playerUUID = event.getPlayer().getUniqueId();
    String playerName = event.getPlayer().getName();
    String command = event.getMessage(); // The full command the player entered
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // Detect name changes and log the event
    if (!playerName.equals(lastKnownNames.get(playerUUID))) {
      logToPlayerFile(playerUUID, playerName, "[" + timestamp + "] Name changed to: " + playerName);
      lastKnownNames.put(playerUUID, playerName);
    }

    // Log the command to the player's file
    logToPlayerFile(playerUUID, playerName, "[" + timestamp + "] " + playerName + ": " + command);

    // Optionally, send a confirmation to the player
    // event.getPlayer().sendMessage(Component.text("Your command has been logged to your personal log file."));
  }

  private void logToPlayerFile(UUID playerUUID, String playerName, String message) {
    // Use a file name format with UUID and current name (e.g., "UUID_PlayerName.txt")
    String sanitizedPlayerName = playerName.replaceAll("[^a-zA-Z0-9_-]", "_"); // Sanitize to avoid illegal characters
    File playerLogFile = new File(logsFolder, playerUUID.toString() + "_" + sanitizedPlayerName + ".txt");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(playerLogFile, true))) {
      writer.write(message);
      writer.newLine();
    } catch (IOException e) {
      System.err.println("Failed to write to " + playerLogFile.getName() + ": " + e.getMessage());
    }
  }
}
