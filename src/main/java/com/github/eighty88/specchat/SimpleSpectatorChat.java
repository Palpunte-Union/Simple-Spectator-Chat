package com.github.eighty88.specchat;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.japanize.JapanizeType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public final class SimpleSpectatorChat extends JavaPlugin implements Listener {
    private boolean lunaChatEnable = true;

    String specPattern = ChatColor.GRAY + "[霊界チャット] %Name%: %Message% %Sub%";
    String NormalPattern = "%Name%" + ChatColor.GREEN + ": " + ChatColor.WHITE + "%Message%" + "%Sub%";

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        Log(Arrays.asList(
                "|=================================",
                "|  Loading Simple Spectator Chat  ",
                "|                                 ",
                "|  By eight_y_88                  ",
                "|  Version: " + this.getDescription().getVersion(),
                "|  Website:                       ",
                "|   " + this.getDescription().getWebsite(),
                "|  For 1.13+                      ",
                "|                                 ",
                "|==========================="
        ));

        Plugin lc = Bukkit.getPluginManager().getPlugin("LunaChat");

        if(lc != null) {
            lunaChatEnable = true;
        } else {
            Log(Arrays.asList(
                    "LunaChat プラグインがロードされていません。",
                    "LunaChat プラグインをロードすることで、",
                    "スペクテイター中のチャットでも日本語変換を利用できます。"
            ));
        }
    }

    public void Log(List<String> list) {
        for(String str: list) {
            getLogger().info(str);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String str1 = e.getMessage();
        byte[] buf1 = new byte[0];
        try {
            buf1 = str1.getBytes("SJIS");
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        }
        boolean bytes = str1.length() == buf1.length;
        String message;
        if (e.getMessage().startsWith("#")) {
            message = NormalPattern.replace("%Name%", e.getPlayer().getName()).replace("%Message%", e.getMessage()).replace("%Sub%", "");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(message);
                getLogger().info(message);
            }
        } else if(lunaChatEnable) {
            if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                if(bytes) {
                    message = specPattern.replace("%Name%", e.getPlayer().getName()).replace("%Message%", LunaChat.getAPI().japanize(e.getMessage(), JapanizeType.GOOGLE_IME)).replace("%Sub%", e.getMessage());
                } else {
                    message = specPattern.replace("%Name%", e.getPlayer().getName()).replace("%Message%", e.getMessage()).replace("%Sub%", "");
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode() == GameMode.SPECTATOR) {
                        player.sendMessage(message);
                        getLogger().info(message);
                    }
                }
            } else {
                if(bytes) {
                    message = NormalPattern.replace("%Name%", e.getPlayer().getName()).replace("%Message%", LunaChat.getAPI().japanize(e.getMessage(), JapanizeType.GOOGLE_IME)).replace("%Sub%", ChatColor.GRAY + " (" + e.getMessage() + ")");
                } else {
                    message = NormalPattern.replace("%Name%", e.getPlayer().getName()).replace("%Message%", e.getMessage()).replace("%Sub%", "");
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(message);
                    getLogger().info(message);
                }
            }
        } else {
            if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                message = specPattern.replace("%Name%", e.getPlayer().getName()).replace("%Message%", e.getMessage()).replace("%Sub%", "");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode() == GameMode.SPECTATOR) {
                        player.sendMessage(message);
                        getLogger().info(message);
                    }
                }
            } else {
                message = NormalPattern.replace("%Name%", e.getPlayer().getName()).replace("%Message%", e.getMessage()).replace("%Sub%", "");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(message);
                    getLogger().info(message);
                }
            }
        }
        e.setCancelled(true);
    }
}
