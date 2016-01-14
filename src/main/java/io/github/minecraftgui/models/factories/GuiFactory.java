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

package io.github.minecraftgui.models.factories;

import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.State;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.factories.models.css.CssRule;
import io.github.minecraftgui.models.factories.models.xml.*;
import io.github.minecraftgui.views.MinecraftGuiService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by Samuel on 2016-01-05.
 */
public class GuiFactory {

    private static Pattern CLASS_SELECTOR = Pattern.compile("\\.\\w*");
    private static Pattern ID_SELECTOR = Pattern.compile("#\\w*");

    public static GuiModel createGuiModel(File file){
        GuiModel guiModel = new GuiModel();
        Document document = getDocument(file);

        generateHeader(document, guiModel);
        generateBody(document, guiModel);

        loadCssFiles(guiModel, file);
        setCssRules(guiModel);

        return guiModel;
    }

    private static void setCssRules(GuiModel model){
        for(CssRule rule : model.cssRules){
            try {
                ArrayList<Tag> tags = null;

                for (String selector : rule.getSelectors())
                    tags = getComponentTags(model, tags, selector);

                for (Tag tag : tags)
                    ((ComponentTag) tag).addCssRule(rule);
            }catch(Exception e){
                System.err.println("The css rule "+rule.getSelectors().toString()+" is not valid.");
            }
        }
    }

    private static ArrayList<Tag> getComponentTags(GuiModel model, ArrayList<Tag> list, String selector){
        ArrayList<Tag> tags = new ArrayList<>();

        if(list == null){
            if(selector.startsWith("#"))
                tags.add(model.ids.get(selector.substring(1)));
            else if(selector.startsWith("."))
                tags.addAll(model.classses.get(selector.substring(1)));
            else{
                Class<? extends ComponentTag> tag = (Class<? extends ComponentTag>) Tag.getTagClass(selector);

                tags.addAll(model.tags.get(tag));
            }
        }
        else{
            if(selector.startsWith("#") && list.contains(model.ids.get(selector.substring(1)).getParent()))
                tags.add(model.ids.get(selector.substring(1)));
            else if(selector.startsWith(".")){
                for(ComponentTag tag : model.classses.get(selector.substring(1)))
                    if(list.contains(tag.getParent()))
                        tags.add(tag);
            }
            else{
                for(Tag tag : list) {
                    ArrayList<Tag> l = tag.getTagIndexed(Tag.getTagClass(selector));

                    if(l != null)
                        tags.addAll(l);
                }
            }
        }

        return tags;
    }

    private static void loadCssFiles(GuiModel model, File file){
        if(model.getCssFiles() != null) {
            String path = file.getPath().substring(0, file.getPath().lastIndexOf("\\")+1);

            for(Tag tag : model.getCssFiles()){
                LinkTag linkTag = (LinkTag) tag;
                File css = new File(path+linkTag.getSrc());

                if(css.exists() && css.isFile())
                    loadCssFile(model, css);
            }
        }
    }

    private static void loadCssFile(GuiModel model, File file){
        try{
            StringBuilder buffer = new StringBuilder((int) file.length());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while((line = br.readLine()) != null)
                buffer.append(line);

            br.close();

            String cssClean = buffer.toString().replaceAll("(\\r|\\n)", "").trim();
            String rules[] = cssClean.split("}");

            for(String rule : rules)
                model.cssRules.add(createCssRule(rule));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static CssRule createCssRule(String rule){
        CssRule cssRule = new CssRule();
        String selector = rule.substring(0, rule.indexOf("{")).trim().toLowerCase();
        String declarations[] = rule.substring(rule.indexOf("{") + 1).split(";");

        if(selector.endsWith(":hover"))
            cssRule.setState(State.HOVER);
        else if(selector.endsWith(":active"))
            cssRule.setState(State.ACTIVE);
        else if(selector.endsWith(":focus"))
            cssRule.setState(State.FOCUS);
        else
            cssRule.setState(State.NORMAL);

        if(selector.contains(":"))
            selector = selector.substring(0, selector.indexOf(":"));

        if(selector.contains(">")){
            String selectors[] = selector.split(">");

            for(String selec : selectors)
                cssRule.addSelector(selec.trim());
        }
        else
            cssRule.addSelector(selector.trim());

        for(String declaration : declarations)
            cssRule.addDeclaration(declaration.trim());

        return cssRule;
    }

    private static void generateHeader(Document document, GuiModel model){
        Element head = (Element) document.getDocumentElement().getElementsByTagName("head").item(0);

        model.header = new HeadTag(head, model);
        model.header.init();
    }

    private static void generateBody(Document document, GuiModel model){
        if(document.getDocumentElement().getElementsByTagName("root").getLength() == 1) {
            Element root = (Element) document.getDocumentElement().getElementsByTagName("root").item(0);

            model.body = new RootTag(root, model);
            model.body.init();
        }
        else if(document.getDocumentElement().getElementsByTagName("component").getLength() == 1) {
            Element component = (Element) document.getDocumentElement().getElementsByTagName("component").item(0);

            model.body = new ComponentDependencyTag(component, model);
            model.body.init();
        }
    }

    private static Document getDocument(File file){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new FileInputStream(file));
            doc.getDocumentElement().normalize();

            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public final static class GuiModel {

        private final HashMap<Class<? extends Tag>, ArrayList<Tag>> tags;
        private final HashMap<String, ComponentTag> ids;
        private final HashMap<String, ArrayList<ComponentTag>> classses;
        private final ArrayList<CssRule> cssRules;
        private HeadTag header;
        private ComponentTag body;

        private GuiModel(){
            tags = new HashMap<>();
            ids = new HashMap<>();
            classses = new HashMap<>();
            cssRules = new ArrayList<>();
        }

        public Component getBody(MinecraftGuiService service, UserGui userGui){
            return body.getComponent(service, userGui);
        }

        public void printHead(){
            printTag(header, "");
        }

        public void printBody(){
            printTag(body, "");
        }

        private ArrayList<Tag> getCssFiles(){
            return tags.get(LinkTag.class);
        }

        private void printTag(Tag tag, String indentation){
            System.out.println(indentation+tag.getClass().getSimpleName());

            if(tag instanceof ComponentTag)
                for(CssRule rule : ((ComponentTag) tag).getRules())
                    System.out.println(indentation+"- "+rule.getSelectors().toString());

            for(int i = 0; i < tag.getChildren().size(); i++){
                printTag(tag.getChildren().get(i), indentation+"    ");
            }
        }

        public void addTag(Tag tag){
            ArrayList<Tag> tags = this.tags.get(tag.getClass());

            if(tags == null){
                tags = new ArrayList<>();
                this.tags.put(tag.getClass(), tags);
            }

            tags.add(tag);

            if(tag instanceof ComponentTag) {
                ComponentTag componentTag = (ComponentTag) tag;

                for(String clazz : componentTag.getClasses()){
                    ArrayList<ComponentTag> classes = this.classses.get(clazz);

                    if (classes == null) {
                        classes = new ArrayList<>();
                        this.classses.put(clazz, classes);
                    }

                    classes.add(componentTag);
                }

                ids.put(((ComponentTag) tag).getId(), (ComponentTag) tag);
            }
        }

    }

}
