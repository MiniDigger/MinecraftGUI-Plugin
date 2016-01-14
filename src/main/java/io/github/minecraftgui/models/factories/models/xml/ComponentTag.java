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

package io.github.minecraftgui.models.factories.models.xml;

import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.factories.GuiFactory;
import io.github.minecraftgui.models.factories.models.css.CssRule;
import io.github.minecraftgui.models.shapes.*;
import io.github.minecraftgui.views.MinecraftGuiService;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Samuel on 2016-01-12.
 */
public abstract class ComponentTag extends Tag {

    protected abstract Component createComponent(MinecraftGuiService service, UserGui userGui);

    protected final String id;
    protected final ArrayList<String> classes;
    protected Class<? extends Shape> shape;
    protected final ArrayList<CssRule> rules;

    public ComponentTag(Element element, GuiFactory.GuiModel model) {
        super(element, model);
        id = element.getAttribute("id");
        shape = getShapeByName(element.getAttribute("shape"));
        this.classes = generateClasses(element);
        this.rules = new ArrayList<>();
    }

    public ArrayList<CssRule> getRules() {
        return rules;
    }

    public void addCssRule(CssRule rule){
        if(!rules.contains(rule))
            rules.add(rule);
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    protected void setAttributes(Component component){
        for(CssRule cssRule : rules)
            cssRule.applyRulesOnComponent(component);
    }

    public Component getComponent(MinecraftGuiService service, UserGui userGui){
        Component component = createComponent(service, userGui);
        setAttributes(component);

        for(Tag tag : getChildren())
            if(tag instanceof ComponentTag)
                ((ComponentTag) tag).getComponent(service, userGui, component);

        return component;
    }

    private void getComponent(MinecraftGuiService service, UserGui userGui, Component parent){
        Component component = createComponent(service, userGui);
        System.out.println(parent.getClass().getName());
        parent.add(component, userGui);

        setAttributes(component);

        for(Tag tag : getChildren())
            if(tag instanceof ComponentTag) {
                ComponentTag componentTag = ((ComponentTag) tag);

                componentTag.getComponent(service, userGui, component);
            }
    }

    private ArrayList<String> generateClasses(Element element){
        if(!element.getAttribute("class").equals(""))
            return new ArrayList<>(Arrays.asList(element.getAttribute("class").split(" ")));
        else
            return new ArrayList<>();
    }

    protected static Class<? extends Shape> getShapeByName(String name){
        name = name.toLowerCase();

        if(name.equals("ellipse-color")) return EllipseColor.class;
        if(name.equals("polygon-color")) return PolygonColor.class;
        if(name.equals("rectangle-color")) return RectangleColor.class;
        if(name.equals("rectangle-image")) return RectangleImage.class;

        return RectangleColor.class;
    }

}
