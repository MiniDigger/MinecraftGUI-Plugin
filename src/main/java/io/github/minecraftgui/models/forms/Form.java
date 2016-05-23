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

package io.github.minecraftgui.models.forms;

import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.listeners.OnClickListener;
import io.github.minecraftgui.models.listeners.OnFormSendListener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-01-04.
 */
public class Form {

    private Component button = null;
    private final ConcurrentHashMap<String, Valuable> valuables;
    private final CopyOnWriteArrayList<OnFormSendListener> listeners;
    private final OnClickListener onClickListener;

    public Form() {
        this.valuables = new ConcurrentHashMap<>();
        this.listeners = new CopyOnWriteArrayList<>();

        Form form = this;

        this.onClickListener = component -> {
            for (OnFormSendListener listener : listeners)
                listener.onFormSend(form);
        };
    }

    public Component getButton() {
        return button;
    }

    public void setButton(Component button){
        if(this.button != null)
            this.button.removeOnClickListener(onClickListener);

        this.button = button;
        this.button.addOnClickListener(onClickListener);
    }

    public void addValuable(String name, Valuable valuable){
        valuables.put(name.toLowerCase(), valuable);
    }

    public Valuable getValuable(String name){
        return valuables.get(name.toLowerCase());
    }

    public void addOnFormSendListener(OnFormSendListener listener){
        listeners.add(listener);
    }

    public void removeOnFormSendListener(OnFormSendListener listener){
        listeners.remove(listener);
    }
}
