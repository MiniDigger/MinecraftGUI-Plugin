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

package io.github.minecraftgui.models.components;

import io.github.minecraftgui.controllers.NetworkController;

/**
 * Created by Samuel on 2015-12-31.
 */
public class AttributeDouble extends Attribute {

    public static final AttributeDouble WIDTH = new AttributeDouble( NetworkController.WIDTH );
    public static final AttributeDouble HEIGHT = new AttributeDouble( NetworkController.HEIGHT );
    public static final AttributeDouble BORDER_TOP = new AttributeDouble( NetworkController.BORDER_TOP );
    public static final AttributeDouble BORDER_LEFT = new AttributeDouble( NetworkController.BORDER_LEFT );
    public static final AttributeDouble BORDER_RIGHT = new AttributeDouble( NetworkController.BORDER_RIGHT );
    public static final AttributeDouble BORDER_BOTTOM = new AttributeDouble( NetworkController.BORDER_BOTTOM );
    public static final AttributeDouble PADDING_TOP = new AttributeDouble( NetworkController.PADDING_TOP );
    public static final AttributeDouble PADDING_LEFT = new AttributeDouble( NetworkController.PADDING_LEFT );
    public static final AttributeDouble PADDING_RIGHT = new AttributeDouble( NetworkController.PADDING_RIGHT );
    public static final AttributeDouble PADDING_BOTTOM = new AttributeDouble( NetworkController.PADDING_BOTTOM );
    public static final AttributeDouble MARGIN_TOP = new AttributeDouble( NetworkController.MARGIN_TOP );
    public static final AttributeDouble MARGIN_LEFT = new AttributeDouble( NetworkController.MARGIN_LEFT );
    public static final AttributeDouble MARGIN_RIGHT = new AttributeDouble( NetworkController.MARGIN_RIGHT );
    public static final AttributeDouble MARGIN_BOTTOM = new AttributeDouble( NetworkController.MARGIN_BOTTOM );
    public static final AttributeDouble TEXT = new AttributeDouble( NetworkController.TEXT );

    private AttributeDouble( String name ) {
        super( name );
    }

    @Override
    public String toString() {
        return getName();
    }

    public static AttributeDouble valueOf( String name ) {
        name = name.replaceAll( "-", "_" );
        switch ( name.toUpperCase() ) {
            case "WIDTH":
                return WIDTH;
            case "HEIGHT":
                return HEIGHT;
            case "BORDER_TOP":
                return BORDER_TOP;
            case "BORDER_LEFT":
                return BORDER_LEFT;
            case "BORDER_RIGHT":
                return BORDER_RIGHT;
            case "BORDER_BOTTOM":
                return BORDER_BOTTOM;
            case "PADDING_TOP":
                return PADDING_TOP;
            case "PADDING_LEFT":
                return PADDING_LEFT;
            case "PADDING_RIGHT":
                return PADDING_RIGHT;
            case "PADDING_BOTTOM":
                return PADDING_BOTTOM;
            case "MARGIN_TOP":
                return MARGIN_TOP;
            case "MARGIN_LEFT":
                return MARGIN_LEFT;
            case "MARGIN_RIGHT":
                return MARGIN_RIGHT;
            case "MARGIN_BOTTOM":
                return MARGIN_BOTTOM;
            case "TEXT":
                return TEXT;
        }

        return null;
    }
}
