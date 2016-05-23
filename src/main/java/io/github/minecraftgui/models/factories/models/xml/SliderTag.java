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
import io.github.minecraftgui.models.components.Slider;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.factories.GuiFactory;
import io.github.minecraftgui.models.shapes.Rectangle;
import io.github.minecraftgui.models.shapes.RectangleColor;
import io.github.minecraftgui.views.PluginInterface;
import org.w3c.dom.Element;

/**
 * Created by Samuel on 2016-01-05.
 */
public class SliderTag extends ComponentTag {

    private final Class<? extends Rectangle> shapeProgress;
    private final ComponentTag button;
    private final Slider.Type type;
    private final double percentage;

    public SliderTag( Element element, GuiFactory.GuiModel model ) {
        super( element, model );
        button = (ComponentTag) getXmlTagSetAs( element, "button" );
        shapeProgress = (Class<? extends Rectangle>) getShapeByName( element.getAttribute( "shapeProgress" ) );
        type = element.hasAttribute( "type" ) ? Slider.Type.valueOf( element.getAttribute( "type" ).toUpperCase() ) : Slider.Type.HORIZONTAL;
        percentage = element.hasAttribute( "percentage" ) ? Double.parseDouble( element.getAttribute( "percentage" ) ) : 0;

        if ( button != null ) {
            model.addTag( button );
        }
    }

    @Override
    public Component createComponent( PluginInterface service, UserGui userGui ) {
        Component b = button == null ? new Div( RectangleColor.class ) : button.createComponent( service, userGui );

        return new Slider( type, (Class<? extends Rectangle>) shape, shapeProgress, b, id );
    }

    @Override
    protected void setAttributes( PluginInterface plugin, UserGui userGui, Component component ) {
        super.setAttributes( plugin, userGui, component );
        Slider slider = (Slider) component;

        if ( button != null ) {
            button.setAttributes( plugin, userGui, slider.getButton() );
        }

        slider.setPercentage( percentage );
    }
}
