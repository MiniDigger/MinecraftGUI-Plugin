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
import io.github.minecraftgui.models.components.List;
import io.github.minecraftgui.models.forms.Dropdown;
import io.github.minecraftgui.models.forms.Form;
import io.github.minecraftgui.models.forms.RadioButtonGroup;
import io.github.minecraftgui.models.listeners.OnFormSendListener;
import io.github.minecraftgui.models.listeners.OnGuiListener;
import io.github.minecraftgui.models.shapes.RectangleColor;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.awt.*;

@Plugin(id = "MinecraftGUIServer", name = "Minecraft GUI Server", version = "2.0")
public class Sponge implements OnGuiListener {

    @Inject private Logger logger;
    @Inject private Game game;
    private SpongeNetwork spongeNetwork;

    @Listener
    public void onInitializationEvent(GameInitializationEvent event) {
        this.spongeNetwork = new SpongeNetwork(this, game);
        this.spongeNetwork.addPlugin(this, "Tes");
    }

    @Listener
    public void onStartingEvent(GameStartingServerEvent event) {
        this.spongeNetwork.sortPlugins();
    }

    @Override
    public void onGuiPreInit(UserGui userGui) {
        userGui.addFont("http://www.1001freefonts.com/d/325/orange_juice.zip");
        userGui.addFontToGenerate("orange juice", 12, Color.BLACK);
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


        List list = new List(RectangleColor.class, new Div(RectangleColor.class), new Div(RectangleColor.class));
        Paragraph paragraph = new Paragraph(RectangleColor.class, new Div(RectangleColor.class), new Div(RectangleColor.class));
        root.add(list);
        root.add(paragraph);

        list.setYRelative(State.NORMAL, 150);
        list.setXRelative(State.NORMAL, 30);
        list.getShape().setWidth(State.NORMAL, 100);
        list.getShape().setHeight(State.NORMAL, 100);
        list.getShape().setBackground(State.NORMAL, Color.GRAY);
        list.setNbComponentPerList(5);

        list.getButtonListBefore().getShape().setWidth(State.NORMAL, 50);
        list.getButtonListBefore().getShape().setHeight(State.NORMAL, 15);
        list.getButtonListBefore().getShape().setBackground(State.NORMAL, Color.green);
        list.getButtonListBefore().addYRelativeTo(list.getShape(), AttributeDouble.HEIGHT, 1);

        list.getButtonListAfter().getShape().setWidth(State.NORMAL, 50);
        list.getButtonListAfter().getShape().setHeight(State.NORMAL, 15);
        list.getButtonListAfter().getShape().setBackground(State.NORMAL, Color.YELLOW);
        list.getButtonListAfter().addYRelativeTo(list.getShape(), AttributeDouble.HEIGHT, 1);
        list.getButtonListAfter().addXRelativeTo(list.getShape(), AttributeDouble.WIDTH, 0.5);

        paragraph.setYRelative(State.NORMAL, 130);
        paragraph.setXRelative(State.NORMAL, 30);
        paragraph.getShape().setHeight(State.NORMAL, 20);
        paragraph.getShape().setWidth(State.NORMAL, 100);
        paragraph.getShape().setBackground(State.NORMAL, Color.WHITE);
        paragraph.getShape().setBackground(State.HOVER, Color.LIGHT_GRAY);

        paragraph.setFontColor(State.NORMAL, Color.BLACK);
        paragraph.setFont(State.NORMAL, "orange juice");
        paragraph.setFontSize(State.NORMAL, 12);

        Dropdown dropdown = new Dropdown(paragraph, list);

        for(int i = 0; i < 15; i++){
            Paragraph paragraph1 = new Paragraph(RectangleColor.class, new Div(RectangleColor.class), new Div(RectangleColor.class));
            list.add(paragraph1);
            paragraph1.getShape().setHeight(State.NORMAL, 20);
            paragraph1.getShape().setWidth(State.NORMAL, 100);
            paragraph1.getShape().setBackground(State.NORMAL, Color.WHITE);
            paragraph1.getShape().setBackground(State.HOVER, Color.LIGHT_GRAY);

            paragraph1.setFontColor(State.NORMAL, Color.BLACK);
            paragraph1.setFont(State.NORMAL, "orange juice");
            paragraph1.setFontSize(State.NORMAL, 12);

            dropdown.addValue(paragraph1, "- "+i);
        }

        dropdown.init("Test");

        Form form = new Form(div);
        form.addValuable("list", group);
        form.addValuable("drop", dropdown);
        form.addOnFormSendListener(new OnFormSendListener() {
            @Override
            public void onFormSend(Form form) {
                System.out.println(form.getValuable("list").getValue());
                System.out.println(form.getValuable("drop").getValue());
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
