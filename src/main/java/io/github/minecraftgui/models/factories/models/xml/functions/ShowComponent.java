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

package io.github.minecraftgui.models.factories.models.xml.functions;

import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.components.Visibility;

/**
 * Created by Samuel on 2016-01-17.
 */
public class ShowComponent extends DelayedFunction {

    public ShowComponent(String[] args) {
        super(args);
    }

    @Override
    public void delayedEvent(UserGui userGui, Component component) {
        for(int i = 1; i < args.length; i++){
            Component comp = userGui.getComponent(args[i]);

            if(comp != null)
                comp.setVisibility(Visibility.VISIBLE);
        }
    }

}
