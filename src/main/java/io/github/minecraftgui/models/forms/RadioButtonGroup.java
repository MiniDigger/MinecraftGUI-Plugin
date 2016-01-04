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

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-01-04.
 */
public class RadioButtonGroup implements Valuable<Integer> {

    private final CopyOnWriteArrayList<CheckBox> checkBoxes;
    private CheckBox current = null;
    private boolean skipNextEvent = false;

    public RadioButtonGroup(){
        this.checkBoxes = new CopyOnWriteArrayList<>();
    }

    public void addCheckBox(CheckBox checkBox){
        this.checkBoxes.add(checkBox);

        checkBox.addOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(ComponentValuable component) {
                CheckBox box = (CheckBox) component;

                System.out.println(((CheckBox) component).getValue());

                if(current != box) {
                    if (box.getValue().booleanValue()) {
                        current = box;

                        for (CheckBox check : checkBoxes)
                            if (check != box)
                                check.setChecked(false);
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
    public Integer getValue() {
        return checkBoxes.indexOf(current);
    }

}
