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

package io.github.minecraftgui.controllers;

import io.github.minecraftgui.models.listeners.OnGuiListener;
import io.github.minecraftgui.models.network.UserConnection;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2015-12-13.
 */
public abstract class NetworkController {

    public static final String MINECRAFT_GUI_CHANNEL = "MinecraftGUI";
    public static final UUID ROOT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static final int PACKET_INIT_CONNECTION = 0;
    public static final int PACKET_INIT_CLIENT = 1;
    public static final int PACKET_CLIENT_INITIATED = 2;
    public static final int PACKET_INIT_INTERFACE = 3;
    public static final int PACKET_INTERFACE_INITIATED = 4;
    public static final int PACKET_CREATE_COMPONENT = 5;
    public static final int PACKET_DELETE_COMPONENT = 6;
    public static final int PACKET_ADD_EVENT = 7;

    public static final int PACKET_EVENT_ON_CLICK = 100;
    public static final int PACKET_EVENT_ON_DOUBLE_CLICK = 102;
    public static final int PACKET_EVENT_ON_INPUT = 103;
    public static final int PACKET_EVENT_ON_KEY_PRESSED = 104;
    public static final int PACKET_EVENT_ON_VALUE_CHANGED = 105;
    public static final int PACKET_EVENT_ON_BLUR = 106;
    public static final int PACKET_EVENT_ON_FOCUS = 107;
    public static final int PACKET_EVENT_ON_REMOVE = 108;
    public static final int PACKET_EVENT_ON_GUI_OPEN = 109;
    public static final int PACKET_EVENT_ON_GUI_CLOSE = 110;
    public static final int PACKET_EVENT_ON_MOUSE_ENTER = 111;
    public static final int PACKET_EVENT_ON_MOUSE_LEAVE = 112;

    public static final int PACKET_SET_ATTRIBUTE_BACKGROUND_COLOR = 1000;
    public static final int PACKET_SET_ATTRIBUTE_WIDTH = 1001;
    public static final int PACKET_SET_ATTRIBUTE_HEIGHT = 1002;
    public static final int PACKET_ADD_COMPONENT_RELATIVE_TO_X = 1003;
    public static final int PACKET_ADD_COMPONENT_RELATIVE_TO_Y = 1004;
    public static final int PACKET_SET_RELATIVE_X = 1005;
    public static final int PACKET_SET_RELATIVE_Y = 1006;
    public static final int PACKET_SET_ATTRIBUTE_BORDER_TOP = 1007;
    public static final int PACKET_SET_ATTRIBUTE_BORDER_LEFT = 1008;
    public static final int PACKET_SET_ATTRIBUTE_BORDER_RIGHT = 1009;
    public static final int PACKET_SET_ATTRIBUTE_BORDER_BOTTOM = 1010;
    public static final int PACKET_SET_ATTRIBUTE_BORDER_TOP_COLOR = 1011;
    public static final int PACKET_SET_ATTRIBUTE_BORDER_LEFT_COLOR = 1012;
    public static final int PACKET_SET_ATTRIBUTE_BORDER_RIGHT_COLOR = 1013;
    public static final int PACKET_SET_ATTRIBUTE_BORDER_BOTTOM_COLOR = 1014;
    public static final int PACKET_SET_ATTRIBUTE_PADDING_TOP = 1015;
    public static final int PACKET_SET_ATTRIBUTE_PADDING_LEFT = 1016;
    public static final int PACKET_SET_ATTRIBUTE_PADDING_RIGHT = 1017;
    public static final int PACKET_SET_ATTRIBUTE_PADDING_BOTTOM = 1018;
    public static final int PACKET_SET_ATTRIBUTE_MARGIN_TOP = 1019;
    public static final int PACKET_SET_ATTRIBUTE_MARGIN_LEFT = 1020;
    public static final int PACKET_SET_ATTRIBUTE_MARGIN_RIGHT = 1021;
    public static final int PACKET_SET_ATTRIBUTE_MARGIN_BOTTOM = 1022;
    public static final int PACKET_SET_CURSOR = 1023;
    public static final int PACKET_SET_VISIBILITY = 1024;
    public static final int PACKET_SET_ATTRIBUTE_BACKGROUND_IMAGE = 1025;
    public static final int PACKET_SET_ATTRIBUTE_FONT = 1026;
    public static final int PACKET_SET_ATTRIBUTE_FONT_COLOR = 1027;
    public static final int PACKET_SET_ATTRIBUTE_FONT_SIZE = 1028;
    public static final int PACKET_SET_ATTRIBUTE_CURSOR_COLOR = 1029;
    public static final int PACKET_SET_VALUE = 1030;
    public static final int PACKET_SET_TEXT_ALIGNMEMT = 1031;
    public static final int PACKET_SET_POSITIONS = 1032;
    public static final int PACKET_SET_TEXT_NB_LINE = 1034;
    public static final int PACKET_UPDATE_LIST = 1035;

