/*
 *
 *  *     Minecraft GUI Server
 *  *     Copyright (C) 2015  Samuel Marchildon-Lavoie
 *  *
 *  *     This program is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     This program is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU General Public License
 *  *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.github.minecraftgui.views.sponge;

import io.github.minecraftgui.controllers.NetworkController;
import io.github.minecraftgui.models.network.UserConnection;
import org.json.JSONObject;
import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.network.*;

import java.util.UUID;

/**
 * Created by Samuel on 2015-12-02.
 */
public class SpongeNetwork extends NetworkController implements MessageHandler<SpongeNetwork.Packet> {

    private final Game game;
    private final ChannelBinding.IndexedMessageChannel indexedMessageChannel;

    public SpongeNetwork(Object plugin, Game game) {
        this.game = game;
        indexedMessageChannel = game.getChannelRegistrar().createChannel(plugin, NetworkController.MINECRAFT_GUI_CHANNEL);
        indexedMessageChannel.registerMessage(SpongeNetwork.Packet.class, 0, this);
        game.getEventManager().registerListeners(plugin, this);
    }

    @Listener
    public void playerDisconnect(ClientConnectionEvent.Disconnect event){
        removeUserConnection(event.getTargetEntity().getUniqueId());
    }

    @Override
    public void handleMessage(Packet packet, RemoteConnection remoteConnection, Platform.Type type) {
        if(remoteConnection instanceof PlayerConnection) {
            UUID uuid = ((PlayerConnection) remoteConnection).getPlayer().getProfile().getUniqueId();
            UserConnection userConnection = getUserConnection(uuid);

            if(userConnection != null)
                userConnection.receivePacket(packet.jsonObject);
            else
                createUserConnection(uuid).receivePacket(packet.jsonObject);
        }
    }

    @Override
    public void sendPacktTo(UUID uuid, JSONObject jsonObject) {
        indexedMessageChannel.sendTo(game.getServer().getPlayer(uuid).get(), new Packet(jsonObject));
    }

    public static class Packet implements Message {

        private JSONObject jsonObject;

        public Packet(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public Packet() {
        }

        @Override
        public void readFrom(ChannelBuf channelBuf) {
            jsonObject = new JSONObject(new String(channelBuf.array()).trim());
        }

        @Override
        public void writeTo(ChannelBuf channelBuf) {
            for(byte b : jsonObject.toString().getBytes())
                channelBuf.writeByte(b);
        }

    }

}
