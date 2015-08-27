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

package djxy.controllers;

import djxy.models.ButtonEvent;
import djxy.models.component.Component;
import djxy.models.component.ComponentState;
import djxy.models.component.ComponentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComponentFactory {

    private static int idToReturn = 0;

    public static Component load(File xml, ArrayList<CSSFactory.CSSRule> cssRules){
        HashMap<Integer, Component> components = new HashMap<>();
        Component rootComponent = new Component(Component.rootComponentId, ComponentType.PANEL);

        try {
            Document document = Jsoup.parse(xml, "UTF-8");
            Element root = document.select("root").first();

            for(Element child : root.children())
                generateComponent(rootComponent, child, components);

            setComponentAttributes(root, cssRules, components);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootComponent;
    }


    private static void setComponentAttributes(Element root, ArrayList<CSSFactory.CSSRule> cssRules, HashMap<Integer, Component> components){
        for(CSSFactory.CSSRule cssRule : cssRules){
            Elements elements = root.select(cssRule.getSelector());

            for(Element element : elements){
                if(!element.nodeName().equals("root")) {
                    Component component = components.get(Integer.parseInt(element.attr("fakeId")));

                    for (Map.Entry pairs : cssRule.getDeclarations().entrySet()) {
                        if (cssRule.getState() != null)
                            component.getAttributes().setAttribute((String) pairs.getKey(), cssRule.getState(), pairs.getValue());
                        else
                            component.getAttributes().setAttribute((String) pairs.getKey(), pairs.getValue());
                    }
                }
            }
        }
    }

    private static void generateComponent(Component parent, Element element, HashMap<Integer, Component> components){
        String nodeName = element.nodeName().toLowerCase();
        Component component = null;

        if(nodeName.equals("buttonevent") && parent != null) {
            parent.getAttributes().addButtonEvent(new ButtonEvent(element.attr("componentId"), ComponentState.getComponentState(element.attr("state")), element.attr("attribute"), CSSFactory.getObject(element.attr("value"))));
            element.remove();
        }
        else if(nodeName.equals("input") && parent != null) {
            parent.getAttributes().addInput(element.attr("componentId"));
            element.remove();
        }
        else{
            String id = element.attr("id");
            ComponentType type = null;

            switch (nodeName){
                case "button":
                    type = ComponentType.BUTTON;
                    break;
                case "buttonurl":
                    type = ComponentType.BUTTON_URL;
                    break;
                case "image":
                    type = ComponentType.IMAGE;
                    break;
                case "inputtext":
                    type = ComponentType.INPUT_TEXT;
                    break;
                case "inputpassword":
                    type = ComponentType.INPUT_PASSWORD;
                    break;
                case "inputdecimal":
                    type = ComponentType.INPUT_DECIMAL;
                    break;
                case "inputinteger":
                    type = ComponentType.INPUT_INTEGER;
                    break;
                case "inputinvisible":
                    type = ComponentType.INPUT_INVISIBLE;
                    break;
                case "panel":
                    type = ComponentType.PANEL;
                    break;
                case "list":
                    type = ComponentType.LIST;
                    break;
                case "paragraph":
                    type = ComponentType.PARAGRAPH;
                    break;
            }

            if(type != null){
                if(id.isEmpty())
                    component = new Component(type, parent);
                else
                    component = new Component(id, type, parent);

                int fakeId = generateComponentFakeId();

                components.put(fakeId, component);
                element.attr("fakeId", fakeId+"");

                for (Element child : element.children())
                    generateComponent(component, child, components);
            }
        }
    }

    private synchronized static int generateComponentFakeId(){
        return idToReturn++;
    }
}
