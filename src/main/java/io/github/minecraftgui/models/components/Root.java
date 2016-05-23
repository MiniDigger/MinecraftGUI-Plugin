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
import io.github.minecraftgui.models.network.UserConnection;
import io.github.minecraftgui.models.shapes.RectangleColor;

import java.util.UUID;

/**
 * Created by Samuel on 2016-01-02.
 */
public final class Root extends Component {

    public Root( UserGui userGui, UserConnection userConnection ) {
        super( "", RectangleColor.class );
        this.userGui = userGui;
        this.userConnection = userConnection;
    }

    @Override
    public UUID getUniqueId() {
        return NetworkController.ROOT_ID;
    }

    @Override
    public void remove() {
    }

    @Override
    public void add( Component component ) {
        if ( component.parent != null || component.userConnection != null || component.userGui != null ) {
            throw new ComponentException( "Can't add a component that is already assigned." );
        }

        component.parent = this;
        component.userGui = this.userGui;
        component.userConnection = this.userConnection;
        this.userGui.addComponent( component );
        this.userConnection.addComponent( component, true );
        this.children.add( component );

        component.init();
        userConnection.addEventListener( component, NetworkController.ON_REMOVE_LISTENER );

        for ( Component specialChild : component.specialChildren ) {
            component.add( specialChild );
        }

    }

    @Override
    public void add( Component component, UserGui gui ) {
        this.add( component );
    }

}
