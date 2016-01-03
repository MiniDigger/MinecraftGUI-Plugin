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

package io.github.minecraftgui.models.components;

import io.github.minecraftgui.models.network.UserConnection;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2015-12-29.
 */
public final class PluginGUI {

    private final UserConnection userConnection;
    private final ConcurrentHashMap<UUID, Component> components;
    private final ConcurrentHashMap<String, Component> componentsWithId;
    private final Root root;

    public PluginGUI(UserConnection userConnection) {
        this.userConnection = userConnection;
        this.components = new ConcurrentHashMap<>();
        this.componentsWithId = new ConcurrentHashMap<>();
        this.root = new Root(this, userConnection);
    }

    public UUID getPlayerUUID(){
        return userConnection.getUuid();
    }

    public Root getRoot() {
        return root;
    }

    public Component getComponent(String id){
        return componentsWithId.get(id);
    }

    protected final void addComponent(Component component){
        components.put(component.getUniqueId(), component);

        if(component.getId() != null)
            componentsWithId.put(component.getId(), component);
    }

    protected final void removeComponent(Component component){
        components.remove(component.getUniqueId());

        if(component.getId() != null)
            componentsWithId.remove(component.getId());
    }

}
