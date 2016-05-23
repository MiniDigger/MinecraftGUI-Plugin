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

package io.github.minecraftgui.models.shapes;

import io.github.minecraftgui.controllers.NetworkController;
import io.github.minecraftgui.models.components.AttributeDouble;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.State;
import io.github.minecraftgui.models.network.UserConnection;

import java.awt.*;

/**
 * Created by Samuel on 2015-10-22.
 */
public abstract class Shape<V> {

    private final String type;
    private final String componentShape;
    protected final Component component;
    protected UserConnection userConnection;

    public abstract void setBackground( State state, V value );

    public abstract void setBackground( State state, V value, long time );

    public Shape( String type, String componentShape, Component component ) {
        this.type = type;
        this.componentShape = componentShape;
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    public String getComponentShape() {
        return componentShape;
    }

    public String getType() {
        return type;
    }

    public void setUserConnection( UserConnection userConnection ) {
        this.userConnection = userConnection;
    }

    public void setHeight( State state, double value ) {
        userConnection.setAttribute( NetworkController.HEIGHT, component, this, state, 1, 0, value );
    }

    public void setHeight( State state, double value, long time ) {
        userConnection.setAttribute( NetworkController.HEIGHT, component, this, state, 1, time, value );
    }

    public void setHeight( State state, Shape componentShapeRelativeTo, AttributeDouble componentShapeAttributeRelativeTo, long time, double percentage ) {
        userConnection.setAttribute( NetworkController.HEIGHT, component, this, state, percentage, time, componentShapeRelativeTo, componentShapeAttributeRelativeTo );
    }

    public void setWidth( State state, double value ) {
        userConnection.setAttribute( NetworkController.WIDTH, component, this, state, 1, 0, value );
    }

    public void setWidth( State state, double value, long time ) {
        userConnection.setAttribute( NetworkController.WIDTH, component, this, state, 1, time, value );
    }

    public void setWidth( State state, Shape componentShapeRelativeTo, AttributeDouble componentShapeAttributeRelativeTo, long time, double percentage ) {
        userConnection.setAttribute( NetworkController.WIDTH, component, this, state, percentage, time, componentShapeRelativeTo, componentShapeAttributeRelativeTo );
    }

    public void setMargin( State state, Margin margin, double value ) {
        String m = "";
        switch ( margin ) {
            case BOTTOM:
                m = NetworkController.MARGIN_BOTTOM;
                break;
            case TOP:
                m = NetworkController.MARGIN_TOP;
                break;
            case LEFT:
                m = NetworkController.MARGIN_LEFT;
                break;
            case RIGHT:
                m = NetworkController.MARGIN_RIGHT;
                break;
        }

        userConnection.setAttribute( m, component, this, state, 1, 0, value );
    }

    public void setMargin( State state, Margin margin, double value, long time ) {
        String m = "";
        switch ( margin ) {
            case BOTTOM:
                m = NetworkController.MARGIN_BOTTOM;
                break;
            case TOP:
                m = NetworkController.MARGIN_TOP;
                break;
            case LEFT:
                m = NetworkController.MARGIN_LEFT;
                break;
            case RIGHT:
                m = NetworkController.MARGIN_RIGHT;
                break;
        }

        userConnection.setAttribute( m, component, this, state, 1, time, value );
    }

    public void setPadding( State state, double value ) {
        userConnection.setAttribute( NetworkController.PADDING_BOTTOM, component, this, state, 1, 0, value );
        userConnection.setAttribute( NetworkController.PADDING_TOP, component, this, state, 1, 0, value );
        userConnection.setAttribute( NetworkController.PADDING_LEFT, component, this, state, 1, 0, value );
        userConnection.setAttribute( NetworkController.PADDING_RIGHT, component, this, state, 1, 0, value );
    }

    public void setPadding( State state, Padding padding, double value ) {
        String p = "";
        switch ( padding ) {
            case BOTTOM:
                p = NetworkController.PADDING_BOTTOM;
                break;
            case TOP:
                p = NetworkController.PADDING_TOP;
                break;
            case LEFT:
                p = NetworkController.PADDING_LEFT;
                break;
            case RIGHT:
                p = NetworkController.PADDING_RIGHT;
                break;
        }

        userConnection.setAttribute( p, component, this, state, 1, 0, value );
    }

    public void setPadding( State state, Padding padding, double value, long time ) {
        String p = "";
        switch ( padding ) {
            case BOTTOM:
                p = NetworkController.PADDING_BOTTOM;
                break;
            case TOP:
                p = NetworkController.PADDING_TOP;
                break;
            case LEFT:
                p = NetworkController.PADDING_LEFT;
                break;
            case RIGHT:
                p = NetworkController.PADDING_RIGHT;
                break;
        }

        userConnection.setAttribute( p, component, this, state, 1, time, value );
    }

    public void setBorder( State state, double value ) {
        userConnection.setAttribute( NetworkController.BORDER_BOTTOM, component, this, state, 1, 0, value );
        userConnection.setAttribute( NetworkController.BORDER_TOP, component, this, state, 1, 0, value );
        userConnection.setAttribute( NetworkController.BORDER_LEFT, component, this, state, 1, 0, value );
        userConnection.setAttribute( NetworkController.BORDER_RIGHT, component, this, state, 1, 0, value );
    }

    public void setBorder( State state, Border border, double value ) {
        String b = "";
        switch ( border ) {
            case BOTTOM:
                b = NetworkController.BORDER_BOTTOM;
                break;
            case TOP:
                b = NetworkController.BORDER_TOP;
                break;
            case LEFT:
                b = NetworkController.BORDER_LEFT;
                break;
            case RIGHT:
                b = NetworkController.BORDER_RIGHT;
                break;
        }

        userConnection.setAttribute( b, component, this, state, 1, 0, value );
    }

    public void setBorder( State state, Border border, double value, long time ) {
        String b = "";
        switch ( border ) {
            case BOTTOM:
                b = NetworkController.BORDER_BOTTOM;
                break;
            case TOP:
                b = NetworkController.BORDER_TOP;
                break;
            case LEFT:
                b = NetworkController.BORDER_LEFT;
                break;
            case RIGHT:
                b = NetworkController.BORDER_RIGHT;
                break;
        }

        userConnection.setAttribute( b, component, this, state, 1, time, value );
    }

    public void setBorderColor( State state, Color value ) {
        userConnection.setAttribute( NetworkController.BORDER_BOTTOM_COLOR, component, this, state, 1, 0, value );
        userConnection.setAttribute( NetworkController.BORDER_TOP_COLOR, component, this, state, 1, 0, value );
        userConnection.setAttribute( NetworkController.BORDER_LEFT_COLOR, component, this, state, 1, 0, value );
        userConnection.setAttribute( NetworkController.BORDER_RIGHT_COLOR, component, this, state, 1, 0, value );
    }

    public void setBorderColor( State state, Border border, Color value ) {
        String b = "";
        switch ( border ) {
            case BOTTOM:
                b = NetworkController.BORDER_BOTTOM_COLOR;
                break;
            case TOP:
                b = NetworkController.BORDER_TOP_COLOR;
                break;
            case LEFT:
                b = NetworkController.BORDER_LEFT_COLOR;
                break;
            case RIGHT:
                b = NetworkController.BORDER_RIGHT_COLOR;
                break;
        }

        userConnection.setAttribute( b, component, this, state, 1, 0, value );
    }

    public void setBorderColor( State state, Border border, Color value, long time ) {
        String b = "";
        switch ( border ) {
            case BOTTOM:
                b = NetworkController.BORDER_BOTTOM_COLOR;
                break;
            case TOP:
                b = NetworkController.BORDER_TOP_COLOR;
                break;
            case LEFT:
                b = NetworkController.BORDER_LEFT_COLOR;
                break;
            case RIGHT:
                b = NetworkController.BORDER_RIGHT_COLOR;
                break;
        }

        userConnection.setAttribute( b, component, this, state, 1, time, value );
    }

}
