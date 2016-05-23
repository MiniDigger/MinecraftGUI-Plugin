package io.github.minecraftgui.views.bukkit.commands;

import io.github.minecraftgui.controllers.NetworkController;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Martin on 23.05.2016.
 */
public class ReloadCommand implements CommandExecutor {

    private final NetworkController networkController;

    public ReloadCommand( NetworkController networkController ) {
        this.networkController = networkController;
    }

    @Override
    public boolean onCommand( CommandSender commandSource, Command command, String lable, String[] args ) {
        if ( commandSource instanceof Player && args.length >= 1 && args[0].equalsIgnoreCase( "reload" ) ) {
            Player player = (Player) commandSource;

            player.sendMessage( ChatColor.GRAY + "Your gui will be reloaded." );
            networkController.reloadUser( player.getUniqueId() );
        }

        return true;
    }
}
