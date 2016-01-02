package io.github.minecraftgui.models.network.packets;

import org.json.JSONObject;

/**
 * Created by Samuel on 2015-12-11.
 */
public abstract class PacketOut {

    public abstract JSONObject toJSON();

}
