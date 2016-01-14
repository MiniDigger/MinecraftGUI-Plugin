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
import io.github.minecraftgui.views.MinecraftGuiService;
import org.w3c.dom.Element;

import java.util.regex.Matcher;

/**
 * Created by Samuel on 2016-01-12.
 */
public class DownloadTag extends Tag {

    public enum Type{IMAGE, FONT}

    private final Type type;
    private final String url;
    private final String name;

    public DownloadTag(Element element, GuiFactory.GuiModel model) {
        super(element, model);
        this.type = Type.valueOf(element.getAttribute("type").toUpperCase());
        this.url = element.getAttribute("url");
        this.name = element.getAttribute("name");
    }

    public String getUrl(MinecraftGuiService service, UserGui userGui) {
        StringBuilder url = new StringBuilder(this.url);

        Matcher matcher = PATTERN_PLAYER_UUID.matcher(url);

        if(matcher.find())
            url.replace(matcher.start(), matcher.end(), userGui.getPlayerUUID().toString());

        matcher = PATTERN_PLAYER_NAME.matcher(url);

        if(matcher.find())
            url.replace(matcher.start(), matcher.end(), service.getPlayerName(userGui.getPlayerUUID()));

        return url.toString();
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}
