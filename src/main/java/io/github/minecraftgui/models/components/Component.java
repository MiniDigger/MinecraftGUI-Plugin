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

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2015-12-29.
 */
public class Component {

    protected UserGui userGui;
    protected UserConnection userConnection;
    protected final String type;
    protected final UUID uuid;
    protected final String id;
    protected Shape shape;
    protected final HashMap<String, Object> objects;
    protected final CopyOnWriteArrayList<Component> specialChildren;
    protected final CopyOnWriteArrayList<Component> children;
    protected final CopyOnWriteArrayList<OnClickListener> onClickListeners;
    protected final CopyOnWriteArrayList<OnDoubleClickListener> onDoubleClickListeners;
    protected final CopyOnWriteArrayList<OnKeyPressedListener> onKeyPressedListeners;
    protected final CopyOnWriteArrayList<OnInputListener> onInputListeners;
    protected final CopyOnWriteArrayList<OnBlurListener> onBlurListeners;
    protected final CopyOnWriteArrayList<OnFocusListener> onFocusListeners;
    protected final CopyOnWriteArrayList<OnRemoveListener> onRemoveListeners;
    protected final CopyOnWriteArrayList<OnMouseEnterListener> onMouseEnterListeners;
    protected final CopyOnWriteArrayList<OnMouseLeaveListener> onMouseLeaveListeners;
    protected Component parent;
    private boolean removed = false;

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
        this.onMouseEnterListeners = new CopyOnWriteArrayList<>();
        this.onMouseLeaveListeners = new CopyOnWriteArrayList<>();
        this.objects = new HashMap<>();
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
        this.onMouseEnterListeners = new CopyOnWriteArrayList<>();
        this.onMouseLeaveListeners = new CopyOnWriteArrayList<>();
        this.objects = new HashMap<>();
    }

    public void addObject(String tag, Object object){
        objects.put(tag.toLowerCase(), object);
    }

    public Object getObject(String tag){
        return objects.get(tag.toLowerCase());
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

    public UserGui getUserGui() {
        return userGui;
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
        add(component, this.userGui);
    }

    public void add(Component component, UserGui userGui) {
        if(component.parent != null || component.userConnection != null || component.userGui != null)
            throw new ComponentException("Can't add a component that is already assigned.");
        if(this.parent == null || this.userConnection == null || this.userGui == null)
            throw new ComponentException("You can't add a component to a component without parent.");

        component.parent = this;
        component.userGui = userGui;
        component.userConnection = this.userConnection;
        userGui.addComponent(component);

        if(!this.specialChildren.contains(component)) {
            this.children.add(component);
            this.userConnection.addComponent(component, true);
        }
        else
            this.userConnection.addComponent(component, false);

        component.init();
        userConnection.addEventListener(component, NetworkController.ON_REMOVE_LISTENER);

        for(Component specialChild : component.specialChildren)
            component.add(specialChild, userGui);
    }

    protected void init(){
        shape.setUserConnection(userConnection);
    }

    public void remove(){
        if(this.parent != null && !removed) {
            this.removed = true;
            this.parent.children.remove(this);
            userGui.removeComponent(this);
            userConnection.removeComponent(this);
        }
    }

    protected void setUserConnection(UserConnection userConnection) {
        this.userConnection = userConnection;
    }

    public final void addOnClickListener(OnClickListener listener){
        if(onClickListeners.size() == 0)
            userConnection.addEventListener(this, NetworkController.ON_CLICK_LISTENER);

        onClickListeners.add(listener);
    }

    public void addOnRemoveListener(OnRemoveListener listener){
        onRemoveListeners.add(listener);
    }

    public void addOnMouseLeaveListener(OnMouseLeaveListener listener){
        if(onMouseLeaveListeners.size() == 0)
            userConnection.addEventListener(this, NetworkController.ON_MOUSE_LEAVE_LISTENER);

        onMouseLeaveListeners.add(listener);
    }

    public void addOnMouseEnterListener(OnMouseEnterListener listener){
        if(onMouseEnterListeners.size() == 0)
            userConnection.addEventListener(this, NetworkController.ON_MOUSE_ENTER_LISTENER);

        onMouseEnterListeners.add(listener);
    }

    public void addOnDoubleClickListener(OnDoubleClickListener listener){
        if(onDoubleClickListeners.size() == 0)
            userConnection.addEventListener(this, NetworkController.ON_DOUBLE_CLICK_LISTENER);

        onDoubleClickListeners.add(listener);
    }

    public void addOnKeyPressedListener(OnKeyPressedListener listener){
        if(onKeyPressedListeners.size() == 0)
            userConnection.addEventListener(this, NetworkController.ON_KEY_PRESSED_LISTENER);

        onKeyPressedListeners.add(listener);
    }

    public void addOnInputListener(OnInputListener listener){
        if(onInputListeners.size() == 0)
            userConnection.addEventListener(this, NetworkController.ON_INPUT_LISTENER);

        onInputListeners.add(listener);
    }

    public void addOnBlurListener(OnBlurListener listener){
        if(onBlurListeners.size() == 0)
            userConnection.addEventListener(this, NetworkController.ON_BLUR_LISTENER);

        onBlurListeners.add(listener);
    }

    public void addOnFocusListener(OnFocusListener listener){
        if(onFocusListeners.size() == 0)
            userConnection.addEventListener(this, NetworkController.ON_FOCUS_LISTENER);

        onFocusListeners.add(listener);
    }

    public void onMouseEnter(){
        for(OnMouseEnterListener listener : onMouseEnterListeners)
            listener.onMouseEnter(this);
    }

    public void onMouseLeave(){
        for(OnMouseLeaveListener listener : onMouseLeaveListeners)
            listener.onMouseLeave(this);
    }

    public void onRemove(){
        for(OnRemoveListener listener : onRemoveListeners)
            listener.onRemove(this);
    }

    public void onBlur(){
        for(OnBlurListener listener : onBlurListeners)
            listener.onBlur(this);
    }

    public void onFocus(){
        for(OnFocusListener listener : onFocusListeners)
            listener.onFocus(this);
    }

    public void onClick(){
        for(OnClickListener listener : onClickListeners)
            listener.onClick(this);
    }

    public void onDoubleClick(){
        for(OnDoubleClickListener listener : onDoubleClickListeners)
            listener.onDoubleClick(this);
    }

    public void onInput(char input){
        for(OnInputListener listener : onInputListeners)
            listener.onInput(this, input);
    }

    public void onKeyPressed(int keyCode){
        for(OnKeyPressedListener listener : onKeyPressedListeners)
            listener.onKeyPressed(this, keyCode);
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
