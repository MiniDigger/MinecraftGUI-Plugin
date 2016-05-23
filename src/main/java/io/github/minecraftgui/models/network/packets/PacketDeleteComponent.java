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
import io.github.minecraftgui.models.components.Component;
import org.json.JSONObject;

/**
 * Created by Samuel on 2015-12-30.
 */
public class PacketDeleteComponent extends PacketOut {

    private final Component component;

    public PacketDeleteComponent( Component component ) {
        this.component = component;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject().put( NetworkController.COMPONENT_ID, component.getUniqueId().toString() );
    }

}
