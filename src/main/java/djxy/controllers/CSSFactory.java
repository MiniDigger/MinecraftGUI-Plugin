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

package djxy.controllers;

import djxy.models.component.ComponentState;
import djxy.models.component.Side;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Samuel on 2015-08-22.
 */
public class CSSFactory {

    private static Pattern REGEX_ATTRIBUTE = Pattern.compile("(\\[[^\\]]+\\])");
    private static Pattern REGEX_ID = Pattern.compile("(#[^\\s\\+>~\\.\\[:]+)");
    private static Pattern REGEX_CLASS = Pattern.compile("(\\.[^\\s\\+>~\\.\\[:]+)");
    private static Pattern REGEX_PSEUDO_CLASS_BRACKET = Pattern.compile("(:[\\w-]+\\([^\\)]*\\))");
    private static Pattern REGEX_ELEMENT = Pattern.compile("([^\\s\\+>~\\.\\[:]+)");

    private static Pattern REGEX_COLOR = Pattern.compile("(^([0-9]{1,3},[0-9]{1,3},[0-9]{1,3},[0-9]{1,3})$)");
    private static Pattern REGEX_SIDE = Pattern.compile("(^(((false|true),?){4})$)", Pattern.CASE_INSENSITIVE);
    private static Pattern REGEX_INTEGER = Pattern.compile("(^(-?[0-9]{1,})$)");
    private static Pattern REGEX_PERCENTAGE = Pattern.compile("(^(-?[0-9]{1,}%)$)");
    private static Pattern REGEX_BOOLEAN = Pattern.compile("(^(true|false)$)", Pattern.CASE_INSENSITIVE);
    private static Pattern REGEX_ACTION = Pattern.compile(":focus|:active|:hover", Pattern.CASE_INSENSITIVE);

