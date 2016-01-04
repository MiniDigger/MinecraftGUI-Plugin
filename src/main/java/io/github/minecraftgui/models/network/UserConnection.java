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

package io.github.minecraftgui.models.network;

import io.github.minecraftgui.controllers.NetworkController;
import io.github.minecraftgui.models.components.*;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.Cursor;
import io.github.minecraftgui.models.components.List;
import io.github.minecraftgui.models.listeners.OnGuiListener;
import io.github.minecraftgui.models.network.packets.*;
import io.github.minecraftgui.models.shapes.Shape;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2015-12-29.
 */
public class UserConnection {

    private final ConcurrentHashMap<OnGuiListener, UserGui> userGuis;
    private final ConcurrentHashMap<UUID, Component> components;
    private final NetworkController networkController;
    private final UUID uuid;

    public UserConnection(NetworkController networkController, UUID uuid) {
        this.networkController = networkController;
        this.uuid = uuid;
        this.components = new ConcurrentHashMap<>();
        this.userGuis = new ConcurrentHashMap<>();

        for(OnGuiListener plugin : networkController.getPlugins())
            userGuis.put(plugin, new UserGui(this));
    }

    public void reloadGui(){
        sendPacket(NetworkController.PACKET_INIT_INTERFACE, new PacketInitInterface());
    }

    public UUID getUuid() {
        return uuid;
    }

    public final Component getComponent(UUID uuid){
        return components.get(uuid);
    }

    public UserGui getUserGui(OnGuiListener plugin){
        return userGuis.get(plugin);
    }

    public void addEventListener(Component component, String event){
        sendPacket(NetworkController.PACKET_ADD_EVENT, new PacketAddEventListener(component, event));
    }

    public void updateList(List list){
        sendPacket(NetworkController.PACKET_UPDATE_LIST, new PacketSetAttribute(list));
    }

    public void setListNbComponent(Component component, int nb){
        sendPacket(NetworkController.PACKET_SET_LIST_NB_COMPONENT, new PacketSetAttribute(component, nb));
    }

    public void setTextNbLine(Component component, int nb){
        sendPacket(NetworkController.PACKET_SET_TEXT_NB_LINE, new PacketSetAttribute(component, nb));
    }

    public void setPositions(Component component, Shape shape, double positions[][]){
        sendPacket(NetworkController.PACKET_SET_POSITIONS, new PacketSetAttribute(component, shape, positions));
    }

    public void setTextAlignment(Component component, Object value){
        sendPacket(NetworkController.PACKET_SET_TEXT_ALIGNMEMT, new PacketSetAttribute(component, value));
    }

    public void setValue(Component component, Object value){
        sendPacket(NetworkController.PACKET_SET_VALUE, new PacketSetAttribute(component, value));
    }

    public void setFontSize(Component component, State state, int value){
        sendPacket(NetworkController.PACKET_SET_ATTRIBUTE_FONT_SIZE, new PacketSetAttribute(component, state, value));
    }

    public void setFont(Component component, State state, String value){
        sendPacket(NetworkController.PACKET_SET_ATTRIBUTE_FONT, new PacketSetAttribute(component, state, value));
    }

    public void setFontColor(Component component, State state, Color value){
        sendPacket(NetworkController.PACKET_SET_ATTRIBUTE_FONT_COLOR, new PacketSetAttribute(component, state, value));
    }

    public void setCursorColor(Component component, State state, Color value){
        sendPacket(NetworkController.PACKET_SET_ATTRIBUTE_CURSOR_COLOR, new PacketSetAttribute(component, state, value));
    }

    public void setBackgroundImage(Component component, Shape shape, State state, String image){
        sendPacket(NetworkController.PACKET_SET_ATTRIBUTE_BACKGROUND_IMAGE, new PacketSetAttribute(component, shape, state, image));
    }

    public void setVisibility(Component component, Visibility visibility){
        sendPacket(NetworkController.PACKET_SET_VISIBILITY, new PacketSetAttribute(component, visibility.name().toUpperCase()));
    }

    public void setCursor(Component component, State state, Cursor cursor){
        sendPacket(NetworkController.PACKET_SET_CURSOR, new PacketSetAttribute(component, state, cursor.name().toUpperCase()));
    }

