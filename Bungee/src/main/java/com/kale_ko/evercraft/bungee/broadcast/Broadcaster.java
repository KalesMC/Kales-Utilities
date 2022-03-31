package com.kale_ko.evercraft.bungee.broadcast;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.shared.util.Closable;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class Broadcaster implements Closable {
    private ScheduledTask task;

    public Broadcaster() {
        Random random = new Random();

        task = BungeeMain.getInstance().getProxy().getScheduler().schedule(BungeeMain.getInstance(), new Runnable() {
            public void run() {
                for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                    player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginConfig().getStringList("broadcaster.messages").get(random.nextInt(BungeeMain.getInstance().getPluginConfig().getStringList("broadcaster.messages").size())))));
                }
            }
        }, 0, BungeeMain.getInstance().getPluginConfig().getInteger("broadcaster.intervail"), TimeUnit.SECONDS);
    }

    public void close() {
        task.cancel();
    }
}