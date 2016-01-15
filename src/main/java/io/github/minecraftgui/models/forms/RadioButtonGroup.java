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

import io.github.minecraftgui.models.components.CheckBox;
import io.github.minecraftgui.models.components.ComponentValuable;
import io.github.minecraftgui.models.listeners.OnValueChangeListener;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-01-04.
 */
public class RadioButtonGroup implements Valuable<String> {

    private final ConcurrentHashMap<CheckBox, String> checkBoxes;
    private CheckBox current = null;
    private boolean skipNextEvent = false;

    public RadioButtonGroup(){
        this.checkBoxes = new ConcurrentHashMap<>();
    }

    public void addCheckBox(CheckBox checkBox, String value){
        this.checkBoxes.put(checkBox, value);

        checkBox.addOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(ComponentValuable component) {
                CheckBox box = (CheckBox) component;

                if(current != box) {
                    if (box.getValue().booleanValue()) {
                        Enumeration<CheckBox> checks = checkBoxes.keys();
                        current = box;

                        while(checks.hasMoreElements()) {
                            CheckBox check = checks.nextElement();

                            if (check != box)
                                check.setChecked(false);
                        }
                    }
                }
                else if(!skipNextEvent) {
                    box.setChecked(true);
                    skipNextEvent = true;
                }
                else
                    skipNextEvent = false;
            }
        });
    }

    @Override
    public String getValue() {
        return current == null?null:checkBoxes.get(current);
    }

}