    public void setAttribute(String attribute, Component component, Shape shape, State state, double percentage, long time, Double value){
        int packetId = -1;

        switch (attribute){
            case NetworkController.WIDTH: packetId = NetworkController.PACKET_SET_ATTRIBUTE_WIDTH; break;
            case NetworkController.HEIGHT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_HEIGHT; break;
            case NetworkController.RELATIVE_X: packetId = NetworkController.PACKET_SET_RELATIVE_X; break;
            case NetworkController.RELATIVE_Y: packetId = NetworkController.PACKET_SET_RELATIVE_Y; break;
            case NetworkController.BORDER_BOTTOM: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_BOTTOM; break;
            case NetworkController.BORDER_LEFT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_LEFT; break;
            case NetworkController.BORDER_RIGHT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_RIGHT; break;
            case NetworkController.BORDER_TOP: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_TOP; break;
            case NetworkController.PADDING_BOTTOM: packetId = NetworkController.PACKET_SET_ATTRIBUTE_PADDING_BOTTOM; break;
            case NetworkController.PADDING_LEFT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_PADDING_LEFT; break;
            case NetworkController.PADDING_RIGHT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_PADDING_RIGHT; break;
            case NetworkController.PADDING_TOP: packetId = NetworkController.PACKET_SET_ATTRIBUTE_PADDING_TOP; break;
            case NetworkController.MARGIN_BOTTOM: packetId = NetworkController.PACKET_SET_ATTRIBUTE_MARGIN_BOTTOM; break;
            case NetworkController.MARGIN_LEFT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_MARGIN_LEFT; break;
            case NetworkController.MARGIN_RIGHT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_MARGIN_RIGHT; break;
            case NetworkController.MARGIN_TOP: packetId = NetworkController.PACKET_SET_ATTRIBUTE_MARGIN_TOP; break;
        }

        sendPacket(packetId, new PacketSetAttribute(component, shape, state, percentage, time, value));
    }

    public void setAttribute(String attribute, Component component, Shape shape, State state, double percentage, long time, Color value){
        int packetId = -1;

        switch (attribute){
            case NetworkController.BACKGROUND_COLOR: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BACKGROUND_COLOR; break;
            case NetworkController.BORDER_BOTTOM_COLOR: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_BOTTOM_COLOR; break;
            case NetworkController.BORDER_LEFT_COLOR: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_LEFT_COLOR; break;
            case NetworkController.BORDER_RIGHT_COLOR: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_RIGHT_COLOR; break;
            case NetworkController.BORDER_TOP_COLOR: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_TOP_COLOR; break;
        }

        sendPacket(packetId, new PacketSetAttribute(component, shape, state, percentage, time, value));
    }

    public void setAttribute(String attribute, Component component, Shape shape, State state, double percentage, long time, Shape componentShapeRelativeTo, Attribute componentShapeAttributeRelativeTo){
        int packetId = -1;

        switch (attribute){
            case NetworkController.WIDTH: packetId = NetworkController.PACKET_SET_ATTRIBUTE_WIDTH; break;
            case NetworkController.HEIGHT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_HEIGHT; break;
            case NetworkController.BACKGROUND_COLOR: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BACKGROUND_COLOR; break;
            case NetworkController.COMPONENT_RELATIVE_TO_X: packetId = NetworkController.PACKET_ADD_COMPONENT_RELATIVE_TO_X; break;
            case NetworkController.COMPONENT_RELATIVE_TO_Y: packetId = NetworkController.PACKET_ADD_COMPONENT_RELATIVE_TO_Y; break;
            case NetworkController.BORDER_BOTTOM_COLOR: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_BOTTOM_COLOR; break;
            case NetworkController.BORDER_LEFT_COLOR: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_LEFT_COLOR; break;
            case NetworkController.BORDER_RIGHT_COLOR: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_RIGHT_COLOR; break;
            case NetworkController.BORDER_TOP_COLOR: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_TOP_COLOR; break;
            case NetworkController.BORDER_BOTTOM: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_BOTTOM; break;
            case NetworkController.BORDER_LEFT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_LEFT; break;
            case NetworkController.BORDER_RIGHT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_RIGHT; break;
            case NetworkController.BORDER_TOP: packetId = NetworkController.PACKET_SET_ATTRIBUTE_BORDER_TOP; break;
            case NetworkController.PADDING_BOTTOM: packetId = NetworkController.PACKET_SET_ATTRIBUTE_PADDING_BOTTOM; break;
            case NetworkController.PADDING_LEFT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_PADDING_LEFT; break;
            case NetworkController.PADDING_RIGHT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_PADDING_RIGHT; break;
            case NetworkController.PADDING_TOP: packetId = NetworkController.PACKET_SET_ATTRIBUTE_PADDING_TOP; break;
            case NetworkController.MARGIN_BOTTOM: packetId = NetworkController.PACKET_SET_ATTRIBUTE_MARGIN_BOTTOM; break;
            case NetworkController.MARGIN_LEFT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_MARGIN_LEFT; break;
            case NetworkController.MARGIN_RIGHT: packetId = NetworkController.PACKET_SET_ATTRIBUTE_MARGIN_RIGHT; break;
            case NetworkController.MARGIN_TOP: packetId = NetworkController.PACKET_SET_ATTRIBUTE_MARGIN_TOP; break;
        }

        sendPacket(packetId, new PacketSetAttribute(component, shape, state, percentage, time, componentShapeRelativeTo.getComponent().getUniqueId().toString(), componentShapeAttributeRelativeTo.getName(), componentShapeRelativeTo.getComponentShape()));
    }

