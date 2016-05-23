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

package io.github.minecraftgui.models.network.packets;

import io.github.minecraftgui.controllers.NetworkController;
import io.github.minecraftgui.models.components.CheckBox;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.List;
import io.github.minecraftgui.models.components.Paragraph;
import io.github.minecraftgui.models.components.ProgressBar;
import io.github.minecraftgui.models.components.Slider;
import io.github.minecraftgui.models.components.TextArea;
import org.json.JSONObject;

/**
 * Created by Samuel on 2015-12-30.
 */
public class PacketCreateComponent extends PacketOut {

    private final Component component;

    public PacketCreateComponent( Component component ) {
        this.component = component;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put( NetworkController.PARENT_ID, component.getParent().getUniqueId().toString() );
        jsonObject.put( NetworkController.COMPONENT, generateJSONObject( component ) );

        return jsonObject;
    }

    private JSONObject generateJSONObject( Component component ) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put( NetworkController.COMPONENT_ID, component.getUniqueId().toString() );
        jsonObject.put( NetworkController.TYPE, component.getType() );
        jsonObject.put( NetworkController.SHAPE, component.getShape().getType() );

        switch ( component.getType() ) {
            case NetworkController.CHECKBOX:
                CheckBox checkBox = (CheckBox) component;
                jsonObject.put( NetworkController.SHAPE_ON_VALUE_TRUE, checkBox.getShapeOnValueTrue().getType() );
                jsonObject.put( NetworkController.SHAPE_ON_VALUE_FALSE, checkBox.getShapeOnValueFalse().getType() );
                break;
            case NetworkController.DIV:
                break;
            case NetworkController.INPUT:
                break;
            case NetworkController.PROGRESS_BAR_VERTICAL:
                ProgressBar progressBarV = (ProgressBar) component;
                jsonObject.put( NetworkController.SHAPE_ON_PROGRESS, progressBarV.getShapeOnProgress().getType() );
                break;
            case NetworkController.PROGRESS_BAR_HORIZONTAL:
                ProgressBar progressBarH = (ProgressBar) component;
                jsonObject.put( NetworkController.SHAPE_ON_PROGRESS, progressBarH.getShapeOnProgress().getType() );
                break;
            case NetworkController.LIST:
                List list = (List) component;
                jsonObject.put( NetworkController.BUTTON_LIST_BEFORE, generateJSONObject( list.getButtonListBefore() ) );
                jsonObject.put( NetworkController.BUTTON_LIST_AFTER, generateJSONObject( list.getButtonListAfter() ) );
                break;
            case NetworkController.PARAGRAPH:
                Paragraph paragraph = (Paragraph) component;
                jsonObject.put( NetworkController.BUTTON_LINE_BEFORE, generateJSONObject( paragraph.getButtonLineBefore() ) );
                jsonObject.put( NetworkController.BUTTON_LINE_AFTER, generateJSONObject( paragraph.getButtonLineAfter() ) );
                break;
            case NetworkController.SLIDER_VERTICAL:
                Slider sliderV = (Slider) component;
                jsonObject.put( NetworkController.SHAPE_ON_PROGRESS, sliderV.getShapeOnProgress().getType() );
                jsonObject.put( NetworkController.SLIDER_BUTTON, generateJSONObject( sliderV.getButton() ) );
                break;
            case NetworkController.SLIDER_HORIZONTAL:
                Slider sliderH = (Slider) component;
                jsonObject.put( NetworkController.SHAPE_ON_PROGRESS, sliderH.getShapeOnProgress().getType() );
                jsonObject.put( NetworkController.SLIDER_BUTTON, generateJSONObject( sliderH.getButton() ) );
                break;
            case NetworkController.TEXT_AREA:
                TextArea textArea = (TextArea) component;
                jsonObject.put( NetworkController.BUTTON_LINE_BEFORE, generateJSONObject( textArea.getButtonLineBefore() ) );
                jsonObject.put( NetworkController.BUTTON_LINE_AFTER, generateJSONObject( textArea.getButtonLineAfter() ) );
                break;
        }

        return jsonObject;
    }

}
