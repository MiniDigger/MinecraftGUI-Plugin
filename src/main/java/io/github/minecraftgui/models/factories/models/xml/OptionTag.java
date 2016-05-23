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
import io.github.minecraftgui.models.components.Paragraph;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.factories.GuiFactory;
import io.github.minecraftgui.models.shapes.Rectangle;
import io.github.minecraftgui.models.shapes.RectangleColor;
import io.github.minecraftgui.views.PluginInterface;
import org.w3c.dom.Element;

/**
 * Created by Samuel on 2016-01-15.
 */
public class OptionTag extends ComponentTag {

    protected final String value;
    private final String dropdown;

    public OptionTag( Element element, GuiFactory.GuiModel model ) {
        super( element, model );
        dropdown = element.getAttribute( "dropdown" );
        value = element.getAttribute( "value" );
    }

    @Override
    protected Component createComponent( PluginInterface service, UserGui userGui ) {
        return new Paragraph( (Class<? extends Rectangle>) shape, id, new Div( RectangleColor.class ), new Div( RectangleColor.class ) );
    }

    @Override
    protected void setAttributes( PluginInterface plugin, UserGui userGui, Component component ) {
        super.setAttributes( plugin, userGui, component );
        Paragraph paragraph = (Paragraph) component;

        paragraph.setText( convertString( plugin, userGui, getText() ) );

        if ( !dropdown.equals( "" ) ) {
            userGui.getDropdown( this.dropdown ).addValue( paragraph, getText() );
        }
    }
}
