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

package io.github.minecraftgui.models.network.packets;

import io.github.minecraftgui.controllers.NetworkController;
import io.github.minecraftgui.models.components.List;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Samuel on 2016-01-16.
 */
public class PacketUpdateList extends PacketOut {

    private final List list;
    private final UUID updateAfterComponent;

    public PacketUpdateList(List list, UUID updateAfterComponent) {
        this.list = list;
        this.updateAfterComponent = updateAfterComponent;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject()
                .put(NetworkController.COMPONENT_ID, list.getUniqueId().toString())
                .put(NetworkController.AFTER_COMPONENT_ID, updateAfterComponent.toString());
    }

}
