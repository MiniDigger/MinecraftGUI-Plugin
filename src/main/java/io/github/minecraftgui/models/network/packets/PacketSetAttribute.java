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
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.State;
import io.github.minecraftgui.models.shapes.Shape;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;

/**
 * Created by Samuel on 2015-12-31.
 */
public class PacketSetAttribute extends PacketOut {

    private final Component component;
    private final Shape shape;
    private final State state;
    private final Object value;
    private final double percentage;
    private final long time;
    private String componentIdToLink;
    private String componentAttributeToLink;
    private String componentShapeToLink;

    public PacketSetAttribute(Component component) {
        this.component = component;
        this.shape = component.getShape();
        this.state = State.NORMAL;
        this.percentage = 1;
        this.time = 0;
        this.value = "";
    }

    public PacketSetAttribute(Component component, Object value) {
        this.component = component;
        this.shape = component.getShape();
        this.state = State.NORMAL;
        this.percentage = 1;
        this.time = 0;
        this.value = value;
    }

    public PacketSetAttribute(Component component, Shape shape, double positions[][]) {
        this.component = component;
        this.shape = shape;
        this.state = State.NORMAL;
        this.percentage = 1;
        this.time = 0;
        this.value = new JSONArray();

        for(double pos[] : positions){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(NetworkController.X, pos[0]);
            jsonObject.put(NetworkController.Y, pos[1]);

            ((JSONArray) value).put(jsonObject);
        }
    }

    public PacketSetAttribute(Component component, State state, int value) {
        this.component = component;
        this.shape = component.getShape();
        this.state = state;
        this.percentage = 1;
        this.time = 0;
        this.value = value;
    }

    public PacketSetAttribute(Component component, State state, Color value) {
        this.component = component;
        this.shape = component.getShape();
        this.state = state;
        this.percentage = 1;
        this.time = 0;
        this.value = new JSONObject().put(NetworkController.R, value.getRed()).put(NetworkController.G, value.getGreen()).put(NetworkController.B, value.getBlue()).put(NetworkController.A, value.getAlpha());
    }

    public PacketSetAttribute(Component component, State state, String value) {
        this.component = component;
        this.shape = component.getShape();
        this.state = state;
        this.percentage = 1;
        this.time = 0;
        this.value = value;
    }

    public PacketSetAttribute(Component component, Shape shape, State state, String value) {
        this.component = component;
        this.shape = shape;
        this.state = state;
        this.percentage = 1;
        this.time = 0;
        this.value = value;
    }

    public PacketSetAttribute(Component component, Shape shape, State state, double percentage, long time, Double value) {
        this.component = component;
        this.shape = shape;
        this.state = state;
        this.percentage = percentage;
        this.time = time;
        this.value = value;
    }

    public PacketSetAttribute(Component component, Shape shape, State state, double percentage, long time, Color value) {
        this.component = component;
        this.shape = shape;
        this.state = state;
        this.percentage = percentage;
        this.time = time;
        this.value = new JSONObject().put(NetworkController.R, value.getRed()).put(NetworkController.G, value.getGreen()).put(NetworkController.B, value.getBlue()).put(NetworkController.A, value.getAlpha());
    }

    public PacketSetAttribute(Component component, Shape shape, State state, double percentage, long time, String componentIdToLink, String componentAttributeToLink, String componentShapeToLink) {
        this.component = component;
        this.shape = shape;
        this.state = state;
        this.percentage = percentage;
        this.time = time;
        this.value = null;
        this.componentIdToLink = componentIdToLink;
        this.componentAttributeToLink = componentAttributeToLink;
        this.componentShapeToLink = componentShapeToLink;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonAttr = new JSONObject();
        jsonObject.put(NetworkController.COMPONENT_ID, component.getUniqueId().toString());
        jsonObject.put(NetworkController.STATE, state.name().toUpperCase());
        jsonObject.put(NetworkController.ATTRIBUTE, jsonAttr);
        jsonObject.put(NetworkController.SHAPE, shape.getComponentShape());

        jsonAttr.put(NetworkController.TIME, time);
        jsonAttr.put(NetworkController.PERCENTAGE, percentage);

        if(value == null)
            jsonAttr.put(NetworkController.COMPONENT_ID, componentIdToLink).put(NetworkController.ATTRIBUTE, componentAttributeToLink).put(NetworkController.SHAPE, componentShapeToLink);
        else
            jsonAttr.put(NetworkController.VALUE, value);

        return jsonObject;
    }

}
