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

import io.github.minecraftgui.models.shapes.Shape;

import java.awt.*;

/**
 * Created by Samuel on 2016-01-02.
 */
public abstract class ComponentText extends ComponentValuable<String> {

    public ComponentText(String type, Class<? extends Shape> shape) {
        super(type, shape);
    }

    public ComponentText(String type, Class<? extends Shape> shape, String id) {
        super(type, shape, id);
    }

    public String getText(){
        return this.value;
    }

    public void setText(String text){
        userConnection.setValue(this, text);
    }

    public void setFont(State state, String font){
        userConnection.setFont(this, state, font);
    }

    public void setFontSize(State state, int size){
        userConnection.setFontSize(this, state, size);
    }

    public void setFontColor(State state, Color color){
        userConnection.setFontColor(this, state, color);
    }

}
