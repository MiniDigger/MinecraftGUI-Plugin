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
import io.github.minecraftgui.models.components.TextArea;
import io.github.minecraftgui.models.forms.Form;
import io.github.minecraftgui.models.forms.RadioButtonGroup;
import io.github.minecraftgui.models.listeners.OnFormSendListener;
import io.github.minecraftgui.models.listeners.OnGuiListener;
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
    public void onGuiPreInit(UserGui userGui) {
        userGui.addFont("http://www.1001freefonts.com/d/325/orange_juice.zip");
        userGui.addImage("http://media3.giphy.com/media/tp1U2lhRChsDC/200w.gif", "google.gif");
        userGui.addFontToGenerate("orange juice", 24, Color.BLACK);
    }

    @Override
    public void onGuiInit(UserGui userGui) {
        Root root = userGui.getRoot();
        RadioButtonGroup group = new RadioButtonGroup();
        Div div = new Div(RectangleColor.class);
        root.add(div);

        div.setYRelative(State.NORMAL, 50);
        div.setXRelative(State.NORMAL, 30);
        div.getShape().setWidth(State.NORMAL, 50);
        div.getShape().setHeight(State.NORMAL, 20);
        div.getShape().setBackground(State.NORMAL, Color.BLUE);


        for(int i = 0; i < 5; i++){
            CheckBox checkBox = new CheckBox(RectangleColor.class, RectangleColor.class);
            root.add(checkBox);

            checkBox.setYRelative(State.NORMAL, 30);
            checkBox.setXRelative(State.NORMAL, 30 + 40 * i);
            checkBox.getShapeOnValueTrue().setWidth(State.NORMAL, 10);
            checkBox.getShapeOnValueTrue().setHeight(State.NORMAL, 10);
            checkBox.getShapeOnValueTrue().setBackground(State.NORMAL, Color.GREEN);
            checkBox.getShapeOnValueFalse().setWidth(State.NORMAL, 10);
            checkBox.getShapeOnValueFalse().setHeight(State.NORMAL, 10);
            checkBox.getShapeOnValueFalse().setBackground(State.NORMAL, Color.RED);

            group.addCheckBox(checkBox);
        }

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

        Div button = new Div(RectangleColor.class);
        Slider slider = new Slider(Slider.Type.HORIZONTAL, RectangleColor.class, RectangleColor.class, button);
        userGui.getRoot().add(slider);
        slider.setXRelative(State.NORMAL, 100);
        slider.setYRelative(State.NORMAL, 100);

        slider.getShape().setWidth(State.NORMAL, 50);
        slider.getShape().setHeight(State.NORMAL, 2);
        slider.getShape().setBackground(State.NORMAL, new Color(236, 240, 241, 125));
        slider.getShapeOnProgress().setBackground(State.NORMAL, new Color(231, 76, 60, 255));
        slider.getShapeOnProgress().setWidth(State.NORMAL, slider.getShape(), AttributeDouble.WIDTH, 300, 1);
        slider.setCursor(State.HOVER, Cursor.HAND);

        button.getShape().setBackground(State.NORMAL, new Color(236, 240, 241, 255));
        button.getShape().setWidth(State.NORMAL, 2);
        button.getShape().setHeight(State.NORMAL, 6);
        button.setCursor(State.HOVER, Cursor.HAND);
        button.addXRelativeTo(button.getShape(), AttributeDouble.WIDTH, -0.5);
        button.addYRelativeTo(slider.getShape(), AttributeDouble.HEIGHT, 0.5);
        button.addYRelativeTo(button.getShape(), AttributeDouble.HEIGHT, -0.5);

        Form form = new Form(div);
        form.addValuable("text", textArea);
        form.addValuable("list", group);
        form.addValuable("slider", slider);
        form.addOnFormSendListener(new OnFormSendListener() {
            @Override
            public void onFormSend(Form form) {
                System.out.println(form.getValuable("list").getValue());
                System.out.println(form.getValuable("text").getValue());
                System.out.println(form.getValuable("slider").getValue());
            }
        });
    }

    @Override
    public void onGuiOpen(UserGui userGui) {

    }

    @Override
    public void onGuiClose(UserGui userGui) {
    }

}
