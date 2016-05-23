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
import io.github.minecraftgui.models.forms.Dropdown;
import io.github.minecraftgui.models.shapes.Rectangle;
import io.github.minecraftgui.models.shapes.RectangleColor;
import io.github.minecraftgui.views.PluginInterface;
import org.w3c.dom.Element;

/**
 * Created by Samuel on 2016-01-05.
 */
public class ParagraphTag extends ComponentTag {

    protected final ComponentTag buttonLineBefore;
    protected final ComponentTag buttonLineAfter;
    protected final String text;
    private final String dropdown;

    public ParagraphTag( Element element, GuiFactory.GuiModel model ) {
        super( element, model );
        text = element.getTextContent().trim();
        dropdown = element.getAttribute( "dropdown" );
        buttonLineAfter = (ComponentTag) getXmlTagSetAs( element, "after" );
        buttonLineBefore = (ComponentTag) getXmlTagSetAs( element, "before" );

        if ( buttonLineAfter != null ) {
            model.addTag( buttonLineAfter );
        }
        if ( buttonLineBefore != null ) {
            model.addTag( buttonLineBefore );
        }
    }

    @Override
    public Component createComponent( PluginInterface service, UserGui userGui ) {
        Component blb = buttonLineBefore == null ? new Div( RectangleColor.class ) : buttonLineBefore.createComponent( service, userGui );
        Component bla = buttonLineAfter == null ? new Div( RectangleColor.class ) : buttonLineAfter.createComponent( service, userGui );

        return new Paragraph( (Class<? extends Rectangle>) shape, id, blb, bla );
    }

    @Override
    protected void setAttributes( PluginInterface plugin, UserGui userGui, Component component ) {
        super.setAttributes( plugin, userGui, component );
        Paragraph paragraph = (Paragraph) component;

        if ( buttonLineAfter != null ) {
            buttonLineAfter.setAttributes( plugin, userGui, paragraph.getButtonLineAfter() );
        }

        if ( buttonLineBefore != null ) {
            buttonLineBefore.setAttributes( plugin, userGui, paragraph.getButtonLineBefore() );
        }

        paragraph.setText( convertString( plugin, userGui, getText() ) );

        if ( !form.equals( "" ) && !dropdown.equals( "" ) ) {
            Dropdown dropdown = userGui.getDropdown( this.dropdown );
            userGui.getForm( form ).addValuable( this.dropdown, dropdown );
            dropdown.setParagraphValueDisplayed( paragraph );
        }
    }

    @Override
    protected void initAfterChildrenCreated( PluginInterface service, UserGui userGui, Component component ) {
        super.initAfterChildrenCreated( service, userGui, component );

        if ( !form.equals( "" ) && !dropdown.equals( "" ) ) {
            userGui.getDropdown( this.dropdown ).init();
        }
    }
}
