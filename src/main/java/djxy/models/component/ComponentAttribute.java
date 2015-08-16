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

import java.awt.*;

public enum ComponentAttribute {
    FOCUS,
    POSITION,
    LOCATION_FREEZE,
    VISIBILITY,
    X_RELATIVE,
    Y_RELATIVE,
    VALUE,
    WIDTH,
    HEIGHT,
    PADDING_SIZE,
    PADDING_SIDE,
    BORDER_SIZE,
    BORDER_SIDE,
    BORDER_COLOR,
    MARGIN_LEFT,
    MARGIN_TOP,
    MARGIN_RIGHT,
    MARGIN_BOT,
    BACKGROUND,
    TEXT_ALIGNMENT,
    TEXT_COLOR,
    HINT,
    URL,
    FONT,
    FONT_SIZE,
    IMAGE_TYPE,
    IMAGE_NAME,
    MAX_TEXT_LINES,
    LIST_ORDER,
    LIST_ORIGIN;

    public static boolean attributeHasState(ComponentAttribute attribute){
        if(FOCUS == attribute || POSITION == attribute || LOCATION_FREEZE == attribute || VISIBILITY == attribute || X_RELATIVE == attribute || Y_RELATIVE == attribute || VALUE == attribute || MAX_TEXT_LINES == attribute || LIST_ORDER == attribute || LIST_ORIGIN == attribute || HINT  == attribute || URL  == attribute)
            return false;
        if(WIDTH == attribute || HEIGHT == attribute || PADDING_SIZE == attribute || PADDING_SIDE == attribute || BORDER_SIZE == attribute || BORDER_SIDE == attribute || BORDER_COLOR == attribute || MARGIN_LEFT == attribute || MARGIN_TOP == attribute || MARGIN_RIGHT == attribute || MARGIN_BOT == attribute || BACKGROUND == attribute || TEXT_ALIGNMENT == attribute || TEXT_COLOR == attribute || FONT == attribute || FONT_SIZE == attribute || IMAGE_TYPE == attribute || IMAGE_NAME == attribute)
            return true;

        return true;
    }

    public static String componentAttributeValueToString(ComponentAttribute componentAttribute, Object object){
        if(componentAttribute == POSITION){
            if(object instanceof Position)
                return ((Position) object).name();
        }
        else if(componentAttribute == BACKGROUND){
            if(object instanceof Background)
                return ((Background) object).name();
            if(object instanceof Color){
                Color color = (Color) object;

                return color.getRed()+","+color.getGreen()+","+color.getBlue()+","+color.getAlpha();
            }
        }
        else if(componentAttribute == TEXT_ALIGNMENT){
            if(object instanceof TextAlignment)
                return ((TextAlignment) object).name();
        }
        else if(componentAttribute == IMAGE_TYPE){
            if(object instanceof ImageType)
                return ((ImageType) object).name();
        }
        else if(componentAttribute == LIST_ORDER){
            if(object instanceof ListOrder)
                return ((ListOrder) object).name();
        }
        else if(componentAttribute == LIST_ORIGIN){
            if(object instanceof ListOrigin)
                return ((ListOrigin) object).name();
        }
        else if(componentAttribute == IMAGE_NAME || componentAttribute == FONT_SIZE || componentAttribute == FONT || componentAttribute == VALUE || componentAttribute == HINT || componentAttribute == URL || componentAttribute == MAX_TEXT_LINES || componentAttribute == MARGIN_BOT || componentAttribute == MARGIN_LEFT || componentAttribute == MARGIN_RIGHT || componentAttribute == MARGIN_TOP || componentAttribute == LOCATION_FREEZE || componentAttribute == FOCUS || componentAttribute == VISIBILITY || componentAttribute == LOCATION_FREEZE || componentAttribute == LOCATION_FREEZE || componentAttribute == PADDING_SIZE || componentAttribute == BORDER_SIZE){
            return object.toString();
        }
        else if(componentAttribute == X_RELATIVE || componentAttribute == Y_RELATIVE || componentAttribute == WIDTH || componentAttribute == HEIGHT){
            if(object instanceof Integer){
                Integer integer = (Integer) object;

                return integer+"";
            }
            if(object instanceof Float){
                Float f = (Float) object;

                return f*100+"%";
            }
        }
        else if(componentAttribute == PADDING_SIDE || componentAttribute == BORDER_SIDE){
            if(object instanceof Side){
                Side side = (Side) object;

                return side.isLeft()+","+side.isTop()+","+side.isRight()+","+side.isBottom();
            }
        }
        else if(componentAttribute == BORDER_COLOR || componentAttribute == TEXT_COLOR){
            if(object instanceof Color){
                Color color = (Color) object;

                return color.getRed()+","+color.getGreen()+","+color.getBlue()+","+color.getAlpha();
            }
        }

        return "";
    }

    public static ListOrigin getListOrigin(String value){
        try{
            return ListOrigin.valueOf(value.toUpperCase());
        }catch(Exception e){}
        return ListOrigin.TOP;
    }

