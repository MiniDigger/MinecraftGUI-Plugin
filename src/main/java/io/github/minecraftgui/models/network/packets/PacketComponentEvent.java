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
import io.github.minecraftgui.models.components.ComponentValuable;
import io.github.minecraftgui.models.network.UserConnection;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Samuel on 2016-01-03.
 */
public abstract class PacketComponentEvent extends PacketIn {

    protected final Component component;

    public PacketComponentEvent(UserConnection userConnection, JSONObject jsonObject) {
        super(jsonObject);
        this.component = userConnection.getComponent(UUID.fromString(jsonObject.getString(NetworkController.COMPONENT_ID)));
    }

    public Component getComponent() {
        return component;
    }

    public static class OnBlur extends PacketComponentEvent {

        public OnBlur(UserConnection userConnection, JSONObject jsonObject) {
            super(userConnection, jsonObject);

            component.onBlur();
        }
    }

    public static class OnFocus extends PacketComponentEvent {

        public OnFocus(UserConnection userConnection, JSONObject jsonObject) {
            super(userConnection, jsonObject);

            component.onFocus();
        }
    }

    public static class OnClick extends PacketComponentEvent {

        public OnClick(UserConnection userConnection, JSONObject jsonObject) {
            super(userConnection, jsonObject);

            component.onClick();
        }
    }

    public static class OnDoubleClick extends PacketComponentEvent {

        public OnDoubleClick(UserConnection userConnection, JSONObject jsonObject) {
            super(userConnection, jsonObject);

            component.onDoubleClick();
        }
    }

    public static class OnInput extends PacketComponentEvent {

        public OnInput(UserConnection userConnection, JSONObject jsonObject) {
            super(userConnection, jsonObject);

            component.onInput(jsonObject.getString(NetworkController.INPUT).charAt(0));
        }
    }

    public static class OnKeyPressed extends PacketComponentEvent {

        public OnKeyPressed(UserConnection userConnection, JSONObject jsonObject) {
            super(userConnection, jsonObject);

            component.onKeyPressed(jsonObject.getInt(NetworkController.KEY));
        }
    }

    public static class OnRemove extends PacketComponentEvent {

        public OnRemove(UserConnection userConnection, JSONObject jsonObject) {
            super(userConnection, jsonObject);

            component.onRemove();
        }
    }

    public static class OnMouseEnter extends PacketComponentEvent {

        public OnMouseEnter(UserConnection userConnection, JSONObject jsonObject) {
            super(userConnection, jsonObject);

            component.onMouseEnter();
        }
    }

    public static class OnMouseLeave extends PacketComponentEvent {

        public OnMouseLeave(UserConnection userConnection, JSONObject jsonObject) {
            super(userConnection, jsonObject);

            component.onMouseLeave();
        }
    }

    public static class OnValueChange extends PacketComponentEvent {

        public OnValueChange(UserConnection userConnection, JSONObject jsonObject) {
            super(userConnection, jsonObject);

            if(component instanceof ComponentValuable)
                ((ComponentValuable) component).setValue(jsonObject.get(NetworkController.VALUE));
        }
    }

}
