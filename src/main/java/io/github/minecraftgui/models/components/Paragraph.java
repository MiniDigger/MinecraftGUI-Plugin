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
public class Paragraph extends ComponentText {

    private final Component buttonLineBefore;
    private final Component buttonLineAfter;

    public Paragraph(Class<? extends Rectangle> shape, Component buttonLineBefore, Component buttonLineAfter) {
        super(NetworkController.PARAGRAPH, shape);

        if(buttonLineAfter == null || buttonLineBefore == null)
            throw new ComponentException("The buttons can't be null.");

        this.buttonLineAfter = buttonLineAfter;
        this.buttonLineBefore = buttonLineBefore;
        specialChildren.add(buttonLineBefore);
        specialChildren.add(buttonLineAfter);
    }

    public Paragraph(Class<? extends Rectangle> shape, String id, Component buttonLineBefore, Component buttonLineAfter) {
        super(NetworkController.PARAGRAPH, shape, id);

        if(buttonLineAfter == null || buttonLineBefore == null)
            throw new ComponentException("The buttons can't be null.");

        this.buttonLineAfter = buttonLineAfter;
        this.buttonLineBefore = buttonLineBefore;
        specialChildren.add(buttonLineBefore);
        specialChildren.add(buttonLineAfter);
    }

    public void setNbLineVisible(int nb){
        userConnection.setTextNbLine(this, nb);
    }

    public void setTextAlignement(TextAlignment alignement) {
        userConnection.setTextAlignment(this, alignement);
    }

    public Component getButtonLineBefore() {
        return buttonLineBefore;
    }

    public Component getButtonLineAfter() {
        return buttonLineAfter;
    }

    @Override
    public Rectangle getShape() {
        return (Rectangle) super.getShape();
    }

}
