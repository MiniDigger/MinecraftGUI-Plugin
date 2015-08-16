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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Attributes {

    private static int idToReturn = 0;
    private String id = "";
    private HashMap<ComponentAttribute, HashMap<ComponentState, String>> attributesWithState;
    private HashMap<ComponentAttribute, String> attributesWithoutState;
    private LinkedHashMap<Integer, String> inputs;
    private LinkedHashMap<Integer, ButtonEvent> buttonEvents;

    /**
     * @param componentId The component id of the component to do modification.
     */
    public Attributes(String componentId) {
        this.id = componentId;
        attributesWithState = new HashMap<>();
        attributesWithoutState = new HashMap<>();
        inputs = new LinkedHashMap<>();
        buttonEvents = new LinkedHashMap<>();
    }

    public Attributes() {
        attributesWithState = new HashMap<>();
        attributesWithoutState = new HashMap<>();
        inputs = new LinkedHashMap<>();
        buttonEvents = new LinkedHashMap<>();
    }

    public String getAttributeValue(ComponentAttribute attribute){
        return attributesWithoutState.get(attribute);
    }

    public String getAttributeValue(ComponentAttribute attribute, ComponentState state){
        HashMap<ComponentState, String> values = attributesWithState.get(attribute);

        if(values != null)
            return values.get(state);

        return null;
    }

    public LinkedHashMap<Integer, String> getInputs() {
        return (LinkedHashMap<Integer, String>) inputs.clone();
    }

    public LinkedHashMap<Integer, ButtonEvent> getButtonEvents() {
        return (LinkedHashMap<Integer, ButtonEvent>) buttonEvents.clone();
    }

    public void setInput(int id, String input){
        inputs.put(id, input);
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
     * That will affect the left side of the component. If the value is positive, that will reduce
     * the width of the component by the left.
     *
     * @param state Which state to set this value.
     * @param margin The size of the margin.
     */
    public void setMarginLeft(ComponentState state, int margin){
        addUpdate(state, ComponentAttribute.MARGIN_LEFT, margin+"");
    }

    /**
     * That will affect the left side of the component. If the value is positive, that will reduce
     * the height of the component by the top.
     *
     * @param state Which state to set this value.
     * @param margin The size of the margin.
     */
    
    public void setMarginTop(ComponentState state, int margin){
        addUpdate(state, ComponentAttribute.MARGIN_TOP, margin+"");
    }

    /**
     * That will affect the left side of the component. If the value is positive, that will reduce
     * the width of the component by the right.
     *
     * @param state Which state to set this value.
     * @param margin The size of the margin.
     */
    public void setMarginRight(ComponentState state, int margin){
        addUpdate(state, ComponentAttribute.MARGIN_RIGHT, margin+"");
    }

    /**
     * That will affect the left side of the component. If the value is positive, that will reduce
     * the height of the component by the bottom.
     *
     * @param state Which state to set this value.
     * @param margin The size of the margin.
     */
    public void setMarginBot(ComponentState state, int margin){
        addUpdate(state, ComponentAttribute.MARGIN_BOT, margin+"");
    }

    /**
     * That will set the position of the component. By default it's TOP_LEFT.
     * This setting will affect the x relatif and the y relatif.
     *
     * @param position The position of the component on his parent.
     */
    public void setPosition(Position position){
        addUpdate(ComponentAttribute.POSITION, position.name());
    }

    /**
     * This setting only affect the components that are directly on the screen.
     * They can be moved by player.
     *
     * @param freeze If the component can be moved.
     */
    public void setLocationFreeze(boolean freeze){
        addUpdate(ComponentAttribute.LOCATION_FREEZE, freeze+"");
    }

    /**
     * This setting will affect the component and his children. If the component is not visible,
     * he won't be render and his children won't be render.
     *
     * @param visibility If the component is visible.
     */
    public void setVisibility(Visibility visibility){
        addUpdate(ComponentAttribute.VISIBILITY, visibility.name());
    }

    public void setFocus(boolean focus){
        addUpdate(ComponentAttribute.FOCUS, focus+"");
    }

    /**
     * Will move the component on the x axis dependent of his position.
     *
     * @param x The distance on the x axis.
     */
    public void setXRelative(int x){
        addUpdate(ComponentAttribute.X_RELATIVE, x+"");
    }

    /**
     * Will move the component on the x axis dependent of his position and the value
     * will be a percentage of the width of the component. Example: If it's 50%, it will
     * move the component by 50% of his width on the x axis.
     *
     * @param x The distance on the x axis as a percentage.
     */
    public void setXRelative(float x){
        addUpdate(ComponentAttribute.X_RELATIVE, x*100+"%");
    }


    /**
     * Will move the component on the y axis dependent of his position.
     *
     * @param y The distance on the y axis.
     */
    public void setYRelative(int y){
        addUpdate(ComponentAttribute.Y_RELATIVE, y+"");
    }


    /**
     * Will move the component on the y axis dependent of his position and the value
     * will be a percentage of the height of the component. Example: If it's 50%, it will
     * move the component by 50% of his height on the y axis.
     *
     * @param y The distance on the y axis as a percentage.
     */
    public void setYRelative(float y){
        addUpdate(ComponentAttribute.Y_RELATIVE, y*100+"%");
    }

    /**
     * The value is a text the component will contain. Each type of component will act differently with it.
     *
     * @param value A text the component will contain.
     */
    public void setValue(String value){
        addUpdate(ComponentAttribute.VALUE, value);
    }

    /**
     * This setting only work with the component type ButtonURL. When the player will click
     * on this button, that will open the default web browser of the player at this url.
     *
     * @param url The url of a site.
     */
    public void setURL(String url){
        addUpdate(ComponentAttribute.URL, url);
    }

    /**
     * This setting only work with the inputs. When the value of the input is empty
     * that will render this text.
     *
     * @param hint The text to render when the the value is empty.
     */
    public void setHint(String hint){
        addUpdate(ComponentAttribute.HINT, hint);
    }

    /**
     * The image type will do the difference between a custom image and a image from the minecraft resource.
     * If the type is Minecraft, you will draw minecraft item. If it's Custom, you will draw custom image.
     *
     * @param state Which state to set this value.
     * @param type The type of the image.
     */
    public void setImageType(ComponentState state, ImageType type){
        addUpdate(state, ComponentAttribute.IMAGE_TYPE, type.name());
    }

    /**
     * If the image type is Minecraft, you could set the item id or his name.
     * If the image type is Custom, you need to give the file name of the image.
     *
     * @param state Which state to set this value.
     * @param imageName The name of the image to draw.
     */
    public void setImageName(ComponentState state, String imageName){
        addUpdate(state, ComponentAttribute.IMAGE_NAME, imageName);
    }

    /**
     * That will set one of the two minecraft fonts already loaded.
     *
     * @param state Which state to set this value.
     * @param font The Minecraft font to use.
     */
    public void setFont(ComponentState state, Font font){
        addUpdate(state, ComponentAttribute.FONT, font.name());
    }

    /**
     * That will draw the text with a custom font loaded from internet.
     *
     * @param state Which state to set this value.
     * @param font The post-script name of the font.
     */
    public void setFont(ComponentState state, String font){
        addUpdate(state, ComponentAttribute.FONT, font);
    }

    /**
     * That will change the size of the font. The two minecraft fonts
     * are not affected by this setting.
     *
     * @param state Which state to set this value.
     * @param size The size of the font.
     */
    public void setFontSize(ComponentState state, int size){
        addUpdate(state, ComponentAttribute.FONT_SIZE, size+"");
    }

    /**
     * The width of the component. This value won't be affected by the parent.
     *
     * @param state Which state to set this value.
     * @param width The width of the component.
     */
    public void setWidth(ComponentState state, int width){
        addUpdate(state, ComponentAttribute.WIDTH, width+"");
    }

    /**
     * The width of the component. This value will be equal to the width of his parent multiplied by the percentage.
     *
     * @param state Which state to set this value.
     * @param width The width of the component in percentage.
     */
    public void setWidth(ComponentState state, float width){
        addUpdate(state, ComponentAttribute.WIDTH, width*100+"%");
    }

    /**
     * The height of the component. This value won't be affected by the parent.
     *
     * @param state Which state to set this value.
     * @param height The height of the component.
     */
    public void setHeight(ComponentState state, int height){
        addUpdate(state, ComponentAttribute.HEIGHT, height+"");
    }

    /**
     * The height of the component. This value will be equal to the height of his parent multiplied by the percentage.
     *
     * @param state Which state to set this value.
     * @param height The height of the component in percentage.
     */
    public void setHeight(ComponentState state, float height){
        addUpdate(state, ComponentAttribute.HEIGHT, height*100+"%");
    }

    /**
     * The component will have a padding the same color of his background.
     *
     * @param state Which state to set this value.
     * @param side The sides of the padding.
     */
    public void setPaddingSide(ComponentState state, Side side){
        addUpdate(state, ComponentAttribute.PADDING_SIDE, side.isLeft()+","+side.isTop()+","+side.isRight()+","+side.isBottom());
    }

    /**
     * Every sides of the padding will be affected by this size.
     *
     * @param state Which state to set this value.
     * @param size The size of the padding.
     */
    public void setPaddingSize(ComponentState state, int size){
        addUpdate(state, ComponentAttribute.PADDING_SIZE, size+"");
    }

    /**
     * The component will have a border.
     *
     * @param state Which state to set this value.
     * @param side The sides of the border.
     */
    public void setBorderSide(ComponentState state, Side side){
        addUpdate(state, ComponentAttribute.BORDER_SIDE, side.isLeft()+","+side.isTop()+","+side.isRight()+","+side.isBottom());
    }


    /**
     * Every sides of the border will be affected by this size.
     *
     * @param state Which state to set this value.
     * @param size The size of the border.
     */
    public void setBorderSize(ComponentState state, int size){
        addUpdate(state, ComponentAttribute.BORDER_SIZE, size+"");
    }

    /**
     * Every sides of the border will be this color.
     *
     * @param state Which state to set this value.
     * @param color The color of the border.
     */
    public void setBorderColor(ComponentState state, Color color){
        addUpdate(state, ComponentAttribute.BORDER_COLOR, color.getRed()+","+color.getGreen()+","+color.getBlue()+","+color.getAlpha());
    }

    /**
     * That will set the background of the component with a Minecraft background.
     *
     * @param state Which state to set this value.
     * @param background The Minecraft background.
     */
    public void setBackground(ComponentState state, Background background){
        addUpdate(state, ComponentAttribute.BACKGROUND, background.name());
    }

    /**
     * The background of the component will be a color. That support alpha.
     *
     * @param state Which state to set this value.
     * @param color The color of the background.
     */
    public void setBackground(ComponentState state, Color color){
        addUpdate(state, ComponentAttribute.BACKGROUND, color.getRed()+","+color.getGreen()+","+color.getBlue()+","+color.getAlpha());
    }

    /**
     * The text will be displayed differently.
     *
     * @param state Which state to set this value.
     * @param textAlignment The text alignment.
     */
    public void setTextAlignment(ComponentState state, TextAlignment textAlignment){
        addUpdate(state, ComponentAttribute.TEXT_ALIGNMENT, textAlignment.name());
    }


    /**
     * The color of the text. That support alpha.
     *
     * @param state Which state to set this value.
     * @param color The color of the text.
     */
    public void setTextColor(ComponentState state, Color color){
        addUpdate(state, ComponentAttribute.TEXT_COLOR, color.getRed()+","+color.getGreen()+","+color.getBlue()+","+color.getAlpha());
    }


    /**
     * The text will have a limit of line. If the limit is exceeded, the text won't be render.
     *
     * @param maxLines The number of line the text can have.
     */
    public void setMaxTextLines(int maxLines){
        addUpdate(ComponentAttribute.MAX_TEXT_LINES, maxLines+"");
    }

    public void setListOrder(ListOrder listOrder){
        addUpdate(ComponentAttribute.LIST_ORDER, listOrder.name());
    }

    public void setListOrigin(ListOrigin listOrigin){
        addUpdate(ComponentAttribute.LIST_ORIGIN, listOrigin.name());
    }

    /**
     *  This setting is only for the buttons. When the player will click on that button, the button
     *  will send a form with all the inputs you have added.
     *
     * @param inputId The input id.
     */
    public int addInput(String inputId){
        int id = generateIdToReturn();

        inputs.put(id, inputId);

        return id;
    }

    public void removeInput(int id){
        inputs.remove(id);
    }

    public int addButtonEvent(String componentIdToUpdate, ComponentState state, ComponentAttribute componentAttribute, String value){
        return addButtonEvent(new ButtonEvent(componentIdToUpdate, state, componentAttribute, value));
    }

    public int addButtonEvent(ButtonEvent buttonEvent){
        int id = generateIdToReturn();

        buttonEvents.put(id, buttonEvent);

        return id;
    }

    public void removeButtonEvent(int id){
        buttonEvents.remove(id);
    }
    
    public JSONArray getCommands(){
        JSONArray commands = new JSONArray();
        
        for(String inputId : inputs.values())
            commands.add(createAddInputCommand(inputId));

        for(ButtonEvent buttonEvent : buttonEvents.values())
            commands.add(createAddButtonEventCommand(buttonEvent));
        
        for(Map.Entry pairs : attributesWithoutState.entrySet()){
            ComponentAttribute attribute = (ComponentAttribute) pairs.getKey();
            String value = (String) pairs.getValue();
            
            commands.add(createUpdateCommand(ComponentState.NORMAL, attribute, value));
        }
        
        for(Map.Entry pairs : attributesWithState.entrySet()){
            ComponentAttribute attribute = (ComponentAttribute) pairs.getKey();
            HashMap<ComponentState, String> states = (HashMap<ComponentState, String>) pairs.getValue();
            
            for(Map.Entry pairs2 : states.entrySet())
                commands.add(createUpdateCommand((ComponentState) pairs2.getKey(), attribute, (String) pairs2.getValue()));
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
        jsonObject.put("Attribute", buttonEvent.getComponentAttribute().name());
        jsonObject.put("Value", buttonEvent.getValue());

        return jsonObject;
    }
    
    private JSONObject createUpdateCommand(ComponentState state, ComponentAttribute attribute, String value){
        JSONObject update = new JSONObject();
        
        update.put("Command", "SET COMPONENT");
        update.put("ComponentId", id);
        update.put("State", state.name());
        update.put("Attribute", attribute.name());
        update.put("Value", value);
        
        return update;
    }
    
    public void addUpdate(ComponentState state, ComponentAttribute attribute, String value){
        HashMap<ComponentState, String> states = attributesWithState.get(attribute);

        if (states == null) {
            attributesWithState.put(attribute, new HashMap<ComponentState, String>());
            states = attributesWithState.get(attribute);
        }

        states.put(state, value);
    }

    public void addUpdate(ComponentAttribute attribute, String value){
        attributesWithoutState.put(attribute, value);
    }

    public Attributes clone(String id){
        Attributes clone = new Attributes(id);

        clone.attributesWithoutState = (HashMap<ComponentAttribute, String>) attributesWithoutState.clone();

        for(String input : inputs.values())
            clone.addInput(input);

        for(ButtonEvent buttonEvent : buttonEvents.values())
            clone.addButtonEvent(buttonEvent.clone());

        for(Map.Entry pairs : attributesWithState.entrySet())
            clone.attributesWithState.put((ComponentAttribute)pairs.getKey(), (HashMap<ComponentState, String>) ((HashMap<ComponentState, String>) pairs.getValue()).clone());

        return clone;
    }

    private static int generateIdToReturn(){
        return idToReturn++;
    }
}
