package io.github.minecraftgui.views.bukkit;

import io.github.minecraftgui.controllers.NetworkController;
import io.github.minecraftgui.models.network.UserConnection;
import io.github.minecraftgui.views.sponge.SpongeNetwork;
import org.json.JSONObject;
import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.Message;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.network.RemoteConnection;

import java.util.UUID;

/**
 * Created by Martin on 23.05.2016.
 */
public class BukkitNetwork extends NetworkController {

    private final Game game;
    private final ChannelBinding.IndexedMessageChannel indexedMessageChannel;

    public BukkitNetwork( Object plugin, Game game ) {
        this.game = game;
        indexedMessageChannel = game.getChannelRegistrar().createChannel( plugin, NetworkController.MINECRAFT_GUI_CHANNEL );
        indexedMessageChannel.registerMessage( BukkitNetwork.Packet.class, 0, this );
        game.getEventManager().registerListeners( plugin, this );
    }

    @Listener
    public void playerDisconnect( ClientConnectionEvent.Disconnect event ) {
        removeUserConnection( event.getTargetEntity().getUniqueId() );
    }

    @Override
    public void handleMessage( Packet packet, RemoteConnection remoteConnection, Platform.Type type ) {
        if ( remoteConnection instanceof PlayerConnection ) {
            UUID uuid = ( (PlayerConnection) remoteConnection ).getPlayer().getProfile().getUniqueId();
            UserConnection userConnection = getUserConnection( uuid );

            if ( userConnection != null ) {
                userConnection.receivePacket( packet.jsonObject );
            } else {
                createUserConnection( uuid ).receivePacket( packet.jsonObject );
            }
        }
    }

    @Override
    public void sendPacktTo( UUID uuid, JSONObject jsonObject ) {
        indexedMessageChannel.sendTo( game.getServer().getPlayer( uuid ).get(), new Packet( jsonObject ) );
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
