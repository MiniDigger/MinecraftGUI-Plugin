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

/**
 * Created by Samuel on 2015-12-30.
 */
public class List extends Component {

    private final Component buttonListBefore;
    private final Component buttonListAfter;

    public List(Class<? extends Rectangle> shape, Component buttonListBefore, Component buttonListAfter) {
        super(NetworkController.LIST, shape);

        if(buttonListAfter == null || buttonListBefore == null)
            throw new ComponentException("The buttons can't be null.");

        this.buttonListAfter = buttonListAfter;
        this.buttonListBefore = buttonListBefore;
        specialChildren.add(buttonListBefore);
        specialChildren.add(buttonListAfter);
    }

    public List(Class<? extends Rectangle> shape, Component buttonListBefore, Component buttonListAfter, String id) {
        super(NetworkController.LIST, shape, id);

        if(buttonListAfter == null || buttonListBefore == null)
            throw new ComponentException("The buttons can't be null.");

        this.buttonListAfter = buttonListAfter;
        this.buttonListBefore = buttonListBefore;
        specialChildren.add(buttonListBefore);
        specialChildren.add(buttonListAfter);
    }

    public void update(){
        userConnection.updateList(this);
    }

    public void setNbComponentPerList(int nb){
        userConnection.setListNbComponent(this, nb);
    }

    public Component getButtonListBefore() {
        return buttonListBefore;
    }

    public Component getButtonListAfter() {
        return buttonListAfter;
    }

    @Override
    public Rectangle getShape() {
        return (Rectangle) super.getShape();
    }
}
