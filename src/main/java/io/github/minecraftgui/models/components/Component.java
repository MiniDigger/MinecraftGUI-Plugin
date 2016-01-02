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

package io.github.minecraftgui.models.components;

import io.github.minecraftgui.controllers.NetworkController;
import io.github.minecraftgui.models.exceptions.ComponentException;
import io.github.minecraftgui.models.listeners.*;
import io.github.minecraftgui.models.network.UserConnection;
import io.github.minecraftgui.models.shapes.*;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2015-12-29.
 */
public class Component {

    protected PluginGUI pluginGUI;
    protected UserConnection userConnection;
    protected final String type;
    protected final UUID uuid;
    protected final String id;
    protected Shape shape;
    protected final CopyOnWriteArrayList<Component> specialChildren;
    protected final CopyOnWriteArrayList<Component> children;
    protected final CopyOnWriteArrayList<OnClickListener> onClickListeners;
    protected final CopyOnWriteArrayList<OnDoubleClickListener> onDoubleClickListeners;
    protected final CopyOnWriteArrayList<OnKeyPressedListener> onKeyPressedListeners;
    protected final CopyOnWriteArrayList<OnInputListener> onInputListeners;
    protected final CopyOnWriteArrayList<OnBlurListener> onBlurListeners;
    protected final CopyOnWriteArrayList<OnFocusListener> onFocusListeners;
    protected final CopyOnWriteArrayList<OnRemoveListener> onRemoveListeners;
    protected Component parent;

    public Component(String type, Class<? extends Shape> shape) {
        this.type = type;
        this.shape = getShapeByClass(shape, NetworkController.SHAPE_NORMAL);
        this.uuid = UUID.randomUUID();
        this.id = null;
        this.specialChildren = new CopyOnWriteArrayList<>();
        this.children = new CopyOnWriteArrayList<>();
        this.onClickListeners = new CopyOnWriteArrayList<>();
        this.onDoubleClickListeners = new CopyOnWriteArrayList<>();
        this.onKeyPressedListeners = new CopyOnWriteArrayList<>();
        this.onInputListeners = new CopyOnWriteArrayList<>();
        this.onBlurListeners = new CopyOnWriteArrayList<>();
        this.onFocusListeners = new CopyOnWriteArrayList<>();
        this.onRemoveListeners = new CopyOnWriteArrayList<>();
    }

    public Component(String type, Class<? extends Shape> shape, String id) {
        this.type = type;
        this.shape = getShapeByClass(shape, NetworkController.SHAPE_NORMAL);
        this.uuid = UUID.randomUUID();
        this.id = id;
        this.specialChildren = new CopyOnWriteArrayList<>();
        this.children = new CopyOnWriteArrayList<>();
        this.onClickListeners = new CopyOnWriteArrayList<>();
        this.onDoubleClickListeners = new CopyOnWriteArrayList<>();
        this.onKeyPressedListeners = new CopyOnWriteArrayList<>();
        this.onInputListeners = new CopyOnWriteArrayList<>();
        this.onBlurListeners = new CopyOnWriteArrayList<>();
        this.onFocusListeners = new CopyOnWriteArrayList<>();
        this.onRemoveListeners = new CopyOnWriteArrayList<>();
    }

    public String getType() {
        return type;
    }

    public Shape getShape() {
        return shape;
    }

    public String getId() {
        return id;
    }

    public PluginGUI getPluginGUI() {
        return pluginGUI;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public Component getParent() {
        return parent;
    }

    public CopyOnWriteArrayList<Component> getChildren() {
        return (CopyOnWriteArrayList<Component>) children.clone();
    }

    public void setVisibility(Visibility visibility){
        userConnection.setVisibility(this, visibility);
    }

    public void setCursor(State state, Cursor cursor){
        userConnection.setCursor(this, state, cursor);
    }

    public void addXRelativeTo(Shape shapeRelativeTo, AttributeDouble attributeRelativeTo, double percentage){
        userConnection.setAttribute(NetworkController.COMPONENT_RELATIVE_TO_X, this, getShape(), State.NORMAL, percentage, 0, shapeRelativeTo, attributeRelativeTo);
    }

    public void addYRelativeTo(Shape shapeRelativeTo, Attribute attributeRelativeTo, double percentage){
        userConnection.setAttribute(NetworkController.COMPONENT_RELATIVE_TO_Y, this, getShape(), State.NORMAL, percentage, 0, shapeRelativeTo, attributeRelativeTo);
    }

    public void setXRelative(State state, double value){
        userConnection.setAttribute(NetworkController.RELATIVE_X, this, getShape(), state, 1, 0, value);
    }

    public void setYRelative(State state, double value){
        userConnection.setAttribute(NetworkController.RELATIVE_Y, this, getShape(), state, 1, 0, value);
    }

    public void add(Component component) {
        add(component, this.pluginGUI);
    }

    public void add(Component component, PluginGUI pluginGUI) {
        if(component.parent != null || component.userConnection != null || component.pluginGUI != null)
            throw new ComponentException("Can't add a component that is already assigned.");
        if(this.parent == null || this.userConnection == null || this.pluginGUI == null)
            throw new ComponentException("You can't add a component to a component without parent.");

        component.parent = this;
        component.pluginGUI = pluginGUI;
        component.userConnection = this.userConnection;
        component.setShapeUserConnection();
        pluginGUI.addComponent(component);
        this.userConnection.addComponent(component);

        if(!this.specialChildren.contains(component))
            this.children.add(component);

        for(Component specialChild : specialChildren)
            component.add(specialChild);
    }

    public void remove(){
        if(this.parent != null) {
            this.parent.children.remove(this);
            pluginGUI.removeComponent(this);
            userConnection.removeComponent(this);
        }
    }

    protected void setShapeUserConnection(){
        shape.setUserConnection(userConnection);
    }

    protected void setPluginGUI(PluginGUI pluginGUI) {
        this.pluginGUI = pluginGUI;
    }

    protected void setUserConnection(UserConnection userConnection) {
        this.userConnection = userConnection;
    }

    public final void addOnClickListener(OnClickListener listener){
        onClickListeners.add(listener);
    }

    public void addOnRemoveListener(OnRemoveListener listener){
        onRemoveListeners.add(listener);
    }

    public void addOnDoubleClickListener(OnDoubleClickListener listener){
        onDoubleClickListeners.add(listener);
    }

    public void addOnKeyPressedListener(OnKeyPressedListener listener){
        onKeyPressedListeners.add(listener);
    }

    public void addOnInputListener(OnInputListener listener){
        onInputListeners.add(listener);
    }

    public void addOnBlurListener(OnBlurListener listener){
        onBlurListeners.add(listener);
    }

    public void addOnFocusListener(OnFocusListener listener){
        onFocusListeners.add(listener);
    }

    protected Shape getShapeByClass(Class<? extends Shape> shape, String componentShape){
        if(shape == EllipseColor.class)
            return new EllipseColor(componentShape, this);
        else if(shape == PolygonColor.class)
            return new PolygonColor(componentShape, this);
        else if(shape == RectangleColor.class)
            return new RectangleColor(componentShape, this);
        else if(shape == RectangleImage.class)
            return new RectangleImage(componentShape, this);

        return null;
    }

}
