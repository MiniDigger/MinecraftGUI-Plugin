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

import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.factories.GuiFactory;
import io.github.minecraftgui.views.PluginInterface;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Samuel on 2016-01-05.
 */
public abstract class Tag {

    public static final Pattern PATTERN_PLAYER_UUID = Pattern.compile("\\{PLAYER_UUID\\}");
    public static final Pattern PATTERN_PLAYER_NAME = Pattern.compile("\\{PLAYER_NAME\\}");

    private static final ConcurrentHashMap<String, Class<? extends Tag>> xmlTags = new ConcurrentHashMap<>();

    static {
        xmlTags.put("head", HeadTag.class);
        xmlTags.put("component", ComponentDependencyTag.class);
        xmlTags.put("root", RootTag.class);
        xmlTags.put("textarea", TextAreaTag.class);
        xmlTags.put("checkbox", CheckBoxTag.class);
        xmlTags.put("div", DivTag.class);
        xmlTags.put("input", InputTag.class);
        xmlTags.put("list", ListTag.class);
        xmlTags.put("p", ParagraphTag.class);
        xmlTags.put("progressbar", ProgressBarTag.class);
        xmlTags.put("slider", SliderTag.class);
        xmlTags.put("dependency", DependencyTag.class);
        xmlTags.put("download", DownloadTag.class);
        xmlTags.put("generate", GenerateTag.class);
        xmlTags.put("img", ImageTag.class);
        xmlTags.put("link", LinkTag.class);
        xmlTags.put("option", OptionTag.class);
        xmlTags.put("onformsend", OnFormSendTag.class);
        xmlTags.put("playerworld", PlayerWorldTag.class);
        xmlTags.put("onguievent", OnGuiEventTag.class);
    }

    private static Tag createXmlTag(Element element, GuiFactory.GuiModel model){
        try {
            Class<? extends Tag> clazz = xmlTags.get(element.getTagName().toLowerCase());
            Constructor<?> ctor = clazz.getConstructor(Element.class, GuiFactory.GuiModel.class);
            return (Tag) ctor.newInstance(element, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<? extends Tag> getTagClass(String tag){
        return xmlTags.get(tag.toLowerCase());
    }

    /*******************************************
    *******************************************/

    private final GuiFactory.GuiModel guiModel;
    private Tag parent;
    private final Element element;
    private final HashMap<Class<? extends Tag>, ArrayList<Tag>> childrenIndexed;
    private final ArrayList<Tag> children;
    private String text;

    public Tag(Element element, GuiFactory.GuiModel model) {
        children = new ArrayList<>();
        childrenIndexed = new HashMap<>();
        this.guiModel = model;
        this.element = element;
        this.text = "";

        for(int i = 0; i < element.getChildNodes().getLength(); i++)
            text = text.equals("") && element.getChildNodes().item(i).getNodeName().equals("#text")?element.getChildNodes().item(i).getTextContent().trim():text;
    }

    public Tag getParent() {
        return parent;
    }

    public ArrayList<Tag> getChildren() {
        return children;
    }

    public void add(Tag tag){
        children.add(tag);
        addIndexedTag(tag);
    }

    public ArrayList<Tag> getTagIndexed(Class<? extends Tag> clazz){
        return childrenIndexed.get(clazz);
    }

    protected String getText(){
        return text;
    }

    private void addIndexedTag(Tag tag){
        ArrayList<Tag> models = childrenIndexed.get(tag.getClass());

        if(models == null){
            models = new ArrayList<>();
            childrenIndexed.put(tag.getClass(), models);
        }

        models.add(tag);
    }

    protected String convertString(PluginInterface service, UserGui userGui, String text){
        StringBuilder str = new StringBuilder(text);
        Matcher matcher;

        while((matcher = PATTERN_PLAYER_UUID.matcher(str)).find())
            str.replace(matcher.start(), matcher.end(), userGui.getPlayerUUID().toString());

        while((matcher = PATTERN_PLAYER_NAME.matcher(str)).find())
            str.replace(matcher.start(), matcher.end(), service.getPlayerName(userGui.getPlayerUUID()));

        return str.toString();
    }

    protected Tag getXmlTagSetAs(Element element, String as){
        for(int i = 0; i < element.getChildNodes().getLength(); i++){
            Node node = element.getChildNodes().item(i);

            if(node instanceof Element && ((Element) node).getAttribute("setAs").equalsIgnoreCase(as)) {
                element.removeChild(node);
                Tag tag = createXmlTag((Element) node, this.guiModel);

                addIndexedTag(tag);

                return tag;
            }
        }

        return null;
    }

    public void init(){
        this.guiModel.addTag(this);

        for(int i = 0; i < element.getChildNodes().getLength(); i++) {
            Node node = element.getChildNodes().item(i);

            if (node instanceof Element) {
                Tag tag = createXmlTag((Element) node, this.guiModel);
                tag.parent = this;
                this.add(tag);

                tag.init();
            }
        }
    }

}
