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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.Message;

import java.util.UUID;

/**
 * Created by Martin on 23.05.2016.
 */
public class BukkitNetwork extends NetworkController implements PluginMessageListener, Listener {

    private final Plugin plugin;
    private final JSONParser parser;

    public BukkitNetwork( Plugin plugin ) {
        this.plugin = plugin;
        parser = new JSONParser();
        plugin.getServer().getMessenger().registerIncomingPluginChannel( plugin, NetworkController.MINECRAFT_GUI_CHANNEL, this );
        plugin.getServer().getPluginManager().registerEvents( this, plugin );
    }

    @EventHandler
    public void playerDisconnect( PlayerQuitEvent event ) {
        removeUserConnection( event.getPlayer().getUniqueId() );
    }

    @Override
    public void onPluginMessageReceived( String s, Player player, byte[] bytes ) {
        UserConnection userConnection = getUserConnection( player.getUniqueId() );
        JSONObject obj;
        try {
            obj = (JSONObject) parser.parse( new String( bytes ) );
        } catch ( ParseException e ) {
            e.printStackTrace();
            return;
        }

        if ( userConnection != null ) {
            userConnection.receivePacket( obj );
        } else {
            createUserConnection( player.getUniqueId() ).receivePacket( obj );
        }
    }

    @Override
    public void sendPacktTo( UUID uuid, JSONObject jsonObject ) {
        org.bukkit.Bukkit.getPlayer( uuid ).sendPluginMessage( plugin, NetworkController.MINECRAFT_GUI_CHANNEL, jsonObject.toString().getBytes() );
    }

    public static class Packet implements Message {

        private JSONObject jsonObject;

        public Packet( JSONObject jsonObject ) {
            this.jsonObject = jsonObject;
        }

        public Packet() {
        }

        @Override
        public void readFrom( ChannelBuf channelBuf ) {
            jsonObject = new JSONObject( new String( channelBuf.array() ).trim() );
        }

        @Override
        public void writeTo( ChannelBuf channelBuf ) {
            for ( byte b : jsonObject.toString().getBytes() ) {
                channelBuf.writeByte( b );
            }
        }
    }
}
