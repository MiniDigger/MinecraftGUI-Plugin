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

import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.Div;
import io.github.minecraftgui.models.components.List;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.factories.GuiFactory;
import io.github.minecraftgui.models.shapes.Rectangle;
import io.github.minecraftgui.models.shapes.RectangleColor;
import io.github.minecraftgui.views.MinecraftGuiService;
import org.w3c.dom.Element;

/**
 * Created by Samuel on 2016-01-05.
 */
public class ListTag extends ComponentTag {

    private final ComponentTag buttonListBefore;
    private final ComponentTag buttonListAfter;

    public ListTag(Element element, GuiFactory.GuiModel model) {
        super(element, model);
        buttonListBefore = (ComponentTag) getXmlTagSetAs(element, "buttonListBefore");
        buttonListAfter = (ComponentTag) getXmlTagSetAs(element, "buttonListAfter");

        if(buttonListBefore != null)
            model.addTag(buttonListBefore);
        if(buttonListAfter != null)
            model.addTag(buttonListAfter);
    }

    @Override
    public Component createComponent(MinecraftGuiService service, UserGui userGui) {
        Component blb = buttonListBefore == null?new Div(RectangleColor.class):buttonListBefore.createComponent(service, userGui);
        Component bla = buttonListAfter == null?new Div(RectangleColor.class):buttonListAfter.createComponent(service, userGui);

        return new List((Class<? extends Rectangle>) shape, blb, bla, id);
    }

    @Override
    protected void setAttributes(Component component) {
        super.setAttributes(component);
        List list = (List) component;

        if(buttonListAfter != null)
            buttonListAfter.setAttributes(list.getButtonListAfter());

        if(buttonListBefore !=  null)
            buttonListBefore.setAttributes(list.getButtonListBefore());
    }
}
