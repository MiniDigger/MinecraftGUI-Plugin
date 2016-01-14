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

import io.github.minecraftgui.models.factories.GuiFactory;
import org.w3c.dom.Element;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Samuel on 2016-01-12.
 */
public class GenerateTag extends Tag {

    public static final Pattern PATTERN_COLOR = Pattern.compile("^[0-9]{1,3},[0-9]{1,3},[0-9]{1,3},[0-9]{1,3}$");

    private final String name;
    private final int size;
    private final Color color;

    public GenerateTag(Element element, GuiFactory.GuiModel model) {
        super(element, model);
        this.name = element.getAttribute("name");
        this.size = Integer.parseInt(element.getAttribute("size"));

        String color = element.getAttribute("color");
        Matcher matcher = PATTERN_COLOR.matcher(color);

        if(matcher.find()){
            String colors[] = color.split(",");
            int r = Integer.parseInt(colors[0]);
            int g = Integer.parseInt(colors[1]);
            int b = Integer.parseInt(colors[2]);
            int a = Integer.parseInt(colors[3]);

            this.color = new Color(r,g,b,a);
        }
        else
            this.color = null;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }
}
