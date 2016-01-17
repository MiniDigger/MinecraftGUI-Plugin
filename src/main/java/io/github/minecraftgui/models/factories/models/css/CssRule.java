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

package io.github.minecraftgui.models.factories.models.css;

import io.github.minecraftgui.models.components.*;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.TextArea;
import io.github.minecraftgui.models.shapes.PolygonColor;
import io.github.minecraftgui.models.shapes.Shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Samuel on 2016-01-13.
 */
public class CssRule {

    private static final String SHAPE_ON_PROGRESS = "on-progress";
    private static final String SHAPE_ON_VALUE_TRUE = "on-value-true";
    private static final String SHAPE_ON_VALUE_FALSE = "on-value-false";
    private static final Pattern VALUE_DOUBLE = Pattern.compile("^-?\\d+(\\.\\d)?$");
    private static final Pattern VALUE_DOUBLE_TIME = Pattern.compile("^\\d+(\\.\\d)? \\d+$");
    private static final Pattern VALUE_DOUBLE_RELATIVE = Pattern.compile("^\\d+%( \\d+)?$");
    private static final Pattern VALUE_DOUBLE_RELATIVE_SPECIFIC = Pattern.compile("^\\d+% \\w+( \\d+)$");
    private static final Pattern VALUE_COLOR = Pattern.compile("^(\\d{1,3},){3}\\d{1,3}$");
    private static final Pattern VALUE_COLOR_TIME = Pattern.compile("^(\\d{1,3},){3}\\d{1,3} \\d+$");
    private static final Pattern VALUE_POSITIONS = Pattern.compile("^-?\\d+(\\.\\d)?,-?\\d+(\\.\\d)?( -?\\d+(\\.\\d)?,-?\\d+(\\.\\d)?){2,}$");
    private static final Pattern VALUE_POSITION = Pattern.compile("^(parent@)?\\w+ -?\\d+%(, (parent@)?\\w+ -?\\d+%)*$");

    private final ArrayList<String> selectors;
    private final ArrayList<CssDeclaration> declarations;
    private State state;

    public CssRule() {
        selectors = new ArrayList<>();
        declarations = new ArrayList<>();
    }

    public void applyRulesOnComponent(Component component){
        for(CssDeclaration cssDeclaration : declarations)
            cssDeclaration.applyOnComponent(component);
    }

