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

import io.github.minecraftgui.models.components.*;
import io.github.minecraftgui.models.listeners.OnClickListener;
import io.github.minecraftgui.models.shapes.RectangleColor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-01-04.
 */
public class Dropdown implements Valuable<String> {

    private Paragraph paragraphValueDisplayed;
    private final ConcurrentHashMap<Component, String> values;
    private List list;
    private boolean isListVisible = false;
    private Component lastComponentClicked = null;
    private OnClickListener onClickListener;

    public Dropdown() {
        this.values = new ConcurrentHashMap<>();
        this.onClickListener = new OnClickListener() {
            @Override
            public void onClick(Component component) {
                if(isListVisible)
                    setListVisibility(Visibility.INVISIBLE);
                else
                    setListVisibility(Visibility.VISIBLE);

                isListVisible = !isListVisible;
            }
        };
    }

    public void setParagraphValueDisplayed(Paragraph paragraphValueDisplayed) {
        if(this.paragraphValueDisplayed != null)
            this.paragraphValueDisplayed.removeOnClickListener(onClickListener);

        this.paragraphValueDisplayed = paragraphValueDisplayed;
        this.paragraphValueDisplayed.addOnClickListener(onClickListener);
    }

    public void setList(List list) {
        this.list = list;
    }

    public List getList() {
        return list;
    }

    public Paragraph getParagraphValueDisplayed() {
        return paragraphValueDisplayed;
    }

    public void init(String valueDisplayed){
        list.update();
        list.setVisibility(Visibility.INVISIBLE);
        paragraphValueDisplayed.setText(valueDisplayed);
    }

    public Paragraph addValue(String value){
        if(list != null) {
            Paragraph paragraph = new Paragraph(RectangleColor.class, new Div(RectangleColor.class), new Div(RectangleColor.class));
            list.add(paragraph);

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

            return paragraph;
        }

        return null;
    }

    @Override
    public String getValue() {
        return lastComponentClicked == null?null:values.get(lastComponentClicked);
    }

    private void setListVisibility(Visibility visibility){
        if(list != null)
            list.setVisibility(visibility);
    }

}
