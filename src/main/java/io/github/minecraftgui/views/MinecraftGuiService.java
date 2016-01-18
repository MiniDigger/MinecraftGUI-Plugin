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

package io.github.minecraftgui.views;

import io.github.minecraftgui.controllers.NetworkController;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.factories.GuiFactory;
import io.github.minecraftgui.models.listeners.OnGuiListener;

import java.io.File;
import java.util.UUID;

/**
 * Created by Samuel on 2016-01-16.
 */
public class MinecraftGuiService {

    private final NetworkController networkController;
    private final PluginInterface pluginInterface;

    public MinecraftGuiService(NetworkController networkController, PluginInterface pluginInterface) {
        this.networkController = networkController;
        this.pluginInterface = pluginInterface;
    }

    public void addPlugin(OnGuiListener plugin, String name){
        networkController.addPlugin(plugin, name);
    }

    public void addPlugin(OnGuiListener plugin, String name, String... dependencies){
        networkController.addPlugin(plugin, name, dependencies);
    }

    public PluginInterface getPluginInterface() {
        return pluginInterface;
    }

    public UserGui getUserGui(UUID player, String plugin) {
        return networkController.getUserConnection(player).getUserGui(plugin);
    }

    public GuiFactory.GuiModel createGuiModel(File file) {
        return GuiFactory.createGuiModel(file);
    }

    public boolean isPlayerConnectedWithClient(UUID player) {
        return networkController.getUserConnection(player) != null;
    }

}