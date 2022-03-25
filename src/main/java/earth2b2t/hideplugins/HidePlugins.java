package earth2b2t.hideplugins;

import earth2b2t.i18n.BukkitI18n;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.logging.Level;

public final class HidePlugins extends JavaPlugin implements Listener {

    private BukkitI18n i18n;

    @Override
    public void onEnable() {
        i18n = BukkitI18n.get(this);
        i18n.setDefaultLanguage("ja_jp");
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTabComplete(TabCompleteEvent event) {
        if (event.isCancelled()) return;
        if (event.getSender().isOp()) return;
        if (!(matchHelp(event.getBuffer()) || matchPlugin(event.getBuffer()))) return;
        // さようならタブ
        event.setCompletions(Collections.emptyList());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        if (event.getPlayer().isOp()) return;

        if (matchHelp(event.getMessage())) {
            event.getPlayer().sendMessage(
                    ChatColor.DARK_GRAY + "==== " +
                            ChatColor.WHITE + "Earth" + ChatColor.BLUE + "2b2t " + ChatColor.GRAY + "Help" +
                            ChatColor.DARK_GRAY + " ===="
            );
            i18n.print(event.getPlayer(), "hide-plugins.help-message", ChatColor.GRAY);
            event.setCancelled(true);
        } else if (matchPlugin(event.getMessage())) {
            Bukkit.getLogger().log(Level.WARNING, String.format("Warning! Player %s has used the plugin command!", event.getPlayer().getName()));
            event.getPlayer().sendMessage("Plugins (1): " + ChatColor.RED + ChatColor.BOLD + "Hide Plugins XD" + ChatColor.RESET);
            event.setCancelled(true);
        }
    }

    private boolean matchPlugin(String c) {
        return c.contains("pl") || c.contains("plugins") || // /pl
                c.contains("bukkit:pl") || c.contains("bukkit:plugins"); // /bukkit:pl
    }

    private boolean matchHelp(String c) {
        return c.contains("help") || c.contains("bukkit:help") || // /help
                c.contains("?") || c.contains("bukkit:?") || // /?
                c.contains("about") || c.contains("bukkit:about") || // /about
                c.length() <= 1;
    }
}