    public ArrayList<String> getSelectors() {
        return selectors;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void addDeclaration(String declaration){
        String split[] = declaration.split(":");
        String attribute = split[0].toLowerCase().trim();
        String shape = "";

        if(attribute.startsWith(SHAPE_ON_PROGRESS)) {
            attribute = attribute.substring(SHAPE_ON_PROGRESS.length()+1);
            shape = SHAPE_ON_PROGRESS;
        }
        else if(attribute.startsWith(SHAPE_ON_VALUE_FALSE)) {
            attribute = attribute.substring(SHAPE_ON_VALUE_FALSE.length()+1);
            shape = SHAPE_ON_VALUE_FALSE;
        }
        else if(attribute.startsWith(SHAPE_ON_VALUE_TRUE)) {
            attribute = attribute.substring(SHAPE_ON_VALUE_TRUE.length()+1);
            shape = SHAPE_ON_VALUE_TRUE;
        }

        switch (attribute){
            case "text-cursor-color": declarations.add(new TextCursorColor(shape, state, split[1].trim())); break;
            case "text-nb-line": declarations.add(new TextNbLine(shape, state, split[1].trim())); break;
            case "text-alignment": declarations.add(new TextAlignment(shape, state, split[1].trim())); break;
            case "font": declarations.add(new Font(shape, state, split[1].trim())); break;
            case "font-color": declarations.add(new FontColor(shape, state, split[1].trim())); break;
            case "font-size": declarations.add(new FontSize(shape, state, split[1].trim())); break;
            case "width": declarations.add(new Width(shape, state, split[1].trim())); break;
            case "height": declarations.add(new Height(shape, state, split[1].trim())); break;
            case "x-relative": declarations.add(new XRelative(shape, state, split[1].trim())); break;
            case "y-relative": declarations.add(new YRelative(shape, state, split[1].trim())); break;
            case "x-position": declarations.add(new PositionX(shape, state, split[1].trim())); break;
            case "y-position": declarations.add(new PositionY(shape, state, split[1].trim())); break;
            case "visibility": declarations.add(new Visibility(shape, state, split[1].trim())); break;
            case "cursor": declarations.add(new Cursor(shape, state, split[1].trim())); break;
            case "positions": declarations.add(new Positions(shape, state, split[1].trim())); break;
            case "background-color": declarations.add(new BackgroundColor(shape, state, split[1].trim())); break;
            case "background-image": declarations.add(new BackgroundImage(shape, state, split[1].trim())); break;
            case "margin":
                declarations.add(new Margin(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Margin.TOP));
                declarations.add(new Margin(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Margin.RIGHT));
                declarations.add(new Margin(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Margin.BOTTOM));
                declarations.add(new Margin(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Margin.LEFT));
                break;
            case "margin-top": declarations.add(new Margin(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Margin.TOP)); break;
            case "margin-left": declarations.add(new Margin(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Margin.LEFT)); break;
            case "margin-right": declarations.add(new Margin(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Margin.RIGHT)); break;
            case "margin-bottom": declarations.add(new Margin(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Margin.BOTTOM)); break;
            case "border":
                declarations.add(new Border(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.TOP));
                declarations.add(new Border(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.RIGHT));
                declarations.add(new Border(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.BOTTOM));
                declarations.add(new Border(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.LEFT));
                break;
            case "border-top": declarations.add(new Border(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.TOP)); break;
            case "border-left": declarations.add(new Border(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.LEFT)); break;
            case "border-right": declarations.add(new Border(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.RIGHT)); break;
            case "border-bottom": declarations.add(new Border(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.BOTTOM)); break;
            case "border-color":
                declarations.add(new BorderColor(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.TOP));
                declarations.add(new BorderColor(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.RIGHT));
                declarations.add(new BorderColor(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.BOTTOM));
                declarations.add(new BorderColor(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.LEFT));
                break;
            case "border-top-color": declarations.add(new BorderColor(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.TOP)); break;
            case "border-left-color": declarations.add(new BorderColor(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.LEFT)); break;
            case "border-right-color": declarations.add(new BorderColor(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.RIGHT)); break;
            case "border-bottom-color": declarations.add(new BorderColor(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Border.BOTTOM)); break;
            case "padding":
                declarations.add(new Padding(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Padding.TOP));
                declarations.add(new Padding(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Padding.RIGHT));
                declarations.add(new Padding(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Padding.BOTTOM));
                declarations.add(new Padding(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Padding.LEFT));
                break;
            case "padding-top": declarations.add(new Padding(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Padding.TOP)); break;
            case "padding-left": declarations.add(new Padding(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Padding.LEFT)); break;
            case "padding-right": declarations.add(new Padding(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Padding.RIGHT)); break;
            case "padding-bottom": declarations.add(new Padding(shape, state, split[1].trim(), io.github.minecraftgui.models.shapes.Padding.BOTTOM)); break;
        }
    }

    public void addSelector(String selector){
        selectors.add(selector);
    }

    private static class TextNbLine extends DoubleDeclaration{

        public TextNbLine(String shape, State state, String value) {
            super(shape, state, value);
        }

        @Override
        public void applyOnComponent(Component component) {
            if(component instanceof TextArea){
                TextArea text = (TextArea) component;
                text.setNbLineVisible(value.intValue());
            }
            else if(component instanceof Paragraph){
                Paragraph text = (Paragraph) component;
                text.setNbLineVisible(value.intValue());
            }
        }
    }

    private static class TextAlignment extends CssDeclaration{

        private final io.github.minecraftgui.models.components.TextAlignment value;

        public TextAlignment(String shape, State state, String value) {
            super(shape, state);
            this.value = io.github.minecraftgui.models.components.TextAlignment.valueOf(value.trim().toUpperCase());
        }

        @Override
        public void applyOnComponent(Component component) {
            if(component instanceof Paragraph){
                Paragraph paragraph = (Paragraph) component;
                paragraph.setTextAlignement(value);
            }
            else if(component instanceof TextArea){
                TextArea textArea = (TextArea) component;
                textArea.setTextAlignement(value);
            }
        }
    }

    private static class TextCursorColor extends ColorDeclaration{

        public TextCursorColor(String shape, State state, String value) {
            super(shape, state, value);
        }

        @Override
        public void applyOnComponent(Component component) {
            if(component instanceof ComponentEditableText){
                ComponentEditableText text = (ComponentEditableText) component;
                text.setCursorColor(state, value);
            }
        }
    }

    private static class FontSize extends DoubleDeclaration{

        public FontSize(String shape, State state, String value) {
            super(shape, state, value);
        }

        @Override
        public void applyOnComponent(Component component) {
            if(component instanceof ComponentText){
                ComponentText text = (ComponentText) component;
                text.setFontSize(state, value.intValue());
            }
        }
    }

    private static class FontColor extends ColorDeclaration{

        public FontColor(String shape, State state, String value) {
            super(shape, state, value);
        }

        @Override
        public void applyOnComponent(Component component) {
            if(component instanceof ComponentText){
                ComponentText text = (ComponentText) component;
                text.setFontColor(state, value);
            }
        }
    }

    private static class Font extends CssDeclaration{

        private final String value;

        public Font(String shape, State state, String value) {
            super(shape, state);
            this.value = value.trim();
        }

        @Override
        public void applyOnComponent(Component component) {
            if(component instanceof ComponentText){
                ComponentText text = (ComponentText) component;
                text.setFont(state, value);
            }
        }
    }

    private static class PositionY extends PositionDeclaration{

        public PositionY(String shape, State state, String value) {
            super(shape, state, value);
        }

        @Override
        public void applyOnComponent(Component component) {
            for(PositionContainer positionContainer : positionContainers)
                component.addYRelativeTo(getShape(component, positionContainer), positionContainer.attribute, positionContainer.percentage);
        }
    }

    private static class PositionX extends PositionDeclaration{

        public PositionX(String shape, State state, String value) {
            super(shape, state, value);
        }

        @Override
        public void applyOnComponent(Component component) {
            for(PositionContainer positionContainer : positionContainers)
                component.addXRelativeTo(getShape(component, positionContainer), positionContainer.attribute, positionContainer.percentage);
        }
    }

    private static abstract class PositionDeclaration extends CssDeclaration{

        protected final ArrayList<PositionContainer> positionContainers;

        public PositionDeclaration(String shape, State state, String value) {
            super(shape, state);
            this.positionContainers = new ArrayList<>();

            Matcher matcher = VALUE_POSITION.matcher(value);

            if(matcher.find()) {
                if (value.contains(",")) {
                    String values[] = value.split(",");

                    for (String val : values) {
                        String s[] = val.trim().split(" ");
                        PositionContainer positionContainer = new PositionContainer();
                        positionContainer.isParentAttribute = s[0].startsWith("parent@");
                        positionContainer.percentage = Double.parseDouble(s[1].substring(0, s[1].length() - 1)) / 100;
                        positionContainer.attribute = positionContainer.isParentAttribute ? AttributeDouble.valueOf(s[0].substring(s[0].indexOf("@") + 1).toUpperCase().replaceAll("-", "_")) : AttributeDouble.valueOf(s[0].toUpperCase().replaceAll("-", "_"));

                        positionContainers.add(positionContainer);
                    }
                } else {
                    String s[] = value.trim().split(" ");
                    PositionContainer positionContainer = new PositionContainer();
                    positionContainer.isParentAttribute = s[0].startsWith("parent@");
                    positionContainer.percentage = Double.parseDouble(s[1].substring(0, s[1].length() - 1)) / 100;
                    positionContainer.attribute = positionContainer.isParentAttribute ? AttributeDouble.valueOf(s[0].substring(s[0].indexOf("@") + 1).toUpperCase().replaceAll("-", "_")) : AttributeDouble.valueOf(s[0].toUpperCase().replaceAll("-", "_"));

                    positionContainers.add(positionContainer);
                }
            }
        }

        public Shape getShape(Component component, PositionContainer positionContainer){
            return positionContainer.isParentAttribute?component.getParent().getShape():component.getShape();
        }

        protected static class PositionContainer{
            boolean isParentAttribute;
            AttributeDouble attribute;
            double percentage;
        }
    }

    private static class YRelative extends DoubleTimeDeclaration{

        public YRelative(String shape, State state, String value) {
            super(shape, state, value);
        }

        @Override
        public void applyOnComponent(Component component) {
            component.setYRelative(state, value, time);
        }
    }

    private static class XRelative extends DoubleTimeDeclaration{

        public XRelative(String shape, State state, String value) {
            super(shape, state, value);
        }

        @Override
        public void applyOnComponent(Component component) {
            component.setXRelative(state, value, time);
        }
    }

    private static class Visibility extends CssDeclaration{

        private final String value;

        public Visibility(String shape, State state, String value) {
            super(shape, state);
            this.value = value;
        }

        @Override
        public void applyOnComponent(Component component) {
            component.setVisibility(io.github.minecraftgui.models.components.Visibility.valueOf(value.toUpperCase()));
        }
    }

    private static class Cursor extends CssDeclaration{

        private final String value;

        public Cursor(String shape, State state, String value) {
            super(shape, state);
            this.value = value;
        }

        @Override
        public void applyOnComponent(Component component) {
            component.setCursor(state, io.github.minecraftgui.models.components.Cursor.valueOf(value.toUpperCase()));
        }
    }

    private static class Positions extends CssDeclaration{

        private double positions[][];

        public Positions(String shape, State state, String value) {
            super(shape, state);
            String positionsStr[] = value.split(" ");

            Matcher matcher = VALUE_POSITIONS.matcher(value);

            if(matcher.find()) {
                positions = new double[positionsStr.length][2];

                for(int i = 0; i < positionsStr.length; i++){
                    String values[] = positionsStr[i].split(",");

                    positions[i][0] = Double.parseDouble(values[0]);
                    positions[i][1] = Double.parseDouble(values[1]);
                }
            }
            else
                positions = new double[0][0];
        }

        @Override
        public void applyOnComponent(Component component) {
            Shape shape = getShape(component);

            if(shape != null){
                if (this.shape.equals("") && component instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) component;

                    if (checkBox.getShapeOnValueTrue() instanceof PolygonColor)
                        ((PolygonColor) checkBox.getShapeOnValueTrue()).setPositions(positions);
                    if (checkBox.getShapeOnValueFalse() instanceof PolygonColor)
                        ((PolygonColor) checkBox.getShapeOnValueFalse()).setPositions(positions);
                } else if (shape instanceof PolygonColor)
                    ((PolygonColor) shape).setPositions(positions);
            }
        }
    }

