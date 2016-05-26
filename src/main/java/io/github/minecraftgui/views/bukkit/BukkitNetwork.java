package io.github.minecraftgui.views.bukkit;

import io.github.minecraftgui.controllers.NetworkController;
import io.github.minecraftgui.models.network.UserConnection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Martin on 23.05.2016.
 */
public class BukkitNetwork extends NetworkController implements PluginMessageListener, Listener {

    private final Plugin plugin;

    public BukkitNetwork( Plugin plugin ) {
        this.plugin = plugin;
        plugin.getServer().getMessenger().registerIncomingPluginChannel( plugin, NetworkController.MINECRAFT_GUI_CHANNEL, this );
        plugin.getServer().getMessenger().registerOutgoingPluginChannel( plugin, NetworkController.MINECRAFT_GUI_CHANNEL );
        plugin.getServer().getPluginManager().registerEvents( this, plugin );
    }

    @EventHandler
    public void playerDisconnect( PlayerQuitEvent event ) {
        removeUserConnection( event.getPlayer().getUniqueId() );
    }

    @Override
    public void onPluginMessageReceived( String s, Player player, byte[] bytes ) {
        UserConnection userConnection = getUserConnection( player.getUniqueId() );
        JSONObject obj = new JSONObject( new String( bytes ).trim() );
        if ( userConnection != null ) {
            userConnection.receivePacket( obj );
        } else {
            createUserConnection( player.getUniqueId() ).receivePacket( obj );
        }
    }

    @Override
    public void sendPacktTo( UUID uuid, JSONObject jsonObject ) {
        // forge and its damm registries... we need to add a leading 0x0 byte so that forge can figure out the right packet class. Thanks md_5!
        byte[] temp = jsonObject.toString().getBytes();
        byte[] data = new byte[temp.length + 1];
        System.arraycopy( temp, 0, data, 1, temp.length );
        data[0] = 0;
        org.bukkit.Bukkit.getPlayer( uuid ).sendPluginMessage( plugin, NetworkController.MINECRAFT_GUI_CHANNEL, data );
    }
}
