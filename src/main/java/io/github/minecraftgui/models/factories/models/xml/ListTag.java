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
import io.github.minecraftgui.views.PluginInterface;
import org.w3c.dom.Element;

/**
 * Created by Samuel on 2016-01-05.
 */
public class ListTag extends ComponentTag {

    private final ComponentTag buttonListBefore;
    private final ComponentTag buttonListAfter;
    private final String dropdown;

    public ListTag(Element element, GuiFactory.GuiModel model) {
        super(element, model);
        dropdown = element.getAttribute("dropdown");
        buttonListBefore = (ComponentTag) getXmlTagSetAs(element, "before");
        buttonListAfter = (ComponentTag) getXmlTagSetAs(element, "after");

        if(buttonListBefore != null)
            model.addTag(buttonListBefore);
        if(buttonListAfter != null)
            model.addTag(buttonListAfter);
    }

    @Override
    public Component createComponent(PluginInterface service, UserGui userGui) {
        Component blb = buttonListBefore == null?new Div(RectangleColor.class):buttonListBefore.createComponent(service, userGui);
        Component bla = buttonListAfter == null?new Div(RectangleColor.class):buttonListAfter.createComponent(service, userGui);

        return new List((Class<? extends Rectangle>) shape, blb, bla, id);
    }

    @Override
    protected void setAttributes(PluginInterface plugin, UserGui userGui, Component component) {
        super.setAttributes(plugin, userGui, component);
        List list = (List) component;

        if(buttonListAfter != null)
            buttonListAfter.setAttributes(plugin, userGui, list.getButtonListAfter());

        if(buttonListBefore !=  null)
            buttonListBefore.setAttributes(plugin, userGui, list.getButtonListBefore());

        if(!dropdown.equals(""))
            userGui.getDropdown(this.dropdown).setList(list);
    }

    @Override
    protected void initAfterChildrenCreated(PluginInterface service, UserGui userGui, Component component) {
        super.initAfterChildrenCreated(service, userGui, component);
        ((List) component).update();
    }
}