    private static class BackgroundImage extends CssDeclaration{

        private final String value;

        public BackgroundImage(String shape, State state, String value) {
            super(shape, state);
            this.value = value.trim();
        }

        @Override
        public void applyOnComponent(Component component) {
            Shape shape = getShape(component);

            if(shape != null){
                if(this.shape.equals("") && component instanceof CheckBox){
                    CheckBox checkBox = (CheckBox) component;

                    checkBox.getShapeOnValueTrue().setBackground(state, value);
                    checkBox.getShapeOnValueFalse().setBackground(state, value);
                }
                else
                    shape.setBackground(state, value);
            }
        }
    }

    private static class BackgroundColor extends ColorTimeDeclaration{

        public BackgroundColor(String shape, State state, String value) {
            super(shape, state, value);
        }

        @Override
        public void applyOnComponent(Component component) {
            Shape shape = getShape(component);

            if(shape != null){
                if(this.shape.equals("") && component instanceof CheckBox){
                    CheckBox checkBox = (CheckBox) component;

                    checkBox.getShapeOnValueTrue().setBackground(state, value, time);
                    checkBox.getShapeOnValueFalse().setBackground(state, value, time);
                }
                else
                    shape.setBackground(state, value, time);
            }
        }
    }

    private static class BorderColor extends ColorTimeDeclaration{

