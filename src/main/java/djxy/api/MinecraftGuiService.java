/*
 *
 *       Minecraft GUI Server
 *       Copyright (C) 2015  Samuel Marchildon-Lavoie
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package djxy.api;

import djxy.models.ComponentManager;
import djxy.models.component.Attributes;
import djxy.models.component.Component;
import djxy.models.resource.Resource;

import java.util.ArrayList;

public interface MinecraftGuiService {

    /**
     * Register your component manager.
     *
     * @param manager The ComponentManager you are using to receive the events from MinecraftGUI
     * @param playerNeedAuthentication If you need to certify the player identity
     * @param resourceToDownload All the resources the player will download at his connection
     */
    void registerComponentManager(ComponentManager manager, boolean playerNeedAuthentication, ArrayList<Resource> resourceToDownload);

    /**
     * Register your component manager.
     *
     * @param manager The ComponentManager you are using to receive the events from MinecraftGUI.
     * @param playerNeedAuthentication If you need to certify the player identity
     */
    void registerComponentManager(ComponentManager manager, boolean playerNeedAuthentication);

    /**
     * You will receive all the clicks of one button.
     *
     * @param manager The ComponentManager you are using to receive the events from MinecraftGUI.
     * @param buttonId The id of the button you want to listen
     */
    void listenButton(ComponentManager manager, String buttonId);

    /**
     * You remove a component in X second of the player's screen.
     *
     * @param playerUUID The player to remove the component
     * @param componentIdToRemove The id of the component to remove
     * @param second The number of second.
     */
    void createTimerRemover(String playerUUID, String componentIdToRemove, int second);

    /**
     * Do all the events of a button.
     *
     * @param playerUUID The player to send the action
     * @param buttonId The id of the button
     */
    void callButtonEvent(String playerUUID, String buttonId);

    /**
     * Create one component on the player's screen
     *
     * @param playerUUID The player to send the component
     * @param component The component to create
     */
    void createComponent(String playerUUID, Component component);

    /**
     * Change the value of one attribute of a component
     *
     * @param playerUUID The player to send the update
     * @param attributes The attributes to send
     */
    void updateComponent(String playerUUID, Attributes attributes);

    /**
     * Remove a component of the player's screen
     *
     * @param playerUUID The player to remove the component
     * @param componentId The id of the component to remove
     */
    void removeComponent(String playerUUID, String componentId);

    /**
     * The player will download the resource sent.
     *
     * @param playerUUID The player to send the resource
     * @param resource The resource to send
     */
    void downloadResource(String playerUUID, Resource resource);

}
