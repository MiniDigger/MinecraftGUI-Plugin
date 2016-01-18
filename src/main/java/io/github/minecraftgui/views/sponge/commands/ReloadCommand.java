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

package io.github.minecraftgui.views.sponge.commands;

import io.github.minecraftgui.controllers.NetworkController;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Samuel on 2016-01-18.
 */
public class ReloadCommand implements CommandExecutor {

    private final NetworkController networkController;

    public ReloadCommand(NetworkController networkController) {
        this.networkController = networkController;
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        if(commandSource instanceof Player && args.<String>getOne("action").get().equalsIgnoreCase("reload")){
            Player player = (Player) commandSource;

            player.sendMessage(Text.builder("Your gui will be reloaded.").color(TextColors.GRAY).build());
            networkController.reloadUser(player.getUniqueId());
        }

        return CommandResult.success();
    }

}
