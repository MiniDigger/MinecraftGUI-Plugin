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

public class ComponentFactory {
/*
    private static int idToReturn = 0;

    public static Component load(File xml, ArrayList<CSSFactory.CSSRule> cssRules){
        HashMap<Integer, Component> components = new HashMap<>();
        Component rootComponent = new Component(Component.rootComponentId, ComponentType.PANEL);

        try {
            Document document = Jsoup.parse(xml, "UTF-8");
            Element root = document.select("root").first();

            for(Element child : root.children())
                generateComponent(rootComponent, child, components);

            setComponentsAttributes(root, cssRules, components);
            setComponentsAttribute("url", ComponentAttribute.URL.name(), root, components);
            setComponentsAttribute("hint", ComponentAttribute.HINT.name(), root, components);
            setComponentsAttribute("value", ComponentAttribute.VALUE.name(), root, components);
            setComponentsValue(root, components);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootComponent;
    }

    private static void setComponentsValue(Element root, HashMap<Integer, Component> components){
        for(Element element : root.select("*")){
            String node = element.nodeName().toLowerCase();

            if(!node.equals("root") && !element.ownText().isEmpty()) {
                Component component = components.get(Integer.parseInt(element.attr("fakeId")));

                System.out.println(element.ownText()+" "+component.getType());

                component.getAttributes().setAttribute(ComponentAttribute.VALUE.name(), element.ownText());
            }
        }
    }

    private static void setComponentsAttribute(String nodeAttribute, String attribute, Element root, HashMap<Integer, Component> components){
        for(Element element : root.select("["+nodeAttribute+"]")){
            Component component = components.get(Integer.parseInt(element.attr("fakeId")));

            component.getAttributes().setAttribute(attribute, element.attr(nodeAttribute));
        }
    }


    private static void setComponentsAttributes(Element root, ArrayList<CSSFactory.CSSRule> cssRules, HashMap<Integer, Component> components){
        for(CSSFactory.CSSRule cssRule : cssRules){
            Elements elements = root.select(cssRule.getSelector());

            for(Element element : elements){
                if(!element.nodeName().equals("root")) {
                    System.out.println(element.nodeName());
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
            parent.getAttributes().addButtonEvent(new ButtonEvent(element.attr("componentId"), ComponentState.getComponentState(element.attr("state")), element.attr("attribute"), CSSFactory.convertStringToObject(element.attr("value"))));
            element.remove();
        }
        else if(nodeName.equals("inputtosend") && parent != null) {
            parent.getAttributes().addInput(element.attr("id"));
            element.remove();
        }
        else{
            String inputType;
            String id = element.attr("id");
            ComponentType type = null;

            switch (nodeName){
                case "button":
                    inputType = element.attr("type").toLowerCase();

                    switch (inputType){
                        case "":
                            type = ComponentType.BUTTON;
                            break;
                        case "normal":
                            type = ComponentType.BUTTON;
                            break;
                        case "url":
                            type = ComponentType.BUTTON_URL;
                            break;
                    }
                    break;
                case "image":
                    type = ComponentType.IMAGE;
                    break;
                case "img":
                    type = ComponentType.IMAGE;
                    break;
                case "input":
                    inputType = element.attr("type").toLowerCase();

                    switch (inputType){
                        case "":
                            type = ComponentType.INPUT_TEXT;
                            break;
                        case "text":
                            type = ComponentType.INPUT_TEXT;
                            break;
                        case "password":
                            type = ComponentType.INPUT_PASSWORD;
                            break;
                        case "decimal":
                            type = ComponentType.INPUT_DECIMAL;
                            break;
                        case "integer":
                            type = ComponentType.INPUT_INTEGER;
                            break;
                        case "invisible":
                            type = ComponentType.INPUT_INVISIBLE;
                            break;
                    }
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
    }*/
}
