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

package io.github.minecraftgui.models.factories.models.xml;

import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.factories.GuiFactory;
import io.github.minecraftgui.models.forms.Form;
import io.github.minecraftgui.models.listeners.OnFormSendListener;
import io.github.minecraftgui.views.PluginInterface;
import org.w3c.dom.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Samuel on 2016-01-15.
 */
public class OnFormSendTag extends Tag {

    private static final Pattern VALUE = Pattern.compile("\\{\\w+\\}");

    private final String form;
    private final String command;

    public OnFormSendTag(Element element, GuiFactory.GuiModel model) {
        super(element, model);
        form = element.getAttribute("form");
        command = element.getAttribute("command");
    }

    public void addEvent(PluginInterface pluginInterface, UserGui userGui){
        userGui.getForm(this.form).addOnFormSendListener( form1 -> pluginInterface.sendCommand(userGui.getPlayerUUID(), convertCommand( form1 )) );
    }

    private String convertCommand(Form form){
        StringBuilder str = new StringBuilder(command);
        Matcher matcher;

        while((matcher = VALUE.matcher(str)).find()) {
            String value = matcher.group().substring(1, matcher.group().length()-1);
            str.replace(matcher.start(), matcher.end(), form.getValuable(value).getValue().toString());
        }

        return str.toString();
    }

}
