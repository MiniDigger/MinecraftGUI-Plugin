package io.github.minecraftgui.views.bukkit;

import io.github.minecraftgui.models.components.Paragraph;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.views.AdminPlugin;
import io.github.minecraftgui.views.MinecraftGuiService;
import io.github.minecraftgui.views.PluginInterface;
import io.github.minecraftgui.views.bukkit.commands.DevCommand;
import io.github.minecraftgui.views.bukkit.commands.ReloadCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Created by Martin on 23.05.2016.
 */
public class Bukkit extends JavaPlugin implements PluginInterface, Listener {

    private BukkitNetwork bukkitNetwork;
    private MinecraftGuiService service;
    private AdminPlugin adminPlugin;

    private int enabledPlugins = 0;

    public void onEnable() {
        this.bukkitNetwork = new BukkitNetwork( this );
        this.service = new MinecraftGuiService( bukkitNetwork, this );

        adminPlugin = new AdminPlugin( service, getDataFolder().getAbsolutePath() );

        getServer().getServicesManager().register( MinecraftGuiService.class, this.service, this, ServicePriority.Normal );

        getCommand( "gui" ).setExecutor( new ReloadCommand( bukkitNetwork ) );
        getCommand( "guidev" ).setExecutor( new DevCommand( adminPlugin ) );

        getServer().getPluginManager().registerEvents( this, this );
    }

    @EventHandler
    public void onPluginInitialize( PluginEnableEvent event ) {
        enabledPlugins++;
        if ( enabledPlugins >= getServer().getPluginManager().getPlugins().length ) {
            System.out.println( "sort plugins" );
            this.bukkitNetwork.sortPlugins();
            enabledPlugins = 0;
        }
    }

    @Override
    public UserGui getUserGui( String plugin, UUID player ) {
        return this.bukkitNetwork.getUserConnection( player ).getUserGui( plugin );
    }

    @Override
    public String getPlayerName( UUID player ) {
        return org.bukkit.Bukkit.getPlayer( player ).getName();
    }

    @Override
    public void sendCommand( UUID sender, String command ) {
        org.bukkit.Bukkit.getPlayer( sender ).performCommand( command );
    }

    @Override
    public void setParagraphToWorldChangeEvent( UUID player, Paragraph paragraph ) {
        getServer().getPluginManager().registerEvents( new WorldChangeEvent( paragraph, player ), this );

        paragraph.setText( org.bukkit.Bukkit.getPlayer( player ).getWorld().getName() );
    }

    public void setParagraphToEconomyTransactionEvent( UUID player, Paragraph paragraph, String currency ) {
        //TODO implement updating of the player's balance
//        game.getEventManager().registerListeners( this, new EconomyTransactionEvent( paragraph, player, currency ) );
//
//        EconomyService economyService = game.getServiceManager().provide( EconomyService.class ).get();
//        UniqueAccount account = economyService.getAccount( player ).get();
//        Currency curr = null;
//
//        for ( Currency c : economyService.getCurrencies() ) {
//            if ( c.getDisplayName().toPlain().equalsIgnoreCase( currency ) ) {
//                curr = c;
//            }
//        }
//
//        if ( curr != null ) {
//            paragraph.setText( account.getBalance( curr ).toString() );
//        }
    }

}