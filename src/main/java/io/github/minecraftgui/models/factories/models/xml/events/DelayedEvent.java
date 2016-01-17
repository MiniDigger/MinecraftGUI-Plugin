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

package io.github.minecraftgui.models.factories.models.xml.events;

import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.UserGui;

/**
 * Created by Samuel on 2016-01-17.
 */
public abstract class DelayedEvent extends Event {

    private final long time;

    public abstract void delayedEvent(UserGui userGui, Component component);

    public DelayedEvent(String[] args) {
        super(args);
        if(args.length >= 1)
            time = Long.parseLong(args[0]);
        else
            time = 0;
    }

    @Override
    public void event(UserGui userGui, Component component) {
        if (time != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    delayedEvent(userGui, component);
                }
            }).start();
        }
        else
            delayedEvent(userGui, component);
    }

}
