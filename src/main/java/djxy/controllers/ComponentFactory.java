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
import djxy.models.component.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComponentFactory {

    public static Component load(File xml, File css){
        HashMap<String, AttributesStructure> attributes = generateAttributes(css);
        Component rootComponent = new Component(Component.rootComponentId, ComponentType.PANEL);

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(xml);

            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            NodeList list = root.getChildNodes();

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE)
                    generateComponent(rootComponent, attributes, (Element) node);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootComponent;
    }

    public static void save(Component component, File xml, File css){
        try {
            ArrayList<AttributesStructure> attributesStructures = new ArrayList<>();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("root");
            doc.appendChild(root);

            for(Component child : component.getChildren())
                root.appendChild(createComponentElement(doc, child, attributesStructures));

            save(css, attributesStructures);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, new StreamResult(new FileOutputStream(xml)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Element createComponentElement(Document doc, Component component, ArrayList<AttributesStructure> attributesStructures){
        Element element = doc.createElement(component.getType().name().replace("_", "").toLowerCase());
        Attributes attributes = component.getAttributes();
        AttributesStructure normal = new AttributesStructure(component.getAttributeId(ComponentState.NORMAL));
        AttributesStructure hover = new AttributesStructure(component.getAttributeId(ComponentState.HOVER));
        AttributesStructure click = new AttributesStructure(component.getAttributeId(ComponentState.CLICK));

        for(ComponentAttribute attribute : ComponentAttribute.values()){
            String value;

            if(ComponentAttribute.attributeHasState(attribute)) {
                for (ComponentState state : ComponentState.values()) {
                    value = attributes.getAttributeValue(attribute, state);

                    if(value != null){
                        switch (state){
                            case NORMAL:
                                normal.setAttribute(attribute, value);
                                break;
                            case HOVER:
                                hover.setAttribute(attribute, value);
                                break;
                            case CLICK:
                                click.setAttribute(attribute, value);
                                break;
                        }
                    }
                }
            }
            else{
                value = attributes.getAttributeValue(attribute);

                if(value != null)
                    normal.setAttribute(attribute, value);
            }
        }

        if(normal.attributes.size() != 0)
            attributesStructures.add(normal);
        else
            component.setAttributeId(ComponentState.NORMAL, null);

        if(hover.attributes.size() != 0)
            attributesStructures.add(hover);
        else
            component.setAttributeId(ComponentState.HOVER, null);

        if(click.attributes.size() != 0)
            attributesStructures.add(click);
        else
            component.setAttributeId(ComponentState.CLICK, null);

        if (!component.getId().isEmpty() && component.getId().charAt(0) != '*')
            element.setAttribute("id", component.getId());

        for (ComponentState state : ComponentState.values())
            if(component.getAttributeId(state) != null)
                element.setAttribute(state.name().toLowerCase(), component.getAttributeId(state));

        for (ButtonEvent buttonEvent : component.getAttributes().getButtonEvents().values()) {
            Element buttonEve = doc.createElement("buttonEvent");
            buttonEve.setAttribute("componentId", buttonEvent.getComponentIdToUpdate());
            buttonEve.setAttribute("state", buttonEvent.getState().name());
            buttonEve.setAttribute("attribute", buttonEvent.getComponentAttribute().name());
            buttonEve.setAttribute("value", buttonEvent.getValue());

            element.appendChild(buttonEve);
        }

        for(String input : component.getAttributes().getInputs().values()){
            Element inp = doc.createElement("input");
            inp.setAttribute("id", input);

            element.appendChild(inp);
        }

        for(Component child : component.getChildren())
            element.appendChild(createComponentElement(doc, child, attributesStructures));

        return element;
    }

    private static void generateComponent(Component parent, HashMap<String, AttributesStructure> attributes, Element element){
        String nodeName = element.getNodeName().toLowerCase();
        Component component = null;

        if(nodeName.equals("buttonevent") && parent != null) {
            String value = element.getAttribute("value");

            parent.getAttributes().addButtonEvent(element.getAttribute("componentId"), ComponentState.getComponentState(element.getAttribute("state")), ComponentAttribute.getComponentAttribute(element.getAttribute("attribute")), value);
        }
        else if(nodeName.equals("input") && parent != null) {
            parent.getAttributes().addInput(element.getAttribute("id"));
        }
        else{
            String id = element.getAttribute("id");
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

                for(ComponentState state : ComponentState.values()){
                    String attributeId = element.getAttribute(state.name().toLowerCase());

                    if(!attributeId.isEmpty() && attributes.get(attributeId) != null) {
                        component.setAttributeId(state, attributeId);
                        setAttributeToComponent(component, state, attributes.get(attributeId));
                    }
                }

                NodeList list = element.getChildNodes();

                for (int i = 0; i < list.getLength(); i++) {
                    Node node = list.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE)
                        generateComponent(component, attributes, (Element) node);
                }
            }
        }
    }

    public static void setAttributeToComponent(Component component, ComponentState state, AttributesStructure attributesStructure){
        for(Map.Entry pairs : attributesStructure.getAttributes().entrySet()){
            ComponentAttribute attribute = (ComponentAttribute) pairs.getKey();
            String value = (String) pairs.getValue();

            if(ComponentAttribute.attributeHasState(attribute))
                component.getAttributes().addUpdate(state, attribute, value);
            else
                component.getAttributes().addUpdate(attribute, value);
        }
    }

    private static HashMap<String, AttributesStructure> generateAttributes(File file){
        HashMap<String, AttributesStructure> attributes = new HashMap<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            boolean getOpeningBrace = false;

            AttributesStructure currentAttributes = null;

            while((line = bufferedReader.readLine()) != null){
                line = line.trim();

                if(!line.isEmpty()){
                    if(!getOpeningBrace){
                        if(line.charAt(line.length()-1) == '{'){
                            getOpeningBrace = true;
                            String id = line.substring(0, line.length() - 1).trim();

                            currentAttributes = new AttributesStructure(id);
                            attributes.put(id, currentAttributes);
                        }
                    }
                    else{
                        if(line.charAt(0) == '}'){
                            currentAttributes = null;
                            getOpeningBrace = false;
                        }
                        else{
                            int index = line.indexOf(':');
                            if(0 < index && line.length()-1 != index) {
                                String attr = line.substring(0, index).replace("-", "_");
                                ComponentAttribute componentAttribute = ComponentAttribute.getComponentAttribute(attr);

                                if (componentAttribute != null) {
                                    String value = line.substring(index+1).trim();

                                    if(value.lastIndexOf(';') == value.length()-1)
                                        value = value.substring(0, value.length()-1);

                                    currentAttributes.setAttribute(componentAttribute, value);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return attributes;
    }

    private static void save(File css, ArrayList<AttributesStructure> attributes){
        String structure = "";

        for(AttributesStructure attr : attributes){
            structure += attr.getId()+" {\n";

            for(Map.Entry pairs : attr.getAttributes().entrySet()) {
                String value = (String) pairs.getValue();

                if(!value.isEmpty())
                    structure += "  " + ((ComponentAttribute) pairs.getKey()).name() + ": " + value + ";\n";
            }

            structure += "}\n\n";
        }

        try {
            FileWriter writer = new FileWriter(css);

            writer.write(structure);

            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class AttributesStructure {

        private String id;
        private HashMap<ComponentAttribute, String> attributes;

        public AttributesStructure(String id) {
            this.id = id;
            this.attributes = new HashMap<>();
        }

        public String getId() {
            return id;
        }

        public HashMap<ComponentAttribute, String> getAttributes() {
            return (HashMap<ComponentAttribute, String>) attributes.clone();
        }

        public void setAttribute(ComponentAttribute componentAttribute, String value){
            attributes.put(componentAttribute, value);
        }
    }
}
