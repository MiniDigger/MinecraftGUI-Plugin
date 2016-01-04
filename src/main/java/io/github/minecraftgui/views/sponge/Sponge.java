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
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.TextArea;
import io.github.minecraftgui.models.listeners.*;
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

    @Listener
    public void onPreInitializationEvent(GamePreInitializationEvent event) {
        this.spongeNetwork = new SpongeNetwork(this, game);
        this.spongeNetwork.addPlugin(this);
    }

    @Override
    public void onGuiInit(UserGui userGui) {
        Root root = userGui.getRoot();

        TextArea textArea = new TextArea(RectangleColor.class, "Paragraph", new Div(RectangleColor.class), new Div(RectangleColor.class));
        root.add(textArea);
        textArea.setXRelative(State.NORMAL, 200);
        textArea.setYRelative(State.NORMAL, 50);
        textArea.getShape().setHeight(State.NORMAL, 50);
        textArea.getShape().setWidth(State.NORMAL, 100);
        textArea.setNbLineVisible(4);

        textArea.setFontColor(State.NORMAL, Color.BLACK);
        textArea.setFont(State.NORMAL, "orange juice");
        textArea.setFontSize(State.NORMAL, 24);
        textArea.setCursorColor(State.NORMAL, Color.WHITE);

        textArea.getButtonLineBefore().getShape().setWidth(State.NORMAL, 50);
        textArea.getButtonLineBefore().getShape().setHeight(State.NORMAL, 15);
        textArea.getButtonLineBefore().getShape().setBackground(State.NORMAL, Color.green);
        textArea.getButtonLineBefore().addYRelativeTo(textArea.getShape(), AttributeDouble.HEIGHT, 1);

        textArea.getButtonLineAfter().getShape().setWidth(State.NORMAL, 50);
        textArea.getButtonLineAfter().getShape().setHeight(State.NORMAL, 15);
        textArea.getButtonLineAfter().getShape().setBackground(State.NORMAL, Color.YELLOW);
        textArea.getButtonLineAfter().addYRelativeTo(textArea.getShape(), AttributeDouble.HEIGHT, 1);
        textArea.getButtonLineAfter().addXRelativeTo(textArea.getShape(), AttributeDouble.WIDTH, 0.5);

        textArea.addOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(ComponentValuable component) {
                System.out.println(component.getValue());
            }
        });
        textArea.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Component component) {
                System.out.println("Click");
            }
        });
        textArea.addOnDoubleClickListener(new OnDoubleClickListener() {
            @Override
            public void onDoubleClick(Component component) {
                System.out.println("DoubleClick");
            }
        });
        textArea.addOnBlurListener(new OnBlurListener() {
            @Override
            public void onBlur(Component component) {
                System.out.println("Blur");
            }
        });
        textArea.addOnFocusListener(new OnFocusListener() {
            @Override
            public void onFocus(Component component) {
                System.out.println("Focus");
            }
        });
        textArea.addOnInputListener(new OnInputListener() {
            @Override
            public void onInput(Component component, char input) {
                System.out.println("Input: "+input);
            }
        });
        textArea.addOnKeyPressedListener(new OnKeyPressedListener() {
            @Override
            public void onKeyPressed(Component component, int keyCode) {
                System.out.println("Key pressed: "+keyCode);
            }
        });
        textArea.addOnRemoveListener(new OnRemoveListener() {
            @Override
            public void onRemove(Component component) {
                System.out.println("Remove");
            }
        });

        textArea.setText("Bonjour");
    }

    @Override
    public void onGuiOpen(UserGui userGui) {

    }

    @Override
    public void onGuiClose(UserGui userGui) {
        userGui.getComponent("Paragraph").remove();
    }

}
