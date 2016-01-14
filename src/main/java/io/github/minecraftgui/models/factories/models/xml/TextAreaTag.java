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
import io.github.minecraftgui.models.components.TextArea;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.factories.GuiFactory;
import io.github.minecraftgui.models.shapes.Rectangle;
import io.github.minecraftgui.models.shapes.RectangleColor;
import io.github.minecraftgui.views.MinecraftGuiService;
import org.w3c.dom.Element;

/**
 * Created by Samuel on 2016-01-05.
 */
public class TextAreaTag extends ComponentTag {

    private final ComponentTag buttonLineBefore;
    private final ComponentTag buttonLineAfter;

    public TextAreaTag(Element element, GuiFactory.GuiModel model) {
        super(element, model);
        buttonLineAfter = (ComponentTag) getXmlTagSetAs(element, "buttonLineAfter");
        buttonLineBefore = (ComponentTag) getXmlTagSetAs(element, "buttonLineBefore");

        if(buttonLineAfter != null)
            model.addTag(buttonLineAfter);

        if(buttonLineBefore != null)
            model.addTag(buttonLineBefore);
    }

    @Override
    public Component createComponent(MinecraftGuiService service, UserGui userGui) {
        Component blb = buttonLineBefore == null?new Div(RectangleColor.class):buttonLineBefore.createComponent(service, userGui);
        Component bla = buttonLineAfter == null?new Div(RectangleColor.class):buttonLineAfter.createComponent(service, userGui);

        return new TextArea((Class<? extends Rectangle>) shape, id, blb, bla);
    }

    @Override
    protected void setAttributes(Component component) {
        super.setAttributes(component);
        TextArea textArea = (TextArea) component;

        if(buttonLineAfter != null)
            buttonLineAfter.setAttributes(textArea.getButtonLineAfter());

        if(buttonLineBefore != null)
            buttonLineBefore.setAttributes(textArea.getButtonLineBefore());
    }
}