        private final io.github.minecraftgui.models.shapes.Border border;

        public BorderColor(String shape, State state, String value, io.github.minecraftgui.models.shapes.Border border) {
            super(shape, state, value);
            this.border = border;
        }

        @Override
        public void applyOnComponent(Component component) {
            Shape shape = getShape(component);

            if(shape != null){
                if(this.shape.equals("") && component instanceof CheckBox){
                    CheckBox checkBox = (CheckBox) component;

                    checkBox.getShapeOnValueTrue().setBorderColor(state, border, value, time);
                    checkBox.getShapeOnValueFalse().setBorderColor(state, border, value, time);
                }
                else
                    shape.setBorderColor(state, border, value, time);
            }
        }
    }

    private static class Padding extends DoubleTimeDeclaration{

        private final io.github.minecraftgui.models.shapes.Padding padding;

        public Padding(String shape, State state, String value, io.github.minecraftgui.models.shapes.Padding padding) {
            super(shape, state, value);
            this.padding = padding;
        }

        @Override
        public void applyOnComponent(Component component) {
            Shape shape = getShape(component);

            if(shape != null){
                if(this.shape.equals("") && component instanceof CheckBox){
                    CheckBox checkBox = (CheckBox) component;

                    checkBox.getShapeOnValueTrue().setPadding(state, padding, value, time);
                    checkBox.getShapeOnValueFalse().setPadding(state, padding, value, time);
                }
                else
                    shape.setPadding(state, padding, value, time);
            }
        }

    }

