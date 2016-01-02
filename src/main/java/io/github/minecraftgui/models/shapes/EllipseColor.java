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
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.State;

import java.awt.*;

/**
 * Created by Samuel on 2015-10-23.
 */
public class EllipseColor extends Shape<Color> {

    public EllipseColor(String componentShape, Component component) {
        super(NetworkController.ELLIPSE_COLOR, componentShape, component);
    }

    @Override
    public void setBackground(State state, Color value) {
        userConnection.setAttribute(NetworkController.BACKGROUND_COLOR, component, this,  state, 1, 0, value);
    }

    @Override
    public void setMargin(State state, Margin margin, double value) {

    }

    @Override
    public void setBorder(State state, Border border, double value) {

    }

    @Override
    public void setBorderColor(State state, Border border, Color value) {

    }

    @Override
    public void setPadding(State state, Padding padding, double value) {

    }

}
