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

import io.github.minecraftgui.models.components.CheckBox;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.factories.GuiFactory;
import io.github.minecraftgui.models.forms.RadioButtonGroup;
import io.github.minecraftgui.models.shapes.Rectangle;
import io.github.minecraftgui.views.PluginInterface;
import org.w3c.dom.Element;

/**
 * Created by Samuel on 2016-01-05.
 */
public class CheckBoxTag extends ComponentTag {

    private final Class<? extends Rectangle> shapeValueOnTrue;
    private final Class<? extends Rectangle> shapeValueOnFalse;
    private final boolean checked;
    private final String group;
    private final String value;

    public CheckBoxTag(Element element, GuiFactory.GuiModel model) {
        super(element, model);
        shapeValueOnTrue = (Class<? extends Rectangle>) getShapeByName(element.getAttribute("shapeValueOnTrue"));
        shapeValueOnFalse = (Class<? extends Rectangle>) getShapeByName(element.getAttribute("shapeValueOnFalse"));
        checked = element.hasAttribute("checked")?Boolean.parseBoolean(element.getAttribute("checked")) : false;
        group = element.getAttribute("group");
        value = element.getAttribute("value");
    }

    @Override
    public Component createComponent(PluginInterface service, UserGui userGui) {
       return new CheckBox(shapeValueOnTrue, shapeValueOnFalse, id);
    }

    @Override
    protected void setAttributes(PluginInterface plugin, UserGui userGui, Component component) {
        super.setAttributes(plugin, userGui, component);
        ((CheckBox) component).setChecked(checked);

        if(!form.equals("") && !group.equals("")) {
            RadioButtonGroup radioButtonGroup = userGui.getRadioButtonGroup(group);
            userGui.getForm(form).addValuable(group, radioButtonGroup);
            radioButtonGroup.addCheckBox((CheckBox) component, value);
        }
    }

}
