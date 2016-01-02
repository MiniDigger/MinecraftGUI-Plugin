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
import io.github.minecraftgui.models.shapes.RectangleColor;
import io.github.minecraftgui.models.shapes.Shape;

/**
 * Created by Samuel on 2015-12-30.
 */
public class CheckBox extends ComponentValuable<Boolean>{

    private final Shape shapeOnValueTrue;
    private final Shape shapeOnValueFalse;

    public CheckBox(Class<? extends Shape> shapeOnValueTrue, Class<? extends Shape> shapeOnValueFalse) {
        super(NetworkController.CHECKBOX, RectangleColor.class);
        this.value = false;
        this.shapeOnValueTrue = getShapeByClass(shapeOnValueTrue, NetworkController.SHAPE_ON_VALUE_TRUE);
        this.shapeOnValueFalse = getShapeByClass(shapeOnValueFalse, NetworkController.SHAPE_ON_VALUE_TRUE);
        this.shape = this.shapeOnValueTrue;
    }

    public CheckBox(Class<? extends Shape> shapeOnValueTrue, Class<? extends Shape> shapeOnValueFalse, String id) {
        super(NetworkController.CHECKBOX, RectangleColor.class, id);
        this.value = false;
        this.shapeOnValueTrue = getShapeByClass(shapeOnValueTrue, NetworkController.SHAPE_ON_VALUE_TRUE);
        this.shapeOnValueFalse = getShapeByClass(shapeOnValueFalse, NetworkController.SHAPE_ON_VALUE_TRUE);
        this.shape = this.shapeOnValueTrue;
    }

    public void setChecked(boolean checked){
        userConnection.setValue(this, checked);
    }

    @Override
    protected void setShapeUserConnection() {
        super.setShapeUserConnection();
        this.shapeOnValueTrue.setUserConnection(userConnection);
        this.shapeOnValueFalse.setUserConnection(userConnection);
    }

    public Shape getShapeOnValueTrue() {
        return shapeOnValueTrue;
    }

    public Shape getShapeOnValueFalse() {
        return shapeOnValueFalse;
    }
}
