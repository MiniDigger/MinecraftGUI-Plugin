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
public class AttributeColor extends Attribute {

    //TODO is this class still usefull?

    public static final AttributeColor BACKGROUND_COLOR = new AttributeColor(NetworkController.BACKGROUND_COLOR);
    public static final AttributeColor BORDER_TOP_COLOR = new AttributeColor(NetworkController.BORDER_TOP_COLOR);
    public static final AttributeColor BORDER_LEFT_COLOR = new AttributeColor(NetworkController.BORDER_LEFT_COLOR);
    public static final AttributeColor BORDER_RIGHT_COLOR = new AttributeColor(NetworkController.BORDER_RIGHT_COLOR);
    public static final AttributeColor BORDER_BOTTOM_COLOR = new AttributeColor(NetworkController.BORDER_BOTTOM_COLOR);

    private AttributeColor(String name) {
        super(name);
    }

}