    private static class Margin extends DoubleTimeDeclaration{

        private final io.github.minecraftgui.models.shapes.Margin margin;

        public Margin(String shape, State state, String value, io.github.minecraftgui.models.shapes.Margin margin) {
            super(shape, state, value);
            this.margin = margin;
        }

        @Override
        public void applyOnComponent(Component component) {
            Shape shape = getShape(component);

            if(shape != null){
                if(this.shape.equals("") && component instanceof CheckBox){
                    CheckBox checkBox = (CheckBox) component;

                    checkBox.getShapeOnValueTrue().setMargin(state, margin, value, time);
                    checkBox.getShapeOnValueFalse().setMargin(state, margin, value, time);
                }
                else
                    shape.setMargin(state, margin, value, time);
            }
        }

    }

    private static class Border extends DoubleTimeDeclaration{

        private final io.github.minecraftgui.models.shapes.Border border;

        public Border(String shape, State state, String value, io.github.minecraftgui.models.shapes.Border border) {
            super(shape, state, value);
            this.border = border;
        }

        @Override
        public void applyOnComponent(Component component) {
            Shape shape = getShape(component);

            if(shape != null){
                if(this.shape.equals("") && component instanceof CheckBox){
                    CheckBox checkBox = (CheckBox) component;

                    checkBox.getShapeOnValueTrue().setBorder(state, border, value, time);
                    checkBox.getShapeOnValueFalse().setBorder(state, border, value, time);
                }
                else
                    shape.setBorder(state, border, value, time);
            }
        }

    }

    private static class Width extends DoubleRelativeDeclaration{

        public Width(String shape, State state, String value) {
            super(shape, state, value, AttributeDouble.WIDTH);
        }

        @Override
        public void applyOnComponent(Component component) {
            Shape shape = getShape(component);

            if(shape != null){
                if(this.shape.equals("") && component instanceof CheckBox){
                    CheckBox checkBox = (CheckBox) component;

                    if(isRelative) {
                        checkBox.getShapeOnValueTrue().setWidth(state, component.getParent().getShape(), attributeRelativeTo, time, value);
                        checkBox.getShapeOnValueFalse().setWidth(state, component.getParent().getShape(), attributeRelativeTo, time, value);
                    }
                    else {
                        checkBox.getShapeOnValueTrue().setWidth(state, value, time);
                        checkBox.getShapeOnValueFalse().setWidth(state, value, time);
                    }
                }
                else if(this.shape.equals(SHAPE_ON_PROGRESS) && isRelative){
                    Shape parentShape = null;

                    if(component instanceof Slider)
                        parentShape = ((Slider) component).getShape();
                    if(component instanceof ProgressBar)
                        parentShape = ((ProgressBar) component).getShape();

                    shape.setWidth(state, parentShape, attributeRelativeTo, time, value);
                }
                else{
                    if(isRelative)
                        shape.setWidth(state, component.getParent().getShape(), attributeRelativeTo, time, value);
                    else
                        shape.setWidth(state, value, time);
                }
            }
        }

    }

    private static class Height extends DoubleRelativeDeclaration{

        public Height(String shape, State state, String value) {
            super(shape, state, value, AttributeDouble.HEIGHT);
        }

        @Override
        public void applyOnComponent(Component component) {
            Shape shape = getShape(component);

            if(shape != null){
                if(this.shape.equals("") && component instanceof CheckBox){
                    CheckBox checkBox = (CheckBox) component;

                    if(isRelative) {
                        checkBox.getShapeOnValueTrue().setHeight(state, component.getParent().getShape(), attributeRelativeTo, time, value);
                        checkBox.getShapeOnValueFalse().setHeight(state, component.getParent().getShape(), attributeRelativeTo, time, value);
                    }
                    else {
                        checkBox.getShapeOnValueTrue().setHeight(state, value, time);
                        checkBox.getShapeOnValueFalse().setHeight(state, value, time);
                    }
                }
                else if(this.shape.equals(SHAPE_ON_PROGRESS) && isRelative){
                    Shape parentShape = null;

                    if(component instanceof Slider)
                        parentShape = ((Slider) component).getShape();
                    if(component instanceof ProgressBar)
                        parentShape = ((ProgressBar) component).getShape();

                    shape.setHeight(state, parentShape, attributeRelativeTo, time, value);
                }
                else{
                    if(isRelative)
                        shape.setHeight(state, component.getParent().getShape(), attributeRelativeTo, time, value);
                    else
                        shape.setHeight(state, value, time);
                }
            }
        }

    }

