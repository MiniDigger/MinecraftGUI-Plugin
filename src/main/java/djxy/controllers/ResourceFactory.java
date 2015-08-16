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

import djxy.models.resource.FontResource;
import djxy.models.resource.ImageResource;
import djxy.models.resource.Resource;
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
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ResourceFactory {

    public static ArrayList<Resource> load(File res){
        ArrayList<Resource> resources = new ArrayList<>();

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(res);

            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            NodeList list = root.getChildNodes();

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Resource resource = generateResource((Element) node);

                    if(resource != null)
                        resources.add(resource);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resources;
    }

    public static void save(File xml, ArrayList<Resource> resources){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("resources");
            doc.appendChild(root);

            for(Resource resource : resources) {
                Element element = doc.createElement("resource");
                String type = resource instanceof ImageResource?"image":"font";

                element.setAttribute("type", type);
                element.setAttribute("url", resource.getUrl());

                if(type.equals("image"))
                    element.setAttribute("name", ((ImageResource) resource).getName());

                root.appendChild(element);
            }

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

    private static Resource generateResource(Element element){
        if(element.getNodeName().equals("resource")){
            String type = element.getAttribute("type");
            String url = element.getAttribute("url");

            if(type.equalsIgnoreCase("image"))
                return new ImageResource(url, element.getAttribute("name"));
            if(type.equalsIgnoreCase("font"))
                return new FontResource(url);
        }

        return null;
    }
}
