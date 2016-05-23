package io.github.minecraftgui.views.bukkit;

import io.github.minecraftgui.models.components.Paragraph;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.UUID;

/**
 * Created by Martin on 23.05.2016.
 */
public class WorldChangeEvent implements Listener {

    private final Paragraph paragraph;
    private final UUID player;

    public WorldChangeEvent( Paragraph paragraph, UUID player ) {
        this.paragraph = paragraph;
        this.player = player;
    }

    @EventHandler
    public void onWorldChange( PlayerChangedWorldEvent event ) {
        if ( event.getPlayer().getUniqueId().equals( player ) ) {
            //TODO is this right?
            if ( !event.getFrom().getName().equals( event.getPlayer().getWorld().getName() ) ) {
                paragraph.setText( event.getPlayer().getWorld().getName() );
            }
        }
    }
}

