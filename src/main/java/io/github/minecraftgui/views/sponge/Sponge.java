/*
 *     Minecraft GUI Server
 *     Copyright (C) 2015  Samuel Marchildon-Lavoie
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.minecraftgui.views.sponge;

import com.google.inject.Inject;
import io.github.minecraftgui.models.components.*;
import io.github.minecraftgui.models.components.Cursor;
import io.github.minecraftgui.models.listeners.OnGuiListener;
import io.github.minecraftgui.models.shapes.PolygonColor;
import io.github.minecraftgui.models.shapes.RectangleColor;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.awt.*;

@Plugin(id = "MinecraftGUIServer", name = "Minecraft GUI Server", version = "2.0")
public class Sponge implements OnGuiListener {

    @Inject private Logger logger;
    @Inject private Game game;
    private SpongeNetwork spongeNetwork;
    private Div div;

    @Listener
    public void onPreInitializationEvent(GamePreInitializationEvent event) {
        this.spongeNetwork = new SpongeNetwork(this, game);
        this.spongeNetwork.addPlugin(this);
        this.spongeNetwork.addPlugin(new OnGuiListener() {
            Slider rect;
            Div button;

            @Override
            public void onGuiInit(UserGui userGui) {
                button = new Div(RectangleColor.class);
                rect = new Slider(Slider.Type.HORIZONTAL, RectangleColor.class, RectangleColor.class, button);
                userGui.getRoot().add(rect);
                rect.setXRelative(State.NORMAL, 100);
                rect.setYRelative(State.NORMAL, 100);

                rect.getShape().setWidth(State.NORMAL, 50);
                rect.getShape().setHeight(State.NORMAL, 2);
                rect.getShape().setBackground(State.NORMAL, new Color(236, 240, 241, 125));
                rect.getShapeOnProgress().setBackground(State.NORMAL, new Color(231, 76, 60, 255));
                rect.getShapeOnProgress().setWidth(State.NORMAL, rect.getShape(), AttributeDouble.WIDTH, 300, 1);
                rect.setCursor(State.HOVER, Cursor.HAND);

                button.getShape().setBackground(State.NORMAL, new Color(236, 240, 241, 255));
                button.getShape().setWidth(State.NORMAL, 2);
                button.getShape().setHeight(State.NORMAL, 6);
                button.setCursor(State.HOVER, Cursor.HAND);
                button.addXRelativeTo(button.getShape(), AttributeDouble.WIDTH, -0.5);
                button.addYRelativeTo(rect.getShape(), AttributeDouble.HEIGHT, 0.5);
                button.addYRelativeTo(button.getShape(), AttributeDouble.HEIGHT, -0.5);

            }

            @Override
            public void onGuiOpen(UserGui userGui) {
                rect.setPercentage(0.2);
            }

            @Override
            public void onGuiClose(UserGui userGui) {
                rect.setPercentage(0.9);
            }
        });
    }

    @Override
    public void onGuiInit(UserGui userGui) {
        div = new Div(PolygonColor.class);
        userGui.getRoot().add(div);

        div.setVisibility(Visibility.INVISIBLE);
        div.setXRelative(State.NORMAL, 50);
        div.setYRelative(State.NORMAL, 50);

        ((PolygonColor)div.getShape()).setPositions(new double[][]{
                        {0, -10},
                        {10, 10},
                        {30, 10}
                }
        );

        ((PolygonColor) div.getShape()).setBackground(State.NORMAL, Color.RED);
        ((PolygonColor) div.getShape()).setBackground(State.HOVER, Color.BLUE);
    }

    @Override
    public void onGuiOpen(UserGui userGui) {
        div.setVisibility(Visibility.VISIBLE);
    }

    @Override
    public void onGuiClose(UserGui userGui) {
        div.setVisibility(Visibility.INVISIBLE);
    }

}
