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

package io.github.minecraftgui.views.sponge;

import io.github.minecraftgui.models.components.Paragraph;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;

import java.util.UUID;

/**
 * Created by Samuel on 2016-01-15.
 */
public class WorldChangeEvent {

    private final Paragraph paragraph;
    private final UUID player;

    public WorldChangeEvent( Paragraph paragraph, UUID player ) {
        this.paragraph = paragraph;
        this.player = player;
    }

    @Listener
    public void onWorldChange( DisplaceEntityEvent.TargetPlayer event ) {
        if ( event.getTargetEntity().getUniqueId().equals( player ) ) {
            if ( event.getFromTransform().getExtent() != event.getToTransform().getExtent() ) {
                paragraph.setText( event.getToTransform().getExtent().getName() );
            }
        }
    }
}