    public static ArrayList<CSSRule> load(File css){
        ArrayList<CSSRule> cssRules = new ArrayList<>();

        try {
            StringBuilder buffer = new StringBuilder((int) css.length());
            BufferedReader br = new BufferedReader(new FileReader(css));
            String line;

            while((line = br.readLine()) != null)
                buffer.append(line);

            br.close();

            String cssClean = buffer.toString().replaceAll("(\\r|\\n)", "");
            String rules[] = cssClean.split("}");

            for(String rule : rules)
                cssRules.addAll(generateCSSRule(rule));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(cssRules);

        return cssRules;
    }

    private static ArrayList<CSSRule> generateCSSRule(String rule){
        ArrayList<CSSRule> cssRules = new ArrayList<>();
        int indexOpenBrace = rule.indexOf("{");
        String selectors[] = rule.substring(0, indexOpenBrace).split(",");
        String declarations[] = rule.substring(indexOpenBrace+1).split(";");
        HashMap<String, Object> attributes = new HashMap<>();

        for (String declaration : declarations) {
            String attribute = declaration.substring(0, declaration.indexOf(":")).trim().replace("-", "_").toUpperCase();
            Object value = getObject(declaration.substring(declaration.indexOf(":") + 1).trim());

            attributes.put(attribute, value);
        }

        for(String selector : selectors) {
            String action = null;
            selector = selector.trim();

            if(!selector.isEmpty()) {
                Matcher matcherAction = REGEX_ACTION.matcher(selector);

                if (matcherAction.find()) {
                    MatchResult resultAction = matcherAction.toMatchResult();

                    if (!matcherAction.find()) {//One action allowed
                        action = resultAction.group().toLowerCase();

                        if (selector.length() == resultAction.end())
                            selector = selector.substring(0, resultAction.start());
                        else
                            selector = null;
                    }
                }

                if (selector != null) {
                    CSSRule cssRule;

                    if (action == null)
                        cssRule = new CSSRule(selector);
                    else
                        cssRule = new CSSRule(selector, ComponentState.getComponentState(action.substring(1)));

                    cssRule.declarations = (HashMap<String, Object>) attributes.clone();
                    cssRules.add(cssRule);
                }
            }
        }

        return cssRules;
    }

    public static Object getObject(String value){
        Object object;

        if((object = getColor(value)) != null) return object;
        if((object = getBoolean(value)) != null) return object;
        if((object = getSide(value)) != null) return object;
        if((object = getPercentage(value)) != null) return object;
        if((object = getInteger(value)) != null) return object;

        return value;
    }

    public static Integer getInteger(String integer){
        Matcher matcher = REGEX_INTEGER.matcher(integer);


        if(matcher.find()){
            return Integer.parseInt(integer);
        } else
            return null;
    }

    public static Float getPercentage(String percentage){
        Matcher matcher = REGEX_PERCENTAGE.matcher(percentage);


        if(matcher.find()){
            return Float.parseFloat(percentage.substring(0, percentage.indexOf("%")))/100;
        }
        else
            return null;
    }

    public static Side getSide(String side){
        side = side.charAt(side.length()-1) != ','?side:side.substring(0,side.length()-1);
        Matcher matcher = REGEX_SIDE.matcher(side);

        if(matcher.find()){
            String values[] = side.split(",");
            boolean bool[] = new boolean[4];

            for(int i = 0; i < values.length; i++)
                bool[i] = Boolean.parseBoolean(values[i]);

            return new Side(bool[0], bool[1], bool[2], bool[3]);
        }
        else
            return null;
    }

    public static Boolean getBoolean(String bool){
        Matcher matcher = REGEX_BOOLEAN.matcher(bool);

        if(matcher.find())
            return Boolean.parseBoolean(bool);
        else
            return null;
    }

    public static Color getColor(String color){
        Matcher matcher = REGEX_COLOR.matcher(color);

        if(matcher.find()){
            String values[] = color.split(",");
            int bytes[] = new int[4];

            for(int i = 0; i < values.length; i++){
                int integer = Integer.parseInt(values[i]);

                if(0 <= integer && integer <= 255)
                    bytes[i] = integer;
                else
                    return null;
            }

            return new Color(bytes[0], bytes[1], bytes[2], bytes[3]);
        }
        else
            return null;
    }

    public static class CSSRule implements Comparable<CSSRule> {

        private final String selector;
        private final ComponentState state;
        private HashMap<String, Object> declarations;
        private int score;

        public CSSRule(String selector, ComponentState state) {
            this.selector = selector;
            this.state = state;
            this.declarations = new HashMap<>();
            initScore();
        }

        public CSSRule(String selector) {
            this.selector = selector;
            this.state = null;
            this.declarations = new HashMap<>();
            initScore();
        }

        public int getScore() {
            return score;
        }

        public Object getDeclarationValue(String declaration){
            return declarations.get(declaration.toLowerCase());
        }

        public void setDeclarationValue(String declaration, Object value){
            declarations.put(declaration.toLowerCase(), value);
        }

        public String getSelector() {
            return selector;
        }

        public ComponentState getState() {
            return state;
        }

        public HashMap<String, Object> getDeclarations() {
            return declarations;
        }

        private void initScore(){
            if(!selector.equals("*")) {
                Matcher matcher;

                matcher = REGEX_ID.matcher(selector);

                while (matcher.find())
                    score += 100;

                matcher = REGEX_CLASS.matcher(selector);

                while (matcher.find())
                    score += 10;

                if (state != null)
                    score += 10;

                matcher = REGEX_PSEUDO_CLASS_BRACKET.matcher(selector);

                while (matcher.find())
                    score += 10;

                matcher = REGEX_ATTRIBUTE.matcher(selector);

                while (matcher.find())
                    score += 10;

                matcher = REGEX_ELEMENT.matcher(selector);

                while (matcher.find())
                    score += 1;
            }
            else
                score = -1;
        }

        @Override
        public int compareTo(CSSRule cssRule) {
            return this.score - cssRule.score;
        }
    }
}
