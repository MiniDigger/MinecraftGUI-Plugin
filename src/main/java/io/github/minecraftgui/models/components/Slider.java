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
import io.github.minecraftgui.models.exceptions.ComponentException;
import io.github.minecraftgui.models.shapes.Rectangle;
import io.github.minecraftgui.models.shapes.Shape;

/**
 * Created by Samuel on 2015-12-30.
 */
public class Slider extends ComponentValuable<Double> {

    public enum Type{HORIZONTAL, VERTICAL}

    private final Shape shapeOnProgress;
    private final Component button;

    public Slider(Type type, Class<? extends Rectangle> shape, Class<? extends Rectangle> shapeOnProgress, Component button) {
        super((type == Type.HORIZONTAL? NetworkController.SLIDER_HORIZONTAL:NetworkController.SLIDER_VERTICAL), shape);
        this.shapeOnProgress = getShapeByClass(shapeOnProgress, NetworkController.SHAPE_ON_PROGRESS);

        if(button == null)
            throw new ComponentException("The button can't be null.");

        this.button = button;
        specialChildren.add(button);
    }

    public Slider(Type type, Class<? extends Rectangle> shape, Class<? extends Rectangle> shapeOnProgress, Component button, String id) {
        super((type == Type.HORIZONTAL? NetworkController.SLIDER_HORIZONTAL:NetworkController.SLIDER_VERTICAL), shape, id);
        this.shapeOnProgress = getShapeByClass(shapeOnProgress, NetworkController.SHAPE_ON_PROGRESS);

        if(button == null)
            throw new ComponentException("The button can't be null.");

        this.button = button;
        specialChildren.add(button);
    }

    public void setPercentage(double percentage){
        userConnection.setValue(this, percentage);
    }

    @Override
    protected void setShapeUserConnection() {
        super.setShapeUserConnection();
        this.shapeOnProgress.setUserConnection(userConnection);
    }

    public Component getButton() {
        return button;
    }

    @Override
    public Rectangle getShape() {
        return (Rectangle) super.getShape();
    }

    public Shape getShapeOnProgress() {
        return shapeOnProgress;
    }

}
