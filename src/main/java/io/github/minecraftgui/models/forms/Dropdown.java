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
import io.github.minecraftgui.models.components.List;
import io.github.minecraftgui.models.components.Paragraph;
import io.github.minecraftgui.models.components.Visibility;
import io.github.minecraftgui.models.listeners.OnClickListener;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-01-04.
 */
public class Dropdown implements Valuable<String> {

    private final Paragraph paragraphValueDisplayed;
    private final ConcurrentHashMap<Component, String> values;
    private final List list;
    private boolean isListVisible = false;
    private Component lastComponentClicked = null;

    public Dropdown(Paragraph paragraph, List list) {
        this.paragraphValueDisplayed = paragraph;
        this.list = list;
        this.values = new ConcurrentHashMap<>();

        this.paragraphValueDisplayed.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Component component) {
                if(isListVisible)
                    list.setVisibility(Visibility.INVISIBLE);
                else
                    list.setVisibility(Visibility.VISIBLE);

                isListVisible = !isListVisible;
            }
        });
    }

    public void init(String valueDisplayed){
        list.update();
        list.setVisibility(Visibility.INVISIBLE);
        paragraphValueDisplayed.setText(valueDisplayed);
    }

    public void addValue(Paragraph paragraph, String value){
        values.put(paragraph, value);

        paragraph.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Component component) {
                Paragraph para = (Paragraph) component;
                lastComponentClicked = component;

                paragraphValueDisplayed.setText(para.getText());
                list.setVisibility(Visibility.INVISIBLE);
                isListVisible = false;
            }
        });
    }

    @Override
    public String getValue() {
        return values.get(lastComponentClicked);
    }

}
