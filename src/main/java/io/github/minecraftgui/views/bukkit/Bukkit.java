package io.github.minecraftgui.views.bukkit;

import com.google.inject.Inject;
import io.github.minecraftgui.models.components.Paragraph;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.views.AdminPlugin;
import io.github.minecraftgui.views.MinecraftGuiService;
import io.github.minecraftgui.views.PluginInterface;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Created by Martin on 23.05.2016.
 */
public class Bukkit extends JavaPlugin implements PluginInterface {
    @Inject
    private Game game;

    @Inject
    @DefaultConfig( sharedRoot = false )
    private Path defaultConfig;

    private BukkitNetwork bukkitNetwork;
    private MinecraftGuiService service;
    private AdminPlugin adminPlugin;

    @Listener
    public void onInitializationEvent( GameInitializationEvent event ) {
        this.bukkitNetwork = new BukkitNetwork( this, game );
        this.service = new MinecraftGuiService( bukkitNetwork, this );

        adminPlugin = new AdminPlugin( service, defaultConfig.toString().substring( 0, defaultConfig.toString().lastIndexOf( File.separator ) ) );

        game.getServiceManager().setProvider( this, MinecraftGuiService.class, this.service );


        getCommand( "gui" ).setExecutor( new io.github.minecraftgui.views.bukkit.commands.ReloadCommand( bukkitNetwork ) );
        getCommand( "guidev" ).setExecutor( new io.github.minecraftgui.views.bukkit.commands.DevCommand( adminPlugin ) );
    }

    @Listener
    public void onStartingEvent( GameStartingServerEvent event ) {
        this.bukkitNetwork.sortPlugins();
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