    public static Boolean getFocus(String value){
        Boolean focus = convertStringToBoolean(value);

        if(focus != null)
            return focus;

        return false;
    }

    public static ListOrder getListOrder(String value){
        try{
            return ListOrder.valueOf(value.toUpperCase());
        }catch(Exception e){}
        return ListOrder.FIRST_ADDED_TO_LAST_ADDED;
    }

    public static int getFontSize(String value){
        Integer size =  convertStringToInteger(value);

        if(size != null)
            return size;

        return 0;
    }

    public static int getMargin(String value){
        Integer margin =  convertStringToInteger(value);

        if(margin != null)
            return margin;

        return 0;
    }

    public static ImageType getImageType(String value){
        try{
            return ImageType.valueOf(value.toUpperCase());
        }catch(Exception e){}
        return ImageType.MINECRAFT;
    }

    public static Side getPaddingSide(String value){
        return convertStringToSide(value);
    }

    public static Side getBorderSide(String value){
        return convertStringToSide(value);
    }

    public static Position getPosition(String value){
        try{
            return Position.valueOf(value.toUpperCase());
        }catch(Exception e){}
        return Position.TOP_LEFT;
    }

    public static TextAlignment getTextAlignment(String value){
        try{
            return TextAlignment.valueOf(value.toUpperCase());
        }catch(Exception e){}
        return TextAlignment.LEFT;
    }

    public static Object getWidth(String value){
        return getFloatOrInteger(value);
    }

    public static Object getHeight(String value){
        return getFloatOrInteger(value);
    }

    public static Object getXRelative(String value){
        Object relative = getFloatOrInteger(value);

        if(relative != null)
            return relative;

        return 0;
    }

    public static Object getYRelative(String value){
        Object relative = getFloatOrInteger(value);

        if(relative != null)
            return relative;

        return 0;
    }

    public static Visibility getVisibility(String value){
        try{
            return Visibility.valueOf(value.toUpperCase());
        }catch(Exception e){}
        return Visibility.VISIBLE;
    }

    public static Boolean getLocationFreeze(String value){
        Boolean freeze = convertStringToBoolean(value);

        if(freeze != null)
            return freeze;

        return true;
    }

    public static Integer getPaddingSize(String value){
        return convertStringToInteger(value);
    }

    public static Integer getBorderSize(String value){
        return convertStringToInteger(value);
    }

    public static Color getBorderColor(String value){
        return convertStringToColor(value);
    }

    public static Color getTextColor(String value){
        return convertStringToColor(value);
    }

    public static Integer getMaxTextLines(String value){
        Integer lines = convertStringToInteger(value);

        if(lines != null)
            return lines;

        return Integer.MAX_VALUE;
    }

    public static Object getBackground(String value){
        Object background = convertStringToColor(value);

        if(background != null)
            return background;

        return value;
    }

    private static Color convertStringToColor(String value){
        try{
            int r,g,b,a;
            int firstIndex = value.indexOf(',');
            int secondIndex;

            r = convertStringToInteger(value.substring(0, firstIndex));

            secondIndex = value.indexOf(',', firstIndex+1);

            g = convertStringToInteger(value.substring(firstIndex+1, secondIndex));

            firstIndex = secondIndex;

            secondIndex = value.indexOf(',', firstIndex+1);

            b = convertStringToInteger(value.substring(firstIndex+1, secondIndex));

            firstIndex = secondIndex;

            a = convertStringToInteger(value.substring(firstIndex+1, value.length()));

            return new Color(r, g, b, a);
        }catch(Exception e){}

        return null;
    }

    private static Object getFloatOrInteger(String value){
        if(value.charAt(value.length()-1) == '%')
            return (float) convertStringToInteger(value.substring(0, value.lastIndexOf('.')))/100;
        else
            return convertStringToInteger(value);
    }

    private static Integer convertStringToInteger(String value){
        try{
            return Integer.parseInt(value);
        }catch(Exception e){}

        return null;
    }

    private static Boolean convertStringToBoolean(String value){
        try{
            return Boolean.parseBoolean(value.toUpperCase());
        }catch(Exception e){}

        return null;
    }

    private static Side convertStringToSide(String value){
        try{
            boolean left,top,right,bottom;
            int firstIndex = value.indexOf(',');
            int secondIndex;

            left = Boolean.parseBoolean(value.substring(0, firstIndex));

            secondIndex = value.indexOf(',', firstIndex+1);

            top = Boolean.parseBoolean(value.substring(firstIndex+1, secondIndex));

            firstIndex = secondIndex;

            secondIndex = value.indexOf(',', firstIndex+1);

            right = Boolean.parseBoolean(value.substring(firstIndex+1, secondIndex));

            firstIndex = secondIndex;

            bottom = Boolean.parseBoolean(value.substring(firstIndex+1, value.length()));

            return new Side(left, top, right, bottom);
        }catch(Exception e){}

        return null;
    }

    public static ComponentAttribute getComponentAttribute(String value){
        try{
            return valueOf(value.toUpperCase());
        }catch (Exception e){
        }

        return null;
    }
}