    public static final String CONTENT = "content";
    public static final String PACKET_ID = "packetId";

    public static final String EVENT = "event";
    public static final String ON_BLUR_LISTENER = "onBlurListener";
    public static final String ON_FOCUS_LISTENER = "onFocusListener";
    public static final String ON_CLICK_LISTENER = "onClickListener";
    public static final String ON_DOUBLE_CLICK_LISTENER = "onDoubleClickListener";
    public static final String ON_INPUT_LISTENER = "onInputListener";
    public static final String ON_KEY_PRESSED_LISTENER = "onKeyPressedListener";
    public static final String ON_REMOVE_LISTENER = "onRemoveListener";
    public static final String ON_VALUE_CHANGE_LISTENER = "onValueChangeListener";
    public static final String ON_MOUSE_ENTER_LISTENER = "onMouseEnterListener";
    public static final String ON_MOUSE_LEAVE_LISTENER = "onMouseLeaveListener";

    public static final String X = "x";
    public static final String Y = "y";
    public static final String RELATIVE_X = "xRelative";
    public static final String RELATIVE_Y = "yRelative";
    public static final String COMPONENT_RELATIVE_TO_X = "componentRelativeToX";
    public static final String COMPONENT_RELATIVE_TO_Y = "componentRelativeToY";
    public static final String R = "r";
    public static final String G = "g";
    public static final String B = "b";
    public static final String A = "a";
    public static final String URL = "url";
    public static final String NAME = "name";
    public static final String SIZE = "size";
    public static final String FONTS = "fonts";
    public static final String IMAGES = "images";
    public static final String FONTS_TO_GENERATE = "fontsToGenerate";
    public static final String TIME = "time";
    public static final String PERCENTAGE = "percentage";
    public static final String ATTRIBUTE = "attribute";
    public static final String STATE = "state";
    public static final String VALUE = "value";
    public static final String KEY = "key";
    public static final String COMPONENT = "component";
    public static final String COMPONENT_ID = "componentId";
    public static final String TYPE = "type";
    public static final String PARENT_ID = "parentId";
    public static final String SHAPE = "shape";
    public static final String BUTTON_LIST_BEFORE = "buttonListBefore";
    public static final String SLIDER_BUTTON = "sliderButton";
    public static final String BUTTON_LIST_AFTER = "buttonListAfter";
    public static final String BUTTON_LINE_BEFORE = "buttonLineBefore";
    public static final String BUTTON_LINE_AFTER = "buttonLineAfter";
    public static final String CHECKBOX = "checkBox";
    public static final String DIV = "div";
    public static final String LIST = "list";
    public static final String PARAGRAPH = "paragraph";
    public static final String INPUT = "input";
    public static final String PROGRESS_BAR_VERTICAL = "progressbarVertical";
    public static final String PROGRESS_BAR_HORIZONTAL = "progressbarHorizontal";
    public static final String SLIDER_VERTICAL = "sliderVertical";
    public static final String SLIDER_HORIZONTAL = "sliderHorizontal";
    public static final String TEXT_AREA = "textArea";
    public static final String SHAPE_NORMAL = "shapeNormal";
    public static final String SHAPE_ON_VALUE_TRUE = "shapeOnValueTrue";
    public static final String SHAPE_ON_VALUE_FALSE = "shapeOnValueFalse";
    public static final String SHAPE_ON_PROGRESS = "shapeOnProgress";
    public static final String TEXT = "text";
    public static final String WIDTH = "WIDTH";
    public static final String HEIGHT = "HEIGHT";
    public static final String BORDER_TOP = "BORDER_TOP";
    public static final String BORDER_LEFT = "BORDER_LEFT";
    public static final String BORDER_RIGHT = "BORDER_RIGHT";
    public static final String BORDER_BOTTOM = "BORDER_BOTTOM";
    public static final String MARGIN_TOP = "MARGIN_TOP";
    public static final String MARGIN_LEFT = "MARGIN_LEFT";
    public static final String MARGIN_RIGHT = "MARGIN_RIGHT";
    public static final String MARGIN_BOTTOM = "MARGIN_BOTTOM";
    public static final String PADDING_TOP = "PADDING_TOP";
    public static final String PADDING_LEFT = "PADDING_LEFT";
    public static final String PADDING_RIGHT = "PADDING_RIGHT";
    public static final String PADDING_BOTTOM = "PADDING_BOTTOM";
    public static final String VISIBILITY = "VISIBILITY";
    public static final String CURSOR = "CURSOR";
    public static final String BACKGROUND_IMAGE = "BACKGROUND_IMAGE";
    public static final String BACKGROUND_COLOR = "BACKGROUND_COLOR";
    public static final String BORDER_TOP_COLOR = "BORDER_TOP_COLOR";
    public static final String BORDER_LEFT_COLOR = "BORDER_LEFT_COLOR";
    public static final String BORDER_RIGHT_COLOR = "BORDER_RIGHT_COLOR";
    public static final String BORDER_BOTTOM_COLOR = "BORDER_BOTTOM_COLOR";
    public static final String POLYGON_POSITIONS = "POLYGON_POSITIONS";
    public static final String ELLIPSE_COLOR = "ellipseColor";
    public static final String POLYGON_COLOR = "polygonColor";
    public static final String RECTANGLE_COLOR = "rectangleColor";
    public static final String RECTANGLE_IMAGE = "rectangleImage";