    public final void addComponent(Component component, boolean createComponent){
        components.put(component.getUniqueId(), component);

        if(createComponent)
            sendPacket(NetworkController.PACKET_CREATE_COMPONENT, new PacketCreateComponent(component));
    }

    public final void removeComponent(Component component){
        sendPacket(NetworkController.PACKET_DELETE_COMPONENT, new PacketDeleteComponent(component));
    }

    public void receivePacket(JSONObject jsonObject){
        int packetId = jsonObject.getInt(NetworkController.PACKET_ID);
        JSONObject content = jsonObject.getJSONObject(NetworkController.CONTENT);

        switch (packetId){
            case NetworkController.PACKET_INIT_CONNECTION:
                ArrayList<String> fonts = new ArrayList<>();
                HashMap<String, String> images = new HashMap<>();
                HashMap<String, HashMap<Color, ArrayList<Integer>>> fontsGenerate = new HashMap<>();
                HashMap<Color, ArrayList<Integer>> font = new HashMap<>();

                fonts.add("http://www.1001freefonts.com/d/325/orange_juice.zip");

                images.put("http://media3.giphy.com/media/tp1U2lhRChsDC/200w.gif", "google.gif");

                ArrayList<Integer> sizes = new ArrayList<>();
                font.put(Color.BLACK, sizes);
                sizes.add(24);

                fontsGenerate.put("orange juice", font);

                sendPacket(NetworkController.PACKET_INIT_CLIENT, new PacketInitClient(fonts, images, fontsGenerate));
                break;
            case NetworkController.PACKET_CLIENT_INITIATED: sendPacket(NetworkController.PACKET_INIT_INTERFACE, new PacketInitInterface()); break;
            case NetworkController.PACKET_INTERFACE_INITIATED: onGuiInit(); break;
            case NetworkController.PACKET_EVENT_ON_GUI_CLOSE: onGuiClose(); break;
            case NetworkController.PACKET_EVENT_ON_GUI_OPEN: onGuiOpen(); break;
            case NetworkController.PACKET_EVENT_ON_CLICK: new PacketComponentEvent.OnClick(this, content); break;
            case NetworkController.PACKET_EVENT_ON_DOUBLE_CLICK: new PacketComponentEvent.OnDoubleClick(this, content); break;
            case NetworkController.PACKET_EVENT_ON_BLUR: new PacketComponentEvent.OnBlur(this, content); break;
            case NetworkController.PACKET_EVENT_ON_FOCUS: new PacketComponentEvent.OnFocus(this, content); break;
            case NetworkController.PACKET_EVENT_ON_INPUT: new PacketComponentEvent.OnInput(this, content); break;
            case NetworkController.PACKET_EVENT_ON_KEY_PRESSED: new PacketComponentEvent.OnKeyPressed(this, content); break;
            case NetworkController.PACKET_EVENT_ON_REMOVE:
                PacketComponentEvent event = new PacketComponentEvent.OnRemove(this, content);
                components.remove(event.getComponent().getUniqueId());
                System.out.println(components.size());
                break;
            case NetworkController.PACKET_EVENT_ON_VALUE_CHANGED: new PacketComponentEvent.OnValueChange(this, content); break;
        }
    }

    private void onGuiInit(){
        Enumeration<OnGuiListener> listeners = userGuis.keys();

        while(listeners.hasMoreElements()) {
            OnGuiListener listener = listeners.nextElement();

            listener.onGuiInit(userGuis.get(listener));
        }
    }

    private void onGuiClose(){
        Enumeration<OnGuiListener> listeners = userGuis.keys();

        while(listeners.hasMoreElements()) {
            OnGuiListener listener = listeners.nextElement();

            listener.onGuiClose(userGuis.get(listener));
        }
    }

    private void onGuiOpen(){
        Enumeration<OnGuiListener> listeners = userGuis.keys();

        while(listeners.hasMoreElements()) {
            OnGuiListener listener = listeners.nextElement();

            listener.onGuiOpen(userGuis.get(listener));
        }
    }

    private void sendPacket(int packetId, PacketOut packetOut){
        networkController.sendPacktTo(uuid, generateJSONHeader(packetId, packetOut.toJSON()));
    }

    private JSONObject generateJSONHeader(int id, JSONObject content){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(NetworkController.PACKET_ID, id);
        jsonObject.put(NetworkController.CONTENT, content);

        return jsonObject;
    }

}
