package io.github.minecraftgui.views.bukkit.commands;

import io.github.minecraftgui.views.AdminPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Martin on 23.05.2016.
 */
public class DevCommand implements CommandExecutor {

    private AdminPlugin adminPlugin;

    public DevCommand( AdminPlugin adminPlugin ) {
        this.adminPlugin = adminPlugin;
    }

    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String label, String[] args ) {
        if ( args.length == 1 ) {
            boolean state = Boolean.parseBoolean( args[0] );
            adminPlugin.setDevMode( state );
        }
        return true;
    }
}
