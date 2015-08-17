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

package djxy.api;

import djxy.models.ComponentManager;
import djxy.models.component.Attributes;
import djxy.models.component.Component;
import djxy.models.resource.Resource;

import java.util.ArrayList;

public abstract class MinecraftGuiAPI {

    /**
     * You need to use this method to register your ComponentManager.
     *
     * @param manager The ComponentManager you are using to receive the events from MinecraftGUI.
     */
    public abstract void registerComponentManager(ComponentManager manager, boolean playerNeedAuthentication, ArrayList<Resource> resourceToDownload);

    /**
     * You need to use this method to register your ComponentManager.
     *
     * @param manager The ComponentManager you are using to receive the events from MinecraftGUI.
     */
    public abstract void registerComponentManager(ComponentManager manager, boolean playerNeedAuthentication);

    public abstract void listenButton(ComponentManager manager, String buttonId);

    public abstract void createTimerRemover(String playerUUID, String componentIdToRemove, int second);

    public abstract void callButtonEvent(String playerUUID, String buttonId);

    /**
     * This method will create one component on the screen of the player
     *
     * @param playerUUID The player to send the component
     * @param component The component to create
     */
    public abstract void createComponent(String playerUUID, Component component);

    /**
     * This method will change properties of one component on the screen of the player
     *
     * @param playerUUID The player to send the update
     * @param attributes The attributes to send
     */
    public abstract void updateComponent(String playerUUID, Attributes attributes);

    /**
     * This method will remove a component of the player's screen
     *
     * @param playerUUID The player to remove the component
     * @param componentId The id of the component to remove
     */
    public abstract void removeComponent(String playerUUID, String componentId);

    public abstract void downloadResource(String playerUUID, Resource resource);

}