    private static abstract class ColorDeclaration extends CssDeclaration{

        protected Color value = null;

        public ColorDeclaration(String shape, State state, String value) {
            super(shape, state);

            Matcher matcher = VALUE_COLOR.matcher(value);

            if(matcher.find()){
                String colors[] = value.split(",");
                int r = Integer.parseInt(colors[0]);
                int g = Integer.parseInt(colors[1]);
                int b = Integer.parseInt(colors[2]);
                int a = Integer.parseInt(colors[3]);

                this.value = new Color(r,g,b,a);
            }
        }

    }

    private static abstract class ColorTimeDeclaration extends ColorDeclaration{

        protected long time = 0;

        public ColorTimeDeclaration(String shape, State state, String value) {
            super(shape, state, value);

            if(this.value == null){
                Matcher matcher = VALUE_COLOR_TIME.matcher(value);

                if(matcher.find()) {
                    String values[] = value.split(" ");
                    String colors[] = values[0].split(",");
                    int r = Integer.parseInt(colors[0]);
                    int g = Integer.parseInt(colors[1]);
                    int b = Integer.parseInt(colors[2]);
                    int a = Integer.parseInt(colors[3]);

                    this.value = new Color(r,g,b,a);
                    this.time = Long.parseLong(values[1]);
                }
            }
        }

    }

    private static abstract class DoubleTimeDeclaration extends DoubleDeclaration{

        protected long time = 0;

        public DoubleTimeDeclaration(String shape, State state, String value) {
            super(shape, state, value);

            if(this.value == null){
                Matcher matcher = VALUE_DOUBLE_TIME.matcher(value);

                if(matcher.find()) {
                    String values[] = value.split(" ");

                    this.value = Double.parseDouble(values[0]);
                    this.time = Long.parseLong(values[1]);
                }
            }
        }
    }

    private static abstract class DoubleDeclaration extends CssDeclaration{

        protected Double value = null;

        public DoubleDeclaration(String shape, State state, String value) {
            super(shape, state);

            Matcher matcher = VALUE_DOUBLE.matcher(value);

            if(matcher.find())
                this.value = Double.parseDouble(value);
        }
    }

    private static abstract class DoubleRelativeDeclaration extends DoubleTimeDeclaration{

        protected AttributeDouble attributeRelativeTo;
        protected boolean isRelative = false;

        public DoubleRelativeDeclaration(String shape, State state, String value, AttributeDouble defaultValue) {
            super(shape, state, value);

            if(this.value == null){
                Matcher matcher = VALUE_DOUBLE_RELATIVE_SPECIFIC.matcher(value);

                if(matcher.find()) {
                    isRelative = true;
                    String values[] = value.split(" ");

                    this.value = Double.parseDouble(values[0].substring(0, values[0].length()-1))/100;
                    this.time = values.length  == 3?Long.parseLong(values[2]):0;
                    this.attributeRelativeTo = AttributeDouble.valueOf(values[1]);
                }

                matcher = VALUE_DOUBLE_RELATIVE.matcher(value);

                if(matcher.find()) {
                    isRelative = true;
                    String values[] = value.split(" ");

                    this.value = Double.parseDouble(values[0].substring(0, values[0].length()-1))/100;
                    this.time = values.length  == 2?Long.parseLong(values[1]):0;
                }

                if(isRelative && attributeRelativeTo == null)
                    attributeRelativeTo = defaultValue;
            }
        }
    }

    private static abstract class CssDeclaration{

        protected final String shape;
        protected final State state;

        public abstract void applyOnComponent(Component component);

        public CssDeclaration(String shape, State state) {
            this.shape = shape.toLowerCase();
            this.state = state;
        }

        public Shape getShape(Component component){
            switch (shape){
                case "": return component.getShape();
                case SHAPE_ON_VALUE_TRUE:
                    if(component instanceof CheckBox)
                        return ((CheckBox) component).getShapeOnValueTrue();
                    break;
                case SHAPE_ON_VALUE_FALSE:
                    if(component instanceof CheckBox)
                        return ((CheckBox) component).getShapeOnValueFalse();
                    break;
                case SHAPE_ON_PROGRESS:
                    if(component instanceof ProgressBar)
                        return ((ProgressBar) component).getShapeOnProgress();
                    if(component instanceof Slider)
                        return ((Slider) component).getShapeOnProgress();
                    break;
            }

            return null;
        }

    }

}
