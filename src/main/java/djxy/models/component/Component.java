/*
 *     Minecraft GUI Server
 *     Copyright (C) 2015  Samuel Marchildon-Lavoie
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package djxy.models.component;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Component {

    public static String rootComponentId = "@ROOT";

    private String id;
    private ComponentType type;
    private final ArrayList<Component> directChildren;
    private Component parent;
    private Attributes attributes;
    private final HashMap<String, Component> allChildren;
    private final HashMap<ComponentState, String> attributeId;

    public Component(Component parent) {
        this.id = generateCustomId();
        this.type = ComponentType.PANEL;
        this.parent = parent;
        directChildren = new ArrayList<>();
        attributes = new Attributes(this.id);
        allChildren = new HashMap<>();
        attributeId = new HashMap<>();

        initAttributesId();
        parent.addChild(this);
    }

    public Component(ComponentType type) {
        this.id = generateCustomId();
        this.type = type;
        this.parent = null;
        directChildren = new ArrayList<>();
        attributes = new Attributes(this.id);
        allChildren = new HashMap<>();
        attributeId = new HashMap<>();

        initAttributesId();
    }

    public Component(String id, ComponentType type) {
        this.id = id;
        this.type = type;
        this.parent = null;
        directChildren = new ArrayList<>();
        attributes = new Attributes(this.id);
        allChildren = new HashMap<>();
        attributeId = new HashMap<>();

        initAttributesId();
    }

    public Component(ComponentType type, Component parent) {
        this.id = generateCustomId();
        this.type = type;
        this.parent = null;
        directChildren = new ArrayList<>();
        attributes = new Attributes(this.id);
        allChildren = new HashMap<>();
        attributeId = new HashMap<>();

        initAttributesId();
        parent.addChild(this);
    }

    public Component(String id, ComponentType type, Component parent) {
        this.id = id;
        this.type = type;
        this.parent = null;
        directChildren = new ArrayList<>();
        attributes = new Attributes(this.id);
        allChildren = new HashMap<>();
        attributeId = new HashMap<>();

        initAttributesId();
        parent.addChild(this);
    }

    private void initAttributesId(){
        setAttributeId(ComponentState.NORMAL, this.id+"NORMAL");
        setAttributeId(ComponentState.HOVER, this.id+"HOVER");
        setAttributeId(ComponentState.CLICK, this.id + "CLICK");
    }

    public void setAttributeId(ComponentState state, String id){
        attributeId.put(state, id);
    }

    public String getAttributeId(ComponentState state){
        return attributeId.get(state);
    }

    public String getId() {
        return id;
    }

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public Component getParent() {
        return parent;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setId(String id){
        id = id.isEmpty() == true?generateCustomId():id;

        if(this.parent != null)
            removeIndexComponent(this.parent, this);

        this.id = id;

        if(this.parent != null)
            indexComponent(this.parent, this);

        attributes.setId(this.id);
        initAttributesId();
    }

    public void delete(){
        if(parent != null) {
            removeIndexComponent(this.parent, this);
            this.parent.directChildren.remove(this);
            this.parent = null;
        }
    }

    public void addChild(Component component){
        if(component.parent != null) {
            component.parent.directChildren.remove(component);
            removeIndexComponent(component.parent, component);
        }

        component.parent = this;
        this.directChildren.add(component);
        indexComponent(this, component);
    }

    public Component getChild(String childId){
        return allChildren.get(childId.toLowerCase());
    }

    public ArrayList<Component> getChildren() {
        return directChildren;
    }

    private void removeIndexComponent(Component parent, Component compToIndex) {
        parent.allChildren.remove(compToIndex.id.toLowerCase());
        for(Component child : compToIndex.allChildren.values())
            parent.allChildren.remove(child.id.toLowerCase());

        if(parent.parent != null)
            removeIndexComponent(parent.parent, compToIndex);
    }

    private void indexComponent(Component parent, Component compToIndex) {
        parent.allChildren.put(compToIndex.id.toLowerCase(), compToIndex);
        for(Component child : compToIndex.allChildren.values())
            parent.allChildren.put(child.id.toLowerCase(), child);

        if(parent.parent != null)
            indexComponent(parent.parent, compToIndex);
    }

    public JSONArray getCommands(){
        JSONArray commands = new JSONArray();
        JSONObject createCommand = new JSONObject();
        String parentId = parent != null?parent.id:rootComponentId;

        createCommand.put("Command", "CREATE COMPONENT");
        createCommand.put("ComponentId", id);
        createCommand.put("ParentId", parentId);
        createCommand.put("Type", type.name());
        
        commands.add(createCommand);
        commands.addAll(attributes.getCommands());
        
        return commands;
    }

    private static String generateCustomId(){
        return "*"+System.nanoTime();
    }

    public Component clone(String suffix, boolean fakeParent){
        Component clone;

        if(fakeParent && this.parent != null)
            clone = new Component(this.id+suffix, this.type, new Component(this.parent.getId(), ComponentType.PANEL));
        else if(this.parent != null)
            clone = new Component(this.id+suffix, this.type, this.parent);
        else
            clone = new Component(this.id+suffix, this.type);

        clone.attributes = attributes.clone(clone.getId());
        clone.setAttributeId(ComponentState.NORMAL, clone.getId()+"NORMAL");
        clone.setAttributeId(ComponentState.HOVER, clone.getId()+"HOVER");
        clone.setAttributeId(ComponentState.CLICK, clone.getId() + "CLICK");

        for(Component copy : directChildren)
            clone(copy, clone, suffix);

        return clone;
    }

    private void clone(Component original, Component parent, String suffix){
        Component clone = new Component(original.id+suffix, original.type, parent);

        clone.attributes = original.attributes.clone(clone.getId());
        clone.setAttributeId(ComponentState.NORMAL, clone.getId()+"NORMAL");
        clone.setAttributeId(ComponentState.HOVER, clone.getId()+"HOVER");
        clone.setAttributeId(ComponentState.CLICK, clone.getId()+"CLICK");

        for(Component copy : original.directChildren)
            clone(copy, clone, suffix);
    }

}
