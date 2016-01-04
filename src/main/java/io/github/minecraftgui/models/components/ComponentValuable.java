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
import io.github.minecraftgui.models.listeners.OnValueChangeListener;
import io.github.minecraftgui.models.shapes.Shape;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2015-12-30.
 */
public abstract class ComponentValuable<V> extends Component {

    private final CopyOnWriteArrayList<OnValueChangeListener> listeners;
    protected V value;

    public ComponentValuable(String type, Class<? extends Shape> shape) {
        super(type, shape);
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public ComponentValuable(String type, Class<? extends Shape> shape, String id) {
        super(type, shape, id);
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public final void addOnValueChangeListener(OnValueChangeListener listener){
        if(listeners.size() == 0)
            userConnection.addEventListener(this, NetworkController.ON_VALUE_CHANGE_LISTENER);

        this.listeners.add(listener);
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value){
        this.value = value;

        for(OnValueChangeListener listener : listeners)
            listener.onValueChange(this);
    }

}
