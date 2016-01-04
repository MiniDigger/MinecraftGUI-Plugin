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
import io.github.minecraftgui.models.shapes.Rectangle;
import io.github.minecraftgui.models.shapes.Shape;

/**
 * Created by Samuel on 2015-12-30.
 */
public class ProgressBar extends ComponentValuable<Double> {

    public enum Type{HORIZONTAL, VERTICAL}

    private final Shape shapeOnProgress;

    public ProgressBar(Type type, Class<? extends Rectangle> shape, Class<? extends Rectangle> shapeOnProgress) {
        super((type == Type.HORIZONTAL? NetworkController.PROGRESS_BAR_HORIZONTAL:NetworkController.PROGRESS_BAR_VERTICAL), shape);
        this.shapeOnProgress = getShapeByClass(shapeOnProgress, NetworkController.SHAPE_ON_PROGRESS);
    }

    public ProgressBar(Type type, Class<? extends Rectangle> shape, Class<? extends Rectangle> shapeOnProgress, String id) {
        super((type == Type.HORIZONTAL? NetworkController.PROGRESS_BAR_HORIZONTAL:NetworkController.PROGRESS_BAR_VERTICAL), shape, id);
        this.shapeOnProgress = getShapeByClass(shapeOnProgress, NetworkController.SHAPE_ON_PROGRESS);
    }

    public void setPercentage(double percentage){
        userConnection.setValue(this, percentage);
    }

    @Override
    protected void init() {
        super.init();
        this.shapeOnProgress.setUserConnection(userConnection);
    }

    @Override
    public Rectangle getShape() {
        return (Rectangle) super.getShape();
    }

    public Shape getShapeOnProgress() {
        return shapeOnProgress;
    }
}