    private final ConcurrentHashMap<UUID, UserConnection> playerConnections;
    private final ArrayList<PluginInfo> pluginsInfo;
    private final ArrayList<OnGuiListener> plugins;

    public abstract void sendPacktTo(UUID uuid, JSONObject jsonObject);

    public NetworkController() {
        this.playerConnections = new ConcurrentHashMap<>();
        this.plugins = new ArrayList<>();
        this.pluginsInfo = new ArrayList<>();
    }

    public void addPlugin(OnGuiListener plugin, String pluginName){
        pluginsInfo.add(new PluginInfo(pluginName, plugin, new ArrayList<String>()));
    }

    public void addPlugin(OnGuiListener plugin, String pluginName, String... dependencies){
        pluginsInfo.add(new PluginInfo(pluginName, plugin, new ArrayList<String>(Arrays.asList(dependencies))));
    }

    public void sortPlugins(){
        ArrayList<PluginInfo> pluginsToRemove = new ArrayList<>();
        ArrayList<PluginInfo> pluginsAdded = new ArrayList<>();
        int i,j;

        while(pluginsInfo.size() != 0){
            for(i = 0; i < pluginsInfo.size(); i++){
                PluginInfo pluginInfo = pluginsInfo.get(i);

                if(pluginInfo.dependencies.size() == 0){
                    pluginsAdded.add(pluginInfo);
                    pluginsToRemove.add(pluginInfo);
                }
                else {
                    int nbDependenciesToFind = pluginInfo.dependencies.size();

                    for(j = 0; j < pluginsAdded.size(); j++){
                        PluginInfo pluginInfoAdded = pluginsAdded.get(j);

                        if(pluginInfo.dependencies.contains(pluginInfoAdded.name))
                            nbDependenciesToFind--;
                    }

                    if(nbDependenciesToFind == 0){
                        pluginsAdded.add(pluginInfo);
                        pluginsToRemove.add(pluginInfo);
                    }
                }
            }

            if(pluginsToRemove.size() == 0)
                pluginsInfo.clear();
            else
                pluginsInfo.removeAll(pluginsToRemove);

            pluginsToRemove.clear();
        }


        for(PluginInfo pluginInfo : pluginsAdded)
            plugins.add(pluginInfo.onGuiListener);
    }

    public ArrayList<OnGuiListener> getPlugins() {
        return plugins;
    }

    public final UserConnection getUserConnection(UUID uuid){
        return playerConnections.get(uuid);
    }

    public final void removeUserConnection(UUID uuid){
        playerConnections.remove(uuid);
    }

    public final UserConnection createUserConnection(UUID uuid){
        UserConnection userConnection = new UserConnection(this, uuid);

        playerConnections.put(uuid, userConnection);

        return userConnection;
    }

    public void reloadUser(UUID uuid){
        UserConnection userConnection = playerConnections.get(uuid);

        if(userConnection != null)
            userConnection.reloadGui();
    }

    private class PluginInfo{

        private final String name;
        private final OnGuiListener onGuiListener;
        private final ArrayList<String> dependencies;

        public PluginInfo(String name, OnGuiListener onGuiListener, ArrayList<String> dependencies) {
            this.name = name;
            this.onGuiListener = onGuiListener;
            this.dependencies = dependencies;
        }
    }

}
