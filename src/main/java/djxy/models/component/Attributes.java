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

import djxy.models.ButtonEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Attributes {

    private static int idToReturn = 0;
    private String id = "";
    private HashMap<String, HashMap<ComponentState, Object>> attributesWithState;
    private HashMap<String, Object> attributesWithoutState;
    private ArrayList<String> inputs;
    private LinkedHashMap<Integer, ButtonEvent> buttonEvents;

    /**
     * @param componentId The component id of the component to do modification.
     */
    public Attributes(String componentId) {
        this.id = componentId;
        attributesWithState = new HashMap<>();
        attributesWithoutState = new HashMap<>();
        inputs = new ArrayList<>();
        buttonEvents = new LinkedHashMap<>();
    }

    public Attributes() {
        attributesWithState = new HashMap<>();
        attributesWithoutState = new HashMap<>();
        inputs = new ArrayList<>();
        buttonEvents = new LinkedHashMap<>();
    }

    public void setAttribute(String attribute, Object value){
        attributesWithoutState.put(attribute.toLowerCase(), value);
    }

    public void setAttribute(String attribute, ComponentState state, Object value){
        HashMap<ComponentState, Object> values = attributesWithState.get(attribute.toLowerCase());

        if(values == null){
            attributesWithState.put(attribute.toLowerCase(), new HashMap<ComponentState, Object>());
            values = attributesWithState.get(attribute.toLowerCase());
        }

        values.put(state, value);
    }

    public Object getAttribute(String attribute){
        return attributesWithoutState.get(attribute);
    }

    public Object getAttribute(String attribute, ComponentState state){
        HashMap<ComponentState, Object> values = attributesWithState.get(attribute);

        if(values != null)
            return values.get(state);

        return null;
    }

    public ArrayList<String> getInputs() {
        return (ArrayList<String>) inputs.clone();
    }

    public LinkedHashMap<Integer, ButtonEvent> getButtonEvents() {
        return (LinkedHashMap<Integer, ButtonEvent>) buttonEvents.clone();
    }

    public ButtonEvent getButtonEvent(int buttonEventId){
        return buttonEvents.get(buttonEventId);
    }

    /**
     * @return The component id.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     *  This setting is only for the buttons. When the player will click on that button, the button
     *  will send a form with all the inputs you have added.
     *
     * @param inputId The input id.
     */
    public void addInput(String inputId){
        inputs.add(inputId);
    }

    public void removeInput(String input){
        inputs.remove(input);
    }

    public int addButtonEvent(ButtonEvent buttonEvent){
        int id = generateButtonEventId();

        buttonEvents.put(id, buttonEvent);

        return id;
    }

    public void removeButtonEvent(int id){
        buttonEvents.remove(id);
    }
    
    public JSONArray getCommands(){
        JSONArray commands = new JSONArray();
        
        for(String inputId : inputs)
            commands.add(createAddInputCommand(inputId));

        for(ButtonEvent buttonEvent : buttonEvents.values())
            commands.add(createAddButtonEventCommand(buttonEvent));
        
        for(Map.Entry pairs : attributesWithoutState.entrySet()){
            commands.add(createUpdateCommand(ComponentState.NORMAL, (String) pairs.getKey(), pairs.getValue()));
        }
        
        for(Map.Entry pairs : attributesWithState.entrySet()){
            HashMap<ComponentState, String> states = (HashMap<ComponentState, String>) pairs.getValue();
            
            for(Map.Entry pairs2 : states.entrySet())
                commands.add(createUpdateCommand((ComponentState) pairs2.getKey(), (String) pairs.getKey(), pairs2.getValue()));
        }
        
        return commands;
    }
    
    private JSONObject createAddInputCommand(String inputId){
        JSONObject object = new JSONObject();
        
        object.put("Command", "SET INPUT_TEXT");
        object.put("ButtonId", id);
        object.put("InputId", inputId);
        
        return object;
    }

    private JSONObject createAddButtonEventCommand(ButtonEvent buttonEvent){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("Command", "SET BUTTON_EVENT");
        jsonObject.put("ButtonId", id);
        jsonObject.put("ComponentId", buttonEvent.getComponentIdToUpdate());
        jsonObject.put("State", buttonEvent.getState().name());
        jsonObject.put("Attribute", buttonEvent.getAttribute());
        jsonObject.put("Value", buttonEvent.getValue());

        return jsonObject;
    }
    
    private JSONObject createUpdateCommand(ComponentState state, String attribute, Object value){
        JSONObject update = new JSONObject();
        
        update.put("Command", "SET COMPONENT");
        update.put("ComponentId", id);
        update.put("State", state.name());
        update.put("Attribute", attribute.toUpperCase());

        if(value instanceof String){
            update.put("Value", value);
        }
        else if(value instanceof Side){
            Side side = (Side) value;
            update.put("Value", side.isLeft()+","+side.isTop()+","+side.isRight()+","+side.isBottom());
        }
        else if(value instanceof Integer){
            Integer i = (Integer) value;
            update.put("Value", i+"");
        }
        else if(value instanceof Float){
            Float f = (Float) value;

            update.put("Value", f*100+"%");
        }
        else if(value instanceof Boolean){
            Boolean bool = (Boolean) value;

            update.put("Value", bool+"");
        }
        else if(value instanceof Color){
            Color color = (Color) value;

            update.put("Value", color.getRed()+","+color.getGreen()+","+color.getBlue()+","+color.getAlpha());
        }
        
        return update;
    }

    public Attributes clone(String id){
        Attributes clone = new Attributes(id);

        clone.attributesWithoutState = (HashMap<String, Object>) attributesWithoutState.clone();

        clone.inputs = (ArrayList<String>) inputs.clone();

        for(ButtonEvent buttonEvent : buttonEvents.values())
            clone.addButtonEvent(buttonEvent.clone());

        for(Map.Entry pairs : attributesWithState.entrySet())
            clone.attributesWithState.put((String) pairs.getKey(), (HashMap<ComponentState, Object>) ((HashMap<ComponentState, String>) pairs.getValue()).clone());

        return clone;
    }

    public String toString(){
        System.out.println(id);

        for(Map.Entry pairs : attributesWithoutState.entrySet())
            System.out.println(pairs.getKey()+": "+pairs.getValue().toString());

        System.out.println();

        for(Map.Entry pairs1 : attributesWithState.entrySet())
            for(Map.Entry pairs2 : attributesWithState.get(pairs1.getValue()).entrySet())
                System.out.println(pairs1.getKey()+" | "+pairs2.getKey()+": "+pairs2.getValue().toString());

        for(String input : inputs)
            System.out.println(input);

        for(ButtonEvent buttonEvent : buttonEvents.values())
            System.out.println(buttonEvent);

        return "";
    }

    private static int generateButtonEventId(){
        return idToReturn++;
    }
}
